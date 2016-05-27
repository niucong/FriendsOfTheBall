package com.fob.page;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.fob.balls.LoginActivity;
import com.fob.balls.MapActivity;
import com.fob.balls.R;
import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.CourtDetail;
import com.fob.balls.net.bean.CourtDetailBean;
import com.fob.balls.net.bean.GeneralImages;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructPlace;
import com.fob.util.JsonParseTool;

public class FOBPagePlaceDetails extends FOBPageBase {
	// private final String TAG = "FOBPagePlaceDetails";

	private FOBStructPlace mMode;
	private boolean isCommon, isConfrim;

	private View mRootView;
	private ImageView imageViewl;

	private boolean refreash;
	private boolean showBig;

	public FOBPagePlaceDetails(FOBStructPlace mode, boolean isCommon,
			boolean isConfrim) {
		super(true);
		this.mMode = mode;
		this.isCommon = isCommon;
		this.isConfrim = isConfrim;
		new CourtDetailTask(mRootView).execute();
	}

	public FOBPagePlaceDetails(FOBStructPlace mode, boolean isCommon) {
		super(true);
		this.mMode = mode;
		this.isCommon = isCommon;
		new CourtDetailTask(mRootView).execute();
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		mRootView = View.inflate(mActivity, R.layout.fob_page_place_detail,
				root);
		return mRootView;
	}

	private TextView finish;

	@Override
	protected void onFirstDisplayed() {
		finish = (TextView) getParentView().findViewById(
				R.id.fob_page_place_detail_finish);
		if (!isCommon)
			finish.setText("取消常用");
		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AddMyTask().execute();
			}
		});

	}

	@Override
	public void closePage() {
		if (showBig) {
			imageViewl.setVisibility(View.GONE);
			showBig = false;
		} else {
			super.closePage();
			if (refreash && FOBPagePlace.fobPagePlace != null) {
				FOBPagePlace.fobPagePlace.refreash(3);
			}
		}
	}

	private void setTitleBar(String title) {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();
		titleBarInfo.barTitle = title;
		updateTitleBarInfo(titleBarInfo);
	}

	private class AddMyTask extends AsyncTask<String, Integer, String> {

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			LoginActivity.getActivity().spdDialog
					.showProgressDialog("正在处理中...");
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				String sport = App.preferences.getStringMessage("user",
						"sport", "网球");
				if (isCommon) {
					result = APIRequestServers.addMyCourt(mMode.getId(),
							App.preferences.getStringMessage("user", "sport",
									sport));
				} else {
					result = APIRequestServers.removeMyCourt(mMode.getId(),
							App.preferences.getStringMessage("user", "sport",
									sport));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		@Override
		protected void onPostExecute(String result) {
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
			if (result != null) {
				try {
					ResultBasicBean mc = (ResultBasicBean) JsonParseTool
							.dealSingleResult(result, ResultBasicBean.class);
					if ("1".equals(mc.getRet())) {
						if (isCommon) {
							ShowToast.getToast(App.app).show("添加成功");
							isCommon = true;
							finish.setText("取消常用");
						} else {
							ShowToast.getToast(App.app).show("取消成功");
							isCommon = false;
							finish.setText("添加到常用");
						}
						refreash = true;
					} else if ("0".equals(mc.getRet())) {
						ShowToast.getToast(LoginActivity.getActivity()).show(
								mc.getErr());
					} else {
						ShowToast.getToast(App.app).show("操作失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(App.app).show("操作失败");
				}
			} else {
				ShowToast.getToast(App.app).show("操作失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
		}
	}

	private class CourtDetailTask extends AsyncTask<String, Integer, String> {

		View view;

		public CourtDetailTask(View view) {
			this.view = view;
		}

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			LoginActivity.getActivity().spdDialog
					.showProgressDialog("正在加载中...");
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				if (isConfrim) {
					result = APIRequestServers.getCourtDetail(mMode
							.getSportcusername());
				} else {
					result = APIRequestServers.getCourtDetail(mMode.getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		@Override
		protected void onPostExecute(String result) {
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
			if (result != null) {
				try {
					CourtDetailBean cdb = (CourtDetailBean) JsonParseTool
							.dealComplexResult(result, CourtDetailBean.class);
					if ("1".equals(cdb.getRet())) {
						final CourtDetail cd = cdb.getCourtDetail();

						// long ut = App.preferences.getLongMessage(
						// "msglastUpdate", cd.getCourtid(), 0);

						ImageView imageView = (ImageView) view
								.findViewById(R.id.fob_page_place_detail_image);
						imageViewl = (ImageView) view
								.findViewById(R.id.fob_page_place_detail_image_l);
						try {
							final GeneralImages gis = cd.getFaceimgs_all().get(
									0);
							String imageUrl = gis.getMiddle().getUrl();
							FinalBitmap.create(LoginActivity.getActivity())
									.display(imageView, imageUrl);

							imageView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									imageViewl.setVisibility(View.VISIBLE);
									showBig = true;

									imageViewl
											.setOnTouchListener(new MulitPointTouchListener(
													imageViewl));
									imageViewl
											.setScaleType(ScaleType.FIT_CENTER);

									String lUrl = gis.getLarge().getUrl();
									FinalBitmap.create(
											LoginActivity.getActivity())
											.display(imageViewl, lUrl);
								}
							});
							// imageViewl
							// .setOnClickListener(new OnClickListener() {
							//
							// @Override
							// public void onClick(View v) {
							// imageViewl.setVisibility(View.GONE);
							// showBig = false;
							// }
							// });
						} catch (Exception e) {
							e.printStackTrace();
						}

						setTitleBar(cd.getCourtname());

						TextView tv_time = (TextView) view
								.findViewById(R.id.fob_page_place_detail_time);
						tv_time.setText("营业时间: " + cd.getOpentime()
								+ "  黄金时间: " + cd.getGoldtime());
						TextView tv_time2 = (TextView) view
								.findViewById(R.id.fob_page_place_detail_time2);
						String timeremark = cd.getTimeremark();
						if (timeremark != null && !"".equals(timeremark)) {
							tv_time2.setVisibility(View.VISIBLE);
							tv_time2.setText(timeremark);
						} else {
							tv_time2.setVisibility(View.GONE);
						}

						TextView tv_phone = (TextView) view
								.findViewById(R.id.fob_page_place_detail_phone_text);
						tv_phone.setText(cd.getTel());

						TextView tv_address = (TextView) view
								.findViewById(R.id.fob_page_place_detail_address_text);
						tv_address.setText(cd.getAddress());
						view.findViewById(R.id.fob_page_place_detail_address)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent i = new Intent(LoginActivity
												.getActivity(),
												MapActivity.class);
										// i.putExtra("address", address);
										i.putExtra("lat", cd.getLat());
										i.putExtra("lon", cd.getLon());
										LoginActivity.getActivity()
												.startActivity(i);
									}
								});

						TextView tv_sport = (TextView) view
								.findViewById(R.id.fob_page_place_detail_sport);
						TextView tv_price = (TextView) view
								.findViewById(R.id.fob_page_place_detail_price);
						final TextView tv_sport_show = (TextView) view
								.findViewById(R.id.fob_page_place_detail_sport_show);
						final TextView tv_price_show = (TextView) view
								.findViewById(R.id.fob_page_place_detail_price_show);
						String sport = cd.getCourtinfos();
						if (sport != null && sport.length() > 4)
							sport = sport.substring(1, sport.length() - 1)
									.replace(",", "\n\n").replaceAll("\"", "");
						tv_sport_show.setText(sport);
						String price = cd.getProductprices();
						if (price != null && price.length() > 4)
							price = price.substring(1, price.length() - 1)
									.replace(",", "\n\n").replaceAll("\"", "");
						tv_price_show.setText(price);
						tv_sport.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								tv_sport_show.setVisibility(View.VISIBLE);
								tv_price_show.setVisibility(View.GONE);
							}
						});
						tv_price.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								tv_sport_show.setVisibility(View.GONE);
								tv_price_show.setVisibility(View.VISIBLE);
							}
						});
					} else if ("0".equals(cdb.getRet())) {
						ShowToast.getToast(LoginActivity.getActivity()).show(
								cdb.getErr());
					} else {
						ShowToast.getToast(App.app).show("加载失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ShowToast.getToast(App.app).show("加载失败");
				}
			} else {
				ShowToast.getToast(App.app).show("加载失败");
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
		}
	}

	public class MulitPointTouchListener implements OnTouchListener {

		Matrix matrix = new Matrix();
		Matrix savedMatrix = new Matrix();

		public ImageView image;
		static final int NONE = 0;
		static final int DRAG = 1;
		static final int ZOOM = 2;
		int mode = NONE;

		PointF start = new PointF();
		PointF mid = new PointF();
		float oldDist = 1f;

		public MulitPointTouchListener(ImageView image) {
			super();
			this.image = image;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			this.image.setScaleType(ScaleType.MATRIX);
			ImageView view = (ImageView) v;
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				matrix.set(view.getImageMatrix());
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);
				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
			}

			view.setImageMatrix(matrix);
			return true;
		}

		@SuppressLint("FloatMath")
		private float spacing(MotionEvent event) {
			double x = event.getX(0) - event.getX(1);
			double y = event.getY(0) - event.getY(1);
			return ((float) Math.sqrt(x * x + y * y));
		}

		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}
	}

}
