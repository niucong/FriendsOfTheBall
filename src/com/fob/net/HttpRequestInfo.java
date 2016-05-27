package com.fob.net;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;


public class HttpRequestInfo
{
	private static final int TIMEOUT_DEFAULT = 15000;

	private URL mUrl;

	private LinkedHashMap<String,Object> postParams;

	private LinkedHashMap<String,String> queryParams;

	private HashMap<String,String> mHeaderFields;

	private String mPostEncoding = "utf-8";

	private String mMethod = "GET";

	private boolean mDisableRedirecting;

	private boolean mAutoClose = true;

	private int mTimeout = TIMEOUT_DEFAULT;

	private String mCertificateFile;

	private FOBRequestListener mListener;

	private IResponseParse mResponseParse;


	private boolean mIsCertificate;
	private byte[] mCertificateData;

	private boolean isPostFormData;

	public boolean isPostFormData()
	{
		return isPostFormData || isPoseBytes();
	}

	private boolean isPoseBytes()
	{
		if(postParams!=null)
		{
			Iterator iter = postParams.values().iterator();
			while(iter.hasNext())
			{
				if(iter.next() instanceof byte[])
				{
					return true;
				}
			}
		}
		return false;
	}

	public void setPostFormData(boolean isPostFormData)
	{
		this.isPostFormData = isPostFormData;
	}

	public HttpRequestInfo(String url)
	{
		init();
		try
		{
			mUrl = new URL(url);
		}
		catch(Exception e)
		{
		}
	}

	private void init()
	{

	}

	public String getMethod()
	{
		return mMethod;
	}

	public void setMethod(String method)
	{
		mMethod = method;
	}

	public URL getUrl()
	{
		return mUrl;
	}

	public IResponseParse getmResponseParse() {
		return mResponseParse;
	}

	public void setmResponseParse(IResponseParse mResponseParse) {
		this.mResponseParse = mResponseParse;
	}

	public final static int DEFAULT_HTTPS_PORT = 443;
	public void setUrl(String url)
	{
		try
		{
			mUrl = new URL(url);
			if("https".equals(mUrl.getProtocol()))
			{
				if(mUrl.getPort() == -1)
				{
					//append port.
					url = mUrl.getProtocol() + "://" + mUrl.getHost() + ":" + DEFAULT_HTTPS_PORT;
					String path = mUrl.getPath();
					if(path != null)
					{
						url += path;
					}
					String query = mUrl.getQuery();
					if(query != null)
					{
						url += "?" + query;
					}
					mUrl = new URL(url);
				}
			}
		}
		catch(Exception e)
		{
		}

	}

	public void setCertificateFile(String file)
	{
		mCertificateFile = file;
	}

	public String getCertificateFile()
	{
		return mCertificateFile;
	}

	public String getPostEncoding()
	{
		return mPostEncoding;
	}

	public boolean isPost()
	{
		return (postParams!=null && postParams.size()>0) || mMethod.equalsIgnoreCase("POST");
	}

	public LinkedHashMap<String,Object> getPostParams()
	{
		return postParams;
	}

	public void setPostParams(LinkedHashMap<String,Object> postParams)
	{
		this.postParams = postParams;
	}

	public void addPostParams(String key,Object value)
	{
		if(postParams==null)
		{
			postParams = new LinkedHashMap<String, Object>();
		}
		postParams.put(key, value);
	}

	public void addQueryParams(String key,String value)
	{
		if(queryParams==null)
		{
			queryParams = new LinkedHashMap<String, String>();
		}
		queryParams.put(key, value);
	}

	public HashMap<String,String> getHeaderFields()
	{
		return mHeaderFields;
	}

	public void setHeaderFields(HashMap<String,String> fields)
	{
		if(mHeaderFields != null)
		{
			Iterator<String> iter = fields.keySet().iterator();
			String key = null;
			while(iter.hasNext())
			{
				key = iter.next();
				mHeaderFields.put(key, fields.get(key));
			}
		}
		else
		{
			mHeaderFields = fields;
		}
	}

	public void setHeader(String key,String value)
	{
		if(mHeaderFields == null)
		{
			mHeaderFields = new HashMap<String,String>();
		}
		mHeaderFields.put(key, value);
	}

	public boolean isDisableRedirecting()
	{
		return mDisableRedirecting;
	}

	public void setDisableRedirecting(boolean disable)
	{
		mDisableRedirecting = disable;
	}

	public boolean isAutoClose()
	{
		return mAutoClose;
	}

	public void setAutoClose(boolean autoClose)
	{
		mAutoClose = autoClose;
	}

	public int getTimeout()
	{
		return mTimeout;
	}

	public void setTimeout(int timeout)
	{
		mTimeout = timeout;
	}

	public FOBRequestListener getListener()
	{
		return mListener;
	}

	public void setListener(FOBRequestListener listener)
	{
		this.mListener = listener;
	}

	public boolean isCertificate()
	{
		return mIsCertificate;
	}

	public void setIsCertificate(boolean mIsCertificate)
	{
		this.mIsCertificate = mIsCertificate;
	}



	public byte[] getCertificateData()
	{
		return mCertificateData;
	}

	public void setCertificateData(byte[] certificateData)
	{
		this.mCertificateData = certificateData;
	}


	public LinkedHashMap<String, String> getQueryParams()
	{
		return queryParams;
	}

	public void setQueryParams(LinkedHashMap<String, String> queryParams)
	{
		this.queryParams = queryParams;
	}
}
