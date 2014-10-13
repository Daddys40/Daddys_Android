package com.daddys40;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.RequestThread;
import com.daddys40.network.SignUpRequest;
import com.daddys40.util.DefineConst;
import com.daddys40.util.DialogMaker;
import com.daddys40.util.LogUtil;

public class InvitedActivity extends Activity {
	
	private EditText mEtCode;
	private EditText mEtEmail;
	private EditText mEtPwd;
	private Button mBtnConnect;
	
	private Button mBtnTerms;
	private Button mBtnPrivacy;

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
		mBtnConnect = (Button) findViewById(R.id.btn_invited_connecting);
		mCheckService = findViewById(R.id.check_box_service);
		mCheckPrivacy = findViewById(R.id.check_box_privacy);
		
		mBtnTerms = (Button) findViewById(R.id.btn_terms);
		mBtnPrivacy = (Button) findViewById(R.id.btn_privacy);
	}

	private void initEvent() {
		mBtnPrivacy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogMaker dm = new DialogMaker(InvitedActivity.this, R.layout.dialog_webview, 0.95, 0.9);
				final Dialog dlg = dm.getDialog();
				dlg.setCancelable(false);
				((Button) dlg.findViewById(R.id.btn_webview_terms)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dlg.dismiss();
					}
				});
				((WebView) dlg.findViewById(R.id.webview_dialog)).getSettings().setJavaScriptEnabled(true);
				((WebView) dlg.findViewById(R.id.webview_dialog)).loadUrl(DefineConst.NETWORK_URL_PRIVACY);
				dlg.show();
			}
		});
		mBtnTerms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogMaker dm = new DialogMaker(InvitedActivity.this, R.layout.dialog_webview, 0.95, 0.9);
				final Dialog dlg = dm.getDialog();
				dlg.setCancelable(false);
				((Button) dlg.findViewById(R.id.btn_webview_terms)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dlg.dismiss();
					}
				});
				((WebView) dlg.findViewById(R.id.webview_dialog)).getSettings().setJavaScriptEnabled(true);
				((WebView) dlg.findViewById(R.id.webview_dialog)).loadUrl(DefineConst.NETWORK_URL_TERMS);
				dlg.show();
			}
		});
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
				RequestThread rq = new SignUpRequest(mEtEmail.getText().toString(),mEtPwd.getText().toString(),mEtCode.getText().toString(),null,null);
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
