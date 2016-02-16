/* UDP client in the internet domain */
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

void error(const char *);
int main(int argc, char *argv[]) {
	int sock, n;
	unsigned int length;
	struct sockaddr_in server, from;
	struct hostent *hp;
	char buffer[256];

	if (argc != 2) {
		printf("Usage: server port\n");
		exit(1);
	}
	while (1) {
		sock = socket(AF_INET, SOCK_DGRAM, 0);
		if (sock < 0)
			error("socket");

		server.sin_family = AF_INET;
		hp = gethostbyname(argv[1]);
		if (hp == 0)
			error("Unknown host");

		bcopy((char *) hp->h_addr, (char *) &server.sin_addr, hp->h_length);
		server.sin_port = htons(atoi("8888"));
		length = sizeof(struct sockaddr_in);
		printf("xDiag: ");
		bzero(buffer, 256);
		fgets(buffer, 255, stdin);
		n = sendto(sock, buffer, strlen(buffer), 0, (const struct sockaddr *) &server, length);
		if (n < 0)
			error("Sendto");

		do {
			n = recvfrom(sock, buffer, 256, 0, (struct sockaddr *) &from, &length);
			buffer[n] = '\0';
			printf("%s\n", buffer);
			if (strcmp(buffer, "Done") == 0)
				break;
		} while (1);
		close(sock);
	}
	return 0;
}

void error(const char *msg) {
	perror(msg);
	exit(0);
}
