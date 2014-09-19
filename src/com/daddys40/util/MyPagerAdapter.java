package com.daddys40.util;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * Viewpager 사용을 위한 Adapter
 * @author Kim
 *
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;

	public MyPagerAdapter(FragmentManager fm, List<Fragment> fragment) {
		super(fm);
		fragments = fragment;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}
