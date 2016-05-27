package com.fob.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.balls.R;
import com.fob.page.FOBPageDialogue;
import com.fob.page.FOBPageEventDetails;
import com.fob.struct.FOBStructEvent;

public class FOBAdapterMessageNewEvent extends FOBAdapterItemBase {
	private Context context;

	private FOBPageDialogue mPage;
	private FOBStructEvent mMode;

	public FOBAdapterMessageNewEvent(Context context, FOBStructEvent mode,
			FOBPageDialogue page) {
		this.context = context;
		this.mPage = page;
		this.mMode = mode;
	}

	@Override
	public View getView(View convertView, boolean isScrolling) {
		convertView = View.inflate(context,
				R.layout.fob_adapter_message_new_evnet_item, null);
		final TextView agree = (TextView) convertView
				.findViewById(R.id.fob_adapter_message_new_event_agree);
		agree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				agree.setText("已接受");
				agree.setTextColor(Color.GRAY);
			}
		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPage.goNextPage(new FOBPageEventDetails(mMode));
			}
		});
		return convertView;
	}

	@Override
	public String getString() {
		return null;
	}

	@Override
	public FOBStructEvent getModel() {
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
