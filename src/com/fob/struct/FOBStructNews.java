package com.fob.struct;

import com.fob.adapter.base.FOBAdapterModelBase;

public class FOBStructNews implements FOBAdapterModelBase{
	String title;
	String address;
	String content;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
