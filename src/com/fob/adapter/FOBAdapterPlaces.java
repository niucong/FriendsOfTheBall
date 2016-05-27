package com.fob.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fob.balls.R;
import com.fob.balls.net.bean.AvBean;
import com.fob.balls.net.bean.AvHourBean;
import com.fob.page.FOBPagePlaceInventory;
import com.fob.page.FOBPagePlaceOwner;
import com.fob.struct.FOBStructPlace;
import com.fob.util.L;

@SuppressLint("SimpleDateFormat")
public class FOBAdapterPlaces extends BaseAdapter {
	private final String TAG = "FOBAdapterPlaces";

	private Context context;

	private List<AvHourBean> list;
	private FOBPagePlaceInventory mPage;
	private FOBStructPlace mMode;
	private String date, date_now;

	private int lastEnd;

	public FOBAdapterPlaces(Context context, List<AvHourBean> list,
			FOBPagePlaceInventory page, FOBStructPlace mode, String date) {
		this.context = context;
		this.mPage = page;
		this.list = list;
		this.mMode = mode;
		this.date = date;
		L.d(TAG, "FOBAdapterPlaces date=" + date);
		date_now = new SimpleDateFormat("yyyyMMdd").format(new Date());

		try {
			lastEnd = Integer.valueOf(list.get(list.size() - 1).getG());
		} catch (Exception e) {
			lastEnd = 22;
		}
	}

	public int getCount() {
		return this.list != null ? this.list.size() : 0;
	}

	public Object getItem(int position) {
		return this.list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.fob_adapter_place_inventory_item, null);
		}
		final AvHourBean mode = list.get(position);
		TextView time = (TextView) convertView
				.findViewById(R.id.fob_adapter_adapter_title_time);
		time.setText(mode.getG() + ":00");

		boolean noBefore = false;
		try {
			noBefore = date_now.equals(date);
			if (noBefore)
				noBefore = noBefore
						&& Integer.valueOf(mode.getG()) < new Date().getHours();
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<AvBean> avs = mode.getList();
		if (avs != null) {
			int size = avs.size();
			LinearLayout ll_in = (LinearLayout) convertView
					.findViewById(R.id.fob_adapter_place_in);
			ll_in.setVisibility(View.GONE);
			LinearLayout ll_out = (LinearLayout) convertView
					.findViewById(R.id.fob_adapter_place_out);
			ll_out.setVisibility(View.GONE);
			LinearLayout ll_vip = (LinearLayout) convertView
					.findViewById(R.id.fob_adapter_place_vip);
			ll_vip.setVisibility(View.GONE);
			if (size > 0) {
				ll_in.setVisibility(View.VISIBLE);
				TextView tv_in_name = (TextView) convertView
						.findViewById(R.id.fob_adapter_place_in_name);
				TextView tv_in = (TextView) convertView
						.findViewById(R.id.fob_adapter_place_in_num);
				final AvBean av_in = avs.get(0);
				final String num_in = av_in.getN();
				final String name_in = av_in.getT();
				tv_in_name.setText(name_in);
				tv_in.setText(num_in);
				try {
					if (Integer.valueOf(num_in) > 0 && !noBefore) {
						ll_in.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mPage.goNextPage(new FOBPagePlaceOwner("预订场次",
										2, mMode, date, Integer.valueOf(mode
												.getG()), name_in, Integer
												.valueOf(num_in), lastEnd));
							}
						});

						tv_in.setTextColor(context.getResources().getColor(
								R.color.tab_text_selected));
					} else {
						tv_in.setTextColor(context.getResources().getColor(
								R.color.tab_text_selected2));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (size > 1) {
					ll_out.setVisibility(View.VISIBLE);
					TextView tv_out_name = (TextView) convertView
							.findViewById(R.id.fob_adapter_place_out_name);
					TextView tv_out = (TextView) convertView
							.findViewById(R.id.fob_adapter_place_out_num);
					final AvBean av_out = avs.get(1);
					final String num_out = av_out.getN();
					final String name_out = av_out.getT();
					tv_out_name.setText(name_out);
					tv_out.setText(num_out);

					try {
						if (Integer.valueOf(num_out) > 0 && !noBefore) {
							ll_out.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									mPage.goNextPage(new FOBPagePlaceOwner(
											"预订场次", 2, mMode, date, Integer
													.valueOf(mode.getG()),
											name_out, Integer.valueOf(num_out),
											lastEnd));
								}
							});

							tv_out.setTextColor(context.getResources()
									.getColor(R.color.tab_text_selected));
						} else {
							tv_out.setTextColor(context.getResources()
									.getColor(R.color.tab_text_selected2));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (size > 2) {
						ll_vip.setVisibility(View.VISIBLE);
						TextView tv_vip_name = (TextView) convertView
								.findViewById(R.id.fob_adapter_place_vip_name);
						TextView tv_vip = (TextView) convertView
								.findViewById(R.id.fob_adapter_place_vip_num);
						final AvBean av_vip = avs.get(2);
						final String num_vip = av_vip.getN();
						final String name_vip = av_vip.getT();
						tv_vip_name.setText(name_vip);
						tv_vip.setText(num_vip);

						try {
							if (Integer.valueOf(num_vip) > 0 && !noBefore) {
								ll_vip.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										mPage.goNextPage(new FOBPagePlaceOwner(
												"预订场次", 2, mMode, date, Integer
														.valueOf(mode.getG()),
												name_vip, Integer
														.valueOf(num_vip),
												lastEnd));
									}
								});

								tv_vip.setTextColor(context.getResources()
										.getColor(R.color.tab_text_selected));
							} else {
								tv_vip.setTextColor(context.getResources()
										.getColor(R.color.tab_text_selected2));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		return convertView;
	}
}
