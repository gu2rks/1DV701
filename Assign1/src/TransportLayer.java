import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public abstract class TransportLayer {
	SocketAddress remoteBindPoint;
	SocketAddress localBindPoint;
	protected String remoteIP;
	protected int remotePort;
	protected int bufferSize;
	protected byte[] buffer;
	protected int transferRate;
	protected final int SEC = 1000;
	int ok = 0;
	int notOk = 0;

	public TransportLayer(String args[]) {
		validationCTRL(args);
		System.out.println("Press ctrl+c to terminate the program");
		delay(2000); // delete if you want to. 
	}
	
	/*
	 * Exception handling and error messages output. for all 
	 */
	public void validationCTRL(String args[]) {

		IPv4IsValid(args[0]);
		portIsValid(Integer.valueOf(args[1]));
		bufferSizeIsValid(Integer.valueOf(args[2]));
		checkTransferRate(Integer.valueOf(args[3]));

		//if everything goes well -> bind it
		
		remoteBindPoint = new InetSocketAddress(remoteIP, remotePort);
			

	}
	/*
	 * check if IPv4 is valid
	 */
	protected void IPv4IsValid(String IP) {
		String[] octets = IP.split("\\.");
		if (octets.length != 4)
			throw new IllegalArgumentException(String.format("### ERROR: IP adress must have 4 octets"));
		for (String octet : octets) {
			int temp = Integer.valueOf(octet);
			if (temp < 0 || temp > 225) {
				throw new IllegalArgumentException(String.format("### ERROR: Each octets can must be between 0-255"));
			}
		}
		remoteIP = IP; 
	}
	/*
	 * check if port number is between 0-65535
	 */
	protected void portIsValid(int port) {
		if (port < 0 || port > 65535) {
			throw new IllegalArgumentException(String.format("### ERROR: Port number is invalid, must be between 0-65535"));
		}
		remotePort = port;

	}
	/*
	 * check if buffer size is to bigs
	 */
	protected void bufferSizeIsValid(int bufSize) {
		try {
			bufferSize = bufSize;
			buffer = new byte[bufferSize];
		} catch (OutOfMemoryError e) {
			throw new IllegalArgumentException(String.format("### ERROR: Buffer size is too big, please lowing it"));
		}
	}
	/*
	 * check transfer rate
	 */
	protected void checkTransferRate(int transferRate) {
		if (transferRate < 0)
			throw new IllegalArgumentException(String.format("### ERROR: Transfer rate must be more than 0"));
		this.transferRate = transferRate;
	}
	
	protected void specialCase(int transRate) {
		if (transRate == 0) {
			try {
				sendAndReceive();
			} catch (IOException e) {
				System.out.println("### Error to send/received the packet: "+e.getMessage());
				e.printStackTrace();
			}
			System.exit(-1);
		}
	} 
	/*
	 * just a delay
	 */
	protected void delay(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
	/*
	 * Send packets each one sec 
	 */
	protected void forOneSec() {
		int countPacket = 0;
		long startTimer = 0;
		startTimer = System.currentTimeMillis();
		while(true){
			for (int sentMessages = 0; sentMessages < transferRate; sentMessages++) {
				try {
					sendAndReceive();
				} catch (IOException e) {
					System.out.println("### Error to send/received the packet: "+e.getMessage());
					e.printStackTrace();
				}
				countPacket++;

				if (System.currentTimeMillis() - startTimer > 1000)
					break;
			}
			
		break;
		}
		System.out.printf("\n%d packets of %d are sent and recevice, %d packets remain\n%d packets are equal, %d packets are mess up\nNext loop start in one sec", countPacket,
				transferRate, transferRate - countPacket,ok, notOk);
		//reset everything
		startTimer = 0; 
		countPacket = 0;
		notOk = 0;
		ok = 0;
		//delay
		delay(SEC);
		

	}
	
	/*
	 * run client
	 */
	protected abstract void run() throws IOException;

	/*
	 * send and receive packets
	 */
	protected abstract void sendAndReceive() throws IOException;


}
