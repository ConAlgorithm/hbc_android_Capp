package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.ExceptionErrorCode;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.ServerException;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestIMTokenUpdate;
import com.hugboga.custom.data.request.RequestOrderCancel;
import com.hugboga.custom.data.request.RequestOrderDetail;
import com.hugboga.custom.data.request.RequestPayNoByAli;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by admin on 2015/7/18.
 */
@ContentView(R.layout.fg_order)
public class FgOrder extends BaseFragment {

    public static final String KEY_ORDER_ID = "key_order_id";
    public static final StringBuffer SB = new StringBuffer();

    @ViewInject(R.id.header_right_btn)
    private ImageView rightBtn;

    @ViewInject(R.id.pay_scroll)
    private ScrollView scrollView;//滚动布局
    @ViewInject(R.id.pay_trip_switch_btn)
    private TextView orderInfoDetailBtn;//订单详情 用于收展 按钮
    @ViewInject(R.id.view_details)
    private View orderInfoDetail;//订单详情 用于收展
    @ViewInject(R.id.include_progress)
    private View headProgress;//进度条


    @ViewInject(R.id.order_hotel_phone_pick_layout)
    private View orderHotelPhoneLayoutPick;//酒店电话
    @ViewInject(R.id.txt_tel_pick)
    private TextView orderHotelPhonePick;//酒店电话
    @ViewInject(R.id.order_hotel_phone_send_layout)
    private View orderHotelPhoneLayoutSend;//酒店电话
    @ViewInject(R.id.txt_tel_send)
    private TextView orderHotelPhoneSend;//酒店电话
    @ViewInject(R.id.order_hotel_phone_daily_layout)
    private View orderHotelPhoneLayoutDaily;//酒店电话
    @ViewInject(R.id.txt_tel_daily)
    private TextView orderHotelPhoneDaily;//酒店电话
    @ViewInject(R.id.pay_time_tip)
    private TextView orderTimeTip;//下单时间超时提示
    @ViewInject(R.id.txt_time)
    private TextView orderServerTime;//服务时间
    @ViewInject(R.id.txt_time_desc)
    private TextView orderServerTimeDesc;//服务时间扩展 :(东京时间)
    @ViewInject(R.id.order_place_from)
    private TextView orderPlaceFrom;//起始地点
    @ViewInject(R.id.order_place_from_desc)
    private TextView orderPlaceFromDesc;//起始地点
    @ViewInject(R.id.order_place_to)
    private TextView orderPlaceTo;//目的地点
    @ViewInject(R.id.order_place_desc)
    private TextView orderPlaceToDesc;//目的地点扩展
    @ViewInject(R.id.txt_flight_value)
    private TextView orderFlightNo;//航班号
    @ViewInject(R.id.txt_flight_value_layout)
    private View orderFlightNoLayout;//航班号layout
    @ViewInject(R.id.order_car_info)
    private TextView orderCarInfo;//汽车信息 型号 座位
    @ViewInject(R.id.txt_order_sn_value)
    private TextView orderSN;//订单号

    //日租包车
    @ViewInject(R.id.order_daily_place)
    private TextView orderDailyPlace;//日租包车 起始地 至 目的地
    @ViewInject(R.id.order_daily_day)
    private TextView orderDailyDay;//日租包车 起始日期 至 结束日期(N天)
    @ViewInject(R.id.order_daily_day_detail)
    private TextView orderDailyDayDetail;//市内N天，市外1天
    @ViewInject(R.id.order_daily_pass_city_layout)
    private View orderDailyPassLayout;//日租包车 途径城市
    @ViewInject(R.id.txt_daily_pass_label)
    private TextView orderDailyPassLabel;//日租包车 途径城市label
    @ViewInject(R.id.txt_daily_pass_value)
    private TextView orderDailyPassValue;//日租包车 途径城市value
    @ViewInject(R.id.txt_daily_start_layout)
    private View orderDailyStartLayout;//日租包车 出发城市layout
    @ViewInject(R.id.txt_daily_start_value)
    private TextView orderDailyStart;//日租包车 出发地点
    @ViewInject(R.id.txt_daily_start_detail)
    private TextView orderDailyStartDetail;//日租包车 出发地点 详情
    @ViewInject(R.id.order_daily_tips)
    private View orderDailyTipsLayout;//日租包车 提示layout
    @ViewInject(R.id.txt_daily_tips)
    private TextView orderDailyTips;//日租包车 提示



    @ViewInject(R.id.order_commend_layout)
    private View orderCommendLayout;//精品线路 简介 layout
    @ViewInject(R.id.order_commend_content)
    private TextView orderCommendContent;//精品线路 简介
    @ViewInject(R.id.order_commend_date_layout)
    private View orderCommendDateLayout;//精品线路 服务时间 layout
    @ViewInject(R.id.order_commend_date_value)
    private TextView orderCommendDateValue;//精品线路 服务时间 value

    @ViewInject(R.id.order_contact_name)
    private TextView orderContactName;//联系人
    @ViewInject(R.id.txt_contact01)
    private TextView orderContactPhone;//联系电话
    @ViewInject(R.id.txt_contact02)
    private TextView orderContactPhone2;//联系电话2
    @ViewInject(R.id.txt_contact03)
    private TextView orderContactPhone3;//联系电话3

    @ViewInject(R.id.order_contact_layout2)
    private View orderContactPhoneLayout2;//联系电话2
    @ViewInject(R.id.order_contact_layout3)
    private View orderContactPhoneLayout3;//联系电话3
    @ViewInject(R.id.pay_placard_layout)
    private View orderPlacardLayout;//接机牌layout
    @ViewInject(R.id.order_placard_value)
    private TextView orderPlacard;//接机牌
    @ViewInject(R.id.order_passenger_num)
    private TextView orderPassengerNum;//乘客人数 2成人，2儿童
    @ViewInject(R.id.order_children_seat_layout)
    private RelativeLayout orderChildrenSeatLayout;//儿童座椅数
    @ViewInject(R.id.txt_children_seat_value)
    private TextView orderChildrenSeatValue;//儿童座椅数
    @ViewInject(R.id.order_visa_value)
    private TextView orderVisa;//签证情况
    @ViewInject(R.id.order_visa_value_layout)
    private View orderVisaLayout;//签证情况
    @ViewInject(R.id.order_remark)
    private TextView orderRemark;//备注

    @ViewInject(R.id.pay_change_trip)
    private TextView orderChangeTrip;//修改行程
    //价格
    @ViewInject(R.id.pay_total_title)
    private TextView orderPayTotalTitle;//订单费用title
    @ViewInject(R.id.pay_total_value)
    private TextView orderPayTotal;//订单费用
    @ViewInject(R.id.pay_should_layout)
    private View orderShouldLayout;//应付费用layout
    @ViewInject(R.id.pay_should_value)
    private TextView orderShouldTotal;//应付费用
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

    @ViewInject(R.id.pay_money_layout)
    private View payMoneyLayout;//支付成功金额layout
    @ViewInject(R.id.pay_success_layout)
    private View paySuccessLayout;//支付成功金额layout
    @ViewInject(R.id.order_pay_total_price)
    private TextView paySuccessPrice;//支付成功金额Value
    @ViewInject(R.id.order_pay_detail)
    private View paySuccessDetail;//支付成功金额查看更多
    @ViewInject(R.id.order_pay_point)
    private View paySuccessPoint;//支付成功金额提示
//    @ViewInject(R.id.pay_over_model_layout)
//    private View payOverModeLayout;//支付方式layout
//    @ViewInject(R.id.pay_over_mode_value)
//    private TextView payOverMode;//支付方式
    //退款费用
    @ViewInject(R.id.order_pay_cancel_layout)
    private View payCancelLayout;//退款费用DetailLayout
    @ViewInject(R.id.pay_cancel_value)
    private TextView payCancel;//退款费用
    //车导信息
    @ViewInject(R.id.order_guide_layout)
    private View orderGuideLayout;//车导layout
    @ViewInject(R.id.order_guide_empty)
    private View orderGuideEmptyLayout;//车导empty layout
    @ViewInject(R.id.order_guide_layout)
    private View orderGuideInfoLayout;//车导信息 layout
    @ViewInject(R.id.guide_head_img)
    private ImageView guideImg;//车导头像
    @ViewInject(R.id.order_guide_name)
    private TextView guideName;//车导姓名
    @ViewInject(R.id.order_guide_car_info)
    private TextView guideCarInfo;//车型
    @ViewInject(R.id.order_guide_star)
    private RatingBar guideRateStar;//评分
    @ViewInject(R.id.order_guide_star_value)
    private TextView guideRateStarValue;//评分值
    @ViewInject(R.id.guide_btn_chat)
    private View guideChat;//聊天
    @ViewInject(R.id.guide_btn_chat_num)
    private TextView guideChatNum;//聊天角标
    @ViewInject(R.id.guide_btn_call)
    private View guideCall;//车导电话
    @ViewInject(R.id.guide_btn_assessment)
    private TextView guideAssessment;//车导评价按钮

    //车导评价星级信息
    @ViewInject(R.id.order_evaluation_layout)
    View evaluationLayout;
    @ViewInject(R.id.assessment_label1)
    TextView evaluationLabel1;
    @ViewInject(R.id.assessment_ratingbar1)
    RatingBar evaluationRatingBar1;
    @ViewInject(R.id.assessment_ratingbar2)
    RatingBar evaluationRatingBar2;
    @ViewInject(R.id.assessment_ratingbar3)
    RatingBar evaluationRatingBar3;
    @ViewInject(R.id.assessment_comment)
    TextView evaluationCommentEditText;

    @ViewInject(R.id.order_info_tai)
    private View orderInfoTai;//泰国接机

    @ViewInject(R.id.submit_bottom)
    private View submitBottomLayout;//底部
    @ViewInject(R.id.bottom_bar_total_value)
    private TextView orderPayBottomPrice;//底部价格
    @ViewInject(R.id.bottom_bar_text)
    private TextView orderPayBottomPriceTitle;//底部价格title
    @ViewInject(R.id.bottom_bar_btn)
    private TextView bottomBtn;

    private boolean isShowDetail = false;//详细新是否展开
    private int payType = Constants.PAY_STATE_ALIPAY;//支付方式
    private OrderBean mOrderBean;//order 实体
    private String orderId;//订单号
    private double shouldPay;//需要支付
    private String hotelAreaCode;
    private String hotelPhone;

    CouponBean couponBean;
    private DialogUtil mDialogUtil;

    @Override
    protected void initHeader() {
        setProgressState(3);
        if (mOrderBean == null) return;

        if (mOrderBean.orderStatus.code >= OrderStatus.PAYSUCCESS.code && mOrderBean.orderStatus.code <= OrderStatus.SERVICING.code || (mOrderBean.orderStatus == OrderStatus.INITSTATE && mSourceFragment instanceof FgTravel)) {
            rightBtn.setImageResource(R.mipmap.top_more);
            rightBtn.setVisibility(View.VISIBLE);
        } else {
            rightBtn.setVisibility(View.GONE);
        }
        if(mBusinessType==-1){
            fgTitle.setText("订单详情");
        }
        if(mBusinessType!=3){
            fgTitle.setText(getString(Constants.TitleMap.get(mBusinessType)));
        }else{
            fgTitle.setText(getString(Constants.TitleMap.get(mGoodsType)));
        }

    }

    @Override
    protected void initView() {
        mDialogUtil = DialogUtil.getInstance(getActivity());
    }

    protected void inflateContent() {
        initHeader();
        ((TextView) getView().findViewById(R.id.bottom_bar_btn)).setText("立即支付");
        if (mOrderBean == null) return;
      /*  if (!(mSourceFragment instanceof FgSubmit)) {
            headProgress.setVisibility(View.GONE);
        } else {
            headProgress.setVisibility(View.VISIBLE);
        }*/
        //酒店电话
        if (!TextUtils.isEmpty(mOrderBean.serviceAddressTel)) {
            hotelAreaCode = mOrderBean.serviceAreaCode;
            hotelPhone = mOrderBean.serviceAddressTel;
            if (hotelAreaCode.indexOf("+") < 0) hotelAreaCode = "+" + hotelAreaCode;
        }
        String serviceTime = mOrderBean.serviceTime;
        try {
            serviceTime = DateUtils.getWeekStrFromDate(serviceTime);
        } catch (ParseException e) {}
        orderServerTime.setText(serviceTime);
        orderServerTimeDesc.setText("(当地时间)");
        orderServerTimeDesc.setVisibility(View.VISIBLE);
        submitBottomLayout.setVisibility(View.GONE);
        switch (mBusinessType) {
            case Constants.BUSINESS_TYPE_PICK:
                orderPlacardLayout.setVisibility(TextUtils.isEmpty(mOrderBean.brandSign) ? View.GONE : View.VISIBLE);
                orderPlacard.setText(mOrderBean.brandSign);
                orderPlaceFrom.setText(mOrderBean.startAddress);
                orderPlaceTo.setText(mOrderBean.destAddress);
                orderPlaceToDesc.setText(mOrderBean.destAddressDetail);
                orderFlightNo.setText(mOrderBean.flight);
                MLog.e("mOrderBean.serviceCityName = " + mOrderBean.serviceCityName);
                for (String airPort[] : Constants.TaiCityNames)
                    if (mOrderBean.serviceCityName.contains(airPort[0])) {
                        orderInfoTai.setVisibility(View.VISIBLE);
                        break;
                    }
                if (!TextUtils.isEmpty(hotelPhone)) {
                    orderHotelPhoneLayoutPick.setVisibility(View.VISIBLE);
                    orderHotelPhonePick.setText(hotelAreaCode + " " + hotelPhone);
                } else {
                    orderHotelPhoneLayoutPick.setVisibility(View.GONE);
                }
                orderChangeTrip.setTextColor(getResources().getColor(R.color.basic_pick_toolbar_color));
                orderChangeTrip.setBackgroundResource(R.drawable.btn_round_blue);
                break;
            case Constants.BUSINESS_TYPE_SEND:
                orderPlacardLayout.setVisibility(View.GONE);
                orderPlaceFromDesc.setVisibility(View.VISIBLE);
                orderPlaceToDesc.setVisibility(View.GONE);
                orderPlaceFrom.setText(mOrderBean.startAddress);
                orderPlaceTo.setText(mOrderBean.destAddress);
                orderPlaceFromDesc.setText(mOrderBean.startAddressDetail);
                orderVisaLayout.setVisibility(View.GONE);
                String flight = TextUtils.isEmpty(mOrderBean.flight) ? "暂未填写" : mOrderBean.flight;
                orderFlightNo.setText(flight);
                orderChangeTrip.setTextColor(getResources().getColor(R.color.basic_send_toolbar_color));
                orderChangeTrip.setBackgroundResource(R.drawable.btn_round_green);
                orderHotelPhoneLayoutSend.setVisibility(TextUtils.isEmpty(hotelPhone) ? View.GONE : View.VISIBLE);
                orderHotelPhoneSend.setText(hotelAreaCode + " " + hotelPhone);
                orderPayCheckInLayout.setVisibility(Double.isNaN(mOrderBean.orderPriceInfo.checkInPrice) ? View.GONE : View.VISIBLE);
                orderPayCheckInValue.setText(mOrderBean.orderPriceInfo.checkInPrice + "元");
                break;
            case Constants.BUSINESS_TYPE_DAILY:
                orderPlacardLayout.setVisibility(View.GONE);
                orderFlightNoLayout.setVisibility(View.GONE);
                orderVisaLayout.setVisibility(View.GONE);
                orderPlaceFrom.setVisibility(View.GONE);
                orderPlaceTo.setVisibility(View.GONE);
                orderPlaceToDesc.setVisibility(View.GONE);

                orderDailyPlace.setVisibility(View.VISIBLE);
                orderDailyDay.setVisibility(View.VISIBLE);
                orderDailyStartLayout.setVisibility(View.VISIBLE);
                String dailyPlace = mOrderBean.serviceCityName;
                if (mOrderBean.orderGoodsType!=3&&!TextUtils.isEmpty(mOrderBean.serviceEndCityName)) {
                    //不是市内包车，并且结束城市不为空
                    dailyPlace += " 至 " + mOrderBean.serviceEndCityName;
                }
                orderDailyPlace.setText(dailyPlace);
                  serviceTime = mOrderBean.serviceTime+ mOrderBean.serviceStartTime;
                try {
                    serviceTime = DateUtils.getWeekStrFromDate(serviceTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                orderServerTime.setText(serviceTime);
                if( mOrderBean.orderGoodsType==3){ //市内
                    if(mOrderBean.isHalfDaily==1){//半日包
                        orderDailyDay.setText(getString(R.string.order_daily_days_half, mOrderBean.serviceTime));
                    }else{
                        orderDailyDay.setText(getString(R.string.order_daily_days_local, mOrderBean.serviceTime, mOrderBean.serviceEndTime, mOrderBean.totalDays==null?0:mOrderBean.totalDays));
                    }
                    orderDailyPassLabel.setText("游玩计划");
                    if(!TextUtils.isEmpty(mOrderBean.journeyComment)){
                        orderDailyPassLayout.setVisibility(View.VISIBLE);
                        orderDailyPassValue.setText(mOrderBean.journeyComment.toString());
                    } else {
                        orderDailyPassLayout.setVisibility(View.GONE);
                    }
                }else {
                    orderDailyDay.setText(getString(R.string.order_daily_days, mOrderBean.serviceTime, mOrderBean.serviceEndTime, mOrderBean.totalDays==null?0:mOrderBean.totalDays));
                    orderDailyDayDetail.setText(getString(R.string.order_daily_days_detail,mOrderBean.serviceCityName,mOrderBean.inTownDays, mOrderBean.outTownDays));
                    orderDailyDayDetail.setVisibility(View.VISIBLE);
                    orderDailyPassLabel.setText("游玩城市");
                    if (mOrderBean.passByCity != null && mOrderBean.passByCity.size() > 2) {
                        orderDailyPassLayout.setVisibility(View.VISIBLE);
                        StringBuffer sb = new StringBuffer();
                        for (int i = 1; i < mOrderBean.passByCity.size()-1; i++) {
                            CityBean cityBean = mOrderBean.passByCity.get(i);
                            sb.append(cityBean.name).append("、");
                        }
                        orderDailyPassValue.setText(sb.toString());
                    } else {
                        orderDailyPassLayout.setVisibility(View.GONE);
                    }
                }
                if (!TextUtils.isEmpty(mOrderBean.startAddress)) {
                    orderDailyStart.setText(mOrderBean.startAddress);
                } else {
                    orderDailyStart.setText("暂未填写");
                }
                if (!TextUtils.isEmpty(mOrderBean.startAddressDetail)) {
                    orderDailyStartDetail.setText(mOrderBean.startAddressDetail);
                    orderDailyStartDetail.setVisibility(View.VISIBLE);
                } else {
                    orderDailyStartDetail.setVisibility(View.GONE);
                }
                if(!TextUtils.isEmpty(mOrderBean.dailyTips)){
                    orderDailyTipsLayout.setVisibility(View.VISIBLE);
                    orderDailyTips.setText(mOrderBean.dailyTips);
                }else{
                    orderDailyTipsLayout.setVisibility(View.GONE);
                }
                orderChangeTrip.setTextColor(getResources().getColor(R.color.basic_daily_toolbar_color));
                orderChangeTrip.setBackgroundResource(R.drawable.btn_round_orange);
                if (!TextUtils.isEmpty(hotelPhone)) {
                    orderHotelPhoneLayoutDaily.setVisibility(View.VISIBLE);
                    orderHotelPhoneDaily.setText(hotelAreaCode + " " + hotelPhone);
                } else {
                    orderHotelPhoneLayoutDaily.setVisibility(View.GONE);
                }
                break;
            case Constants.BUSINESS_TYPE_RENT:
                orderPlacardLayout.setVisibility(View.GONE);
                orderPlaceFrom.setText(mOrderBean.startAddress);
                orderPlaceFromDesc.setText(mOrderBean.startAddressDetail);
                orderPlaceTo.setText(mOrderBean.destAddress);
                orderPlaceToDesc.setText(mOrderBean.destAddressDetail);
                orderFlightNoLayout.setVisibility(View.GONE);
                orderVisaLayout.setVisibility(View.GONE);
                orderPlaceFromDesc.setVisibility(View.VISIBLE);
                orderChangeTrip.setTextColor(getResources().getColor(R.color.basic_rent_toolbar_color));
                orderChangeTrip.setBackgroundResource(R.drawable.btn_round_pick);
                break;

            case Constants.BUSINESS_TYPE_COMMEND:
                orderServerTimeDesc.setVisibility(View.GONE);
                orderPlacardLayout.setVisibility(View.GONE);
                orderFlightNoLayout.setVisibility(View.GONE);
                orderVisaLayout.setVisibility(View.GONE);
                orderPlaceFrom.setVisibility(View.GONE);
                orderPlaceTo.setVisibility(View.GONE);
                orderPlaceToDesc.setVisibility(View.GONE);

                orderDailyPlace.setVisibility(View.VISIBLE);
                orderCommendDateLayout.setVisibility(View.VISIBLE);
                orderDailyDay.setVisibility(View.VISIBLE);
                orderDailyStartLayout.setVisibility(View.VISIBLE);
                orderDailyPlace.setText(mOrderBean.serviceCityName + "出发");
                orderServerTime.setText(mOrderBean.lineSubject);
                if( mOrderBean.orderGoodsType==3){ //市内
                    if(mOrderBean.isHalfDaily==1){//半日包
                        orderDailyDay.setText(getString(R.string.order_daily_days_half, mOrderBean.serviceTime));
                    }else{
                        orderDailyDay.setText(getString(R.string.order_daily_days_local, mOrderBean.serviceTime, mOrderBean.serviceEndTime, mOrderBean.totalDays));
                    }
                    orderDailyPassLabel.setText("游玩计划");
                    if(!TextUtils.isEmpty(mOrderBean.journeyComment)){
                        orderDailyPassValue.setText(mOrderBean.journeyComment.toString());
                    } else {
                        orderDailyPassLayout.setVisibility(View.GONE);
                    }
                }else {
                    orderDailyDay.setText(getString(R.string.order_daily_days, mOrderBean.serviceTime, mOrderBean.serviceEndTime, mOrderBean.totalDays));
                    orderDailyDayDetail.setText(getString(R.string.order_daily_days_detail,mOrderBean.serviceCityName,mOrderBean.inTownDays, mOrderBean.outTownDays));
                    orderDailyDayDetail.setVisibility(View.VISIBLE);
                    orderDailyPassLabel.setText("游玩城市");
                    if (mOrderBean.passByCity != null && mOrderBean.passByCity.size() > 0) {
                        orderDailyPassLayout.setVisibility(View.VISIBLE);
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < mOrderBean.passByCity.size(); i++) {
                            CityBean cityBean = mOrderBean.passByCity.get(i);
                            sb.append(cityBean.name).append("、");
                        }
                        orderDailyPassValue.setText(sb.toString());
                    } else {
                        orderDailyPassLayout.setVisibility(View.GONE);
                    }
                }
                orderCommendDateValue.setText(mOrderBean.serviceTime + (TextUtils.isEmpty(mOrderBean.serviceStartTime) ? "" : mOrderBean.serviceStartTime));
                orderCommendLayout.setVisibility(View.VISIBLE);
                orderCommendContent.setText("\t\t\t" + mOrderBean.lineDescription);
                orderDailyPassLayout.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(mOrderBean.startAddress)&& !"null".equals(mOrderBean.startAddress)) {
                    orderDailyStart.setText(mOrderBean.startAddress);
                } else {
                    orderDailyStart.setText("暂未填写");
                }
                if(!TextUtils.isEmpty(mOrderBean.dailyTips)){
                    orderDailyTipsLayout.setVisibility(View.VISIBLE);
                    orderDailyTips.setText(mOrderBean.dailyTips);
                }else{
                    orderDailyTipsLayout.setVisibility(View.GONE);
                }
                orderChangeTrip.setTextColor(getResources().getColor(R.color.basic_commend_toolbar_color));
                orderChangeTrip.setBackgroundResource(R.drawable.btn_round_purple);
                orderChangeTrip.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(hotelPhone)) {
                    orderHotelPhoneLayoutDaily.setVisibility(View.VISIBLE);
                    orderHotelPhoneDaily.setText(hotelAreaCode + " " + hotelPhone);
                } else {
                    orderHotelPhoneLayoutDaily.setVisibility(View.GONE);
                }

                break;
        }

        //基本信息
        orderCarInfo.setText(mOrderBean.carDesc);
        orderSN.setText(mOrderBean.orderNo);
        orderPassengerNum.setText(mOrderBean.adult + "成人、" + mOrderBean.child + "儿童");
        if (Constants.VisaInfoMap.containsKey(mOrderBean.visa))
            orderVisa.setText(Constants.VisaInfoMap.get(mOrderBean.visa));
        if (!TextUtils.isEmpty(mOrderBean.memo)) {
            orderRemark.setText(mOrderBean.memo);
        }
        // 儿童座椅
        if (mOrderBean.childSeat != null && mOrderBean.childSeat.size() > 0) {
            orderChildrenSeatLayout.setVisibility(View.VISIBLE);
            StringBuffer sb = new StringBuffer();
            boolean isFirst = true;
            for (String childrenSeat : mOrderBean.childSeat) {
                String[] seats = childrenSeat.split("-");
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append("\n");
                }
                sb.append(Constants.ChildrenSeatMap.get(Integer.valueOf(seats[0])) + "X" + seats[1]);
            }
            orderChildrenSeatValue.setText(sb.toString());
        }else{
            orderChildrenSeatLayout.setVisibility(View.GONE);
        }
        //价格
        orderPayTotal.setText(mOrderBean.orderPriceInfo.orderPrice + "元");//订单价格
        orderShouldLayout.setVisibility(Double.isNaN(mOrderBean.orderPriceInfo.shouldPay) ? View.GONE : View.VISIBLE);
        orderShouldTotal.setText(mOrderBean.orderPriceInfo.shouldPay + "元");//应付费用
        shouldPay = mOrderBean.orderPriceInfo.shouldPay;
        //优惠券金额
        if (mOrderBean.orderCoupon != null && mOrderBean.orderCoupon.price != null) {
            orderPayCouponsLayout.setVisibility(View.VISIBLE);
            orderPayCoupons.setText(mOrderBean.orderCoupon.price);
        } else {
            //没有使用优惠券，则直接不显示
            orderPayCouponsLayout.setVisibility(View.GONE);
        }
        if (mOrderBean.orderStatus == OrderStatus.INITSTATE && mOrderBean.orderCoupon != null && mOrderBean.orderCoupon.price != null) {
            shouldPay = mOrderBean.orderCoupon.actualPrice;
        } else {
            shouldPay = mOrderBean.orderPriceInfo.shouldPay;
        }
        if (shouldPay < 0) shouldPay = 0;
        orderPayActualLayout.setVisibility(Double.isNaN(mOrderBean.orderPriceInfo.actualPay) ? View.GONE : View.VISIBLE);
        orderActualPay.setText(mOrderBean.orderPriceInfo.actualPay + "元");//实付费用
        orderPayBottomPrice.setText("" + shouldPay);
        paySuccessPrice.setText(String.valueOf(mOrderBean.orderPriceInfo.actualPay));
        orderPayTotalTitle.setText(Constants.TitleMap2.get(mBusinessType));
        paySuccessPoint.setVisibility((mOrderBean.additionIsRead == 0)? View.VISIBLE: View.GONE);

        //退款费用
        if (Double.isNaN(mOrderBean.orderPriceInfo.refundPrice)) {
            payCancelLayout.setVisibility(View.GONE);
        } else {
            payCancel.setText(mOrderBean.orderPriceInfo.refundPrice + "元");
        }
        //车导
        if (mOrderBean.orderGuideInfo != null && mOrderBean.orderGuideInfo.guideID != null) {
            orderGuideInfoLayout.setVisibility(View.VISIBLE);
            ImageOptions options = new ImageOptions.Builder()
                    .setLoadingDrawableId(R.mipmap.journey_head_portrait)
                    .setFailureDrawableId(R.mipmap.journey_head_portrait)
                    .setCircular(true)
                    .build();
            x.image().bind(guideImg,mOrderBean.orderGuideInfo.guideAvatar,options);
            guideName.setText(mOrderBean.orderGuideInfo.guideName + "/");
            guideCarInfo.setText(mOrderBean.orderGuideInfo.car);
            guideRateStar.setVisibility(View.VISIBLE);
            guideRateStar.setRating((float) mOrderBean.orderGuideInfo.guideStarLevel);
            guideRateStarValue.setText("" + mOrderBean.orderGuideInfo.guideStarLevel);
            if (mOrderBean.canChat && !TextUtils.isEmpty(mOrderBean.imToken)) {
                showMessageNum(guideChatNum, mOrderBean.imcount, mOrderBean.imToken, "G" + mOrderBean.orderGuideInfo.guideID);
            }
        }
        initStatusView();//根据状态调整信息
        initContactView();//联系方式
        initBottomView();//底部支付
        if(mOrderBean.orderType== Constants.BUSINESS_TYPE_COMMEND){
            orderChangeTrip.setVisibility(View.GONE);
        }
    }

    /**
     * 底部的支付
     */
    private void initBottomView() {
        if (mOrderBean.orderStatus == OrderStatus.INITSTATE) {
            submitBottomLayout.setVisibility(View.VISIBLE);
        } else {
            submitBottomLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 联系人
     */
    private void initContactView() {
        String areaCode;
        orderContactName.setText(mOrderBean.contactName);
        if (mOrderBean.contact != null && mOrderBean.contact.size() > 0) {
            areaCode = mOrderBean.contact.get(0).areaCode;
            if (!TextUtils.isEmpty(areaCode) && areaCode.indexOf("+") < 0) {
                areaCode = "+" + areaCode;
            }
            orderContactPhone.setText(areaCode + " " + mOrderBean.contact.get(0).tel);
            if (mOrderBean.contact.size() > 1) {
                areaCode = mOrderBean.contact.get(1).areaCode;
                if (areaCode.indexOf("+") < 0) {
                    areaCode = "+" + areaCode;
                }
                orderContactPhoneLayout2.setVisibility(TextUtils.isEmpty(mOrderBean.contact.get(1).tel) ? View.GONE : View.VISIBLE);
                orderContactPhone2.setText(areaCode + " " + mOrderBean.contact.get(1).tel);
                if (mOrderBean.contact.size() > 2) {
                    areaCode = mOrderBean.contact.get(2).areaCode;
                    if (areaCode.indexOf("+") < 0) {
                        areaCode = "+" + areaCode;
                    }
                    orderContactPhoneLayout3.setVisibility(TextUtils.isEmpty(mOrderBean.contact.get(2).tel) ? View.GONE : View.VISIBLE);
                    orderContactPhone3.setText(areaCode + " " + mOrderBean.contact.get(2).tel);
                } else {
                    orderContactPhoneLayout3.setVisibility(View.GONE);
                }
            } else {
                orderContactPhoneLayout2.setVisibility(View.GONE);
                orderContactPhoneLayout3.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 车导信息
     */
    private void initStatusView() {
        orderGuideLayout.setVisibility(View.VISIBLE);
        payCancelLayout.setVisibility(View.GONE);
        switch (mOrderBean.orderStatus) {
            case INITSTATE: //"等待支付";
                orderInfoTai.setVisibility(View.GONE);
                orderGuideLayout.setVisibility(View.GONE);
                orderTimeTip.setVisibility(View.VISIBLE);
                orderChangeTrip.setVisibility(View.VISIBLE);
                orderPayCouponsLayout.setVisibility(View.VISIBLE);
                orderPayCouponsArrow.setVisibility(View.VISIBLE);
                orderPayActualLayout.setVisibility(View.GONE);
                paySuccessLayout.setVisibility(View.GONE);
                if (mOrderBean.orderCoupon==null||mOrderBean.orderCoupon.price == null) {
                    orderPayCoupons.setTextColor(getResources().getColor(R.color.basic_gold));
                    orderPayCoupons.setText("添加");
                }
                orderTimeTip.setText(getString(R.string.pay_time_tip, mOrderBean.payDeadTime));
                break;
            case PAYSUCCESS://"预订成功";
                orderGuideEmptyLayout.setVisibility(View.VISIBLE);
                orderGuideInfoLayout.setVisibility(View.GONE);
                orderChangeTrip.setVisibility(View.VISIBLE);
                paySuccessDetail.setVisibility(View.GONE);
                break;
            case AGREE://"车导已接单";
                guideChat.setVisibility(View.VISIBLE);
                guideCall.setVisibility(View.VISIBLE);
                orderChangeTrip.setVisibility(View.VISIBLE);
                payMoneyLayout.setVisibility(View.GONE);
                break;
            case ARRIVED: //"车导已到达";
                guideChat.setVisibility(View.VISIBLE);
                guideCall.setVisibility(View.VISIBLE);
                orderChangeTrip.setVisibility(View.VISIBLE);
                payMoneyLayout.setVisibility(View.GONE);
                break;
            case SERVICING:  //"服务中";
                guideChat.setVisibility(View.VISIBLE);
                guideCall.setVisibility(View.VISIBLE);
                orderChangeTrip.setVisibility(View.VISIBLE);
                payMoneyLayout.setVisibility(View.GONE);
                break;
            case NOT_EVALUATED://"未评价";
                orderInfoTai.setVisibility(View.GONE);
                orderGuideEmptyLayout.setVisibility(View.GONE);
                guideAssessment.setVisibility(View.VISIBLE);
                guideAssessment.setBackgroundResource(Constants.BtnBg.get(mOrderBean.orderType));
                guideChat.setVisibility(View.VISIBLE);
                guideCall.setVisibility(View.GONE);
                orderChangeTrip.setVisibility(View.GONE);
                payMoneyLayout.setVisibility(View.GONE);
                break;
            case COMPLETE:// "已完成";
                orderInfoTai.setVisibility(View.GONE);
                payMoneyLayout.setVisibility(View.GONE);
                if (mOrderBean.assessmentBean != null) {
                    evaluationLayout.setVisibility(View.VISIBLE);
                    if (mOrderBean.orderType == 1 || mOrderBean.orderType == 2 || mOrderBean.orderType == 4) {
                        //接机|送机
                        evaluationLabel1.setText("准时程度");
                    }
                    evaluationRatingBar1.setRating((float) mOrderBean.assessmentBean.sceneryNarrate);
                    evaluationRatingBar2.setRating((float) mOrderBean.assessmentBean.serviceAttitude);
                    evaluationRatingBar3.setRating((float) mOrderBean.assessmentBean.routeFamiliar);
                    evaluationCommentEditText.setText(mOrderBean.assessmentBean.content);
                } else {
                    evaluationLayout.setVisibility(View.GONE);
                }
                orderInfoTai.setVisibility(View.GONE);
                orderGuideEmptyLayout.setVisibility(View.GONE);
                guideCall.setVisibility(View.GONE);
                orderChangeTrip.setVisibility(View.GONE);
                paySuccessLayout.setVisibility(View.VISIBLE);
                payCancelLayout.setVisibility(View.GONE);
                break;
            case REFUNDED:// "已退款";
                payCancelLayout.setVisibility(View.VISIBLE);
            case COMPLAINT:// "客诉处理中";
            case CANCELLED:// "已取消";
                orderInfoTai.setVisibility(View.GONE);
                orderGuideEmptyLayout.setVisibility(View.GONE);
//                guideAssessment.setVisibility(View.GONE);
                guideCall.setVisibility(View.GONE);
                orderChangeTrip.setVisibility(View.GONE);
                paySuccessLayout.setVisibility(View.GONE);
                break;
            default:// "其他";
                break;
        }
        if (mOrderBean.orderGuideInfo == null || mOrderBean.orderGuideInfo.guideID == null) {
            orderGuideInfoLayout.setVisibility(View.GONE);
        } else {
            orderGuideEmptyLayout.setVisibility(View.GONE);
        }
    }


    @Override
    protected Callback.Cancelable requestData() {
        bottomBtn.setBackgroundColor(getResources().getColor(Constants.BgColors.get(mBusinessType)));
        orderId = getArguments().getString(KEY_ORDER_ID);
        RequestOrderDetail request = new RequestOrderDetail(getActivity(),orderId);
        return requestData(request);
    }

    private void requestPayNo() {
        if (mOrderBean == null) return;
        RequestPayNoByAli request = new RequestPayNoByAli(getActivity(),mOrderBean.orderNo, shouldPay, this.payType, mOrderBean.orderCoupon.couponID);
        requestData(request);
    }

    private void cancelOrder(String orderID, double cancelPrice) {
        if (cancelPrice < 0) cancelPrice = 0;
        RequestOrderCancel request = new RequestOrderCancel(getActivity(),orderID, cancelPrice, "");
        requestData(request);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest parser) {
        if (parser instanceof RequestOrderDetail) {
            RequestOrderDetail mParser = (RequestOrderDetail) parser;
            mOrderBean = mParser.getData();
            mBusinessType = mOrderBean.orderType;
            inflateContent();
            flushCoupon();
        } else if (parser instanceof RequestPayNoByAli) {
            RequestPayNoByAli mParser = (RequestPayNoByAli) parser;
            payByAlipay(mParser.getData());
        } else if (parser instanceof RequestOrderCancel) {
            DialogUtil dialogUtil = DialogUtil.getInstance(getActivity());
            dialogUtil.showCustomDialog("取消订单成功", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    bringToFront(FgTravel.class,new Bundle());
                    finish();
                }
            });
            notifyOrderList(FgTravel.TYPE_ORDER_CANCEL, true, false, true);
        } else if(parser instanceof RequestIMTokenUpdate){
            RequestIMTokenUpdate mParser = (RequestIMTokenUpdate) parser;
            mOrderBean.imToken = mParser.getData();
            gotoChatView(mOrderBean.imToken, "G" + mOrderBean.orderGuideInfo.guideID);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest parser) {
        if (errorInfo.state == ExceptionErrorCode.ERROR_CODE_SERVER) {
            ServerException exception = (ServerException) errorInfo.exception;
            if (exception.getCode() == 2) {
                mDialogUtil.showLoadingDialog();
                mHandler.sendEmptyMessageDelayed(1, 3000);
                return;
            }

        }
        super.onDataRequestError(errorInfo, parser);
    }

    //支付宝回调
    Handler mAlipayHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            MLog.e(this + "handleMessage：" + msg.obj);
          /*  switch (msg.what) {
                case PayResult.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        mDialogUtil.showLoadingDialog();
                        mHandler.sendEmptyMessageDelayed(1, 3000);
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        Toast.makeText(getActivity(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                    } else {
                    }
                    break;
                }
                case PayResult.SDK_CHECK_FLAG: {
                    Toast.makeText(getActivity(), "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }*/
            inflateContent();
        }
    };
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mDialogUtil.dismissLoadingDialog();
            finish();
            Bundle bundle = new Bundle();
            int overPrice = 0;
            bundle.putInt("pay", overPrice);
            bundle.putInt("orderType", mOrderBean.orderType);
            bundle.putString(KEY_ORDER_ID, mOrderBean.orderNo);
            bundle.putString("from", mSourceFragment.getClass().getSimpleName());
//            startFragment(new FgPaySuccess(), bundle);
            notifyOrderList(FgTravel.TYPE_ORDER_RUNNING, true, false, false);
        }
    };


    @Event({R.id.pay_trip_switch_btn,
            R.id.pay_change_trip,
            R.id.pay_type_alipay_layout,
            R.id.pay_type_wechat_layout,
            R.id.pay_type_bank_layout,
            R.id.order_hotel_phone_pick_layout,
            R.id.order_hotel_phone_send_layout,
            R.id.order_hotel_phone_daily_layout,
            R.id.pay_coupons_layout,
            R.id.bottom_bar_btn,
            R.id.guide_btn_chat,
            R.id.guide_btn_call,
            R.id.guide_btn_assessment,
            R.id.head_btn_right,
            R.id.order_pay_detail,
            R.id.order_info_tai
    })
    private void onClickView(View view) {
        if (mOrderBean == null) return;
        Bundle bundle;
        switch (view.getId()) {
            case R.id.head_btn_right:
            case R.id.head_text_right:
                if (mOrderBean.orderStatus.code >= OrderStatus.PAYSUCCESS.code && mOrderBean.orderStatus.code <= OrderStatus.SERVICING.code) {
                    showPopupWindow(view);
                } else {
                    if (mOrderBean.orderStatus == OrderStatus.INITSTATE && mSourceFragment.getClass().getSimpleName().equals(FgTravel.class.getSimpleName())) {
                        showPopupWindow(view);
                    } else {
                        mDialogUtil.showCallDialog();
                    }
                }
                break;
            case R.id.head_btn_left:
                onKeyBack();
                break;
            case R.id.pay_trip_switch_btn://展开详情
                switchDetailView();
                break;
            case R.id.pay_change_trip://修改行程
                bundle = new Bundle();
//                bundle.putSerializable(FgChangeTrip.KEY_ORDER_BEAN, mOrderBean);
//                startFragment(new FgChangeTrip(), bundle);
                break;
            case R.id.pay_type_alipay_layout://
                chosePayType(Constants.PAY_STATE_ALIPAY);
                break;
            case R.id.pay_type_wechat_layout:
                chosePayType(Constants.PAY_STATE_WECHAT);
                break;
            case R.id.pay_type_bank_layout:
                chosePayType(Constants.PAY_STATE_BANK);
                break;
            case R.id.pay_coupons_layout:
                //优惠券
                if (mOrderBean.orderStatus == OrderStatus.INITSTATE) {
                    bundle = new Bundle();
                    bundle.putString(FgCoupon.ORDER_ID, mOrderBean.orderNo);
                    bundle.putString(FgCoupon.KEY_COUPON_ID, mOrderBean.orderCoupon.couponID);
                    startFragment(new FgCoupon(), bundle);
                }
                break;
            case R.id.order_hotel_phone_pick_layout:
            case R.id.order_hotel_phone_send_layout:
            case R.id.order_hotel_phone_daily_layout:
                //拨打酒店电话
                String phone = hotelAreaCode + " " + hotelPhone;
                PhoneInfo.CallDial(getActivity(), phone);
                break;
            case R.id.order_pay_detail:
                //增项费用查看详情
                paySuccessPoint.setVisibility(View.GONE);
                bundle = new Bundle();
      /*          bundle.putString(FgOrderOverPrice.KEY_ORDER_NO, mOrderBean.orderNo);
                bundle.putInt(FgOrderOverPrice.KEY_ORDER_IS_READ, mOrderBean.additionIsRead);
                bundle.putSerializable(FgOrderOverPrice.KEY_ORDER_PRICE, mOrderBean.orderPriceInfo);
                bundle.putSerializable(FgOrderOverPrice.KEY_ORDER_COUPON, mOrderBean.orderCoupon);
                startFragment(new FgOrderOverPrice(), bundle);*/
                break;
            case R.id.guide_btn_chat:
                //车导聊天
                gotoChatView(mOrderBean.imToken, "G" + mOrderBean.orderGuideInfo.guideID);
//                gotoChatView(mOrderBean.imToken, "S114997482130");
                break;
            case R.id.guide_btn_call:
                //车导电话
                PhoneInfo.CallDial(getActivity(), mOrderBean.orderGuideInfo.guideTel);
                break;
            case R.id.guide_btn_assessment:
                //车导评价
                if (mOrderBean == null || mOrderBean.orderGuideInfo == null) return;
                bundle = new Bundle();
/*                bundle.putString(FgAssessment.GUIDE_ID, mOrderBean.orderGuideInfo.guideID);
                bundle.putString(FgAssessment.ORDER_ID, mOrderBean.orderNo);
                bundle.putInt(FgAssessment.ORDER_TYPE, mOrderBean.orderType);
                bundle.putString(FgAssessment.GUIDE_NAME, mOrderBean.orderGuideInfo.guideName);
                startFragment(new FgAssessment(), bundle);*/
                break;
            case R.id.bottom_bar_btn:
//                notifyOrderList();
                requestPayNo();
                break;
            case R.id.order_info_tai:
                // 泰国接机
                for (String airPort[] : Constants.TaiCityNames)
                    if (mOrderBean.serviceCityName.contains(airPort[0])) {
                        FgWebInfo fgWebInfo = new FgWebInfo();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString(FgWebInfo.WEB_URL, airPort[1]);
                        fgWebInfo.setArguments(bundle1);
                        startFragment(fgWebInfo);
                        break;
                    }
                break;
        }
    }

    @Override
    protected int getBusinessType() {
        return super.getBusinessType();
    }

    private void switchDetailView() {
        if (isShowDetail) {
            orderInfoDetail.setVisibility(View.GONE);
            scrollView.scrollTo(0, 0);
            isShowDetail = false;
            orderInfoDetailBtn.setText("展开详情");
            orderInfoDetailBtn.setSelected(false);
        } else {
            orderInfoDetail.setVisibility(View.VISIBLE);
            isShowDetail = true;
            orderInfoDetailBtn.setText("收起详情");
            orderInfoDetailBtn.setSelected(true);
        }
    }

    private int payTypeRadioIds[][] = {{Constants.PAY_STATE_ALIPAY, R.id.pay_type_alipay_radio},
            {Constants.PAY_STATE_WECHAT, R.id.pay_type_wechat_radio},
            {Constants.PAY_STATE_BANK, R.id.pay_type_bank_radio},};

    private void chosePayType(int type) {
        this.payType = type;
        for (int[] ids : payTypeRadioIds) {
            if (ids[0] == type) {
                ((RadioButton) getView().findViewById(ids[1])).setChecked(true);
            } else {
                ((RadioButton) getView().findViewById(ids[1])).setChecked(false);
            }
        }
    }

    private int requestIMTokenCount = 0;
    /**
     * 开始聊天
     *
     * @param token
     * @param chatId 聊天对象的Id
     */
    private void gotoChatView(String token, final String chatId) {
        mDialogUtil.showLoadingDialog(true);
        MLog.e("gotoChatView "+token);

        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                MLog.e("onTokenIncorrect");
                if (requestIMTokenCount < 3) {
                    requestIMTokenCount++;
                    requestIMTokenUpdate(mOrderBean.orderNo);
                } else {
                    mDialogUtil.dismissLoadingDialog();
                }
            }

            @Override
            public void onSuccess(String s) {
                guideChatNum.setVisibility(View.GONE);
                mDialogUtil.dismissLoadingDialog();
                //刷新IM头像
                String userIcon = UserEntity.getUser().getAvatar(getActivity());
                userIcon = userIcon.replaceFirst("https:", "http:");
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(s, UserEntity.getUser().getNickname(getActivity()), Uri.parse(userIcon)));
//                IMChatActivity.orderId = chatId;
//                IMChatActivity.ids = s;
                RongIM.getInstance().startPrivateChat(getActivity(), chatId, "title");
                userIcon = mOrderBean.orderGuideInfo.guideAvatar;
                userIcon = userIcon.replaceFirst("https:", "http:");
                UserInfo peerUser = new UserInfo(chatId, mOrderBean.orderGuideInfo.guideName, Uri.parse(userIcon));
                RongContext.getInstance().getUserInfoCache().put(chatId, peerUser);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MLog.e("IMChatActivity onError=" + errorCode.getValue() + " " + errorCode.toString());
                if (requestIMTokenCount < 3) {
                    //您需要更换 Token
                    requestIMTokenCount++;
                    requestIMTokenUpdate(mOrderBean.orderNo);
                } else {
                    mDialogUtil.dismissLoadingDialog();
                }
            }
        });
    }

    /**
     * 设置聊一聊未读个数小红点
     *
     * @param chatNumTextView
     * @param token
     * @param guideId
     */
    private void showMessageNum(final TextView chatNumTextView, Integer imcount, String token, final String guideId) {
        if (imcount > 0) {
            chatNumTextView.setVisibility(View.VISIBLE);
            chatNumTextView.setText(String.valueOf(imcount));
        } else {
            chatNumTextView.setVisibility(View.GONE);
        }
    }

    private void notifyOrderList(int jumpType, boolean refreshRunning, boolean refreshFinish, boolean refreshCancel) {
        Intent intent = new Intent();
        intent.setAction(FgTravel.FILTER_FLUSH);
        intent.putExtra(FgTravel.JUMP_TYPE, jumpType);
        intent.putExtra(FgTravel.REFRESH_RUNNING, refreshRunning);
        intent.putExtra(FgTravel.REFRESH_FINISH, refreshFinish);
        intent.putExtra(FgTravel.REFRESH_CANCEL, refreshCancel);
        getActivity().sendBroadcast(intent);
    }

    private void payByAlipay(final String payInfo) {
        MLog.e("payInfo=" + payInfo);
        if (!TextUtils.isEmpty(payInfo))
            if ("couppay".equals(payInfo)) {
                //优惠券0元支付
                mDialogUtil.showLoadingDialog();
                mHandler.sendEmptyMessageDelayed(1, 3000);
            } else {//正常支付
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 构造PayTask 对象
                       /* PayTask alipay = new PayTask(getActivity());
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(payInfo);

                        Message msg = new Message();
                        msg.what = PayResult.SDK_PAY_FLAG;
                        msg.obj = result;
                        mAlipayHandler.sendMessage(msg);*/
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        String from = bundle.getString(KEY_FRAGMENT_NAME);
       /* if (FgChangeTrip.class.getSimpleName().equals(from)) {
            requestDate();
        } else if (FgOrderCancel.class.getSimpleName().equals(from)) {
            requestDate();
        } else if (FgCoupon.class.getSimpleName().equals(from)) {
            couponBean = (CouponBean) bundle.getSerializable(FgCoupon.KEY_COUPON);
            flushCoupon();
        }*/
    }

    /**
     * 刷新优惠券信息
     */
    private void flushCoupon() {
        if (couponBean != null) {
            String oldCouponId = mOrderBean.orderCoupon.couponID;
            String newCouponId = couponBean.couponID;
            double total = mOrderBean.orderCoupon.actualPrice;
            if (total < 0) {
                Toast.makeText(getActivity(), "优惠券异常", Toast.LENGTH_LONG).show();
                return;
            }
            if (newCouponId != null && !newCouponId.equals(oldCouponId)) {
                orderPayCouponsArrow.setVisibility(View.VISIBLE);
                mOrderBean.orderCoupon = couponBean;
                orderPayCoupons.setText(couponBean.price);
                orderPayCoupons.setTextColor(0xff727272);
                shouldPay = couponBean.actualPrice;
            } else {
                orderPayCouponsArrow.setVisibility(View.GONE);
                mOrderBean.orderCoupon = new CouponBean();
                orderPayCoupons.setTextColor(getResources().getColor(R.color.basic_gold));
                orderPayCoupons.setText("添加");
                shouldPay = mOrderBean.orderPriceInfo.shouldPay;
            }
            orderPayBottomPrice.setText("" + shouldPay);
        }
    }

    /**
     * 右上角的菜单，取消订单 联系客服
     *
     * @param parent 点击的按钮
     */
    public void showPopupWindow(View parent) {
        PopupMenu popup = new PopupMenu(getActivity(), parent);
        popup.getMenuInflater()
                .inflate(R.menu.menu_order_more, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.order_cancel) {
                    //如果此订单不能取消，直接进行提示
                    if (!mOrderBean.cancelable && !TextUtils.isEmpty(mOrderBean.cancelText)) {
                        mDialogUtil = DialogUtil.getInstance(getActivity());
                        mDialogUtil.showCustomDialog(mOrderBean.cancelText);
                        return true;
                    }
                    mDialogUtil = DialogUtil.getInstance(getActivity());
                    String tip = "";
                    if (mOrderBean.orderStatus == OrderStatus.INITSTATE) {
                        tip = getString(R.string.order_cancel_tip);
                    } else {
                        tip = mOrderBean.cancelTip;
                    }
                    mDialogUtil.showCustomDialog(getString(R.string.app_name), tip, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mOrderBean.orderStatus == OrderStatus.INITSTATE) {
                                cancelOrder(mOrderBean.orderNo, 0);
                            } else {
                                finish();
                                Bundle bundle = new Bundle();
//                                bundle.putSerializable(FgOrderCancel.KEY_ORDER, mOrderBean);
//                                startFragment(new FgOrderCancel(), bundle);
                            }
                        }
                    }, "返回", null);
                } else {
                    mDialogUtil.showCallDialog();
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    public boolean onBackPressed() {
        onKeyBack();
        return true;
    }

    private void onKeyBack() {
        MLog.e("onKeyBack " + mSourceFragment);
       /* if (mSourceFragment != null && mSourceFragment instanceof  FgSubmit) {
            mDialogUtil.showCustomDialog(getString(R.string.app_name), getString(R.string.order_cancel_pay), "返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FgOrder.this.finish();
                }
            }, "继续支付", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        } else {*/
            finish();
//        }

    }

    /**
     * 获取IM token
     */
    private void requestIMToken(String orderId) {
        RequestIMTokenUpdate parser = new RequestIMTokenUpdate(getActivity(),orderId);
        requestData(parser);
      /*  mHttpUtils = new HttpRequestUtils(getActivity(),parser,this);
        mHttpUtils.isShowLoading = false;
        mHttpUtils.execute();*/
    }

    /**
     * update token
     */
    private void requestIMTokenUpdate(String orderId) {
        RequestIMTokenUpdate parser = new RequestIMTokenUpdate(getActivity(),orderId);
        requestData(parser);
    }

    /**
     * 处理下 时间
     * @param dateStr
     * @return
     */
    private String splitDateStr(String dateStr){
        if(dateStr==null)return null;
        String[] dateArray = dateStr.split(" ");
        if(dateArray!=null&&dateArray.length>1)return dateArray[0];
        return dateStr;
    }

}
