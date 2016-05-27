package com.fob.balls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.balls.net.bean.SetSelfInfoRES;
import com.fob.util.CharUtil;
import com.fob.util.JsonParseTool;

public class InfoSettingActivity extends UMengActivity implements
		OnClickListener {

	private TextView tv_finish, tv_level, tv_role, tv_sex, tv_city, tv_balls;
	private EditText et_name, et_email;
	private ImageView iv_back;

	private String nickname, favorsportlevel, sex, role, email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View.inflate(this, R.layout.info_setting,
				null);
		setContentView(mContentView);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.info_setting_back);
		tv_finish = (TextView) findViewById(R.id.info_setting_finish);
		tv_level = (TextView) findViewById(R.id.info_setting_level);
		tv_sex = (TextView) findViewById(R.id.info_setting_sex);
		tv_city = (TextView) findViewById(R.id.info_setting_city);
		tv_balls = (TextView) findViewById(R.id.info_setting_balls);

		et_name = (EditText) findViewById(R.id.info_setting_name);
		tv_role = (TextView) findViewById(R.id.info_setting_role);
		et_email = (EditText) findViewById(R.id.info_setting_email);
	}

	private void setView() {
		iv_back.setOnClickListener(this);
		tv_finish.setOnClickListener(this);
		tv_level.setOnClickListener(this);
		tv_sex.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		tv_balls.setOnClickListener(this);
		tv_role.setOnClickListener(this);

		tv_city.setText(App.preferences.getStringMessage("user", "city", "北京"));
		tv_balls.setText(App.preferences
				.getStringMessage("user", "sport", "网球"));
		nickname = App.preferences.getStringMessage("user", "nickname", "");
		if (nickname == null)
			nickname = "";
		favorsportlevel = App.preferences.getStringMessage("user",
				"favorsportlevel", "");
		if (favorsportlevel == null)
			favorsportlevel = "";
		sex = App.preferences.getStringMessage("user", "sex", "");
		if (sex == null)
			sex = "";
		role = App.preferences.getStringMessage("user", "role", "");
		if (role == null)
			role = "";
		email = App.preferences.getStringMessage("user", "email", "");
		if (email == null)
			email = "";

		et_name.setText(nickname);
		tv_role.setText(role);
		et_email.setText(email);
		tv_level.setText(favorsportlevel);
		tv_sex.setText(sex);

		new GetSelfInfoTask().execute();

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

	private class GetSelfInfoTask extends AsyncTask<String, Integer, String> {

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			spdDialog.showProgressDialog("正在加载中...");
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				result = APIRequestServers.getselfinfo();
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
					SetSelfInfoRES res = (SetSelfInfoRES) JsonParseTool
							.dealSingleResult(result, SetSelfInfoRES.class);
					if ("1".equals(res.getRet())) {
						tv_city.setText(res.getCity());
						tv_balls.setText(res.getFavorsport());
						nickname = res.getNickname();
						favorsportlevel = res.getFavorsportlevel();
						sex = res.getSex();
						role = res.getRole();
						email = res.getEmail();

						if (nickname != null && !"".equals(nickname)) {
							et_name.setText(nickname);
						} else {
							nickname = "";
						}
						if (role != null && !"".equals(role)) {
							tv_role.setText(role);
						} else {
							role = "";
						}
						if (email != null && !"".equals(email)) {
							et_email.setText(email);
						} else {
							email = "";
						}
						if (favorsportlevel != null
								&& !"".equals(favorsportlevel)) {
							tv_level.setText(favorsportlevel);
						} else {
							favorsportlevel = "";
						}
						if (sex != null && !"".equals(sex)) {
							tv_sex.setText(sex);
						} else {
							sex = "";
						}
					} else if ("0".equals(res.getRet())) {
						ShowToast.getToast(App.app).show(res.getErr());
					} else {
						ShowToast.getToast(App.app).show("加载失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(App.app).show("加载失败");
				}
			} else {
				ShowToast.getToast(App.app).show("加载失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.info_setting_back:
			setSelfInfo();
			break;
		case R.id.info_setting_finish:

			break;
		case R.id.info_setting_level:
			final String[] levels = { "初级", "中级", "高级" };
			int j = 0;
			for (int k = 0; k < levels.length; k++) {
				if (levels[k].equals(tv_level.getText().toString())) {
					j = k;
					break;
				}
			}
			new AlertDialog.Builder(this)
					.setTitle("选择球技水平")
					.setSingleChoiceItems(levels, j,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									tv_level.setText(levels[which]);
									dialog.cancel();
								}
							}).show();
			break;
		case R.id.info_setting_sex:
			final String[] sexs = { "男", "女" };
			int i = "男".equals(tv_sex.getText().toString()) ? 0 : 1;
			new AlertDialog.Builder(this)
					.setTitle("选择性别")
					.setSingleChoiceItems(sexs, i,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									tv_sex.setText(sexs[which]);
									dialog.cancel();
								}
							}).show();
			break;
		case R.id.info_setting_role:
			final String[] roles = { "球友", "教练" };
			int m = "球友".equals(tv_role.getText().toString()) ? 0 : 1;
			new AlertDialog.Builder(this)
					.setTitle("选择职业")
					.setSingleChoiceItems(roles, m,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									tv_role.setText(roles[which]);
									dialog.cancel();
								}
							}).show();
			break;
		case R.id.info_setting_city:
			Intent ci = new Intent(this, SetCityActivity.class);
			ci.putExtra("InfoSetting", true);
			startActivityForResult(ci, 10);
			break;
		case R.id.info_setting_balls:
			Intent bi = new Intent(this, SetBallsActivity.class);
			bi.putExtra("InfoSetting", true);
			startActivityForResult(bi, 11);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 11) {
				String sport = data.getStringExtra("sport");
				tv_balls.setText(sport);
			} else if (requestCode == 10) {
				String city = data.getStringExtra("city");
				tv_city.setText(city);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setSelfInfo();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setSelfInfo() {
		String rn = et_name.getText().toString();
		String rr = tv_role.getText().toString();
		String re = et_email.getText().toString();
		String rl = tv_level.getText().toString();
		String rs = tv_sex.getText().toString();

		try {
			if (!nickname.equals(rn) || !favorsportlevel.equals(rl)
					|| !sex.equals(rs) || !role.equals(rr) || !email.equals(re)) {
				if (!CharUtil.isValidName(rn)) {
					ShowToast.getToast(InfoSettingActivity.this).show(
							"昵称不可含有特殊字符");
				} else if (!CharUtil.isValidEmail(re)) {
					ShowToast.getToast(InfoSettingActivity.this).show("邮箱格式错误");
				} else {
					new SelfInfoTask(rn, rl, rs, rr, re).execute();
				}
			} else {
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
			finish();
		}
	}

	private class SelfInfoTask extends AsyncTask<String, Integer, String> {

		String nn, fsl, sx, re, em;

		public SelfInfoTask(String nickname, String favorsportlevel,
				String sex, String role, String email) {
			this.nn = nickname;
			this.fsl = favorsportlevel;
			this.sx = sex;
			this.re = role;
			this.em = email;
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
				result = APIRequestServers.setSelfInfo(nn, fsl, sx, re, em);
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
						App.preferences.saveStringMessage("user", "nickname",
								nn);
						App.preferences.saveStringMessage("user",
								"favorsportlevel", fsl);
						App.preferences.saveStringMessage("user", "sex", sx);
						App.preferences.saveStringMessage("user", "role", re);
						App.preferences.saveStringMessage("user", "email", em);
						ShowToast.getToast(InfoSettingActivity.this).show(
								"设置成功");
						finish();
					} else if ("0".equals(mc.getRet())) {
						ShowToast.getToast(InfoSettingActivity.this).show(
								mc.getErr());
					} else {
						ShowToast.getToast(InfoSettingActivity.this).show(
								"设置失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(InfoSettingActivity.this).show("设置失败");
				}
			} else {
				ShowToast.getToast(InfoSettingActivity.this).show("设置失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}
}
