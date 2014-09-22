package com.daddys40.util;

public class InstantUserData {
	private static InstantUserData iud = null;
	private String email;
	private String pwd;
	private String name;
	private String gender;
	private String baby_name;
	private int age;
	private int height;
	private int weight;
	private String dday;
	
	private InstantUserData(){
		
	}
	public synchronized InstantUserData getInstance(){
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
	public void setAge(int age){
		this.age = age;
	}
	public void setHeight(int height){
		this.height = height;
	}
	public void setWeight(int weight){
		this.weight = weight;
	}
	public void setDday(String dday){
		this.dday = dday;
	}
	public void setData(String email, String pwd, String name, String gender, String baby_name, int age, int height, int weight, String dday){
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
}
