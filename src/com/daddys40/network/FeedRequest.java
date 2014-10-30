package com.daddys40.network;

import com.daddys40.data.InstantUserData;
import com.daddys40.util.DefineConst;
import com.daddys40.util.LogUtil;
import com.daddys40.util.UserData;

public class FeedRequest extends RequestThread {
	int id = -1;
	String requestVar = "";
	public FeedRequest() {
		if(!UserData.getInstance().isLatestFeed() && UserData.getInstance().currentWeek() <= 40 && UserData.getInstance().currentWeek() >= 5){
			requestVar += "&week="+UserData.getInstance().currentWeek()+"&count="+UserData.getInstance().getAnswerCount();
			LogUtil.e("Current Week & count", "&week="+UserData.getInstance().currentWeek()+"&count="+UserData.getInstance().getAnswerCount());
			UserData.getInstance().setChangeLatestFeed();
		}
	}
	public FeedRequest(int id) {
		this.id = id;
	}
	@Override
	public void run() {
		super.run();
		LogUtil.e("send token", InstantUserData.getInstance().getToken());
		addParams("authentication_token", InstantUserData.getInstance().getToken());
		if(id == -1){
			httpGetMethod(DefineConst.NETWORK_URL_FEED + "?authentication_token=" + InstantUserData.getInstance().getToken()+requestVar);
			LogUtil.e("Request URL", DefineConst.NETWORK_URL_FEED + "?authentication_token=" + InstantUserData.getInstance().getToken()+requestVar);
		}
		else{
			httpGetMethod(DefineConst.NETWORK_URL_FEED+"/"+id + "?authentication_token=" + InstantUserData.getInstance().getToken());
		}
	}
}
