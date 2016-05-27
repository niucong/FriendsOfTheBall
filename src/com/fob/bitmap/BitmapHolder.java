package com.fob.bitmap;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import android.graphics.Bitmap;

/**
 * Image 2 cache
 * First level, hard cache, the picture there is always memory,do not release,the number of fixed
 * The second level of the soft cache, the state of the system memory when needed to release the picture.
 * Picture key - value pairs (key-value) stored
 */
public class BitmapHolder 
{
	class BitmapInfo{
		static final byte from_cache = 1;
		static final byte from_sd = 2;
		static final byte from_net = 3;
		static final byte from_sd_external = 4;
		Bitmap data;
		byte from;
		short w;
		short h;
		
		BitmapInfo(Bitmap data,byte from,short w,short h){
			this.data = data;
			this.from = from;
			this.w = w;
			this.h = h;
		}
	}
	
	public BitmapInfo createBitmapInfo(Bitmap data,byte from,short w,short h){
		return new BitmapInfo(data, from, w, h);
	}
	
	private final static float DEFAULT_LOADFACTOR = 0.75f;
	private final static int DEFAULT_CACHECAPACITY = 16;
	//private static final int HARD_CACHE_CAPACITY = 16;
	
	/**
	 * First level cache to store a fixed number of pictures (as required to minimize the number, to meet the requirements)
	 */
	private HashMap<String, BitmapInfo> mHardBitmapCache;// = new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true);
	
	/**
	 * The second level cache，The LinkedHashMap storage LinkedHashMap is ordered (the default is the order of addition), you can use the LRU algorithm (Least Recently Used, Least Recently Used algorithm) to give priority to release
	 * Do not use the picture. Use SoftReference namely soft references for storing pictures, Java memory mechanism in the system memory is tight, SoftReference referenced object will be automatically released.
	 */
	private LinkedHashMap<String,SoftReference<BitmapInfo>> mSoftBitmapCache;
	
	/**
	 * Courtesy sometimes need to re-load the pictures have been released, because the picture loaded and the environment may vary, use the interface
	 */
//	BitmapProvider mBitmapProvider;
	
	/**
	 * First level cache size (image number)
	 */
	private int mHardCacheCapacity = DEFAULT_CACHECAPACITY;
	
	/**
	 * The second level cache size (image number).
	 */
	private int mSoftCacheCapacity = DEFAULT_CACHECAPACITY;
	
	/**
	 * 
	 * @param cacheCapacity capacity of buffer (Max enabled bitmaps).
	 */
	public BitmapHolder(int hardCacheCapacity,int softCacheCapacity)
	{
		mHardCacheCapacity = hardCacheCapacity;
		mSoftCacheCapacity = softCacheCapacity;
		mHardBitmapCache = new LinkedHashMap<String, BitmapInfo>(mHardCacheCapacity / 2, DEFAULT_LOADFACTOR, true);
		mSoftBitmapCache = new LinkedHashMap<String,SoftReference<BitmapInfo>>(mSoftCacheCapacity / 2, DEFAULT_LOADFACTOR, true);
	}
	
	public boolean isEmpty()
	{
		return mHardBitmapCache.isEmpty();
	}
	
//	public void setBitmapProvider(BitmapProvider provider)
//	{
//		mBitmapProvider = provider;
//	}
	
	/**
	 * Most do not use the picture  removed from a cache(one).
	 */
	private void removeEldestEntry()
	{
		//Only in the number of pictures of the actual storage is greater than the cache capacity until removed
		if(mHardBitmapCache.size() >= mHardCacheCapacity)
		{
			Iterator<String> keys = mHardBitmapCache.keySet().iterator();
			String key = keys.next();
			BitmapInfo img = mHardBitmapCache.get(key);
			
			//Removed from first level cache
			mHardBitmapCache.remove(key);
			
			if(img != null)
			{
				//Put remove's picture to the secondary cache
				mSoftBitmapCache.put(key, new SoftReference<BitmapInfo>(img));
				
				//If the second level cache is full, remove redundant picture
				if(mSoftBitmapCache.size() > mSoftCacheCapacity)
				{
					Iterator<String> softKeys = mSoftBitmapCache.keySet().iterator();
					if(softKeys.hasNext()){
						String softKey = softKeys.next();
						mSoftBitmapCache.remove(softKey);
					}
				}
			}
		}
	}
	
	

	public BitmapInfo getBitmap(String key) 
	{ 
	    // First try the hard reference cache 
	    synchronized (mHardBitmapCache)
	    { 
	        final BitmapInfo bitmap = mHardBitmapCache.get(key); 
	        if (bitmap != null) //find image from the first level cache and returns the result
	        { 
	        	//Move the image to last
	        	mHardBitmapCache.remove(key); 
	        	mHardBitmapCache.put(key, bitmap); 
	            return bitmap; 
	        } 
	    }

	    SoftReference<BitmapInfo> bitmapReference = mSoftBitmapCache.get(key); 
	    if (bitmapReference != null)
	    { 
	        final BitmapInfo bitmap = bitmapReference.get(); 
	        if (bitmap != null) //find image from the second level cache and returns the result
	        { 
	        	 return bitmap; 
	        } else
	        { 
	        	//Remove Image reference,if the image has been released
	        	mSoftBitmapCache.remove(key); 
	        } 
	    }
	    
	    //No results were found in the cache, and reload the picture
//	    if(mBitmapProvider != null)
//	    {
//	    	boolean loadBitmapDirectly = args.length > 0 && (Boolean)args[0]; //Is asynchronous loading ?
//	    	Bitmap img = mBitmapProvider.provideBitmap(this,key,loadBitmapDirectly);
//	    	
//	    	//The newly loaded image stored in the cache.
//	    	//Note, if you need asynchronous loading, where the return value may be empty, you need to manually the call putBitmap method of client programs in the completion of the asynchronous loading
//	    	if(img != null)
//	    	{
//	    		putBitmap(key, img);
//	    		return img;
//	    	}
//	    }
	    return null; 
	}

	public void putBitmap(String key, BitmapInfo bitmap)
	{ 
		if(bitmap != null)
		{
			synchronized (mHardBitmapCache) 
		    { 
		    	mHardBitmapCache.put(key, bitmap);
		    	removeEldestEntry();
		    } 
		}
	}
	
	public void removeBitmap(String key)
	{
		mHardBitmapCache.remove(key);
		mSoftBitmapCache.remove(key);
	}
	
	public void destroy(){
		mHardBitmapCache.clear();
		mSoftBitmapCache.clear();
		mHardBitmapCache = null;
		mSoftBitmapCache = null;
	}
	public void clear(){
		mHardBitmapCache.clear();
		mSoftBitmapCache.clear();
	}
}
