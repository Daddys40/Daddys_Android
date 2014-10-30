package com.daddys40;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation.ActivityResult;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daddys40.alarm.NotiMomQuestionActivity;
import com.daddys40.data.FeedItem;
import com.daddys40.data.InstantUserData;
import com.daddys40.network.FeedRequest;
import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.RequestThread;
import com.daddys40.util.DefineConst;
import com.daddys40.util.DialogMaker;
import com.daddys40.util.LogUtil;
import com.daddys40.util.URLImageView;
import com.daddys40.util.UserData;

public class FeedActivity extends MyActivity {
	static private FeedActivity mMyActivity = null;
	
	private ListView mFeedListView;
	private ArrayList<FeedItem> mFeedItem = new ArrayList<FeedItem>();
	private FeedAdapter mFeedAdapter;
	
	private Button mBtnSetting;
	private Button mBtnShare;
	private Button mBtnInvitation;
	
	private boolean isResume = false;
	
	@Override
	protected void onResume() {
		if(isResume){
			startActivity(new Intent(FeedActivity.this,LogoLodingActivity.class));
			finish();
		}
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(mMyActivity == null)
			mMyActivity = this;
		else{
			mMyActivity.finish();
			mMyActivity = this;
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_main);
		setBackPressMessage("앱을 종료하시겠습니까?");
		initView();
		initEvent();
		RequestThread rq = new FeedRequest();
		rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
			
			@Override
			public void onFinish(String result, JSONObject jsonObject) {
				LogUtil.e("Feed Result", result);
				JSONParser parser = new JSONParser();
				JSONArray jsonArray = null;
				try {
					jsonArray = ((JSONArray)parser.parse(result));
				} catch (ParseException e) {
					e.printStackTrace();
				}
//				JSONArray jsonArray = ((JSONArray)jsonObject.get("data"));
				for(int i = 0 ; i < jsonArray.size(); i++){
					JSONObject jsonObjectTemp = (JSONObject) jsonArray.get(i);
//					jsonObjectTemp = (JSONObject) jsonObjectTemp.get("data"); //????
//					JSONArray jsonArrayTemp = (JSONArray) jsonObjectTemp.get("data");
					int width = 0;
					int height = 0;
					String img_src = "";
					if(((JSONArray)jsonObjectTemp.get("resources")) != null && ((JSONArray)jsonObjectTemp.get("resources")).size() > 0){
						img_src = ((JSONObject)((JSONArray)jsonObjectTemp.get("resources")).get(0)).get("image_url") + "";
						width = Integer.parseInt(((JSONObject)((JSONArray)jsonObjectTemp.get("resources")).get(0)).get("width") + "");
						height = Integer.parseInt(((JSONObject)((JSONArray)jsonObjectTemp.get("resources")).get(0)).get("height") + "");
					}
					FeedItem tempFeedItem = new FeedItem(jsonObjectTemp.get("week")+"", ""+jsonObjectTemp.get("content"), ""+jsonObjectTemp.get("title"));
					tempFeedItem.setImgSrc(img_src);
					tempFeedItem.setWidth(width);
					tempFeedItem.setHeight(height);
					tempFeedItem.setId(Integer.parseInt(jsonObjectTemp.get("id") + ""));
					if((jsonObjectTemp.get("readed") + "").equals("true"))
						tempFeedItem.setReaded(true);
					else
						tempFeedItem.setReaded(false);
					mFeedItem.add(0,tempFeedItem);
				}
				notiDataSetChanged.sendEmptyMessage(0);
			}
			@Override
			public void onExceptionError(Exception e) {
			}
		});
		rq.start();
	}
	private Handler notiDataSetChanged = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message arg0) {
			mFeedAdapter.notifyDataSetChanged();
			return false;
		}
	});
	private void initView(){
		mFeedListView = (ListView) findViewById(R.id.ListView_feed);
		mFeedListView.setDivider(null);
		mFeedAdapter = new FeedAdapter();
		mFeedListView.setAdapter(mFeedAdapter);
		mFeedAdapter.notifyDataSetChanged();
		
		mBtnSetting = (Button) findViewById(R.id.btn_feed_setting);
		mBtnShare = (Button) findViewById(R.id.btn_feed_share);
		mBtnInvitation = (Button)findViewById(R.id.btn_feed_invitation);
		
		if(InstantUserData.getInstance().isConnected())
			mBtnInvitation.setVisibility(View.GONE);
	}
	private void initEvent(){
		mBtnSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(FeedActivity.this, SettingActivity.class), 0);
			}
		});
		mBtnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DialogMaker dm = new DialogMaker(FeedActivity.this, R.layout.dialog_share);
				Dialog dlg = dm.getDialog();
				dlg.show();
//				((Button) dlg.findViewById(R.id.btn_share_kakao)).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						try {
//							final KakaoLink kakaoLink = KakaoLink.getKakaoLink(FeedActivity.this);
//							final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink
//									.createKakaoTalkLinkMessageBuilder();
//							final String linkContents = kakaoTalkLinkMessageBuilder
//									.addText(getResources().getString(R.string.share_msg))
//									.addAppButton(
//											"앱으로 이동",
//											new AppActionBuilder()
//													.addActionInfo(
//															AppActionInfoBuilder.createAndroidActionInfoBuilder()
//																	.setExecuteParam("").setMarketParam("").build())
//													.addActionInfo(
//															AppActionInfoBuilder.createiOSActionInfoBuilder()
//																	.setExecuteParam("").build()).build()).build();
//							kakaoLink.sendMessage(linkContents, FeedActivity.this);
//						}
//						catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				});
				((Button) dlg.findViewById(R.id.btn_share_band)).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/plain");
						intent.putExtra(Intent.EXTRA_SUBJECT, "Daddy\'s 40");
						intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg)
								+ "\n"+ DefineConst.NETWORK_URL_STORE);
						intent.setPackage("com.nhn.android.band");
						try {
							startActivity(intent);
						}
						catch (Exception e) {
							Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri
									.parse("https://play.google.com/store/apps/details?id=com.nhn.android.band&hl=ko"));
							intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent1);
						}
					}
				});
			}
		});
		mBtnInvitation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FeedActivity.this, InvitingActivity.class);
				intent.putExtra("FROM_FEED", true);
				startActivity(intent);
			}
		});
//		findViewById(R.id.btn_feed_answer).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				UserData.getInstance().setToken(null);
////				startActivity(new Intent(FeedActivity.this, MainLoginActivity.class));
//				startActivity(new Intent(FeedActivity.this, NotiMomQuestionActivity.class));
//				finish();
//			}
//		});
	}
	private class FeedAdapter extends BaseAdapter{

		private LayoutInflater layoutInflater;
		
		public FeedAdapter() {
			layoutInflater = (LayoutInflater) FeedActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return mFeedItem.size();
		}

		@Override
		public Object getItem(int position) {
			return mFeedItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(mFeedItem.get(position).isChanged()){
				mFeedItem.get(position).setChanged(false);
				if(convertView == null){
					if(mFeedItem.get(position).getImgSrc().equals("")){
						convertView = layoutInflater.inflate(R.layout.item_listview_feed_type1, parent, false);
					}	
					else{
						convertView = layoutInflater.inflate(R.layout.item_listview_feed_type2, parent, false);
					}
				}
				final View view = convertView;
				((TextView)convertView.findViewById(R.id.tv_item_feed_week)).setText(mFeedItem.get(position).getWeek());
				((TextView)convertView.findViewById(R.id.tv_item_feed_content)).setText(mFeedItem.get(position).getContent());
				if(!mFeedItem.get(position).getImgSrc().equals("")){
					URLImageView urlIv = new URLImageView(((ImageView)view.findViewById(R.id.iv_item_feed_img)), FeedActivity.this);
					urlIv.setImageURL(mFeedItem.get(position).getImgSrc(),mFeedItem.get(position).getWidth(),mFeedItem.get(position).getHeight());
				}
				((TextView)view.findViewById(R.id.tv_item_feed_title)).setText(mFeedItem.get(position).getTitle());
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						DialogMaker dm = null;
						if(mFeedItem.get(position).getImgSrc().equals(""))
							dm = new DialogMaker(FeedActivity.this, R.layout.item_listview_feed_type1_scroll,0.95,0.95);
						else
							dm = new DialogMaker(FeedActivity.this, R.layout.item_listview_feed_type2_scroll,0.95,0.95);
						final Dialog dlg = dm.getDialog();
						dlg.show();
						((TextView)dlg.findViewById(R.id.tv_item_feed_week)).setText(mFeedItem.get(position).getWeek());
						((TextView)dlg.findViewById(R.id.tv_item_feed_content)).setText(mFeedItem.get(position).getContent());
						((TextView)dlg.findViewById(R.id.tv_item_feed_title)).setText(mFeedItem.get(position).getTitle());
						if(!mFeedItem.get(position).getImgSrc().equals("")){
							((ImageView)dlg.findViewById(R.id.iv_item_feed_img)).getLayoutParams().height = ((ImageView)view.findViewById(R.id.iv_item_feed_img)).getLayoutParams().height;
							((ImageView)dlg.findViewById(R.id.iv_item_feed_img)).getLayoutParams().width = ((ImageView)view.findViewById(R.id.iv_item_feed_img)).getLayoutParams().width;
							((ImageView)dlg.findViewById(R.id.iv_item_feed_img)).setImageDrawable(((ImageView)view.findViewById(R.id.iv_item_feed_img)).getDrawable());
						}
						RequestThread rq = new FeedRequest(mFeedItem.get(position).getId());
						rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
							
							@Override
							public void onFinish(String result, JSONObject jsonObject) {
								LogUtil.e("Special Id Feed Result", result);
								int width = 0;
								int height = 0;
								String img_src = "";
								if(((JSONArray)jsonObject.get("resources"))!= null && ((JSONArray)jsonObject.get("resources")).size() > 0){
									img_src = ((JSONObject)((JSONArray)jsonObject.get("resources")).get(0)).get("image_url") + "";
									width = Integer.parseInt(((JSONObject)((JSONArray)jsonObject.get("resources")).get(0)).get("width") + "");
									height = Integer.parseInt(((JSONObject)((JSONArray)jsonObject.get("resources")).get(0)).get("height") + "");
								}
								if(!mFeedItem.get(position).getWeek().equals(jsonObject.get("week")+"") || !mFeedItem.get(position).getTitle().equals(""+jsonObject.get("title"))
										|| !mFeedItem.get(position).getContent().equals(""+jsonObject.get("content"))){
									mFeedItem.get(position).setWeek(jsonObject.get("week")+"");
									mFeedItem.get(position).setContent(""+jsonObject.get("content"));
									mFeedItem.get(position).setTitle( ""+jsonObject.get("title"));
									mFeedItem.get(position).setChanged(true);
								}
								mFeedItem.get(position).setWidth(width);
								mFeedItem.get(position).setHeight(height);
								mFeedItem.get(position).setId(Integer.parseInt(jsonObject.get("id") + ""));
								
								if(!img_src.equals(mFeedItem.get(position).getImgSrc())){
									mFeedItem.get(position).setChanged(true);
									mFeedItem.get(position).setImgSrc(img_src);
									((ImageView)dlg.findViewById(R.id.iv_item_feed_img)).post(new Runnable() {
										
										@Override
										public void run() {
											URLImageView urlIv = new URLImageView(((ImageView)dlg.findViewById(R.id.iv_item_feed_img)), FeedActivity.this);
											urlIv.setImageURL(mFeedItem.get(position).getImgSrc(),mFeedItem.get(position).getWidth(),mFeedItem.get(position).getHeight());
										}
									});
								}
								
								if((jsonObject.get("readed") + "").equals("true"))
									mFeedItem.get(position).setReaded(true);
								else
									mFeedItem.get(position).setReaded(false);
								
								notiDataSetChanged.sendEmptyMessage(0);
							}
							
							@Override
							public void onExceptionError(Exception e) {
								
							}
						});
						rq.start();
					}
				});
			}
			if(mFeedItem.get(position).isReaded()){
				//읽었을때 뷰 수정 부분
				convertView.findViewById(R.id.layout_item_feed_background).setBackgroundColor(0xe0444444);
				((TextView) convertView.findViewById(R.id.tv_item_feed_readed)).setText("읽음");
			}
			return convertView;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 101){
			startActivity(new Intent(FeedActivity.this,MainLoginActivity.class));
			finish();
		}
	}
}
