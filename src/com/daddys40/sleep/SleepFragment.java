//package com.daddys40.sleep;
//
//import android.app.Dialog;
//import android.app.Service;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.daddys40.R;
//import com.daddys40.sleep.SleepService.LocalBinder;
//import com.daddys40.sleep.SleepService.OnMediaEndListener;
//import com.daddys40.util.DialogMaker;
//import com.daddys40.util.LogUtil;
//import com.daddys40.util.MyTagManager;
//import com.daddys40.util.UserData;
//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.MapBuilder;
//
//public class SleepFragment extends Fragment {
//	private final int STATE_RAIN = 0;
//	private final int STATE_MOUNT = 1;
//	private final int STATE_SEA = 2;
//
//	private View mView;
//	private int mCurrentState = STATE_RAIN;
//
//	private int STATE_BACKGROUND[] = { R.drawable.sleep_bg_rain, R.drawable.sleep_bg_mount, R.drawable.sleep_bg_sea };
//	private int STATE_CENTER[] = { R.drawable.sleep_bgicon_rain, R.drawable.sleep_bgicon_mount,
//			R.drawable.sleep_bgicon_sea };
//
//	private Button mBtnLeft;
//	private Button mBtnRight;
//	private Button mBtnAlarm;
//	
//	private SleepService mService = null;    // 연결 타입 서비스
//	public SleepFragment() {
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		
//		mView = inflater.inflate(R.layout.activity_sleep, container, false);
//		mView.setBackgroundColor(0xff555555);
//		initView();
//		initEvent();
//		
//		if(mService == null){
//			LogUtil.e("Service", "service not ready");
////			Intent service = new Intent("Sleep");
//			Intent service = new Intent(getActivity(), SleepService.class);
//			getActivity().bindService(service, mConnection, Context.BIND_AUTO_CREATE);
//		}
//		return mView;
//	}
//	@Override
//	public void setMenuVisibility(boolean menuVisible) {
//		super.setMenuVisibility(menuVisible);
//		MyTagManager.getInstance(getActivity()).send("appview", "SleepModeScreen");
//		if(menuVisible){
//			if(!UserData.getInstance().isSleepCoachOk()){
//				final DialogMaker dm = new DialogMaker(getActivity(), R.layout.dialog_sleep_coach,1,1);
//				dm.getDialog().findViewById(R.id.btn_sleep_coach_close).setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						UserData.getInstance().setSleepCoachOk();
//						dm.getDialog().dismiss();
//					}
//				});
//				dm.getDialog().show();
//			}
//		}
//	}
//	private void initView() {
//		mBtnLeft = (Button) mView.findViewById(R.id.btn_sleep_mode_left);
//		mBtnRight = (Button) mView.findViewById(R.id.btn_sleep_mode_right);
//		mBtnAlarm = (Button) mView.findViewById(R.id.btn_sleep_mode_alarm);
//	}
//
//	private void initEvent() {
//		mView.findViewById(R.id.lay_sleep_main).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				EasyTracker.getInstance(getActivity()).send(MapBuilder
////					      .createEvent("ui_action",     // Event category (required)
////				                   "button_press",  // Event action (required)
////				                   "tab sleep mode",   // Event label
////				                   null)            // Event value
////				      .build());
//				MyTagManager.getInstance(getActivity()).sendEvent("Tab Sleep Mode");
//				if(mService.isPlaying()){
//					mService.stopSound();
//				}
//				else{
//					mService.startSound(mCurrentState);
//					mView.setBackgroundResource(STATE_BACKGROUND[mCurrentState]);
//				}
//			}
//		});
//		mBtnLeft.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (mCurrentState == STATE_RAIN)
//					mCurrentState = STATE_MOUNT;
//				else if (mCurrentState == STATE_MOUNT)
//					mCurrentState = STATE_RAIN;
//				else if (mCurrentState == STATE_SEA)
//					mCurrentState = STATE_MOUNT;
//				updateView();
//				mService.startSound(mCurrentState);
//				mView.setBackgroundResource(STATE_BACKGROUND[mCurrentState]);
//			}
//		});
//		mBtnRight.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (mCurrentState == STATE_RAIN)
//					mCurrentState = STATE_SEA;
//				else if (mCurrentState == STATE_MOUNT)
//					mCurrentState = STATE_SEA;
//				else if (mCurrentState == STATE_SEA)
//					mCurrentState = STATE_RAIN;
//				updateView();
//				mService.startSound(mCurrentState);
//				mView.setBackgroundResource(STATE_BACKGROUND[mCurrentState]);
//			}
//		});
//		mBtnAlarm.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				DialogMaker dm = new DialogMaker(getActivity(), R.layout.dialog_sleep_alarm);
//				final Dialog dlg = dm.getDialog();
//				dlg.findViewById(R.id.btn_sleep_mode_alarm_30).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						mService.startSound(mCurrentState, 30);
//						mView.setBackgroundResource(STATE_BACKGROUND[mCurrentState]);
//						Toast.makeText(getActivity().getApplicationContext(), "30분후 종료합니다.", 1000).show();
//						dlg.dismiss();
//					}
//				});
//				dlg.findViewById(R.id.btn_sleep_mode_alarm_60).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						mService.startSound(mCurrentState, 60);
//						mView.setBackgroundResource(STATE_BACKGROUND[mCurrentState]);
//						Toast.makeText(getActivity().getApplicationContext(), "60분후 종료합니다.", 1000).show();
//						dlg.dismiss();
//					}
//				});
//				dlg.findViewById(R.id.btn_sleep_mode_alarm_90).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						mService.startSound(mCurrentState, 90);
//						mView.setBackgroundResource(STATE_BACKGROUND[mCurrentState]);
//						Toast.makeText(getActivity().getApplicationContext(), "90분후 종료합니다.", 1000).show();
//						dlg.dismiss();
//					}
//				});
//				dlg.show();
//			}
//		});
//	}
////	private void startSound(){
////		
////	}
//	private void updateView() {
//		mView.setBackgroundResource(STATE_BACKGROUND[mCurrentState]);
//		mView.findViewById(R.id.iv_sleep_center_bg).setBackgroundResource(STATE_CENTER[mCurrentState]);
//		if (mCurrentState == STATE_RAIN) {
//			mView.findViewById(R.id.btn_sleep_mode_left).setBackgroundResource(R.drawable.sel_btn_mount);
//			mView.findViewById(R.id.btn_sleep_mode_right).setBackgroundResource(R.drawable.sel_btn_sea);
//		}
//		else if (mCurrentState == STATE_MOUNT) {
//			mView.findViewById(R.id.btn_sleep_mode_left).setBackgroundResource(R.drawable.sel_btn_rain);
//			mView.findViewById(R.id.btn_sleep_mode_right).setBackgroundResource(R.drawable.sel_btn_sea);
//		}
//		else if (mCurrentState == STATE_SEA) {
//			mView.findViewById(R.id.btn_sleep_mode_left).setBackgroundResource(R.drawable.sel_btn_mount);
//			mView.findViewById(R.id.btn_sleep_mode_right).setBackgroundResource(R.drawable.sel_btn_rain);
//		}
//	}
//	private ServiceConnection mConnection = new ServiceConnection(){
//		 
//		@Override
//		public void onServiceDisconnected(ComponentName arg0){
//		}
//
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			// TODO Auto-generated method stub
//			LogUtil.e("Service", "service  ready");
//			LocalBinder binder = (LocalBinder) service;
//			mService = binder.getService();
////			mService.startSound(mCurrentState);
//			mService.setOnMediaEndListener(new OnMediaEndListener() {
//				
//				@Override
//				public void onMediaEnd() {
//					mView.setBackgroundColor(0xff555555);
//				}
//			});
//		}
//		};
//
//}
