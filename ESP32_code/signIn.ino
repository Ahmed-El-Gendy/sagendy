#include <Arduino.h>
#include <WiFi.h>
#include <Firebase_ESP_Client.h>
#include "DHT.h"

#define DHTPIN 18     // what pin we're connected to
#define DHTTYPE DHT11   // DHT 11 
DHT dht(DHTPIN, DHTTYPE);

#include "addons/TokenHelper.h"
#include "addons/RTDBHelper.h"

#define WIFI_SSID "AAA"
#define WIFI_PASSWORD "deleted_for_privacy"
#define API_KEY "deleted_for_privacy"  
#define DATABASE_URL "deleted_for_privacy"
#define USER_EMAIL "sagendywbeso@gmail.com"
#define USER_PASSWORD "12345678180129122"

#define gasSensor 34
#define flameSensor 21
#define IRIn 14 //35
#define IROut 26
#define doorIn 33
#define doorOut 32
#define buzzer 15
#define inner_leds 25 
#define right_leds 19
#define left_leds 5

// Define Firebase objects
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
String uid;

unsigned long sendDataPrevMillis = 0;

int people = 0;
bool irO = false;
bool irI = false;
bool alarms = false;
int atHome = 0;

void setup(){
  Serial.begin(9600);

  // initial pins
  //pinMode(gasSensor, INPUT);
  pinMode(flameSensor, INPUT);
  pinMode(IRIn, INPUT);
  pinMode(IROut, INPUT);
  pinMode(inner_leds, OUTPUT);
  pinMode(right_leds, OUTPUT);
  pinMode(left_leds, OUTPUT);
  pinMode(buzzer, OUTPUT);
  pinMode(doorOut, OUTPUT);
  pinMode(doorIn, INPUT);
  digitalWrite(doorOut,HIGH);
  dht.begin();

  // using WIFI and Firebase
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to WiFi ..");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print('.');
    delay(500);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;
  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD;

  Firebase.reconnectWiFi(true);
  fbdo.setResponseSize(4096); //4096
  config.token_status_callback = tokenStatusCallback;
  config.max_token_generation_retry = 5; //5

  Firebase.begin(&config, &auth);

  Serial.println("Getting User UID");
  while ((auth.token.uid) == "") {
    Serial.print('.');
    delay(1000);
  }
  uid = auth.token.uid.c_str();
  Serial.print("User UID: ");
  Serial.print(uid);
  Serial.println();
}

void loop(){

  
  float h = dht.readHumidity();
  // Read temperature as Celsius
  float t = dht.readTemperature();
  
  // Check if any reads failed and exit early (to try again).
  if (isnan(h) || isnan(t)) {
    Serial.println("Failed to read from DHT sensor!");
  }
  Serial.print("Humidity: "); 
  Serial.print(h);
  Serial.print(" %\t");
  Serial.print("Temperature: "); 
  Serial.print(t);
  Serial.print(" *C ");

  if (Firebase.ready()){
    Firebase.RTDB.setIntAsync(&fbdo, "gas", gas());
    Firebase.RTDB.setIntAsync(&fbdo, "temp", t);
    Firebase.RTDB.setIntAsync(&fbdo, "humidity", h);
    Firebase.RTDB.setIntAsync(&fbdo, "people", peoples());
    Firebase.RTDB.setBoolAsync(&fbdo, "fire", fire());
    Firebase.RTDB.setBoolAsync(&fbdo, "alarms", alarms);
    if (atHome)
    {
      Firebase.RTDB.setBoolAsync(&fbdo, "safe", true);
    }
    Firebase.RTDB.setBoolAsync(&fbdo, "test", 0);


    if (Firebase.RTDB.getInt(&fbdo, "athome")) {
      if (fbdo.dataType() == "int") {
        int intValue = fbdo.intData();
        Serial.printf("atHome  %d \n", intValue);
        atHome = intValue;
      }
    }

    if (Firebase.RTDB.getInt(&fbdo, "/livingroom/light")) {
      if (fbdo.dataType() == "int") {
        int intValue = fbdo.intData();
        //Serial.printf("Inner Lamp: %d \n", intValue);
        analogWrite(inner_leds, intValue);
      }
    }
    else {
      //Serial.println(fbdo.errorReason());
    }

    if (Firebase.RTDB.getInt(&fbdo, "/kitchen/light")) {
      if (fbdo.dataType() == "int") {
        int intValue = fbdo.intData();
        //Serial.printf("Right Lamp: %d \n", intValue);
        analogWrite(right_leds, intValue);
      }
    }
    else {
      //Serial.println(fbdo.errorReason());
    }

    if (Firebase.RTDB.getInt(&fbdo, "/bathroom/light")) {
      if (fbdo.dataType() == "int") {
        int intValue = fbdo.intData();
        //Serial.printf("Left Lamp: %d \n", intValue);
        analogWrite(left_leds, intValue);
      }
    }
    else {
      //Serial.println(fbdo.errorReason());
    }
    if (Firebase.RTDB.getInt(&fbdo, "alarmr")) {
      if (fbdo.dataType() == "int") {
        int value = fbdo.intData();
        if(!value)
        {
          buzzer_off();
          Firebase.RTDB.setIntAsync(&fbdo, "alarmr", 1);
        }
      }
    }



  }
  

}


int peoples()
{
  // People counter
  bool irIn = digitalRead(IRIn);
  bool irOut = !digitalRead(IROut);
  Serial.print("ir in ");
  Serial.println(irIn);
  Serial.print("ir out ");
  Serial.println(irOut);
  Serial.print("door in ");
  bool doorin = digitalRead(doorIn);
  Serial.println(doorin);
  Firebase.RTDB.setBoolAsync(&fbdo, "/livingroom/door", !doorin);
  
  if (irO && irIn)
  {
    people++;
    irO = false;
    irI = false;
  }
  else if (irIn)
  {
    irI = true;
  }

  if (irI && irOut)
  {
    people--;
    irO = false;
    irI = false;
  }
  else if (irOut)
  {
    irO = true;
  }

  if ((!doorin) && !atHome)
  {
    buzzer_on();
    Firebase.RTDB.setBoolAsync(&fbdo, "safe", false);
  }
  if (people < 0)
  {
    people = 0;
  }
  Serial.println(people);
  return (people);
}
int gas()
{
  int gasValue = analogRead(gasSensor);
  Serial.printf("gas value: %d\n", gasValue);
  if(gasValue>1200)
  {
    buzzer_on();
  }
  return (gasValue);
}

int fire()
{
  bool fireValue = digitalRead(flameSensor);
  Serial.printf("flame value: %d\n", !fireValue);
  if(!fireValue)
  {
    buzzer_on();
  }
  return (fireValue);
}

int irin()
{
  int irValue = digitalRead(IRIn);
  Serial.printf("ir in value: %d\n", irValue);
  return (irValue);
}

int irout()
{
  int irValue = digitalRead(IROut);
  Serial.printf("ir out value: %d\n", irValue);
  return (irValue);
}

void buzzer_on()
{
  digitalWrite(buzzer,HIGH);
  alarms = true;
}
void buzzer_off()
{
  digitalWrite(buzzer,LOW);
  alarms = false;
}




