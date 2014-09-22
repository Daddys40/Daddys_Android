package com.daddys40;

import org.json.simple.JSONObject;

import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.VersionCheckRequest;
import com.daddys40.util.DefineConst;
import com.daddys40.util.LogUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.Toast;

public class LogoLodingActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		// Version Check
		VersionCheckRequest request = new VersionCheckRequest();
		request.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
			@Override
			public void onFinish(String result, JSONObject jsonObject) {
				// {latest_version: "1.0.0",
				// update_message: "",
				// needs_force_update: false,}
				LogUtil.e("Version check result", result);
				LogUtil.e("Version : ",  jsonObject.get("latest_version") + "");
				LogUtil.e("Update Message : ", jsonObject.get("update_message") + "");
				String force_update = jsonObject.get("needs_force_update")+ "";
				LogUtil.e("Version check result", force_update);
				if("true".equals(force_update)){
					//Dialog로 수정. Update Message 표시, 업데이트 선택권ㅏ
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(DefineConst.NETWORK_URL_STORE));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}else{
					startHandler.sendEmptyMessageDelayed(0, 1000);
				}
			}
			@Override
			public void onExceptionError(Exception e) {
				toastHandler.sendEmptyMessage(0);
				finish();
			}
		});
		request.start();
	}

	private Handler startHandler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			startActivity(new Intent(LogoLodingActivity.this, MainLoginActivity.class));
			finish();
			return false;
		}
	});
	private Handler toastHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			Toast.makeText(LogoLodingActivity.this, R.string.exeption_network, 1500).show();
			return false;
		}
	});
}
