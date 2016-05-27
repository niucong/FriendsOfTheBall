package com.fob.struct;

import com.fob.adapter.base.FOBAdapterModelBase;

public class FOBStructLeave implements FOBAdapterModelBase {
	String time;
	String head_url;
	String name;
	String content;
	FOBStructFriend user;
	boolean isUpdate;

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public FOBStructFriend getUser() {
		return user;
	}

	public void setUser(FOBStructFriend user) {
		this.user = user;
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

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getId() {
		return null;
	}

}
