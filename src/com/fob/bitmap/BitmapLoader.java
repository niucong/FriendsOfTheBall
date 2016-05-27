package com.fob.bitmap;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.fob.bitmap.BitmapHolder.BitmapInfo;
import com.fob.tools.FOBUtils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public class BitmapLoader
{

	private static ExecutorService sThreadPool;
	private boolean isShutDown;	
	private BitmapHolder mBitmapHolder;	
	private Handler mInternalHandler;
	private BitmapPreprocessor mBitmapPreprocessor;
	
	private int max_W;
	private int max_H;
	
	private static final int CORE_POOL_SIZE = 2;
	private static final int POOL_SIZE = 2;
	private static final int KEEP_ALIVE_TIME = 5;
	private static final int QUEUE_SIZE = 200;
	
	private boolean isLoadFromNetWork = true;
	
	private static ExecutorService getThreadPool(){
		if(sThreadPool == null){
			sThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, POOL_SIZE,
					KEEP_ALIVE_TIME, TimeUnit.SECONDS,
					new ArrayBlockingQueue<Runnable>(QUEUE_SIZE));
		}
		return sThreadPool;
	}
	
	public void setImageMaxWH(int maxw,int maxh){
		max_W = maxw;
		max_H = maxh;
	}
	
	public BitmapLoader()
	{
		
		mInternalHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.obj!=null){
					MsgObj msgObj = (MsgObj)(msg.obj);
					if(msgObj.listener!=null){
						msgObj.listener.onLoadFinish(msgObj.bitmap);
					}
				}
			}
		};
	}
	
	public void setBitmapPreprocessor(BitmapPreprocessor processor){
		mBitmapPreprocessor = processor;
	}
	
	private Bitmap loadBitmap(String url,int w,int h) {
		Bitmap img = null;
		img = getBitmapFromMemCache(url);
		if(img!=null){
			return img;
		}
		String key = BitmapUtil.generateKey(url);
		if(url.startsWith(ImageCacheHelper.getExternalStorageDirectory())){
			img = BitmapUtil.optimizeBitmap(url, w, h);
			if(img!=null){
				if(mBitmapHolder!=null){
					BitmapInfo bitmapInfo = mBitmapHolder.createBitmapInfo(img, BitmapInfo.from_sd_external, (short)w, (short)h);
					mBitmapHolder.putBitmap(key,bitmapInfo);
				}
				return mBitmapPreprocessor != null ? mBitmapPreprocessor.preprocessBitmap(url, img) : img;
			}
		}
		
		img = ImageCacheHelper.loadCachedImage(url,w,h);
		if(img!=null){
			if(mBitmapHolder!=null){
				BitmapInfo bitmapInfo = mBitmapHolder.createBitmapInfo(img, BitmapInfo.from_sd, (short)w, (short)h);
				mBitmapHolder.putBitmap(key,bitmapInfo);
			}
			return mBitmapPreprocessor != null ? mBitmapPreprocessor.preprocessBitmap(url, img) : img;
		}else{
			if(isLoadFromNetWork){
				img = FOBUtils.getBitmapFromUrl(url);
				if(img!=null){
					if(mBitmapHolder!=null){
						BitmapInfo bitmapInfo = mBitmapHolder.createBitmapInfo(img, BitmapInfo.from_net,(short)w, (short)h);
						mBitmapHolder.putBitmap(key,bitmapInfo);
					}
					ImageCacheHelper.cacheImage(url, img, true);
					return mBitmapPreprocessor != null ? mBitmapPreprocessor.preprocessBitmap(url, img) : img;
				}
			}
		}
		return null;
	}
	
	public Bitmap getBitmapFromMemCache(String url){
		String key = BitmapUtil.generateKey(url);
		if(mBitmapHolder!=null){
			BitmapInfo bitmapInfo = mBitmapHolder.getBitmap(key);
			if(bitmapInfo!=null){
				return bitmapInfo.data;
			}
		}
		return null;
	}
	
	public synchronized void clear(){
		if(mBitmapHolder != null){
			mBitmapHolder.clear();
		}
	}
	
	public synchronized void shutDown()
	{

		isShutDown = true;
		notifyAll();
		
		if(mBitmapHolder != null){
			mBitmapHolder.destroy();
			mBitmapHolder = null;
		}

	}
	
	public synchronized void waitingForEnd(){
		notifyAll();
	}
	
	private synchronized void addTask(String url,int w,int h,BitmapLoadListener listener)
	{
		if(!isShutDown){
			try {
				

			BitmapLoader.getThreadPool().execute(new RunnableWorker(url,w,h,listener));
			} catch (Exception e) {
			}
		}

	}
	
	public interface BitmapLoadImplementer
	{
		public Bitmap loadBitmap(BitmapLoader loader,Object... args);
	}
	
	public interface Callback
	{
		public void onBitmapLoadSuccess(BitmapLoader loader,Bitmap img,Object... args);
		public void onBitmapLoadFailed(BitmapLoader loader,Object... args);
	}
	
	public interface BitmapPreprocessor{
		public Bitmap preprocessBitmap(String url,Bitmap bitmap);
	}

	public void setBitmapHolder(BitmapHolder bitmapHolder) {
		this.mBitmapHolder = bitmapHolder;
	}
	
	
	public void loadBitmap(String url,int w,int h,BitmapLoadListener listener){
		if(listener!=null){
			listener.onLoadStart();
		}
		addTask(url,w,h,listener);
	}
	
	public void loadBitmap(String url,BitmapLoadListener listener){
		loadBitmap(url,max_W,max_H,listener);
	}
	
	public void setIsLoadFromNetWork(boolean isLoadFromNetWork){
		this.isLoadFromNetWork = isLoadFromNetWork;
	}
	
	private class MsgObj{
		private Bitmap bitmap;
		private BitmapLoadListener listener;
	}
	
	
	class RunnableWorker implements Runnable{
		private BitmapLoadListener mlistener;
		private String mUrl;
		private int w;
		private int h;
		public RunnableWorker(String url,int w,int h,BitmapLoadListener listener){
			this.mUrl = url;
			this.mlistener = listener;
			this.w = w;
			this.h = h;
		}

		@Override
		public void run() {
			Bitmap img = null;
			if(mUrl==null || mUrl.trim().length() == 0){
			}else{
				img = loadBitmap(mUrl,w,h);
				
			}

			MsgObj msgObj = new MsgObj();
			msgObj.bitmap = img;
			msgObj.listener = mlistener;
			Message msg = new Message();
			msg.obj = msgObj;
			mInternalHandler.sendMessage(msg);
		}
	}
}
