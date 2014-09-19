package com.daddys40;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class InvitedActivity extends Activity {
	private EditText mEtCode;
	private EditText mEtEmail;
	private EditText mEtPwd;
	private Button mBtnFind;
	private Button mBtnConnect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invited);
		initView();
		initEvent();
	}
	private void initView(){
		mEtCode = (EditText) findViewById(R.id.et_invited_code);
		mEtEmail = (EditText) findViewById(R.id.et_invited_email);
		mEtPwd = (EditText) findViewById(R.id.et_invited_password);
		mBtnFind = (Button) findViewById(R.id.btn_invited_find);
		mBtnConnect = (Button) findViewById(R.id.btn_invited_connecting);
	}
	private void initEvent(){
		
	}
}
