package com.fob.page;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.fob.adapter.FOBAdapterFriends;
import com.fob.balls.R;
import com.fob.balls.LoginActivity;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructFriend;

public class FOBPageFriends extends FOBPageBase {
	public FOBPageFriends() {
		super(false);
	}

	private TabHost mTabHost;
	private String[] mTabTitle = { "球 友", "球友群" };
	private ListView mFriendsListView;

	private ImageView iv_add, iv_search;

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
		mFriendsListView.setVisibility(View.GONE);
		
		if ("球 友".equals(tabId)) {
			iv_add.setVisibility(View.GONE);
		} else {
			iv_add.setVisibility(View.VISIBLE);
		}
		for (int i = 0; i < mTabTitle.length; i++) {
			if (tabId.equals(mTabTitle[i])) {
				View mView = mTabHost.getTabWidget().getChildAt(i);
				ImageView imageView = (ImageView) mView
						.findViewById(R.id.fob_page_tab_item_imageview);
				TextView t = (TextView) mView
						.findViewById(R.id.fob_page_tab_item_textview);
				imageView.setVisibility(View.VISIBLE);
				t.setTextColor(Color.WHITE);
				mFriendsListView.setAdapter(getAdapter(tabId));
				break;
			}
		}

	}

	private ListAdapter getAdapter(String tabId) {
		FOBAdapterFriends adapter = null;
		if (tabId.equals(mTabTitle[0])) {
			List<FOBStructFriend> list = new ArrayList<FOBStructFriend>();
			for (int i = 0; i < 20; i++) {
				FOBStructFriend mode = new FOBStructFriend();
				mode.setAddre("北京海淀王子网球馆");
				mode.setFlag(i % 2 + "");
				mode.setGender(i % 2 == 0 ? "男" : "女");
				mode.setGrade("中");
				mode.setHeadurl("");
				mode.setNickname("网球王子哥");
				list.add(mode);
			}
			adapter = new FOBAdapterFriends(LoginActivity.getActivity(), list,
					FOBPageFriends.this);
		}
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
		View mRootView = View.inflate(mActivity, R.layout.fob_page_friends,
				root);
		titleClick(mRootView);
		return mRootView;
	}

	private void titleClick(View mRootView) {
		iv_add = (ImageView) mRootView.findViewById(R.id.fob_page_friends_add);
		iv_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		iv_search = (ImageView) mRootView
				.findViewById(R.id.fob_page_friends_search);
		iv_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				goNextPage(new FOBPageFriendsSearch());
			}
		});
	}

	@Override
	protected void onFirstDisplayed() {
		mFriendsListView = (ListView) getParentView().findViewById(
				R.id.fob_page_friends_list_friends);
		
		mTabHost = (TabHost) getParentView().findViewById(
				R.id.fob_page_friends_tab);
		mTabHost.setup();
		for (int i = 0; i < mTabTitle.length; i++) {
			mTabHost.addTab(mTabHost.newTabSpec(mTabTitle[i])
					.setIndicator(getTabItemView(i))
					.setContent(R.id.fob_page_friends_list_friends));
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
