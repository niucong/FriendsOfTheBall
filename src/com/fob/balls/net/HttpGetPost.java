package com.fob.balls.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.fob.util.L;

public class HttpGetPost {

	private static final String TAG = "HttpGetPost";

	public static String post(String url, JSONObject json,
			Map<String, String> headers) {
		L.d(TAG, "post url=" + url + ",json=" + json);

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json");
		if (headers != null) {
			Set<String> keys = headers.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = (String) i.next();
				post.addHeader(key, headers.get(key));
			}
		}

		try {
			StringEntity s = new StringEntity(json.toString(), "UTF-8");
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(s);

			HttpResponse httpResponse = client.execute(post);
			L.i(TAG, "post StatusCode="
					+ httpResponse.getStatusLine().getStatusCode());

			InputStream inStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inStream, "UTF-8"));
			StringBuilder strber = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null)
				strber.append(line + "\n");
			inStream.close();
			L.getLongLog(TAG, "", "post strber=" + strber.toString()
					+ httpResponse.getStatusLine().getStatusCode());
			return strber.toString();
			// if (httpResponse.getStatusLine().getStatusCode() ==
			// HttpStatus.SC_OK) {
			// HttpEntity entity = httpResponse.getEntity();
			// String charset = EntityUtils.getContentCharSet(entity);
			// L.i(TAG, "post charset=" + charset);
			// return charset;
			// }
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * get
	 * 
	 * @param url
	 * @param headerKey
	 * @param headerVaue
	 */
	public static String getHttp(String url, Map<String, String> headers) {
		L.d(TAG, "getHttp url=" + url);
		HttpGet httpGet = new HttpGet(url);

		if (headers != null) {
			Set<String> keys = headers.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = (String) i.next();
				httpGet.addHeader(key, headers.get(key));
			}
		}

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 15 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, 25 * 1000);

		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		try {
			HttpResponse httpResponse = httpclient.execute(httpGet);
			L.i(TAG, "getHttp =" + httpResponse.getStatusLine().getStatusCode());
			InputStream inStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inStream, "utf-8"));
			StringBuilder strber = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
				strber.append(line + "\n");
			inStream.close();
			L.getLongLog(TAG, "", "getHttp strber=" + strber.toString());
			return strber.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param url
	 * @param headers
	 * @return
	 */
	public static int getHttpStatus(String url, Map<String, String> headers) {
		HttpGet httpGet = new HttpGet(url);

		if (headers != null) {
			Set<String> keys = headers.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = (String) i.next();
				httpGet.addHeader(key, headers.get(key));
			}
		}

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 15 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, 25 * 1000);

		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		try {
			HttpResponse httpResponse = httpclient.execute(httpGet);
			L.i(TAG, "getHttp =" + httpResponse.getStatusLine().getStatusCode());
			return httpResponse.getStatusLine().getStatusCode();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * @param url
	 *            the web will be connected
	 * @param headers
	 * @param parmas
	 *            data will be sent
	 */
	private void postHttp(String url, Map<String, String> headers,
			Map<String, String> parmas) {
		HttpPost httpPost = new HttpPost(url);

		if (headers != null) {
			Set<String> keys = headers.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = (String) i.next();
				httpPost.addHeader(key, headers.get(key));
			}
		}
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		if (parmas != null) {
			Set<String> keys = parmas.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = (String) i.next();
				pairs.add(new BasicNameValuePair(key, parmas.get(key)));
			}
		}

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 15 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, 25 * 1000);

		HttpClient httpclient = new DefaultHttpClient(httpParameters);

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8")); //
			HttpResponse httpResponse = httpclient.execute(httpPost);
			InputStream inStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inStream, "utf-8"));
			StringBuilder strber = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
				strber.append(line + "\n");
			inStream.close();
			Log.i("MobilpriseActivity", strber.toString());
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Log.i("MobilpriseActivity", "success");

			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static JSONObject posts(String url, JSONObject json,
			Map<String, String> headers) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		JSONObject response = null;
		// post.setHeader("Content-Type", "application/json");
		if (headers != null) {
			Set<String> keys = headers.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = (String) i.next();
				post.addHeader(key, headers.get(key));

			}
		}

		try {
			json = new JSONObject();
			json.put("Email", "testbuyer@buyercompany.com");
			;
			json.put("Password", "123456");
			StringEntity s = new StringEntity(json.toString(), "utf-8");
			// s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
			// "application/json"));
			s.setContentEncoding("HTTP.UTF_8");
			// s.setContentType("application/json");
			s.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(s);

			// HttpResponse res = client.execute(post);
			HttpResponse httpResponse = client.execute(post);
			InputStream inStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inStream, "utf-8"));
			StringBuilder strber = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
				strber.append(line + "\n");
			inStream.close();
			Log.i("MobilpriseActivity", strber.toString());
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				String charset = EntityUtils.getContentCharSet(entity);
				// response = new JSONObject(new JSONTokener(new
				// InputStreamReader(entity.getContent(),charset)));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

}
