package com.daddys40.network;

import com.daddys40.util.DefineConst;

public class SignInRequest extends RequestThread{
	private String mEmail = null;
	private String mPassword = null;
	private String mToken = null;
	
	public SignInRequest(String token) {
		mToken = token;
	}
	public SignInRequest(String email, String password){
		mEmail = email;
		mPassword = password;
	}
	
	@Override
	public void run() {
		super.run();
		if(mToken == null){
//			?user[email]=breaht103@gmail.com&user[password]=1994Kurt
			addParams("email", mEmail);
			addParams("password", mPassword);
			httpPostMethod(DefineConst.NETWORK_URL_SIGN_IN);
		}
		else{
//			authentication_token=?
			addParams("authentication_token", mToken);
			httpPostMethod(DefineConst.NETWORK_URL_VALIDATE);
//			httpGetMethod(DefineConst.NETWORK_URL_VALIDATE+"?authentication_token="+mToken);
		}
	}
}
