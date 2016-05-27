package com.fob.balls;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.CityBean;
import com.fob.balls.net.bean.CodeListBean;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.util.JsonParseTool;

public class SetCityActivity extends UMengActivity implements OnClickListener,
		OnItemClickListener {

	private TextView tv_finish;
	private ImageView iv_back;
	private ListView listview;

	private List<String> data;

	private boolean isInfoSetting;

	private String city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View.inflate(this, R.layout.set_city, null);
		setContentView(mContentView);
		isInfoSetting = getIntent().getBooleanExtra("InfoSetting", false);

		findView();
		setView();

		InitLocation();
		App.app.mLocationClient.start();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.set_city_back);
		tv_finish = (TextView) findViewById(R.id.set_city_finish);
		listview = (ListView) findViewById(R.id.set_city_listView);
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
		city = App.preferences.getStringMessage("user", "city", "北京");
		for (int i = 0; i < data.size(); i++) {
			if (city.equals(data.get(i))) {
				s = i;
				break;
			}
		}
		listview.setItemChecked(s, true);
	}

	private void getData() {
		data = new ArrayList<String>();
		String codeList = App.preferences.getStringMessage("user", "CodeList",
				"");
		try {
			CodeListBean mc = (CodeListBean) JsonParseTool.dealComplexResult(
					codeList, CodeListBean.class);
			List<CityBean> list = mc.getCitycodes();
			for (CityBean bean : list) {
				data.add(bean.getCity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_city_back:
			if (!isInfoSetting)
				startActivity(new Intent(this, SetNameActivity.class));
			finish();
			break;
		case R.id.set_city_finish:
			new CityTask(city).execute();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		App.app.mLocationClient.stop();
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		city = data.get(position);
	}

	private class CityTask extends AsyncTask<String, Integer, String> {

		String city;

		public CityTask(String city) {
			this.city = city;
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
				result = APIRequestServers.setCity(city, App.app.mLon,
						App.app.mlat);
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
						App.preferences.saveStringMessage("user", "city", city);
						if (!isInfoSetting) {
							startActivity(new Intent(SetCityActivity.this,
									SetBallsActivity.class));
						} else {
							Intent i = new Intent();
							i.putExtra("city", city);
							setResult(RESULT_OK, i);
						}
						ShowToast.getToast(SetCityActivity.this).show("设置成功");
						finish();
					} else if ("0".equals(mc.getRet())) {
						ShowToast.getToast(SetCityActivity.this).show(
								mc.getErr());
					} else {
						ShowToast.getToast(SetCityActivity.this).show("设置失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(SetCityActivity.this).show("设置失败");
				}
			} else {
				ShowToast.getToast(SetCityActivity.this).show("设置失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			spdDialog.cancelProgressDialog("");
		}
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("gcj02");// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		// option.setIsNeedAddress(checkGeoLocation.isChecked());
		App.app.mLocationClient.setLocOption(option);
	}
}
