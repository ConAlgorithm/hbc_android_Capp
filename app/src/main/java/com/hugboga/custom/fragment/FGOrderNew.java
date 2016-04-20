package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyt on 16/4/18.
 */

@ContentView(R.layout.fg_order_new)
public class FGOrderNew extends BaseFragment {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.city)
    TextView city;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.mans)
    TextView mans;
    @Bind(R.id.seat)
    TextView seat;
    @Bind(R.id.baggage)
    TextView baggage;
    @Bind(R.id.cartype)
    TextView cartype;
    @Bind(R.id.order_user_name)
    EditText orderUserName;
    @Bind(R.id.area_code_click)
    TextView areaCodeClick;
    @Bind(R.id.user_phone)
    EditText userPhone;
    @Bind(R.id.phone2_line)
    TextView phone2Line;
    @Bind(R.id.area_code_2_click)
    TextView areaCode2Click;
    @Bind(R.id.user_phone_2)
    EditText userPhone2;
    @Bind(R.id.phone2_layout)
    LinearLayout phone2Layout;
    @Bind(R.id.phone3_line)
    TextView phone3Line;
    @Bind(R.id.area_code_3_click)
    TextView areaCode3Click;
    @Bind(R.id.user_phone_3)
    EditText userPhone3;
    @Bind(R.id.phone3_layout)
    LinearLayout phone3Layout;
    @Bind(R.id.checkbox_other)
    CheckBox checkboxOther;
    @Bind(R.id.add_other_phone_click)
    TextView addOtherPhoneClick;
    @Bind(R.id.order_user_name_other)
    EditText orderUserNameOther;
    @Bind(R.id.area_code_other_click)
    TextView areaCodeOtherClick;
    @Bind(R.id.user_phone_other)
    EditText userPhoneOther;
    @Bind(R.id.for_other_people_layout)
    LinearLayout forOtherPeopleLayout;
    @Bind(R.id.up_time_text)
    TextView upTimeText;
    @Bind(R.id.up_site_text)
    EditText upSiteText;
    @Bind(R.id.hotel_phone_text_code_click)
    TextView hotelPhoneTextCodeClick;
    @Bind(R.id.hotel_phone_text)
    EditText hotelPhoneText;
    @Bind(R.id.all_money_left)
    TextView allMoneyLeft;
    @Bind(R.id.all_money_left_text)
    TextView allMoneyLeftText;
    @Bind(R.id.all_money_submit_click)
    TextView allMoneySubmitClick;
    @Bind(R.id.all_money_info)
    TextView allMoneyInfo;
    @Bind(R.id.bottom)
    RelativeLayout bottom;

    @Override
    protected void initHeader() {

    }

    String citystr;
    String datestr;
    String mansstr;
    String seatstr;
    String baggagestr;
    String cartypestr;
    @Override
    protected void initView() {
        citystr = this.getArguments().getString("citystr");
        datestr = this.getArguments().getString("datestr");
        mansstr = this.getArguments().getString("mansstr");
        seatstr = this.getArguments().getString("seatstr");
        baggagestr = this.getArguments().getString("baggagestr");
        cartypestr = this.getArguments().getString("cartypestr");

        city.setText(citystr);
        date.setText(datestr);
        mans.setText(mansstr);
        seat.setText(seatstr);
        baggage.setText(baggagestr);
        cartype.setText(cartypestr);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
        }
    }

    /**
     * 时间选择器
     */
    public void showTimeSelect() {
        Calendar cal = Calendar.getInstance();
        MyTimePickerDialogListener myTimePickerDialog = new MyTimePickerDialogListener();
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(myTimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        datePickerDialog.setAccentColor(getActivity().getResources().getColor(R.color.all_bg_yellow));
        datePickerDialog.show(this.getActivity().getFragmentManager(), "TimePickerDialog");                //显示日期设置对话框
    }

    String serverTime = "";
    /*
         * Function  :       自定义MyDatePickerDialog类，用于实现DatePickerDialog.OnDateSetListener接口，
         *                           当点击日期设置对话框中的“设置”按钮时触发该接口方法
         */
    class MyTimePickerDialogListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            String hour = String.format("%02d", hourOfDay);
            String minuteStr = String.format("%02d", minute);
            serverTime = hour + ":" + minuteStr;
            upTimeText.setText(serverTime + "(当地时间)");
        }
    }

    @OnClick({R.id.up_time_text,R.id.header_left_btn, R.id.header_title, R.id.area_code_click, R.id.area_code_2_click, R.id.area_code_3_click, R.id.add_other_phone_click, R.id.area_code_other_click, R.id.hotel_phone_text_code_click, R.id.all_money_submit_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.up_time_text:
                showTimeSelect();
                break;
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.header_title:
                break;
            case R.id.area_code_click:
            case R.id.area_code_2_click:
            case R.id.area_code_3_click:
            case R.id.add_other_phone_click:
            case R.id.area_code_other_click:
            case R.id.hotel_phone_text_code_click:
                FgChooseCountry chooseCountry = new FgChooseCountry();
                Bundle bundle = new Bundle();
                bundle.putInt("airportCode", view.getId());
                startFragment(chooseCountry, bundle);
                break;
            case R.id.all_money_submit_click:
                break;
        }
    }
}
