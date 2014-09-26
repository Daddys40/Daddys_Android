package com.daddys40.network;

import java.util.Calendar;
import java.util.TimeZone;

import org.json.simple.JSONObject;

import com.daddys40.util.DefineConst;
import com.daddys40.util.InstantUserData;
import com.daddys40.util.LogUtil;

public class SignUpRequest extends RequestThread {
	private String email;
	private String pwd;
	private String name;
	private String gender;
	private String baby_name;
	private int age;
	private int height;
	private int weight;
	private String dday;

	public SignUpRequest(String email, String pwd, String name, String gender, String baby_name, String age, String height, String weight, String dday) {
//		this.email = email;
//		this.pwd = pwd;
//		this.name = name;
//		this.gender = gender;
//		this.baby_name = baby_name;
//		this.age = age;
//		this.height = height;
//		this.weight = weight;
//		this.dday = dday;
		addParams("user[email]", email);
		addParams("user[password]", pwd);
		addParams("user[name]", name);
		addParams("user[gender]", gender);
		addParams("user[baby_name]", baby_name);
		addParams("user[age]", age);
		addParams("user[height]", height);
		addParams("user[weight]", weight);
		addParams("user[baby_due]", dday + " 00:00:00 " + TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().getID()));
		LogUtil.e("send Data", email +"/"+pwd +"/"+name+"/"+gender+"/"+baby_name+"/"+age+"/"+height+"/"+weight+"/"+dday);
	}

	@Override
	public void run() {
		super.run();
		httpPostMethod(DefineConst.NETWORK_URL_SIGN_UP);
	}
}
