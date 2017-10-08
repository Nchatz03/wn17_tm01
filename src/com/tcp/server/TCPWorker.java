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
import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class has the server TCPWorker that each on represents a client/thread
 * working on server
 * 
 * @author Nikolas Chatzigiannis nchatz03@cs.ucy.ac.cy
 * @version 1
 * @since 1
 */
public class TCPWorker implements Runnable {

	/**
	 * the socket that clients is connected
	 */
	private Socket client;
	/**
	 * the clients buffer that contains the message/request
	 */
	private String clientbuffer;
	/**
	 * the clients user_id/thread_id
	 */
	private int clientid;

	/**
	 * The constructor of a TCPWorker on server
	 * 
	 * @param client
	 *            the socket
	 * @param clientid
	 *            the thread_id/User id
	 */
	public TCPWorker(Socket client, int clientid) {
		this.client = client;
		this.clientbuffer = "";
		this.clientid = clientid;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		int requests_per_client = 0;

		try {

			///////////////////////////////////////////////////////////////////////////////////////////////////
			/* message for client connection establisment */
			System.out.println("Client connected with: " + this.client.getInetAddress());
			///////////////////////////////////////////////////////////////////////////////////////////////////

			///////////////////////////////////////////////////////////////////////////////////////////////////
			/* creating input/output stream */
			DataOutputStream output = new DataOutputStream(client.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			///////////////////////////////////////////////////////////////////////////////////////////////////

			/*********************************************************
			 * PHASE ONE: SERVER REICIVING REQUEST AND SENDING RESPONSE
			 *********************************************************/
			///////////////////////////////////////////////////////////////////////////////////////////////////

			while (true) {

				/*
				 * saving the client message in a seperate object for each
				 * request
				 */

				this.clientbuffer = reader.readLine();

				/* check if message is not null */
				if (this.clientbuffer != null) {

					/*
					 * mutex enclosure in order to protect the procedure of
					 * receiving a request from the client till the time it send
					 * the response
					 * 
					 */
					final Lock _mutex = new ReentrantLock(true);

					_mutex.lock();

					/* throughput coutner and reseting the clock */
					if (System.currentTimeMillis() - ServerGlobal.SERVER_INIT_TIME > 1000) {
						ServerGlobal.THROUGHPUT_MILESTONE++;
						ServerGlobal.SERVER_INIT_TIME = System.currentTimeMillis();
						ServerGlobal.MEMUTIL = ServerGlobal.MEMUTIL + Runtime.getRuntime().freeMemory()/Runtime.getRuntime().totalMemory();
					}

					/* print clients message to server console */
					System.out.println(this.clientbuffer);

					/* create randomized payload base to system time */
					Random randomnum = new Random(System.nanoTime());

					/* Bound payload in a range of 300 to 2000 KB */
					int payloadnumKB = randomnum.nextInt((2000 - 300) + 1) + 300;

					/* Convert KB to B */
					int payloadnumB = payloadnumKB * 1024;

					/* create StringByulider */
					System.out.println(payloadnumB);
					StringBuilder payload = new StringBuilder();

					/* Use append to attach payload to response String */
					for (int i = 0; i < payloadnumB; i++) {
						payload.append('/');
					}
					
					
					/* if limit reach stop all clients */
					if (ServerGlobal.REQUESTS_REACHED >= ServerGlobal.REQUEST_LIMIT) {
						ServerGlobal.LIMITFLAG = true;
						output.writeBytes("Limit_Reached" + System.lineSeparator());
						break;
					/* if a client reach his maximum request limit terminates */
					} else if (requests_per_client == ServerGlobal.CLIENT_MAX_REQUEST) {
						ServerGlobal.CLIENT_SERVED++;
						output.writeBytes("Limit_Reached" + System.lineSeparator());
						break;
					} else {
						
					/* increment counters respectively */
						requests_per_client++;
						ServerGlobal.REQUESTS_REACHED++;
						output.writeBytes(
								"WELCOME " + "user_" + this.clientid + " " + payload + System.lineSeparator());
					}
					
					/* mutex release */
					_mutex.unlock();
				}

			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////

			
			/******************************************
			 * PHASE TWO: PREPARE SERVERS ANALYTICS
			 ******************************************/
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ServerGlobal.CLIENT_SERVED >= ServerGlobal.CLIENT_SUBMIT || ServerGlobal.LIMITFLAG == true) {
				try {

					ServerGlobal.getServerAnalytics();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////


	}

}