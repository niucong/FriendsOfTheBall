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
public class GeneralImages {

	private GeneralImage thumb;

	private GeneralImage middle;
	
	private GeneralImage large;
	
	private GeneralImage original;

	public GeneralImage getThumb() {
		return thumb;
	}

	public void setThumb(GeneralImage thumb) {
		this.thumb = thumb;
	}

	public GeneralImage getMiddle() {
		return middle;
	}

	public void setMiddle(GeneralImage middle) {
		this.middle = middle;
	}

	public GeneralImage getLarge() {
		return large;
	}

	public void setLarge(GeneralImage large) {
		this.large = large;
	}

	public GeneralImage getOriginal() {
		return original;
	}

	public void setOriginal(GeneralImage original) {
		this.original = original;
	}

	@Override
	public String toString() {
		return "GeneralImages [thumb=" + thumb + ", middle=" + middle
				+ ", large=" + large + ", original=" + original + "]";
	}
	
	
}

