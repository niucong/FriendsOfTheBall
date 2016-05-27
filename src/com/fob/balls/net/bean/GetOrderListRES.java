package com.fob.balls.net.bean;

import java.util.List;

public class GetOrderListRES extends ResultBasicBean {
	private int pageno;
	private long pagecount;

	private List<CourtorderRES> list;

	public GetOrderListRES() {
	}

	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public long getPagecount() {
		return pagecount;
	}

	public void setPagecount(long pagecount) {
		this.pagecount = pagecount;
	}

	public List<CourtorderRES> getList() {
		return list;
	}

	public void setList(List<CourtorderRES> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "GetOrderListRES [pageno=" + pageno + ", pagecount=" + pagecount
				+ ", list=" + list + "]";
	}

}
