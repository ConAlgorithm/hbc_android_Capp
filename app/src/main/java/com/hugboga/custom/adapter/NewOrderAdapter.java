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
import com.hugboga.custom.activity.EvaluateNewActivity;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.InsureActivity;
import com.hugboga.custom.activity.NIMChatActivity;
import com.hugboga.custom.activity.NewOrderActivity;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.adapter.viewholder.NewOrderVH;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ImChatInfo;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderGuideInfo;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestImChatId;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.image.ImageOptions;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
                .setFailureDrawableId(R.mipmap.icon_avatar_user)
                .setLoadingDrawableId(R.mipmap.icon_avatar_user)
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
            vh.citysTV.setVisibility(VISIBLE);
            vh.mTypeStr.setVisibility(GONE);
            vh.verticalLine.setVisibility(GONE);
            vh.mCarType.setVisibility(GONE);
            vh.startAddressIV1.setVisibility(GONE);
            vh.startAddressIV2.setVisibility(GONE);
            vh.itemTime.setBackgroundResource(R.mipmap.trip_icon_date);
            if (orderBean.carPool) {//是否拼车
                Drawable drawable = context.getResources().getDrawable(R.mipmap.carpooling);
                drawable.setBounds(0, 0, UIUtils.dip2px(36), UIUtils.dip2px(18));
                SpannableString spannable = new SpannableString("[icon]" + orderBean.lineSubject);
                ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                spannable.setSpan(span, 0, "[icon]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                vh.citysTV.setText(spannable);
                vh.chexing.setVisibility(GONE);
                vh.endAddressLayout.setVisibility(GONE);
                //startAddressLayout.setVisibility(View.GONE);

            } else {
                vh.citysTV.setText(orderBean.lineSubject);

                if (TextUtils.isEmpty(orderBean.carDesc)) {
                    vh.endAddressLayout.setVisibility(GONE);
                    vh.chexing.setVisibility(GONE);
                } else {
                    vh.chexing.setVisibility(VISIBLE);
                    vh.chexing.setBackgroundResource(R.mipmap.trip_icon_car);
                    vh.endAddressLayout.setVisibility(VISIBLE);
                    //startAddressIV.setBackgroundResource(R.mipmap.order_car);
                    //startAddressIV2.setBackgroundResource(R.mipmap.trip_icon_line);
                    vh.endAddressTV.setText(orderBean.carDesc);
                }
            }

            vh.timeTV.setText(orderBean.serviceTime + " 至 " + orderBean.serviceEndTime + " " + orderBean.totalDays + "天");
            vh.timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

            if (TextUtils.isEmpty(orderBean.serviceCityName)) {
                vh.startAddressLayout.setVisibility(GONE);
                vh.xianlu_iv.setVisibility(GONE);
            } else {
                vh.startAddressLayout.setVisibility(VISIBLE);
                //endAddressIV.setBackgroundResource(R.mipmap.trip)
                //startAddressIV2.setBackgroundResource(R.mipmap.trip_icon_line);
                String dailyPlace = orderBean.serviceCityName;
                if (!TextUtils.isEmpty(orderBean.serviceEndCityName)) {
                    dailyPlace += " - " + orderBean.serviceEndCityName;
                }
                vh.startAddressTV.setText(dailyPlace);
                vh.xianlu_iv.setVisibility(VISIBLE);
                vh.xianlu_iv.setBackgroundResource(R.mipmap.trip_icon_line);
            }
        } else {
            vh.citysTV.setVisibility(GONE);
            vh.mTypeStr.setVisibility(VISIBLE);
            vh.verticalLine.setVisibility(VISIBLE);
            vh.mCarType.setVisibility(VISIBLE);
            vh.mCarType.setText(orderBean.carDesc);//车辆类型
            vh.chexing.setVisibility(GONE);
            vh.xianlu_iv.setVisibility(GONE);
            switch (orderBean.orderType) {
                case Constants.BUSINESS_TYPE_PICK://接机
                    vh.mTypeStr.setText("中文接机");
                    vh.itemTime.setBackgroundResource(R.mipmap.trip_icon_time);
                    try {
                        vh.timeTV.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    vh.timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.startAddress)) {
                        vh.startAddressLayout.setVisibility(GONE);
                    } else {
                        vh.startAddressLayout.setVisibility(VISIBLE);
                        //startAddressIV.setBackgroundResource(R.mipmap.order_place);
                        vh.startAddressTV.setText(orderBean.startAddress + " " + orderBean.startAddressDetail);
                    }

                    if (TextUtils.isEmpty(orderBean.destAddress)) {
                        vh.endAddressLayout.setVisibility(GONE);
                    } else {
                        vh.endAddressLayout.setVisibility(VISIBLE);
                        //endAddressIV.setBackgroundResource(R.mipmap.order_flag);
                        vh.endAddressTV.setText(orderBean.destAddress + " " + orderBean.destAddressDetail);
                    }
                    vh.startAddressIV1.setVisibility(VISIBLE);
                    vh.startAddressIV2.setVisibility(GONE);
                    break;
                case Constants.BUSINESS_TYPE_SEND://送机
                    vh.mTypeStr.setText("中文送机");
                    vh.itemTime.setBackgroundResource(R.mipmap.trip_icon_time);
                    try {
                        vh.timeTV.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    vh.timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.startAddress)) {
                        vh.startAddressLayout.setVisibility(GONE);
                    } else {
                        vh.startAddressLayout.setVisibility(VISIBLE);
                        //startAddressIV.setBackgroundResource(R.mipmap.order_place);
                        vh.startAddressTV.setText(orderBean.startAddress + " " + orderBean.startAddressDetail);
                    }

                    if (TextUtils.isEmpty(orderBean.destAddress)) {
                        vh.endAddressLayout.setVisibility(GONE);
                    } else {
                        vh.endAddressLayout.setVisibility(VISIBLE);
                        //endAddressIV.setBackgroundResource(R.mipmap.order_flag);
                        vh.endAddressTV.setText(orderBean.destAddress + " " + orderBean.destAddressDetail);
                    }
                    vh.startAddressIV1.setVisibility(VISIBLE);
                    vh.startAddressIV2.setVisibility(GONE);
                    break;
                case Constants.BUSINESS_TYPE_DAILY://日租 包车游
                    vh.mTypeStr.setText("按天包车游");
                    vh.itemTime.setBackgroundResource(R.mipmap.trip_icon_date);
                    if (orderBean.isHalfDaily == 1) {//半日包
                        vh.timeTV.setText(orderBean.serviceTime + " 半天");
                    } else {
                        vh.timeTV.setText(orderBean.serviceTime + " 至 " + orderBean.serviceEndTime + " " + orderBean.totalDays + "天");
                    }
                    vh.timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.serviceCityName)) {
                        vh.startAddressLayout.setVisibility(GONE);
                    } else {
                        vh.startAddressLayout.setVisibility(VISIBLE);
                        //startAddressIV.setBackgroundResource(R.mipmap.trip_icon_line);
                        String dailyPlace = orderBean.serviceCityName;
                        if (!TextUtils.isEmpty(orderBean.serviceEndCityName)) {
                            dailyPlace += " - " + orderBean.serviceEndCityName;
                        }
                        vh.startAddressTV.setText(dailyPlace);
                    }
                    vh.startAddressIV1.setVisibility(GONE);
                    vh.startAddressIV2.setVisibility(VISIBLE);
                    vh.startAddressIV2.setBackgroundResource(R.mipmap.trip_icon_line);
                    vh.endAddressLayout.setVisibility(GONE);
                    break;
                case Constants.BUSINESS_TYPE_RENT://次租 单次接送
                    vh.mTypeStr.setText("单次接送");
                    vh.itemTime.setBackgroundResource(R.mipmap.trip_icon_time);
                    try {
                        vh.timeTV.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    vh.timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.startAddress)) {
                        vh.startAddressLayout.setVisibility(GONE);
                    } else {
                        vh.startAddressLayout.setVisibility(VISIBLE);
                        //startAddressIV.setBackgroundResource(R.mipmap.order_place);
                        vh.startAddressTV.setText(orderBean.startAddress + " " + orderBean.startAddressDetail);
                    }

                    if (TextUtils.isEmpty(orderBean.destAddress)) {
                        vh.endAddressLayout.setVisibility(GONE);
                    } else {
                        vh.endAddressLayout.setVisibility(VISIBLE);
                        //endAddressIV.setBackgroundResource(R.mipmap.order_flag);
                        vh.endAddressTV.setText(orderBean.destAddress + " " + orderBean.destAddressDetail);
                    }
                    vh.startAddressIV1.setVisibility(VISIBLE);
                    vh.startAddressIV2.setVisibility(GONE);
                    break;
                case Constants.BUSINESS_TYPE_COMBINATION://组合单
                    vh.mTypeStr.setText("按天包车游");
                    vh.itemTime.setBackgroundResource(R.mipmap.trip_icon_date);
                    vh.timeTV.setText(orderBean.serviceTime + " 至 " + orderBean.serviceEndTime + " " + orderBean.totalDays + "天");
                    vh.timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.serviceCityName)) {
                        vh.startAddressLayout.setVisibility(GONE);
                    } else {
                        vh.startAddressLayout.setVisibility(VISIBLE);
                        //startAddressIV.setBackgroundResource(R.mipmap.trip_icon_line);
                        String dailyPlace = orderBean.serviceCityName;
                        if (!TextUtils.isEmpty(orderBean.serviceEndCityName)) {
                            dailyPlace += " - " + orderBean.serviceEndCityName;
                        }
                        dailyPlace += String.format("(含%1$s段行程)", orderBean.orderJourneyCount);
                        vh.startAddressTV.setText(dailyPlace);
                    }
                    vh.startAddressIV1.setVisibility(GONE);
                    vh.startAddressIV2.setVisibility(VISIBLE);
                    vh.startAddressIV2.setBackgroundResource(R.mipmap.trip_icon_line);
                    vh.endAddressLayout.setVisibility(GONE);
                    break;
            }
        }
        //mBtnChat.setVisibility(View.GONE);
        setStatusView(vh,orderBean);
    }

    /**
     * 根据状态重置操作栏
     *
     * @param vh
     * @param orderBean
     */
    private void setStatusView(NewOrderVH vh, final OrderBean orderBean) {
        vh.mAssessment.setOnClickListener(null);
        vh.mStatus.setText(orderBean.orderStatus.name);
        boolean isShowAvartarLayout = false;
        if (orderBean.orderType == 888 && orderBean.isSeparateOrder() && orderBean.orderStatus.code > 1) {
            List<String> subOrderGuideAvartar = orderBean.subOrderGuideAvartar;
            if (subOrderGuideAvartar == null || subOrderGuideAvartar.size() <= 0) {
                vh.travel_item_head_layout_all.setVisibility(GONE);
                vh.mStatusLayout.setVisibility(GONE);
                vh.lineView.setVisibility(View.INVISIBLE);
            } else {
                vh.mStatusLayout.setVisibility(VISIBLE);
                vh.lineView.setVisibility(VISIBLE);
                vh.travel_item_head_layout_all.setVisibility(VISIBLE);
                vh.mHeadLayout.setVisibility(GONE);
                vh.mAssessment.setVisibility(GONE);//评价司导
                vh.mBtnPay.setVisibility(GONE);

                int size = subOrderGuideAvartar.size();
                if (0 < size) {
                    isShowAvartarLayout = true;
                    vh.travel_item_head_img1.setVisibility(VISIBLE);
                    Tools.showImage(vh.travel_item_head_img1, subOrderGuideAvartar.get(0), R.mipmap.icon_avatar_guide);
                } else {
                    vh.travel_item_head_img1.setVisibility(GONE);
                }
                if (1 < size) {
                    vh.travel_item_head_img2.setVisibility(VISIBLE);
                    Tools.showImage(vh.travel_item_head_img2, subOrderGuideAvartar.get(1), R.mipmap.icon_avatar_guide);
                } else {
                    vh.travel_item_head_img2.setVisibility(GONE);
                }
                if (2 < size) {
                    vh.travel_item_head_img3.setVisibility(VISIBLE);
                    Tools.showImage(vh.travel_item_head_img3, subOrderGuideAvartar.get(2), R.mipmap.icon_avatar_guide);
                } else {
                    vh.travel_item_head_img3.setVisibility(GONE);
                }
                vh.travel_item_head_more_tv.setVisibility(3 < size ? VISIBLE : GONE);
            }
        } else {
            vh.travel_item_head_layout_all.setVisibility(GONE);
        }
        switch (orderBean.orderStatus) {
            case INITSTATE://等待支付 初始状态
                vh.mStatus.setTextColor(0xffff2525);
                vh.mStatusLayout.setVisibility(VISIBLE);
                vh.lineView.setVisibility(VISIBLE);
                vh.br_layout.setVisibility(GONE);//添加投保人
                vh.mPrice.setVisibility(VISIBLE);//支付TV
                if(orderBean.orderPriceInfo != null){
                    vh.mPrice.setText("支付金额：" + Math.round(orderBean.orderPriceInfo.actualPay) + "元");
                }
                vh.mHeadLayout.setVisibility(GONE);//司导信息
                vh.mBtnPay.setVisibility(VISIBLE);//立即支付btn
                vh.mBtnPay.setTag(orderBean);
                vh.mBtnPay.setOnClickListener(new TravelOnClickListener(orderBean));
                vh.mBtnChat.setVisibility(GONE);
                vh.mAssessment.setVisibility(GONE);//评价司导
                break;
            case PAYSUCCESS://预订成功
                vh.mStatus.setTextColor(0xff7f7f7f);
                vh.mPrice.setVisibility(GONE);
                vh.mBtnPay.setVisibility(GONE);
                vh.mHeadLayout.setVisibility(GONE);
                vh.mAssessment.setVisibility(GONE);
                if (orderBean.insuranceEnable) {
                    vh.mStatusLayout.setVisibility(VISIBLE);
                    vh.lineView.setVisibility(VISIBLE);
                    vh.br_layout.setVisibility(VISIBLE);
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
                    if (!isShowAvartarLayout) {
                        vh.mStatusLayout.setVisibility(GONE);
                        vh.lineView.setVisibility(View.INVISIBLE);
                    }
                    vh.br_layout.setVisibility(GONE);
                }
                break;
            case AGREE://司导已接单
            case ARRIVED://司导已到达
            case SERVICING://服务中
                vh.mStatus.setTextColor(0xff7f7f7f);
                vh.mPrice.setVisibility(GONE);
                vh.mBtnPay.setVisibility(GONE);
                vh.mAssessment.setVisibility(GONE);

                boolean isShowStatusLayout = false;
                if (!isShowAvartarLayout && orderBean.orderGuideInfo != null) {
                    vh.mHeadLayout.setVisibility(VISIBLE);
                    vh.mHeadTitle.setText(orderBean.getGuideName());
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        vh.mHeadImg.setImageResource(R.mipmap.icon_avatar_guide);
                    } else {
                        Tools.showImage(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.icon_avatar_guide);
                    }
                    vh.mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    vh.mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));

                    if(orderBean.isIm){
                        vh.mBtnChat.setVisibility(VISIBLE);
                        vh.mBtnChat.setOnClickListener(new TravelOnClickListener(orderBean));
                        showMessageNum(vh.mBtnChatNum, orderBean.imcount);//显示未读小红点个数
                    }else{
                        vh.mBtnChat.setVisibility(GONE);
                    }
                    isShowStatusLayout = true;
                } else {
                    vh.mHeadLayout.setVisibility(GONE);
                }

                if (orderBean.insuranceEnable) {
                    vh.br_layout.setVisibility(VISIBLE);
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
                    isShowStatusLayout = true;
                } else {
                    vh.br_layout.setVisibility(GONE);
                }
                if (isShowStatusLayout) {
                    vh.mStatusLayout.setVisibility(VISIBLE);
                    vh.lineView.setVisibility(VISIBLE);
                } else if (!isShowAvartarLayout) {
                    vh.mStatusLayout.setVisibility(GONE);
                    vh.lineView.setVisibility(View.INVISIBLE);
                }
                break;
            case NOT_EVALUATED://未评价
            case COMPLETE://已完成
                vh.mStatus.setTextColor(0xff7f7f7f);
                vh.mStatusLayout.setVisibility(VISIBLE);
                vh.lineView.setVisibility(VISIBLE);
                vh.mPrice.setVisibility(GONE);
                vh.mBtnPay.setVisibility(GONE);
                vh.br_layout.setVisibility(GONE);

                if (orderBean.orderGuideInfo != null && !isShowAvartarLayout) {
                    vh.mHeadLayout.setVisibility(VISIBLE);
                    vh.mStatusLayout.setVisibility(VISIBLE);
                    vh.lineView.setVisibility(VISIBLE);

                    vh.mHeadTitle.setText(orderBean.getGuideName());
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        vh.mHeadImg.setImageResource(R.mipmap.icon_avatar_guide);
                    } else {
                        Tools.showImage(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.icon_avatar_guide);
                    }
                    vh.mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    vh.mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));

                    if(orderBean.isIm){
                        vh.mBtnChat.setVisibility(VISIBLE);
                        vh.mBtnChat.setOnClickListener(new TravelOnClickListener(orderBean));
                        showMessageNum(vh.mBtnChatNum, orderBean.imcount);//显示未读小红点个数
                    }else{
                        vh.mBtnChat.setVisibility(GONE);
                    }

                    if (!orderBean.isEvaluated() && orderBean.orderType != 888) {//服务完成未评价
                        vh.mAssessment.setVisibility(VISIBLE);
                        vh.mAssessment.setOnClickListener(new TravelOnClickListener(orderBean));
                    } else {
                        vh.mAssessment.setVisibility(GONE);
                    }
                } else {
                    if (!isShowAvartarLayout) {
                        vh.mStatusLayout.setVisibility(GONE);
                        vh.lineView.setVisibility(View.INVISIBLE);
                    }
                    vh.mHeadLayout.setVisibility(GONE);
                    vh.mAssessment.setVisibility(GONE);
                }
                break;
            case CANCELLED://已取消
            case REFUNDED://已退款
                vh.mStatus.setTextColor(0xff7f7f7f);
                vh.mPrice.setVisibility(GONE);
                vh.mBtnPay.setVisibility(GONE);
                vh.mAssessment.setVisibility(GONE);
                vh.br_layout.setVisibility(GONE);
                if (orderBean.orderGuideInfo != null && !isShowAvartarLayout) {
                    vh.mHeadLayout.setVisibility(VISIBLE);
                    vh.mStatusLayout.setVisibility(VISIBLE);
                    vh.lineView.setVisibility(VISIBLE);

                    vh.mHeadTitle.setText(orderBean.getGuideName());
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        vh.mHeadImg.setImageResource(R.mipmap.icon_avatar_guide);
                    } else {
                        Tools.showImage(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.icon_avatar_guide);
                    }
                    vh.mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    vh.mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));
                    vh.mBtnChat.setVisibility(GONE);
                } else {
                    if (!isShowAvartarLayout) {
                        vh.mStatusLayout.setVisibility(GONE);
                        vh.lineView.setVisibility(View.INVISIBLE);
                    }
                    vh.mHeadLayout.setVisibility(GONE);
                }
                break;
            case COMPLAINT://客诉处理中
                vh.mStatus.setTextColor(0xff7f7f7f);
                vh.mPrice.setVisibility(GONE);
                vh.mBtnPay.setVisibility(GONE);
                vh.mAssessment.setVisibility(GONE);
                vh.br_layout.setVisibility(GONE);
                if (orderBean.orderGuideInfo != null && !isShowAvartarLayout) {
                    vh.mHeadLayout.setVisibility(VISIBLE);
                    vh.mStatusLayout.setVisibility(VISIBLE);
                    vh.lineView.setVisibility(VISIBLE);

                    vh.mHeadTitle.setText(orderBean.getGuideName());
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        vh.mHeadImg.setImageResource(R.mipmap.icon_avatar_guide);
                    } else {
                        Tools.showImage(vh.mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.icon_avatar_guide);
                    }
                    vh.mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    vh.mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));
                    vh.mBtnChat.setVisibility(GONE);
                } else {
                    if (!isShowAvartarLayout) {
                        vh.mStatusLayout.setVisibility(GONE);
                        vh.lineView.setVisibility(View.INVISIBLE);
                    }
                    vh.mHeadLayout.setVisibility(GONE);
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
        chatNumTextView.setVisibility(GONE);
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
                    intent = new Intent(v.getContext(), EvaluateNewActivity.class);
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
                    if(mOrderBean.imInfo!=null){
                        String imId = mOrderBean.imInfo.getNeTargetId();
                        if(!TextUtils.isEmpty(imId)){
                            gotoChatView(mOrderBean.imInfo.targetId, imId);
                        }else{
                            if(mOrderBean.orderGuideInfo!=null){
                                requestImChatId(mOrderBean.orderGuideInfo);
                            }
                        }
                    }else{
                        if(mOrderBean.orderGuideInfo!=null){
                            requestImChatId(mOrderBean.orderGuideInfo);
                        }
                    }
                    break;
                case R.id.travel_item_head_img:
                case R.id.travel_item_head_title:
                    if(mOrderBean.orderGuideInfo == null || mOrderBean.orderGuideInfo.guideID == null) {
                        return;
                    }
                    GuideWebDetailActivity.Params guideDetailParams = new GuideWebDetailActivity.Params();
                    guideDetailParams.guideId = mOrderBean.orderGuideInfo.guideID;
//                    guideDetailParams.guideCarId = mOrderBean.orderGuideInfo.guideCarId;
//                    guideDetailParams.guideAgencyDriverId = mOrderBean.guideAgencyDriverId;
//                    guideDetailParams.isSelectedService = mOrderBean.guideAgencyType == 3;

                    intent = new Intent(v.getContext(), GuideWebDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, guideDetailParams);
                    intent.putExtra(Constants.PARAMS_SOURCE, "行程");
                    v.getContext().startActivity(intent);
                    break;
            }
        }
    }

    private void requestImChatId(final OrderGuideInfo orderGuideInfo){
        if(TextUtils.isEmpty(orderGuideInfo.guideID)){
            return;
        }
        RequestImChatId requestImChatId = new RequestImChatId(context, UserEntity.getUser().getUserId(context),"2",orderGuideInfo.guideID,"1");
        HttpRequestUtils.request(context,requestImChatId,new HttpRequestListener(){
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                Object object = request.getData();
                if(object instanceof ImChatInfo){
                    ImChatInfo imChatInfo = (ImChatInfo)object;
                    gotoChatView(imChatInfo.targetId, imChatInfo.neTargetId);
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

    private void gotoChatView(String guideId, String imChatId) {
        String source = "行程";
        if (context instanceof NewOrderActivity) {
            source = ((NewOrderActivity) context).getEventSource();
        }
        NIMChatActivity.start(context,guideId,imChatId,source);
    }

}
