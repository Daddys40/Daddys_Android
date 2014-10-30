//package com.daddys40.deprecated;
//
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.ViewPager;
//
//import com.daddys40.R;
//import com.daddys40.R.id;
//import com.daddys40.R.layout;
//import com.daddys40.util.MyTagManager;
//import com.google.analytics.tracking.android.EasyTracker;
///**
// * Viewpager에 각 Fragment의 FragmentActivity
// * @author Kim
// *
// */
//public class MainFragmentActivity extends FragmentActivity {
//
//	Context context;
//	SectionPagerAdapter sectionPagerAdapter;
//	ViewPager viewPager;
//	
//	public MainFragmentActivity() {
//	}
//	
//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//        setContentView(R.layout.pager_main);
//		this.context = getApplicationContext();
//		sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), getApplicationContext());
//		
//		viewPager = (ViewPager)findViewById(R.id.pager_main);
//		try{
//			viewPager.setAdapter(sectionPagerAdapter);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		EasyTracker.getInstance(this).activityStart(this);
//		MyTagManager.getInstance(this).send("appview", "MainScreen");
//	}
//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		EasyTracker.getInstance(this).activityStop(this);
//	}
//}
