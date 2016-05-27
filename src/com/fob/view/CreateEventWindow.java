package com.fob.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

import com.fob.balls.R;
import com.fob.manager.FOBTabPageManager;
import com.fob.page.FOBPageMembersAdd;
import com.fob.page.FOBPageMembersView;
import com.fob.page.base.FOBPageBase;

public class CreateEventWindow extends PopupWindow {

	private CreateEventWindow(Context context, View v, int width, int height,
			final FOBPageBase pageBase) {
		super(v, width, height, true);

		View place = v.findViewById(R.id.fob_popu_create_event_place);
		place.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 订场
				FOBTabPageManager.getInstance().showTabPage(2);
				dismiss();
			}
		});

		View check = v.findViewById(R.id.fob_popu_create_event_check);
		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 查看成员
				pageBase.goNextPage(new FOBPageMembersView());
				dismiss();
			}
		});

		View add = v.findViewById(R.id.fob_popu_create_event_add);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 添加成员
				pageBase.goNextPage(new FOBPageMembersAdd());
				dismiss();
			}
		});
	}

	public final static CreateEventWindow createDialog(Context context,
			int width, int height, FOBPageBase pageBase) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.fob_popu_create_event, null);
		CreateEventWindow d = new CreateEventWindow(context, v, width, height,
				pageBase);
		return d;
	}

}
