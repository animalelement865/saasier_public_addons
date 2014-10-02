/*************************************************** 
  This is an example sketch for our optical Fingerprint sensor

  Designed specifically to work with the Adafruit BMP085 Breakout 
  ----> http://www.adafruit.com/products/751

  These displays use TTL Serial to communicate, 2 pins are required to 
  interface
  Adafruit invests time and resources providing this open source code, 
  please support Adafruit and open-source hardware by purchasing 
  products from Adafruit!

  Written by Limor Fried/Ladyada for Adafruit Industries.  
  BSD license, all text above must be included in any redistribution
 ****************************************************/


#include <Adafruit_Fingerprint.h>
#include <SoftwareSerial.h>

uint8_t getFingerprintEnroll(uint8_t id);
int getFingerprintIDez();

int menu_selected = 2;
int enroll_id_value;  
  

// pin #2 is IN from sensor (GREEN wire)
// pin #3 is OUT from arduino  (WHITE wire)
SoftwareSerial mySerial(52, 53);

Adafruit_Fingerprint finger = Adafruit_Fingerprint(&mySerial);

void setup()  
{
  Serial.begin(9600);
  //Serial.println("m,fingertest");
  delay(500);
  // set the data rate for the sensor serial port
  finger.begin(57600);
  
  delay(500);
  
  if (finger.verifyPassword()) {
    Serial.println("m,Sensor");
  } else {
    Serial.println("m,NoSensor");
    while (1);
  }
  
  delay(500);
  
  Serial.println("m,Waiting for valid finger");
  
  //Serial.print("Press E to Enroll ");
  //Serial.print("Press I to Identify Fingerprint ");
}

void loop()                     // run over and over again
{
  while (Serial.available() > 0) {
    menu_selected = Serial.parseInt();
    enroll_id_value = Serial.parseInt();
    
    // 1 = Enroll fingerprint
    // 2 = Check fingerprint
    
    if (Serial.read() == '\n') {   
    // end of serial read. 
    }
  }
    if (menu_selected == 1)
    {
      //Serial.println("/m/Enroll");
      //Serial.println("Type in the ID # you want to save this finger as...");
      uint8_t id = 0;
      
      Serial.print("m,Enrolling ID #");
      Serial.println(enroll_id_value);
      Serial.print("e,");
      Serial.println(enroll_id_value);
              
      while (!  getFingerprintEnroll(enroll_id_value) );
      menu_selected = 2;
    }
    
    if (menu_selected == 2)
    {
      getFingerprintIDez();
    }
  
}

uint8_t getFingerprintID() 
{
  uint8_t p = finger.getImage();
  switch (p) {
    case FINGERPRINT_OK:
      Serial.println("m,Image taken");
      break;
    case FINGERPRINT_NOFINGER:
      Serial.println("m,No finger detected");
      return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("m,Communication error");
      return p;
    case FINGERPRINT_IMAGEFAIL:
      Serial.println("m,Imaging error");
      return p;
    default:
      Serial.println("m,Unknown error");
      return p;
  }

  // OK success!

  p = finger.image2Tz();
  switch (p) {
    case FINGERPRINT_OK:
      Serial.println("m,Image converted");
      break;
    case FINGERPRINT_IMAGEMESS:
      Serial.println("m,Image too messy");
      return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("m,Communication error");
      return p;
    case FINGERPRINT_FEATUREFAIL:
      Serial.println("m,Could not find fingerprint features");
      return p;
    case FINGERPRINT_INVALIDIMAGE:
      Serial.println("m,Could not find fingerprint features");
      return p;
    default:
      Serial.println("m,Unknown error");
      return p;
  }
  
  // OK converted!
  p = finger.fingerFastSearch();
  if (p == FINGERPRINT_OK) {
    Serial.println("m,Found a print match!");
  } else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    Serial.println("m,Communication error");
    return p;
  } else if (p == FINGERPRINT_NOTFOUND) {
    Serial.println("m,Did not find a match");
    return p;
  } else {
    Serial.println("m,Unknown error");
    return p;
  }   
  
  // found a match!
  Serial.print("i,"); Serial.print(finger.fingerID); 
  Serial.print(","); Serial.println(finger.confidence); 
}

// returns -1 if failed, otherwise returns ID #
int getFingerprintIDez() {
  uint8_t p = finger.getImage();
  if (p != FINGERPRINT_OK)  return -1;

  p = finger.image2Tz();
  if (p != FINGERPRINT_OK)  return -1;

  p = finger.fingerFastSearch();
  if (p != FINGERPRINT_OK)  return -1;
  
  // found a match!
  Serial.print("i,"); Serial.print(finger.fingerID); 
  Serial.print(","); Serial.println(finger.confidence);
  return finger.fingerID; 
}

uint8_t getFingerprintEnroll(uint8_t id) {
  uint8_t p = -1;
  Serial.println("m,Waiting for valid finger to enroll");
  while (p != FINGERPRINT_OK) {
    p = finger.getImage();
    switch (p) {
    case FINGERPRINT_OK:
      Serial.println("m,Image taken");
      break;
    case FINGERPRINT_NOFINGER:
      Serial.println(".");
      break;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("m,Communication error");
      break;
    case FINGERPRINT_IMAGEFAIL:
      Serial.println("m,Imaging error");
      break;
    default:
      Serial.println("m,Unknown error");
      break;
    }
  }

  // OK success!

  p = finger.image2Tz(1);
  switch (p) {
    case FINGERPRINT_OK:
      Serial.println("m,Image converted");
      break;
    case FINGERPRINT_IMAGEMESS:
      Serial.println("m,Image too messy");
      return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("m,Communication error");
      return p;
    case FINGERPRINT_FEATUREFAIL:
      Serial.println("m,Could not find fingerprint features");
      return p;
    case FINGERPRINT_INVALIDIMAGE:
      Serial.println("m,Could not find fingerprint features");
      return p;
    default:
      Serial.println("m,Unknown error");
      return p;
  }
  
  Serial.println("m,Remove finger");
  delay(2000);
  p = 0;
  while (p != FINGERPRINT_NOFINGER) {
    p = finger.getImage();
  }

  p = -1;
  Serial.println("m,Place same finger again");
  while (p != FINGERPRINT_OK) {
    p = finger.getImage();
    switch (p) {
    case FINGERPRINT_OK:
      Serial.println("m,Image taken");
      break;
    case FINGERPRINT_NOFINGER:
      Serial.print(".");
      break;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("m,Communication error");
      break;
    case FINGERPRINT_IMAGEFAIL:
      Serial.println("m,Imaging error");
      break;
    default:
      Serial.println("m,Unknown error");
      break;
    }
  }

  // OK success!

  p = finger.image2Tz(2);
  switch (p) {
    case FINGERPRINT_OK:
      Serial.println("m,Image converted");
      break;
    case FINGERPRINT_IMAGEMESS:
      Serial.println("m,Image too messy");
      return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("m,Communication error");
      return p;
    case FINGERPRINT_FEATUREFAIL:
      Serial.println("m,Could not find fingerprint features");
      return p;
    case FINGERPRINT_INVALIDIMAGE:
      Serial.println("m,Could not find fingerprint features");
      return p;
    default:
      Serial.println("m,Unknown error");
      return p;
  }
  
  
  // OK converted!
  p = finger.createModel();
  if (p == FINGERPRINT_OK) {
    Serial.println("m,Prints matched!");
  } else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    Serial.println("m,Communication error");
    return p;
  } else if (p == FINGERPRINT_ENROLLMISMATCH) {
    Serial.println("m,Fingerprints did not match");
    return p;
  } else {
    Serial.println("m,Unknown error");
    return p;
  }   
  
  p = finger.storeModel(id);
  if (p == FINGERPRINT_OK) {
    Serial.println("m,Stored!");
  } else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    Serial.println("m,Communication error");
    return p;
  } else if (p == FINGERPRINT_BADLOCATION) {
    Serial.println("m,Could not store in that location");
    return p;
  } else if (p == FINGERPRINT_FLASHERR) {
    Serial.println("m,Error writing to flash");
    return p;
  } else {
    Serial.println("m,Unknown error");
    return p;
  }   

}
