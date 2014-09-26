package com.daddys40;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.SignInRequest;
import com.daddys40.util.LogUtil;
import com.daddys40.util.ProgressDialogManager;
import com.daddys40.util.ToastManager;
import com.daddys40.util.UserData;

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
		mBtnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ProgressDialogManager.showProgressDialog(LoginActivity.this, "접속중", "이메일과 비밀번호를 확인중입니다..");
				ToastManager.getInstance();
				SignInRequest rq = new SignInRequest(mEtEmail.getText().toString(), 
						mEtPassword.getText().toString());
				rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
					
					@Override
					public void onFinish(String result, JSONObject jsonObject) {
						ProgressDialogManager.dismiss();
						LogUtil.e("Login Result", result);
						if("".equals(jsonObject.get("errors")) || jsonObject.get("errors") != null){
//							ToastManager.getInstance().showToast(LoginActivity.this, "아이디 혹은 비밀번호가 올바르지 않습니다.", 2000);
							ToastManager.getInstance().showToast(LoginActivity.this, jsonObject.get("errors") + "", 2000);
						}
						else{
							ToastManager.getInstance().showToast(LoginActivity.this, "로그인 성공", 2000);
							UserData.init(LoginActivity.this);
							LogUtil.e("token", ((JSONObject) jsonObject.get("current_user")).get("authentication_token")+"");
							UserData.getInstance().setToken(((JSONObject) jsonObject.get("current_user")).get("authentication_token")+"");
						}
					}
					@Override
					public void onExceptionError(Exception e) {
						ProgressDialogManager.dismiss();
						LogUtil.e("Login Result", "fail");
						ToastManager.getInstance().showToast(LoginActivity.this, "네트워크 연결 상태를 확인해주세요.", 2000);
					}
				});
				rq.start();
			}
		});
	}
}
