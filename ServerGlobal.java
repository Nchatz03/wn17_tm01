/**Copyright [2017] [Nikolas Chatzigiannis]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */

package com.tcp.server;

//import statements
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class has the global variables,flags and analysis method of server and
 * tcpWorker
 * 
 * @author Nikolas Chatzigiannis nchatz03@cs.ucy.ac.cy
 * @version 1
 * @since 1
 */
public class ServerGlobal {

	/**
	 * The number of repetitions that given as a parameter to the server through
	 * the command line
	 */
	static int REQUEST_LIMIT;

	/**
	 * The server port that given to it as a command line argument
	 */
	static int PORT;

	/** The number of clients submitted to server */
	static int CLIENT_SUBMIT = 0;

	/** The number of clients fully served from client */
	static int CLIENT_SERVED = 0;

	/** The time server initialize */
	static long SERVER_INIT_TIME;

	/** The maximum thread server can served */
	static int MAXTHREAD = 10;

	/** The total number of request reached server */
	static int REQUESTS_REACHED = 0;

	/** throughput counter that increments every second server runs */
	static int THROUGHPUT_MILESTONE = 0;

	/** Flag for limit reaching */
	static boolean LIMITFLAG;

	/** Maximum request that server can satisfy per client 300 **/
	static int CLIENT_MAX_REQUEST = 300;
	
	/** Memory utilazation **/
	static long MEMUTIL =0;
	
	/**
	 * Server analysis method 
	 * <p>
	 * This method creates a file containing server's analysis
	 * and reinitialize the global variables and flags to keep
	 * server alive.
	 * 
	 *@exception IOException if file exception
	 *
	 */
	static void getServerAnalytics() throws IOException {

		FileWriter fileWriter = new FileWriter("Server_Analysis.txt", true);
		PrintWriter file = new PrintWriter(fileWriter);
		file.write("------------------------------------------------------------------------------\n");
		file.write("|                              Server Analysis                               |\n");
		file.write("------------------------------------------------------------------------------\n");
		file.write("\n");
		file.write("\n");
		file.printf("------------------------------------------------------------------------------\n");
		file.printf("Server listening Port                     : %d\n", PORT);
		file.printf("Server request Limit                      : %d REQUESTS\n", REQUEST_LIMIT);
		file.printf("Maximum request per Client                : %d REQUESTS\n", CLIENT_MAX_REQUEST);
		file.printf("Clients submitted to the server           : %d CLIENTS\n", CLIENT_SUBMIT);
		file.printf("Total of Clients fully served by Server   : %d CLIENTS\n", CLIENT_SERVED);
		file.printf("Server's maximum number of threads        : %d THREADS\n", MAXTHREAD);
		file.printf("Total Client's requests Server satisfy    : %d REQUESTS\n", REQUESTS_REACHED);
		file.printf("Total time Server Used (Seconds)          : %d SECONDS\n", THROUGHPUT_MILESTONE);
		file.printf("Server's Throughput (Requests per Second) : %d REQUESTS/SECONDS\n",
				(int) (REQUESTS_REACHED / THROUGHPUT_MILESTONE));
		file.printf("Server reach Limit                        : %s\n", LIMITFLAG);
		file.printf("Server memory utilazation                 : %d\n", (int) (MEMUTIL / THROUGHPUT_MILESTONE));
		file.printf("\n");
		file.printf("------------------------------------------------------------------------------\n");
		file.close();
		CLIENT_SUBMIT = 0;
		CLIENT_SERVED = 0;
		SERVER_INIT_TIME = 0;
		REQUESTS_REACHED = 0;
		THROUGHPUT_MILESTONE = 0;
		LIMITFLAG = false;

	}

}
