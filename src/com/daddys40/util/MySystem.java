package com.daddys40.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MySystem {
	public final int LANGUAGE_EN = 0;
	public final int LANGUAGE_KR = 1;

	private final char HANGUL_BEGIN_UNICODE = 44032; // 가
	private final char HANGUL_LAST_UNICODE = 55203; // 힣
	private final char HANGUL_BASE_UNIT = 588; // 각자음 마다 가지는 글자수

	private final int IMAGE_MAX_SIZE = 720;
	private static Context mContext;

	protected DisplayMetrics outMetrics = null;

	// 자음
	private final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ',
			'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

	static final MySystem mMySystem = new MySystem();

	private MySystem() {

	}

	public static MySystem getInstance() {
		return mMySystem;
	}

	public static void init(Context context) {
		mContext = context;
	}

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
			+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	public void setScreen(Activity act, boolean hideAnn, boolean hideTitle) {
		if (hideAnn == true) {
			act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		if (hideTitle == true) {
			act.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
	}

	public void Vibrate(long millisec) {
		Vibrator vib = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
		vib.vibrate(millisec);
	}

	public void Vibrate(long[] pattern, int repeat) {
		Vibrator vib = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
		vib.vibrate(pattern, repeat);
	}

	public int getLCDWidth() {
		Display display = ((WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth();

		return width;
	}

	public int getLCDHeight() {
		Display display = ((WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE)).getDefaultDisplay();
		int height = display.getHeight();

		return height;
	}

	public void hideKeypad(EditText edit) {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	public void showKeypad(EditText edit) {
		edit.requestFocus();
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
		imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
	}

	public boolean isWifiConnected() {
		ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
		return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
	}

	public boolean is3GConnected() {
		ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
		return manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
	}

	public boolean isNetworkConnected() {
		boolean bResult = true;

		if (!isWifiConnected() && !is3GConnected())
			bResult = false;

		return bResult;
	}

	public boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public void showDialog(final Activity act, String msg, String ok) {
		new AlertDialog.Builder(mContext).setMessage(msg).setPositiveButton(ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}

	public Bitmap rotateBitmap(String imagePath) {
		final int IMAGE_MAX_SIZE = 720;
		Bitmap srcImage = null;
		Bitmap resizeImage = null;
		File f = new File(imagePath);

		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			srcImage = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();

			// 이미지를 상황에 맞게 회전시킨다
			ExifInterface exif = new ExifInterface(imagePath);
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int exifDegree = exifOrientationToDegrees(exifOrientation);
			resizeImage = rotate(srcImage, exifDegree);
		}
		catch (IOException e) {
		}
		return resizeImage;
	}

	public int exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		}
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		}
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}

	public Bitmap rotate(Bitmap bitmap, int degrees) {
		if (degrees != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

			try {
				Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

				if (bitmap != converted) {
					bitmap.recycle();
					bitmap = converted.copy(converted.getConfig(), true);
					converted.recycle();
				}
			}
			catch (OutOfMemoryError ex) {
				// 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
			}
		}
		return bitmap;
	}

	public boolean saveBitmapToFile(Bitmap srcBitmap, String destPath) {
		boolean bResult = true;

		FileOutputStream outStream = null;
		File file = new File(destPath);
		String fileType = "";

		try {
			outStream = new FileOutputStream(file);
			fileType = getFileTypeForUpload(file.getPath()); // 파일 타입 가져오기

			if (fileType.equals("image/jpeg"))
				srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			else if (fileType.equals("image/png"))
				srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);

			outStream.flush();
			outStream.close();

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			bResult = false;

		}
		catch (IOException e) {
			e.printStackTrace();
			bResult = false;
		}

		return bResult;
	}

	public String getFileTypeForUpload(String fileName) {
		String ext = "";
		String result = "";
		String s = fileName;
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}

		if (ext.equals("jpg") || ext.equals("jpeg"))
			result = "image/jpeg";
		else if (ext.equals("png"))
			result = "image/png";

		return result;
	}

	public int getAge(Date birth) {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		int year, month, date, age;

		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH) + 1;
		date = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birth);

		age = year - cal.get(Calendar.YEAR);

		if ((month < cal.get(Calendar.MONTH))
				|| ((month == cal.get(Calendar.MONTH)) && (date < cal.get(Calendar.DAY_OF_MONTH)))) {
			--age;
		}

		return age;
	}

	public String getSystemLanguage() {
		String result = "EN";

		String locale = Locale.getDefault().getDisplayLanguage();

		if (locale.equals(Locale.KOREA.getDisplayLanguage())) {
			result = "KR";
		}
		else {
			result = "EN";
		}

		return result;
	}

	public int getSystemLan() {
		int result = LANGUAGE_KR;

		String locale = Locale.getDefault().getDisplayLanguage();

		if (locale.equals(Locale.KOREA.getDisplayLanguage())) {
			result = LANGUAGE_KR;
		}
		else {
			result = LANGUAGE_EN;
		}

		return result;
	}

	public String getMcc() {
		TelephonyManager tel = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
		String networkOperator = tel.getNetworkOperator();
		String mcc = "";

		if (!networkOperator.equals("")) {
			mcc = networkOperator.substring(0, 3);
		}

		Log.d("buddyup", mcc);
		return mcc;
	}

	@SuppressWarnings("deprecation")
	public String getCurrentTime() {
		String JELLY_BEAN = "16";
		// Date date = new Date(System.currentTimeMillis());
		Date date = null;
		if (android.os.Build.VERSION.SDK.equals(JELLY_BEAN)) { // Jelly bean인 경우
																// 13가 앞서있음.
			date = toGMT(new Date(System.currentTimeMillis() + 130000));
		}
		else {
			date = toGMT(new Date(System.currentTimeMillis()));
		}

		String year = Integer.toString(date.getYear() + 1900);
		String mon = Integer.toString(date.getMonth() + 1);
		String day = Integer.toString(date.getDate());
		String hour = Integer.toString(date.getHours());
		String min = Integer.toString(date.getMinutes());
		String sec = Integer.toString(date.getSeconds());

		String result = getAdd0(year) + getAdd0(mon) + getAdd0(day) + getAdd0(hour) + getAdd0(min) + getAdd0(sec);

		return result;
	}

	private Date toGMT(Date date) {
		TimeZone timeZone = TimeZone.getTimeZone("GMT");
		return changeTimeZone(date, timeZone);
	}

	private Date changeTimeZone(Date date, TimeZone zone) {
		Calendar srcDate = Calendar.getInstance(zone);
		srcDate.setTimeInMillis(date.getTime());

		Calendar desDate = Calendar.getInstance();
		desDate.set(Calendar.YEAR, srcDate.get(Calendar.YEAR));
		desDate.set(Calendar.MONTH, srcDate.get(Calendar.MONTH));
		desDate.set(Calendar.DAY_OF_MONTH, srcDate.get(Calendar.DAY_OF_MONTH));
		desDate.set(Calendar.HOUR_OF_DAY, srcDate.get(Calendar.HOUR_OF_DAY));
		desDate.set(Calendar.MINUTE, srcDate.get(Calendar.MINUTE));
		desDate.set(Calendar.SECOND, srcDate.get(Calendar.SECOND));
		desDate.set(Calendar.MILLISECOND, srcDate.get(Calendar.MILLISECOND));

		return desDate.getTime();
	}

	private String getAdd0(String time) {
		if (Integer.parseInt(time) < 10)
			time = "0" + time;

		String result = time;

		return result;
	}

	public String getMD5Hash(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String md5 = number.toString(16);
			// When MD5 has "0" at first position, the length of MD5 is 31.
			while (md5.length() < 32)
				md5 = "0" + md5;
			return md5;
		}
		catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public String getMyPhoneNumberList() {
		String projection[] = new String[] { ContactsContract.Data._ID, ContactsContract.Data.CONTACT_ID,
				ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1, // Phone
																				// Number
				ContactsContract.Data.DISPLAY_NAME };

		final String token = ",";

		String result = "";

		Cursor contacts = mContext.getContentResolver().query(
				/* url */ContactsContract.Data.CONTENT_URI,
				/* projection */projection,
				/* selection */ContactsContract.Data.MIMETYPE + "='"
						+ ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'",
				/* selectionArgs */null,
				/* sortOrder */ContactsContract.Data.DISPLAY_NAME + "," + ContactsContract.Data._ID
						+ " COLLATE LOCALIZED ASC");

		if (contacts == null)
			return result;

		while (contacts.moveToNext()) {
			String mimeType = contacts.getString(contacts.getColumnIndex(ContactsContract.Data.MIMETYPE));

			if (mimeType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
				String value = null;
				value = contacts.getString(contacts.getColumnIndex(ContactsContract.Data.DATA1));
				if (value != null) {
					if (result.equals("")) {
						result = value;
					}
					else {
						result += token + value;
					}
				}
			}
		}
		contacts.close();
		return result;
	}

	public boolean downloadFile(String downURL, String folderName, String fileName) {
		boolean nRet = true;

		try {
			File fDir = new File(folderName);

			if (fDir.exists() == false) {
				boolean nFileRet = new File(folderName).mkdir();
				if (nFileRet == false)
					return nFileRet;
			}

			String saveLocalPath = folderName + fileName;

			URL url = new URL(downURL);
			File file = new File(saveLocalPath);

			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();

		}
		catch (IOException e) {
			Log.d("ImageManager", "Error: " + e);
			nRet = false;
		}
		return nRet;
	}

	public String readTextFromWeb(String txtUrl) {
		String result = "";

		try {
			URL url = new URL(txtUrl);
			URLConnection connection = url.openConnection();

			InputStream is = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			String buf = "";
			while ((buf = br.readLine()) != null) {
				result += buf + "\n";
			}

		}
		catch (MalformedURLException mue) {
		}
		catch (IOException ioe) {
		}

		return result;
	}

	public String readDBFromWeb(String txtUrl) {
		String result = "";

		try {
			URL url = new URL(txtUrl);
			URLConnection connection = url.openConnection();

			InputStream is = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			String buf = "";
			while ((buf = br.readLine()) != null) {
				result += buf;
			}

		}
		catch (MalformedURLException mue) {
		}
		catch (IOException ioe) {
		}

		return result;
	}

	// 몇초전에 등록했는지 가져오자.
	private long getSecAgo(Date curDate, Date agoDate) {
		long startTime = agoDate.getTime();
		long endTime = curDate.getTime();

		long mills = endTime - startTime;

		// 차이를 "초"로 리턴
		return mills / 1000;
	}

	// 몇분전에 등록했는지 가져오자.
	private long getMinAgo(Date curDate, Date agoDate) {
		long startTime = agoDate.getTime();
		long endTime = curDate.getTime();

		long mills = endTime - startTime;

		// 차이를 "분"으로 리턴
		return mills / 60000;
	}

	public String calcuDate(String dateTime) {
		String result = "";
		int lan = getSystemLan();

		Date agoDate = null;
		Date curDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			agoDate = ((Date) formatter.parse(dateTime));
			curDate = formatter.parse(formatter.format(new Date()));
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		long minAgo = getMinAgo(curDate, agoDate);

		if (minAgo < 1) {
			long secAgo = getSecAgo(curDate, agoDate);
			if (secAgo < 0)
				secAgo = 0;

			if (lan == LANGUAGE_KR)
				result = Long.toString(secAgo) + "초 전";
			else
				result = Long.toString(secAgo) + "s ago";
		}
		else if (minAgo < 60) {
			if (lan == LANGUAGE_KR)
				result = Long.toString(minAgo) + "분 전";
			else
				result = Long.toString(minAgo) + "m ago";

		}
		else if (minAgo < 60 * 24) {
			if (lan == LANGUAGE_KR)
				result = Long.toString(minAgo / 60) + "시간 전";
			else
				result = Long.toString(minAgo / 60) + "h ago";
		}
		else {
			Calendar calendar = Calendar.getInstance();
			int currentYear = calendar.get(Calendar.YEAR);

			int agoYear = agoDate.getYear() + 1900;
			int agoMon = agoDate.getMonth() + 1;
			int agoDay = agoDate.getDate();

			if (lan == LANGUAGE_KR) {
				result = Integer.toString(agoMon) + "월 " + Integer.toString(agoDay) + "일 ";
				if (currentYear != agoYear)
					result = Integer.toString(agoYear) + "년 " + result;

			}
			else {
				String month[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
				String day = "";
				if (agoDay == 1 || agoDay == 21 || agoDay == 31)
					day = agoDay + "st";
				else if (agoDay == 2 || agoDay == 22)
					day = agoDay + "nd";
				else if (agoDay == 3 || agoDay == 23)
					day = agoDay + "rd";
				else
					day = Integer.toString(agoDay) + "th";

				result = month[agoMon - 1] + " " + day;

				if (currentYear != agoYear) {
					result = result + " " + Integer.toString(agoYear);
				}
			}
		}

		return result;
	}

	public boolean isHangul(char c) {
		return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
	}

	private boolean isInitialSound(char searchar) {
		for (char c : INITIAL_SOUND) {
			if (c == searchar) {
				return true;
			}
		}
		return false;
	}

	private char getInitialSound(char c) {
		int hanBegin = (c - HANGUL_BEGIN_UNICODE);
		int index = hanBegin / HANGUL_BASE_UNIT;
		return INITIAL_SOUND[index];
	}

	public boolean matchString(String value, String search) {
		int t = 0;
		int seof = value.length() - search.length();
		int slen = search.length();
		if (seof < 0)
			return false; // 검색어가 더 길면 false를 리턴한다.
		for (int i = 0; i <= seof; i++) {
			t = 0;
			while (t < slen) {
				// 만약 현재 char이 초성이고 value가 한글이면
				if (isInitialSound(search.charAt(t)) == true && isHangul(value.charAt(i + t))) {

					// 각각의 초성끼리 같은지 비교한다
					if (getInitialSound(value.charAt(i + t)) == search.charAt(t)) {
						t++;
					}
					else {
						break;
					}
				}

				// char이 초성이 아니라면
				else {
					// 그냥 같은지 비교한다.
					if (value.charAt(i + t) == search.charAt(t)) {
						t++;
					}
					else {
						break;
					}
				}
			}
			if (t == slen)
				return true; // 모두 일치한 결과를 찾으면 true를 리턴한다.
		}
		return false; // 일치하는 것을 찾지 못했으면 false를 리턴한다.
	}

	public String getCheckSMSReceiverNum(String strRcvNum) {
		String strSumReceiverNum = "";

		int nSenderNum = strRcvNum.length();

		for (int i = 0; i < nSenderNum; i++) {
			if (Character.isDigit(strRcvNum.charAt(i))) {
				strSumReceiverNum += strRcvNum.charAt(i);
			}
		}

		// 숫자가 82, 01X 인 경우만 통과하도록
		if (strSumReceiverNum.startsWith("01") || strSumReceiverNum.startsWith("82")) {
			return strSumReceiverNum;
		}
		else {
			return "";
		}
	}

	public String getBuddyUpDate() {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			date = formatter.parse(formatter.format(new Date()));
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		int month = date.getMonth() + 1;
		int day = date.getDate();
		int week = date.getDay();

		int lan = getSystemLan();

		String result = "";

		if (lan == LANGUAGE_KR) {
			result = Integer.toString(month) + "월 " + Integer.toString(day) + "일,";
		}
		else {
			String strDay[] = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
			result = strDay[week] + ",";
		}

		return result;
	}

	public String getBuddyUpDay() {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			date = formatter.parse(formatter.format(new Date()));
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		int month = date.getMonth() + 1;
		int day = date.getDate();
		int week = date.getDay();

		int lan = getSystemLan();

		String result = "";

		if (lan == LANGUAGE_KR) {
			String strDay[] = { "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" };
			result = strDay[week];
		}
		else {
			String strDate[] = { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER",
					"OCTOBER", "NOVEMBER", "DECEMBER" };
			result = strDate[month - 1] + " " + Integer.toString(day);
		}

		return result;
	}

	public void createDirectory(String dirName) {
		File fDir = new File(dirName);
		if (fDir == null)
			return;

		if (fDir.exists() == false) {
			boolean nFileRet = new File(dirName).mkdir();

			// if(nFileRet == true)
			// setDirectoryAuthority(dirName, authority);

		}
	}

	public boolean isFile(String path) {
		boolean bResult = false;
		File file = new File(path);

		if (file.exists())
			bResult = true;

		return bResult;
	}

	public String getTimeZone() {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("Z");

		TimeZone mytimezone = TimeZone.getDefault();
		String locationText = mytimezone.getID();
		Time now = new Time(locationText);

		mytimezone = TimeZone.getTimeZone(now.getCurrentTimezone().toString());

		df.setTimeZone(mytimezone);

		String strTimezone = df.format(date).toString();
		strTimezone = strTimezone.substring(0, 3).replace("0", "").replace("+", "");

		return strTimezone;
	}

	public boolean isSupportFormat(String fileName) {
		boolean result = false;

		String ext = "";
		String s = fileName;
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}

		if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
			result = true;
		}

		return result;
	}

	public Bitmap nonRotateBitmap(String imagePath) {
		Bitmap srcImage = null;
		Bitmap resizeImage = null;
		File f = new File(imagePath);

		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			srcImage = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
			// 이미지를 상황에 맞게 회전시킨다
			ExifInterface exif = new ExifInterface(imagePath);
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int exifDegree = exifOrientationToDegrees(exifOrientation);
			// resizeImage = rotate(srcImage, exifDegree);

			// 회전안하도록 수정
			resizeImage = rotate(srcImage, 0);
		}
		catch (IOException e) {
		}
		return resizeImage;
	}

	public void refreshSD() {
		Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
				+ Environment.getExternalStorageDirectory()));
		mContext.sendBroadcast(intent);
	}

	public int getSampliSize(int width, int height) {

		float dw = getLCDWidth();
		float dh = getLCDWidth();

		// 가로/세로 축소 비율 취득
		int widthtRatio = (int) Math.ceil(width / dw);
		int heightRatio = (int) Math.ceil(height / dh);

		// 초기 리사이즈 비율
		int sampleSize = 1;

		// 가로 세로 비율이 화면보다 큰경우에만 처리
		if (widthtRatio > 1 && height > 1) {
			if (widthtRatio > heightRatio) {
				// 가로 축소 비율이 큰 경우
				sampleSize = widthtRatio;
			}
			else {
				// 세로 축소 비율이 큰 경우
				sampleSize = heightRatio;
			}
		}

		return sampleSize;
	}

	public Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {
		if (degrees != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

			try {
				Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

				if (bitmap != b2) {
					bitmap.recycle();
					bitmap = b2;
				}
			}
			catch (OutOfMemoryError e) {
				// 메모리 부족에러시, 원본을 반환
			}
		}

		return bitmap;
	}

	public int GetExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;

		try {
			exif = new ExifInterface(filepath);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			if (orientation != -1) {
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;

				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}

		return degree;
	}

	public String calcuDateYearMonthDay(String dateTime) {
		String result = "";
		int lan = getSystemLan();

		Date agoDate = null;
		Date curDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
			agoDate = ((Date) formatter.parse(dateTime));
			curDate = formatter.parse(formatter.format(new Date()));
		}
		catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);

		int agoYear = agoDate.getYear() + 1900;
		int agoMon = agoDate.getMonth() + 1;
		int agoDay = agoDate.getDate();

		result = Integer.toString(agoYear) + ". " + Integer.toString(agoMon) + ". " + Integer.toString(agoDay);

		return result;
	}

	// Calendar 위해 추가된 함수
	public String getCalendarBuddyUpDate(Calendar cal) {
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int week = cal.get(Calendar.DAY_OF_WEEK);

		int lan = getSystemLan();

		String result = "";

		if (lan == LANGUAGE_KR) {
			result = Integer.toString(month) + "월 " + Integer.toString(day) + "일,";
		}
		else {
			String strDay[] = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
			result = strDay[week - 1] + ",";
		}

		return result;
	}

	public String getCalendarBuddyUpDay(Calendar cal) {
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int week = cal.get(Calendar.DAY_OF_WEEK);

		int lan = getSystemLan();

		String result = "";

		if (lan == LANGUAGE_KR) {
			String strDay[] = { "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" };
			result = strDay[week - 1];
		}
		else {
			String strDate[] = { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER",
					"OCTOBER", "NOVEMBER", "DECEMBER" };
			result = strDate[month - 1] + " " + Integer.toString(day);
		}

		return result;
	}
}
