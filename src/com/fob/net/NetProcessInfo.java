package com.fob.net;

public class NetProcessInfo {
	
	public final static int PROCESS_PREPARE = 0;
	public final static int PROCESS_UPLOAD = 1;
	public final static int PROCESS_DOWNLOAD = 2;
	public final static int PROCESS_ALLDONE = 3;
	
	public int process;
	public int bytes2Upload;
	public int bytes2Download;
	public int uploadedBytes;
	public int downloadBytes;
}
