package com.daddys40.re;

import java.util.Calendar;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.daddys40.alarm.EnrollAlarm;
import com.daddys40.data.InstantUserData;
import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.SignInRequest;
import com.daddys40.util.LogUtil;
import com.daddys40.util.MyTagManager;
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
	@Override
	protected void onStart() {
		super.onStart();
		MyTagManager.getInstance(this).send("appview", "N_Login Activity");
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
//							ToastManager.getInstance().showToast(LoginActivity.this, "로그인 성공", 2000);
							UserData.init(LoginActivity.this);
							LogUtil.e("token", ((JSONObject) jsonObject.get("current_user")).get("authentication_token")+"");
							
							UserData.getInstance().setToken(((JSONObject) jsonObject.get("current_user")).get("authentication_token")+"");
							UserData.getInstance().setAlarmDay(((JSONObject) jsonObject.get("current_user")).get("notifications_days") + "");
							UserData.getInstance().setAlarmTime( Integer.parseInt((((JSONObject) jsonObject.get("current_user")).get("notificate_at") + "").substring(0, 2)),
									Integer.parseInt((((JSONObject) jsonObject.get("current_user")).get("notificate_at") + "").substring(3, 5)));
							InstantUserData.getInstance().setToken(((JSONObject) jsonObject.get("current_user")).get("authentication_token")+"");
							
							UserData.getInstance().setSex(((JSONObject) jsonObject.get("current_user")).get("gender") + "");
							InstantUserData.getInstance().setGender(((JSONObject) jsonObject.get("current_user")).get("gender") + "");
							InstantUserData.getInstance().setAlarmDay(((JSONObject) jsonObject.get("current_user")).get("notifications_days") + "");
							InstantUserData.getInstance().setAlarmTime(((JSONObject) jsonObject.get("current_user")).get("notificate_at") + "");
							InstantUserData.getInstance().setAge(((JSONObject) jsonObject.get("current_user")).get("age") + "");
							InstantUserData.getInstance().setWeight(((JSONObject) jsonObject.get("current_user")).get("weight") + "");
							InstantUserData.getInstance().setHeight(((JSONObject) jsonObject.get("current_user")).get("height") + "");
							
							String strCal = ((JSONObject) jsonObject.get("current_user")).get("baby_due") + "";
							if(strCal.equals(""))
								strCal =((JSONObject)((JSONObject) jsonObject.get("current_user")).get("partner")).get("baby_due") + "";
							LogUtil.e("Baby Due", strCal);
							Calendar cal = Calendar.getInstance();
							cal.set(Calendar.YEAR, Integer.parseInt(strCal.substring(0, 4)));
							cal.set(Calendar.MONTH, Integer.parseInt(strCal.substring(5, 7)) - 1);
					
							cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strCal.substring(8,10)));
							LogUtil.e("Baby Due Cal", cal.toString());
							UserData.getInstance().setCalendar(cal);
							
							if(!(((JSONObject) jsonObject.get("current_user")).get("partner") + "").equals("null")){
								InstantUserData.getInstance().setConnected(true);
								InstantUserData.getInstance().setPartnerName((((JSONObject)((JSONObject) jsonObject.get("current_user")).get("partner"))).get("name") + "");
							}
							EnrollAlarm.getInstance().setAlarm(LoginActivity.this);
							
//							if((((JSONObject) jsonObject.get("current_user")).get("gender")+"").equals("male")){
//								myAlarmManager = new FatherAlarmManger();
//							}
//							else{
//								myAlarmManager = new MotherAlarmManager();
//							}
//							myAlarmManager.setAlarm(LoginActivity.this);
							
							startActivity(new Intent(LoginActivity.this, FeedActivity.class));
							setResult(1);
							finish();
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
