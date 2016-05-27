package com.fob.balls.net.bean;

public class CourtBean {

	private String courtid;
	private String courtname;
	private String isbook;
	private GeneralImages headimg_all;
	private String operatestatus;
	private String status;

	public String getOperatestatus() {
		return operatestatus;
	}

	public void setOperatestatus(String operatestatus) {
		this.operatestatus = operatestatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public GeneralImages getHeadimg_all() {
		return headimg_all;
	}

	public void setHeadimg_all(GeneralImages headimg_all) {
		this.headimg_all = headimg_all;
	}

	public String getIsbook() {
		return isbook;
	}

	public void setIsbook(String isbook) {
		this.isbook = isbook;
	}

	public String getCourtid() {
		return courtid;
	}

	public void setCourtid(String courtid) {
		this.courtid = courtid;
	}

	public String getCourtname() {
		return courtname;
	}

	public void setCourtname(String courtname) {
		this.courtname = courtname;
	}
}
