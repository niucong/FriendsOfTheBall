package com.fob.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fob.balls.R;
import com.fob.struct.FOBStructMessage;

public class FOBAdapterMessage extends BaseAdapter {
	private Context context;

	private List<FOBStructMessage> list;

	public FOBAdapterMessage(Context context, List<FOBStructMessage> list) {
		this.context = context;

		this.list = list;
	}

	public int getCount() {
		return this.list != null ? this.list.size() : 0;
	}

	// ���ListViewλ�÷���View
	public Object getItem(int position) {
		return this.list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		FOBStructMessage mode = list.get(position);
		if ("0".equals(mode.getFlag())) {
			convertView = View.inflate(context,
					R.layout.fob_adapter_message_own_item, null);
		} else {
			convertView = View.inflate(context,
					R.layout.fob_adapter_message_friend_item, null);
		}

		TextView content = (TextView) convertView
				.findViewById(R.id.fob_adapter_message_content);
		TextView time = (TextView) convertView
				.findViewById(R.id.fob_adapter_message_time);
		ImageView head = (ImageView) convertView
				.findViewById(R.id.fob_adapter_message_header_view);
		content.setText(mode.getContent());
		time.setText(mode.getTime());
		if ("Ů".equals(mode.getGender())) {
			head.setImageResource(R.drawable.fob_adapter_friends_header_default_lady);
		} else {
			head.setImageResource(R.drawable.fob_adapter_friends_header_default_man);
		}

		return convertView;
	}
}
