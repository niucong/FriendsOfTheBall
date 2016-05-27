package com.fob.page;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.fob.adapter.MembersViewAdapter;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructFriend;

public class FOBPageMembersView extends FOBPageBase {
	public FOBPageMembersView() {
		super(true);
	}

	private GridView mFriendsListView;

	private ListAdapter getAdapter() {
		MembersViewAdapter adapter = null;
		ArrayList<FOBStructFriend> list = new ArrayList<FOBStructFriend>();
		for (int i = 0; i < 10; i++) {
			FOBStructFriend mode = new FOBStructFriend();
			mode.setAddre("北京海淀王子网球馆");
			mode.setFlag(i % 2 + "");
			mode.setGender(i % 2 == 0 ? "男" : "女");
			mode.setGrade("中");
			mode.setHeadurl("");
			mode.setNickname("网球王子哥");
			list.add(mode);
		}
		list.add(new FOBStructFriend());
		list.add(new FOBStructFriend());
		adapter = new MembersViewAdapter(LoginActivity.getActivity(), list,
				FOBPageMembersView.this);
		return adapter;
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity,
				R.layout.fob_page_members_view, root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		mFriendsListView = (GridView) getParentView().findViewById(
				R.id.fob_page_members_view_grid);
		mFriendsListView.setAdapter(getAdapter());
		setTitleBar();
	}

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = "聊天组（10人）";
		// if (isCanCreateEvent) {
		titleBarInfo.rightImageResId = R.drawable.more;
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
