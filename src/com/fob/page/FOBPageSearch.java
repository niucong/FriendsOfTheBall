package com.fob.page;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fob.adapter.FOBAdapterPlaceAllItem;
import com.fob.adapter.base.FOBAdapter;
import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.CourtBean;
import com.fob.balls.net.bean.CourtListBean;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructPlace;
import com.fob.util.JsonParseTool;
import com.fob.util.L;

public class FOBPageSearch extends FOBPageBase {
	private final String TAG = "FOBPageSearch";
	private boolean isSubPage;

	private ListAdapter allAdapter;

	private FOBPagePlace fobPagePlace;
	public static FOBPageSearch fobPageSearch;

	private String keyword;

	public FOBPageSearch(boolean isSubPage, FOBPagePlace fobPagePlace) {
		super(isSubPage);
		this.isSubPage = isSubPage;
		this.fobPagePlace = fobPagePlace;
		fobPageSearch = this;
	}

	@Override
	protected void onPageClosed() {
		super.onPageClosed();
		fobPagePlace = null;
		fobPageSearch = null;
	}

	private ListView mPlaceListViewAll;

	/**
	 * 全部
	 * 
	 * @return
	 */
	private void getAllAdapter(List<CourtBean> list) {
		ArrayList<FOBAdapterItemBase> allList = new ArrayList<FOBAdapterItemBase>();
		for (CourtBean courtBean : list) {
			FOBStructPlace mode = new FOBStructPlace();
			mode.setName(courtBean.getCourtname());
			mode.setId(courtBean.getCourtid());
			mode.setHeadimg_all(courtBean.getHeadimg_all());
			mode.setOperatestatus(courtBean.getOperatestatus());
			mode.setStatus(courtBean.getStatus());
			allList.add(new FOBAdapterPlaceAllItem(LoginActivity.getActivity(),
					mode, fobPagePlace));
		}
		allAdapter = new FOBAdapter(mPlaceListViewAll, allList);
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.search, root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		mPlaceListViewAll = (ListView) getParentView().findViewById(
				R.id.search_listView);

		if (isSubPage) {
			setTitleBar();
		}

		final EditText et_search = (EditText) getParentView().findViewById(
				R.id.fob_page_place_search_et);
		Button btn_search = (Button) getParentView().findViewById(
				R.id.fob_page_place_search_cancel);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
		titleBarInfo.barTitle = "搜索场地";
		updateTitleBarInfo(titleBarInfo);
	}

	private class CourtListTask extends AsyncTask<String, Integer, String> {

		int cut;

		public CourtListTask(int cut) {
			this.cut = cut;
		}

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
				String city = App.preferences.getStringMessage("user", "city",
						"北京");
				String sport = App.preferences.getStringMessage("user",
						"sport", "网球");
				switch (cut) {
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
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
			if (result != null) {
				try {
					CourtListBean clbs;
					switch (cut) {
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
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
		}
	}

}
