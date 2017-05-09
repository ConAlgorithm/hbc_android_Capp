package com.hugboga.custom.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;

public class OrderDetailTravelerInfoActivity extends BaseActivity{

    @Bind(R.id.sku_order_traveler_info_contacts_et)
    EditText contactsET;

    @Bind(R.id.sku_order_traveler_info_title_tv)
    TextView travelerInfoTitleTV;
    @Bind(R.id.sku_order_traveler_info_contacts_star_tv)
    TextView contactsStarTV;
    @Bind(R.id.sku_order_traveler_info_phone_star_tv)
    TextView phoneStarTV;

    @Bind(R.id.sku_order_traveler_info_address_book_tv)
    TextView addressBookTV;
    @Bind(R.id.sku_order_traveler_info_address_book_iv)
    ImageView addressBookIV;

    @Bind(R.id.sku_order_traveler_info_code_arrow_iv)
    ImageView codeArrowIV;
    @Bind(R.id.sku_order_traveler_info_code_tv)
    TextView codeTV;
    @Bind(R.id.sku_order_traveler_info_phone_et)
    EditText phoneET;

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
    @Bind(R.id.sku_order_traveler_info_start_address_arrow_iv)
    ImageView addressArrowIV;

    @Bind(R.id.sku_order_traveler_info_mark_et)
    EditText markET;
    @Bind(R.id.sku_order_traveler_info_mark_line_view)
    View markLineView;

    private TimePicker picker;
    String serverTime = "09:00";
    private DialogUtil mDialogUtil;

    private ContactUsersBean contactUsersBean;
    private OrderBean orderBean;
    RequestOrderEdit.Params requestParams;

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

        setContentView(R.layout.activity_order_detail_traveler_info);
        ButterKnife.bind(this);
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
                if (orderBean.orderStatus.code <= 5) {
                    EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_INFO, orderBean.orderNo));
                }
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

        travelerInfoTitleTV.setVisibility(View.GONE);

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

        if ((orderBean.orderType == 3 || orderBean.orderType == 888) && orderBean.journeyList != null && orderBean.journeyList.size() > 1) {
            OrderBean.JourneyItem journeyItem = orderBean.journeyList.get(0);
            if (journeyItem.pickup != null && journeyItem.journey != null) {//包车加接机
                addressLayout.setVisibility(View.GONE);
                timeLayout.setVisibility(View.VISIBLE);
                markLineView.setVisibility(View.GONE);
            } else if (journeyItem.pickup != null) {//只接机
                addressLayout.setVisibility(View.GONE);
                timeLayout.setVisibility(View.GONE);
                markLineView.setVisibility(View.GONE);
            } else {//包车
                addressLayout.setVisibility(View.VISIBLE);
                timeLayout.setVisibility(View.VISIBLE);
                markLineView.setVisibility(View.VISIBLE);
            }
        } else {//线路
            addressLayout.setVisibility(View.VISIBLE);
            timeLayout.setVisibility(View.VISIBLE);
            markLineView.setVisibility(View.VISIBLE);
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
        contactUsersBean.isForOther = "2".equals(orderBean.isRealUser);
        requestParams = new RequestOrderEdit.Params();

        if (orderBean.orderStatus.code > 5) {
            codeArrowIV.setEnabled(false);
            fgRightTV.setVisibility(View.GONE);
            fgRightTV.setOnClickListener(null);
            setHideTV(contactsET);
            setHideTV(codeTV);
            setHideTV(phoneET);
            timeLayout.setEnabled(false);
            setHideTV(timeTV);
            addressLayout.setEnabled(false);
            setHideTV(addressTV);
            setHideTV(addressDescriptionTV);
            setHideTV(markET);
            addressArrowIV.setVisibility(View.INVISIBLE);
            codeArrowIV.setVisibility(View.INVISIBLE);
            addressBookTV.setVisibility(View.INVISIBLE);
            addressBookIV.setVisibility(View.INVISIBLE);
            contactsStarTV.setVisibility(View.INVISIBLE);
            phoneStarTV.setVisibility(View.INVISIBLE);
        }
    }

    private void setHideTV(TextView textView) {
        if (textView.getText() == null || TextUtils.isEmpty(textView.getText().toString())) {
            textView.setHint("");
        }
        textView.setEnabled(false);
    }

    @OnClick({R.id.sku_order_traveler_info_address_book_tv, R.id.sku_order_traveler_info_code_arrow_iv, R.id.sku_order_traveler_info_code_tv, R.id.sku_order_traveler_info_start_address_layout})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.sku_order_traveler_info_address_book_tv://通讯录
                requestPermisson();
                intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 102);
                break;
            case R.id.sku_order_traveler_info_code_tv://区号
            case R.id.sku_order_traveler_info_code_arrow_iv:
                Bundle bundleCode = new Bundle();
                bundleCode.putInt("airportCode", view.getId());
                intent = new Intent(this, ChooseCountryActivity.class);
                intent.putExtras(bundleCode);
                startActivity(intent);
                break;
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

    @TargetApi(23)
    public void requestPermisson(){
        if (Build.VERSION.SDK_INT >= 23) {
            final List<String> permissionsList = new ArrayList<String>();
            addPermission(permissionsList, Manifest.permission.READ_CONTACTS);
            addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS);
            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), SkuOrderActivity.REQUEST_CODE_PICK_CONTACTS);
                return;
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
        return true;
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
                codeTV.setText(CommonUtils.addPhoneCodeSign(areaCodeBean.getCode()));
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
        if (102 == requestCode) {
            Uri result = data.getData();
            String[] contact = PhoneInfo.getPhoneContacts(this, result);
            if (contact == null || contact.length < 2) {
                return;
            }
            if (!TextUtils.isEmpty(contact[0])) {
                contactsET.setText(contact[0]);
            }
            if (!TextUtils.isEmpty(contact[1])) {
                String phone = contact[1];
                if (!TextUtils.isEmpty(phone)) {
                    phone = phone.replace("+86", "");
                }
                phoneET.setText(phone);
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
            CommonUtils.showToast("联系人姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(phoneET.getText())) {
            CommonUtils.showToast("联系人电话不能为空");
            return;
        }
        mDialogUtil.showLoadingDialog();
        requestParams.orderNo = orderBean.orderNo;
        requestParams.orderType = orderBean.orderType;//int 可选1-接机；2-送机；3-日租；4-次租
        requestParams.serviceRecTime = orderBean.serviceStartTime;//上车时间
        requestParams.startAddress = TextUtils.isEmpty(addressTV.getText()) ? "" : addressTV.getText().toString();//上车地点
        requestParams.startAddressDetail = TextUtils.isEmpty(addressDescriptionTV.getText()) ? "" : addressDescriptionTV.getText().toString();
        requestParams.userRemark = TextUtils.isEmpty(markET.getText()) ? "" : markET.getText().toString();//备注
        requestParams.userEx = getUserExJson();
        requestParams.realUserEx = getRealUserExJson();
        requestParams.realSendSms = contactUsersBean.isSendMessage ? 1 : 0;
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
            userExJson.append("{name:\"" + contactsET.getText().toString() + "\",areaCode:\"" + codeTV.getText().toString() + "\",mobile:\"" + phoneET.getText().toString() + "\"}");
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
        if (contactUsersBean.isForOther && !TextUtils.isEmpty(contactUsersBean.otherName)) {
            realUserExJson.append("[");
            realUserExJson.append("{name:\"" + contactUsersBean.otherName + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(contactUsersBean.otherphoneCode) + "\",mobile:\"" + contactUsersBean.otherPhone + "\"}");
            realUserExJson.append("]");
        }
        return realUserExJson.toString();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (orderBean.orderStatus.code <= 5) {
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_INFO, orderBean.orderNo));
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
