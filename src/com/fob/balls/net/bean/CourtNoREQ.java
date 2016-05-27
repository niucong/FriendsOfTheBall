package com.fob.balls.net.bean;

public class CourtNoREQ {

	private Integer courtno;

	private Integer timegap;

	public CourtNoREQ() {
	}

	public Integer getCourtno() {
		return courtno;
	}

	public void setCourtno(Integer courtno) {
		this.courtno = courtno;
	}

	public Integer getTimegap() {
		return timegap;
	}

	public void setTimegap(Integer timegap) {
		this.timegap = timegap;
	}

	@Override
	public String toString() {
		return "CourtstatusREQ [courtno=" + courtno + ", timegap=" + timegap
				+ "]";
	}

}
