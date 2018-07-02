package com.syjjkj.kxwq.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.ClipboardManager;

import com.syjjkj.kxwq.homepage.BaseFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/21.
 */
public class ShareUtils {
    /**
     * 微信好友圈分享
     */
    public static void Wechat_Favorite(final BaseFragment context, String strSharetxt, final ArrayList<String> picShareList) {
        // 利用sharesdk分享一个链接
		/*
		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_TEXT); // 分享到网页的方式打开连接
		sp.setText("http://www.carcle.cn/" + id + ".htm");
		sp.setTitle("分享车圈车辆");
		// sp.setImageUrl("http://365ccyx.com/templates/ccyx/images/pic/logo.png");
		// sp.setUrl("http://www.baidu.com");
		Platform WechatMoment = ShareSDK.getPlatform(Wechat.NAME);
		WechatMoment.setPlatformActionListener(this);
		WechatMoment.SSOSetting(true);
		*/
        // 用Intent 启动系统分享
        final Intent sendIntent = new Intent();
        // 已知微信的Component名，可以直接分享到微信朋友圈，不用进入系统菜单
        sendIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
//        sendIntent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));

        // 分享的类型
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.setType("image/*");
        // 设置分享的图片，顺序就是添加的顺序
        final ArrayList<Uri> imageUris = new ArrayList<Uri>();
        String desc = strSharetxt;
        LogUtil.e("文字描述"+desc);
        // 用系统剪切板，复制需要的文字，以便于分享朋友圈时候直接粘贴文字
        ClipboardManager copy = (ClipboardManager) context.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText(desc);
        context.showWaitDialog("正在加载图片");
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < picShareList.size(); i++) {
//                    String url = UrlConfig.PIC_PATH + picShareList.get(i);
                    String url =  picShareList.get(i);
                    String name = url.substring(
                            url.lastIndexOf("/") + 1,
                            url.lastIndexOf("."));
                    // 你要执行的方法
//                    String path = Environment
//                            .getExternalStorageDirectory().getPath()
//                            + "/CarCircle/" + name + ".pngx";
                    String path = Environment
                            .getExternalStorageDirectory().getPath()
                            + "/kxwq/" + name + ".pngx";
                    int result = 1;
                    if (!new File(path).exists()) {
                        HttpDownloader httpDownLoader = new HttpDownloader();
                        result = httpDownLoader.downfile(url, path);
                    }

                    if (result == 1) {// 成功
                        String pathdesc = Environment
                                .getExternalStorageDirectory().getPath()
                                + "/kxwq/" + name + ".png";
                        File file = new File(path);
                        if(file.exists())
                        {
                            Utils.copyFile(path, pathdesc);
                            imageUris.add(Uri.fromFile(new File(pathdesc)));
                        }
                    }
                }

                sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                context.startActivity(sendIntent);
            }
        }.start();
    }
}
