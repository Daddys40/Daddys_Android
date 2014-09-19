//package com.daddys40.sleep;
//
//import android.content.Context;
//import android.media.MediaPlayer;
//import android.os.Handler;
//import android.os.Message;
//
//public class MediaAdapter {
//	private MediaPlayer mMediaPlayer;
//
//	public MediaAdapter(Context context, int id) {
//		mMediaPlayer = MediaPlayer.create(context, id);
//		mMediaPlayer.setLooping(true);
//	}
//
//	public void startPlayer(int mintime) {
//		startPlayer();
//		stopPlayerDelayed(mintime);
//	}
//	public void startPlayer(){
//		mediaStopHandler.removeMessages(0);
//		mMediaPlayer.start();
//	}
////	private Thread mediaThread = new Thread(new Runnable() {
////		@Override
////		public void run() {
////				mMediaPlayer.start();
////		}
////	});
//	public boolean isPlaying(){
//		return mMediaPlayer.isPlaying();
//	}
//	private Handler mediaStopHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			stopPlayer();
//		};
//	};
//
//	public void stopPlayer() {
//		mediaStopHandler.removeMessages(0);
//		mMediaPlayer.stop();
//		try {
//			mMediaPlayer.prepare();
//		}
//		catch (Exception e) {
//		}
////		mediaThread.interrupt();
//	}
//	public void stopPlayerDelayed(int mintime){
//		mediaStopHandler.removeMessages(0);
//		mediaStopHandler.sendEmptyMessageDelayed(0, mintime * 60000);
//	}
//}
