package com.fob.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.adapter.base.FOBAdapterModelBase;
import com.fob.balls.R;
import com.fob.page.FOBPageMessage;
import com.fob.struct.FOBStructLeave;

public class FOBAdapterMessageLeaveItem extends FOBAdapterItemBase {
	private Context context;

	private FOBPageMessage mPage;
	private FOBStructLeave mMode;

	public FOBAdapterMessageLeaveItem(Context context, FOBStructLeave mode,
			FOBPageMessage page) {
		this.context = context;
		this.mPage = page;
		this.mMode = mode;
	}

	@Override
	public View getView(View convertView, boolean isScrolling) {
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.fob_adapter_message_leave_item, null);
		}

		final ImageView iv_new = (ImageView) convertView
				.findViewById(R.id.fob_adapter_message_leave_header_new);
		if (mMode != null && mMode.isUpdate()) {
			iv_new.setVisibility(View.VISIBLE);
		} else {
			iv_new.setVisibility(View.GONE);
		}

		TextView name = (TextView) convertView
				.findViewById(R.id.fob_adapter_message_leave_name);
		name.setText(mMode.getName());
		TextView address = (TextView) convertView
				.findViewById(R.id.fob_adapter_message_leave_time);
		address.setText(mMode.getTime());
		TextView content = (TextView) convertView
				.findViewById(R.id.fob_adapter_message_leave_content_text);
		content.setText(mMode.getContent());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// mPage.goNextPage(new FOBPageNewsDetails(mMode));
			}
		});

		return convertView;
	}

	@Override
	public String getString() {
		return null;
	}

	@Override
	public FOBAdapterModelBase getModel() {
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
