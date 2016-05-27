package com.fob.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;

import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.balls.R;
import com.fob.page.FOBPageEvent;
import com.fob.page.FOBPageEventDetails;
import com.fob.struct.FOBStructEvent;
import com.fob.view.EventOperateWindow;

public class FOBAdapterEventNowItem extends FOBAdapterItemBase {
	private Context context;

	private FOBPageEvent mPage;
	private FOBStructEvent mMode;

	private boolean isHistory;

	public FOBAdapterEventNowItem(Context context, FOBStructEvent mode,
			FOBPageEvent page, boolean isHistory) {
		this.context = context;
		this.mPage = page;
		this.mMode = mode;

		this.isHistory = isHistory;
	}

	@Override
	public View getView(View convertView, boolean isScrolling) {
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.fob_adapter_evnet_new_item, null);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPage.goNextPage(new FOBPageEventDetails(mMode, isHistory));
			}
		});
		final EventOperateWindow dialog = EventOperateWindow.createDialog(
				context, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
				mPage);
		View operateSwitch = convertView
				.findViewById(R.id.fob_adapter_event_operate_switch);
		operateSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.showAsDropDown(v);
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
