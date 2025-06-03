#include <Keypad.h> 

const byte ROWS = 4; 
const byte COLS = 4; 

char keys[ROWS][COLS] = {
  {'1', '2', '3', 'A'}, 
  {'4', '5', '6', 'B'}, 
  {'7', '8', '9', 'C'}, 
  {'*', '0', '#', 'D'} 
};

byte rowPins[ROWS] = {6, 7, 8, 9};

byte colPins[COLS] = {2, 3, 4, 5};

Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);

const int ledPin = A1;        

const int trigPin = 10;       
const int echoPin = 11;       

void setup() {
  Serial.begin(9600); 

  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, LOW); 
  
  pinMode(trigPin, OUTPUT); 
  pinMode(echoPin, INPUT);  

  Serial.println("Arduino pronto. Enviando dados para Java...");
}

void loop() {
  char tecla = keypad.getKey(); 
  if (tecla) { 
    Serial.print("K:"); 
    Serial.println(tecla); 
    
    digitalWrite(ledPin, HIGH);
    delay(50); 
    digitalWrite(ledPin, LOW);
  }

  long duration;
  float distance;

  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10); 
  digitalWrite(trigPin, LOW);

  duration = pulseIn(echoPin, HIGH, 30000); 

  distance = (duration / 2.0) * 0.0343;

  if (duration == 0) { 
    Serial.println("D:TIMEOUT"); 
    Serial.println("DEBUG: Sensor - Timeout (objeto fora do alcance ou erro)"); 
  } else if (distance > 0 && distance < 400) { 
    Serial.print("D:"); 
    Serial.println(distance); 
    Serial.print("DEBUG: Sensor - Distancia: "); 
    Serial.print(distance);
    Serial.println(" cm");
  } else {
    Serial.println("D:INVALID"); 
    Serial.print("DEBUG: Sensor - Leitura invalida: "); 
    Serial.print(distance);
    Serial.println(" cm");
  }

  delay(100); 
}
