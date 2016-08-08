package com.hugboga.custom.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityActivity;
import com.hugboga.custom.activity.DatePickerActivity;
import com.hugboga.custom.activity.PickFlightListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.SaveStartEndCity;
import com.hugboga.custom.data.event.EventAction;

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
@ContentView(R.layout.fg_choose_air_address)
public class FgChooseAirAddress extends BaseFragment {

    @Bind(R.id.from_city)
    TextView fromCity;
    @Bind(R.id.end_city)
    TextView endCity;
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
    @Bind(R.id.exchange)
    ImageView exchange;
    @Bind(R.id.start_layout)
    LinearLayout startLayout;
    @Bind(R.id.end_layout)
    LinearLayout endLayout;

    @Override
    protected void initHeader() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getSaveInfo();
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
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }


    private int getMaxId() {
        return cityList.size() == 0 ? 0 : cityList.get(0).id + 1;
    }

    ArrayList<SaveStartEndCity> cityList = new ArrayList<>();

    private void getSaveInfo() {
        try {
            Type resultType = new TypeToken<List<SaveStartEndCity>>() {
            }.getType();
            if (null != resultType) {
                cityList = Reservoir.get("savedHistoryCityBean", resultType);
            }
            if (null != cityList) {
                genHistoryList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void genHistoryList() {
        View view = null;
        TextView historyText;
        ImageView historyDel;
        historyLayout.removeAllViews();
        if (null != cityList && cityList.size() > 0) {
            showHistory.setVisibility(View.VISIBLE);
            for (int i = 0; i < cityList.size(); i++) {
                view = LayoutInflater.from(this.getContext()).inflate(R.layout.air_history_item, null);
                historyText = (TextView) view.findViewById(R.id.history_text);
                historyDel = (ImageView) view.findViewById(R.id.history_del);
                historyText.setText(cityList.get(i).startCityName + " - " + cityList.get(i).endCityName);
                historyText.setTag(cityList.get(i).id);
                historyDel.setTag(cityList.get(i).id);
                view.setTag(cityList.get(i).id);
                historyText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int vId = Integer.valueOf(v.getTag().toString()).intValue();
                        for (int i = 0; i < cityList.size(); i++) {
                            if (cityList.get(i).id == vId) {
                                cityFromId = cityList.get(i).startCityId;
                                cityToId = cityList.get(i).endCityId;
                                fromCityName = cityList.get(i).startCityName;
                                endCityName = cityList.get(i).endCityName;
                                fromCity.setText(fromCityName);
                                endCity.setText(endCityName);
                            }
                        }
                        checkNextBtnStatus();
                    }
                });
                historyDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int vId = Integer.valueOf(v.getTag().toString()).intValue();
                        for (int i = 0; i < historyLayout.getChildCount(); i++) {
                            if (vId == Integer.valueOf(historyLayout.getChildAt(i).getTag().toString())) {
                                historyLayout.removeViewAt(i);
                            }
                        }

                        for (int i = 0; i < cityList.size(); i++) {
                            if (cityList.get(i).id == vId) {
                                cityList.remove(i);
                            }
                        }

                        try {
                            Reservoir.put("savedHistoryCityBean", cityList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (historyLayout.getChildCount() == 0) {
                            showHistory.setVisibility(View.GONE);
                        }
                    }
                });
                historyLayout.addView(view);
            }
        }

    }


    private void addHistoryData() {
        SaveStartEndCity savedCityBean = new SaveStartEndCity();
        savedCityBean.startCityId = cityFromId;
        savedCityBean.startCityName = fromCityName;
        savedCityBean.endCityId = cityToId;
        savedCityBean.endCityName = endCityName;
        savedCityBean.id = getMaxId();
        for (int i = 0; i < cityList.size(); i++) {
            if (cityFromId == cityList.get(i).startCityId
                    && fromCityName.equalsIgnoreCase(cityList.get(i).startCityName)
                    && endCityName.equalsIgnoreCase(cityList.get(i).endCityName)
                    && cityToId == cityList.get(i).endCityId
                    ) {
                cityList.remove(i);
                return;
            }

        }
        if (cityList.size() == 15) {
            cityList.remove(14);
        }
        cityList.add(0, savedCityBean);
        try {
            Reservoir.put("savedHistoryCityBean", cityList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    int cityFromId, cityToId;
    String fromCityName, endCityName;

//    @Override
//    public void onFragmentResult(Bundle bundle) {
//        MLog.w(this + " onFragmentResult " + bundle);
//        String from = bundle.getString(KEY_FROM);
//        if ("startAddress".equals(from)) {
//            CityBean city = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
//            if (city != null) {
//                fromCityName = city.name;
//                fromCity.setText(fromCityName);
//                cityFromId = city.cityId;
//            }
//        } else if ("end".equals(from)) {
//            CityBean city = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
//            if (city != null) {
//                endCityName = city.name;
//                endCity.setText(endCityName);
//                cityToId = city.cityId;
//            }
//        } else if ("FlightList".equals(from)) {
//            EventBus.getDefault().post(new EventAction(EventType.AIR_NO, bundle.getSerializable(FgPickFlight.KEY_AIRPORT)));
//            finish();
//        }
//        checkNextBtnStatus();
//    }

    @OnClick({R.id.start_layout,R.id.end_layout,R.id.end_city_tips, R.id.from_city_tips, R.id.from_city, R.id.end_city, R.id.search, R.id.clean_all_history, R.id.exchange, R.id.address_tips, R.id.rl_address})
    public void onClick(View view) {
//        FgChooseCity city = new FgChooseCity();
        Intent intent = new Intent(getActivity(), ChooseCityActivity.class);
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.start_layout:
            case R.id.from_city_tips:
            case R.id.from_city:
                bundle.putString(KEY_FROM, "startAddress");
                bundle.putInt(ChooseCityActivity.KEY_SHOW_TYPE, ChooseCityActivity.ShowType.PICK_UP);
                bundle.putInt(ChooseCityActivity.KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_PICK);
//                city.setArguments(bundle);
//                startFragment(city);

                intent.putExtras(bundle);
                getActivity().startActivity(intent);

                break;
            case R.id.end_layout:
            case R.id.end_city_tips:
            case R.id.end_city:
                bundle.putString(KEY_FROM, "end");
                bundle.putInt(ChooseCityActivity.KEY_SHOW_TYPE, ChooseCityActivity.ShowType.PICK_UP);
                bundle.putInt(ChooseCityActivity.KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_PICK);
//                city.setArguments(bundle);
//                startFragment(city);

                intent.putExtras(bundle);
                getActivity().startActivity(intent);

                break;
            case R.id.search:
                addHistoryData();
                startFlightByCity();
                break;
            case R.id.clean_all_history:
                historyLayout.removeAllViews();
                cityList.clear();
                try {
                    Reservoir.put("savedHistoryCityBean", cityList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (historyLayout.getChildCount() == 0) {
                    showHistory.setVisibility(View.GONE);
                }
                break;
            case R.id.exchange:
                exChangeCity();
                break;
            case R.id.address_tips:
            case R.id.rl_address:
//                showDaySelect(addressTips);
                intent = new Intent(getActivity(),DatePickerActivity.class);
                intent.putExtra("type",3);
                intent.putExtra("title","请选择出发日期");
                intent.putExtra("chooseDateBean",chooseDateBean);
                getActivity().startActivity(intent);
                break;
        }
    }

    ChooseDateBean chooseDateBean;
    CityBean city;
    @Subscribe
    public void onEventMainThread(final EventAction action) {

        switch (action.getType()) {
            case CHOOSE_DATE:
                chooseDateBean = (ChooseDateBean) action.getData();
                if (chooseDateBean.type == 3) {
                    time2Str = chooseDateBean.halfDateStr;
                    addressTips.setText(time2Str);
                    checkNextBtnStatus();
                }
                break;
            case CHOOSE_START_CITY_BACK:
                city =  (CityBean)action.getData();
                if (city != null) {
                    fromCityName = city.name;
                    fromCity.setText(fromCityName);
                    cityFromId = city.cityId;
                }
                checkNextBtnStatus();
                break;
            case CHOOSE_END_CITY_BACK:
                city =  (CityBean)action.getData();
                if (city != null) {
                    endCityName = city.name;
                    endCity.setText(endCityName);
                    cityToId = city.cityId;
                }
                checkNextBtnStatus();
                break;



        }
    }

    private void checkNextBtnStatus() {
        String from = fromCity.getText().toString();
        String to = endCity.getText().toString();
        String time1Str = addressTips.getText().toString();
        if (!TextUtils.isEmpty(from) && !TextUtils.isEmpty(to) && !TextUtils.isEmpty(time1Str)) {
            search.setEnabled(true);
            search.setBackgroundColor(Color.parseColor("#fad027"));
        } else {
            search.setEnabled(false);
            search.setBackgroundColor(Color.parseColor("#d5dadb"));
        }

    }

    String time2Str = "";

    /**
     * 根据城市查
     */
    private void startFlightByCity() {
//        FgPickFlightList fragment = new FgPickFlightList();
        Bundle bundle = new Bundle();
        bundle.putInt(PickFlightListActivity.KEY_FLIGHT_FROM, cityFromId);
        bundle.putInt(PickFlightListActivity.KEY_FLIGHT_TO, cityToId);
        bundle.putInt(PickFlightListActivity.KEY_FLIGHT_TYPE, 2);
        bundle.putString(PickFlightListActivity.KEY_FLIGHT_DATE, time2Str);
//        fragment.setArguments(bundle);
//        startFragment(fragment);
        Intent intent = new Intent(getActivity(),PickFlightListActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    private void exChangeCity() {
        int tmpId = cityFromId;
        cityFromId = cityToId;
        cityToId = tmpId;

        fromCityName = endCityName;
        endCityName = fromCity.getText().toString();
        fromCity.setText(fromCityName);
        endCity.setText(endCityName);
    }

}
