package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
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
    EditText otherName;

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
     RelativeLayout hotelPhoneLayout;
    @ViewInject(R.id.hotel_phone_text_code_click)
    TextView hotelPhoneTextCodeClick;
    @ViewInject(R.id.hotel_phone_text)
    EditText hotelPhoneText;

    //起飞航班
    @ViewInject(R.id.airport_name_layout)
     RelativeLayout airportNameLayout;
    @ViewInject(R.id.airport_name)
    EditText airportName;

    @ViewInject(R.id.header_right_btn)//保存
     ImageView rightBtn;

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
            orderBean = (OrderBean) savedInstanceState.getParcelable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                orderBean = (OrderBean) bundle.getParcelable(Constants.PARAMS_DATA);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void initHeader() {
        fgTitle.setText("出行人信息");
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FgOrderDetail.Params params = new FgOrderDetail.Params();
//                params.orderGoodsType = orderBean.orderGoodsType;
//                params.orderId = orderBean.orderNo;
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(Constants.PARAMS_DATA, params);
//                bringToFront(FgOrderDetail.class, bundle);
//                if (!UserEntity.getUser().getNickname(getActivity()).equals(contactUsersBean.userName)) {
                    EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_INFO, contactUsersBean.userName));
//                }
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
            if (!TextUtils.isEmpty(orderBean.realUserName)) {//TODO 乘车人 没有提交
                otherName.setText(orderBean.realUserName);
            }
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
            otherLayout.setVisibility(View.GONE);
            pickNameLayout.setVisibility(View.GONE);
            pickUpTime.setVisibility(View.GONE);
            pickUpLocationLayout.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(orderBean.flightNo)) {
                airportName.setText(orderBean.flightNo);
            }
        } else if (orderBean.orderGoodsType == 4) {//单次接送
            otherLayout.setVisibility(View.GONE);
            pickNameLayout.setVisibility(View.GONE);
            pickUpTime.setVisibility(View.GONE);
            pickUpLocationLayout.setVisibility(View.GONE);
            hotelPhoneLayout.setVisibility(View.GONE);
            airportNameLayout.setVisibility(View.GONE);
        } else {//包车
            otherLayout.setVisibility(View.GONE);
            pickNameLayout.setVisibility(View.GONE);
            airportNameLayout.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(orderBean.serviceTime)) {//TODO 上车时间
                upRight.setText(orderBean.serviceTime);
            }
            if (!TextUtils.isEmpty(orderBean.startAddress)) {//TODO 上车地点
                upAddressRight.setText(orderBean.startAddress);
            }
            if (!TextUtils.isEmpty(orderBean.serviceAreaCode)) {
                hotelPhoneTextCodeClick.setText("+" + orderBean.serviceAreaCode);
            }
            if (!TextUtils.isEmpty(orderBean.serviceAddressTel)) {
                hotelPhoneText.setText(orderBean.serviceAddressTel);
            }
        }
        String userName = orderBean.contactName;
        String userPhone = UserEntity.getUser().getPhone(this.getActivity());//TODO
        manName.setText(userName);
        manPhone.setText(userPhone);
        mark.setText(orderBean.userRemark);

        contactUsersBean = new ContactUsersBean();
        contactUsersBean.userName = userName;
        contactUsersBean.userPhone = userPhone;

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
            if (orderBean.orderGoodsType == 1) {//接机 的时候有乘车人
                if (!TextUtils.isEmpty(contactUsersBean.otherName)) {
                    otherName.setText(contactUsersBean.otherName);
                }
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
        }
    }

    private void startArrivalSearch(int cityId, String location) {
        Log.i("aa","cityId--- " +cityId + "  location  "+location);
        if (location != null) {
            FgPoiSearch fg = new FgPoiSearch();
            Bundle bundle = new Bundle();
            bundle.putInt(FgPoiSearch.KEY_CITY_ID, cityId);
            bundle.putString(FgPoiSearch.KEY_LOCATION, location);
            startFragment(fg, bundle);
        }
    }

    @Event({R.id.man_phone_layout, R.id.for_other_man, R.id.up_right, R.id.up_address_right, R.id.hotel_phone_text_code_click})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.man_phone_layout://为他人订车
            case R.id.for_other_man:
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
        //TODO 用户名不需要？ 乘车人？？
        requestParams.orderNo = orderBean.orderNo;
        requestParams.orderType = orderBean.orderType;//int 可选1-接机；2-送机；3-日租；4-次租

//        requestParams.adultNum = 0;//int 成人座位数
//        requestParams.childNum = 0;//int 小孩座位数
        requestParams.serviceAddressTel = TextUtils.isEmpty(hotelPhoneText.getText()) ? "" : hotelPhoneText.getText().toString();//目的地酒店或者区域电话号码
        requestParams.serviceAreaCode = TextUtils.isEmpty(hotelPhoneTextCodeClick.getText()) ? "" : hotelPhoneTextCodeClick.getText().toString();//目的地区域

        requestParams.userAreaCode1 = contactUsersBean.phoneCode;//区域号1
        requestParams.userMobile1 = contactUsersBean.userPhone;//用户手机号1
        requestParams.userAreaCode2 = contactUsersBean.phone1Code;//区域号2
        requestParams.userMobile2 = contactUsersBean.user1Phone;//用户手机号2
        requestParams.userAreaCode3 = contactUsersBean.phone2Code;//区域号3
        requestParams.userMobile3 = contactUsersBean.user2Phone;//用户手机号3

        requestParams.userRemark = TextUtils.isEmpty(mark.getText()) ? "" : mark.getText().toString();//备注
//        requestParams.userName = contactUsersBean.userName;//联系人姓名
        requestParams.isArrivalVisa = 0;//int 是否落地签
        requestParams.serviceDate = TextUtils.isEmpty(upRight.getText()) ? "" : upRight.getText().toString();//服务时间 TODO 上车时间
//        requestParams.serviceRecTime = "";//日租服务时间的时分秒
//        requestParams.servicePassCitys = "";//日租途径城市
        requestParams.startAddress = TextUtils.isEmpty(upAddressRight.getText()) ? "" : upAddressRight.getText().toString();//出发地 TODO 上车地点
        requestParams.flightBrandSign = TextUtils.isEmpty(pickName.getText()) ? "" : pickName.getText().toString();//接送机接机牌名称
        requestParams.flightNo = TextUtils.isEmpty(airportName.getText()) ? "" : airportName.getText().toString();//送机航班号
//        requestParams.flightAirportCode = "";//送机航班机场三字码
//        requestParams.flightAirportName = "";//送机机场名称
//        requestParams.flightFlyTimeL = "";//送机起飞时间
//        requestParams.flightArriveTimeL = "";//送机到达时间
//        requestParams.flightAirportBuiding = "";//送机航站楼
        requestData(new RequestOrderEdit(getActivity(), requestParams));
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestOrderEdit) {
            CommonUtils.showToast("信息修改成功");
        }
    }

}
