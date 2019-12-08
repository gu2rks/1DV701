

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;


public class UDPServer {

	private static final int BUFSIZE = 1024;
	private static final int MYPORT = 4950;

	public static void main(String[] args) {
		byte[] buf = new byte[BUFSIZE];

		/* Create socket */		
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(null);
		} catch (SocketException e1) {
			System.out.println("Error initializing socket:"+e1.getMessage());
			e1.printStackTrace();
		}
		

		/* Create local bind point */
		SocketAddress localBindPoint = new InetSocketAddress(MYPORT);
		try {
			socket.bind(localBindPoint);
		} catch (SocketException e) {
			System.out.println("Error to bind the socket :"+e.getMessage());
			e.getStackTrace();
		}

		System.out.println("UDP server is running...");
		while (true) {
			/* Create datagram packet for receiving message */
			DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

			/* Receiving message */
			try {
				socket.receive(receivePacket);
			} catch (IOException e) {
				System.out.println("Error! unable to receive a packet from this socket:"+e.getMessage());
				e.printStackTrace();
			}

			/* Create datagram packet for sending message */
			DatagramPacket sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(),
					receivePacket.getAddress(), receivePacket.getPort());

			/* Send message */
			try {
				socket.send(sendPacket);
			} catch (IOException e) {
				System.out.println("Error! unable to send a packet from this socket:"+e.getMessage());
				e.printStackTrace();
				socket.close();
			}
			System.out.printf("UDP echo request from %s", receivePacket.getAddress().getHostAddress());
			System.out.printf(" using port %d\n", receivePacket.getPort());
		}
	}
}