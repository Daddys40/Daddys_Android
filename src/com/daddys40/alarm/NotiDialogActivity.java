package com.daddys40.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.daddys40.re.LogoLodingActivity;
import com.daddys40.re.R;
import com.daddys40.util.MySystem;
import com.daddys40.util.MyTagManager;
import com.daddys40.util.UserData;
/**
 * Noti가 갈때 실행되는 클래스
 * @author Kim
 *
 */
public class NotiDialogActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		win.requestFeature(Window.FEATURE_NO_TITLE);
		UserData.init(NotiDialogActivity.this);
		if (UserData.getInstance().currentWeek() >= 5 || UserData.getInstance().currentWeek() <= 41) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //잠금 화면 위에 뜨게 하기.
					| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); //화면 깨우기

			UserData.init(NotiDialogActivity.this);
			MySystem.init(NotiDialogActivity.this);
			MySystem.getInstance().Vibrate(1500L);
			setContentView(R.layout.dialog_noti);
			EnrollAlarm.getInstance().setAlarm(NotiDialogActivity.this);
			((Button) findViewById(R.id.btn_noti_ok)).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					MyTagManager.getInstance(NotiDialogActivity.this).sendEvent("N_Go to app on push dialog_male");
					Intent intent = new Intent(NotiDialogActivity.this, LogoLodingActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}
		else {
			finish();
		}
	}
	@Override
	protected void onStart() {
		super.onStart();
		MyTagManager.getInstance(this).send("appview", "N_Push dialog_male");
	}
	@Override
	public void onBackPressed() {
		MyTagManager.getInstance(NotiDialogActivity.this).sendEvent("N_Back button on push dialog");
		super.onBackPressed();
	}
}
