package com.fob.adapter;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.balls.dialog.App;
import com.fob.balls.net.bean.CourtNumREQ;
import com.fob.balls.net.bean.CourtorderRES;
import com.fob.page.FOBPagePlace;
import com.fob.page.FOBPagePlaceDetails;
import com.fob.page.FOBPagePlaceOwner;
import com.fob.struct.FOBStructPlace;
import com.fob.util.AsynImageLoader;

public class FOBAdapterPlaceConfrimItem extends FOBAdapterItemBase {
	// private final String TAG = "FOBAdapterPlaceConfrimItem";

	private Context context;

	private FOBPagePlace mPage;
	private FOBStructPlace mMode;
	private CourtorderRES bean;

	public FOBAdapterPlaceConfrimItem(Context context, FOBStructPlace mode,
			FOBPagePlace page, CourtorderRES bean) {
		this.context = context;
		this.mPage = page;
		this.mMode = mode;
		this.bean = bean;
	}

	@SuppressLint("CutPasteId")
	@Override
	public View getView(View convertView, boolean isScrolling) {
		if (convertView == null)
			convertView = View.inflate(context,
					R.layout.fob_adapter_place_confrim_item, null);

		final ImageView iv_new = (ImageView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_new);
		TextView tv = (TextView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_status);
		TextView tv_name = (TextView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_name_text);
		TextView tv_details = (TextView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_details);
		TextView tv_deal = (TextView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_deal);
		TextView tv_status = (TextView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_status);
		TextView tv_stus = (TextView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_stus);

		ImageView iv = (ImageView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_icon);

		if (bean != null) {
			if (mMode != null && mMode.isUpdate()) {
				iv_new.setVisibility(View.VISIBLE);
			} else {
				iv_new.setVisibility(View.GONE);
			}
			tv.setVisibility(View.INVISIBLE);

			try {
				String Url = mMode.getHeadimg_all().getThumb().getUrl();
				if (Url != null) {
					FinalBitmap.create(context).display(iv, Url);
				} else {
					iv.setImageResource(R.drawable.fob_adapter_friends_header_default_man);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			tv_name.setText(bean.getCourtname());
			String details = "";

			try {
				String date = bean.getOrderdate();
				details += date.substring(4, 6) + "-" + date.substring(6) + " ";
				CourtNumREQ cn = bean.getCourtnumreqs().get(0);
				details += cn.getStartgap() + ":00-" + cn.getEndgap() + ":00  ";
				details += cn.getMtype() + ":" + cn.getCourtnum() + "片  ";
			} catch (Exception e) {
				e.printStackTrace();
			}

			tv_details.setText(details);
			tv_deal.setText(bean.getStatus());
			tv_stus.setText(bean.getExecutestatus());

			try {
				if (bean.getStatus().contains("成功")) {
					tv_deal.setTextColor(LoginActivity.getActivity()
							.getResources().getColor(R.color.tab_text_green));
				} else {
					tv_deal.setTextColor(LoginActivity.getActivity()
							.getResources().getColor(R.color.tab_text_red));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// if (new Random().nextInt(2) == 1)
			// tv.setVisibility(View.VISIBLE);
			convertView.findViewById(R.id.fob_adapter_place_confrim_icon)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mMode.setSportcusername(bean.getSportcusername());
							mPage.goNextPage(new FOBPagePlaceDetails(mMode,
									true, true));
						}
					});
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					App.preferences.saveBooleanMessage("lastUpdateClick",
							bean.getCourtorderid(), true);
					iv_new.setVisibility(View.GONE);
					mMode.setUpdate(false);
					mPage.goNextPage(new FOBPagePlaceOwner(bean
							.getCourtorderid(), 0, bean, mMode));
				}
			});
		}
		return convertView;
	}

	@Override
	public String getString() {
		return null;
	}

	@Override
	public FOBStructPlace getModel() {
		return mMode;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public String getId() {
		return mMode.getId();
	}
}
