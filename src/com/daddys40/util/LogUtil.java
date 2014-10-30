package com.daddys40.util;

import android.util.Log;

public class LogUtil {
	static public void e(String tag,String msg){
		if(DefineConst.logOn)
			Log.e(tag, msg);
	}
}
