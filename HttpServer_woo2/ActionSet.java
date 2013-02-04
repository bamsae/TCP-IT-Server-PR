import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

class ActionSet {

	private HashMap<String, HttpAction> actionHash;
	private static ActionSet singleton = null;
	
	public static ActionSet getActionSet() {
		if(singleton == null) {
			singleton = new ActionSet();
		}

		return singleton;
	} 
	private ActionSet() {
		actionHash = new HashMap<String, HttpAction>();
	}
	public void addAction(String path, HttpAction action) {
		actionHash.put("/", action);
	}

	public boolean isAction(String path) {
		if(actionHash.get(path) == null)
			return false;
		return true;
	}
	public void doAction(String action, HttpRequest req, HttpResponse res) {
		HttpAction httpAction;
		httpAction = actionHash.get(action);
		httpAction.action(req, res);
	}
}