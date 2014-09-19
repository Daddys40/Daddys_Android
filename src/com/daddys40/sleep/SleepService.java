//package com.daddys40.sleep;
//package com.daddys40.sleep;
//
//import android.app.Service;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.os.Binder;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//
//import com.daddys40.R;
//
//public class SleepService extends Service {
//
//	private final IBinder mBinder = new LocalBinder();
//	private OnMediaEndListener onMediaEndListener = new OnMediaEndListener() {
//		
//		@Override
//		public void onMediaEnd() {
//			// TODO Auto-generated method stub
//			
//		}
//	};
//	public class LocalBinder extends Binder {
//
//		SleepService getService() {
//
//			return SleepService.this;
//		}
//	}
//	public interface OnMediaEndListener{
//		public void onMediaEnd();
//	};
//	private final int STATE_RAIN = 0;
//	private final int STATE_MOUNT = 1;
//	private final int STATE_SEA = 2;
//
//	private MediaAdapter mMediaAdapterRain;
//	private MediaAdapter mMediaAdapterMount;
//	private MediaAdapter mMediaAdapterSea;
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return mBinder;
//	}
//
//	@Override
//	public void onCreate() {
//		super.onCreate();
//		mMediaAdapterRain = new MediaAdapter(getApplicationContext(), R.raw.sound_rain);
//		mMediaAdapterMount = new MediaAdapter(getApplicationContext(), R.raw.sound_water);
//		mMediaAdapterSea = new MediaAdapter(getApplicationContext(), R.raw.sound_wave);
//	}
//
////	@Override
////	public int onStartCommand(Intent intent, int flags, int startId) {
////		if (intent != null) {
////			int mCurrentState = intent.getIntExtra("TYPE", STATE_RAIN);
////			int DelayedTime = intent.getIntExtra("TIME", -1);
////			if (DelayedTime == -1) {
////				if (mCurrentState == STATE_RAIN)
////					mMediaAdapterRain.startPlayer();
////				else if (mCurrentState == STATE_MOUNT)
////					mMediaAdapterMount.startPlayer();
////				else if (mCurrentState == STATE_SEA)
////					mMediaAdapterSea.startPlayer();
////			}
////			else {
////				if (mCurrentState == STATE_RAIN)
////					mMediaAdapterRain.startPlayer(DelayedTime);
////				else if (mCurrentState == STATE_MOUNT)
////					mMediaAdapterMount.startPlayer(DelayedTime);
////				else if (mCurrentState == STATE_SEA)
////					mMediaAdapterSea.startPlayer(DelayedTime);
////			}
////		}
////		return super.onStartCommand(intent, flags, startId);
////	}
//	public void startSound(int type){
//		stopSound();
//		if (type == STATE_RAIN)
//			mMediaAdapterRain.startPlayer();
//		else if (type == STATE_MOUNT)
//			mMediaAdapterMount.startPlayer();
//		else if (type == STATE_SEA)
//			mMediaAdapterSea.startPlayer();
//	}
//	public void startSound(int type, int mintime){
//		startSound(type);
//		mediaStopHandler.sendEmptyMessageDelayed(0, mintime*60000);
//	}
//	public void stopSound(){
//		mediaStopHandler.removeMessages(0);
//		onMediaEndListener.onMediaEnd();
//		mMediaAdapterRain.stopPlayer();
//		mMediaAdapterMount.stopPlayer();
//		mMediaAdapterSea.stopPlayer();
//	}
//	public void setOnMediaEndListener(OnMediaEndListener listener){
//		onMediaEndListener = listener;
//	}
//	@Override
//	public void onDestroy() {
//		mMediaAdapterRain.stopPlayer();
//		mMediaAdapterMount.stopPlayer();
//		mMediaAdapterSea.stopPlayer();
//		super.onDestroy();
//	}
//	public boolean isPlaying(){
//		return mMediaAdapterMount.isPlaying() || mMediaAdapterRain.isPlaying() || mMediaAdapterSea.isPlaying();
//	}
//	private Handler mediaStopHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			stopSound();
//		};
//	};
//}
