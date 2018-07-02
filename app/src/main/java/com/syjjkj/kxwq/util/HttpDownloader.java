package com.syjjkj.kxwq.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownloader {
	private URL url = null;

	public int downfile(String urlStr, String path) {

			try {
				InputStream input = null;
				input = getInputStream(urlStr);
				File resultFile = write2SDFromInput(path,
						input);
				if (resultFile == null) {
					return 0;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return 0;
			}

		return 1;
	}

	// 由于得到一个InputStream对象是所有文件处理前必须的操作，所以将这个操作封装成了一个方法
	public InputStream getInputStream(String urlStr) throws IOException {
		InputStream is = null;
		try {
			url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			urlConn.setRequestProperty("Connection", "close");
			urlConn.connect();
			is = urlConn.getInputStream();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return is;
	}
	
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(String path, InputStream input) {
		File file = new File(path);
		OutputStream output = null;
		try {
			if(file.exists()){
				file.delete();
			}else{
				file.mkdirs();
				file.delete();
			}
			output = new FileOutputStream(new File(path));
			byte buffer[] = new byte[4 * 1024];

			while (true) {
				int temp = input.read(buffer, 0, buffer.length);
				if (temp == -1) {
					break;
				}
				output.write(buffer, 0, temp);
			}

			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

}
