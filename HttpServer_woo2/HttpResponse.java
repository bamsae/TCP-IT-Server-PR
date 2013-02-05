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
		//--cache--//\
	private CacheOption cache = null;
	//Body---------------------
	private boolean	isBody = false;
	private byte[]	resBody = null;

	//------------------------
	private	OutputStream out = null;
	private	DataOutputStream outputco = null;

	public HttpResponse(Socket socket) {
		cache = new CacheOption();
		try {
			this.socket = socket;
			out = socket.getOutputStream();
			outputco = new DataOutputStream(out);
		} catch(Exception e) {
			e.printStackTrace();
		}
		versionOfHttp = "HTTP/1.1";
	}
	public void close() {
		try {
			outputco.close();
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public boolean sendResponseHead() {
		try {
			holeHead = versionOfHttp + " " + statusCode + statusText + "\n";
			SimpleDateFormat formatter = new SimpleDateFormat ("E, dd MMM yy HH:mm:ss", Locale.ENGLISH);
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date currentTime = new Date ();
			String dTime = formatter.format(currentTime);
			System.out.println (dTime);
			holeHead = holeHead + "Date: " + dTime + " GMT\n";
			if(cache.getIsCache() == false) {
				holeHead = holeHead + "Pragma: no-cache\n";
				holeHead = holeHead + "Cache-Control: no-cache\n";
			}
			else {
				if(cache.getExpires() != null) {
					String sExpires = formatter.format(cache.getExpires());
					holeHead = holeHead + "Expires: " + sExpires + " GMT\n";
				}
				//else if(cache.getCacheControl() != -1) {
					holeHead = holeHead + "Cache-Control: max-age=60" + "\n";
				//}
				if(cache.getIsLastModified()){ 
					String lMtime = formatter.format(cache.getLastModified());
					holeHead = holeHead + "Last-Modified: " + lMtime + "\n";
				}
				else if(cache.getIsEtag()) {
					holeHead = holeHead + "ETag: " + cache.getETag() + "\n";
				}
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
		if(isBody == false)
			return;
		contentType = contentType + "; charset=" + type;
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
	public void setCache(HttpRequest req) {
		cache.setCache(req);
	}
	public void setCache(HttpRequest req, int type) {
		cache.setConditionalGET(req, type);
	}
	public CacheOption getCache() {
		return cache;
	}
	public void sendFile(File file) throws Exception{
		if(cache.checkCache(file)) {
			this.setStatus(StatusCode.HTTP_NOT_MODIFIED);	
			this.sendResponseHead();
			return ;
		}
		
		this.setStatus(StatusCode.HTTP_OK);
		this.sendResponseHead();

		FileInputStream imfis = new FileInputStream(file);
		
		int aa = -1;
		aa = imfis.read();
		while(aa != -1){
			char cc = (char)aa;
			this.sendString(String.valueOf(cc));
			aa = imfis.read();
		} 

		imfis.close();
	}
}