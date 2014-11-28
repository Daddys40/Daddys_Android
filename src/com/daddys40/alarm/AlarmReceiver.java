package com.daddys40.alarm;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.daddys40.re.R;
import com.daddys40.util.LogUtil;
import com.daddys40.util.UserData;
/**
 * 등록된 알람 실행시 수행되는 리시버
 * @author Kim
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Toast.makeText(context, "Alarm Received!", Toast.LENGTH_LONG).show();
		UserData.init(context);
		if( UserData.getInstance().getToken() == null)
			return;
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			LogUtil.e("Receiver", "BOOT COMPLETED RECEIVER");
			EnrollAlarm.getInstance().setAlarm(context);
//			Intent intent2 = new Intent(context, BootAlarmActivity.class);
//			intent2.putExtras(intent);
//			intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(intent2);
		}
		else {
			LogUtil.e("Receiver", "General Receiver");
			NotificationManager notifier = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notify = new Notification(R.drawable.ic_launcher, "text", System.currentTimeMillis());

			Intent intent2 = null;
			if(UserData.getInstance().getSex() == UserData.SEX_FEMALE)
				intent2 = new Intent(context, NotiMomQuestionActivity.class);
			else
				intent2 = new Intent(context, NotiDialogActivity.class);
			intent2.putExtras(intent);
			intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent2);
		}
		// PendingIntent pender = PendingIntent.getActivity(context, 0, intent2, 0);
		// notify.setLatestEventInfo(context, intent.getStringExtra("title"),intent.getStringExtra("content"), pender);
		// notify.flags |= Notification.FLAG_AUTO_CANCEL;
		// notify.number++;
		// notifier.notify(1, notify);
	}

}
