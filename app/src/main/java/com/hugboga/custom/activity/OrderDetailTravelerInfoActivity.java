package com.hugboga.custom.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderContactBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestOrderEdit;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.TravelerInfoDataCheck;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.SkuOrderTravelerInfoView;
import com.hugboga.custom.widget.TravelerInfoItemView;
import com.sevenheaven.iosswitch.ShSwitchView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;

public class OrderDetailTravelerInfoActivity extends BaseActivity implements TravelerInfoDataCheck.OnDataChangeListener {

    @Bind(R.id.sku_order_traveler_info_title_tv)
    TextView travelerInfoTitleTV;

    @Bind(R.id.traveler_info_contacts_layout)
    TravelerInfoItemView contactsLayout;

    @Bind(R.id.sku_order_traveler_info_contacts_choose_iv)
    ImageView standbyContactsChooseIV;
    @Bind(R.id.sku_order_traveler_info_contacts_choose_tv)
    TextView standbyContactsChooseTV;
    @Bind(R.id.traveler_info_standby_contacts_layout)
    TravelerInfoItemView standbyContactsLayout;

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
    @Bind(R.id.sku_order_traveler_info_start_address_arrow_iv)
    ImageView addressArrowIV;

    @Bind(R.id.sku_order_traveler_info_wechat_layout)
    RelativeLayout wechatLayout;
    @Bind(R.id.sku_order_traveler_info_wechat_et)
    EditText wechatET;

    @Bind(R.id.sku_order_traveler_info_mark_layout)
    RelativeLayout markLayout;
    @Bind(R.id.sku_order_traveler_info_mark_et)
    EditText markET;

    @Bind(R.id.view_order_traveler_info_bottom_hint_tv)
    TextView bottomHintTV;

    private TimePicker picker;
    String serverTime = "09:00";
    private DialogUtil mDialogUtil;
    private boolean isStandby = false;

    private ContactUsersBean contactUsersBean;
    private OrderBean orderBean;
    private PoiBean startPoiBean;
    RequestOrderEdit.Params requestParams;

    private boolean isRequested = false;

    private TravelerInfoDataCheck dataCheck;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_detail_traveler_info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            orderBean = (OrderBean) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
            contactUsersBean = (ContactUsersBean)savedInstanceState.getSerializable("contactUsersBean");
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                orderBean = (OrderBean) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        EventBus.getDefault().register(this);

        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideSoftInput();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (orderBean != null) {
            outState.putSerializable(Constants.PARAMS_DATA, orderBean);
        }
        if (contactUsersBean != null) {
            outState.putSerializable("contactUsersBean", contactUsersBean);
        }
    }

    public void initView() {
        mDialogUtil = DialogUtil.getInstance(this);

        initDefaultTitleBar();
        fgTitle.setText(R.string.traveler_info_title);
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                finish();
            }
        });
        fgRightTV.setText(R.string.traveler_info_save);
        fgRightTV.setEnabled(false);
        fgRightTV.setTextColor(0xFFA8A8A8);
        fgRightTV.setVisibility(View.VISIBLE);
        fgRightTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        if (!TextUtils.isEmpty(orderBean.serviceStartTime)) {
            String time = orderBean.serviceStartTime.substring(0, orderBean.serviceStartTime.lastIndexOf(":00"));
            timeTV.setText(time + "(当地时间)");
        }

        if (!TextUtils.isEmpty(orderBean.startAddress)) {
            addressTV.setText(orderBean.startAddress);
            if (!TextUtils.isEmpty(orderBean.startAddressDetail)) {
                addressDescriptionTV.setVisibility(View.VISIBLE);
                addressDescriptionTV.setText(orderBean.startAddressDetail);
            } else {
                addressDescriptionTV.setVisibility(View.GONE);
            }
        }

        wechatET.setText(orderBean.userWechat);

        if (orderBean.orderType == 3 || orderBean.orderType == 888) {
            if (orderBean.journeyList != null && orderBean.journeyList.size() > 1) {
                OrderBean.JourneyItem journeyItem = orderBean.journeyList.get(0);
                if (journeyItem.pickup != null && journeyItem.journey != null) {//包车加接机
                    addressLayout.setVisibility(View.GONE);
                    timeLayout.setVisibility(View.GONE);
                    addressLineView.setVisibility(View.GONE);
                } else if (journeyItem.pickup != null) {//只接机
                    addressLayout.setVisibility(View.GONE);
                    timeLayout.setVisibility(View.GONE);
                    addressLineView.setVisibility(View.GONE);
                }
            } else {//包车
                addressLayout.setVisibility(View.VISIBLE);
                timeLayout.setVisibility(View.VISIBLE);
                addressLineView.setVisibility(View.VISIBLE);
            }
            setOtherContactsHide(true);
            setBottomHint();
        } else if (orderBean.orderType == 5 || orderBean.orderType == 6) {//线路
            addressLayout.setVisibility(View.VISIBLE);
            timeLayout.setVisibility(View.VISIBLE);
            addressLineView.setVisibility(View.VISIBLE);
            setOtherContactsHide(true);
            setBottomHint();
        } else {
            timeLayout.setVisibility(View.GONE);
            addressLayout.setVisibility(View.GONE);
            addressLineView.setVisibility(View.GONE);
            if (orderBean.orderType == 1 && !TextUtils.isEmpty(orderBean.flightBrandSign) && !TextUtils.isEmpty(orderBean.flightBrandSign.trim())) {//接机
                checkinLayout.setVisibility(View.VISIBLE);
                checkinSwitchView.setVisibility(View.GONE);
                checkinPriceTV.setVisibility(View.GONE);
                checkinET.setVisibility(View.VISIBLE);
                checkinET.setText(orderBean.flightBrandSign);
                checkinStarTV.setVisibility(View.VISIBLE);
            } else if (orderBean.orderType == 2) {//送机
                flightLayout.setVisibility(View.VISIBLE);
                flightET.setText(orderBean.flightNo);
            }
            bottomHintTV.setVisibility(View.GONE);
        }

        contactUsersBean = new ContactUsersBean();
        ArrayList<OrderContactBean> userList = orderBean.userList;
        if (userList != null && userList.size() > 0) {
            String userName = userList.get(0).name;
            String userPhone = userList.get(0).mobile;
            contactsLayout.init(userName, CommonUtils.addPhoneCodeSign(userList.get(0).areaCode), userPhone);
            markET.setText(orderBean.userRemark);
            for (int i = 0; i < userList.size(); i++) {
                if (i == 0) {
                    contactUsersBean.userName = userList.get(i).name;
                    contactUsersBean.userPhone = userList.get(i).mobile;
                    contactUsersBean.phoneCode = userList.get(i).areaCode;
                } else if (i == 1) {
                    contactUsersBean.user1Name = userList.get(i).name;
                    contactUsersBean.user1Phone = userList.get(i).mobile;
                    contactUsersBean.phone1Code = userList.get(i).areaCode;
                } else if (i == 2) {
                    contactUsersBean.user2Name = userList.get(i).name;
                    contactUsersBean.user2Phone = userList.get(i).mobile;
                    contactUsersBean.phone2Code = userList.get(i).areaCode;
                }
            }
            if (!TextUtils.isEmpty(contactUsersBean.user1Name) && !TextUtils.isEmpty(contactUsersBean.user1Phone)) {
                standbyContactsLayout.init(contactUsersBean.user1Name, CommonUtils.addPhoneCodeSign(contactUsersBean.phone1Code), contactUsersBean.user1Phone);
                setStandbyContactsShow(true);
            } else {
                setStandbyContactsShow(false);
            }
        }
        ArrayList<OrderContactBean> realUserList = orderBean.realUserList;
        if (realUserList != null && realUserList.size() > 0 && !TextUtils.isEmpty(realUserList.get(0).name)) {
            contactUsersBean.otherName = realUserList.get(0).name;
            contactUsersBean.otherPhone = realUserList.get(0).mobile;
            contactUsersBean.otherphoneCode = realUserList.get(0).areaCode;
        }
        contactUsersBean.isSendMessage = "1".equals(orderBean.realSendSms);
        boolean isRealUser = "2".equals(orderBean.isRealUser);
        contactUsersBean.isForOther = isRealUser;
        if (isRealUser) {
            otherContactsLayout.init(contactUsersBean.otherName, CommonUtils.addPhoneCodeSign(contactUsersBean.otherphoneCode), contactUsersBean.otherPhone);
            setOtherContactsHide(false);
            sendMessageSwitchView.setOn(contactUsersBean.isSendMessage);
            contactsSwitchView.setOn(true);
        }

        sendMessageSwitchView.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean b) {
                contactUsersBean.isSendMessage = b;
                if (dataCheck != null) {
                    dataCheck.checkIsSendMessage(b);
                }
            }
        });
        contactsSwitchView.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean b) {
                contactUsersBean.isForOther = !contactUsersBean.isForOther;
                if (contactUsersBean.isForOther) {
                    otherContactsLayout.setVisibility(View.VISIBLE);
                    sendMessageLayout.setVisibility(View.VISIBLE);
                } else {
                    otherContactsLayout.setVisibility(View.GONE);
                    sendMessageLayout.setVisibility(View.GONE);
                }
                CommonUtils.hideSoftInput(OrderDetailTravelerInfoActivity.this);
                if (dataCheck != null) {
                    dataCheck.checkIsForOther(b);
                }
            }
        });

        contactsLayout.setRequestCode(SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS);
        standbyContactsLayout.setRequestCode(SkuOrderTravelerInfoView.REQUEST_CODE_PICK_STANDBY_CONTACTS);
        otherContactsLayout.setRequestCode(SkuOrderTravelerInfoView.REQUEST_CODE_PICK_OTHER_CONTACTS);

        otherContactsLayout.setContactsHintText("乘车人");

        requestParams = new RequestOrderEdit.Params();

        if (orderBean.orderStatus.code > 5) {
            fgRightTV.setVisibility(View.GONE);
            fgRightTV.setOnClickListener(null);

            standbyContactsChooseIV.setVisibility(View.GONE);
            standbyContactsChooseTV.setVisibility(View.GONE);

            contactsLayout.setReadOnly();

            if (!isStandby) {
                standbyContactsLayout.setVisibility(View.GONE);
            } else {
                standbyContactsLayout.setReadOnly();
            }

            if (contactUsersBean.isForOther) {
                contactsSwitchView.setVisibility(View.GONE);
                otherContactsLayout.setReadOnly();
                sendMessageLayout.setVisibility(View.GONE);
                otherContactsLayout.setBottomLineVisibility(View.GONE);
            } else {
                setOtherContactsHide(true);
            }

//            checkinStarTV.setVisibility(View.GONE);
            setItemEnabled(flightLayout, flightET, orderBean.flightNo);
            setItemEnabled(checkinLayout, checkinET, orderBean.flightBrandSign);
            setItemEnabled(wechatLayout, wechatET, orderBean.userWechat);
            setItemEnabled(markLayout, markET, orderBean.userRemark);

            setItemEnabled(timeLayout, timeTV, timeTV.getText() != null ? timeTV.getText().toString() : "");

            setItemEnabled(addressLayout, timeTV, addressTV.getText() != null ? addressTV.getText().toString() : "");

            timeLayout.setEnabled(false);
            if (TextUtils.isEmpty(addressTV.getText() != null ? addressTV.getText().toString() : "")) {
                addressLayout.setVisibility(LinearLayout.GONE);
                addressLineView.setVisibility(View.GONE);
            } else {
                setHideTV(addressDescriptionTV);
                addressLayout.setEnabled(false);
                addressTV.setEnabled(false);
                addressArrowIV.setVisibility(View.INVISIBLE);
            }
        } else {
            dataCheck = new TravelerInfoDataCheck();
            dataCheck.contactsName = contactsLayout.getNameStr();
            dataCheck.contactsPhone = contactsLayout.getPhoneStr();
            dataCheck.contactsAreaCode = contactsLayout.getAreaCodeStr();

            dataCheck.isStandby = isStandby;
            dataCheck.standbyContactsName = standbyContactsLayout.getNameStr();
            dataCheck.standbyContactsPhone = standbyContactsLayout.getPhoneStr();
            dataCheck.standbyContactsAreaCode = standbyContactsLayout.getAreaCodeStr();

            dataCheck.otherContactsName = otherContactsLayout.getNameStr();
            dataCheck.otherContactsPhone = otherContactsLayout.getPhoneStr();
            dataCheck.otherContactsAreaCode = otherContactsLayout.getAreaCodeStr();
            dataCheck.isSendMessage = contactUsersBean.isSendMessage;
            dataCheck.isForOther = contactUsersBean.isForOther;

            dataCheck.time = timeTV.getText() != null ? timeTV.getText().toString() : "";
            dataCheck.address = orderBean.startAddress;
            dataCheck.addressDescription = orderBean.startAddressDetail;
            dataCheck.wechat = orderBean.userWechat;

            dataCheck.flightBrandSign = orderBean.userWechat;
            dataCheck.flight = orderBean.flightNo;
            dataCheck.mark = orderBean.userRemark;

            dataCheck.contactsET = contactsLayout.getNameView();
            dataCheck.phoneET = contactsLayout.getPhoneView();
            dataCheck.standbyContactsET = standbyContactsLayout.getNameView();
            dataCheck.standbyPhoneET = standbyContactsLayout.getPhoneView();
            dataCheck.otherContactsET = otherContactsLayout.getNameView();
            dataCheck.otherPhoneET = otherContactsLayout.getPhoneView();
            dataCheck.checkinET = checkinET;
            dataCheck.flightET = flightET;
            dataCheck.wechatET = wechatET;
            dataCheck.markET = markET;

            dataCheck.setListener(this);
        }
    }

    public void setBottomHint() {
        bottomHintTV.setVisibility(View.VISIBLE);
        String hint = "·  如果在司导接单后，您需要修改上车时间、上车地点，请先在APP-私聊 中与司导沟通确认，再提交修改~ 平台将按照聊天记录为您提供后续保障";
        String clickHint = "APP-私聊";
        OrderUtils.genCLickSpan(this, bottomHintTV, hint, clickHint, getResources().getColor(R.color.default_highlight_blue), new OrderUtils.MyCLickSpan.OnSpanClickListener() {
            @Override
            public void onSpanClick(View view) {
                Intent intent = new Intent(OrderDetailTravelerInfoActivity.this, MainActivity.class);
                startActivity(intent);
                EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 2));
            }
        });
    }

    private void setHideTV(TextView textView) {
        if (textView.getText() == null || TextUtils.isEmpty(textView.getText().toString())) {
            textView.setHint("");
        }
        textView.setEnabled(false);
    }

    private void setItemEnabled(RelativeLayout layout, TextView textView, String text) {
        if (TextUtils.isEmpty(text)) {
            layout.setVisibility(LinearLayout.GONE);
        } else {
            textView.setEnabled(false);
        }
    }

    @OnClick({R.id.sku_order_traveler_info_contacts_choose_tv
            , R.id.sku_order_traveler_info_contacts_choose_iv})
    public void chooseStandbyContacts() {
        isStandby = !isStandby;
        setStandbyContactsShow(isStandby);
        if (dataCheck != null) {
            dataCheck.checkIsStandby(isStandby);
        }
    }

    public void setStandbyContactsShow(boolean isShow) {
        isStandby = isShow;
        standbyContactsChooseIV.setSelected(isShow);
        standbyContactsLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
        CommonUtils.hideSoftInput(this);
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
            otherContactsLayout.setVisibility(View.VISIBLE);
            sendMessageLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.sku_order_traveler_info_start_address_layout)
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.sku_order_traveler_info_start_address_layout://上车地点
                if (orderBean.startAddressPoi != null) {
                    Bundle bundlePoiSearch = new Bundle();
                    bundlePoiSearch.putInt(PoiSearchActivity.KEY_CITY_ID, orderBean.serviceCityId);
                    bundlePoiSearch.putString(PoiSearchActivity.KEY_LOCATION, orderBean.startAddressPoi);
                    intent = new Intent(OrderDetailTravelerInfoActivity.this, PoiSearchActivity.class);
                    intent.putExtras(bundlePoiSearch);
                    intent.putExtra("mBusinessType",orderBean.orderType);
                    startActivity(intent);
                }
                break;
        }
    }


    public void intentPickContacts(int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS:
            case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_OTHER_CONTACTS:
            case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_STANDBY_CONTACTS:
                intentPickContacts(requestCode);
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_COUNTRY_BACK:
                if (!(action.getData() instanceof AreaCodeBean)) {
                    break;
                }
                AreaCodeBean areaCodeBean = (AreaCodeBean) action.getData();
                if (areaCodeBean == null) {
                    break;
                }
                String code = CommonUtils.addPhoneCodeSign(areaCodeBean.getCode());
                switch (areaCodeBean.viewId) {
                    case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS:
                        contactsLayout.setAreaCode(code);
                        if (dataCheck != null) {
                            dataCheck.checkContactsAreaCode(code);
                        }
                        break;
                    case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_OTHER_CONTACTS:
                        otherContactsLayout.setAreaCode(code);
                        if (dataCheck != null) {
                            dataCheck.checkOtherContactsAreaCode(code);
                        }
                        break;
                    case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_STANDBY_CONTACTS:
                        standbyContactsLayout.setAreaCode(code);
                        if (dataCheck != null) {
                            dataCheck.checkStandbyContactsAreaCode(code);
                        }
                        break;
                }
                break;
            case CHOOSE_POI_BACK:
                if (!(action.getData() instanceof PoiBean)) {
                    break;
                }
                PoiBean poiBean = (PoiBean) action.getData();
                this.startPoiBean = poiBean;
                if (poiBean == null) {
                    break;
                }
                addressTV.setText(poiBean.placeName);
                if (!TextUtils.isEmpty(poiBean.placeDetail)) {
                    addressDescriptionTV.setVisibility(View.VISIBLE);
                    addressDescriptionTV.setText(poiBean.placeDetail);
                } else {
                    addressDescriptionTV.setVisibility(View.GONE);
                    addressDescriptionTV.setText("");
                }
                if (dataCheck != null) {
                    dataCheck.checkAddress(poiBean.placeName);
                    dataCheck.checkAddressDetail(poiBean.placeDetail);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS == requestCode
                || SkuOrderTravelerInfoView.REQUEST_CODE_PICK_OTHER_CONTACTS == requestCode
                || SkuOrderTravelerInfoView.REQUEST_CODE_PICK_STANDBY_CONTACTS == requestCode) {
            Uri result = data.getData();
            String[] contact = PhoneInfo.getPhoneContacts(this, result);
            if (contact == null || contact.length < 2) {
                return;
            }
            if (!TextUtils.isEmpty(contact[0])) {
                switch (requestCode) {
                    case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS:
                        contactsLayout.setName(contact[0]);
                        break;
                    case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_OTHER_CONTACTS:
                        otherContactsLayout.setName(contact[0]);
                        break;
                    case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_STANDBY_CONTACTS:
                        standbyContactsLayout.setName(contact[0]);
                        break;
                }
            }
            if (!TextUtils.isEmpty(contact[1])) {
                String phone = contact[1];
                if (!TextUtils.isEmpty(phone)) {
                    phone = phone.replace("+86", "");//此处拷贝自以前代码。。。
                    phone = CommonUtils.getNum(phone);
                }
                switch (requestCode) {
                    case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS:
                        contactsLayout.setPhone(phone);
                        if (dataCheck != null) {
                            dataCheck.checkContactsPhone(phone);
                        }
                        break;
                    case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_OTHER_CONTACTS:
                        otherContactsLayout.setPhone(phone);
                        if (dataCheck != null) {
                            dataCheck.checkOtherContactsPhone(phone);
                        }
                        break;
                    case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_STANDBY_CONTACTS:
                        standbyContactsLayout.setPhone(phone);
                        if (dataCheck != null) {
                            dataCheck.checkStandbyContactsPhone(phone);
                        }
                        break;
                }
            }
        }
    }

    //上车时间
    @OnClick({R.id.sku_order_traveler_info_start_time_layout})
    public void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(DateUtils.timeFormat.parse(orderBean.serviceStartTime));
        } catch (ParseException e) {
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 0);
        }
        picker = new TimePicker(activity, TimePicker.HOUR_24);
        picker.setTitleText("请选择上车时间");
        picker.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                serverTime = hour + ":" + minute;
                timeTV.setText(serverTime + "(当地时间)");
                orderBean.serviceStartTime = serverTime + ":00";
                if (dataCheck != null) {
                    dataCheck.checkTime(timeTV.getText().toString());
                }
                picker.dismiss();
            }
        });

        picker.show();
    }

    private void sendRequest() {
        if (isRequested) {
            return;
        }
        if (TextUtils.isEmpty(contactsLayout.getName())) {
            CommonUtils.showToast("请填写联系人姓名");
            return;
        }
        if (TextUtils.isEmpty(contactsLayout.getPhone().toString())) {
            CommonUtils.showToast("请填写联系人手机号");
            return;
        }
        if (!CommonUtils.checkInlandPhoneNumber(contactsLayout.getAreaCode().toString(), contactsLayout.getPhone().toString())) {
            return;
        }

        if (isStandby) {
            if (TextUtils.isEmpty(standbyContactsLayout.getName())) {
                CommonUtils.showToast("请填写备用联系人姓名!");
                return;
            }
            if (TextUtils.isEmpty(standbyContactsLayout.getPhone())) {
                CommonUtils.showToast("请填写备用联系人手机号!");
                return;
            }
            if (!CommonUtils.checkInlandPhoneNumber(standbyContactsLayout.getAreaCode().toString(), standbyContactsLayout.getPhone().toString())) {
                return;
            }
        }
        if (contactUsersBean.isForOther) {
            if (TextUtils.isEmpty(otherContactsLayout.getName())) {
                CommonUtils.showToast("请填写乘车人姓名!");
                return;
            }
            if (TextUtils.isEmpty(otherContactsLayout.getPhone())) {
                CommonUtils.showToast("请填写乘车人手机号!");
                return;
            }
            if (!CommonUtils.checkInlandPhoneNumber(otherContactsLayout.getAreaCode().toString(), otherContactsLayout.getPhone().toString())) {
                return;
            }
        }
        if (checkinLayout.getVisibility() == View.VISIBLE && TextUtils.isEmpty(checkinET.getText())) {
            CommonUtils.showToast("请填写接机牌姓名");
            return;
        }
        isRequested = true;
        mDialogUtil.showLoadingDialog();
        requestParams.orderNo = orderBean.orderNo;
        requestParams.orderType = orderBean.orderType;//int 可选1-接机；2-送机；3-日租；4-次租
        requestParams.userRemark = TextUtils.isEmpty(markET.getText()) ? "" : markET.getText().toString();//备注
        requestParams.serviceRecTime = orderBean.serviceStartTime;//上车时间
        requestParams.startAddress = TextUtils.isEmpty(addressTV.getText()) ? "" : addressTV.getText().toString();//上车地点
        requestParams.startAddressDetail = TextUtils.isEmpty(addressDescriptionTV.getText()) ? "" : addressDescriptionTV.getText().toString();
        requestParams.startAddressPoi = startPoiBean == null ? "" : startPoiBean.location;
        requestParams.userWechat = TextUtils.isEmpty(wechatET.getText()) ? "" : wechatET.getText().toString();
        requestParams.userEx = getUserExJson();
        requestParams.realUserEx = getRealUserExJson();
        requestParams.realSendSms = contactUsersBean.isSendMessage ? 1 : 0;
        if (orderBean.orderType == 1) {
            requestParams.flightBrandSign = TextUtils.isEmpty(checkinET.getText()) ? "" : checkinET.getText().toString();//接送机接机牌名称
        } else if (orderBean.orderType == 2) {
            requestParams.flightNo = TextUtils.isEmpty(flightET.getText()) ? "" : flightET.getText().toString();//航班号
        }
        requestData(new RequestOrderEdit(this, requestParams));
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestOrderEdit) {
            CommonUtils.showToast("信息修改成功");
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_INFO, orderBean.orderNo));
            finish();
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        isRequested = false;
    }

    private String getUserExJson() {
        StringBuffer userExJson = new StringBuffer();
        userExJson.append("[");

        if (!TextUtils.isEmpty(contactsLayout.getName())) {
            userExJson.append("{name:\"" + contactsLayout.getName().toString() + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(contactsLayout.getAreaCode().toString()) + "\",mobile:\"" + contactsLayout.getPhone().toString() + "\"}");
        }

        if (isStandby && !TextUtils.isEmpty(standbyContactsLayout.getName())) {
            userExJson.append(",{name:\"" + standbyContactsLayout.getName().toString() + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(standbyContactsLayout.getAreaCode().toString()) + "\",mobile:\"" + standbyContactsLayout.getPhone().toString() + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user2Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user2Name + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(contactUsersBean.phone2Code) + "\",mobile:\"" + contactUsersBean.user2Phone + "\"}");
        }
        userExJson.append("]");
        return userExJson.toString();
    }

    private String getRealUserExJson() {
        StringBuffer realUserExJson = new StringBuffer();
        if (contactUsersBean.isForOther && !TextUtils.isEmpty(otherContactsLayout.getName())) {
            realUserExJson.append("[");
            realUserExJson.append("{name:\"" + otherContactsLayout.getName().toString() + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(otherContactsLayout.getAreaCode().toString()) + "\",mobile:\"" + otherContactsLayout.getPhone().toString() + "\"}");
            realUserExJson.append("]");
        }
        return realUserExJson.toString();
    }

    @Override
    public void onDataChange(boolean isChange) {
        fgRightTV.setEnabled(isChange);
        fgRightTV.setTextColor(isChange ? getResources().getColor(R.color.default_black) : 0xFFA8A8A8);
    }
}
