package com.fob.page;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fob.adapter.FOBAdapterPlaces;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.AvDayBean;
import com.fob.balls.net.bean.AvHourBean;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructPlace;
import com.fob.util.JsonParseTool;

public class FOBPagePlaceInventory extends FOBPageBase {

	private FOBStructPlace mMode;

	private String date;

	public FOBPagePlaceInventory(FOBStructPlace mMode) {
		super(true);
		this.mMode = mMode;
	}

	private LinearLayout timeRoot;
	private ListView mListView;
	private Calendar cal;

	private void setDate(int flag) {
		String[] weekDays = { "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		cal.add(cal.DATE, flag);
		int y, m, d, w;
		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH) + 1;
		d = cal.get(Calendar.DATE);
		w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		TextView year = (TextView) timeRoot
				.findViewById(R.id.fob_page_place_time_year);
		year.setText(y + "年");
		TextView day = (TextView) timeRoot
				.findViewById(R.id.fob_page_place_time_day);
		day.setText(m + "月" + d + "日");
		TextView week = (TextView) timeRoot
				.findViewById(R.id.fob_page_place_time_week);
		week.setText(weekDays[w]);

		date = "";
		date += y;
		if (m < 10)
			date += "0";
		date += m;
		if (d < 10)
			date += "0";
		date += d;

		new AvListTask().execute();
	}

	private void showListView(List<AvHourBean> list) {
		FOBAdapterPlaces adapter = new FOBAdapterPlaces(
				LoginActivity.getActivity(), list, FOBPagePlaceInventory.this,
				mMode, date);
		mListView.setAdapter(adapter);
	}

	private String getDateString() {
		int y, m, d;
		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH);
		d = cal.get(Calendar.DATE);
		return y + "年" + m + "月" + d + "日";
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity,
				R.layout.fob_page_place_inventory, root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		timeRoot = (LinearLayout) getParentView().findViewById(
				R.id.fob_page_place_time);
		mListView = (ListView) getParentView().findViewById(
				R.id.fob_page_place_list_view);

		cal = Calendar.getInstance();
		setDate(0);
		ImageView left = (ImageView) timeRoot
				.findViewById(R.id.fob_page_place_time_left);
		left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (cal.getTime().after(new Date())) {
					setDate(-1);
				}
			}
		});
		ImageView right = (ImageView) timeRoot
				.findViewById(R.id.fob_page_place_time_right);
		right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int d1 = (int) (cal.getTime().getTime() / (1000 * 3600 * 24));
				int d0 = (int) (new Date().getTime() / (1000 * 3600 * 24));
				if (d1 - d0 < 7) {
					setDate(1);
				}
			}
		});
		setTitleBar();
	}

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = "查询球场";
		updateTitleBarInfo(titleBarInfo);
	}

	private class AvListTask extends AsyncTask<String, Integer, String> {

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			LoginActivity.getActivity().spdDialog
					.showProgressDialog("正在加载中...");
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				result = APIRequestServers.getAvList(mMode.getId(), date);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		@Override
		protected void onPostExecute(String result) {
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
			if (result != null) {
				try {
					AvDayBean adb = (AvDayBean) JsonParseTool
							.dealComplexResult(result, AvDayBean.class);
					if ("1".equals(adb.getRet())) {
						showListView(adb.getList());
					} else if ("0".equals(adb.getRet())) {
						ShowToast.getToast(LoginActivity.getActivity()).show(
								adb.getErr());
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
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
		}
	}

}
