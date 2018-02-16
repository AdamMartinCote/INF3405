package client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import utils.Utils;

public class Client {
	private final static String FILE_PATH = "/home/void/code/tp/INF3405/TP2/src/lassonde.jpg";

    private BufferedReader in;
    private PrintWriter out;

    
    Client() {

    }

    @SuppressWarnings("resource")
	public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        String serverAddress = Utils.getValidIpFromUser();
        int port = Utils.getValidPortFromUser();

        Socket socket;
		socket = new Socket(serverAddress, port);
		
        System.out.format("Connected to %s:%d%n", serverAddress, port);
        
//        in = new BufferedReader(
//                new InputStreamReader(socket.getInputStream()));
//        out = new PrintWriter(socket.getOutputStream(), true);

    }

    public static void main(String[] args) throws Exception {
    	
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        
        Client client = new Client();
        try {
        	client.connectToServer();
        } catch(Exception e) {
        	System.out.println("Error while connecting -- " + e.getMessage());
        }
//        System.out.println("Please select a file");
        File imageToSend = new File(FILE_PATH);
        
        byte [] mybytearray  = new byte [(int)imageToSend.length()];
        fis = new FileInputStream(imageToSend);
        bis = new BufferedInputStream(fis);
        bis.read(mybytearray,0,mybytearray.length);
    }
}
