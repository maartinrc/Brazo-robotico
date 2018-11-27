  //Programa de hagarre de brazo
  #include<Servo.h>
  #include <LiquidCrystal_I2C.h>  // Librería para controlar el LCD con el dispositivo I2C
  //Crear el objeto lcd  dirección  0x3F y 16 columnas x 2 filas
  LiquidCrystal_I2C lcd(0x3F, 16, 2); 
  #define ledVerde 5
  #define ledRojo 7
  #define ledNaranja 4
  #define interrupcion 2
  #include <EEPROM.h>//se incluye la libreria para el manejo de la memoria eeprom

  int dato=0;
  
  int direccion = 0;//esta variable actuará como índice al momento de escribir/leer los datos de la eeprom 
  Servo  servos [4];//griper,doll,elbow,shoulder
  int pines []={3,6,9,10};//Arreglo que contiene los pines para los servo motores
  int contID;//Contador para datos que llegan del puerto serial
  int positions[5]; //arreglo de posiciones que llegan por el puerto serial
  boolean abortar=false;
  boolean emer=false;
  boolean take=false;
  boolean registro=false;
  int dato_rx;//variable donde se almacenara el valor del motor a pasos
  int numero_pasos = 0; //numero de pasos dados
  void setup(){ 
     //Se instancia los servos utilizados
      for(int index=0; index<4;index++){
        servos[index].attach(pines[index]);
      }
      contID= 0;
      pinMode(ledVerde,OUTPUT);
      pinMode(ledRojo,OUTPUT);
      pinMode(ledNaranja,OUTPUT);
      pinMode(interrupcion,INPUT);
      pinMode(13, OUTPUT);    // Pin 11 conectar a IN4
      pinMode(12, OUTPUT);    // Pin 10 conectar a IN3
      pinMode(11, OUTPUT);     // Pin 9 conectar a IN2
      pinMode(8, OUTPUT);     // Pin 8 conectar a IN1
      // Inicializar el LCD
        lcd.init();

      //Encender la luz de fondo.
        lcd.backlight();
        lcd.setCursor(0,0);
        lcd.print("Esperando instru");
        lcd.setCursor(0,1);
        lcd.print("cciones...");
        cargarEEPROM();
        attachInterrupt(digitalPinToInterrupt(interrupcion), interrup,RISING);
        Serial.begin(9600);
   }
 
   void loop(){
        digitalWrite(ledRojo,LOW);
        digitalWrite(ledVerde,HIGH);
        digitalWrite(ledNaranja,LOW);
        
        if (Serial.available())  //Verificar si te tiene informacion pendiente de leer en el bus Serial
        {
          delay(100);//retardo para permitir que informacion termine de recibirse en el bus Serial
          while (Serial.available() > 0) 
              {
                  String caracter = Serial.readStringUntil(',');
                  int data = caracter.toInt();

                  positions[contID]=data;//Almacena valor de pinza 
                  contID++;
                 
                  if(data==222){//identificador para rutina(cadena extensa de posiciones)
                    registro=true;
                    contID=0;
                  }
                 
                  if(registro==true){
                    if(contID==5){
                    digitalWrite(ledVerde,LOW);
                    digitalWrite(ledNaranja,HIGH);
                    digitalWrite(ledRojo,LOW);
                    lcd.clear();
                    lcd.setCursor(0,0);
                    lcd.print("Ejecutandose..");
                    guardaEEPROM();
                    doIt();//Metodo de ejecucion de pasos
                    delay(1000);
                    }
                  }
           
                  if(data==666){//identificador para ejecutar los movimientos cargados
                    abortar=true;
                  }

                  if(data==111){//identificador para hacer una rutina de pasos
                    take=true;
                    
                  }
              }
              registro=false;
        }
        //Si se proporciono el codigo se ejecutaran los pasos cargados
        if(abortar==true){
          digitalWrite(ledVerde,LOW);
          digitalWrite(ledNaranja,HIGH);
          digitalWrite(ledRojo,LOW);
          lcd.clear();
          lcd.setCursor(0,0);
          lcd.print("Ejecutandose..");
          guardaEEPROM();
          doIt();//Metodo de ejecucion de pasos
          
        }

        if(take==true){//Metodo precargado para tomar objeto
          digitalWrite(ledVerde,LOW);
          digitalWrite(ledNaranja,HIGH);
          digitalWrite(ledRojo,LOW);
          lcd.clear();
          lcd.setCursor(0,0);
          lcd.print("Ejecutandose..");
          toma();
          abortar=false;              
          contID=0;
          lcd.clear();
          lcd.setCursor(0,0);
          lcd.print("Esperando instru");
          lcd.setCursor(0,1);
          lcd.print("cciones...");
        }

        //Si hay una emergencia se ejecutan movimientos predeterminados
        while(emer==true){
        lcd.clear();
        lcd.setCursor(0,0);
        lcd.print("Emergencia!!");
        digitalWrite(ledRojo,HIGH);
        digitalWrite(ledVerde,LOW);
        digitalWrite(ledNaranja,LOW);
        emergencia();
        abortar=false;              
          contID=0;
          lcd.clear();
          lcd.setCursor(0,0);
          lcd.print("Esperando instru");
          lcd.setCursor(0,1);
          lcd.print("cciones...");
        }
      
   }

   void doIt(){
    Serial.print("Posiciones: ");
    for(int index=0;index<5;index++){
          Serial.print(positions[index]);
          Serial.print(", ");
    }
    Serial.println();
        servos[3].write(positions[0]);
        delay(800);
        
     dato_rx=positions[1];
     dato_rx*=1.42222222222;// Ajuste de 512 vueltas a los 360 grados
     while (dato_rx>numero_pasos){   // Girohacia la izquierda en grados
         paso_izq();
        numero_pasos = numero_pasos + 1;
     }
     while (dato_rx<numero_pasos){   // Giro hacia la derecha en grados
          paso_der();
          numero_pasos = numero_pasos -1;
    }  
      apagado();         // Apagado del Motor para que no se caliente
    /*++++++++++++++++++++++*/
        // Se ejecutan cada una de las posiciones con su respectivo servo
        servos[2].write(positions[2]);
        delay(800);
        servos[1].write(positions[3]);
        delay(800);
        servos[0].write(positions[4]);
        delay(800);
   
        abortar=false;              
        contID=0;
        lcd.clear();
        lcd.setCursor(0,0);
        lcd.print("Esperando instru");
        lcd.setCursor(0,1);
        lcd.print("cciones...");
        delay(1000);
        
   }
  //Interrupcion
   void interrup(){
    emer=!emer;
    emergencia();
   }
    //Movimientos definidos de emergencia
   void emergencia(){
        servos[0].write(0);
        delay(100);
        servos[1].write(90);
        delay(100);
        servos[2].write(90);
        delay(100);
        servos[3].write(130);
        delay(100);    
   }
   
    void paso_der(){         // Pasos a la derecha motor a pasos
      digitalWrite(13, LOW); 
      digitalWrite(12, LOW);  
      digitalWrite(11, HIGH);  
      digitalWrite(8, HIGH);  
      delay(5); 
      digitalWrite(13, LOW); 
      digitalWrite(12, HIGH);  
      digitalWrite(11, HIGH);  
      digitalWrite(8, LOW);  
      delay(5); 
      digitalWrite(13, HIGH); 
      digitalWrite(12, HIGH);  
      digitalWrite(11, LOW);  
      digitalWrite(8, LOW);  
      delay(5); 
      digitalWrite(13, HIGH); 
      digitalWrite(12, LOW);  
      digitalWrite(11, LOW);  
      digitalWrite(8, HIGH);  
      delay(5);  
    }

    void paso_izq() {        // Pasos a la izquierda
      digitalWrite(13, HIGH); 
      digitalWrite(12, HIGH);  
      digitalWrite(11, LOW);  
      digitalWrite(8, LOW);  
      delay(5); 
      digitalWrite(13, LOW); 
      digitalWrite(12, HIGH);  
      digitalWrite(11, HIGH);  
      digitalWrite(8, LOW);  
      delay(5); 
      digitalWrite(13, LOW); 
      digitalWrite(12, LOW);  
      digitalWrite(11, HIGH);  
      digitalWrite(8, HIGH);  
      delay(5); 
      digitalWrite(13, HIGH); 
      digitalWrite(12, LOW);  
      digitalWrite(11, LOW);  
      digitalWrite(8, HIGH);  
      delay(5); 
    }
          
    void apagado() {         // Apagado del Motor
      digitalWrite(13, LOW); 
      digitalWrite(12, LOW);  
      digitalWrite(11, LOW);  
      digitalWrite(8, LOW);  
    }
    
    void toma(){
      servos[3].write(90);
      delay(800);
      servos[2].write(90);
      delay(800);
      servos[1].write(90);
      delay(800);
      servos[0].write(90);
      delay(800);
      
      servos[3].write(138);
      delay(800);
      servos[2].write(60);
      delay(800);
      servos[1].write(90);
      delay(800);
      servos[0].write(50);
      delay(800);
    
      
      servos[3].write(90);
      delay(800);
      servos[2].write(90);
      delay(800);
      servos[1].write(90);
      delay(800);
      servos[0].write(50);
      delay(800);
      
      dato_rx=180;
      dato_rx*=1.42222222222;// Ajuste de 512 vueltas a los 360 grados
      while (dato_rx>numero_pasos){   // Girohacia la izquierda en grados
          paso_izq();
          numero_pasos = numero_pasos + 1;
      }
      while (dato_rx<numero_pasos){   // Giro hacia la derecha en grados
          paso_der();
          numero_pasos = numero_pasos -1;
      }
    apagado();         // Apagado del Motor para que no se caliente
//gira180
  
      servos[3].write(138);
      delay(800);
      servos[2].write(60);
      delay(800);
      servos[1].write(90);
      delay(800);
      servos[0].write(180);
      delay(800);
    
    
      servos[3].write(90);
      delay(800);
      servos[2].write(90);
      delay(800);
      servos[1].write(90);
      delay(800);
      servos[0].write(90);
      delay(800);
      
      dato_rx=0;
      dato_rx*=1.42222222222;// Ajuste de 512 vueltas a los 360 grados
      while (dato_rx>numero_pasos){   // Girohacia la izquierda en grados
          paso_izq();
          numero_pasos = numero_pasos + 1;
      }
      while (dato_rx<numero_pasos){   // Giro hacia la derecha en grados
          paso_der();
          numero_pasos = numero_pasos -1;
      }
      apagado();         // Apagado del Motor para que no se caliente
      //gira 0
      take=false;
    }


    void guardaEEPROM(){ 
//    for(int i=0; i <= sizeof(positions);i++){ //guardar en la memoria EEPROM las posiciones del brazo robótico
//     // EEPROM.put(direccion,positions[i]);
//     EEPROM.update(direccion,positions[i]); // a diferencia de put, update() comprueba antes de escribir el valor existente en la memoria, y escribe únicamente si el valor es diferente del almacenado. 
//     direccion++;
//    }
//    direccion=0;//reiniciamos índice
    }
  
   void cargarEEPROM(){ 
//  
//   direccion = sizeof(positions);
//    for(int i = direccion-1 ;i >= 0 ;i--){
//       EEPROM.get(direccion,dato);
//      positions[i]=dato;
//      Serial.print("dato: ");
//      Serial.println(dato);    }
//    
   
  }
  
