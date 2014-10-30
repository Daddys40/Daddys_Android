package com.daddys40.alarm;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.daddys40.LogoLodingActivity;
import com.daddys40.R;
import com.daddys40.data.QnAData;
import com.daddys40.data.QnADataContainer;
import com.daddys40.util.LogUtil;
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
//			MyTagManager.getInstance(this).send("appview", "Push dialog");
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //잠금 화면 위에 뜨게 하기.
					| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); //화면 깨우기

			UserData.init(NotiDialogActivity.this);
			MySystem.init(NotiDialogActivity.this);
			MySystem.getInstance().Vibrate(1500L);
			setContentView(R.layout.dialog_noti);
//
//			QnADataContainer.getInstance().doDeserialization(NotiDialogActivity.this);
//			LogUtil.e("IN NOTI", "Current Week : " + UserData.getInstance().currentWeek());
//
//			ArrayList<QnAData> mArray = QnADataContainer.getInstance().getQnAArrayList(UserData.getInstance().currentWeek());
//			int index;
//			if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) < Calendar.WEDNESDAY)
//				index = 0;
//			else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) < Calendar.FRIDAY)
//				index = 1;
//			else
//				index = 2;
//			if(UserData.getInstance().currentWeek() == 41)
//				index = 0;
//			LogUtil.e("IN NOTI", "Current index : " + index);
//			//아빠일 경우 질문형태. 엄마일 경우 제목
//			if(UserData.getInstance().getSex() == UserData.SEX_MALE)
//				((TextView) findViewById(R.id.tv_noti_title)).setText(mArray.get(index).getQuestion());
//			else
//				((TextView) findViewById(R.id.tv_noti_title)).setText(mArray.get(index).getTitle());
			((Button) findViewById(R.id.btn_noti_ok)).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
//					MyTagManager.getInstance(NotiDialogActivity.this).sendEvent("On app button in push dialog");
//					Intent intent = new Intent(NotiDialogActivity.this, MainFragmentActivity.class);
//					intent.putExtra("noti", true);
//					startActivity(intent);
					finish();
				}
			});
		}
		else {
			finish();
		}
		EnrollAlarm.getInstance().setAlarm(NotiDialogActivity.this);
		((Button) findViewById(R.id.btn_noti_ok)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(NotiDialogActivity.this, LogoLodingActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	@Override
	public void onBackPressed() {
//		MyTagManager.getInstance(NotiDialogActivity.this).sendEvent("Back button on push dialog");
		super.onBackPressed();
	}
}
