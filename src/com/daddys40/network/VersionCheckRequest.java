package com.daddys40.network;

import com.daddys40.util.DefineConst;


public class VersionCheckRequest extends RequestThread{
	@Override
	public void run() {
		super.run();
		httpGetMethod(DefineConst.NETWORK_URL_VIRSION_CHECK);
	}
}
