package com.fob.balls;

/**
 * The SDK error code list
 * @author talaya
 *
 */
public interface FOBErrorCode{
	public static final int ERRORCODE_SUCCESS = 0;
	
	public static final int ERRORCODE_UNKNOWN_ERROR = -1;
	public static final int ERRORCODE_NETWORK_UNAVAILABLE = 10101;
	public static final int ERRORCODE_DATAFORMAT_ERROR = 10102;
	public static final int ERRORCODE_CANCEL = 10103;
	public static final int ERRORCODE_CONNECT_FAILED = 10104;
	public static final int FOB_ERRORCODE_INVALID_ACCESSTOKEN = 40305;

}
