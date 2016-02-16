all:
	gcc -o main main.c -lwiringPi
	gcc -o server_udp server_udp.c
clean:
	@rm -rf main
