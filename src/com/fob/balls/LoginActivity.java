package com.fob.balls;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.AuthuserBean;
import com.fob.balls.net.bean.CodeListBean;
import com.fob.balls.net.bean.OrderHeartBeatRES;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.manager.FOBPageManager;
import com.fob.page.FOBPageEvent;
import com.fob.page.FOBPageFrame;
import com.fob.page.FOBPageFriends;
import com.fob.page.FOBPageMessage;
import com.fob.page.FOBPagePlace;
import com.fob.page.FOBPageSetting;
import com.fob.page.base.FOBPageBase;
import com.fob.tools.MD5EncodeUtil;
import com.fob.util.CharUtil;
import com.fob.util.JsonParseTool;
import com.fob.util.L;
import com.fob.util.MusicRing;

public class LoginActivity extends UMengActivity implements OnClickListener {
	public static final String TAG = "LoginActivity";

	private static LoginActivity mActivity;
	public FOBPageFrame frameView;

	private LinearLayout ll_login, ll_forget;
	private EditText et_phone, et_password;
	private ImageView iv_back;
	private TextView tv_change;

	private boolean isRegister, isEnterMain;

	public LoginActivity() {
		mActivity = this;
	}

	private boolean isStop = false;

//	private MusicRing sound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isRegister = getIntent().getBooleanExtra("Register", false);
		isEnterMain = getIntent().getBooleanExtra("EnterMain", false);
		frameView = new FOBPageFrame();

//		sound = new MusicRing(this);

		String token = App.preferences.getStringMessage("user", "Token", "");
		L.i(TAG, "onCreate token=" + token);
		if ((isEnterMain || App.preferences.getBooleanMessage("user",
				"hasLogin", false)) && !"".equals(token)) {
			setContentView(R.layout.main);
			initView2();
			setContentView(frameView.createContentView(2));
		} else {
			setContentView(R.layout.login);
			initView();
		}
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.login_back);
		ll_login = (LinearLayout) findViewById(R.id.login_next);
		ll_forget = (LinearLayout) findViewById(R.id.login_forget);

		tv_change = (TextView) findViewById(R.id.login_change);
		et_phone = (EditText) findViewById(R.id.login_phone);
		et_password = (EditText) findViewById(R.id.login_password);

		iv_back.setOnClickListener(this);
		ll_login.setOnClickListener(this);
		ll_forget.setOnClickListener(this);
		tv_change.setOnClickListener(this);

		et_phone.setText(App.preferences.getStringMessage("user", "phone", ""));
		et_phone.setSelection(et_phone.getText().toString().length());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_back:
			startActivity(new Intent(this, WelcomePageActivity.class));
			finish();
			break;
		case R.id.login_change:
			et_password.setText("");
			et_phone.setText("");
			break;
		case R.id.login_next:
			String phone = et_phone.getEditableText().toString();
			String code = et_password.getEditableText().toString();
			if (phone == null || "".equals(phone)) {
				ShowToast.getToast(this).show("手机号码不能为空");
			} else if (!CharUtil.isValidPhone(phone)) {
				Toast.makeText(this, "手机号码格式错误", Toast.LENGTH_LONG).show();
			} else if (code == null || "".equals(code)) {
				ShowToast.getToast(this).show("密码不能为空");
			} else if (!CharUtil.isValidPassword(code)) {
				Toast.makeText(this, "密码格式错误", Toast.LENGTH_LONG).show();
			} else {
				new LoginTask(phone, code).execute();
			}
			break;
		case R.id.login_forget:
			startActivity(new Intent(this, ForgetActivity.class));
			finish();
			break;
		case R.id.main_friend:
			setContentView(frameView.createContentView(1));
			break;
		case R.id.main_activity:
			setContentView(frameView.createContentView(3));
			break;
		case R.id.main_booking:
			setContentView(frameView.createContentView(2));
			break;
		case R.id.main_message:
			setContentView(frameView.createContentView(0));
			break;
		default:
			break;
		}
	}

	private class LoginTask extends AsyncTask<String, Integer, String> {

		String phone, code;

		public LoginTask(String phone, String code) {
			this.phone = phone;
			this.code = code;
		}

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			spdDialog.showProgressDialog("正在处理中...");
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				result = APIRequestServers.login(phone, code);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		@Override
		protected void onPostExecute(String result) {
			spdDialog.cancelProgressDialog("");
			if (result != null) {
				try {
					AuthuserBean mc = (AuthuserBean) JsonParseTool
							.dealSingleResult(result, AuthuserBean.class);
					String ret = mc.getRet();
					L.d(TAG, "authuser Curt=" + mc.getCurt() + ",ret=" + ret);
					if ("1".equals(ret)) {
						try {
							((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(getCurrentFocus()
											.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
						} catch (Exception e) {
							e.printStackTrace();
						}

						App.preferences.saveStringMessage("user", "phone",
								phone);
						App.preferences.saveStringMessage("user", "Token",
								mc.getToken());
						App.preferences.saveStringMessage("user", "Username",
								mc.getUsername());

						if (isRegister) {
							startActivity(new Intent(LoginActivity.this,
									SetNameActivity.class));
							finish();
						} else {
							setContentView(R.layout.main);
							initView2();
							setContentView(frameView.createContentView(2));
						}
					} else if ("0".equals(mc.getRet())) {
						ShowToast.getToast(LoginActivity.this)
								.show(mc.getErr());
					} else {
						ShowToast.getToast(LoginActivity.this).show("登录失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(LoginActivity.this).show("登录失败");
				}
			} else {
				ShowToast.getToast(LoginActivity.this).show("登录失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}

	@SuppressLint("HandlerLeak")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Uri uri = null;
			File saveFile = null;
			switch (requestCode) {
			case FOBPageSetting.FROM_CAMERA:
				saveFile = new File(FOBPageSetting.PATH);
				File picture = new File(saveFile, FOBPageSetting.headimg_name);
				uri = Uri.fromFile(picture);
				startPhotoZoom(uri);
				break;
			case FOBPageSetting.FROM_GALLERY:
				uri = data.getData();
				startPhotoZoom(uri);
				break;
			case FOBPageSetting.FROM_CROP:
				saveFile = new File(FOBPageSetting.PATH);
				final File headPic = new File(saveFile,
						FOBPageSetting.headimg_name);
				FOBPageSetting.setHead(getHeadFilePath(Uri.fromFile(headPic)));

				final Handler h = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						switch (msg.what) {
						case 0:
							ShowToast.getToast(LoginActivity.this).show(
									"头像修改失败");
							break;
						case 1:
							ShowToast.getToast(LoginActivity.this).show(
									"头像修改成功");
							App.preferences.saveStringMessage("user", "header",
									headPic.getPath());
							break;
						default:
							break;
						}
					}
				};

				ShowToast.getToast(LoginActivity.this).show("头像修改中");
				new Thread() {
					public void run() {
						try {
							String result = httpUpload(APIRequestServers.URL2
									+ "uploadmyhead", headPic);
							ResultBasicBean rb = (ResultBasicBean) JsonParseTool
									.dealSingleResult(result,
											ResultBasicBean.class);
							if ("1".equals(rb.getRet())) {
								h.sendEmptyMessage(1);
							} else {
								h.sendEmptyMessage(0);
							}
						} catch (Exception e) {
							e.printStackTrace();
							h.sendEmptyMessage(0);
						}
					};
				}.start();
				break;
			}
		}
	}

	/**
	 * HttpURLConnection POST上传文件
	 * 
	 * @param uploadUrl
	 * @param filename
	 * @throws Exception
	 */
	private String httpUpload(String uploadUrl, File file) throws Exception {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		DataOutputStream dos = null;
		FileInputStream fis = null;
		String result = null;

		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(true);
			httpURLConnection.setRequestMethod("POST");

			String nonce = (new Random().nextInt(899999) + 100000) + "";//
			String expires = System.currentTimeMillis() + "";// 1393086533785
			String token = App.preferences
					.getStringMessage("user", "Token", "");
			String accessKey = App.preferences.getStringMessage("user",
					"Username", "");
			String signature = MD5EncodeUtil.sign(token, expires, nonce);
			L.i(TAG, "setHeaders nonce=" + nonce + ",expires=" + expires
					+ ",accessKey=" + accessKey + ",signature=" + signature
					+ ",User-Agent=" + android.os.Build.MODEL + ";"
					+ android.os.Build.VERSION.RELEASE);
			httpURLConnection.setRequestProperty("User-Agent",
					android.os.Build.MODEL + ";"
							+ android.os.Build.VERSION.RELEASE);
			httpURLConnection.setRequestProperty("Nonce", nonce);
			httpURLConnection.setRequestProperty("Signature", signature);
			httpURLConnection.setRequestProperty("Expires", expires);
			httpURLConnection.setRequestProperty("YQKAccessKey", accessKey);

			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			String reqHeader = twoHyphens
					+ boundary
					+ end
					+ "Content-Disposition: form-data; name=\"file\"; filename=\""
					+ file.getName() + "\"" + end
					+ "Content-Type: image/jpeg; charset=UTF-8" + end + end;
			String reqEnder = end + twoHyphens + boundary + twoHyphens + end;

			long totalLength = file.length();
			httpURLConnection.setFixedLengthStreamingMode(reqHeader.length()
					+ (int) (totalLength) + reqEnder.length());

			dos = new DataOutputStream(httpURLConnection.getOutputStream());
			dos.writeBytes(reqHeader);
			L.d(TAG, "path=" + file.getAbsolutePath());
			fis = new FileInputStream(file);
			L.d(TAG, "httpUpload totalLength=" + totalLength);
			long uploadSize = 0;
			int progress = 0;
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
				uploadSize += count;
				progress = (int) (uploadSize * 100 / totalLength);
				if (progress > 0) {
					L.d(TAG, "httpUpload uploadSize=" + uploadSize
							+ ",progress=" + progress);
				}
			}

			dos.writeBytes(reqEnder);
			dos.flush();

			InputStream inStream = httpURLConnection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inStream, "UTF-8"));
			StringBuilder strber = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null)
				strber.append(line + "\n");
			inStream.close();
			result = strber.toString();
			L.d(TAG, "httpUpload result=" + result);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			if (dos != null)
				try {
					dos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 *            图片路径
	 **/
	public void startPhotoZoom(Uri uri) {
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		cropIntent.setDataAndType(uri, "image/*");// 设置要裁剪的图片
		cropIntent.putExtra("crop", "true");// crop=true 裁剪页面.
		// 设置其他信息：
		// cropIntent.putExtra("aspectX", 1);// 设置裁剪框的比例.
		// cropIntent.putExtra("aspectY", 1);// x:y=1:1
		// outputX outputY 是裁剪图片宽高
		cropIntent.putExtra("outputX", 180);
		cropIntent.putExtra("outputY", 180);
		cropIntent.putExtra("return-data", "false");
		File saveFile = new File(FOBPageSetting.PATH);
		File picture = new File(saveFile, FOBPageSetting.headimg_name);
		cropIntent.putExtra("output", Uri.fromFile(picture));// 保存输出文件
		cropIntent.putExtra("outputFormat", "JPEG");// 返回格式
		startActivityForResult(cropIntent, FOBPageSetting.FROM_CROP);
	}

	/**
	 * 获取返回图片的路径
	 * 
	 * @param uri
	 */
	public Bitmap getHeadFilePath(Uri uri) {
		String sUri = uri.toString();
		// 如果是从系统gallery取图片，返回一个contentprovider的uri
		if (sUri.startsWith("content://")) {
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				return BitmapFactory.decodeFile(cursor.getString(1)); // 图片文件路径
			} else {

			}
		} else if (sUri.startsWith("file://")
				&& (sUri.endsWith(".png") || sUri.endsWith(".jpg") || sUri
						.endsWith(".gif"))) {
			// 如果从某些第三方软件中选取文件，返回的是一个文件具体路径。
			return BitmapFactory.decodeFile(sUri.replace("file://", ""));
		} else if (sUri.startsWith(FOBPageSetting.PATH)) {
			// 直接获取临时图片地址
			return BitmapFactory.decodeFile(sUri);
		} else {
			// 返回的uri不合法或者文件不是图片。

		}
		return null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			FOBPageBase fpb = FOBPageManager.getLastPage();
			if (fpb != null) {
				if (!(fpb instanceof FOBPageMessage)
						&& !(fpb instanceof FOBPageFriends)
						&& !(fpb instanceof FOBPagePlace)
						&& !(fpb instanceof FOBPageEvent)
						&& !(fpb instanceof FOBPageSetting)) {
					fpb.closePage();
				} else {
					this.finish();
				}
			} else {
				this.finish();
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		isStop = true;
		super.onDestroy();
	}

	private void initView2() {
		// findViewById(R.id.main_friend).setOnClickListener(this);
		// findViewById(R.id.main_activity).setOnClickListener(this);
		// findViewById(R.id.main_booking).setOnClickListener(this);
		// findViewById(R.id.main_message).setOnClickListener(this);

		App.preferences.saveBooleanMessage("user", "hasLogin", true);
		startHeart();
	}

	public static LoginActivity getActivity() {
		return mActivity;
	}

	@SuppressLint("HandlerLeak")
	private void startHeart() {
		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				boolean[] flags = (boolean[]) msg.obj;
				frameView.setNews(flags[0], flags[1]);
			}
		};

		new Thread() {
			public void run() {
				while (!isStop) {
					String sport = App.preferences.getStringMessage("user",
							"sport", "网球");
					boolean csFlag = false;
					boolean ucsFlag = false;
					try {
						String str = APIRequestServers.orderHeartBeat(sport,
								App.preferences.getLongMessage("user",
										"lasttime", 0));
						OrderHeartBeatRES ohb = (OrderHeartBeatRES) JsonParseTool
								.dealSingleResult(str, OrderHeartBeatRES.class);

						App.preferences.saveLongMessage("user", "lasttime",
								ohb.getCurt());

						int ucs = 0;
						try {
							ucs = Integer.valueOf(ohb.getUcs());
						} catch (Exception e) {
							e.printStackTrace();
						}
						int cs = 0;
						try {
							cs = Integer.valueOf(ohb.getCs());
						} catch (Exception e) {
							e.printStackTrace();
						}
						int c = App.preferences.getIntMessage("user", "cs", 0);
						int uc = App.preferences
								.getIntMessage("user", "ucs", 0);
						csFlag = c != cs;
						ucsFlag = uc != ucs;

						if (ucs > 0 || cs > 0) {
							App.preferences.saveIntMessage("user", "ucs", ucs);
							App.preferences.saveIntMessage("user", "cs", cs);
						}

						int as = 0;
						try {
							as = Integer.valueOf(ohb.getAs());
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (as > 0) {
							App.preferences.saveIntMessage("user", "as", as);
						}

						if (ucs > 0 || cs > 0 || as > 0) {
							// sound.playSounds();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					boolean[] flags = { csFlag, ucsFlag };
					Message msg = new Message();
					msg.obj = flags;
					h.sendMessage(msg);

					int time = 0;
					String codeList = App.preferences.getStringMessage("user",
							"CodeList", "");
					try {
						CodeListBean mc = (CodeListBean) JsonParseTool
								.dealComplexResult(codeList, CodeListBean.class);
						time = Integer.valueOf(mc.getOrderheartbeatrate());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (time == 0)
						time = 15;
					try {
						sleep(time * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
}
