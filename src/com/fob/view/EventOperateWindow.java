package com.fob.view;

import com.fob.balls.R;
import com.fob.page.FOBPageDialogue;
import com.fob.page.FOBPageEventDetails;
import com.fob.page.base.FOBPageBase;
import com.fob.struct.FOBStructEvent;
import com.fob.tools.StateTouchListener;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

public class EventOperateWindow extends PopupWindow {

	private EventOperateWindow(Context context, View v, int width, int height, final FOBPageBase pageBase) {
		super(v, width, height, true);

		setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		View chat =v.findViewById(R.id.fob_page_friend_operate_chat);
		View invite =v.findViewById(R.id.fob_page_friend_operate_invite);
		invite.setVisibility(View.GONE);
		View delete =v.findViewById(R.id.fob_page_friend_operate_delete);
		delete.setVisibility(View.GONE);
		View add =v.findViewById(R.id.fob_page_friend_operate_add);	
		add.setVisibility(View.GONE);
		View edit =v.findViewById(R.id.fob_page_friend_operate_edit);
		edit.setVisibility(View.GONE);
		View details =v.findViewById(R.id.fob_page_friend_operate_details);
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
			}
		});
		delete.setOnTouchListener(new StateTouchListener());
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
				pageBase.goNextPage(new FOBPageEventDetails(new FOBStructEvent()));
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

	public final static EventOperateWindow createDialog(Context context, int width, int height, FOBPageBase pageBase) {
		View v = LayoutInflater.from(context).inflate(R.layout.fob_popu_friend_operate, null);
		EventOperateWindow d = new EventOperateWindow(context, v, width, height, pageBase);
		return d;
	}

}
