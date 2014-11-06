package com.daddys40.util;

import java.io.File;
import java.util.Calendar;

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
	public void setSex(String sex){
		if(sex.equals("male"))
			mEditor.putInt(SEX, SEX_MALE);
		else
			mEditor.putInt(SEX, SEX_FEMALE);
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
	public void setAlarmDay(String str){
		mEditor.putString("days", str);
		mEditor.commit();
	}
	public String getAlarmDay(){
		return mSharedPreferences.getString("days", "135");
	}
	public void setChangeLatestFeed(){
		mEditor.putInt("change_week" + getToken(), currentWeek());
		mEditor.putInt("change_count"+ getToken(), getAnswerCount());
		mEditor.commit();
	}
//	public void setFeedReset(){
//		mEditor.putInt("change_week", currentWeek());
//		mEditor.putInt("change_count", getAnswerCount());
//		mEditor.commit();
//	}
	public boolean isLatestFeed(){
		int week = mSharedPreferences.getInt("change_week"+ getToken(), -1);
		int count = mSharedPreferences.getInt("change_count"+ getToken(), -1);
		if(week == currentWeek()){
			if(count < getAnswerCount())
				return false;
			else 
				return true;
		}
		return false;
	}
	public int getAnswerCount(){
		int day1 = Integer.parseInt(getAlarmDay().toCharArray()[0] + "");
		int day2 = Integer.parseInt(getAlarmDay().toCharArray()[1] + "");
		int day3 = Integer.parseInt(getAlarmDay().toCharArray()[2] + "");
		
		Calendar cal = Calendar.getInstance();
		if((cal.get(Calendar.DAY_OF_WEEK) + 1) <= day1){
			return 0;
		}
		else if((cal.get(Calendar.DAY_OF_WEEK) + 1) <= day2){
			return 1;
		}
		else
			return 2;
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
		LogUtil.e("Set Month? ",dueCal.toString());
		LogUtil.e("Day",week + "");
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
	public void setTutorialOk(){
		mEditor.putBoolean("TUTORIAL", true);
		mEditor.commit();
	}
	public boolean isTutorialOk(){
		return mSharedPreferences.getBoolean("TUTORIAL", false);
	}
//	public void encEventCounter(){
//		int count = mSharedPreferences.getInt("EVENT_C", 0);
//		count++;
//		mEditor.putInt("EVENT_C",  count);
//		mEditor.commit();
//	}
//	public int getEventCount(){
//		return mSharedPreferences.getInt("EVENT_C", 0);
//	}
//	public void setSleepCoachOk(){
//		mEditor.putBoolean("SLEEP", true);
//		mEditor.commit();
//	}
//	public boolean isSleepCoachOk(){
//		return mSharedPreferences.getBoolean("SLEEP", false);
//	}
	
//	public void setAge(int age){
//		mEditor.putInt("age", age);
//		mEditor.commit();
//	}
//	public void setHeight(int height){
//		mEditor.putInt("height", height);
//		mEditor.commit();
//	}
//	public void setWeight(int weight){
//		mEditor.putInt("weight", weight);
//		mEditor.commit();
//	}
//	public int getAge(){
//		return mSharedPreferences.getInt("age", 0);
//	}
//	public int getHeight(){
//		return mSharedPreferences.getInt("height", 0);
//	}
//	public int getWeight(){
//		return mSharedPreferences.getInt("weight", 0);
//	}
//	추가된부분
	public void setToken(String token){
		mEditor.putString(TOKEN, token);
		mEditor.commit();
	}
	public String getToken(){
		return mSharedPreferences.getString(TOKEN, null);
	}
}
