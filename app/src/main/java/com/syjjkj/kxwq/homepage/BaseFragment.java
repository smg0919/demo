package com.syjjkj.kxwq.homepage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.http.ObserverCallBack;
import com.syjjkj.kxwq.util.FinalBitmapUtil;
import com.syjjkj.kxwq.util.StringUtil;



public class BaseFragment extends Fragment implements ObserverCallBack {
	protected ProgressDialog waitDialog;
	protected View rootView;
	/**
	 * 标记标题左右两边的类型:文字
	 */
	protected final int TITLE_TYPE_TEXT = 0;
	/**
	 * 标记标题左右两边的类型:图片
	 */
	protected final int TITLE_TYPE_IMG = 1;

	protected TextView tvTitle;
	protected TextView tvLeft;
	protected LinearLayout llLeft;
	protected ImageView ivLeft;
	protected TextView tvRight;
	protected ImageView ivRight;
	protected LinearLayout llRight;

	protected HttpUtils httpUtils;// 网络访问声明
	protected BitmapUtils bitmapUtils;
	protected Dialog loading_dialog;
	protected FinalBitmapUtil util;
	protected RequestParams params;
	
	protected TextView tvPercent=null; //等待提示框


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		bitmapUtils = new BitmapUtils(getActivity());
		httpUtils = new HttpUtils();
		return rootView;
	}

	/**
	 * 需在setContentView方法之后调用. 设置后,如果不对左侧的事件进行监听,默认的点击事件是结束当前界面.
	 * <p>
	 * 标题传资源id和字符串皆可.
	 * <p>
	 * 如果某一侧显示的是图片,则那一侧只能传对应的图片资源id.如果是文字,则资源id和字符串皆可.
	 * 
	 * @param title
	 *            标题
	 * @param left
	 *            是否显示左侧的部分
	 * @param leftType
	 *            左侧的类型
	 * @param l
	 *            左侧部分内容
	 * @param right
	 *            是否显示右侧的部分
	 * @param rightType
	 *            右侧的类型
	 * @param r
	 *            右侧部分的内容
	 */
	protected void setTitle(Object title, boolean left, int leftType, Object l,
							boolean right, int rightType, Object r) {
		try {
			tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
			tvLeft = (TextView) rootView.findViewById(R.id.tv_title_left);
			llLeft = (LinearLayout) rootView.findViewById(R.id.ll_title_left);
			ivLeft = (ImageView) rootView.findViewById(R.id.iv_title_left);
			tvRight = (TextView) rootView.findViewById(R.id.tv_title_right);
			ivRight = (ImageView) rootView.findViewById(R.id.iv_title_right);
			llRight = (LinearLayout) rootView.findViewById(R.id.ll_title_right);
			if (title != null && title instanceof String) {
				if (!TextUtils.isEmpty((String) title)) {
					tvTitle.setVisibility(View.VISIBLE);
					tvTitle.setText((String) title);
				} else {
					tvTitle.setVisibility(View.INVISIBLE);
				}
			} else if (title != null && title instanceof Integer) {
				if (((Integer) title) > 0) {
					tvTitle.setVisibility(View.VISIBLE);
					tvTitle.setText((Integer) title);
				} else {
					tvTitle.setVisibility(View.INVISIBLE);
				}

			}
			if (left) {
				llLeft.setVisibility(View.VISIBLE);
				if (leftType == TITLE_TYPE_TEXT) {
					ivLeft.setVisibility(View.GONE);
					tvLeft.setVisibility(View.VISIBLE);
					if (l instanceof String) {
						tvLeft.setText((String) l);
					} else if (l instanceof Integer) {
						tvLeft.setText((Integer) l);
					}
				} else if (leftType == TITLE_TYPE_IMG) {
					ivLeft.setVisibility(View.VISIBLE);
					tvLeft.setVisibility(View.GONE);
					if (l instanceof Integer) {
						ivLeft.setImageResource((Integer) l);
					}
					llLeft.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							getActivity().finish();
						}
					});
				}
			} else {
				llLeft.setVisibility(View.INVISIBLE);
			}
			if (right) {
				llRight.setVisibility(View.VISIBLE);
				if (rightType == TITLE_TYPE_TEXT) {
					ivRight.setVisibility(View.GONE);
					tvRight.setVisibility(View.VISIBLE);
					if (r instanceof String) {
						tvRight.setText((String) r);
					} else if (r instanceof Integer) {
						tvRight.setText((Integer) r);
					}
				} else if (rightType == TITLE_TYPE_IMG) {
					ivRight.setVisibility(View.VISIBLE);
					tvRight.setVisibility(View.GONE);
					if (r instanceof Integer) {
						ivRight.setImageResource((Integer) r);
					}
				}
			} else {
				llRight.setVisibility(View.INVISIBLE);
			}

		} catch (Exception e) {

		}
	}

	/**
	 * 全局等待对话框
	 */
	public void showWaitDialog(String content) {
		loading_dialog = new Dialog(getActivity(), R.style.MyDialog);
		View view= LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_loading, null);
		tvPercent=(TextView) view.findViewById(R.id.tv_percent);
		if (!StringUtil.isEmpty(content)) {
			TextView tv= (TextView) view.findViewById(R.id.tv_dialog);
			tv.setText(content);
		}
		loading_dialog.setContentView(view);
		loading_dialog.show();
	}

	public void dismissDialog() {
		if (loading_dialog != null && loading_dialog.isShowing()) {
			loading_dialog.dismiss();
			loading_dialog = null;
			tvPercent=null;
			
		}
	}

	@Override
	public void onStartHttp() {
	}

	@Override
	public void onLoadingHttp(long total, long current, boolean isUploading) {
	}

	@Override
	public void onSuccessHttp(String responseInfo, int resultCode) {
//		dismissDialog();
	}

	@Override
	public void onFailureHttp(HttpException error, String msg) {
//		dismissDialog();
	}


	@Override
	public void onDestroy() {
	    super.onDestroy();
	    if((loading_dialog != null) && loading_dialog.isShowing() ){
	    	loading_dialog.dismiss();
			loading_dialog = null;
	    }
	}

	
}
