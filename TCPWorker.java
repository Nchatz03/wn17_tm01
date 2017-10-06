package com.tcp.server;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class TCPWorker implements Runnable {

	private Socket client;
	private String clientbuffer;

	public TCPWorker(Socket client) {
		this.client = client;
		this.clientbuffer = "";
	}

	/* throughput counter */
	public static int throughput = 0;

	/* Initialize stopwatch in order to count the throughput per second */
	Stopwatch watch = new Stopwatch();

	@Override
	public void run() {

		try {

			/* message for client connection */
			System.out.println("Client connected with: " + this.client.getInetAddress());
			
			

            ///////////////////////////////////////////////////////////////////////////////////////////////////
			/* creating input/output stream */
			DataOutputStream output = new DataOutputStream(client.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			///////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			
			while (true) {

				/*
				 * saving the client message in a seperate object for each
				 * request
				 */
				this.clientbuffer = reader.readLine();

				/* if message is null disconnect client */
				if (this.clientbuffer != null) {

					/* increment throughput counter */
					throughput++;

					/*
					 * if stopwatch reach 1 second reinitialize counter and
					 * watch
					 */
					if (watch.elapsedTime() >= 1) {
						System.out.println("throughput = " + throughput + "requests per second");
						throughput = 0;
						watch = new Stopwatch();
					}

					/* print clients message */
					System.out.println(this.clientbuffer);

					/* tokenize client message base to whitespace */
					String[] tokens = this.clientbuffer.split("\\s");

					/* welcome message */
					String response = "WELCOME ";

					/* host name */
					String hostname = tokens[3];

					/* create randomized payload base to system time */
					Random randomnum = new Random(System.nanoTime());

					/* Bound payload in a range of 300 to 2000 KB */
					int payloadnumKB = randomnum.nextInt((2000 - 300) + 1) + 300;

					/* Convert KB to B */
					int payloadnumB = payloadnumKB * 1;

					/* create StringByulider */
					System.out.println(payloadnumB);
					StringBuilder payload = new StringBuilder();

					/* Use append to attach payload to response String */
					for (int i = 0; i < payloadnumB; i++) {
						payload.append('/'); //
					}

					/* send response message to client */
					output.writeBytes(response + hostname + " " + payload + System.lineSeparator());

				}					

			}
		

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}