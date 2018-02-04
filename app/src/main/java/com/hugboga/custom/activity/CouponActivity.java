package com.hugboga.custom.activity;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CouponAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.CouponTitleContent;
import com.hugboga.custom.data.bean.MostFitAvailableBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestAvailableCoupon;
import com.hugboga.custom.data.request.RequestCouponExchange;
import com.hugboga.custom.data.request.RequestUnusedCoupon;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.AnimationUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CouponItemView;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.ZListView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by on 16/8/4.
 */
public class CouponActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final String ORDER_ID = "ORDER_ID";
    public static final String ORDER_PRICE = "ORDER_PRICE";
    public static final String KEY_COUPON = "KEY_COUPON";
    public static final String KEY_COUPON_ID = "KEY_COUPON_ID";

    @BindView(R.id.coupon_listview)
    ZListView listView;
    @BindView(R.id.coupon_btn_carnumber)
    EditText carNumberEditText;
    @BindView(R.id.coupon_listview_empty)
    RelativeLayout emptyLayout;
    @BindView(R.id.coupon_pay_layout)
    LinearLayout payLayout;

    CouponAdapter adapter;
    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.header_right_btn)
    ImageView headerRightBtn;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.header_right_txt)
    TextView headerRightTxt;
    @BindView(R.id.coupon_btn_pay)
    Button couponBtnPay;
    @BindView(R.id.next)
    TextView next;
    @BindView(R.id.des)
    TextView des;
    private String orderId;
    private double orderPrice;
    private String couponId;
    private int mPageSize = 20;
    private boolean isFromMyspace = false;
    private MostFitAvailableBean paramsData;
    private ImageView headerSelectedIV;
    private boolean couponExchange = false;

    private String idStr = null;

    @Override
    public int getContentViewId() {
        return R.layout.fg_coupon;
    }

    @Override
    public String getEventSource() {
        return "我的优惠券";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (MostFitAvailableBean) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                paramsData = (MostFitAvailableBean) bundle.getSerializable(Constants.PARAMS_DATA);
                idStr = bundle.getString("idStr");
                orderId = bundle.getString(ORDER_ID, "");
                couponId = bundle.getString(KEY_COUPON_ID, "");
                orderPrice = bundle.getDouble(ORDER_PRICE);
            }
        }

        initView();
        requestData();
        carNumberEditText.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable editable) {
                setCouponBtnPay();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });
        setSensorsPageViewEvent("优惠券", SensorsConstant.COUPON);
        setSensorsDefaultEvent();
    }

    protected boolean isDefaultEvent(){
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideInputMethod(carNumberEditText);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    private void initView() {
        isFromMyspace = getIntent().getBooleanExtra("isFromMyspace",false);
        if(isFromMyspace){
            headerTitle.setText(R.string.coupon_title);
        }else {
            headerTitle.setText(R.string.coupon_title_choose);
        }

        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventAction(EventType.SETTING_BACK));
                finish();
            }
        });
        headerRightTxt.setVisibility(View.VISIBLE);
        headerRightTxt.setText(R.string.coupon_instructions);
        headerRightTxt.setTextColor(0xff151515);
        headerRightTxt.setTextSize(15);
        headerRightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CouponActivity.this, CouponDesActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);
            }
        });
        listView.setEmptyView(emptyLayout);
        listView.setOnItemClickListener(this);
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);
        LinearLayout mFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.coupon_listview, null);
        TextView footer = (TextView) mFooter.findViewById(R.id.footer);
        if(isFromMyspace){
            footer.setText(R.string.coupon_expiry);
        }else{
            footer.setText(R.string.coupon_disabled);
        }
        listView.addFooterView(mFooter);
        if (paramsData != null) {
            RelativeLayout headerView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.view_coupon_header, null);
            headerView.findViewById(R.id.coupon_header_item_selected).setVisibility(TextUtils.isEmpty(idStr) ? View.VISIBLE : View.GONE);
            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventAction(EventType.SELECT_COUPON_BACK, null));
                    finish();
                }
            });
            listView.addHeaderView(headerView);
        }

        next.setVisibility(View.GONE);
        mFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CouponActivity.this, CouponInvalidActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PARAMS_DATA, paramsData);
                intent.putExtras(bundle);
                intent.putExtra("isFromMyspace",isFromMyspace);
                startActivity(intent);
            }
        });
        setCouponBtnPay();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CouponActivity.this, CouponInvalidActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PARAMS_DATA, paramsData);
                intent.putExtras(bundle);
                intent.putExtra("isFromMyspace",isFromMyspace);
                startActivity(intent);
            }
        });
        des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CouponActivity.this, CouponDesActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);
            }
        });

        if(!isFromMyspace){
            next.setText(R.string.coupon_disabled);
        }
    }
    private void setCouponBtnPay(){
        if(carNumberEditText!= null && couponBtnPay != null){
            if(carNumberEditText.getText().toString().trim().length() >0){
                couponBtnPay.setEnabled(true);
                //couponBtnPay.setBackgroundColor(getResources().getColor(R.color.all_bg_yellow));
            }else {
                couponBtnPay.setEnabled(false);
                //couponBtnPay.setBackgroundColor(getResources().getColor(R.color.login_unready));
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new EventAction(EventType.SETTING_BACK));
        finish();
    }

    ZListView.OnRefreshListener onRefreshListener = new ZListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (adapter != null) {
                adapter = null;
            }
            runData(orderId, 0);
        }
    };

    ZListView.OnLoadListener onLoadListener = new ZListView.OnLoadListener() {
        @Override
        public void onLoad() {
            if (adapter.getCount() > 0) {
                runData(orderId, adapter == null ? 0 : adapter.getCount());
            }
        }
    };


    private Callback.Cancelable runData(String orderId, int pageIndex) {
        BaseRequest request = null;
        if (paramsData == null) {
            request = new RequestUnusedCoupon(this, pageIndex, mPageSize);
        } else {
            request = new RequestAvailableCoupon(this, paramsData, pageIndex);
        }
        return requestData(request);
    }

    private void requestData() {
        //默认加载所有优惠券
        if (adapter != null) {
            adapter = null;
        }
        runData(orderId, 0);
    }

    @OnClick({R.id.coupon_btn_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.coupon_btn_pay:
                //兑换优惠券
                collapseSoftInputMethod(carNumberEditText);
                String couponNum = carNumberEditText.getText().toString();
                if (couponNum.isEmpty()) {
                    CommonUtils.showToast(R.string.coupon_input_num_hint);
                    carNumberEditText.requestFocus();
                    return;
                }
                RequestCouponExchange requestCoupon = new RequestCouponExchange(this, couponNum);
                requestData(requestCoupon);
                SensorsUtils.onAppClick("优惠券","兑换",getIntentSource());
                break;
            default:
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestUnusedCoupon) {
            RequestUnusedCoupon mRequest = (RequestUnusedCoupon) request;
            setData(mRequest.getData());
        } else if (request instanceof RequestAvailableCoupon) {
            RequestAvailableCoupon requestAvailableCoupon = (RequestAvailableCoupon) request;
            setData(requestAvailableCoupon.getData());
        } else if (request instanceof RequestCouponExchange) {
            RequestCouponExchange mParser = (RequestCouponExchange) request;
            couponExchange = true;
            requestData();
            DialogUtil dialogUtil = DialogUtil.getInstance(this);
            dialogUtil.showCustomDialog(CommonUtils.getString(R.string.coupon_exchange_success), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    carNumberEditText.setText(""); //清空兑换成功的兑换码
                }
            }).show();
        }
    }

    private void setData(List<CouponBean> list) {
        if (list != null && list.size()>0) {
            if (adapter == null) {
                adapter = new CouponAdapter(this, idStr);
                listView.setAdapter(adapter);
                adapter.setList(list);
            } else {
                adapter.addList(list);
            }
            if (TextUtils.isEmpty(idStr) && couponExchange) {
                EventBus.getDefault().post(new EventAction(EventType.SELECT_COUPON_BACK, null));
            }
        }
        if(adapter != null && adapter.getCount() > 0){
            next.setVisibility(View.GONE);
        }else{
            next.setVisibility(View.VISIBLE);
        }
        if (list != null && list.size() < mPageSize) {
            listView.onLoadCompleteNone();
        } else {
            listView.onLoadComplete();
        }
        listView.onRefreshComplete();
    }

    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (paramsData != null) {
            position--;
        }
        if (position >= adapter.getCount()) {
            adapter.notifyDataSetChanged();
            return;
        }
        CouponBean bean = (CouponBean) adapter.getItem(position - 1);
        if (paramsData != null) {
            EventBus.getDefault().post(new EventAction(EventType.SELECT_COUPON_BACK, bean));
            finish();
        } else if (!TextUtils.isEmpty(orderId)) {
            //点击回传优惠券
            if (bean != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_COUPON, bean);
                EventBus.getDefault().post(new EventAction(EventType.SELECT_COUPON_BACK, bean));
            }
        } else {
            //点击查看详情
            showCouponInfoByDialog(bean);
        }
    }
    /**
     * 查看订单详情,弹框型
     *
     * @param bean
     */
    private void showCouponInfoByDialog(CouponBean bean){
        RelativeLayout rl = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.coupon_info_layout, null);

        TextView rule= ((TextView) rl.findViewById(R.id.coupon_info_rule));
        TextView content = ((TextView) rl.findViewById(R.id.coupon_info_content));
        TextView price = ((TextView) rl.findViewById(R.id.coupon_info_price));

        SpannableString spannableString = new SpannableString(bean.price);
        if(bean.price.endsWith("折")){
            spannableString.setSpan(new AbsoluteSizeSpan(50), bean.price.length() - 3, bean.price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if(bean.price.endsWith("元")){
            spannableString.setSpan(new AbsoluteSizeSpan(50), bean.price.length() - 1, bean.price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        price.setText(spannableString);
        TextPaint paint = price.getPaint();
        int priceWight = (int) paint.measureText(price.getText().toString());
        rule.setText(bean.batchName);
        content.setText(bean.applyRule);

        //根据价格宽度,边距动态调整rule的宽度
        RelativeLayout.LayoutParams ruleLp = (RelativeLayout.LayoutParams) rule.getLayoutParams();
        ruleLp.width = UIUtils.dip2px(330)-priceWight- UIUtils.dip2px(15)*2-UIUtils.dip2px(10);
        ruleLp.height=RelativeLayout.LayoutParams.WRAP_CONTENT;
        rule.setLayoutParams(ruleLp);
        //根据价格宽度,边距动态调整content的宽度
        RelativeLayout.LayoutParams contentLp = (RelativeLayout.LayoutParams) content.getLayoutParams();
        contentLp.width = UIUtils.dip2px(330)-priceWight- UIUtils.dip2px(15)*2-UIUtils.dip2px(10);
        contentLp.height=RelativeLayout.LayoutParams.WRAP_CONTENT;
        content.setLayoutParams(contentLp);

        if(bean.couponStatus.equals(2) || bean.couponStatus.equals(-1) || bean.couponStatus.equals(98)){
            ((TextView) rl.findViewById(R.id.coupon_info_price)).setTextColor(getResources().getColor(R.color.common_font_color_gray2));
        }

        setCouponStatus(bean,rl);

        if (bean.endDate.equals("0")) {
            ((TextView) rl.findViewById(R.id.coupon_info_limit_time)).setText("有效期：长期有效");
        } else {
            ((TextView) rl.findViewById(R.id.coupon_info_limit_time)).setText("有效期：" + bean.startDate + " 至 " + bean.endDate);
        }

        LinearLayout dataListLayout = (LinearLayout) rl.findViewById(R.id.data_list_layout);
        CouponItemView couponItemView;
        for(CouponTitleContent titleContent:bean.dataList){
            couponItemView = new CouponItemView(activity);
            if(bean.couponStatus.equals(2) || bean.couponStatus.equals(-1) || bean.couponStatus.equals(98)){
                couponItemView.setTitleType2(titleContent.title);
            }else if(bean.couponStatus.equals(1)){
                couponItemView.setTitleType1(titleContent.title);
            }
            couponItemView.setContent(titleContent.content);
            dataListLayout.addView(couponItemView);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(rl);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width= UIUtils.dip2px(330);
        lp.height=UIUtils.dip2px(380);
        dialogWindow.setAttributes(lp);
    }

    private void setCouponStatus(CouponBean bean,RelativeLayout rl){
        if(bean != null){
            if(bean.couponStatus.equals(2)){
                rl.findViewById(R.id.coupon_invalid).setVisibility(View.VISIBLE);
                ((TextView) rl.findViewById(R.id.coupon_invalid)).setText("已使用");
            }else if(bean.couponStatus.equals(-1)){
                rl.findViewById(R.id.coupon_invalid).setVisibility(View.VISIBLE);
                ((TextView) rl.findViewById(R.id.coupon_invalid)).setText("已过期");
            }else if(bean.couponStatus.equals(98)){
                rl.findViewById(R.id.coupon_invalid).setVisibility(View.VISIBLE);
                ((TextView) rl.findViewById(R.id.coupon_invalid)).setText("已冻结");
            }else if(bean.couponStatus.equals(1)){
                rl.findViewById(R.id.coupon_invalid).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 查看订单详情,整页型
     *
     * @param bean
     */
    private void showCouponInfo(CouponBean bean) throws ParseException {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View popView = inflater.inflate(R.layout.coupon_info_layout, null);
        ImageView leftBtn = (ImageView) popView.findViewById(R.id.header_left_btn);
        leftBtn.setImageResource(R.mipmap.top_close);
        TextView header_title = (TextView) popView.findViewById(R.id.header_title);
        header_title.setText(R.string.coupon_title);
        final PopupWindow pw = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //内容赋值
        ((TextView) popView.findViewById(R.id.coupon_info_price)).setText(bean.price);
        ((TextView) popView.findViewById(R.id.coupon_info_rule)).setText(bean.batchName);
        ((TextView) popView.findViewById(R.id.coupon_info_content)).setText(bean.applyRule);
        if (bean.endDate.equals("0")) {
            ((TextView) popView.findViewById(R.id.coupon_info_limit_time)).setText("有效期：长期有效");
        } else {
            ((TextView) popView.findViewById(R.id.coupon_info_limit_time)).setText("有效期：" + bean.startDate + " 至 " + bean.endDate);
        }
        LinearLayout dataListLayout = (LinearLayout) popView.findViewById(R.id.data_list_layout);
        CouponItemView couponItemView;
        for(CouponTitleContent titleContent:bean.dataList){
            couponItemView = new CouponItemView(activity);
            //couponItemView.setTitle(titleContent.title);
            couponItemView.setContent(titleContent.content);
            dataListLayout.addView(couponItemView);
        }
        //设置后进行展示
        pw.setBackgroundDrawable(new ColorDrawable(0));
        pw.setFocusable(true);
//        pw.setOutsideTouchable(true);
        pw.showAtLocation(emptyLayout, Gravity.CENTER, 0, 0);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtils.hideAnimation(popView, 500, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        pw.dismiss();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
//                pw.dismiss();
            }
        });

        AnimationUtils.showAnimation(popView, 500, null);
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_COUPON;
    }

}
