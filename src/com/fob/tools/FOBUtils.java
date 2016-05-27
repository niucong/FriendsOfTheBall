package com.fob.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Hashtable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fob.bitmap.ImageCacheHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class FOBUtils
{

	/**
	 * image cache path
	 */
	private final static String DIR_DATACACHE = "fob/images/";

	public final static void sdkInit()
	{
		ImageCacheHelper.setHomeDir(DIR_DATACACHE);
	}

	private static Hashtable<String,Object> sSavedObjects = new Hashtable<String,Object>();
	private static int sKeyTicker;
	public final static String saveObject(Object object)
	{
		if(object == null)
		{
			return null;
		}
		String key = String.valueOf(System.currentTimeMillis()) + sKeyTicker++;
		sSavedObjects.put(key, object);
		return key;
	}

	public final static Bitmap getSavedBitmap(String key,boolean remove)
	{
		Bitmap bmp = (Bitmap)sSavedObjects.get(key);
		if(remove)
		{
			sSavedObjects.remove(key);
		}
		return bmp;
	}

	public final static byte[] getSavedData(String key,boolean remove)
	{
		byte[] data = (byte[])sSavedObjects.get(key);
		if(remove)
		{
			sSavedObjects.remove(key);
		}
		return data;
	}

	public final static Object getSavedObject(String key,boolean remove)
	{
		Object obj = sSavedObjects.get(key);
		if(remove)
		{
			sSavedObjects.remove(key);
		}
		return obj;
	}

	public final static void removeObject(String key)
	{
		sSavedObjects.remove(key);
	}

	public final static String imageUri2File(Context context,Uri uri)
	{
		Cursor c = context.getContentResolver().query(uri, new String[] {MediaStore.Images.Media.DATA}, null, null, null);
		int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		c.moveToFirst();
		String path = c.getString(index);
		c.close();
		return path;
	}

	public final static String videoUri2File(Context context,Uri uri)
	{
		Cursor c = context.getContentResolver().query(uri, new String[] {MediaStore.Video.Media.DATA}, null, null, null);
		int index = c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
		c.moveToFirst();
		String path = c.getString(index);
		c.close();
		return path;
	}

	public final static Bitmap loadBitmapFromUri(Context context,Uri uri,Object... sizeLimit)
	{
		String schema = uri.getScheme();
		String file = null;
		if("content".equals(schema))
		{
			file = imageUri2File(context,uri);
		}
		else if("file".equals(schema))
		{
			file = uri.getPath();
		}
		if(file != null)
		{
			File f = new File(file);
			if(f.exists())
			{
				if(sizeLimit.length > 0)
				{
					int maxWidth = (Integer)sizeLimit[0];
					int maxHeight = maxWidth;
					if(sizeLimit.length > 1)
					{
						maxHeight = (Integer)sizeLimit[1];
					}
					return loadBitmapWithSizeLimit(file,maxWidth,maxHeight);
				}
				else
				{
					return BitmapFactory.decodeFile(file);
				}
			}
		}
		return null;
	}

	public final static byte[] loadVideoDataFromUri(Context context,Uri uri)
	{
		String schema = uri.getScheme();
		String file = null;
		if("content".equals(schema))
		{
			file = videoUri2File(context,uri);
		}
		else if("file".equals(schema))
		{
			file = uri.getPath();
		}

		if(file != null)
		{
			File f = new File(file);
			if(f.exists())
			{
				try
				{
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
					byte[] data = new byte[bis.available()];
					bis.read(data);
					bis.close();
					bis = null;
					return data;
				}
				catch(Exception e)
				{
				}
			}
		}
		return null;
	}

	public final static Bitmap loadBitmapWithSizeLimit(String file,int maxWidth,int maxHeight)
	{
		try
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			FileInputStream is = new FileInputStream(file);
			if(is != null)
			{
				BitmapFactory.decodeStream(is, null, options);
				is.close();
			}

			options.inJustDecodeBounds = false;
			int outW = options.outWidth;
			int outH = options.outHeight;
			//options.inPreferredConfig = Bitmap.Config.ALPHA_8;
			if(outW > maxWidth || outH > maxHeight)
			{
				options.inSampleSize = Math.max(outW / maxWidth, outH / maxHeight);
				if(options.inSampleSize < 2)
				{
					options.inSampleSize = 2;
				}
			}

			is = new FileInputStream(file);
			if(is != null)
			{
				Bitmap img = BitmapFactory.decodeStream(is, null, options);
				is.close();
				return img;
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}

	public static Bitmap getBitmapFromUrl(String url)
	{
		Bitmap image = null;
		{
			try
			{
				HttpGet request = new HttpGet(url);
				DefaultHttpClient client = new DefaultHttpClient(request.getParams());
				HttpResponse response = client.execute(request);
				if(response.getStatusLine().getStatusCode() == 200)
				{
					HttpEntity entity = response.getEntity();
					BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
					InputStream is = bufferedEntity.getContent();

					/*InputStream is = entity.getContent();*/

					/*if(options != null)
					{
						image =  BitmapFactory.decodeStream(is,null,options);
					}
					else
					{
						image = BitmapFactory.decodeStream(is);
					}*/
					image = BitmapFactory.decodeStream(is);


					is.close();
				}
			}
			catch(Exception e)
			{
			}
		}
		return image;
	}


	public static Bitmap getBitmapFromUrlWithSizeLimit(String url,int maxWidth,int maxHeight)
	{
		Bitmap image = null;
		{
			try
			{
				HttpGet request = new HttpGet(url);
				DefaultHttpClient client = new DefaultHttpClient(request.getParams());
				HttpResponse response = client.execute(request);
				if(response.getStatusLine().getStatusCode() == 200)
				{
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					HttpEntity entity = response.getEntity();
					BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
					InputStream is = bufferedEntity.getContent();
					if(is != null)
					{
						BitmapFactory.decodeStream(is, null, options);
						is.close();
					}

					options.inJustDecodeBounds = false;
					int outW = options.outWidth;
					int outH = options.outHeight;
					//options.inPreferredConfig = Bitmap.Config.ALPHA_8;
					if(outW > maxWidth || outH > maxHeight)
					{
						options.inSampleSize = Math.max(outW / maxWidth, outH / maxHeight);
						if(options.inSampleSize < 2)
						{
							options.inSampleSize = 2;
						}
					}

					//reconnect.
					HttpGet request2 = new HttpGet(url);
					DefaultHttpClient client2 = new DefaultHttpClient(request2.getParams());
					HttpResponse response2 = client2.execute(request2);
					if(response2.getStatusLine().getStatusCode() == 200)
					{

						HttpEntity entity2 = response2.getEntity();
						BufferedHttpEntity bufferedEntity2 = new BufferedHttpEntity(entity2);
						InputStream is2 = bufferedEntity2.getContent();
						if(is2 != null)
						{
							Bitmap img = BitmapFactory.decodeStream(is2, null, options);
							is2.close();
							return img;
						}
					}
				}
			}
			catch(Exception e)
			{
			}
		}
		return image;
	}

	public static Bitmap toGrayscale(Bitmap bmpOriginal)
	{
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx)
	{
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}




	/**
	 * load cache data
	 * @param name	cache data name
	 * @return	cache data
	 */
	public final static String loadSavedResponse(Context context,String name)
	{
		//String key = "loadSavedResponse";
		//YLog.recordTime(key);
		try
		{
			String s = null;
			DBOpenHelper dbOpenHelper = new DBOpenHelper(context,DBOpenHelper.DB_NAME,null,DBOpenHelper.VERSION);
			SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
			Cursor c = db.query(DBOpenHelper.TABLE_RESPONSE, new String[] {DBOpenHelper.COL_RESPONSE},
			                    DBOpenHelper.COL_NAME + "=?", new String[] {name}, null, null, null);
			if(c != null)
			{
				if(c.moveToFirst())
				{
					s = c.getString(0);
				}
				c.close();
			}
			db.close();
			//YLog.v(TAG, "Load Response: time->" + (YLog.timeElapse(key)));
			return s;
		}
		catch(Exception e)
		{
		}
		//YLog.v(TAG, "Load Response: time->" + (YLog.timeElapse(key)));
		return null;
	}

	/**
	 * cache data
	 * @param name cache data name
	 * @param response	cache data content
	 */
	public final static void saveResponse(Context context,String name,String response)
	{
		try
		{
			DBOpenHelper dbOpenHelper = new DBOpenHelper(context,DBOpenHelper.DB_NAME,null,DBOpenHelper.VERSION);
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

			ContentValues values = new ContentValues();

			values.put(DBOpenHelper.COL_NAME, name);
			values.put(DBOpenHelper.COL_RESPONSE, response);

			Cursor c = db.query(DBOpenHelper.TABLE_RESPONSE, new String[] {DBOpenHelper.COL_NAME},
			                    DBOpenHelper.COL_NAME + "=?", new String[] {name}, null, null, null);
			boolean saved = c.moveToFirst();
			c.close();
			if(saved)
			{
				db.update(DBOpenHelper.TABLE_RESPONSE, values, DBOpenHelper.COL_NAME + "=?", new String[] {name});
			}
			else
			{
				db.insert(DBOpenHelper.TABLE_RESPONSE, null, values);
			}
			db.close();
		}
		catch(Exception e)
		{
		}
	}

	public final static void deleteResponse(Context context,String name)
	{
		try
		{
			DBOpenHelper dbOpenHelper = new DBOpenHelper(context,DBOpenHelper.DB_NAME,null,DBOpenHelper.VERSION);
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			db.delete(DBOpenHelper.TABLE_RESPONSE, DBOpenHelper.COL_NAME + "=?", new String[] {name});
			db.close();
		}
		catch(Exception e)
		{
		}
	}

	public final static void clearAllResponse(Context context)
	{
		try
		{
			if(context.getPackageName().startsWith(FOBConst.CONST_TEST_PKG_NAME))
			{
				DBOpenHelper dbOpenHelper = new DBOpenHelper(context,DBOpenHelper.DB_NAME,null,DBOpenHelper.VERSION);
				SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
				db.delete(DBOpenHelper.TABLE_RESPONSE, null, null);
				db.close();
			}
		}
		catch(Exception e)
		{
		}
	}

	static class DBOpenHelper extends SQLiteOpenHelper
	{

		public final static String DB_NAME = "yodo1_sdk.db";

		public final static int VERSION = 1;

		final static String TABLE_RESPONSE = "yodo1_api_responses";
		//final static String TABLE_IMAGES = "yodo1_images";

		final static String COL_NAME = "name";
		final static String COL_RESPONSE = "response";
		final static String COL_URL = "url";
		final static String COL_IMAGE = "image";


		public DBOpenHelper(Context context, String name, CursorFactory factory, int version)
		{
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			createTables(db);
		}


		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			switch(newVersion)
			{
			case 1:
				createTables(db);
				break;
			}
		}

		/**
		 * create table
		 * @param db
		 */
		private void createTables(SQLiteDatabase db)
		{

			String sqlString = "CREATE TABLE IF NOT EXISTS " + TABLE_RESPONSE +
			                   " (" + COL_NAME + " TEXT, " + COL_RESPONSE + " TEXT);";
			db.execSQL(sqlString);
		}
	}

	public final static boolean isTablet(Context context)
	{
		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE)
		{
			return true;
		}
		else
		{
			boolean xlarge = (context.getResources().getConfiguration().screenLayout
			                  & Configuration.SCREENLAYOUT_SIZE_MASK)
			                 >= Configuration.SCREENLAYOUT_SIZE_LARGE;
			if(xlarge)
			{
				//DisplayMetrics metrics = new DisplayMetrics();
				//Activity activity = (Activity) context;
				//activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
				DisplayMetrics metrics = context.getResources().getDisplayMetrics();
				// MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
				// DENSITY_TV=213, DENSITY_XHIGH=320
				if (metrics.densityDpi >= 160/*metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
	                         || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
	                         || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
	                         || metrics.densityDpi == 213
	                         || metrics.densityDpi == 320*/)
				{
					return true;
				}
			}
		}
		return false;
	}

	public final static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null;
	}

	public final static boolean isWifiNet(Context context)
	{
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
		if (netWrokInfo == null || !netWrokInfo.isAvailable())
		{
			return false;
		}
		else
		{
			String typeName = netWrokInfo.getTypeName().toUpperCase();
			if(typeName.contains("WIFI"))
			{
				return true;
			}
		}
		return false;
	}

	public final static String getNetWorkType(Context context)
	{
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
		if (netWrokInfo == null || !netWrokInfo.isAvailable())
		{
			return "not";
		}
		else
		{
			String typeName = netWrokInfo.getTypeName().toUpperCase();
			if(typeName.contains("WIFI"))
			{
				return "wifi";
			}
			else if(typeName.contains("MOBILE"))
			{
				String extraInfo = netWrokInfo.getExtraInfo();
				if(extraInfo!=null && extraInfo.toUpperCase().contains("3G"))
				{
					return "3g";
				}
				else
				{
					return "2g";
				}
			}
			else
			{
				return "unknown";
			}
		}
	}

	public static int getVersionCode(Context context)
	{
		try
		{
			PackageInfo pinfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0);
			return pinfo.versionCode;
		}
		catch (NameNotFoundException e)
		{
		}
		return -1;
	}
	public static String getVersionName(Context context)
	{
		try
		{
			PackageInfo pinfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0);
			return pinfo.versionName;
		}
		catch (NameNotFoundException e)
		{
		}
		return "unknown";
	}

	public static void HideSoftInput(View v){
		if(v==null){
			return;
		}
		InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
