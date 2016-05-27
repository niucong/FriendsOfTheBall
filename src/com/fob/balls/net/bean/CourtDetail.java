package com.fob.balls.net.bean;

import java.util.ArrayList;
import java.util.List;

public class CourtDetail {
	private String courtname;

	private String courtid;

	private String opentime;
	private String goldtime;
	private String timeremark;

	private String tel;

	private double lat;

	private double lon;

	private String courtinfos;

	private String productprices;

	private List<GeneralImage> faceimgs = new ArrayList<GeneralImage>();

	private List<GeneralImages> faceimgs_all = new ArrayList<GeneralImages>();

	private String address;

	public CourtDetail() {
	}

	public String getGoldtime() {
		return goldtime;
	}

	public void setGoldtime(String goldtime) {
		this.goldtime = goldtime;
	}

	public List<GeneralImages> getFaceimgs_all() {
		return faceimgs_all;
	}

	public void setFaceimgs_all(List<GeneralImages> faceimgs_all) {
		this.faceimgs_all = faceimgs_all;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<GeneralImage> getFaceimgs() {
		return faceimgs;
	}

	public void setFaceimgs(List<GeneralImage> faceimgs) {
		this.faceimgs = faceimgs;
	}

	public String getCourtname() {
		return courtname;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getOpentime() {
		return opentime;
	}

	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCourtinfos() {
		return courtinfos;
	}

	public void setCourtinfos(String courtinfos) {
		this.courtinfos = courtinfos;
	}

	public String getProductprices() {
		return productprices;
	}

	public void setProductprices(String productprices) {
		this.productprices = productprices;
	}

	public void setCourtname(String courtname) {
		this.courtname = courtname;
	}

	public String getCourtid() {
		return courtid;
	}

	public void setCourtid(String courtid) {
		this.courtid = courtid;
	}

	public String getTimeremark() {
		return timeremark;
	}

	public void setTimeremark(String timeremark) {
		this.timeremark = timeremark;
	}

	@Override
	public String toString() {
		return "CourtDetail [courtname=" + courtname + ", courtid=" + courtid
				+ ", opentime=" + opentime + ", tel=" + tel + ", lat=" + lat
				+ ", lon=" + lon + ", courtinfos=" + courtinfos
				+ ", productprices=" + productprices + "]";
	}

}
