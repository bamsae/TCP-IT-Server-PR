import java.net.*;
import java.io.*;

public class HttpServer {

	private ServerSocket serverSocket;
	private HttpRequest request;
	private HttpResponse response;

	public static void main(String args[]) {
		HttpServer server = new HttpServer();
		server.setPort(7777);
		server.start();
	}
	public void setPort(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		while(true) {
			try {
				Socket socket = null;

				socket = serverSocket.accept();

				//---------Request---------//
				request = new HttpRequest(socket);
				System.out.print(request.getHead());
				if(request.getIsBody()) {
					System.out.println(request.getReqBodyToString());
				}
				System.out.println("URI : " + request.getReqUri());
				System.out.println("host : " + request.getHostUri());
				System.out.println("Type : " + request.getBodyType());
				System.out.println("Length : " + request.getBodyLength());
				
				//--------Response---------//
				response = new HttpResponse(socket);
				File file = new File("." + request.getReqUri());
				if(request.getReqUri().equals("/favicon.ico")) {
					response.setStatus(StatusCode.HTTP_OK);
					response.setContentType("image/gif");
					response.sendResponseHead();

					FileInputStream imfis = new FileInputStream(file);
	        		
	        		int aa = -1;
	        		aa = imfis.read();
	        		while(aa != -1){
	        			char cc = (char)aa;
	        			response.sendString(String.valueOf(cc));
	        			aa = imfis.read();
	        		} 

	        		imfis.close();
				}
				else if(file.exists() == true) {
					response.setStatus(StatusCode.HTTP_OK);
					response.setContentType("text/html");
					response.setCharset("UTF-8");
					response.sendResponseHead();
					response.sendString("[Notice] Test Message1 from Server.<br>\n");
					
					FileInputStream imfis = new FileInputStream(file);
	        		
	        		int aa = -1;
	        		aa = imfis.read();
	        		while(aa != -1){
	        			char cc = (char)aa;
	        			response.sendString(String.valueOf(cc));
	        			aa = imfis.read();
	        		} 

	        		imfis.close();
				}
				else {
					response.setStatus(StatusCode.HTTP_BAD_REQUEST);
					response.setContentType("text/html");
					response.setCharset("UTF-8");
					response.sendResponseHead();
					System.out.println("hahaha");
					int count = 0;
					int real = 0;
					while(real < 10){
						if(count % 1000000000 == 0) {
							response.sendString("hahaha<br>\n");
							real ++;
						}
						count ++;
					}
				}

				socket.close();

			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}