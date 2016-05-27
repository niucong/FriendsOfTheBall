package com.fob.bitmap;

import android.graphics.Bitmap;


public interface BitmapLoadListener {
	public void onLoadStart();
	public void onLoadFinish(Bitmap bitmap);
}
