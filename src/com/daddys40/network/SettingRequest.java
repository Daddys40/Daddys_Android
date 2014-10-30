package com.daddys40.network;

import com.daddys40.util.DefineConst;
import com.daddys40.util.LogUtil;
import com.daddys40.util.UserData;

public class SettingRequest extends  RequestThread {
	public void addSettingParams(String key, String data){
		addParams("user["+key+"]", data);

	}
	@Override
	public void run() {
		super.run();
		addParams("authentication_token", UserData.getInstance().getToken());
		LogUtil.e("send token", UserData.getInstance().getToken());
		httpPutMethod(DefineConst.NETWORK_URL_UPDATE);
	}
}
