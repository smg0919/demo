package com.syjjkj.kxwq.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.syjjkj.kxwq.R;

import net.tsz.afinal.FinalBitmap;

import java.io.File;

public class FinalBitmapUtil {
	static FinalBitmap fb;
	static Context mContext;
	private static FinalBitmapUtil instance;
	private static String cachePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ "bijia"
			+ File.separator
			+ "image";

	private FinalBitmapUtil(Context context) {
		if (fb == null) {
			mContext = context;
			fb = FinalBitmap.create(context);
			fb.configLoadingImage(R.drawable.icon_default_car);
			fb.configLoadfailImage(R.drawable.icon_default_car);
			fb.configMemoryCachePercent(0.3f);
			fb.configDiskCachePath(cachePath);
		}
	}

	private static FinalBitmapUtil create(Context context) {
		if (instance == null) {
			return new FinalBitmapUtil(context.getApplicationContext());
		} else {
			return instance;
		}
	}

	/**
	 * 用于头像显示 .已经定义好loadingbitmap 和failedbitmap.
	 * 
	 * @param mContext
	 * 
	 * @return
	 */
	public void displayForHeader(View view, String uri) {
		if (StringUtil.isEmpty(uri)) {
			ImageView iv = (ImageView) view;
			iv.setImageResource(R.drawable.icon_default_car);
		} else {
			fb.display(
					view,
					uri,
					((BitmapDrawable) mContext.getResources().getDrawable(
							R.drawable.icon_default_car)).getBitmap(),
					((BitmapDrawable) mContext.getResources().getDrawable(
							R.drawable.icon_default_car)).getBitmap());
		}

		// fb.configLoadingImage(R.drawable.icon_head_default);
		// fb.configLoadfailImage(R.drawable.icon_head_default);
		// fb.display(view, uri);
	}

	/**
	 * 用于头像显示 .已经定义好loadingbitmap 和failedbitmap.
	 * 
	 * @param view
	 * @param uri
	 *            图片地址
	 * @param defaultIcon
	 *            默认图片资源R.drawable.xxx，可以填0
	 */
	public void displayForHeader(View view, String uri, int defaultIcon) {
		if (!(defaultIcon > 0)) {
			defaultIcon = R.drawable.icon_default_car;
		}
		fb.configLoadingImage(defaultIcon);
		fb.configLoadfailImage(defaultIcon);
		fb.display(view, uri);
		if (StringUtil.isEmpty(uri)) {
			ImageView iv = (ImageView) view;
			iv.setImageResource(defaultIcon);
		} else {
			fb.display(
					view,
					uri,
					((BitmapDrawable) mContext.getResources().getDrawable(
							defaultIcon)).getBitmap(),
					((BitmapDrawable) mContext.getResources().getDrawable(
							defaultIcon)).getBitmap());
		}
	}

	/**
	 * 用于图片显示 .已经定义好loadingbitmap 和failedbitmap.
	 * 
	 * @param mContext
	 * 
	 * @return
	 */
	public void displayForPicture(View view, String uri) {
		if (StringUtil.isEmpty(uri)) {
			ImageView iv = (ImageView) view;
			iv.setImageResource(R.drawable.icon_default_car);
		} else {
			fb.display(
					view,
					uri,
					((BitmapDrawable) mContext.getResources().getDrawable(
							R.drawable.icon_default_car)).getBitmap(),
					((BitmapDrawable) mContext.getResources().getDrawable(
							R.drawable.icon_default_car)).getBitmap());
		}

		// fb.configLoadingImage(R.drawable.icon_default_logo);
		// fb.configLoadfailImage(R.drawable.icon_default_logo);
		// fb.display(view, uri);
	}

	private FinalBitmap getFinalBitmap() {
		return fb;
	}

	private static void clearCache(String logo) {
		fb.clearCache(logo);
	}
}
