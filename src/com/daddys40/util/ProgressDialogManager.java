package com.daddys40.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public class ProgressDialogManager {
	private static ProgressDialog progressDialog = null;
	private ProgressDialogManager() {
	}

	public synchronized static void dismiss() {
		Handler dissmissHandler = new Handler(new Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				progressDialog.dismiss();
				return false;
			}
		});
		dissmissHandler.sendEmptyMessage(0);
	}

	public synchronized static void showProgressDialog(final Context context, final String title,
			final String message) {
		Handler showingHandler = new Handler(new Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				progressDialog = ProgressDialog.show(context, title, message);
				return false;
			}
		});
		showingHandler.sendEmptyMessage(0);
	}
}
