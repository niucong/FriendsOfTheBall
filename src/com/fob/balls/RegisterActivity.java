package com.fob.balls;

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
import com.fob.util.CharUtil;
import com.fob.util.JsonParseTool;

public class RegisterActivity extends UMengActivity implements OnClickListener {

	private LinearLayout ll_login, ll_registe;
	private EditText et;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View.inflate(this, R.layout.register, null);
		setContentView(mContentView);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.register_back);
		ll_login = (LinearLayout) findViewById(R.id.register_next);
		ll_registe = (LinearLayout) findViewById(R.id.register_login);

		et = (EditText) findViewById(R.id.register_et);
		et.setText(App.preferences.getStringMessage("user", "phone", ""));
	}

	private void setView() {
		iv_back.setOnClickListener(this);
		ll_login.setOnClickListener(this);
		ll_registe.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_back:
			startActivity(new Intent(this, WelcomePageActivity.class));
			finish();
			break;
		case R.id.register_login:
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			break;
		case R.id.register_next:
			String phone = et.getText().toString();
			if (!"".equals(phone)) {
				if (!CharUtil.isValidPhone(phone)) {
					Toast.makeText(this, "手机号码格式错误", Toast.LENGTH_LONG).show();
				} else {
					new RegisterTask(phone).execute();
				}
			} else {
				Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
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
						Intent i = new Intent(RegisterActivity.this,
								RegisterCheckActivity.class);
						i.putExtra("phone", phone);
						startActivity(i);
						finish();
						ShowToast.getToast(RegisterActivity.this).show(
								"验证码已发送到" + phone + "请查收");
					} else if ("0".equals(rbb.getRet())) {
						ShowToast.getToast(RegisterActivity.this).show(
								rbb.getErr());
					} else {
						ShowToast.getToast(RegisterActivity.this).show("注册失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(RegisterActivity.this).show("注册失败");
				}
			} else {
				ShowToast.getToast(RegisterActivity.this).show("注册失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}
}
