package com.daddys40.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public class ProgressDialogManager {
	private static ProgressDialog progressDialog = null;
	private static Context mContext;
	private static String mTitle;
	private static String mMsg;

	private ProgressDialogManager() {
	}

	public synchronized static void dismiss() {

		dissmissHandler.sendEmptyMessage(0);
	}
	public synchronized static void showProgressDialog(final Context context, final String title, final String message) {
		mContext = context;
		mTitle = title;
		mMsg =message;
		showingHandler.sendEmptyMessage(0);
	}

	private final static Handler dissmissHandler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			progressDialog.dismiss();
			return false;
		}
	});

	private final static Handler showingHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
//			if(progressDialog != null)
//				progressDialog.dismiss();
			progressDialog = ProgressDialog.show(mContext, mTitle, mMsg);
			return false;
		}
	});
}
