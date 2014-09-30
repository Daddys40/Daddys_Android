package com.daddys40;

import org.json.simple.JSONObject;

import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.RequestThread;
import com.daddys40.network.SignUpRequest;
import com.daddys40.util.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InvitedActivity extends Activity {
	
	private EditText mEtCode;
	private EditText mEtEmail;
	private EditText mEtPwd;
	private Button mBtnFind;
	private Button mBtnConnect;

	private View mCheckService;
	private boolean mToggleService = false;

	private View mCheckPrivacy;
	private boolean mTogglePrivacy = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invited);
		initView();
		initEvent();
	}

	private void initView() {
		mEtCode = (EditText) findViewById(R.id.et_invited_code);
		mEtEmail = (EditText) findViewById(R.id.et_invited_email);
		mEtPwd = (EditText) findViewById(R.id.et_invited_password);
		mBtnFind = (Button) findViewById(R.id.btn_invited_find);
		mBtnConnect = (Button) findViewById(R.id.btn_invited_connecting);
		mCheckService = findViewById(R.id.check_box_service);
		mCheckPrivacy = findViewById(R.id.check_box_privacy);
	}

	private void initEvent() {
		mCheckService.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mToggleService) {
					mToggleService = false;
					mCheckService.setBackgroundResource(R.drawable.box_icon);
				}
				else {
					mToggleService = true;
					mCheckService.setBackgroundResource(R.drawable.box_icon_checked);
				}

			}
		});
		mCheckPrivacy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mTogglePrivacy) {
					mTogglePrivacy = false;
					mCheckPrivacy.setBackgroundResource(R.drawable.box_icon);
				}
				else {
					mTogglePrivacy = true;
					mCheckPrivacy.setBackgroundResource(R.drawable.box_icon_checked);
				}

			}
		});
		mBtnConnect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RequestThread rq = new SignUpRequest(mEtEmail.getText().toString(),mEtPwd.getText().toString(),mEtCode.getText().toString());
				rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
					
					@Override
					public void onFinish(String result, JSONObject jsonObject) {
						LogUtil.e("Invited connect result", result);
					}
					
					@Override
					public void onExceptionError(Exception e) {
						
					}
				});
				rq.start();
			}
		});
	}
}
