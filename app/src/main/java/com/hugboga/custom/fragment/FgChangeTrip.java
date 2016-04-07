package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderContact;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.request.RequestChangeTrip;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 2015/7/18.
 */
@ContentView(R.layout.fg_change_trip)
public class FgChangeTrip extends BaseFragment implements View.OnClickListener {

    public static final String KEY_ORDER_BEAN = "KEY_ORDER_BEAN";

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


    private TextView[] areaCodeArray;//区号数组
    private TextView[] areaPhoneArray;//电话数组

    @ViewInject(R.id.submit_adult_1)
    private TextView adultCount;//成人数
    @ViewInject(R.id.submit_child_1)
    private TextView childCount;//儿童数

    @ViewInject(R.id.submit_child_seat_layout)
    private LinearLayout orderChildrenSeatLayout;//儿童座椅
    @ViewInject(R.id.txt_children_seat_value)
    private TextView orderChildrenSeatValue;//儿童座椅数
    @ViewInject(R.id.submit_pick_name)
    private TextView pickName;//接机牌
    @ViewInject(R.id.submit_pick_name_layout)
    private View pickNameLayout;//接机牌

    @ViewInject(R.id.submit_hotel_phone_layout)
    private View hotelPhoneLayout;//酒店电话
    @ViewInject(R.id.submit_hotel_phone_star)
    private TextView hotelPhoneStar;//酒店电话星
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
    @ViewInject(R.id.submit_daily_city_value)
    private TextView dailyPassCityValue;//包车 途径城市
    @ViewInject(R.id.submit_daily_pass_city_layout)
    private View dailyPassCityLayout;//包车 途径城市
    @ViewInject(R.id.submit_daily_city_arrow)
    private View dailyPassCityArrow;//包车 途径城市 Arrow

    @ViewInject(R.id.submit_flight_no)
    private TextView flightNo;//航班号,送机填写
    @ViewInject(R.id.submit_flight_no_layout)
    private View flightNOLayout;//航班号,送机填写
    @ViewInject(R.id.submit_remark)
    private TextView remark;//备注


    @ViewInject(R.id.change_trip_btn)
    private TextView tripBtn;//按钮
    @ViewInject(R.id.change_trip_label)
    TextView tripLabel; //提示文案

    private OrderBean mOrderBean;
    private int phoneLayoutCount = 1;//联系方式数量
    private String serverDate;//服务时间
    private FlightBean flightBean;
    private boolean needChildrenSeat = false;//是否需要儿童座椅
    private boolean needBanner = true;//是否可以展示接机牌

    private PopupWindow popupWindow;
    private DbManager dbUtils;
    private int child;
    private int adult;
    private ArrayList<CityBean> passCityList;
    private PoiBean poiBean;
    private DialogUtil mDialogUtil;


    @Override
    protected void initView() {
        mOrderBean = (OrderBean) getArguments().getSerializable(KEY_ORDER_BEAN);
        mDialogUtil = DialogUtil.getInstance(getActivity());
        dbUtils = new DBHelper(getActivity()).getDbManager();
        adult = mOrderBean.adult;
        child = mOrderBean.child;
        areaCodeArray = new TextView[]{areaCode, areaCode2, areaCode3};
        areaPhoneArray = new TextView[]{connectPhone, connectPhone2, connectPhone3};
        AirPort airPort = findAirPortByCode(mOrderBean.flightAirportCode);
        CityBean cityBean = findCityById(mOrderBean.serviceCityId);
        serverDate = mOrderBean.serviceStartTime;
        passCityList = new ArrayList<CityBean>();
        if (mOrderBean.passByCity != null)
            for (int i = 1; i < mOrderBean.passByCity.size() - 1; i++) {
                passCityList.add(mOrderBean.passByCity.get(i));
            }
        if (airPort != null) {
            needChildrenSeat = airPort.childSeatSwitch;
            needBanner = airPort.bannerSwitch;
        } else if (cityBean != null) {
            needChildrenSeat = cityBean.childSeatSwitch;
        }

        switch (mBusinessType) {
            case Constants.BUSINESS_TYPE_PICK:
                tripLabel.setText(getResources().getString(R.string.trip_tip_label1));
                flightNOLayout.setVisibility(View.GONE);
                hotelPhone.setText(mOrderBean.serviceAddressTel);
                pickNameLayout.setVisibility(needBanner ? View.VISIBLE : View.GONE);
                if (!TextUtils.isEmpty(mOrderBean.serviceAreaCode))
                    hotelPhoneAreaCode.setText("+" + mOrderBean.serviceAreaCode);
                break;
            case Constants.BUSINESS_TYPE_SEND:
                tripLabel.setText(getResources().getString(R.string.trip_tip_label1));
                pickNameLayout.setVisibility(View.GONE);
                hotelPhoneStar.setVisibility(View.INVISIBLE);
                pickVisaLayout.setVisibility(View.GONE);
                flightNOLayout.setVisibility(View.VISIBLE);
                flightNo.setText(mOrderBean.flight);
                hotelPhone.setText(mOrderBean.serviceAddressTel);

                if (!TextUtils.isEmpty(mOrderBean.serviceAreaCode)) {
                    hotelPhoneAreaCode.setText("+" + mOrderBean.serviceAreaCode);
                }
                break;
            case Constants.BUSINESS_TYPE_DAILY:
                dailyPassCityLayout.setVisibility(View.VISIBLE);
            case Constants.BUSINESS_TYPE_COMMEND:
                tripLabel.setText(getResources().getString(R.string.trip_tip_label2));
                pickNameLayout.setVisibility(View.GONE);
                pickNameLayout.setVisibility(View.GONE);
                pickVisaLayout.setVisibility(View.GONE);
                flightNOLayout.setVisibility(View.GONE);
                serverDateTimeLayout.setVisibility(View.VISIBLE);
                serverPlaceLayout.setVisibility(View.VISIBLE);
                serverPlace.setText(mOrderBean.startAddress);
                serverDateTime.setText(mOrderBean.serviceTime + " " + mOrderBean.serviceStartTime + "(当地时间)");
                hotelPhone.setText(mOrderBean.serviceAddressTel);
                if (!TextUtils.isEmpty(mOrderBean.serviceAreaCode)) {
                    hotelPhoneAreaCode.setText("+" + mOrderBean.serviceAreaCode);
                }


                if (mOrderBean.orderGoodsType == 3) {//市内包车
                    dailyPassCityArrow.setVisibility(View.GONE);
                    dailyPassCityValue.setClickable(false);
                    dailyPassCityLayout.setClickable(false);
                    dailyPassCityValue.setText(mOrderBean.journeyComment.toString());
                } else {//跨城市
                    dailyPassCityArrow.setVisibility(View.VISIBLE);
                    dailyPassCityValue.setClickable(true);
                    dailyPassCityValue.setFocusable(false);
                    dailyPassCityLayout.setClickable(true);
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < passCityList.size(); i++) {
                        CityBean city = passCityList.get(i);
                        sb.append(city.name).append("、");
                    }
                    dailyPassCityValue.setText(sb.toString());
                }
                break;
            case Constants.BUSINESS_TYPE_RENT:
                tripLabel.setText(getResources().getString(R.string.trip_tip_label1));
                pickNameLayout.setVisibility(View.GONE);
                hotelPhoneLayout.setVisibility(View.GONE);
                pickVisaLayout.setVisibility(View.GONE);
                flightNOLayout.setVisibility(View.GONE);
                hotelPhoneTip.setVisibility(View.GONE);
                break;
        }


        connectName.setText(mOrderBean.contactName);
        areaCode.setText("+" + mOrderBean.contact.get(0).areaCode);
        connectPhone.setText(mOrderBean.contact.get(0).tel);
        phoneLayoutCount = mOrderBean.contact.size();
        for (int i = 0; i < mOrderBean.contact.size(); i++) {
            OrderContact orderContact = mOrderBean.contact.get(i);
            String areaCode = orderContact.areaCode;
            if (areaCode.indexOf("+") < 0) areaCode = "+" + areaCode;
            areaCodeArray[i].setText(areaCode);
            areaPhoneArray[i].setText(orderContact.tel);
            if (i == 1) {
                phoneLayout2.setVisibility(View.VISIBLE);
            } else if (i == 2) {
                phoneLayout3.setVisibility(View.VISIBLE);
            }
        }
        // 儿童座椅
        if (mOrderBean.childSeat != null) {
            for (String childrenSeat : mOrderBean.childSeat) {
                String[] seats = childrenSeat.split("-");
                if (Integer.valueOf(seats[0]) >= 1)
                    childrenSeatNumbers[Integer.valueOf(seats[0]) - 1] = Integer.valueOf(seats[1]);
            }
        }
        inflateChildrenSeat();
        adultCount.setText(String.format(getString(R.string.submit_adult), adult));
        childCount.setText(String.format(getString(R.string.submit_child), child));
        pickName.setText(mOrderBean.brandSign);
        pickVisa.setText(Constants.VisaInfoMap.get(mOrderBean.visa));
        remark.setText(mOrderBean.memo);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    /**
     * 根据cityId 查城市
     *
     * @param cityId
     * @return
     */
    private CityBean findCityById(int cityId) {
        try {
            return dbUtils.findById(CityBean.class, cityId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AirPort findAirPortByCode(String code) {

        try {
            Selector<AirPort> selector = dbUtils.selector(AirPort.class);
            selector.where("airport_code", "=", code);
            return selector.findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void initHeader() {
        fgRightBtn.setVisibility(View.VISIBLE);
        fgTitle.setText("修改行程资料");
    }

    @Event({R.id.change_trip_btn,
            R.id.submit_phone_add,
            R.id.submit_adult_sub,
            R.id.submit_adult_plus,
            R.id.submit_child_sub,
            R.id.submit_child_plus,
            R.id.popup_order_children_item_sub,
            R.id.popup_order_children_item_plus,
            R.id.submit_areacode,
            R.id.submit_areacode2,
            R.id.submit_areacode3,
            R.id.submit_phone_del_2,
            R.id.submit_phone_del_3,
            R.id.submit_pick_visa_layout,
            R.id.submit_date_time,
            R.id.submit_date_time_layout,
            R.id.submit_hotel_phone_areacode,
            R.id.submit_flight_no_layout,
            R.id.submit_start_place_layout,
            R.id.submit_daily_city_value,
            R.id.submit_daily_pass_city_layout,
    })
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.change_trip_btn:
                submitData();
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
                }
                break;
            case R.id.submit_phone_del_2:
                areaCode2.setText("");
                connectPhone2.setText("");
                phoneLayout2.setVisibility(View.GONE);
                phoneLayoutCount--;
                break;
            case R.id.submit_phone_del_3:
                areaCode3.setText("");
                connectPhone3.setText("");
                phoneLayout3.setVisibility(View.GONE);
                phoneLayoutCount--;
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
                childCount.setText(String.format(getString(R.string.submit_child), child));
                inflateChildrenSeat();
                break;
            case R.id.submit_areacode:
            case R.id.submit_areacode2:
            case R.id.submit_areacode3:
            case R.id.submit_hotel_phone_areacode:
                FgChooseCountry chooseCountry = new FgChooseCountry();
                Bundle bundle = new Bundle();
                bundle.putInt("airportCode", view.getId());
                startFragment(chooseCountry, bundle);
                break;
            case R.id.submit_date_time_layout:
            case R.id.submit_date_time:
                showTimeSelect();
                break;
            case R.id.submit_pick_visa_layout:
                showSelectVisa();
                break;
            case R.id.submit_flight_no_layout:
                startFragment(new FgPickFlight());
                break;
            case R.id.submit_start_place_layout:
                startArrivalSearch(mOrderBean.serviceCityId, mOrderBean.startLocation);
                break;
            case R.id.submit_daily_city_value:
                if (mOrderBean.orderGoodsType == 3) return;
                bundle = new Bundle();
                bundle.putString(KEY_FROM, "pass");
                bundle.putInt(FgChooseCity.KEY_CITY_ID, mOrderBean.serviceCityId);
                bundle.putInt(FgChooseCity.KEY_CHOOSE_TYPE, FgChooseCity.KEY_TYPE_MULTIPLY);
                bundle.putSerializable(FgChooseCity.KEY_CITY_LIST_CHOSEN, passCityList);
                bundle.putInt(FgChooseCity.KEY_CHOOSE_TYPE, FgChooseCity.KEY_TYPE_MULTIPLY);
                ArrayList<Integer> exceptId = new ArrayList<>();
                exceptId.add(mOrderBean.serviceCityId);
                exceptId.add(mOrderBean.serviceEndCityid);
                bundle.putSerializable(FgChooseCity.KEY_CITY_EXCEPT_ID_LIST, exceptId);
                startFragment(new FgChooseCity(), bundle);
                break;
            default:
                super.onClick(view);
                break;
        }
    }


    private void startArrivalSearch(int cityId, String location) {
        if (location != null) {
            FgPoiSearch fg = new FgPoiSearch();
            Bundle bundle = new Bundle();
            bundle.putInt(FgPoiSearch.KEY_CITY_ID, cityId);
            bundle.putString(FgPoiSearch.KEY_LOCATION, location);
            startFragment(fg, bundle);
        }
    }

    private void submitData() {
        MLog.e("mOrderBean.orderNo=" + mOrderBean.orderNo);
        final OrderBean orderBean = new OrderBean();
        orderBean.orderNo = mOrderBean.orderNo;
        String contactName = connectName.getText().toString().trim();
        if (TextUtils.isEmpty(contactName)) {
            showTip("请填写联系人姓名");
            return;
        }
        orderBean.contact = new ArrayList<OrderContact>();
        OrderContact contact;
        for (int i = 0; i < 3; i++) {
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
                contact.areaCode = "";
                contact.tel = "";
            }
            contact.areaCode = contact.areaCode.replace("+", "");
            orderBean.contact.add(contact);
        }
        for (int i = 0; i < orderBean.contact.size(); i++) {
            for (int j = i + 1; j < orderBean.contact.size(); j++) {
                String contact1 = orderBean.contact.get(i).areaCode + orderBean.contact.get(i).tel;
                String contact2 = orderBean.contact.get(j).areaCode + orderBean.contact.get(j).tel;
                if (contact1.equals(contact2) && !"".equals(contact1)) {
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
        if (adult + child >= orderBean.seatCategory) {
            Toast.makeText(getActivity(), "您选择的出行人数超出车型所能容纳的人数,请重新填写出行人数", Toast.LENGTH_LONG).show();
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
        hotelPhoneAreaCodeStr = hotelPhoneAreaCodeStr.replace("+", "");
        orderBean.serviceAreaCode = hotelPhoneAreaCodeStr;
        orderBean.serviceAddressTel = hotelPhoneStr;
        switch (mBusinessType) {
            case Constants.BUSINESS_TYPE_PICK:
                orderBean.brandSign = brandSign;
                orderBean.visa = mOrderBean.visa;
                break;
            case Constants.BUSINESS_TYPE_SEND:
                if (flightBean != null)
                    orderBean.flight = flightBean.flightNo;
                orderBean.flightBean = flightBean;
                break;
            case Constants.BUSINESS_TYPE_DAILY:
                //日租修改时间
                orderBean.serviceTime = mOrderBean.serviceTime;
                orderBean.serviceStartTime = serverDate;
                if (poiBean != null) {
                    orderBean.startAddress = poiBean.placeName;
                    orderBean.startAddressDetail = poiBean.placeDetail;
                }
                if (mOrderBean.orderGoodsType == 3) {
                    orderBean.journeyComment = dailyPassCityValue.getText().toString();
                }
                orderBean.stayCityListStr = getPassCityStr();
                break;
        }
        orderBean.orderType = getBusinessType();
        orderBean.contactName = contactName;
        orderBean.adult = adult;
        orderBean.child = child;
        orderBean.memo = remark.getText().toString().trim();
        orderBean.childSeat = new ArrayList<>();
        for (int i = 0; i < childrenSeatNumbers.length; i++) {
            if (childrenSeatNumbers[i] != 0)
                orderBean.childSeat.add((i + 1) + "-" + childrenSeatNumbers[i]);
        }
        mDialogUtil.showCustomDialog("确认提交行程单", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestChangeTrip request = new RequestChangeTrip(getActivity(), orderBean);
                requestData(request);
            }
        }, null);

    }

    @Override
    protected int getBusinessType() {
        return super.getBusinessType();
    }

    @Override
    public void onDataRequestSucceed(BaseRequest parser) {
        finishForResult(new Bundle());
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
                        mOrderBean.visa = which + 1;
                        pickVisa.setText(Constants.VisaInfoMap.get(which + 1));
                    }
                })
                .show();
    }

    //popup  儿童座椅
    private TextView[] popupItemName = new TextView[4];
    private TextView[] popupItemNumber = new TextView[4];
    private TextView[] popupItemSub = new TextView[4];
    private TextView[] popupItemPlus = new TextView[4];

    private int[] childrenSeatNumbers = new int[4];

    //popup  儿童座椅
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

    //popup  儿童座椅
    private void initPopupView(View popupView) {
        popupView.findViewById(R.id.popup_order_children_cancel).setOnClickListener(this);
        popupView.findViewById(R.id.popup_order_children_ok).setOnClickListener(this);

        initPopupItem(popupView, R.id.popup_order_children_item_1, 0);
        initPopupItem(popupView, R.id.popup_order_children_item_2, 1);
        initPopupItem(popupView, R.id.popup_order_children_item_3, 2);
        initPopupItem(popupView, R.id.popup_order_children_item_4, 3);
    }

    //popup  儿童座椅
    private void initPopupItem(View popupView, int id, int index) {
        View item = popupView.findViewById(id);
        if (item == null) return;
        popupItemName[index] = (TextView) item.findViewById(R.id.popup_order_children_item_name);
        popupItemNumber[index] = (TextView) item.findViewById(R.id.popup_order_children_item_number);
        popupItemSub[index] = (TextView) item.findViewById(R.id.popup_order_children_item_sub);
        popupItemPlus[index] = (TextView) item.findViewById(R.id.popup_order_children_item_plus);

        popupItemName[index].setText(Constants.ChildrenSeatMap.get(index + 1));
        popupItemNumber[index].setText(String.valueOf(childrenSeatNumbers[index]));
        popupItemSub[index].setTag(index);
        popupItemPlus[index].setTag(index);
        popupItemSub[index].setOnClickListener(this);
        popupItemPlus[index].setOnClickListener(this);
    }

    private void inflateChildrenSeat() {
        if (!needChildrenSeat) {
            orderChildrenSeatLayout.setVisibility(View.GONE);
            return;
        }
        orderChildrenSeatLayout.setVisibility(child > 0 ? View.VISIBLE : View.GONE);
        orderChildrenSeatLayout.removeAllViews();
        for (int i = 0; i < childrenSeatNumbers.length; i++) {
            if (childrenSeatNumbers[i] <= 0) continue;
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_children_seat, null);
            TextView label = (TextView) view.findViewById(R.id.item_children_seat_label);
            TextView value = (TextView) view.findViewById(R.id.item_children_seat_value);
            label.setText(Constants.ChildrenSeatMap.get(i + 1));
            value.setText("x" + childrenSeatNumbers[i]);
            orderChildrenSeatLayout.addView(view);
        }
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
                if (bean.depAirportCode == null || !bean.depAirportCode.equals(mOrderBean.flightAirportCode)) {
                    Toast.makeText(getActivity(), "请选择与送达机场相符的航班", Toast.LENGTH_LONG).show();
                } else {
                    flightBean = bean;
                    String flightInfoStr = flightBean.flightNo + " ";
                    flightInfoStr += flightBean.depAirport.cityName + "-" + flightBean.arrivalAirport.cityName;
                    flightInfoStr += "\n当地时间" + flightBean.arrDate + " " + flightBean.depTime + "起飞";
                    flightNo.setText(flightInfoStr);
                }
            }
        } else if (FgPoiSearch.class.getSimpleName().equals(fragmentName)) {
            poiBean = (PoiBean) bundle.getSerializable(FgPoiSearch.KEY_ARRIVAL);
            serverPlace.setText(poiBean.placeName + "\n" + poiBean.placeDetail);
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


    public void showTimeSelect() {
        Calendar cal = Calendar.getInstance();
        MyTimePickerDialogListener mytimePickerDialog = new MyTimePickerDialogListener();
        TimePickerDialog datePickerDialog = new TimePickerDialog(this.getActivity(),
                mytimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        datePickerDialog.show();                //显示日期设置对话框
    }

    @Override
    public void onClick(View v) {
        onClickView(v);
    }

    /*
         * Function  :       自定义MyDatePickerDialog类，用于实现DatePickerDialog.OnDateSetListener接口，
         *                           当点击日期设置对话框中的“设置”按钮时触发该接口方法
         */
    class MyTimePickerDialogListener implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hour = String.format("%02d", hourOfDay);
            String minuteStr = String.format("%02d", minute);
            serverDate = hour + ":" + minuteStr;
            serverDateTime.setText(mOrderBean.serviceTime + " " + serverDate + "(当地时间)");
        }
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
        passCity += mOrderBean.serviceCityId + "-0";
        for (CityBean city : passCityList) {
            passCity += "," + city.cityId + "-0";
        }
        passCity += "," + mOrderBean.serviceEndCityid + "-0";
        return passCity;
    }
}
