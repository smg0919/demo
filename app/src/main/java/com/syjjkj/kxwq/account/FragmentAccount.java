package com.syjjkj.kxwq.account;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.syjjkj.kxwq.R;
import com.syjjkj.kxwq.homepage.BaseFragment;
import com.syjjkj.kxwq.homepage.MyApplication;
import com.syjjkj.kxwq.homepage.UserInfoBean;
import com.syjjkj.kxwq.http.AnsynHttpRequest;
import com.syjjkj.kxwq.http.HttpStaticApi;
import com.syjjkj.kxwq.http.ParserJsonBean;
import com.syjjkj.kxwq.http.UrlConfig;
import com.syjjkj.kxwq.login.LoginActivity;
import com.syjjkj.kxwq.myview.RoundImageView;
import com.syjjkj.kxwq.util.ImageOperate;
import com.syjjkj.kxwq.util.LogUtil;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;
import com.syjjkj.kxwq.util.Utils;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAccount.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccount extends BaseFragment  implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_CAMERA = 0;// 拍照
    private static final int REQUEST_CODE_GALLERY = 1;// 从相册获取
    public static final int PHOTORESOULT = 3;// 结果

    private Bitmap photo;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RoundImageView account_im_head;
    private String strPhone="";
    private String strLevel="";
    private String direct_type="";
    private String logo="";
  //  private String strPv="";
//    private FragmentAccountSalesAdapter adapter;
//    private ListView listView;
    private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();

    private TextView tvPhone,tvLevel,tvPv;
    private RelativeLayout account_exit;
    private RelativeLayout rlModifyPass;
  //  private RelativeLayout rlSalesRecord;
    private RelativeLayout rlOnlineService;
    //private RelativeLayout rlOnlineLvsuoshu;
    private RelativeLayout rlOnlineMoney;
    private RelativeLayout rlOnlinefenxiao;
    public FragmentAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAccount newInstance(String param1, String param2) {
        FragmentAccount fragment = new FragmentAccount();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        rootView=inflater.inflate(R.layout.fragment_account, container, false);
        initView();
        initlistener();
        initData();
        return rootView;
    }
    private void initView(){
        account_im_head=(RoundImageView) rootView.findViewById(R.id.account_im_head);
        tvPhone=(TextView)rootView.findViewById(R.id.account_tv_phone);
        tvLevel=(TextView)rootView.findViewById(R.id.account_tv_level);
       // tvPv=(TextView)rootView.findViewById(R.id.account_tv_pv);
        rlModifyPass=(RelativeLayout) rootView.findViewById(R.id.account_rl_safety);
        //rlSalesRecord=(RelativeLayout) rootView.findViewById(R.id.account_record);
        //tvRight = (TextView) rootView.findViewById(R.id.tv_title_right);
        rlOnlineService=(RelativeLayout) rootView.findViewById(R.id.account_rl_online_service);
       // rlOnlineLvsuoshu=(RelativeLayout)rootView.findViewById(R.id.account_rl_lvsuoshu) ;
        rlOnlineMoney=(RelativeLayout)rootView.findViewById(R.id.account_rl_money) ;
        rlOnlinefenxiao=(RelativeLayout)rootView.findViewById(R.id.account_Distribution);
        account_exit = (RelativeLayout) rootView.findViewById(R.id.account_exit);
//        listView=(ListView) rootView.findViewById(R.id.account_lv);
    }
    private void initlistener(){
        account_im_head.setOnClickListener(this);
        //tvRight.setOnClickListener(this);
        rlModifyPass.setOnClickListener(this);
       // rlSalesRecord.setOnClickListener(this);
        rlOnlineService.setOnClickListener(this);
       // rlOnlineLvsuoshu.setOnClickListener(this);
        account_exit.setOnClickListener(this);
        rlOnlineMoney.setOnClickListener(this);
        rlOnlinefenxiao.setOnClickListener(this);
        account_im_head
                .setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v,
                                                    ContextMenu.ContextMenuInfo menuInfo) {
                        menu.setHeaderTitle("选择图片");
                        menu.add(0, 0, 0, "拍照");
                        menu.add(0, 1, 1, "从相机获取");
                    }
                });
    }
    private void initData(){
//        adapter=new FragmentAccountSalesAdapter(getActivity());
//        listView.setAdapter(adapter);
//        getsalesRecord();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onResume() {
        super.onResume();
        getMyInfo(UrlConfig.MY_INFO,HttpStaticApi.getmyinfo);

        ImageLoader.getInstance().displayImage(UserInfoBean.getLogo(getActivity()), account_im_head, MyApplication.optionsPerson);
}

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.account_im_head:
                account_im_head.showContextMenu();
                break;
            case R.id.account_rl_safety:
                intent = new Intent(getActivity(), ModifyPassActivity.class);
                startActivity(intent);
                break;
            /*case R.id.tv_title_right:
                intent=new Intent(getActivity(),WithDrawActivity.class);
                intent.putExtra("pv",strPv);
                startActivity(intent);
                break;
            case R.id.account_record:
                intent=new Intent(getActivity(),SalesRecordActivity.class);
                startActivity(intent);
                break;*/
            case R.id.account_rl_online_service:
//           AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//			builder.setTitle("客服热线");
//			builder.setMessage("400-024-5656");
//		    builder.show();
                new AlertDialog.Builder(getActivity())
                        .setTitle("客服热线")
                        .setMessage("400-024-5656")
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {


                                    }
                                })
                        .setPositiveButton("拨打",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        onCall("4000245656");

                                    }
                                }).show();
                break;
            case R.id.account_exit:
                UserInfoBean.userLogout(getActivity());
                MyApplication.getInstance().exit();
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                ToastUtil.makeShortText(getActivity(), "退出成功,请重新登录");
                break;
            /*case R.id.account_rl_lvsuoshu:
                intent = new Intent(getActivity(), UserDirectActivity.class);
                startActivity(intent);
                break;*/
            case R.id.account_rl_money:
                intent = new Intent(getActivity(), UserMoneyActivity.class);
                startActivity(intent);
                break;
            case R.id.account_Distribution:
                intent = new Intent(getActivity(), UserFenxiaoManagementActivity.class);
                startActivity(intent);
                break;
        }
    }
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    public void onCall(String mobile){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_CALL_PHONE);
                return;
            }else{
                //上面已经写好的拨号方法
                callDirectly(mobile);
            }
        } else {
            //上面已经写好的拨号方法
            callDirectly(mobile);
        }
    }
    @Override    //动态获取打电话权限
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    callDirectly("4000245656");
                } else {
                    // Permission Denied
                    ToastUtil.makeShortText(getActivity(), "您拒绝了权限的授予,无法拨打");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void callDirectly(String mobile)
    {
        Intent intent = new Intent();
        //把打电话的动作ACTION_CALL封装至意图对象当中
        intent.setAction(Intent.ACTION_CALL);
        //设置打给谁
        intent.setData(Uri.parse("tel:" + mobile));//这个tel：必须要加上，表示我要打电话。否则不会有打电话功能，由于在打电话清单文件里设置了这个“协议”
        //把动作告诉系统,启动系统打电话功能。
        startActivity(intent);
        //phoneBean.dialPhone("4000245656");
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if ("0".equals(item.getItemId() + "")) {
            getAvatarFromCamera();
        } else if ("1".equals(item.getItemId() + "")) {
            getAvatarFromGallery();
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 通过相册获取头像
     */
    private void getAvatarFromGallery() {
        //Intent intent = new Intent();
        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent, REQUEST_CODE_GALLERY);
        // 以上代码在android5.0以上版本无效，使用下面代码
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    /**
     * 通过拍照获取头像
     */
    private void getAvatarFromCamera() {
        Intent intent = Utils.photo(getActivity());
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
    // 登陆方法
    private void getMyInfo(String url,
                         int resultCode) {
        if (!StringUtil.isNetworkConnected(getActivity())) {
            ToastUtil.makeShortText(getActivity(), "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(getActivity());
        params = new RequestParams();
        params.addBodyParameter(ParserJsonBean.UID,UserInfoBean.uid);
        params.addBodyParameter(ParserJsonBean.TOKEN, UserInfoBean.token);
//        showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, url, params,
                this, httpUtils, resultCode);
    }
    // 获取下级购买记录
    private void getsalesRecord() {
        if (!StringUtil.isNetworkConnected(getActivity())) {
            ToastUtil.makeShortText(getActivity(), "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(getActivity());
        params = new RequestParams();
        params.addBodyParameter(ParserJsonBean.UID,UserInfoBean.uid);
        params.addBodyParameter(ParserJsonBean.TOKEN, UserInfoBean.token);
//        showWaitDialog("正在努力加载...");
        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_SALES_RECORDS, params,
                this, httpUtils, HttpStaticApi.get_sales_records);
    }
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
            case HttpStaticApi.getmyinfo:

                try {
                    Bundle bundle = ParserJsonBean.parserMyInfo(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {

                            strPhone=bundle.getString(ParserJsonBean.PHONE,"");
                            strLevel=bundle.getString(ParserJsonBean.LEVEL,"");
                            direct_type=bundle.getString(ParserJsonBean.direct_type,"");
                           // strPv=bundle.getString(ParserJsonBean.PV,"");
                            UserInfoBean.saveLogo(getActivity(),bundle.getString(ParserJsonBean.LOGO,""));
                            //UserInfoBean.setUserLogo(getActivity(),ParserJsonBean.LOGO);
                            tvPhone.setText(strPhone);
                            tvLevel.setText(strLevel);
                            //rlOnlineLvsuoshu.setVisibility(View.GONE);
                            if ("3".equals(direct_type))   //判断是否为分销用户
                            {
                                rlOnlineMoney.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                rlOnlineMoney.setVisibility(View.GONE);
                            }
                            logo=bundle.getString(ParserJsonBean.LOGO,"");
                            //String path="http://kxhotspring.com/api/Tools/getThumb.php?pic="+logo;
                            /*Bitmap bm = BitmapFactory.decodeFile(path+logo);
                            account_im_head.setImageBitmap(bm);*/
                            //Uri u=java.net.URLEncoder.encode(path, "UTF-8")
                            try {
                                URL thumb_u = new URL(logo);
                                Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
                                account_im_head.setImageDrawable(thumb_d);
                            }
                            catch (Exception e) {
                                // handle it
                            }
                            //tvPv.setText(strPv);


                        } else {
                            ToastUtil.makeShortText(getActivity(),
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case HttpStaticApi.logoEdit:
                try {
                    Bundle bundle = ParserJsonBean.parserMyInfo(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            UserInfoBean.saveLogo(getActivity(),bundle.getString(ParserJsonBean.LOGO,""));
                            ImageLoader.getInstance().displayImage(UserInfoBean.getLogo(getActivity()), account_im_head, MyApplication.optionsPerson);

                        } else {
                            ToastUtil.makeShortText(getActivity(),
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
//            case HttpStaticApi.get_sales_records:
//                try {
//                    Bundle bundle = ParserJsonBean.parseSalesRecords(responseInfo);
//                    if (bundle != null) {
//                        int state = bundle.getInt(ParserJsonBean.STATE);
//                        if (state == 1) {
//                            ArrayList<HashMap<String, Object>> arrayListTemp1 = (ArrayList<HashMap<String, Object>>) bundle
//                                    .getSerializable("list1");
//                            ArrayList<HashMap<String, Object>> arrayListTemp2 = (ArrayList<HashMap<String, Object>>) bundle
//                                    .getSerializable("list2");
//                                arrayList.clear();
//                            if (arrayListTemp1!=null)
//                                arrayList.addAll(arrayListTemp1);
//                            if (arrayListTemp2!=null)
//                            arrayList.addAll(arrayListTemp2);
//
//                            adapter.setData(arrayList);
//                            adapter.notifyDataSetChanged();
//
//                        } else {
//                            ToastUtil.makeLongText(getActivity(),
//                                    bundle.getString(ParserJsonBean.MESSAGE));
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                dismissDialog();
//                break;

            default:
                break;
        }
    }
    public  void startPhotoZoom(Activity activity, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT);

    }
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    String path1;
                    try {
                        path1 = ImageOperate.revitionImageSize(Utils.path, getActivity());
                        File picture = new File(path1);
                        startPhotoZoom(getActivity(), Uri.fromFile(picture));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_CODE_GALLERY:
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        String picturePath = Utils.getRealPathFromURI(getActivity(), selectedImage);
                        try {
                            picturePath = ImageOperate.revitionImageSize(
                                    picturePath, getActivity());
                            File file = new File(picturePath);
                            Bitmap bm = BitmapFactory.decodeFile(picturePath);
                            account_im_head.setImageBitmap(bm);
                            startPhotoZoom(getActivity(), Uri.fromFile(file));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Utils.PHOTORESOULT://这里是是账户Fragment上传头像的结果
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        String uid = UserInfoBean.getUid(getActivity());
                        String token = UserInfoBean.getToken(getActivity());
                        AnsynHttpRequest.uploadFile(getActivity(),
                                UrlConfig.UPLOADPIC, uid, token,
                                ParserJsonBean.LOGO, photo, this,
                                HttpStaticApi.logoEdit);
                        LogUtil.e("头像上传1");
                    }
                    break;
//                case 14:
//                    MyInfo();
//                    break;

            }
        }
    }
}
