package com.daddys40.re;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.daddys40.util.DialogMaker;

public class MyActivity extends Activity {
	private String backMessage = "종료합니다.";

	protected void setBackPressMessage(String msg) {
		backMessage = msg;
	}

	@Override
	public void onBackPressed() {
		DialogMaker dm = new DialogMaker(MyActivity.this, R.layout.dialog_backpress,0.8,0.3);
		final Dialog dlg = dm.getDialog();
		((TextView) dlg.findViewById(R.id.tv_backpress_content)).setText(backMessage);
		((Button) dlg.findViewById(R.id.btn_backpress_ok)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dlg.dismiss();
				finish();
			}
		});
		((Button) dlg.findViewById(R.id.btn_backpress_cancel)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dlg.dismiss();
			}
		});
		dlg.show();
	}
}
