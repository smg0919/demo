package com.syjjkj.kxwq.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.util.Log;
import android.widget.RemoteViews;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.HomePageActivity;
import com.syjjkj.kxwq.http.ParserJsonBean;

import org.json.JSONException;

public class PushReceiver extends BroadcastReceiver{
    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);

                    Log.d("GetuiSdkDemo", "receiver payload : " + data);

                    CharSequence contentTitle = "";
                    CharSequence contentText = "";
                    try {
                        Bundle rspbundle = ParserJsonBean.parserPushMessage(data);
                        if (rspbundle != null)
                        {
                            contentTitle = rspbundle.getCharSequence("title");
                            contentText = rspbundle.getCharSequence("content");
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    Intent notificationIntent = new Intent(context, HomePageActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式

                    Notification notification =  new Notification.Builder(context)
                            .setContentTitle(contentTitle)
                            .setContentText(contentText)
                            .setSmallIcon(R.drawable.push)
                            .build();
                    notification.tickerText = contentTitle;
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    notification.defaults |= Notification.DEFAULT_SOUND;
                    notification.defaults |= Notification.DEFAULT_VIBRATE;
                    long[] vibrate = {0,100,200,300};
                    notification.vibrate = vibrate;
                    notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
                    notification.icon = R.drawable.push;
                    // 1、创建一个自定义的消息布局 notification.xml
                    // 2、在程序代码中使用RemoteViews的方法来定义image和text。然后把RemoteViews对象传到contentView字段
                    RemoteViews remoteView = new RemoteViews(context.getPackageName(),R.layout.item_notification);
                    remoteView.setTextViewText(R.id.title , contentTitle);
                    remoteView.setTextViewText(R.id.content, contentText);
                    notification.contentView = remoteView;
                    // 3、为Notification的contentIntent字段定义一个Intent(注意，使用自定义View不需要setLatestEventInfo()方法)

                    //这儿点击后简答启动Settings模块
                    PendingIntent contentIntent = PendingIntent.getActivity
                            (context, 0,new Intent("com.syjjkj.kxwq.homepage.HomePageActivity"), 0);
                    notification.contentIntent = contentIntent;
                    mNotificationManager.notify(1, notification);

                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 *
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }
}

