package com.daddys40;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.daddys40.data.FeedItem;
import com.daddys40.network.FeedRequest;
import com.daddys40.network.NetworkRequestDoneListener;
import com.daddys40.network.RequestThread;
import com.daddys40.util.DialogMaker;
import com.daddys40.util.LogUtil;
import com.daddys40.util.URLImageView;
import com.daddys40.util.UserData;

public class FeedActivity extends Activity {
	private ListView mFeedListView;
	private ArrayList<FeedItem> mFeedItem = new ArrayList<FeedItem>();
	private FeedAdapter mFeedAdapter;
	
	private Button mBtnSetting;
	private Button mBtnShare;
	private Button mBtnInvitation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_main);
		initView();
		initEvent();
		RequestThread rq = new FeedRequest();
		rq.setOnNetworkRequestDoneListener(new NetworkRequestDoneListener() {
			
			@Override
			public void onFinish(String result, JSONObject jsonObject) {
				LogUtil.e("Feed Result", result);
				JSONArray jsonArray = ((JSONArray)jsonObject.get("data"));
				for(int i = 0 ; i < jsonArray.size(); i++){
					JSONObject jsonObjectTemp = (JSONObject) jsonArray.get(i);
					int width = 0;
					int height = 0;
					String img_src = "";
					if(((JSONArray)jsonObjectTemp.get("resources")).size() > 0){
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
					mFeedItem.add(tempFeedItem);
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
	}
	private void initEvent(){
		mBtnInvitation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FeedActivity.this, InvitingActivity.class);
				intent.putExtra("FROM_FEED", true);
				startActivity(intent);
			}
		});
		findViewById(R.id.btn_feed_logout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserData.getInstance().setToken(null);
				startActivity(new Intent(FeedActivity.this, MainLoginActivity.class));
				finish();
			}
		});
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
								if(((JSONArray)jsonObject.get("resources")).size() > 0){
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
			if(mFeedItem.get(position).isReaded())
				convertView.findViewById(R.id.layout_item_feed_background).setBackgroundColor(0xe0444444);
			return convertView;
		}
	}
}
