package com.fob.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.fob.balls.FOBErrorCode;
import com.fob.balls.FOBKeys;
import com.fob.tools.Base64;
import com.fob.tools.FOBConst;

public class FOBHttpManage extends HttpManage {

	private static FOBHttpManage instance;

	private static byte[] certificateData;
	static {
		String certificateString = "MIIEnjCCA4agAwIBAgIDAVYcMA0GCSqGSIb3DQEBBQUAMEAxCzAJBgNVBAYTAlVTMRcwFQYDVQQKEw5HZW9UcnVzdCwgSW5jLjEYMBYGA1UEAxMPR2VvVHJ1c3QgU1NMIENBMB4XDTEyMDQwODIwNTYxM1oXDTEzMDQxMTEyMjQwMVowgbYxKTAnBgNVBAUTIE1TSUMvcllNOWFDU3BsSkk3TDMtN1lHSzc3UXdJTUh6MQswCQYDVQQGEwJDTjEQMA4GA1UECBMHQmVpamluZzEQMA4GA1UEBxMHQmVpamluZzEwMC4GA1UECgwn5YyX5Lqs5ri46YGT5piT572R57uc5paH5YyW5pyJ6ZmQ5YWs5Y+4MRAwDgYDVQQLEwdJVCBEZXB0MRQwEgYDVQQDDAsqLnlvZG8xLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAOqZ1pwv8Z8csrBHTDjqFiL9uRN0sModva2j1k2TgSKeQImriovBTIQjbh4ZTTP6YYy5IDcxkV2ChQUdjtA/egHxCDfNNKMC63AxGngTFIV6W0HAwPe/rQpXdgB8Ra5lRQwrDr5h96b0Tnu0grTQ28traTjMaEnnTkHEK1r2O9+ueU1sJTKEbi4Vt/SbU1O+Ri1APg6z3S0JNtRPLI1fSF9cdUljwP3xlZ0lo3wxn5hcwB9chJsYIcgcxDdwsdZk5Wax/C0bLSpnG54O6sqIsftVfp5rX1Ec1+7zzURdC8fvTKotLWj6m6JGvAYqlR4tE5ZuNKd5Ii/vfVivd0vQDA0CAwEAAaOCASgwggEkMB8GA1UdIwQYMBaAFEJ5VBthzVUrPmPVPEhX9Z/7Rc5KMA4GA1UdDwEB/wQEAwIEsDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwIQYDVR0RBBowGIILKi55b2RvMS5jb22CCXlvZG8xLmNvbTA9BgNVHR8ENjA0MDKgMKAuhixodHRwOi8vZ3Rzc2wtY3JsLmdlb3RydXN0LmNvbS9jcmxzL2d0c3NsLmNybDAdBgNVHQ4EFgQUru+zVFX2cm+Vz7l8wO/zedV6sW0wDAYDVR0TAQH/BAIwADBDBggrBgEFBQcBAQQ3MDUwMwYIKwYBBQUHMAKGJ2h0dHA6Ly9ndHNzbC1haWEuZ2VvdHJ1c3QuY29tL2d0c3NsLmNydDANBgkqhkiG9w0BAQUFAAOCAQEADW07xBYkq0QD/9n+fAp+xW/sjGP+wETDzEwMsFW44xPHJHmdNI1WtsRv8bsVh6VENdUQlukb9gtungnV1DMYPTUGYy5Bqye40eqz5XdB2nrbuLdhbS0q6Qkyram1x3e8jDMomru2Vcj++LHqjR6AuWagi6sc0/2PZwnN9VPm+YvwZ/TTAPomHkGm++nDhiMLd6Qo1m1gRp4LnCzTaEAYongKG54pQAKHcxZRxpkcpUTz+pO/8r8SNxXZEcG7GMxMAmZUs5b9A5I+sQ28tXY6SaS+q5dxW5i1wGcpsZ+fyaTmxeIZuVE/WWARoZSn4swu8laYD+KwIwHI0sBqMxdNqQ==";
		certificateData = Base64.decode(certificateString);
	}

	public static FOBHttpManage getInstance() {
		if (instance == null) {
			instance = new FOBHttpManage();
		}
		return instance;
	}

	private FOBHttpManage() {
		super();
	}

	public void connect(final HttpConnInfo httpConnInfo) {

		if (httpConnInfo.isCertificate()) {
			httpConnInfo.setCertificateData(certificateData);
		}
		superConnect(httpConnInfo);

	}

	private void superConnect(HttpConnInfo httpConnInfo) {
		super.connect(httpConnInfo, new MyRequestListener(httpConnInfo));
	}

	class MyRequestListener implements HttpRequestListener {
		private HttpConnInfo requestInfo;

		public MyRequestListener(HttpConnInfo httpConnInfo) {
			this.requestInfo = httpConnInfo;
		}

		@Override
		public void onPreConnect() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPostConnect(int responseCode, byte[] data) {
			requestInfo.setResponseData(data);

			if (requestInfo.getListener() != null) {
				FOBResponse responseObject = getResponseObject(requestInfo);
				if (responseObject.getErrorCode() == FOBErrorCode.FOB_ERRORCODE_INVALID_ACCESSTOKEN && !requestInfo.isReAuth()) {
					requestInfo.setReAuth(true);
					ReAuthRequest request = new ReAuthRequest();
					request.connect(requestInfo);
				} else {
					requestInfo.getListener().onYodo1RequestComplete(responseObject);
				}
			}
		}

		@Override
		public void onCanceled() {
			if (requestInfo.getListener() != null) {
				FOBResponse responseObject = new FOBResponse(requestInfo.getRequestType(), false, FOBErrorCode.ERRORCODE_CANCEL);
				responseObject.setDataParse(requestInfo.getResponseParse());
				requestInfo.getListener().onYodo1RequestComplete(responseObject);
			}
		}

		@Override
		public void onUpdate(NetProcessInfo args) {

		}

		@Override
		public void onFailed() {
			if (requestInfo.getListener() != null) {
				FOBResponse responseObject = new FOBResponse(requestInfo.getRequestType(), false);
				responseObject.setSuccess(false);
				responseObject.setErrorCode(FOBErrorCode.ERRORCODE_CONNECT_FAILED);
				responseObject.setDataParse(requestInfo.getResponseParse());
				requestInfo.getListener().onYodo1RequestComplete(responseObject);
			}
		}

		private FOBResponse getResponseObject(HttpConnInfo httpConnInfo) {

			JSONObject jsonData = null;
			byte[] data = httpConnInfo.getResponseData();
			String message = null;
			FOBResponse responseObject = new FOBResponse(httpConnInfo.getRequestType(), false);

			if (data == null) {
				responseObject.setErrorCode(FOBErrorCode.ERRORCODE_DATAFORMAT_ERROR);
			} else {
				try {
					String s = new String(data, FOBConst.CHAR_ENCODE);

					JSONObject obj = new JSONObject(s);

					if (isSuccess(obj, responseObject)) {
						// success
						jsonData = obj;
					}

				} catch (UnsupportedEncodingException e) {
				} catch (JSONException e) {
					try {
						message = new String(data, FOBConst.CHAR_ENCODE);
					} catch (UnsupportedEncodingException e1) {
					}
					;
				}
			}

			responseObject.setRawData(data);
			responseObject.setDataParse(httpConnInfo.getResponseParse());
			if (message != null) {
				responseObject.setMessage(message);
			}

			responseObject.setResponse(jsonData);

			return responseObject;
		}

	}

	private boolean isSuccess(JSONObject obj, FOBResponse responseObject) {
		boolean isSuccess = true;
		boolean isTokenLose = false;
		int errorCode = 0;
		String message = "";
		if (obj.has(FOBKeys.KEY_ERRORCODE)) {
			isSuccess = false;
			try {
				errorCode = obj.getInt(FOBKeys.KEY_ERRORCODE);

			} catch (JSONException e) {
			}
			HashMap<String, String> codeMap = new HashMap<String, String>();
			codeMap.put("10006", "true");// 缺少参数AppKey (Token有问题时经常出这个错)
			codeMap.put("21314", "true");// Token已经被使用.
			codeMap.put("21315", "true");// Token已经过期.
			codeMap.put("21316", "true");// Token不合法.
			codeMap.put("21317", "true");// Token不合法.
			codeMap.put("21318", "true");// Pin码认证失败.
			codeMap.put("21319", "true");// 授权关系已经被解除.
			codeMap.put("21327", "true");// token过期
			codeMap.put("21332", "true");// Token无效
			codeMap.put("21501", "true");
			if (codeMap.get(errorCode) != null) {
				isTokenLose = true;
			}
			message = obj.optString("error");
		}
		if (!obj.isNull("ret")) {
			if (!"0".equals(obj.optString("ret"))) {
				isSuccess = false;
				HashMap<String, String> codeMap = new HashMap<String, String>();
				codeMap.put("3", "true");// 鉴权code
				codeMap.put("100007", "true");// 缺少access token.
				codeMap.put("100014", "true");// token过期
				codeMap.put("100015", "true");// token废除
				codeMap.put("100016", "true");// access token验证失败
				codeMap.put("100030", "true");// access token验证失败
				codeMap.put("-23", "true");// token is invalid
				codeMap.put("-21", "true");// token is invalid
				codeMap.put("41003", "true");// token is invalid
				if (obj.has("errcode")) {
					isSuccess = false;
					try {
						errorCode = obj.getInt("errcode");

					} catch (JSONException e) {
					}

					if (codeMap.get(errorCode) != null) {
						isTokenLose = true;
					}
					message = obj.optString("msg");
				}
				if (codeMap.get(obj.optString("ret")) != null) {
					isTokenLose = true;
				}

			}
		}
		responseObject.setErrorCode(errorCode);
		responseObject.setMessage(message);
		responseObject.setSuccess(isSuccess);
		return isSuccess;

	}

	public void destroy() {
		// certificateData = null;
		instance = null;
	}

	class ReAuthRequest extends NetRequestBase {
		@Override
		public void connect(HttpConnInfo httpConnInfo) {
			super.connect(httpConnInfo);
		}
	}

}
