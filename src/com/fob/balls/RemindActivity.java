package com.fob.balls;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RemindActivity extends FragmentActivity implements OnClickListener {

	private TextView tv_finish;
	private ImageView iv_back;
	private LinearLayout ll_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View.inflate(this, R.layout.remind, null);
		setContentView(mContentView);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.remind_back);
		tv_finish = (TextView) findViewById(R.id.remind_finish);
		ll_time = (LinearLayout) findViewById(R.id.remind_time);
	}

	private void setView() {
		iv_back.setOnClickListener(this);
		tv_finish.setOnClickListener(this);
		ll_time.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.remind_back:
			finish();
			break;
		case R.id.remind_finish:

			break;
		case R.id.remind_time:
			break;
		default:
			break;
		}
	}

}
