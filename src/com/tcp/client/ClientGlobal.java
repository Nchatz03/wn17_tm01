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
package com.tcp.client;

//import statements
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;

/**
 * @author NXATZ
 *
 */
public class ClientGlobal {

	/**
	*  The servers ip
	*/
	static String INSTANCE_IP;

	/**
	* the port client has to connect
	*/
	static int PORT;

	/**
	* total request of all clients commit
	*/
	static int TOTAL_REQUEST_COMPLETE = 0;

	/**
	* clients connected to server 
	*/
	static int CLIENTS_CONNECTED = 5;
	
	/**
	 * clients that fully served
	 */
	static int CLIENTS_SERVED = 0;
	/**
	*  The total latency of all clients and requests
	*/
	static double LATENCY_TOTAL;
	
	/**
	 * clients thread id / user id
	 */
	public static int ID = 1;

	/**
	 * Clients analysis method 
	 * <p>
	 * This method creates a file containing client's analysis
	 * 
	 * @throws IOException for file 
	 */
	static void getClientsAnalytics() throws IOException {

		FileWriter fileWriter = new FileWriter("Clients_Analysis.txt", true);
		PrintWriter file = new PrintWriter(fileWriter);
		file.write("------------------------------------------------------------------------------\n");
		file.write("|                              Clients Analysis                              |\n");
		file.write("------------------------------------------------------------------------------\n");
		file.write("\n");
		file.write("\n");
		file.printf("------------------------------------------------------------------------------\n");
		file.printf("Client connects to Port                            : %d \n", PORT);
		file.printf("Clients IP Address                                 : %s \n", Inet4Address.getLocalHost().getHostAddress());
		file.printf("Servers IP Address                                 : %s \n", INSTANCE_IP);
		file.printf("Total request Complete from all clients            : %d REQUESTS\n", TOTAL_REQUEST_COMPLETE - CLIENTS_CONNECTED);
		file.printf("Latency total (Seconds)                            : %f SECONDS\n", LATENCY_TOTAL);
		file.printf("Average Latency of a request (Seconds per Request) : %f REQUESTS/SECONDS\n", (double) (LATENCY_TOTAL / TOTAL_REQUEST_COMPLETE));
		file.printf("\n");
		file.printf("------------------------------------------------------------------------------\n");
		file.close();


	}

}
