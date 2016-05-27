package com.fob.balls.net.bean;

import java.util.List;

public class CourtorderRES {
	private String courtorderid;

	private String courtname;

	private String sportcusername;

	private String captionusername;

	private String captionnickname;

	private String captionmobile;

	private String mnickname;

	// private String chatroomid;

	private String orderdate;

	private String detail;

	private List<CourtNumREQ> courtnumreqs;

	// private List<CourtNoREQ> courtnoreqs;

	private long lastupdate;

	private long msglastupdate;

	private String status;

	private String executestatus;

	private String lastestmsg;

	private String groupid;// 聊天室id

	private GeneralImages headimg_all;

	public GeneralImages getHeadimg_all() {
		return headimg_all;
	}

	public void setHeadimg_all(GeneralImages headimg_all) {
		this.headimg_all = headimg_all;
	}

	public CourtorderRES() {
	}

	public long getMsglastupdate() {
		return msglastupdate;
	}

	public void setMsglastupdate(long msglastupdate) {
		this.msglastupdate = msglastupdate;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getLastestmsg() {
		return lastestmsg;
	}

	public void setLastestmsg(String lastestmsg) {
		this.lastestmsg = lastestmsg;
	}

	public String getExecutestatus() {
		return executestatus;
	}

	public void setExecutestatus(String executestatus) {
		this.executestatus = executestatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCaptionusername() {
		return captionusername;
	}

	public void setCaptionusername(String captionusername) {
		this.captionusername = captionusername;
	}

	public String getSportcusername() {
		return sportcusername;
	}

	public void setSportcusername(String sportcusername) {
		this.sportcusername = sportcusername;
	}

	// public List<CourtNoREQ> getCourtnoreqs() {
	// return courtnoreqs;
	// }
	//
	// public void setCourtnoreqs(List<CourtNoREQ> courtnoreqs) {
	// this.courtnoreqs = courtnoreqs;
	// }

	public String getCourtname() {
		return courtname;
	}

	public void setCourtname(String courtname) {
		this.courtname = courtname;
	}

	public String getMnickname() {
		return mnickname;
	}

	public void setMnickname(String mnickname) {
		this.mnickname = mnickname;
	}

	public String getCourtorderid() {
		return courtorderid;
	}

	public void setCourtorderid(String courtorderid) {
		this.courtorderid = courtorderid;
	}

	public String getCaptionnickname() {
		return captionnickname;
	}

	public void setCaptionnickname(String captionnickname) {
		this.captionnickname = captionnickname;
	}

	public String getCaptionmobile() {
		return captionmobile;
	}

	public void setCaptionmobile(String captionmobile) {
		this.captionmobile = captionmobile;
	}

	// public String getChatroomid() {
	// return chatroomid;
	// }
	//
	// public void setChatroomid(String chatroomid) {
	// this.chatroomid = chatroomid;
	// }

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public List<CourtNumREQ> getCourtnumreqs() {
		return courtnumreqs;
	}

	public void setCourtnumreqs(List<CourtNumREQ> courtnumreqs) {
		this.courtnumreqs = courtnumreqs;
	}

	public long getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(long lastupdate) {
		this.lastupdate = lastupdate;
	}

	@Override
	public String toString() {
		return "CourtorderRES [courtorderid=" + courtorderid + ", courtname="
				+ courtname + ", sportcusername=" + sportcusername
				+ ", captionusername=" + captionusername + ", captionnickname="
				+ captionnickname + ", captionmobile="
				+ captionmobile
				+ ", mnickname="
				+ mnickname// + ", chatroomid=" + chatroomid
				+ ", orderdate=" + orderdate + ", detail=" + detail
				+ ", courtnumreqs="
				+ courtnumreqs// + ", courtnoreqs="+ courtnoreqs
				+ ", lastupdate=" + lastupdate + ", status=" + status
				+ ", executestatus=" + executestatus + "]";
	}

}
