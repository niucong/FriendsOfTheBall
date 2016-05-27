package com.fob.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;

public class ParamUtils {
	public final static String map2QueryString(HashMap<String,Object> map){
		StringBuffer buf = new StringBuffer();
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			Object value = map.get(key);
			if(buf.length() > 0){
				buf.append('&');
			}
			buf.append(key);
			buf.append('=');
			if(value != null) {
				buf.append(value.toString());
			}
		}
		return buf.toString();
	}
	
	public final static String list2QueryString(List<NameValuePair> list){
		StringBuffer buf = new StringBuffer();
		int len = (list==null)?0:list.size();
		for(int i=0;i<len;i++){
			String key = list.get(i).getName();
			String value = list.get(i).getValue();
			if(buf.length() > 0){
				buf.append('&');
			}
			buf.append(key);
			buf.append('=');
			if(value != null) {
				buf.append(value.toString());
			}
		}
		return buf.toString();
	}
}
