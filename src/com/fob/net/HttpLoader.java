package com.fob.net;






interface HttpLoader
{
	public static final String CHAR_ENCODE = "utf-8";
	public final static int API_JAVANET = 0;
	public final static int API_APACHE = 1;

//	byte[] getData();

//	int getId();

//	Object getTag();

//	void setListener(IHttpConnListener listener);

//	void setTag(Object tag);

	//void setApi(int api);

//	int getResponseCode();
	
	public abstract void connect(HttpRequestInfo info);
	
	public abstract void setListener(HttpRequestListener requestListener);
	
//	String getUrl();
	
//	HttpConnInfo getHttpConnInfo();
	
}
