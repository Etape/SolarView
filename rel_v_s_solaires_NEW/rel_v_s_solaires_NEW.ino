
/*programme de relevé des mesures d'irradiation, d'ensoleillement
 humidité, temperature tension et courant sur des panneaux solaires
à intervalle d'une minute. stockage de date-heure dans un fichier  
Excel
 The circuit:
 * SD card attached to SPI bus as follows:
 ** MOSI - pin 11;
 ** MISO - pin 12
 ** CLK - pin 13
 ** CS - pin 10 (for MKRZero SD: SDCARD_SS_PIN)

************************************************************
format des données enregistrées sur la carte SD 
 aaaa-mm-jj;hh:mm;irradiation;vent;humudité;temperature ambiante;temperature panneau;courant batterie;courant charge
 exemple :
 2020-10-01;10:22;1.5;800;1.75;22.60;35.32;8.2 
*/
/* étant donné le nombre de pins limité de l'ATMEGA328P, nous avons decidé d'utiliser 
 * un relai pour transiter entre la capture du temps et la capture de temperature du panneau 
*/
#include <Wire.h>
#include <TimeLib.h>
#include <SD.h>   // librairie de gestion des fichiers de la SD
#include <DS1307RTC.h> // librairie pour la gestion de l'horloge
//#include <Time.h>
#include <SPI.h>
#include <DHT.h>  // capteur de temperature et d'humidité
#include <DHT_U.h>
#include <SoftwareSerial.h>

#define DHTPIN_PAN1 2
#define DHTPIN_PAN2 3
#define DHTPIN_ENV 4
#define DHTPIN_BAT 5
#define DHTPIN_OND 6
#define DHTTYPE_PAN1 DHT11
#define DHTTYPE_PAN2 DHT11
#define DHTTYPE_ENV DHT11
#define DHTTYPE_OND DHT11
#define DHTTYPE_BAT DHT22

int RadianceSensorPin=A0;
int anemometer=A1;
int CurrentSensorPin1=A2;
int CurrentSensorPin2=A3;
int voltPan1=A4;
int voltPan2=A5;
int voltBat=A6;

String date="";
  String heure="";
  String temperature_amb="";
  String temperature_pan1="";
  String temperature_pan2="";
  String temperature_bat="";
  String temperature_ond="";
  String humidite="";
  String courant_pan1="";
  String tension_pan1="";
  String courant_pan2="";
  String tension_pan2="";
  String tension_bat="";
  String Wind="";
  String radiance="";
  String upload="";
  String upload_VI="";

// Set RTC pins:  CE, IO,CLK
// DS1307RTC RTC(A4 SDA,A5 SCL);
DHT dht_PAN1(DHTPIN_PAN1, DHTTYPE_PAN1);
DHT dht_PAN2(DHTPIN_PAN2, DHTTYPE_PAN2);
DHT dht_ENV(DHTPIN_ENV, DHTTYPE_ENV);
DHT dht_BAT(DHTPIN_BAT, DHTTYPE_BAT);
DHT dht_OND(DHTPIN_OND, DHTTYPE_OND);

File myFile;
const int chipSelect = 53;
String fileName="";
String datage="";
void setup() {  //initial welcome :)
  Serial.begin(9600);
  dht_PAN1.begin();
  dht_PAN2.begin();
  dht_ENV.begin();
  dht_BAT.begin();
  dht_OND.begin();
  Serial.print("Initializing SD card and RTC Module...");
  readTime();
   fileName +="D_";
   fileName += datage;
   fileName +=".csv";
  
  pinMode(chipSelect,OUTPUT); 
  digitalWrite(chipSelect,HIGH);
  // see if the card is present and can be initialized:
  if (!SD.begin(chipSelect)) {
    Serial.println("Card failed, or not present");
    while(1);
  }
  Serial.print("card initialized. Begining writing in file...");
  Serial.println(fileName);
  upload+="date;heure";
  upload+=";";
  upload+="irradiation";
  upload+=";";
  upload+="Vitesse du vent";
  upload+=";";
  upload+="humidite ambiante";
  upload+=";";
  upload+="temperature ambiante";
  upload+=";";
  upload+="temperature panneau 1";
  upload+=";";
  upload+="temperature panneau 2";
  upload+=";";
  upload+="temperature batterie";
  upload+=";";
  upload+="temperature onduleur";
  upload+=";";
  upload+="tension panneau 1";
  upload+=";";
  upload+="tension panneau 2";
  upload+=";";
  upload+="tension batteries";
  upload+=";";
  upload+="courant panneau 1";
  upload+=";";
  upload+="courant panneau 2";
  upload+=";";
  Serial.println("trying writing headers ....");
   bool t=writeInFile();
  while(!t){
    delay(2000);
    t=writeInFile();
  }
  Serial.println(" Headers written successfully ....");
}

void loop() {
  
   date= String("");
   heure=String("");
   temperature_amb=String("");
   temperature_pan1=String("");
   temperature_pan2=String("");
   temperature_bat=String("");
   temperature_ond=String("");
   humidite=String("");
   courant_pan1=String("");
   tension_pan1=String("");
   courant_pan2=String("");
   tension_pan2=String("");
   tension_bat=String("");
   Wind=String("");
   radiance=String("");
   upload=String("");
   upload_VI=String("");
   
   readTime();
   readTempHum();
   readCurrent();
   readTensions();
   readIrradiation();
   readWindSpeed();

    upload+=date;
    upload+=";";
    upload+=heure;
    upload+=";";
    upload+=radiance;
    upload+=";";
    upload+=Wind;
    upload+=";";
    upload+=humidite;
    upload+=";";
    upload+=temperature_amb;
    upload+=";";
    upload+=temperature_pan1;
    upload+=";";
    upload+=temperature_pan2;
    upload+=";";
    upload+=temperature_bat;
    upload+=";";
    upload+=temperature_ond;
    upload+=";";
    upload+=tension_pan1;
    upload+=";";
    upload+=tension_pan2;
    upload+=";";
    upload+=tension_bat;
    upload+=";";
    upload+=courant_pan1;
    upload+=";";
    upload+=courant_pan2;
    upload+=";";
    // ***********************************
  /* upload_VI+=temperature_pan;
  upload_VI+="a";
  upload_VI+=temperature_amb;
  upload_VI+="b";
  upload_VI+=radiance;
  upload_VI+="c";
  upload_VI+=courant_pan;
  upload_VI+="d";
  upload_VI+=humidite;
  upload_VI+="e";
  upload_VI+=Wind;
  upload_VI+="f";
  upload_VI+=courant_charge;
 */
  writeInFile();
  delay (2000);
}
bool writeInFile(){
  
  // open the file. note that only one file can be open at a time,
  // so you have to close this one before opening another.
  File dataFile = SD.open(fileName, FILE_WRITE);
  // if the file is available, write to it:
  if (dataFile) {
    dataFile.println(upload);
    dataFile.close();
    // print to the serial port too:
    Serial.println(upload);
    return true;    
  }
  // if the file isn't open, pop up an error:
  else {
    Serial.print("error opening  ");
    Serial.println(fileName);
    return false;
  }
}

void readTime(){
  //DS1302 timestamp structure 
 tmElements_t tm;
  
  if (RTC.read(tm)) {  

    date+=tmYearToCalendar(tm.Year);
    date+="-";
    date+=tm.Month;
    date+="-";
    date+=tm.Day;

    heure+=tm.Hour;
    heure+=":";
    heure+=tm.Minute;
    heure+=":";
    heure+=tm.Second;
    
    datage+=tm.Hour;
    datage+=tm.Minute;
    datage+=tm.Second;  
  }
  else {
    Serial.println("DS1302 read error!  Please check the circuitry.");
    Serial.println("***********************************************************");
  }
}
void readTensions(){
  float Vp1=0;
  float Vp2=0;
  float Vbat=0;
    for(int i=0;i<100;i++){
       Vp1 = Vp1 + 4.819*analogRead(voltPan1)/1000.0;
       Vp2 = Vp2 + 4.819*analogRead(voltPan2)/1000.0;
       Vbat = Vbat + 4.819*analogRead(voltBat)/1000.0;     
       delay(2); 
     }
  Vp1=11.5*Vp1/100;
  Vp2=11.5*Vp2/100;
  Vbat=11.5*Vbat/100;
  
  tension_bat+=Vbat;
  tension_pan1+=Vp1;
  tension_pan2+=Vp2;
}

void readTempHum(){
    // Reading temperature or humidity takes about 250 milliseconds!
  // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
  float temperature_PAN1 = dht_PAN1.readTemperature();
  float temperature_PAN2 = dht_PAN2.readTemperature();
  float humidity_ENV = dht_ENV.readHumidity();
  float temperature_ENV = dht_ENV.readTemperature();
  float temperature_BAT = dht_BAT.readTemperature();
  float temperature_OND = dht_OND.readTemperature();

   temperature_amb+=temperature_ENV;
   temperature_pan1+=temperature_PAN1;
   temperature_pan2+=temperature_PAN2;
   temperature_bat+=temperature_BAT;
   temperature_ond+=temperature_OND;
   humidite+=humidity_ENV;
}


void readCurrent(){
   float sample1=0;
   float sample2=0;
 
   for(int i=0;i<150;i++){
     sample1 = sample1 + analogRead(CurrentSensorPin1);    
     sample2 = sample2 + analogRead(CurrentSensorPin2);     
     delay(2); 
   }
     
   sample1=sample1/150;
   sample2=sample2/150;
   sample1=(sample1/1024)*5000;
   sample2=(sample2/1024)*5000;
   sample1=sample1-2500;
   sample2=sample2-2500;
     
  float mA1 = (sample1/66)+0.52;
  float mA2 = (sample2/66)+0.52;
  courant_pan1+=mA1;
  courant_pan2+=mA2;
}
 void readIrradiation(){ float sample=0;
 //Cumulatively read and sum up 64 values of the sensor voltage signalfor the Irradiation Value  
     for(int i=0;i<64;i++){
       sample = sample + 4.819159335*analogRead(RadianceSensorPin)/1000.0;     
       delay(2); 
     }
     float Irradiation;
     Irradiation = (sample/64.0)*250.0;     // Find the average value by dividing by 64 
      if(Irradiation>2000){
        Irradiation=0;
      }
      radiance+=Irradiation;
 }
 void readWindSpeed(){ 
  float sample2=0;
 //Cumulatively read and sum up 64 values of the sensor voltage signalfor the Irradiation Value  
     for(int i=0;i<100;i++){
       sample2 = sample2 + 4.819159335*analogRead(anemometer)/1000.0;     
       delay(2); 
     }
     float windSpeed;
     windSpeed = (sample2/100.0);     // Find the average value by dividing by 64 
      windSpeed=(windSpeed-0.4)*32;
      windSpeed=(windSpeed/10);
      Wind+=windSpeed;
 }
