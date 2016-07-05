package com.hugboga.custom.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.SaveStartEndCity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.greenrobot.eventbus.EventBus;

/**
 * Created  on 16/5/13.
 */
@ContentView(R.layout.fg_choose_air_number)
public class FgChooseAirNumber extends BaseFragment {

    @Bind(R.id.number_left)
    TextView numberLeft;
    @Bind(R.id.number_tips)
    EditText numberTips;
    @Bind(R.id.rl_number)
    LinearLayout rlNumber;
    @Bind(R.id.address_left)
    TextView addressLeft;
    @Bind(R.id.address_tips)
    TextView addressTips;
    @Bind(R.id.rl_address)
    LinearLayout rlAddress;
    @Bind(R.id.search)
    Button search;
    @Bind(R.id.history_layout)
    LinearLayout historyLayout;
    @Bind(R.id.clean_all_history)
    TextView cleanAllHistory;
    @Bind(R.id.show_history)
    LinearLayout showHistory;

    @Override
    protected void initHeader() {

    }

    @Override
    protected void initView() {
        numberTips.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkNextBtnStatus();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getSaveInfo();
    }


    private int getMaxId(){
        return cityList.size() == 0?0:cityList.get(0).id + 1 ;
    }
    ArrayList<SaveStartEndCity> cityList = new ArrayList<>();

    private void getSaveInfo(){
        try {
            Type resultType = new TypeToken<List<SaveStartEndCity>>() {}.getType();
            cityList = Reservoir.get("savedHistoryCityBeanNo", resultType);
            genHistoryList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void genHistoryList(){
        View view = null;
        TextView historyText;
        ImageView historyDel;
        historyLayout.removeAllViews();
        if(null != cityList && cityList.size() > 0){
            showHistory.setVisibility(View.VISIBLE);
            for(int i = 0;i < cityList.size();i++) {
                view = LayoutInflater.from(this.getContext()).inflate(R.layout.air_history_item, null);
                historyText = (TextView) view.findViewById(R.id.history_text);
                historyDel = (ImageView) view.findViewById(R.id.history_del);
                historyText.setText(cityList.get(i).airNo);
                historyText.setTag(cityList.get(i).id);
                historyDel.setTag(cityList.get(i).id);
                view.setTag(cityList.get(i).id);
                historyText.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int vId = Integer.valueOf(v.getTag().toString()).intValue();
                        for(int i = 0;i< cityList.size();i++) {
                            if(cityList.get(i).id == vId) {
                                noStr = cityList.get(i).airNo;
                                numberTips.setText(noStr);
                            }
                        }
                        checkNextBtnStatus();
                    }
                });
                historyDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int vId = Integer.valueOf(v.getTag().toString()).intValue();
                        for(int i =0 ;i< historyLayout.getChildCount();i++){
                            if(vId == Integer.valueOf(historyLayout.getChildAt(i).getTag().toString())){
                                historyLayout.removeViewAt(i);
                            }
                        }

                        for(int i = 0;i< cityList.size();i++) {
                            if(cityList.get(i).id == vId) {
                                cityList.remove(i);
                            }
                        }

                        try {
                            Reservoir.put("savedHistoryCityBeanNo", cityList);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if(historyLayout.getChildCount() == 0){
                            showHistory.setVisibility(View.GONE);
                        }
                    }
                });
                historyLayout.addView(view);
            }
        }

    }


    private void addHistoryData(){
        SaveStartEndCity savedCityBean = new SaveStartEndCity();
        savedCityBean.airNo = noStr.toUpperCase();
        savedCityBean.id = getMaxId();
        for(int i = 0;i< cityList.size();i++){
            if(noStr.toUpperCase().equalsIgnoreCase(cityList.get(i).airNo)){
                cityList.remove(i);
                return;
            }

        }
        if(cityList.size() ==15 ){
            cityList.remove(14);
        }
        cityList.add(0,savedCityBean);
        try {
            Reservoir.put("savedHistoryCityBeanNo", cityList);
        }catch (Exception e){
            e.printStackTrace();
        }
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

    @OnClick({R.id.search, R.id.clean_all_history,R.id.address_tips})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                startFlightByNo();
                addHistoryData();
                break;
            case R.id.clean_all_history:
                historyLayout.removeAllViews();
                cityList.clear();
                try {
                    Reservoir.put("savedHistoryCityBeanNo", cityList);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(historyLayout.getChildCount() == 0){
                    showHistory.setVisibility(View.GONE);
                }
                break;
            case R.id.address_tips:
                showDaySelect(addressTips);
                break;
        }
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
        String from = bundle.getString(KEY_FROM);
        if ("FlightList".equals(from)) {
            EventBus.getDefault().post(new EventAction(EventType.AIR_NO,bundle.getSerializable(FgPickFlight.KEY_AIRPORT)));
            finish();
        }
    }



    String noStr= "";
    /**
     * 根据航班查
     */
    private void startFlightByNo() {
        noStr = numberTips.getText().toString();
        String time1Str = addressTips.getText().toString();
        if (TextUtils.isEmpty(noStr)) {
            Toast.makeText(getActivity(), "请填写航班号", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(time1Str)) {
            Toast.makeText(getActivity(), "请选择航班时间", Toast.LENGTH_LONG).show();
            return;
        }

        FgPickFlightList fragment = new FgPickFlightList();
        Bundle bundle = new Bundle();
        bundle.putString(FgPickFlightList.KEY_FLIGHT_NO, noStr.toUpperCase());
        bundle.putString(FgPickFlightList.KEY_FLIGHT_DATE, time1Str);
        bundle.putInt(FgPickFlightList.KEY_FLIGHT_TYPE, 1);
        bundle.putString("source",source);
        fragment.setArguments(bundle);
        startFragment(fragment);
    }

    class MyDatePickerListener implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
        TextView mTextView;

        MyDatePickerListener(TextView textView) {
            this.mTextView = textView;
        }

        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear + 1;
            String monthStr = String.format("%02d", month);
            String dayOfMonthStr = String.format("%02d", dayOfMonth);
            String serverDate = year + "-" + monthStr + "-" + dayOfMonthStr;
            mTextView.setText(serverDate);
            checkNextBtnStatus();
        }
    }



    private void checkNextBtnStatus(){
        String noStr = numberTips.getText().toString();
        String time1Str = addressTips.getText().toString();
        if (!TextUtils.isEmpty(noStr) && !TextUtils.isEmpty(time1Str)) {
            search.setEnabled(true);
            search.setBackgroundColor(Color.parseColor("#fad027"));
        }else{
            search.setEnabled(true);
            search.setBackgroundColor(Color.parseColor("#d5dadb"));
        }

    }



    public void showDaySelect(TextView sDateTime) {
        Calendar cal = Calendar.getInstance();
        MyDatePickerListener myDatePickerDialog = new MyDatePickerListener(sDateTime);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                myDatePickerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal = Calendar.getInstance();
        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        dpd.setMaxDate(cal);
        dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框

    }


}
