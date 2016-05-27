package com.fob.balls.net.bean;

public class SimpleChatromsg {

	private long abstime;
	private String groupid;
	private String sender;
	private String sendernick;
	private String msgtype;
	private String msgmime;
	private String msg;
	private String image;

	public long getAbstime() {
		return abstime;
	}

	public void setAbstime(long abstime) {
		this.abstime = abstime;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSendernick() {
		return sendernick;
	}

	public void setSendernick(String sendernick) {
		this.sendernick = sendernick;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getMsgmime() {
		return msgmime;
	}

	public void setMsgmime(String msgmime) {
		this.msgmime = msgmime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
