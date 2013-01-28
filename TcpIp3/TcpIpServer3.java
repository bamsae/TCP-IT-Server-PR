import java.net.*;
import java.io.*;
import java.util.*;

public class TcpIpServer3 {
	HashMap clients;

	TcpIpServer3() {
		clients = new HashMap();
		Collections.synchronizedMap(clients);
	}

	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(7777);
			System.out.println("서버가 시작되었습니다.");

			while(true) {
				socket = serverSocket.accept();
				System.out.println("[" + socket.getInetAddress() + ":" + 
					socket.getPort() + "]" + "에서 접속하였습니다.");
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	void sendToAll(String msg) {
		Iterator it = clients.keySet().iterator();

		while(it.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream)clients.get(it.next());
				
				String Headd = "HTTP/1.1 200 OK\nServer: nginx\nDate: Thu, 24 Jan 2013 05:10:32 GMT\nContent-Type: text/html; charset=UTF-8\nConnection: close\nCache-Control: no-cache, no-store, must-revalidate\nPragma: no-cache\nVary: Accept-Encoding,User-Agent\n";
				out.writeUTF(Headd + "\n" + msg);
			} catch(IOException e) {}
		}
	}

	public static void main(String args[]) {
		new TcpIpServer3().start();
	}


	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream in;
		DataOutputStream out;

		ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch(IOException e) {}
		}

		public void run() {
			String name = "";
			try {
				name = in.readUTF();
				if(name.equals(""))
					name = "test with curl";
				sendToAll("#" + name + "님이 들어오셨습니다.");

				String Headd = "HTTP/1.1 200 OK\nServer: nginx\nDate: Thu, 24 Jan 2013 05:10:32 GMT\nContent-Type: text/html; charset=UTF-8\nConnection: close\nCache-Control: no-cache, no-store, must-revalidate\nPragma: no-cache\nVary: Accept-Encoding,User-Agent\n";
				out.writeUTF(Headd + "\n" + "welcome");

				clients.put(name, out);
				System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");

				while(in != null) {
					System.out.println("hahaha");
					sendToAll(in.readUTF());
				}
			} catch(IOException e) {
				//hehe
			} finally {
				sendToAll("#" + name + "님이 나가셨습니다.");
				clients.remove(name);
				System.out.println("[" + socket.getInetAddress() + ":" +
					socket.getPort() + "]" + "에서 접속을 종료하였습니다.");
				System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");
			}
		}
	}
}
