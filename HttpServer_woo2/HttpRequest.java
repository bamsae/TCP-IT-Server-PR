import java.net.*;
import java.io.*;
import java.util.regex.*;


public class HttpRequest {
	private Socket socket = null;

	private String holeHead = null;
	//StartLine---------
	private String httpMethod = null;
	private String reqUri = null;
	private String reqPath = null;
	private String reqQuery = null;
	private String versionOfHttp = null;
	//HEAD--------------
	private String userAgent = null;
	private String hostUri = null;
	private String httpConnection = null;
	private String acceptMediaType = null;
	private String acceptEncoding = null;
	private String acceptLanguage = null;
	private String acceptCharset = null;

	private int contentLength = -1;
	private String contentType = null;

		//--cache--//
	private String ifModifiedSince = null;
	private String ifNoneMatch = null;

	//BODY--------------
	private Boolean isBody = false;

	private byte[] reqBody = null;

	public HttpRequest(Socket socket) {
				
		InputStream in = null;
		DataInputStream inputco = null;
		try {
			this.socket = socket;

			in = socket.getInputStream();
			inputco = new DataInputStream(in);
		
			byte[] check = new byte[4];
			int checkn = 0;


		//---------HEAD & STARTLINE--------//

			holeHead = new String("");
			while(true){
				byte aa = -1;
				aa = inputco.readByte();
				checkn = (checkn+1)%4;
				check[checkn] = aa;

				holeHead = holeHead + new String(new byte[] {aa});

				if(check[checkn] == 10 && check[(checkn+1)%4] == 13 &&
					check[(checkn+2)%4] == 10 && check[(checkn+3)%4] == 13)
					break;
			}

			if(holeHead.indexOf("Content") != -1) {
				isBody = true;
				System.out.println("isbody");
			}

			//------------STARTLINE------------//

			setStartLine((holeHead.split("\\r?\\n")[0]));

			//------------STARTLINE------------//


			//---------------HEAD--------------//

			setHead(holeHead);

			//---------------HEAD--------------//


			//---------------BODY--------------//

			if(isBody) {
				reqBody = new byte[contentLength + 1];
				try {
					for(int i = 0 ; i < contentLength ; i ++) {
						reqBody[i] = inputco.readByte();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("HEAD부분 작업 에러");
		}
		//---------------BODY--------------//

	} 

	private void setStartLine(String method) throws Exception{
		String[] splitArr = method.split(" ");
		httpMethod = splitArr[0];
		reqUri = splitArr[1];
		String[] splitUri = reqUri.split("\\?");
		reqPath = splitUri[0];
		if(splitUri.length == 2)
			reqQuery = splitUri[1];
		versionOfHttp = splitArr[2];
	}
	private void setHead(String headd) {
		String head = new String(headd);
		String[] ch = null;
		if(head.indexOf("Host: ") != -1) {
			ch = head.split("Host: ");
			hostUri = (ch[1].split("\\r?\\n"))[0];
		}
		if(head.indexOf("Connection: ") != -1) {
			ch = head.split("Connection: ");
			httpConnection = (ch[1].split("\\r?\\n"))[0];
		}
		if(head.indexOf("Accept: ") != -1) {
			ch = head.split("Accept: ");
			acceptMediaType = (ch[1].split("\\r?\\n"))[0];
		}
		if(head.indexOf("User-Agent: ") != -1) {
			ch = head.split("User-Agent: ");
			userAgent = (ch[1].split("\\r?\\n"))[0];
		}
		if(head.indexOf("Accept-Encoding: ") != -1) {
			ch = head.split("Accept-Encoding: ");
			acceptEncoding = (ch[1].split("\\r?\\n"))[0];
		}
		if(head.indexOf("Accept-Language: ") != -1) {
			ch = head.split("Accept-Language: ");
			acceptLanguage = (ch[1].split("\\r?\\n"))[0];
		}
		if(head.indexOf("Accept-Charset: ") != -1) {
			ch = head.split("Accept-Charset: ");
			acceptCharset = (ch[1].split("\\r?\\n"))[0];
		}

		//------------body data----------//
		if(head.indexOf("Content-Length: ") != -1) {
			ch = head.split("Content-Length: ");
			contentLength = Integer.parseInt((ch[1].split("\\r?\\n"))[0]);
		}
		if(head.indexOf("Content-Type: ") != -1) {
			ch = head.split("Content-Type: ");
			contentType = (ch[1].split("\\r?\\n"))[0];
		}

		//-------------cache-------------//
		if(head.indexOf("If-Modified-Since: ") != -1) {
			ch = head.split("If-Modified-Since: ");
			ifModifiedSince = (ch[1].split("\\r?\\n"))[0];
		}
		if(head.indexOf("If-None-Match: ") != -1) {
			ch = head.split("If-None-Match: ");
			ifNoneMatch = (ch[1].split("\\r?\\n"))[0];
		}
	}

	public String getHead() {
		return holeHead;
	}
	public String getHttpMethod() {
		return httpMethod;
	}
	public String getReqUri() {
		return reqUri;
	}
	public String getReqPath() {
		return reqPath;
	}
	public String getReqQuery() {
		return reqQuery;
	}
	public String getVersion() {
		return versionOfHttp;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public String getHostUri() {
		return hostUri;
	}
	public String getHttpConnection() {
		return httpConnection;
	}
	public String getAcceptMediaType() {
		return acceptMediaType;
	}
	public String getAcceptEncoding() {
		return acceptEncoding;
	}
	public String getAcceptLanguage() {
		return acceptLanguage;
	}
	public String getAcceptCharset() {
		return acceptCharset;
	}
	public boolean getIsBody() {
		return isBody;
	}
	public int getBodyLength() {
		return contentLength;
	}
	public String getBodyType() {
		return contentType;
	}
	

	public String getReqBodyToString() {
		return new String(reqBody);
	}
	public byte[] getReqBodyToByteArr() {
		return reqBody;
	}

	public String getModifiedSince() {
		return ifModifiedSince;
	}
	public String getNoneMatch() {
		return ifNoneMatch;
	}

}