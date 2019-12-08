import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class HTTPHeader {

	private PrintWriter header;
	private String contentType;
	private DataOutputStream content;

	private final String CODE500 = "../resources/500.html";

	public HTTPHeader(PrintWriter header, DataOutputStream content) {
		this.header = header;
		this.content = content;
	}
	/**
	 * create file input stream, read all byte of data and put in the byte array
	 * @param file file to be read
	 * @return byte arry contains byte of data.
	 * @throws FileNotFoundException
	 */
	public byte[] readFile(File file) throws FileNotFoundException{
		// create new file input stream
		FileInputStream fileInput = new FileInputStream(file);
		byte[] fileData = new byte[(int) file.length()];
		try {
			//read a byte of data and put in the byte arry
			fileInput.read(fileData);
			fileInput.close();
		} catch (IOException e) {
			System.out.println("Error to read the file"+e.getMessage());
			outToClient(CODE500, "500 Internal Server Error");
			e.printStackTrace();
		}
		;
		return fileData;
	}
	
	/**
	 * Check file's content type
	 * @param requestedFile :path to the requested file
	 */
	public void checkContentType(String requestedFile) {
		if (requestedFile.endsWith(".html") || requestedFile.endsWith(".htm"))
			contentType = "text/html";
		if (requestedFile.endsWith(".jpeg") || requestedFile.endsWith(".jpg"))
			contentType = "image/jpeg";
		if (requestedFile.endsWith(".png"))
			contentType = "image/png";
	}
	
	/**
	 * Create HTTP response header and send the file's content back to client
	 * @param path : path to the requested file
	 * @param status : HTTTP response code
	 */
	public void outToClient(String path, String status) {
		File file = new File(path);
		byte[] filedata = null;
		try {
			filedata = readFile(file);
		} catch (FileNotFoundException e1) {
			System.out.println(path+" do not exit in ther server :" + e1.getMessage());
			//it never gonna reach this part..
			//because I already check if the file is exist in the server before i calling this method
			String code404 = "404 File Not Found";
			outToClient("../resources/404.html", code404);
			e1.printStackTrace();
		}

		// everything fine -> check content-type
		checkContentType(path); 

		// create a http response header
		header.println("HTTP/1.1 " + status);
		header.println("Server: WebServer by GU2RKS@GITHUB : 1.0");
		header.println("Date: " + new Date());
		header.println("Content-type: " + contentType);
		header.println("Content-length: " + file.length());
		header.println();// blank line between headers and content, very important !
		header.flush(); // flush character output stream buffer
		try {
			//write file's content to the stream
			content.write(filedata, 0, filedata.length);
			content.flush();
		} catch (IOException e) {
			System.out.println("Error to write the content of data :" + e.getMessage());
			outToClient(CODE500, "500 Internal Server Error");
			e.printStackTrace();
		}
	}
}
