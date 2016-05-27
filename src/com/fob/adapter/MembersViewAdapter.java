package com.fob.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fob.balls.R;
import com.fob.page.FOBPageMembersAdd;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructFriend;

public class MembersViewAdapter extends BaseAdapter {
	@SuppressWarnings("unused")
	private static final String TAG = "MembersViewAdapter";

	private ViewHolder mHolder;
	private Context context;
	private ArrayList<FOBStructFriend> chatList;
	private boolean inDelete;

	private FOBPageBase pageBase;

	public MembersViewAdapter(Context context,
			ArrayList<FOBStructFriend> chatList, FOBPageBase page) {
		this.context = context;
		this.chatList = chatList;
		this.pageBase = page;
	}

	@Override
	public int getCount() {
		return chatList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.member_gv_item, null);
			mHolder = new ViewHolder();
			mHolder.iv = (ImageView) convertView.findViewById(R.id.gv_head);
			mHolder.iv_delete = (ImageView) convertView
					.findViewById(R.id.gv_delete);
			mHolder.tv = (TextView) convertView.findViewById(R.id.gv_name);

			// mHolder.tv.setLayoutParams(new RelativeLayout.LayoutParams(
			// itemWidth, 30));
			mHolder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					chatList.remove(position);
					inDelete = false;
					notifyDataSetChanged();
				}
			});
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		final FOBStructFriend memberSimpleBean = chatList.get(position);
		mHolder.tv.setText(memberSimpleBean.getNickname());
		if ((chatList.size() - 2) == position) {
			mHolder.iv.setImageResource(R.drawable.create_new);
			mHolder.iv_delete.setVisibility(View.GONE);
			if (inDelete)
				convertView.setVisibility(View.GONE);
			else
				convertView.setVisibility(View.VISIBLE);
		} else if ((chatList.size() - 1) == position) {
			mHolder.iv.setImageResource(R.drawable.delete_mmeber);
			mHolder.iv_delete.setVisibility(View.GONE);
			if (inDelete) {
				convertView.setVisibility(View.GONE);
			} else {
				convertView.setVisibility(View.VISIBLE);
			}
		} else {
			if ("女".equals(memberSimpleBean.getGender())) {
				mHolder.iv
						.setImageResource(R.drawable.fob_adapter_friends_header_default_lady);
			} else {
				mHolder.iv
						.setImageResource(R.drawable.fob_adapter_friends_header_default_man);
			}
			convertView.setVisibility(View.VISIBLE);
			if (inDelete)
				mHolder.iv_delete.setVisibility(View.VISIBLE);
			else
				mHolder.iv_delete.setVisibility(View.GONE);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((chatList.size() - 1) == position) {
					inDelete = true;
					notifyDataSetChanged();
				} else if ((chatList.size() - 2) == position) {
					pageBase.goNextPage(new FOBPageMembersAdd());
				} else {
					if (!inDelete) {

					}
				}
			}
		});

		return convertView;
	}

	public void cancelDelete() {
		inDelete = false;
		notifyDataSetChanged();
	}

	public boolean inDelete() {
		return inDelete;
	}

	class ViewHolder {
		ImageView iv;
		TextView tv;
		ImageView iv_delete;
	}

}
