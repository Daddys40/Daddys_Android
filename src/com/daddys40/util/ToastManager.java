package com.daddys40.util;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.Toast;

public class ToastManager {
	private static ToastManager tm;
	private Context context;
	private String message;
	private int duration;
	private ToastManager(){
		
	}
	synchronized public static ToastManager getInstance(){
		if(tm == null)
			tm = new ToastManager();
		return tm;
	}
	public void showToast(Context context, String msg, int duration){
		this.context = context;
		this.message = msg;
		this.duration = duration;
		toastHandler.sendEmptyMessage(0);
	}
	public void init(){
	}
	private Handler toastHandler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			Toast.makeText(context, message, duration).show();
			return false;
		}
	});
}
