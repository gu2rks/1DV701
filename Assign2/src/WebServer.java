import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
	
	static ServerSocket webServer;
	
	public static void main(String[] args) {
		
		try {
			webServer = new ServerSocket(8888);
		} catch (IOException e) {
			System.out.println("Error initializing socket: Socket is Already in use "+e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		
		System.out.println("Web server is up and runing... " 
				+ "\nListening for connection on port 8888"
				+ "\nPress ctrl+c to terminate the program");
		//run forever (untill user terminate with ctrl+c)
		while(true) {
			try {
				Socket listen = webServer.accept();
				new Thread(new AcceptClients(listen)).run();
			} catch (IOException e) {
				System.out.println("Error to accept a client"+e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
