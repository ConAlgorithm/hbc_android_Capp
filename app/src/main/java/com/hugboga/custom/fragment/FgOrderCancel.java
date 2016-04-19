package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.OverPriceAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.request.RequestOrderCancel;
import com.hugboga.custom.utils.UmengUtils;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单退单
 * Created by admin on 2015/7/31.
 */
@ContentView(R.layout.fg_order_cancel)
public class FgOrderCancel extends BaseFragment {

    public static final String KEY_ORDER = "KEY_ORDER";

    @ViewInject(R.id.order_cancel_type_value)
    private TextView orderType;
    @ViewInject(R.id.order_cancel_no_value)
    private TextView orderNoValue;
    @ViewInject(R.id.order_cancel_apply_value)
    private TextView orderApplyValue;//已付金额
    @ViewInject(R.id.order_cancel_back_value)
    private TextView orderBack;//退改费用
    @ViewInject(R.id.order_cancel_cancel_layout)
    private View orderCancelLayout;//可退金额layout
    @ViewInject(R.id.order_cancel_cancel_value)
    private TextView orderCancel;//可退金额

    private OrderBean orderBean;
    private DialogUtil mDialogUtil;
    private String paystyle = "";


    @Override
    protected void initHeader() {
        fgTitle.setText("申请取消行程");
    }

    protected void initView(){
        if(getArguments()!=null){
            source = getArguments().getString("source");
            paystyle = getArguments().getString("paystyle");
        }
        mDialogUtil = DialogUtil.getInstance(getActivity());
        orderBean = (OrderBean) getArguments().getSerializable(KEY_ORDER);
        OverPriceAdapter adapter = new OverPriceAdapter(getActivity());
        if(orderBean.orderType==3){
            orderType.setText(getString(Constants.TitleMap.get(orderBean.orderGoodsType)));
        }else{
            orderType.setText(getString(Constants.TitleMap.get(orderBean.orderType)));
        }
        orderNoValue.setText(orderBean.orderNo);
        orderApplyValue.setText(orderBean.orderPriceInfo.actualPay+"元");
        orderBack.setText(orderBean.orderPriceInfo.cancelFee + "元");
        if(orderBean.cancelable){
            getView().findViewById(R.id.order_cancel_btn).setVisibility(View.VISIBLE);
//            getView().findViewById(R.id.order_cancel_btn).setBackgroundResource(Constants.BtnBg.get(orderBean.orderType));
        }else{
            getView().findViewById(R.id.order_cancel_btn).setVisibility(View.INVISIBLE);
        }
        if (Double.isNaN(orderBean.orderPriceInfo.refundablePrice)) {
            orderCancelLayout.setVisibility(View.GONE);
        } else {
            orderCancelLayout.setVisibility(View.VISIBLE);
            orderCancel.setText(orderBean.orderPriceInfo.refundablePrice + "元");
        }
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }


    @Override
    protected void inflateContent() {
    }

    @Override
    public boolean onBackPressed() {
        finish();
        goBackOrderFg();
        return true;
    }

    private void goBackOrderFg() {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_BUSINESS_TYPE, orderBean.orderType);
        bundle.putString(FgOrder.KEY_ORDER_ID, orderBean.orderNo);
        bringToFront(FgOrder.class, bundle);
    }

    @Event({R.id.order_cancel_btn})
    private void onClickView(View view) {
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
        RequestOrderCancel request = new RequestOrderCancel(getActivity(),orderBean.orderNo,cancelPrice,orderBean.memo);
        requestData(request);
        uMengClickEvnet();
    }

    private void uMengClickEvnet(){
        Map<String, String> map_value = new HashMap<String, String>();
        map_value.put("source" , source);
        map_value.put("carstyle",orderBean.carType+"");
        map_value.put("paystyle",paystyle);
        map_value.put("paysource",source);
        map_value.put("clicksource",source);
        map_value.put("guestcount",orderBean.adult + orderBean.child + "");
        map_value.put("payableamount",orderBean.orderPriceInfo.shouldPay+"");
        map_value.put("actualamount", orderBean.orderPriceInfo.actualPay + "");

        if(umeng_key.equalsIgnoreCase("pay_oneday") || umeng_key.equalsIgnoreCase("launch_paysucceed_oneday")) {
            map_value.put("begincity", orderBean.startAddress);
//            if(isForOther) {
//                map_value.put("forother", "是");
//            }else{
//                map_value.put("forother", "否");
//            }
        }
        UmengUtils.mobClickEvent(getActivity(), umeng_key, map_value);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        DialogUtil dialogUtil = DialogUtil.getInstance(getActivity());
        dialogUtil.showCustomDialog("取消订单成功", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bringToFront(FgTravel.class, new Bundle());
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
        getActivity().sendBroadcast(intent);
    }
}
