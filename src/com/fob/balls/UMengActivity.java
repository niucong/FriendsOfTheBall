package com.fob.balls;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.inputmethod.InputMethodManager;

import com.fob.balls.dialog.SpinnerProgressDialog;
import com.umeng.analytics.MobclickAgent;

public class UMengActivity extends FragmentActivity {

	public SpinnerProgressDialog spdDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// MobclickAgent.onError(this);
		spdDialog = new SpinnerProgressDialog(this);
	}

	@Override
	protected void onDestroy() {
		try {
			((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}