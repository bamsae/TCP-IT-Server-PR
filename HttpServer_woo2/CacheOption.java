import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

class CacheOption {
	public static final int CACHE_MODIFIED = 0;
	public static final int CACHE_ETAG = 1;

	private HttpRequest request = null;

	private boolean isChaching = false;
	//----Expires & Cache-controls---
	private Date expires = null;
	private int cache_control_maxage = -1;
	//--------LastModified---------
	private boolean useLastModified = false;
	private Date	lastModified = null;
	private String	ifModifiedSince = null;
	//--------Etag-----------------
	private boolean	useEtag = false;
	private String etag = null;
	private String ifNoneMatch = null;

	public boolean getIsCache() {
		return isChaching;
	}

	public void setCache(HttpRequest req) {
		isChaching = true;
		useEtag = true;
	}
	public void setExpires(Date date) {
		isChaching = true;
		expires = date;
	}
	public void setCacheControl(int sec) {
		isChaching = true;
		cache_control_maxage = sec;
	}
	public Date getExpires() {
		return expires;
	}
	public int getCacheControl() {
		return cache_control_maxage;
	}
	public void setConditionalGET(HttpRequest req, int type) {
		isChaching = true;
		request = req;
		if(type == 0) {
			useLastModified = true;
			ifModifiedSince = req.getModifiedSince();
		}
		else {
			useEtag = true;
			ifNoneMatch = req.getNoneMatch();
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

	public boolean getIsEtag() {
		return useEtag;
	}
	public String getNoneMatch() {
		return ifNoneMatch;
	}
	public String getETag() {
		return etag;
	}


	public boolean checkCache(File file) throws Exception{
		if(this.getIsCache()) {
			if(useLastModified) {
				this.setLastModified(file);
				if(this.getModifiedSince() != null) {
					SimpleDateFormat formatter = new SimpleDateFormat ("E, dd MMM yy HH:mm:ss", Locale.ENGLISH);
					formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
					Date ifModifiedDate = formatter.parse(this.getModifiedSince());
					if(this.getLastModified().compareTo(ifModifiedDate) <= 0) {
						return true;
					}
				}
			}
			else if(useEtag) {
				etag = Long.toHexString(file.lastModified()) + Long.toHexString(file.length());
				if(this.getNoneMatch() != null) {
					if(this.getNoneMatch().equals(etag))
						return true;
				}
			}
		}
		return false;
	}
}