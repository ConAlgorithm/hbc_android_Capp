package com.hugboga.custom.widget;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
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
import com.hugboga.custom.activity.ChooseCountryActivity;
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
import com.sevenheaven.iosswitch.ShSwitchView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by qingcha on 16/12/17.
 */
public class SkuOrderTravelerInfoView extends LinearLayout implements ShSwitchView.OnSwitchStateChangeListener{

    private static final int AHEAD_TIME = 90 * 60 * 1000;
    public static final int REQUEST_CODE_PICK_CONTACTS = 101;
    public static final int REQUEST_CODE_PICK_OTHER_CONTACTS = 102;

    @Bind(R.id.sku_order_traveler_info_contacts_choose_iv)
    ImageView contactsChooseIV;
    @Bind(R.id.sku_order_traveler_info_contacts_choose_tv)
    TextView contactsChooseTV;

    @Bind(R.id.sku_order_traveler_info_contacts_et)
    EditText contactsET;
    @Bind(R.id.sku_order_traveler_info_code_tv)
    TextView codeTV;
    @Bind(R.id.sku_order_traveler_info_phone_et)
    EditText phoneET;

    @Bind(R.id.sku_order_traveler_info_other_contacts_et)
    EditText otherContactsET;
    @Bind(R.id.sku_order_traveler_info_other_code_tv)
    TextView otherCodeTV;
    @Bind(R.id.sku_order_traveler_info_other_phone_et)
    EditText otherPhoneET;
    @Bind(R.id.sku_order_traveler_info_other_contacts_layout)
    RelativeLayout otherContactsLayout;
    @Bind(R.id.sku_order_traveler_info_other_phone_layout)
    RelativeLayout otherPhoneLayout;

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

        contactsET.setText(travelerInfoBean.travelerName);
        codeTV.setText(CommonUtils.addPhoneCodeSign(travelerInfoBean.areaCode));
        phoneET.setText(travelerInfoBean.travelerPhone);
        checkinSwitchView.setOnSwitchStateChangeListener(this);
    }

    @OnClick({R.id.sku_order_traveler_info_address_book_tv
            , R.id.sku_order_traveler_info_code_arrow_iv
            , R.id.sku_order_traveler_info_code_tv
            , R.id.sku_order_traveler_info_start_address_layout})
    public void setContact(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.sku_order_traveler_info_address_book_tv://通讯录
                intentPickContactsCheckPermisson(REQUEST_CODE_PICK_CONTACTS);
                break;
            case R.id.sku_order_traveler_info_code_tv://区号
            case R.id.sku_order_traveler_info_code_arrow_iv:
                intent = new Intent(getContext(), ChooseCountryActivity.class);
                intent.putExtra("viewId", R.id.sku_order_traveler_info_code_tv);
                getContext().startActivity(intent);
                break;
        }
    }

    @OnClick({R.id.sku_order_traveler_info_other_address_book_tv
            , R.id.sku_order_traveler_info_other_code_arrow_iv
            , R.id.sku_order_traveler_info_other_code_tv})
    public void setOtherContact(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.sku_order_traveler_info_other_address_book_tv://通讯录
                intentPickContactsCheckPermisson(REQUEST_CODE_PICK_OTHER_CONTACTS);
                break;
            case R.id.sku_order_traveler_info_other_code_arrow_iv://区号
            case R.id.sku_order_traveler_info_other_code_tv:
                intent = new Intent(getContext(), ChooseCountryActivity.class);
                intent.putExtra("viewId", R.id.sku_order_traveler_info_other_code_tv);
                getContext().startActivity(intent);
                break;
        }
    }

    @OnClick({R.id.sku_order_traveler_info_start_address_layout})
    public void setStartAddress() {
        EventBus.getDefault().post(new EventAction(EventType.CHOOSE_POI));
    }

    @OnClick({R.id.sku_order_traveler_info_contacts_choose_tv
            , R.id.sku_order_traveler_info_contacts_choose_iv})
    public void chooseOtherContacts() {
        travelerInfoBean.isOther = !travelerInfoBean.isOther;
        contactsChooseIV.setSelected(travelerInfoBean.isOther);
        if (travelerInfoBean.isOther) {
            otherContactsLayout.setVisibility(View.VISIBLE);
            otherPhoneLayout.setVisibility(View.VISIBLE);
            sendMessageLayout.setVisibility(View.VISIBLE);
        } else {
            otherContactsLayout.setVisibility(View.GONE);
            otherPhoneLayout.setVisibility(View.GONE);
            sendMessageLayout.setVisibility(View.GONE);
        }
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
        } else if (orderType == 5 || orderType == 6) {//线路
            timeLayout.setVisibility(View.VISIBLE);
            addressLayout.setVisibility(View.VISIBLE);
            addressLineView.setVisibility(View.VISIBLE);
            timeTV.setHint("09:00（当地时间）");
            addressTV.setHint("请添加上车地点");
            travelerInfoBean.serverTime = SkuOrderActivity.SERVER_TIME;
        } else {//接送次
            timeLayout.setVisibility(View.GONE);
            addressLayout.setVisibility(View.GONE);
            addressLineView.setVisibility(View.GONE);
            contactsChooseIV.setVisibility(View.VISIBLE);
            contactsChooseTV.setVisibility(View.VISIBLE);
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

    @TargetApi(23)
    public boolean requestPermisson(int requestCode) {
        if (checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions((Activity)getContext(), new String[]{Manifest.permission.READ_CONTACTS}, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public void intentPickContacts(int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        ((Activity)getContext()).startActivityForResult(intent, requestCode);
    }

    public void intentPickContactsCheckPermisson(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23 && requestPermisson(requestCode)) {
            intentPickContacts(requestCode);
        } else {
            intentPickContacts(requestCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_CONTACTS:
            case REQUEST_CODE_PICK_OTHER_CONTACTS:
                intentPickContacts(requestCode);
                break;
        }
    }

    public void setAreaCode(String code) {
        travelerInfoBean.areaCode = code;
        codeTV.setText(CommonUtils.addPhoneCodeSign(code));
    }

    public void setTravelerName(String travelerName) {
        travelerInfoBean.travelerName = travelerName;
        contactsET.setText(travelerName);
    }

    public void setTravelerPhone(String travelerPhone) {
        travelerInfoBean.travelerPhone = travelerPhone;
        phoneET.setText(travelerPhone);
    }

    public void setOtherAreaCode(String code) {
        travelerInfoBean.otherAreaCode = code;
        otherCodeTV.setText(CommonUtils.addPhoneCodeSign(code));
    }

    public void setOtherTravelerName(String travelerName) {
        travelerInfoBean.otherName = travelerName;
        otherContactsET.setText(travelerName);
    }

    public void setOtherTravelerPhone(String travelerPhone) {
        travelerInfoBean.otherPhone = travelerPhone;
        otherPhoneET.setText(travelerPhone);
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
        if (infoBean.isOther) {
            if (TextUtils.isEmpty(infoBean.otherName)) {
                CommonUtils.showToast("请填写乘车人姓名!");
                return false;
            }
            if (TextUtils.isEmpty(infoBean.otherPhone)) {
                CommonUtils.showToast("请填写乘车人电话!");
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
        travelerInfoBean.travelerName = getText(contactsET, false);
        travelerInfoBean.travelerPhone = getText(phoneET, true);
        travelerInfoBean.mark = getText(markET, false);
        travelerInfoBean.wechatNo = getText(wechatET, false);
        if (travelerInfoBean.isOther) {
            travelerInfoBean.otherName = getText(otherContactsET, false);
            travelerInfoBean.otherPhone = getText(otherPhoneET, true);
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
        public String sendFlight;//送机航班
        public String otherName;
        public String otherPhone;
        public String otherAreaCode = "86";
        public boolean isOther;

        public ContactUsersBean getContactUsersBean() {
            ContactUsersBean contactUsersBean = new ContactUsersBean();
            contactUsersBean.userName = travelerName;
            contactUsersBean.userPhone = travelerPhone;
            contactUsersBean.phoneCode = CommonUtils.removePhoneCodeSign(areaCode);
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
