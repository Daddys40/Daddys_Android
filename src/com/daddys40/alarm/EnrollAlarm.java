package com.daddys40.alarm;

import com.daddys40.data.InstantUserData;
import com.daddys40.util.UserData;

import android.content.Context;
/**
 * 알람 등록시 실행하는 클래스
 * @author Kim
 *
 */
public class EnrollAlarm {
	private static EnrollAlarm mEnrollAlarm;

	private EnrollAlarm() {

	}

	public static synchronized EnrollAlarm getInstance() {
		if (mEnrollAlarm == null)
			mEnrollAlarm = new EnrollAlarm();
		return mEnrollAlarm;
	}

	public void setAlarm(Context context) {
		MyAlarmManager myAlarmManager = null;
//		if(UserData.getInstance().getSex() == UserData.SEX_MALE)
			myAlarmManager = new FatherAlarmManger();
//		else
//			myAlarmManager = new MotherAlarmManager();
		myAlarmManager.setAlarm(context);
	}
}
