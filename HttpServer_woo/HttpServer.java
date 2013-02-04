import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class HttpServer {

	private ServerSocket serverSocket;
	private HttpRequest request;
	private HttpResponse response;

	private Date	lastModifiedDate;

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
		lastModifiedDate = new Date();
		System.out.println("openServer " + lastModifiedDate);
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
				

					//---setCaching---//
				Calendar calendar = new java.util.GregorianCalendar();
        		calendar.setTime(lastModifiedDate);
        		calendar.add(Calendar.MINUTE,1);

				response.setCache(calendar.getTime());
				SimpleDateFormat formatter = new SimpleDateFormat ("E, dd MMM yy HH:mm:ss", Locale.ENGLISH);
				formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

				File file = new File("." + request.getReqUri());
				lastModifiedDate.setTime(file.lastModified());
				
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
				else if(request.getReqUri().equals("/") != true
					&& file.exists() == true) {

					Date checkDate = new Date();
					
					if(request.getModifiedSince() != null) {
						Date imsi = formatter.parse(request.getModifiedSince());
						System.out.println("hahahahaha" + imsi + "hh");
						checkDate = imsi;

						String imst = formatter.format(lastModifiedDate);
						lastModifiedDate = formatter.parse(imst);
						System.out.println("hahahahaha" + lastModifiedDate + "hh");
					}
					if(request.getModifiedSince() == null ||
						(request.getModifiedSince() != null && 
							checkDate.compareTo(lastModifiedDate) < 0)) {
						response.setStatus(StatusCode.HTTP_OK, lastModifiedDate);
						if(request.getReqUri().indexOf(".jpg") != -1)
							response.setContentType("image/jpeg");
						else {
							response.setContentType("text/html");
							response.setCharset("UTF-8");
						}
						response.sendResponseHead();
						//response.sendString("[Notice] Test Message1 from Server.<br>\n");
						
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
	        			response.setStatus(StatusCode.HTTP_NOT_MODIFIED, lastModifiedDate);
						response.sendResponseHead();	
	        		}
				}
				else {
					response.setCache(false);
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

			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}