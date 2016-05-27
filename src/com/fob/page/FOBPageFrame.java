package com.fob.page;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.balls.dialog.App;
import com.fob.manager.FOBTabPageManager;
import com.fob.page.base.FOBPageBase;
import com.fob.page.tab.FOBTabPage;
import com.fob.util.L;

public class FOBPageFrame implements FOBTabPageManager.TabPageContentBuilder {
	private final String TAG = "FOBPageFrame";

	private View mRootView;

	public ArrayList<ImageView> ivs = new ArrayList<ImageView>();
	private FOBPagePlace fpp;
	private FOBPageMessage fpm;

	public View createContentView(int i) {
		// if (mRootView == null) {
		Activity mActivity = LoginActivity.getActivity();
		mRootView = View.inflate(mActivity, R.layout.fob_page_frame, null);
		// }
		onFirstDisplayed(i);
		return mRootView;
	}

	protected void onFirstDisplayed(int i) {
		Activity mActivity = LoginActivity.getActivity();
		ViewGroup tabIndicatorParent = (ViewGroup) mRootView
				.findViewById(R.id.fob_page_frame_tab_ll);
		ViewGroup contentParent = (ViewGroup) mRootView
				.findViewById(R.id.fob_page_frame_content);
		FOBTabPageManager.getInstance().init(mActivity, tabIndicatorParent,
				contentParent);
		FOBTabPageManager.getInstance().setTabPageContentBuilder(this);
		setTabPage(i);
	}

	private void setTabPage(int i) {
		int drawable_message = R.drawable.fob_page_frame_tab_message;
		int drawable_messageS = R.drawable.fob_page_frame_tab_message_s;
		FOBTabPage tabpage_message = createTabPage(drawable_message,
				drawable_messageS, "系统消息");
		FOBTabPageManager.getInstance().addTabPage(tabpage_message);

		int drawable_friends = R.drawable.fob_page_frame_tab_friends;
		int drawable_friendsS = R.drawable.fob_page_frame_tab_friends_s;
		FOBTabPage tabpage_friends = createTabPage(drawable_friends,
				drawable_friendsS, "我的球友");
		FOBTabPageManager.getInstance().addTabPage(tabpage_friends);

		int drawable_place = R.drawable.fob_page_frame_tab_place;
		int drawable_placeS = R.drawable.fob_page_frame_tab_place_s;
		FOBTabPage tabpage_place = createTabPage(drawable_place,
				drawable_placeS, "订  场");
		FOBTabPageManager.getInstance().addTabPage(tabpage_place);

		int drawable_event = R.drawable.fob_page_frame_tab_event;
		int drawable_eventS = R.drawable.fob_page_frame_tab_event_s;
		FOBTabPage tabpage_event = createTabPage(drawable_event,
				drawable_eventS, "我的活动");
		FOBTabPageManager.getInstance().addTabPage(tabpage_event);

		int drawable_setting = R.drawable.fob_page_frame_tab_setting;
		int drawable_settingS = R.drawable.fob_page_frame_tab_setting_s;
		FOBTabPage tabpage_setting = createTabPage(drawable_setting,
				drawable_settingS, "设  置");
		FOBTabPageManager.getInstance().addTabPage(tabpage_setting);

		L.i(TAG, "setTabPage i=" + i);
		FOBTabPageManager.getInstance().showTabPage(i);

		setNews(false, false);
	}

	private FOBTabPage createTabPage(int drawableId, int drawableIdS,
			String name) {
		Activity activity = LoginActivity.getActivity();
		ViewGroup tabIndicator = (ViewGroup) (activity.getLayoutInflater()
				.inflate(R.layout.fob_titlebar_item, null));
		tabIndicator.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		ImageView imageview = (ImageView) tabIndicator
				.findViewById(R.id.fob_titlebar_item_image);
		imageview.setImageResource(drawableId);
		TextView textview = (TextView) tabIndicator
				.findViewById(R.id.fob_titlebar_item_text);
		textview.setText(name);

		ivs.add((ImageView) tabIndicator
				.findViewById(R.id.fob_titlebar_item_new));

		FOBTabPage tabpage;
		tabpage = new FOBTabPage(activity, tabIndicator, drawableId,
				drawableIdS);
		return tabpage;
	}

	public void setNews(boolean csFlag, boolean ucsFlag) {
		if (ivs != null && ivs.size() == 5) {
			int ucs = App.preferences.getIntMessage("user", "ucs", 0);
			int cs = App.preferences.getIntMessage("user", "cs", 0);
			if (ucs > 0 || cs > 0) {
				ivs.get(2).setVisibility(View.VISIBLE);
			} else {
				ivs.get(2).setVisibility(View.INVISIBLE);
			}

			int as = App.preferences.getIntMessage("user", "as", 0);
			if (as > 0) {
				ivs.get(0).setVisibility(View.VISIBLE);
			} else {
				ivs.get(0).setVisibility(View.INVISIBLE);
			}
			L.d(TAG, "setNews ucs=" + ucs + ",cs=" + cs + ",as=" + as);
		}
		if (fpp != null) {
			fpp.setNews(csFlag, ucsFlag);
			L.d(TAG, "setNews FOBPagePlace...");
		}

		if (fpm != null) {
			fpm.setNews();
			L.d(TAG, "setNews FOBPageFrame...");
		}
	}

	@Override
	public FOBPageBase buildContentForTab(int tabIndex) {
		switch (tabIndex) {
		case 0:
			fpp = null;
			fpm = new FOBPageMessage();
			setNews(false, false);
			return fpm;
		case 1:
			return new FOBPageFriends();
		case 2:
			fpm = null;
			fpp = new FOBPagePlace(false);
			setNews(false, false);
			return fpp;
		case 3:
			return new FOBPageEvent();
		case 4:
			return new FOBPageSetting();
		}
		return null;
	}
}
