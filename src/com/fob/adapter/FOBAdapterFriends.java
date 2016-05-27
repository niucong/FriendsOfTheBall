package com.fob.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.fob.balls.FriendsAddActivity;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.page.FOBPageDialogue;
import com.fob.page.FOBPageUserDetails;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructFriend;
import com.fob.view.FriendOperateWindow;

public class FOBAdapterFriends extends BaseAdapter {
	private Context context;

	private List<FOBStructFriend> list;
	private FOBPageBase mPage;

	private boolean isSearch;

	public FOBAdapterFriends(Context context, List<FOBStructFriend> list,
			FOBPageBase page) {
		this.context = context;
		this.mPage = page;
		this.list = list;
	}

	public FOBAdapterFriends(Context context, List<FOBStructFriend> list,
			FOBPageBase page, boolean isSearch) {
		this.context = context;
		this.mPage = page;
		this.list = list;
		this.isSearch = isSearch;
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
					R.layout.fob_adapter_friends_item, null);
		}
		final FOBStructFriend mode = list.get(position);
		View flag = convertView
				.findViewById(R.id.fob_adapter_friends_header_view_flag);
		if ("1".equals(mode.getFlag())) {
			flag.setVisibility(View.VISIBLE);
		} else {
			flag.setVisibility(View.GONE);
		}
		ImageView head = (ImageView) convertView
				.findViewById(R.id.fob_adapter_friends_header_view);
		TextView name = (TextView) convertView
				.findViewById(R.id.fob_adapter_friends_inof_name_textview);
		TextView grade = (TextView) convertView
				.findViewById(R.id.fob_adapter_friends_inof_other_grade_text);
		TextView addre = (TextView) convertView
				.findViewById(R.id.fob_adapter_friends_inof_other_addre);

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
				mPage.goNextPage(new FOBPageUserDetails(mode, isSearch));
			}
		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isSearch) {
					mPage.goNextPage(new FOBPageUserDetails(mode, isSearch));
				} else {
					mPage.goNextPage(new FOBPageDialogue(false));
				}
			}
		});
		final ImageView switchView = (ImageView) convertView
				.findViewById(R.id.fob_adapter_friends_operate_switch);
		final FriendOperateWindow dialog = FriendOperateWindow.createDialog(
				context, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
				mPage);
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				switchView
						.setImageResource(R.drawable.fob_friend_operate_switch);
			}
		});
		if (isSearch)
			switchView.setVisibility(View.GONE);
		switchView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isSearch) {
					LoginActivity.getActivity().startActivity(
							new Intent(LoginActivity.getActivity(),
									FriendsAddActivity.class));
				} else {
					dialog.showAsDropDown(v);
					switchView
							.setImageResource(R.drawable.fob_friend_operate_switch_up);
				}
			}
		});
		return convertView;
	}
}
