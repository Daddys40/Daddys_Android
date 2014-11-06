package com.daddys40.data;

import android.graphics.Path;


public class InstantUserData {
	
	private static InstantUserData iud = null;
	private String email;
	private String pwd;
	private String name;
	private String gender;
	private String baby_name;
	private String age;
	private String height;
	private String weight;
	private String dday;
	private String token;
	private String alarm_day;
	private String alarm_time;
	private String partnerName;
	
	private boolean connected = false;
	
	private InstantUserData(){
		
	}
	public static void resetInstantUserData(){
		iud = new InstantUserData();
	}
	public static synchronized InstantUserData getInstance(){
		if(iud == null)
			iud = new InstantUserData();
		return iud;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public void setPassword(String pwd){
		this.pwd = pwd;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setGender(String gender){
		this.gender = gender;
	}
	public void setBabyName(String baby_name){
		this.baby_name = baby_name;
	}
	public void setAge(String age){
		this.age = age;
	}
	public void setHeight(String height){
		this.height = height;
	}
	public void setWeight(String weight){
		this.weight = weight;
	}
	public void setDday(String dday){
		this.dday = dday;
	}
	public void setToken(String token){
		this.token = token;
	}
	public String getToken(){
		return token;
	}
	public void setAlarmDay(String alarm_day){
		this.alarm_day = alarm_day;
	}
	public String getAlarmDay(){
		return alarm_day;
	}
	public void setAlarmTime(String alarm_time){
		this.alarm_time = alarm_time;
	}
	public String getAlarmTime(){
		return alarm_time;
	}
	public String getGender(){
		return gender;
	}
	public String getAge(){
		return age;
	}
	public String getWeight(){
		return weight;
	}
	public String getHeight(){
		return height;
	}
	public void setData(String email, String pwd, String name, String gender, String baby_name, String age, String height, String weight, String dday){
		setEmail(email);
		setPassword(pwd); 
		setName(baby_name);
		setGender(gender);
		setBabyName(baby_name);
		setAge(age);
		setHeight(height);
		setWeight(weight);
		setDday(dday);
	}
	public void setConnected(boolean bool){
		connected = bool;
	}
	public boolean isConnected(){
		return connected;
	}
	public void setPartnerName(String name){
		partnerName = name;
	}
	public String getPartnerName(){
		return partnerName;
	}
}
