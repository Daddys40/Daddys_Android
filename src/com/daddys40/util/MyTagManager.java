package com.daddys40.util;

import java.util.HashMap;

import android.content.Context;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
/**
 * 구글 애널리틱스 관련
 * @author Kim
 *
 */
public class MyTagManager {
//	private final String CONTAINER_ID = "GTM-56MD3M_v1";
	private final String ANALYTICS_ID = "UA-53330191-1";
	private static MyTagManager mMyTagManager;
//	volatile private Container mContainer;
//	private TagManager mTagManager;
	private Tracker tracker;
	private MyTagManager(){
		
	}
	static public synchronized MyTagManager getInstance(Context context){
		if(mMyTagManager == null){
			mMyTagManager = new MyTagManager();
			mMyTagManager.init(context);
		}
		return mMyTagManager;
	}
	private void init(Context context){
		tracker = GoogleAnalytics.getInstance(context).getTracker(ANALYTICS_ID);
	}
	public void send(String type, String screenName){
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE,type);
		hitParameters.put(Fields.SCREEN_NAME,screenName);
		
		tracker.send(hitParameters);
	}
	public void sendEvent(String event){
		tracker.send(MapBuilder
			      .createEvent("ui_action",     // Event category (required)
			                   "button_press",  // Event action (required)
			                   event,   // Event label
			                   null)            // Event value
			      .build()
			  );
	}
	
//	public void pushEventData(String type, String key, String value){
//		DataLayer dataLayer = mTagManager.getDataLayer();
//	    dataLayer.push(DataLayer.mapOf("event",
//	                                   type,      // The event type. This value should be used consistently for similar event types.
//	                                   key,      // Writes a key "screenName" to the dataLayer map.
//	                                   value)     // Writes a value "Home Screen" for the "screenName" key.
//	    );
//	}
}
