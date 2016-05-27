package com.fob.page;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fob.balls.R;
import com.fob.balls.LoginActivity;
import com.fob.page.base.FOBPageBase;

public class FOBPageAnnouncement extends FOBPageBase {
	public FOBPageAnnouncement() {
		super(false);
	}

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private final String KEY = "DlqbQ3eEPvvkpEBhmm8ctRhQ";
	TextView latitudeView;
	TextView longitudeView;
	TextView addrView;

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			latitudeView.setText("���ȣ�" + location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			longitudeView.setText("γ�ȣ�" + location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				addrView.setText("λ�ã�" + location.getAddrStr());
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			latitudeView.setText("���ȣ�" + poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			longitudeView.setText("γ�ȣ�" + poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			// if(poiLocation.hasPoi()){
			// sb.append("\nPoi:");
			// sb.append(poiLocation.getPoi());
			// }else{
			// sb.append("noPoi information");
			// }
			System.out.println("=================" + sb.toString());
		}
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mContentView = View.inflate(mActivity,
				R.layout.fob_page_announcement, root);
		return mContentView;
	}

	@Override
	protected void onFirstDisplayed() {
		Activity mActivity = LoginActivity.getActivity();
		latitudeView = (TextView) getParentView().findViewById(
				R.id.fob_page_announcement_latitude);
		longitudeView = (TextView) getParentView().findViewById(
				R.id.fob_page_announcement_longitude);
		addrView = (TextView) getParentView().findViewById(
				R.id.fob_page_announcement_addr);

		mLocationClient = new LocationClient(mActivity); // ����LocationClient��
		// mLocationClient.setAK( KEY );
		// mLocationClient.registerLocationListener(myListener ); //ע�������
		// LocationClientOption option = new LocationClientOption();
		// option.setOpenGps(true);
		// option.setAddrType("all");//���صĶ�λ�����ַ��Ϣ
		// option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		// option.setScanSpan(5000);//���÷���λ����ļ��ʱ��Ϊ5000ms
		// option.disableCache(true);//��ֹ���û��涨λ
		// option.setPoiNumber(5); //��෵��POI����
		// option.setPoiDistance(1000); //poi��ѯ����
		// option.setPoiExtraInfo(true); //�Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ
		// mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		}
	}
}
