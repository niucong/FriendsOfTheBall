package com.fob.adapter.base;

import android.view.View;
import android.widget.AdapterView;

/**
 * @author talaya
 * 
 */
public abstract class FOBAdapterItemBase {
	private int mOrderId;

	// private FOBAdapter mAdapter;
	public abstract View getView(View mView, boolean isScrolling);

	public abstract String getString();

	public abstract FOBAdapterModelBase getModel();

	public abstract void onItemClick(AdapterView<?> parent, View view,
			int position, long id);

	public FOBAdapterItemBase() {
	}

	/**
	 * @return
	 */
	public abstract String getId();

	// public FOBAdapter getAdapter() {
	// return mAdapter;
	// }
	// public void setAdapter(FOBAdapter listAdapter) {
	// this.mAdapter = listAdapter;
	// }

	public int getItemViewType() {
		return 0;
	}

	public int getOrderId() {
		return mOrderId;
	}

	public void setOrderId(int mOrderId) {
		this.mOrderId = mOrderId;
	}

}
