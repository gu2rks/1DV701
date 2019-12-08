package trash;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.NotDirectoryException;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

public class TCPServer {
	private static final int BUFSIZE = 1024;
	private static final int MYPORT = 8888;
	private static int userId = 0;
	
	static ServerSocket socket;

	public static void main(String[] args) {
		byte[] buf = new byte[BUFSIZE];

		try {
			/* Create socket */
			socket = new ServerSocket(MYPORT);
		} catch (IOException e) {
			System.out.println("Error initializing socket \n" + e.getMessage());
		}

		System.out.println("Web server is up are runing... " + "\nListening for connection on port" + MYPORT
				+ "\nPress ctrl+c to terminate the program");
		while (true) {
			try {
				Socket listen = socket.accept();
				userId++;
				new Thread(new ClientHandler(listen, userId, buf)).start();
			} catch (IOException e) {
				System.out.println("Error: can't accept the connection \n" + e.getMessage());
				e.printStackTrace();
			}
		}

	}


}

class ClientHandler implements Runnable {

	private Socket socket;
	private int userId;
	private byte[] buf;
	private String path = "../resources/user1";
	private final String homePage = "index.html"; 
	public ClientHandler(Socket listen, int id, byte[] buf) {
		socket = listen;
		userId = id;
		this.buf = buf;
	}

	public void start() throws IOException {
		//input from client
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//binary
		BufferedOutputStream dataOut = new BufferedOutputStream(socket.getOutputStream());
		DataOutputStream  outToClient = new DataOutputStream(socket.getOutputStream());
		String clientInput = in.readLine();
		StringTokenizer split = new StringTokenizer(clientInput);
		String method = split.nextToken().toUpperCase(); //get a method
		path = path+split.nextToken().toLowerCase(); //path to requested file
		//check if path is valid
		System.out.println(path);
		directoryisValid(path);
	
		//read file
		File file = new File(path);
		StringBuilder fileContents = new StringBuilder();
	    try (Scanner scanner = new Scanner(file)) {
	        while(scanner.hasNextLine()) {
	            fileContents.append(scanner.nextLine() + System.lineSeparator());
	        }
	   
	    }
	    
	    //string
	    PrintWriter out = new PrintWriter(socket.getOutputStream());
		
//		int fileLength = (int) file.length();
//		byte[] fileData = new byte[fileLength];
//		char [] test = new char[(int) file.length()];
	    
	    //header
		out.println("HTTP/1.1 200 OK");
		out.println("Server: Java HTTP Server from SSaurel : 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: text/html");
		out.println("Content-length: " + file.length());
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer
		String favicon = "<link rel=\"icon\" href=\"data:,\">"; //prevent favicon.ico requests
//		outToClient.writeBytes(favicon+fileContents.toString()); 
		out.write(favicon+fileContents.toString());
//		dataOut.flush();
//		stringOut.print(fileContents);
//		stringOut.flush();
		socket.close();
	}

	@Override
	public void run() {
		System.out
				.println("Accepted connection from " + socket.getInetAddress().getHostAddress() + " User id " + userId);
		try {
			start();
			System.out.println(
					"Closed conection from " + socket.getInetAddress().getHostAddress() + " User id " + userId);
		} catch (IOException e) {
			System.out.println("Conection from " + socket.getInetAddress().getHostAddress()
					+ " is closed, cause by user side, User id: " + userId);
		}

	}
	
	public void directoryisValid(String path) {

		try {
			File directory = new File(path);// may throw nullpointerexception
				if (directory.exists() == false) 
					System.out.println("notexit");

			} catch (Exception e) {
			System.out.println("Error :"+e.getMessage());
			e.getStackTrace();
			System.exit(-1);
			}
	}

}