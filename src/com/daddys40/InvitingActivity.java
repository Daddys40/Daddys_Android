package com.daddys40;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class InvitingActivity extends Activity {
	private EditText mEtMyNum;
	private EditText mEtYourNum;
	private Button mBtnSend;
	private Button mBtnSkip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inviting);
		initView();
		initEvent();
	}
	private void initView(){
		mEtMyNum = (EditText) findViewById(R.id.et_inviting_mynum);
		mEtYourNum = (EditText) findViewById(R.id.et_inviting_yournum);
		mBtnSend = (Button) findViewById(R.id.btn_inviting_send);
		mBtnSkip = (Button) findViewById(R.id.btn_inviting_skip);
	}
	private void initEvent(){
		
	}
}
