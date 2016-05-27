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
import com.fob.balls.R;
import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.page.FOBPagePlace;
import com.fob.page.FOBPagePlaceDetails;
import com.fob.page.FOBPagePlaceInventory;
import com.fob.page.FOBPagePlaceOwner;
import com.fob.struct.FOBStructPlace;

public class FOBAdapterPlaceAllItem extends FOBAdapterItemBase {
	private static final String TAG = "FOBAdapterPlaceAllItem";

	private Context context;

	private FOBPagePlace mPage;
	private FOBStructPlace mMode;

	public FOBAdapterPlaceAllItem(Context context, FOBStructPlace mode,
			FOBPagePlace page) {
		this.context = context;
		this.mPage = page;
		this.mMode = mode;
	}

	@SuppressLint("CutPasteId")
	@Override
	public View getView(View convertView, boolean isScrolling) {
		if (convertView == null)
			convertView = View.inflate(context,
					R.layout.fob_adapter_place_confrim_item, null);

		TextView tv = (TextView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_status);
		tv.setVisibility(View.INVISIBLE);
		TextView tv_name = (TextView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_name_text);

		TextView tv_details = (TextView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_details);
		TextView tv_deal = (TextView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_deal);

		ImageView iv = (ImageView) convertView
				.findViewById(R.id.fob_adapter_place_confrim_icon);
		if (mMode == null) {
			tv_name.setText("自主订场");
			convertView.findViewById(R.id.fob_adapter_place_confrim_details)
					.setVisibility(View.GONE);
		} else {
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

			tv_name.setText(mMode.getName());
			tv_deal.setText(mMode.getStatus());
			tv_details.setText(mMode.getOperatestatus());

			convertView.findViewById(R.id.fob_adapter_place_confrim_icon)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mPage.goNextPage(new FOBPagePlaceDetails(mMode,
									true));
						}
					});
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMode == null) {
					mPage.goNextPage(new FOBPagePlaceOwner("自主订场", 4, mMode,
							"", 0, "", 0, 0));
				} else {
					if ("歇业".equals(mMode.getStatus())) {
						ShowToast.getToast(App.app).show("场馆已歇业");
					} else {
						mPage.goNextPage(new FOBPagePlaceInventory(mMode));
					}
				}
			}
		});
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
