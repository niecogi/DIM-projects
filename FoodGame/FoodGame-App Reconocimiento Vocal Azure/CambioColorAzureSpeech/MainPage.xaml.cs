using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Core;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;
using Windows.UI.Popups;
using Microsoft.CognitiveServices.Speech;
using Microsoft.CognitiveServices.Speech.Intent;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;
using System.Drawing;

// La plantilla de elemento Página en blanco está documentada en https://go.microsoft.com/fwlink/?LinkId=402352&clcid=0xc0a

namespace CambioColorAzureSpeech
{
    /// <summary>
    /// Página vacía que se puede usar de forma independiente o a la que se puede navegar dentro de un objeto Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        int estadoVaso = 0;
        int platosPedidos = 0;
        private IntentRecognizer recognizer;

        public MainPage()
        {
            this.InitializeComponent();
            EnableMicrophone();
            ConfigureIntentRecognizer();
        }

        public async void MyMessageBox(string mytext)
        {
            var dialog = new MessageDialog(mytext);
            await dialog.ShowAsync();
        }

        private async void EnableMicrophone()
        {
            try
            {
                var mediaCapture = new Windows.Media.Capture.MediaCapture();
                var settings = new Windows.Media.Capture.MediaCaptureInitializationSettings();
                settings.StreamingCaptureMode = Windows.Media.Capture.StreamingCaptureMode.Audio;
                await mediaCapture.InitializeAsync(settings);
            }
            catch (Exception)
            {
                MyMessageBox("No se pudo inicializar el micrófono");
                
            }

        }
        private void cambiarEstadoVaso(int nuevoEstado)
        {
            switch (nuevoEstado)
            {
                case 0:
                    pictureVacio.Visibility = Visibility.Visible;
                    pictureMedio.Visibility = Visibility.Collapsed;
                    pictureLleno.Visibility = Visibility.Collapsed;
    
                    break;
                case 1:
                    pictureVacio.Visibility = Visibility.Collapsed;
                    pictureMedio.Visibility = Visibility.Visible;
                    pictureLleno.Visibility = Visibility.Collapsed;
                    rules.Text = "";
                    break;
                case 2:
                    pictureVacio.Visibility = Visibility.Collapsed;
                    pictureMedio.Visibility = Visibility.Collapsed;
                    pictureLleno.Visibility = Visibility.Visible;
                    break;

            }
            estadoVaso = nuevoEstado;

        }

        private async void ConfigureIntentRecognizer()
        {
            var config = SpeechConfig.FromSubscription("91e3f1782b43422d855aeff5ba6dff44", "westeurope");
            config.SpeechRecognitionLanguage = "es-es";
            var stopRecognition = new TaskCompletionSource<int>();
            using (recognizer = new IntentRecognizer(config))
            {
                var model = LanguageUnderstandingModel.FromAppId("9c4567bc-3ed8-4cab-af30-ad3d28578746");
                recognizer.AddAllIntents(model,"ComerComida");

                recognizer.Recognized += async (s, e) => {
                    await recognizedText.Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                    {
                        recognizedText.Text = e.Result.Text;
                    });
    
                    if (e.Result.Reason == ResultReason.RecognizedIntent)
                    {
                        
                        var jsonResponse = JObject.Parse(e.Result.Properties.GetProperty(PropertyId.LanguageUnderstandingServiceResponse_JsonResult));
                        await textJson.Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                        {
                            textJson.Text = jsonResponse.ToString();
                        });
                        var intent = jsonResponse.SelectToken("topScoringIntent.intent").ToString();

                        if (intent.Equals("ComerComida"))
                        {
                            try
                            {
                                string comida = jsonResponse.SelectToken("$..entities[?(@.type=='Comida')].entity").ToString();
                                await Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                                {
                                    if (comida.ToLower() == "hamburguesa")
                                    {
                                        if (pictureBurger.Visibility == Visibility.Collapsed)
                                        {
                                            rules.Text = "No te puedes comer la hamburguesa, no está en el plato";
                                        }
                                        else { pictureBurger.Visibility = Visibility.Collapsed; }
                                       
                                    }
                                   
                                    else if (comida.ToLower() == "ensalada")
                                    {
                                        if (pictureSalad.Visibility == Visibility.Collapsed)
                                        {
                                            rules.Text = "No te puedes comer la ensalada, no está en el plato";
                                        }
                                        else
                                        {
                                            pictureSalad.Visibility = Visibility.Collapsed;
                                        }
                                    }
                                    else if (comida.ToLower() == "pasta")
                                    {
                                        if (picturePasta.Visibility == Visibility.Collapsed)
                                        {
                                            rules.Text = "No te puedes comer la pasta, no está en el plato";
                                        }
                                        else
                                        {
                                            picturePasta.Visibility = Visibility.Collapsed;
                                        }
                                    }
                                });
                                
                            }

                            catch (NullReferenceException except) {
                                
                            }

                        }
               
                        if (intent.Equals("PedirComida"))
                        {
                            try
                            {
                                string comida = jsonResponse.SelectToken("$..entities[?(@.type=='Comida')].entity").ToString();
                                await Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                                {
                                    
                                    if (comida.ToLower() == "hamburguesa")
                                    {
                                        if (pictureBurger.Visibility == Visibility.Visible)
                                        {
                                            rules.Text = "Ya tienes una hamburguesa en la mesa, acabatela primero";
                                        }
                                        else
                                        {
                                            pictureBurger.Visibility = Visibility.Visible;
                                            rules.Text = "";
                                            platosPedidos++;
                                        }
                                    }
                                    else if (comida.ToLower() == "ensalada")
                                    {
                                        if (pictureSalad.Visibility == Visibility.Visible)
                                        {
                                            rules.Text = "Ya tienes un plato de ensalada en la mesa, acabatelo primero";
                                        }
                                        else
                                        {
                                            pictureSalad.Visibility = Visibility.Visible;
                                            rules.Text = "";
                                            platosPedidos++;
                                        }
                                    }
                                    else if (comida.ToLower() == "pasta")
                                    {
                                        if (picturePasta.Visibility == Visibility.Visible)
                                        {
                                            rules.Text = "Ya tienes un plato de pasta en la mesa, acabatelo primero";
                                        }
                                        else
                                        {
                                            picturePasta.Visibility = Visibility.Visible;
                                            rules.Text = "";
                                            platosPedidos++;
                                        }
                                    }
                                });

                            }

                            catch (NullReferenceException except) {
                                
                            }

                        }
                        if (intent.Equals("BeberCantidad"))
                        {
                            try
                            {
                                string cantidadS = jsonResponse.SelectToken("$..entities[?(@.type=='Cantidad')].entity").ToString();
                                int cantidad = 0;
                                if (cantidadS.ToLower() == "poco" || cantidadS.ToLower() == "trago") {
                                    cantidad = 1;
                                } else if (cantidadS.ToLower() == "mucho") {
                                    cantidad = 2;
                                }
                               
                                    int nuevoestado = estadoVaso - cantidad;
                                
                                if (nuevoestado < 0)
                                {
                                    nuevoestado = 0;
                                    await Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                                    {
                                        rules.Text = "No puedes beber, el vaso está vacio";
                                    });
                                    

                                }
                               
                                await Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                                {
                                    cambiarEstadoVaso(nuevoestado);
                                });

                            }

                            catch (NullReferenceException except) {
                                
                            }

                        }
                        if (intent.Equals("RellenarCantidad"))
                        {
                            try
                            {
                                string cantidadS = jsonResponse.SelectToken("$..entities[?(@.type=='Cantidad')].entity").ToString();
                                int cantidad = 0;
                                if (cantidadS.ToLower() == "poco" )
                                {
                                    cantidad = 1;
                                }
                                else if (cantidadS.ToLower() == "mucho")
                                {
                                    cantidad = 2;
                                }
                                
                                int nuevoestado = estadoVaso + cantidad;
                               
                                if (nuevoestado > 2)
                                {
                                    await Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                                    {
                                        rules.Text = "El vaso está lleno, no puedes rellenar más";
                                    });
                                    
                                       
                                    nuevoestado = 2;
                                }

                                await Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                                {
                                    cambiarEstadoVaso(nuevoestado);
                                });

                            }

                            catch (NullReferenceException except) {
                            
                            }

                        }
                        if (intent.Equals("PagarModo"))
                        {
                            try
                            {
                                string modoPago = jsonResponse.SelectToken("$..entities[?(@.type=='Modo')].entity").ToString();
                              
                                
                                await Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>                       
                                {
                                    if (platosPedidos == 0)
                                    {
                                        rules.Text = "No puedes pagar, no has pedido ningún plato.";
                                    }
                                    else { 
       
                                    if (modoPago.ToLower() == "metalico" || modoPago.ToLower() == "efectivo")
                                    {
                                        pictureMoney.Visibility = Visibility.Visible;
                                            rules.Text = "";
                                        }
                                    else if (modoPago.ToLower() == "tarjeta")
                                    {
                                        pictureTarjeta.Visibility = Visibility.Visible;
                                        rules.Text = "";
                                        }
                                    }

                                });

                            }

                            catch (NullReferenceException except) {
                             
                            }

                        }


                    }
                    else if (e.Result.Reason == ResultReason.RecognizedSpeech)
                    {
                        await recognizedText.Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                        {
                            recognizedText.Text = e.Result.Text;
                        });
                    }
                    else if (e.Result.Reason == ResultReason.NoMatch)
                    {
                        await recognizedText.Dispatcher.RunAsync(CoreDispatcherPriority.Normal, () =>
                        {
                            recognizedText.Text = "Speech could not be recognized.";
                        }); 
                    }
                };

                recognizer.Canceled += (s, e) => {
                    Console.WriteLine($"CANCELED: Reason={e.Reason}");

                    if (e.Reason == CancellationReason.Error)
                    {
                        Console.WriteLine($"CANCELED: ErrorCode={e.ErrorCode}");
                        Console.WriteLine($"CANCELED: ErrorDetails={e.ErrorDetails}");
                        Console.WriteLine($"CANCELED: Did you update the subscription info?");
                    }                    
                };

                recognizer.SessionStopped += (s, e) => {
                    Console.WriteLine("\n    Session stopped event.");
                    Console.WriteLine("\nStop recognition.");
                    stopRecognition.TrySetResult(0);
                };

                Console.WriteLine("Say something...");
                await recognizer.StartContinuousRecognitionAsync().ConfigureAwait(false);

                // Waits for completion.
                // Use Task.WaitAny to keep the task rooted.
                Task.WaitAny(new[] { stopRecognition.Task });

                // Stops recognition.
                await recognizer.StopContinuousRecognitionAsync().ConfigureAwait(false);
            }

        }

        private async Task Start_Button_Click(object sender, RoutedEventArgs e)
        {
            await recognizer.StartContinuousRecognitionAsync().ConfigureAwait(false);
        }

        private async Task Stop_Button_Click(object sender, RoutedEventArgs e)
        {
            await recognizer.StopContinuousRecognitionAsync().ConfigureAwait(false);
        }


    }
}
