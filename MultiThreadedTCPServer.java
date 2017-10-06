package com.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedTCPServer {

	/* max thread number for threadpool */
	private static int maxclientnum = 10;

	public static ExecutorService TCP_WORKER_SERVICE = Executors.newFixedThreadPool(maxclientnum);

	public static void main(String args[]) {
		try {
			
			

			//////////////////////////////////////////////////////////////////////////////////////////////
			/* create Socket */
			ServerSocket socket = new ServerSocket(90);
			//////////////////////////////////////////////////////////////////////////////////////////////
			
			

			/* Connection Establishment Message */
			System.out.println("Server listening to: " + socket.getInetAddress() + ":" + socket.getLocalPort());

			while (true) {
				
				

				//////////////////////////////////////////////////////////////////////////////////////////////
				/* server accepts client */
				Socket client = socket.accept();
				//////////////////////////////////////////////////////////////////////////////////////////////
				
				

				/* Submit client to a thread */
				TCP_WORKER_SERVICE.submit(new TCPWorker(client));
				
				if(client.isClosed()){
					socket.close();
					
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
