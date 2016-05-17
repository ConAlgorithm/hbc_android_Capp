package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.utils.ToastUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created  on 16/5/13.
 */
@ContentView(R.layout.fg_sendnew)
public class FgSendNew extends BaseFragment {

    @Bind(R.id.info_left)
    TextView infoLeft;
    @Bind(R.id.info_tips)
    TextView infoTips;
    @Bind(R.id.air_title)
    TextView airTitle;
    @Bind(R.id.air_detail)
    TextView airDetail;
    @Bind(R.id.rl_info)
    RelativeLayout rlInfo;
    @Bind(R.id.address_left)
    TextView addressLeft;
    @Bind(R.id.address_tips)
    TextView addressTips;
    @Bind(R.id.rl_address)
    RelativeLayout rlAddress;
    @Bind(R.id.time_left)
    TextView timeLeft;
    @Bind(R.id.time_text)
    TextView timeText;
    @Bind(R.id.rl_starttime)
    RelativeLayout rlStarttime;

    private AirPort airPortBean;//航班信息
    private PoiBean poiBean;//达到目的地
    private String serverDate;
    private String serverTime;


    @Override
    protected void initHeader() {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.info_tips, R.id.air_title, R.id.air_detail, R.id.rl_info, R.id.address_tips, R.id.rl_address, R.id.time_text, R.id.rl_starttime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_tips:
            case R.id.air_title:
            case R.id.air_detail:
            case R.id.rl_info://从哪里出发
                if (airPortBean != null) {
                    FgPoiSearch fg = new FgPoiSearch();
                    Bundle bundle = new Bundle();
                    bundle.putInt(FgPoiSearch.KEY_CITY_ID, airPortBean.cityId);
                    bundle.putString(FgPoiSearch.KEY_LOCATION, airPortBean.location);
                    fg.setArguments(bundle);
                    startFragment(fg);
                }else {
                    ToastUtils.showShort("先选择机场");
                }
                break;
            case R.id.address_tips://选择机场
                startFragment(new FgChooseAirport());
                break;
            case R.id.rl_address:
                FgChooseAir fgChooseAir = new FgChooseAir();
                startFragment(fgChooseAir);
                break;
            case R.id.time_text://出发时间
                if (airPortBean == null) {
                    ToastUtils.showShort("先选择机场");
                    return;
                }
                showDaySelect();
                break;
            case R.id.rl_starttime:
                break;
        }
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseAirport.class.getSimpleName().equals(from)) {
            airPortBean = (AirPort) bundle.getSerializable(FgChooseAirport.KEY_AIRPORT);
            addressTips.setText(airPortBean.cityName + " " + airPortBean.airportName);
            poiBean = null;
            infoTips.setVisibility(View.VISIBLE);
            airTitle.setVisibility(View.GONE);
            airDetail.setVisibility(View.GONE);
        } else if (FgPoiSearch.class.getSimpleName().equals(from)) {
            poiBean = (PoiBean) bundle.getSerializable("arrival");
            infoTips.setVisibility(View.GONE);
            airTitle.setVisibility(View.VISIBLE);
            airDetail.setVisibility(View.VISIBLE);
            airTitle.setText(poiBean.placeName);
            airDetail.setText(poiBean.placeDetail);
            collapseSoftInputMethod();
        }
    }

    public void showDaySelect() {
        Calendar cal = Calendar.getInstance();
        MyDatePickerListener myDatePickerDialog = new MyDatePickerListener(timeText);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                myDatePickerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal = Calendar.getInstance();
        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        dpd.setMaxDate(cal);
        dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框

    }

    class MyDatePickerListener implements DatePickerDialog.OnDateSetListener {
        TextView mTextView;

        MyDatePickerListener(TextView textView) {
            this.mTextView = textView;
        }

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear + 1;
            String monthStr = String.format("%02d", month);
            String dayOfMonthStr = String.format("%02d", dayOfMonth);
            serverDate = year + "-" + monthStr + "-" + dayOfMonthStr;
            showTimeSelect();
        }
    }

    public void showTimeSelect() {
        Calendar cal = Calendar.getInstance();
        MyTimePickerDialogListener myTimePickerDialog = new MyTimePickerDialogListener();
        TimePickerDialog datePickerDialog = TimePickerDialog.newInstance(myTimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        datePickerDialog.show(this.getActivity().getFragmentManager(), "TimePickerDialog");                //显示日期设置对话框
    }


    class MyTimePickerDialogListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            String hour = String.format("%02d", hourOfDay);
            String minuteStr = String.format("%02d", minute);
            serverTime = hour + ":" + minuteStr;
            timeText.setText(serverDate + " " + serverTime);
        }
    }
}
