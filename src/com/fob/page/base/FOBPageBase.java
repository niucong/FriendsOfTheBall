package com.fob.page.base;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.manager.FOBPageManager;

public abstract class FOBPageBase {
	protected abstract View createContentView(ViewGroup root);

	protected abstract void onFirstDisplayed();

	private ViewGroup mParentView;
	private boolean isSubPage;
	private View mPageTitleBarView;
	private PageTitleBarInfo mPageTitleBarInfo;

	private boolean isShowed;
	private boolean isClosed;
	private boolean isLoadView;
	private FOBPageBase mPreviousPage;
	private FrameLayout mLayout;

	// public static ArrayList<FOBPageBase> fpbList = new
	// ArrayList<FOBPageBase>();

	public FOBPageBase(boolean isSubPage) {
		FOBPageManager.registerAlivePage(this);
		this.isSubPage = isSubPage;
		loadView();
		if (isSubPage) {
			setDefaultTitleBarInfo();
		}
		init();
	}

	protected void init() {
	}

	public void onDisplayed() {
	}

	public void onDisplayeStateChanged(boolean isDisplay) {
	}

	public void goNextPage(FOBPageBase page) {
		try {
			View v = page.getParentView();
			ViewGroup vg = (ViewGroup) getParentView().getParent();
			if (vg.indexOfChild(v) != -1) {
				vg.removeView(page.getParentView());
			}
			page.setPreviousPage(this);
			page.pageTo(((ViewGroup) getParentView().getParent()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void waitLayoutFinish() {
		onFirstDisplayed();
		onDisplayeStateChanged(true);
	}

	public void pageTo(ViewGroup viewGroup) {

		ViewGroup viewgroup = getParentView();
		viewGroup.addView(viewgroup);

		this.onDisplayed();

		if (!isShowed) {
			isShowed = true;
			waitLayoutFinish();
		} else {
			onDisplayeStateChanged(true);
		}

	}

	public boolean isClosed() {
		return isClosed;
	}

	public void closePage() {
		if (isClosed) {
			return;
		}
		FOBPageManager.unregisterAlivePage(this);
		removeFromParentView();
		onPageClosed();
		if (mPreviousPage != null && !mPreviousPage.isClosed()) {
			mPreviousPage.subPageClosed();
			mPreviousPage.onDisplayed();
		}
		this.destroy();
		this.isClosed = true;
	}

	protected void onPageClosed() {

	}

	protected void destroy() {
		this.mLayout = null;
		this.mPageTitleBarInfo = null;
		this.mPageTitleBarView = null;
		this.mParentView = null;
		this.mPreviousPage = null;
	}

	protected void subPageClosed() {
		// subPage = null;
		// this.onDisplayed();
	}

	public void removeFromParentView() {
		View view = getParentView();
		((ViewGroup) view.getParent()).removeView(view);
		onDisplayeStateChanged(false);
	}

	private void setPreviousPage(FOBPageBase page) {
		this.mPreviousPage = page;
	}

	protected View createContentView(int layoutId, ViewGroup root) {
		return LoginActivity.getActivity().getLayoutInflater()
				.inflate(layoutId, root);
	}

	public ViewGroup getParentView() {
		// if (this.isClosed()) {
		// return null;
		// }
		if (mParentView == null) {
			loadView();
			setDefaultTitleBarInfo();
		}
		return mParentView;
	}

	private void loadView() {
		Activity activity = LoginActivity.getActivity();
		mLayout = new FrameLayout(activity);
		mLayout.setClickable(true);
		mLayout.setBackgroundColor(Color.WHITE);
		mLayout.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT));
		if (isSubPage) {
			LinearLayout linearLayout = new LinearLayout(activity);
			linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			mPageTitleBarView = generateTitleBarView();
			linearLayout.addView(mPageTitleBarView);
			createContentView(linearLayout);
			mLayout.addView(linearLayout);
		} else {
			createContentView(mLayout);
		}

		mLayout.invalidate();
		mParentView = mLayout;
		isLoadView = true;
	}

	private View generateTitleBarView() {
		Activity activity = LoginActivity.getActivity();
		int layoutId = R.layout.fob_base_page_titlebar;
		return activity.getLayoutInflater().inflate(layoutId, null);
	}

	private void setDefaultTitleBarInfo() {
		if (isSubPage) {
			PageTitleBarInfo info = new PageTitleBarInfo();
			info.leftImageResId = R.drawable.fob_back;
			// info.leftImageBgId = -1;
			info.leftButtonClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					FOBPageBase.this.closePage();
					// TODO
				}
			};
			updateTitleBarInfo(info);
		}
	}

	public PageTitleBarInfo getPageTitleBarInfo() {
		return mPageTitleBarInfo;
	}

	public void updateTitleBarInfo(PageTitleBarInfo titleBarInfo) {
		if (!isSubPage) {
			return;
		}
		if (titleBarInfo == null) {
			return;
		}
		if (!isLoadView) {
			loadView();
		}
		mPageTitleBarInfo = titleBarInfo;
		setTitleBarInfo(titleBarInfo);
	}

	public void setRightButton(int rightResId, int rightBgId,
			View.OnClickListener rightButtonListener) {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.rightImageResId = rightResId;
		titleBarInfo.rightImageBgId = rightBgId;
		titleBarInfo.rightButtonClickListener = rightButtonListener;
	}

	private void setTitleBarInfo(PageTitleBarInfo titleBarInfo) {
		if (mPageTitleBarView != null) {
			ImageView leftButton = (ImageView) mPageTitleBarView
					.findViewById(R.id.fob_base_page_titlebar_left);
			ImageView rightButton = (ImageView) mPageTitleBarView
					.findViewById(R.id.fob_base_page_titlebar_right);
			TextView title = (TextView) mPageTitleBarView
					.findViewById(R.id.fob_base_page_titlebar_title);

			if (titleBarInfo.leftImageResId <= 0
					&& (titleBarInfo.leftButtonTitle == null || titleBarInfo.leftButtonTitle
							.length() == 0)) {
				leftButton.setVisibility(View.INVISIBLE);
			} else {
				leftButton.setVisibility(View.VISIBLE);
				if (titleBarInfo.leftImageResId > 0) {
					leftButton.setImageResource(titleBarInfo.leftImageResId);
				}
				if (titleBarInfo.leftImageBgId > 0) {
					leftButton
							.setBackgroundResource(titleBarInfo.leftImageBgId);
				}
				if (titleBarInfo.leftButtonClickListener != null) {
					leftButton
							.setOnClickListener(titleBarInfo.leftButtonClickListener);
					// leftButton.setOnTouchListener(new StateTouchListener());
				}
			}

			if (titleBarInfo.rightImageResId <= 0
					&& (titleBarInfo.rightButtonTitle == null || titleBarInfo.rightButtonTitle
							.length() == 0)) {
				rightButton.setVisibility(View.INVISIBLE);
			} else {
				rightButton.setVisibility(View.VISIBLE);
				if (titleBarInfo.rightImageResId > 0) {
					rightButton.setImageResource(titleBarInfo.rightImageResId);
				}
				if (titleBarInfo.rightImageBgId > 0) {
					rightButton
							.setBackgroundResource(titleBarInfo.rightImageBgId);
				}
				if (titleBarInfo.rightButtonClickListener != null) {
					rightButton
							.setOnClickListener(titleBarInfo.rightButtonClickListener);
					// rightButton.setOnTouchListener(new StateTouchListener());
				}

			}

			if (titleBarInfo.barTitle != null
					&& titleBarInfo.barTitle.length() > 0) {
				title.setText(titleBarInfo.barTitle);
			}
		}
	}

	public View getPageTitleBarView() {
		return mPageTitleBarView;
	}

	public class PageTitleBarInfo {
		public int leftImageResId;
		public int leftImageBgId;
		public int rightImageResId;
		public int rightImageBgId;
		public String leftButtonTitle;
		public String rightButtonTitle;
		public String barTitle;
		public View.OnClickListener leftButtonClickListener;
		public View.OnClickListener rightButtonClickListener;
		public View.OnClickListener ownLeftClickListener;
		public View.OnClickListener ownRightClickListener;
	}

	// 系统back键事件
	public void getLayoutFacus() {
	}

	public void clearLayoutFacus() {
	}
}
