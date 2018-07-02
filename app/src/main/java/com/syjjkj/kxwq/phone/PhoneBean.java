package com.syjjkj.kxwq.phone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.syjjkj.kxwq.homepage.MyApplication;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;

public class PhoneBean {
	private Context context;
	private Intent intent;
	public PhoneBean(Context context) {
		this.context=context;
	}
	public void dialPhone(String phone){
		if (!StringUtil.isEmpty(phone)) {
			intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ phone));
			context.startActivity(intent);
		} else {
			ToastUtil.makeLongText(MyApplication.getInstance(), "对不起，对方电话不可用");
//			intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
//					+ ParserJsonBean.DEFAULTPHONE));
//			context.startActivity(intent);
		}

	}

}
