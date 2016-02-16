/*
 * blink.c:
 *      blinks the first LED
 *      Gordon Henderson, projects@drogon.net
 */

#include <stdio.h>
#include <wiringPi.h>
#include "getch.c"
#define GPIO_MOTO_UP_1 2 //RIGHT
#define GPIO_MOTO_DOWN_1 0 //RIGHT

#define GPIO_MOTO_UP_2 1 //LEFT
#define GPIO_MOTO_DOWN_2 3
void __configOutput()

{
	pinMode(GPIO_MOTO_UP_1, OUTPUT);
	pinMode(GPIO_MOTO_DOWN_1, OUTPUT);
	pinMode(GPIO_MOTO_UP_2, OUTPUT);
	pinMode(GPIO_MOTO_DOWN_2, OUTPUT);
}

void __stop() {
	digitalWrite(GPIO_MOTO_UP_1, 0);
	digitalWrite(GPIO_MOTO_DOWN_1, 0);
	digitalWrite(GPIO_MOTO_UP_2, 0);
	digitalWrite(GPIO_MOTO_DOWN_2, 0);
}
void __up() {
	__stop();
	digitalWrite(GPIO_MOTO_UP_1, 1);
	digitalWrite(GPIO_MOTO_UP_2, 1);
}
void __down() {
	__stop();
	digitalWrite(GPIO_MOTO_DOWN_1, 1);
	digitalWrite(GPIO_MOTO_DOWN_2, 1);
}
void __left() {
	__stop();
	digitalWrite(GPIO_MOTO_UP_1, 1);
//        digitalWrite(GPIO_MOTO_UP_2,1);
}
void __right() {
	__stop();
//        digitalWrite(GPIO_MOTO_UP_1,1);
	digitalWrite(GPIO_MOTO_UP_2, 1);
}
void __getkey() {
	char key;
	printf("w up , s down ,a left,d right , e to exit\n:");
	while (1) {
		key = getch();

		if (key == 'w')
			__up();
		else if (key == 's')
			__down();
		else if (key == 'd')
			__right();
		else if (key == 'a')
			__left();
		else if (key == ' ')
			__stop();
		else if (key == 'e') {
			__stop();
			break;
		}
	}
}
void __control(char key) {
	if (key == 'u')
		__up();
	else if (key == 'd')
		__down();
	else if (key == 'r')
		__right();
	else if (key == 'f')
		__left();
	else if (key == 's')
		__stop();
	else if (key == 'e') {
		__stop();
	}
}
int __main(void) {
	printf("Raspberry Pi blink\n");

	if (wiringPiSetup() == -1)
		return 1;

	__configOutput();

	__getkey();
	return 0;
}
