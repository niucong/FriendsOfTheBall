package com.fob.adapter;


import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.adapter.base.FOBAdapterModelBase;
import com.fob.balls.R;
import com.fob.page.FOBPageDialogue;
import com.fob.page.FOBPageLeaveDetails;
import com.fob.page.FOBPageMessage;
import com.fob.page.FOBPageNewsDetails;
import com.fob.struct.FOBStructLeave;
import com.fob.struct.FOBStructNews;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

public class FOBAdapterMessageHistoryItem extends FOBAdapterItemBase{
	  private Context context;
	    
	    private FOBPageDialogue mPage;
	    private FOBStructLeave mMode;
	    public FOBAdapterMessageHistoryItem(Context context, FOBStructLeave mode,FOBPageDialogue page)
	    {
	        this.context = context;
	        this.mPage=page;
	        this.mMode = mode;
	    }
		@Override
		public View getView(View convertView, boolean isScrolling) {
			if(convertView == null)
	        {    
	            convertView = View.inflate(context,R.layout.fob_adapter_message_leave_item, null);
	        }
	        TextView address=(TextView)convertView.findViewById(R.id.fob_adapter_message_leave_time);
	        address.setText(mMode.getTime());
	        TextView content=(TextView)convertView.findViewById(R.id.fob_adapter_message_leave_content_text);
	        content.setText(mMode.getContent());
	        convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					mPage.goNextPage(new FOBPageLeaveDetails(mMode));
					
				}
			});
	        
	        return convertView;
		}
		@Override
		public String getString() {
			return null;
		}
		@Override
		public FOBAdapterModelBase getModel() {
			return mMode;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {			
		}
		@Override
		public String getId() {
			return mMode.getId();
		}
}
