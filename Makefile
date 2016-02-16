all:
	gcc -o main main.c -lwiringPi
clean:
	@rm -rf main
