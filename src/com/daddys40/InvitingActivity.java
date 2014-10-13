package com.daddys40;

import org.json.simple.JSONObject;

import com.daddys40.network.InvitingRequest;
import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.util.InstantUserData;
import com.daddys40.util.LogUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InvitingActivity extends Activity {
	private EditText mEtMyNum;
	private EditText mEtYourNum;
	private Button mBtnSend;
	private Button mBtnSkip;
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
	private void initView(){
		mEtMyNum = (EditText) findViewById(R.id.et_inviting_mynum);
		mEtYourNum = (EditText) findViewById(R.id.et_inviting_yournum);
		mBtnSend = (Button) findViewById(R.id.btn_inviting_send);
		mBtnSkip = (Button) findViewById(R.id.btn_inviting_skip);
	}
	private void initEvent(){
		mBtnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InvitingRequest rq = new InvitingRequest(InstantUserData.getInstance().getToken());
				rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
					
					@Override
					public void onFinish(String result, JSONObject jsonObject) {
						LogUtil.e("Inviting Request Result", result);
						LogUtil.e("Inviting code : ", jsonObject.get("invitation_code") + "");
						SmsManager smsManager = SmsManager.getDefault();
						smsManager.sendTextMessage(mEtYourNum.getText().toString(), null, 
								"초대코드 : " + jsonObject.get("invitation_code"), null, null);
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
				if(mFromFeed)
					finish();
				else
					startFeedActivity();
			}
		});
	}
	private void startFeedActivity(){
		if(!mFromFeed)
			startActivity(new Intent(InvitingActivity.this, FeedActivity.class));
		finish();
	}
}
