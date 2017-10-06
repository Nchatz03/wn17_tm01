package com.tcp.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedTCPServer {

	private static class TCPWorker implements Runnable {

		private Socket client;
		private String clientbuffer;

		public TCPWorker(Socket client) {
			this.client = client;
			this.clientbuffer = "";
		}

		
		public static int throughput = 0;
        Stopwatch watch = new Stopwatch();
		@Override
		public void run() {

			try {
				System.out.println("Client connected with: " + this.client.getInetAddress());

				DataOutputStream output = new DataOutputStream(client.getOutputStream());
				BufferedReader reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
				

				while (client.isConnected()) {
					this.clientbuffer = reader.readLine();
					if (this.clientbuffer != null) {

						throughput++;
						if (watch.elapsedTime() >= 1) {
							System.out.println("throughput = " + throughput + "requests per second");
							throughput = 0;
							watch = new Stopwatch();
						}

						System.out.println(this.clientbuffer);

						String[] tokens = this.clientbuffer.split("\\s");
						String response = "WELCOME ";
						String hostname = tokens[3];
						Random randomnum = new Random(System.nanoTime());
						int payloadnumKB = randomnum.nextInt((2000 - 300) + 1) + 300;
						int payloadnumB = payloadnumKB * 1;  // convert
																// kilobytes
																// to bytes
						System.out.println(payloadnumB);
						StringBuilder payload = new StringBuilder();
						for (int i = 0; i < payloadnumB; i++) {
							payload.append('/'); //
						}
						output.writeBytes(response + hostname + " " + payload + System.lineSeparator());
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private static int maxclientnum = 10;

	public static ExecutorService TCP_WORKER_SERVICE = Executors.newFixedThreadPool(maxclientnum);

	public static void main(String args[]) {
		try {
			ServerSocket socket = new ServerSocket(90);

			System.out.println("Server listening to: " + socket.getInetAddress() + ":" + socket.getLocalPort());

			while (true) {
				Socket client = socket.accept();

				TCP_WORKER_SERVICE.submit(new TCPWorker(client));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
