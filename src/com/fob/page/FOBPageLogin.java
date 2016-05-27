package com.fob.page;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.fob.balls.R;
import com.fob.balls.LoginActivity;
import com.fob.page.base.FOBPageBase;

public class FOBPageLogin extends FOBPageBase{
	public FOBPageLogin() {
		super(true);
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.fob_page_login, root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		
		setTitleBar();
	}

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = "登 录";
		updateTitleBarInfo(titleBarInfo);
	}
	public static boolean checkLogin(FOBPageBase parentPage) {
		parentPage.goNextPage(new FOBPageLogin());
//		if (YpUserManage.getInstance().getCurUser() == null) {
//
//			parentPage.goNextPage(new FOBPageLogin());
//			return false;
//		} else {
//			// YLog.v(TAG, "User Already Logged in.");
			return true;
//		}
	}
}
