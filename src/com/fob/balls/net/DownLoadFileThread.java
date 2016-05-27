package com.fob.balls.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.fob.util.AppSharedPreferences;
import com.fob.util.FileUtil;
import com.fob.util.L;

public class DownLoadFileThread extends Thread {
	private final String TAG = "DownLoadFileThread";

	private int id;
	private String fileUrl;
	private long fileLength;
	private String filename;
	private Context context;

	// 通知栏
	// private NotificationManager downloadNotificationManager = null;
	// private Notification downloadNotification = null;
	// // 通知栏跳转Intent
	// private Intent downloadIntent = null;
	// private PendingIntent downloadPendingIntent = null;

	private String versionName;

	private Handler pbHandler;

	/**
	 * 普通文件下载
	 * 
	 * @param context
	 * @param fileUrl
	 * @param fileLength
	 * @param filename
	 */
	public DownLoadFileThread(Context context, String fileUrl, long fileLength,
			String filename, Handler pbHandler) {
		this.context = context;
		this.id = (int) System.currentTimeMillis();
		this.fileUrl = fileUrl;
		this.fileLength = fileLength;
		this.filename = filename;
		this.pbHandler = pbHandler;

		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		if (filename == null || filename.equals("")) {
			try {
				this.filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (!filename.contains(".")) {
			this.filename = this.filename
					+ fileUrl.substring(fileUrl.lastIndexOf("."));
		}

		try {
			if (versionName != null && !"".equals(versionName)) {
				this.filename = filename
						.substring(0, filename.lastIndexOf("."))
						+ versionName
						+ filename.substring(filename.lastIndexOf("."));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// downloadNotificationManager = (NotificationManager) context
		// .getSystemService("notification");
		// downloadNotification = new Notification();
		//
		// // 设置下载过程中，点击通知栏，跳到OpenFile.class
		// downloadIntent = new Intent();
		// downloadIntent.putExtra("fileName", filename);
		// downloadPendingIntent = PendingIntent.getActivity(context, 0,
		// downloadIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		//
		// // 设置通知栏显示内容
		// downloadNotification.icon = R.drawable.ic_launcher;
		// downloadNotification.tickerText = "开始下载“" + filename + "”";
		// downloadNotification.setLatestEventInfo(context, "开始下载", filename,
		// downloadPendingIntent);
		// downloadNotificationManager.notify(id, downloadNotification);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		super.run();
		int currentSize = 0;
		L.d(TAG, "run fileUrl = " + fileUrl);
		InputStream fisFileInputStream = null;
		HttpURLConnection hrc = null;
		BufferedInputStream is = null;
		FileOutputStream fos = null;
		File proFile = null;
		try {
			URL url = new URL(fileUrl);
			hrc = (HttpURLConnection) url.openConnection();

			hrc.setRequestProperty("User-Agent", "NetBear");
			hrc.setRequestProperty("Content-type",
					"application/x-java-serialized-object");
			hrc.setRequestProperty("connection", "Keep-Alive");
			if (currentSize > 0) {
				hrc.setRequestProperty("RANGE", "bytes = " + currentSize + "-");
			}
			hrc.setConnectTimeout(20 * 1000);
			hrc.setReadTimeout(30 * 1000);
			hrc.connect();

			// for (int i = 1; i > 0; i++) {
			// String str = hrc.getHeaderFieldKey(i);
			// if (str != null) {
			// L.d(TAG,
			// "HeaderField#" + i + ":" + str + "="
			// + hrc.getHeaderField(str));
			// } else {
			// break;
			// }
			// }

			fisFileInputStream = hrc.getInputStream();

			is = new BufferedInputStream(fisFileInputStream);

			File myFile = null;

			String fileAbsolutePath = Environment.getExternalStorageDirectory()
					.toString() + "/";
			proFile = new File(fileAbsolutePath + filename + ".sp");
			myFile = new File(fileAbsolutePath + filename);
			L.i(TAG, "fileAbsolutePath : " + fileAbsolutePath);

			fos = new FileOutputStream(proFile);

			if (fileLength == 0) {
				fileLength = hrc.getContentLength();
			}
			L.i(TAG, "fileLength : " + fileLength);
			int progress = 0;
			int length = 0;
			byte buf[] = new byte[1024];
			progress = (int) (currentSize * 100 / fileLength);
			while ((length = is.read(buf)) != -1) {
				fos.write(buf, 0, length);
				currentSize += length;
				Message msg = Message.obtain();
				msg.obj = computeFileSize(currentSize) + "/"
						+ computeFileSize(fileLength);
				pbHandler.sendMessage(msg);
				L.i(TAG, "currentSize : " + currentSize);
				// int i = (int) (currentSize * 100 / fileLength);
				// if (i > progress) {
				// progress = i;
				// // // 设置通知显示的参数
				// // downloadNotification.setLatestEventInfo(context, "已下载 "
				// // + progress + "%", filename, downloadPendingIntent);
				// // // 这个可以理解为开始执行这个通知
				// // downloadNotificationManager
				// // .notify(id, downloadNotification);
				// // L.i(TAG, "id=" + id + ",progress=" + progress
				// // + ",currentSize=" + currentSize);
				// }
			}
			fos.flush();

			if (proFile != null && proFile.exists())
				proFile.renameTo(myFile);

			// // 设置通知显示的参数
			// downloadNotification.setLatestEventInfo(context, "下载完成",
			// filename,
			// downloadPendingIntent);
			// // 这个可以理解为开始执行这个通知
			// downloadNotificationManager.notify(id, downloadNotification);
			// downloadNotificationManager.cancel(null, id);

			AppSharedPreferences preferences = new AppSharedPreferences(context);
			preferences.saveStringMessage("user", "uploadname", "");
			preferences.saveStringMessage("user", "uploadurl", "");

			new FileUtil().openFile(context, myFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (proFile != null && proFile.exists())
				proFile.delete();
			try {
				if (is != null)
					is.close();
				if (fos != null)
					fos.close();
				if (hrc != null)
					hrc.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 计算文件大小
	 * 
	 * @param len
	 * @return
	 */
	private String computeFileSize(long len) {
		String size = "1K";
		float len2 = len;
		DecimalFormat df = new DecimalFormat("#0.#");
		if (len > 1024) {
			len2 = len2 / 1024;
		} else {
			return size;
		}
		if (len2 > 1024) {
			len2 = len2 / 1024;
		} else {
			size = df.format(len2) + "K";
			return size;
		}
		if (len2 > 1024) {
			len2 = len2 / 1024;
			size = df.format(len2) + "G";
		} else {
			size = df.format(len2) + "M";
		}
		return size;
	}

}
