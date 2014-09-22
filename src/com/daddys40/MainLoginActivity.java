package com.daddys40;

import org.json.simple.JSONObject;

import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.SignInRequest;
import com.daddys40.util.UserData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainLoginActivity extends Activity {
	private Button mBtnLogin;
	private Button mBtnRegistration;
	private Button mBtnDoConnect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_main);
		UserData.init(this);
		initView();
		initEvent();
		checkToken();
	}
	private void checkToken(){
		if(UserData.getInstance().getToken() != null){
			SignInRequest rq = new SignInRequest(UserData.getInstance().getToken());
			rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
				@Override
				public void onFinish(String result, JSONObject jsonObject) {
					//받아온 유저 데이터 저장
				}
				@Override
				public void onExceptionError(Exception e) {
					
				}
			});
		}
	}
	private void initView(){
		mBtnLogin = (Button) findViewById(R.id.btn_main_login);
		mBtnRegistration = (Button) findViewById(R.id.btn_main_registration);
		mBtnDoConnect = (Button) findViewById(R.id.btn_main_connect);
	}
	private void initEvent(){
		mBtnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainLoginActivity.this, LoginActivity.class));
			}
		});
		mBtnRegistration.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainLoginActivity.this, SignupActivity.class));
			}
		});
		mBtnDoConnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainLoginActivity.this, InvitedActivity.class));
			}
		});
	}
}
