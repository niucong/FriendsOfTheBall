package com.fob.balls;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.CodeListBean;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.balls.net.bean.SportBean;
import com.fob.util.JsonParseTool;

public class SetBallsActivity extends UMengActivity implements OnClickListener,
		OnItemClickListener {

	private TextView tv_finish;
	private ImageView iv_back;
	private ListView listview;

	private List<String> data;

	private boolean isInfoSetting;

	private String sport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View.inflate(this, R.layout.set_balls, null);
		setContentView(mContentView);
		isInfoSetting = getIntent().getBooleanExtra("InfoSetting", false);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.set_balls_back);
		tv_finish = (TextView) findViewById(R.id.set_balls_finish);
		listview = (ListView) findViewById(R.id.set_balls_listView);
	}

	private void setView() {
		iv_back.setOnClickListener(this);
		tv_finish.setOnClickListener(this);
		listview.setOnItemClickListener(this);

		getData();
		listview.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, data));
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		int s = 0;
		sport = App.preferences.getStringMessage("user", "sport", "网球");
		for (int i = 0; i < data.size(); i++) {
			if (sport.equals(data.get(i))) {
				s = i;
				break;
			}
		}
		listview.setItemChecked(s, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_balls_back:
			if (!isInfoSetting) {
				startActivity(new Intent(this, SetCityActivity.class));
			}
			finish();
			break;
		case R.id.set_balls_finish:
			new SportTask(sport).execute();
			break;
		default:
			break;
		}
	}

	private void getData() {
		data = new ArrayList<String>();
		String codeList = App.preferences.getStringMessage("user", "CodeList",
				"");
		try {
			CodeListBean mc = (CodeListBean) JsonParseTool.dealComplexResult(
					codeList, CodeListBean.class);
			List<SportBean> list = mc.getSportcodes();
			for (SportBean bean : list) {
				data.add(bean.getItem());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		sport = data.get(position);
	}

	private class SportTask extends AsyncTask<String, Integer, String> {

		String sport;

		public SportTask(String sport) {
			this.sport = sport;
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
				result = APIRequestServers.setFavorSport(sport);
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
						App.preferences.saveStringMessage("user", "sport",
								sport);
						ShowToast.getToast(SetBallsActivity.this).show("设置成功");
						if (!isInfoSetting) {
							Intent i = new Intent(SetBallsActivity.this,
									LoginActivity.class);
							i.putExtra("EnterMain", true);
							startActivity(i);
						} else {
							Intent i = new Intent();
							i.putExtra("sport", sport);
							setResult(RESULT_OK, i);
						}
						finish();
					} else if ("0".equals(mc.getRet())) {
						ShowToast.getToast(SetBallsActivity.this).show(
								mc.getErr());
					} else {
						ShowToast.getToast(SetBallsActivity.this).show("设置失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(SetBallsActivity.this).show("设置失败");
				}
			} else {
				ShowToast.getToast(SetBallsActivity.this).show("设置失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}

}
