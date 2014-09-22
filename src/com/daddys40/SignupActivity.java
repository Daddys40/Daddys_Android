package com.daddys40;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends Activity {
	
	private EditText mEtEmail;
	private EditText mEtPwd;
	private EditText mEtName;
	private EditText mEtDday;
	private EditText mEtAge;
	private EditText mEtHeight;
	private EditText mEtAlarm;
	
	private Button mBtnNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		initView();
		initEvent();
	}

	private void initView() {
		mBtnNext = (Button) findViewById(R.id.btn_signup_next);
	}

	private void initEvent() {
		mBtnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SignupActivity.this,InvitingActivity.class));
			}
		});
	}
}
