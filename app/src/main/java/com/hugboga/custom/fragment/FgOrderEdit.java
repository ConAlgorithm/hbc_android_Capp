package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderContactBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestOrderEdit;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.ToastUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.view.annotation.Event;

import java.util.ArrayList;
import java.util.Calendar;
import de.greenrobot.event.EventBus;

/**
 * Created on 16/6/4.
 * 代码来自 FGOrderNew... View命名乱套。。。
 * RequestOrderEdit
 */
@ContentView(R.layout.fg_order_info_edit)
public class FgOrderEdit extends BaseFragment {

    @ViewInject(R.id.man_name)
    TextView manName;
    @ViewInject(R.id.man_phone)
    TextView manPhone;
    @ViewInject(R.id.mark)
    EditText mark;

    //乘车人
    @ViewInject(R.id.other_layout)
     RelativeLayout otherLayout;
    @ViewInject(R.id.other_phone_et)
    TextView otherName;

    //接机牌姓名
    @ViewInject(R.id.pick_name_layout)
     RelativeLayout pickNameLayout;
    @ViewInject(R.id.pick_name)
    EditText pickName;

    @ViewInject(R.id.single_no_show_time)//上车时间
     RelativeLayout pickUpTime;
    @ViewInject(R.id.up_right)
    TextView upRight;

    @ViewInject(R.id.single_no_show_address)//上车地点
     RelativeLayout pickUpLocationLayout;
    @ViewInject(R.id.up_address_right)
    TextView upAddressRight;

    //酒店电话
    @ViewInject(R.id.hotel_phone_layout)
    LinearLayout hotelPhoneLayout;
    @ViewInject(R.id.hotel_phone_text_code_click)
    TextView hotelPhoneTextCodeClick;
    @ViewInject(R.id.hotel_phone_text)
    EditText hotelPhoneText;

    //起飞航班
    @ViewInject(R.id.airport_name_layout)
     RelativeLayout airportNameLayout;
    @ViewInject(R.id.airport_name)
    EditText airportName;

    private DialogUtil mDialogUtil;

    private ContactUsersBean contactUsersBean;
    private OrderBean orderBean;
    RequestOrderEdit.Params requestParams;


    public static FgOrderEdit newInstance(OrderBean _orderBean) {
        FgOrderEdit fragment = new FgOrderEdit();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.PARAMS_DATA, _orderBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            orderBean = savedInstanceState.getParcelable(Constants.PARAMS_DATA);
            contactUsersBean = savedInstanceState.getParcelable("contactUsersBean");
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                orderBean = bundle.getParcelable(Constants.PARAMS_DATA);
            }
        }
        EventBus.getDefault().register(this);
        mDialogUtil = DialogUtil.getInstance(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (orderBean != null) {
            outState.putSerializable(Constants.PARAMS_DATA, orderBean);
        }
        if (contactUsersBean != null) {
            outState.putParcelable("contactUsersBean", contactUsersBean);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void initHeader() {//TODO 接机牌判断
        fgTitle.setText("出行人信息");
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_INFO, contactUsersBean.userName));
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
        if (orderBean.orderGoodsType == 1) {//接机
            pickUpTime.setVisibility(View.GONE);
            pickUpLocationLayout.setVisibility(View.GONE);
            airportNameLayout.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(orderBean.flightBrandSign)) {
                pickName.setText(orderBean.flightBrandSign);
            }
            if (!TextUtils.isEmpty(orderBean.serviceAreaCode)) {
                hotelPhoneTextCodeClick.setText("+" + orderBean.serviceAreaCode);
            }
            if (!TextUtils.isEmpty(orderBean.serviceAddressTel)) {
                hotelPhoneText.setText(orderBean.serviceAddressTel);
            }
        } else if (orderBean.orderGoodsType == 2) {//送机
            pickNameLayout.setVisibility(View.GONE);
            pickUpTime.setVisibility(View.GONE);
            pickUpLocationLayout.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(orderBean.flightNo)) {
                airportName.setText(orderBean.flightNo);
            }
        } else if (orderBean.orderGoodsType == 4) {//单次接送
            pickNameLayout.setVisibility(View.GONE);
            pickUpTime.setVisibility(View.GONE);
            pickUpLocationLayout.setVisibility(View.GONE);
            hotelPhoneLayout.setVisibility(View.GONE);
            airportNameLayout.setVisibility(View.GONE);
        } else {//包车
            pickNameLayout.setVisibility(View.GONE);
            airportNameLayout.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(orderBean.serviceStartTime)) {
                upRight.setText(orderBean.serviceStartTime);
            }
            if (!TextUtils.isEmpty(orderBean.startAddress)) {
                upAddressRight.setText(orderBean.startAddress);
            }
            if (!TextUtils.isEmpty(orderBean.serviceAreaCode)) {
                hotelPhoneTextCodeClick.setText("+" + orderBean.serviceAreaCode);
            }
            if (!TextUtils.isEmpty(orderBean.serviceAddressTel)) {
                hotelPhoneText.setText(orderBean.serviceAddressTel);
            }
        }
        contactUsersBean = new ContactUsersBean();
        ArrayList<OrderContactBean> userList = orderBean.userList;
        if (userList != null && userList.size() > 0) {
            String userName = userList.get(0).name;
            String userPhone = userList.get(0).mobile;
            manName.setText(userName);
            manPhone.setText(userPhone);
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
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    public void onEventMainThread(EventAction action) {
        if (action.getType() == EventType.CONTACT_BACK) {
            contactUsersBean = (ContactUsersBean) action.getData();
            if (!TextUtils.isEmpty(contactUsersBean.userName)) {
                manName.setText(contactUsersBean.userName);
                manPhone.setText(contactUsersBean.phoneCode + " " + contactUsersBean.userPhone);
            }
            if (contactUsersBean.isForOther && !TextUtils.isEmpty(contactUsersBean.otherName)) {
                otherLayout.setVisibility(View.VISIBLE);
                otherName.setText(contactUsersBean.otherName);
            } else {
                otherLayout.setVisibility(View.GONE);
            }
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
                if (!TextUtils.isEmpty(areaCode)) {
                    orderBean.serviceAreaCode = areaCode;
                    codeTv.setText("+" + areaCode);
                }
            }
        } else if (FgPoiSearch.class.getSimpleName().equals(fragmentName)) {
                PoiBean poiBean = (PoiBean) bundle.getSerializable(FgPoiSearch.KEY_ARRIVAL);
                upAddressRight.setText(poiBean.placeName + "\n" + poiBean.placeDetail);
        }
    }

    /**
     * 时间选择器
     */
    public void showTimeSelect() {
        Calendar cal = Calendar.getInstance();
        MyTimePickerDialogListener myTimePickerDialog = new MyTimePickerDialogListener();
        TimePickerDialog datePickerDialog = TimePickerDialog.newInstance(myTimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        datePickerDialog.setAccentColor(getActivity().getResources().getColor(R.color.all_bg_yellow));
        datePickerDialog.show(this.getActivity().getFragmentManager(), "TimePickerDialog");                //显示日期设置对话框
    }

    String serverTime = "09:00";
    class MyTimePickerDialogListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            String hour = String.format("%02d", hourOfDay);
            String minuteStr = String.format("%02d", minute);
            serverTime = hour + ":" + minuteStr;
            upRight.setText(serverTime + "(当地时间)");
            orderBean.serviceStartTime = serverTime;
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

    @Event({R.id.man_phone_layout, R.id.for_other_man, R.id.up_right, R.id.up_address_right, R.id.hotel_phone_text_code_click, R.id.other_layout})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.man_phone_layout://为他人订车
            case R.id.for_other_man:
            case R.id.other_layout:
                FgChooseOther fgChooseOther = new FgChooseOther();
                Bundle bundle = new Bundle();
                bundle.putParcelable("contactUsersBean", contactUsersBean);
                fgChooseOther.setArguments(bundle);
                startFragment(fgChooseOther);
                break;
            case R.id.hotel_phone_text_code_click://选择区号
                FgChooseCountry chooseCountry = new FgChooseCountry();
                Bundle bundleCode = new Bundle();
                bundleCode.putInt("airportCode", view.getId());
                startFragment(chooseCountry, bundleCode);
                break;
            case R.id.up_right:
                showTimeSelect();
            case R.id.up_address_right:
                startArrivalSearch(orderBean.serviceCityId, orderBean.startLocation);//TODO 上车ID 和地址
                break;
        }
    }

    private void sendRequest() {
        if (TextUtils.isEmpty(manName.getText())) {
            ToastUtils.showLong("联系人姓名不能为空!");
            return;
        }
        if (TextUtils.isEmpty(manPhone.getText())) {
            ToastUtils.showLong("联系人电话不能为空!");
            return;
        }
        if (orderBean.orderGoodsType == 1) {//接机
            if (TextUtils.isEmpty(pickName.getText())) {
                ToastUtils.showLong("接机牌姓名不能为空!");
                return;
            }
        } else if (orderBean.orderGoodsType == 3 || orderBean.orderGoodsType == 5) {//包车
            if (TextUtils.isEmpty(upAddressRight.getText())) {
                ToastUtils.showLong("上车地点不能为空!");
                return;
            }
        }

        mDialogUtil.showLoadingDialog();
        requestParams.orderNo = orderBean.orderNo;
        requestParams.orderType = orderBean.orderType;//int 可选1-接机；2-送机；3-日租；4-次租
        requestParams.serviceAddressTel = TextUtils.isEmpty(hotelPhoneText.getText()) ? "" : hotelPhoneText.getText().toString();//目的地酒店或者区域电话号码
        requestParams.serviceAreaCode = orderBean.serviceAreaCode;//目的地区域
        requestParams.userRemark = TextUtils.isEmpty(mark.getText()) ? "" : mark.getText().toString();//备注
        if (orderBean.orderGoodsType == 3 || orderBean.orderGoodsType == 5) {
            requestParams.startAddress = TextUtils.isEmpty(upAddressRight.getText()) ? "" : upAddressRight.getText().toString();//出发地
        }
        if (orderBean.orderGoodsType == 1) {
            requestParams.flightBrandSign = TextUtils.isEmpty(pickName.getText()) ? "" : pickName.getText().toString();//接送机接机牌名称
        }
        requestParams.flightNo = TextUtils.isEmpty(airportName.getText()) ? "" : airportName.getText().toString();//送机航班号
        requestParams.userEx = getUserExJson();
        requestParams.realUserEx = getRealUserExJson();
        requestParams.serviceRecTime = orderBean.serviceStartTime;

        requestParams.adultNum = orderBean.adult;
        requestParams.childNum = orderBean.child;
        requestParams.isArrivalVisa = orderBean.visa;
        requestParams.serviceDate = orderBean.serviceTime;

//        requestParams.servicePassCitys = getRealUserExJson();
//        requestParams.flightAirportCode = orderBean.flightAirportCode;//送机航班机场三字码
//        requestParams.flightAirportName = getRealUserExJson();//送机机场名称
//        requestParams.flightFlyTimeL = getRealUserExJson();
//        requestParams.flightArriveTimeL = getRealUserExJson();
//        requestParams.flightAirportBuiding = getRealUserExJson();
        requestData(new RequestOrderEdit(getActivity(), requestParams));
        Log.i("aa", "hotelPhoneTextCodeClick.getText().toString() "+hotelPhoneTextCodeClick.getText().toString()+" -- "+hotelPhoneText.getText().toString());
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestOrderEdit) {
            CommonUtils.showToast("信息修改成功");
        }
    }

    private String getUserExJson() {
        StringBuffer userExJson = new StringBuffer();
        userExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.userPhone)) {
            userExJson.append("{name:\"" + contactUsersBean.userName + "\",areaCode:\"" + (null == contactUsersBean.phoneCode ? "+86" : contactUsersBean.phoneCode) + "\",mobile:\"" + contactUsersBean.userPhone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user1Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user1Name + "\",areaCode:\"" + (null == contactUsersBean.phone1Code ? "+86" : contactUsersBean.phone1Code) + "\",mobile:\"" + contactUsersBean.user1Phone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user2Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user2Name + "\",areaCode:\"" + (null == contactUsersBean.phone2Code ? "+86" : contactUsersBean.phone2Code) + "\",mobile:\"" + contactUsersBean.user2Phone + "\"}");
        }
        userExJson.append("]");
        return userExJson.toString();
    }

    private String getRealUserExJson() {
        StringBuffer realUserExJson = new StringBuffer();
        if (!TextUtils.isEmpty(contactUsersBean.otherName)) {
            realUserExJson.append("[");
            realUserExJson.append("{name:\"" + contactUsersBean.otherName + "\",areaCode:\"" + contactUsersBean.otherphoneCode + "\",mobile:\"" + contactUsersBean.otherPhone + "\"}");
            realUserExJson.append("]");
        }
        return realUserExJson.toString();
    }

}
