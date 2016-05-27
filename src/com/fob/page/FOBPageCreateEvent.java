package com.fob.page;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fob.balls.R;
import com.fob.balls.LoginActivity;
import com.fob.page.base.FOBPageBase;
import com.fob.page.base.FOBPageBase.PageTitleBarInfo;

public class FOBPageCreateEvent extends FOBPageBase{
	public FOBPageCreateEvent() {
		super(true);
	}


	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.fob_page_create_event, root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		TextView finish=(TextView)getParentView().findViewById(R.id.fob_page_create_event_finish);
		finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				goNextPage(new FOBPagePlace(true));	
				closePage();
			}
		});
		setTitleBar();
	}

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();

		titleBarInfo.barTitle = "创建活动";
		updateTitleBarInfo(titleBarInfo);

	}
}
