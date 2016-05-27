package com.fob.net;

public class NetRequestBase {

	protected void connect(final HttpConnInfo httpConnInfo){
			FOBHttpManage.getInstance().connect(httpConnInfo);

	}
}
