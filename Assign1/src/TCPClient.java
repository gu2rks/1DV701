
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPClient extends TransportLayer{
	
	public static final String MSG = "An Echo Message!An Echo Message!An Echo Message!aaaaaaaaaaaaaaaa";
	private Socket socket;
	private boolean work = true;
	private DataInputStream input;
	private DataOutputStream output;
	
	public static void main(String[]args) {
		if (args.length != 4) {
			throw new IllegalArgumentException(
					String.format("Usage : java -cp . TCPClient IP PORT BUFFERSIZE TRANSFERRATE"));
		}
		TCPClient clinet = new TCPClient(args);
		clinet.run();
	}
	
	public TCPClient(String[] args) {
		super(args);
		
	}
	
	/*
	 * create socket, connection and run the client
	 * @see NetworkLayer#run()
	 */
	@Override
	protected void run(){
			socket = new Socket();
			try {
				socket.bind(super.localBindPoint);
				socket.connect(super.remoteBindPoint); //TCP = connection oriented
			} catch (IOException e) {
				System.out.println("Error: server is not runing :"+e.getMessage());
				e.printStackTrace();
				System.exit(-1);
			}
		while (work){ //do it forever, utill user ctrl+c
		specialCase(transferRate);
		forOneSec();
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Error to close socket"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*
	 * create input stream for reading massage,
	 * output stream for sending massage
	 * NOTE: do need to handle exception when using it
	 * @see NetworkLayer#sendAndReceive()
	 */
	@Override
	protected void sendAndReceive()  {
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("##ERROR: socket is not connected.");
		}
		
		try {// Send message to server
		output.write(MSG.getBytes());
		}catch (IOException e) {
			System.out.println("### ERROR: occour when writing message to datastream");
		}

		StringBuilder receiveData = new StringBuilder();
		String fragments = "";
		
		
		try {  //READ A MESSAGE

			do { //do while input's byte is not "finish"
				
				int byteToRead = input.read(buffer, 0, buffer.length);
				fragments = new String(buffer, 0, byteToRead); // create fragments of recived message
				receiveData.append(fragments); //build up the whole message
			} while (input.available() > 0);

		} catch (IOException e) {
			System.out.println("### ERROR: occour when reading data from datastream");
		}
		
		/* Compare sent and received message */
		if (receiveData.toString().compareTo(MSG) == 0) {
			super.ok++;
			System.out.printf("%d bytes sent and received\n", receiveData.length());
		}
		else{
			super.notOk++;
			System.out.printf("Sent and received msg not equal!\n");

		}
	}
	
}
