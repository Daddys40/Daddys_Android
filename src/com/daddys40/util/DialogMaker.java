package com.daddys40.util;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.view.GestureDetector;
import android.view.Window;
import android.view.WindowManager;
/**
 * Dialog 사용을 위한 래핑클래스
 * @author Kim
 *
 */
public class DialogMaker {
	static private Dialog dlg;
	static private DialogFragment dlgFrag;
 	public DialogMaker(Context mContext, int layoutId , double widthPercent, double heightPercent) {
		dlg = new Dialog(mContext);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setContentView(layoutId);
		dlg.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
//		dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
		MySystem.init(mContext);
		params.width = (int) (MySystem.getInstance().getLCDWidth() * widthPercent);
		params.height = (int) (MySystem.getInstance().getLCDHeight() * heightPercent);
		dlg.getWindow().setAttributes(params);
		dlg.setCancelable(true);
	}
 	public DialogMaker(Context mContext, int layoutId) {
		dlg = new Dialog(mContext);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setContentView(layoutId);
		dlg.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
//		dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
		MySystem.init(mContext);
		dlg.setCancelable(true);
	}
 	public Dialog getDialog(){
 		return dlg;
 	}
}
