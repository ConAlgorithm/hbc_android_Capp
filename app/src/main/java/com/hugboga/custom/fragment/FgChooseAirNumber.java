package com.hugboga.custom.fragment;

import android.content.Intent;
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

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.DatePickerActivity;
import com.hugboga.custom.activity.PickFlightListActivity;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.SaveStartEndCity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
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
//                showDaySelect(addressTips);
                Intent intent = new Intent(getActivity(),DatePickerActivity.class);
                intent.putExtra("type",3);
                intent.putExtra("title","请选择出发日期");
                intent.putExtra("chooseDateBean",chooseDateBean);
                getActivity().startActivity(intent);
                break;
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
            CommonUtils.showToast("请填写航班号");
            return;
        }
        if (TextUtils.isEmpty(time1Str)) {
            CommonUtils.showToast("请选择航班时间");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(PickFlightListActivity.KEY_FLIGHT_NO, noStr.toUpperCase());
        bundle.putString(PickFlightListActivity.KEY_FLIGHT_DATE, time1Str);
        bundle.putInt(PickFlightListActivity.KEY_FLIGHT_TYPE, 1);
        bundle.putString("source",source);
        Intent intent = new Intent(getActivity(),PickFlightListActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    ChooseDateBean chooseDateBean;
    @Subscribe
    public void onEventMainThread(final EventAction action) {

        switch (action.getType()) {
            case CHOOSE_DATE:
                chooseDateBean = (ChooseDateBean) action.getData();
                if (chooseDateBean.type == 3) {
                    String serverDate = chooseDateBean.halfDateStr;
                    addressTips.setText(serverDate);
                    checkNextBtnStatus();
                }
                break;
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




}
