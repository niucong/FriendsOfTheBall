package com.fob.bitmap;


public class BitmapLoaderInfoBase {
	private BitmapLoadListener listener;
	
	private int retryCount;
	
	private int loadMaxCount = 3;
	
//	private byte[] data;
	
	private boolean isLoading;
	
	private boolean isBitampSetted;
	
	private boolean isSuccess;
	
	
	public BitmapLoadListener getListener() {
		return listener;
	}

	public void setListener(BitmapLoadListener listener) {
		this.listener = listener;
	}
	
	public int getRetryCount() {
		return retryCount;
	}

	public void addRetryCount() {
		retryCount++;
	}
	
	public boolean canLoad(){
		return !isLoading() && (retryCount<=loadMaxCount);
	}
	
//	public byte[] getData() {
//		return data;
//	}
	
//	public Bitmap parseBitmap(){
//		if(data == null){
//			return null;
//		}else{
//			return BitmapFactory.decodeByteArray(data, 0, data.length);
//		}
//	}
	
	public boolean isSuccess(){
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess){
		this.isSuccess = isSuccess;
	}
//	public void setBitmap(Bitmap bitmap){
//		if(bitmap!=null){
//			data = BitmapUtil.bitmapToByte(bitmap);
//		}
//	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public boolean isBitampSetted() {
		return isBitampSetted;
	}

	public void setBitampSetted(boolean isBitampSetted) {
		this.isBitampSetted = isBitampSetted;
	}
	
}
