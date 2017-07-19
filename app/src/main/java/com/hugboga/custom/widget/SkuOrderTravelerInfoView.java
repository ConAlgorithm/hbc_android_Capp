package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CombinationOrderActivity;
import com.hugboga.custom.activity.SkuOrderActivity;
import com.hugboga.custom.data.bean.CarAdditionalServicePrice;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.sevenheaven.iosswitch.ShSwitchView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;
/**
 * Created by qingcha on 16/12/17.
 */
public class SkuOrderTravelerInfoView extends LinearLayout{

    private static final int AHEAD_TIME = 90 * 60 * 1000;
    public static final int REQUEST_CODE_PICK_CONTACTS = 101;
    public static final int REQUEST_CODE_PICK_OTHER_CONTACTS = 102;
    public static final int REQUEST_CODE_PICK_STANDBY_CONTACTS = 103;

    @Bind(R.id.traveler_info_contacts_layout)
    TravelerInfoItemView contactsLayout;

    @Bind(R.id.sku_order_traveler_info_contacts_choose_iv)
    ImageView standbyContactsChooseIV;
    @Bind(R.id.sku_order_traveler_info_contacts_choose_tv)
    TextView standbyContactsChooseTV;
    @Bind(R.id.traveler_info_standby_contacts_layout)
    TravelerInfoItemView standbyContactsLayout;

    @Bind(R.id.sku_order_traveler_info_flight_layout)
    RelativeLayout flightLayout;
    @Bind(R.id.sku_order_traveler_info_flight_et)
    EditText flightET;

    @Bind(R.id.sku_order_traveler_info_checkin_layout)
    RelativeLayout checkinLayout;
    @Bind(R.id.sku_order_traveler_info_checkin_hint_tv)
    TextView checkinHintTV;
    @Bind(R.id.sku_order_traveler_info_checkin_et)
    EditText checkinET;
    @Bind(R.id.sku_order_traveler_info_checkin_switch_view)
    ShSwitchView checkinSwitchView;
    @Bind(R.id.sku_order_traveler_info_checkin_price_tv)
    TextView checkinPriceTV;
    @Bind(R.id.sku_order_traveler_info_checkin_star_tv)
    TextView checkinStarTV;

    @Bind(R.id.sku_order_traveler_info_other_contacts_top_space)
    View otherContactsTopSpaceView;
    @Bind(R.id.traveler_info_other_contacts_switch_layout)
    RelativeLayout contactsSwitchLayout;
    @Bind(R.id.sku_order_traveler_info_contacts_switch_view)
    ShSwitchView contactsSwitchView;
    @Bind(R.id.traveler_info_other_contacts_layout)
    TravelerInfoItemView otherContactsLayout;
    @Bind(R.id.sku_order_traveler_info_sendmessage_layout)
    RelativeLayout sendMessageLayout;
    @Bind(R.id.sku_order_traveler_info_sendmessage_switch_view)
    ShSwitchView sendMessageSwitchView;

    @Bind(R.id.sku_order_traveler_info_start_time_layout)
    RelativeLayout timeLayout;
    @Bind(R.id.sku_order_traveler_info_start_time_tv)
    TextView timeTV;

    @Bind(R.id.sku_order_traveler_info_start_address_layout)
    RelativeLayout addressLayout;
    @Bind(R.id.sku_order_traveler_info_start_address_tv)
    TextView addressTV;
    @Bind(R.id.sku_order_traveler_info_start_address_description_tv)
    TextView addressDescriptionTV;
    @Bind(R.id.sku_order_traveler_info_start_address_line_view)
    View addressLineView;

    @Bind(R.id.sku_order_traveler_info_wechat_et)
    EditText wechatET;

    @Bind(R.id.sku_order_traveler_info_mark_et)
    EditText markET;

    private TravelerInfoBean travelerInfoBean;
    private TimePicker picker;
    private int orderType;
    private CarAdditionalServicePrice additionalServicePrice;
    private OnSwitchPickOrSendListener onSwitchPickOrSendListener;

    public SkuOrderTravelerInfoView(Context context) {
        this(context, null);
    }

    public SkuOrderTravelerInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_sku_order_traveler_info, this);
        ButterKnife.bind(view);

        travelerInfoBean = new TravelerInfoBean();

        contactsLayout.init(travelerInfoBean.travelerName, CommonUtils.addPhoneCodeSign(travelerInfoBean.areaCode), travelerInfoBean.travelerPhone);
        checkinSwitchView.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isSelect) {
                if (orderType == 1) {//接机
                    travelerInfoBean.isPickup = isSelect;
                    checkinET.setVisibility(isSelect ? View.VISIBLE : View.GONE);
                    checkinStarTV.setVisibility(isSelect ? View.VISIBLE : View.GONE);
                } else if (orderType == 2) {//送机
                    travelerInfoBean.isCheckin = isSelect;
                }
                CommonUtils.hideSoftInput((Activity) getContext());
                if (onSwitchPickOrSendListener != null) {
                    onSwitchPickOrSendListener.onSwitchPickOrSend(isSelect, getAdditionalPrice());
                }
            }
        });

        contactsSwitchView.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean b) {
                travelerInfoBean.isOther = !travelerInfoBean.isOther;
                if (travelerInfoBean.isOther) {
                    otherContactsLayout.setVisibility(View.VISIBLE);
                    sendMessageLayout.setVisibility(View.VISIBLE);
                } else {
                    otherContactsLayout.setVisibility(View.GONE);
                    sendMessageLayout.setVisibility(View.GONE);
                }
                CommonUtils.hideSoftInput((Activity) getContext());
            }
        });

        contactsLayout.setRequestCode(REQUEST_CODE_PICK_CONTACTS);
        standbyContactsLayout.setRequestCode(REQUEST_CODE_PICK_STANDBY_CONTACTS);
        otherContactsLayout.setRequestCode(REQUEST_CODE_PICK_OTHER_CONTACTS);

        otherContactsLayout.setContactsHintText("乘车人");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (REQUEST_CODE_PICK_CONTACTS == requestCode || REQUEST_CODE_PICK_OTHER_CONTACTS == requestCode || REQUEST_CODE_PICK_STANDBY_CONTACTS == requestCode) {
            Uri result = data.getData();
            String[] contact = PhoneInfo.getPhoneContacts(getContext(), result);
            if (contact == null || contact.length < 2) {
                return;
            }
            if (!TextUtils.isEmpty(contact[0])) {
                switch (requestCode) {
                    case REQUEST_CODE_PICK_CONTACTS:
                        setTravelerName(contact[0]);
                        break;
                    case REQUEST_CODE_PICK_OTHER_CONTACTS:
                        setOtherTravelerName(contact[0]);
                        break;
                    case REQUEST_CODE_PICK_STANDBY_CONTACTS:
                        setStandbyTravelerName(contact[0]);
                        break;
                }
            }
            if (!TextUtils.isEmpty(contact[1])){
                String phone = contact[1];
                if (!TextUtils.isEmpty(phone)) {
                    phone = phone.replace("+86", "");//此处拷贝自以前代码。。。
                    phone = CommonUtils.getNum(phone);
                }
                switch (requestCode) {
                    case REQUEST_CODE_PICK_CONTACTS:
                        setTravelerPhone(phone);
                        break;
                    case REQUEST_CODE_PICK_OTHER_CONTACTS:
                        setOtherTravelerPhone(phone);
                        break;
                    case REQUEST_CODE_PICK_STANDBY_CONTACTS:
                        setStandbyTravelerPhone(phone);
                        break;
                }
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode) {
        TravelerInfoItemView travelerInfoItemView = null;
        switch (requestCode) {
            case REQUEST_CODE_PICK_CONTACTS:
                travelerInfoItemView = contactsLayout;
                break;
            case REQUEST_CODE_PICK_OTHER_CONTACTS:
                travelerInfoItemView = otherContactsLayout;
                break;
            case REQUEST_CODE_PICK_STANDBY_CONTACTS:
                travelerInfoItemView = standbyContactsLayout;
                break;
        }
        travelerInfoItemView.intentPickContacts(requestCode);
    }

    @OnClick({R.id.sku_order_traveler_info_start_address_layout})
    public void setStartAddress() {
        EventBus.getDefault().post(new EventAction(EventType.CHOOSE_POI));
    }

    @OnClick({R.id.sku_order_traveler_info_contacts_choose_tv
            , R.id.sku_order_traveler_info_contacts_choose_iv})
    public void chooseStandbyContacts() {
        travelerInfoBean.isStandby = !travelerInfoBean.isStandby;
        standbyContactsChooseIV.setSelected(travelerInfoBean.isStandby);
        standbyContactsLayout.setVisibility(travelerInfoBean.isStandby ? View.VISIBLE : View.GONE);
        CommonUtils.hideSoftInput((Activity) getContext());
    }

    //上车时间
    @OnClick({R.id.sku_order_traveler_info_start_time_layout})
    public void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        try {
            if (!TextUtils.isEmpty(travelerInfoBean.serverTime)) {
                calendar.setTime(DateUtils.timeFormat.parse(travelerInfoBean.serverTime + ":00"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (picker == null) {
            picker = new TimePicker((Activity) getContext(), TimePicker.HOUR_24);
            picker.setTitleText("请选择上车时间");
//            if (orderType == 3) {
//                CharterDataUtils charterDataUtils = CharterDataUtils.getInstance();
//                if (charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {
//                    try {
//                        Date date = DateUtils.timeFormat2.parse(charterDataUtils.flightBean.arrivalTime);
//                        picker.setRangeStart(date.getHours(), date.getMinutes());
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
            picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                @Override
                public void onTimePicked(String hour, String minute) {
                    String serverTime = hour + ":" + minute;
                    travelerInfoBean.serverTime = serverTime;
                    timeTV.setText(travelerInfoBean.serverTime);
                    picker.dismiss();
                }
            });
        }
        picker.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.show();
    }

    public void setOrderType(int orderType) {
        setOrderType(orderType, null);
    }

    public void setOrderType(int orderType, CarListBean carListBean) {
        this.orderType = orderType;
        if (orderType == 3) {//组合单
            CharterDataUtils charterDataUtils = CharterDataUtils.getInstance();
            if (charterDataUtils.travelList != null && charterDataUtils.travelList.get(0).routeType == CityRouteBean.RouteType.PICKUP) {// 只接机
                timeLayout.setVisibility(View.GONE);
                addressLayout.setVisibility(View.GONE);
                addressLineView.setVisibility(View.GONE);
                travelerInfoBean.serverTime = charterDataUtils.flightBean.arrivalTime;
            } else if (charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {// 包车加接机
                timeLayout.setVisibility(View.GONE);
                addressLayout.setVisibility(View.GONE);
                addressLineView.setVisibility(View.GONE);
                // 上车时间：航班降落当地时间+90分钟（航班落地时间 xx:xx）可修改，但不能早于航班落地时间
                String arrivalTime = charterDataUtils.flightBean.arrivalTime;
                travelerInfoBean.serverTime = arrivalTime;
//                travelerInfoBean.serverTime = DateUtils.getDifferenceTime(arrivalTime, AHEAD_TIME);
//                timeTV.setHint(String.format("%1$s（航班落地时间 %2$s）", travelerInfoBean.serverTime, arrivalTime));
            } else {//只包车
                timeLayout.setVisibility(View.VISIBLE);
                addressLayout.setVisibility(View.VISIBLE);
                addressLineView.setVisibility(View.VISIBLE);
                timeTV.setHint("09:00（第1天上车时间）");
                addressTV.setHint("添加第1天上车地点");
                travelerInfoBean.serverTime = CombinationOrderActivity.SERVER_TIME.substring(0, CombinationOrderActivity.SERVER_TIME.lastIndexOf(":00"));
            }
            setOtherContactsHide(true);
        } else if (orderType == 5 || orderType == 6) {//线路
            timeLayout.setVisibility(View.VISIBLE);
            addressLayout.setVisibility(View.VISIBLE);
            addressLineView.setVisibility(View.VISIBLE);
            timeTV.setHint("09:00（当地时间）");
            addressTV.setHint("请添加上车地点");
            travelerInfoBean.serverTime = SkuOrderActivity.SERVER_TIME;
            setOtherContactsHide(true);
        } else {//接送次
            timeLayout.setVisibility(View.GONE);
            addressLayout.setVisibility(View.GONE);
            addressLineView.setVisibility(View.GONE);
            checkinET.setVisibility(View.GONE);
            sendMessageSwitchView.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
                @Override
                public void onSwitchStateChange(boolean b) {
                    travelerInfoBean.isSendMessage = b;
                }
            });
            setCarListBean(carListBean);
            if (orderType == 1 && additionalServicePrice != null && !TextUtils.isEmpty(additionalServicePrice.pickupSignPrice)) {//接机
                checkinLayout.setVisibility(View.VISIBLE);
                checkinHintTV.setText("举牌接机");
                checkinET.setHint("接机牌姓名");
            } else if (orderType == 2) {//送机
                if (additionalServicePrice != null && !TextUtils.isEmpty(additionalServicePrice.checkInPrice)) {
                    checkinLayout.setVisibility(View.VISIBLE);
                    checkinHintTV.setText("协助登机Check in");
                }
                flightLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setOtherContactsHide(boolean isHide) {
        if (isHide) {
            otherContactsTopSpaceView.setVisibility(View.GONE);
            contactsSwitchLayout.setVisibility(View.GONE);
            otherContactsLayout.setVisibility(View.GONE);
            sendMessageLayout.setVisibility(View.GONE);
        } else {
            otherContactsTopSpaceView.setVisibility(View.VISIBLE);
            contactsSwitchLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setCarListBean(CarListBean carListBean) {
        if (carListBean == null || carListBean.additionalServicePrice == null) {
            return;
        }
        this.additionalServicePrice = carListBean.additionalServicePrice;
        if (orderType == 1) {//接机
            checkinPriceTV.setText(getContext().getResources().getString(R.string.sign_rmb) + CommonUtils.getCountInteger(carListBean.additionalServicePrice.pickupSignPrice));
        } else if (orderType == 2) {//送机
            checkinPriceTV.setText(getContext().getResources().getString(R.string.sign_rmb) + CommonUtils.getCountInteger(carListBean.additionalServicePrice.checkInPrice));
        }
    }

    public void setAreaCode(String _code, int requestCode) {
        String code = CommonUtils.addPhoneCodeSign(_code);
        switch (requestCode) {
            case REQUEST_CODE_PICK_CONTACTS:
                travelerInfoBean.areaCode = code;
                contactsLayout.setAreaCode(code);
                break;
            case REQUEST_CODE_PICK_OTHER_CONTACTS:
                travelerInfoBean.otherAreaCode = code;
                otherContactsLayout.setAreaCode(code);
                break;
            case REQUEST_CODE_PICK_STANDBY_CONTACTS:
                travelerInfoBean.standbyAreaCode = code;
                standbyContactsLayout.setAreaCode(code);
                break;
        }
    }

    public void setTravelerName(String travelerName) {
        travelerInfoBean.travelerName = travelerName;
        contactsLayout.setName(travelerName);
    }

    public void setTravelerPhone(String travelerPhone) {
        travelerInfoBean.travelerPhone = travelerPhone;
        contactsLayout.setPhone(travelerPhone);
    }

    public void setOtherTravelerName(String travelerName) {
        travelerInfoBean.otherName = travelerName;
        otherContactsLayout.setName(travelerName);
    }

    public void setOtherTravelerPhone(String travelerPhone) {
        travelerInfoBean.otherPhone = travelerPhone;
        otherContactsLayout.setPhone(travelerPhone);
    }


    public void setStandbyTravelerName(String travelerName) {
        travelerInfoBean.standbyName = travelerName;
        standbyContactsLayout.setName(travelerName);
    }

    public void setStandbyTravelerPhone(String travelerPhone) {
        travelerInfoBean.standbyPhone = travelerPhone;
        standbyContactsLayout.setPhone(travelerPhone);
    }

    public void setPlace(PoiBean poiBean) {
        if (poiBean == null) {
            return;
        }
        travelerInfoBean.poiBean = poiBean;
        addressTV.setText(poiBean.placeName);
        if (TextUtils.isEmpty(poiBean.placeDetail)) {
            addressDescriptionTV.setVisibility(View.GONE);
        } else {
            addressDescriptionTV.setVisibility(View.VISIBLE);
            addressDescriptionTV.setText(poiBean.placeDetail);
        }
    }

    private String getText(EditText editText, boolean isRemoveAllBlank) {
        String result = editText.getText() != null ? editText.getText().toString() : "";
        if (!TextUtils.isEmpty(result)) {
            if (isRemoveAllBlank) {
                result.replaceAll(" ", "");
            } else {
                result.trim();
            }
        }
        return result;
    }

    public boolean checkTravelerInfo() {
        TravelerInfoBean infoBean = getTravelerInfoBean();
        if (TextUtils.isEmpty(infoBean.travelerName)) {
            CommonUtils.showToast("请填写联系人姓名!");
            return false;
        } else if (TextUtils.isEmpty(infoBean.travelerPhone)) {
            CommonUtils.showToast("请填写联系人手机号!");
            return false;
        }
        if (!CommonUtils.checkInlandPhoneNumber(infoBean.areaCode, infoBean.travelerPhone)) {
            return false;
        }

        if (infoBean.isStandby) {
            if (TextUtils.isEmpty(infoBean.standbyName)) {
                CommonUtils.showToast("请填写备用联系人姓名!");
                return false;
            }
            if (TextUtils.isEmpty(infoBean.standbyPhone)) {
                CommonUtils.showToast("请填写备用联系人手机号!");
                return false;
            }
            if (!CommonUtils.checkInlandPhoneNumber(infoBean.standbyAreaCode, infoBean.standbyPhone)) {
                return false;
            }
        }

        if (infoBean.isOther) {
            if (TextUtils.isEmpty(infoBean.otherName)) {
                CommonUtils.showToast("请填写乘车人姓名!");
                return false;
            }
            if (TextUtils.isEmpty(infoBean.otherPhone)) {
                CommonUtils.showToast("请填写乘车人手机号!");
                return false;
            }
            if (!CommonUtils.checkInlandPhoneNumber(infoBean.otherAreaCode, infoBean.otherPhone)) {
                return false;
            }
        }
        if (infoBean.isPickup && TextUtils.isEmpty(infoBean.pickName)) {
            CommonUtils.showToast("请填写接机牌姓名!");
            return false;
        }
        return true;
    }

    public int getAdditionalPrice() {
        int result = 0;
        if (orderType == 1 && travelerInfoBean.isPickup) {//接机
            result = CommonUtils.getCountInteger(additionalServicePrice.pickupSignPrice);
        } else if (orderType == 2 && travelerInfoBean.isCheckin) {//送机
            result = CommonUtils.getCountInteger(additionalServicePrice.checkInPrice);
        }
        return result;
    }

    public interface OnSwitchPickOrSendListener {
        public void onSwitchPickOrSend(boolean isSelect, int additionalPrice);
    }

    public void setOnSwitchPickOrSendListener(OnSwitchPickOrSendListener listener) {
        this.onSwitchPickOrSendListener = listener;
    }

    public TravelerInfoBean getTravelerInfoBean() {
        travelerInfoBean.travelerName = getText(contactsLayout.getNameView(), false);
        travelerInfoBean.travelerPhone = getText(contactsLayout.getPhoneView(), true);
        travelerInfoBean.mark = getText(markET, false);
        travelerInfoBean.wechatNo = getText(wechatET, false);
        if (travelerInfoBean.isStandby) {
            travelerInfoBean.standbyName = getText(standbyContactsLayout.getNameView(), false);
            travelerInfoBean.standbyPhone = getText(standbyContactsLayout.getPhoneView(), true);
        }
        if (travelerInfoBean.isOther) {
            travelerInfoBean.otherName = getText(otherContactsLayout.getNameView(), false);
            travelerInfoBean.otherPhone = getText(otherContactsLayout.getPhoneView(), true);
        }
        if (travelerInfoBean.isPickup) {
            travelerInfoBean.pickName = getText(checkinET, false);
        } else {
            travelerInfoBean.pickName = "";
        }
        travelerInfoBean.sendFlight = getText(flightET, false);
        return travelerInfoBean;
    }

    public static class TravelerInfoBean implements Serializable {

        public TravelerInfoBean() {
            final Context context = MyApplication.getAppContext();
            travelerName = UserEntity.getUser().getUserName(context);
            travelerPhone = UserEntity.getUser().getPhone(context);
            areaCode = UserEntity.getUser().getAreaCode(context);
            if (TextUtils.isEmpty(areaCode)) {
                areaCode = "86";
            }
            isOther = false;
            isStandby = false;
        }

        public String travelerName;
        public String travelerPhone;
        public String areaCode;
        public PoiBean poiBean;
        public String mark;
        public String serverTime;
        public String wechatNo;
        public String pickName;
        public boolean isPickup = false;
        public boolean isCheckin = false;
        public boolean isSendMessage = false;
        public boolean isStandby = false;
        public String sendFlight;//送机航班
        public String standbyName;
        public String standbyPhone;
        public String standbyAreaCode = "86";
        public String otherName;
        public String otherPhone;
        public String otherAreaCode = "86";
        public boolean isOther;

        public ContactUsersBean getContactUsersBean() {
            ContactUsersBean contactUsersBean = new ContactUsersBean();
            contactUsersBean.userName = travelerName;
            contactUsersBean.userPhone = travelerPhone;
            contactUsersBean.phoneCode = CommonUtils.removePhoneCodeSign(areaCode);
            if (isStandby) {
                contactUsersBean.user1Name = standbyName;
                contactUsersBean.user1Phone = standbyPhone;
                contactUsersBean.phone1Code = CommonUtils.removePhoneCodeSign(standbyAreaCode);
            }
            if (isOther) {
                contactUsersBean.isForOther = isOther;
                contactUsersBean.isSendMessage = isSendMessage;
                contactUsersBean.otherName = otherName;
                contactUsersBean.otherPhone = otherPhone;
                contactUsersBean.otherphoneCode = CommonUtils.removePhoneCodeSign(otherAreaCode);
            }
            return contactUsersBean;
        }
    }
}
