
#include <Servo.h>
#include <Wire.h> 
#include <LiquidCrystal_I2C.h>
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h> 

LiquidCrystal_I2C lcd(0x27, 16, 2); // In NodeMcu SDA=> D2 , SCL=>D3 
Servo myServo;

#define FIREBASE_HOST "Your Project host address"  // Example: my-app.ieon321.com (provided by firebase) 
#define FIREBASE_AUTH "Firebase Authendication secret key"
#define WIFI_SSID "your Wifi name"
#define WIFI_PASSWORD "your Wifi password"

int pos;
byte statusLed    = 13 ;    // device is working or not.
byte sensorInterrupt = 0  ; // 0 = digital pin 2
byte sensorPin       = 5;   // Pin Number in NodeMcu D1
byte servoPin = 2;          // Pin Number in NodeMcu D4  
// The hall-effect flow sensor outputs approximately 4.5 pulses per second per
// litre/minute of flow.
float calibrationFactor = 4.5;
volatile byte pulseCount;  
float flowRate;
unsigned int flowMilliLitres;
unsigned long totalMilliLitres ;
unsigned long oldTime;
int limit;
//boolean swh;
String value="NO";
void setup()
{
  myServo.attach(servoPin); 
  // Initialize a serial connection for reporting values to the host
  Serial.begin(9600);
  myServo.write(0);
  Wire.begin(4,0); // In NodeMcu SDA=> D2(4) , SCL=>D3(0) 
  lcd.begin();
  lcd.backlight();
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting");
  lcd.setCursor(0,0);
  lcd.print("Connecting.....");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  
  // Set up the status LED line as an output
  pinMode(statusLed, OUTPUT);
  digitalWrite(statusLed, HIGH);  // We have an active-low LED attached
  
  pinMode(sensorPin, INPUT);
  digitalWrite(sensorPin, HIGH);

  pulseCount        = 0;
  flowRate          = 0.0;
  flowMilliLitres   = 0;
  totalMilliLitres  =0;
  oldTime           = 0;
  Firebase.setInt("User/0001/limit",1000);
  limit=Firebase.getInt("User/0001/limit");
  // The Hall-effect sensor is connected to pin 2 which uses interrupt 0.
  // Configured to trigger on a FALLING state change (transition from HIGH
  // state to LOW state)
  attachInterrupt(digitalPinToInterrupt(sensorPin), pulseCounter, FALLING);
  //totalMilliLitres+=Firebase.getFloat("User/0001/water");
  Firebase.setString("User/0001/tap","OPEN");
  //Firebase.setString("User/0001/tapU","CLOSE");

  }
int n=0;

void loop()
{
  limit=Firebase.getInt("User/0001/limit");
 
   
   if((millis() - oldTime) > 1000)    // Only process counters once per second
  { 
    // Disable the interrupt while calculating flow rate and sending the value to
    // the host
    detachInterrupt(digitalPinToInterrupt(sensorPin));
        
    // Because this loop may not complete in exactly 1 second intervals we calculate
    // the number of milliseconds that have passed since the last execution and use
    // that to scale the output. We also apply the calibrationFactor to scale the output
    // based on the number of pulses per second per units of measure (litres/minute in
    // this case) coming from the sensor.
    flowRate = ((1000.0 / (millis() - oldTime)) * pulseCount) / calibrationFactor;
    
    // Note the time this processing pass was executed. Note that because we've
    // disabled interrupts the millis() function won't actually be incrementing right
    // at this point, but it will still return the value it was set to just before
    // interrupts went away.
    oldTime = millis();
    
    // Divide the flow rate in litres/minute by 60 to determine how many litres have
    // passed through the sensor in this 1 second interval, then multiply by 1000 to
    // convert to millilitres.
    flowMilliLitres = (flowRate / 60) * 1000;
    
    // Add the millilitres passed in this second to the cumulative total
    totalMilliLitres += flowMilliLitres;
    
    unsigned int frac;
    
    // Print the flow rate for this second in litres / minute
     Serial.print("Flow rate: ");
    Serial.print(int(flowRate));  // Print the integer part of the variable
    Serial.print(" L/min");
    Serial.print("\t");       // Print tab space
    // Print the cumulative total of litres flowed since starting
    Serial.print("Output Liquid Quantity: ");        
    Serial.print(totalMilliLitres);
    Serial.print("mL     "); 
    //Serial.print("\t");       // Print tab space
    Serial.println(totalMilliLitres/1000);
    Serial.print("L");
    
    lcd.setCursor(0,0);
    lcd.print("FlowSpeed:");
    lcd.print(int(flowRate));
    lcd.print("L/min");
    lcd.setCursor(0,1);
    lcd.print("WaterUsed:");
    lcd.print(totalMilliLitres);
    lcd.print("ml");
    Firebase.setFloat("User/0001/water", totalMilliLitres); //send data to colud firebase
     value = Firebase.getString("User/0001/tap");
     //limit = Firebase.getInt("User/0001/limit");   // if limit get from firebase it's crashed whole code. 
     value.toUpperCase();

  // handle error
  if (Firebase.failed()) {
      Serial.print("setting /number failed:");
      Serial.println(Firebase.error());  
      return;
  }
    //delay(1000);
    // Reset the pulse counter so we can start incrementing again
    pulseCount = 0;
    
    // Enable the interrupt again now that we've finished sending output
    attachInterrupt(digitalPinToInterrupt(sensorPin), pulseCounter, FALLING);

    if(limit>totalMilliLitres){
      while(pos>0){
          myServo.write(pos);
          delay(50);
          pos-=1;
        }
      
    }else {
      Firebase.setString("User/0001/tap","CLOSE");
      if(Firebase.getString("User/0001/tap").equals("OPEN")){
        while(pos>0){
          myServo.write(pos);
          delay(50);
          pos-=1;
        }
        Firebase.setString("User/0001/tap","OPEN");
      }else if(Firebase.getString("User/0001/tap").equals("CLOSE")){
        while(pos<=75){
          myServo.write(pos);
          delay(50);
          pos+=1;
        }
        //Firebase.setString("User/0001/tap","CLOSE");
      }
    }
    /*if(Firebase.getString("User/0001/tap").equals("OPEN")){
      while(pos>0){
          myServo.write(pos);
          delay(50);
          pos-=1;
      
    }
    Firebase.setString("User/0001/tap","OPEN");
    }
    if(Firebase.getString("User/0001/tap").equals("CLOSE")){
      while(pos<=75){
          myServo.write(pos);
          delay(50);
          pos+=1;
        }
        Firebase.setString("User/0001/tap","OPEN");
    }*/
    
   /* if(Firebase.getString("User/0001/tap").equals("OPEN")){
      if(limit<=totalMilliLitres){
        Firebase.setString("User/0001/tap","CLOSE");
        while(pos<=75){
          myServo.write(pos);
          delay(50);
          pos+=1;
        }
      }else{
          myServo.write(0);
       }
   }else if(Firebase.getString("User/0001/tap").equals("CLOSE")){
      while(pos<=75){
          myServo.write(pos);
          delay(50);
          pos+=1;
        }
   }*/
  }
   
}
void pulseCounter()
{
  // Increment the pulse counter
  pulseCount++;
}

  


