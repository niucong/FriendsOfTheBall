package com.fob.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

class CustomThreadpoolApacheLoader implements HttpLoader, Handler.Callback {
	private final static String TAG = "AsyncApacheLoader";

	// public final boolean VERBOSE =YLog.isLogOn();

	// public final static int API_JAVANET = 0;
	// public final static int API_APACHE = 1;

	public final static int ERROR_NETWORKERROR = 0;
	public final static int ERROR_CONNECTIONERROR = 1;
	public final static int ERROR_SERVERERROR = 2;

	// protected int mApi = API_JAVANET;

	protected HttpRequestListener mListener;

	// protected byte[] mResponseData;
	// protected int mResponseCode;

	// protected String mResponseText;
	// protected Object mResponse;

	protected boolean mSuppressPublish = true;

	private final static int MAX_BUFFER_SIZE = 1024 * 1;
	// protected byte[] mBuffer = new byte[BUFFER_SIZE];

	// protected int mId;

	// protected StringBuffer mVerbose;

	// protected ApacheSSLVerifier mApacheSslVerifier;

	// protected Handler mMessageHandler;

	protected boolean mCancelled;
	protected boolean mCompleted;
	// protected String mUrl;

	// private HttpRequestInfo mHttpConnInfo;

	// private boolean connectfail = false;

	// public AsyncApacheLoader()
	// {
	// // mMessageHandler = new Handler(this);
	// if(VERBOSE)
	// {
	// mVerbose = new StringBuffer();
	// }
	// }

	private static ExecutorService sExecutor;

	private final static int CORE_THREADS = 2;
	private final static int MAX_THREADS = 3;
	private final static int THREAD_ALIVE_TIME = 5;
	private static final int QUEUE_SIZE = 200;

	private static ExecutorService getExecutor() {
		if (sExecutor == null) {
			sExecutor = new ThreadPoolExecutor(CORE_THREADS, MAX_THREADS, THREAD_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(QUEUE_SIZE));
		}
		return sExecutor;
	}

	// private static Handler sHandler;
	private final static int MESSAGE_ON_POSTEXECUTE = 0;

	private Handler getHandler() {
		// if(sHandler == null){
		// sHandler = new Handler(Looper.getMainLooper());
		// }
		// return sHandler;
		return new Handler(Looper.getMainLooper(), this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		Log.d(TAG, "handleMessage");
		// TODO Auto-generated method stub
		switch (msg.what) {
		case MESSAGE_ON_POSTEXECUTE:
			HttpResultInfo result = (HttpResultInfo) msg.obj;
			onPostExecute(result);
			break;
		}
		return true;
	}

	public CustomThreadpoolApacheLoader(HttpManage httpManage) {
		// this();
		// mId = httpInfo.getRequestType();
		// mListener = httpManage.getHttpConnListener();
		// if(VERBOSE)
		// {
		// mVerbose = new StringBuffer();
		// }
	}

	/*
	 * public AsyncApacheLoader(int id,String prompt) { this mId = id; }
	 */

	protected void onPreExecute() {
		if (mListener != null) {
			mListener.onPreConnect();
		}
	}

	protected HttpResultInfo doInBackground(HttpRequestInfo... infos) {
		if (infos != null) {
			HttpRequestInfo info = infos[0];

			return process(info);

		}
		return null;
	}

	protected HttpResultInfo process(HttpRequestInfo info) {
		NetProcessInfo processInfo = null;
		try {
			// YLog.e(TAG, "process()");
			if (!mSuppressPublish) {
				processInfo = new NetProcessInfo();
				processInfo.process = NetProcessInfo.PROCESS_PREPARE;
			}

			URL url = info.getUrl();
			String mUrl = url.toString();
			// handle query params
			String queryString = buildQueryParams(info);
			if (queryString != null && mUrl != null) {
				if (mUrl.contains("?")) {
					mUrl += "&";
				} else {
					mUrl += "?";
				}
				mUrl += queryString;
			}

			HttpRequestBase request = info.isPost() ? new HttpPost(mUrl) : new HttpGet(mUrl);
			HttpClient client = getHttpsHttpClient(info);

//			String method = info.isPost() ? "POST" : "GET";

			/*
			 * int len = connection.getContentLength(); publishProgress(new
			 * Integer(len));
			 */
			// connection.setFollowRedirects(!info.isDisableRedirecting());
			if (!mSuppressPublish) {
				processInfo.process = NetProcessInfo.PROCESS_UPLOAD;
			}
			if (info.isPost()) {
				HttpPost post = (HttpPost) request;
				// post.set
				// connection.setRequestMethod("POST");

				HttpEntity httpEntity = buildHttpEntity(info);

				// byte[] data = info.getPostData();
				// if(data == null)
				// {
				// String s = info.getPostString();
				// if(null != s)
				// {
				// data = s.getBytes("utf-8");
				// }
				// }
				if (httpEntity != null) {
					if (!mSuppressPublish) {
						processInfo.bytes2Upload = (int) httpEntity.getContentLength();
					}
					// YLog.v(TAG, "prepare to post data: len=" + data.length);

					post.setEntity(httpEntity);
					// String keyContentType = "Content-Type";
					// Header[] h = post.getHeaders(keyContentType);
					// if(h == null || h.length == 0){
					// post.setHeader("Content-Type","application/x-www-form-urlencoded");
					// }
					// //OutputStream os = connection.getOutputStream();
					// YLog.v(TAG, "posted " + data.length + " bytes.");
				}
				if (info.isPostFormData()) {
					info.setHeader("Content-Type", ((MultipartHttpEntity) httpEntity).getContentType().getValue());
				} else {
					info.setHeader("Content-Type", "application/x-www-form-urlencoded");
				}
			}

			// handle headers
			HashMap<String, String> headers = info.getHeaderFields();
			if (headers != null) {
				Iterator<String> keys = headers.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					String value = (String) headers.get(key);
					request.setHeader(key, value);
				}
			}

			HttpResponse response = client.execute(request);

			int statusCode = response.getStatusLine().getStatusCode();
			InputStream is = response.getEntity().getContent();
			if (!mSuppressPublish) {
				processInfo.process = NetProcessInfo.PROCESS_DOWNLOAD;
				processInfo.bytes2Download = is.available();
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			// int bufSize = MAX_BUFFER_SIZE;
			byte[] buf = new byte[MAX_BUFFER_SIZE];
			while (true) {
				int size = is.read(buf);
				if (size == -1) {
					break;
				}
				bos.write(buf, 0, size);
				/*
				 * if(isCancelled()){ return processInfo; }
				 */

				if (!mSuppressPublish) {
					processInfo.downloadBytes = bos.size();
				}
				/*
				 * if(size < BUFFER_SIZE) { break; }
				 */
			}
			is.close();
			byte[] mResponseData = bos.toByteArray();
			bos.close();
			if (!mSuppressPublish) {
				processInfo.process = NetProcessInfo.PROCESS_ALLDONE;
			}
			HttpResultInfo resultInfo = new HttpResultInfo(HttpResultInfo.RESPONSECODE_OK);
			resultInfo.setStatusCode(statusCode);
			resultInfo.setData(mResponseData);
			return resultInfo;
		} catch (Exception e) {

			if (!mSuppressPublish) {
				processInfo.process = NetProcessInfo.PROCESS_ALLDONE;
			}
			HttpResultInfo resultInfo = new HttpResultInfo(HttpResultInfo.RESPONSECODE_ERROR);
			return resultInfo;
		}

	}

//	private SSLSocketFactory getSSLSocketFactory(boolean isCertificate, byte[] certificateData) {
//		SSLSocketFactory sf = null;
//		if (isCertificate && certificateData != null) {
//			try {
//				KeyStore trustStore = KeyStore.getInstance("PKCS12", "BC");
//				trustStore.load(null, null);
//
//				if (certificateData != null) {
//					// read Certificate
//					CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
//
//					InputStream is = new ByteArrayInputStream(certificateData);
//					java.security.cert.Certificate cer = cerFactory.generateCertificate(is);
//					trustStore.setCertificateEntry("trust", cer);
//					is.close();
//				}
//
//				sf = new SSLSocketFactoryEx(trustStore);
//				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//			} catch (KeyManagementException e) {
//				e.printStackTrace();
//			} catch (KeyStoreException e) {
//				e.printStackTrace();
//			} catch (NoSuchProviderException e) {
//				e.printStackTrace();
//			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			} catch (CertificateException e) {
//				e.printStackTrace();
//			} catch (UnrecoverableKeyException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return sf;
//	}

//	private HttpClient getHttpsHttpClient_(HttpRequestInfo info) {
//		try {
//
//			HttpParams params = new BasicHttpParams();
//			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//
//			// HttpProtocolParams.se
//			int timeout = info.getTimeout();
//			HttpConnectionParams.setConnectionTimeout(params, timeout);
//			HttpConnectionParams.setSoTimeout(params, timeout);
//			HttpConnectionParams.setSocketBufferSize(params, 2 * 8192);
//
//			SchemeRegistry registry = new SchemeRegistry();
//			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//			if (info.getUrl().getProtocol().equals("https")) {
//				registry.register(new Scheme("https", this.getSSLSocketFactory(info.isCertificate(), info.getCertificateData()), 443));
//			}
//
//			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
//
//			return new DefaultHttpClient(ccm, params);
//		} catch (Exception e) {
//			return new DefaultHttpClient();
//		}
//	}

	private HttpClient getHttpsHttpClient(HttpRequestInfo info) {
		try {

			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

			trustStore.load(null, null);



			HttpParams params = new BasicHttpParams();

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();

			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);

		} catch (Exception e) {

			return new DefaultHttpClient();
		}
	}

	// protected void applyConfigs(HttpConnInfo info,HttpRequestBase request)
	// {
	// //apply header fields.
	// HashMap<String,String> headers = info.getHeaderFields();
	// if(headers != null)
	// {
	// Iterator<String> keys = headers.keySet().iterator();
	// while(keys.hasNext())
	// {
	// String key = keys.next();
	// String value = headers.get(key);
	//
	// YLog.i(TAG, "added header fields: " + key + "=" + value);
	// request.setHeader(key, value);
	// }
	// }
	//
	// //apply parameters.
	// BasicHttpParams param = new BasicHttpParams();
	// HashMap<String,String> params = info.getParams();
	// boolean isPost = info.isPost();
	// if(params != null && !params.isEmpty())
	// {
	// //BasicHttpParams param = new BasicHttpParams();
	// Iterator<String> keys = params.keySet().iterator();
	// List<NameValuePair> paramEntities = new ArrayList<NameValuePair>();
	// while(keys.hasNext())
	// {
	// String key = keys.next();
	// String value = params.get(key);
	//
	// YLog.i(TAG, "added param fields: " + key + "=" + value);
	//
	// //request.setHeader(key, value);
	// param.setParameter(key, value);
	// if(isPost)
	// {
	// paramEntities.add(new BasicNameValuePair(key,value));
	// }
	// }
	//
	// if(isPost)
	// {
	// try
	// {
	// ((HttpPost)request).setEntity(new UrlEncodedFormEntity(paramEntities));
	// } catch (UnsupportedEncodingException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// //request.setParams(param);
	// }
	// HttpClientParams.setRedirecting(param, !info.isDisableRedirecting());
	// request.setParams(param);
	// //timeout.
	// }

	/*
	 * public void close() {
	 * 
	 * }
	 */

	protected void onCancelled() {
		// YLog.v(TAG, "net cancelled!!!");

		mCancelled = true;

		// mResponseData = null;
		if (mListener != null) {
			mListener.onCanceled();
		}
	}

	/*
	 * public boolean isCancelled() { return m_cancelled; }
	 */

	protected void onPostExecute(HttpResultInfo result) {
		if (mListener != null) {
			if (result.getResponseCode() != HttpResultInfo.RESPONSECODE_OK) {
				// Yodo1SDKResponse responseObject = new
				// Yodo1SDKResponse(mHttpConnInfo.getRequestType(), false,
				// Yodo1ErrorCode.ERRORCODE_CONNECT_FAILED);
				mListener.onFailed();
			} else {
				mListener.onPostConnect(result.getStatusCode(), result.getData());
			}
		}
		mCompleted = true;
		clear();
	}

	protected void onProgressUpdate(NetProcessInfo... values) {
		if (mListener != null) {
			mListener.onUpdate(values[0]);
		}
	}

	// public void setListener(IHttpConnListener listener)
	// {
	// mListener = listener;
	// }

	// public byte[] getData()
	// {
	// return mResponseData;
	// }

	/*
	 * public Object getResponse() { return mResponse; }
	 */

	// public int getResponseCode()
	// {
	// return mResponseCode;
	// }

	/*
	 * public String getText() { return this.mResponseText; }
	 */

	// public int getId()
	// {
	// return mId;
	// }

	// public void setId(int id)
	// {
	// mId = id;
	// }

	public boolean terminate() {
		if (mCompleted) {
			return false;
		}
		// return cancel(true);
		return false;
	}

	private Object mTag;

	public void setTag(Object tag) {
		mTag = tag;
	}

	public Object getTag() {
		return mTag;
	}

	public void clear() {
		// \\this.mBuffer = null;
		// this.mMessageHandler = null;
		this.mListener = null;
		// this.mResponse = null;
		// this.mResponseData = null;
		// this.mResponseText = null;
		// this.mUrl = null;
	}

	@Override
	public void connect(final HttpRequestInfo info) {
		// this.execute(info);
		ExecutorService executor = this.getExecutor();
		final Handler handler = getHandler();
		executor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				onPreExecute();
				HttpResultInfo result = doInBackground(info);

				Message msg = handler.obtainMessage(MESSAGE_ON_POSTEXECUTE, result);
				handler.sendMessage(msg);
				// onPostExecute(result);
			}
		});
	}

	@Override
	public void setListener(HttpRequestListener requestListener) {
		this.mListener = requestListener;
	}

	// @Override
	// public String getUrl() {
	// return mUrl;
	// }

	// @Override
	// public HttpConnInfo getHttpConnInfo() {
	// return mHttpConnInfo;
	// }

	private HttpEntity buildHttpEntity(HttpRequestInfo info) throws UnsupportedEncodingException {
		LinkedHashMap<String, Object> mParams = info.getPostParams();
		if (mParams != null && mParams.size() > 0) {
			// HttpLog.i(TAG,"http params="+ParamUtils.map2QueryString(mParams));
			if (info.isPostFormData()) {
				Part[] parts = new Part[mParams.size()];
				int pos = 0;
				for (Iterator it = mParams.entrySet().iterator(); it.hasNext();) {
					Entry<String, Object> entry = (Entry<String, Object>) it.next();
					Object value = entry.getValue();
					if (value instanceof byte[]) {
						parts[pos] = new FilePart(entry.getKey(), new ByteArrayPartSource("f", (byte[]) value));
					} else {
						parts[pos] = new StringPart(entry.getKey(), value.toString());
					}
					pos++;
				}
				// Iterator<String> iterator = mParams.keySet().iterator();
				// int pos = 0;
				// while (iterator.hasNext()) {
				// String key = iterator.next();
				// Object value = mParams.get(key);
				// if(value instanceof byte[]){
				// parts[pos] = new FilePart(key, new ByteArrayPartSource("f",
				// (byte[])value));
				// }else{
				// parts[pos] = new StringPart(key, value.toString());
				// }
				// pos++;
				// }
				return new MultipartHttpEntity(parts);
			} else {
				Iterator<String> iterator = mParams.keySet().iterator();
				List<NameValuePair> pair = new ArrayList<NameValuePair>();
				while (iterator.hasNext()) {
					String key = iterator.next();
					Object value = mParams.get(key);
					pair.add(new BasicNameValuePair(key, value.toString()));
				}
				return new StringEntity(ParamUtils.list2QueryString(pair), CHAR_ENCODE);
			}
		}

		return null;
	}

	private String buildQueryParams(HttpRequestInfo info) {
		return toHttpParam(info.getQueryParams());
	}

	private final static String toHttpParam(LinkedHashMap<String, String> map) {
		if (map != null && map.size() > 0) {
			StringBuffer buf = new StringBuffer();
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				Entry<String, String> entry = (Entry<String, String>) it.next();
				Object value = entry.getValue();
				if (buf.length() > 0) {
					buf.append('&');
				}
				buf.append(entry.getKey());
				buf.append('=');
				if (value != null) {
					buf.append(value);
				}
			}
			// Iterator<String> iter = map.keySet().iterator();
			// while(iter.hasNext()){
			// String key = iter.next();
			// Object value = map.get(key);
			// if(buf.length() > 0){
			// buf.append('&');
			// }
			// buf.append(key);
			// buf.append('=');
			// if(value != null) {
			// buf.append(value);
			// }
			// }
			return buf.toString();
		} else {
			return null;
		}
	}

}
