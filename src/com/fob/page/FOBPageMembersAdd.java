package com.fob.page;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fob.adapter.FOBAdapterMembers;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructFriend;

public class FOBPageMembersAdd extends FOBPageBase {
	public FOBPageMembersAdd() {
		super(true);
	}

	private ListView mFriendsListView;

	private ListAdapter getAdapter() {
		FOBAdapterMembers adapter = null;
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
		adapter = new FOBAdapterMembers(LoginActivity.getActivity(), list,
				FOBPageMembersAdd.this);
		return adapter;
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.fob_page_members_add,
				root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		mFriendsListView = (ListView) getParentView().findViewById(
				R.id.fob_page_members_add_list);
		mFriendsListView.setAdapter(getAdapter());

		setTitleBar();
	}

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = "我的球友";
		// if (isCanCreateEvent) {
		titleBarInfo.rightImageResId = R.drawable.fob_creat_event;
		titleBarInfo.rightImageBgId = -1;
		titleBarInfo.rightButtonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePage();
			}
		};
		// }
		updateTitleBarInfo(titleBarInfo);
	}
}
