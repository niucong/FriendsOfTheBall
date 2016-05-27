package com.fob.page;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fob.balls.R;
import com.fob.balls.LoginActivity;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructNews;

public class FOBPageNewsDetails extends FOBPageBase{
	private FOBStructNews mMode;
	public FOBPageNewsDetails(FOBStructNews mode) {
		super(true);
		this.mMode=mode;
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.fob_page_news, root);
		return mRootView;
	}

	@Override
	protected void onFirstDisplayed() {
		TextView finish=(TextView)getParentView().findViewById(R.id.fob_page_news_finish);
		finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closePage();			
			}
		});
		setTitleBar();
	}

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = "公告详细";
		updateTitleBarInfo(titleBarInfo);

	}
}
