import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

class test extends HttpAction {

	public void action(HttpRequest req, HttpResponse res) {
		try {	
			res.setContentType("text/html");
			res.setCharset("UTF-8");
			res.setCache(req, CacheOption.CACHE_MODIFIED);

			File file = new File("./html/test.html");
			res.sendFile(file);
			res.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}