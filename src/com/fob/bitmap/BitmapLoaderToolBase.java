package com.fob.bitmap;

import com.fob.adapter.base.FOBAdapterItemBase;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public abstract class BitmapLoaderToolBase {
	
	protected BitmapLoaderInfoBase loadInfo;
	private BitmapLoader mIconsLoader;
	private FOBAdapterItemBase mAdapterItem; 
	protected ImageView imageView;
	private boolean isPressStyle; 
	
	protected abstract void initView(View view,boolean bitmapIsLoaded);
	protected abstract void setImageBitmap(View view,Bitmap bitmap);
	protected abstract void setReplaceBitmap(View view);
	protected abstract ImageView getTouchImageView(View view);
	
	protected int replaceResId;
	
	private BitmapLoadListener mBitmapLoadListener;
	//private BitmapChangeListener mBitmapChangeListener;
	
//	public interface BitmapChangeListener{
//		public final static int BITMAPSOURCE_MEMERY_CACHE = 0;
//		public final static int BITMAPSOURCE_FILE_CACHE = 1;
//		public final static int BITMAPSOURCE_NETWORK = 2;
//		
//		public void bitmapWillChange(Bitmap bitmap,int bitmapSource);
//	}
	
	public BitmapLoaderToolBase(FOBAdapterItemBase adapterItem,BitmapLoader iconsLoader){
		this.mAdapterItem = adapterItem;
		this.mIconsLoader = iconsLoader;
		createIconLoadInfo();
	}
	
	public BitmapLoaderToolBase(ImageView imageView,BitmapLoader iconsLoader){
		this.imageView = imageView;
		this.mIconsLoader = iconsLoader;
		createIconLoadInfo();
	}
	
//	public void setBitmapChangeListener(BitmapChangeListener listener){
//		mBitmapChangeListener = listener;
//	}
	
	public void setBitmapLoadListener(BitmapLoadListener listener){
		mBitmapLoadListener = listener;
	}
	
	public void setIsLoadFromNetWork(boolean isLoadFromNetWork){
		if(this.mIconsLoader!=null){
			this.mIconsLoader.setIsLoadFromNetWork(isLoadFromNetWork);
		}
	}
	
	public void setReplaceResId(int replaceResId){
		this.replaceResId = replaceResId;
	}
	
	protected int getReplaceResId(){
		return replaceResId;
	}
	
	public boolean isLoadSuccess(){
		return loadInfo.isSuccess();
	}
	
	private void createIconLoadInfo(){
		loadInfo = new BitmapLoaderInfoBase();
		loadInfo.setListener(new BitmapLoadListener() {
			@Override
			public void onLoadStart() {
				if(mBitmapLoadListener != null){
					mBitmapLoadListener.onLoadStart();
				}
				loadInfo.setLoading(true);
			}
			
			@Override
			public void onLoadFinish(Bitmap bitmap){
				if(mBitmapLoadListener != null){
					mBitmapLoadListener.onLoadFinish(bitmap);
				}
//				if(mBitmapChangeListener != null){
//					mBitmapChangeListener.bitmapWillChange(bitmap, BitmapChangeListener.BITMAPSOURCE_NETWORK);
//				}
				loadInfo.setLoading(false);
				// if(bitmap!=null){
				// loadInfo.setSuccess(true);
				// // YoAdapter adapter = getAdapter();
				// if(mAdapterItem!=null){
				// if(mAdapterItem.getAdapter()!=null){
				//
				// mAdapterItem.getAdapter().updateView(mAdapterItem.getOrderId());
				// onSettedBitmap();
				// }
				// }else if(imageView!=null){
				// imageView.setImageBitmap(bitmap);
				// onSettedBitmap();
				// }
				// }
			}
		});
	}
	
	protected void onSettedBitmap(){
		
	}
	public boolean loadIcon(String url,View imageSwitch){
		return loadIcon(url, imageSwitch, true);
	}
	
	public Bitmap getBitmapCache(String url){
		return mIconsLoader.getBitmapFromMemCache(url);
	}
	
	public boolean loadIcon(String url,View imageSwitch,boolean isLoadFromNetOrSdCard){
		Bitmap bitmap = mIconsLoader.getBitmapFromMemCache(url);
		boolean hasCache = (bitmap != null);
		initView(imageSwitch,hasCache);
		setEvent(imageSwitch);
		
		
//		YLog.i("test", (bitmap!=null)+":"+loadInfo.isSuccess());
		if(loadInfo.isSuccess() && bitmap!=null){
//			if(mBitmapChangeListener != null){
//				mBitmapChangeListener.bitmapWillChange(bitmap, BitmapChangeListener.BITMAPSOURCE_MEMERY_CACHE);
//			}
			setImageBitmap(imageSwitch,bitmap);
		}else{
			setReplaceBitmap(imageSwitch);
			if(isLoadFromNetOrSdCard){
				if(loadInfo.canLoad()){
					mIconsLoader.loadBitmap(url,loadInfo.getListener());
				}
			}
		}
		return hasCache;
	}
	private void setEvent(View imageSwitcher){
		if(isPressStyle){
			imageSwitcher.setOnTouchListener(getImageOnTouchListener());
		}
	}
	
	private View.OnTouchListener mImageOnTouchListener;
	private View.OnTouchListener getImageOnTouchListener(){
		if(mImageOnTouchListener==null){
			mImageOnTouchListener = new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					ImageView iv2 = getTouchImageView(v);
					//ImageView iv2 = (ImageView)v;
					int action = event.getAction();
					if(action == MotionEvent.ACTION_DOWN){
						iv2.getDrawable().setAlpha(128);
						v.invalidate();
					}else if(action == MotionEvent.ACTION_UP || 
							action == MotionEvent.ACTION_OUTSIDE ||
							action == MotionEvent.ACTION_CANCEL){
						iv2.getDrawable().setAlpha(255);
						v.invalidate();
					}
					return false;
				}
			}; 
		}
		return mImageOnTouchListener;
	}
	
	
	public void setPressStyle(boolean isPressStyle) {
		this.isPressStyle = isPressStyle;
	}
	public boolean isPlayedAnimation(){
		return loadInfo.isBitampSetted();
	}
}
