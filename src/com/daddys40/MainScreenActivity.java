package com.daddys40;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class MainScreenActivity extends Activity{
	
	private Button mBtnLogin;
	private Button mBtnReg;
	private Button mBtnConnect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		initView();
		initEvent();
	}
	private void initView(){
		mBtnLogin = (Button) findViewById(R.id.btn_main_screen_login);
		mBtnReg = (Button) findViewById(R.id.btn_main_screen_registration);
		mBtnConnect = (Button) findViewById(R.id.btn_main_screen_connect);
	}
	private void initEvent(){
		
	}
}
