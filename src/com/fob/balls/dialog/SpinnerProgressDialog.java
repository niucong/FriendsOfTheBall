package com.fob.balls.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class SpinnerProgressDialog {

	private Context context;
	private ProgressDialog pd;

	public SpinnerProgressDialog(Context context) {
		this.context = context;
	}

	/**
	 * 加载中对话框
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public void showProgressDialog(String msg) {
		try {
			if (pd == null) {
				pd = new ProgressDialog(context);
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			}
			pd.setMessage(msg);
			if (!pd.isShowing()) {
				pd.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setMsg(String msg) {
		pd.setMessage(msg);
	}

	public ProgressDialog getPd() {
		return pd;
	}

	public void setCancel(boolean flag) {
		pd.setCancelable(flag);
	}

	/**
	 * 提示
	 * 
	 * @param msg
	 */
	public void cancelProgressDialog(String msg) {
		try {
			if (pd != null && pd.isShowing()) {
				pd.cancel();
			}
			if (msg != null && !msg.equals("")) {
				ShowToast.getToast(context).show(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
