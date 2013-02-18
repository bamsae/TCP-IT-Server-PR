import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class HttpServer {

	private ServerSocket serverSocket;
	private Socket socket;

	public HttpServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		while(true) {
			try {
				socket = serverSocket.accept();

				serverThread thread = new serverThread(socket);

				thread.start();
			} catch(Exception e) {
				e.printStackTrace();
			} //try - catch
		} //while
	} //start

	class serverThread extends Thread {
		private Socket socket = null;
		private HttpRequest request = null;
		private HttpResponse response = null;

		public serverThread(Socket socket) {
			try {
				this.socket = socket;
				request = new HttpRequest(socket);
				response = new HttpResponse(socket);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				System.out.println("ReqPath : " + request.getReqPath());
				System.out.println("ReqQuery : " + request.getReqQuery());
				if(ActionSet.getActionSet().isAction(request.getReqPath())) {
					System.out.println("setting cache");
					ActionSet.getActionSet().doAction(request.getReqPath(),request,response);
				}
				else {
					File file = new File("." + request.getReqPath());
					System.out.println("file.jpgcheck");
					if(file.exists() == true) {
						if(request.getReqPath().indexOf(".jpg") != -1)
							response.setContentType("image/jpeg");
						else if(request.getReqPath().indexOf(".ico") != -1)
							response.setContentType("image/gif");
						else {
							response.setContentType("text/html");
							response.setCharset("UTF-8");
						}
						response.setCache(request, CacheOption.CACHE_ETAG);
						System.out.println("Modified Since : " + request.getModifiedSince());
						response.sendFile(file);
					}
					else {
						response.setStatus(StatusCode.HTTP_NOT_FOUND);
						response.sendResponseHead();
					}
				}	
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				response.close();
				try {
					socket.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}