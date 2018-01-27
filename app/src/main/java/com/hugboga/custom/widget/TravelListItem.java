package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.EvaluateNewActivity;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.InsureActivity;
import com.hugboga.custom.activity.NIMChatActivity;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ImChatInfo;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderGuideInfo;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestImChatId;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/6/26.
 */

public class TravelListItem extends LinearLayout implements HbcViewBehavior{

    @BindView(R.id.travel_item_typestr)
    public TextView mTypeStr; //订单类型
    @BindView(R.id.travel_item_cartype)
    public TextView mCarType; //车辆类型
    @BindView(R.id.order_item_time_tv)
    public TextView timeTV;
    @BindView(R.id.travel_item_citys)
    public TextView citysTV;
    @BindView(R.id.order_item_time_local_tv)
    public TextView timeLocalTV;
    @BindView(R.id.order_item_time)
    public ImageView itemTime;
    @BindView(R.id.order_item_start_address_tv)
    public TextView startAddressTV;
    @BindView(R.id.order_item_end_address_tv)
    public TextView endAddressTV;
    @BindView(R.id.order_item_start_address_iv1_layout)
    public LinearLayout startAddressIV1;
    @BindView(R.id.order_item_start_address_iv2_layout)
    public ImageView startAddressIV2;
    @BindView(R.id.order_item_xianlu_iv)
    public ImageView xianlu_iv;
    @BindView(R.id.order_item_chexing)
    public ImageView chexing;
    @BindView(R.id.order_item_start_address_layout)
    public LinearLayout startAddressLayout;
    @BindView(R.id.order_item_end_address_layout)
    public LinearLayout endAddressLayout;
    @BindView(R.id.order_list_line)
    public View lineView;
    @BindView(R.id.order_list_vertical_line)
    public View verticalLine;
    @BindView(R.id.travel_item_status)
    public TextView mStatus; //订单状态
    @BindView(R.id.travel_item_status_layout)
    public RelativeLayout mStatusLayout; //状态行为操作栏
    @BindView(R.id.travel_item_price)
    public TextView mPrice; //支付费用
    @BindView(R.id.travel_item_head_layout)
    public LinearLayout mHeadLayout; //导游信息
    @BindView(R.id.travel_item_head_img)
    public CircleImageView mHeadImg;//导游头像
    @BindView(R.id.travel_item_head_title)
    public TextView mHeadTitle; //导游名称
    @BindView(R.id.travel_item_btn_pay)
    public TextView mBtnPay; //立即支付
    @BindView(R.id.travel_item_btn_chat)
    public TextView mBtnChat; //聊一聊按钮
    @BindView(R.id.travel_item_btn_chat_num)
    public TextView mBtnChatNum; //未读消息个数
    @BindView(R.id.travel_item_btn_assessment)
    public TextView mAssessment; //评价车导
    @BindView(R.id.br_layout)
    public LinearLayout br_layout;
    @BindView(R.id.travel_item_btn_br)
    public TextView travel_item_btn_br;
    @BindView(R.id.travel_item_btn_br_tips)
    public ImageView travel_item_btn_br_tips;

    @BindView(R.id.travel_item_head_layout_all)
    public LinearLayout travel_item_head_layout_all;
    @BindView(R.id.travel_item_head_img1)
    public CircleImageView travel_item_head_img1;;
    @BindView(R.id.travel_item_head_img2)
    public CircleImageView travel_item_head_img2;
    @BindView(R.id.travel_item_head_layout_1)
    public FrameLayout travel_item_head_layout_1;
    @BindView(R.id.travel_item_head_img3)
    public CircleImageView travel_item_head_img3;
    @BindView(R.id.travel_item_head_more_tv)
    public TextView travel_item_head_more_tv;

    public TravelListItem(Context context) {
        this(context, null);
    }

    public TravelListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.order_list_item, this);
        ButterKnife.bind(view);

    }

    @Override
    public void update(Object _data) {
        OrderBean orderBean = (OrderBean)_data;
        //订单状态
        if (orderBean.orderType == Constants.BUSINESS_TYPE_COMMEND || orderBean.orderType == Constants.BUSINESS_TYPE_RECOMMEND) {//线路包车
            citysTV.setVisibility(View.GONE);
            mTypeStr.setVisibility(View.VISIBLE);
            verticalLine.setVisibility(View.VISIBLE);
            mCarType.setVisibility(View.GONE);
            startAddressIV1.setVisibility(GONE);
            startAddressIV2.setVisibility(GONE);
            itemTime.setBackgroundResource(R.mipmap.trip_icon_date);
            if(!TextUtils.isEmpty(orderBean.carDesc)){
                mCarType.setVisibility(View.VISIBLE);
                mCarType.setText(orderBean.carDesc);
            }
            if (orderBean.carPool) {//是否拼车
                Drawable drawable = getResources().getDrawable(R.mipmap.carpooling);
                drawable.setBounds(0, 0, UIUtils.dip2px(36), UIUtils.dip2px(18));
                SpannableString spannable = new SpannableString("[icon]" + "线路包车游");
                ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                spannable.setSpan(span, 0, "[icon]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                mTypeStr.setText(spannable);
                chexing.setVisibility(GONE);
                endAddressLayout.setVisibility(View.GONE);
            } else {
                mTypeStr.setText("线路包车游");
                if (TextUtils.isEmpty(orderBean.carDesc)) {
                    endAddressLayout.setVisibility(View.GONE);
                    chexing.setVisibility(GONE);
                } else {
                    chexing.setVisibility(VISIBLE);
                    endAddressLayout.setVisibility(View.VISIBLE);
                    endAddressTV.setText(orderBean.lineSubject);
                }
            }

            timeTV.setText(orderBean.serviceTime + " 至 " + orderBean.serviceEndTime + " " + orderBean.totalDays + "天");
            timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

            if (TextUtils.isEmpty(orderBean.serviceCityName)) {
                startAddressLayout.setVisibility(View.GONE);
                xianlu_iv.setVisibility(GONE);
            } else {
                startAddressLayout.setVisibility(View.VISIBLE);
                String dailyPlace = orderBean.serviceCityName;
                if (!TextUtils.isEmpty(orderBean.serviceEndCityName)) {
                    dailyPlace += " - " + orderBean.serviceEndCityName;
                }
                startAddressTV.setText(dailyPlace);
                xianlu_iv.setVisibility(VISIBLE);
                xianlu_iv.setBackgroundResource(R.mipmap.trip_icon_line);
            }
        } else {
            citysTV.setVisibility(View.GONE);
            mTypeStr.setVisibility(View.VISIBLE);
            verticalLine.setVisibility(View.VISIBLE);
            mCarType.setVisibility(View.VISIBLE);
            mCarType.setText(orderBean.carDesc);//车辆类型
            chexing.setVisibility(GONE);
            xianlu_iv.setVisibility(GONE);
            switch (orderBean.orderType) {
                case Constants.BUSINESS_TYPE_PICK://接机
                    mTypeStr.setText("中文接机");
                    itemTime.setBackgroundResource(R.mipmap.trip_icon_time);
                    try {
                        timeTV.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.startAddress)) {
                        startAddressLayout.setVisibility(View.GONE);
                    } else {
                        startAddressLayout.setVisibility(View.VISIBLE);
                        //startAddressIV.setBackgroundResource(R.mipmap.order_place);
                        startAddressTV.setText(orderBean.startAddress + " " + orderBean.startAddressDetail);
                    }

                    if (TextUtils.isEmpty(orderBean.destAddress)) {
                        endAddressLayout.setVisibility(View.GONE);
                    } else {
                        endAddressLayout.setVisibility(View.VISIBLE);
                        //endAddressIV.setBackgroundResource(R.mipmap.order_flag);
                        endAddressTV.setText(orderBean.destAddress + " " + orderBean.destAddressDetail);
                    }
                    startAddressIV1.setVisibility(VISIBLE);
                    startAddressIV2.setVisibility(GONE);
                    break;
                case Constants.BUSINESS_TYPE_SEND://送机
                    mTypeStr.setText("中文送机");
                    itemTime.setBackgroundResource(R.mipmap.trip_icon_time);
                    try {
                        timeTV.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.startAddress)) {
                        startAddressLayout.setVisibility(View.GONE);
                    } else {
                        startAddressLayout.setVisibility(View.VISIBLE);
                        //startAddressIV.setBackgroundResource(R.mipmap.order_place);
                        startAddressTV.setText(orderBean.startAddress + " " + orderBean.startAddressDetail);
                    }

                    if (TextUtils.isEmpty(orderBean.destAddress)) {
                        endAddressLayout.setVisibility(View.GONE);
                    } else {
                        endAddressLayout.setVisibility(View.VISIBLE);
                        //endAddressIV.setBackgroundResource(R.mipmap.order_flag);
                        endAddressTV.setText(orderBean.destAddress + " " + orderBean.destAddressDetail);
                    }
                    startAddressIV1.setVisibility(VISIBLE);
                    startAddressIV2.setVisibility(GONE);
                    break;
                case Constants.BUSINESS_TYPE_DAILY://日租 包车游
                    mTypeStr.setText("按天包车游");
                    itemTime.setBackgroundResource(R.mipmap.trip_icon_date);
                    if (orderBean.isHalfDaily == 1) {//半日包
                        timeTV.setText(orderBean.serviceTime + " 半天");
                    } else {
                        timeTV.setText(orderBean.serviceTime + " 至 " + orderBean.serviceEndTime + " " + orderBean.totalDays + "天");
                    }
                    timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.serviceCityName)) {
                        startAddressLayout.setVisibility(View.GONE);
                    } else {
                        startAddressLayout.setVisibility(View.VISIBLE);
                        //startAddressIV.setBackgroundResource(R.mipmap.trip_icon_line);
                        String dailyPlace = orderBean.serviceCityName;
                        if (!TextUtils.isEmpty(orderBean.serviceEndCityName)) {
                            dailyPlace += " - " + orderBean.serviceEndCityName;
                        }
                        startAddressTV.setText(dailyPlace);
                    }
                    startAddressIV1.setVisibility(GONE);
                    startAddressIV2.setVisibility(VISIBLE);
                    startAddressIV2.setBackgroundResource(R.mipmap.trip_icon_line);
                    endAddressLayout.setVisibility(View.GONE);
                    break;
                case Constants.BUSINESS_TYPE_RENT://次租 单次接送
                    mTypeStr.setText("单次接送");
                    itemTime.setBackgroundResource(R.mipmap.trip_icon_time);
                    try {
                        timeTV.setText(DateUtils.getWeekStrFromDate(orderBean.serviceTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.startAddress)) {
                        startAddressLayout.setVisibility(View.GONE);
                    } else {
                        startAddressLayout.setVisibility(View.VISIBLE);
                        //startAddressIV.setBackgroundResource(R.mipmap.order_place);
                        startAddressTV.setText(orderBean.startAddress + " " + orderBean.startAddressDetail);
                    }

                    if (TextUtils.isEmpty(orderBean.destAddress)) {
                        endAddressLayout.setVisibility(View.GONE);
                    } else {
                        endAddressLayout.setVisibility(View.VISIBLE);
                        //endAddressIV.setBackgroundResource(R.mipmap.order_flag);
                        endAddressTV.setText(orderBean.destAddress + " " + orderBean.destAddressDetail);
                    }
                    startAddressIV1.setVisibility(VISIBLE);
                    startAddressIV2.setVisibility(GONE);
                    break;
                case Constants.BUSINESS_TYPE_COMBINATION://组合单
                    mTypeStr.setText("按天包车游");
                    itemTime.setBackgroundResource(R.mipmap.trip_icon_date);
                    timeTV.setText(orderBean.serviceTime + " 至 " + orderBean.serviceEndTime + " " + orderBean.totalDays + "天");
                    timeLocalTV.setText("(" + orderBean.serviceCityName + "时间)");//当地城市时间

                    if (TextUtils.isEmpty(orderBean.serviceCityName)) {
                        startAddressLayout.setVisibility(View.GONE);
                    } else {
                        startAddressLayout.setVisibility(View.VISIBLE);
                        //startAddressIV.setBackgroundResource(R.mipmap.trip_icon_line);
                        String dailyPlace = orderBean.serviceCityName;
                        if (!TextUtils.isEmpty(orderBean.serviceEndCityName)) {
                            dailyPlace += " - " + orderBean.serviceEndCityName;
                        }
                        dailyPlace += String.format("(含%1$s段行程)", orderBean.orderJourneyCount);
                        startAddressTV.setText(dailyPlace);
                    }
                    startAddressIV1.setVisibility(GONE);
                    startAddressIV2.setVisibility(VISIBLE);
                    startAddressIV2.setBackgroundResource(R.mipmap.trip_icon_line);
                    endAddressLayout.setVisibility(View.GONE);
                    break;
            }
        }
        //mBtnChat.setVisibility(View.GONE);
        setStatusView(orderBean);
    }

    /**
     * 根据状态重置操作栏
     *
     * @param orderBean
     */
    private void setStatusView(final OrderBean orderBean) {
        mAssessment.setOnClickListener(null);
        mStatus.setText(orderBean.isTwiceConfirm ? getContext().getResources().getString(R.string.order_detail_state_twiceconfirm) : orderBean.orderStatus.name);
        boolean isShowAvartarLayout = false;
        if (orderBean.orderType == 888 && orderBean.isSeparateOrder() && orderBean.orderStatus.code > 1) {
            List<String> subOrderGuideAvartar = orderBean.subOrderGuideAvartar;
            if (subOrderGuideAvartar == null || subOrderGuideAvartar.size() <= 0) {
                travel_item_head_layout_all.setVisibility(View.GONE);
                mStatusLayout.setVisibility(View.GONE);
                lineView.setVisibility(View.INVISIBLE);
            } else {
                mStatusLayout.setVisibility(View.VISIBLE);
                lineView.setVisibility(View.VISIBLE);
                travel_item_head_layout_all.setVisibility(View.VISIBLE);
                mHeadLayout.setVisibility(View.GONE);
                mAssessment.setVisibility(View.GONE);//评价司导
                mBtnPay.setVisibility(View.GONE);

                int size = subOrderGuideAvartar.size();
                if (0 < size) {
                    isShowAvartarLayout = true;
                    travel_item_head_img1.setVisibility(View.VISIBLE);
                    Tools.showImage(travel_item_head_img1, subOrderGuideAvartar.get(0), R.mipmap.icon_avatar_guide);
                } else {
                    travel_item_head_img1.setVisibility(View.GONE);
                }
                if (1 < size) {
                    travel_item_head_img2.setVisibility(View.VISIBLE);
                    Tools.showImage(travel_item_head_img2, subOrderGuideAvartar.get(1), R.mipmap.icon_avatar_guide);
                } else {
                    travel_item_head_img2.setVisibility(View.GONE);
                }
                if (2 < size) {
                    travel_item_head_img3.setVisibility(View.VISIBLE);
                    Tools.showImage(travel_item_head_img3, subOrderGuideAvartar.get(2), R.mipmap.icon_avatar_guide);
                } else {
                    travel_item_head_img3.setVisibility(View.GONE);
                }
                travel_item_head_more_tv.setVisibility(3 < size ? View.VISIBLE : View.GONE);
            }
        } else {
            travel_item_head_layout_all.setVisibility(View.GONE);
        }
        switch (orderBean.orderStatus) {
            case INITSTATE://等待支付 初始状态
                mStatus.setTextColor(0xffff2525);
                mStatusLayout.setVisibility(View.VISIBLE);
                lineView.setVisibility(View.VISIBLE);
                br_layout.setVisibility(View.GONE);//添加投保人
                mPrice.setVisibility(View.VISIBLE);//支付TV
                if(orderBean.orderPriceInfo != null){
                    mPrice.setText("支付金额：" + Math.round(orderBean.orderPriceInfo.actualPay) + "元");
                }
                mHeadLayout.setVisibility(View.GONE);//司导信息
                mBtnPay.setVisibility(View.VISIBLE);//立即支付btn
                mBtnPay.setTag(orderBean);
                mBtnPay.setOnClickListener(new TravelOnClickListener(orderBean));
                mBtnChat.setVisibility(View.GONE);
                mAssessment.setVisibility(View.GONE);//评价司导
                break;
            case PAYSUCCESS://预订成功
                mStatus.setTextColor(orderBean.isTwiceConfirm ? 0xffff2525 : 0xff7f7f7f);
                mPrice.setVisibility(View.GONE);
                mBtnPay.setVisibility(View.GONE);
                mHeadLayout.setVisibility(View.GONE);
                mAssessment.setVisibility(View.GONE);
                if (orderBean.insuranceEnable) {
                    mStatusLayout.setVisibility(View.VISIBLE);
                    lineView.setVisibility(View.VISIBLE);
                    br_layout.setVisibility(View.VISIBLE);
                    travel_item_btn_br.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("orderBean",orderBean);
                            bundle.putString("from","orderList");
                            Intent intent = new Intent(getContext(), InsureActivity.class);
                            intent.putExtras(bundle);
                            getContext().startActivity(intent);
                        }
                    });
                    travel_item_btn_br_tips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), WebInfoActivity.class);
                            intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_INSURANCE);
                            v.getContext().startActivity(intent);
                        }
                    });
                } else {
                    if (!isShowAvartarLayout) {
                        mStatusLayout.setVisibility(View.GONE);
                        lineView.setVisibility(View.INVISIBLE);
                    }
                    br_layout.setVisibility(View.GONE);
                }
                break;
            case AGREE://司导已接单
            case ARRIVED://司导已到达
            case SERVICING://服务中
                mStatus.setTextColor(0xff7f7f7f);
                mPrice.setVisibility(View.GONE);
                mBtnPay.setVisibility(View.GONE);
                mAssessment.setVisibility(View.GONE);

                boolean isShowStatusLayout = false;
                if (!isShowAvartarLayout && orderBean.orderGuideInfo != null) {
                    mHeadLayout.setVisibility(View.VISIBLE);
                    mHeadTitle.setText(orderBean.getGuideName());
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        mHeadImg.setImageResource(R.mipmap.icon_avatar_guide);
                    } else {
                        Tools.showImage(mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.icon_avatar_guide);
                    }
                    mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));

                    if(orderBean.isIm){
                        mBtnChat.setVisibility(View.VISIBLE);
                        mBtnChat.setOnClickListener(new TravelOnClickListener(orderBean));
                        showMessageNum(mBtnChatNum, orderBean.imcount);//显示未读小红点个数
                    }else{
                        mBtnChat.setVisibility(View.GONE);
                    }
                    isShowStatusLayout = true;
                } else {
                    mHeadLayout.setVisibility(View.GONE);
                }

                if (orderBean.insuranceEnable) {
                    br_layout.setVisibility(View.VISIBLE);
                    travel_item_btn_br.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("orderBean",orderBean);
                            bundle.putString("from","orderList");
                            Intent intent = new Intent(getContext(), InsureActivity.class);
                            intent.putExtras(bundle);
                            getContext().startActivity(intent);
                        }
                    });
                    travel_item_btn_br_tips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), WebInfoActivity.class);
                            intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_INSURANCE);
                            v.getContext().startActivity(intent);
                        }
                    });
                    isShowStatusLayout = true;
                } else {
                    br_layout.setVisibility(View.GONE);
                }
                if (isShowStatusLayout) {
                    mStatusLayout.setVisibility(View.VISIBLE);
                    lineView.setVisibility(View.VISIBLE);
                } else if (!isShowAvartarLayout) {
                    mStatusLayout.setVisibility(View.GONE);
                    lineView.setVisibility(View.INVISIBLE);
                }
                break;
            case NOT_EVALUATED://未评价
            case COMPLETE://已完成
                mStatus.setTextColor(0xff7f7f7f);
                mStatusLayout.setVisibility(View.VISIBLE);
                lineView.setVisibility(View.VISIBLE);
                mPrice.setVisibility(View.GONE);
                mBtnPay.setVisibility(View.GONE);
                br_layout.setVisibility(View.GONE);

                if (orderBean.orderGuideInfo != null && !isShowAvartarLayout) {
                    mHeadLayout.setVisibility(View.VISIBLE);
                    mStatusLayout.setVisibility(View.VISIBLE);
                    lineView.setVisibility(View.VISIBLE);

                    mHeadTitle.setText(orderBean.getGuideName());
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        mHeadImg.setImageResource(R.mipmap.icon_avatar_guide);
                    } else {
                        Tools.showImage(mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.icon_avatar_guide);
                    }
                    mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));

                    if(orderBean.isIm){
                        mBtnChat.setVisibility(View.VISIBLE);
                        mBtnChat.setOnClickListener(new TravelOnClickListener(orderBean));
                        showMessageNum(mBtnChatNum, orderBean.imcount);//显示未读小红点个数
                    }else{
                        mBtnChat.setVisibility(View.GONE);
                    }

                    if (!orderBean.isEvaluated() && orderBean.orderType != 888) {//服务完成未评价
                        mAssessment.setVisibility(View.VISIBLE);
                        mAssessment.setOnClickListener(new TravelOnClickListener(orderBean));
                    } else {
                        mAssessment.setVisibility(View.GONE);
                    }
                } else {
                    if (!isShowAvartarLayout) {
                        mStatusLayout.setVisibility(View.GONE);
                        lineView.setVisibility(View.INVISIBLE);
                    }
                    mHeadLayout.setVisibility(View.GONE);
                    mAssessment.setVisibility(View.GONE);
                }
                break;
            case CANCELLED://已取消
            case REFUNDED://已退款
                mStatus.setTextColor(0xff7f7f7f);
                mPrice.setVisibility(View.GONE);
                mBtnPay.setVisibility(View.GONE);
                mAssessment.setVisibility(View.GONE);
                br_layout.setVisibility(View.GONE);
                if (orderBean.orderGuideInfo != null && !isShowAvartarLayout) {
                    mHeadLayout.setVisibility(View.VISIBLE);
                    mStatusLayout.setVisibility(View.VISIBLE);
                    lineView.setVisibility(View.VISIBLE);

                    mHeadTitle.setText(orderBean.getGuideName());
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        mHeadImg.setImageResource(R.mipmap.icon_avatar_guide);
                    } else {
                        Tools.showImage(mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.icon_avatar_guide);
                    }
                    mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));
                    mBtnChat.setVisibility(View.GONE);
                } else {
                    if (!isShowAvartarLayout) {
                        mStatusLayout.setVisibility(View.GONE);
                        lineView.setVisibility(View.INVISIBLE);
                    }
                    mHeadLayout.setVisibility(View.GONE);
                }
                break;
            case COMPLAINT://客诉处理中
                mStatus.setTextColor(0xff7f7f7f);
                mPrice.setVisibility(View.GONE);
                mBtnPay.setVisibility(View.GONE);
                mAssessment.setVisibility(View.GONE);
                br_layout.setVisibility(View.GONE);
                if (orderBean.orderGuideInfo != null && !isShowAvartarLayout) {
                    mHeadLayout.setVisibility(View.VISIBLE);
                    mStatusLayout.setVisibility(View.VISIBLE);
                    lineView.setVisibility(View.VISIBLE);

                    mHeadTitle.setText(orderBean.getGuideName());
                    if (TextUtils.isEmpty(orderBean.orderGuideInfo.guideAvatar)) {
                        mHeadImg.setImageResource(R.mipmap.icon_avatar_guide);
                    } else {
                        Tools.showImage(mHeadImg, orderBean.orderGuideInfo.guideAvatar, R.mipmap.icon_avatar_guide);
                    }
                    mHeadTitle.setOnClickListener(new TravelOnClickListener(orderBean));
                    mHeadImg.setOnClickListener(new TravelOnClickListener(orderBean));
                    mBtnChat.setVisibility(View.GONE);
                } else {
                    if (!isShowAvartarLayout) {
                        mStatusLayout.setVisibility(View.GONE);
                        lineView.setVisibility(View.INVISIBLE);
                    }
                    mHeadLayout.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
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
                    //立即支付，进入订单详情
                    OrderDetailActivity.Params params = new OrderDetailActivity.Params();
                    params.orderType = mOrderBean.orderType;
                    params.orderId = mOrderBean.orderNo;
                    intent = new Intent(v.getContext(), OrderDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra(Constants.PARAMS_SOURCE,getEventSource());
                    v.getContext().startActivity(intent);
                    SensorsUtils.onAppClick(getEventSource(),"立即支付","");
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
        RequestImChatId requestImChatId = new RequestImChatId(getContext(), UserEntity.getUser().getUserId(getContext()),"2",orderGuideInfo.guideID,"1");
        HttpRequestUtils.request(getContext(),requestImChatId,new HttpRequestListener(){
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
        NIMChatActivity.start(getContext(), guideId, imChatId,"行程");
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

    public String getEventSource() {
        return "行程";
    }
}
