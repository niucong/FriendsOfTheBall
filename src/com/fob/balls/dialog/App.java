package com.fob.balls.dialog;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.fob.util.AppSharedPreferences;

public class App extends Application {
	public static App app;

	public static AppSharedPreferences preferences;

	public String loc;
	public double mlat, mLon;
	private final String TAG = "App";

	public LocationClient mLocationClient = null;
	public Vibrator mVibrator;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		preferences = new AppSharedPreferences(this);

		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			mlat = location.getLatitude();
			mLon = location.getLongitude();
		}

	}

}
