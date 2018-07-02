package com.syjjkj.kxwq.util;

import com.syjjkj.kxwq.photo.PhotoItem;

import java.util.Comparator;

public class DateTimeComparator implements Comparator<PhotoItem> {

	@Override
	public int compare(PhotoItem p0, PhotoItem p1) {
		return p1.getDateTime().compareTo(p0.getDateTime());
	}
}
