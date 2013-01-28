import java.net.*;
import java.io.*;

public class HttpServer {

	ServerSocket serverSocket;

	public static void main(String args[]) {
		HttpServer server = new HttpServer();
		server.start();
	}

	public void start() {
		try {
			ServerSocket serverSocket = new ServerSocket(7777);
			Socket socket = null;
			OutputStream out;
			DataOutputStream outputco;
			InputStream in;
			DataInputStream inputco;

			socket = serverSocket.accept();



			out = socket.getOutputStream();
			outputco = new DataOutputStream(out);
			in = socket.getInputStream();
			inputco = new DataInputStream(in);


			byte[] check = new byte[4];
			int checkn = 0;
			while(true){
				byte aa = -1;
				aa = inputco.readByte();
				checkn = (checkn+1)%4;
				check[checkn] = aa;

				System.out.print(new String(new byte[] {aa}));

				if(check[checkn] == 10 && check[(checkn+1)%4] == 13 &&
					check[(checkn+2)%4] == 10 && check[(checkn+3)%4] == 13)
					break;
			}

			String Headd = "HTTP/1.1 200 OK\nContent-Type: text/html; charset=UTF-8\n";
			
			outputco.writeUTF(Headd + "\n[Notice] Test Message1 from Server.\n");
			System.out.println("hahaha");
			int count = 0;
			int real = 0;
			while(real < 10){
				if(count % 1000000000 == 0) {
					outputco.writeUTF("hahaha\n");
					real ++;
				}
				count ++;
			}

			inputco.close();
			outputco.close();
			socket.close();

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}