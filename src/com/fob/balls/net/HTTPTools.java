/**
 * 与服务器通信交互类
 */
package com.fob.balls.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;

import android.net.http.AndroidHttpClient;

public class HTTPTools {
	private static final String TAG = "HTTPtools";

	private static CookieStore cookieStore;// 定义一个Cookie来保存session

	/**
	 * 向服务器发送URL请求 获得返回数据
	 * 
	 * @param doUrl
	 *            stauts的类名
	 * @param actionName
	 *            方法名
	 * @param params
	 *            传递的参数
	 * @return 获得返回的JSON结果
	 * @throws Exception
	 * @throws IllegalStateException
	 */
	public static String postRequest(String url, List<NameValuePair> params)
			throws Exception {
		AndroidHttpClient httpClient = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type",
					"application/json; charset=utf-8");
			if (params != null && params.size() > 0) {
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				httpPost.setEntity(entity);
			}
			BasicHttpContext context = new BasicHttpContext();
			if (cookieStore == null) {
				cookieStore = new BasicCookieStore();
			}
			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			httpClient = AndroidHttpClient.newInstance("");
			HttpResponse httpResponse = httpClient.execute(httpPost, context);
			return readData(httpResponse.getEntity().getContent());
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
	}

	/**
	 * 读取请求数据
	 * 
	 * @param inSream
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	public static String readData(InputStream inSream) throws Exception {
		ByteArrayOutputStream outStream = null;
		String str = null;
		try {
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = inSream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			byte[] data = outStream.toByteArray();
			str = new String(data, "utf-8");
		} catch (OutOfMemoryError e) {
		} finally {
			if (outStream != null)
				outStream.close();
			if (inSream != null)
				inSream.close();
		}
		return str;
	}

	/**
	 * MD5加密字符串
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String md5Util(String str) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		String md5str = null;

		MessageDigest md = MessageDigest.getInstance("MD5");

		md.update(str.trim().getBytes("UTF-8"));
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		md5str = buf.toString();
		return md5str;
	}
}
