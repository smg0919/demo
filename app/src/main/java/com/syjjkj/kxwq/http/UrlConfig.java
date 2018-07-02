package com.syjjkj.kxwq.http;

/**
 * url
 */
@SuppressWarnings("unused")
public class UrlConfig {
	public static final String ROOT_URL = "https://kxhotspring.com/api/index.php?r=default/";//"http://x18.bloveapp.com/index.php?r=default/"
	/**
	 * 1.4获取验证码
	 */
	public static final String GETCODE = ROOT_URL+"user/getCode";
	/**
	 * 短信登录
	 */
	public static final String DODXLOGIN = ROOT_URL+"user/dologinCode";
	/**
	 * 注册
	 */
	public static final String REGISTER_USER = ROOT_URL+"user/registerUser";
	/**
	 * 登录
	 */
	public static final String DO_LOGIN = ROOT_URL+"user/dologin";
	/**
	 *获取个人账户信息
	 */
	public static final String MY_INFO = ROOT_URL+"user/myInfo";
	/**
	 *上传
	 */
	public static final String UPLOADPIC = ROOT_URL+"user/uploadPic";
	/**
	 *修改密码
	 */
	public static final String PASSWORDEDIT= ROOT_URL+"user/modifyPwd";
	/**
	 *提现
	 */
	public static final String GET_WITHDRAW= ROOT_URL+"user/withdraw";
	/**
	 *获取分享素材
	 */
	public static final String GET_MATERIAL= ROOT_URL+"material/getMaterialList";
	/**
	 *分享图片
	 */
	public static final String PIC_PATH="http://kxhotspring.com/api/upload/";
	/**
	 *APKVersion
	 */
	public static final String APK_VERSION=ROOT_URL+"user/apkversion";
	/**
	 *下载APK
	 */
	public static final String GET_APK="http://kxhotspring.com/api/upload/apk/kxwq.apk";
	/**
	 *获取门票种类
	 */
	public static final String GET_PRODUCT_TYPE =ROOT_URL+"ticket/getProductType";
	/**
	 *获取门票
	 */
	public static final String GET_PRODUCT=ROOT_URL+"ticket/getProduct";
	/**
	 *获取积分
	 */
	public static final String GET_PV=ROOT_URL+"user/getPV";
	/**
	 *购买票
	 */
	public static final String BUY_TICKETS=ROOT_URL+"ticket/buy";
	/**
	 *购票记录
	 */
	public static final String GET_TICKETS_RECORDS=ROOT_URL+"ticket/buyRecord";
	/**
	 *旅行社记录
	 */
	public static final String GET_ALLLVXINGSHE=ROOT_URL+"user/getCompanys";
	/**
	 *分销员工修改所属
	 */
	public static final String changeCompany=ROOT_URL+"user/changeCompany";
	/**
	 *下级记录
	 */
	public static final String GET_SALES_RECORDS=ROOT_URL+"user/salesRecord";
	/**
	 *底部红点
	 */
	public static final String RED_DOT_NUM=ROOT_URL+"user/redDotNum";

	/**
	 *更新个推
	 */
	public static final String UPDATE_CID=ROOT_URL+"user/updateCid";
	/**
	 *统一下单
	 */
	public static final String UNIFED_ORDER=ROOT_URL+"pay/unifiedorder";
	/**
	 *查询结果
	 */
	public static final String GET_PAY_RESULT=ROOT_URL+"pay/getPayResult";
	/**
	 *退款请求
	 */
	public static final String GET_REFUND=ROOT_URL+"pay/refund";
	/**
	 *积分下单购票
	 */
	public static final String PV_TICKETS=ROOT_URL+"pay/payPV";
	/**
	 * 退出
	 */
	public static  final  String Exit=ROOT_URL+"user/exit";
	/**
	 * 修改支付密码
	 */
	public static  final  String MODIFYPAYPWD=ROOT_URL+"user/modifyPayPwd";
	/**
	 * 提现
	 */
	public static  final  String tiXian=ROOT_URL+"user/tiXian";
	/**
	 * 充值
	 */
	public static  final  String chongZhi=ROOT_URL+"pay/chongZhi";
	/**
	 * 钱包买票
	 */
	public static  final  String QBbuyTicket=ROOT_URL+"pay/payQB";
	/**
	 * 活动
	 */
	public static  final  String ALLactives=ROOT_URL+"event/eventList";
	/**
	 *混合下单
	 */
	public static final String hybridPay=ROOT_URL+"pay/hybridPay";
	/**
	 *订房下单
	 */
	public static final String dingfangPay=ROOT_URL+"room/buy";
	/**
	 *微信订房支付
	 */
	public static final String Payroom=ROOT_URL+"pay/unifiedorder_KF";
	/**
	 *钱包订房支付
	 */
	public static final String PayQBroom=ROOT_URL+"pay/payQB_KF";
	/**
	 *钱包订房支付
	 */
	public static final String PayHunheroom=ROOT_URL+"pay/hybridPay_KF";
	/**
	 *注册或充值获取验证码
	 */
	public static final String RegeorChongzhigetCode=ROOT_URL+"Card/getCode";
	/**
	 *注册开卡
	 */
	public static final String RegisterCard=ROOT_URL+"Card/registerCard";
	/**
	 *充值卡号
	 */
	public static final String RechargeCard =ROOT_URL+"Card/rechargeCard";
	/**
	 *充值卡号
	 */
	public static final String PayCard =ROOT_URL+"Card/payCard";
	/**
	 *混合充值卡号
	 */
	public static final String HybridPayCard =ROOT_URL+"Card/hybridPayCard";
	/**
	 *钱包充值卡号
	 */
	public static final String QbPayCard  =ROOT_URL+"Card/qbPayCard";
	/**
	 *修改卡号信息
	 */
	public static final String ModifyCard  =ROOT_URL+"Card/modifyCard";
	/**
	 *修改分销组员活动价格
	 */
	public static final String setAddPrice  =ROOT_URL+"fxUser/setAddPrice";
	/**
	 *修改分销组员活动价格
	 */
	public static final String modifyUser  =ROOT_URL+"fxUser/modifyUser";
	/**
	 *修改分销组员活动价格
	 */
	public static final String deleteUser  =ROOT_URL+"fxUser/deleteUser";


}
