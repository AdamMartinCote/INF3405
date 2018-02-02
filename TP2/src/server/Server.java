/**
 * @author Adam Martin-Côté et Laurent Pepin
 */
package server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Server {

	private static Scanner reader = new Scanner(System.in);

	public static void main(String[] args) throws Exception {

		String serverIp = getValidIpFromUser();
		int port = getValidPortFromUser(); // CRASH

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

	private static String getValidIpFromUser() {
		final Pattern IP_PATTERN = Pattern
				.compile("^(([012]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

		String serverIp = null;
		do {
			System.out.println("Enter server IP address: ");
			serverIp = reader.nextLine();
		} while (!IP_PATTERN.matcher(serverIp).matches());

		return serverIp;
	}

	private static int getValidPortFromUser() {
		int serverPort = 0;
		do {
			System.out.println("Enter server Port number NOW: ");
			String tmp = reader.nextLine();
			try {
				serverPort = Integer.parseInt(tmp);
			} catch (NumberFormatException e) {
				System.out.println("ERROR: invalid port number format -- " + e.getMessage());
			} catch (Exception e) {
				System.out.println("ERROR: unknown error -- " + e.getMessage());
			}
		} while (serverPort < 5000 || serverPort > 5050);

		return serverPort;
	}
}
