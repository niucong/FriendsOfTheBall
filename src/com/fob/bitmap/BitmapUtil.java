package com.fob.bitmap;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class BitmapUtil
{
	private final static String TAG = "BitmapUtil";
	public final static String generateKey(String s)
	{
		long hashcode = s.hashCode();
		if(hashcode < 0)
		{
			hashcode = - hashcode;
		}
		return String.valueOf(hashcode);
	}

	public static byte[] bitmapToByte(Bitmap bitmap)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}

	public static byte[] bitmapToJpeg(Bitmap bitmap,int quality)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
		bitmap.recycle();
		bitmap = null;
		return stream.toByteArray();
	}


	public static Bitmap optimizeBitmap(byte[] data, int maxWidth, int maxHeight)
	{
		if(maxWidth == 0)
		{
			maxWidth = BitmapConst.BITMAP_MAX_W;
		}
		if(maxHeight == 0)
		{
			maxHeight = BitmapConst.BITMAP_MAX_H;
		}

		Bitmap result = null;
		int length = data.length;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		result = BitmapFactory.decodeByteArray(data, 0, length, options);
		int widthRatio = (int) Math.ceil(options.outWidth / maxWidth);
		int heightRatio = (int) Math.ceil(options.outHeight / maxHeight);
		if(widthRatio > 1 || heightRatio > 1)
		{
			if(widthRatio > heightRatio)
			{
				options.inSampleSize = widthRatio;
			}
			else
			{
				options.inSampleSize = heightRatio;
			}
		}
		options.inJustDecodeBounds = false;
		result = BitmapFactory.decodeByteArray(data, 0, length, options);
		return result;
	}

	public static Bitmap optimizeBitmap(String filepath, int maxWidth, int maxHeight)
	{
		int degree = readPictureDegree(filepath);
		
		if(maxWidth == 0)
		{
			maxWidth = BitmapConst.BITMAP_MAX_W;
		}
		if(maxHeight == 0)
		{
			maxHeight = BitmapConst.BITMAP_MAX_H;
		}
		try
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			FileInputStream is = new FileInputStream(filepath);
			if(is != null)
			{
				BitmapFactory.decodeStream(is, null, options);
				is.close();
			}

			int widthRatio = (int) Math.ceil((double)options.outWidth / (double)maxWidth);
			int heightRatio = (int) Math.ceil((double)options.outHeight / (double)maxHeight);
			if(widthRatio > 1 || heightRatio > 1)
			{
				if(widthRatio > heightRatio)
				{
					options.inSampleSize = widthRatio;
				}
				else
				{
					options.inSampleSize = heightRatio;
				}
			}
			options.inJustDecodeBounds = false;

			is = new FileInputStream(filepath);
			if(is != null)
			{
				Bitmap img = BitmapFactory.decodeStream(is, null, options);
				is.close();
				
				img = angleMatrixBitmap(img,degree);
				return img;
			}
		}
		catch (Exception e)
		{
		}

		return null;
	}

	public static Bitmap angleMatrixBitmap(Bitmap img,int degree) {
		if(degree > 0){
            Matrix m = new Matrix();  
            int width = img.getWidth();  
            int height = img.getHeight();  
            m.setRotate(degree); 
            img = Bitmap.createBitmap(img, 0, 0, width, height,m, true);
		}
		
		return img;
	}

	public static void preloadBitmaps(String[] urls,BitmapLoader bitmapLoader,final BitmapsLoadListener listener)
	{
		class RecordCount
		{
			int finishNum;
			int successNum;
			synchronized void finishNumAdd()
			{
				finishNum++;
			}
			synchronized void successNumAdd()
			{
				successNum++;
			}
			int getFinishNum()
			{
				return finishNum;
			}
			int getSuccessNum()
			{
				return successNum;
			}
		}
		final BitmapLoader loader;
		final boolean isInnerLoader;
		if(bitmapLoader==null)
		{
			isInnerLoader = true;
			loader = new BitmapLoader();
			loader.setBitmapHolder(new BitmapHolder(12, 12));
		}
		else
		{
			isInnerLoader = false;
			loader = bitmapLoader;
		}

		if(urls!=null && urls.length > 0)
		{

			final int len = urls.length;
			final RecordCount record = new RecordCount();
			for(int i = 0; i < len; i++)
			{
				String imageUrl = urls[i];
				if(imageUrl!=null && imageUrl.length()>0)
				{
					BitmapLoaderInfoBase loadInfo = new BitmapLoaderInfoBase();

					loadInfo.setListener(new BitmapLoadListener()
					{
						@Override
						public void onLoadFinish(Bitmap bitmap)
						{
							record.finishNumAdd();
							if(bitmap!=null)
							{
								record.successNumAdd();
							}
							if(record.getFinishNum()>=len)
							{
								listener.onLoadFinish(record.getFinishNum(), record.getSuccessNum());
								if(isInnerLoader)
								{
									loader.shutDown();
								}
							}
						}

						@Override
						public void onLoadStart()
						{

						}
					});
					loader.loadBitmap(imageUrl,loadInfo.getListener());
				}
				else
				{
					record.finishNumAdd();
				}
			}
			if(record.getFinishNum()>=len)
			{
				listener.onLoadFinish(record.getFinishNum(), record.getSuccessNum());
				if(isInnerLoader)
				{
					loader.shutDown();
				}
			}
		}
		else
		{
			listener.onLoadFinish(0, 0);
		}
	}

	public static int readPictureDegree(String path)
	{
		int degree = 0;
		try
		{
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation)
			{
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		}
		catch (IOException e)
		{
		}
		return degree;
	}
}
