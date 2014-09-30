package com.daddys40.network;


import com.daddys40.util.DefineConst;
import com.daddys40.util.LogUtil;

public class InvitingRequest extends RequestThread{
	private String mToken = null;
	
	public InvitingRequest(String token) {
		mToken = token;
	}
	@Override
	public void run() {
		super.run();
		addParams("authentication_token", mToken);
		httpPostMethod(DefineConst.NETWORK_URL_INVITING);
	}
}
