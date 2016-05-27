package com.fob.net;

public abstract class HttpManage {
	
	public HttpManage(){
		init();
	}
	
	private void init(){
	}
	
	protected void connect(HttpRequestInfo httpConnInfo,HttpRequestListener listener){
		HttpLoader httploader = getHttpLoader(true);
		httploader.setListener(listener);
		httploader.connect(httpConnInfo);
	}
	
	
	private final HttpLoader getHttpLoader(boolean async){

		return new CustomThreadpoolApacheLoader(this);

	}
}
