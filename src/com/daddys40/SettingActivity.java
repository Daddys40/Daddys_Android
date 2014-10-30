package com.daddys40;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.daddys40.alarm.EnrollAlarm;
import com.daddys40.data.InstantUserData;
import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.SettingRequest;
import com.daddys40.util.LogUtil;
import com.daddys40.util.UserData;

public class SettingActivity extends Activity {
//
//	private EditText mEtAge;
//	private EditText mEtHeight;
//	private EditText mEtWeight;

	private EditText mEtAlarm;
	private String mCurrentSetDay = "135";
	private Button mBtnDay[] = new Button[7];
	
	private Button mBtnSave;

	private boolean daySelect[] = { false, false, false, false, false, false, false };

	private final int DAY_BACKGROUND[] = { R.drawable.day0, R.drawable.day1, R.drawable.day2, R.drawable.day3,
			R.drawable.day4, R.drawable.day5, R.drawable.day6 };

	private final int DAY_BACKGROUND_SELECTED[] = { R.drawable.day0_select, R.drawable.day1_select,
			R.drawable.day2_select, R.drawable.day3_select, R.drawable.day4_select, R.drawable.day5_select,
			R.drawable.day6_select };

	private int daySelectNumber = 3;
	
	private Button mBtnLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
		initEvent();
	}

	private void initView() {
//		mEtAge = (EditText) findViewById(R.id.et_signup_age);
//		mEtAge.setText(InstantUserData.getInstance().getAge() + "");
//		mEtHeight = (EditText) findViewById(R.id.et_signup_height);
//		mEtHeight.setText(InstantUserData.getInstance().getHeight() + "");
//		mEtWeight = (EditText) findViewById(R.id.et_signup_weight);
//		mEtWeight.setText(InstantUserData.getInstance().getWeight() + "");
		
		mEtAlarm = (EditText) findViewById(R.id.et_signup_time);
		mEtAlarm.setFocusable(false);

		for (int i = 0; i < 7; i++)
			mBtnDay[i] = (Button) findViewById(R.id.btn_signup_0 + i);
		
		mCurrentSetDay = UserData.getInstance().getAlarmDay();
		
		mBtnDay[Integer.parseInt(mCurrentSetDay.charAt(0) + "")].setBackgroundResource
		(DAY_BACKGROUND_SELECTED[Integer.parseInt(mCurrentSetDay.charAt(0) + "")]);
		mBtnDay[Integer.parseInt(mCurrentSetDay.charAt(1) + "")].setBackgroundResource
		(DAY_BACKGROUND_SELECTED[Integer.parseInt(mCurrentSetDay.charAt(1) + "")]);
		mBtnDay[Integer.parseInt(mCurrentSetDay.charAt(2) + "")].setBackgroundResource
		(DAY_BACKGROUND_SELECTED[Integer.parseInt(mCurrentSetDay.charAt(2) + "")]);
		daySelect[Integer.parseInt(mCurrentSetDay.charAt(0) + "")] = true;
		daySelect[Integer.parseInt(mCurrentSetDay.charAt(1) + "")] = true;
		daySelect[Integer.parseInt(mCurrentSetDay.charAt(2) + "")] = true;
		
		int hour = UserData.getInstance().getAlarmTimeHour();
		int min = UserData.getInstance().getAlarmTimeMin();
		if (min >= 10)
			if (hour < 10)
				mEtAlarm.setText("0" + hour + ":" + min);
			else
				mEtAlarm.setText(hour + ":" + min);
		else if (hour < 10)
			mEtAlarm.setText("0" + hour + ":0" + min);
		else
			mEtAlarm.setText(hour + ":0" + min);
		
		mBtnLogout = (Button) findViewById(R.id.btn_setting_logout);
		mBtnSave = (Button) findViewById(R.id.btn_setting_save);
	}

	private void initEvent() {
		mBtnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SettingRequest rq = new SettingRequest();
				rq.addSettingParams("notifications_days", mCurrentSetDay);
				rq.addSettingParams("notificate_at", mEtAlarm.getText().toString());
				rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
					
					@Override
					public void onFinish(String result, JSONObject jsonObject) {
						LogUtil.e("setting request result", result);
						
						UserData.getInstance().setToken(((JSONObject) jsonObject.get("current_user")).get("authentication_token")+"");
						UserData.getInstance().setAlarmDay(((JSONObject) jsonObject.get("current_user")).get("notifications_days") + "");
						UserData.getInstance().setAlarmTime( Integer.parseInt((((JSONObject) jsonObject.get("current_user")).get("notificate_at") + "").substring(0, 2)),
								Integer.parseInt((((JSONObject) jsonObject.get("current_user")).get("notificate_at") + "").substring(3, 5)));
						InstantUserData.getInstance().setToken(((JSONObject) jsonObject.get("current_user")).get("authentication_token")+"");
						
						UserData.getInstance().setSex(((JSONObject) jsonObject.get("current_user")).get("gender") + "");
						InstantUserData.getInstance().setGender(((JSONObject) jsonObject.get("current_user")).get("gender") + "");
						InstantUserData.getInstance().setAlarmDay(((JSONObject) jsonObject.get("current_user")).get("notifications_days") + "");
						InstantUserData.getInstance().setAlarmTime(((JSONObject) jsonObject.get("current_user")).get("notificate_at") + "");
						InstantUserData.getInstance().setAge(((JSONObject) jsonObject.get("current_user")).get("age") + "");
						InstantUserData.getInstance().setWeight(((JSONObject) jsonObject.get("current_user")).get("weight") + "");
						InstantUserData.getInstance().setHeight(((JSONObject) jsonObject.get("current_user")).get("height") + "");
						
						if(((JSONObject) jsonObject.get("current_user")).get("partner") != null) 
							InstantUserData.getInstance().setConnected(true);
						
						EnrollAlarm.getInstance().setAlarm(SettingActivity.this);
						finish();
					}
					
					@Override
					public void onExceptionError(Exception e) {
						
					}
				});
				rq.start();
			}
		});
		
		for (int i = 0; i < 7; i++)
			mBtnDay[i].setOnClickListener(dayClickListener);
		mEtAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TimePickerDialog timePicker = new TimePickerDialog(SettingActivity.this, new OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						UserData.getInstance().setAlarmTime(hourOfDay, minute);

						if (minute >= 10)
							if (hourOfDay < 10)
								mEtAlarm.setText("0" + hourOfDay + ":" + minute);
							else
								mEtAlarm.setText(hourOfDay + ":" + minute);
						else if (hourOfDay < 10)
							mEtAlarm.setText("0" + hourOfDay + ":0" + minute);
						else
							mEtAlarm.setText(hourOfDay + ":0" + minute);
					}
				}, UserData.getInstance().getAlarmTimeHour(), UserData.getInstance().getAlarmTimeMin(), true);
				timePicker.show();
			}
		});
		mBtnLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserData.getInstance().setToken(null);
				setResult(101);
				finish();
			}
		});
	}

	private OnClickListener dayClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == mBtnDay[0].getId()) {
				selectDayHelper(0);
			}
			else if (v.getId() == mBtnDay[1].getId()) {
				selectDayHelper(1);
			}
			else if (v.getId() == mBtnDay[2].getId()) {
				selectDayHelper(2);
			}
			else if (v.getId() == mBtnDay[3].getId()) {
				selectDayHelper(3);
			}
			else if (v.getId() == mBtnDay[4].getId()) {
				selectDayHelper(4);
			}
			else if (v.getId() == mBtnDay[5].getId()) {
				selectDayHelper(5);
			}
			else if (v.getId() == mBtnDay[6].getId()) {
				selectDayHelper(6);
			}
		}
	};

	private synchronized void selectDayHelper(int day_index) {
		if (!daySelect[day_index] && daySelectNumber == 3)
			return;
		if (daySelect[day_index]) {
			daySelect[day_index] = false;
			daySelectNumber++;
			mBtnDay[day_index].setBackgroundResource(DAY_BACKGROUND[day_index]);
		}
		else {
			daySelect[day_index] = true;
			daySelectNumber--;
			mBtnDay[day_index].setBackgroundResource(DAY_BACKGROUND_SELECTED[day_index]);
		}
		mCurrentSetDay = "";
		for (int i = 0; i < 7; i++) {
			if (daySelect[i])
				mCurrentSetDay += i;
		}
		LogUtil.e("Selected day to integer", mCurrentSetDay);
	}
}
