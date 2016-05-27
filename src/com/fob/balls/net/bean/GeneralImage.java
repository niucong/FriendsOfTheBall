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


//public class Chatromsg extends BaseEntity implements java.io.Serializable{
public class GeneralImage {

	private String url;
	
	private int w;
	
	private int h;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	@Override
	public String toString() {
		return "GeneralImage [url=" + url + ", w=" + w + ", h=" + h + "]";
	}
	
	
}

