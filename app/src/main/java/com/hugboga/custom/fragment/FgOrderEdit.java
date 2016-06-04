package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.OrderBean;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.io.Serializable;

import butterknife.Bind;

import static com.hugboga.custom.R.id.airport_name;

/**
 * Created on 16/6/4.
 * 代码来自 FGOrderNew
 * RequestOrderEdit
 */
@ContentView(R.layout.fg_order_info_edit)
public class FgOrderEdit extends BaseFragment {

    @Bind(R.id.man_name)
    TextView manName;
    @Bind(R.id.man_phone)
    TextView manPhone;

    @Bind(R.id.other_layout)//乘车人
     RelativeLayout otherLayout;
    @Bind(R.id.pick_name_layout)//接机牌姓名
     RelativeLayout pickNameLayout;
    @Bind(R.id.single_no_show_time)//上车时间
     RelativeLayout pickUpTime;
    @Bind(R.id.single_no_show_address)//上车地点
     RelativeLayout pickUpLocationLayout;
    @Bind(R.id.hotel_phone_layout)//酒店电话
     RelativeLayout hotelPhoneLayout;
    @Bind(R.id.airport_name_layout)//起飞航班
     RelativeLayout airportNameLayout;

    private ContactUsersBean contactUsersBean;

    private Params paramsData;

    public static class Params implements Serializable {
        public OrderBean orderBean;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (FgOrderEdit.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                paramsData = (FgOrderEdit.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    @Override
    protected void initHeader() {
        fgTitle.setText("出行人信息");
        OrderBean orderBean = paramsData.orderBean;
        if (orderBean != null) {
            if (orderBean.orderGoodsType == 1) {//接机
                pickUpTime.setVisibility(View.GONE);
                pickUpLocationLayout.setVisibility(View.GONE);
                airportNameLayout.setVisibility(View.GONE);
            } else if (orderBean.orderGoodsType == 2) {//送机
                otherLayout.setVisibility(View.GONE);
                pickNameLayout.setVisibility(View.GONE);
                pickUpTime.setVisibility(View.GONE);
                pickUpLocationLayout.setVisibility(View.GONE);
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
            }
        }
//        contactUsersBean = new ContactUsersBean();
//        String userName = UserEntity.getUser().getNickname(this.getActivity());
//        String userPhone = UserEntity.getUser().getPhone(this.getActivity());
//        contactUsersBean.userName = userName;
//        contactUsersBean.userPhone = userPhone;
//        manName.setText(userName);
//        manPhone.setText(userPhone);
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
}
