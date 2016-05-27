package com.fob.page.tab;

import com.fob.balls.R;
import com.fob.balls.WelcomePageActivity;
import com.fob.manager.FOBTabPageManager;
import com.fob.page.base.FOBPageBase;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FOBTabPage {

	private FOBPageBase mContent;

	protected ViewGroup mTabIndicator;
	protected int mImageIdN;
	protected int mImageIdS;
	protected int mIndex;

	protected Activity mActivity;

	public FOBTabPage(Activity activity, ViewGroup tabIndicator, int mImageIdN,
			int mImageIdS) {
		this.mImageIdN = mImageIdN;
		this.mImageIdS = mImageIdS;
		this.mActivity = activity;
		this.mTabIndicator = tabIndicator;
		init();
	}

	private void init() {
		setValue();
		setEvent();
	}

	private void setValue() {

	}

	private void setEvent() {
		mTabIndicator.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FOBTabPageManager.getInstance().onTabPageChanged(
						FOBTabPage.this);
			}
		});
	}

	public void refreshIndicatorState() {
		if (FOBTabPageManager.getInstance().getCurTabIndex() == mIndex) {
			mTabIndicator.setBackgroundColor(Color.WHITE);
			TextView text = (TextView) mTabIndicator
					.findViewById(R.id.fob_titlebar_item_text);
			text.setTextColor(Color.parseColor("#208400"));
			ImageView image = (ImageView) mTabIndicator
					.findViewById(R.id.fob_titlebar_item_image);
			image.setImageResource(mImageIdS);
		} else {
			ImageView image = (ImageView) mTabIndicator
					.findViewById(R.id.fob_titlebar_item_image);
			image.setImageResource(mImageIdN);
			TextView text = (TextView) mTabIndicator
					.findViewById(R.id.fob_titlebar_item_text);
			text.setTextColor(Color.GRAY);
			mTabIndicator.setBackgroundDrawable(null);
		}
	}

	public FOBPageBase getContent() {
		return mContent;
	}

	public void setContent(FOBPageBase page) {
		mContent = page;
	}

	public void destroyContent() {
		if (mContent != null) {
			mContent.closePage();
			mContent = null;
		}
	}

	public ViewGroup getTabIndicator() {
		return mTabIndicator;
	}

	public int getIndex() {
		return mIndex;
	}

	public void setIndex(int index) {
		this.mIndex = index;
	}

	public void onRefresh() {

	}

	public int getmImageIdN() {
		return mImageIdN;
	}

	public void setmImageIdN(int mImageIdN) {
		this.mImageIdN = mImageIdN;
	}

	public int getmImageIdS() {
		return mImageIdS;
	}

	public void setmImageIdS(int mImageIdS) {
		this.mImageIdS = mImageIdS;
	}

}
