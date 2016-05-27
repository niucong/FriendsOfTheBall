package com.fob.bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.fob.tools.FOBConst;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
public class ImageCacheHelper
{

	private static ArrayList<String> sImageSDCacheList;
	private static String sHomeDir;
	private static String sImgDir;
	private final static String IMAGE_DIR = "image_cache/";
	private final static String IMAGE_EXTENSION = ".cache";

	public final static int MAX_ENABLED_IMAGES = 5000;
	private static int sMaxEnabledImages = MAX_ENABLED_IMAGES;

	public final static void setHomeDir(String dir)
	{

		if(!dir.startsWith("/"))
		{
			dir = "/" + dir;
		}

		if(!dir.endsWith("/"))
		{
			dir += "/";
		}

		sHomeDir = dir;
		sImgDir = sHomeDir + IMAGE_DIR;
		initImageCache();
	}

	public final static void setMaxEnabledImages(int max)
	{
		sMaxEnabledImages = max;
	}

	private final static void initImageCache()
	{
		if(sHomeDir == null)
		{
			return;
		}

		sImageSDCacheList = new ArrayList<String>();
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			String dir = Environment.getExternalStorageDirectory() + sImgDir;
			File fDir = new File(dir);
			if(!fDir.exists())
			{
				boolean createdDir = fDir.mkdirs();

				if(createdDir)
				{
					//place a no-media file to prevent system media scanning.
					try
					{
						OutputStream os = new FileOutputStream(".nomedia");
						os.close();
					}
					catch(Exception e)
					{
					}
				}
			}
			else
			{
				File[] images = fDir.listFiles();
				int len = images.length;
				for(int i = 0; i < len; i++)
				{
					String name = images[i].getName();
					if(name.endsWith(IMAGE_EXTENSION))
					{
						sImageSDCacheList.add( name.substring(0,name.length() - IMAGE_EXTENSION.length()));
					}
				}
			}
		}
	}

	public final static void cacheImage(final String url,final Bitmap bitmap,final boolean async)
	{
		if(async)
		{
			new Thread()
			{
				public void run()
				{
					cacheImageImpl(url,bitmap);
				}
			} .start();
		}
		else
		{
			cacheImageImpl(url,bitmap);
		}
	}


	public final static void cacheImageImpl(String url,Bitmap bitmap)
	{
		if(sHomeDir == null)
		{
			return;
		}

		try
		{
			//long time =  System.currentTimeMillis();

			//String name = String.valueOf(url.hashCode());
			if(sImageSDCacheList.size() >= sMaxEnabledImages )
			{
				return;
			}

			String name = BitmapUtil.generateKey(url);
			if(sImageSDCacheList.contains(name))
			{
				return;
			}

			sImageSDCacheList.add(name);
			//s_imageSDCacheMap.put(url, time);
			String dir = Environment.getExternalStorageDirectory() + sImgDir;
			File fDir = new File(dir);
			if(!fDir.exists())
			{
				fDir.mkdirs();
			}

			String file = fDir + "/" + name + IMAGE_EXTENSION;
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
			//\\YLog.v(TAG, "saved image to " + file + " url = " + url);
		}
		catch(Exception e)
		{
		}
	}

	public final static Bitmap loadCachedImage(String url)
	{
		return loadCachedImage(url,BitmapConst.BITMAP_MAX_W,BitmapConst.BITMAP_MAX_H);
	}

	public final static Bitmap loadCachedImage(String url,int w,int h)
	{
		if(sHomeDir == null)
		{
			return null;
		}

		String fileName = BitmapUtil.generateKey(url);
		if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			return null;
		}
		try
		{
			File f = new File(Environment.getExternalStorageDirectory() + sImgDir + "/" + fileName + IMAGE_EXTENSION);
			//YLog.v(TAG, "load from " + f.toString());
			if(!f.exists())
			{
				sImageSDCacheList.remove(fileName);
				//YLog.v(TAG, "image cache not found!!!");
				return null;
			}
			else
			{
				if(!sImageSDCacheList.contains(fileName))
				{
					sImageSDCacheList.add(fileName);
				}
//				InputStream is = new FileInputStream(f.toString());
//				//YLog.v(TAG, "is = " + is);
//				if(is == null)
//				{
//					f.delete();
//					sImageSDCacheList.remove(fileName);
//					return null;
//				}
//				if(is.available() == 0)
//				{
//					sImageSDCacheList.remove(fileName);
//					is.close();
//					f.delete();
//					return null;
//				}
//				byte[] data = loadByteArray(is);
//				is.close();
//				Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length);
//
//				return img;
				return BitmapUtil.optimizeBitmap(f.toString(), w, h);
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}

	public final static boolean isImageCached(String url)
	{
		String key = BitmapUtil.generateKey(url);
		if(sHomeDir == null || sImageSDCacheList == null ||
		        !sImageSDCacheList.contains(key) ||
		        !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			return false;
		}
		try
		{
			File f = new File(Environment.getExternalStorageDirectory() + sImgDir + "/" + key + IMAGE_EXTENSION);
			//YLog.v(TAG, "load from " + f.toString());
			if(!f.exists())
			{
				sImageSDCacheList.remove(key);
				//YLog.v(TAG, "image cache not found!!!");
				return false;
			}
			InputStream is = new FileInputStream(f.toString());
			//YLog.v(TAG, "is = " + is);
			if(is == null)
			{
				f.delete();
				sImageSDCacheList.remove(key);
				return false;
			}
			if(is.available() == 0)
			{
				sImageSDCacheList.remove(key);
				is.close();
				f.delete();
				return false;
			}
			is.close();
		}
		catch(Exception e)
		{
		}
		return true;
	}
	public final static String getImageCachedURL(String url)
	{
		String key = BitmapUtil.generateKey(url);
		if(sHomeDir == null || sImageSDCacheList == null ||
		        !sImageSDCacheList.contains(key) ||
		        !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			return "";
		}
		try
		{
			File f = new File(Environment.getExternalStorageDirectory() + sImgDir + "/" + key + IMAGE_EXTENSION);
			//YLog.v(TAG, "load from " + f.toString());
			if(!f.exists())
			{
				sImageSDCacheList.remove(key);
				//YLog.v(TAG, "image cache not found!!!");
				return "";
			}
			InputStream is = new FileInputStream(f.toString());
			//YLog.v(TAG, "is = " + is);
			if(is == null)
			{
				f.delete();
				sImageSDCacheList.remove(key);
				return "";
			}
			if(is.available() == 0)
			{
				sImageSDCacheList.remove(key);
				is.close();
				f.delete();
				return "";
			}
			is.close();
			return f.toString();
		}
		catch(Exception e)
		{
		}
		return "";
	}


	public final static boolean clearCache()
	{
		if(sHomeDir == null)
		{
			return false;
		}
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			String dir = Environment.getExternalStorageDirectory() + sImgDir;
			File fDir = new File(dir);
			if(fDir.exists())
			{
				File[] files = fDir.listFiles();
				int len = files.length;
				for(int i = 0; i < len; i++)
				{
					files[i].delete();
				}
				sImageSDCacheList.clear();
			}
			return true;
		}
		return false;
	}



	public final static byte[] loadByteArray(InputStream is,Object... args) throws Exception
	{
		if(is != null)
		{
			int bufSize = 1024;
			if(args.length > 0)
			{
				bufSize = (Integer)args[0];
			}
			byte[] buf = new byte[bufSize];
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int readBytes = -1;
			while(true)
			{
				readBytes = is.read(buf);
				if(readBytes == -1)
				{
					break;
				}
				bos.write(buf,0,readBytes);
			}
			return bos.toByteArray();
		}
		return null;
	}

	public final static String getExternalStorageDirectory()
	{
		return Environment.getExternalStorageDirectory().toString();
	}

	public static void clearImageCache(Context context)
	{
		try
		{
			if(context.getPackageName().startsWith(FOBConst.CONST_TEST_PKG_NAME))
			{
				if(sImgDir==null)
				{
					return;
				}
				String dir = Environment.getExternalStorageDirectory() + sImgDir;
				File file = new File(dir);
				delete(file);
			}
		}
		catch (Exception e)
		{
		}
	}

	public static void delete(File file)
	{
		if (file.isFile())
		{
			file.delete();
			return;
		}
		if(file.isDirectory())
		{
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0)
			{
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++)
			{
				delete(childFiles[i]);
			}
			file.delete();
		}
	}
}
