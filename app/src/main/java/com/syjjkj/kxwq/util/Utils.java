package com.syjjkj.kxwq.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

public class Utils {
	public static String path = null;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final int PHOTORESOULT =3;// 结果

	/**
	 * 获取app的存储目录
	 * <p>
	 * 一般情况下是这样的/storage/emulated/0/Android/data/包名/
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppDir(Context context) {
		return (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? (Environment
				.getExternalStorageDirectory().getPath() + "/Android/data/")
				: (context.getCacheDir().getPath()))
				+ context.getPackageName();
	}
	
	public static String getVoicePath(Context context) {
		File voiceFile = new File(Utils.getAppDir(context) + File.separator
				+ "voice");
		if (!voiceFile.exists()) {
			voiceFile.mkdir();
		}
		File file = new File(voiceFile + File.separator
				+ System.currentTimeMillis() + ".amr");
		return file.getAbsolutePath();
	}

	public static String getPicPath(Context context) {
		File picFile = new File(Utils.getAppDir(context) + File.separator
				+ "pic");
		if (!picFile.exists()) {
			picFile.mkdir();
		}
		File file = new File(picFile + File.separator
				+ System.currentTimeMillis() + ".png");
		return file.getAbsolutePath();
	}

	public static String getRealPathFromURI(Activity activity, Uri contentUri) {
		String realPath = "";
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			realPath = PhotoUtil.getPath(activity, contentUri);
		}else{
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			Cursor c = activity.getContentResolver().query(contentUri,
					filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			realPath = c.getString(columnIndex);
		}
		return realPath;
    }
	
	public static void copyFile(String orgFile, String newFile) {
		// 拷贝
		try {
			InputStream inStream = new FileInputStream(orgFile);
			FileOutputStream fos = new FileOutputStream(newFile); // 定义输出流
			byte[] bt = new byte[4096];
			int len = -1;
			while ((len = inStream.read(bt)) != -1) {
				fos.write(bt, 0, len);
			}
			inStream.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 开启一个系统页面来裁剪传进来的照片
	 * 
	 * @param Uri
	 *            uri 需要裁剪的照片的URI值
	 */
	public static void startPhotoZoom(Activity activity, Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, PHOTORESOULT);
	}
	public static void startPhotoZoom(Activity activity, Uri uri, int aspectX, int aspectY, int outputX) {
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setType("image/*");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		activity.startActivityForResult(intent, PHOTORESOULT);
	}
	public static void startPhotoZoom1(Activity activity, Uri uri, int aspectX, int aspectY, int outputX) {
		/*
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		intent.putExtra("scale", false);
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		// outputX outputY 是裁剪图片宽高
//		intent.putExtra("outputX", outputX);
//		intent.putExtra("outputY", (outputX/aspectX)*aspectY);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, PHOTORESOULT);
		*/
		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 400);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		activity.startActivityForResult(intent, PHOTORESOULT);
	}
	
	public static Intent photo(Context context) {

		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			String filePath = getAppDir(context) + "/image/";
			File files = new File(filePath);
			if (!files.exists()) {
				files.mkdirs();
			}

			File file = new File(filePath, String.valueOf(System
					.currentTimeMillis()) + ".jpg");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			path = file.getPath();
			Uri imageUri = Uri.fromFile(file);
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		} else {
			ToastUtil.makeShortText(context, "请插入内存卡");
		}
		return openCameraIntent;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取设备ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (!StringUtil.isEmpty(manager.getDeviceId())) {
			return manager.getDeviceId();
		} else {
			final String tmDevice, tmSerial, tmPhone, androidId;

			tmDevice = "" + manager.getDeviceId();

			tmSerial = "" + manager.getSimSerialNumber();

			androidId = ""
					+ android.provider.Settings.Secure.getString(
							context.getContentResolver(),
							android.provider.Settings.Secure.ANDROID_ID);

			UUID deviceUuid = new UUID(androidId.hashCode(),
					((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

			String uniqueId = deviceUuid.toString();

			LogUtil.e("android deviceId is null, uuid=" + uniqueId);
			return uniqueId;
		}
	}

	/**
	 * 获取应用AppKey
	 * 
	 * @return
	 */
	public static String getAppKey(Context context) {
		ApplicationInfo applicationInfo = null;
		String appKey = null;
		try {
			applicationInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			appKey = applicationInfo.metaData.getString("app_key");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		LogUtil.i("app_key=" + appKey);
		return appKey;
	}

	public static String getAppSecret(Context context) {
		ApplicationInfo applicationInfo = null;
		String appSecret = null;
		try {
			applicationInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			appSecret = applicationInfo.metaData.getString("app_secret");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		LogUtil.i("app_secret=" + appSecret);
		return appSecret;
	}

	/**
	 * sdcard 是否存在
	 * 
	 * @return
	 */
	public static boolean hasSDCard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * bitmap转成byte数组
	 * 
	 * @param avatar
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap avatar) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		avatar.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();

	}

	/**
	 * 此方法用于现场互动界面,格式固定. e.g.day=18,mounth=九月.
	 * 
	 * @param day
	 * @param month
	 * @return
	 */
	public static boolean isToday(String day, String month) {
		Calendar c = Calendar.getInstance();
		int m = c.get(Calendar.MONTH);
		int d = c.get(Calendar.DAY_OF_MONTH);
		if (!StringUtil.isEmpty(day) && !StringUtil.isEmpty(month)) {
			if (month.equals(Num2Hanzi(m)) && String.valueOf(d).equals(day)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 转化月份.数字转汉字
	 * 
	 * @param num
	 * @return
	 */
	public static String Num2Hanzi(int num) {
		StringBuffer sb = new StringBuffer();
		String[] strings = new String[] { "一", "二", "三", "四", "五", "六", "七",
				"八", "九", "十", "十一", "十二" };
		return sb.append(strings[num]).append("月").toString();
	}

	/**
	 * 转化月份.汉字转数字
	 * 
	 * @param hanzi
	 * @return
	 */
	public static int Hanzi2Num(String hanzi) {
		int num = 0;
		int[] numbers = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		String[] strings = new String[] { "一月", "二月", "三月", "四月", "五月", "六月",
				"七月", "八月", "九月", "十月", "十一月", "十二月" };
		for (int i = 0; i < strings.length; i++) {

			if (hanzi.equals(strings[i])) {
				num = numbers[i];
			}
		}
		return num;
	}

	/**
	 * 获取手机上的联系人电话.
	 * 
	 * @return
	 */
	public static void getContacts(Context context,
			final IContactCallback callback) {
		final Context ctx = context;
		new Thread(new Runnable() {
			@Override
			public void run() {

				StringBuffer sb = new StringBuffer();
				Cursor cursor = ctx.getContentResolver().query(
						ContactsContract.Contacts.CONTENT_URI, null, null,
						null, null);
				if (cursor != null) {
					while (cursor.moveToNext()) {

						String contactId = cursor.getString(cursor
								.getColumnIndex(ContactsContract.Contacts._ID));
						String displayName = cursor.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						int numberCount = cursor.getInt(cursor
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
						LogUtil.e("联系人: " + displayName);
						if (numberCount > 0) {
							Cursor c = ctx
									.getContentResolver()
									.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
											null,
											ContactsContract.CommonDataKinds.Phone.CONTACT_ID
													+ "=" + contactId, null,
											null);
							if (c != null) {
								while (c.moveToNext()) {
									String number = c.getString(c
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
									if (number.contains(" ")) {
										number = number.replace(" ", "");
									}
									if (number.contains("-")) {
										number = number.replace("-", "");
									}
									// 截取后11位.针对前面有+86前缀的.
									if (number.length() > 11) {
										number = number.subSequence(
												number.length() - 11,
												number.length()).toString();
									}

									LogUtil.e("联系电话: " + number);
									sb.append(number);
									sb.append(",");
								}
								c.close();
								c = null;
							}
						}
					}
					cursor.close();
					cursor = null;
				}
				if (sb.toString().endsWith(",")) {
					sb.delete(sb.length() - 1, sb.length());
				}
				callback.callback(sb.toString());
			}

		}).start();
	}

	public interface IContactCallback {
		void callback(String string);
	}

	/**
	 * 根据两点的经纬度计算距离.
	 * 
	 * @param lat1
	 * @param lat2
	 * @param lon1
	 * @param lon2
	 * @return 单位:m
	 */
	public static double getDistatce(double lat1, double lat2, double lon1,
			double lon2) {
		double EARTH_RADIUS = 6378.137 * 1000; // 赤道半径(单位:m)
		double distance = 0.0;
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLon = (lon2 - lon1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(lat1 * Math.PI / 180)
				* Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)))
				* EARTH_RADIUS;
		return distance;
	}

	/**
	 * 存储到scard卡路径
	 * 
	 * @author wjh
	 * @param content
	 * @param path
	 *            自定义路径"eg : /yitu/pictures"
	 * @return 存储路径
	 */
	@SuppressLint("NewApi")
	public static String ScardPathUtil(Context content, String path) {
		String capturePath = null;
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File dateDir = Environment.getExternalStorageDirectory();
			String out_file_path = null;
			try {
				out_file_path = dateDir.getCanonicalPath() + path;
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (out_file_path.isEmpty()) {
				Toast.makeText(content, "路径为空", Toast.LENGTH_SHORT).show();
			} else {
				File dir = new File(out_file_path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				capturePath = out_file_path + System.currentTimeMillis()
						+ ".jpg";
			}
		} else {
			Toast.makeText(content, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
		}
		return capturePath;
	}

//	/**
//	 * 获取指定目录的图片
//	 *
//	 * @return
//	 */
//	@SuppressLint("SdCardPath")
//	public static String[] ListFile() {
//		File file = PathUtil.getInstance().getImagePath();
//		File[] f = file.listFiles();
//		String Path[] = new String[f.length];
//
//		for (int i = 0; i < f.length; i++)
//
//		{
//
//			Path[i] = f[i].getPath();
//		}
//
//		return Path;
//
//	}
/**
 * 删除指定文件目录
 * @param path
 */
	public static void delefile(String path) {
		File file = new File(path);
		file.delete();
	}
	

}
