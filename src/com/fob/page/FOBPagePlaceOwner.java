package com.fob.page;

import java.util.ArrayList;
import java.util.Date;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.fastjson.JSONArray;
import com.fob.adapter.MembersViewAdapter;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.CourtNumREQ;
import com.fob.balls.net.bean.CourtorderRES;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.manager.FOBPageManager;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructFriend;
import com.fob.struct.FOBStructPlace;
import com.fob.util.JsonParseTool;
import com.fob.util.L;

public class FOBPagePlaceOwner extends FOBPageBase {

	private final String TAG = "FOBPagePlaceOwner";

	private String title;

	/**
	 * 类型：0已确认、1待确认、2全部、3常用、4自主订场
	 */
	private int type;
	private int start, end, num, lastEnd;

	private FOBStructPlace mode;
	private String date, mType;

	private TextView tv_num;

	private CourtorderRES bean;

	private boolean refreash;

	/**
	 * 
	 * @param title
	 * @param type
	 * @param mode
	 * @param date
	 * @param start
	 * @param mType
	 * @param num
	 */
	public FOBPagePlaceOwner(String title, int type, FOBStructPlace mode,
			String date, int start, String mType, int num, int lastEnd) {
		super(false);
		this.title = title;
		this.type = type;
		this.mode = mode;
		this.date = date;
		this.start = start;
		this.mType = mType;
		this.num = num;
		this.lastEnd = lastEnd;
	}

	/**
	 * 
	 * @param title
	 * @param type
	 * @param mode
	 * @param date
	 * @param start
	 * @param mType
	 * @param num
	 */
	public FOBPagePlaceOwner(String title, int type, CourtorderRES bean,
			FOBStructPlace mode) {
		super(false);
		this.title = title;
		this.type = type;
		this.bean = bean;
		this.mode = mode;
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.fob_page_place_owner,
				root);
		return mRootView;
	}

	@Override
	protected void onPageClosed() {
		super.onPageClosed();
		if (refreash) {
			FOBPageBase fpb = FOBPageManager.getLastPage();
			if (fpb != null && (fpb instanceof FOBPagePlaceInventory)) {
				fpb.closePage();
			}
			if (FOBPagePlace.fobPagePlace != null) {
				if (type == 2 || type == 3) {
					FOBPagePlace.fobPagePlace.refreash(1);
				} else {
					FOBPagePlace.fobPagePlace.refreash(type);
				}
			}
		}
	}

	@Override
	protected void onFirstDisplayed() {
		ImageView back = (ImageView) getParentView().findViewById(
				R.id.place_owner_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FOBPagePlaceOwner.this.closePage();
			}
		});
		TextView tv_title = (TextView) getParentView().findViewById(
				R.id.place_owner_title);
		tv_title.setText(title);

		TextView finish = (TextView) getParentView().findViewById(
				R.id.place_owner_finish);
		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FOBPagePlaceOwner.this.closePage();
			}
		});

		EditText et_name = (EditText) getParentView().findViewById(
				R.id.place_owner_name);
		TextView tv_date = (TextView) getParentView().findViewById(
				R.id.place_owner_date);

		final TextView tv_start = (TextView) getParentView().findViewById(
				R.id.place_owner_start);
		// start.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// setTime(start);
		// }
		// });
		final TextView tv_end = (TextView) getParentView().findViewById(
				R.id.place_owner_end);
		// end.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// setTime(end);
		// }
		// });

		tv_num = (TextView) getParentView().findViewById(R.id.place_owner_num);

		TextView tv_type = (TextView) getParentView().findViewById(
				R.id.place_owner_type);

		TextView send = (TextView) getParentView().findViewById(
				R.id.place_owner_send);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new BookTask().execute();
			}
		});
		TextView refuse = (TextView) getParentView().findViewById(
				R.id.place_owner_refuse);
		TextView tip = (TextView) getParentView().findViewById(
				R.id.place_owner_tip);
		TextView tip2 = (TextView) getParentView().findViewById(
				R.id.place_owner_tip2);

		GridView gv = (GridView) getParentView().findViewById(
				R.id.place_owner_grid);

		final ImageView iv_new = (ImageView) getParentView().findViewById(
				R.id.place_owner_new);

		if (type == 0 || type == 1) {
			et_name.setText(bean.getCourtname());
			try {
				date = bean.getOrderdate();
				tv_date.setText(date.subSequence(0, 4) + "年"
						+ date.substring(4, 6) + "月" + date.substring(6) + "日");
				CourtNumREQ cn = bean.getCourtnumreqs().get(0);
				tv_start.setText(cn.getStartgap() + ":00");
				tv_end.setText(cn.getEndgap() + ":00");
				tv_num.setText(cn.getCourtnum() + "");
				tv_type.setText(cn.getMtype());

				TextView tv_id = (TextView) getParentView().findViewById(
						R.id.place_owner_id);
				String courtnos = cn.getCourtnos();
				if (courtnos != null && !"".equals(courtnos)) {
					tv_id.setText(courtnos);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			send.setVisibility(View.GONE);
			getParentView().findViewById(R.id.place_owner_grid_line)
					.setVisibility(View.GONE);
			getParentView().findViewById(R.id.place_owner_mambers_line)
					.setVisibility(View.GONE);
			getParentView().findViewById(R.id.place_owner_members)
					.setVisibility(View.GONE);
			gv.setVisibility(View.GONE);

			TextView tv_status = (TextView) getParentView().findViewById(
					R.id.place_owner_status_info);
			tv_status.setText(bean.getStatus());

			if (type == 0) {
				try {
					if (bean.getStatus().contains("成功")) {
						tv_status.setTextColor(LoginActivity.getActivity()
								.getResources()
								.getColor(R.color.tab_text_green));
					} else {
						tv_status.setTextColor(LoginActivity.getActivity()
								.getResources().getColor(R.color.tab_text_red));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				getParentView().findViewById(R.id.place_owner_id0)
						.setVisibility(View.VISIBLE);
				getParentView().findViewById(R.id.place_owner_id1)
						.setVisibility(View.VISIBLE);
				tip.setVisibility(View.VISIBLE);
				refuse.setText("申请退订");
			} else {
				tip.setVisibility(View.VISIBLE);
				tip.setText("管理员未处理时，可直接取消此订单。");
				tv_status.setTextColor(LoginActivity.getActivity()
						.getResources().getColor(R.color.tab_text_blue));
				refuse.setText("取消");
			}

			String lmsg = bean.getLastestmsg();
			TextView tv_msg = (TextView) getParentView().findViewById(
					R.id.place_owner_msg);
			tv_msg.setText(lmsg);
			if (mode != null && mode.isUpdate() && lmsg != null
					&& !"".equals(lmsg)) {
				iv_new.setVisibility(View.VISIBLE);
			} else {
				iv_new.setVisibility(View.GONE);
			}

		} else {
			Drawable drawable = LoginActivity.getActivity().getResources()
					.getDrawable(R.drawable.fob_arrows_gray);
			int dw = drawable.getIntrinsicWidth();
			int dh = drawable.getIntrinsicHeight();
			drawable.setBounds(0, 0, 1 * dw / 2, 1 * dh / 2);
			
			tip2.setVisibility(View.VISIBLE);
			tip2.setText("无支付，价格请点头像查询或咨询管理员。\n首次预订时，建议您和管理员电话确认。");
			et_name.setText(mode.getName());
			tv_date.setText(date.subSequence(0, 4) + "年" + date.substring(4, 6)
					+ "月" + date.substring(6) + "日");
			tv_start.setText(start + ":00");
			if (start < lastEnd) {
				end = start + 2;
			} else {
				end = start + 1;
			}

			if (end > 23)
				end = 0;

			tv_end.setCompoundDrawables(null, null, drawable, null);
			tv_end.setText(end + ":00");
			tv_end.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int t = lastEnd - start + 1;
					final String[] levels = new String[t];
					for (int i = 0; i < t; i++) {
						if ((i + 1) > 23) {
							levels[i] = "0:00";
						} else {
							levels[i] = start + (i + 1) + ":00";
						}
					}
					int j = 0;
					for (int k = 0; k < levels.length; k++) {
						if (levels[k].equals(end + ":00")) {
							j = k;
							break;
						}
					}
					new AlertDialog.Builder(LoginActivity.getActivity())
							.setTitle("时间设置")
							.setSingleChoiceItems(levels, j,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											try {
												end = Integer.valueOf(levels[which]
														.substring(
																0,
																levels[which]
																		.indexOf(":")));
											} catch (Exception e) {
												e.printStackTrace();
											}
											tv_end.setText(levels[which]);
											dialog.cancel();
										}
									}).show();
				}
			});

			tv_num.setCompoundDrawables(null, null, drawable, null);
			tv_num.setText("1");
			tv_num.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String[] nums = new String[num];
					for (int i = 0; i < num; i++) {
						nums[i] = i + 1 + "";
					}
					final String[] levels = nums;
					int j = 0;
					for (int k = 0; k < levels.length; k++) {
						if (levels[k].equals(tv_num.getText().toString())) {
							j = k;
							break;
						}
					}
					new AlertDialog.Builder(LoginActivity.getActivity())
							.setTitle("场地数量")
							.setSingleChoiceItems(levels, j,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											tv_num.setText(levels[which]);
											dialog.cancel();
										}
									}).show();
				}
			});
			tv_type.setText(mType);

			gv.setAdapter(getAdapter());

			getParentView().findViewById(R.id.place_owner_status)
					.setVisibility(View.GONE);
			getParentView().findViewById(R.id.place_owner_status2)
					.setVisibility(View.GONE);
		}

		ImageView iv = (ImageView) getParentView().findViewById(
				R.id.place_owner_head);
		try {
			final String Url = mode.getHeadimg_all().getThumb().getUrl();
			if (Url != null) {
				FinalBitmap.create(LoginActivity.getActivity())
						.display(iv, Url);
			}
			LinearLayout chat = (LinearLayout) getParentView().findViewById(
					R.id.place_owner_chat);
			chat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					iv_new.setVisibility(View.GONE);
					// FOBTabPageManager.getInstance().showTabPage(1);
					String groupid = bean.getGroupid();
					String courtorderid = bean.getCourtorderid();
					L.i(TAG, "onFirstDisplayed groupid=" + groupid
							+ ",courtorderid=" + courtorderid);
					goNextPage(new FOBPageDialogue(true, groupid, courtorderid,
							Url));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		refuse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tip = "";
				if (type == 0) {
					tip = "确定要退订场地吗？";
				} else {
					tip = "确定要取消订单吗？";
				}
				new AlertDialog.Builder(LoginActivity.getActivity())
						.setTitle("提示")
						.setMessage(tip)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										new BookTask().execute();
									}
								}).setNegativeButton("取消", null).show();
			}
		});
	}

	@SuppressWarnings({ "deprecation", "unused" })
	private void setTime(final TextView tv) {
		new TimePickerDialog(LoginActivity.getActivity(),
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						tv.setText(hourOfDay + ":" + minute);
					}
				}, new Date().getHours(), new Date().getMinutes(), true).show();// true表示24小时制，false表示12小时制
	}

	private ListAdapter getAdapter() {
		MembersViewAdapter adapter = null;
		ArrayList<FOBStructFriend> list = new ArrayList<FOBStructFriend>();
		for (int i = 0; i < 10; i++) {
			FOBStructFriend mode = new FOBStructFriend();
			mode.setAddre("北京海淀王子网球馆");
			mode.setFlag(i % 2 + "");
			mode.setGender(i % 2 == 0 ? "男" : "女");
			mode.setGrade("中");
			mode.setHeadurl("");
			mode.setNickname("网球王子哥");
			list.add(mode);
		}
		list.add(new FOBStructFriend());
		list.add(new FOBStructFriend());
		adapter = new MembersViewAdapter(LoginActivity.getActivity(), list,
				FOBPagePlaceOwner.this);
		return adapter;
	}

	private class BookTask extends AsyncTask<String, Integer, String> {

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
				if (type == 0) {
					result = APIRequestServers.confirmCancel(bean
							.getCourtorderid());
				} else if (type == 1) {
					result = APIRequestServers.bookCancel(bean
							.getCourtorderid());
				} else {
					CourtNumREQ cnr = new CourtNumREQ();
					cnr.setStartgap(start);
					cnr.setEndgap(end);
					cnr.setMtype(mType);
					cnr.setCourtnum(Integer
							.valueOf(tv_num.getText().toString()));
					ArrayList<CourtNumREQ> list = new ArrayList<CourtNumREQ>();
					list.add(cnr);
					JSONArray array = new JSONArray();
					array.addAll(list);

					result = APIRequestServers.book(array, mode.getId(), "888",
							date);
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
						if (type == 0) {
							ShowToast.getToast(App.app).show("待管理员批准");
						} else if (type == 1) {
							ShowToast.getToast(App.app).show("取消成功");
						} else {
							ShowToast.getToast(App.app).show("等待管理员确认");
						}
						refreash = true;
						FOBPagePlaceOwner.this.closePage();
						if (FOBPageSearch.fobPageSearch != null)
							FOBPageSearch.fobPageSearch.closePage();
					} else if ("0".equals(mc.getRet())) {
						ShowToast.getToast(App.app).show(mc.getErr());
					} else {
						failTip();
					}
				} catch (Exception e) {
					e.printStackTrace();
					failTip();
				}
			} else {
				failTip();
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			LoginActivity.getActivity().spdDialog.cancelProgressDialog("");
		}
	}

	private void failTip() {
		if (type == 0 || type == 1) {
			ShowToast.getToast(App.app).show("退订失败");
		} else {
			ShowToast.getToast(App.app).show("预订失败");
		}
	}

}
