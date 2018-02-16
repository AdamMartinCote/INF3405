package client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import commun.Message;
import utils.Utils;

public class Client {
	private final static String FILE_PATH = "src/resources/lassonde.jpg";

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    
    Client() {

    }

    @SuppressWarnings("resource")
	public void connectToServer() throws IOException {

        // Get the server address from a dialog box.
        String serverAddress = Utils.getValidIpFromUser();
        int port = Utils.getValidPortFromUser();

		socket = new Socket(serverAddress, port);
		
        System.out.format("Connected to %s:%d%n", serverAddress, port);
        
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
        
		InputStream inputStream = client.socket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		PrintWriter out = new PrintWriter(client.socket.getOutputStream(), true);

		String username = Utils.getUsername();
		String pwd = Utils.getPassword();
		
//		while (true){
//			username = in.readLine();
//			String password = in.readLine();
//			if (this.credentialsMatch(username, password)) {
//				out.println(Message.LOGIN_SUCCESS);
//				break;
//			} else {
//				out.println(Message.LOGIN_FAIL);
//			}
//		}
        
        
//        File imageToSend = new File(FILE_PATH);
//
//        byte [] mybytearray  = new byte [(int)imageToSend.length()];
//        fis = new FileInputStream(imageToSend);
//        bis = new BufferedInputStream(fis);
//        bis.read(mybytearray,0,mybytearray.length);
//        
//        bis.close();
    }
}
