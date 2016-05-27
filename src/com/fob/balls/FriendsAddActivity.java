package com.fob.balls;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsAddActivity extends Activity implements OnClickListener {

	private ImageView iv_back;
	private TextView tv_send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_add);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.friends_add_back);
		tv_send = (TextView) findViewById(R.id.friends_add_send);
	}

	private void setView() {
		iv_back.setOnClickListener(this);
		tv_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friends_add_back:
			finish();
			break;
		case R.id.friends_add_send:
			Toast.makeText(this, "提交成功，等待验证", Toast.LENGTH_LONG).show();
			finish();
			break;
		default:
			break;
		}
	}
}
