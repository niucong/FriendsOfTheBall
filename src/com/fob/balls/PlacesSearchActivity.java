package com.fob.balls;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PlacesSearchActivity extends Activity implements OnClickListener {

	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.places_search);

		findView();
		setView();
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.places_search_back);
	}

	private void setView() {
		iv_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.places_search_back:
			finish();
			break;

		default:
			break;
		}
	}
}
