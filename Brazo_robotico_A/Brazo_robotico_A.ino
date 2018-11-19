  //Programa de hagarre de brazo
  #include<Servo.h>

  Servo  servos [4];//griper,doll,elbow,shoulder
  int pines []={3,5,9,10};
  char caracter;
  int contID;
  int positions[5];
  int contPositions;
  boolean isPositions=false;
  void setup(){ 
      for(int index=0; index<4;index++){
        servos[index].attach(pines[index]);
        servos[index].write(90);     
      }
      contID= 0;
      contPositions=0;
      Serial.begin(9600);
   }
 
   void loop(){
      if (Serial.available())  //Verificar si te tiene informacion pendiente de leer en el bus Serial
        {
          while (Serial.available() > 0) 
              {
                  caracter = Serial.read(); //Leer 1 carácter
        
                  if(caracter==','){
                    contID++;  
                  }
                 switch(contID){
                  case 0: positions[contPositions]+=caracter;//Almacena valor de pinza 
                          break; 
                  case 1: positions[contPositions]+=caracter;//Almacena valor de muñeca
                          break; 
                  case 2: positions[contPositions]+=caracter;//Almacena valor de codo
                          break; 
                  case 3: positions[contPositions]+=caracter;//Almacena valor de hombro
                          break; 
                  case 4: positions[contPositions]+=caracter;//Almacena valor de base
                          break; 
                 }
              }
              isPositions=true;
        }


        if(isPositions==true){
          doIt();
        }
   }

   void doIt(){
        servos[0].write(positions[0]);
        delay(50);
        servos[1].write(positions[1]);
        delay(50);
        servos[2].write(positions[2]);
        delay(50);
        servos[3].write(positions[3]);
        delay(50);
        isPositions=false;
        contID=0;
   }
   
//   void positions(int start,int pos){
//    int theEnd =start;
//    for(;start>0;start--){
//      servos[pos].write(start);
//      delay(10);
//    }
//
//    for(;start<theEnd;start++){
//      servos[pos].write(start);
//      delay(10);
//    }
//   }
    
