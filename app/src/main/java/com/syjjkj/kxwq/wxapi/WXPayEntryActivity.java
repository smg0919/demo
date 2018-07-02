package com.syjjkj.kxwq.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseActivity;
import com.syjjkj.kxwq.homepage.HomePageActivity;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{

	private TextView tvPayResult;
	private TextView tvValidity;
	private TextView tvPrice;
	private TextView tvCount;
	private TextView tvContact;
	private TextView tvPhone;
	private TextView Trl_content_3_tv_2;
	private TextView Trl_content_4_tv_2;
	private TextView tvidcard;
	private TextView Trl_4_tv_2;
	private TextView Trl_5_tv_1;
	private TextView Trl_6_tv_1;
	private TextView Trl_8_tv_1;
	private TextView Ttv_title_center;
	private RelativeLayout Rrl_2;
	private RelativeLayout Rrl_3;
	private RelativeLayout Rrl_4;
	private RelativeLayout Rrl_5;
	private RelativeLayout Rrl_6;
	private RelativeLayout Rrl_8;
	private Button backhome;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);
		WXBean.wxApi = WXAPIFactory.createWXAPI(this, WXBean.WX_APP_ID);
		WXBean.wxApi.handleIntent(getIntent(), this);
		initView();
		initListener();
	}
	private void initView(){
		tvPayResult=(TextView) findViewById(R.id.pay_result_rl_1_tv_result);
		tvValidity=(TextView) findViewById(R.id.pay_result_rl_2_tv_validity);
		tvPrice=(TextView) findViewById(R.id.pay_result_rl_3_tv_price);
		tvCount=(TextView) findViewById(R.id.pay_result_rl_4_tv_count);
		tvContact=(TextView) findViewById(R.id.pay_result_rl_5_tv_name);
		tvPhone=(TextView) findViewById(R.id.pay_result_rl_6_tv_phone);
		tvidcard=(TextView)findViewById(R.id.pay_result_rl_8_tv_idcard);
		Trl_content_3_tv_2=(TextView) findViewById(R.id.rl_content_3_tv_2);
		Trl_content_4_tv_2=(TextView) findViewById(R.id.rl_content_4_tv_2);
		Ttv_title_center=(TextView)findViewById(R.id.tv_title_center) ;
		Trl_4_tv_2=(TextView) findViewById(R.id.rl_4_tv_2);
		Trl_5_tv_1=(TextView)findViewById(R.id.rl_5_tv_1);
		Trl_6_tv_1=(TextView)findViewById(R.id.rl_6_tv_1);
		Trl_8_tv_1=(TextView)findViewById(R.id.rl_8_tv_1);
		Rrl_2=(RelativeLayout)findViewById(R.id.rl_2);
		Rrl_3=(RelativeLayout)findViewById(R.id.rl_3);
		Rrl_4=(RelativeLayout)findViewById(R.id.rl_4);
		Rrl_5=(RelativeLayout)findViewById(R.id.rl_5);
		Rrl_6=(RelativeLayout)findViewById(R.id.rl_6);
		Rrl_8=(RelativeLayout)findViewById(R.id.rl_8);
		backhome=(Button)findViewById(R.id.backhome);
		/*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});*/

	}
	private void initListener() {
		backhome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(WXPayEntryActivity.this, HomePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});

	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		WXBean.wxApi.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//			builder.show();
			getPayResult();
		}
	}
	// 购票
	private void getPayResult()
	{
		if (!StringUtil.isNetworkConnected(this)) {
			ToastUtil.makeShortText(this, "请检查网络");
			return;
		}
		UserInfoBean.getUserInfo(this);
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", UserInfoBean.uid);
		params.addBodyParameter("token", UserInfoBean.token);
		params.addBodyParameter("order_id", UserInfoBean.getOrderId(this));
		params.addBodyParameter("prepayid", UserInfoBean.getPrepayId(this));
		httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_PAY_RESULT, params,
				this, httpUtils, HttpStaticApi.get_pay_result);
	}
	@Override
	public void onSuccessHttp(String responseInfo, int resultCode) {
		dismissDialog();
		super.onSuccessHttp(responseInfo, resultCode);
		switch (resultCode) {
			case HttpStaticApi.get_pay_result:
				try {
					Bundle bundle = ParserJsonBean.parseCheckResult(responseInfo);
					if (bundle != null) {
						if (bundle.getInt(ParserJsonBean.STATE) == 1) {
							String type=bundle.getString("usage");
							if(type.equals("0"))
							{
								String strPhone=bundle.getString(ParserJsonBean.PHONE);
								String strName=bundle.getString(ParserJsonBean.NAME);
								String strRealPrice=bundle.getString("real_price");
								String strOrderTime=bundle.getString("order_time");
								String strResult=bundle.getString("error");
								String num=bundle.getString("product_num");
								//String name=bundle.getString("product_name");
								Ttv_title_center.setText("支付结果");
								tvPayResult.setText(strResult);
								tvValidity.setText(strOrderTime);
								tvContact.setText(strName);
								tvPhone.setText(strPhone);
								tvCount.setText(num);
								tvPrice.setText(strRealPrice);
								Rrl_8.setVisibility(View.INVISIBLE);
							}
							else if(type.equals("1"))  //保证金
							{

							}
							else if(type.equals("2"))  //钱包充值
							{
								String order_tid=bundle.getString("order_tid");
								String strRealPrice=bundle.getString("real_price");
								String strOrderTime=bundle.getString("order_time");
								Trl_content_3_tv_2.setText("订单号");
								Trl_content_4_tv_2.setText("订单时间");
								Trl_4_tv_2.setText("充值金额");
								tvValidity.setText(order_tid);
								tvPrice.setText(strOrderTime);
								tvCount.setText(strRealPrice);
								tvPayResult.setText("充值成功");
								Ttv_title_center.setText("充值结果");
								Rrl_5.setVisibility(View.INVISIBLE);
								Rrl_6.setVisibility(View.INVISIBLE);
								Rrl_8.setVisibility(View.INVISIBLE);

							}
							else if(type.equals("3"))   	//客房预定
							{
								String strRealPrice=bundle.getString("real_price");
								String strOrderTime=bundle.getString("order_time");
								String strPhone=bundle.getString(ParserJsonBean.PHONE);
								String strName=bundle.getString(ParserJsonBean.NAME);
								String idcard=bundle.getString("paper_id");
								String room_name=bundle.getString("room_name");
								Trl_content_3_tv_2.setText("预定房型");
								Trl_content_4_tv_2.setText("入住时间");
								Trl_4_tv_2.setText("支付金额");
								Trl_8_tv_1.setText("身份证号");
								Trl_5_tv_1.setText("住宿人");
								Trl_6_tv_1.setText("手机号");
								tvPayResult.setText("订房成功");
								Ttv_title_center.setText("支付结果");
								tvValidity.setText(room_name);
								tvPrice.setText(strOrderTime);
								tvCount.setText(strRealPrice);
								tvContact.setText(strName);
								tvPhone.setText(strPhone);
								tvidcard.setText(idcard);
							}
							else if(type.equals("4"))   	//开卡判断是充值还是注册
							{
								String order_type=bundle.getString("order_type");
								if("0".equals(order_type))   //注册并充值
								{
									String strPhone=bundle.getString(ParserJsonBean.PHONE);
									String strName=bundle.getString(ParserJsonBean.NAME);
									String strRealPrice=bundle.getString("real_price");
									String strOrderTime=bundle.getString("order_time");
									String idCard=bundle.getString("idCard");
									String hy_level=bundle.getString("hy_level");
									String type_name="";
									Trl_content_3_tv_2.setText("手机号");
									Trl_content_4_tv_2.setText("办理时间");
									Trl_4_tv_2.setText("卡型级别");
									if("1".equals(hy_level))
									{
										type_name="康乐卡";
									}
									else if("2".equals(hy_level))
									{
										type_name="康养卡";
									}
									else if("3".equals(hy_level))
									{
										type_name="康享卡";
									}
									Trl_8_tv_1.setText("身份证号");
									Trl_5_tv_1.setText("姓名");
									Trl_6_tv_1.setText("卡金额");
									tvPayResult.setText("开卡成功");
									Ttv_title_center.setText("支付结果");
									tvValidity.setText(strPhone);
									tvPrice.setText(strOrderTime);
									tvCount.setText(type_name);
									tvidcard.setText(idCard);
									tvContact.setText(strName);
									tvPhone.setText(strRealPrice);
									Rrl_8.setVisibility(View.INVISIBLE);

								}
								else  	//只充值
								{
									String phone=bundle.getString("phone");
									String price=bundle.getString("price");
									Trl_content_3_tv_2.setText("手机号");
									Trl_content_4_tv_2.setText("充值金额");
									tvPayResult.setText("充值成功");
									Ttv_title_center.setText("充值结果");
									tvValidity.setText(phone);
									tvPrice.setText(price);
									Rrl_4.setVisibility(View.INVISIBLE);
									Rrl_5.setVisibility(View.INVISIBLE);
									Rrl_6.setVisibility(View.INVISIBLE);
									Rrl_8.setVisibility(View.INVISIBLE);
								}
							}
						} else {
							Rrl_2.setVisibility(View.INVISIBLE);
							Rrl_3.setVisibility(View.INVISIBLE);
							Rrl_4.setVisibility(View.INVISIBLE);
							Rrl_5.setVisibility(View.INVISIBLE);
							Rrl_6.setVisibility(View.INVISIBLE);
							Rrl_8.setVisibility(View.INVISIBLE);
							Ttv_title_center.setText("操作结果");
							tvPayResult.setText("失败");
							/*tvValidity.setText("-");
							tvContact.setText("-");
							tvPhone.setText("-");
							tvCount.setText("-");
							tvPrice.setText("-");*/
							/*ToastUtil.makeShortText(this,
									bundle.getString(ParserJsonBean.MESSAGE));*/
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
		}
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				Intent intent=new Intent(WXPayEntryActivity.this, HomePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			return false;
		}
		return super.dispatchKeyEvent(event);

	}



}