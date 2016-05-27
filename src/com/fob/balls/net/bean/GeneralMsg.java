/*
 * Powered By [DYL]
 * Web Site: www.DYL.com
 * Since 1991 - 2013
 */

package com.fob.balls.net.bean;

//import javacommon.base.*;
//import javacommon.util.*;
//import cn.org.rapid_framework.util.*;
//import cn.org.rapid_framework.web.util.*;
//import cn.org.rapid_framework.page.*;
//import cn.org.rapid_framework.page.impl.*;
//import com.dyl.dyl001.service.*;
//import com.dyl.dyl001.vo.query.*;

/**
 * @author peter.su email:sumin3127@163.com
 * @version 1.0
 * @since 1.0
 */

// public class Chatromsg extends BaseEntity implements java.io.Serializable{
public class GeneralMsg {// implements Comparable<GeneralMsg> {

	private long abstime;

	private String groupid;

	private String courtorderid;

	// sender
	private String sender;

	private String sendernick;

	// msgtype:chat
	private java.lang.String msgtype;

	// msgmime:text/plain
	private java.lang.String msgmime;

	// msg
	private java.lang.String msg;

	// image:{thumb:{url:,width:,height:},middle:{url:,width:,height:},large:{url:,width:,height:},original:{url:,width:,height}}
	private GeneralImages image;

	// columns END

	public GeneralMsg() {
	}

	public String getCourtorderid() {
		return courtorderid;
	}

	public void setCourtorderid(String courtorderid) {
		this.courtorderid = courtorderid;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getSendernick() {
		return sendernick;
	}

	public void setSendernick(String sendernick) {
		this.sendernick = sendernick;
	}

	public GeneralImages getImage() {
		return image;
	}

	public void setImage(GeneralImages image) {
		this.image = image;
	}

	public long getAbstime() {
		return abstime;
	}

	public void setAbstime(long abstime) {
		this.abstime = abstime;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public java.lang.String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(java.lang.String msgtype) {
		this.msgtype = msgtype;
	}

	public java.lang.String getMsgmime() {
		return msgmime;
	}

	public void setMsgmime(java.lang.String msgmime) {
		this.msgmime = msgmime;
	}

	public java.lang.String getMsg() {
		return msg;
	}

	public void setMsg(java.lang.String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "GeneralMsg [abstime=" + abstime + ", groupid=" + groupid
				+ ", courtorderid=" + courtorderid + ", sender=" + sender
				+ ", sendernick=" + sendernick + ", msgtype=" + msgtype
				+ ", msgmime=" + msgmime + ", msg=" + msg + ", image=" + image
				+ "]";
	}

	// public int compareTo(GeneralMsg anotherMsg) {
	// return abstime.compareTo(anotherMsg.getAbstime());
	// }

}
