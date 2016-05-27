package com.fob.page;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fob.balls.AboutActivity;
import com.fob.balls.InfoSettingActivity;
import com.fob.balls.LoginActivity;
import com.fob.balls.PasswordSettingActivity;
import com.fob.balls.R;
import com.fob.balls.RemindActivity;
import com.fob.balls.RemindTimeActivity;
import com.fob.balls.dialog.App;
import com.fob.page.base.FOBPageBase;
import com.fob.util.AppSharedPreferences;
import com.fob.util.L;

public class FOBPageSetting extends FOBPageBase {
	private static final String TAG = "FOBPageSetting";

	static ImageView iv_head;

	public FOBPageSetting() {
		super(false);
	}

	@Override
	protected View createContentView(ViewGroup root) {
		Activity mActivity = LoginActivity.getActivity();
		View mRootView = View.inflate(mActivity, R.layout.fob_page_setting,
				root);
		return mRootView;
	}

	public static void setHead(Bitmap bm) {
		if (iv_head == null)
			L.i(TAG, "setHead head");
		if (bm == null)
			L.i(TAG, "setHead bm");
		L.i(TAG, "setHead");
		if (iv_head != null && bm != null)
			iv_head.setImageBitmap(bm);
	}

	@Override
	protected void onFirstDisplayed() {
		iv_head = (ImageView) getParentView().findViewById(
				R.id.fob_page_setting_head_iv);

		String header = App.preferences.getStringMessage("user", "header", "");
		if (header != null && !"".equals(header)) {
			File headPic = new File(header);
			if (headPic != null && headPic.exists()) {
				setHead(LoginActivity.getActivity().getHeadFilePath(
						Uri.fromFile(headPic)));
			}
		}

		LinearLayout head_setting = (LinearLayout) getParentView()
				.findViewById(R.id.fob_page_setting_head);
		head_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showUpHeadDialog();
			}
		});

		LinearLayout info_setting = (LinearLayout) getParentView()
				.findViewById(R.id.fob_page_setting_info);
		info_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LoginActivity.getActivity().startActivity(
						new Intent(LoginActivity.getActivity(),
								InfoSettingActivity.class));
			}
		});

		LinearLayout password = (LinearLayout) getParentView().findViewById(
				R.id.fob_page_setting_password);
		password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LoginActivity.getActivity().startActivity(
						new Intent(LoginActivity.getActivity(),
								PasswordSettingActivity.class));
			}
		});

		LinearLayout time = (LinearLayout) getParentView().findViewById(
				R.id.fob_page_setting_time);
		time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LoginActivity.getActivity().startActivity(
						new Intent(LoginActivity.getActivity(),
								RemindTimeActivity.class));
			}
		});

		LinearLayout remind = (LinearLayout) getParentView().findViewById(
				R.id.fob_page_setting_remind);
		remind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LoginActivity.getActivity().startActivity(
						new Intent(LoginActivity.getActivity(),
								RemindActivity.class));
			}
		});

		LinearLayout about = (LinearLayout) getParentView().findViewById(
				R.id.fob_page_setting_about);
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LoginActivity.getActivity().startActivity(
						new Intent(LoginActivity.getActivity(),
								AboutActivity.class));
			}
		});

		// LinearLayout citys = (LinearLayout) getParentView().findViewById(
		// R.id.fob_page_setting_citys);
		// citys.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// LoginActivity.getActivity().startActivity(
		// new Intent(LoginActivity.getActivity(),
		// SetCityActivity.class));
		// }
		// });
		//
		// LinearLayout balls = (LinearLayout) getParentView().findViewById(
		// R.id.fob_page_setting_balls);
		// balls.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// LoginActivity.getActivity().startActivity(
		// new Intent(LoginActivity.getActivity(),
		// SetBallsActivity.class));
		// }
		// });

		LinearLayout exit = (LinearLayout) getParentView().findViewById(
				R.id.fob_page_setting_exit);
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(LoginActivity.getActivity())
						.setTitle("提示")
						.setMessage("退出当前帐号？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// closePage();
										LoginActivity.getActivity().finish();
										new AppSharedPreferences(LoginActivity
												.getActivity())
												.saveBooleanMessage("user",
														"hasLogin", false);
										App.preferences.saveBooleanMessage(
												"user", "MyCourt", false);
									}
								}).setNegativeButton("取消", null).show();
			}
		});
	}

	public static final int FROM_GALLERY = 3;
	public static final int FROM_CAMERA = 1;
	public static final int FROM_CROP = 2;
	public static final String PATH = Environment.getExternalStorageDirectory()
			.toString();
	public static final String headimg_name = "headimg.jpg";

	/**
	 * 显示修改头像对话框
	 **/
	private void showUpHeadDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LoginActivity.getActivity());
		builder.setTitle("修改头像");
		String[] dialogMsg = new String[] { "从相册选择", "拍照" };
		builder.setItems(dialogMsg, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					LoginActivity.getActivity().startActivityForResult(
							Intent.createChooser(intent, "选择图片"), FROM_GALLERY);
					break;
				case 1:
					Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File saveFile = new File(PATH);
					if (saveFile.exists()) {
						// Log.d(TAG,"目录已存在");
					} else {
						saveFile.mkdirs();
					}
					intent1.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(saveFile, headimg_name)));
					LoginActivity.getActivity().startActivityForResult(intent1,
							FROM_CAMERA);
					break;
				}
			}
		});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
	}

}
