package com.tcp.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Socket;

public class TCPClient {

	public static void main(String args[]) {
		try {

			/* request of each client that complete */
			int requestcomplete = 0;

			/* maximum requests for each client */
			int maxrequestnum = 300;
			
			

			//////////////////////////////////////////////////////////////////////////////////////////////
			/* opening the socket */
			String message, response;
			Socket socket = new Socket("localhost", 90);
            //////////////////////////////////////////////////////////////////////////////////////////////
			
			//////////////////////////////////////////////////////////////////////////////////////////////
			/* creating input/output stream */
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			BufferedReader server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //////////////////////////////////////////////////////////////////////////////////////////////
			
			
			
			
			while (true) {

				/* hello message */
				String handshake = "HELLO " + requestcomplete + " ";

				/* device ip */
				String ip = Inet4Address.getLocalHost().getHostAddress() + " ";

				/* hostname */
				String hostname = Inet4Address.getLocalHost().getHostName() + " ";

				/* port */
				String port = String.valueOf(socket.getPort()) + " ";

				/* full message to stream to server */
				message = handshake + ip + port + hostname + System.lineSeparator();

				/*
				 * Initialization of stopwatch counting the latency of each
				 * request
				 */
				Stopwatch comlatency = new Stopwatch();

				/* sending request to server */
				output.writeBytes(message);

				/* client reading servers response */
				response = server.readLine();

				/* the time that response come to client */
				double latency = comlatency.elapsedTime();

				/* Increment request counter */
				requestcomplete++;

				/* Print response to client console */
				System.out.println(requestcomplete + " " + response);

				System.out.println("latency =" + latency);

				/*
				 * if the request number reach maximum request number client
				 * socket close
				 */
				if (requestcomplete == maxrequestnum) {
					
					output.close();
					server.close();
					socket.close();
					break;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
