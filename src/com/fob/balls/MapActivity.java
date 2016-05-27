package com.fob.balls;

import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.fob.balls.dialog.App;
import com.fob.util.L;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class MapActivity extends UMengActivity implements OnClickListener {
	private final String TAG = "MapActivity";

	private WebView wv_map;
	private ImageView iv_back;
	private TextView tv_title;

	private String address;

	private double lat, lon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View mContentView = View.inflate(this, R.layout.map, null);
		setContentView(mContentView);

		InitLocation();
		App.app.mLocationClient.start();

		findView();
		setView();
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("gcj02");// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		// option.setIsNeedAddress(checkGeoLocation.isChecked());
		App.app.mLocationClient.setLocOption(option);
	}

	private void findView() {
		iv_back = (ImageView) findViewById(R.id.map_back);
		wv_map = (WebView) findViewById(R.id.map_wv);
		tv_title = (TextView) findViewById(R.id.map_name);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setView() {
		iv_back.setOnClickListener(this);

		Intent i = getIntent();
		address = i.getStringExtra("address");
		lat = i.getDoubleExtra("lat", 0);
		lon = i.getDoubleExtra("lon", 0);
		L.d(TAG, "setView address=" + address + ",lat=" + lat + ",lon=" + lon);
		if (address != null && !"".equals(address))
			tv_title.setText(address);

		wv_map.getSettings().setJavaScriptEnabled(true);
		wv_map.loadUrl("file:///android_asset/maps.html");

		wv_map.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				view.loadUrl("javascript:updateHtml(" + (App.app.mLon + lon)
						/ 2 + "," + (App.app.mlat + lat) / 2 + ","
						+ App.app.mLon + "," + App.app.mlat + "," + lon + ","
						+ lat + ")");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.map_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		App.app.mLocationClient.stop();
		super.onDestroy();
	}

}
