package com.fob.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fob.balls.R;
import com.fob.page.FOBPageUserDetails;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructFriend;

public class FOBAdapterMembers extends BaseAdapter {
	private Context context;

	private List<FOBStructFriend> list;
	private FOBPageBase mPage;

	public FOBAdapterMembers(Context context, List<FOBStructFriend> list,
			FOBPageBase page) {
		this.context = context;
		this.mPage = page;
		this.list = list;
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

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.fob_adapter_members_item, null);
		}
		final FOBStructFriend mode = list.get(position);
		View flag = convertView
				.findViewById(R.id.fob_adapter_members_header_view_flag);
		if ("1".equals(mode.getFlag())) {
			flag.setVisibility(View.VISIBLE);
		} else {
			flag.setVisibility(View.GONE);
		}
		ImageView head = (ImageView) convertView
				.findViewById(R.id.fob_adapter_members_header_view);
		TextView name = (TextView) convertView
				.findViewById(R.id.fob_adapter_members_inof_name_textview);
		TextView grade = (TextView) convertView
				.findViewById(R.id.fob_adapter_members_inof_other_grade_text);
		TextView addre = (TextView) convertView
				.findViewById(R.id.fob_adapter_members_inof_other_addre);

		name.setText(mode.getNickname());
		grade.setText(mode.getGrade());
		addre.setText(mode.getAddre());
		if ("女".equals(mode.getGender())) {
			head.setImageResource(R.drawable.fob_adapter_friends_header_default_lady);
		} else {
			head.setImageResource(R.drawable.fob_adapter_friends_header_default_man);
		}
		head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPage.goNextPage(new FOBPageUserDetails(mode));
			}
		});
		final CheckBox cb = (CheckBox) convertView
				.findViewById(R.id.fob_adapter_members_checkBox);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cb.setChecked(!cb.isChecked());
			}
		});
		return convertView;
	}
}
