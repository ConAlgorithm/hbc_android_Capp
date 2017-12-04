package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestOrderCancel;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.DialogUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by on 16/8/4.
 */
public class OrderCancelActivity extends BaseActivity{

    @BindView(R.id.order_cancel_type_value)
    TextView orderType;
    @BindView(R.id.order_cancel_no_value)
    TextView orderNoValue;
    @BindView(R.id.order_cancel_apply_value)
    TextView orderApplyValue;//已付金额
    @BindView(R.id.order_cancel_back_value)
    TextView orderBack;//退改费用
    @BindView(R.id.order_cancel_cancel_layout)
    View orderCancelLayout;//可退金额layout
    @BindView(R.id.order_cancel_cancel_value)
    TextView orderCancel;//可退金额
    @BindView(R.id.order_cancel_travel_fund_layout)
    FrameLayout travelFundLyout;//可退旅游基金
    @BindView(R.id.order_cancel_travel_fund_value)
    TextView travelFundTV;
    @BindView(R.id.order_cancel_travel_fund_title_tv)
    TextView travelFundTitleTV;

    private OrderBean orderBean;
    private DialogUtil mDialogUtil;
    CsDialog csDialog;
    @Override
    public int getContentViewId() {
        return R.layout.fg_order_cancel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialogUtil != null) {
            mDialogUtil.dismissDialog();
        }
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            source = bundle.getString("source");
            orderBean = (OrderBean) bundle.getSerializable(Constants.PARAMS_DATA);
        }
        initDefaultTitleBar();
        fgTitle.setText(R.string.cancel_title);
        mDialogUtil = DialogUtil.getInstance(this);
        orderType.setText(OrderUtils.getOrderTypeStr(orderBean.orderType));
        orderNoValue.setText(orderBean.orderNo);
        orderApplyValue.setText(orderBean.orderPriceInfo.actualPay+"元");
        orderBack.setText(orderBean.orderPriceInfo.cancelFee + "元");

        if (orderBean.orderPriceInfo.travelFundRefundable > 0) {//可退旅游基金
            travelFundLyout.setVisibility(View.VISIBLE);
            travelFundTitleTV.setText("可退旅游基金");
            travelFundTV.setText(orderBean.orderPriceInfo.travelFundRefundable + "元");
//        } else if (orderBean.orderPriceInfo.couponPrice > 0) {////可退优惠劵
//            travelFundLyout.setVisibility(View.VISIBLE);
//            travelFundTitleTV.setText(orderBean.orderPriceInfo.couponRefundStatus == 1 ? R.string.order_detail_refund_coupon2 : R.string.order_detail_refund_coupon);
//            travelFundTV.setText(orderBean.orderPriceInfo.couponPrice + "元");
        } else {
            travelFundLyout.setVisibility(View.GONE);
        }

        if(orderBean.cancelable){
            findViewById(R.id.order_cancel_btn).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.order_cancel_btn).setVisibility(View.INVISIBLE);
        }
        if (Double.isNaN(orderBean.orderPriceInfo.refundablePrice)) {
            orderCancelLayout.setVisibility(View.GONE);
        } else {
            orderCancelLayout.setVisibility(View.VISIBLE);
            orderCancel.setText(orderBean.orderPriceInfo.refundablePrice + "元");
        }
    }

    @OnClick({R.id.order_cancel_btn})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_cancel_btn:
                if(orderBean==null)return;
                AlertDialogUtils.showAlertDialog(OrderCancelActivity.this, "确定要取消订单吗？", "我要取消", "暂不取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cancelOrder();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
            default:
                break;
        }
    }

    private void cancelOrder() {
        double cancelPrice = orderBean.orderPriceInfo.refundablePrice;
        if(cancelPrice<0)cancelPrice=0;
        RequestOrderCancel request = new RequestOrderCancel(this,orderBean.orderNo,cancelPrice,orderBean.cancelReason);
        requestData(request);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE, orderBean.orderNo));
        EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
        DialogUtil dialogUtil = DialogUtil.getInstance(this);
        dialogUtil.showCustomDialog("取消订单成功", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goBackOrderFg();
                finish();
            }
        });
    }

    private void goBackOrderFg() {
        OrderDetailActivity.Params params = new OrderDetailActivity.Params();
        params.orderType = orderBean.orderType;
        params.orderId = orderBean.orderNo;
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }

    @Override
    public String getEventSource() {
        return "取消订单";
    }
}
