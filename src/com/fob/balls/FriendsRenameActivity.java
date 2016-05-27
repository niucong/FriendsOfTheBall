package com.fob.balls;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsRenameActivity extends Activity implements OnClickListener {

	private ImageView iv_back;
	private TextView tv_send;
	private EditText et_old_name, et_new_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_rename);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.friends_rename_back);
		tv_send = (TextView) findViewById(R.id.friends_rename_send);
		et_old_name = (EditText) findViewById(R.id.friends_rename_old);
		et_new_name = (EditText) findViewById(R.id.friends_rename_new);
	}

	private void setView() {
		iv_back.setOnClickListener(this);
		tv_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friends_rename_back:
			finish();
			break;
		case R.id.friends_rename_send:
			finish();
			break;
		default:
			break;
		}
	}
}
