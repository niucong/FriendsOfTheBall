package com.fob.adapter;

import java.io.File;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.net.Uri;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fob.adapter.base.FOBAdapterItemBase;
import com.fob.balls.LoginActivity;
import com.fob.balls.R;
import com.fob.balls.dialog.App;
import com.fob.balls.dialog.ShowToast;
import com.fob.page.FOBPageDialogue;
import com.fob.struct.FOBStructMessage;

public class FOBAdapterMessageNowItem extends FOBAdapterItemBase {
	private Context context;

	private FOBPageDialogue mPage;
	private FOBStructMessage mMode;
	public TextView time;
	private String header;

	public FOBAdapterMessageNowItem(Context context, FOBStructMessage mode,
			FOBPageDialogue page, String header) {
		this.context = context;
		this.mPage = page;
		this.mMode = mode;
		this.header = header;
	}

	@Override
	public View getView(View convertView, boolean isScrolling) {
		String accessKey = App.preferences.getStringMessage("user", "Username",
				"");
		if (!accessKey.equals(mMode.getSender())) {
			convertView = View.inflate(context,
					R.layout.fob_adapter_message_own_item, null);
		} else {
			convertView = View.inflate(context,
					R.layout.fob_adapter_message_friend_item, null);
		}
		TextView content = (TextView) convertView
				.findViewById(R.id.fob_adapter_message_content);
		time = (TextView) convertView
				.findViewById(R.id.fob_adapter_message_time);
		ImageView head = (ImageView) convertView
				.findViewById(R.id.fob_adapter_message_header_view);
		content.setText(mMode.getContent());
		time.setText(mMode.getTime());

		content.setOnLongClickListener(new OnLongClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onLongClick(View v) {
				ClipboardManager clipboardManager = (ClipboardManager) LoginActivity
						.getActivity().getSystemService(
								Context.CLIPBOARD_SERVICE);
				clipboardManager.setText(mMode.getContent());
				ShowToast.getToast(App.app).show("已复制到剪切板");
				return false;
			}
		});

		int res;
		if ("女".equals(mMode.getGender())) {
			res = R.drawable.fob_adapter_friends_header_default_lady;
		} else {
			res = R.drawable.fob_adapter_friends_header_default_man;
		}
		if (accessKey.equals(mMode.getSender())) {
			String header = App.preferences.getStringMessage("user", "header",
					"");
			if (header != null && !"".equals(header)) {
				File headPic = new File(header);
				if (headPic != null && headPic.exists()) {
					head.setImageBitmap(LoginActivity.getActivity()
							.getHeadFilePath(Uri.fromFile(headPic)));
				} else {
					head.setImageResource(res);
				}
			} else {
				head.setImageResource(res);
			}
		} else {
			if (header != null) {
				FinalBitmap.create(LoginActivity.getActivity()).display(head,
						header);
			} else {
				head.setImageResource(res);
			}
		}

		// TODO AsynImageLoader asynImageLoader = new AsynImageLoader();
		// asynImageLoader.showImageAsyn(head, mMode.getHeadurl(), res);
		return convertView;
	}

	@Override
	public String getString() {
		return null;
	}

	@Override
	public FOBStructMessage getModel() {
		return mMode;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public String getId() {
		return mMode.getId();
	}
}
