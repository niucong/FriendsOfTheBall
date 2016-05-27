package com.fob.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.adapter.base.FOBAdapterModelBase;
import com.fob.balls.R;
import com.fob.page.FOBPageMessage;
import com.fob.page.FOBPageNewsDetails;
import com.fob.struct.FOBStructLeave;
import com.fob.struct.FOBStructNews;

public class FOBAdapterMessageNewsItem extends FOBAdapterItemBase {
	private Context context;

	private FOBPageMessage mPage;
	private FOBStructNews mMode;

	public FOBAdapterMessageNewsItem(Context context, FOBStructNews mode,
			FOBPageMessage mPage) {
		this.context = context;
		this.mPage = mPage;
		this.mMode = mode;
	}

	@Override
	public View getView(View convertView, boolean isScrolling) {
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.fob_adapter_message_news_item, null);
		}
		TextView address = (TextView) convertView
				.findViewById(R.id.fob_news_address_text);
		address.setText(mMode.getAddress());
		TextView title = (TextView) convertView
				.findViewById(R.id.fob_news_title_text);
		title.setText(mMode.getTitle());
		TextView content = (TextView) convertView
				.findViewById(R.id.fob_news_content_text);
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
