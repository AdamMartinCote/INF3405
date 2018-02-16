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
import commun.Utils;

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

		String response;
		do {
			String username = Utils.getUsername();
			String pwd = Utils.getPassword();
			
			out.println(username);
			out.println(pwd);
		
			response = Utils.readNextLineFromSocket(in);
			if(Integer.parseInt(response) != Message.LOGIN_SUCCESS) {
				System.out.println("erreur dans la saisie du mot de passe");
			}
		} while (Integer.parseInt(response) != Message.LOGIN_SUCCESS);
		
		System.out.println("Sending file");
        File imageToSend = new File(FILE_PATH);

        
        System.out.println("image length: " + (int)imageToSend.length());
        byte [] mybytearray  = new byte [(int)imageToSend.length()];
        fis = new FileInputStream(imageToSend);
        bis = new BufferedInputStream(fis);
        bis.read(mybytearray,0,mybytearray.length);
        
        String text1 = new String(mybytearray, "UTF-8");
        char[] chars = text1.toCharArray();
        
        out.write(chars,0,chars.length);

		

        // Tear down
        bis.close();
    }
}
