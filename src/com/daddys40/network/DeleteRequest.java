package com.daddys40.network;

import com.daddys40.util.DefineConst;
import com.daddys40.util.UserData;

public class DeleteRequest extends RequestThread{
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		httpDeleteMethod(DefineConst.NETWORK_URL_DELETE+"?authentication_token="+UserData.getInstance().getToken());
	}
}
