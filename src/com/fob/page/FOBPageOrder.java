package com.fob.page;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fob.balls.R;
import com.fob.balls.LoginActivity;
import com.fob.manager.FOBCreateEventFinishManager;
import com.fob.page.base.FOBPageBase;
import com.fob.page.base.FOBPageBase.PageTitleBarInfo;
import com.fob.struct.FOBStructEvent;
import com.fob.struct.FOBStructOrder;

public class FOBPageOrder extends FOBPageBase{

	FOBStructOrder mOrder;
	public FOBPageOrder(FOBStructOrder order) {
		super(true);
		this.mOrder=order;
	}
	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.fob_page_order, root);
		return mRootView;
	}
	@Override
	protected void onFirstDisplayed() {
		final Activity mActivity = LoginActivity.getActivity();
		final ViewGroup mRootView = getParentView();
		TextView name=(TextView)mRootView.findViewById(R.id.fob_page_order_place_name_text);
		name.setText(mOrder.getName());
		TextView number=(TextView)mRootView.findViewById(R.id.fob_page_order_place_number_text);
		number.setText(mOrder.getNumber());
		TextView time=(TextView)mRootView.findViewById(R.id.fob_page_order_place_time_text);
		time.setText(mOrder.getTime());
		TextView own=(TextView)mRootView.findViewById(R.id.fob_page_order_place_own_text);
		own.setText(mOrder.getOwn());
		TextView phone=(TextView)mRootView.findViewById(R.id.fob_page_order_place_phone_text);
		phone.setText(mOrder.getPhone());
		View flagView=mRootView.findViewById(R.id.fob_page_order_sendmessage);
		flagView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mRootView.findViewById(R.id.fob_page_order_send_message_flag).getVisibility()==View.VISIBLE){
					mRootView.findViewById(R.id.fob_page_order_send_message_flag).setVisibility(View.GONE);
				}else{
					mRootView.findViewById(R.id.fob_page_order_send_message_flag).setVisibility(View.VISIBLE);
				}
				
				
			}
		});
		View finishView=mRootView.findViewById(R.id.fob_page_order_finish);
		finishView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(mActivity, "成功", 1000);
				closePage();					
				FOBCreateEventFinishManager.getInstance().noticeCreateEventFinish(new FOBStructEvent());
			}
		});
		setTitleBar();
	}

	private void setTitleBar() {
		PageTitleBarInfo titleBarInfo = getPageTitleBarInfo();

		titleBarInfo.barTitle = "预定场次";
		updateTitleBarInfo(titleBarInfo);

	}
}
