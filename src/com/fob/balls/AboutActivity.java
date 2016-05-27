package com.fob.balls;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.AboutBean;
import com.fob.util.JsonParseTool;

public class AboutActivity extends UMengActivity implements OnClickListener {

	private TextView tv_text, tv_version;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View.inflate(this, R.layout.about, null);
		setContentView(mContentView);

		findView();
		setView();

		String text = App.preferences.getStringMessage("user", "about", "");
		if (!"".equals(text)) {
			tv_text.setText(text);
			new AboutTask(false).execute();
		} else {
			new AboutTask(true).execute();
		}
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.about_back);
		tv_text = (TextView) findViewById(R.id.about_text);
		tv_version = (TextView) findViewById(R.id.about_version);
	}

	private void setView() {
		iv_back.setOnClickListener(this);

		tv_version.setText("V" + getVersionName());
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_back:
			finish();
			break;
		default:
			break;
		}
	}

	private class AboutTask extends AsyncTask<String, Integer, String> {

		boolean isShow;

		public AboutTask(boolean isShow) {
			this.isShow = isShow;
		}

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			if (isShow)
				spdDialog.showProgressDialog("正在处理中...");
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				result = APIRequestServers.about();
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
					AboutBean rb = (AboutBean) JsonParseTool.dealSingleResult(
							result, AboutBean.class);
					if ("1".equals(rb.getRet())) {
						tv_text.setText(rb.getText());
						App.preferences.saveStringMessage("user", "about",
								rb.getText());
					} else if ("0".equals(rb.getRet())) {
						ShowToast.getToast(AboutActivity.this)
								.show(rb.getErr());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}
}
