package com.syjjkj.kxwq.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.syjjkj.kxwq.R;
/**
 * 通知栏里面的进度notifaction
 * @author Administrator
 *
 */
public class NotificationProgress {
	private NotificationManager notificationManager;
	private Notification notification;
	public CharSequence contentTitle = "下载";
	public CharSequence contentText = "内容";

	public NotificationProgress(Context context) {
		notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// notification = new Notification(R.drawable.ic_launcher, "下载", System
		// .currentTimeMillis());
		notification = new Notification.Builder(context)
				.setContentTitle(contentTitle).setContentText(contentText)
				.setSmallIcon(R.drawable.push).build();
		notification.tickerText = contentTitle;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		//******************震动****************************
//		notification.defaults |= Notification.DEFAULT_SOUND;
//		notification.defaults |= Notification.DEFAULT_VIBRATE;
		// long[] vibrate = {0,100,200,300};
		// notification.vibrate = vibrate;
		// notification.sound =
		// Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
		notification.icon = R.drawable.push;
		RemoteViews view = new RemoteViews(context.getPackageName(),
				R.layout.item_notification_progress);
		notification.contentView = view;

		PendingIntent contentIntent = PendingIntent
				.getActivity(context, 0, new Intent(
						"com.syjjkj.carcircle.homepage.HomePageActivity"), 0);

		notification.contentIntent = contentIntent;
	}

	public void setNotiTitle() {

	}

	public void setNotiContent(String con) {
		// 更改文字
		notification.contentView.setTextViewText(R.id.noti_tv, con);
		// 发送消息
		notificationManager.notify(0, notification);
	}

	public void setNotiPro(int pro) {
		// 更改进度条
		notification.contentView.setProgressBar(R.id.noti_pb, 100, pro, false);
		// 发送消息
		notificationManager.notify(0, notification);
	}

	public void setNoti(String con, int pro) {
		// 更改文字
		notification.contentView.setTextViewText(R.id.noti_tv, con);
		// 更改进度条
		notification.contentView.setProgressBar(R.id.noti_pb, 100, pro, false);
		// 发送消息
		notificationManager.notify(0, notification);
	}

}
