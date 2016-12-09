package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.hugboga.custom.widget.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;

/**
 * Created by on 16/8/4.
 *
 * 代码来自 FGOrderNew... View命名乱套。。。
 */
public class OrderEditActivity extends BaseActivity {

    @Bind(R.id.man_name)
    TextView manName;
    @Bind(R.id.man_phone)
    TextView manPhone;
    @Bind(R.id.mark)
    EditText mark;
    @Bind(R.id.man_phone_layout)
    LinearLayout manPhoneLayout;

    //乘车人
    @Bind(R.id.other_layout)
    RelativeLayout otherLayout;
    @Bind(R.id.other_phone_et)
    TextView otherName;

    //接机牌姓名
    @Bind(R.id.pick_name_layout)
    RelativeLayout pickNameLayout;
    @Bind(R.id.pick_name)
    EditText pickName;

    //上车时间
    @Bind(R.id.single_no_show_time)
    RelativeLayout pickUpTime;
    @Bind(R.id.up_right)
    TextView upRight;

    //上车地点
    @Bind(R.id.single_no_show_address)
    RelativeLayout pickUpLocationLayout;
    @Bind(R.id.up_address_left)
    TextView upAddressLeft;
    @Bind(R.id.up_address_right)
    TextView upAddressRight;

    //酒店电话
    @Bind(R.id.hotel_phone_layout)
    LinearLayout hotelPhoneLayout;
    @Bind(R.id.hotel_phone_text_code_click)
    TextView hotelPhoneTextCodeClick;
    @Bind(R.id.hotel_phone_text)
    EditText hotelPhoneText;
    @Bind(R.id.hotel_phone_line_view)
    View phoneLineView;

    //起飞航班
    @Bind(R.id.airport_name_layout)
    RelativeLayout airportNameLayout;
    @Bind(R.id.airport_name)
    EditText airportName;

    @Bind(R.id.for_other_man)
    TextView otherTV;

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

        setContentView(R.layout.fg_order_info_edit);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideInputMethod(mark);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
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
        fgRightBtn.setText("保存");
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        if (orderBean.orderType == 1) {//接机
            pickUpTime.setVisibility(View.GONE);
            pickUpLocationLayout.setVisibility(View.GONE);
            airportNameLayout.setVisibility(View.GONE);
            if ("1".equals(orderBean.isFlightSign)) {
                if (!TextUtils.isEmpty(orderBean.flightBrandSign)) {
                    pickName.setText(orderBean.flightBrandSign);
                }
            } else {
                pickNameLayout.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(orderBean.serviceAreaCode)) {
                hotelPhoneTextCodeClick.setText(CommonUtils.addPhoneCodeSign(orderBean.serviceAreaCode));
            }
            if (!TextUtils.isEmpty(orderBean.serviceAddressTel)) {
                hotelPhoneText.setText(orderBean.serviceAddressTel);
            }
        } else if (orderBean.orderType == 2) {//送机
            pickNameLayout.setVisibility(View.GONE);
            pickUpTime.setVisibility(View.GONE);
            pickUpLocationLayout.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(orderBean.flightNo)) {
                airportName.setText(orderBean.flightNo);
            }
            if (!TextUtils.isEmpty(orderBean.serviceAreaCode)) {
                hotelPhoneTextCodeClick.setText(CommonUtils.addPhoneCodeSign(orderBean.serviceAreaCode));
            }
            if (!TextUtils.isEmpty(orderBean.serviceAddressTel)) {
                hotelPhoneText.setText(orderBean.serviceAddressTel);
            }
        } else if (orderBean.orderType == 4) {//单次接送
            pickNameLayout.setVisibility(View.GONE);
            pickUpTime.setVisibility(View.GONE);
            pickUpLocationLayout.setVisibility(View.GONE);
            hotelPhoneLayout.setVisibility(View.GONE);
            airportNameLayout.setVisibility(View.GONE);
        } else {//包车
            pickNameLayout.setVisibility(View.GONE);
            airportNameLayout.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(orderBean.serviceStartTime)) {
                upRight.setText(orderBean.serviceStartTime + "(当地时间)");
            }
            if (!TextUtils.isEmpty(orderBean.startAddress)) {
                upAddressRight.setText(orderBean.startAddress);
            }
            if (!TextUtils.isEmpty(orderBean.serviceAreaCode)) {
                hotelPhoneTextCodeClick.setText(CommonUtils.addPhoneCodeSign(orderBean.serviceAreaCode));
            }
            if (!TextUtils.isEmpty(orderBean.serviceAddressTel)) {
                hotelPhoneText.setText(orderBean.serviceAddressTel);
            }
            if (orderBean.orderType == 5 || orderBean.orderType == 6) {
                upAddressLeft.setText(getString(R.string.up_site));
            } else {
                upAddressLeft.setText(" 上车地点");
            }
        }
        contactUsersBean = new ContactUsersBean();
        ArrayList<OrderContactBean> userList = orderBean.userList;
        if (userList != null && userList.size() > 0) {
            String userName = userList.get(0).name;
            String userPhone = userList.get(0).mobile;
            manName.setText(userName);
            manPhone.setText(CommonUtils.addPhoneCodeSign(userList.get(0).areaCode) + " " + userPhone);
            mark.setText(orderBean.memo);
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
            otherLayout.setVisibility(View.VISIBLE);
            otherName.setText(realUserList.get(0).name);
            contactUsersBean.otherName = realUserList.get(0).name;
            contactUsersBean.otherPhone = realUserList.get(0).mobile;
            contactUsersBean.otherphoneCode = realUserList.get(0).areaCode;
        } else {
            otherLayout.setVisibility(View.GONE);
        }
        contactUsersBean.isSendMessage = "1".equals(orderBean.realSendSms);
        contactUsersBean.isForOther = "2".equals(orderBean.isRealUser);
        requestParams = new RequestOrderEdit.Params();

        //1-5可以修改，后面的都不能改
        if (orderBean.orderStatus.code > 5) {
            fgRightBtn.setVisibility(View.GONE);
            fgRightBtn.setOnClickListener(null);
            otherTV.setVisibility(View.GONE);
            otherTV.setOnClickListener(null);
            manName.setEnabled(false);
            manPhone.setEnabled(false);
            otherLayout.setEnabled(false);
            manPhoneLayout.setEnabled(false);
            setHideTV(pickName);
            setHideTV(hotelPhoneTextCodeClick);
            setHideTV(hotelPhoneText);
            setHideTV(airportName);
            setHideTV(upRight);
            setHideTV(upAddressRight);
            setHideTV(mark);

            setHideTV(hotelPhoneTextCodeClick);
            setHideTV(hotelPhoneText);
            if (hotelPhoneTextCodeClick.getText() == null || TextUtils.isEmpty(hotelPhoneTextCodeClick.getText().toString())
                    && hotelPhoneText.getText() == null || TextUtils.isEmpty(hotelPhoneText.getText().toString())) {
                phoneLineView.setVisibility(View.GONE);
            }
        }
    }

    private void setHideTV(TextView textView) {
        if (textView.getText() == null || TextUtils.isEmpty(textView.getText().toString())) {
            textView.setHint("");
        }
        textView.setEnabled(false);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CONTACT_BACK:
                if (!(action.getData() instanceof ContactUsersBean)) {
                    break;
                }
                contactUsersBean = (ContactUsersBean) action.getData();
                if (contactUsersBean == null) {
                    break;
                }
                if (!TextUtils.isEmpty(contactUsersBean.userName)) {
                    manName.setText(contactUsersBean.userName);
                    manPhone.setText(CommonUtils.addPhoneCodeSign(contactUsersBean.phoneCode) + " " + contactUsersBean.userPhone);
                }
                if (contactUsersBean.isForOther && !TextUtils.isEmpty(contactUsersBean.otherName)) {
                    otherLayout.setVisibility(View.VISIBLE);
                    otherName.setText(contactUsersBean.otherName);
                } else {
                    otherLayout.setVisibility(View.GONE);
                }
                break;
            case CHOOSE_COUNTRY_BACK:
                if (!(action.getData() instanceof AreaCodeBean)) {
                    break;
                }
                AreaCodeBean areaCodeBean = (AreaCodeBean) action.getData();
                if (areaCodeBean == null) {
                    break;
                }
                orderBean.serviceAreaCode = areaCodeBean.getCode();
                hotelPhoneTextCodeClick.setText(CommonUtils.addPhoneCodeSign(areaCodeBean.getCode()));
                break;
            case CHOOSE_POI_BACK:
                if (!(action.getData() instanceof PoiBean)) {
                    break;
                }
                PoiBean poiBean = (PoiBean) action.getData();
                if (poiBean == null) {
                    break;
                }
                upAddressRight.setText(poiBean.placeName + "\n" + poiBean.placeDetail);
                break;
            default:
                break;
        }
    }
    String serverTime = "09:00";
    TimePicker picker;
    public void showYearMonthDayTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        picker = new TimePicker(activity, TimePicker.HOUR_OF_DAY);
        picker.setTitle("请选择上车时间");
        picker.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                serverTime = hour + ":" + minute;
                upRight.setText(serverTime + "(当地时间)");
                orderBean.serviceStartTime = serverTime;
                picker.dismiss();
            }
        });

        picker.show();
    }


    @OnClick({R.id.man_phone_layout, R.id.for_other_man, R.id.up_right, R.id.up_address_right, R.id.hotel_phone_text_code_click, R.id.other_layout})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.man_phone_layout://为他人订车
            case R.id.for_other_man:
            case R.id.other_layout:
                Bundle bundle = new Bundle();
                bundle.putSerializable("contactUsersBean", contactUsersBean);
                intent = new Intent(OrderEditActivity.this, ChooseOtherActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.hotel_phone_text_code_click://选择区号
                Bundle bundleCode = new Bundle();
                bundleCode.putInt("airportCode", view.getId());
                intent = new Intent(OrderEditActivity.this, ChooseCountryActivity.class);
                intent.putExtras(bundleCode);
                startActivity(intent);
                break;
            case R.id.up_right:
                showYearMonthDayTimePicker();
                break;
            case R.id.up_address_right:
                if (orderBean.startLocation != null) {
                    Bundle bundlePoiSearch = new Bundle();
                    bundlePoiSearch.putInt(PoiSearchActivity.KEY_CITY_ID, orderBean.serviceCityId);
                    bundlePoiSearch.putString(PoiSearchActivity.KEY_LOCATION, orderBean.startLocation);
                    intent = new Intent(OrderEditActivity.this, PoiSearchActivity.class);
                    intent.putExtras(bundlePoiSearch);
                    intent.putExtra("mBusinessType",orderBean.orderType);
                    startActivity(intent);
                }
                break;
        }
    }

    private void sendRequest() {
        if (TextUtils.isEmpty(manName.getText())) {
            CommonUtils.showToast("联系人姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(manPhone.getText())) {
            CommonUtils.showToast("联系人电话不能为空");
            return;
        }
        if (orderBean.orderType == 1 && "1".equals(orderBean.isFlightSign)) {//接机
            if (TextUtils.isEmpty(pickName.getText())) {
                CommonUtils.showToast("接机牌姓名不能为空");
                return;
            }
        } else if (orderBean.orderType == 5 || orderBean.orderType == 6) {//线路
            if (TextUtils.isEmpty(upAddressRight.getText())) {
                CommonUtils.showToast("上车地点不能为空");
                return;
            }
        }

        mDialogUtil.showLoadingDialog();
        requestParams.orderNo = orderBean.orderNo;
        requestParams.orderType = orderBean.orderType;//int 可选1-接机；2-送机；3-日租；4-次租
        requestParams.serviceAddressTel = TextUtils.isEmpty(hotelPhoneText.getText()) ? "" : hotelPhoneText.getText().toString();//目的地酒店或者区域电话号码
        requestParams.serviceAreaCode = CommonUtils.removePhoneCodeSign(orderBean.serviceAreaCode);//目的地区域
        requestParams.userRemark = TextUtils.isEmpty(mark.getText()) ? "" : mark.getText().toString();//备注
        if (orderBean.orderType == 3 || orderBean.orderType == 5 || orderBean.orderType == 6) {
            requestParams.startAddress = TextUtils.isEmpty(upAddressRight.getText()) ? "" : upAddressRight.getText().toString();//出发地
        }
        if (orderBean.orderType == 1) {
            requestParams.flightBrandSign = TextUtils.isEmpty(pickName.getText()) ? "" : pickName.getText().toString();//接送机接机牌名称
        }
        requestParams.flightNo = TextUtils.isEmpty(airportName.getText()) ? "" : airportName.getText().toString();//送机航班号
        requestParams.userEx = getUserExJson();
        requestParams.realUserEx = getRealUserExJson();
        requestParams.serviceRecTime = orderBean.serviceStartTime;
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

        if (!TextUtils.isEmpty(contactUsersBean.userPhone)) {
            userExJson.append("{name:\"" + contactUsersBean.userName + "\",areaCode:\"" + CommonUtils.removePhoneCodeSign(contactUsersBean.phoneCode) + "\",mobile:\"" + contactUsersBean.userPhone + "\"}");
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

}
