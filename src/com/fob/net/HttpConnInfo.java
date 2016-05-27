package com.fob.net;

public class HttpConnInfo extends HttpRequestInfo
{
	private int mRequestType;
	
	private byte[] mResponseData;
	
	private IResponseParse mResponseParse;
	
	private boolean isReAuth;
//	private int mResponseCode;
	
	public boolean isReAuth() {
		return isReAuth;
	}

	public void setReAuth(boolean isReAuth) {
		this.isReAuth = isReAuth;
	}

	public HttpConnInfo(String url) {
		super(url);
	}
	
	public int getRequestType() {
		return mRequestType;
	}

	public void setRequestType(int requestType) {
		this.mRequestType = requestType;
	}

	public byte[] getResponseData() {
		return mResponseData;
	}

	public void setResponseData(byte[] responseData) {
		this.mResponseData = responseData;
	}

	public void setListener(int requestType,IResponseParse responseParse,FOBRequestListener listener){
		this.mRequestType = requestType;
		this.mResponseParse = responseParse;
		setListener(listener);
	}
	
	public IResponseParse getResponseParse() {
		return mResponseParse;
	}
}
