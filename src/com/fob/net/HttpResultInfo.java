package com.fob.net;

public class HttpResultInfo {
	public static int RESPONSECODE_OK = 1;
	public static int RESPONSECODE_CANCEL = 2;
	public static int RESPONSECODE_ERROR = 3;
	
	private int responseCode;
	private int statusCode;
	private byte[] data;
	
	public HttpResultInfo(int responseCode){
		this.responseCode = responseCode;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public byte[] getData() {
		return data;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getResponseCode() {
		return responseCode;
	}
	
	
}
