package com.fob.balls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.util.JsonParseTool;
import com.fob.util.L;

public class RemindTimeActivity extends UMengActivity implements
		OnClickListener {
	private final String TAG = "RemindTimeActivity";

	private ImageView iv_back;
	private LinearLayout ll_start, ll_end;
	private TextView tv_start, tv_end;
	private Switch sh;

	private int sl, el;
	private boolean usedisvalid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View
				.inflate(this, R.layout.remind_time, null);
		setContentView(mContentView);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.remind_time_back);

		sh = (Switch) findViewById(R.id.remind_time_status);
		ll_start = (LinearLayout) findViewById(R.id.remind_time_start);
		ll_end = (LinearLayout) findViewById(R.id.remind_time_end);
		tv_start = (TextView) findViewById(R.id.remind_time_start_tv);
		tv_end = (TextView) findViewById(R.id.remind_time_end_tv);
	}

	private void setView() {
		iv_back.setOnClickListener(this);
		ll_start.setOnClickListener(this);
		ll_end.setOnClickListener(this);

		sl = App.preferences.getIntMessage("user", "start", 0);
		el = App.preferences.getIntMessage("user", "end", 0);
		tv_start.setText(sl + ":00");
		tv_end.setText(el + ":00");

		usedisvalid = App.preferences.getBooleanMessage("user", "usedisvalid",
				false);
		sh.setChecked(usedisvalid);
	}

	@Override
	public void onClick(View v) {
		int st = 0, ed = 0;
		try {
			String start = tv_start.getText().toString();
			String end = tv_end.getText().toString();
			L.i(TAG, "onClick start=" + start + ",end=" + end);
			st = Integer.valueOf(start.substring(0, start.indexOf(":")));
			ed = Integer.valueOf(end.substring(0, end.indexOf(":")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		switch (v.getId()) {
		case R.id.remind_time_back:
			setUsedRresh(st, ed);
			break;
		case R.id.remind_time_start:
			setTime(tv_start, st);
			break;
		case R.id.remind_time_end:
			setTime(tv_end, ed);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int st = 0, ed = 0;
			try {
				String start = tv_start.getText().toString();
				String end = tv_end.getText().toString();
				L.i(TAG, "onClick start=" + start + ",end=" + end);
				st = Integer.valueOf(start.substring(0, start.indexOf(":")));
				ed = Integer.valueOf(end.substring(0, end.indexOf(":")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			setUsedRresh(st, ed);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setUsedRresh(int st, int ed) {
		boolean ud = sh.isChecked();
		L.i(TAG, "onClick st=" + st + ",ed=" + ed + ",ud=" + ud);
		if (ed > st || !ud) {
			if (sl != st || el != ed || ud != usedisvalid) {
				new UsedRreshTask(ud, st, ed).execute();
			} else {
				finish();
			}
		} else {
			ShowToast.getToast(RemindTimeActivity.this).show("结束时间必须大于开始时间");
		}
	}

	private void setTime(final TextView tv, int index) {
		final String[] levels = { "0:00", "1:00", "2:00", "3:00", "4:00",
				"5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00",
				"12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
				"19:00", "20:00", "21:00", "22:00", "23:00" };
		new AlertDialog.Builder(this)
				.setTitle("时间设置")
				.setSingleChoiceItems(levels, index,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								tv.setText(levels[which]);
								dialog.cancel();
							}
						}).show();
	}

	private class UsedRreshTask extends AsyncTask<String, Integer, String> {

		boolean ud;
		int start, end;

		public UsedRreshTask(boolean ud, int start, int end) {
			this.ud = ud;
			this.start = start;
			this.end = end;
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
				String u = "无效";
				if (ud) {
					u = "有效";
				}
				result = APIRequestServers.setUsedRresh(u, start, end);
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
						App.preferences.saveIntMessage("user", "start", start);
						App.preferences.saveIntMessage("user", "end", end);
						App.preferences.saveBooleanMessage("user",
								"usedisvalid", ud);
						ShowToast.getToast(RemindTimeActivity.this)
								.show("设置成功");
						finish();
					} else if ("0".equals(mc.getRet())) {
						ShowToast.getToast(RemindTimeActivity.this).show(
								mc.getErr());
					} else {
						ShowToast.getToast(RemindTimeActivity.this)
								.show("设置失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(RemindTimeActivity.this).show("设置失败");
				}
			} else {
				ShowToast.getToast(RemindTimeActivity.this).show("设置失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}
}
