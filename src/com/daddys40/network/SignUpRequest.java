package com.daddys40.network;

import com.daddys40.util.DefineConst;
import com.daddys40.util.LogUtil;

public class SignUpRequest extends RequestThread {
//	private String email;
//	private String pwd;
//	private String name;
//	private String gender;
//	private String baby_name;
//	private int age;
//	private int height;
//	private int weight;
//	private String dday;
//	
//	private final int TYPE_SIGN_UP = 0;
//	private final int TYPE_INVITED = 1;
//	private int requestType = TYPE_SIGN_UP;

	public SignUpRequest(String email, String pwd, String name, String gender, String baby_name, String age, String height, String weight, String dday, String alarm_day ,String alarm_time) {
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
		addParams("user[baby_due]", dday + " 00:00:00 ");
		//+TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().getID())
		addParams("user[notifications_days]", alarm_day);
		addParams("user[notificate_at]", alarm_time);
		
		LogUtil.e("Sign Up Request send Data", email +"/"+pwd +"/"+name+"/"+gender+"/"+baby_name+"/"+age+"/"+height+"/"+weight+"/"+dday);
//		requestType = TYPE_SIGN_UP;
	}

	public SignUpRequest(String email, String pwd, String invitation_code, String alarm_day ,String alarm_time) {
		addParams("user[email]", email);
		addParams("user[password]", pwd);
		addParams("user[partner_invitation_code]", invitation_code);
		
		addParams("user[notifications_days]", alarm_day);
		addParams("user[notificate_at]", alarm_time);
		
		LogUtil.e("Sign Up Request send Data", email +"/"+pwd + "/" + invitation_code + "/" + alarm_day + "/" + alarm_time);
//		requestType = TYPE_INVITED;
	}

	@Override
	public void run() {
		super.run();
		httpPostMethod(DefineConst.NETWORK_URL_SIGN_UP);
	}
}
