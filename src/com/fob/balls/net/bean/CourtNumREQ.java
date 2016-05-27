package com.fob.balls.net.bean;


public class CourtNumREQ {

	int startgap;
	int endgap;
	String mtype;
	int courtnum;
	String courtnos;

	public String getCourtnos() {
		return courtnos;
	}

	public void setCourtnos(String courtnos) {
		this.courtnos = courtnos;
	}

	public int getStartgap() {
		return startgap;
	}

	public void setStartgap(int startgap) {
		this.startgap = startgap;
	}

	public int getEndgap() {
		return endgap;
	}

	public void setEndgap(int endgap) {
		this.endgap = endgap;
	}

	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}

	public int getCourtnum() {
		return courtnum;
	}

	public void setCourtnum(int courtnum) {
		this.courtnum = courtnum;
	}

}
