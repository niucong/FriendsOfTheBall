package com.fob.balls.net.bean;

public class OrderHeartBeatRES extends ResultBasicBean {
	// 是否状态有改变

	private String cs;// 已确认
	private String ucs;// 未确认
	private String as;// 是否有公告

	public String getUcs() {
		return ucs;
	}

	public void setUcs(String ucs) {
		this.ucs = ucs;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}

	@Override
	public String toString() {
		return "OrderHeartBeatRES [cs=" + cs + ", ucs=" + ucs + ", getRet()="
				+ getRet() + ", getErr()=" + getErr() + ", getCurt()="
				+ getCurt() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}

}
