package com.hugboga.custom.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.NewOrderVH;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.fragment.FgActivity;
import com.hugboga.custom.fragment.FgAssessment;
import com.hugboga.custom.fragment.FgInsure;
import com.hugboga.custom.fragment.FgOrder;
import com.hugboga.custom.fragment.FgWebInfo;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.ParseException;

import io.rong.imkit.RongIM;

/**
 * 聊天历史订单
 * Created by ZHZEPHI on 2015/11/7 11:47.
 */
public class NewOrderAdapter extends ZBaseAdapter<OrderBean, NewOrderVH> {

    private final ImageOptions options;
    DialogUtil dialog;
    BaseFragment fragment;

    public NewOrderAdapter(BaseFragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
        dialog = DialogUtil.getInstance(fragment.getActivity());
        options = new ImageOptions.Builder()
                .setFailureDrawableId(R.mipmap.chat_head)
                .setLoadingDrawableId(R.mipmap.chat_head)
                .setCircular(true)
                .build();
    }

    @Override
    protected int initResource() {
        return R.layout.order_history_item;
    }

    @Override
    protected ZBaseViewHolder getVH(View view) {
        return new NewOrderVH(view);
    }

    @Override
    protected void getView(int position, NewOrderVH vh) {
        OrderBean orderBean = datas.get(position);
        //车辆类型
        vh.mCarType.setText(orderBean.carDesc);
        //当地城市时间
        vh.mServiceTimeLocl.setVisibility(View.VISIBLE);
        vh.mServiceTimeLocl.setText("(" + orderBean.serviceCityName + "时间)");
        //订单状态
        vh.mStatus.setText(orderBean.orderStatus.name);
        vh.mCommendStartAddress.setVisibility(View.GONE);
        switch (orderBean.orderType) {
            case Constants.BUSINESS_TYPE_PICK:
                //接机
                vh.mLineView.setBackgroundResource(R.drawable.jour_blue_ttt);
                vh.mTypeStr.setText(R.string.title_pick);
                vh.mDays.setVisibility(View.GONE);
                vh.mFrom.setVisibility(View.VISIBLE);
                vh.mFrom.setText(orderBean.startAddress);
                vh.mTo.setVisibility(View.VISIBLE);
                vh.mTo.setText(orderBean.destAddress);
                vh.mCitys.setVisibility(View.GONE);
                try {
                    vh.mServiceTime.setVisibility(View.VISIBLE);
                    vh.mServiceTime.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.BUSINESS_TYPE_SEND:
                //送机
                vh.mLineView.setBackgroundResource(R.drawable.jour_green_ttt);
                vh.mTypeStr.setText(R.string.title_send);
                vh.mDays.setVisibility(View.GONE);
                vh.mFrom.setVisibility(View.VISIBLE);
                vh.mFrom.setText(orderBean.startAddress);
                vh.mTo.setVisibility(View.VISIBLE);
                vh.mTo.setText(orderBean.destAddress);
                vh.mCitys.setVisibility(View.GONE);
                try {
                    vh.mServiceTime.setVisibility(View.VISIBLE);
                    vh.mServiceTime.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.BUSINESS_TYPE_DAILY:
                //日租
                vh.mLineView.setBackgroundResource(R.drawable.jour_yellow_ttt);
                vh.mTypeStr.setText(R.string.title_daily);

                vh.mServiceTime.setVisibility(View.VISIBLE);
                if (orderBean.isHalfDaily == 1) {//半日包
                    vh.mDays.setVisibility(View.GONE);
                    vh.mServiceTime.setText(orderBean.serviceTime + "(半日包)");
                } else {
                    vh.mDays.setVisibility(View.VISIBLE);
                    vh.mDays.setText((orderBean.totalDays) + "天");
                    vh.mServiceTime.setText(orderBean.serviceTime + " 至 " + orderBean.serviceEndTime);
                }
                if (orderBean.orderGoodsType == Constants.BUSINESS_TYPE_DAILY) {
                    vh.mTypeStr.setText("包车游");
                } else {
                    vh.mTypeStr.setText("包车游");
                }
                vh.mFrom.setVisibility(View.GONE);
                vh.mTo.setVisibility(View.GONE);
                vh.mCitys.setVisibility(View.VISIBLE);
                String dailyPlace = orderBean.serviceCityName;
                if (!TextUtils.isEmpty(orderBean.serviceEndCityName)) {
                    dailyPlace += " - " + orderBean.serviceEndCityName;
                }
                vh.mCitys.setText(dailyPlace);
                break;
            case Constants.BUSINESS_TYPE_RENT:
                //次租
                vh.mLineView.setBackgroundResource(R.drawable.jour_red_ttt);
                vh.mTypeStr.setText(R.string.title_rent);
                vh.mDays.setVisibility(View.GONE);
                vh.mFrom.setVisibility(View.VISIBLE);
                vh.mFrom.setText(orderBean.startAddress);
                vh.mTo.setVisibility(View.VISIBLE);
                vh.mTo.setText(orderBean.destAddress);
                vh.mCitys.setVisibility(View.GONE);
                try {
                    vh.mServiceTime.setVisibility(View.VISIBLE);
                    vh.mServiceTime.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.BUSINESS_TYPE_COMMEND:
                //线路包车
                vh.mLineView.setBackgroundResource(R.drawable.jour_purple_ttt);
                vh.mTypeStr.setText(R.string.title_commend);
                vh.mDays.setVisibility(View.GONE);
                vh.mServiceTime.setVisibility(View.VISIBLE);
                vh.mServiceTime.setText(splitDateStr(orderBean.serviceTime) + " 至 " + splitDateStr(splitDateStr(orderBean.serviceEndTime)));
                vh.mCommendStartAddress.setText("(出发地：" + orderBean.serviceCityName + ")");
                vh.mCommendStartAddress.setVisibility(View.VISIBLE);
                vh.mFrom.setVisibility(View.GONE);
                vh.mTo.setVisibility(View.GONE);
                vh.mServiceTimeLocl.setVisibility(View.GONE);
                vh.mCitys.setVisibility(View.VISIBLE);
                vh.mCitys.setText(orderBean.lineSubject);
                break;
        }
        vh.mBtnChat.setVisibility(View.GONE);
        setStatusView(vh, orderBean);
    }

    /**
     * 根据状态重置操作栏
     *
     * @param vh
     * @param orderBean
     */
    private void setStatusView(NewOrderVH vh, final OrderBean orderBean) {
        vh.mAssessment.setOnClickListener(null);
        switch (orderBean.orderStatus) {
            case INITSTATE:
                //等待支付 初始状态
                vh.mStatusLayout.setVisibility(View.VISIBLE);
                vh.mPrice.setVisibility(View.VISIBLE);
                vh.br_layout.setVisibility(View.GONE);
                if(orderBean.orderPriceInfo!=null){
                    vh.mPrice.setText("应付金额：" + orderBean.orderPriceInfo.shouldPay + "元");
                }
                vh.mHeadLayout.setVisibility(View.GONE);
                vh.mBtnPay.setVisibility(View.VISIBLE);
                vh.mBtnPay.setTag(orderBean);
                vh.mBtnPay.setOnClickListener(new TravelOnClickListener(orderBean));
                vh.mBtnChat.setVisibility(View.GONE);
//                vh.mBtnCall.setVisibility(View.GONE);
                vh.mAssessment.setVisibility(View.GONE);
                vh.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                break;
            case PAYSUCCESS:
                //预订成功
                vh.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                vh.mStatusLayout.setVisibility(View.GONE);

                if(orderBean.insuranceEnable) {
                    vh.br_layout.setVisibility(View.VISIBLE);
                    vh.travel_item_btn_br.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FgInsure fgAddInsure = new FgInsure();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("orderBean",orderBean);
                            bundle.putString("from","orderList");
                            fgAddInsure.setArguments(bundle);
                            fragment.startFragment(fgAddInsure);
                        }
                    });
                    vh.travel_item_btn_br_tips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundleUrlAll = new Bundle();
                            bundleUrlAll.putString(FgWebInfo.WEB_URL, UrlLibs.H5_INSURANCE);
                            fragment.startFragment(new FgActivity(), bundleUrlAll);
                        }
                    });
                }else{
                    vh.br_layout.setVisibility(View.GONE);
                }

                break;
            case AGREE:
                //导游已接单
                vh.mStatusLayout.setVisibility(View.VISIBLE);
                vh.mPrice.setVisibility(View.GONE);
                vh.mHeadLayout.setVisibility(View.VISIBLE);
                if (orderBean.orderGuideInfo != null) {
                    vh.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);

                    x.image().bind(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar);
                }
                vh.mBtnChat.setVisibility(View.VISIBLE);
                vh.mBtnPay.setVisibility(View.GONE);
                vh.mAssessment.setVisibility(View.GONE);
                vh.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                break;
            case ARRIVED:
                //导游已到达
                vh.mStatusLayout.setVisibility(View.VISIBLE);
                vh.mPrice.setVisibility(View.GONE);
                vh.mHeadLayout.setVisibility(View.VISIBLE);
                if(orderBean.orderGuideInfo!=null){
                    vh.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                    x.image().bind(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar);
                }
                vh.mBtnPay.setVisibility(View.GONE);
                vh.mBtnChat.setVisibility(View.VISIBLE);
                vh.mAssessment.setVisibility(View.GONE);
                vh.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                break;
            case SERVICING:
                //您已上车
                vh.mStatusLayout.setVisibility(View.VISIBLE);
                vh.mPrice.setVisibility(View.GONE);
                vh.mHeadLayout.setVisibility(View.VISIBLE);
                if (orderBean.orderGuideInfo != null) {
                    vh.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                    x.image().bind(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar);
                }
                vh.mBtnChat.setVisibility(View.VISIBLE);
                vh.mBtnPay.setVisibility(View.GONE);
                vh.mAssessment.setVisibility(View.GONE);
                vh.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                break;
            case NOT_EVALUATED:
                //是否可以评价
                vh.mAssessment.setVisibility(View.VISIBLE);
                MLog.e("onclick " + orderBean.orderNo + " orderType = " + orderBean.orderType);
                MLog.e("onclick=" + vh.mAssessment.hasOnClickListeners());
                vh.mAssessment.setOnClickListener(new TravelOnClickListener(orderBean));
                vh.mStatusLayout.setVisibility(View.VISIBLE);
                vh.mPrice.setVisibility(View.GONE);
                vh.mHeadLayout.setVisibility(View.VISIBLE);
                if(orderBean.orderGuideInfo!=null){
                    vh.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                    x.image().bind(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar);
                }
                vh.mBtnPay.setVisibility(View.GONE);
                vh.mBtnChat.setVisibility(View.VISIBLE);
                vh.mStatus.setTextColor(Color.parseColor("#F3AD5B"));
                break;
            case COMPLAINT:
                //客诉处理中
                vh.mStatusLayout.setVisibility(View.GONE);
                vh.mStatus.setTextColor(Color.parseColor("#B3B3B3"));
                break;
            case COMPLETE:
                //已完成
                vh.mAssessment.setVisibility(View.GONE);
                vh.mStatusLayout.setVisibility(View.VISIBLE);
                vh.mHeadLayout.setVisibility(View.VISIBLE);
                vh.mBtnChat.setVisibility(View.VISIBLE);
                vh.mStatus.setTextColor(Color.parseColor("#BDBDBD"));
                if(orderBean.orderGuideInfo!=null){
                    vh.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                    x.image().bind(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar);
                }
                break;
            case CANCELLED:
            case REFUNDED:
                //已取消
                vh.mStatusLayout.setVisibility(View.GONE);
                vh.mStatus.setTextColor(Color.parseColor("#B3B3B3"));
                break;
            default:
                break;
        }
        if(orderBean.canChat && (orderBean.imToken!=null && !orderBean.imToken.isEmpty()) && (orderBean.orderGuideInfo!=null && orderBean.orderGuideInfo.guideID!=null)){
            vh.mBtnChat.setVisibility(View.VISIBLE);
            vh.mBtnChat.setOnClickListener(new TravelOnClickListener(orderBean));
        }else{
            vh.mBtnChat.setVisibility(View.GONE);
        }

        //显示未读小红点个数
//        showMessageNum(vh.mBtnChatNum, orderBean.imcount);
    }

    /**
     * 设置聊一聊未读个数小红点
     * @param chatNumTextView
     */
    private void showMessageNum(final TextView chatNumTextView, Integer imcount){
        if(imcount>0){
            chatNumTextView.setVisibility(View.VISIBLE);
            chatNumTextView.setText(String.valueOf(imcount));
        }else{
            chatNumTextView.setVisibility(View.GONE);
        }
    }

    private String splitDateStr(String dateStr) {
        if (dateStr == null) return null;
        String[] dateArray = dateStr.split(" ");
        if (dateArray != null && dateArray.length > 1) return dateArray[0];
        return dateStr;
    }

    class TravelOnClickListener implements View.OnClickListener {

        private OrderBean mOrderBean = null;

        public TravelOnClickListener(OrderBean orderBean) {
            this.mOrderBean = orderBean;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.travel_item_btn_assessment:
                    MLog.e("评价车导2 " + mOrderBean.orderNo + " orderType = " + mOrderBean.orderType);
                    Bundle bundle = new Bundle();
                    bundle.putString(FgAssessment.GUIDE_ID, mOrderBean.orderGuideInfo.guideID);
                    bundle.putString(FgAssessment.ORDER_ID, mOrderBean.orderNo);
                    bundle.putInt(FgAssessment.ORDER_TYPE, mOrderBean.orderType);
                    bundle.putInt(BaseFragment.KEY_BUSINESS_TYPE, mOrderBean.orderType);
                    bundle.putString(FgAssessment.GUIDE_NAME, mOrderBean.orderGuideInfo.guideName);
                    fragment.startFragment(new FgAssessment(), bundle);
                    break;
                case R.id.travel_item_btn_pay:
                    MLog.e("立即支付 " + mOrderBean.orderNo);
                    //立即支付，进入订单详情
                    bundle = new Bundle();
                    bundle.putInt(FgOrder.KEY_BUSINESS_TYPE, mOrderBean.orderType);
                    bundle.putInt(FgOrder.KEY_GOODS_TYPE, mOrderBean.orderGoodsType);
                    bundle.putString(FgOrder.KEY_ORDER_ID, mOrderBean.orderNo);
                    bundle.putString("source", mOrderBean.orderType == 5 ? mOrderBean.serviceCityName : "首页");
                    fragment.startFragment(new FgOrder(), bundle);
                    break;
                case R.id.travel_item_btn_chat:
                    MLog.e("进入聊天" + mOrderBean.orderNo);
                    if(mOrderBean.orderGuideInfo!=null&&mOrderBean.orderGuideInfo.guideID!=null){
                        gotoChatView(mOrderBean.orderGuideInfo.guideID,mOrderBean.orderGuideInfo.guideAvatar,mOrderBean.orderGuideInfo.guideName);
                    }

                    break;
            }
        }
    }
    private void gotoChatView( final String chatId,String targetAvatar,String targetName) {
        String titleJson = getChatInfo(chatId,  targetAvatar, targetName, "3");
        RongIM.getInstance().startPrivateChat(fragment.getActivity(), "G"+chatId, titleJson);
    }
    private String getChatInfo(String userId, String userAvatar, String title, String targetType) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.isChat = true;
        chatInfo.userId = userId;
        chatInfo.userAvatar = userAvatar;
        chatInfo.title = title;
        chatInfo.targetType = targetType;
        return new ParserChatInfo().toJsonString(chatInfo);
    }

}
