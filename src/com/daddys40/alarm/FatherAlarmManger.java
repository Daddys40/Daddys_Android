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
 * @author Kim
 *
 */
public class FatherAlarmManger implements MyAlarmManager{
//	static FatherAlarmManger mFatherAlarmManger;
//	private FatherAlarmManger(){
//		
//	}
//	public static synchronized MyAlarmManager getInstance(){
//		if(mFatherAlarmManger == null)
//			mFatherAlarmManger = new FatherAlarmManger();
//		return mFatherAlarmManger;
//	}
	public void setAlarm(Context context) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		int hour = UserData.getInstance().getAlarmTimeHour();
		int min = UserData.getInstance().getAlarmTimeMin();

		Calendar setCalendar = Calendar.getInstance();
		setCalendar.set(Calendar.HOUR_OF_DAY, hour);
		setCalendar.set(Calendar.MINUTE, min);
		LogUtil.e("setAlarm", "alarm start");
		if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			if (Calendar.getInstance().compareTo(setCalendar) >= 0)
				setCalendar.add(Calendar.DAY_OF_MONTH, 2);
		}
		else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)
			setCalendar.add(Calendar.DAY_OF_MONTH, 1);
		else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
			if (Calendar.getInstance().compareTo(setCalendar) >= 0)
				setCalendar.add(Calendar.DAY_OF_MONTH, 2);
		}
		else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
			setCalendar.add(Calendar.DAY_OF_MONTH, 1);
		else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
			if (Calendar.getInstance().compareTo(setCalendar) >= 0)
				setCalendar.add(Calendar.DAY_OF_MONTH, 3);
		}
		else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			setCalendar.add(Calendar.DAY_OF_MONTH, 2);
		else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			setCalendar.add(Calendar.DAY_OF_MONTH, 1);

		if(UserData.getInstance().getCalendar().before(setCalendar))
			return;
		setCalendar.set(Calendar.SECOND, 0);
		LogUtil.e("enroll Alarm", setCalendar.toString());
		am.set(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), sender);
	}
}
