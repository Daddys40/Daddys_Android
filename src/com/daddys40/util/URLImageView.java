package com.daddys40.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.ImageView;

public class URLImageView {
	private ImageView iv;
	private Bitmap bm;
	private String urlPath;
	private ProgressDialog pDlg;
	private Dialog netFailDlg;

	private int width = 500;
	private int height = 500;

	/**
	 * @author KimSC
	 * @param ImageView 의 아이디 입력
	 */
	public URLImageView(ImageView iv, Context mContext) {
		// TODO Auto-generated constructor stub
		this.iv = iv;
		// pDlg = new ProgressDialog(mContext);
		// pDlg.setTitle("조회중");
		// pDlg.setCancelable(false);
		// pDlg.setMessage("불러오는 중입니다...");
		// netFailDlg = new Dialog(mContext);
		// netFailDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// netFailDlg.setCancelable(false);
		// netFailDlg.setContentView(R.layout.netfail_dialog);
	}

	/**
	 * @author KimSC
	 * @param urlPath URL 경로 입력 ex) "http://ww.jpg" @
	 */
	public void setImageURL(String urlPath, int width, int height) {
		this.urlPath = urlPath;
		this.width = width;
		this.height = height;
		// pDlg.show();
		SetImageThread trd = new SetImageThread();
		trd.start();
	}

	class SetImageThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Config.RGB_565;
				options.inSampleSize = 4;
				options.inPurgeable = true;
				
				URL url = new URL(urlPath);
				URLConnection conn = url.openConnection();
				conn.connect();
				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
				bm = BitmapFactory.decodeStream(bis, null, options);
				bis.close();
				a.sendEmptyMessage(0);
			}
			catch (IOException e) {
				b.sendEmptyMessage(0);
			}
		}
	}

	private Handler a = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			iv.setImageBitmap(bm);
			iv.getLayoutParams().height = (iv.getWidth() * height) / width;
			// pDlg.dismiss();
			return false;
		}
	});

	private Handler b = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			pDlg.dismiss();
			// ((Button) netFailDlg.findViewById(R.id.netfail_dialog_btn_cancel)).setOnClickListener(new
			// OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// netFailDlg.dismiss();
			// }
			// });
			// ((Button) netFailDlg.findViewById(R.id.netfail_dialog_btn_retry)).setOnClickListener(new
			// OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// netFailDlg.dismiss();
			// SetImageThread trd = new SetImageThread();
			// trd.start();
			// }
			// });
			netFailDlg.show();
			return false;
		}
	});
}
