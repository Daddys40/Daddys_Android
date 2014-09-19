package com.daddys40.appintro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.daddys40.R;
import com.daddys40.util.MyTagManager;
import com.daddys40.util.UserData;
/**
 * 다른 앱 소개 페이지를 표현하는 Fragment
 * @author Kim
 *
 */
public class AppIntroFragment extends Fragment {
	private final String BACK_COLOR[] = { "ddd9d0", "ddd9d0", "ddd9d0", "fee75f", "965ba2", "84cfcb", "ef5959",
			"f98d43", "b3babe", "ffb9a9", "ffb9a9", "84cfcb", "85cc9e", "85cc9e", "e591c3", "bfddf4",
			"257c74", "c7da3e", "0b4f6d", "afaaa", "ed2d8c"};
	private final int ICON_ID[] = { R.drawable.illust_1,R.drawable.illust_1,R.drawable.illust_1, R.drawable.illust_2,  R.drawable.illust_3,
			R.drawable.illust_4, R.drawable.illust_5, R.drawable.illust_6, R.drawable.illust_7, R.drawable.illust_8,
			R.drawable.illust_8, R.drawable.illust_9, R.drawable.illust_10, R.drawable.illust_10, R.drawable.illust_11,
			R.drawable.illust_12, R.drawable.illust_13, R.drawable.illust_14, R.drawable.illust_15,
			R.drawable.illust_16, R.drawable.illust_17};
	private View mView;
	private WebView mMainWebView;
	private ProgressBar mProgressBar;
	private int mSetIndex = -1;
	
	private boolean finishToggle = false;

	public AppIntroFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//
		mView = inflater.inflate(R.layout.activity_app_intro, container, false);
		// mView.setBackgroundColor(0xff555555);
		 initView();
		 initEvent();
		 refreshWebUrl();
		return mView;
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if(mSetIndex != -1 && mSetIndex != UserData.getInstance().currentWeek() / 2)
			refreshWebUrl();
	}

	private void initView() {
		mMainWebView = (WebView) mView.findViewById(R.id.webview);
		mProgressBar = (ProgressBar) mView.findViewById(R.id.loading_webview);
	}
	private void refreshWebUrl(){
		mSetIndex = UserData.getInstance().currentWeek() / 2;
		String mFont = "";
		if(mSetIndex != 3 && mSetIndex != 17)
			mFont = "ffffff";
		mMainWebView.loadUrl("http://daddys40.woobi.co.kr/app_view.php?color=" + BACK_COLOR[mSetIndex] +"&font=" + mFont);
	}
	private void initEvent() {
		mMainWebView.getSettings().setJavaScriptEnabled(true);
		mMainWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				if(url.contains("https://play.google.com")){
					MyTagManager.getInstance(getActivity()).sendEvent("Go to another app through introducing page");
					mMainWebView.stopLoading();
					Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent1);
				}
				mProgressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				mProgressBar.setVisibility(View.GONE);
			}
		});
		mMainWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				mProgressBar.setProgress(newProgress);
			}
		});
	}
}
