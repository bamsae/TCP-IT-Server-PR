import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

class CacheOption {
	public static final int CACHE_MODIFIED = 0;
	public static final int CACHE_ETAG = 1;

	private HttpRequest request = null;

	private boolean isChaching = false;
	private boolean useLastModified = false;
	private Date	lastModified = null;
	private String	ifModifiedSince = null;
	private boolean	useEtag = false;

	public boolean getIsCache() {
		return isChaching;
	}

	public void setCache(HttpRequest req) {
		isChaching = true;
		useEtag = true;
	}
	public void setCache(HttpRequest req, int type) {
		isChaching = true;
		request = req;
		if(type == 0) {
			useLastModified = true;
			ifModifiedSince = req.getModifiedSince();
		}
		else {
			useEtag = true;
		}
	}
	public boolean getIsLastModified() {
		return useLastModified;
	}
	public void setLastModified(File file) {
		lastModified = new Date();
		lastModified.setTime(file.lastModified());
	}
	public Date getLastModified() {
		return lastModified;
	}
	public String getModifiedSince() {
		return ifModifiedSince;
	}

	public boolean getUseEtag() {
		return useEtag;
	}
}