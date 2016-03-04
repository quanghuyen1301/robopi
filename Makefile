all:
	#gcc -o main main.c -lwiringPi
	gcc -o server_udp server_udp.c -lwiringPi
	gcc -o rada rada.c -lwiringPi
clean:
	@rm -rf main
