//package com.daddys40.deprecated;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.daddys40.R;
//import com.daddys40.R.drawable;
//import com.daddys40.R.id;
//import com.daddys40.R.layout;
//import com.daddys40.R.string;
//import com.daddys40.alarm.EnrollAlarm;
//import com.daddys40.alarm.FatherAlarmManger;
//import com.daddys40.data.QnAData;
//import com.daddys40.data.QnADataContainer;
//import com.daddys40.util.DialogMaker;
//import com.daddys40.util.MyTagManager;
//import com.daddys40.util.UserData;
///**
// * 기본 메인화면 Fragment
// * @author Kim
// *
// */
//public class MainFragment extends Fragment{
//	private View v;
//	private final int ICON_ID[] = { R.drawable.illust_1,R.drawable.illust_1,R.drawable.illust_1, R.drawable.illust_2,  R.drawable.illust_3,
//			R.drawable.illust_4, R.drawable.illust_5, R.drawable.illust_6, R.drawable.illust_7, R.drawable.illust_8,
//			R.drawable.illust_8, R.drawable.illust_9, R.drawable.illust_10, R.drawable.illust_10, R.drawable.illust_11,
//			R.drawable.illust_12, R.drawable.illust_13, R.drawable.illust_14, R.drawable.illust_15,
//			R.drawable.illust_16, R.drawable.illust_17};
//
//	private final int BACK_COLOR[] = { 0xffddd9d0, 0xffddd9d0, 0xffddd9d0, 0xfffee75f, 0xff965ba2, 0xff84cfcb, 0xffef5959,
//			0xfff98d43, 0xffb3babe, 0xffffb9a9, 0xffffb9a9, 0xff84cfcb, 0xff85cc9e, 0xff85cc9e, 0xffe591c3, 0xffbfddf4,
//			0xff257c74, 0xffc7da3e, 0xff0b4f6d, 0xffafaaa, 0xffed2d8c};
//	private final int PREV_WEEK = 0;
//	private final int CURRENT_WEEK = 1;
//	private final int NEXT_WEEK = 2;
//	
//	
//	private Button mBtnSetting;
//	private Button mBtnShare;
//	private Button mBtnShowDlg;
//
//	private ImageView mIvIcon;
//	
//	private Dialog mResultDialog;
////	private int mCurrentState = CURRENT_WEEK;
//	private int mCurrentWeek = 0;
//	
//	public MainFragment() {
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		/*
//		 View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
//         TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
//         dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
//         return rootView;
//         
//         */
//		v   = inflater.inflate(R.layout.activity_result, container,false);
//		UserData.init(getActivity().getApplicationContext());
//		EnrollAlarm.getInstance().setAlarm(getActivity().getApplicationContext());
//		Intent intent = getActivity().getIntent();
//		initView();
//		initEvent();
//		QnADataContainer.getInstance().doDeserialization(getActivity());
//		if (intent.getBooleanExtra("noti", false)) {
//			// From Noti
//			showResultDialog();
//		}
//		else {
//			// General
////			if(!UserData.getInstance().isEventOk() && UserData.getInstance().getEventCount() > 2){
////				MyTagManager.getInstance(getActivity()).send("appview", "review Event dialog");
////				DialogMaker dm = new DialogMaker(getActivity(), R.layout.dialog_event, 1, 1);
////				final Dialog dlg = dm.getDialog();
////				dlg.setCancelable(false);
////				dlg.show();
////				UserData.getInstance().setEventOk();
////				((Button) dlg.findViewById(R.id.btn_event_close)).setOnClickListener(new OnClickListener() {
////					
////					@Override
////					public void onClick(View v) {
////						dlg.dismiss();
////					}
////				});
////				((RelativeLayout) dlg.findViewById(R.id.lay_event_go)).setOnClickListener(new OnClickListener() {
////					
////					@Override
////					public void onClick(View v) {
////						MyTagManager.getInstance(getActivity()).sendEvent("google store button for review");
////						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
////								.parse("https://play.google.com/store/apps/details?id=com.daddys40"));
////						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////						startActivity(intent);
////					}
////				});
////			}
////			else if(UserData.getInstance().getEventCount() == 0){
////				DialogMaker dm = new DialogMaker(getActivity(), R.layout.dialog_coach,1,1);
////				final Dialog dlg = dm.getDialog();
////				dlg.setCancelable(false);
////				dlg.findViewById(R.id.btn_coach_close).setOnClickListener(new OnClickListener() {
////					@Override
////					public void onClick(View arg0) {
////						UserData.getInstance().encEventCounter();
////						dlg.dismiss();
////					}
////				});
////				dlg.show();
////			}
////			else if(!UserData.getInstance().isEventOk())
////				UserData.getInstance().encEventCounter();
//		}
//		return v;
//         
//	}
//	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		initView();
//		EnrollAlarm.getInstance().setAlarm(getActivity().getApplicationContext());
//	}
//	
//	private void initView() {
//		// mMotherViewPager = (ViewPager) findViewById(R.id.lay_result_dialog_mom_pager);
//
//		mBtnShare = (Button) v.findViewById(R.id.btn_result_share);
//		mBtnSetting = (Button) v.findViewById(R.id.btn_result_setting);
//		mBtnShowDlg = (Button) v.findViewById(R.id.btn_result_show);
//
//		mIvIcon = (ImageView) v.findViewById(R.id.iv_result_icon);
//		mIvIcon.setImageResource(ICON_ID[UserData.getInstance().currentWeek() / 2]);
//		((RelativeLayout) v.findViewById(R.id.lay_result_background)).setBackgroundColor(BACK_COLOR[UserData
//				.getInstance().currentWeek() / 2]);
//		if (ICON_ID[UserData.getInstance().currentWeek() / 2] == R.drawable.illust_2
//				|| ICON_ID[UserData.getInstance().currentWeek() / 2] == R.drawable.illust_14) {
//			((ImageView) v.findViewById(R.id.iv_result_bottom)).setBackgroundResource(R.drawable.illust_allsame_black);
//			mBtnShare.setBackgroundResource(R.drawable.sel_btn_main_share_black);
//			mBtnSetting.setBackgroundResource(R.drawable.sel_btn_main_setting_black);
//			mBtnShowDlg.setBackgroundResource(R.drawable.sel_btn_main_detail_black);
//		}
//		else{
//			((ImageView) v.findViewById(R.id.iv_result_bottom)).setBackgroundResource(R.drawable.illust_allsame_white);
//			mBtnShare.setBackgroundResource(R.drawable.sel_btn_main_share);
//			mBtnSetting.setBackgroundResource(R.drawable.sel_btn_main_setting);
//			mBtnShowDlg.setBackgroundResource(R.drawable.sel_btn_main_detail);
//		}
//	}
//
//	private void initEvent() {
//		mBtnShare.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				MyTagManager.getInstance(getActivity()).sendEvent("Share Button");
//				DialogMaker dm = new DialogMaker(getActivity(), R.layout.dialog_share);
//				Dialog dlg = dm.getDialog();
//				dlg.show();
//				((Button) dlg.findViewById(R.id.btn_share_kakao)).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						try {
////							MyTagManager.getInstance(getActivity()).sendEvent("Share to kakao");
////							final KakaoLink kakaoLink = KakaoLink.getKakaoLink(getActivity());
////							final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink
////									.createKakaoTalkLinkMessageBuilder();
////							final String linkContents = kakaoTalkLinkMessageBuilder
////									.addText(getResources().getString(R.string.share_msg))
////									.addAppButton(
////											"앱으로 이동",
////											new AppActionBuilder()
////													.addActionInfo(
////															AppActionInfoBuilder.createAndroidActionInfoBuilder()
////																	.setExecuteParam("").setMarketParam("").build())
////													.addActionInfo(
////															AppActionInfoBuilder.createiOSActionInfoBuilder()
////																	.setExecuteParam("").build()).build()).build();
////							kakaoLink.sendMessage(linkContents, getActivity());
//						}
//						catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				});
//				((Button) dlg.findViewById(R.id.btn_share_band)).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						MyTagManager.getInstance(getActivity()).sendEvent("Share to band");
//						Intent intent = new Intent(Intent.ACTION_SEND);
//						intent.setType("text/plain");
//						intent.putExtra(Intent.EXTRA_SUBJECT, "Daddy\'s 40");
//						intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg)
//								+ "\n https://play.google.com/store/apps/details?id=com.daddys40");
//						intent.setPackage("com.nhn.android.band");
//						try {
//							startActivity(intent);
//						}
//						catch (Exception e) {
//							Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri
//									.parse("https://play.google.com/store/apps/details?id=com.nhn.android.band&hl=ko"));
//							intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							startActivity(intent1);
//						}
//					}
//				});
//			}
//		});
//		mBtnShowDlg.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				showResultDialog();
//			}
//		});
//		mBtnSetting.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				startActivityForResult(new Intent(getActivity(), SettingActivity.class), 0);
//			}
//		});
//	}
//
////	@Override
////	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////		super.onActivityResult(requestCode, resultCode, data);
////		initView();
////		if (UserData.getInstance().getSex() == UserData.SEX_MALE)
////			CustomAlarmManger.getInstance().setAlarm(getActivity().getApplicationContext());
////	}
//
//	private void showResultDialog() {
////		mCurrentState = CURRENT_WEEK;
//		DialogMaker dm = new DialogMaker(getActivity(), R.layout.dialog_result, 1.0, 1.0);
//		mResultDialog = dm.getDialog();
//		mResultDialog.show();
//		
//		// General Event
//		((RelativeLayout) mResultDialog.findViewById(R.id.lay_main_bottom)).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				MyTagManager.getInstance(getActivity()).sendEvent("click facebook link");
//				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/moms40"));
//				startActivity(intent);
//			}
//		});
//		((Button) mResultDialog.findViewById(R.id.btn_result_close)).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mResultDialog.dismiss();
//			}
//		});
//		
//		if (UserData.getInstance().getSex() == UserData.SEX_MALE) {
//			MyTagManager.getInstance(getActivity()).sendEvent("Show result_father");
//			ArrayList<QnAData> mArray = QnADataContainer.getInstance()
//					.getQnAArrayList(UserData.getInstance().currentWeek());
//			int index;
//			if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) < Calendar.WEDNESDAY)
//				index = 0;
//			else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) < Calendar.FRIDAY)
//				index = 1;
//			else
//				index = 2;
//			if (UserData.getInstance().currentWeek() == 41)
//				index = 0;
//			
//			((Button) mResultDialog.findViewById(R.id.btn_result_prev)).setVisibility(View.GONE);
//			((Button) mResultDialog.findViewById(R.id.btn_result_next)).setVisibility(View.GONE);
//			
//			((LinearLayout) mResultDialog.findViewById(R.id.lay_result_dialog_father)).setVisibility(View.VISIBLE);
//			((LinearLayout) mResultDialog.findViewById(R.id.lay_result_dialog_mom)).setVisibility(View.GONE);
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_title)).setText(mArray.get(index).getQuestion());
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_father_content)).setText(mArray.get(index)
//					.getAnswer());
//		}
//		else {
//			MyTagManager.getInstance(getActivity()).sendEvent("Show result_mother");
//			// new MotherDialogFragment().show(getSupportFragmentManager(), "");
//			setMotherResult();
//		}
//	}
//	private void setMotherResult(){
//		((ScrollView) mResultDialog.findViewById(R.id.scroll_result_dialog)).scrollTo(0, 0);
//		if(mCurrentWeek == 0)
//			mCurrentWeek = UserData.getInstance().currentWeek();
//		//주차 옮기는 기능 보류
//		if(mCurrentWeek <= 5){
//			((Button) mResultDialog.findViewById(R.id.btn_result_prev)).setVisibility(View.GONE);
//			((Button) mResultDialog.findViewById(R.id.btn_result_next)).setVisibility(View.VISIBLE);
//		}
//		else if(mCurrentWeek >= 41){
//			((Button) mResultDialog.findViewById(R.id.btn_result_next)).setVisibility(View.GONE);
//			((Button) mResultDialog.findViewById(R.id.btn_result_prev)).setVisibility(View.VISIBLE);
//		}
//		else{
//			((Button) mResultDialog.findViewById(R.id.btn_result_next)).setVisibility(View.VISIBLE);
//			((Button) mResultDialog.findViewById(R.id.btn_result_prev)).setVisibility(View.VISIBLE);
//		}
//		
//		ArrayList<QnAData> mArray = QnADataContainer.getInstance()
//				.getQnAArrayList(mCurrentWeek);
//		
//		((LinearLayout) mResultDialog.findViewById(R.id.lay_result_dialog_father)).setVisibility(View.GONE);
//		((LinearLayout) mResultDialog.findViewById(R.id.lay_result_dialog_mom)).setVisibility(View.VISIBLE);
//		((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_title)).setText("임신 "
//				+ mCurrentWeek + " 주의 예비엄마를 위한 조언");
//		
//		if (mCurrentWeek != 41) {
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_content2)).setVisibility(View.VISIBLE);
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_content3)).setVisibility(View.VISIBLE);
//			
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_title1)).setText("1. "
//					+ mArray.get(0).getTitle() + "\n");
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_title2)).setText("2. "
//					+ mArray.get(1).getTitle() + "\n");
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_title3)).setText("3. "
//					+ mArray.get(2).getTitle() + "\n");
//
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_content1)).setText(mArray.get(0)
//					.getAnswer());
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_content2)).setText(mArray.get(1)
//					.getAnswer());
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_content3)).setText(mArray.get(2)
//					.getAnswer());
//			if(mCurrentWeek == 5)
//				((Button) mResultDialog.findViewById(R.id.btn_result_prev)).setVisibility(View.GONE);
//		}
//		else {
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_title1)).setText(mArray.get(0).getTitle());
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_title2)).setVisibility(View.GONE);
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_title3)).setVisibility(View.GONE);
//
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_content1)).setText(mArray.get(0)
//					.getAnswer());
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_content2)).setVisibility(View.GONE);
//			((TextView) mResultDialog.findViewById(R.id.tv_result_dialog_mother_content3)).setVisibility(View.GONE);
//			
//			((Button) mResultDialog.findViewById(R.id.btn_result_next)).setVisibility(View.GONE);
//		}
//		
//		((Button) mResultDialog.findViewById(R.id.btn_result_next)).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(mCurrentWeek < 41){
//					mCurrentWeek++;
//					setMotherResult();
//				}
//			}
//		});
//		((Button) mResultDialog.findViewById(R.id.btn_result_prev)).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(mCurrentWeek > 5){
//					mCurrentWeek--;
//					setMotherResult();
//				}
//			}
//		});
//	}
//}
