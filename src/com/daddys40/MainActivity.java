package com.daddys40;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daddys40.alarm.EnrollAlarm;
import com.daddys40.util.DataManager;
import com.daddys40.util.DataManager.DoneCallback;
import com.daddys40.util.DialogMaker;
import com.daddys40.util.MyTagManager;
import com.daddys40.util.UserData;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.tagmanager.Container;
import com.google.tagmanager.ContainerOpener;
import com.google.tagmanager.ContainerOpener.OpenType;
import com.google.tagmanager.TagManager;
/**
 * 로딩화면, 초기설정, 셋팅 편집 부분을 포함하는 액티비티
 * @author Kim
 *
 */
public class MainActivity extends Activity {

	private Button mBtnSave;
	private Button mBtnMale;
	private Button mBtnFemale;

	private EditText mEtDate;
	private EditText mEtName;
	private EditText mEtHour;
	
	private EditText mEtAge;
	private EditText mEtHeight;
	private EditText mEtWeight;
	
	private int mSelYear = 0;
	private int mSelMonth = -1;
	private int mSelDay = 0;
	private int mSelHour = 19;
	private int mSelMin = 0;
	private int mSelSex = UserData.SEX_MALE;
	private LinearLayout mFatherSettingLay;

	private boolean isFromSetting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		MyTagManager.getInstance(this).pushEventData("main", "screen", "main screen");
		
		UserData.init(MainActivity.this);
		Intent intent = getIntent();
		isFromSetting = intent.getBooleanExtra("FromSetting", false);
		if (isFromSetting) {
			setContentView(R.layout.activity_main);
			initView();
			initEvent();
		}
		else {
			setContentView(R.layout.activity_loading);
			Handler handler = new Handler(new Callback() {
				@Override
				public boolean handleMessage(Message msg) {
					if (UserData.getInstance().getName() != null && UserData.getInstance().isSerialized()) {
//						startActivity(new Intent(MainActivity.this, ResultActivity.class));
						startActivity(new Intent(MainActivity.this, MainFragmentActivity.class));
						finish();
					}
					else {
						setContentView(R.layout.activity_main);
						initView();
						initEvent();
					}
					return false;
				}
			});
			handler.sendEmptyMessageDelayed(0, 3000);
		}
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	private void initView() {
		mFatherSettingLay = (LinearLayout) findViewById(R.id.lay_main_mother_info);
		
		mBtnSave = (Button) findViewById(R.id.btn_save);
		mBtnMale = (Button) findViewById(R.id.btn_main_male);
		mBtnFemale = (Button) findViewById(R.id.btn_main_female);

		mEtAge = (EditText) findViewById(R.id.et_main_age);
		mEtHeight = (EditText) findViewById(R.id.et_main_height);
		mEtWeight = (EditText) findViewById(R.id.et_main_weight);
		
		mEtDate = (EditText) findViewById(R.id.et_main_cal);
		mEtName = (EditText) findViewById(R.id.et_main_name);
		mEtHour = (EditText) findViewById(R.id.et_main_hour);
		if (isFromSetting) {
			((RelativeLayout) findViewById(R.id.lay_main_top_for_setting)).setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.btn_main_back)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					onBackPressed();
				}
			});
			if (UserData.getInstance().getAlarmTimeMin() >= 10)
				mEtHour.setText(UserData.getInstance().getAlarmTimeHour() + " : "
						+ UserData.getInstance().getAlarmTimeMin());
			else
				mEtHour.setText(UserData.getInstance().getAlarmTimeHour() + " : 0"
						+ UserData.getInstance().getAlarmTimeMin());
			Calendar cal = UserData.getInstance().getCalendar();
			mSelYear = cal.get(Calendar.YEAR);
			mSelMonth = cal.get(Calendar.MONTH);
			mSelDay = cal.get(Calendar.DAY_OF_MONTH);
			mSelHour = UserData.getInstance().getAlarmTimeHour();
			mSelMin = UserData.getInstance().getAlarmTimeMin();
			mEtDate.setText(mSelYear + " / " + (mSelMonth + 1) + " / " + mSelDay);
			mEtName.setText(UserData.getInstance().getName());
			mSelSex = UserData.getInstance().getSex();
			if (mSelSex == UserData.SEX_MALE) {
				mBtnMale.setBackgroundResource(R.drawable.setting_edit_btn_dad_on);
				mBtnFemale.setBackgroundResource(R.drawable.setting_edit_btn_mom_off);
				mFatherSettingLay.setVisibility(View.VISIBLE);
				if(UserData.getInstance().getAge() != 0)
					mEtAge.setText(UserData.getInstance().getAge()+ "");
				if(UserData.getInstance().getHeight() != 0)
					mEtHeight.setText(UserData.getInstance().getHeight()+ "");
				if(UserData.getInstance().getWeight() != 0)
					mEtWeight.setText(UserData.getInstance().getWeight()+ "");
			}
			else {
				mBtnMale.setBackgroundResource(R.drawable.setting_edit_btn_dad_off);
				mBtnFemale.setBackgroundResource(R.drawable.setting_edit_btn_mom_on);
				mFatherSettingLay.setVisibility(View.GONE);
			}
		}
		else{
			((RelativeLayout) findViewById(R.id.lay_main_top_for_setting)).setVisibility(View.INVISIBLE);
			mEtHour.setText("19 : 00");
		}
		mEtDate.setFocusable(false);
		mEtHour.setFocusable(false);
	}

	private void initEvent() {
		mEtDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Dialog datepicker = new DatePickerDialog(MainActivity.this, new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						mSelYear = year;
						mSelMonth = monthOfYear;
						mSelDay = dayOfMonth;
						mEtDate.setText(year + " / " + (monthOfYear + 1) + " / " + dayOfMonth);

					}
				}, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar
						.getInstance().get(Calendar.DAY_OF_MONTH));
				Calendar minCal = Calendar.getInstance();
				minCal.add(Calendar.DAY_OF_MONTH, -1);
				((DatePickerDialog) datepicker).getDatePicker().setMinDate(minCal.getTimeInMillis());
				Calendar maxCal = Calendar.getInstance();
				maxCal.add(Calendar.WEEK_OF_YEAR, 36);
				((DatePickerDialog) datepicker).getDatePicker().setMaxDate(maxCal.getTimeInMillis());
				datepicker.show();
			}
		});
		mBtnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSelYear != 0 && mSelMonth != -1 && mSelDay != 0 && mEtName.getText().toString() != null
						&& !mEtName.getText().toString().equals("")) {
					Calendar calendar = Calendar.getInstance();
					calendar.set(mSelYear, mSelMonth, mSelDay, 0, 0, 0);
					UserData.getInstance().setCalendar(calendar);
					UserData.getInstance().setName(mEtName.getText().toString());
					UserData.getInstance().setAlarmTime(mSelHour, mSelMin);
					UserData.getInstance().setSex(mSelSex);
					if(mSelSex == UserData.SEX_MALE){
						if(mEtAge.getText() != null && !"".equals(mEtAge.getText().toString()))
							UserData.getInstance().setAge(Integer.parseInt(mEtAge.getText().toString()));
						if(mEtHeight.getText() != null && !"".equals(mEtHeight.getText().toString()))
							UserData.getInstance().setHeight(Integer.parseInt(mEtHeight.getText().toString()));
						if(mEtWeight.getText() != null && !"".equals(mEtWeight.getText().toString()))
							UserData.getInstance().setWeight(Integer.parseInt(mEtWeight.getText().toString()));
					}
					if (isFromSetting) {
						setResult(0);
						finish();
					}
					else {
						DataManager.getInstance().initQnAData(MainActivity.this);
						if (mSelSex == UserData.SEX_MALE)
							EnrollAlarm.getInstance().setAlarm(MainActivity.this);
						if (UserData.getInstance().isSerialized()) {
							if (mSelSex == UserData.SEX_MALE) {
								callMaleDlg();
							}
							else {
								callFemaleDlg();
							}
						}
						DataManager.getInstance().setOnNetworkDoneListener(new DoneCallback() {

							@Override
							public void onNetworkDoneListener() {
								if (mSelSex == UserData.SEX_MALE) {
									maleDlg.sendEmptyMessage(0);
								}
								else {
									femaleDlg.sendEmptyMessage(0);
								}
							}
						});
					}
				}
				else
					Toast.makeText(MainActivity.this, "출산 예정일과 태명을 정확히 입력해 주세요.", 1000).show();
			}
		});
		mEtHour.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickerDialog timePicker = new TimePickerDialog(MainActivity.this, new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						mSelHour = hourOfDay;
						mSelMin = minute;
						if (minute >= 10)
							mEtHour.setText(hourOfDay + " : " + minute);
						else
							mEtHour.setText(hourOfDay + " : 0" + minute);
					}
				}, mSelHour, mSelMin, true);
				timePicker.show();
			}
		});
		mBtnMale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFatherSettingLay.setVisibility(View.VISIBLE);
				mSelSex = UserData.SEX_MALE;
				mBtnMale.setBackgroundResource(R.drawable.setting_edit_btn_dad_on);
				mBtnFemale.setBackgroundResource(R.drawable.setting_edit_btn_mom_off);
			}
		});
		mBtnFemale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFatherSettingLay.setVisibility(View.GONE);
				mSelSex = UserData.SEX_FEMALE;
				mBtnMale.setBackgroundResource(R.drawable.setting_edit_btn_dad_off);
				mBtnFemale.setBackgroundResource(R.drawable.setting_edit_btn_mom_on);
			}
		});
	}

	Handler maleDlg = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			callMaleDlg();
			return false;
		}
	});

	Handler femaleDlg = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			callFemaleDlg();
			return false;
		}
	});

	private void callMaleDlg() {
		DialogMaker dm = new DialogMaker(MainActivity.this, R.layout.dialog_mode_explain, 1.0, 1.0);
		final Dialog dialog = dm.getDialog();
		((Button) dialog.findViewById(R.id.btn_dialog_mode_ok)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				startActivity(new Intent(MainActivity.this, ResultActivity.class));
				startActivity(new Intent(MainActivity.this, MainFragmentActivity.class));
				finish();
			}
		});
		dialog.show();
	}

	private void callFemaleDlg() {
		DialogMaker dm = new DialogMaker(MainActivity.this, R.layout.dialog_mode_explain, 1.0, 1.0);
		final Dialog dialog = dm.getDialog();
		((TextView) dialog.findViewById(R.id.dialog_mode_title)).setText(getResources().getString(
				R.string.mode_mother_title));
		((TextView) dialog.findViewById(R.id.dialog_mode_content)).setText(getResources().getString(
				R.string.mode_mother));
		((Button) dialog.findViewById(R.id.btn_dialog_mode_ok)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				startActivity(new Intent(MainActivity.this, ResultActivity.class));
				startActivity(new Intent(MainActivity.this, MainFragmentActivity.class));
				finish();
			}
		});
		dialog.show();
	}
}
