package com.syjjkj.kxwq.util;

import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;

/**
 * 对图片进行切割
 */
public class ImageSplitterUtil {
	public static ArrayList<String> split(Bitmap bitmap, int xPiece, int yPiece, String directory) {

		ArrayList<String> pieces = new ArrayList<String>();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int pieceWidth = width / xPiece;
		int pieceHeight = height / yPiece;
		int count = 0;
		Bitmap piece = null;
		for (int i = 0; i < yPiece; i++) {
			for (int j = 0; j < xPiece; j++) {
				count++;
				int xValue = j * pieceWidth;
				int yValue = i * pieceHeight;
				piece = Bitmap.createBitmap(bitmap, xValue, yValue,
						pieceWidth, pieceHeight);
				String path = directory+count+".pngx";
				File file = FileOperation.savePic(ImageOperate.getBitmapByte(piece, "pngx"),path);
				
				pieces.add(file.getPath());//放入图片保存的路径
			}
		}

		return pieces;
	}
}
