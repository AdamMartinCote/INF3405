/**
 * @author Adam Martin-C�t� et Laurent Pepin
 */
package server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;


import utils.Utils;

public class Server {

	private static Scanner reader = new Scanner(System.in);

	public static void main(String[] args) throws Exception {

		String serverIp = Utils.getValidIpFromUser();
		int port = Utils.getValidPortFromUser();

		ServerSocket listener;
		InetAddress locIP = InetAddress.getByName(serverIp);

		listener = new ServerSocket();
		listener.setReuseAddress(true);
		try {
			listener.bind(new InetSocketAddress(locIP, port));
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return;
		}
		System.out.format("Out program server is running on %s:%d%n", serverIp, port);

		try {
			while (true) {
				;
			}
		} finally {
			listener.close();
			// Tear down
			reader.close();
		}
	}
}

