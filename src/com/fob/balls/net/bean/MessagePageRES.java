package com.fob.balls.net.bean;

import java.util.List;

public class MessagePageRES extends ResultBasicBean {
	private int pageno;

	private List<GeneralMsg> pagecontentlist;

	private long pagecount;

	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public List<GeneralMsg> getPagecontentlist() {
		return pagecontentlist;
	}

	public void setPagecontentlist(List<GeneralMsg> pagecontentlist) {
		this.pagecontentlist = pagecontentlist;
	}

	public long getPagecount() {
		return pagecount;
	}

	public void setPagecount(long pagecount) {
		this.pagecount = pagecount;
	}

	@Override
	public String toString() {
		return "Page [pageno=" + pageno + ", pagecontentlist="
				+ pagecontentlist + ", pagecount=" + pagecount + "]";
	}

}
