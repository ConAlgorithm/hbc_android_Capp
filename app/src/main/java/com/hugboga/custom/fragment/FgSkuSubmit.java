package com.hugboga.custom.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderContact;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestPriceSku;
import com.hugboga.custom.data.request.RequestSubmitBase;
import com.hugboga.custom.data.request.RequestSubmitDaily;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * SKu 下单
 * Created by admin on 2016/3/17.
 */
@ContentView(R.layout.fg_sku_submit)
public class FgSkuSubmit extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.sku_title)
    private TextView skuTitle;//标题
    @ViewInject(R.id.sku_label)
    private TextView skuLabel;//标签描述
    @ViewInject(R.id.sku_days)
    private TextView skuDays;//天数
    @ViewInject(R.id.sku_start_day_edit)
    private TextView skuStartDayEdit;//起始地点
    @ViewInject(R.id.sku_car_type_edit)
    private TextView skuCarTypeEdit;//车类
    @ViewInject(R.id.sku_start_time_edit)
    private TextView skuStartTime;//起始时间
    @ViewInject(R.id.submit_adult)
    private TextView skuAdultEdit;//成人数
    @ViewInject(R.id.submit_child)
    private TextView skuChildEdit;//儿童数
    @ViewInject(R.id.submit_child_seat_layout)
    private LinearLayout childSeatLayout;//儿童座椅
    @ViewInject(R.id.sku_start_address_edit)
    private TextView skuStartAddress;//地址
    @ViewInject(R.id.sku_user_name_edit)
    private TextView skuUserName;//用户名
    @ViewInject(R.id.sku_area_code)
    private TextView skuAreaCode;//用户手机区号
    @ViewInject(R.id.sku_user_phone_edit)
    private TextView skuPhone;//用户手机
    @ViewInject(R.id.sku_remark_edit)
    private TextView remark;//备注
    @ViewInject(R.id.bottom_bar_total_value)
    private TextView totalPrice;//价钱

    private SkuItemBean skuBean;
    private CityBean cityBean;
    private String serverDate;//包车日期，yyyy-MM-dd
    private String serverTime = "09:00";//时间 HH-mm
    private int adult;//成人数
    private int child;//儿童数
    private boolean needChildrenSeat = false;//是否需要儿童座椅
    private PopupWindow popupWindow;//弹窗
    private CarListBean carListBean;//车型
    private FgCarSuk fgCarSuk;
    private CarBean carTypeBean;//车型
    private String areaCode;//区号
    private PoiBean startPoiBean;//上车地点

    @Override
    protected void initHeader() {
        fgTitle.setText("填写订单");
    }

    @Override
    protected void initView() {
        skuBean = (SkuItemBean) getArguments().getSerializable(FgSkuDetail.WEB_SKU);
        cityBean = (CityBean) getArguments().getSerializable(FgSkuDetail.WEB_CITY);
        MLog.e("skuBean= " + skuBean);
        if (skuBean == null) return;
        skuTitle.setText(skuBean.goodsName);
        skuLabel.setText(skuBean.places);
        skuDays.setText(getString(R.string.sku_days, skuBean.daysCount));
        SharedPre sharedPre = new SharedPre(getActivity());
        String areaCode = sharedPre.getStringValue(SharedPre.CODE);
        String phone = sharedPre.getStringValue(SharedPre.PHONE);
        if (!TextUtils.isEmpty(areaCode)) {
            setAreaCode(areaCode);
            this.areaCode = areaCode;
        }
        if (!TextUtils.isEmpty(phone)) {
            skuPhone.setText(phone);
        }
//        needChildrenSeat = cityBean != null && cityBean.childSeatSwitch;
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Event({
            R.id.sku_start_day_layout,
            R.id.sku_start_day_edit,
            R.id.sku_car_type_layout,
            R.id.sku_car_type_edit,
            R.id.sku_start_time_layout,
            R.id.sku_start_time_edit,
            R.id.submit_adult_sub,
            R.id.submit_adult_plus,
            R.id.submit_child_sub,
            R.id.submit_child_plus,
            R.id.sku_start_address_layout,
            R.id.sku_start_address_edit,
            R.id.sku_area_code,
            R.id.sku_info_call_1,
            R.id.sku_info_call_2,
            R.id.bottom_bar_btn
    })
    private void onClickView(View view) {
        HashMap<String,String> map = new HashMap<String,String>();
        switch (view.getId()) {
            case R.id.sku_start_day_layout://开始日期
            case R.id.sku_start_day_edit://开始日期
                showDaySelect();
                break;
            case R.id.sku_car_type_layout://车型
            case R.id.sku_car_type_edit://车型
                if (carListBean == null) {
                    Toast.makeText(getActivity(), "请先选择日期", Toast.LENGTH_LONG).show();
                    return;
                }
                if (carListBean.carList == null || carListBean.carList.isEmpty()) {
                    Toast.makeText(getActivity(), "请先选择日期", Toast.LENGTH_LONG).show();
                    return;
                }
                if (fgCarSuk == null) {
                    fgCarSuk = new FgCarSuk();
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(FgCarSuk.KEY_CAR_LIST, carListBean);
                startFragment(fgCarSuk, bundle);
                break;
            case R.id.sku_start_time_layout://开时时间,默认8:00
            case R.id.sku_start_time_edit://开时时间,默认8:00
                showTimeSelect();
                break;
            case R.id.submit_adult_sub://成人数量减
                if (adult <= 0) break;
                skuAdultEdit.setText(String.format(getString(R.string.submit_adult), --adult));
                break;
            case R.id.submit_adult_plus://成人数量加
                skuAdultEdit.setText(String.format(getString(R.string.submit_adult), ++adult));
                break;
            case R.id.submit_child_sub://儿童数减
//                if (needChildrenSeat) {
//                    showChildrenSeat();
//                    break;
//                }
                if (child <= 0) break;
                skuChildEdit.setText(String.format(getString(R.string.submit_child), --child));
                break;
            case R.id.submit_child_plus://儿童数加
//                if (needChildrenSeat) {
//                    showChildrenSeat();
//                    break;
//                }
                skuChildEdit.setText(String.format(getString(R.string.submit_child), ++child));
                break;
            case R.id.sku_start_address_layout://出发地点 选poi
            case R.id.sku_start_address_edit://出发地点 选poi
                FgPoiSearch fg = new FgPoiSearch();
                bundle = new Bundle();
                bundle.putString(KEY_FROM, "from");
                bundle.putString("source","下单过程中");
                bundle.putInt(FgPoiSearch.KEY_CITY_ID, skuBean.depCityId);
                bundle.putString(FgPoiSearch.KEY_LOCATION, cityBean.location);
                startFragment(fg, bundle);
                map.put("source", "下单过程中");
                MobclickAgent.onEvent(getActivity(), "search_trigger", map);
                break;
            case R.id.sku_area_code://电话 区号
                startFragment(new FgChooseCountry());
                break;
            case R.id.sku_info_call_1://电话 国内
                PhoneInfo.CallDial(getActivity(), Constants.CALL_NUMBER_IN);
                map.put("source", "提交订单页面");
                MobclickAgent.onEvent(getActivity(), "calldomestic_route", map);
                break;
            case R.id.sku_info_call_2://电话 境外
                PhoneInfo.CallDial(getActivity(), Constants.CALL_NUMBER_OUT);
                map.put("source", "提交订单页面");
                MobclickAgent.onEvent(getActivity(), "calloverseas_route", map);
                break;
            case R.id.bottom_bar_btn:
                submit();
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popup_order_children_item_sub:
                int index = (int) view.getTag();
                if (childrenSeatNumbers[index] > 0) {
                    popupItemNumber[index].setText(String.valueOf(--childrenSeatNumbers[index]));
                }
                break;
            case R.id.popup_order_children_item_plus:
                index = (int) view.getTag();
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
                skuChildEdit.setText(String.format(getString(R.string.submit_child), child));
                inflateChildrenSeat();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgCarSuk.class.getSimpleName().equals(from)) {
            int carTypePosition = bundle.getInt(FgCarSuk.KYE_POSITION);
            if (carTypePosition < carListBean.carList.size() && carTypePosition >= 0) {
                CarBean carBean = carListBean.carList.get(carTypePosition);
                if (carBean != null) {
                    carTypeBean = carBean;
                    skuCarTypeEdit.setText(carTypeBean.desc);
                    totalPrice.setText("" + carTypeBean.originalPrice);
                }
            }

        } else if (FgChooseCountry.class.getSimpleName().equals(from)) {
            areaCode = bundle.getString(FgChooseCountry.KEY_COUNTRY_CODE);
            String areaCodeName = bundle.getString(FgChooseCountry.KEY_COUNTRY_NAME);
            setAreaCode(areaCode);
        } else if (FgPoiSearch.class.getSimpleName().equals(from)) {
            startPoiBean = (PoiBean) bundle.getSerializable(FgPoiSearch.KEY_ARRIVAL);
            if (startPoiBean != null) {
                MLog.e("placeName = "+startPoiBean.placeName+" location ="+startPoiBean.location);
                skuStartAddress.setText(startPoiBean.placeName);
            }
        }
    }

    private void setAreaCode(String areaCode) {
        String input = "+" + areaCode + " ｜";
        skuAreaCode.setText(input);
    }

    /**
     * 下单
     */
    private void submit() {
        if (checkInput()) {
            if (UserEntity.getUser().isLogin(getActivity())) {
                OrderBean orderBean = getOrderByInput();
                RequestSubmitDaily request = new RequestSubmitDaily(getActivity(), orderBean);
                requestData(request);

                HashMap<String,String> map = new HashMap<String,String>();//用于统计
                map.put("carstyle", orderBean.carDesc);
                map.put("source", source);
                map.put("guestcount", orderBean.adult + orderBean.child + "");
                map.put("quoteprice", orderBean.orderPrice + "");
                map.put("payableamount", orderBean.orderPriceInfo != null ? orderBean.orderPriceInfo.shouldPay + "" : "");
                MobclickAgent.onEventValue(getActivity(), "submitorder_route" , map, 1);
            }else{
                Bundle bundle = new Bundle();//用于统计
                bundle.putString("source","SKU下单");
                startFragment(new FgLogin(), bundle);

                HashMap<String,String> map = new HashMap<String,String>();//用于统计
                map.put("source", "SKU下单");
                MobclickAgent.onEvent(getActivity(), "login_trigger", map);
            }
        }

    }

    private OrderBean getOrderByInput() {
        OrderBean orderBean = new OrderBean();//订单
        orderBean.orderType = 5;
        orderBean.goodsNo = skuBean.goodsNo;
        orderBean.lineSubject = skuBean.goodsName;
        orderBean.lineDescription = skuBean.salePoints;
        orderBean.orderGoodsType = skuBean.goodsType;
        orderBean.serviceTime = serverDate;//日期
        orderBean.serviceStartTime = serverTime + ":00";//时间
        orderBean.serviceEndTime = getServiceEndTime(serverDate, skuBean.daysCount - 1);
        orderBean.distance = String.valueOf(carListBean.distance);//距离
        orderBean.expectedCompTime = carListBean.interval;//耗时
        orderBean.carDesc = carTypeBean.desc;//车型描述
        orderBean.carType = carTypeBean.carType;//车型
        orderBean.seatCategory = carTypeBean.carSeat;
        orderBean.orderPrice = carTypeBean.originalPrice;
        orderBean.priceMark = carTypeBean.pricemark;
        orderBean.urgentFlag = carTypeBean.urgentFlag;
        orderBean.adult = adult;//成人数
        orderBean.child = child;//儿童数
        orderBean.childSeat = new ArrayList<>();
        for (int i = 0; i < childrenSeatNumbers.length; i++) {
            if (childrenSeatNumbers[i] != 0)
                orderBean.childSeat.add((i + 1) + "-" + childrenSeatNumbers[i]);
        }
        String contactName = skuUserName.getText().toString().trim();
        orderBean.contactName = contactName;
        orderBean.contact = new ArrayList<OrderContact>();
        OrderContact orderContact = new OrderContact();
        orderContact.areaCode = areaCode;
        orderContact.tel = skuPhone.getText().toString().trim();
        orderBean.contact.add(orderContact);
        orderBean.memo = remark.getText().toString().trim();
        if (startPoiBean != null) {
            orderBean.startAddress = startPoiBean.placeName;
            orderBean.startAddressDetail = startPoiBean.placeDetail;
            orderBean.startLocation = startPoiBean.location;
        }
        orderBean.serviceCityId = skuBean.depCityId;
        orderBean.serviceCityName = skuBean.depCityName;
        //出发地，到达地经纬度
        orderBean.terminalLocation = null;
        orderBean.destAddress = skuBean.arrCityName;
        orderBean.serviceEndCityid = skuBean.arrCityId;
        orderBean.serviceEndCityName = skuBean.arrCityName;
        orderBean.totalDays = skuBean.daysCount;
        orderBean.oneCityTravel = skuBean.goodsType == 3 ? 1 : 2;//1：市内畅游  2：跨城市
        orderBean.isHalfDaily = 0;
        orderBean.inTownDays = skuBean.goodsType == 3 ? skuBean.daysCount : 0;
        orderBean.outTownDays = skuBean.goodsType == 3 ? 0 : skuBean.daysCount;
        orderBean.skuPoi = getPoiStr();
        orderBean.stayCityListStr = getPassCityStr();
        orderBean.priceChannel = carTypeBean.originalPrice+"";
        orderBean.userName = contactName;

        return orderBean;
    }

    private String getServiceEndTime(String date, int day) {
        try {
            String[] ymd = date.split("-");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(ymd[0]), Integer.valueOf(ymd[1])-1, Integer.valueOf(ymd[2]));
            calendar.add(Calendar.DAY_OF_YEAR, day);
            return DateUtils.dateDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            MLog.e("解析时间格式错误", e);
        }
        return null;
    }

    private String getPassCityStr() {
        String passCity = "";
        passCity += skuBean.depCityId + "-0";
        for (CityBean city : skuBean.passCityList) {
            passCity += "," + city.cityId + "-0";
        }
        passCity += "," + skuBean.arrCityId + "-0";
        return passCity;
    }

    private String getPoiStr() {
        return skuBean.passPoiListStr;
    }

    private boolean checkInput() {

        if (TextUtils.isEmpty(serverDate)) {
            Toast.makeText(getActivity(), "请选择服务时间", Toast.LENGTH_LONG).show();
            return false;
        }

        if (carTypeBean == null) {
            Toast.makeText(getActivity(), "请选择服务车型", Toast.LENGTH_LONG).show();
            return false;
        }

        if (adult == 0) {
            Toast.makeText(getActivity(), "请选择成人数量", Toast.LENGTH_LONG).show();
            return false;
        }
        //客户端做check
        if(adult+child>=carTypeBean.carSeat){
            Toast.makeText(getActivity(),"您选择的出行人数超出车型所能容纳的人数,请重新填写出行人数", Toast.LENGTH_LONG).show();
            return false;
        }
        if (startPoiBean == null) {
            showTip("请添加您的上车地点");
            return false;
        }
        String contactName = skuUserName.getText().toString().trim();
        if (TextUtils.isEmpty(contactName)) {
            Toast.makeText(getActivity(), "请填写联系人姓名", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(areaCode)) {
            Toast.makeText(getActivity(), "请选择区号", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(skuPhone.getText().toString().trim())) {
            Toast.makeText(getActivity(), "请填写联系电话", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public void showDaySelect() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new MyDatePickerListener(),
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(getActivity().getResources().getColor(R.color.all_bg_yellow));
        cal = Calendar.getInstance();
        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        dpd.setMaxDate(cal);
        dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框

    }

    /*
         * Function  :       自定义MyDatePickerDialog类，用于实现DatePickerDialog.OnDateSetListener接口，
         *                           当点击日期设置对话框中的“设置”按钮时触发该接口方法
         */
    class MyDatePickerListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear + 1;
            String monthStr = String.format("%02d", month);
            String dayOfMonthStr = String.format("%02d", dayOfMonth);
            serverDate = year + "-" + monthStr + "-" + dayOfMonthStr;
            skuStartDayEdit.setText(serverDate);
            requestCarList();//选完时间自动请求车型
        }
    }


    /**
     * 时间选择器
     */
    public void showTimeSelect() {
        Calendar cal = Calendar.getInstance();
        MyTimePickerDialogListener myTimePickerDialog = new MyTimePickerDialogListener();
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(myTimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        datePickerDialog.setAccentColor(getActivity().getResources().getColor(R.color.all_bg_yellow));
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
            skuStartTime.setText(serverTime + "(当地时间)");
        }
    }

    /**
     * 请求车型
     */
    private void requestCarList() {
        String serverDayTime = serverDate + " " + serverTime + ":00";
        MLog.e("serverDayTime= " + serverDayTime);
        RequestPriceSku request = new RequestPriceSku(getActivity(), skuBean.goodsNo, serverDayTime);
        requestData(request);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestPriceSku) {
            carListBean = ((RequestPriceSku) request).getData();
            carTypeBean = null;
            skuCarTypeEdit.setText("");
            totalPrice.setText("--");
            if(carListBean==null||carListBean.carList==null||carListBean.carList.isEmpty()){
                DialogUtil.getInstance(getActivity()).showCustomDialog("该日期暂无车辆库存,请重新选择日期");
                return;
            }
            if (fgCarSuk == null) {
                fgCarSuk = new FgCarSuk();
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(FgCarSuk.KEY_CAR_LIST, carListBean);
            startFragment(fgCarSuk, bundle);
        } else if (request instanceof RequestSubmitBase) {
            bringToFront(FgTravel.class, new Bundle());
            String orderNo = ((RequestSubmitBase) request).getData();
            Bundle bundle = new Bundle();
            bundle.putString(FgOrder.KEY_ORDER_ID, orderNo);
            startFragment(new FgOrder(), bundle);
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
}
