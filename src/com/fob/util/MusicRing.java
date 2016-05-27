package com.fob.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.fob.balls.R;

public class MusicRing {
	private Context mContext;
	private SoundPool soundPool;
	private int hitOkSfx;

	public MusicRing(Context context) {
		this.mContext = context;
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		hitOkSfx = soundPool.load(mContext, R.raw.newmsg, 0);
	}

	public void playSounds() {
		soundPool.play(hitOkSfx, 1, 1, 0, 0, 1);
	}
}
