package com.fob.balls;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.util.CharUtil;
import com.fob.util.JsonParseTool;

public class SetNameActivity extends UMengActivity implements OnClickListener {

	private LinearLayout ll_next;
	private EditText et_name;
	private ImageView iv_back;

	private boolean isRegisterCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View.inflate(this, R.layout.set_name, null);
		setContentView(mContentView);
		isRegisterCheck = getIntent().getBooleanExtra("RegisterCheck", false);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.set_name_back);
		ll_next = (LinearLayout) findViewById(R.id.register_check_next);

		et_name = (EditText) findViewById(R.id.set_name_et);
	}

	private void setView() {
		iv_back.setOnClickListener(this);
		ll_next.setOnClickListener(this);

		et_name.addTextChangedListener(new TextWatcher() {
			String s0;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					if (CharUtil.getByteLen(s.toString()) > 9) {
						et_name.setText(s0);
						et_name.setSelection(s0.length() - 1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				s0 = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_name_back:
			if (isRegisterCheck) {
				startActivity(new Intent(this, RegisterCheckActivity.class));
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			finish();
			break;
		case R.id.register_check_next:
			String name = et_name.getText().toString();
			if (name != null && !"".equals(name)) {
				if (CharUtil.isValidName(name)) {
					new NickTask(name).execute();
				} else {
					ShowToast.getToast(this).show("昵称不可含有特殊字符");
				}
			} else {
				ShowToast.getToast(this).show("昵称不能为空");
			}
			break;
		default:
			break;
		}
	}

	private class NickTask extends AsyncTask<String, Integer, String> {

		String name;

		public NickTask(String name) {
			this.name = name;
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
				result = APIRequestServers.setNick(name);
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
					if ("1".equals(mc.getRet())) {
						startActivity(new Intent(SetNameActivity.this,
								SetCityActivity.class));
						ShowToast.getToast(SetNameActivity.this).show("设置成功");
						finish();
					} else if ("0".equals(mc.getRet())) {
						ShowToast.getToast(SetNameActivity.this).show(
								mc.getErr());
					} else {
						ShowToast.getToast(SetNameActivity.this).show("设置失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(SetNameActivity.this).show("设置失败");
				}
			} else {
				ShowToast.getToast(SetNameActivity.this).show("设置失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}

}
