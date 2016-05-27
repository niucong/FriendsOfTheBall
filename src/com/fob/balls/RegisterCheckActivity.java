package com.fob.balls;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.AuthuserBean;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.util.CharUtil;
import com.fob.util.JsonParseTool;
import com.fob.util.L;

public class RegisterCheckActivity extends UMengActivity implements
		OnClickListener {
	private final String TAG = "RegisterCheckActivity";

	private LinearLayout ll_next, ll_again;
	private TextView tv_phone;
	private ImageView iv_back;
	private EditText et_code;

	private String phone;
	private boolean isFindPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View.inflate(this, R.layout.register_check,
				null);
		setContentView(mContentView);
		phone = getIntent().getStringExtra("phone");
		isFindPassword = getIntent().getBooleanExtra("FindPassword", false);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.register_check_back);
		ll_next = (LinearLayout) findViewById(R.id.register_check_next);
		ll_again = (LinearLayout) findViewById(R.id.register_check_again);

		tv_phone = (TextView) findViewById(R.id.register_check_phone);
		et_code = (EditText) findViewById(R.id.register_check_et);
	}

	private void setView() {
		iv_back.setOnClickListener(this);
		ll_next.setOnClickListener(this);
		ll_again.setOnClickListener(this);

		tv_phone.setText("短信验证码已发送到" + phone + "，请稍等，或稍后重新注册。");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_check_back:
			if (isFindPassword) {
				startActivity(new Intent(this, ForgetActivity.class));
			} else {
				startActivity(new Intent(this, RegisterActivity.class));
			}
			finish();
			break;
		case R.id.register_check_next:
			String code = et_code.getEditableText().toString();
			if (code != null && !"".equals(code)) {
				if (!CharUtil.isValidPassword(code)) {
					Toast.makeText(this, "验证码格式错误", Toast.LENGTH_LONG).show();
				} else {
					new RegisterCheckTask(phone, code).execute();
				}
			} else {
				ShowToast.getToast(this).show("手机验证码不能为空");
			}
			break;
		case R.id.register_check_again:
			new RegisterTask(phone).execute();
			break;
		default:
			break;
		}
	}

	private class RegisterCheckTask extends AsyncTask<String, Integer, String> {

		String phone, code;

		public RegisterCheckTask(String phone, String code) {
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
				result = APIRequestServers.authuser(phone, code);
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
						App.preferences.saveStringMessage("user", "phone",
								phone);
						App.preferences.saveStringMessage("user", "Token",
								mc.getToken());
						App.preferences.saveStringMessage("user", "Username",
								mc.getUsername());

						if (!isFindPassword) {
							Intent i = new Intent(RegisterCheckActivity.this,
									SetNameActivity.class);
							i.putExtra("RegisterCheck", true);
							startActivity(i);
							finish();
						} else {
							Intent i = new Intent(RegisterCheckActivity.this,
									LoginActivity.class);
							i.putExtra("EnterMain", true);
							startActivity(i);
							finish();
						}
					} else if ("0".equals(mc.getRet())) {
						ShowToast.getToast(RegisterCheckActivity.this).show(
								mc.getErr());
					} else {
						ShowToast.getToast(RegisterCheckActivity.this).show(
								"验证失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(RegisterCheckActivity.this).show("验证失败");
				}
			} else {
				ShowToast.getToast(RegisterCheckActivity.this).show("验证失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}

	private class RegisterTask extends AsyncTask<String, Integer, String> {

		String phone;

		public RegisterTask(String phone) {
			this.phone = phone;
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
				result = APIRequestServers.reguser(phone);
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
					ResultBasicBean rbb = (ResultBasicBean) JsonParseTool
							.dealSingleResult(result, ResultBasicBean.class);
					if ("1".equals(rbb.getRet())) {
						ShowToast.getToast(RegisterCheckActivity.this).show(
								"验证码已发送到" + phone + "请查收");
					} else if ("0".equals(rbb.getRet())) {
						ShowToast.getToast(RegisterCheckActivity.this).show(
								rbb.getErr());
					} else {
						ShowToast.getToast(RegisterCheckActivity.this).show(
								"注册失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(RegisterCheckActivity.this).show("注册失败");
				}
			} else {
				ShowToast.getToast(RegisterCheckActivity.this).show("注册失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}

}
