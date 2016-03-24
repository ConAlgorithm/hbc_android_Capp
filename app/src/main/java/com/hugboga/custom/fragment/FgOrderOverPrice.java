package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.OverPriceAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.constants.ResourcesConstants;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.OrderOverPrice;
import com.hugboga.custom.data.bean.OrderPriceInfo;
import com.hugboga.custom.data.request.RequestOverPrice;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 订单增项费用
 * Created by admin on 2015/7/31.
 */

@ContentView(R.layout.fg_order_over_price)
public class FgOrderOverPrice extends BaseFragment {

    public static final String KEY_ORDER_NO = "KEY_ORDER_NO";
    public static final String KEY_ORDER_PRICE = "KEY_ORDER_PRICE";
    public static final String KEY_ORDER_COUPON = "KEY_ORDER_COUPON";
    public static final String KEY_ORDER_IS_READ = "KEY_ORDER_ADDITION_IS_Read";

    @ViewInject(R.id.over_price_list)
    private ListView listView;

    //价格
    @ViewInject(R.id.order_pay_total_price)
    private TextView paySuccessPrice;//支付成功金额Value
    @ViewInject(R.id.pay_total_title)
    private TextView orderPayTotalTitle;//总价title
    @ViewInject(R.id.pay_total_value)
    private TextView orderPayTotal;//总价
    @ViewInject(R.id.pay_should_value)
    private TextView orderShouldTotal;//总价
    @ViewInject(R.id.pay_coupons_value)
    private TextView orderPayCoupons;//优惠券
    @ViewInject(R.id.pay_coupons_layout)
    private View orderPayCouponsLayout;//优惠券layout
    @ViewInject(R.id.pay_actual_layout)
    private View orderPayActualLayout;//实付layout
    @ViewInject(R.id.pay_check_in_layout)
    private View orderPayCheckInLayout;//送机CheckIn layout
    @ViewInject(R.id.pay_check_in_value)
    private TextView orderPayCheckInValue;//送机CheckIn
    @ViewInject(R.id.pay_coupons_arrow)
    private View orderPayCouponsArrow;//优惠券箭头
    @ViewInject(R.id.pay_need_title)
    private TextView orderShouldPayTitle;//需要支付title
    @ViewInject(R.id.pay_actual_value)
    private TextView orderActualPay;//需要支付

    private OrderPriceInfo mPriceInfo;
    private OrderOverPrice mOverPrice;
    private CouponBean mOrderCoupon;
    private String orderNo;
    private int additionIsRead;
    private String applyfee;

    @Override
    protected void initHeader() {
        fgTitle.setText("增项费用");

    }
    @Override
    protected void initView(){
        mPriceInfo = (OrderPriceInfo) getArguments().getSerializable(KEY_ORDER_PRICE);
        mOrderCoupon = (CouponBean) getArguments().getSerializable(KEY_ORDER_COUPON);
        orderNo = getArguments().getString(KEY_ORDER_NO);
        additionIsRead = getArguments().getInt(KEY_ORDER_IS_READ);

        //优惠券金额
        if (mOrderCoupon != null && mOrderCoupon.price != null) {
            orderPayCouponsLayout.setVisibility(View.VISIBLE);
            orderPayCoupons.setText(mOrderCoupon.price);
        }else{
            //没有使用优惠券，则直接不显示
            orderPayCouponsLayout.setVisibility(View.GONE);
        }
        orderPayTotal.setText(mPriceInfo.orderPrice + "元");
        orderShouldTotal.setText(mPriceInfo.shouldPay + "元");
        orderActualPay.setText(mPriceInfo.actualPay + "元");
        orderPayCheckInLayout.setVisibility(Double.isNaN(mPriceInfo.checkInPrice)? View.GONE: View.VISIBLE);
        orderPayCheckInValue.setText(mPriceInfo.checkInPrice+"元");
        paySuccessPrice.setText(String.valueOf(mPriceInfo.actualPay));
        orderPayTotalTitle.setText(Constants.TitleMap2.get(mBusinessType));
    }
    @Override
    protected Callback.Cancelable requestData() {

        if(additionIsRead!=-1) {
            listView.setVisibility(View.VISIBLE);
            RequestOverPrice request = new RequestOverPrice(getActivity(),orderNo);
            return requestData(request);
        }else{
            listView.setVisibility(View.GONE);
        }
        return null;
    }

    @Override
    protected void inflateContent() {
        if(mOverPrice.orderCostApplyInfos!=null&&mOverPrice.orderCostApplyInfos.size()>0) {
            OverPriceAdapter adapter = new OverPriceAdapter(getActivity());
            adapter.setList(mOverPrice.orderCostApplyInfos);
            View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_over_price_header, null);
            View footView = LayoutInflater.from(getActivity()).inflate(R.layout.list_over_price_footer, null);
            TextView actualValue = (TextView) footView.findViewById(R.id.pay_over_actual_value);
            actualValue.setText(applyfee);
            footView.setVisibility(View.VISIBLE);
            listView.addHeaderView(headerView);
            listView.addFooterView(footView);
            listView.setAdapter(adapter);
        }else{
            listView.setVisibility(View.GONE);
        }
    }

    @Event({R.id.over_price_tip})
    private void onClickView(View view) {
        switch (view.getId()){
            case R.id.over_price_tip:
                Bundle bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, ResourcesConstants.OverPriceMap.get(mBusinessType));
                startFragment( new FgWebInfo(),bundle);
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        RequestOverPrice mParser = (RequestOverPrice)request;
        mOverPrice = new OrderOverPrice();
        applyfee = mParser.applyfee;
        mOverPrice.orderCostApplyInfos =mParser.getData();
        inflateContent();
    }
}
