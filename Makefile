all:
	#gcc -o main main.c -lwiringPi
	gcc -o server_udp server_udp.c -lwiringPi
clean:
	@rm -rf main
