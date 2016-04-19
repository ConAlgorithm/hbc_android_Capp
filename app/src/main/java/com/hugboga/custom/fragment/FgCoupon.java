package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CouponAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.request.RequestCoupon;
import com.hugboga.custom.data.request.RequestCouponExchange;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.ParseException;

/**
 * Created by ZHZEPHI on 2015/7/24.
 */
@ContentView(R.layout.fg_coupon)
public class FgCoupon extends BaseFragment implements AdapterView.OnItemClickListener {

    public static final String ORDER_ID = "ORDER_ID";
    public static final String ORDER_PRICE = "ORDER_PRICE";
    public static final String KEY_COUPON = "KEY_COUPON";
    public static final String KEY_COUPON_ID = "KEY_COUPON_ID";

    @ViewInject(R.id.coupon_listview)
    ZListView listView;
    @ViewInject(R.id.coupon_btn_carnumber)
    EditText carNumberEditText;
    @ViewInject(R.id.coupon_listview_empty)
    RelativeLayout emptyLayout;

    CouponAdapter adapter;
    private String orderId;
    private double orderPrice;
    private String couponId;
    private int mPageSize = 20;


    @Override
    protected void initHeader() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderId = bundle.getString(ORDER_ID, "");
            couponId = bundle.getString(KEY_COUPON_ID, "");
            orderPrice = bundle.getDouble(ORDER_PRICE);
        }

        fgTitle.setText("我的优惠券");
        listView.setEmptyView(emptyLayout);
        listView.setOnItemClickListener(this);
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);
    }

    @Override
    protected void initView() {

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
        RequestCoupon requestCoupon = new RequestCoupon(getActivity(), orderId, orderPrice, pageIndex, mPageSize);
        return requestData(requestCoupon);
    }

    @Override
    protected Callback.Cancelable requestData() {
        //默认加载所有优惠券

        if (adapter != null) {
            adapter = null;
        }
        return runData(orderId, 0);
    }

    @Override
    protected void inflateContent() {
    }

    @Event({R.id.coupon_btn_pay})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.coupon_btn_pay:
                //兑换优惠券
                collapseSoftInputMethod();
                String couponNum = carNumberEditText.getText().toString();
                if (couponNum.isEmpty()) {
                    showTip("请输入优惠券兑换码");
                    carNumberEditText.requestFocus();
                    return;
                }
                RequestCouponExchange requestCoupon = new RequestCouponExchange(getActivity(), couponNum);
                requestData(requestCoupon);
                break;
            default:
                break;
        }
    }
//    @Override
//    public void onDataRequestSucceed(BaseRequest request) {

    //    }
    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCoupon) {
            RequestCoupon mRequest = (RequestCoupon) request;
            if (mRequest.getData() != null) {
                if (adapter == null) {
                    adapter = new CouponAdapter(getActivity());
                    adapter.idStr = couponId;
                    listView.setAdapter(adapter);
                    adapter.setList(mRequest.getData());
                } else {
                    adapter.addList(mRequest.getData());
                }
            }
            if (mRequest.getData() != null && mRequest.getData().size() < mPageSize) {
                listView.onLoadCompleteNone();
            } else {
                listView.onLoadComplete();
            }
            listView.onRefreshComplete();
        } else if (request instanceof RequestCouponExchange) {
            RequestCouponExchange mParser = (RequestCouponExchange) request;
            requestData();
            DialogUtil dialogUtil = DialogUtil.getInstance(getActivity());
            dialogUtil.showCustomDialog("优惠券兑换成功", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    carNumberEditText.setText(""); //清空兑换成功的兑换码
                }
            }).show();
        }
    }

    @Override
    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CouponBean bean = (CouponBean) adapter.getItem(position - 1);
        if (!TextUtils.isEmpty(orderId)) {
            //点击回传优惠券
            if (bean != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_COUPON, bean);
                finishForResult(bundle);
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
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View popView = inflater.inflate(R.layout.coupon_info_layout, null);
        if (bean.couponStatus == 1) {
            //可用
            popView.findViewById(R.id.coupon_info_layout).setBackgroundResource(R.mipmap.coupon_details);
            popView.findViewById(R.id.coupon_info_price_line).setBackgroundResource(R.drawable.coupon_dash_line);
            popView.findViewById(R.id.coupon_info_content_line).setBackgroundColor(Color.parseColor("#E997A4"));
        } else {
            //已使用，已过期
            popView.findViewById(R.id.coupon_info_layout).setBackgroundResource(R.mipmap.coupon_details_no);
            popView.findViewById(R.id.coupon_info_price_line).setBackgroundResource(R.drawable.coupon_dash_line2);
            popView.findViewById(R.id.coupon_info_content_line).setBackgroundColor(Color.parseColor("#BCBCBC"));
        }

        final PopupWindow pw = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //内容赋值
        ((TextView) popView.findViewById(R.id.coupon_info_price)).setText(bean.price);
        ((TextView) popView.findViewById(R.id.coupon_info_rule)).setText(bean.batchName);
        if (bean.endDate.equals("0")) {
            ((TextView) popView.findViewById(R.id.coupon_info_limit_time)).setText("有效期：长期有效");
        } else {
            ((TextView) popView.findViewById(R.id.coupon_info_limit_time)).setText("有效期：" + bean.startDate + " 至 " + bean.endDate);
        }
        ((TextView) popView.findViewById(R.id.coupon_info_limit1_content)).setText(bean.applyArea);
        ((TextView) popView.findViewById(R.id.coupon_info_limit2_content)).setText(bean.applyType);
        ((TextView) popView.findViewById(R.id.coupon_info_limit3_content)).setText(bean.applyCar);
        ((TextView) popView.findViewById(R.id.coupon_info_content)).setText(bean.applyRule);
        ((TextView) popView.findViewById(R.id.coupon_info_memo)).setText("详细说明：" + bean.content);
        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        //设置后进行展示
        pw.setBackgroundDrawable(new ColorDrawable(0));
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.showAtLocation(emptyLayout, Gravity.CENTER, 0, 0);
    }


}
