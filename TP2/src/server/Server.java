/**
 * @author Adam Martin-C�t� et Laurent Pepin
 */
package server;

import commun.Message;
import commun.Utils;
import server.Sobel;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Server {

	private static Scanner reader = new Scanner(System.in);
	private static int nClients = 0;
	
	public static void main(String[] args) throws Exception {
		
//		String serverIp = Utils.getValidIpFromUser();
//		int port = Utils.getValidPortFromUser();
		String serverIp = "10.200.12.249";
		int port = 5005;
		ServerSocket listener;
		InetAddress locIP = InetAddress.getByName(serverIp);
		listener = new ServerSocket();
		listener.setReuseAddress(true);
		try {
			listener.bind(new InetSocketAddress(locIP, port));
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			System.out.println("Ending program");
			listener.close();
			reader.close();
			return;
		}
		
		// Server started
		reader.close();
		System.out.format("Out program server is running on %s:%d%n", serverIp, port);
		
		// Wait for clients to connect
		try {
			while (true) {
				Socket socket = listener.accept();
				nClients++;
				new ServerThread(socket, nClients).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class ServerThread extends Thread {
		private Socket socket;
		private int clientNumber;
		private String credentialsFilePath;
		public ServerThread(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			this.credentialsFilePath = "credentials.txt";
			System.out.println("New connection: client #" + this.clientNumber);
		}
		
		public void run() {
			try {
				// Client is connected
				InputStream inputStream = this.socket.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
				PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
				DataInputStream dis = new DataInputStream(this.socket.getInputStream());
				String username = null;
				while (true){
					username = Utils.readNextLineFromSocket(in);
					String password = Utils.readNextLineFromSocket(in);
					if (this.credentialsMatch(username, password)) {
						out.println(Message.LOGIN_SUCCESS);
						break;
					} else {
						out.println(Message.LOGIN_FAIL);
					}
				}
				
				// Transform pictures
				while (true) {
					String fileName = Utils.readNextLineFromSocket(in);
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss");
					Date date = new Date();
					System.out.println("[" + username + " - " + this.socket.getInetAddress().toString() + ":" + this.socket.getPort() 
						+ " - " + dateFormat.format(date) + "] : " +  fileName);
					
					// Get file size
					System.out.println("before");
					int fileSize = Integer.parseInt(Utils.readNextLineFromSocket(in));
					System.out.println("Client: " + this.clientNumber + " File size = " + fileSize);
					
					
					//dis = new DataInputStream(this.socket.getInputStream());
					byte[] data = new byte[fileSize];
				    dis.readFully(data);
				    // Never going here
				    System.out.println("here");
				    ByteArrayInputStream ian = new ByteArrayInputStream(data);
				    BufferedImage bImage = ImageIO.read(ian);
				    System.out.println("here");
				    
				    // Save locally for test purpose
				    File image = new File("test.jpg");
				    ImageIO.write(bImage, "jpg", image);
				    dis.close();
				    System.out.println("end");
					break;
				}
			} catch (IOException e) {
				System.out.println("Error with client #" + this.clientNumber + " : " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Closing socket failed for client #" + this.clientNumber);
				}
			}
		}
		
		@SuppressWarnings("finally")
		private boolean credentialsMatch(String username, String password) {
			File file = new File(this.credentialsFilePath);
			if (file.exists()) {
				// Check for username
				Scanner input = null;
				try {
					input = new Scanner(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				String line;
				while (input.hasNextLine()) {
					line = input.nextLine();
					if (line.compareTo(username) == 0) {
						line = input.nextLine();
						// Return true if password match username, false if not
						return (line.compareTo(password) == 0);
					}
				}
				// New credentials
				return this.insertNewCredentials(username, password);
			} else {
				try {
					file.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
					return false;
				} finally {
					// Create fist credentials
					return this.insertNewCredentials(username, password);
				}
			}
		}
		
		private boolean insertNewCredentials(String username, String password) {
			Writer output;
			try {
				output = new BufferedWriter(new FileWriter(this.credentialsFilePath, true));
				output.append(username + "\r\n");
				output.append(password + "\r\n");
				output.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}

