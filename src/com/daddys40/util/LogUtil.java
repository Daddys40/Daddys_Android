package com.daddys40.util;

import android.util.Log;

public class LogUtil {
	static private boolean logOn = false;
	static public void e(String tag,String msg){
		if(logOn)
			Log.e(tag, msg);
	}
}
