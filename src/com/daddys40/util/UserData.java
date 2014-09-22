package com.daddys40.util;

import java.io.File;
import java.util.Calendar;

import com.daddys40.alarm.NotiDialogActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
/**
 * 사용자의 기본 정보를 유지하는 클래스
 * @author Kim
 *
 */
public class UserData {
	final static private String PREF_KEY = "daddy";
	final static private String INITIALIZED = "init";
	final private String NAME_KEY = "name";
	final private String YEAR_KEY = "year";
	final private String MONTH_KEY = "month";
	final private String DAY_KEY = "day";
	final private String SEX = "sex";	
	
	final static public int SEX_MALE = 0;
	final static  public int SEX_FEMALE = 1;
	
	private static SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor mEditor;
	
	private static UserData userData;
	
//	추가된 부분
	final private String TOKEN = "token";
	
	private UserData() {
		
	}
	public boolean isInitialized(){
		return mSharedPreferences.getBoolean(INITIALIZED, false);
	}
	public void setSex(int sex){
		mEditor.putInt(SEX, sex);
		mEditor.commit();
	}
	public int getSex(){
		return mSharedPreferences.getInt(SEX, 0);
	}
	public void setName(String name){
		mEditor.putString(NAME_KEY, name);
		mEditor.commit();
	}
	public String getName(){
		return mSharedPreferences.getString(NAME_KEY, null);
	}
	public void setCalendar(Calendar calendar){
		mEditor.putInt(YEAR_KEY, calendar.get(Calendar.YEAR));
		mEditor.putInt(MONTH_KEY, calendar.get(Calendar.MONTH));
		mEditor.putInt(DAY_KEY, calendar.get(Calendar.DAY_OF_MONTH));
		mEditor.commit();
	}
	public Calendar getCalendar(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(mSharedPreferences.getInt(YEAR_KEY, 0), mSharedPreferences.getInt(MONTH_KEY, 0), mSharedPreferences.getInt(DAY_KEY, 0), 0, 0, 0);
		return calendar;
	}
	public static void init(Context context){
		mSharedPreferences = context.getSharedPreferences(PREF_KEY, context.MODE_WORLD_WRITEABLE);
		mEditor = mSharedPreferences.edit();
		userData = new UserData();
		mEditor.putBoolean(INITIALIZED, true);
		mEditor.commit();
	}
	public static UserData getInstance(){
		return userData;
	}
	public boolean isSerialized(){
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		File dirFile = new File(path + "/.daddys40/daddys40_1.ser");
		return dirFile.length() > 100;
//		return false;
	}
	public void setAlarmTime(int hour, int min){
		LogUtil.e("set Time", "Hour : " + hour + "/ Min : " + min);
		mEditor.putInt("hour", hour);
		mEditor.putInt("min", min);
		mEditor.commit();
	}
	public int getAlarmTimeHour(){
		return mSharedPreferences.getInt("hour", 18);
	}
	public int getAlarmTimeMin(){
		return mSharedPreferences.getInt("min", 30);
	}
	public int currentWeek() {
		Calendar dueCal;
		int week;
		dueCal = getCalendar();
		Calendar cal = Calendar.getInstance();
		if (dueCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
			week = dueCal.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR);
			LogUtil.e("Function currentWeek", dueCal.get(Calendar.DAY_OF_YEAR) + "");
			LogUtil.e("Function currentWeek", cal.get(Calendar.DAY_OF_YEAR) + "");
			LogUtil.e("Function currentWeek", "current week is this year");
		}
		else {
			week = cal.getMaximum(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR) + dueCal.get(Calendar.DAY_OF_YEAR);
			LogUtil.e("Function currentWeek", "current week is next year");
		}
		week = week/7;
		week = 40 - week;
		LogUtil.e("Now Week", "Calc week : " + week);
		if(week < 5)
			return 5;
		else if(week > 41 || dueCal.compareTo(cal) <= 0){
			return 41;
		}
		else
			return week;
	}
	public void setEventOk(){
		mEditor.putBoolean("EVENT", true);
		mEditor.commit();
	}
	public boolean isEventOk(){
		return mSharedPreferences.getBoolean("EVENT", false);
	}
	public void encEventCounter(){
		int count = mSharedPreferences.getInt("EVENT_C", 0);
		count++;
		mEditor.putInt("EVENT_C",  count);
		mEditor.commit();
	}
	public int getEventCount(){
		return mSharedPreferences.getInt("EVENT_C", 0);
	}
//	public void setSleepCoachOk(){
//		mEditor.putBoolean("SLEEP", true);
//		mEditor.commit();
//	}
//	public boolean isSleepCoachOk(){
//		return mSharedPreferences.getBoolean("SLEEP", false);
//	}
	
	public void setAge(int age){
		mEditor.putInt("age", age);
		mEditor.commit();
	}
	public void setHeight(int height){
		mEditor.putInt("height", height);
		mEditor.commit();
	}
	public void setWeight(int weight){
		mEditor.putInt("weight", weight);
		mEditor.commit();
	}
	public int getAge(){
		return mSharedPreferences.getInt("age", 0);
	}
	public int getHeight(){
		return mSharedPreferences.getInt("height", 0);
	}
	public int getWeight(){
		return mSharedPreferences.getInt("weight", 0);
	}
//	추가된부분
	public void setToken(String token){
		mEditor.putString(TOKEN, token);
		mEditor.commit();
	}
	public String getToken(){
		return mSharedPreferences.getString(TOKEN, null);
	}
}
