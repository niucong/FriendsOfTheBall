package com.fob.page;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.fob.adapter.FOBAdapterPlaceAllItem;
import com.fob.adapter.FOBAdapterPlaceCommonItem;
import com.fob.adapter.FOBAdapterPlaceConfrimItem;
import com.fob.adapter.FOBAdapterPlaceWaitItem;
import com.fob.adapter.base.FOBAdapter;
import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.CourtBean;
import com.fob.balls.net.bean.CourtListBean;
import com.fob.balls.net.bean.CourtorderRES;
import com.fob.balls.net.bean.GetOrderListRES;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructPlace;
import com.fob.util.JsonParseTool;
import com.fob.util.L;

public class FOBPagePlace extends FOBPageBase {
	private final String TAG = "FOBPagePlace";
	// implements FOBCreateEventFinishManager.CreateEventFinishListener
	private boolean isSubPage;

	private ImageView iv_search;
	private ListAdapter waitAdapter, confirmAdapter, allAdapter, commonAdapter;
	private ArrayList<ImageView> ivs = new ArrayList<ImageView>();

	public static FOBPagePlace fobPagePlace;

	private String keyword;

	public FOBPagePlace(boolean isSubPage) {
		super(isSubPage);
		this.isSubPage = isSubPage;
		// FOBCreateEventFinishManager.getInstance().registerObserver(this);
		fobPagePlace = this;
	}

	@Override
	protected void onPageClosed() {
		super.onPageClosed();
		// fobPagePlace = null;
	}

	private TabHost mTabHost;
	private String[] mTabTitle = { "已确认", "待确认", "全部", "常用" };
	private int[] mTabId = { R.id.fob_page_place_list_view_confrim,
			R.id.fob_page_place_list_view_wait,
			R.id.fob_page_place_list_view_all,
			R.id.fob_page_place_list_view_common };
	private ListView mPlaceListViewConfirm;
	private ListView mPlaceListViewWait;
	private ListView mPlaceListViewAll;
	private ListView mPlaceListViewCommon;
	private LinearLayout ll_footer, ll_footer2;
	private ProgressBar pb_footer, pb_footer2;
	private TextView tv_footer, tv_footer2;

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
		setNews(false, false);
	}

	private int cut;

	private void setTabWidget(String tabId) {
		L.d(TAG, "setTabWidget tabId=" + tabId);
		if ("全部".equals(tabId)) {
			iv_search.setVisibility(View.VISIBLE);
		} else {
			iv_search.setVisibility(View.GONE);
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
				// refreash(i);
				break;
			}
		}
	}

	boolean loading = false;

	public void refreash(int i) {
		L.i(TAG, "refreash i=" + i);
		mTabHost.setCurrentTab(i);
		if (!loading) {
			new CourtListTask(i).execute();
		}
	}

	private ArrayList<FOBAdapterItemBase> cList;

	/**
	 * 已确定
	 * 
	 * @return
	 */
	private void getConfirmAdapter(List<CourtorderRES> list) {
		if (cList == null)
			cList = new ArrayList<FOBAdapterItemBase>();
		for (CourtorderRES bean : list) {
			FOBStructPlace mode = new FOBStructPlace();
			mode.setName(bean.getCourtname());
			mode.setId(bean.getCourtorderid());
			mode.setHeadimg_all(bean.getHeadimg_all());

			String id = bean.getCourtorderid();
			long lastUpdate = bean.getLastupdate();
			long ut = App.preferences.getLongMessage("lastUpdate", id, 0);
			L.d(TAG, "getView id=" + id + ",lastUpdate=" + lastUpdate + ",ut="
					+ ut);
			if (ut < lastUpdate) {
				mode.setUpdate(true);
				App.preferences.saveLongMessage("lastUpdate", id, lastUpdate);
				App.preferences
						.saveBooleanMessage("lastUpdateClick", id, false);
			} else if (App.preferences.getBooleanMessage("lastUpdateClick", id,
					false)) {
				mode.setUpdate(false);
			}

			long msgLastUpdate = bean.getMsglastupdate();
			long mut = App.preferences.getLongMessage("msgLastUpdate", id, 0);
			if (mut < msgLastUpdate) {
				mode.setMsgUpdate(true);
				App.preferences.saveLongMessage("msgLastUpdate", id,
						msgLastUpdate);
			} else {
				mode.setMsgUpdate(false);
			}
			cList.add(new FOBAdapterPlaceConfrimItem(LoginActivity
					.getActivity(), mode, FOBPagePlace.this, bean));
		}
		if (confirmAdapter == null)
			confirmAdapter = new FOBAdapter(mPlaceListViewConfirm, cList);
	}

	/**
	 * 待确定
	 * 
	 * @return
	 */
	private void getWaitAdapter(List<CourtorderRES> list) {
		ArrayList<FOBAdapterItemBase> uList = new ArrayList<FOBAdapterItemBase>();
		if (list != null)
			for (CourtorderRES bean : list) {
				FOBStructPlace mode = new FOBStructPlace();
				mode.setName(bean.getCourtname());
				mode.setId(bean.getCourtorderid());
				mode.setHeadimg_all(bean.getHeadimg_all());

				String id = bean.getCourtorderid();
				long lastUpdate = bean.getLastupdate();
				long ut = App.preferences.getLongMessage("lastUpdate", id, 0);
				L.d(TAG, "getView id=" + id + ",lastUpdate=" + lastUpdate
						+ ",ut=" + ut);
				if (ut < lastUpdate) {
					mode.setUpdate(true);
					App.preferences.saveLongMessage("lastUpdate", id,
							lastUpdate);
					App.preferences.saveBooleanMessage("lastUpdateClick", id,
							false);
				} else if (App.preferences.getBooleanMessage("lastUpdateClick",
						id, false)) {
					mode.setUpdate(false);
				}

				long msgLastUpdate = bean.getMsglastupdate();
				long mut = App.preferences.getLongMessage("msgLastUpdate", id,
						0);
				if (mut < msgLastUpdate) {
					mode.setMsgUpdate(true);
					App.preferences.saveLongMessage("msgLastUpdate", id,
							msgLastUpdate);
				} else {
					mode.setMsgUpdate(false);
				}

				// mode.setNumber(courtBean.get);
				uList.add(new FOBAdapterPlaceWaitItem(LoginActivity
						.getActivity(), mode, FOBPagePlace.this, bean));
				// App.preferences.saveLongMessage("WaitCourt",
				// bean.getCourtorderid(), bean.getLastupdate());
				L.d(TAG,
						"getWaitAdapter Courtorderid=" + bean.getCourtorderid()
								+ ",Lastupdate=" + bean.getLastupdate());
			}
		waitAdapter = new FOBAdapter(mPlaceListViewWait, uList);
	}

	private ArrayList<FOBAdapterItemBase> allList;

	/**
	 * 全部
	 * 
	 * @return
	 */
	private void getAllAdapter(List<CourtBean> list) {
		if (allList == null)
			allList = new ArrayList<FOBAdapterItemBase>();
		// list.add(new FOBAdapterPlaceAllItem(LoginActivity.getActivity(),
		// null,
		// FOBPagePlace.this));
		for (CourtBean courtBean : list) {
			FOBStructPlace mode = new FOBStructPlace();
			mode.setName(courtBean.getCourtname());
			mode.setId(courtBean.getCourtid());
			mode.setHeadimg_all(courtBean.getHeadimg_all());
			mode.setOperatestatus(courtBean.getOperatestatus());
			mode.setStatus(courtBean.getStatus());
			allList.add(new FOBAdapterPlaceAllItem(LoginActivity.getActivity(),
					mode, FOBPagePlace.this));
		}
		allAdapter = new FOBAdapter(mPlaceListViewConfirm, allList);
	}

	/**
	 * 常用
	 * 
	 * @return
	 */
	private void getCommonAdapter(List<CourtBean> list) {
		// if (listCommon.size() == 0) {
		// listCommon.add(new FOBAdapterPlaceCommonItem(LoginActivity
		// .getActivity(), null, FOBPagePlace.this));
		ArrayList<FOBAdapterItemBase> listCommon = new ArrayList<FOBAdapterItemBase>();
		for (CourtBean courtBean : list) {
			FOBStructPlace mode = new FOBStructPlace();
			mode.setName(courtBean.getCourtname());
			mode.setId(courtBean.getCourtid());
			mode.setIsBook(courtBean.getIsbook());
			mode.setHeadimg_all(courtBean.getHeadimg_all());
			mode.setOperatestatus(courtBean.getOperatestatus());
			mode.setStatus(courtBean.getStatus());
			listCommon.add(new FOBAdapterPlaceCommonItem(LoginActivity
					.getActivity(), mode, FOBPagePlace.this));
		}
		// }
		commonAdapter = new FOBAdapter(mPlaceListViewConfirm, listCommon);
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
		View mRootView = View.inflate(mActivity, R.layout.fob_page_place, root);
		titleClick(mRootView);
		return mRootView;
	}

	private void titleClick(View mRootView) {
		iv_search = (ImageView) mRootView
				.findViewById(R.id.fob_page_place_search);
	}

	@Override
	protected void onFirstDisplayed() {
		mPlaceListViewConfirm = (ListView) getParentView().findViewById(
				R.id.fob_page_place_list_view_confrim);
		mPlaceListViewWait = (ListView) getParentView().findViewById(
				R.id.fob_page_place_list_view_wait);
		mPlaceListViewAll = (ListView) getParentView().findViewById(
				R.id.fob_page_place_list_view_all);
		mPlaceListViewCommon = (ListView) getParentView().findViewById(
				R.id.fob_page_place_list_view_common);

		ll_footer = (LinearLayout) LayoutInflater.from(
				LoginActivity.getActivity()).inflate(R.layout.fob_page_footer,
				null);
		mPlaceListViewConfirm.addFooterView(ll_footer);
		pb_footer = (ProgressBar) ll_footer
				.findViewById(R.id.fob_page_footer_pb);
		tv_footer = (TextView) ll_footer.findViewById(R.id.fob_page_footer_tv);
		pb_footer.setVisibility(View.GONE);
		ll_footer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_footer.setText("加载中…");
				pb_footer.setVisibility(View.VISIBLE);
				new CourtListTask(cut).execute();
			}
		});

		ll_footer2 = (LinearLayout) LayoutInflater.from(
				LoginActivity.getActivity()).inflate(R.layout.fob_page_footer,
				null);
		mPlaceListViewAll.addFooterView(ll_footer2);
		pb_footer2 = (ProgressBar) ll_footer2
				.findViewById(R.id.fob_page_footer_pb);
		tv_footer2 = (TextView) ll_footer2
				.findViewById(R.id.fob_page_footer_tv);
		pb_footer2.setVisibility(View.GONE);
		ll_footer2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_footer2.setText("加载中…");
				pb_footer2.setVisibility(View.VISIBLE);
				new CourtListTask(cut).execute();
			}
		});

		LinearLayout footer = (LinearLayout) LayoutInflater.from(
				LoginActivity.getActivity()).inflate(R.layout.fob_page_footer2,
				null);
		TextView tv = (TextView) footer.findViewById(R.id.fob_page_footer_tv);
		tv.setText("下单后，请等待管理员确认。\n如长时间未处理，取消此订单后，改订其它馆。");
		mPlaceListViewWait.addFooterView(footer);

		if (isSubPage) {
			getParentView().findViewById(R.id.fob_page_place_title)
					.setVisibility(View.GONE);
			setTitleBar();
		}
		mTabHost = (TabHost) getParentView().findViewById(
				R.id.fob_page_place_tab);
		mTabHost.setup();
		for (int i = 0; i < mTabTitle.length; i++) {
			mTabHost.addTab(mTabHost.newTabSpec(mTabTitle[i])
					.setIndicator(getTabItemView(i)).setContent(mTabId[i]));
		}

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				page = 0;
				page2 = 0;
				resetTabWidget();
				setTabWidget(tabId);
				refreash(cut);
			}
		});
		// resetTabWidget();

		if (App.preferences.getBooleanMessage("user", "MyCourt", false)) {
			refreash(3);
		} else {
			refreash(2);
		}

		final LinearLayout ll_search = (LinearLayout) getParentView()
				.findViewById(R.id.fob_page_place_search_ll);
		final EditText et_search = (EditText) getParentView().findViewById(
				R.id.fob_page_place_search_et);
		Button btn_search = (Button) getParentView().findViewById(
				R.id.fob_page_place_search_cancel);
		iv_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if (ll_search.isShown()) {
				// ll_search.setVisibility(View.GONE);
				// } else {
				// ll_search.setVisibility(View.VISIBLE);
				// }
				fobPagePlace.goNextPage(new FOBPageSearch(true, fobPagePlace));
			}
		});
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ll_search.setVisibility(View.GONE);
				new CourtListTask(2).execute();
			}
		});
		et_search.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				L.i(TAG, "keyCode=" + keyCode + ",isOnKey=" + isOnKey);
				if (keyCode == KeyEvent.KEYCODE_ENTER && isOnKey) {
					isOnKey = false;
					keyword = et_search.getText().toString();
					if (!"".equals(keyword))
						new CourtListTask(4).execute();
				} else {
					isOnKey = true;
				}
				return false;
			}
		});
	}

	private boolean isOnKey;

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = "球场列表";
		updateTitleBarInfo(titleBarInfo);
	}

	private long page = 0;
	private long page2 = 0;

	private class CourtListTask extends AsyncTask<String, Integer, String> {

		int cut;

		public CourtListTask(int cut) {
			this.cut = cut;
		}

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			loading = true;
			LoginActivity.getActivity().spdDialog
					.showProgressDialog("正在加载中...");
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				String city = App.preferences.getStringMessage("user", "city",
						"北京");
				String sport = App.preferences.getStringMessage("user",
						"sport", "网球");
				switch (cut) {
				case 0:
					result = APIRequestServers.getConfirmed(sport, page + "",
							"5");
					break;
				case 1:
					result = APIRequestServers.getUnconfirmed(sport, "0", "5");
					break;
				case 2:
					result = APIRequestServers.getAllCourt(city, sport, page2
							+ "", "5");
					break;
				case 3:
					result = APIRequestServers.getMyCourt(sport);
					break;
				case 4:
					result = APIRequestServers.searchallcourt(keyword, city,
							sport);
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		@Override
		protected void onPostExecute(String result) {
			loading = false;
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
			if (result != null) {
				try {
					GetOrderListRES ols;
					CourtListBean clbs;
					switch (cut) {
					case 0:
						App.preferences.saveIntMessage("user", "cs", 0);
						LoginActivity.getActivity().frameView.setNews(false,
								false);
						setNews(false, false);

						ols = (GetOrderListRES) JsonParseTool
								.dealComplexResult(result,
										GetOrderListRES.class);
						if ("1".equals(ols.getRet())) {
							if (page == 0) {
								confirmAdapter = null;
								cList = null;
							}
							page = ols.getPageno() + 1;
							if (page > ols.getPagecount()) {
								ll_footer.setVisibility(View.GONE);
							} else {
								tv_footer.setText("更多");
								pb_footer.setVisibility(View.GONE);
								ll_footer.setVisibility(View.VISIBLE);
							}

							List<CourtorderRES> list = ols.getList();
							if (list != null && list.size() > 0) {
								L.d(TAG,
										"CourtListTask size="
												+ list.size()
												+ ",Count="
												+ mPlaceListViewConfirm
														.getFooterViewsCount());

								if (confirmAdapter == null) {
									getConfirmAdapter(list);
									mPlaceListViewConfirm
											.setAdapter(confirmAdapter);
								} else {
									getConfirmAdapter(list);
								}
							} else {
								if (confirmAdapter == null
										|| confirmAdapter.getCount() == 0)
									ShowToast.getToast(App.app).show("暂无数据");
							}
						} else if ("0".equals(ols.getRet())) {
							String tip = ols.getErr();
							if (tip != null && !tip.equals(""))
								ShowToast.getToast(App.app).show(tip);
						} else {
							ShowToast.getToast(App.app).show("加载失败");
						}
						break;
					case 1:
						App.preferences.saveIntMessage("user", "ucs", 0);
						LoginActivity.getActivity().frameView.setNews(false,
								false);
						setNews(false, false);

						ols = (GetOrderListRES) JsonParseTool
								.dealComplexResult(result,
										GetOrderListRES.class);
						if ("1".equals(ols.getRet())) {
							List<CourtorderRES> list = ols.getList();
							getWaitAdapter(list);
							mPlaceListViewWait.setAdapter(waitAdapter);
							if (list != null && list.size() > 0) {
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
						break;
					case 2:
						clbs = (CourtListBean) JsonParseTool.dealComplexResult(
								result, CourtListBean.class);
						if ("1".equals(clbs.getRet())) {
							if (page2 == 0) {
								allAdapter = null;
								allList = null;
							}
							page2 = clbs.getPageno() + 1;
							if (page2 > clbs.getPagecount()) {
								ll_footer2.setVisibility(View.GONE);
							} else {
								tv_footer2.setText("更多");
								pb_footer2.setVisibility(View.GONE);
								ll_footer2.setVisibility(View.VISIBLE);
							}

							List<CourtBean> list = clbs.getList();
							if (list != null && list.size() > 0) {
								getAllAdapter(list);
								mPlaceListViewAll.setAdapter(allAdapter);
							} else {
								ShowToast.getToast(App.app).show("暂无数据");
							}
						} else if ("0".equals(clbs.getRet())) {
							String tip = clbs.getErr();
							if (tip != null && !tip.equals(""))
								ShowToast.getToast(App.app).show(tip);
						} else {
							ShowToast.getToast(App.app).show("加载失败");
						}
						break;
					case 3:
						clbs = (CourtListBean) JsonParseTool.dealComplexResult(
								result, CourtListBean.class);
						if ("1".equals(clbs.getRet())) {
							List<CourtBean> list = clbs.getList();
							if (list != null && list.size() > 0) {
								getCommonAdapter(list);
								App.preferences.saveBooleanMessage("user",
										"MyCourt", true);
							} else {
								getCommonAdapter(new ArrayList<CourtBean>());
								ShowToast.getToast(App.app).show("暂无数据");
								App.preferences.saveBooleanMessage("user",
										"MyCourt", false);
							}
							mPlaceListViewCommon.setAdapter(commonAdapter);
						} else if ("0".equals(clbs.getRet())) {
							String tip = clbs.getErr();
							if (tip != null && !tip.equals(""))
								ShowToast.getToast(App.app).show(tip);
						} else {
							ShowToast.getToast(App.app).show("加载失败");
						}
						break;
					case 4:
						clbs = (CourtListBean) JsonParseTool.dealComplexResult(
								result, CourtListBean.class);
						if ("1".equals(clbs.getRet())) {
							List<CourtBean> list = clbs.getList();
							if (list != null && list.size() > 0) {
								getAllAdapter(list);
								mPlaceListViewAll.setAdapter(allAdapter);
							} else {
								ShowToast.getToast(App.app).show("暂无数据");
							}
						} else if ("0".equals(clbs.getRet())) {
							String tip = clbs.getErr();
							if (tip != null && !tip.equals(""))
								ShowToast.getToast(App.app).show(tip);
						} else {
							ShowToast.getToast(App.app).show("加载失败");
						}
						break;

					default:
						break;
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
			loading = false;
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
		}
	}

	public void setNews(boolean csFlag, boolean ucsFlag) {
		L.d(TAG, "setNews csFlag=" + csFlag + ",ucsFlag=" + ucsFlag);
		if (ivs != null) {
			int cs = App.preferences.getIntMessage("user", "cs", 0);
			L.d(TAG, "setNews cs=" + cs + ",cut=" + cut);
			if (ivs.size() > 0)
				if (cs > 0) {
					ivs.get(0).setVisibility(View.VISIBLE);
				} else {
					ivs.get(0).setVisibility(View.GONE);
				}
			if (csFlag && cut == 0) {
				page = 0;
				new CourtListTask(cut).execute();
			}

			int ucs = App.preferences.getIntMessage("user", "ucs", 0);
			L.d(TAG, "setNews ucs=" + ucs + ",cut=" + cut);
			if (ivs.size() > 1)
				if (ucs > 0) {
					ivs.get(1).setVisibility(View.VISIBLE);
				} else {
					ivs.get(1).setVisibility(View.GONE);
				}
			if (ucsFlag && cut == 1) {
				new CourtListTask(cut).execute();
			}
		}
	}

}
