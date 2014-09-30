package com.daddys40;

import java.util.Calendar;

import org.json.simple.JSONObject;

import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.SignUpRequest;
import com.daddys40.util.InstantUserData;
import com.daddys40.util.LogUtil;
import com.daddys40.util.ProgressDialogManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class SignupActivity extends Activity {

	private EditText mEtEmail;
	private EditText mEtPwd;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		initView();
		initEvent();
	}

	private void initView() {
		mBtnNext = (Button) findViewById(R.id.btn_signup_next);

		mEtEmail = (EditText) findViewById(R.id.et_signup_email);
		mEtPwd = (EditText) findViewById(R.id.et_signup_password);
		mEtName = (EditText) findViewById(R.id.et_signup_name);
		mEtDday = (EditText) findViewById(R.id.et_signup_dday);
		mEtAge = (EditText) findViewById(R.id.et_signup_age);
		mEtHeight = (EditText) findViewById(R.id.et_signup_height);
		mEtWeight = (EditText) findViewById(R.id.et_signup_weight);
		
		mBtnFather = (Button) findViewById(R.id.btn_signup_father);
		mBtnMother = (Button) findViewById(R.id.btn_signup_mother);
		
		mEtDday.setFocusable(false);
	}

	private void initEvent() {
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
				ProgressDialogManager.showProgressDialog(SignupActivity.this,  "접속중", "회원가입중입니다...");
				SignUpRequest rq = new SignUpRequest(mEtEmail.getText().toString(), mEtPwd.getText().toString(),
						mEtName.getText().toString(), gender, mEtName.getText().toString(),
						mEtAge.getText().toString(), mEtHeight.getText().toString(), mEtWeight.getText().toString(),
						mEtDday.getText().toString());
				rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {

					@Override
					public void onFinish(String result, JSONObject jsonObject) {
						ProgressDialogManager.dismiss();
						LogUtil.e("Result", result);
						LogUtil.e("Email", ((JSONObject) jsonObject.get("current_user")).get("email") + "");
						LogUtil.e("token", ((JSONObject)jsonObject.get("current_user")).get("authentication_token") + "");
						InstantUserData.getInstance().setToken(((JSONObject)jsonObject.get("current_user")).get("authentication_token") + "");
						startActivity(new Intent(SignupActivity.this,InvitingActivity.class));
						finish();
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
	}
}
