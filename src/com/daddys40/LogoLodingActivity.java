package com.daddys40;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.RequestThread;
import com.daddys40.network.VersionCheckRequest;
import com.daddys40.util.DialogMaker;
import com.daddys40.util.LogUtil;
import com.daddys40.util.ToastManager;

public class LogoLodingActivity extends Activity {
	private Dialog updateDlg = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		// Version Check
		RequestThread request = new VersionCheckRequest();
		ToastManager.getInstance();
		DialogMaker dm = new DialogMaker(LogoLodingActivity.this, R.layout.dialog_update,0.8,0.35);
		updateDlg = dm.getDialog();
		request.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
			@Override
			public void onFinish(String result, final JSONObject jsonObject) {
				// {latest_version: "1.0.0",
				// update_message: "",
				// needs_force_update: false,}
				LogUtil.e("Version check result", result);
				LogUtil.e("Version : ",  jsonObject.get("current_version") + "");
				LogUtil.e("Update Message : ", jsonObject.get("update_message") + "");
				String force_update = jsonObject.get("needs_force_update")+ "";
				String	 need_update = jsonObject.get("needs_update")+ "";
				LogUtil.e("Version check result", force_update);
				updateDlg.setCancelable(false);
				((TextView) updateDlg.findViewById(R.id.tv_dlg_update_msg)).setText(jsonObject.get("update_message") + "");
				((Button) updateDlg.findViewById(R.id.btn_dlg_update_cancel)).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						updateDlg.dismiss();
						startHandler.sendEmptyMessageDelayed(0, 1000);
					}
				});
				((Button) updateDlg.findViewById(R.id.btn_dlg_update_ok)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(jsonObject.get("url")+""));
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish();
					}
				});
				
				if("true".equals(force_update)){
					//Dialog로 수정. Update Message 표시, 업데이트 선택권ㅏ
					((Button) updateDlg.findViewById(R.id.btn_dlg_update_cancel)).setVisibility(View.INVISIBLE);
					ToastManager.getInstance().showToast(LogoLodingActivity.this, "버전이 낮아 업데이트를 진행해야합니다.", 2000);
					dialogHandler.sendEmptyMessage(0);
				}
				else if("true".equals(need_update)){
					dialogHandler.sendEmptyMessage(0);
				}
				else{
					startHandler.sendEmptyMessageDelayed(0, 1000);
				}
			}
			@Override
			public void onExceptionError(Exception e) {
				ToastManager.getInstance().showToast(LogoLodingActivity.this, 
						getResources().getString(R.string.exeption_network), 2000);
//				toastHandler.sendEmptyMessage(0);
				finish();
			}
		});
		request.start();
	}
	private Handler dialogHandler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			updateDlg.show();
			return false;
		}
	});
	private Handler startHandler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			startActivity(new Intent(LogoLodingActivity.this, MainLoginActivity.class));
			finish();
			return false;
		}
	});
//	private Handler toastHandler = new Handler(new Callback() {
//
//		@Override
//		public boolean handleMessage(Message msg) {
//			Toast.makeText(LogoLodingActivity.this, R.string.exeption_network, 1500).show();
//			return false;
//		}
//	});
}
