package com.syjjkj.kxwq.homepage;

import android.os.Environment;

public class Constant {
	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/llkjxsbyb/";
	public static final String FROMHEADIMAGEURL = "fromHeadImageUrl";
	public static final String FROMNICKNAME = "fromNickName";
	public static final String ACCOUNT_REMOVED = "account_removed";
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
	// -------------------广播
	public static final String BR_UPDATE_GROUPNAME = "com.llkj.updategroupname";

	// 聊天相关 参数
	public static final String USERID = "userId";
	public static final String USERNAME = "userName";
	public static final String USERLOGO = "userLogo";
	public static final String GROUPID = "groupId";
	public static final String GROUPNAME = "groupName";
	public static final String GROUPLOGO = "groupLogo";
	public static final String CHATTYPE = "chatType";
	
	public static final int REFRESH_GROUP = 1;
	public static final int CHATTYPE_GROUP = 2;
	
	public static final int CHAT_BG_REQUEST = 3;//设置照相聊天背景请求
	public static final int CHAT_BG_REQUEST_album = 4;//设置相册聊天背景请求
	public static String CHAT_BG_REQUEST_PATH = "";

}
