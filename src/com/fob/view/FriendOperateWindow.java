package com.fob.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

import com.fob.balls.FriendsRenameActivity;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.page.FOBPageDialogue;
import com.fob.page.FOBPageUserDetails;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructFriend;
import com.fob.tools.StateTouchListener;

public class FriendOperateWindow extends PopupWindow {

	private FriendOperateWindow(Context context, View v, int width, int height,
			final FOBPageBase pageBase) {
		super(v, width, height, true);

		setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		View chat = v.findViewById(R.id.fob_page_friend_operate_chat);
		View invite = v.findViewById(R.id.fob_page_friend_operate_invite);
		View delete = v.findViewById(R.id.fob_page_friend_operate_delete);
		View add = v.findViewById(R.id.fob_page_friend_operate_add);
		View edit = v.findViewById(R.id.fob_page_friend_operate_edit);
		View details = v.findViewById(R.id.fob_page_friend_operate_details);
		View remark = v.findViewById(R.id.fob_page_friend_operate_remark);
		chat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pageBase.goNextPage(new FOBPageDialogue(false));
				dismiss();
			}
		});
		chat.setOnTouchListener(new StateTouchListener());
		invite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		invite.setOnTouchListener(new StateTouchListener());
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				new AlertDialog.Builder(LoginActivity.getActivity())
						.setTitle("提示")
						.setMessage("您要删除好友吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).setNegativeButton("取消", null).show();
			}
		});
		delete.setOnTouchListener(new StateTouchListener());
		remark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoginActivity.getActivity().startActivity(
						new Intent(LoginActivity.getActivity(),
								FriendsRenameActivity.class));
				dismiss();
			}
		});
		remark.setOnTouchListener(new StateTouchListener());
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		add.setOnTouchListener(new StateTouchListener());
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		edit.setOnTouchListener(new StateTouchListener());
		details.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pageBase.goNextPage(new FOBPageUserDetails(
						new FOBStructFriend()));
				dismiss();
			}
		});
		details.setOnTouchListener(new StateTouchListener());

		v.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_MENU:
					case KeyEvent.KEYCODE_BACK:
					case KeyEvent.KEYCODE_SEARCH:
						// activity.onOptionsMenuClosed(menu);
						dismiss();
						return false;
					}
				}
				return false;
			}
		});
	}

	public final static FriendOperateWindow createDialog(Context context,
			int width, int height, FOBPageBase pageBase) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.fob_popu_friend_operate, null);
		FriendOperateWindow d = new FriendOperateWindow(context, v, width,
				height, pageBase);
		return d;
	}

}
