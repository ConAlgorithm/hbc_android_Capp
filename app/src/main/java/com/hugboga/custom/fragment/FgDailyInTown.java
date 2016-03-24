package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.PromiseAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.DailyBean;
import com.hugboga.custom.data.bean.PromiseBean;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 市内包车2.2
 *
 * Created by admin on 2016/1/14.
 */
@ContentView(R.layout.include_daily_intown)
public class FgDailyInTown extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    @ViewInject(R.id.daily_from)
    private TextView wContent;
    @ViewInject(R.id.daily_date_start)
    private TextView sDateTime;
    @ViewInject(R.id.daily_date_end)
    private TextView eDateTime;
    @ViewInject(R.id.daily_is_half)
    private CheckBox checkBoxIsHalf;


    @ViewInject(R.id.daily_time_layout)
    private View allDateLayout;//全天时间布局
    @ViewInject(R.id.daily_half_start)
    private View halfDateLayout;//半天时间布局
    @ViewInject(R.id.daily_half_date_start)
    private TextView halfDateTv;//半天时间

    @ViewInject(R.id.bottom_promise_wait)
    private TextView promiseWait;
    @ViewInject(R.id.bottom_promise_app)
    private TextView promiseApp;
    @ViewInject(R.id.daily_city_tips)
    private TextView cityTips;



    private CityBean startBean;//起始地
    private CityBean arrivalBean;//达到目的地

    private String startDate;
    private String endDate;
    private int mTotalDays;

    private DailyBean bean = new DailyBean();


    @Override
    protected int getBusinessType() {
        mBusinessType = Constants.BUSINESS_TYPE_DAILY;
        setGoodsType(Constants.BUSINESS_TYPE_DAILY);
        return mBusinessType;
    }

    @Override
    protected void initView() {
        promiseWait.setVisibility(View.GONE);
        promiseApp.setVisibility(View.VISIBLE);
        checkBoxIsHalf.setOnCheckedChangeListener(this);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {
    }

    @Event({R.id.daily_btn,
            R.id.daily_from_layout,
            R.id.daily_to_layout,
            R.id.daily_start_layout,
            R.id.daily_end_layout,
            R.id.daily_half_start,
            R.id.bottom_promise_layout,
            R.id.submit_order_tip,
            R.id.daily_half_day_question,
    })
    private void onClickView(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.daily_from_layout:
                bundle = new Bundle();
                bundle.putString(KEY_FROM, "startAddress");
                startFragment(new FgChooseCity(), bundle);
                break;
            case R.id.daily_start_layout:
            case R.id.daily_half_start:
                showDatePicker(sDateTime, null);
                break;
            case R.id.daily_end_layout:
                showDatePicker(eDateTime,sDateTime.getText().toString());
                break;
            case R.id.daily_btn:
                startFgCar();
                break;
            case R.id.bottom_promise_layout:
                showPromiseDialog();
                break;
            case R.id.daily_half_day_question:
                DialogUtil.getInstance(getActivity()).showCustomDialog(getString(R.string.daily_half_day_alert));
                break;
        }
    }

    /**
     * 承诺
     */
    private void showPromiseDialog() {
        int[] types = {0, 2, 3, 4};
        ArrayList<PromiseBean> list = new ArrayList<PromiseBean>();
        for (int type : types) {
            list.add(Constants.PromiseMap.get(type));
        }
        PromiseAdapter adapter = new PromiseAdapter(getActivity());
        adapter.setList(list);
        new AlertDialog.Builder(getActivity())
                .setAdapter(adapter, null)
                .setCancelable(true)
                .show()
                .setCanceledOnTouchOutside(true);
    }
    private boolean checkDataValid(){
        if (startBean == null) {
            Toast.makeText(getActivity(), "选择起始城市", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(bean.startDate) ||  TextUtils.isEmpty(bean.endDate)) {
            Toast.makeText(getActivity(), "选择服务日期", Toast.LENGTH_LONG).show();
            return false;
        }
        if(mTotalDays <0){
            Toast.makeText(getActivity(), "请重新选择服务日期", Toast.LENGTH_LONG).show();
            return false ;
        }

        return  true;
    }


    private void startFgCar() {
        if(!checkDataValid())return;
        bean.startCityID = startBean.cityId;
        bean.startCityName = startBean.name;
        bean.startLocation=startBean.location;
        bean.terminalCityID = arrivalBean.cityId;
        bean.terminalCityName = arrivalBean.name;
        bean.terminalLocation=arrivalBean.location;
        bean.inTownDays = mTotalDays;
        bean.outTownDays = 0;
        bean.totalDay = mTotalDays;
        bean.oneCityTravel  = 1;
        bean.childSeatSwitch = startBean.childSeatSwitch;
        bean.areaCode = startBean.areaCode;
        FgCar fg = new FgCar();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FgCar.KEY_DAILY, bean);
        fg.setArguments(bundle);
        startFragment(fg);
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCity.class.getSimpleName().equals(from)) {
            String fromKey = bundle.getString(KEY_FROM);
            if ("startAddress".equals(fromKey)) {
                startBean = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
                arrivalBean = startBean;
                if (startBean == null) return;
                wContent.setText(startBean.name);
                cityTips.setText(startBean.dailyTip);
                cityTips.setVisibility(View.VISIBLE);
            }
            collapseSoftInputMethod();
        }
    }

    @Override
    protected void initHeader() {

    }

    private void showDatePicker(final TextView mTextView,String startDate){
        Calendar cal = Calendar.getInstance();
        try {
            if(!TextUtils.isEmpty(mTextView.getText().toString())){
                cal.setTime(DateUtils.getDateByStr(mTextView.getText().toString()));
            }else if(!TextUtils.isEmpty(startDate)) {
                cal.setTime(DateUtils.getDateByStr(startDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String monthStr = String.format("%02d", (monthOfYear + 1));
                        String dayOfMonthStr = String.format("%02d", dayOfMonth);
                        String serverDate = year + "-" + monthStr + "-" + dayOfMonthStr;
                        mTextView.setText(serverDate);
                        halfDateTv.setText(sDateTime == null ? "" : sDateTime.getText());
                        if (!bean.isHalfDay) {
                            countDay();
                        } else {
                            bean.startDate = halfDateTv.getText().toString();
                            bean.endDate = bean.startDate;
                        }
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        cal = Calendar.getInstance();
            try {
                if(!TextUtils.isEmpty(startDate)) {

                    cal.setTime(DateUtils.getDateByStr(startDate));
                }
            } catch (ParseException e) {
                e.printStackTrace();
        }
        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        dpd.setMaxDate(cal);
        dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");
    }

    /**
     * 计算总天数
     */
    private void countDay() {
        String startDateStr = sDateTime.getText().toString();
        String endDateStr = eDateTime.getText().toString();
        if (TextUtils.isEmpty(startDateStr)) return;
        try {
            if (TextUtils.isEmpty(endDateStr)) {
                //起始日期加一天
                Date startDate = DateUtils.dateDateFormat.parse(startDateStr);
                Date endDate = DateUtils.addDay(startDate, 1);
                endDateStr = DateUtils.dateDateFormat.format(endDate);
                eDateTime.setText(endDateStr);
            }
            mTotalDays = DateUtils.getDiffByDate(DateUtils.getDateByStr(startDateStr), DateUtils.getDateByStr(endDateStr))+1 ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String startDate = sDateTime.getText().toString();
        String endDate = eDateTime.getText().toString();
        bean.startDate = startDate;
        bean.endDate = endDate;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        MLog.e("onCheckedChanged "+isChecked);
        bean.isHalfDay = isChecked;
        eDateTime.setText("");
        if(isChecked){
            allDateLayout.setVisibility(View.GONE);
            halfDateLayout.setVisibility(View.VISIBLE);
            bean.endDate = bean.startDate;
            mTotalDays = 1;
        }else{
            allDateLayout.setVisibility(View.VISIBLE);
            halfDateLayout.setVisibility(View.GONE);
            bean.endDate = null;
        }
    }
}