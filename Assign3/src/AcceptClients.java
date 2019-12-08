import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class AcceptClients implements Runnable {
	private Socket socket;
	//Root directory for *.html
	private String path = "../resources/"; 
	//path to specific location
	private final String CODE302 = "../resources/302.html";
	private final String CODE403 = "../resources/403.html";
	private final String CODE404 = "../resources/404.html";
	private final String CODE501 = "../resources/501.html";

	public AcceptClients(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		System.out.println("Accepted connection from " + socket.getInetAddress().getHostAddress());
		try {
			start();
		} catch (IOException e) {
			System.out.println(
					"Conection from " + socket.getInetAddress().getHostAddress() + " is closed, cause by user side");
			e.printStackTrace();
		}
		System.out.println("Closed conection from " + socket.getInetAddress().getHostAddress());
	}

	public void start() throws IOException{

		// read input from client
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
		
		// create a http hader
		HTTPHeader generator = new HTTPHeader(out, outToClient);

		String clientRequest = input.readLine();
		System.out.println("Client's Request "+clientRequest);
		
		//check if request is empty or null
		if (clientRequest == null || clientRequest.isEmpty()){
			//send 500 code
			generator.outToClient("../resources/500.html", "500 Internal Server Error");
		}
		
		else {
			// split the input
			StringTokenizer split = new StringTokenizer(clientRequest);
			String method = split.nextToken().toUpperCase(); // get a method
			path += split.nextToken(); //get path
				//check if path if empty
				if (path.endsWith("/")) {
						path += "index.html"; //re locate the path to index.html
					}
				File file = new File(path);
					
				if (method.contains("GET")) {
					if (path.contains("user1/look.png")) {
						String status = "302 Found";
						generator.outToClient(CODE302, status);
					} else if (path.contains("admin")) {
						String status = "403 Forbidden";
						generator.outToClient(CODE403, status);
					} else if (!file.exists()) {
						String status = "404 File Not Found";
						generator.outToClient(CODE404, status);
					}else {
						String status = "200 OK";
						generator.outToClient(path, status);
					}
				}
					
				else {
					String status = "501 Not Implemented";
					generator.outToClient(CODE501, status);
				}
		}
		out.close();
		outToClient.close();

	}

}
