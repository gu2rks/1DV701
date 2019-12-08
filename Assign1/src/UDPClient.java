import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPClient extends TransportLayer {

	public static final String MSG = "An Echo Message!";
	private DatagramSocket socket;
	private DatagramPacket sendPacket;
	private DatagramPacket receivePacket;
	private boolean work = true;

	public static void main(String[] args) {
		if (args.length != 4) {
			throw new IllegalArgumentException(
					String.format("Usage : java -cp . UDPEClinet IP PORT BUFFERSIZE TRANSFERRATE"));
		}
		UDPClient clinet = new UDPClient(args);
		clinet.run();
	}

	public UDPClient(String[] args) {
		super(args);
	}
	
	/*
	 * create socket and run the client
	 * @see NetworkLayer#run()
	 */
	@Override
	protected void run() {
		try {
			socket = new DatagramSocket(null);
			socket.bind(super.localBindPoint);
		} catch (SocketException e) {
			System.out.println("Error binding a socket" +e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
		specialCase(transferRate);

		while (work) {
			super.forOneSec();
		}
		socket.close();
	}
	/*
	 * send and receive packets 
	 * @see NetworkLayer#sendAndReceive()
	 */
	@Override
	public void sendAndReceive() {
		sendPacket = new DatagramPacket(MSG.getBytes(), MSG.length(), super.remoteBindPoint);
		receivePacket = new DatagramPacket(buffer, bufferSize);
		/* Send and receive message */
		try {
			socket.send(sendPacket);
			socket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* Compare sent and received message */
		String receivedData = new String(receivePacket.getData(), receivePacket.getOffset(),
				receivePacket.getLength());
		
		if (receivedData.compareTo(MSG) == 0)
			System.out.printf("%d bytes sent and received\n", receivePacket.getLength());
		else
			System.out.printf("Sent and received msg not equal!\n");

	}
}
