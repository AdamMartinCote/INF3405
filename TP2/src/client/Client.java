package client;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.imageio.ImageIO;

import commun.Message;
import commun.Utils;

public class Client {
	private final static String ROOT_PATH = "./src/resources/";

    public BufferedReader in;
    public PrintWriter out;
    private Socket socket;
    public DataInputStream dataIn;
    public DataOutputStream dataOut;

    
    Client() {}

	public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
//        String serverAddress = Utils.getValidIpFromUser();
//        int port = Utils.getValidPortFromUser();
		String serverAddress = "10.200.12.249";
		int port = 5005;
		socket = new Socket(serverAddress, port);
		
        System.out.format("Connected to %s:%d%n", serverAddress, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.dataOut = new DataOutputStream(socket.getOutputStream());
		this.dataIn = new DataInputStream(socket.getInputStream());
    }

    public static void main(String[] args) throws Exception {
    	 
        Client client = new Client();
        try {
        	client.connectToServer();
        } catch(Exception e) {
        	System.out.println("Error while connecting -- " + e.getMessage());
        }
        
		String response;
		do {
			// String username = Utils.getUsername();
			// String pwd = Utils.getPassword();
			String username = "lp";
			String pwd = "lp";
			client.out.println(username);
			client.out.println(pwd);
		
			response = Utils.readNextLineFromSocket(client.in);
			if(Integer.parseInt(response) != Message.LOGIN_SUCCESS) {
				System.out.println("erreur dans la saisie du mot de passe");
			}
		} while (Integer.parseInt(response) != Message.LOGIN_SUCCESS);
		
		//System.out.println("Please name a file to send (will search \"resources\")");
		//String filename = Utils.getStringFromUser();
		String filename = "lassonde.jpg";
		client.out.println(filename);
		
		System.out.println("Sending file");
		File imageToSend = null;
		while (imageToSend == null) { // TODO still crash on wrong filename
			try {
				imageToSend = new File(filename);
			} catch(Exception e) {
				imageToSend = null;
				System.out.println("wrong filename");
			}
		}
	
		System.out.println("Sending file size");
        long fileSize = imageToSend.length();
		client.out.println(Long.toString(fileSize));
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //transform image buffer to bytes array with header for jpg 
        try {
        	BufferedImage image = ImageIO.read(imageToSend);
			ImageIO.write(image, "jpg", baos);
			baos.flush();
	        byte[] imageBytes = baos.toByteArray();
	        baos.close();
	        client.dataOut.write(imageBytes, 0, imageBytes.length);
	        System.out.println("Image envoyee au serveur");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (true) {}
    }
}
