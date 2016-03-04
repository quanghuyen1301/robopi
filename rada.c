#include <stdio.h> 
#include <wiringPi.h> 
#define CONFIG_RADA_ECHO 4 
#define CONFIG_RADA_TRIG 5
unsigned int micros0 = 0;
unsigned int micros1 = 0;
    unsigned int duration; 
    unsigned int distance;


void gpioedge(void)
{
//printf("%s\n",__func__);
//micros0 = micros1;
micros1 = micros();
//printf("__RX 0x%08x \n",micros1 - micros0);

}

void main(void)
{
//    unsigned int duration; 
  //  unsigned int distance;
        if (wiringPiSetup() == -1)
		return ;


	void (*gpio_irq)() = gpioedge;


        pinMode(CONFIG_RADA_TRIG, OUTPUT);
//        pinMode(CONFIG_RADA_ECHO, INPUT);
          digitalWrite(CONFIG_RADA_TRIG, 0);
//      digitalWrite(CONFIG_RADA_ECHO, 0);
	delay(1000);
//	wiringPiISR(CONFIG_RADA_ECHO,INT_EDGE_BOTH,gpio_irq);   
      wiringPiISR(CONFIG_RADA_ECHO,INT_EDGE_FALLING,gpio_irq);   
//      wiringPiISR(CONFIG_RADA_ECHO,INT_EDGE_RISING,gpio_irq);   
for(;;){
	
      	digitalWrite(CONFIG_RADA_TRIG, 1);
	delayMicroseconds(5);
	digitalWrite(CONFIG_RADA_TRIG, 0);
	micros0 = micros ();

//	while(micros1 == 0)
//	{

//	}
	int timeout = 100;
	for(;micros1 == 0;){
	delay(1);
	timeout --;
	if(timeout ==0){
	printf("Timeout \n");
		break;
	}
	}

if(timeout) {
duration = micros1 - micros0;
distance = ((duration/2)/(29.412));
printf("%d\n",distance);
micros1 = 0;

delay(100);
}
}
        //printf("__TX 0x%08x \n",micros ());

//duration = pulseIn(CONFIG_RADA_ECHO,HIGH); 
//distance = ((duration/2)/(29.412));
//   printf("%d cm",distance);

//        digitalWrite(CONFIG_RADA_TRIG, 0);
//unsigned int micros0 = micros ();
 //  while (digitalRead(CONFIG_RADA_ECHO) == HIGH)
//{
//}

//unsigned int micros1 = micros ();
//printf("%d cm\n",((micros1 - micros0 ) /2)/(29.412));
//printf("0x%08x - 0x%08x\n",micros0,micros1);

//void (*gpio_irq)() = gpioedge;
//wiringPiISR(CONFIG_RADA_ECHO,INT_EDGE_BOTH,gpio_irq);	
//        digitalWrite(CONFIG_RADA_TRIG, 1);

//for(;;){
//	printf("%d",digitalRead(CONFIG_RADA_ECHO));
//}
//	int wiringPiISR (int pin, int edgeType,  void (*function)(void)) ;

//	printf("Hello");
//        digitalWrite(CONFIG_RADA_TRIG, 0);

for(;;);
//delay(1000);	
}
