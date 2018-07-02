package com.syjjkj.kxwq.ticket;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.syjjkj.kxwq.myview.AmountView;
import com.syjjkj.kxwq.util.StringUtil;
import com.syjjkj.kxwq.util.ToastUtil;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuyTicketActivity extends BaseActivity implements View.OnClickListener{
    private String strID="";
    private String strProductName="";
    private String strPrice="";
    //private String strExpired="";
    private String strTotalPrice;//金额
    private String strReturnPV;
    private String strXFPV;
    private int iTicketCount=1;
    private TextView tvProductName;
    private TextView tvValidity;
    private TextView tvPrice;
    private EditText etContact;
    private EditText etPhone;
    private TextView tvTotalprice;//付款给
    private TextView tvReturnPV;
    private EditText etid_card;
    private Button btnOK;
    private AmountView mAmountView;
    private RelativeLayout rlName;
    private RelativeLayout rlPhone;
    private String zhuyi;
    private String need_idcard;
    private WebView tvzhuyi;
    private TextView tvrl_2;
    private TextView tvxuzhi;
    private String xuzhi;
    private String sel_date;
    private String play_date;
    private RelativeLayout rl_2;
    private RelativeLayout rl_7;
    /*private int year;
    private int month;
    private int day;*/
    final int DATE_DIALOG = 1;
    String dates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);
        initView();
        initListener();
        initData();

    }
    private void initData() {
        Intent intent = getIntent();
        strID = intent.getStringExtra(ParserJsonBean.ID);
        strProductName = intent.getStringExtra(ParserJsonBean.PRODUCT_NAME);
        strPrice = intent.getStringExtra(ParserJsonBean.PRICE);
        //strExpired=intent.getStringExtra(ParserJsonBean.EXPIRED);
        zhuyi = intent.getStringExtra(ParserJsonBean.product_tips);
        xuzhi = intent.getStringExtra(ParserJsonBean.product_detail);
        sel_date = intent.getStringExtra(ParserJsonBean.sel_date);
        play_date = intent.getStringExtra(ParserJsonBean.play_date);
        need_idcard=intent.getStringExtra(ParserJsonBean.need_idcard);
        if (sel_date.equals("0"))
        {
            rl_2.setVisibility(View.GONE);
            dates=play_date;
        }
        if(need_idcard.equals("0"))
        {
            rl_7.setVisibility(View.GONE);
        }
       /*Spanned charSequence;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(zhuyi,Html.FROM_HTML_MODE_LEGACY);
        } else {
            charSequence = Html.fromHtml(zhuyi);
        }
        tvzhuyi.setText(charSequence);*/
        tvzhuyi.loadDataWithBaseURL(null, zhuyi.toString(), "text/html" , "utf-8", null);
       //tvzhuyi.setText(zhuyi);
        tvProductName.setText(strProductName);
        tvPrice.setText(strPrice);
        //tvValidity.setText(strExpired);
        setData();
        mAmountView = (AmountView) findViewById(R.id.buy_ticket_amount_view);
        mAmountView.setGoods_storage(Integer.MAX_VALUE);
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
//                Toast.makeText(getApplicationContext(), "Amount=>  " + amount, Toast.LENGTH_SHORT).show();
                iTicketCount=amount;
                setData();
            }
        });



    }

    private void initView(){
        tvProductName=(TextView) findViewById(R.id.buy_ticket_product_rl_1_tv_1);
        //tvValidity=(TextView) findViewById(R.id.buy_ticket_product_rl_2_tv_validity);
        tvPrice=(TextView) findViewById(R.id.buy_ticket_product_rl_3_tv_price);
        rl_2=(RelativeLayout)findViewById(R.id.rl_2);
        rl_7=(RelativeLayout)findViewById(R.id.rl_7);
        etContact=(EditText) findViewById(R.id.buy_ticket_product_rl_5_et_contact);
        etPhone=(EditText) findViewById(R.id.buy_ticket_product_rl_6_et_phone);
        etid_card=(EditText)findViewById(R.id.buy_ticket_product_rl_7_id_card);
        tvzhuyi=(WebView)findViewById(R.id.buy_ticket_product_rl_1_tv_2);
        tvxuzhi=(TextView)findViewById(R.id.buy_ticket_product_rl_1_tv_3);
        tvTotalprice=(TextView) findViewById(R.id.buy_ticket_product_tv_total_price);
       // tvReturnPV=(TextView) findViewById(R.id.buy_ticket_product_tv_return_pv);
        btnOK=(Button)findViewById(R.id.buy_ticket_product_btn);
        tvrl_2=(TextView)findViewById(R.id.buy_ticket_product_rl_2_tv_validity);
      /*  DatePicker date = (DatePicker) findViewById(R.id.datePicker);
        Calendar ca=Calendar.getInstance();
        year = ca.get(Calendar.YEAR);
        month = ca.get(Calendar.MONTH);
        day = ca.get(Calendar.DAY_OF_MONTH);
        // 初始化DatePicker
        date.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                BuyTicketActivity.this.year = year;
                BuyTicketActivity.this.month = month;
                BuyTicketActivity.this.day = day;
                showDate(year, month, day);

            }
        });*/
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd");
        Date curDate =  new Date(System.currentTimeMillis());
        dates  =  formatter.format(curDate);
        tvrl_2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }});
        tvrl_2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
        rlName=(RelativeLayout) findViewById(R.id.rl_5);
        rlPhone=(RelativeLayout) findViewById(R.id.rl_6);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BuyTicketActivity.this, HomePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
   /* protected void showDate(int year2, int month2, int day2) {
        TextView text = (TextView) findViewById(R.id.buy_ticket_product_rl_2_tv_validity);
        text.setText( year2+"-"+month2 +"-"+day2 );
    }*/
   protected void showDatePickDlg() {
       Calendar calendar = Calendar.getInstance();
       DatePickerDialog datePickerDialog = new DatePickerDialog(BuyTicketActivity.this, new DatePickerDialog.OnDateSetListener() {

           @Override
           public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
           {
               monthOfYear=monthOfYear+1;
               dates=year + "-" + monthOfYear + "-" + dayOfMonth;
               tvrl_2.setText(dates);
           }
       }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
       datePickerDialog.show();

   }
    private void initListener(){
        btnOK.setOnClickListener(this);
        rlName.setOnClickListener(this);
        rlPhone.setOnClickListener(this);
        tvxuzhi.setOnClickListener(this);
    }
    private void setData(){
//        iTicketCount=
        //strTotalPrice= String.valueOf(BigDecimal.valueOf(Double.valueOf(strPrice)*iTicketCount));
        BigDecimal bd=(BigDecimal.valueOf(Double.valueOf(strPrice)*iTicketCount));
        bd=bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        strTotalPrice=String.valueOf(bd);
        /*Bigdecimal（(iTicketCount*(Float.valueOf(strPrice)))*/;
        tvTotalprice.setText(String.valueOf(strTotalPrice));
        strReturnPV=String.valueOf(iTicketCount*(Float.valueOf(strPrice))* UserInfoBean.getTicketstypeCount(this));
        tvrl_2.setText(dates);
        //tvReturnPV.setText(strReturnPV);

    }
    // 购票
    private void buyTickets(String strID,String strTicketCount,String strContact,String strPhone,String strTotalPrice,String strXFPV,String dates,String idcard)
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        params.addBodyParameter("product_id", strID);
        params.addBodyParameter("product_num", strTicketCount);
        params.addBodyParameter("contact", strContact);
        params.addBodyParameter("id_card",idcard);
        params.addBodyParameter(ParserJsonBean.PHONE, strPhone);
        params.addBodyParameter(ParserJsonBean.MONEY,strTotalPrice);
        params.addBodyParameter("pv", strXFPV);
        params.addBodyParameter("playDate",dates);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.BUY_TICKETS, params,
                this, httpUtils, HttpStaticApi.buy_tickets);
    }
    // 购票
    private void getPV()
    {
        if (!StringUtil.isNetworkConnected(this)) {
            ToastUtil.makeShortText(this, "请检查网络");
            return;
        }
        UserInfoBean.getUserInfo(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", UserInfoBean.uid);
        params.addBodyParameter("token", UserInfoBean.token);
        httpUtils = new HttpUtils();
//		showWaitDialog("正在努力加载...");

        AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, UrlConfig.GET_PV, params,
                this, httpUtils, HttpStaticApi.get_pv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buy_ticket_product_btn:

                if (StringUtil.isEmpty(iTicketCount+"")){
                    ToastUtil.makeShortText(this,"请选择购买票数");
                    return;
                }
                if (StringUtil.isEmpty(etContact.getText().toString().trim())){
                    ToastUtil.makeShortText(this,"请填写联系人");
                    return;
                }
                if(etContact.getText().length()>4)
                {
                    ToastUtil.makeShortText(this,"姓名至多4位汉子");
                    return;
                }
                if (StringUtil.isEmpty(etPhone.getText().toString().trim())){
                    ToastUtil.makeShortText(this,"请填写电话");
                    return;
                }
                if (!isMatchered(PHONE_PATTERN,etPhone.getText()))
                {
                    ToastUtil.makeShortText(this,"请填写11位电话");
                    return;
                }
                if(need_idcard.equals("1"))
                {
                    if(StringUtil.isEmpty(etid_card.getText().toString().trim()))
                    {
                        ToastUtil.makeShortText(this,"请填写身份证号");
                        return;
                    }
                    if(!isMatchered(REGEX_ID_CARD,etid_card.getText().toString().trim()))
                    {
                        ToastUtil.makeShortText(this,"身份证格式不正确");
                        return;
                    }
                }
                if (sel_date.equals("0"))
                {
                    dates=play_date;
                }
                else{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//年-月-日 时-分
                    try {
                        Date date1 = dateFormat.parse(dateFormat.format(new Date()));//当前日期
                        Date date2 = dateFormat.parse(dates.toString());  //用户选择日期
                        if (date2.getTime()<date1.getTime()){
                            Toast.makeText(BuyTicketActivity.this,"游玩日期小于当前日期", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                buyTickets(strID,iTicketCount+"",etContact.getText().toString().trim(),etPhone.getText().toString().trim(),strTotalPrice,strXFPV,dates,etid_card.getText().toString().trim());
                break;
            /*case  R.id.rl_5:
                etContact.requestFocus();

                break;
            case R.id.rl_6:
                etPhone.requestFocus();
                break;*/
            case R.id.buy_ticket_product_rl_1_tv_3:
                Intent intent=new Intent(this,BuyZhiDaoActivity.class);
                intent.putExtra("tips",xuzhi);
                startActivity(intent);
        }


    }
    public static boolean isMatchered(String patternStr, CharSequence input) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
    public final static String PHONE_PATTERN = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[0-9])|(17[0-9])|(16[0-9])|(19[0-9]))\\d{8}$";
    public static final String REGEX_ID_CARD ="^\\d{15}$|^\\d{17}[0-9Xx]$";
    @Override
    public void onSuccessHttp(String responseInfo, int resultCode) {
        dismissDialog();
        super.onSuccessHttp(responseInfo, resultCode);
        switch (resultCode) {
            /*case HttpStaticApi.get_pv:
                try {
                    Bundle bundle = ParserJsonBean.parserPV(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            String uid = bundle.getString(ParserJsonBean.UID);
                            String pv = bundle.getString(ParserJsonBean.PV);



                        } else {
                            ToastUtil.makeShortText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;*/
            case HttpStaticApi.buy_tickets:
                try {
                    Bundle bundle = ParserJsonBean.parserBuyTickets(responseInfo);
                    if (bundle != null) {
                        if (bundle.getInt(ParserJsonBean.STATE) == 1) {
                            String order_id = bundle.getString(ParserJsonBean.ID);
                            String sid = bundle.getString(ParserJsonBean.SID);

//                            ToastUtil.makeShortText(this,sid+"购票成功");
                            String buy_type=bundle.getString("buy_type");
                            Intent intent=new Intent(this,PayTypeActivity.class);
                            intent.putExtra("strTotalPrice",strTotalPrice);
                           // intent.putExtra(ParserJsonBean.PV,strReturnPV);
                            intent.putExtra("buy_type",buy_type);
                            intent.putExtra("order_id",order_id);
                            startActivity(intent);
                        } else {
                            ToastUtil.makeShortText(this,
                                    bundle.getString(ParserJsonBean.MESSAGE));
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

}
