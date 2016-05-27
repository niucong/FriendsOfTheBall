package com.fob.page;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.fob.adapter.FOBAdapterMessageHistoryItem;
import com.fob.adapter.FOBAdapterMessageNewEvent;
import com.fob.adapter.FOBAdapterMessageNowItem;
import com.fob.adapter.base.FOBAdapter;
import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.CodeListBean;
import com.fob.balls.net.bean.RecvtextRES;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.balls.net.bean.SimpleChatromsg;
import com.fob.manager.FOBCreateEventFinishManager;
import com.fob.manager.FOBPageManager;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructEvent;
import com.fob.struct.FOBStructLeave;
import com.fob.struct.FOBStructMessage;
import com.fob.tools.FOBUtils;
import com.fob.util.JsonParseTool;
import com.fob.util.L;
import com.fob.view.CreateEventWindow;

public class FOBPageDialogue extends FOBPageBase implements
		FOBCreateEventFinishManager.CreateEventFinishListener {
	protected static final String TAG = "FOBPageDialogue";

	private boolean isCanCreateEvent;
	private String groupid, courtorderid, header;

	public FOBPageDialogue(boolean isCanCreateEvent) {
		super(true);
		this.isCanCreateEvent = isCanCreateEvent;
		FOBCreateEventFinishManager.getInstance().registerObserver(this);
	}

	public FOBPageDialogue(boolean isCanCreateEvent, String groupid,
			String courtorderid, String header) {
		super(true);
		this.isCanCreateEvent = isCanCreateEvent;
		this.groupid = groupid;
		this.courtorderid = courtorderid;
		this.header = header;
		FOBCreateEventFinishManager.getInstance().registerObserver(this);
	}

	private TabHost mTabHost;
	private String[] mTabTitle = { "当前对话", "历史记录" };//
	private int[] mTabId = { R.id.fob_page_dialogue_now,
			R.id.fob_page_dialogue_list_history };//
	private ListView mFriendsListViewNow;

	private ListView mFriendsListViewHistory;

	private void resetTabWidget() {
		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			View mView = mTabHost.getTabWidget().getChildAt(i);
			ImageView imageView = (ImageView) mView
					.findViewById(R.id.fob_page_tab_item_imageview);
			TextView t = (TextView) mView
					.findViewById(R.id.fob_page_tab_item_textview);
			imageView.setVisibility(View.INVISIBLE);
			t.setTextColor(Color.argb(255, 20, 148, 00));
		}
	}

	private void setTabWidget(String tabId) {
		for (int i = 0; i < mTabTitle.length; i++) {

			if (tabId.equals(mTabTitle[i])) {
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

	FOBAdapter adapter;
	ArrayList<FOBAdapterItemBase> list = new ArrayList<FOBAdapterItemBase>();

	@SuppressLint("SimpleDateFormat")
	private void getNowAdapter(List<SimpleChatromsg> simpleChatromsgList) {

		if (simpleChatromsgList == null)
			return;
		int i = 1;
		for (SimpleChatromsg sc : simpleChatromsgList) {
			FOBStructMessage mode = new FOBStructMessage();
			mode.setContent(sc.getMsg());
			i++;

			mode.setSender(sc.getSender());
			mode.setGender(i % 2 == 0 ? "男" : "女");
			mode.setHeadurl(sc.getImage());
			String date = new SimpleDateFormat("MM-dd HH:mm").format(new Date(
					sc.getAbstime()));
			mode.setTime(date);
			FOBAdapterMessageNowItem modeItem = new FOBAdapterMessageNowItem(
					LoginActivity.getActivity(), mode, FOBPageDialogue.this,
					header);
			list.add(modeItem);
		}
	}

	private FOBAdapter getHistoryAdapter() {
		ArrayList<FOBAdapterItemBase> list = new ArrayList<FOBAdapterItemBase>();
		for (int i = 0; i < 20; i++) {
			FOBStructLeave mode = new FOBStructLeave();
			mode.setHead_url("");
			mode.setContent("电子猫 | 我昨天来过了，怎么没看见你");
			mode.setTime("13年11月29日 18:30-19:30");
			list.add(new FOBAdapterMessageHistoryItem(LoginActivity
					.getActivity(), mode, FOBPageDialogue.this));
		}
		FOBAdapter adapter = new FOBAdapter(mFriendsListViewHistory, list);
		return adapter;
	}

	private View getTabItemView(int index) {
		View view = View.inflate(LoginActivity.getActivity(),
				R.layout.fob_page_tab_item, null);
		TextView textView = (TextView) view
				.findViewById(R.id.fob_page_tab_item_textview);
		textView.setText(mTabTitle[index]);
		return view;
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.fob_page_dialogue,
				root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		mFriendsListViewNow = (ListView) getParentView().findViewById(
				R.id.fob_page_dialogue_list_now);
		mFriendsListViewNow.setDivider(null);

		mFriendsListViewHistory = (ListView) getParentView().findViewById(
				R.id.fob_page_dialogue_list_history);
		mFriendsListViewHistory.setAdapter(getHistoryAdapter());

		TextView send = (TextView) getParentView().findViewById(
				R.id.fob_page_dialogue_send);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText content = (EditText) getParentView().findViewById(
						R.id.fob_page_dialogue_input_content);
				String msg = content.getText().toString();
				if (msg != null && msg.length() > 0) {
					FOBUtils.HideSoftInput(v);
					content.setText("");

					new SendbookmsgTask(msg).execute();
				}

			}
		});
		// send.setOnTouchListener(new StateTouchListener());
		mTabHost = (TabHost) getParentView().findViewById(
				R.id.fob_page_dialogue_tab);
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
		setTabWidget(mTabTitle[0]);
		mTabHost.setCurrentTab(0);
		setTitleBar();

		startRecv();
		L.i(TAG, "createContentView");
		// new RecvbookmsgTask().execute();
	}

	private CreateEventWindow cew;

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = "聊天";
		// if (isCanCreateEvent) {
		if (!isCanCreateEvent)
			titleBarInfo.rightImageResId = R.drawable.more;
		titleBarInfo.rightImageBgId = -1;
		titleBarInfo.rightButtonClickListener = new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if (cew == null) {
					cew = CreateEventWindow.createDialog(
							LoginActivity.getActivity(),
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT, FOBPageDialogue.this);
					cew.setOutsideTouchable(true);
					cew.setFocusable(true);
					cew.setBackgroundDrawable(new BitmapDrawable());
				}
				L.i(TAG,
						"setTitleBar rightButtonClickListener "
								+ cew.isShowing());
				if (cew.isShowing()) {
					cew.dismiss();
				} else {
					cew.showAsDropDown(v, 0, 5);
				}
			}
		};
		// }
		updateTitleBarInfo(titleBarInfo);
		try {
			mTabHost.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (cew != null && cew.isShowing()) {
						cew.dismiss();
					}
					return false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isStop;

	@Override
	public void closePage() {
		L.i(TAG, "closePage");
		isStop = true;
		super.closePage();
		FOBCreateEventFinishManager.getInstance().unregisterObserver(this);
	}

	@Override
	public boolean onCreateEventFinish(FOBStructEvent event) {
		FOBPageManager.destroyAlivePagesAfter(this);
		FOBAdapterMessageNewEvent modeItem = new FOBAdapterMessageNewEvent(
				LoginActivity.getActivity(), event, FOBPageDialogue.this);
		FOBAdapter adapter = FOBAdapter.getFOBAdapter(mFriendsListViewNow);
		adapter.addListItem(modeItem);
		adapter.notifyDataSetChanged();
		mFriendsListViewNow.setSelection(adapter.getCount() - 1);
		return false;
	}

	private String lasttime = "0";

	@SuppressLint("HandlerLeak")
	private void startRecv() {
		adapter = new FOBAdapter(mFriendsListViewNow, list);
		mFriendsListViewNow.setAdapter(adapter);

		final Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if ("0".equals(lasttime)) {
					LoginActivity.getActivity().spdDialog
							.cancelProgressDialog("");
				}
				if (msg.what == 1) {
					RecvtextRES rt = (RecvtextRES) msg.obj;
					if ("1".equals(rt.getRet())) {
						lasttime = rt.getCurt() + "";
						getNowAdapter(rt.getPagecontentlist());
						adapter.notifyDataSetChanged();
						mFriendsListViewNow.setSelection(adapter.getCount());
					} else if ("0".equals(rt.getRet())) {
						if ("0".equals(lasttime))
							ShowToast.getToast(App.app).show(rt.getErr());
					} else {
						if ("0".equals(lasttime))
							ShowToast.getToast(App.app).show("呼叫失败");
					}
				} else {
					ShowToast.getToast(App.app).show("呼叫失败");
				}
			}
		};

		LoginActivity.getActivity().spdDialog.showProgressDialog("正在呼叫管理员...");
		new Thread() {
			public void run() {
				while (!isStop) {
					try {
						String result = APIRequestServers.recvbookmsg(groupid,
								lasttime, "0", "20");

						if (result != null) {
							try {
								RecvtextRES rt = (RecvtextRES) JsonParseTool
										.dealComplexResult(result,
												RecvtextRES.class);
								Message msg = new Message();
								msg.what = 1;
								msg.obj = rt;
								h.sendMessage(msg);
							} catch (Exception e) {
								e.printStackTrace();
								if ("0".equals(lasttime)) {
									h.sendEmptyMessage(0);
								}
							}
						} else {
							if ("0".equals(lasttime)) {
								h.sendEmptyMessage(0);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
						if ("0".equals(lasttime)) {
							h.sendEmptyMessage(0);
						}
					}

					int time = 0;
					String codeList = App.preferences.getStringMessage("user",
							"CodeList", "");
					try {
						CodeListBean mc = (CodeListBean) JsonParseTool
								.dealComplexResult(codeList, CodeListBean.class);
						time = Integer.valueOf(mc.getManagerchatbeatrate());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (time == 0)
						time = 15;
					try {
						sleep(time * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	private class SendbookmsgTask extends AsyncTask<String, Integer, String> {

		String msg;

		public SendbookmsgTask(String msg) {
			this.msg = msg;
		}

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			LoginActivity.getActivity().spdDialog
					.showProgressDialog("正在发送中...");
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				result = APIRequestServers.sendbookmsg(groupid, "text", msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(String result) {
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
			if (result != null) {
				try {
					ResultBasicBean rt = (ResultBasicBean) JsonParseTool
							.dealSingleResult(result, ResultBasicBean.class);
					if ("1".equals(rt.getRet())) {
						App.preferences.saveLongMessage("msgLastUpdate",
								courtorderid, rt.getCurt());
						App.preferences.saveLongMessage("lastUpdate",
								courtorderid, rt.getCurt());

						FOBStructMessage mode = new FOBStructMessage();
						mode.setContent(msg);
						mode.setSender(App.preferences.getStringMessage("user",
								"Username", ""));
						mode.setGender("男");
						mode.setHeadurl("");
						mode.setTime(new SimpleDateFormat("MM-dd HH:mm")
								.format(new Date(rt.getCurt())));
						FOBAdapterMessageNowItem modeItem = new FOBAdapterMessageNowItem(
								LoginActivity.getActivity(), mode,
								FOBPageDialogue.this, null);
						FOBAdapter adapter = FOBAdapter
								.getFOBAdapter(mFriendsListViewNow);
						adapter.addListItem(modeItem);
						adapter.notifyDataSetChanged();
						mFriendsListViewNow
								.setSelection(adapter.getCount() - 1);
					} else if ("0".equals(rt.getRet())) {
						String tip = rt.getErr();
						if (tip != null && !tip.equals(""))
							ShowToast.getToast(App.app).show(tip);
					} else {

					}
				} catch (Exception e) {
					e.printStackTrace();

				}
			} else {

			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
		}
	}
}
