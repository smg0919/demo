package com.syjjkj.kxwq.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOperation {
	public static String sdFile;
	public static String FOLDER;
	public static String FILE_NAME = System.currentTimeMillis() + "_img.jpg";

	/**
	 * 保存图片到指定目录，时间命名
	 * 
	 * @param context
	 * @param photo
	 * @return
	 */
	public static File saveImg(Context context, byte[] photo) {
		FileOutputStream fileOutputStream = null;
		File file = null;
		if (isSDCardExist()) {
			sdFile = Environment.getExternalStorageDirectory().getPath();
		} else {
			sdFile = context.getFilesDir().getAbsolutePath();
		}
		FOLDER = sdFile + "/CarCircle/";
		String fileName = FOLDER + System.currentTimeMillis() + "_img.jpg";
		// if (isSDCardExist()) {
		try {
			File folder = new File(FOLDER);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			file = new File(fileName);
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(photo);
			fileOutputStream.flush();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// }
		return file;
	}

	public static boolean isSDCardExist() {

		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public static void recursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				recursionDeleteFile(f);
			}
			file.delete();
		}
	}

	/**
	 * 将图片保存在指定目录下，指定图片名字
	 * 
	 * @param context
	 * @param photo
	 * @param picName
	 * @return
	 */
	public static File saveImg(Context context, byte[] photo, String picName) {
		FileOutputStream fileOutputStream = null;
		File file = null;
		if (isSDCardExist()) {
			sdFile = Environment.getExternalStorageDirectory().getPath();
		} else {
			sdFile = context.getFilesDir().getAbsolutePath();
		}
		FOLDER = sdFile + "/CarCircle/";
		String fileName = FOLDER + picName;
		// if (isSDCardExist()) {
		try {
			File folder = new File(FOLDER);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			file = new File(fileName);
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(photo);
			fileOutputStream.flush();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// }
		return file;
	}

	/**
	 * 将图片保存在指定目录下
	 * 
	 * @param photo
	 * @param picName
	 * @return
	 */
	public static File savePic(byte[] photo, String picPath) {
		FileOutputStream fileOutputStream = null;
		File folder = null;
		Log.e("Log", "图片大小="+photo.length);
		try {
			folder = new File(picPath);
			if(folder.exists()){
				folder.delete();
			}else{
				folder.mkdirs();
				folder.delete();
			}

			fileOutputStream = new FileOutputStream(folder);
			fileOutputStream.write(photo);
			fileOutputStream.flush();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return folder;
	}
}
