package com.fob.balls.net.bean;

import java.util.List;

public class CourtListBean extends ResultBasicBean {

	private int pageno;
	private int pagecount;
	private List<CourtBean> list;

	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public List<CourtBean> getList() {
		return list;
	}

	public void setList(List<CourtBean> list) {
		this.list = list;
	}

}
