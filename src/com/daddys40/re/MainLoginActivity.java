package com.daddys40.re;

import java.util.Calendar;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.daddys40.alarm.EnrollAlarm;
import com.daddys40.data.InstantUserData;
import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.RequestThread;
import com.daddys40.network.SignInRequest;
import com.daddys40.util.LogUtil;
import com.daddys40.util.ToastManager;
import com.daddys40.util.UserData;

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
					
					UserData.init(MainLoginActivity.this);
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
					EnrollAlarm.getInstance().setAlarm(MainLoginActivity.this);
					if(!(((JSONObject) jsonObject.get("current_user")).get("partner") + "").equals("null")){
						InstantUserData.getInstance().setConnected(true);
						InstantUserData.getInstance().setPartnerName((((JSONObject)((JSONObject) jsonObject.get("current_user")).get("partner"))).get("name") + "");
					}
					
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
