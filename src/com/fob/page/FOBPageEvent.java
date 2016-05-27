package com.fob.page;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

import com.fob.adapter.FOBAdapterEventNowItem;
import com.fob.adapter.base.FOBAdapter;
import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.balls.R;
import com.fob.balls.LoginActivity;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructEvent;

public class FOBPageEvent extends FOBPageBase {
	ListView mEventListViewNow;
	ListView mEventListViewAll;
	private TabHost mTabHost;
	private String[] mTabTitle = { "当前活动", "历史活动" };
	private int[] mTabId = { R.id.fob_page_event_list_view_now,
			R.id.fob_page_event_list_view_all };

	public FOBPageEvent() {
		super(false);
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
		}
	}

	private void setTabWidget(String tabId) {
		mEventListViewNow.setVisibility(View.GONE);
		mEventListViewAll.setVisibility(View.GONE);
		
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
		View mRootView = View.inflate(mActivity, R.layout.fob_page_event, root);
		return mRootView;
	}

	private ListAdapter getNewAdapter(boolean isHistory) {
		ArrayList<FOBAdapterItemBase> list = new ArrayList<FOBAdapterItemBase>();
		for (int i = 0; i < 5; i++) {
			list.add(new FOBAdapterEventNowItem(LoginActivity
					.getActivity(), new FOBStructEvent(), FOBPageEvent.this, isHistory));

		}
		FOBAdapter adapter = new FOBAdapter(mEventListViewNow, list);
		return adapter;
	}

	@Override
	protected void onFirstDisplayed() {
		mEventListViewNow = (ListView) getParentView().findViewById(
				R.id.fob_page_event_list_view_now);
		mEventListViewNow.setAdapter(getNewAdapter(false));
		mEventListViewAll = (ListView) getParentView().findViewById(
				R.id.fob_page_event_list_view_all);
		mEventListViewAll.setAdapter(getNewAdapter(true));
		
		mTabHost = (TabHost) getParentView().findViewById(
				R.id.fob_page_event_tab);
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
		mTabHost.setCurrentTab(1);
		resetTabWidget();
		setTabWidget(mTabTitle[0]);
		mTabHost.setCurrentTab(0);
	}

}
