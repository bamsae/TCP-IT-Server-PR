import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;


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
	private int		cacheExpires = 0;
	private Date 	cacheExDate = null;
	private boolean isModified = true;
	private Date 	lastModified = null;
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
			SimpleDateFormat formatter = new SimpleDateFormat ("E, dd MMM yy HH:mm:ss", Locale.ENGLISH);
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date currentTime = new Date ();
			String dTime = formatter.format(currentTime);
			System.out.println (dTime);
			holeHead = holeHead + "Date: " + dTime + " GMT\n";
			if(isCache == false) {
				holeHead = holeHead + "Pragma: no-cache\n";
				holeHead = holeHead + "Cache-Control: no-cache\n";
			}
			else { 
				if(cacheExpires > 0)
					holeHead = holeHead + "Cache-Control: max-age=" + cacheExpires + "\n";
				else if(cacheExDate != null) {
					String exTime = formatter.format(cacheExDate);
					holeHead = holeHead + "Expires: " + exTime + "\n";
				}
				String lMtime = formatter.format(lastModified);
				holeHead = holeHead + "Last-Modified: " + lMtime + "\n";
			}
			if(isModified == false) {
				String lMtime = formatter.format(lastModified);
				holeHead = holeHead + "Last-Modified: " + lMtime + "\n";
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
	public void setStatus(int code, Date lastM) {
		statusCode = code;
		statusText = StatusCode.get(code);
		lastModified = lastM;

		if(code == StatusCode.HTTP_NOT_MODIFIED){
			isModified = false;
		}
	}

	public void setContentType(String type) {
		isBody = true;
		contentType = "Content-Type: " + type;
	}
	public void setCharset(String type) {
		contentType = contentType + "; charset=" + type;
	}

	public void setCache(int sec) {
		isCache = true;
		cacheExpires = sec;
	}
	public void setCache(Date date) {
		isCache = true;
		cacheExDate = date;
	}
	public void setCache(boolean check) {
		isCache = check;
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