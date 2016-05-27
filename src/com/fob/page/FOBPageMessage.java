package com.fob.page;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.fob.adapter.FOBAdapterMessageLeaveItem;
import com.fob.adapter.FOBAdapterMessageNewsItem;
import com.fob.adapter.base.FOBAdapter;
import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.GeneralMsg;
import com.fob.balls.net.bean.MessagePageRES;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructLeave;
import com.fob.struct.FOBStructNews;
import com.fob.util.JsonParseTool;
import com.fob.util.L;

public class FOBPageMessage extends FOBPageBase {
	private final String TAG = "FOBPageMessage";

	private TabHost mTabHost;
	private String[] mTabTitle = { "留言", "公告" };
	private int[] mTabId = { R.id.fob_page_message_list_leave,
			R.id.fob_page_message_list_news };
	private ListView mNewsListView;
	private ListView mLeaveListView;
	private TextView tv_tip;

	private ArrayList<ImageView> ivs = new ArrayList<ImageView>();

	public FOBPageMessage() {
		super(false);
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.fob_page_message,
				root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		tv_tip = (TextView) getParentView().findViewById(
				R.id.fob_page_message_tip);

		mNewsListView = (ListView) getParentView().findViewById(
				R.id.fob_page_message_list_news);
		mNewsListView.setAdapter(getNewsAdapter());

		mLeaveListView = (ListView) getParentView().findViewById(
				R.id.fob_page_message_list_leave);
		// mLeaveListView.setAdapter(getLeaveAdapter());

		mTabHost = (TabHost) getParentView().findViewById(R.id.fob_page_tab);
		mTabHost.setup();
		for (int i = 0; i < mTabTitle.length; i++) {
			mTabHost.addTab(mTabHost.newTabSpec(mTabTitle[i])
					.setIndicator(getTabItemView(i)).setContent(mTabId[i]));
		}

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				resetTabWidget();
				setTabWidget(tabId);
			}
		});
		resetTabWidget();
		setTabWidget(mTabTitle[1]);
		mTabHost.setCurrentTab(1);
	}

	private void resetTabWidget() {
		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			View mView = mTabHost.getTabWidget().getChildAt(i);
			ImageView imageView = (ImageView) mView
					.findViewById(R.id.fob_page_tab_item_imageview);
			TextView t = (TextView) mView
					.findViewById(R.id.fob_page_tab_item_textview);
			imageView.setVisibility(View.INVISIBLE);
			t.setTextColor(Color.argb(255, 20, 148, 00));

			ivs.add((ImageView) mView.findViewById(R.id.fob_page_tab_item_new));
		}
		setNews();
	}

	private int cut;

	private FOBAdapter getNewsAdapter() {
		ArrayList<FOBAdapterItemBase> list = new ArrayList<FOBAdapterItemBase>();
		for (int i = 0; i < 20; i++) {
			FOBStructNews mode = new FOBStructNews();
			mode.setAddress("xxx阿呆发地方");
			mode.setContent("海淀第一大球场5好场");
			mode.setTitle("海淀网球三缺一");
			list.add(new FOBAdapterMessageNewsItem(LoginActivity.getActivity(),
					mode, FOBPageMessage.this));

		}
		FOBAdapter adapter = new FOBAdapter(mNewsListView, list);
		return adapter;
	}

	FOBAdapter adapter;

	@SuppressLint("SimpleDateFormat")
	private void getLeaveAdapter(List<GeneralMsg> gms) {
		ArrayList<FOBAdapterItemBase> list = new ArrayList<FOBAdapterItemBase>();
		for (GeneralMsg gm : gms) {
			FOBStructLeave mode = new FOBStructLeave();
			// TODO mode.setHead_url(gm.getImage().getThumb().getUrl());
			mode.setHead_url("");
			mode.setName(gm.getSendernick());
			mode.setContent(gm.getMsg());
			long abstime = gm.getAbstime();
			if (lt < abstime) {
				mode.setUpdate(true);
			} else {
				mode.setUpdate(false);
			}

			String date = new SimpleDateFormat("MM-dd HH:mm").format(new Date(
					abstime));
			mode.setTime(date);
			list.add(new FOBAdapterMessageLeaveItem(
					LoginActivity.getActivity(), mode, FOBPageMessage.this));
		}
		adapter = new FOBAdapter(mNewsListView, list);
	}

	private void setTabWidget(String tabId) {
		L.i(TAG, "setTabWidget tabId=" + tabId);
		if ("留言".equals(tabId)) {
			mNewsListView.setVisibility(View.GONE);
			mLeaveListView.setVisibility(View.GONE);
			tv_tip.setVisibility(View.VISIBLE);
		} else {
			mNewsListView.setVisibility(View.GONE);
			mLeaveListView.setVisibility(View.VISIBLE);
			tv_tip.setVisibility(View.GONE);

			new RecvannounceTask().execute();
		}

		for (int i = 0; i < mTabTitle.length; i++) {
			if (tabId.equals(mTabTitle[i])) {
				cut = i;
				View mView = mTabHost.getTabWidget().getChildAt(i);
				ImageView imageView = (ImageView) mView
						.findViewById(R.id.fob_page_tab_item_imageview);
				TextView t = (TextView) mView
						.findViewById(R.id.fob_page_tab_item_textview);
				imageView.setVisibility(View.VISIBLE);
				t.setTextColor(Color.WHITE);
				break;
			}
		}

	}

	private View getTabItemView(int index) {
		View view = View.inflate(LoginActivity.getActivity(),
				R.layout.fob_page_tab_item, null);
		TextView textView = (TextView) view
				.findViewById(R.id.fob_page_tab_item_textview);
		textView.setText(mTabTitle[index]);
		return view;
	}

	long lt = 0;

	private class RecvannounceTask extends AsyncTask<String, Integer, String> {

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
				lt = App.preferences.getLongMessage("user", "LeaveTime", 0);
				result = APIRequestServers.recvannounce("5", 0);
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
					App.preferences.saveIntMessage("user", "as", 0);
					LoginActivity.getActivity().frameView.setNews(false, false);

					MessagePageRES ols = (MessagePageRES) JsonParseTool
							.dealComplexResult(result, MessagePageRES.class);
					if ("1".equals(ols.getRet())) {
						List<GeneralMsg> list = ols.getPagecontentlist();
						if (list != null && list.size() > 0) {
							getLeaveAdapter(list);
							mLeaveListView.setAdapter(adapter);
							App.preferences.saveLongMessage("user",
									"LeaveTime", ols.getCurt());
						} else {
							ShowToast.getToast(App.app).show("暂无数据");
						}
					} else if ("0".equals(ols.getRet())) {
						String tip = ols.getErr();
						if (tip != null && !tip.equals(""))
							ShowToast.getToast(App.app).show(tip);
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

	public void setNews() {
		if (ivs != null) {
			if (ivs.size() > 1) {
				int as = App.preferences.getIntMessage("user", "as", 0);
				if (as > 0) {
					if (cut == 1) {
						new RecvannounceTask().execute();
					}
					ivs.get(1).setVisibility(View.VISIBLE);
					App.preferences.saveIntMessage("user", "as", 0);
				} else {
					ivs.get(1).setVisibility(View.GONE);
				}
			}
		}
	}
}
