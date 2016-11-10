package com.hugboga.custom.activity;

import android.animation.Animator;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import com.hugboga.custom.data.request.RequestCoupon;
import com.hugboga.custom.data.request.RequestCouponExchange;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.AnimationUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.widget.CouponItemView;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.ZListView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;

import java.text.ParseException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 16/8/4.
 */
public class CouponActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final String ORDER_ID = "ORDER_ID";
    public static final String ORDER_PRICE = "ORDER_PRICE";
    public static final String KEY_COUPON = "KEY_COUPON";
    public static final String KEY_COUPON_ID = "KEY_COUPON_ID";

    @Bind(R.id.coupon_listview)
    ZListView listView;
    @Bind(R.id.coupon_btn_carnumber)
    EditText carNumberEditText;
    @Bind(R.id.coupon_listview_empty)
    RelativeLayout emptyLayout;
    @Bind(R.id.coupon_pay_layout)
    RelativeLayout payLayout;

    CouponAdapter adapter;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.coupon_btn_pay)
    Button couponBtnPay;
    private String orderId;
    private double orderPrice;
    private String couponId;
    private int mPageSize = 20;

    private MostFitAvailableBean paramsData;

    private String idStr = null;

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

        setContentView(R.layout.fg_coupon);
        ButterKnife.bind(this);

        initView();
        requestData();
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
        headerTitle.setText("优惠券");
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setEmptyView(emptyLayout);
        listView.setOnItemClickListener(this);
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);
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
            request = new RequestCoupon(this, orderId, orderPrice, pageIndex, mPageSize);
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
                    CommonUtils.showToast("请输入优惠券兑换码");
                    carNumberEditText.requestFocus();
                    return;
                }
                RequestCouponExchange requestCoupon = new RequestCouponExchange(this, couponNum);
                requestData(requestCoupon);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCoupon) {
            RequestCoupon mRequest = (RequestCoupon) request;
            setData(mRequest.getData());
        } else if (request instanceof RequestAvailableCoupon) {
            RequestAvailableCoupon requestAvailableCoupon = (RequestAvailableCoupon) request;
            setData(requestAvailableCoupon.getData());
        } else if (request instanceof RequestCouponExchange) {
            RequestCouponExchange mParser = (RequestCouponExchange) request;
            requestData();
            DialogUtil dialogUtil = DialogUtil.getInstance(this);
            dialogUtil.showCustomDialog("优惠券兑换成功", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    carNumberEditText.setText(""); //清空兑换成功的兑换码
                }
            }).show();
        }
    }

    private void setData(List<CouponBean> list) {
        if (list != null) {
            if (adapter == null) {
                adapter = new CouponAdapter(this, idStr);
                listView.setAdapter(adapter);
                adapter.setList(list);
            } else {
                adapter.addList(list);
            }
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
            try {
                showCouponInfo(bean);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 查看订单详情
     *
     * @param bean
     */
    private void showCouponInfo(CouponBean bean) throws ParseException {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View popView = inflater.inflate(R.layout.coupon_info_layout, null);
        ImageView leftBtn = (ImageView) popView.findViewById(R.id.header_left_btn);
        leftBtn.setImageResource(R.mipmap.closed_btn);
        TextView header_title = (TextView) popView.findViewById(R.id.header_title);
        header_title.setText("我的优惠券");
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
            couponItemView.setTitle(titleContent.title);
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
