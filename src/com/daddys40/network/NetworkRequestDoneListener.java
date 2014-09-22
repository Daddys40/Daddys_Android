package com.daddys40.network;

import org.json.simple.JSONObject;

public interface NetworkRequestDoneListener {
	public void onFinish(String result, JSONObject jsonObject);
	public void onExceptionError(Exception e);
}
