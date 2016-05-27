package com.fob.tools;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class StateTouchListener implements OnTouchListener {
	// private Drawable background;
	//
	// private static void setContrastTranslateOnly(ColorMatrix cm, float
	// contrast) {
	// float scale = contrast + 1.f;
	// float translate = (-.5f * scale + .5f) * 255.f;
	// cm.set(new float[] {
	// 1, 0, 0, 0, translate,
	// 0, 1, 0, 0, translate,
	// 0, 0, 1, 0, translate,
	// 0, 0, 0, 1, 0 });
	// }
	//
	// @SuppressLint("NewApi")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// int action = event.getAction();
		// ColorMatrix cm = new ColorMatrix();
		// //System.out.println("action:" + action);
		// switch (action) {
		// case MotionEvent.ACTION_DOWN:
		// setContrastTranslateOnly(cm,70/180.f);
		// background=v.getBackground();
		// if(background==null){
		// v.setBackgroundColor(Color.GRAY);
		// }else{
		// background.setColorFilter(new ColorMatrixColorFilter(cm));
		// }
		// v.invalidate();
		// break;
		//
		// case MotionEvent.ACTION_OUTSIDE:
		// case MotionEvent.ACTION_CANCEL:
		// case MotionEvent.ACTION_UP:
		// if(background==null){
		// v.setBackground(background);
		// }else{
		// v.getBackground().setColorFilter(null);
		// }
		//
		// v.invalidate();
		// break;
		// default:
		// break;
		// }
		return false;
	}
}
