package com.fob.balls.net.bean;

public class AuthuserBean extends ResultBasicBean {

	/**
	 * YQKAccessKey
	 */
	private String username;

	/**
	 * YQKAccessKey对应的密钥输入
	 */
	private String token;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
