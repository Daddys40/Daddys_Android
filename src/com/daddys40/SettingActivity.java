package com.daddys40;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.daddys40.alarm.EnrollAlarm;
import com.daddys40.data.InstantUserData;
import com.daddys40.network.DeleteRequest;
import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.RequestThread;
import com.daddys40.network.SettingRequest;
import com.daddys40.util.DialogMaker;
import com.daddys40.util.LogUtil;
import com.daddys40.util.ProgressDialogManager;
import com.daddys40.util.ToastManager;
import com.daddys40.util.UserData;

public class SettingActivity extends Activity {
	//
	// private EditText mEtAge;
	// private EditText mEtHeight;
	// private EditText mEtWeight;

	private EditText mEtAlarm;
	private String mCurrentSetDay = "135";
	private Button mBtnDay[] = new Button[7];

	private ImageView mDayLine[] = new ImageView[7];
	private TextView mTvDday;
	private Button mBtnSave;
	private Button mBtnDelete;

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
		// mEtAge = (EditText) findViewById(R.id.et_signup_age);
		// mEtAge.setText(InstantUserData.getInstance().getAge() + "");
		// mEtHeight = (EditText) findViewById(R.id.et_signup_height);
		// mEtHeight.setText(InstantUserData.getInstance().getHeight() + "");
		// mEtWeight = (EditText) findViewById(R.id.et_signup_weight);
		// mEtWeight.setText(InstantUserData.getInstance().getWeight() + "");

		mTvDday = (TextView) findViewById(R.id.tv_setting_dday);
		mTvDday.setText("임신 " + UserData.getInstance().currentWeek() + "주차");

		mEtAlarm = (EditText) findViewById(R.id.et_signup_time);
		mEtAlarm.setFocusable(false);

		mBtnDay[0] = (Button) findViewById(R.id.btn_signup_0);
		mBtnDay[1] = (Button) findViewById(R.id.btn_signup_1);
		mBtnDay[2] = (Button) findViewById(R.id.btn_signup_2);
		mBtnDay[3] = (Button) findViewById(R.id.btn_signup_3);
		mBtnDay[4] = (Button) findViewById(R.id.btn_signup_4);
		mBtnDay[5] = (Button) findViewById(R.id.btn_signup_5);
		mBtnDay[6] = (Button) findViewById(R.id.btn_signup_6);

		mDayLine[0] = (ImageView) findViewById(R.id.dayline_0);
		mDayLine[1] = (ImageView) findViewById(R.id.dayline_1);
		mDayLine[2] = (ImageView) findViewById(R.id.dayline_2);
		mDayLine[3] = (ImageView) findViewById(R.id.dayline_3);
		mDayLine[4] = (ImageView) findViewById(R.id.dayline_4);
		mDayLine[5] = (ImageView) findViewById(R.id.dayline_5);
		mDayLine[6] = (ImageView) findViewById(R.id.dayline_6);

		mCurrentSetDay = UserData.getInstance().getAlarmDay();

		mBtnDay[Integer.parseInt(mCurrentSetDay.charAt(0) + "")].setBackgroundResource(DAY_BACKGROUND_SELECTED[Integer
				.parseInt(mCurrentSetDay.charAt(0) + "")]);
		mDayLine[Integer.parseInt(mCurrentSetDay.charAt(0) + "")].setVisibility(View.VISIBLE);
		mBtnDay[Integer.parseInt(mCurrentSetDay.charAt(1) + "")].setBackgroundResource(DAY_BACKGROUND_SELECTED[Integer
				.parseInt(mCurrentSetDay.charAt(1) + "")]);
		mDayLine[Integer.parseInt(mCurrentSetDay.charAt(1) + "")].setVisibility(View.VISIBLE);
		mBtnDay[Integer.parseInt(mCurrentSetDay.charAt(2) + "")].setBackgroundResource(DAY_BACKGROUND_SELECTED[Integer
				.parseInt(mCurrentSetDay.charAt(2) + "")]);
		mDayLine[Integer.parseInt(mCurrentSetDay.charAt(2) + "")].setVisibility(View.VISIBLE);
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
		mBtnDelete = (Button) findViewById(R.id.btn_setting_delete);
	}

	private void initEvent() {
		mBtnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogMaker dm = new DialogMaker(SettingActivity.this, R.layout.dialog_backpress, 0.8, 0.3);
				final Dialog dlg = dm.getDialog();
				ToastManager.getInstance().init();
				((TextView) dlg.findViewById(R.id.tv_backpress_content)).setText("정말 탈퇴하시겠습니까?\n 삭제된 정보는 복구할 수 없습니.");
				((Button) dlg.findViewById(R.id.btn_backpress_ok)).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						ProgressDialogManager.showProgressDialog(SettingActivity.this, "처리", "삭제중입니다. . .");
						RequestThread rq = new DeleteRequest();
						rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {

							@Override
							public void onFinish(String result, JSONObject jsonObject) {
								setResult(101);
								ToastManager.getInstance().showToast(SettingActivity.this, "탈퇴가 완료되었습니다.", 2000);
								UserData.getInstance().setToken(null);
								ProgressDialogManager.dismiss();
								dlg.dismiss();
								finish();
							}

							@Override
							public void onExceptionError(Exception e) {
								// TODO Auto-generated method stub
								ToastManager.getInstance().showToast(SettingActivity.this, "네트워크 상태를 확인해주세요.", 2000);
								ProgressDialogManager.dismiss();
							}
						});
						rq.start();
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
		});
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

						UserData.getInstance().setToken(
								((JSONObject) jsonObject.get("current_user")).get("authentication_token") + "");
						UserData.getInstance().setAlarmDay(
								((JSONObject) jsonObject.get("current_user")).get("notifications_days") + "");
						UserData.getInstance()
								.setAlarmTime(
										Integer.parseInt((((JSONObject) jsonObject.get("current_user"))
												.get("notificate_at") + "").substring(0, 2)),
										Integer.parseInt((((JSONObject) jsonObject.get("current_user"))
												.get("notificate_at") + "").substring(3, 5)));
						InstantUserData.getInstance().setToken(
								((JSONObject) jsonObject.get("current_user")).get("authentication_token") + "");

						UserData.getInstance().setSex(((JSONObject) jsonObject.get("current_user")).get("gender") + "");
						InstantUserData.getInstance().setGender(
								((JSONObject) jsonObject.get("current_user")).get("gender") + "");
						InstantUserData.getInstance().setAlarmDay(
								((JSONObject) jsonObject.get("current_user")).get("notifications_days") + "");
						InstantUserData.getInstance().setAlarmTime(
								((JSONObject) jsonObject.get("current_user")).get("notificate_at") + "");
						InstantUserData.getInstance().setAge(
								((JSONObject) jsonObject.get("current_user")).get("age") + "");
						InstantUserData.getInstance().setWeight(
								((JSONObject) jsonObject.get("current_user")).get("weight") + "");
						InstantUserData.getInstance().setHeight(
								((JSONObject) jsonObject.get("current_user")).get("height") + "");

						if (((JSONObject) jsonObject.get("current_user")).get("partner") != null)
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
				InstantUserData.resetInstantUserData();
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
			mDayLine[day_index].setVisibility(View.INVISIBLE);
		}
		else {
			daySelect[day_index] = true;
			daySelectNumber--;
			mBtnDay[day_index].setBackgroundResource(DAY_BACKGROUND_SELECTED[day_index]);
			mDayLine[day_index].setVisibility(View.VISIBLE);
		}
		mCurrentSetDay = "";
		for (int i = 0; i < 7; i++) {
			if (daySelect[i])
				mCurrentSetDay += i;
		}
		LogUtil.e("Selected day to integer", mCurrentSetDay);
	}
}
