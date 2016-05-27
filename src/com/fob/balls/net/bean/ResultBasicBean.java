/**
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * Copyright 2010-2013 DataComo Communications Technology INC.
 * 
 * This source file is a part of MID_WINS_API_V1.0 project. 
 * date: 2013-11-12
 *
 */
package com.fob.balls.net.bean;

public class ResultBasicBean {

	/**
	 * “1”为成功，“0”为失败
	 */
	private String ret;

	/**
	 * 字符串错误码
	 */
	private String err;

	/**
	 * 时间戳
	 */
	private long curt;

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}

	public long getCurt() {
		return curt;
	}

	public void setCurt(long curt) {
		this.curt = curt;
	}

}
