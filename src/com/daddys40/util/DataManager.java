package com.daddys40.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.parser.JSONParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.daddys40.data.QnADataContainer;
/**
 * 	서버로부터 데이터를 받아오는 기능
 * @author Kim
 *
 */
public class DataManager {
	private static DataManager mDataManager;
	private ProgressDialog pd;
	private Context context;
	private DoneCallback doneCallback = null;
	
	private DataManager(){
		
	}
	public static synchronized DataManager getInstance(){
		if(mDataManager == null)
			mDataManager = new DataManager();
		return mDataManager;
	}
	public void setOnNetworkDoneListener(DoneCallback doneCallback) {
		this.doneCallback = doneCallback;
	}

	public void initQnAData(final Context context) {
		this.context = context;
		if (UserData.getInstance().isSerialized()) {
			LogUtil.e("is Serialized?", "TRUE");
			QnADataContainer.getInstance().doDeserialization(context);
		}
		else {
			LogUtil.e("is Serialized?", "FALSE");
			Thread networkThread = new Thread(new Runnable() {
				@Override
				public void run() {
					showHandler.sendEmptyMessage(0);
					HttpClient httpClient = new DefaultHttpClient();
					try {
						HttpGet httpGet = new HttpGet("http://daddys40.woobi.co.kr/getAllData2.php");
						HttpResponse httpResponse;
						httpResponse = httpClient.execute(httpGet);
//						HttpEntity httpEntity = httpResponse.getEntity();
						BufferedReader rd = new BufferedReader(new InputStreamReader(
								httpResponse.getEntity().getContent()));
//						StringBuilder str = new StringBuilder(new String(EntityUtils.toByteArray(httpEntity), "UTF-8"));
						StringBuilder strBuilder = new StringBuilder();
						String line;
						while ((line = rd.readLine()) != null) {
							if(line.contains("<script")){
								String[] str = line.split("<script");
								line = str[0];
							}
							LogUtil.e("", line);
							strBuilder.append(line);
						}
						LogUtil.e("data", strBuilder.toString().length() + "");
						JSONParser parser = new JSONParser();
						DataParser dataParser = new DataParser();
						dataParser.getParsingData(parser.parse(strBuilder.toString()));
						httpGet.abort();
						httpClient.getConnectionManager().shutdown();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					QnADataContainer.getInstance().doSerialization();
					dismissHandler.sendEmptyMessage(0);
					if (doneCallback != null)
						doneCallback.onNetworkDoneListener();
				}
			});
			networkThread.start();
		}
	}

	Handler showHandler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			pd = ProgressDialog.show(context, "Loding", "Now Loading...");
			return false;
		}
	});

	Handler dismissHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			pd.dismiss();
			return false;
		}
	});

	public interface DoneCallback {
		void onNetworkDoneListener();
	}
}
