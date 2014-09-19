package com.daddys40;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private EditText mEtEmail;
	private EditText mEtPassword;
	private Button mBtnLogin;
	private Button mBtnFind;
	private Button mBtnReg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initEvent();
	}

	private void initView() {
		mEtEmail = (EditText) findViewById(R.id.et_login_email);
		mEtPassword = (EditText) findViewById(R.id.et_login_pwd);
		mBtnLogin = (Button) findViewById(R.id.btn_login_login);
		mBtnFind = (Button) findViewById(R.id.btn_login_find);
		mBtnReg = (Button) findViewById(R.id.btn_login_registration);
	}

	private void initEvent() {

	}
}
