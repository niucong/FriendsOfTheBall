package com.fob.manager;

import android.database.Observable;

import com.fob.struct.FOBStructEvent;

//Observer
public class FOBCreateEventFinishManager extends Observable<FOBCreateEventFinishManager.CreateEventFinishListener>
{
	private static FOBCreateEventFinishManager instance;

	private FOBCreateEventFinishManager()
	{
	}

	public static FOBCreateEventFinishManager getInstance()
	{
		if (instance == null)
		{
			instance = new FOBCreateEventFinishManager();
		}
		return instance;
	}

	public void addNewsStateListener(FOBCreateEventFinishManager.CreateEventFinishListener listener)
	{
		registerObserver(listener);
	}

	public void reMoveNewsStateListener(FOBCreateEventFinishManager.CreateEventFinishListener listener)
	{
		unregisterObserver(listener);
	}

	public void noticeCreateEventFinish(FOBStructEvent pagination)
	{
		for(CreateEventFinishListener listener:mObservers){
			listener.onCreateEventFinish(pagination);
		}
		
	}

	public interface CreateEventFinishListener
	{
		public boolean onCreateEventFinish(FOBStructEvent event);
	}


}
