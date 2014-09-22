package com.daddys40.deprecated;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.daddys40.R;
import com.daddys40.R.id;
import com.daddys40.R.layout;
import com.daddys40.alarm.EnrollAlarm;
import com.daddys40.util.DialogMaker;
import com.daddys40.util.UserData;
/**
 * 설정 화면 액티비티
 * @author Kim
 *
 */
public class SettingActivity extends Activity {

	private TextView mTvName;
	private TextView mTvWeek;
	private TextView mTvDate;
	private TextView mTvTime;
	private TextView mTvSuggenstion;

	private Button mBtnEdit;
	private Button mBtnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		UserData.init(SettingActivity.this);
		initView();
		initEvent();
	}

	private void initView() {
		mTvSuggenstion = (TextView) findViewById(R.id.tv_setting_suggestion);

		mBtnBack = (Button) findViewById(R.id.btn_setting_back);
		mBtnEdit = (Button) findViewById(R.id.btn_setting_edit);
		mTvName = (TextView) findViewById(R.id.tv_setting_name);
		mTvName.setText(UserData.getInstance().getName());
		mTvWeek = (TextView) findViewById(R.id.tv_setting_week);
		Calendar calendar = UserData.getInstance().getCalendar();
		mTvWeek.setText(UserData.getInstance().currentWeek() + " 주");
		mTvDate = (TextView) findViewById(R.id.tv_setting_date);
		mTvDate.setText(calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월 "
				+ calendar.get(Calendar.DAY_OF_MONTH) + "일");
		mTvTime = (TextView) findViewById(R.id.tv_setting_time);
		if (UserData.getInstance().getAlarmTimeMin() >= 10)
			mTvTime.setText(UserData.getInstance().getAlarmTimeHour() + " : "
					+ UserData.getInstance().getAlarmTimeMin());
		else
			mTvTime.setText(UserData.getInstance().getAlarmTimeHour() + " : 0"
					+ UserData.getInstance().getAlarmTimeMin());
		// 엄마도 알람이 있도록 수정
		// if(UserData.getInstance().getSex() == UserData.SEX_FEMALE){
		// ((View) findViewById(R.id.tv_setting_time_title)).setVisibility(View.GONE);
		// mTvTime.setVisibility(View.GONE);
		// }
		// else{
		((View) findViewById(R.id.tv_setting_time_title)).setVisibility(View.VISIBLE);
		mTvTime.setVisibility(View.VISIBLE);
		// }
	}

	private void initEvent() {
		mTvSuggenstion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DialogMaker dm = new DialogMaker(SettingActivity.this, R.layout.dialog_suggestion, 1, 1);
				final Dialog dlg = dm.getDialog();
				dlg.setCancelable(false);
				dlg.findViewById(R.id.btn_suggestion_send).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Uri uri = Uri.parse("mailto:ceo@chamchicompany.com");
						Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
						String str = new String("");
						if (!"".equals(((EditText) dlg.findViewById(R.id.et_suggestion_mail)).getText().toString())) {
							str+="답변 받을 메일주소 : "	+ ((EditText) dlg.findViewById(R.id.et_suggestion_mail)).getText().toString() + "\n";
						}
						str += ((EditText) dlg.findViewById(R.id.et_suggestion_content)).getText().toString();
						intent.putExtra(Intent.EXTRA_SUBJECT, ((EditText) dlg.findViewById(R.id.et_suggestion_title))
								.getText().toString());
						intent.putExtra(Intent.EXTRA_TEXT, str);
						startActivity(intent);
					}
				});
				dlg.findViewById(R.id.btn_suggestion_cancel).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dlg.dismiss();
					}
				});
				dlg.show();
			}
		});

		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
		mTvTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickerDialog timePicker = new TimePickerDialog(SettingActivity.this, new OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						UserData.getInstance().setAlarmTime(hourOfDay, minute);
						EnrollAlarm.getInstance().setAlarm(SettingActivity.this);
						if (minute >= 10)
							mTvTime.setText(hourOfDay + " : " + minute);
						else
							mTvTime.setText(hourOfDay + " : 0" + minute);
					}
				}, UserData.getInstance().getAlarmTimeHour(), UserData.getInstance().getAlarmTimeMin(), true);
				timePicker.show();
			}
		});
		mBtnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SettingActivity.this, MainActivity.class);
				intent.putExtra("FromSetting", true);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		initView();
		initEvent();
	}

	@Override
	public void onBackPressed() {
		setResult(0);
		super.onBackPressed();
	}
}
