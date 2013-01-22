import java.net.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TcpIpClient {
	public static void main(String args[]) {
		for(int i = 0 ; i < 5 ; i ++) {
			try {
				String serverIp = "127.0.0.1";

				System.out.println(getTime() + "서버에 연결중입니다. 서버IP :" + serverIp);
				Socket socket = new Socket(serverIp, 7777);

				InputStream in = socket.getInputStream();
				DataInputStream dis = new DataInputStream(in);

				System.out.println(getTime() + "서버로부터 받은 메세지 :" + dis.readUTF());
				System.out.println(getTime() + "연결을 종료합니다.");

				dis.close();
				socket.close();
				System.out.println(getTime() + "연결이 종료되었습니다.");
			} catch(Exception ce){
				ce.printStackTrace();
			} 
		}
	}

	static String getTime() {
		SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
		return f.format(new Date());
	}
}