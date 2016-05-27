package com.fob.balls;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.util.JsonParseTool;

public class ForgetActivity extends UMengActivity implements OnClickListener {

	private LinearLayout ll_next, ll_login;
	private EditText et;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View.inflate(this, R.layout.forget, null);
		setContentView(mContentView);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.forget_back);
		ll_login = (LinearLayout) findViewById(R.id.forget_login);
		ll_next = (LinearLayout) findViewById(R.id.forget_next);

		et = (EditText) findViewById(R.id.forget_phone);
		et.setText(App.preferences.getStringMessage("user", "phone", ""));
	}

	private void setView() {
		iv_back.setOnClickListener(this);
		ll_login.setOnClickListener(this);
		ll_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forget_back:
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			break;
		case R.id.forget_next:
			String phone = et.getText().toString();
			if (!"".equals(phone)) {
				new ForgotPwdTask(phone).execute();
			} else if (!isValidPhone(phone)) {
				Toast.makeText(this, "手机号不合法", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.forget_login:
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 手机号是否合法
	 * 
	 * @param paramString
	 * @return
	 */
	private boolean isValidPhone(String paramString) {
		// L.i(TAG, "isValidPhone phone=" + paramString);
		if (paramString == null || paramString.equals(""))
			return false;
		Pattern p = Pattern.compile("^((1))\\d{10}$");
		Matcher m = p.matcher(paramString);
		return m.matches();
	}

	private class ForgotPwdTask extends AsyncTask<String, Integer, String> {

		String phone;

		public ForgotPwdTask(String phone) {
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
				result = APIRequestServers.forgotpwd(phone);
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
						Intent i = new Intent(ForgetActivity.this,
								RegisterCheckActivity.class);
						i.putExtra("phone", phone);
						i.putExtra("FindPassword", true);
						startActivity(i);
						finish();
						ShowToast.getToast(ForgetActivity.this).show(
								"验证码已发送到" + phone + "请查收");
					} else if ("0".equals(rbb.getRet())) {
						ShowToast.getToast(ForgetActivity.this).show(
								rbb.getErr());
					} else {
						ShowToast.getToast(ForgetActivity.this).show("获取验证码失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(ForgetActivity.this).show("获取验证码失败");
				}
			} else {
				ShowToast.getToast(ForgetActivity.this).show("获取验证码失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}

}
