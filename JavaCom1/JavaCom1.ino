/*
 * This Sketch belongs to the SwingSerial Project and is a very simple example to test the functionality of the Java Software. 
 * The Arduino Standard Baud Rate is used here(9600). 
 * In this example, the pin 13(Builtin LED of the UNO) can be set to high or low using SwingSerial
 * The Sketch also sends analog readings from pin A0 via a serial port
 */

 
int Data,oldData; //These varaibles are used to read and handle the analog input
  
void setup() {
  // put your setup code here, to run once:
Serial.begin(9600); //Establish a Serial Connection with the Standard baud Rate of 9600
pinMode(13,OUTPUT); //Open pin13 as an Output. This pin is also the builtin LED of the UNO
}

void loop() {
  // put your main code here, to run repeatedly:
  int b; // define a Local Variable
  while(Serial.available()>0) //Look if theres Data incoming
  {
     b  = Serial.read(); //As long as there is incoming data, save it to the Local Variable
     b = b -48; // In ASCII 0 = 48. So the get the correct reading, we have to do this.
   
   if(b == 0) //Check the value of the Local Variable
   {
    digitalWrite(13,0); //pin 13 is set to LOW
   }
   if(b== 1)//Check the value of the Local Variable
   {
    digitalWrite(13,1); //pin 13 is set to HIGH
   }
    //Serial.println(b);  //Print back the value of the Local variable. Useful for Debugging   
  }

  Data= analogRead(A0); //read the analog input 

  if(Data!=oldData && Data != oldData+1 && Data!=oldData-1) //Check if the read Data is within +- 1 of the previous reading. We do this to avoid useless readings
  {
     Serial.println(Data); //Send the readings via a serial port
     oldData=Data; // We do this in order to avoid sending to much data
  }

  delay(10); // Software Debouncing
 
 
}
