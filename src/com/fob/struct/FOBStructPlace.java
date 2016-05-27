package com.fob.struct;

import com.fob.adapter.base.FOBAdapterModelBase;
import com.fob.balls.net.bean.GeneralImages;

public class FOBStructPlace implements FOBAdapterModelBase {

	private String name;
	private String time;
	private String date;
	private String timeEnd;
	private String number;
	private String id;
	private String sportcusername;
	private String isBook;
	private GeneralImages headimg_all;
	private String operatestatus;
	private String status;

	public String getOperatestatus() {
		return operatestatus;
	}

	public void setOperatestatus(String operatestatus) {
		this.operatestatus = operatestatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public GeneralImages getHeadimg_all() {
		return headimg_all;
	}

	public void setHeadimg_all(GeneralImages headimg_all) {
		this.headimg_all = headimg_all;
	}

	public String getIsBook() {
		return isBook;
	}

	public void setIsBook(String isBook) {
		this.isBook = isBook;
	}

	boolean isUpdate = true;
	boolean isMsgUpdate;

	public boolean isMsgUpdate() {
		return isMsgUpdate;
	}

	public void setMsgUpdate(boolean isMsgUpdate) {
		this.isMsgUpdate = isMsgUpdate;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getSportcusername() {
		return sportcusername;
	}

	public void setSportcusername(String sportcusername) {
		this.sportcusername = sportcusername;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
