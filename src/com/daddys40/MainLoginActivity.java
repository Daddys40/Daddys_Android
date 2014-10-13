package com.daddys40;

import org.json.simple.JSONObject;

import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.RequestThread;
import com.daddys40.network.SignInRequest;
import com.daddys40.util.InstantUserData;
import com.daddys40.util.LogUtil;
import com.daddys40.util.ToastManager;
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
		ToastManager.getInstance();
		if(UserData.getInstance().getToken() != null){
			InstantUserData.getInstance().setToken(UserData.getInstance().getToken());
			RequestThread rq = new SignInRequest(UserData.getInstance().getToken());
			rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
				@Override
				public void onFinish(String result, JSONObject jsonObject) {
					//받아온 유저 데이터 저장
					LogUtil.e("Auto Login Result", result);
					ToastManager.getInstance().showToast(MainLoginActivity.this, "자동 로그인 성공", 2000);
					startActivity(new Intent(MainLoginActivity.this, FeedActivity.class));
					finish();
				}
				@Override
				public void onExceptionError(Exception e) {
					
				}
			});
			rq.start();
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
				startActivityForResult(new Intent(MainLoginActivity.this, LoginActivity.class),0);
			}
		});
		mBtnRegistration.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(MainLoginActivity.this, SignupActivity.class),0);
			}
		});
		mBtnDoConnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(MainLoginActivity.this, InvitedActivity.class),0);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 1)
			finish();
	}
}
