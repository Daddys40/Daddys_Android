package com.daddys40.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.os.Build;

import com.daddys40.util.DefineConst;

public class RequestThread extends Thread{
	protected NetworkRequestDoneListener networkListener = null;
	private List<NameValuePair> params = new ArrayList<NameValuePair>();
	
	public void setOnNetworkRequestDoneListener(NetworkRequestDoneListener listener){
		networkListener = listener;
	}
	protected void httpGetMethod(String uri){
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, DefineConst.NETWORK_HTTP_USER_AGENT + "1.0.0 ("
				+ Build.MODEL +"; " + Build.VERSION.RELEASE + ")");
		try {
			HttpGet httpGet = new HttpGet(uri);
			HttpResponse httpResponse;
			httpResponse = httpClient.execute(httpGet);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));
			StringBuilder strBuilder = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				strBuilder.append(line);
			}
			httpGet.abort();
			httpClient.getConnectionManager().shutdown();
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(strBuilder.toString());
			networkListener.onFinish(strBuilder.toString(), jsonObject);
		}
		catch (Exception e) {
			networkListener.onExceptionError(e);
			e.printStackTrace();
		}
	}
	protected void addParams(String name, String value){
		params.add(new BasicNameValuePair(name, value));
	}
	protected void httpPostMethod(String uri){
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, DefineConst.NETWORK_HTTP_USER_AGENT);
		try {
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setEntity(ent);
			HttpResponse httpResponse;
			httpResponse = httpClient.execute(httpPost);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));
			StringBuilder strBuilder = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				strBuilder.append(line);
			}
			httpPost.abort();
			httpClient.getConnectionManager().shutdown();
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(strBuilder.toString());
			networkListener.onFinish(strBuilder.toString(), jsonObject);
		}
		catch (Exception e) {
			networkListener.onExceptionError(e);
			e.printStackTrace();
		}
	}
}
