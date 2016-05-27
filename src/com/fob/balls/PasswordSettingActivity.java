package com.fob.balls;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.util.CharUtil;
import com.fob.util.JsonParseTool;

public class PasswordSettingActivity extends UMengActivity implements
		OnClickListener {

	private TextView tv_finish;
	private ImageView iv_back;
	private EditText et_new, et_renew;

	/**
	 * 0:设置密码，1：找回密码，2：注册密码
	 */
	private int Type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View.inflate(this, R.layout.password_setting,
				null);
		setContentView(mContentView);

		Type = getIntent().getIntExtra("Type", 0);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.password_setting_back);
		tv_finish = (TextView) findViewById(R.id.password_setting_finish);

		et_new = (EditText) findViewById(R.id.password_setting_first);
		et_renew = (EditText) findViewById(R.id.password_setting_second);
	}

	private void setView() {
		iv_back.setOnClickListener(this);
		tv_finish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.password_setting_back:
			finish();
			break;
		case R.id.password_setting_finish:
			String psd = et_new.getText().toString();
			String rePsd = et_renew.getText().toString();
			if ("".equals(psd) || "".equals(rePsd)) {
				ShowToast.getToast(PasswordSettingActivity.this).show("密码不能为空");
			} else if (!psd.equals(rePsd)) {
				ShowToast.getToast(PasswordSettingActivity.this).show(
						"两次输入的密码不一致");
			} else if (!CharUtil.isValidPassword(psd)) {
				ShowToast.getToast(PasswordSettingActivity.this).show("密码格式错误");
			} else {
				new SetPsdTask(psd).execute();
			}
			break;
		default:
			break;
		}
	}

	private class SetPsdTask extends AsyncTask<String, Integer, String> {

		String psd;

		public SetPsdTask(String psd) {
			this.psd = psd;
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
				result = APIRequestServers.setPasswd(psd);
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
					ResultBasicBean mc = (ResultBasicBean) JsonParseTool
							.dealSingleResult(result, ResultBasicBean.class);
					String ret = mc.getRet();
					if ("1".equals(ret)) {
						ShowToast.getToast(PasswordSettingActivity.this).show(
								"设置成功");
						if (Type == 1) {
							// ShowToast.getToast(PasswordSettingActivity.this)
							// .show("设置成功,请重新登录");
							Intent i = new Intent(PasswordSettingActivity.this,
									LoginActivity.class);
							i.putExtra("EnterMain", true);
							startActivity(i);
						} else if (Type == 2) {
							Intent i = new Intent(PasswordSettingActivity.this,
									SetNameActivity.class);
							i.putExtra("RegisterCheck", true);
							startActivity(i);
						}
						finish();
					} else {
						ShowToast.getToast(PasswordSettingActivity.this).show(
								"设置失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(PasswordSettingActivity.this).show(
							"设置失败");
				}
			} else {
				ShowToast.getToast(PasswordSettingActivity.this).show("设置失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}

}
