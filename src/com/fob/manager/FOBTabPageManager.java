package com.fob.manager;

import java.util.ArrayList;

import android.content.Context;
import android.view.ViewGroup;

import com.fob.page.base.FOBPageBase;
import com.fob.page.tab.FOBTabPage;
import com.fob.util.L;

public class FOBTabPageManager {
	private static final String TAG = "FOBTabPageManager";

	private ArrayList<TabPageInfo> mTabPages;
	private ViewGroup mTabIndicatorParent;
	private ViewGroup mContentParent;

	private int mCurTabIndex = -1;

	private static FOBTabPageManager sInstance;

	private TabPageContentBuilder mTabPageContentBuilder;

	public interface TabPageContentBuilder {
		public FOBPageBase buildContentForTab(int tabIndex);
	}

	private FOBTabPageManager() {

	}

	public void init(Context context, ViewGroup tabIndicatorParent,
			ViewGroup contentParent) {
		this.mTabIndicatorParent = tabIndicatorParent;
		this.mContentParent = contentParent;
	}

	public void setTabPageContentBuilder(TabPageContentBuilder builder) {
		this.mTabPageContentBuilder = builder;
	}

	public static FOBTabPageManager getInstance() {
		if (sInstance == null) {
			sInstance = new FOBTabPageManager();
		}
		return sInstance;
	}

	public void addTabPage(FOBTabPage tabPage) {
		if (getTabPages().size() == 5)
			getTabPages().clear();
		tabPage.setIndex(getTabPages().size());

		TabPageInfo pageInfo = new TabPageInfo();
		pageInfo.isShow = false;
		pageInfo.tabPage = tabPage;

		getTabPages().add(pageInfo);
		mTabIndicatorParent.addView(tabPage.getTabIndicator());
	}

	private ArrayList<TabPageInfo> getTabPages() {
		if (mTabPages == null) {
			mTabPages = new ArrayList<TabPageInfo>();
		}
		return mTabPages;
	}

	public void showTabPage(FOBTabPage tabPage) {
		onTabPageChanged(tabPage);
	}

	private void setCurTabPage(FOBTabPage tabPage) {
		int index = tabPage.getIndex();
		L.d(TAG, "setCurTabPage index=" + index);
		// if (index != mCurTabIndex) {
		if ((tabPage.getContent() == null || tabPage.getContent().isClosed())
				&& this.mTabPageContentBuilder != null) {
			FOBPageBase page = mTabPageContentBuilder.buildContentForTab(index);
			tabPage.setContent(page);
		}

		// if (mCurTabIndex >= 0) {
		// FOBTabPage lastPage = getCurTabPage();
		// }
		FOBPageManager.destroyAlivePagesExcept(tabPage.getContent());

		mCurTabIndex = tabPage.getIndex();
		refreshAllIndicatorState();

		reMoveAllPage();
		getTabPages().get(mCurTabIndex).isShow = true;
		tabPage.getContent().pageTo(mContentParent);
		noticPageChanged(mCurTabIndex);
		// }
	}

	private void reMoveAllPage() {
		mContentParent.removeAllViews();
	}

	public void showTabPage(int index) {
		int len = getTabPages().size();
		L.i(TAG, "setTabPage len=" + len);
		if (index < len) {
			showTabPage(getTabPages().get(index).tabPage);
		}
	}

	public int getCurTabIndex() {
		return mCurTabIndex;
	}

	public FOBTabPage getCurTabPage() {

		int size = getTabPages().size();
		if (mCurTabIndex >= 0 && size > mCurTabIndex) {
			return getTabPages().get(mCurTabIndex).tabPage;
		} else {
			return null;
		}
	}

	public void onTabPageChanged(FOBTabPage tabPage) {
		setCurTabPage(tabPage);
	}

	private void refreshAllIndicatorState() {
		int len = getTabPages().size();
		for (int i = 0; i < len; i++) {
			getTabPages().get(i).tabPage.refreshIndicatorState();
		}
	}

	private void noticPageChanged(int index) {

	}

	class TabPageInfo {
		FOBTabPage tabPage;
		boolean isShow;
	}

	public void refreshTabs() {

		if (mTabPages != null) {
			for (TabPageInfo info : mTabPages) {
				if (info.tabPage != null) {
					info.tabPage.onRefresh();
				}
			}
		}
	}

	public void destroyContentsForAllTabs() {
		ArrayList<TabPageInfo> tabs = this.getTabPages();
		for (TabPageInfo info : tabs) {
			FOBTabPage tabPage = info.tabPage;
			tabPage.destroyContent();
		}
		this.mCurTabIndex = -1;
	}

	public void destroy() {
		this.mCurTabIndex = -1;
		this.mContentParent = null;
		this.mTabPageContentBuilder = null;
		this.mTabIndicatorParent = null;

		if (mTabPages != null) {
			mTabPages.clear();
			this.mTabPages = null;
		}
		this.sInstance = null;

	}
}
