package com.daddys40.re;

import java.util.Calendar;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.daddys40.data.InstantUserData;
import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.SignUpRequest;
import com.daddys40.util.DefineConst;
import com.daddys40.util.DialogMaker;
import com.daddys40.util.LogUtil;
import com.daddys40.util.MyTagManager;
import com.daddys40.util.ProgressDialogManager;
import com.daddys40.util.ToastManager;
import com.daddys40.util.UserData;

public class SignupActivity extends MyActivity {

	private EditText mEtEmail;
	private EditText mEtPwd;
	private EditText mEtPwdRepeat;
	private EditText mEtBabyName;
	private EditText mEtName;
	private EditText mEtDday;
	private EditText mEtAge;
	private EditText mEtHeight;
	private EditText mEtWeight;
	private EditText mEtAlarm;

	private Button mBtnFather;
	private Button mBtnMother;
	private Button mBtnNext;
	
	private final String SELECT_FATHER = "male";
	private final String SELECT_MOTHER = "female";
	private String gender = SELECT_FATHER;
	
	private String mCurrentSetDay = "";
	
	private Button mBtnDay[] = new Button[7];
	private ImageView mDayLine[] = new ImageView[7];
	private boolean daySelect[] = {false,false,false,false,false,false,false};
	private final int  DAY_BACKGROUND[] = {R.drawable.day0,R.drawable.day1,R.drawable.day2,R.drawable.day3,R.drawable.day4,R.drawable.day5,R.drawable.day6};
	private final int  DAY_BACKGROUND_SELECTED[] = {R.drawable.day0_select,R.drawable.day1_select,R.drawable.day2_select,R.drawable.day3_select,R.drawable.day4_select
			,R.drawable.day5_select,R.drawable.day6_select};
	private int daySelectNumber = 0;
	
	private Button mBtnTerms;
	private Button mBtnPrivacy;

	private View mCheckService;
	private boolean mToggleService = false;

	private View mCheckPrivacy;
	private boolean mTogglePrivacy = false;
	
	private OnClickListener dayClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId() == mBtnDay[0].getId())	{
				selectDayHelper(0);
			}
			else if(v.getId() == mBtnDay[1].getId()){
				selectDayHelper(1);
			}
			else if(v.getId() == mBtnDay[2].getId()){
				selectDayHelper(2);
			}
			else if(v.getId() == mBtnDay[3].getId()){
				selectDayHelper(3);
			}
			else if(v.getId() == mBtnDay[4].getId()){
				selectDayHelper(4);
			}
			else if(v.getId() == mBtnDay[5].getId()){
				selectDayHelper(5);
			}
			else if(v.getId() == mBtnDay[6].getId()){
				selectDayHelper(6);
			}
		}
	};
	
	private synchronized void selectDayHelper(int day_index){
		if(!daySelect[day_index] && daySelectNumber == 3)
			return;
		if(daySelect[day_index])
		{
			daySelect[day_index] = false;
			daySelectNumber--;
			mBtnDay[day_index].setBackgroundResource(DAY_BACKGROUND[day_index]);
			mDayLine[day_index].setVisibility(View.INVISIBLE);
		}
		else
		{
			daySelect[day_index] = true;
			daySelectNumber++;
			mBtnDay[day_index].setBackgroundResource(DAY_BACKGROUND_SELECTED[day_index]);
			mDayLine[day_index].setVisibility(View.VISIBLE);
		}
		mCurrentSetDay = "";
		for(int i = 0; i < 7; i++){
			if(daySelect[i])
				mCurrentSetDay += i;
		}
		LogUtil.e("Selected day to integer", mCurrentSetDay);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		setBackPressMessage("메인 화면으로 돌아가시겠습니까?");
		initView();
		initEvent();
	}
	@Override
	protected void onStart() {
		super.onStart();
		MyTagManager.getInstance(this).send("appview", "N_Signup Activity");
	}

	private void initView() {
		mCheckService = findViewById(R.id.check_box_service);
		mCheckPrivacy = findViewById(R.id.check_box_privacy);
		
		mBtnTerms = (Button) findViewById(R.id.btn_terms);
		mBtnPrivacy = (Button) findViewById(R.id.btn_privacy);
		
		mBtnNext = (Button) findViewById(R.id.btn_signup_next);

		mEtEmail = (EditText) findViewById(R.id.et_signup_email);
		mEtPwd = (EditText) findViewById(R.id.et_signup_password);
		mEtPwdRepeat = (EditText) findViewById(R.id.et_signup_password_repeat);
		
		mEtBabyName = (EditText) findViewById(R.id.et_signup_babyname);
		mEtName = (EditText) findViewById(R.id.et_signup_name);
		mEtDday = (EditText) findViewById(R.id.et_signup_dday);
		mEtAge = (EditText) findViewById(R.id.et_signup_age);
		mEtHeight = (EditText) findViewById(R.id.et_signup_height);
		mEtWeight = (EditText) findViewById(R.id.et_signup_weight);
		mEtAge.setFocusable(false);
		mEtHeight.setFocusable(false);
		mEtWeight.setFocusable(false);
		
		mBtnFather = (Button) findViewById(R.id.btn_signup_father);
		mBtnMother = (Button) findViewById(R.id.btn_signup_mother);
		
		mEtDday.setFocusable(false);
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
		
		Random r = new Random(Calendar.getInstance().getTimeInMillis());
		if(r.nextBoolean()){
			selectDayHelper(1);
			selectDayHelper(3);
			selectDayHelper(5);
		}
		else{
			selectDayHelper(2);
			selectDayHelper(4);
			selectDayHelper(6);
		}
		switch (r.nextInt(3)) {
		case 0:
			mEtAlarm.setText("18:00");
			break;
		case 1:
			mEtAlarm.setText("19:00");
		case 2:
			mEtAlarm.setText("20:00");
		default:
			break;
		}
	}

	private void initEvent() {
mEtAge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogMaker dm = new DialogMaker(SignupActivity.this, R.layout.dialog_numpicker,0.6,0.35);
				final Dialog dlg = dm.getDialog();
				dlg.show();
				((NumberPicker) dlg.findViewById(R.id.numberPicker)).setMaxValue(100);
				((NumberPicker) dlg.findViewById(R.id.numberPicker)).setMinValue(10);
				((NumberPicker) dlg.findViewById(R.id.numberPicker)).setValue(30);
				((Button) dlg.findViewById(R.id.btn_numberPicker_ok)).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dlg.dismiss();
						mEtAge.setText(((NumberPicker) dlg.findViewById(R.id.numberPicker)).getValue() + "");
					}
				});
			}
		});
mEtWeight.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		DialogMaker dm = new DialogMaker(SignupActivity.this, R.layout.dialog_numpicker,0.6,0.35);
		final Dialog dlg = dm.getDialog();
		dlg.show();
		((NumberPicker) dlg.findViewById(R.id.numberPicker)).setMaxValue(150);
		((NumberPicker) dlg.findViewById(R.id.numberPicker)).setMinValue(30);
		((NumberPicker) dlg.findViewById(R.id.numberPicker)).setValue(55);
		((Button) dlg.findViewById(R.id.btn_numberPicker_ok)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dlg.dismiss();
				mEtWeight.setText(((NumberPicker) dlg.findViewById(R.id.numberPicker)).getValue() + "");
			}
		});
	}
});
mEtHeight.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		DialogMaker dm = new DialogMaker(SignupActivity.this, R.layout.dialog_numpicker,0.6,0.35);
		final Dialog dlg = dm.getDialog();
		dlg.show();
		((NumberPicker) dlg.findViewById(R.id.numberPicker)).setMaxValue(210);
		((NumberPicker) dlg.findViewById(R.id.numberPicker)).setMinValue(130);
		((NumberPicker) dlg.findViewById(R.id.numberPicker)).setValue(160);
		((Button) dlg.findViewById(R.id.btn_numberPicker_ok)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dlg.dismiss();
				mEtHeight.setText(((NumberPicker) dlg.findViewById(R.id.numberPicker)).getValue() + "");
			}
		});
	}
});
mBtnPrivacy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogMaker dm = new DialogMaker(SignupActivity.this, R.layout.dialog_webview, 0.95, 0.9);
				final Dialog dlg = dm.getDialog();
				dlg.setCancelable(false);
				((Button) dlg.findViewById(R.id.btn_webview_terms)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dlg.dismiss();
					}
				});
				((WebView) dlg.findViewById(R.id.webview_dialog)).getSettings().setJavaScriptEnabled(true);
				((WebView) dlg.findViewById(R.id.webview_dialog)).loadUrl(DefineConst.NETWORK_URL_PRIVACY);
				dlg.show();
			}
		});
		mBtnTerms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogMaker dm = new DialogMaker(SignupActivity.this, R.layout.dialog_webview, 0.95, 0.9);
				final Dialog dlg = dm.getDialog();
				dlg.setCancelable(false);
				((Button) dlg.findViewById(R.id.btn_webview_terms)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dlg.dismiss();
					}
				});
				((WebView) dlg.findViewById(R.id.webview_dialog)).getSettings().setJavaScriptEnabled(true);
				((WebView) dlg.findViewById(R.id.webview_dialog)).loadUrl(DefineConst.NETWORK_URL_TERMS);
				dlg.show();
			}
		});
		mCheckService.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mToggleService) {
					mToggleService = false;
					mCheckService.setBackgroundResource(R.drawable.box_icon);
				}
				else {
					mToggleService = true;
					mCheckService.setBackgroundResource(R.drawable.box_icon_checked);
				}

			}
		});
		mCheckPrivacy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mTogglePrivacy) {
					mTogglePrivacy = false;
					mCheckPrivacy.setBackgroundResource(R.drawable.box_icon);
				}
				else {
					mTogglePrivacy = true;
					mCheckPrivacy.setBackgroundResource(R.drawable.box_icon_checked);
				}

			}
		});
		
		for( int i= 0 ; i < 7 ; i++ )
			mBtnDay[i].setOnClickListener(dayClickListener);
		
		mEtDday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Dialog datepicker = new DatePickerDialog(SignupActivity.this, new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// mSelYear = year;
						// mSelMonth = monthOfYear;
						// mSelDay = dayOfMonth;
						mEtDday.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.YEAR, year);
						cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						cal.set(Calendar.MONTH, monthOfYear);
//						UserData.getInstance().setCalendar(cal);
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
		mBtnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!mEtPwd.getText().toString().equals(mEtPwdRepeat.getText().toString())){
					ToastManager.getInstance().showToast(SignupActivity.this, "비밀번호를 확인해주세요.", 1000);
					return;
				}
				if(daySelectNumber != 3){
					ToastManager.getInstance().showToast(SignupActivity.this, "알람을 받으실 요일은 반드시 3개여야 합니다.", 1000);
					return;
				}
				if(!mToggleService){
					ToastManager.getInstance().showToast(SignupActivity.this, "가입을 위해선 서비스 이용약관에 동의하셔야 합니다.", 1000);
					return;
				}
				if(!mTogglePrivacy){
					ToastManager.getInstance().showToast(SignupActivity.this, "가입을 위해선 개인정보보호정책에 동의하셔야 합니다.", 1000);
					return;
				}
				if(mEtEmail.getText().toString().equals("") || mEtPwd.getText().toString().equals("") || mEtName.getText().toString().equals("") ||
						mEtDday.getText().toString().equals("") || mEtAge.getText().toString().equals("") || mEtHeight.getText().toString().equals("") ||
						mEtWeight.getText().toString().equals("") || mEtAlarm.getText().toString().equals("")|| mEtBabyName.getText().toString().equals("")){
					ToastManager.getInstance().showToast(SignupActivity.this, "모든 항목을 올바르게 입력해주세요.", 1000);
					return;
				}
				ProgressDialogManager.showProgressDialog(SignupActivity.this,  "접속중", "회원가입중입니다...");
				
				//제대로 입력되지 않았을때 예외처리
				SignUpRequest rq = new SignUpRequest(mEtEmail.getText().toString(), mEtPwd.getText().toString(),
						mEtName.getText().toString(), gender, mEtBabyName.getText().toString(),
						mEtAge.getText().toString(), mEtHeight.getText().toString(), mEtWeight.getText().toString(),
						mEtDday.getText().toString(),mCurrentSetDay, mEtAlarm.getText().toString());
				ToastManager.getInstance().init();
				rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {

					@Override
					public void onFinish(String result, JSONObject jsonObject) {
						ProgressDialogManager.dismiss();
						LogUtil.e("Result", result);
						if(((JSONArray) jsonObject.get("errors"))!= null){
							ToastManager.getInstance().showToast(SignupActivity.this, ((JSONArray) jsonObject.get("errors")).get(0) + "", 1000);
						}
						else{
							LogUtil.e("Email", ((JSONObject) jsonObject.get("current_user")).get("email") + "");
							LogUtil.e("token", ((JSONObject)jsonObject.get("current_user")).get("authentication_token") + "");
							InstantUserData.getInstance().setToken(((JSONObject)jsonObject.get("current_user")).get("authentication_token") + "");
							startActivity(new Intent(SignupActivity.this,InvitingActivity.class));
							setResult(0);
							ToastManager.getInstance().showToast(SignupActivity.this, "회원가입이 되셨습니다.", 1000);
							finish();
						}
					}

					@Override
					public void onExceptionError(Exception e) {
						ProgressDialogManager.dismiss();
					}
				});
				rq.start();
				// startActivity(new Intent(SignupActivity.this,InvitingActivity.class));
			}
		});
		mBtnFather.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				gender = SELECT_FATHER;
				mBtnFather.setBackgroundResource(R.drawable.btn_father_selected);
				mBtnMother.setBackgroundResource(R.drawable.btn_mother);
			}
		});
		mBtnMother.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gender = SELECT_MOTHER;
				mBtnFather.setBackgroundResource(R.drawable.btn_father);
				mBtnMother.setBackgroundResource(R.drawable.btn_mother_selected);
				
			}
		});
		mEtAlarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				TimePickerDialog timePicker = new TimePickerDialog(SignupActivity.this, new OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						UserData.getInstance().setAlarmTime(hourOfDay, minute);
						
						if (minute >= 10)
							if(hourOfDay < 10)
								mEtAlarm.setText("0" + hourOfDay + ":" + minute);
							else
								mEtAlarm.setText(hourOfDay + ":" + minute);
						else
							if(hourOfDay < 10)
								mEtAlarm.setText("0" + hourOfDay + ":0" + minute);
							else
								mEtAlarm.setText(hourOfDay + ":0" + minute);
					}
				}, UserData.getInstance().getAlarmTimeHour(), UserData.getInstance().getAlarmTimeMin(), true);
				timePicker.show();
			}
		});
	}
	
}
