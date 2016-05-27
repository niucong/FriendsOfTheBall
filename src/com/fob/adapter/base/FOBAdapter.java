package com.fob.adapter.base;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

public class FOBAdapter extends BaseAdapter {
	private int typeNum;
	private AdapterView<?> mListView;
	private ArrayList<FOBAdapterItemBase> mDataList;
	private boolean isScrolling;

	public FOBAdapter(AdapterView<?> listview,
			ArrayList<FOBAdapterItemBase> datalist) {
		this.mListView = listview;
		this.mDataList = datalist;
		init();
	}

	private void init() {
		if (mDataList == null) {
			mDataList = new ArrayList<FOBAdapterItemBase>();
		}
		typeNum = calViewTypeNum();
		setListItemParentAdapter();
	}

	// public ArrayList<YAListItemBase> getDataList(){
	// return mDataList;
	// }

	public int getCount() {
		return mDataList.size();
	}

	public Object getItem(int position) {
		FOBAdapterItemBase listItem = mDataList.get(position);
		listItem.setOrderId(position);
		return listItem;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = ((FOBAdapterItemBase) (getItem(position))).getView(
				convertView, isScrolling);
		return view;
	}

	public void updateView(int index) {
		int visiblePosition = mListView.getFirstVisiblePosition();
		int headerNum = 0;
		if (mListView instanceof ListView) {
			ListView listview = (ListView) mListView;
			headerNum = listview.getHeaderViewsCount();
		}
		int childAt = index - visiblePosition + headerNum;
		if (childAt >= 0 && childAt < mListView.getChildCount()) {
			View view = mListView.getChildAt(childAt);
			View v = ((FOBAdapterItemBase) (getItem(index))).getView(view,
					false);
			v.invalidate();
			v.requestLayout();
		}
	}

	public void updateVisibleView() {
		int headerNum = 0;
		if (mListView instanceof ListView) {
			ListView listview = (ListView) mListView;
			headerNum = listview.getHeaderViewsCount();
		}
		int first = mListView.getFirstVisiblePosition();
		int count = mListView.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = mListView.getChildAt(i);
			int itemIndex = (first + i - headerNum);
			if (itemIndex >= 0 && itemIndex < getCount()) {
				((FOBAdapterItemBase) (getItem(itemIndex)))
						.getView(view, false);
			}
		}
	}

	public void clearData() {
		mDataList.clear();
		typeNum = calViewTypeNum();
	}

	public void addListItem(FOBAdapterItemBase item) {
		// item.setAdapter(this);
		mDataList.add(item);
		typeNum = calViewTypeNum();
	}

	public void addListItems(ArrayList<FOBAdapterItemBase> items) {
		if (mDataList == null) {
			mDataList = new ArrayList<FOBAdapterItemBase>();
		}
		if (items != null) {
			int len = items.size();
			for (int i = 0; i < len; i++) {
				FOBAdapterItemBase item = items.get(i);
				// item.setAdapter(this);
				mDataList.add(item);
			}
		}
	}

	public void insertListItem(FOBAdapterItemBase item) {
		// item.setAdapter(this);
		mDataList.add(0, item);
		typeNum = calViewTypeNum();
	}

	public void removeListItem(FOBAdapterItemBase item) {
		mDataList.remove(item);
		typeNum = calViewTypeNum();
	}

	public void removeListItem(FOBAdapterModelBase model) {
		removeListItem(model.getId());
	}

	public void removeListItem(String id) {
		int index = findModelIndex(id);
		if (index >= 0) {
			mDataList.remove(index);
		}
		typeNum = calViewTypeNum();
	}

	public FOBAdapterModelBase findModel(String id) {
		int index = findModelIndex(id);
		if (index >= 0) {
			return mDataList.get(index).getModel();
		} else {
			return null;
		}
	}

	public int findModelIndex(String id) {
		int len = mDataList.size();
		for (int i = 0; i < len; i++) {
			FOBAdapterModelBase tempModel = mDataList.get(i).getModel();
			if (tempModel.getId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	public boolean isFirstAdapterModel(String id) {
		boolean flag = false;

		int len = mDataList.size();
		for (int i = 0; i < len; i++) {
			FOBAdapterModelBase tempModel = mDataList.get(i).getModel();
			if (tempModel.getId().equals(id)) {
				if (i == 0) {
					flag = true;
				}
			}
		}
		return flag;
	}

	public boolean isLastAdapterModel(String id) {
		boolean flag = false;

		int len = mDataList.size();
		for (int i = 0; i < len; i++) {
			FOBAdapterModelBase tempModel = mDataList.get(i).getModel();
			if (tempModel.getId().equals(id)) {
				if (i == (len - 1)) {
					flag = true;
				}
			}
		}
		return flag;
	}

	private void setListItemParentAdapter() {
		int len = mDataList.size();
		for (int i = 0; i < len; i++) {
			// mDataList.get(i).setAdapter(this);
		}
	}

	public void refresh() {
		this.notifyDataSetChanged();
	}

	public void refresh(int start, int len) {
		if (mListView instanceof ListView) {
			ListView listview = (ListView) mListView;
			start += listview.getHeaderViewsCount();
		}
		for (int i = start; i < start + len; i++) {
			updateView(i);
		}
	}

	// public double getNumberValueSum(int index){
	// if(mDataList != null){
	// double sum = 0;
	// int len = mDataList.size();
	// for(int i=0;i<len;i++){
	// sum += mDataList.get(i).getModel().getNumberValue(index);
	// }
	// return sum;
	// }else{
	// return 0;
	// }
	// }

	@Override
	public int getItemViewType(int position) {
		return ((FOBAdapterItemBase) (getItem(position))).getItemViewType();
	}

	@Override
	public int getViewTypeCount() {
		return typeNum;
	}

	private int calViewTypeNum() {
		if (mDataList == null || mDataList.size() < 1) {
			return 1;
		} else {
			String typeStr = "";
			int num = 0;
			int len = mDataList.size();
			for (int i = 0; i < len; i++) {
				int type = ((FOBAdapterItemBase) (mDataList.get(i)))
						.getItemViewType();
				String s = ("#" + String.valueOf(type) + "#");
				if (typeStr.indexOf(s) == -1) {
					typeStr += s;
					num++;
				}
			}
			return num;
		}
	}

	public void setIsScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}

	public static FOBAdapter getFOBAdapter(AdapterView adapterView) {
		Adapter adapter = adapterView.getAdapter();
		if (adapter instanceof HeaderViewListAdapter) {
			return (FOBAdapter) ((HeaderViewListAdapter) adapter)
					.getWrappedAdapter();
		} else {
			return (FOBAdapter) adapter;
		}
	}
}
