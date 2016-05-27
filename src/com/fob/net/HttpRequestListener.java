package com.fob.net;



public interface HttpRequestListener
{
	public void onPreConnect();
	
	public void onPostConnect(int responseCode,byte[] data);
	
	public void onCanceled();
	
	public void onUpdate(NetProcessInfo args);
	
	public void onFailed();
}
