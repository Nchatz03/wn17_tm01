package com.tcp.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.Date;
import java.util.Random;

public class TCPClient {

	public static void main(String args[]) {
		try {

			int requestcomplete = 0;
			int maxrequestnum = 300;

			String message, response;
			Socket socket = new Socket("localhost", 90);

			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			BufferedReader server = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (true) {

				String handshake = "HELLO " +requestcomplete+ " ";
				String ip = Inet4Address.getLocalHost().getHostAddress() + " ";
				String hostname = Inet4Address.getLocalHost().getHostName() + " ";
				String port = String.valueOf(socket.getPort()) + " ";

				message = handshake + ip + port + hostname + System.lineSeparator();

				Stopwatch comlatency = new Stopwatch();
				output.writeBytes(message);

				response = server.readLine();
				double latency = comlatency.elapsedTime();
				
				requestcomplete++;
				System.out.println(requestcomplete+" "+response);
				//System.out.println("latency =" + latency);

			
				if (requestcomplete == maxrequestnum) {
					socket.close();
					break;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
