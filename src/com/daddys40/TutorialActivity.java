package com.daddys40;

import com.daddys40.util.UserData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TutorialActivity extends Activity {
	private Button mBtnNext;

	private Button mBtnPrev;

	private RelativeLayout mLayBackground;

	private int mCurrentState = 0;

	private final int BACKGROUND[] = { R.drawable.img_tutorial_01, R.drawable.img_tutorial_02,
			R.drawable.img_tutorial_03, R.drawable.img_tutorial_04, R.drawable.img_tutorial_05,
			R.drawable.img_tutorial_06,R.drawable.img_tutorial_07,R.drawable.img_tutorial_08 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		initView();
		initEvent();
	}

	private void initView() {
		mBtnNext = (Button) findViewById(R.id.btn_tutorial_next);
		mBtnPrev = (Button) findViewById(R.id.btn_tutorial_prev);
		mLayBackground = (RelativeLayout) findViewById(R.id.layout_tutorial);
	}

	private void initEvent() {
		mBtnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mCurrentState++;
				if(mCurrentState == 8){
					UserData.init(TutorialActivity.this);
					UserData.getInstance().setTutorialOk();
					startActivity(new Intent(TutorialActivity.this, MainLoginActivity.class));
					finish();
				}else{
					if(mCurrentState == 1)
						mBtnPrev.setVisibility(View.VISIBLE);
					mLayBackground.setBackgroundResource(BACKGROUND[mCurrentState]);
				}
			}
		});
		mBtnPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentState--;
				if(mCurrentState == 0){
					mBtnPrev.setVisibility(View.GONE);
				}
				mLayBackground.setBackgroundResource(BACKGROUND[mCurrentState]);
			}
		});
	}
}
