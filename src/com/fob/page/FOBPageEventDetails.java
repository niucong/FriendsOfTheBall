package com.fob.page;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.fob.adapter.MembersViewAdapter;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructEvent;
import com.fob.struct.FOBStructFriend;

public class FOBPageEventDetails extends FOBPageBase {
	private FOBStructEvent mMode;
	private boolean isHistory;

	public FOBPageEventDetails(FOBStructEvent mode) {
		super(true);
		this.mMode = mode;
	}

	public FOBPageEventDetails(FOBStructEvent mode, boolean isHistory) {
		super(true);
		this.mMode = mode;
		this.isHistory = isHistory;
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity,
				R.layout.fob_page_event_detail, root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		setTitleBar();
		GridView mFriendsListView = (GridView) getParentView().findViewById(
				R.id.fob_page_event_detail_grid);
		mFriendsListView.setAdapter(getAdapter());

		TextView accept = (TextView) getParentView().findViewById(
				R.id.fob_page_even_detail_finish);
		accept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePage();
			}
		});

		if (isHistory) {
			mFriendsListView.setVisibility(View.GONE);
			getParentView().findViewById(R.id.fob_page_even_detail_line)
					.setVisibility(View.GONE);
			accept.setVisibility(View.GONE);
		}
	}

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
				FOBPageEventDetails.this);
		return adapter;
	}

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = "活动详细";
		updateTitleBarInfo(titleBarInfo);

	}
}
