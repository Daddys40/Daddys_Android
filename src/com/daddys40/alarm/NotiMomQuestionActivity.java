package com.daddys40.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daddys40.re.LogoLodingActivity;
import com.daddys40.re.R;
import com.daddys40.util.DefineConst;
import com.daddys40.util.LogUtil;
import com.daddys40.util.MySystem;
import com.daddys40.util.MyTagManager;
import com.daddys40.util.ToastManager;
import com.daddys40.util.UserData;

public class NotiMomQuestionActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UserData.getInstance().init(NotiMomQuestionActivity.this);
		
		Window win = getWindow();
		win.requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.noti_question_mom);
		
		MySystem.init(NotiMomQuestionActivity.this);
		MySystem.getInstance().Vibrate(1500L);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //잠금 화면 위에 뜨게 하기.
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); //화면 깨우기
		((WebView) findViewById(R.id.webview_noti_mom)).getSettings()
				.setJavaScriptEnabled(true);
		((WebView) findViewById(R.id.webview_noti_mom))
		.loadUrl(DefineConst.NETWORK_URL_QUESTION_START+"?authentication_token="+UserData.getInstance().getToken()+"&week="
		+ UserData.getInstance().currentWeek() +"&count=" + UserData.getInstance().getAnswerCount());
		ToastManager.getInstance().init();
		
		((WebView) findViewById(R.id.webview_noti_mom))
				.setWebViewClient(new WebViewClient() {
					@Override
					public void onPageFinished(WebView view, String url) {
						super.onPageFinished(view, url);
						if(url.equals(DefineConst.NETWORK_URL_QUESTION_END)){
							LogUtil.e("change URL", url);
							MyTagManager.getInstance(NotiMomQuestionActivity.this).sendEvent("N_Choose complete on push dialog_female");
							startActivity(new Intent(NotiMomQuestionActivity.this, LogoLodingActivity.class));
							ToastManager.getInstance().showToast(NotiMomQuestionActivity.this, "입력되었습니다. 답변을 확인해보세요.", 1000);
							finish();
						}
					}
				});
	}
	@Override
	protected void onStart() {
		super.onStart();
		MyTagManager.getInstance(this).send("appview", "N_Push dialog_female");
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		MyTagManager.getInstance(NotiMomQuestionActivity.this).sendEvent("N_Back button on push dialog_female");
	}
}
