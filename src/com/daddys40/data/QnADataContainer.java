package com.daddys40.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.daddys40.alarm.FatherAlarmManger;
import com.daddys40.util.LogUtil;
import com.daddys40.util.UserData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
/**
 * 컨텐츠를 담고 관리하는 클래스
 * @author Kim
 *
 */
public class QnADataContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private HashMap<Integer, ArrayList<QnAData>> mQnADataHashMap = new HashMap<>();
	private static QnADataContainer mQnADataContainer;

	private QnADataContainer() {
	}

	public static QnADataContainer getInstance() {
		if (mQnADataContainer == null)
			mQnADataContainer = new QnADataContainer();
		return mQnADataContainer;
	}

	public void addQnAData(QnAData qnaData) {
		if (mQnADataHashMap.get(qnaData.getWeek()) == null) {
			ArrayList<QnAData> qnaArrayList = new ArrayList<>();
			mQnADataHashMap.put(qnaData.getWeek(), qnaArrayList);
		}
		mQnADataHashMap.get(qnaData.getWeek()).add(qnaData);
	}

	public ArrayList<QnAData> getQnAArrayList(int week) {
		return mQnADataHashMap.get(week);
	}

	public boolean doSerialization() {
		LogUtil.e("doSerialization", "start");
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		File dirFile = new File(path + "/.daddys40");
		dirFile.mkdir();
		File file = new File(path + "/.daddys40/daddys40_1.ser");
		try {
			LogUtil.e("doSerialization", "file out");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
			objectOutputStream.writeObject(mQnADataContainer);
			objectOutputStream.flush();
			objectOutputStream.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void doDeserialization(Context context) {
		if (UserData.getInstance().isSerialized()) {
			ProgressDialog pd = ProgressDialog.show(context, "Loading", "Loding now...");
			LogUtil.e("deserialization", "start Deser");
			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
			File file = new File(path + "/.daddys40/daddys40_1.ser");
			try {
				ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
				mQnADataContainer = (QnADataContainer) objectInputStream.readObject();
				objectInputStream.close();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pd.dismiss();
		}
	}
}
