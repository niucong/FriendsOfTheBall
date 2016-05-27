package com.fob.balls.net.bean;

public class VersionBean extends ResultBasicBean {

	/**
	 * 版本号为四位，如：0.5.0.1
	 */
	private String androidversion;

	/**
	 * 地址
	 */
	private String androidurl;

	public String getAndroidversion() {
		return androidversion;
	}

	public void setAndroidversion(String androidversion) {
		this.androidversion = androidversion;
	}

	public String getAndroidurl() {
		return androidurl;
	}

	public void setAndroidurl(String androidurl) {
		this.androidurl = androidurl;
	}

}
