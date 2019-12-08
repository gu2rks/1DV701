import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	private static final int BUFSIZE = 3;
	private static final int MYPORT = 8888;
	static ServerSocket socket;
	public static void main(String[] args) {
		byte[] buf = new byte[BUFSIZE];

		try {
			/* Create socket */
			socket = new ServerSocket(MYPORT);
		} catch (IOException e) {
			System.out.println("## Error: port taken, pls use another port\n" + e.getMessage());
		}

		System.out.println("TCP server is running...");
		while (true) {
			try {
				Socket listen = socket.accept();
				new Thread(new ClientHandler(listen, buf)).start();
			} catch (IOException e) {
				System.out.println("## Error: occurs when waiting for a connection. \n" + e.getMessage());
			}
		}
	}

}

class ClientHandler implements Runnable {

	private Socket socket;
	private byte[] buf;
	DataInputStream input;
	DataOutputStream output;


	public ClientHandler(Socket listen, byte[] buf) {
		socket = listen;
		this.buf = buf;
	}

	public void start() {
		/*
		 * create input stream for reading massage, output stream for sending massage
		 */

		
		String message = "";
		do{
			StringBuilder receiveData = new StringBuilder();
			String fragments = "";

			try {  //READ A MESSAGE
				do {

					int byteToRead = input.read(buf, 0, buf.length);
					fragments = new String(buf, 0, byteToRead); // create fragments of recived message
					receiveData.append(fragments); //build up the whole message
					
				} while (input.available() > 0); // while is any data to read

			} catch (IOException e) {
				System.out.println("### ERROR: occour when reading data from datastream");
			}

			message = receiveData.toString(); 
			// if (message.length() < 1)
			// 	break;

			try {// Send message back to client
			output.write(message.getBytes());
			}catch (IOException e) {
				System.out.println("### ERROR: occour when writing message to datastream");
			}

		} while(!message.isEmpty());

		try { //reset everything by closing a socket
			socket.close();
		} catch (IOException e) {
			System.out.println("### ERROR: occurs when closing this socket.");
		}
	}

	@Override
	public void run() {
		try {
		
		System.out
				.println("Accepted connection from " + socket.getInetAddress().getHostAddress() + " User id " + Thread.currentThread().getId());
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
					System.out.println("##ERROR: socket is not connected.");
		}

		start();
		System.out.println("Closed conection from " + socket.getInetAddress().getHostAddress() + " User id "
				+ Thread.currentThread().getId());
			
		} catch (Exception e) {
			System.out.printf("\n### ERROR: connection is closed by client side (IP:%s)",socket.getInetAddress().getHostAddress());
		}
	}
}