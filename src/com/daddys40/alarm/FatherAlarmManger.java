package com.daddys40.alarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.daddys40.util.LogUtil;
import com.daddys40.util.UserData;

/**
 * 아빠의 경우 알람이 등록되는 클래스
 * 
 * @author Kim
 * 
 */
public class FatherAlarmManger implements MyAlarmManager {
	// static FatherAlarmManger mFatherAlarmManger;
	// private FatherAlarmManger(){
	//
	// }
	// public static synchronized MyAlarmManager getInstance(){
	// if(mFatherAlarmManger == null)
	// mFatherAlarmManger = new FatherAlarmManger();
	// return mFatherAlarmManger;
	// }

	private int day1 = 2;
	private int day2 = 4;
	private int day3 = 6;

	private int hour = 0;
	private int min = 0;

	public void setAlarm(Context context) {
		parseHelper();
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Calendar setCalendar = Calendar.getInstance();
		setCalendar.set(Calendar.HOUR_OF_DAY, hour);
		setCalendar.set(Calendar.MINUTE, min);
		LogUtil.e("setAlarm", "alarm start");
		if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) < day1) {
			setCalendar.add(Calendar.DAY_OF_MONTH, day1
					- Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		} 
		else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == day1) {
			if (Calendar.getInstance().compareTo(setCalendar) >= 0)
				setCalendar.add(Calendar.DAY_OF_MONTH, day2 - day1);
		} 
		else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) < day2) {
			setCalendar.add(Calendar.DAY_OF_MONTH, day2
					- Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		} 
		else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == day2) {
			if (Calendar.getInstance().compareTo(setCalendar) >= 0)
				setCalendar.add(Calendar.DAY_OF_MONTH, day3 - day2);
		}
		else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) < day3) {
			setCalendar.add(Calendar.DAY_OF_MONTH, day3
					- Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		} 
		else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == day3) {
			if (Calendar.getInstance().compareTo(setCalendar) >= 0)
				setCalendar.add(Calendar.DAY_OF_MONTH, day1 + 7 - day3 );
		}
		else
			setCalendar.add(Calendar.DAY_OF_MONTH, day1 + 7 - Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
//		if (UserData.getInstance().getCalendar().before(setCalendar))
//			return;
		setCalendar.set(Calendar.SECOND, 0);
		LogUtil.e("enroll Alarm", setCalendar.toString());
		am.set(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), sender);
	}

	private void parseHelper() {
//		String days = InstantUserData.getInstance().getAlarmDay();
		String days = UserData.getInstance().getAlarmDay();
		day1 = Integer.parseInt(days.charAt(0) + "") + 1;
		day2 = Integer.parseInt(days.charAt(1) + "") + 1;
		day3 = Integer.parseInt(days.charAt(2) + "") + 1;

		hour = UserData.getInstance().getAlarmTimeHour();
		min = UserData.getInstance().getAlarmTimeMin();
	}
}
