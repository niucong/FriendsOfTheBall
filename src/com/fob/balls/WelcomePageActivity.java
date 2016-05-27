package com.fob.balls;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fob.balls.dialog.App;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.DownLoadFileThread;
import com.fob.balls.net.bean.VersionBean;
import com.fob.util.JsonParseTool;

public class WelcomePageActivity extends UMengActivity implements
		OnClickListener {

	private TextView tv_login, tv_registe;
	private LinearLayout ll;
	private View mContentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContentView = View.inflate(this, R.layout.fob_page_welcome, null);
		setContentView(mContentView);
		findView();
		setView();

		new CodeListTask().execute();

		new Thread() {
			@Override
			public void run() {
				try {
					VersionBean vb = (VersionBean) JsonParseTool
							.dealSingleResult(
									APIRequestServers.updateversion(),
									VersionBean.class);
					if (vb != null && "1".equals(vb.getRet())) {
						String version = vb.getAndroidversion();
						if (version != null
								&& !version.equals(getVersionName())) {
							Message msg = new Message();
							msg.what = 1;
							msg.obj = new String[] { vb.getAndroidurl(),
									version };
							h.sendMessage(msg);
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				h.sendEmptyMessage(0);
			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler h = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (App.preferences
						.getBooleanMessage("user", "hasLogin", false)) {
					startActivity(new Intent(WelcomePageActivity.this,
							LoginActivity.class));
					finish();
				} else {
					mContentView.setBackgroundResource(R.drawable.wel_bg);
					ll.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				final String[] vs = (String[]) msg.obj;
				Builder builder = new AlertDialog.Builder(
						WelcomePageActivity.this)
						.setTitle("检测到新版本V" + vs[1])
						.setCancelable(false)
						.setPositiveButton("马上升级",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										spdDialog
												.showProgressDialog("正在更新版本...");
										spdDialog.setCancel(false);
										new DownLoadFileThread(
												WelcomePageActivity.this,
												vs[0], 0, null, pbHandler)
												.start();
									}
								});
				AlertDialog ad = builder.create();
				ad.setCancelable(false);
				ad.show();
				break;
			default:
				break;
			}
		};
	};

	private Handler pbHandler = new Handler() {
		public void handleMessage(Message msg) {
			spdDialog.setMsg("已下载 " + ((String) msg.obj) + "...");
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 软件版本号
	 * 
	 * @return
	 */
	public String getVersionName() {
		String versionName = "";
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(),
					0).versionName; // 版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	private void findView() {
		ll = (LinearLayout) findViewById(R.id.wel_layout);
		tv_login = (TextView) findViewById(R.id.wel_login);
		tv_registe = (TextView) findViewById(R.id.wel_registe);
	}

	private void setView() {
		tv_login.setOnClickListener(this);
		tv_registe.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wel_login:
			// redirectTo();
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			break;
		case R.id.wel_registe:
			startActivity(new Intent(this, RegisterActivity.class));
			finish();
			break;
		default:
			break;
		}
	}

	// private void redirectTo() {
	// setContentView(frameView.createContentView());
	// }

	// public static Activity getActivity() {
	// return mActivity;
	// }

	private class CodeListTask extends AsyncTask<String, Integer, String> {

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		// @Override
		// protected void onPreExecute() {
		// spdDialog.showProgressDialog("正在处理中...");
		// }

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				result = APIRequestServers.getCodeList();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		@Override
		protected void onPostExecute(String result) {
			// spdDialog.cancelProgressDialog("");
			if (result != null) {
				// try {
				// CodeListBean mc = (CodeListBean) JsonParseTool
				// .dealSingleResult(result, CodeListBean.class);
				// if (mc.getRet() == 1) {
				App.preferences.saveStringMessage("user", "CodeList", result);
				// }
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		// @Override
		// protected void onCancelled() {
		// spdDialog.cancelProgressDialog("");
		// }
	}

}
