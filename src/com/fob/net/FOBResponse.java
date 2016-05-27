package com.fob.net;

import org.json.JSONObject;


/**
 * Package the result of network request
 * @author talaya
 *
 */
public class FOBResponse{
	private int requestType;
	private boolean isSuccess;
	private JSONObject response;
	private byte[] rawData;
	private int errorCode; 
	private String message;
	private IResponseParse mResponseParse;
	
	/**
	 * Return parsed object
	 * Return different object according to the different request. 
	 * You can get the type using log. Convert it by force to corresponding object when you get the type. 
	 * You can get more information at Yodo1 sample app.
	 * @return
	 */
	public Object getParseObj(){
		if(mResponseParse!=null){
			return mResponseParse.getProcessdData(this);
		}else{
			return null;
		}
	}

	public FOBResponse(int requestType,boolean isSuccess){
		this.requestType = requestType;
		this.isSuccess = isSuccess;
		this.errorCode = 0;
	}
	public FOBResponse(int requestType,boolean isSuccess,int errorCode){
		this.requestType = requestType;
		this.isSuccess = isSuccess;
		this.errorCode = errorCode;
	}
	
	/**
	 * Network reuquest type that indentifies whitch network request the current response for
	 * @return
	 */
	public int getRequestType() {
		return requestType;
	}
	/**
	 * Whether current network request successful
	 * @return
	 */
	public boolean isSuccess() {
		return isSuccess;
	}
	
	/**
	 * Set response type success.
	 * @param isSuccess
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * When the value of isSuccess() is false it means request failed. You can get the detail by errorCode.
	 * @return
	 */
	public int getErrorCode() {
		return errorCode;
	}
	
	/**
	 * Set errorCode for Yodo1SDKResponse objcet.
	 * @param errorCode
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * The data that the network responses. It is a JSONObject object
	 * @return
	 */
	public JSONObject getResponse() {
		return response;
	}
	
	/**
	 * Set json format data for Yodo1SDKResponse objcet.
	 * @param jsonData
	 */
	public void setResponse(JSONObject jsonData) {
		this.response = jsonData;
	}
	
	/**
	 * Get raw byte array that the network responses.
	 * @return
	 */
	public byte[] getRawData() {
		return rawData;
	}
	/**
	 * Set raw byte array data for Yodo1SDKResponse objcet.
	 * @param rawData
	 */
	public void setRawData(byte[] rawData) {
		this.rawData = rawData;
	}
	/**
	 * Get string format data that the network responses.
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Set string format for Yodo1SDKResponse objcet.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setDataParse(IResponseParse responseParse){
		this.mResponseParse = responseParse;
	}
}
