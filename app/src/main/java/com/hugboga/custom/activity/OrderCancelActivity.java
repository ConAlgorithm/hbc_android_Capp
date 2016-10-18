package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestOrderCancel;
import com.hugboga.custom.fragment.FgTravel;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 16/8/4.
 */
public class OrderCancelActivity extends BaseActivity{

    public static final String KEY_ORDER = "KEY_ORDER";

    @Bind(R.id.order_cancel_type_value)
    TextView orderType;
    @Bind(R.id.order_cancel_no_value)
    TextView orderNoValue;
    @Bind(R.id.order_cancel_apply_value)
    TextView orderApplyValue;//已付金额
    @Bind(R.id.order_cancel_back_value)
    TextView orderBack;//退改费用
    @Bind(R.id.order_cancel_cancel_layout)
    View orderCancelLayout;//可退金额layout
    @Bind(R.id.order_cancel_cancel_value)
    TextView orderCancel;//可退金额

    private OrderBean orderBean;
    private DialogUtil mDialogUtil;
    private String paystyle = "";
    private int mBusinessType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_order_cancel);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            source = bundle.getString("source");
            paystyle = bundle.getString("paystyle");
            orderBean = (OrderBean) bundle.getSerializable(KEY_ORDER);
            mBusinessType = bundle.getInt(KEY_BUSINESS_TYPE, -1);
        }
        initDefaultTitleBar();
        fgTitle.setText("申请取消行程");
        mDialogUtil = DialogUtil.getInstance(this);
        if(orderBean.orderType==3){
            orderType.setText(getString(Constants.TitleMap.get(orderBean.orderGoodsType)));
        }else{
            orderType.setText(getString(Constants.TitleMap.get(orderBean.orderType)));
        }
        orderNoValue.setText(orderBean.orderNo);
        orderApplyValue.setText(orderBean.orderPriceInfo.actualPay+"元");
        orderBack.setText(orderBean.orderPriceInfo.cancelFee + "元");
        if(orderBean.cancelable){
            findViewById(R.id.order_cancel_btn).setVisibility(View.VISIBLE);
//            getView().findViewById(R.id.order_cancel_btn).setBackgroundResource(Constants.BtnBg.get(orderBean.orderType));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBackOrderFg();
    }

    private void goBackOrderFg() {
//        Bundle bundle = new Bundle();
//        bundle.putInt(KEY_BUSINESS_TYPE, orderBean.orderType);
//        bundle.putString(FgOrder.KEY_ORDER_ID, orderBean.orderNo);
//        bringToFront(FgOrder.class, bundle);

        OrderDetailActivity.Params params = new OrderDetailActivity.Params();
        params.orderType = orderBean.orderType;
        params.orderId = orderBean.orderNo;
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }

    @OnClick({R.id.order_cancel_btn})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head_btn_right:
                mDialogUtil.showCallDialog();
                break;
            case R.id.head_btn_left:
                goBackOrderFg();
                break;
            case R.id.order_cancel_btn:
                if(orderBean==null)return;
                cancelOrder();
                break;
            default:
                break;
        }
    }

    private void cancelOrder() {
        double cancelPrice = orderBean.orderPriceInfo.refundablePrice;
        if(cancelPrice<0)cancelPrice=0;
        RequestOrderCancel request = new RequestOrderCancel(this,orderBean.orderNo,cancelPrice,orderBean.memo);
        requestData(request);
        uMengStatisticForCancelOrder();
    }

    private void uMengStatisticForCancelOrder(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("source", source);
        map.put("carstyle", orderBean.carType + "");
        map.put("paystyle", paystyle);
        map.put("paysource", source);
        map.put("clicksource", source);
//        map.put("guestcount", orderBean.adult + orderBean.child + "");
//        map.put("payableamount", orderBean.orderPriceInfo.shouldPay + "");
//        map.put("actualamount", orderBean.orderPriceInfo.actualPay + "");

        String type = "";
        switch (mBusinessType) {
            case Constants.BUSINESS_TYPE_PICK:
                type = "cancelorder_pickup";
                break;
            case Constants.BUSINESS_TYPE_SEND:
                type = "cancelorder_dropoff";
                break;
            case Constants.BUSINESS_TYPE_DAILY:
                type = "cancelorder_oneday";
                map.put("begincity", orderBean.serviceCityName);
//                map.put("luggagecount", orderBean.luggageNum);
//                map.put("drivedays", orderBean.totalDays + "");
//                if(isForOther) {
//                    map_value.put("forother", "是");
//                }else{
//                    map_value.put("forother", "否");
//                }
                break;
            case Constants.BUSINESS_TYPE_RENT:
                type = "cancelorder_oneway";
                break;
            case Constants.BUSINESS_TYPE_COMMEND:
                type = "cancelorder_route";
                break;
        }
        MobclickAgent.onEventValue(this, type, map, (int)orderBean.orderPriceInfo.actualPay);

    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        DialogUtil dialogUtil = DialogUtil.getInstance(this);
        dialogUtil.showCustomDialog("取消订单成功", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                bringToFront(FgTravel.class, new Bundle());
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE, orderBean.orderNo));
                EventBus.getDefault().post(new EventAction(EventType.REFRESH_CHAT_LIST));
                finish();
            }
        });
        notifyOrderList(FgTravel.TYPE_ORDER_CANCEL,true,false,true);
    }

    private void notifyOrderList(int jumpType,boolean refreshRunning,boolean refreshFinish,boolean refreshCancel){
        Intent intent = new Intent();
        intent.setAction(FgTravel.FILTER_FLUSH);
        intent.putExtra(FgTravel.JUMP_TYPE,jumpType);
        intent.putExtra(FgTravel.REFRESH_RUNNING, refreshRunning);
        intent.putExtra(FgTravel.REFRESH_FINISH,refreshFinish);
        intent.putExtra(FgTravel.REFRESH_CANCEL, refreshCancel);
        sendBroadcast(intent);
    }
}
