import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

class test extends HttpAction {

	public void action(HttpRequest req, HttpResponse res) {
		try {	
			Calendar toto = Calendar.getInstance();
			toto.add(toto.MINUTE, 1);

			res.setContentType("text/html");
			res.setCharset("UTF-8");
			res.getCache().setExpires(toto.getTime());
			res.getCache().setConditionalGET(req, CacheOption.CACHE_ETAG);

			File file = new File("./html/test.html");
			res.sendFile(file);
			res.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}