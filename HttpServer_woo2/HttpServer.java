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
				System.out.println("REQURI : " + request.getReqUri());
				if(ActionSet.getActionSet().isAction(request.getReqUri())) {
					ActionSet.getActionSet().doAction(request.getReqUri(),request,response);
				}
				else {
					File file = new File("." + request.getReqUri());
					if(file.exists() == true) {
						if(request.getReqUri().indexOf(".jpg") != -1)
							response.setContentType("image/jpeg");
						else if(request.getReqUri().indexOf(".ico") != -1)
							response.setContentType("image/gif");
						else {
							response.setContentType("text/html");
							response.setCharset("UTF-8");
						}
						response.setCache(request, CacheOption.CACHE_MODIFIED);
						System.out.println("Modified Since : " + request.getModifiedSince());
						response.sendFile(file);
					}
					else {
						response.setStatus(StatusCode.HTTP_NOT_FOUND);
						response.sendResponseHead();
					}
				}
				response.close();
				socket.close();
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