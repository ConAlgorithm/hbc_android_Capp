package com.hugboga.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.EvaluateActivity;
import com.hugboga.custom.activity.GuideDetailActivity;
import com.hugboga.custom.activity.InsureActivity;
import com.hugboga.custom.activity.NIMChatActivity;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.adapter.viewholder.NewOrderVH;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.ImChatInfo;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.data.request.RequestImChatId;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.image.ImageOptions;

/**
 * 聊天历史订单
 * Created by ZHZEPHI on 2015/11/7 11:47.
 */
public class NewOrderAdapter extends ZBaseAdapter<OrderBean, NewOrderVH> {

    private final ImageOptions options;
    DialogUtil dialog;
    private Context context;

    public NewOrderAdapter(Context _context) {
        super(_context);
        this.context = _context;
        dialog = DialogUtil.getInstance((Activity) context);
        options = new ImageOptions.Builder()
                .setFailureDrawableId(R.mipmap.chat_head)
                .setLoadingDrawableId(R.mipmap.chat_head)
                .setCircular(true)
                .build();
    }

    @Override
    protected int initResource() {
        return R.layout.order_list_item;
    }

    @Override
    protected ZBaseViewHolder getVH(View view) {
        return new NewOrderVH(view);
    }

    @Override
    protected void getView(int position, NewOrderVH vh) {
        OrderBean orderBean = datas.get(position);
        //订单状态
        if (orderBean.orderType == Constants.BUSINESS_TYPE_COMMEND || orderBean.orderType == Constants.BUSINESS_TYPE_RECOMMEND) {//线路包车
            vh.citysTV.setVisibility(View.VISIBLE);
            vh.mTypeStr.setVisibility(View.GONE);
            vh.verticalLine.setVisibility(View.GONE);
            vh.mCarType.setVisibility(View.GONE);

            if (orderBean.carPool) {//是否拼车
                Drawable drawable = context.getResources().getDrawable(R.mipmap.carpooling);
                drawable.setBounds(0, 0, UIUtils.dip2px(36), UIUtils.dip2px(18));
                SpannableString spannable = new SpannableString("[icon]" + orderBean.lineSubject);
                ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                spannable.setSpan(span, 0, "[icon]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                vh.citysTV.setText(spannable);
            } else {
                vh.citysTV.setText(orderBean.lineSubject);
            }

            vh.timeTV.setText(orderBean.serviceTime + "至" + orderBean.serviceEndTime + " " + orderBean.totalDays + "天");
            vh.timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

            if (TextUtils.isEmpty(orderBean.carDesc)) {
                vh.startAddressLayout.setVisibility(View.GONE);
            } else {
                vh.startAddressLayout.setVisibility(View.VISIBLE);
                vh.startAddressIV.setBackgroundResource(R.mipmap.order_car);
                vh.startAddressTV.setText(orderBean.carDesc);
            }

            if (TextUtils.isEmpty(orderBean.serviceCityName)) {
                vh.endAddressLayout.setVisibility(View.GONE);
            } else {
                vh.endAddressLayout.setVisibility(View.VISIBLE);
                vh.endAddressIV.setBackgroundResource(R.mipmap.trip);
                String dailyPlace = orderBean.serviceCityName;
                if (!TextUtils.isEmpty(orderBean.serviceEndCityName)) {
                    dailyPlace += " - " + orderBean.serviceEndCityName;
                }
                vh.endAddressTV.setText(dailyPlace);
            }
        } else {
            vh.citysTV.setVisibility(View.GONE);
            vh.mTypeStr.setVisibility(View.VISIBLE);
            vh.verticalLine.setVisibility(View.VISIBLE);
            vh.mCarType.setVisibility(View.VISIBLE);
            vh.mCarType.setText(orderBean.carDesc);//车辆类型
            switch (orderBean.orderType) {
                case Constants.BUSINESS_TYPE_PICK://接机
                    vh.mTypeStr.setText("中文接机");

                    try {
                        vh.timeTV.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    vh.timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.startAddress)) {
                        vh.startAddressLayout.setVisibility(View.GONE);
                    } else {
                        vh.startAddressLayout.setVisibility(View.VISIBLE);
                        vh.startAddressIV.setBackgroundResource(R.mipmap.order_place);
                        vh.startAddressTV.setText(orderBean.startAddress + " " + orderBean.startAddressDetail);
                    }

                    if (TextUtils.isEmpty(orderBean.destAddress)) {
                        vh.endAddressLayout.setVisibility(View.GONE);
                    } else {
                        vh.endAddressLayout.setVisibility(View.VISIBLE);
                        vh.endAddressIV.setBackgroundResource(R.mipmap.order_flag);
                        vh.endAddressTV.setText(orderBean.destAddress + " " + orderBean.destAddressDetail);
                    }
                    break;
                case Constants.BUSINESS_TYPE_SEND://送机
                    vh.mTypeStr.setText("中文送机");

                    try {
                        vh.timeTV.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    vh.timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.startAddress)) {
                        vh.startAddressLayout.setVisibility(View.GONE);
                    } else {
                        vh.startAddressLayout.setVisibility(View.VISIBLE);
                        vh.startAddressIV.setBackgroundResource(R.mipmap.order_place);
                        vh.startAddressTV.setText(orderBean.startAddress + " " + orderBean.startAddressDetail);
                    }

                    if (TextUtils.isEmpty(orderBean.destAddress)) {
                        vh.endAddressLayout.setVisibility(View.GONE);
                    } else {
                        vh.endAddressLayout.setVisibility(View.VISIBLE);
                        vh.endAddressIV.setBackgroundResource(R.mipmap.order_flag);
                        vh.endAddressTV.setText(orderBean.destAddress + " " + orderBean.destAddressDetail);
                    }
                    break;
                case Constants.BUSINESS_TYPE_DAILY://日租 包车游
                    vh.mTypeStr.setText("定制包车行程");
                    if (orderBean.isHalfDaily == 1) {//半日包
                        vh.timeTV.setText(orderBean.serviceTime);
                        vh.timeLocalTV.setText("(半日)");
                    } else {
                        vh.timeTV.setText(orderBean.serviceTime + " 至 " + orderBean.serviceEndTime);
                        vh.timeLocalTV.setText("");
                    }

                    if (TextUtils.isEmpty(orderBean.serviceCityName)) {
                        vh.startAddressLayout.setVisibility(View.GONE);
                    } else {
                        vh.startAddressLayout.setVisibility(View.VISIBLE);
                        vh.startAddressIV.setBackgroundResource(R.mipmap.trip);
                        String dailyPlace = orderBean.serviceCityName;
                        if (!TextUtils.isEmpty(orderBean.serviceEndCityName)) {
                            dailyPlace += " - " + orderBean.serviceEndCityName;
                        }
                        vh.startAddressTV.setText(dailyPlace);
                    }

                    vh.endAddressLayout.setVisibility(View.GONE);
                    break;
                case Constants.BUSINESS_TYPE_RENT://次租 单次接送
                    vh.mTypeStr.setText("单次接送");

                    try {
                        vh.timeTV.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    vh.timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.startAddress)) {
                        vh.startAddressLayout.setVisibility(View.GONE);
                    } else {
                        vh.startAddressLayout.setVisibility(View.VISIBLE);
                        vh.startAddressIV.setBackgroundResource(R.mipmap.order_place);
                        vh.startAddressTV.setText(orderBean.startAddress + " " + orderBean.startAddressDetail);
                    }

                    if (TextUtils.isEmpty(orderBean.destAddress)) {
                        vh.endAddressLayout.setVisibility(View.GONE);
                    } else {
                        vh.endAddressLayout.setVisibility(View.VISIBLE);
                        vh.endAddressIV.setBackgroundResource(R.mipmap.order_flag);
                        vh.endAddressTV.setText(orderBean.destAddress + " " + orderBean.destAddressDetail);
                    }
                    break;
            }
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
            case INITSTATE://等待支付 初始状态
                vh.mStatus.setText("等待支付");

                vh.mStatusLayout.setVisibility(View.VISIBLE);
                vh.lineView.setVisibility(View.VISIBLE);
                vh.br_layout.setVisibility(View.GONE);//添加投保人
                vh.mPrice.setVisibility(View.VISIBLE);//支付TV
                if(orderBean.orderPriceInfo != null){
                    vh.mPrice.setText("支付金额：" + orderBean.orderPriceInfo.actualPay + "元");
                }
                vh.mHeadLayout.setVisibility(View.GONE);//司导信息
                vh.mBtnPay.setVisibility(View.VISIBLE);//立即支付btn
                vh.mBtnPay.setTag(orderBean);
                vh.mBtnPay.setOnClickListener(new TravelOnClickListener(orderBean));
                vh.mBtnChat.setVisibility(View.GONE);
                vh.mAssessment.setVisibility(View.GONE);//评价司导
                break;
            case PAYSUCCESS://预订成功
                vh.mStatus.setText("预订成功");
                if (orderBean.insuranceEnable) {
                    vh.mStatusLayout.setVisibility(View.VISIBLE);
                    vh.lineView.setVisibility(View.VISIBLE);
                    vh.mPrice.setVisibility(View.GONE);
                    vh.mBtnPay.setVisibility(View.GONE);
                    vh.mHeadLayout.setVisibility(View.GONE);
                    vh.mAssessment.setVisibility(View.GONE);
                    vh.br_layout.setVisibility(View.VISIBLE);
                    vh.travel_item_btn_br.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("orderBean",orderBean);
                            bundle.putString("from","orderList");
                            Intent intent = new Intent(context, InsureActivity.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    });
                    vh.travel_item_btn_br_tips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), WebInfoActivity.class);
                            intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_INSURANCE);
                            v.getContext().startActivity(intent);
                        }
                    });
                } else {
                    vh.mStatusLayout.setVisibility(View.GONE);
                    vh.lineView.setVisibility(View.INVISIBLE);
                    vh.br_layout.setVisibility(View.GONE);
                }
                break;
            case AGREE://司导已接单
            case ARRIVED://司导已到达
            case SERVICING://服务中
                vh.mStatus.setText(orderBean.orderStatus.name);
                vh.mPrice.setVisibility(View.GONE);
                vh.mBtnPay.setVisibility(View.GONE);
                vh.mAssessment.setVisibility(View.GONE);

                if (orderBean.orderGuideInfo != null) {
                    vh.mHeadLayout.setVisibility(View.VISIBLE);
                    vh.mStatusLayout.setVisibility(View.VISIBLE);
                    vh.lineView.setVisibility(View.VISIBLE);
                    vh.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        vh.mHeadImg.setImageResource(R.mipmap.journey_head_portrait);
                    } else {
                        Tools.showImage(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.journey_head_portrait);
                    }
                    vh.mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    vh.mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));

                    if(orderBean.isIm){
                        vh.mBtnChat.setVisibility(View.VISIBLE);
                        vh.mBtnChat.setOnClickListener(new TravelOnClickListener(orderBean));
                        showMessageNum(vh.mBtnChatNum, orderBean.imcount);//显示未读小红点个数
                    }else{
                        vh.mBtnChat.setVisibility(View.GONE);
                    }

                    if (orderBean.insuranceEnable) {
                        vh.br_layout.setVisibility(View.VISIBLE);
                        vh.travel_item_btn_br.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("orderBean",orderBean);
                                bundle.putString("from","orderList");
                                Intent intent = new Intent(context, InsureActivity.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        });
                        vh.travel_item_btn_br_tips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), WebInfoActivity.class);
                                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_INSURANCE);
                                v.getContext().startActivity(intent);
                            }
                        });
                    } else {
                        vh.br_layout.setVisibility(View.GONE);
                    }
                } else {
                    vh.mStatusLayout.setVisibility(View.GONE);
                    vh.lineView.setVisibility(View.INVISIBLE);
                    vh.mHeadLayout.setVisibility(View.GONE);
                }
                break;
            case NOT_EVALUATED://未评价
            case COMPLETE://已完成
                vh.mStatusLayout.setVisibility(View.VISIBLE);
                vh.lineView.setVisibility(View.VISIBLE);
                vh.mPrice.setVisibility(View.GONE);
                vh.mBtnPay.setVisibility(View.GONE);
                vh.br_layout.setVisibility(View.GONE);

                if (orderBean.orderGuideInfo != null) {
                    vh.mHeadLayout.setVisibility(View.VISIBLE);
                    vh.mStatusLayout.setVisibility(View.VISIBLE);
                    vh.lineView.setVisibility(View.VISIBLE);

                    vh.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        vh.mHeadImg.setImageResource(R.mipmap.journey_head_portrait);
                    } else {
                        Tools.showImage(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.journey_head_portrait);
                    }
                    vh.mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    vh.mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));

                    if(orderBean.isIm){
                        vh.mBtnChat.setVisibility(View.VISIBLE);
                        vh.mBtnChat.setOnClickListener(new TravelOnClickListener(orderBean));
                        showMessageNum(vh.mBtnChatNum, orderBean.imcount);//显示未读小红点个数
                    }else{
                        vh.mBtnChat.setVisibility(View.GONE);
                    }

                    if (!orderBean.isEvaluated()) {//服务完成未评价
                        vh.mAssessment.setVisibility(View.VISIBLE);
                        vh.mAssessment.setOnClickListener(new TravelOnClickListener(orderBean));
                        vh.mStatus.setText("未评价");
                    } else {
                        vh.mAssessment.setVisibility(View.GONE);
                        vh.mStatus.setText("服务完成");
                    }
                } else {
                    vh.mStatusLayout.setVisibility(View.GONE);
                    vh.lineView.setVisibility(View.INVISIBLE);
                    vh.mHeadLayout.setVisibility(View.GONE);
                }
                break;
            case CANCELLED:
            case REFUNDED://已取消、已退款
                vh.mStatus.setText("已取消");
                vh.mStatusLayout.setVisibility(View.GONE);
                vh.lineView.setVisibility(View.INVISIBLE);
                if (orderBean.orderGuideInfo != null) {
                    vh.mHeadLayout.setVisibility(View.VISIBLE);
                    vh.mStatusLayout.setVisibility(View.VISIBLE);
                    vh.lineView.setVisibility(View.VISIBLE);

                    vh.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        vh.mHeadImg.setImageResource(R.mipmap.journey_head_portrait);
                    } else {
                        Tools.showImage(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.journey_head_portrait);
                    }
                    vh.mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    vh.mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));

                    if(orderBean.isIm){
                        vh.mBtnChat.setVisibility(View.VISIBLE);
                        vh.mBtnChat.setOnClickListener(new TravelOnClickListener(orderBean));
                        showMessageNum(vh.mBtnChatNum, orderBean.imcount);//显示未读小红点个数
                    }else{
                        vh.mBtnChat.setVisibility(View.GONE);
                    }
                } else {
                    vh.mStatusLayout.setVisibility(View.GONE);
                    vh.lineView.setVisibility(View.INVISIBLE);
                    vh.mHeadLayout.setVisibility(View.GONE);
                }
                break;
            case COMPLAINT://客诉处理中
                vh.mStatus.setText("客诉处理中");
                vh.mStatusLayout.setVisibility(View.GONE);
                vh.lineView.setVisibility(View.INVISIBLE);
                if (orderBean.orderGuideInfo != null) {
                    vh.mHeadLayout.setVisibility(View.VISIBLE);
                    vh.mStatusLayout.setVisibility(View.VISIBLE);
                    vh.lineView.setVisibility(View.VISIBLE);

                    vh.mHeadTitle.setText(orderBean.orderGuideInfo.guideName);
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        vh.mHeadImg.setImageResource(R.mipmap.journey_head_portrait);
                    } else {
                        Tools.showImage(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.journey_head_portrait);
                    }
                    vh.mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    vh.mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));

                    if(orderBean.isIm){
                        vh.mBtnChat.setVisibility(View.VISIBLE);
                        vh.mBtnChat.setOnClickListener(new TravelOnClickListener(orderBean));
                        showMessageNum(vh.mBtnChatNum, orderBean.imcount);//显示未读小红点个数
                    }else{
                        vh.mBtnChat.setVisibility(View.GONE);
                    }
                } else {
                    vh.mStatusLayout.setVisibility(View.GONE);
                    vh.lineView.setVisibility(View.INVISIBLE);
                    vh.mHeadLayout.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置聊一聊未读个数小红点
     * @param chatNumTextView
     */
    private void showMessageNum(final TextView chatNumTextView, Integer imcount){
//        if(imcount>0){
//            chatNumTextView.setVisibility(View.VISIBLE);
//            chatNumTextView.setText(String.valueOf(imcount));
//        }else{
//            chatNumTextView.setVisibility(View.GONE);
//        }
        chatNumTextView.setVisibility(View.GONE);
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
            Intent intent = null;
            switch (v.getId()) {
                case R.id.travel_item_btn_assessment:
                    intent = new Intent(v.getContext(), EvaluateActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, mOrderBean);
                    v.getContext().startActivity(intent);
                    break;
                case R.id.travel_item_btn_pay:
                    MLog.e("立即支付 " + mOrderBean.orderNo);
                    //立即支付，进入订单详情
                    OrderDetailActivity.Params params = new OrderDetailActivity.Params();
                    params.orderType = mOrderBean.orderType;
                    params.orderId = mOrderBean.orderNo;
                    params.source = mOrderBean.orderType == 5 ? mOrderBean.serviceCityName : "首页";
                    intent = new Intent(v.getContext(), OrderDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra(Constants.PARAMS_SOURCE,params.source);
                    v.getContext().startActivity(intent);
                    break;
                case R.id.travel_item_btn_chat:
                    MLog.e("进入聊天" + mOrderBean.orderNo);
                    if(mOrderBean.orderGuideInfo!=null&&mOrderBean.orderGuideInfo.guideID!=null){
                        requestImChatId(mOrderBean.orderGuideInfo.guideID,mOrderBean.orderGuideInfo.guideAvatar,mOrderBean.orderGuideInfo.guideName);
                    }
                    break;
                case R.id.travel_item_head_img:
                case R.id.travel_item_head_title:
                    if(mOrderBean.orderGuideInfo == null || mOrderBean.orderGuideInfo.guideID == null) {
                        return;
                    }
                    GuideDetailActivity.Params guideDetailParams = new GuideDetailActivity.Params();
                    guideDetailParams.guideId = mOrderBean.orderGuideInfo.guideID;
                    guideDetailParams.guideCarId = mOrderBean.orderGuideInfo.guideCarId;
                    guideDetailParams.guideAgencyDriverId = mOrderBean.guideAgencyDriverId;
                    guideDetailParams.isSelectedService = mOrderBean.guideAgencyType == 3;

                    intent = new Intent(v.getContext(), GuideDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, guideDetailParams);
                    intent.putExtra(Constants.PARAMS_SOURCE, "订单列表");
                    v.getContext().startActivity(intent);
                    break;
            }
        }
    }

    private void requestImChatId(final String chatId,final String targetAvatar,final String targetName){
        RequestImChatId requestImChatId = new RequestImChatId(context, UserEntity.getUser().getUserId(context),"2",chatId,"1");
        HttpRequestUtils.request(context,requestImChatId,new HttpRequestListener(){
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                Object object = request.getData();
                if(object instanceof ImChatInfo){
                    ImChatInfo imChatInfo = (ImChatInfo)object;
                    gotoChatView(chatId,targetAvatar,targetName,imChatInfo.neTargetId,imChatInfo.inBlack);
                }
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {
            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            }
        });
    }

    private void gotoChatView( final String chatId,String targetAvatar,String targetName,String imChatId,int inblack) {
        if(!IMUtil.getInstance().isLogined()){
            return;
        }
        String titleJson = getChatInfo(chatId,  targetAvatar, targetName, "1",imChatId,inblack);
        NIMChatActivity.start(context,imChatId,null,titleJson);
        //RongIM.getInstance().startPrivateChat(context, imChatId, titleJson);
    }
    private String getChatInfo(String userId, String userAvatar, String title, String targetType,String imChatId,int inblack) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.isChat = true;
        chatInfo.userId = userId;
        chatInfo.imUserId =imChatId;
        chatInfo.userAvatar = userAvatar;
        chatInfo.title = title;
        chatInfo.targetType = targetType;
        chatInfo.inBlack = inblack;
        return new ParserChatInfo().toJsonString(chatInfo);
    }

}
