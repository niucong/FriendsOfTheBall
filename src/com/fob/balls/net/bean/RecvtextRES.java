package com.fob.balls.net.bean;

import java.util.ArrayList;
import java.util.List;

public class RecvtextRES extends ResultBasicBean {
	private String pageno;

	private String pagecount;

	private List<SimpleChatromsg> pagecontentlist = new ArrayList<SimpleChatromsg>();

	public String getPageno() {
		return pageno;
	}

	public void setPageno(String pageno) {
		this.pageno = pageno;
	}

	public String getPagecount() {
		return pagecount;
	}

	public void setPagecount(String pagecount) {
		this.pagecount = pagecount;
	}

	public List<SimpleChatromsg> getPagecontentlist() {
		return pagecontentlist;
	}

	public void setPagecontentlist(List<SimpleChatromsg> pagecontentlist) {
		this.pagecontentlist = pagecontentlist;
	}

}
