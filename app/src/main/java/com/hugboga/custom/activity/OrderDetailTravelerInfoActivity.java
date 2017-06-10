package com.hugboga.custom.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
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
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.SkuOrderTravelerInfoView;
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

public class OrderDetailTravelerInfoActivity extends BaseActivity{

    @Bind(R.id.sku_order_traveler_info_title_tv)
    TextView travelerInfoTitleTV;
    @Bind(R.id.sku_order_traveler_info_contacts_choose_iv)
    ImageView contactsChooseIV;
    @Bind(R.id.sku_order_traveler_info_contacts_choose_tv)
    TextView contactsChooseTV;

    @Bind(R.id.sku_order_traveler_info_code_arrow_iv)
    ImageView codeArrowIV;
    @Bind(R.id.sku_order_traveler_info_contacts_et)
    EditText contactsET;
    @Bind(R.id.sku_order_traveler_info_code_tv)
    TextView codeTV;
    @Bind(R.id.sku_order_traveler_info_phone_et)
    EditText phoneET;
    @Bind(R.id.sku_order_traveler_info_contacts_star_tv)
    TextView contactsStarTV;
    @Bind(R.id.sku_order_traveler_info_phone_star_tv)
    TextView phoneStarTV;
    @Bind(R.id.sku_order_traveler_info_address_book_tv)
    TextView addressBookTV;
    @Bind(R.id.sku_order_traveler_info_address_book_iv)
    ImageView addressBookIV;


    @Bind(R.id.sku_order_traveler_info_other_code_arrow_iv)
    ImageView otherCodeArrowIV;
    @Bind(R.id.sku_order_traveler_info_other_contacts_et)
    EditText otherContactsET;
    @Bind(R.id.sku_order_traveler_info_other_phone_et)
    EditText otherPhoneET;
    @Bind(R.id.sku_order_traveler_info_other_contacts_layout)
    RelativeLayout otherContactsLayout;
    @Bind(R.id.sku_order_traveler_info_other_phone_layout)
    RelativeLayout otherPhoneLayout;
    @Bind(R.id.sku_order_traveler_info_other_code_tv)
    TextView otherCodeTV;
    @Bind(R.id.sku_order_traveler_info_other_contacts_star_tv)
    TextView otherContactsStarTV;
    @Bind(R.id.sku_order_traveler_info_other_phone_star_tv)
    TextView otherPhoneStarTV;
    @Bind(R.id.sku_order_traveler_info_other_address_book_tv)
    TextView otherAddressBookTV;
    @Bind(R.id.sku_order_traveler_info_other_address_book_iv)
    ImageView otherAddressBookIV;

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

    private TimePicker picker;
    String serverTime = "09:00";
    private DialogUtil mDialogUtil;

    private ContactUsersBean contactUsersBean;
    private OrderBean orderBean;
    RequestOrderEdit.Params requestParams;

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
        fgTitle.setText("出行人信息");
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                finish();
            }
        });
        fgRightTV.setText("保存");
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

        if ((orderBean.orderType == 3 || orderBean.orderType == 888) && orderBean.journeyList != null && orderBean.journeyList.size() > 1) {
            OrderBean.JourneyItem journeyItem = orderBean.journeyList.get(0);
            if (journeyItem.pickup != null && journeyItem.journey != null) {//包车加接机
                addressLayout.setVisibility(View.GONE);
                timeLayout.setVisibility(View.VISIBLE);
                addressLineView.setVisibility(View.GONE);
            } else if (journeyItem.pickup != null) {//只接机
                addressLayout.setVisibility(View.GONE);
                timeLayout.setVisibility(View.GONE);
                addressLineView.setVisibility(View.GONE);
            } else {//包车
                addressLayout.setVisibility(View.VISIBLE);
                timeLayout.setVisibility(View.VISIBLE);
                addressLineView.setVisibility(View.VISIBLE);
            }
        } else if (orderBean.orderType == 5 || orderBean.orderType == 6) {//线路
            addressLayout.setVisibility(View.VISIBLE);
            timeLayout.setVisibility(View.VISIBLE);
            addressLineView.setVisibility(View.VISIBLE);
        } else {
            timeLayout.setVisibility(View.GONE);
            addressLayout.setVisibility(View.GONE);
            addressLineView.setVisibility(View.GONE);
            contactsChooseIV.setVisibility(View.VISIBLE);
            contactsChooseTV.setVisibility(View.VISIBLE);
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
        }

        contactUsersBean = new ContactUsersBean();
        ArrayList<OrderContactBean> userList = orderBean.userList;
        if (userList != null && userList.size() > 0) {
            String userName = userList.get(0).name;
            String userPhone = userList.get(0).mobile;
            contactsET.setText(userName);
            codeTV.setText(CommonUtils.addPhoneCodeSign(userList.get(0).areaCode));
            phoneET.setText(userPhone);
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
            otherContactsET.setText(contactUsersBean.otherName);
            otherCodeTV.setText(CommonUtils.addPhoneCodeSign(contactUsersBean.otherphoneCode));
            otherPhoneET.setText(contactUsersBean.otherPhone);
            setOtherContactsShow(true);
            sendMessageSwitchView.setOn(contactUsersBean.isSendMessage);
        }

        sendMessageSwitchView.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean b) {
                contactUsersBean.isSendMessage = b;
            }
        });

        requestParams = new RequestOrderEdit.Params();

        if (orderBean.orderStatus.code > 5) {
            fgRightTV.setVisibility(View.GONE);
            fgRightTV.setOnClickListener(null);

            contactsChooseIV.setVisibility(View.GONE);
            contactsChooseTV.setVisibility(View.GONE);

            setHideTV(contactsET);
            setHideTV(codeTV);
            setHideTV(phoneET);
            codeArrowIV.setEnabled(false);
            codeArrowIV.setVisibility(View.INVISIBLE);
            addressBookTV.setVisibility(View.INVISIBLE);
            addressBookIV.setVisibility(View.INVISIBLE);
            contactsStarTV.setVisibility(View.INVISIBLE);
            phoneStarTV.setVisibility(View.INVISIBLE);

            setHideTV(otherContactsET);
            setHideTV(otherCodeTV);
            setHideTV(otherPhoneET);
            otherCodeArrowIV.setEnabled(false);
            otherCodeArrowIV.setVisibility(View.INVISIBLE);
            otherAddressBookTV.setVisibility(View.INVISIBLE);
            otherAddressBookIV.setVisibility(View.INVISIBLE);
            otherContactsStarTV.setVisibility(View.INVISIBLE);
            otherPhoneStarTV.setVisibility(View.INVISIBLE);

            checkinStarTV.setVisibility(View.GONE);
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
        }
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
    public void chooseOtherContacts() {
        contactUsersBean.isForOther = !contactUsersBean.isForOther;
        setOtherContactsShow(contactUsersBean.isForOther);
    }

    public void setOtherContactsShow(boolean isShow) {
        contactsChooseIV.setSelected(isShow);
        if (isShow) {
            otherContactsLayout.setVisibility(View.VISIBLE);
            otherPhoneLayout.setVisibility(View.VISIBLE);
//            if (orderBean.orderStatus.code == 1) {
                sendMessageLayout.setVisibility(View.VISIBLE);
//            }
        } else {
            otherContactsLayout.setVisibility(View.GONE);
            otherPhoneLayout.setVisibility(View.GONE);
            sendMessageLayout.setVisibility(View.GONE);
        }
        CommonUtils.hideSoftInput(this);
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

    @OnClick({R.id.sku_order_traveler_info_address_book_tv
            , R.id.sku_order_traveler_info_code_arrow_iv
            , R.id.sku_order_traveler_info_code_tv
            , R.id.sku_order_traveler_info_start_address_layout})
    public void setContact(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.sku_order_traveler_info_address_book_tv://通讯录
                intentPickContactsCheckPermisson(SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS);
                break;
            case R.id.sku_order_traveler_info_code_tv://区号
            case R.id.sku_order_traveler_info_code_arrow_iv:
                intent = new Intent(this, ChooseCountryActivity.class);
                intent.putExtra("viewId", R.id.sku_order_traveler_info_code_tv);
                startActivity(intent);
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
                intentPickContactsCheckPermisson(SkuOrderTravelerInfoView.REQUEST_CODE_PICK_OTHER_CONTACTS);
                break;
            case R.id.sku_order_traveler_info_other_code_arrow_iv://区号
            case R.id.sku_order_traveler_info_other_code_tv:
                intent = new Intent(this, ChooseCountryActivity.class);
                intent.putExtra("viewId", R.id.sku_order_traveler_info_other_code_tv);
                startActivity(intent);
                break;
        }
    }

    @TargetApi(23)
    public boolean requestPermisson(int requestCode) {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public void intentPickContacts(int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    public void intentPickContactsCheckPermisson(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23 && requestPermisson(requestCode)) {
            intentPickContacts(requestCode);
        } else {
            intentPickContacts(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS:
            case SkuOrderTravelerInfoView.REQUEST_CODE_PICK_OTHER_CONTACTS:
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
                if (areaCodeBean.viewId == R.id.sku_order_traveler_info_code_tv) {
                    codeTV.setText(code);
                } else if (areaCodeBean.viewId == R.id.sku_order_traveler_info_other_code_tv) {
                    otherCodeTV.setText(code);
                }
                break;
            case CHOOSE_POI_BACK:
                if (!(action.getData() instanceof PoiBean)) {
                    break;
                }
                PoiBean poiBean = (PoiBean) action.getData();
                if (poiBean == null) {
                    break;
                }
                addressTV.setText(poiBean.placeName);
                addressDescriptionTV.setText(poiBean.placeDetail);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS == requestCode || SkuOrderTravelerInfoView.REQUEST_CODE_PICK_OTHER_CONTACTS == requestCode) {
            Uri result = data.getData();
            String[] contact = PhoneInfo.getPhoneContacts(this, result);
            if (contact == null || contact.length < 2) {
                return;
            }
            if (!TextUtils.isEmpty(contact[0])) {
                if (SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS == requestCode) {
                    contactsET.setText(contact[0]);
                } else {
                    otherContactsET.setText(contact[0]);
                }
            }
            if (!TextUtils.isEmpty(contact[1])){
                String phone = contact[1];
                if (!TextUtils.isEmpty(phone)) {
                    phone = phone.replace("+86", "");//此处拷贝自以前代码。。。
                    phone = CommonUtils.getNum(phone);
                }
                if (SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS == requestCode) {
                    phoneET.setText(phone);
                } else {
                    otherPhoneET.setText(phone);
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

        if (orderBean.orderType == 3 || orderBean.orderType == 888) {
            if (orderBean.journeyList != null && orderBean.journeyList.size() > 1) {
                OrderBean.JourneyItem journeyItem = orderBean.journeyList.get(0);
                if (journeyItem.pickup != null && journeyItem.journey != null) {//包车加接机
                    Date date = DateUtils.getDateTimeFromStr2(journeyItem.pickup.flightArriveTime);
                    if (date != null) {
                        picker.setRangeStart(date.getHours(), date.getMinutes());
                    }
                }
            }
        }
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                serverTime = hour + ":" + minute;
                timeTV.setText(serverTime + "(当地时间)");
                orderBean.serviceStartTime = serverTime + ":00";
                picker.dismiss();
            }
        });

        picker.show();
    }

    private void sendRequest() {
        if (TextUtils.isEmpty(contactsET.getText())) {
            CommonUtils.showToast("请填写联系人姓名");
            return;
        }
        if (TextUtils.isEmpty(phoneET.getText())) {
            CommonUtils.showToast("请填写联系人手机号");
            return;
        }
        if (!CommonUtils.checkInlandPhoneNumber(codeTV.getText().toString(), phoneET.getText().toString())) {
            return;
        }
        if (contactUsersBean.isForOther) {
            if (TextUtils.isEmpty(otherContactsET.getText())) {
                CommonUtils.showToast("请填写乘车人姓名");
                return;
            }
            if (TextUtils.isEmpty(otherPhoneET.getText())) {
                CommonUtils.showToast("请填写乘车人手机号");
                return;
            }
            if (!CommonUtils.checkInlandPhoneNumber(otherCodeTV.getText().toString(), otherPhoneET.getText().toString())) {
                return;
            }
        }
        if (checkinLayout.getVisibility() == View.VISIBLE && TextUtils.isEmpty(checkinET.getText())) {
            CommonUtils.showToast("请填写接机牌姓名");
            return;
        }
        mDialogUtil.showLoadingDialog();
        requestParams.orderNo = orderBean.orderNo;
        requestParams.orderType = orderBean.orderType;//int 可选1-接机；2-送机；3-日租；4-次租
        requestParams.userRemark = TextUtils.isEmpty(markET.getText()) ? "" : markET.getText().toString();//备注
        requestParams.serviceRecTime = orderBean.serviceStartTime;//上车时间
        requestParams.startAddress = TextUtils.isEmpty(addressTV.getText()) ? "" : addressTV.getText().toString();//上车地点
        requestParams.startAddressDetail = TextUtils.isEmpty(addressDescriptionTV.getText()) ? "" : addressDescriptionTV.getText().toString();
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

    private String getUserExJson() {
        StringBuffer userExJson = new StringBuffer();
        userExJson.append("[");

        if (!TextUtils.isEmpty(contactsET.getText())) {
            userExJson.append("{name:\"" + contactsET.getText().toString() + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(codeTV.getText().toString()) + "\",mobile:\"" + phoneET.getText().toString() + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user1Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user1Name + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(contactUsersBean.phone1Code) + "\",mobile:\"" + contactUsersBean.user1Phone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user2Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user2Name + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(contactUsersBean.phone2Code) + "\",mobile:\"" + contactUsersBean.user2Phone + "\"}");
        }
        userExJson.append("]");
        return userExJson.toString();
    }

    private String getRealUserExJson() {
        StringBuffer realUserExJson = new StringBuffer();
        if (contactUsersBean.isForOther && !TextUtils.isEmpty(otherContactsET.getText())) {
            realUserExJson.append("[");
            realUserExJson.append("{name:\"" + otherContactsET.getText().toString() + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(otherCodeTV.getText().toString()) + "\",mobile:\"" + otherPhoneET.getText().toString() + "\"}");
            realUserExJson.append("]");
        }
        return realUserExJson.toString();
    }
}
