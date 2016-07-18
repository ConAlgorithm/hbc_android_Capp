package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.ExceptionErrorCode;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.DailyBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderContact;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestSubmitBase;
import com.hugboga.custom.data.request.RequestSubmitDaily;
import com.hugboga.custom.data.request.RequestSubmitPick;
import com.hugboga.custom.data.request.RequestSubmitRent;
import com.hugboga.custom.data.request.RequestSubmitSend;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by admin on 2015/7/18.
 */
@ContentView(R.layout.fg_submit)
public class FgSubmit extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    @ViewInject(R.id.submit_air_no)
    private TextView airNo;//航班号
    @ViewInject(R.id.submit_down_time)
    private TextView downTime;//时间
    @ViewInject(R.id.submit_down_time_desc)
    private TextView timeDesc;//时间备注(东京时间)
    @ViewInject(R.id.submit_air_port)
    private TextView airportName;//机场名
    @ViewInject(R.id.submit_hotel)
    private TextView arrivalHotel;//酒店
    @ViewInject(R.id.submit_hotel_desc)
    private TextView arrivalHotelDesc;//酒店描述
    @ViewInject(R.id.submit_car)
    private TextView carInfo;//车信息
    @ViewInject(R.id.submit_car_desc)
    private TextView carInfoDesc;//车信息描述
    @ViewInject(R.id.submit_daily_city)
    private TextView dailyCityName;//包车 城市
    @ViewInject(R.id.submit_daily_days)
    private TextView dailyCityDays;//包车天数


    @ViewInject(R.id.submit_connect_name)
    private TextView connectName;//联系人
    @ViewInject(R.id.submit_areacode)
    private TextView areaCode;//区号
    @ViewInject(R.id.submit_phone)
    private TextView connectPhone;//电话
    @ViewInject(R.id.submit_areacode2)
    private TextView areaCode2;//区号2
    @ViewInject(R.id.submit_phone2)
    private TextView connectPhone2;//电话2
    @ViewInject(R.id.submit_areacode3)
    private TextView areaCode3;//区号3
    @ViewInject(R.id.submit_phone3)
    private TextView connectPhone3;//电话3
    @ViewInject(R.id.submit_phone_layout2)
    private View phoneLayout2;//电话布局2
    @ViewInject(R.id.submit_phone_layout3)
    private View phoneLayout3;//电话布局3
    @ViewInject(R.id.submit_phone_add)
    private TextView phoneAdd;//增加电话

    private TextView[] areaCodeArray;//区号数组
    private TextView[] areaPhoneArray;//电话数组

    @ViewInject(R.id.submit_adult_1)
    private TextView adultCount;//成人数
    @ViewInject(R.id.submit_child_1)
    private TextView childCount;//儿童数

    @ViewInject(R.id.submit_child_seat_layout)
    private LinearLayout childSeatLayout;//儿童座椅
    @ViewInject(R.id.submit_pick_name)
    private TextView pickName;//接机牌
    @ViewInject(R.id.submit_pick_name_layout)
    private View pickNameLayout;//接机牌

    @ViewInject(R.id.submit_hotel_phone_star)
    private TextView hotelPhoneStar;//酒店电话星
    @ViewInject(R.id.submit_hotel_phone_layout)
    private View hotelPhoneLayout;//酒店电话
    @ViewInject(R.id.submit_hotel_phone_areacode)
    private TextView hotelPhoneAreaCode;//酒店电话
    @ViewInject(R.id.submit_hotel_phone)
    private TextView hotelPhone;//酒店电话
    @ViewInject(R.id.submit_hotel_phone_tip)
    private TextView hotelPhoneTip;//酒店电话提示
    @ViewInject(R.id.submit_pick_visa)
    private TextView pickVisa;//签证
    @ViewInject(R.id.submit_pick_visa_layout)
    private View pickVisaLayout;//签证
    @ViewInject(R.id.submit_date_time)
    private TextView serverDateTime;//包车 上车时间
    @ViewInject(R.id.submit_date_time_layout)
    private View serverDateTimeLayout;//包车 上车时间
    @ViewInject(R.id.submit_start_place)
    private TextView serverPlace;//包车 上车地点
    @ViewInject(R.id.submit_start_place_layout)
    private View serverPlaceLayout;//包车 上车地点
    @ViewInject(R.id.submit_daily_pass_city_layout)
    private View dailyPassCityLayout;//包车 途径城市layout
    @ViewInject(R.id.submit_daily_city_value)
    private EditText dailyPassCityValue;//包车 途径城市 editText
    @ViewInject(R.id.submit_daily_city_arrow)
    private View dailyPassCityArrow;//包车 途径城市 Arrow
    @ViewInject(R.id.submit_flight_no)
    private TextView flightNo;//航班号,送机填写
    @ViewInject(R.id.submit_flight_no_layout)
    private View flightNOLayout;//航班号,送机填写
    @ViewInject(R.id.submit_remark)
    private TextView remark;//备注
    @ViewInject(R.id.submit_transfer_layout)
    private View transferCheckInLayout;//送机 check in
    @ViewInject(R.id.submit_transfer_value)
    private TextView transferCheckInValue;//送机 check in
    @ViewInject(R.id.submit_transfer_check)
    private CheckBox transferCheckInCheck;//送机 check in

    @ViewInject(R.id.submit_bottom)
    private View bottomBtnLayout;//提交按钮layout
    @ViewInject(R.id.bottom_bar_btn)
    private TextView bottomBtn;//提交按钮
    @ViewInject(R.id.bottom_bar_total_value)
    private TextView bottomTotal;//总价

    private FlightBean flightBean;//航班信息 接机
    private AirPort airportBean;//机场    送机机
    private DailyBean dailyBean;// 日租 包车
    private PoiBean startBean;//起始地 次租
    private PoiBean arrivalBean;//达到目的地

    private CarBean carBean;//车
    private String serverTime;//服务时间
    private double distance;//距离
    private int expectedCompTime;//服务时长
    private int cityId;

    private int phoneLayoutCount = 1;//联系方式数量
    private int adult = 0;//成人数量
    private int child = 0;//孩子数量
    private int visa = 0;//签证情况。1-落地签；2-国内签；0-未确定
    private PopupWindow popupWindow;//弹窗
    private boolean isCheckIn = true;//送机是否 check in
    private boolean needChildrenSeat = false;//是否需要儿童座椅
    private boolean needBanner = true;//是否可以展示接机牌
    private ArrayList<CityBean> passCityList = new ArrayList<>();
    private DialogUtil mDialogUtil;

    protected void initView() {
        areaCodeArray = new TextView[]{areaCode, areaCode2, areaCode3};
        areaPhoneArray = new TextView[]{connectPhone, connectPhone2, connectPhone3};
        String hotelAreaCode = "";
        switch (mBusinessType) {
            case Constants.BUSINESS_TYPE_PICK:
                airNo.setText(String.format(getString(R.string.submit_airno), flightBean.flightNo));
                downTime.setText(flightBean.arrDate + " " + flightBean.arrivalTime);
                timeDesc.setText("(" + flightBean.arrivalAirport.cityName + "时间)");
                timeDesc.setText("(当地时间)");
                airportName.setText(flightBean.arrivalAirport.airportName);
                arrivalHotel.setText(arrivalBean.placeName);
                arrivalHotelDesc.setText(arrivalBean.placeDetail);
                flightNOLayout.setVisibility(View.GONE);
                pickVisaLayout.setVisibility(flightBean.arrivalAirport.visaSwitch ? View.VISIBLE : View.GONE);
                pickNameLayout.setVisibility(needBanner ? View.VISIBLE : View.GONE);
                hotelAreaCode = "+" + flightBean.arrivalAirport.areaCode;
                break;
            case Constants.BUSINESS_TYPE_SEND:
                airNo.setText("用车时间");
                downTime.setText(serverTime);
                timeDesc.setText("(当地时间)");
                airportName.setText(airportBean.airportName);
                arrivalHotel.setText(arrivalBean.placeName);
                arrivalHotelDesc.setText(arrivalBean.placeDetail);
                pickNameLayout.setVisibility(View.GONE);
                flightNOLayout.setVisibility(View.VISIBLE);
                transferCheckInLayout.setVisibility(View.VISIBLE);
                transferCheckInValue.setText(getString(R.string.transfer_check_in, carBean.checkInPrice));
                transferCheckInCheck.setOnCheckedChangeListener(this);
                hotelAreaCode = "+" + airportBean.areaCode;
                break;
            case Constants.BUSINESS_TYPE_DAILY:

                serverTime = "08:00";
                airNo.setText(dailyBean.startDate + " 至 " + dailyBean.endDate);
                timeDesc.setVisibility(View.GONE);
                serverDateTime.setHint(dailyBean.startDate + " " + serverTime + "(当地时间)");
                hotelAreaCode = "+" + dailyBean.areaCode;
                dailyPassCityLayout.setVisibility(View.VISIBLE);
                if (dailyBean.oneCityTravel == 1) {//市内包车
                    dailyPassCityArrow.setVisibility(View.GONE);
                    dailyPassCityValue.setClickable(false);
                    dailyPassCityLayout.setClickable(false);
                    dailyCityName.setText(dailyBean.startCityName);
                    if (dailyBean.isHalfDay) {
                        dailyCityDays.setText("半日包");
                        airNo.setText(dailyBean.startDate);
                    } else {
                        dailyCityDays.setText(dailyBean.totalDay + "天");
                    }
                } else {//跨城市
                    dailyPassCityArrow.setVisibility(View.VISIBLE);
                    dailyPassCityValue.setClickable(true);
                    dailyPassCityValue.setFocusable(false);
                    dailyPassCityLayout.setClickable(true);
                    dailyCityName.setText(getString(R.string.order_daily_city_name, dailyBean.startCityName, dailyBean.terminalCityName));
                    dailyCityDays.setText(getString(R.string.order_daily_days_out_tower, dailyBean.totalDay, dailyBean.inTownDays, dailyBean.outTownDays));
                }
                dailyCityName.setVisibility(View.VISIBLE);
                dailyCityDays.setVisibility(View.VISIBLE);
                airportName.setVisibility(View.GONE);
                arrivalHotel.setVisibility(View.GONE);
                downTime.setVisibility(View.GONE);
                arrivalHotelDesc.setVisibility(View.GONE);
                pickNameLayout.setVisibility(View.GONE);
                flightNOLayout.setVisibility(View.GONE);
                serverDateTimeLayout.setVisibility(View.VISIBLE);
                serverPlaceLayout.setVisibility(View.VISIBLE);
                break;
            case Constants.BUSINESS_TYPE_RENT:
                airNo.setText("用车时间");
//                downTime.setText(serverTime + "("+cityBean.name+"时间)");
                downTime.setText(serverTime + "(当地时间)");
//              timeDesc.setText("("+airportBean.serviceCityName+"时间)");
                airportName.setText(startBean.placeName);
                arrivalHotel.setText(arrivalBean.placeName);
                arrivalHotelDesc.setText(arrivalBean.placeDetail);
                pickNameLayout.setVisibility(View.GONE);
                hotelPhoneLayout.setVisibility(View.GONE);
                flightNOLayout.setVisibility(View.GONE);
                hotelPhoneTip.setVisibility(View.GONE);
                break;
        }
        hotelPhoneAreaCode.setText(hotelAreaCode);
        String areaCodeValue = UserEntity.getUser().getAreaCode(getActivity());
        if (TextUtils.isEmpty(areaCodeValue)) areaCodeValue = "86";
        areaCode.setText("+" + areaCodeValue);
        connectPhone.setText(UserEntity.getUser().getPhone(getActivity()));
        bottomTotal.setText("" + (carBean.originalPrice + carBean.checkInPrice));
        bottomBtn.setText("提交订单");
//        bottomBtn.setBackgroundColor(getResources().getColor(Constants.BgColors.get(mBusinessType)));
        carInfo.setText(carBean.desc);
        Integer[] carInfo = Constants.CarSeatInfoMap.get(carBean.carSeat);
        carInfoDesc.setText("(" + String.format(getString(R.string.submit_car_info), carInfo[0], carInfo[1]) + ")");
        bottomBtnLayout.setVisibility(View.VISIBLE);

    }

    @Override
    protected Callback.Cancelable requestData() {
        setProgressState(2);
        return null;
    }

    @Override
    protected void inflateContent() {
    }

    @Event({R.id.bottom_bar_btn,
            R.id.submit_phone_add,
            R.id.submit_adult_sub,
            R.id.submit_adult_plus,
            R.id.submit_child_sub,
            R.id.submit_child_plus,
            R.id.submit_areacode,
            R.id.submit_areacode2,
            R.id.submit_areacode3,
            R.id.submit_phone_del_2,
            R.id.submit_phone_del_3,
            R.id.submit_hotel_phone_areacode,
            R.id.submit_pick_visa_layout,
            R.id.submit_date_time_layout,
            R.id.submit_date_time,
            R.id.submit_order_cancel,
            R.id.submit_order_tip,
            R.id.submit_flight_no_layout,
            R.id.submit_start_place_layout,
            R.id.submit_daily_city_value,
//            R.id.popup_order_children_ok,
//            R.id.popup_order_children_cancel,
//            R.id.popup_order_children_item_plus,
//            R.id.popup_order_children_item_sub,
//            R.id.submit_daily_pass_city_layout,
    })
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.bottom_bar_btn:
                    submitData(view);
                break;
            case R.id.submit_phone_add:
                if (phoneLayoutCount == 1) {
                    phoneLayoutCount++;
                    if (phoneLayout2.isShown()) {
                        phoneLayout3.setVisibility(View.VISIBLE);
                    } else {
                        phoneLayout2.setVisibility(View.VISIBLE);
                    }
                } else if (phoneLayoutCount == 2) {
                    phoneLayoutCount++;
                    phoneLayout2.setVisibility(View.VISIBLE);
                    phoneLayout3.setVisibility(View.VISIBLE);
                    phoneAdd.setTextColor(getActivity().getResources().getColor(R.color.basic_gray));
                }
                break;
            case R.id.submit_adult_sub:
                if (adult <= 0) break;
                adultCount.setText(String.format(getString(R.string.submit_adult), --adult));
                break;
            case R.id.submit_adult_plus:
                adultCount.setText(String.format(getString(R.string.submit_adult), ++adult));
                break;
            case R.id.submit_child_sub:
                if (needChildrenSeat) {
                    showChildrenSeat();
                    break;
                }
                if (child <= 0) break;
                childCount.setText(String.format(getString(R.string.submit_child), --child));
                break;
            case R.id.submit_child_plus:
                if (needChildrenSeat) {
                    showChildrenSeat();
                    break;
                }
                childCount.setText(String.format(getString(R.string.submit_child), ++child));
                break;
//            case R.id.popup_order_children_item_sub:
//                int index = (int) view.getTag();
//                if (childrenSeatNumbers[index] > 0) {
//                    popupItemNumber[index].setText(String.valueOf(--childrenSeatNumbers[index]));
//                }
//                break;
//            case R.id.popup_order_children_item_plus:
//                index = (int) view.getTag();
//                if (childrenSeatNumbers[index] < 5) {
//                    popupItemNumber[index].setText(String.valueOf(++childrenSeatNumbers[index]));
//                }
//                break;
//            case R.id.popup_order_children_cancel:
//                popupWindow.dismiss();
//                break;
//            case R.id.popup_order_children_ok:
//                popupWindow.dismiss();
//                child = 0;
//                for (int number : childrenSeatNumbers) {
//                    child += number;
//                }
//                childCount.setText(String.format(getString(R.string.submit_child), child));
//                inflateChildrenSeat();
//                break;

            case R.id.submit_areacode:
            case R.id.submit_areacode2:
            case R.id.submit_areacode3:
            case R.id.submit_hotel_phone_areacode:
                FgChooseCountry chooseCountry = new FgChooseCountry();
                Bundle bundle = new Bundle();
                bundle.putInt("airportCode", view.getId());
                startFragment(chooseCountry, bundle);
                break;
            case R.id.submit_phone_del_2:
                areaCode2.setText("");
                connectPhone2.setText("");
                phoneLayout2.setVisibility(View.GONE);
                if(phoneLayoutCount == 3){
                    phoneAdd.setTextColor(getActivity().getResources().getColor(R.color.basic_daily_color));
                }
                phoneLayoutCount--;
                break;
            case R.id.submit_phone_del_3:
                areaCode3.setText("");
                connectPhone3.setText("");
                phoneLayout3.setVisibility(View.GONE);
                if(phoneLayoutCount == 3){
                    phoneAdd.setTextColor(getActivity().getResources().getColor(R.color.basic_daily_color));
                }
                phoneLayoutCount--;
                break;
            case R.id.submit_date_time_layout:
            case R.id.submit_date_time:
                showTimeSelect();
                break;
            case R.id.submit_pick_visa_layout:
                showSelectVisa();
                break;
            case R.id.submit_order_cancel:
                toWebInfo(UrlLibs.H5_CANCEL);
                break;
            case R.id.submit_order_tip:
                toWebInfo(UrlLibs.H5_NOTICE);
                break;
            case R.id.submit_flight_no_layout:
                startFragment(new FgPickFlight());
                break;
            case R.id.submit_start_place_layout:
                FgPoiSearch fg = new FgPoiSearch();
                bundle = new Bundle();
                bundle.putInt(FgPoiSearch.KEY_CITY_ID, dailyBean.startCityID);
                bundle.putString(FgPoiSearch.KEY_LOCATION, dailyBean.startLocation);
                startFragment(fg, bundle);
                break;
            case R.id.submit_daily_city_value:
            case R.id.submit_daily_pass_city_layout:
                if (dailyBean.oneCityTravel == 1) return;
                bundle = new Bundle();
                bundle.putInt(FgChooseCity.KEY_CITY_ID, dailyBean.startCityID);
                bundle.putSerializable(FgChooseCity.KEY_CITY_LIST_CHOSEN, passCityList);
                bundle.putInt(FgChooseCity.KEY_CHOOSE_TYPE, FgChooseCity.KEY_TYPE_MULTIPLY);
                ArrayList<Integer> exceptId = new ArrayList<>();
                exceptId.add(dailyBean.startCityID);
                exceptId.add(dailyBean.terminalCityID);
                bundle.putString("source","下单过程中");
                bundle.putSerializable(FgChooseCity.KEY_CITY_EXCEPT_ID_LIST, exceptId);
                startFragment(new FgChooseCity(), bundle);
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("source", "下单过程中");
                MobclickAgent.onEvent(getActivity(), "search_trigger", map);
                break;
        }
    }

    //设置儿童座椅的内容
    private void inflateChildrenSeat() {
        childSeatLayout.setVisibility(child > 0 ? View.VISIBLE : View.GONE);
        childSeatLayout.removeAllViews();
        for (int i = 0; i < childrenSeatNumbers.length; i++) {
            if (childrenSeatNumbers[i] <= 0) continue;
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_children_seat, null);
            TextView label = (TextView) view.findViewById(R.id.item_children_seat_label);
            TextView value = (TextView) view.findViewById(R.id.item_children_seat_value);
            label.setText(Constants.ChildrenSeatMap.get(i + 1));
            value.setText("x" + childrenSeatNumbers[i]);
            childSeatLayout.addView(view);
        }
    }

    /**
     * 打开网页
     *
     * @param url
     */
    private void toWebInfo(String url) {
        FgWebInfo fgWebInfo = new FgWebInfo();
        Bundle bundle = new Bundle();
        bundle.putString(FgWebInfo.WEB_URL, url);
        fgWebInfo.setArguments(bundle);
        startFragment(fgWebInfo);
    }

    /**
     * 提交数据
     */
    private void submitData(View btn) {
        OrderBean orderBean = new OrderBean();//订单
        String contactName = connectName.getText().toString().trim();
        if (TextUtils.isEmpty(contactName)) {
            showTip("请填写联系人姓名");
            return;
        }
        orderBean.contact = new ArrayList<OrderContact>();
        OrderContact contact;
        for (int i = 0; i < phoneLayoutCount; i++) {
            contact = new OrderContact();
            contact.areaCode = areaCodeArray[i].getText().toString();
            contact.tel = areaPhoneArray[i].getText().toString();
            if (i == 0) {
                if (TextUtils.isEmpty(contact.areaCode)) {
                    showTip("请选择区号");
                    return;
                } else if (TextUtils.isEmpty(contact.tel)) {
                    areaPhoneArray[i].requestFocus();
                    showTip("请填写联系电话");
                    return;
                }
            } else if (!TextUtils.isEmpty(contact.tel)) {
                if (TextUtils.isEmpty(contact.areaCode)) {
                    areaPhoneArray[i].requestFocus();
                    showTip("请选择区号" + (i + 1));
                    return;
                }
            } else {
                continue;
            }
            contact.areaCode = contact.areaCode.replace("+", "");
            orderBean.contact.add(contact);
        }
        for (int i = 0; i < orderBean.contact.size(); i++) {
            for (int j = i + 1; j < orderBean.contact.size(); j++) {
                if ((orderBean.contact.get(i).areaCode + orderBean.contact.get(i).tel)
                        .equals(orderBean.contact.get(j).areaCode + orderBean.contact.get(j).tel)) {
                    showTip("联系电话重复了");
                    return;
                }
            }
        }
        if (adult == 0) {
            adultCount.requestFocus();
            showTip("请选择成人数量");
            return;
        }
        //客户端做check
        if (adult + child >= carBean.carSeat) {
            mDialogUtil.showCustomDialog("您选择的出行人数超出车型所能容纳的人数,请重新填写出行人数");
            return;
        }

        String brandSign = pickName.getText().toString().trim();
        if (mBusinessType == Constants.BUSINESS_TYPE_PICK && needBanner && TextUtils.isEmpty(brandSign)) {
            showTip("请填写接机牌姓名");
            return;
        }

        String hotelPhoneStr = hotelPhone.getText().toString().trim();
        String hotelPhoneAreaCodeStr = hotelPhoneAreaCode.getText().toString().trim();
        if (!TextUtils.isEmpty(hotelPhoneStr) && TextUtils.isEmpty(hotelPhoneAreaCodeStr)) {
            showTip("请填选择酒店电话区号");
            return;
        }
        if(!UserEntity.getUser().isLogin(getActivity())){
            Bundle bundle = new Bundle();//用于统计
            HashMap<String,String> map = new HashMap<String,String>();//用于统计
            switch (mBusinessType) {
                case Constants.BUSINESS_TYPE_PICK:
                    bundle.putString("source","接机订单");
                    map.put("source", "接机订单");
                case Constants.BUSINESS_TYPE_SEND:
                    bundle.putString("source","送机订单");
                    map.put("source", "送机订单");
                case Constants.BUSINESS_TYPE_DAILY:
                    bundle.putString("source","按天包车");
                    map.put("source", "按天包车");
                case Constants.BUSINESS_TYPE_RENT:
                    bundle.putString("source","单次接送");
                    map.put("source", "单次接送");
            }
            MobclickAgent.onEvent(getActivity(), "login_trigger", map);

            startFragment(new FgLogin(), bundle);
            return;
        }
        hotelPhoneAreaCodeStr = hotelPhoneAreaCodeStr.replace("+", "");
        orderBean.serviceAreaCode = hotelPhoneAreaCodeStr;
        orderBean.serviceAddressTel = hotelPhoneStr;
        orderBean.expectedCompTime = expectedCompTime+"";
        orderBean.urgentFlag = getArguments().getInt(FgCar.KEY_URGENT_FLAG);


        orderBean.orderType = getBusinessType();
        orderBean.carType = carBean.carType;
        orderBean.seatCategory = carBean.carSeat;
        orderBean.carDesc = carBean.desc;
        orderBean.contactName = contactName;
        orderBean.adult = adult;
        orderBean.child = child;
        orderBean.visa = visa;
        orderBean.memo = remark.getText().toString().trim();
        orderBean.orderPrice = isCheckIn ? carBean.originalPrice + carBean.checkInPrice : carBean.originalPrice;
        orderBean.checkInPrice = isCheckIn ? carBean.checkInPrice : null;
        orderBean.priceMark = carBean.pricemark;
        orderBean.distance = "" + distance;
        orderBean.childSeat = new ArrayList<>();
        for (int i = 0; i < childrenSeatNumbers.length; i++) {
            if (childrenSeatNumbers[i] != 0)
                orderBean.childSeat.add((i + 1) + "-" + childrenSeatNumbers[i]);
        }

        RequestSubmitBase requestSubmitBase = null;
        Bundle bundle = new Bundle();//用于统计
        HashMap<String,String> map = new HashMap<String,String>();//用于统计
        String type = "";

        switch (mBusinessType) {
            case Constants.BUSINESS_TYPE_PICK:
                orderBean.flight = flightBean.flightNo;
                orderBean.flightBean = flightBean;
                orderBean.startAddress = flightBean.arrivalAirport.airportName;
                //出发地，到达地经纬度
                orderBean.startLocation = flightBean.arrivalAirport.location;
                orderBean.terminalLocation = arrivalBean.location;

                orderBean.destAddress = arrivalBean.placeName;
                orderBean.destAddressDetail = arrivalBean.placeDetail;

                orderBean.serviceCityId = flightBean.arrivalAirport.cityId;
                orderBean.serviceTime = flightBean.arrDate + " " + flightBean.arrivalTime + ":00";
                orderBean.brandSign = brandSign;
                requestSubmitBase = new RequestSubmitPick(getActivity(), orderBean);
                type = "submitorder_pickup";
                break;
            case Constants.BUSINESS_TYPE_SEND:
                orderBean.startAddress = arrivalBean.placeName;
                orderBean.startAddressDetail = arrivalBean.placeDetail;
                //出发地，到达地经纬度
                orderBean.startLocation = arrivalBean.location;
                orderBean.terminalLocation = airportBean.location;

                orderBean.destAddress = airportBean.airportName;
                orderBean.flightAirportCode = airportBean.airportCode;
                orderBean.serviceCityId = airportBean.cityId;
                orderBean.serviceTime = serverTime + ":00";
                orderBean.serviceAreaCode = hotelPhoneAreaCodeStr;
                orderBean.serviceAddressTel = hotelPhoneStr;
                if (flightBean != null) {
                    orderBean.flightBean = flightBean;
                    orderBean.flight = flightBean.flightNo;
                }
                requestSubmitBase = new RequestSubmitSend(getActivity(), orderBean);
                type = "submitorder_dropoff";
                break;
            case Constants.BUSINESS_TYPE_DAILY:
                if (startBean != null) {
                    orderBean.startAddress = startBean.placeName;
                    orderBean.startAddressDetail = startBean.placeDetail;
                }
                orderBean.serviceCityId = dailyBean.startCityID;
                orderBean.serviceCityName = dailyBean.startCityName;
                //出发地，到达地经纬度
                orderBean.startLocation = dailyBean.startLocation;
                orderBean.terminalLocation = dailyBean.terminalLocation;

                orderBean.destAddress = dailyBean.terminalCityName;
                orderBean.serviceEndCityid = dailyBean.terminalCityID;
                orderBean.serviceEndCityName = dailyBean.terminalCityName;
                orderBean.serviceStartTime = serverTime + ":00";
                orderBean.serviceTime = dailyBean.startDate;
                orderBean.serviceEndTime = dailyBean.endDate;
//                orderBean.passByCityID = dailyBean.passByCityID;
                orderBean.totalDays = dailyBean.totalDay;
                orderBean.stayCityListStr = dailyBean.stayCity;
                orderBean.oneCityTravel = dailyBean.oneCityTravel;
                orderBean.isHalfDaily = dailyBean.isHalfDay ? 1 : 0;
                orderBean.inTownDays = dailyBean.inTownDays;
                orderBean.outTownDays = dailyBean.outTownDays;
                if (dailyBean.oneCityTravel == 1) {
                    orderBean.journeyComment = dailyPassCityValue.getText().toString();
                }
                orderBean.stayCityListStr = getPassCityStr();
                requestSubmitBase = new RequestSubmitDaily(getActivity(), orderBean);
                type = "submitorder_oneday";
                break;
            case Constants.BUSINESS_TYPE_RENT:
                orderBean.startAddress = startBean.placeName;
                orderBean.startAddressDetail = startBean.placeDetail;
                //出发地，到达地经纬度
                orderBean.startLocation = startBean.location;
                orderBean.terminalLocation = arrivalBean.location;

                orderBean.destAddress = arrivalBean.placeName;
                orderBean.destAddressDetail = arrivalBean.placeDetail;
                orderBean.serviceCityId = cityId;
                orderBean.serviceTime = serverTime + ":00";
//                orderBean.city = serverTime;
                requestSubmitBase = new RequestSubmitRent(getActivity(), orderBean);
                type = "submitorder_oneway";
                break;
            case Constants.BUSINESS_TYPE_COMMEND:
                type = "submitorder_route";
                break;
        }

        HttpRequestUtils.request(getActivity(), requestSubmitBase, this, btn);

        map.put("carstyle", carBean.desc);
        map.put("source", source);
//        map.put("guestcount", adult + child + "");
        MobclickAgent.onEventValue(getActivity(), type, map, orderBean.orderPrice);
    }

    /**
     * 获取途径城市String<br/>
     * 兼容上板加上天数0天
     * 123-0,2-0
     * cityId-days,cityId-days
     *
     * @return
     */
    private String getPassCityStr() {
        String passCity = "";
        passCity += dailyBean.startCityID + "-0";
        for (CityBean city : passCityList) {
            passCity += "," + city.cityId + "-0";
        }
        passCity += "," + dailyBean.terminalCityID + "-0";
        return passCity;
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestSubmitBase) {
            RequestSubmitBase mParser = (RequestSubmitBase) request;
//            if (TextUtils.isEmpty(mParser.getData())) {
//                Toast.makeText(getActivity(), "下单失败", Toast.LENGTH_LONG).show();
//                return;
//            }
//            goToOrder(mParser.getData());
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (errorInfo.state == ExceptionErrorCode.ERROR_CODE_NET_TIMEOUT) {
            mDialogUtil.showCustomDialog("网络不稳定，请重试");
        } else {
            super.onDataRequestError(errorInfo, request);
        }
    }

    private void goToOrder(String orderId) {
        EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 2));
//        Bundle bundle = new Bundle();
//        bundle.putString(FgOrder.KEY_ORDER_ID, orderId);
//        bundle.putString("source",source);
//        bringToFront(FgTravel.class, bundle);
//        bundle.putBoolean("needShowAlert",true);
//        //下单后再返回,直接到主页
//        startFragment(new FgOrder(), bundle);
        FgOrderDetail.Params params = new FgOrderDetail.Params();
        params.orderId = orderId;
        params.source = source;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS_DATA, params);
        startFragment(new FgOrderDetail(), bundle);
    }

    private void showSelectVisa() {
        String[] visaInfo = new String[Constants.VisaInfoMap.size() - 1];
        for (int i = 0; i < Constants.VisaInfoMap.size() - 1; i++) {
            visaInfo[i] = Constants.VisaInfoMap.get(i + 1);
        }
        new AlertDialog.Builder(getActivity())
                .setTitle("签证选择")
                .setItems(visaInfo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        visa = which + 1;
                        pickVisa.setText(Constants.VisaInfoMap.get(which + 1));
                    }
                })
                .show();
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        String fragmentName = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCountry.class.getSimpleName().equals(fragmentName)) {
            int viewId = bundle.getInt("airportCode");
            TextView codeTv = (TextView) getView().findViewById(viewId);
            if (codeTv != null) {
                String areaCode = bundle.getString(FgChooseCountry.KEY_COUNTRY_CODE);
                codeTv.setText("+" + areaCode);
            }
        } else if (FgPickFlight.class.getSimpleName().equals(fragmentName)) {
            FlightBean bean = (FlightBean) bundle.getSerializable(FgPickFlight.KEY_AIRPORT);
            if (mBusinessType == Constants.BUSINESS_TYPE_SEND && bean != null) {
                MLog.e("bean.depAirportCode=" + bean.depAirportCode + " airportBean.airportCode=" + airportBean.airportCode);
                if (!bean.depAirportCode.equals(airportBean.airportCode)) {
                    Toast.makeText(getActivity(), "请选择与送达机场相符的航班", Toast.LENGTH_LONG).show();
                }
                flightBean = bean;
                String flightInfoStr = flightBean.flightNo + " ";
                flightInfoStr += flightBean.depAirport.cityName + "-" + flightBean.arrivalAirport.cityName;
                flightInfoStr += "\n当地时间" + flightBean.arrDate + " " + flightBean.depTime + "起飞";
                flightNo.setText(flightInfoStr);
            }
        } else if (FgPoiSearch.class.getSimpleName().equals(fragmentName)) {
            startBean = (PoiBean) bundle.getSerializable(FgPoiSearch.KEY_ARRIVAL);
            serverPlace.setText(startBean.placeName);
        } else if (FgChooseCity.class.getSimpleName().equals(fragmentName)) {
            passCityList = (ArrayList<CityBean>) bundle.getSerializable(FgChooseCity.KEY_CITY_LIST);
            String passCityStr = "";
            boolean isFirst = true;
            for (CityBean city : passCityList) {
                if (isFirst) {
                    passCityStr += city.name;
                    isFirst = false;
                } else {
                    passCityStr += ("、" + city.name);
                }
            }
            dailyPassCityValue.setText(passCityStr);
        }
    }

    @Override
    protected void initHeader() {
        fgRightBtn.setVisibility(View.VISIBLE);
        bottomBtnLayout.setVisibility(View.VISIBLE);
        fgTitle.setText(getString(Constants.TitleMap.get(mGoodsType)));
        mDialogUtil = DialogUtil.getInstance(getActivity());

        Bundle bundle = getArguments();
        flightBean = (FlightBean) bundle.getSerializable(FgCar.KEY_FLIGHT);
        airportBean = (AirPort) bundle.getSerializable(FgCar.KEY_AIRPORT);
        startBean = (PoiBean) bundle.getSerializable(FgCar.KEY_START);
        arrivalBean = (PoiBean) bundle.getSerializable(FgCar.KEY_ARRIVAL);
        dailyBean = (DailyBean) bundle.getSerializable(FgCar.KEY_DAILY);
        carBean = (CarBean) bundle.getSerializable(FgCar.KEY_CAR);
        serverTime = bundle.getString(FgCar.KEY_TIME);
        cityId = bundle.getInt(FgCar.KEY_CITY_ID);
        distance = bundle.getDouble(FgCar.KEY_DISTANCE);
        expectedCompTime = bundle.getInt(FgCar.KEY_COM_TIME);
        needChildrenSeat = bundle.getBoolean(FgCar.KEY_NEED_CHILDREN_SEAT);
        needBanner = bundle.getBoolean(FgCar.KEY_NEED_BANNER);
        source = bundle.getString("source");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isCheckIn = isChecked;
        bottomTotal.setText("" + (isCheckIn ? carBean.originalPrice + carBean.checkInPrice : carBean.originalPrice));
    }


    public void showTimeSelect() {
        Calendar cal = Calendar.getInstance();
        MyTimePickerDialogListener myTimePickerDialog = new MyTimePickerDialogListener();
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(myTimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        datePickerDialog.show(this.getActivity().getFragmentManager(), "TimePickerDialog");                //显示日期设置对话框
    }

    /*
         * Function  :       自定义MyDatePickerDialog类，用于实现DatePickerDialog.OnDateSetListener接口，
         *                           当点击日期设置对话框中的“设置”按钮时触发该接口方法
         */
    class MyTimePickerDialogListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            String hour = String.format("%02d", hourOfDay);
            String minuteStr = String.format("%02d", minute);
            serverTime = hour + ":" + minuteStr;
            serverDateTime.setText(dailyBean.startDate + " " + serverTime + "(当地时间)");
        }
    }

    //popup  儿童座椅
    private TextView[] popupItemName = new TextView[4];
    private TextView[] popupItemNumber = new TextView[4];
    private TextView[] popupItemSub = new TextView[4];
    private TextView[] popupItemPlus = new TextView[4];

    private int[] childrenSeatNumbers = new int[4];


    private void showChildrenSeat() {
        collapseSoftInputMethod();
        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_order_childrenseat, null);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            initPopupView(popupView);
        }
        //设置后进行展示
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);

    }

    private void initPopupView(View popupView) {
        popupView.findViewById(R.id.popup_order_children_cancel).setOnClickListener(this);
        popupView.findViewById(R.id.popup_order_children_ok).setOnClickListener(this);

        initPopupItem(popupView, R.id.popup_order_children_item_1, 0);
        initPopupItem(popupView, R.id.popup_order_children_item_2, 1);
        initPopupItem(popupView, R.id.popup_order_children_item_3, 2);
        initPopupItem(popupView, R.id.popup_order_children_item_4, 3);
    }

    private void initPopupItem(View popupView, int id, int index) {
        View item = popupView.findViewById(id);
        if (item == null) return;

        popupItemName[index] = (TextView) item.findViewById(R.id.popup_order_children_item_name);
        popupItemNumber[index] = (TextView) item.findViewById(R.id.popup_order_children_item_number);
        popupItemSub[index] = (TextView) item.findViewById(R.id.popup_order_children_item_sub);
        popupItemPlus[index] = (TextView) item.findViewById(R.id.popup_order_children_item_plus);

        popupItemName[index].setText(Constants.ChildrenSeatMap.get(index + 1));
        popupItemSub[index].setTag(index);
        popupItemPlus[index].setTag(index);
        popupItemSub[index].setOnClickListener(this);
        popupItemPlus[index].setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_right_txt:
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("source", "提交订单页面");

                switch (mBusinessType) {
                    case Constants.BUSINESS_TYPE_PICK:
                        MobclickAgent.onEvent(getActivity(), "callcenter_pickup", map);
                        v.setTag("提交订单页面,calldomestic_pickup,calldomestic_pickup");
                        break;
                    case Constants.BUSINESS_TYPE_SEND:
                        MobclickAgent.onEvent(getActivity(), "callcenter_dropoff", map);
                        v.setTag("提交订单页面,calldomestic_dropoff,calloverseas_dropoff");
                        break;
                    case Constants.BUSINESS_TYPE_DAILY:
                        MobclickAgent.onEvent(getActivity(), "callcenter_oneday", map);
                        v.setTag("提交订单页面,calldomestic_oneday,calloverseas_oneday");
                        break;
                    case Constants.BUSINESS_TYPE_RENT:
                        MobclickAgent.onEvent(getActivity(), "callcenter_oneway", map);
                        v.setTag("提交订单页面,calldomestic_oneway,calloverseas_oneway");
                        break;
                }
                break;



            case R.id.popup_order_children_item_sub:
                int index = (int) v.getTag();
                if (childrenSeatNumbers[index] > 0) {
                    popupItemNumber[index].setText(String.valueOf(--childrenSeatNumbers[index]));
                }
                break;
            case R.id.popup_order_children_item_plus:
                index = (int) v.getTag();
                if (childrenSeatNumbers[index] < 5) {
                    popupItemNumber[index].setText(String.valueOf(++childrenSeatNumbers[index]));
                }
                break;
            case R.id.popup_order_children_cancel:
                popupWindow.dismiss();
                break;
            case R.id.popup_order_children_ok:
                popupWindow.dismiss();
                child = 0;
                for (int number : childrenSeatNumbers) {
                    child += number;
                }
                childCount.setText(String.format(getString(R.string.submit_child), child));
                inflateChildrenSeat();
                break;
        }
        super.onClick(v);
    }
}
