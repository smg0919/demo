package com.syjjkj.kxwq.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

public class ImageOperate {
	public static String FILE_NAME = System.currentTimeMillis() + "_img.jpg";

	/**
	 * 功能说明：byte数组转化成Bitmap
	 * 
	 * @param temp
	 * @return Bitmap
	 * @author liumc
	 * @since 2013-5-7 下午3:39:02
	 */

	public static Bitmap getBitmapFromByte(byte[] temp) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
		return bitmap;
	}

	/**
	 * 功能说明：Bitmap转化成byte数组
	 * 
	 * @param bitmap
	 * @param parame
	 *            -->后缀名
	 * @return byte[]
	 * @author liumc
	 * @since 2013-5-7 下午3:39:47
	 */

	public static byte[] getBitmapByte(Bitmap bitmap, String parame) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			if (parame.equals("png")) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
		} catch (NullPointerException e) {
			return null;
		}
		return out.toByteArray();
	}

	public static void loadBitmapFromUrl(final Handler handler, final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					HttpGet httpRequest = null;
					URL m = new URL(url);
					try {
						httpRequest = new HttpGet(m.toURI());
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}

					HttpClient httpclient = new DefaultHttpClient();
					HttpResponse response = (HttpResponse) httpclient
							.execute(httpRequest);

					HttpEntity entity = response.getEntity();
					BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(
							entity);
					InputStream instream = bufHttpEntity.getContent();
					Bitmap bitmap = BitmapFactory.decodeStream(instream);
					instream.close();
					Message msg = new Message();
					msg.obj = bitmap;
					handler.sendMessage(msg);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static Bitmap loadBitmapFromUrl(final String url) {
		Bitmap bitmap = null;
		try {

			HttpGet httpRequest = null;
			URL m = new URL(url);
			try {
				httpRequest = new HttpGet(m.toURI());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest);

			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			InputStream instream = bufHttpEntity.getContent();
			bitmap = BitmapFactory.decodeStream(instream);
			instream.close();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.RGB_565);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 20;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.RGB_565);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		// final float roundPx = 20;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx,
												int width, int height) {
		Bitmap bm = Bitmap.createScaledBitmap(bitmap, width, height, true);
		Bitmap output = Bitmap.createBitmap(width, height, Config.RGB_565);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bm, rect, rect, paint);

		return output;
	}

	/**
	 * 使用BitmapFactory.Options的inSampleSize参数来缩放
	 * 
	 * @param bitmap
	 * @param h
	 * @param w
	 * @return
	 */

	@SuppressWarnings("deprecation")
	public static Drawable resizeImage2(String path, int width, int height) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 不加载bitmap到内存中
		BitmapFactory.decodeFile(path, options);
		int outWidth = options.outWidth;
		int outHeight = options.outHeight;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 1;

		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) / 2;
			Log.d("图片缩放", "sampleSize = " + sampleSize);
			options.inSampleSize = sampleSize;
		}

		options.inJustDecodeBounds = false;
		return new BitmapDrawable(BitmapFactory.decodeFile(path, options));
	}

	public static Bitmap CompressionBigBitmap(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;
		float ww = 480f;
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩

	}

	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static void destoryBimap(Bitmap photo) {
		if (photo != null && !photo.isRecycled()) {
			photo.recycle();
			photo = null;
		}
		System.gc();
	}

	public static Bitmap getBitmapByPath(String pathName, int rate) {
		// 读取文件到输入流
		FileInputStream fis = null;
		BitmapFactory.Options options = null;
		try {
			fis = new FileInputStream(pathName);
			options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = rate;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 使用位图工厂类将包含图片信息的输入流转换成位图
		return BitmapFactory.decodeStream(fis, null, options);
	}

	/**
	 * set image src
	 *
	 * @param imageView
	 * @param imagePath
	 */
	public static void setImageSrc(ImageView imageView, String imagePath) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = getImageScale(imagePath);
		Bitmap bm = BitmapFactory.decodeFile(imagePath, option);

		imageView.setImageBitmap(bm);
	}

	public static void setImageSrc(ImageView imageView, String imagePath,
								   int angle) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = getImageScale(imagePath);
		Bitmap bm = BitmapFactory.decodeFile(imagePath, option);
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
				bm.getHeight(), matrix, true);
		imageView.setImageBitmap(resizedBitmap);
	}

	public static ImageView setImageSrcBackground(Context context,
												  String imagePath, int angle) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = getImageScale(imagePath);
		Bitmap bm = BitmapFactory.decodeFile(imagePath, option);
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
				bm.getHeight(), matrix, true);
		int width = context.getResources().getDisplayMetrics().widthPixels;
		int height = (width * bm.getHeight()) / bm.getWidth();
		RelativeLayout.LayoutParams lp = new LayoutParams(width, height);
		ImageView imageView = new ImageView(context);
		BitmapDrawable bd = new BitmapDrawable(bm);
		imageView.setBackgroundDrawable(bd);
		imageView.setLayoutParams(lp);
		return imageView;
	}

	private static int IMAGE_MAX_WIDTH = 480;
	private static int IMAGE_MAX_HEIGHT = 960;

	/**
	 * scale image to fixed height and weight
	 * 
	 * @param imagePath
	 * @return
	 */
	private static int getImageScale(String imagePath) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		// set inJustDecodeBounds to true, allowing the caller to query the
		// bitmap info without having to allocate the
		// memory for its pixels.
		option.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, option);

		int scale = 1;
		while (option.outWidth / scale >= IMAGE_MAX_WIDTH
				|| option.outHeight / scale >= IMAGE_MAX_HEIGHT) {
			scale *= 2;
		}
		return scale;
	}

	public static File saveImg(Context context, byte[] photo) {
		FileOutputStream fileOutputStream = null;
		File file = null;
		String fileName = Utils.getAppDir(context) + "/image/" + FILE_NAME;
		if (Utils.hasSDCard()) {
			try {
				File folder = new File(fileName).getParentFile();
				if (!folder.exists()) {
					folder.mkdirs();
				}
				file = new File(fileName);
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(photo);
				fileOutputStream.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 压缩图片
	 * 
	 * @param path
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public static String revitionImageSize(String path, Context context)
			throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		// 不用insample size 进行压缩，但是用他生成一个比较大的预览
		int i = 0;
		Bitmap bitmapOrg = null;
		while (true) {
			if ((options.outWidth >> i <= 1024 * 2)
					&& (options.outHeight >> i <= 768 * 2)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmapOrg = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		
		float scaleWidth = ((float) 1024) / options.outWidth; 
		float scaleHeight = ((float) 768) / options.outHeight;
		float scaleAll = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleAll, scaleAll);
		
		// recreate the new Bitmap
		Bitmap bitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
				options.outWidth, options.outHeight, matrix, true);
		/* 为了测试内存崩溃用以下代码 */
		//BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		//Bitmap bitmap = BitmapFactory.decodeStream(in);
		//in.close();
		/* 为了测试内存崩溃用以上代码 */
		return saveBitmap(context, bitmap);
	}

	/**
	 * 保存图片到指定目录
	 * 
	 * @param bitmap
	 */
	public static String saveBitmap(Context context, Bitmap mBitmap) {
		// File mFile = new File(Environment.getRootDirectory()+"/oAimages");
		Date mDate = new Date();
		String imgName = Long.toString(mDate.getTime()) + ".jpg";
		File mFile = new File(Utils.getAppDir(context) + "/image/");
		if (!mFile.exists()) {
			mFile.mkdirs();
		}
		try {
			FileOutputStream out = new FileOutputStream(mFile.getAbsolutePath()
					+ "/" + imgName);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mFile = new File(mFile.getAbsolutePath() + "/" + imgName);
		return mFile.getAbsolutePath();
	}

	
	
}
