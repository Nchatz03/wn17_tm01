package com.tcp.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerGlobal {

	/*
	 * The number of repetitions that given as a parameter to the server through
	 * the command line
	 */
	static int REQUEST_LIMIT;

	/*
	 * The server port that given to it as a command line argument
	 */
	static int PORT;

	/* The number of clients submitted to server */
	static int CLIENT_SUBMIT = 0;

	/* The number of clients fully served from client */
	static int CLIENT_SERVED = 0;

	/* The time server initialize */
	static long SERVER_INIT_TIME;

	/* The maximum thread server can served */
	static int MAXTHREAD = 10;

	/* The total number of request reached server */
	static int REQUESTS_REACHED = 0;

	/* throughput counter that increments every second server runs */
	static int THROUGHPUT_MILESTONE = 0;

	/*Flag for limit reaching*/
    static boolean LIMITFLAG;
   
    /*Maximum request that server can satisfy per client 300*/
    static int CLIENT_MAX_REQUEST = 300;

	static void getServerAnalytics() throws IOException {

		
		FileWriter fileWriter = new FileWriter("Server_Analysis.txt",true);
		PrintWriter file = new PrintWriter(fileWriter);
		file.write("------------------------------------------------------------------------------\n");
		file.write("|                              Server Analysis                               |\n");
		file.write("------------------------------------------------------------------------------\n");
		file.write("\n");
		file.write("\n");
		file.printf("------------------------------------------------------------------------------\n");
		file.printf("Server listening Port                     : %d\n",PORT);
		file.printf("Server request Limit                      : %d REQUESTS\n",REQUEST_LIMIT);
		file.printf("Maximum request per Client                : %d REQUESTS\n",CLIENT_MAX_REQUEST);
		file.printf("Clients submitted to the server           : %d CLIENTS\n",CLIENT_SUBMIT);
		file.printf("Total of Clients fully served by Server   : %d CLIENTS\n",CLIENT_SERVED);
		file.printf("Server's maximum number of threads        : %d THREADS\n",MAXTHREAD);
		file.printf("Total Client's requests Server satisfy    : %d REQUESTS\n",REQUESTS_REACHED );
		file.printf("Total time Server Used (Seconds)          : %d SECONDS\n",THROUGHPUT_MILESTONE);
		file.printf("Server's Throughput (Requests per Second) : %f REQUESTS/SECONDS\n",(double)(REQUESTS_REACHED/THROUGHPUT_MILESTONE));
		file.printf("Server reach Limit                        : %s\n",LIMITFLAG);
		file.printf("\n");
		file.printf("------------------------------------------------------------------------------\n");
		file.close();
		CLIENT_SUBMIT = 0;
		CLIENT_SERVED = 0;
		SERVER_INIT_TIME =0;
		REQUESTS_REACHED = 0;
		THROUGHPUT_MILESTONE = 0;
		LIMITFLAG = false;
		
	}

}
