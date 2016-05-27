package com.fob.page;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fob.balls.FriendsAddActivity;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructFriend;

public class FOBPageUserDetails extends FOBPageBase {
	FOBStructFriend mMode;

	private boolean isSearch;

	public FOBPageUserDetails(FOBStructFriend mode) {
		super(true);
		this.mMode = mode;
	}

	public FOBPageUserDetails(FOBStructFriend mode, boolean isSearch) {
		super(true);
		this.mMode = mode;
		this.isSearch = isSearch;
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.fob_page_user_detail,
				root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		TextView finish = (TextView) getParentView().findViewById(
				R.id.fob_page_user_detail_add);
		if (isSearch) {
			finish.setVisibility(View.VISIBLE);
		} else {
			finish.setVisibility(View.GONE);
		}
		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoginActivity.getActivity().startActivity(
						new Intent(LoginActivity.getActivity(),
								FriendsAddActivity.class));
			}
		});
		setTitleBar();
	}

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = "球友详细信息";
		updateTitleBarInfo(titleBarInfo);
	}
}
