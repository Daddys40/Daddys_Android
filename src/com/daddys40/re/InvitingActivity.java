package com.daddys40.re;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.daddys40.alarm.NotiDialogActivity;
import com.daddys40.data.InstantUserData;
import com.daddys40.network.InvitingRequest;
import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.util.LogUtil;
import com.daddys40.util.MyTagManager;
import com.daddys40.util.ToastManager;
import com.kakao.AppActionBuilder;
import com.kakao.AppActionInfoBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoTalkLinkMessageBuilder;

public class InvitingActivity extends Activity {
	// private EditText mEtMyNum;
	private EditText mEtYourNum;

	private Button mBtnSend;
	private Button mBtnSkip;
	
	private Button mBtnMMS;
	private Button mBtnKakao;
	
	private boolean mMMS = true;
	private boolean mFromFeed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inviting);
		Intent intent = getIntent();
		mFromFeed = intent.getBooleanExtra("FROM_FEED", false);
		initView();
		initEvent();
	}

	private void initView() {
		// mEtMyNum = (EditText) findViewById(R.id.et_inviting_mynum);
		mEtYourNum = (EditText) findViewById(R.id.et_inviting_yournum);
		mBtnSend = (Button) findViewById(R.id.btn_inviting_send);
		mBtnSkip = (Button) findViewById(R.id.btn_inviting_skip);
		mBtnMMS = (Button) findViewById(R.id.btn_invite_mms);
		mBtnKakao = (Button) findViewById(R.id.btn_invite_kakao);
	}

	private void initEvent() {
		mBtnMMS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mBtnMMS.setBackgroundColor(0xffC89996);
				mBtnKakao.setBackgroundResource(R.drawable.blank);
				((RelativeLayout) findViewById(R.id.lay_invite_number)).setVisibility(View.VISIBLE);
				mMMS = true;
			}
		});
		mBtnKakao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mBtnKakao.setBackgroundColor(0xffC89996);
				mBtnMMS.setBackgroundResource(R.drawable.blank);
				((RelativeLayout) findViewById(R.id.lay_invite_number)).setVisibility(View.GONE);
				mMMS = false;
			}
		});
		
		mBtnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastManager.getInstance().init();
				if(mEtYourNum.getText().toString().equals("") && mMMS){
					ToastManager.getInstance().showToast(InvitingActivity.this, "번호를 입력해주세요", 1000);
					return;
				}
				InvitingRequest rq = new InvitingRequest(InstantUserData.getInstance().getToken());
				rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {

					@Override
					public void onFinish(String result, JSONObject jsonObject) {
						LogUtil.e("Inviting Request Result", result);
						LogUtil.e("Inviting code : ", jsonObject.get("invitation_code") + "");
						String msg = getResources().getString(R.string.send_invite_msg) + "초대코드 : "
								+ jsonObject.get("invitation_code");
						if(mMMS){
							MyTagManager.getInstance(InvitingActivity.this).sendEvent("N_Send inviting msg to mms");
							SmsManager smsManager = SmsManager.getDefault();
							ArrayList<String> parts = smsManager.divideMessage(msg);
							LogUtil.e("msg content", msg + msg.length());
							smsManager.sendMultipartTextMessage(mEtYourNum.getText().toString(), null, parts,
									null, null);
	//						smsManager.sendTextMessage(
	//								mEtYourNum.getText().toString(),null,getResources().getString(R.string.send_invite_msg) + "초대코드 : "
	//										+ jsonObject.get("invitation_code"), null, null);
							ToastManager.getInstance().showToast(InvitingActivity.this,
									"초대코드가 발송되었습니다. \n초대코드 :" + jsonObject.get("invitation_code"), 3000);
						}
						else{
							
							try {
								MyTagManager.getInstance(InvitingActivity.this).sendEvent("N_Send inviting msg to kakao");
								final KakaoLink kakaoLink = KakaoLink.getKakaoLink(InvitingActivity.this);
								final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink
										.createKakaoTalkLinkMessageBuilder();
								final String linkContents = kakaoTalkLinkMessageBuilder
										.addText(msg).build();
								kakaoLink.sendMessage(linkContents, InvitingActivity.this);
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
						startFeedActivity();
					}

					@Override
					public void onExceptionError(Exception e) {

					}
				});
				rq.start();
			}
		});
		mBtnSkip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyTagManager.getInstance(InvitingActivity.this).sendEvent("N_Skip inviting");
				if (mFromFeed)
					finish();
				else
					startFeedActivity();
			}
		});
	}

	private void startFeedActivity() {
		// if(!mFromFeed)
		// startActivity(new Intent(InvitingActivity.this, FeedActivity.class));
		finish();
	}
}
