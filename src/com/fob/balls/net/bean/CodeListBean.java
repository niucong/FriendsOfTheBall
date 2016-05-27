package com.fob.balls.net.bean;

import java.util.List;

public class CodeListBean extends ResultBasicBean {

	/**
	 * 公告心跳率（秒），刷新底栏状态使用。
	 */
	private String ansheartbeatrate;

	/**
	 * 订单心跳率
	 */
	private String orderheartbeatrate;

	/**
	 * 管理员聊天窗口刷新率（秒），聊天窗口关闭后停止刷新。
	 */
	private String managerchatbeatrate;

	/**
	 * 预订期（天）
	 */
	private String bookperiod;

	/**
	 * 大于当前时间的未确认订单最大数量，小于当前时间的订单不显示
	 */
	private String unconfirmedordernum;

	/**
	 * 大于当前时间的已确认订单最大数量，小于当前时间的订单用分页显示
	 */
	private String confirmedordernum;

	/**
	 * 常用场馆最大数量
	 */
	private String favorgymnum;

	/**
	 * 常用场馆空闲场地刷新频率（秒）
	 */
	private String favorgymbeatrate;

	/**
	 * 支持的城市码表
	 */
	private List<CityBean> citycodes;

	/**
	 * 支持的运动项目码表
	 */
	private List<SportBean> sportcodes;

	public String getAnsheartbeatrate() {
		return ansheartbeatrate;
	}

	public void setAnsheartbeatrate(String ansheartbeatrate) {
		this.ansheartbeatrate = ansheartbeatrate;
	}

	public String getOrderheartbeatrate() {
		return orderheartbeatrate;
	}

	public void setOrderheartbeatrate(String orderheartbeatrate) {
		this.orderheartbeatrate = orderheartbeatrate;
	}

	public String getManagerchatbeatrate() {
		return managerchatbeatrate;
	}

	public void setManagerchatbeatrate(String managerchatbeatrate) {
		this.managerchatbeatrate = managerchatbeatrate;
	}

	public String getBookperiod() {
		return bookperiod;
	}

	public void setBookperiod(String bookperiod) {
		this.bookperiod = bookperiod;
	}

	public String getUnconfirmedordernum() {
		return unconfirmedordernum;
	}

	public void setUnconfirmedordernum(String unconfirmedordernum) {
		this.unconfirmedordernum = unconfirmedordernum;
	}

	public String getConfirmedordernum() {
		return confirmedordernum;
	}

	public void setConfirmedordernum(String confirmedordernum) {
		this.confirmedordernum = confirmedordernum;
	}

	public String getFavorgymnum() {
		return favorgymnum;
	}

	public void setFavorgymnum(String favorgymnum) {
		this.favorgymnum = favorgymnum;
	}

	public String getFavorgymbeatrate() {
		return favorgymbeatrate;
	}

	public void setFavorgymbeatrate(String favorgymbeatrate) {
		this.favorgymbeatrate = favorgymbeatrate;
	}

	public List<CityBean> getCitycodes() {
		return citycodes;
	}

	public void setCitycodes(List<CityBean> citycodes) {
		this.citycodes = citycodes;
	}

	public List<SportBean> getSportcodes() {
		return sportcodes;
	}

	public void setSportcodes(List<SportBean> sportcodes) {
		this.sportcodes = sportcodes;
	}

}
