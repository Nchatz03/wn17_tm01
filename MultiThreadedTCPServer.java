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
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class has the server ThreadPool establishing connection with clients and
 * accepting socket
 * 
 * @author Nikolas Chatzigiannis nchatz03@cs.ucy.ac.cy
 * @version 1
 * @since 1
 */
public class MultiThreadedTCPServer {

	/**
	 * ExecutorService for maintain the number of threads server can manage
	 */

	public static ExecutorService TCP_WORKER_SERVICE = Executors.newFixedThreadPool(ServerGlobal.MAXTHREAD);

	/**
	 * Servers main method
	 * 
	 * this method submit a new client to TCPWorker
	 * 
	 * @param args
	 *            - the command line arguments
	 */
	@SuppressWarnings("resource")
	public static void main(String args[]) {

		ServerGlobal.PORT = Integer.parseInt(args[0]);
		ServerGlobal.REQUEST_LIMIT = Integer.parseInt(args[1]);
		ServerGlobal.LIMITFLAG = false;

		try {

			//////////////////////////////////////////////////////////////////////////////////////////////
			/* create Socket */
			ServerSocket socket = new ServerSocket(ServerGlobal.PORT);
			//////////////////////////////////////////////////////////////////////////////////////////////

			System.out.println("Server listening to: " + socket.getInetAddress() + ":" + ServerGlobal.PORT);

			/** initialize time watch for server */
			long initiallize = System.currentTimeMillis();

			ServerGlobal.SERVER_INIT_TIME = initiallize;

			while (true) {

				//////////////////////////////////////////////////////////////////////////////////////////////
				/* server accepts client */
				Socket client = socket.accept();
				//////////////////////////////////////////////////////////////////////////////////////////////

				/* Submit client to a thread */
				ServerGlobal.CLIENT_SUBMIT++;
				TCP_WORKER_SERVICE.submit(new TCPWorker(client, ServerGlobal.CLIENT_SUBMIT));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
