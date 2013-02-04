import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

abstract class HttpAction {
	private HttpServer server;

	abstract void action(HttpRequest req, HttpResponse res);
}