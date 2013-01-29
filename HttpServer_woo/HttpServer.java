import java.net.*;
import java.io.*;

public class HttpServer {

	ServerSocket serverSocket;
	HttpRequest request;
	HttpResponse response;

	public static void main(String args[]) {
		HttpServer server = new HttpServer();
		server.start();
	}

	public void start() {
		try {
			ServerSocket serverSocket = new ServerSocket(7777);
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
			response.setStatus(StatusCode.HTTP_OK);
			response.setContentType("text/html");
			response.setCharset("UTF-8");
			response.sendResponseHead();
			response.sendString("[Notice] Test Message1 from Server.<br>\n");
			
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

			socket.close();

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}