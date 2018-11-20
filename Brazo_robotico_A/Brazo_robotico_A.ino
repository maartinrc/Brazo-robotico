  //Programa de hagarre de brazo
  #include<Servo.h>
  #define ledVerde A2
  #define ledRojo A0
  #define ledNaranja A1
  #define interrupcion 2
  
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
      contID= 0;
      pinMode(ledVerde,OUTPUT);
      pinMode(ledRojo,OUTPUT);
      pinMode(ledNaranja,OUTPUT);
      pinMode(interrupcion,INPUT);
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
                  Serial.print(positions[contID]);
                  Serial.println();
                  contID++;
                  
                  if(data==666){
                    abortar=true;
                  }
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
              }
        }
        
        if(abortar==true){
          digitalWrite(ledVerde,LOW);
          digitalWrite(ledNaranja,HIGH);
          digitalWrite(ledRojo,LOW);
          doIt();
          
        }

        while(emer==true){
          emergencia();
        }
      
   }

   void doIt(){
        servos[0].write(positions[0]);
        delay(100);
        servos[1].write(positions[1]);
        delay(100);
        servos[2].write(positions[2]);
        delay(100);
        servos[3].write(positions[3]);
        delay(100);
        abortar=false;              
        contID=0;
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
