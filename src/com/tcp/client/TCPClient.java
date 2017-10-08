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
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class has is the TCPClient that sending request to the server and
 * waiting for response when the clients request limit reached the client
 * disconnect and create analysis
 * 
 * @author Nikolas Chatzigiannis nchatz03@cs.ucy.ac.cy
 * @version 1
 * @since 1
 */
public class TCPClient {

	/**
	 * Inner class of client extended by thread to create threads an implements
	 * runnable to use the threads
	 * 
	 * @author Nikolas Chatzigiannis nchatz03@cs.ucy.ac.cy
	 * @version 1
	 * @since 1
	 */
	public static class Client extends Thread implements Runnable {

		/**
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			try {

				//////////////////////////////////////////////////////////////////////////////////////////////
				/* opening the socket */
				String message, response;
				@SuppressWarnings("resource")
				Socket socket = new Socket(ClientGlobal.INSTANCE_IP, ClientGlobal.PORT);
				//////////////////////////////////////////////////////////////////////////////////////////////

				//////////////////////////////////////////////////////////////////////////////////////////////
				/* creating input/output stream */
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				BufferedReader server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//////////////////////////////////////////////////////////////////////////////////////////////

				//////////////////////////////////////////////////////////////////////////////////////////////
				/*
				 * mutex enclosure in order to protect id of the user from
				 * interruption
				 */
				final Lock _mutexClientID = new ReentrantLock(true);

				_mutexClientID.lock();
				int id = ClientGlobal.ID;
				ClientGlobal.ID++;

				/* mutex release */
				_mutexClientID.unlock();
				//////////////////////////////////////////////////////////////////////////////////////////////

				while (true) {

					/**************************************
					 * PHASE ONE: CLIENT SENDING REQUEST
					 **************************************/
					//////////////////////////////////////////////////////////////////////////////////////////////
					/*
					 * mutex enclosure in order to protect the procedure of
					 * sending a request to server
					 */
					final Lock _mutex = new ReentrantLock(true);
					_mutex.lock();

					/* construction of request message */
					message = "HELLO " + Inet4Address.getLocalHost().getHostAddress() + " " + ClientGlobal.PORT + " "
							+ "user_" + id + " " + System.lineSeparator();

					/* start latency stopwatch */
					double time_request = System.currentTimeMillis();

					/* sending request to server */
					output.writeBytes(message);

					/* mutex release */
					_mutex.unlock();
					//////////////////////////////////////////////////////////////////////////////////////////////

					/***************************************
					 * PHASE TWO: CLIENT RECEIVONG RESPONSE
					 ****************************************/
					//////////////////////////////////////////////////////////////////////////////////////////////
					/*
					 * mutex enclosure in order to protect the procedure of
					 * sending a request to server
					 */
					final Lock _mutex1 = new ReentrantLock(true);

					_mutex1.lock();

					/* client reading servers response */
					response = server.readLine();

					/* the time that response come to client */
					double time_response = System.currentTimeMillis();

					/* Total Latency sum up */
					ClientGlobal.LATENCY_TOTAL = ClientGlobal.LATENCY_TOTAL + ((time_response - time_request) / 1000);

					ClientGlobal.TOTAL_REQUEST_COMPLETE++;

					/*
					 * if the request number reach maximum request number client
					 * socket close
					 */
					if (response.equals("Limit_Reached")) {
						ClientGlobal.CLIENTS_SERVED++;
						break;
					} else {
						/* Print response to client console */
						System.out.println(response);
					}

					/* mutex release */
					_mutex1.unlock();
					//////////////////////////////////////////////////////////////////////////////////////////////

				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				/***************************************
				 * PHASE THREE: CLIENT PREPARE ANALYTICS
				 ***************************************/
				////////////////////////////////////////////////////////////////////////////////////////////
				if (ClientGlobal.CLIENTS_SERVED == ClientGlobal.CLIENTS_CONNECTED) {

					try {
						ClientGlobal.getClientsAnalytics();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				////////////////////////////////////////////////////////////////////////////////////////////
			}

		}

	}

	/**
	 * the main method of the client class that creates threads from command
	 * line the arguments given is the instance ip and the port of server that
	 * client has to connect
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {

		ClientGlobal.INSTANCE_IP = args[0];
		ClientGlobal.PORT = Integer.parseInt(args[1]);

		for (int i = 0; i < ClientGlobal.CLIENTS_CONNECTED; i++) {
			Client t = new Client();
			t.start();

		}

	}

}
