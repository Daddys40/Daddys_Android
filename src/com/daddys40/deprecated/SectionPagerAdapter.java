//package com.daddys40.deprecated;
//
//import com.daddys40.R;
//import com.daddys40.R.string;
//import com.daddys40.appintro.AppIntroFragment;
//
//import android.content.Context;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.view.View;
///**
// * Viewpager사용을 위한 adapter
// * @author Kim
// *
// */
//public class SectionPagerAdapter extends FragmentPagerAdapter {
//
//	Context context;
//	public SectionPagerAdapter(FragmentManager fm, Context context) {
//		super(fm);
//		this.context = context;
//	}
//
//	@Override
//	public Fragment getItem(int position) {
//		// LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//		Fragment ret = null;
//		if (position == 0) {
//			ret = new MainFragment();
//		}
//		else if (position == 1) {
//			ret = new AppIntroFragment();
//		}
//		// else if(position==2) {
//		// ret = new SectionFragment3();
//		// }
//
//		return ret;
//	}
//
//	@Override
//	public int getCount() {
//		return 2;
//	}
//
//	@Override
//	public CharSequence getPageTitle(int position) {
//		// TODO Auto-generated method stub
//		CharSequence ret = null;
//		if (position == 0) {
//			ret = context.getString(R.string.title_section1);
//		}
//		else if (position == 1) {
//			ret = context.getString(R.string.title_section2);
//		}
//		else {
//			ret = context.getString(R.string.err_1_k);
//		}
//
//		return ret;
//	}
//}
