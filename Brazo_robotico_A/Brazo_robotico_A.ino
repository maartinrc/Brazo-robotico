  //Programa de hagarre de brazo
  #include<Stepper.h>
  #include<Servo.h>
<<<<<<< HEAD
  #include <LiquidCrystal_I2C.h>  // Librería para controlar el LCD con el dispositivo I2C
  //Crear el objeto lcd  dirección  0x3F y 16 columnas x 2 filas
  LiquidCrystal_I2C lcd(0x3F, 16, 2); 
  #define ledVerde 1
  #define ledRojo 6
  #define ledNaranja 4
  #define interrupcion 2
  #define PASOS 200

  Stepper base(PASOS,8,11,12,13);
=======
  #define ledVerde A2
  #define ledRojo A0
  #define ledNaranja A1
  #define interrupcion 2
>>>>>>> 9a677f37b3e353f970e26d84ffb4baa23cf6160b
  
  Servo  servos [4];//griper,doll,elbow,shoulder
  int pines []={3,5,9,10};
  char caracter;
  int contID;
  int positions[5];
  boolean abortar=false;
  boolean emer=false;
  void setup(){ 
      for(int index=0; index<4;index++){
        servos[index].attach(pines[index]);
        servos[index].write(90);     
      }
      base.step(120);
      contID= 0;
      pinMode(ledVerde,OUTPUT);
      pinMode(ledRojo,OUTPUT);
      pinMode(ledNaranja,OUTPUT);
      pinMode(interrupcion,INPUT);
<<<<<<< HEAD
      //inicalizar velocidad de base
      base.setSpeed(100);
      // Inicializar el LCD
        lcd.init();

      //Encender la luz de fondo.
        lcd.backlight();
        lcd.setCursor(0,0);
        lcd.print("Esperando instru");
        lcd.setCursor(0,1);
        lcd.print("cciones...");
        
=======
>>>>>>> 9a677f37b3e353f970e26d84ffb4baa23cf6160b
      attachInterrupt(digitalPinToInterrupt(interrupcion), interrup,RISING);
      Serial.begin(9600);
   }
 
   void loop(){
      
        digitalWrite(ledRojo,LOW);
        digitalWrite(ledVerde,HIGH);
        digitalWrite(ledNaranja,LOW);
        
        if (Serial.available())  //Verificar si te tiene informacion pendiente de leer en el bus Serial
        {
<<<<<<< HEAD
//          delay(100);//retardo para permitir que informacion termine de recibirse en el bus Serial
=======
          delay(100);//retardo para permitir que informacion termine de recibirse en el bus Serial
>>>>>>> 9a677f37b3e353f970e26d84ffb4baa23cf6160b
          while (Serial.available() > 0) 
              {
                  String caracter = Serial.readStringUntil(',');
                  int data = caracter.toInt();
                  
                  positions[contID]=data;//Almacena valor de pinza 
                  Serial.print(positions[contID]);
                  Serial.println();
                  contID++;
                  
                  if(data==666){
                    abortar=true;
                  }
<<<<<<< HEAD
=======
//                  if(caracter=='*'){
//                   abortar=true;
//                  
//                 }else{
////                  switch(contID){
////                  case 0: positions[contID]+=caracter;//Almacena valor de pinza 
////                          break; 
////                  case 1: positions[contID]+=caracter;//Almacena valor de muñeca
////                          break; 
////                  case 2: positions[contID]+=caracter;//Almacena valor de codo
////                          break; 
////                  case 3: positions[contID]+=caracter;//Almacena valor de hombro
////                          break; 
////                  case 4: positions[contID]+=caracter;//Almacena valor de base
////                          break; 
////                 }
//                 delay(25);
//                }
>>>>>>> 9a677f37b3e353f970e26d84ffb4baa23cf6160b
              }
        }
        
        if(abortar==true){
          digitalWrite(ledVerde,LOW);
          digitalWrite(ledNaranja,HIGH);
          digitalWrite(ledRojo,LOW);
<<<<<<< HEAD
          lcd.clear();
          lcd.setCursor(0,0);
          lcd.print("Ejecutandose..");
          doIt();
          
        }

        while(emer==true){
        lcd.clear();
        lcd.setCursor(0,0);
        lcd.print("Emergencia");
        emergencia();
        }
=======
          doIt();
          
        }

        while(emer==true){
          emergencia();
        }
>>>>>>> 9a677f37b3e353f970e26d84ffb4baa23cf6160b
      
   }

   void doIt(){
        servos[0].write(positions[0]);
<<<<<<< HEAD
        delay(800);
        servos[1].write(positions[1]);
        delay(800);
        servos[2].write(positions[2]);
        delay(800);
        servos[3].write(positions[3]);
        delay(800);
        base.step(positions[4]);
        delay(800);
=======
        delay(100);
        servos[1].write(positions[1]);
        delay(100);
        servos[2].write(positions[2]);
        delay(100);
        servos[3].write(positions[3]);
        delay(100);
>>>>>>> 9a677f37b3e353f970e26d84ffb4baa23cf6160b
        abortar=false;              
        contID=0;
        lcd.clear();
        lcd.setCursor(0,0);
        lcd.print("Esperando instru");
        lcd.setCursor(0,1);
        lcd.print("cciones...");
   }
  
   void interrup(){
    emer=!emer;
   }

   void emergencia(){
        digitalWrite(ledRojo,HIGH);
        digitalWrite(ledVerde,LOW);
        digitalWrite(ledNaranja,LOW);
        servos[0].write(90);
        delay(100);
        servos[1].write(90);
        delay(100);
        servos[2].write(90);
        delay(100);
        servos[3].write(180);
        delay(100);    
   }
<<<<<<< HEAD
=======
  
   void interrup(){
    emer=!emer;
   }

   void emergencia(){
        digitalWrite(ledRojo,HIGH);
        digitalWrite(ledVerde,LOW);
        digitalWrite(ledNaranja,LOW);
        servos[0].write(90);
        delay(100);
        servos[1].write(90);
        delay(100);
        servos[2].write(90);
        delay(100);
        servos[3].write(180);
        delay(100);    
   }
>>>>>>> 9a677f37b3e353f970e26d84ffb4baa23cf6160b
