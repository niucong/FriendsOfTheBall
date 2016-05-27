package com.fob.page;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fob.balls.R;
import com.fob.balls.LoginActivity;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructLeave;

public class FOBPageLeaveDetails extends FOBPageBase {
	FOBStructLeave mMode;

	public FOBPageLeaveDetails(FOBStructLeave mode) {
		super(true);
		this.mMode = mode;
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.fob_page_leave, root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {

		TextView accept = (TextView) getParentView().findViewById(
				R.id.fob_page_leave_accept);
		accept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePage();
			}
		});

		TextView refuse = (TextView) getParentView().findViewById(
				R.id.fob_page_leave_refuse);
		refuse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePage();
			}
		});

		setTitleBar();
	}

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = "留言详情";
		updateTitleBarInfo(titleBarInfo);

	}
}
