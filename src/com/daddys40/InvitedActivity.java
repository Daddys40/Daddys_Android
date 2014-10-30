package com.daddys40;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.daddys40.network.InvitingRequest;
import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.RequestThread;
import com.daddys40.network.SignUpRequest;
import com.daddys40.util.DefineConst;
import com.daddys40.util.DialogMaker;
import com.daddys40.util.LogUtil;
import com.daddys40.util.ToastManager;
import com.daddys40.util.UserData;

public class InvitedActivity extends Activity {
	
	private EditText mEtCode;
	private EditText mEtEmail;
	private EditText mEtPwd;
	private EditText mEtPwdRepeat;
	private Button mBtnConnect;
	
	private Button mBtnTerms;
	private Button mBtnPrivacy;

	private View mCheckService;
	private boolean mToggleService = false;

	private View mCheckPrivacy;
	private boolean mTogglePrivacy = false;
	
	
	private String mCurrentSetDay = "135";
	private EditText mEtAlarm;
	private Button mBtnDay[] = new Button[7];
	private boolean daySelect[] = {false,true,false,true,false,true,false};
	private final int  DAY_BACKGROUND[] = {R.drawable.day0,R.drawable.day1,R.drawable.day2,R.drawable.day3,R.drawable.day4,R.drawable.day5,R.drawable.day6};
	private final int  DAY_BACKGROUND_SELECTED[] = {R.drawable.day0_select,R.drawable.day1_select,R.drawable.day2_select,R.drawable.day3_select,R.drawable.day4_select
			,R.drawable.day5_select,R.drawable.day6_select};
	private int daySelectNumber = 3;
	
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
			daySelectNumber++;
			mBtnDay[day_index].setBackgroundResource(DAY_BACKGROUND[day_index]);
		}
		else
		{
			daySelect[day_index] = true;
			daySelectNumber--;
			mBtnDay[day_index].setBackgroundResource(DAY_BACKGROUND_SELECTED[day_index]);
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invited);
		initView();
		initEvent();
	}

	private void initView() {
		mEtCode = (EditText) findViewById(R.id.et_invited_code);
		mEtEmail = (EditText) findViewById(R.id.et_invited_email);
		mEtPwd = (EditText) findViewById(R.id.et_invited_password);
		mEtPwdRepeat = (EditText) findViewById(R.id.et_invited_password_repeat);
		mBtnConnect = (Button) findViewById(R.id.btn_invited_connecting);
		mCheckService = findViewById(R.id.check_box_service);
		mCheckPrivacy = findViewById(R.id.check_box_privacy);
		
		mBtnTerms = (Button) findViewById(R.id.btn_terms);
		mBtnPrivacy = (Button) findViewById(R.id.btn_privacy);
		
		mEtAlarm = (EditText) findViewById(R.id.et_signup_time);
		mEtAlarm.setFocusable(false);
		for( int i= 0 ; i < 7 ; i++ )
			mBtnDay[i] = (Button) findViewById(R.id.btn_signup_0 + i);
	}

	private void initEvent() {
		for( int i= 0 ; i < 7 ; i++ )
			mBtnDay[i].setOnClickListener(dayClickListener);
		
		mEtAlarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				TimePickerDialog timePicker = new TimePickerDialog(InvitedActivity.this, new OnTimeSetListener() {
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
		
		mBtnPrivacy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogMaker dm = new DialogMaker(InvitedActivity.this, R.layout.dialog_webview, 0.95, 0.9);
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
				DialogMaker dm = new DialogMaker(InvitedActivity.this, R.layout.dialog_webview, 0.95, 0.9);
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
		mBtnConnect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!mToggleService){
					ToastManager.getInstance().showToast(InvitedActivity.this, "가입을 위해선 서비스 이용약관에 동의하셔야 합니다.", 1000);
					return;
				}
				if(!mTogglePrivacy){
					ToastManager.getInstance().showToast(InvitedActivity.this, "가입을 위해선 개인정보보호정책에 동의하셔야 합니다.", 1000);
					return;
				}
				if(mEtAlarm.getText().toString().equals("")){
					ToastManager.getInstance().showToast(InvitedActivity.this, "알람을 받으실 시간을 입력해주세요.", 1000);
					return;
				}
				if(daySelectNumber != 3){
					ToastManager.getInstance().showToast(InvitedActivity.this, "알람을 받으실 요일은 반드시 3개여야 합니다.", 1000);
					return;
				}
				if(!mEtPwd.getText().toString().equals(mEtPwdRepeat.getText().toString())){
					ToastManager.getInstance().showToast(InvitedActivity.this, "비밀번호가 올바르지 않습니다.", 1000);
					return;
				}
				RequestThread rq = new SignUpRequest(mEtEmail.getText().toString(),mEtPwd.getText().toString(),
						mEtCode.getText().toString(),mCurrentSetDay,mEtAlarm.getText().toString());
				rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
					
					@Override
					public void onFinish(String result, JSONObject jsonObject) {
						LogUtil.e("Invited connect result", result);
						finish();
					}
					
					@Override
					public void onExceptionError(Exception e) {
						
					}
				});
				rq.start();
			}
		});
	}
}
