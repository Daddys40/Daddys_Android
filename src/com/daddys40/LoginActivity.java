package com.daddys40;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private EditText mEtEmail;
	private EditText mEtPassword;
	private Button mBtnLogin;

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
	}

	private void initEvent() {

	}
}
