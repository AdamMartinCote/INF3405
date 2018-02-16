package commun;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Utils {
    private static Scanner scanner = new Scanner(System.in);
    
	public static String getValidIpFromUser() {
		final Pattern IP_PATTERN = Pattern
				.compile("^(([012]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

		String serverIp = null;
		do {
			System.out.println("Enter server IP address: ");
			serverIp = scanner.nextLine();
		} while (!IP_PATTERN.matcher(serverIp).matches());

		return serverIp;
	}

	public static int getValidPortFromUser() {
		int serverPort = 0;
		do {
			System.out.println("Enter server Port number (5000-5050): ");
			String tmp = scanner.nextLine();
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
	
	public static String getUsername() {
        System.out.print("Enter username: ");
        return scanner.nextLine();
	}
	
	public static String getPassword() {
        System.out.print("Enter password: ");
        return scanner.nextLine();
	}
	
	
	public static String readNextLineFromSocket(BufferedReader in) throws IOException {
		while (!in.ready()) {}
		return in.readLine();
	}
}
