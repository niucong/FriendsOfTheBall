/*
 * Powered By [DYL]
 * Web Site: www.DYL.com
 * Since 1991 - 2013
 */

package com.fob.balls.net.bean;

/**
 * @author peter.su email:sumin3127@163.com
 * @version 1.0
 * @since 1.0
 */

public class SetSelfInfoRES extends ResultBasicBean {

	private String nickname;
	private String favorsportlevel;
	private String sex;
	private String role;
	private String email;
	private String city;
	private String favorsport;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFavorsportlevel() {
		return favorsportlevel;
	}

	public void setFavorsportlevel(String favorsportlevel) {
		this.favorsportlevel = favorsportlevel;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFavorsport() {
		return favorsport;
	}

	public void setFavorsport(String favorsport) {
		this.favorsport = favorsport;
	}

}
