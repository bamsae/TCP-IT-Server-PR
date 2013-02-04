import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;


class BamsaeGallery {

	public static void main(String args[]) {
		HttpServer server = new HttpServer(7777);
		ActionSet.getActionSet().addAction("/", new test());

		server.start();
	}
}