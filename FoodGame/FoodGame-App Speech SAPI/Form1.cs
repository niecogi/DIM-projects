using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Text;
using System.Windows.Forms;
using System.Speech.Recognition;
using System.Speech.Synthesis ;

namespace REcoSample
{
    public partial class Form1 : Form
    {

        int estadoVaso = 0;
        int platosPedidos = 0;
        int estadoPago = 3;

        private System.Speech.Recognition.SpeechRecognitionEngine _recognizer = 
        new SpeechRecognitionEngine();
        private SpeechSynthesizer synth = new SpeechSynthesizer();
        
        public Form1()
        {
            InitializeComponent();
        }


        private void Form1_Load(object sender, EventArgs e)
        {
            synth.Speak("Bienvenido a la aplicación de comida creada por Nieves. Inicializando la Aplicación ");
            
            Grammar grammar= CreateGrammarBuilderRGBSemantics2(null);
            _recognizer.SetInputToDefaultAudioDevice();
            _recognizer.UnloadAllGrammars();
            // Nivel de confianza del reconocimiento 70%
            _recognizer.UpdateRecognizerSetting("CFGConfidenceRejectionThreshold", 70);
            grammar.Enabled = true;
            _recognizer.LoadGrammar(grammar);
            _recognizer.SpeechRecognized += new EventHandler<SpeechRecognizedEventArgs>(_recognizer_SpeechRecognized);
            //reconocimiento asíncrono y múltiples veces
            _recognizer.RecognizeAsync(RecognizeMode.Multiple);
            synth.Speak("Aplicación preparada para reconocer su voz");
        }

        private void cambiarEstadoVaso(int nuevoEstado) {
            switch (nuevoEstado) {
                case 0:
                    pictureVacio.Visible = true;
                    pictureMedio.Visible = false;
                    pictureLleno.Visible = false;
                    break;
                case 1:
                    pictureVacio.Visible = false;
                    pictureMedio.Visible = true;
                    pictureLleno.Visible = false;
                    break;
                case 2:
                    pictureVacio.Visible = false;
                    pictureMedio.Visible = false;
                    pictureLleno.Visible = true;
                    break;

            }
            estadoVaso = nuevoEstado;
        
        }

        private void cambiarEstadoPago(int nuevoEstado)
        {
            switch (nuevoEstado)
            {
                case 0:
                    pictureMoney.Visible = false;
                    pictureCcard.Visible = true;
                    break;
                case 1:
                    pictureMoney.Visible = true;
                    pictureCcard.Visible = false;
                    break;

            }
            estadoPago = nuevoEstado;

        }

        void _recognizer_SpeechRecognized(object sender, SpeechRecognizedEventArgs e)
        {
            //obtenemos un diccionario con los elementos semánticos
            SemanticValue semantics = e.Result.Semantics;

            string rawText = e.Result.Text;
            RecognitionResult result = e.Result;

            if (semantics.ContainsKey("comer") || semantics.ContainsKey("zampar"))
            {
                int comida = (int)semantics["comer"]["comida"].Value;

                switch (comida)
                {
                    case 0:
                        if (picturePasta.Visible == false) {
                            synth.Speak("No te puedes comer la pasta, no está en el plato"); }
                        else { picturePasta.Visible = false; }
                        break;
                    case 1:
                        if (pictureBurguer.Visible == false) {
                            synth.Speak("No te puedes comer la hamburguesa, no está en el plato"); }
                        else { pictureBurguer.Visible = false; }
                        break;
                    case 2:
                        if (pictureSalad.Visible == false) {
                            synth.Speak("No te puedes comer la ensalada, no está en el plato"); }
                        else { pictureSalad.Visible = false; }
                        break;

                }

            }
            else if (semantics.ContainsKey("pedir"))
            {
                int comida = (int)semantics["pedir"]["comida"].Value;
                switch (comida)
                {
                    case 0:
                        if (picturePasta.Visible == true) {
                            synth.Speak("Ya tienes un plato de pasta en la mesa, acabatelo primero"); }
                        else { picturePasta.Visible = true; platosPedidos++; }
                        break;
                    case 1:
                        if (pictureBurguer.Visible == true) {
                            synth.Speak("Ya tienes una hamburguesa en el plato, acabatelo primero"); }
                        else { pictureBurguer.Visible = true; platosPedidos++; }
                        break;
                    case 2:
                        if (pictureSalad.Visible == true) {
                            synth.Speak("Ya tienes un plato de ensalada en la mesa, acabatelo primero"); }
                        else { pictureSalad.Visible = true; platosPedidos++; }
                        break;

                }
            }

            else if (semantics.ContainsKey("beber"))
            {
                int cantidad = (int)semantics["beber"]["modo"].Value;
                int nuevoestado = estadoVaso - cantidad;
                if (nuevoestado < 0) {
                    synth.Speak("No puedes beber, no te queda agua.");
                    nuevoestado = 0;
                }
                cambiarEstadoVaso(nuevoestado);


            }

            else if (semantics.ContainsKey("rellenar"))
            {
                int cantidad = (int)semantics["rellenar"]["modo"].Value;
                int nuevoestado = estadoVaso + cantidad;
                if (nuevoestado > 2)
                {
                    synth.Speak("No puedes rellenar el vaso con más agua, tú vaso está lleno.");
                    nuevoestado = 2;
                }
                cambiarEstadoVaso(nuevoestado);


            }

            else if (semantics.ContainsKey("pagar"))
            {
                int modoPago = (int)semantics["pagar"]["modo"].Value;
                if (platosPedidos != 0)
                {
                    cambiarEstadoPago(modoPago);
                }
                else { synth.Speak("No puedes pagar, no has pedido ningún plato."); }

            } else if (semantics.ContainsKey("cerrar")) {
                Application.ExitThread();
            }
            
        }
        
      
        private Grammar CreateGrammarBuilderRGBSemantics2(params int[] info)
        {
            //Frase 1: comer
            GrammarBuilder comer = "Comer";
            GrammarBuilder zampar = "Zampar";
            GrammarBuilder engullir = "Engullir";
            Choices alternativas_comer = new Choices(comer, zampar, engullir);

            GrammarBuilder comerPasta = new GrammarBuilder(new SemanticResultValue("Pasta", 0));
            GrammarBuilder comerHamburguesa = new GrammarBuilder(new SemanticResultValue("Hamburguesa", 1));
            GrammarBuilder comerEnsalada = new GrammarBuilder(new SemanticResultValue("Ensalada", 2));
            Choices foodComerChoice = new Choices(comerPasta,comerHamburguesa,comerEnsalada);
            SemanticResultKey foodComerResultKey = new SemanticResultKey("comida",foodComerChoice);
            GrammarBuilder accionComer = new GrammarBuilder();
            accionComer.Append(alternativas_comer);
            accionComer.Append(foodComerResultKey);     
            SemanticResultKey comerResultKey = new SemanticResultKey("comer",accionComer);

            //Frase 2: pedir
            GrammarBuilder pedir = "Pedir";
            GrammarBuilder solicitar = "Solicitar";
            Choices alternativas_pedir = new Choices(pedir, solicitar);
            GrammarBuilder pedirPasta = new GrammarBuilder(new SemanticResultValue("Pasta", 0));
            GrammarBuilder pedirHamburguesa = new GrammarBuilder(new SemanticResultValue("Hamburguesa", 1));
            GrammarBuilder pedirEnsalada = new GrammarBuilder(new SemanticResultValue("Ensalada", 2));
            Choices foodPedirChoice = new Choices(pedirPasta, pedirHamburguesa, pedirEnsalada);
            SemanticResultKey foodPedirResultKey = new SemanticResultKey("comida", foodPedirChoice);
            GrammarBuilder accionPedir = new GrammarBuilder();
            accionPedir.Append(alternativas_pedir);
            accionPedir.Append(foodPedirResultKey);
            SemanticResultKey pedirResultKey = new SemanticResultKey("pedir", accionPedir);

            GrammarBuilder un = new GrammarBuilder("un", 0, 1);
            //Frase 3: beber
            GrammarBuilder beber = "Beber";
            GrammarBuilder beberPoco = new GrammarBuilder(new SemanticResultValue("Poco", 1));
            GrammarBuilder beberMucho = new GrammarBuilder(new SemanticResultValue("Mucho", 2));
            Choices modoBeberChoice = new Choices(beberPoco, beberMucho);
            SemanticResultKey beberModoResultKey = new SemanticResultKey("modo", modoBeberChoice);
            GrammarBuilder accionBeber = new GrammarBuilder();
            accionBeber.Append(beber);
            accionBeber.Append(un);
            accionBeber.Append(beberModoResultKey);

            SemanticResultKey beberResultKey = new SemanticResultKey("beber", accionBeber);


            //Frase 4: rellenar
            GrammarBuilder rellenar = "Rellenar";
            GrammarBuilder rellenarPoco = new GrammarBuilder(new SemanticResultValue("Poco", 1));
            GrammarBuilder rellenarMucho = new GrammarBuilder(new SemanticResultValue("Mucho", 2));
            Choices modoRellenarChoice = new Choices(rellenarPoco, rellenarMucho);
            SemanticResultKey rellenarModoResultKey = new SemanticResultKey("modo", modoRellenarChoice);
            GrammarBuilder accionRellenar = new GrammarBuilder();
            accionRellenar.Append(rellenar);
            accionBeber.Append(un);
            accionRellenar.Append(rellenarModoResultKey);

            SemanticResultKey rellenarResultKey = new SemanticResultKey("rellenar", accionRellenar);

            GrammarBuilder con = new GrammarBuilder("con", 0, 1);
            //Frase 5: pagar
            GrammarBuilder pagar = "Pagar";
            GrammarBuilder efectivo = "Efectivo";
            GrammarBuilder metalico = "Metalico";
            Choices alternativas_efectivo = new Choices(efectivo,metalico);

            GrammarBuilder pagarTarjeta = new GrammarBuilder(new SemanticResultValue("Tarjeta", 0));
            GrammarBuilder pagarEfectivo = new GrammarBuilder(new SemanticResultValue(alternativas_efectivo, 1));
            Choices modoPagarChoice = new Choices(pagarTarjeta, pagarEfectivo);
            SemanticResultKey pagarModoResultKey = new SemanticResultKey("modo", modoPagarChoice);
            GrammarBuilder accionPagar = new GrammarBuilder();
            accionPagar.Append(pagar);
            accionPagar.Append(con);
            accionPagar.Append(pagarModoResultKey);

            SemanticResultKey pagarResultKey = new SemanticResultKey("pagar", accionPagar);

            //Frase 6: Cerrar
            GrammarBuilder cerrar = "Cerrar";
            GrammarBuilder salir = "Salir";
            Choices dos_alternativas = new Choices(cerrar, salir);
            GrammarBuilder aplicacion = "Aplicacion";
            GrammarBuilder de = new GrammarBuilder("de", 0, 1);
            GrammarBuilder la = new GrammarBuilder("la", 0, 1);
            GrammarBuilder accionCerrar = new GrammarBuilder();
            accionCerrar.Append(dos_alternativas);
            accionCerrar.Append(de);
            accionCerrar.Append(la);
            accionCerrar.Append(aplicacion);
            SemanticResultKey cerrarResultKey = new SemanticResultKey("cerrar", accionCerrar);

            //Gramática
            Choices acciones = new Choices(comerResultKey,pedirResultKey, beberResultKey, rellenarResultKey, pagarResultKey, cerrarResultKey);
            Grammar grammar = new Grammar(acciones);

            return grammar;       
        }       
    }
}