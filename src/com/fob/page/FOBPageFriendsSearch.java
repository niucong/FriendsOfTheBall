package com.fob.page;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fob.adapter.FOBAdapterFriends;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructFriend;

public class FOBPageFriendsSearch extends FOBPageBase {

	private ListView lv;

	public FOBPageFriendsSearch() {
		super(true);
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.friends_search, root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		lv = (ListView) getParentView().findViewById(R.id.friends_search_lv);
		lv.setAdapter(getAdapter());
		setTitleBar();
	}

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = "搜索球友";
		updateTitleBarInfo(titleBarInfo);
	}

	private ListAdapter getAdapter() {
		FOBAdapterFriends adapter = null;
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
				FOBPageFriendsSearch.this, true);
		return adapter;
	}
}
