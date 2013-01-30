import java.net.*;
import java.io.*;
import java.util.regex.*;


public class HttpResponse {
	private Socket socket = null;
	//StatusLine---------------
	private String 	versionOfHttp = null;
	private int 	statusCode = -1;
	private String	statusText = null;
	//Header-------------------
	private String 	contentType = null;
	private int		contentLength = -1;
	
	private String 	holeHead = null;
		//--cache--//
	private boolean isCache = false;
	//Body---------------------
	private boolean	isBody = false;
	private byte[]	resBody = null;

	//------------------------
	private	OutputStream out = null;
	private	DataOutputStream outputco = null;

	public HttpResponse(Socket socket) {
		try {
			this.socket = socket;
			out = socket.getOutputStream();
			outputco = new DataOutputStream(out);
		} catch(Exception e) {
			e.printStackTrace();
		}
		versionOfHttp = "HTTP/1.1";
	}
	public boolean sendResponseHead() {
		try {
			if(versionOfHttp == null &&  statusCode == -1 &&
				(isBody == true && contentType ==null))
				return false;

			holeHead = versionOfHttp + " " + statusCode + statusText + "\n";
			if(isCache == false) {
				holeHead = holeHead + "Pragma: no-cache\n";
				holeHead = holeHead + "Cache-Control: no-cache\n";
			}
			if(isBody) {
				holeHead = holeHead + contentType + "\n";	
				if(contentLength > -1)
					holeHead = holeHead + contentLength + "\n";
			}
			System.out.println(holeHead);

			outputco.writeBytes(holeHead + "\n");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void setStatus(int code) {
		statusCode = code;
		statusText = StatusCode.get(code);
	}

	public void setContentType(String type) {
		isBody = true;
		contentType = "Content-Type: " + type;
	}
	public void setCharset(String type) {
		contentType = contentType + "; charset=" + type;
	}

	public void setCache() {
		
	}

	public void sendBytes(byte[] body) {
		try {
			outputco.writeBytes(new String(body));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void sendString(String body) {
		try {
			outputco.writeBytes(body);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}