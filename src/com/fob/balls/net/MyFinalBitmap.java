package com.fob.balls.net;

import android.content.Context;
import android.widget.ImageView;

import com.fob.util.L;

public class MyFinalBitmap {
	private static final String TAG = "MyFinalBitmap";

	// private static ImageLoader imageLoader = ImageLoader.getInstance();
	// private static DisplayImageOptions HEADER;
	// private static DisplayImageOptions POSTER;
	// private static DisplayImageOptions POSTERCORNER;
	// private static DisplayImageOptions IMAGE;
	// private static DisplayImageOptions DETAILIMAGE;
	// private static DisplayImageOptions LOCAL;

	public static void setHeader(Context context, ImageView iv, String url) {
		L.i(TAG, "setHeader url=" + url);
		// try {
		// if (null == HEADER) {
		// HEADER = new DisplayImageOptions.Builder()
		// .showStubImage(
		// R.drawable.fob_adapter_friends_header_default_man)
		// .showImageForEmptyUri(
		// R.drawable.fob_adapter_friends_header_default_man)
		// .showImageOnFail(
		// R.drawable.fob_adapter_friends_header_default_man)
		// .cacheInMemory().cacheOnDisc()
		// .bitmapConfig(Bitmap.Config.ARGB_8888).build();
		// }
		// imageLoader.displayImage(url, iv, HEADER);
		// } catch (Exception e) {
		// e.printStackTrace();
		// } catch (OutOfMemoryError e) {
		// }
	}

	// public static void setPoster(Context context, ImageView iv, String url) {
	// try {
	// if (null == POSTER) {
	// POSTER = new DisplayImageOptions.Builder()
	// .showStubImage(TaskUtil.POSTERDEFAULTLOADSTATEIMG[0])
	// .showImageForEmptyUri(
	// TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
	// .showImageOnFail(TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
	// .cacheInMemory().cacheOnDisc()
	// .bitmapConfig(Bitmap.Config.ARGB_8888).build();
	// }
	// imageLoader.displayImage(url, iv, POSTER);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	// }
	// }
	//
	// public static void setPosterCorner(Context context, ImageView iv, String
	// url) {
	// try {
	// if (null == POSTERCORNER) {
	// float scale = context.getResources().getDisplayMetrics().density;
	// int s = (int) (35 * scale + 0.5f);
	// POSTERCORNER = new DisplayImageOptions.Builder()
	// .showStubImage(TaskUtil.POSTERDEFAULTLOADSTATEIMG[0])
	// .showImageForEmptyUri(
	// TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
	// .showImageOnFail(TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
	// .cacheInMemory().cacheOnDisc()
	// .displayer(new RoundedBitmapDisplayer(s)) // 图片圆角显示，值为整数
	// .bitmapConfig(Bitmap.Config.ARGB_8888).build();
	// }
	// imageLoader.displayImage(url, iv, POSTERCORNER);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	// }
	// }
	//
	// public static void setPosterCorner(Context context, ImageView iv,
	// String url, int roundPixels, SimpleImageLoadingListener listener) {
	// try {
	// if (null == POSTERCORNER) {
	// float scale = context.getResources().getDisplayMetrics().density;
	// int s = Math.round(35 * scale);
	// POSTERCORNER = new DisplayImageOptions.Builder()
	// .showStubImage(TaskUtil.POSTERDEFAULTLOADSTATEIMG[0])
	// .showImageForEmptyUri(
	// TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
	// .showImageOnFail(TaskUtil.POSTERDEFAULTLOADSTATEIMG[1])
	// .cacheInMemory().cacheOnDisc()
	// .displayer(new RoundedBitmapDisplayer(s)) // 图片圆角显示，值为整数
	// .bitmapConfig(Bitmap.Config.ARGB_8888).build();
	// }
	// imageLoader.displayImage(url, iv, POSTERCORNER, listener,
	// LoadAndDisplay.DEFAULT, null);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	// }
	// }

	public static void setImage(Context context, ImageView iv, String url) {
		// try {
		// if (null == IMAGE) {
		// IMAGE = new DisplayImageOptions.Builder()
		// .showStubImage(R.drawable.badminton)
		// .showImageForEmptyUri(R.drawable.badminton)
		// .showImageOnFail(R.drawable.badminton).cacheInMemory()
		// .cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888)
		// .build();
		// }
		// imageLoader.displayImage(url, iv, IMAGE);
		// } catch (Exception e) {
		// e.printStackTrace();
		// } catch (OutOfMemoryError e) {
		// }
	}

	// public static void setLoaclImage(ImageView iv, String url,
	// SimpleImageLoadingListener listener) {
	// try {
	// L.d(TAG, "url:" + url);
	// if (null == DETAILIMAGE) {
	// DETAILIMAGE = new DisplayImageOptions.Builder()
	// .showStubImage(TaskUtil.IMGDEFAULTLOADSTATEIMG[0])
	// .showImageForEmptyUri(
	// TaskUtil.IMGDEFAULTLOADSTATEIMG[1])
	// .cacheInMemory().cacheOnDisc()
	// .bitmapConfig(Bitmap.Config.ARGB_8888).build();
	// }
	// imageLoader.displayImage(url, iv, DETAILIMAGE, listener,
	// LoadAndDisplay.LOCAL, null);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	// }
	// }
	//
	// public static void setLocalImage(Context context, ImageView iv, String
	// id) {
	// try {
	// if (ThumbnailImgUtil.isIdEmpty(id)) {
	// iv.setImageResource(TaskUtil.IMGDEFAULTLOADSTATEIMG[1]);
	// return;
	// }
	// Bitmap bitmap = Images.Thumbnails.getThumbnail(
	// context.getContentResolver(), Long.parseLong(id), 3, null);
	// iv.setImageBitmap(bitmap);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	// }
	// }
	//
	// public static void setLocalAndDisPlayImage(Context context, ImageView iv,
	// String uri) {
	// try {
	// if (null == LOCAL) {
	// LOCAL = new DisplayImageOptions.Builder()
	// .showStubImage(TaskUtil.IMGDEFAULTLOADSTATEIMG[0])
	// .showImageForEmptyUri(
	// TaskUtil.IMGDEFAULTLOADSTATEIMG[1])
	// .showImageOnFail(TaskUtil.IMGDEFAULTLOADSTATEIMG[1])
	// .cacheInMemory().cacheOnDisc()
	// .bitmapConfig(Bitmap.Config.ARGB_8888).build();
	// }
	// imageLoader.displayImage(uri, iv, LOCAL, null,
	// LoadAndDisplay.LOCALTHUMBNAIL, context);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	// }
	// }
	//
	// public static void clearMemoryCache() {
	// L.d(TAG, "clearMemoryCache...");
	// try {
	// imageLoader.clearMemoryCache();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	// }
	// }
	//
	// public static void cacheDiscImage(Context c, String key, File oldFile)
	// throws IOException {
	// try {
	// ImageLoaderConfiguration config = imageLoader
	// .getImageLoaderConfiguration();
	// DiscCacheAware discCache = config.getDiscMemryCache();
	// FileNameGenerator generator = config.getFileNameGenerator();
	// File checkFile = new File(Environment.getExternalStorageDirectory()
	// + "/Android/data/com.datacomo.mc.spider.android/cache/");
	// if (!checkFile.exists()) {
	// checkFile.mkdirs();
	// }
	// File f = new File(checkFile, generator.generate(key));
	// new FileUtil().copyfile(oldFile, f, true);
	// discCache.put(key, f);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	// }
	// }
	//
	// public static void cacheDiscImage(Context c, String key, File oldFile,
	// ImageSize imageSize) throws IOException {
	// try {
	// ImageLoaderConfiguration config = imageLoader
	// .getImageLoaderConfiguration();
	// DiscCacheAware discCache = config.getDiscMemryCache();
	// FileNameGenerator generator = config.getFileNameGenerator();
	// File checkFile = new File(ConstantUtil.CACHE_PHOTO_PATH);
	// if (!checkFile.exists()) {
	// checkFile.mkdirs();
	// }
	// File f = new File(checkFile, generator.generate(key));
	// Bitmap bmap = PhotoUtil.getBitmapFromFile(oldFile,
	// imageSize.getWidth(), imageSize.getHeight());
	// PhotoUtil.bitmap2File(bmap, f);
	// discCache.put(key, f);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	// }
	// }
	//
	// public static Bitmap getPosterBitmap(String key) {
	// try {
	// ImageLoaderConfiguration config = imageLoader
	// .getImageLoaderConfiguration();
	// FileNameGenerator generator = config.getFileNameGenerator();
	// String path = ConstantUtil.CACHE_PHOTO_PATH
	// + generator.generate(key);
	// L.i(TAG, "getPosterBitmap key=" + key + ",path=" + path);
	// return BitmapFactory.decodeFile(path);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	// }
	// return null;
	// }
}
