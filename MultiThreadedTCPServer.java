package com.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedTCPServer {

	/* max thread number for threadpool */

	public static ExecutorService TCP_WORKER_SERVICE = Executors.newFixedThreadPool(ServerGlobal.MAXTHREAD);

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

			/* Connection Establishment Message */
			System.out.println("Server listening to: " + socket.getInetAddress() + ":" + ServerGlobal.PORT);

			long initiallize = System.currentTimeMillis();
			ServerGlobal.SERVER_INIT_TIME = initiallize;
			while (true) {

				//////////////////////////////////////////////////////////////////////////////////////////////
				/* server accepts client */
				Socket client = socket.accept();
				//////////////////////////////////////////////////////////////////////////////////////////////

				/* Submit client to a thread */
				TCP_WORKER_SERVICE.submit(new TCPWorker(client));
				ServerGlobal.CLIENT_SUBMIT++;

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
