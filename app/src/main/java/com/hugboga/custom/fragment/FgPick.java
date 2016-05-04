package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.PromiseAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.constants.ResourcesConstants;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.PromiseBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 接机填写行程
 */
@ContentView(R.layout.fg_pick)
public class FgPick extends BaseFragment {

    @ViewInject(R.id.pick_flight_tip)
    private TextView fTip;
    @ViewInject(R.id.pick_flight_title)
    private TextView fTitle;
    @ViewInject(R.id.pick_flight_content)
    private TextView fContent;
    @ViewInject(R.id.pick_flight_time)
    private TextView fTime;
    @ViewInject(R.id.pick_where_tip)
    private TextView wTip;
    @ViewInject(R.id.pick_where_title)
    private TextView wTitle;
    @ViewInject(R.id.pick_where_content)
    private TextView wContent;

    @ViewInject(R.id.bottom_promise_wait)
    private TextView promiseWait;
    @ViewInject(R.id.bottom_promise_app)
    private TextView promiseApp;

    private FlightBean flightBean;//航班信息
    private PoiBean poiBean;//达到目的地

    @Override
    protected void initHeader() {
//		fgTitle.setText(getString(R.string.title_pick));
//		setProgressState(0);
        if (getArguments() != null) {
            source = getArguments().getString("source","");
        }
    }

    protected void initView() {
        promiseWait.setVisibility(View.VISIBLE);
        promiseApp.setVisibility(View.GONE);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Event({R.id.pick_btn, R.id.pick_where_layout, R.id.pick_flight_layout, R.id.bottom_promise_layout, R.id.submit_order_tip})
    private void onClickView(View view) {
        HashMap<String,String> map = new HashMap<String,String>();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.pick_flight_layout:
                bundle.putString("source","下单过程中");
                startFragment(new FgPickFlight(),bundle);
                map.put("source", "下单过程中");
                MobclickAgent.onEvent(getActivity(), "search_trigger", map);
                break;
            case R.id.pick_where_layout:
                if (flightBean != null && flightBean.arrivalAirport != null) {
                    FgPoiSearch fg = new FgPoiSearch();
                    bundle.putString("source","下单过程中");
                    bundle.putInt(FgPoiSearch.KEY_CITY_ID, flightBean.arrivalAirport.cityId);
                    bundle.putString(FgPoiSearch.KEY_LOCATION, flightBean.arrivalAirport.location);
                    startFragment(fg, bundle);
                    map.put("source", "下单过程中");
                    MobclickAgent.onEvent(getActivity(), "search_trigger", map);
                } else {
                    Toast.makeText(getActivity(), "先选择乘坐航班", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bottom_promise_layout:
                showPromiseDialog();
                break;
            case R.id.submit_order_tip:
                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_NOTICE);
                startFragment(new FgWebInfo(), bundle);
                break;
            case R.id.pick_btn:
                startFgCar();
                break;
        }
    }


    /**
     * 承诺
     */
    private void showPromiseDialog() {
        int[] types = {0, 1, 3, 4};
        ArrayList<PromiseBean> list = new ArrayList<PromiseBean>();
        for (int type : types) {
            list.add(Constants.PromiseMap.get(type));
        }
        PromiseAdapter adapter = new PromiseAdapter(getActivity());
        adapter.setList(list);
        new AlertDialog.Builder(getActivity())
                .setAdapter(adapter, null)
                .show()
                .setCanceledOnTouchOutside(true);
    }

    private void startFgCar() {
        if (flightBean == null) {
            Toast.makeText(getActivity(), "选择航班信息", Toast.LENGTH_LONG).show();
            return;
        } else if (flightBean.depAirport == null || flightBean.arrivalAirport == null) {
            Toast.makeText(getActivity(), "航班异常，机场无信息", Toast.LENGTH_LONG).show();
            return;
        } else if (poiBean == null) {
            Toast.makeText(getActivity(), "选择到达目的地", Toast.LENGTH_LONG).show();
            return;
        }
        FgCar fg = new FgCar();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FgCar.KEY_FLIGHT, flightBean);
        bundle.putSerializable(FgCar.KEY_ARRIVAL, poiBean);
        bundle.putString("source", source);
        startFragment(fg, bundle);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("source", source);
        MobclickAgent.onEvent(getActivity(), "chosecar_pickup", map);
    }


    @Override
    public int getBusinessType() {
        mBusinessType = Constants.BUSINESS_TYPE_PICK;
        setGoodsType(mBusinessType);
        return mBusinessType;
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgPickFlight.class.getSimpleName().equals(from)) {
            flightBean = (FlightBean) bundle.getSerializable(FgPickFlight.KEY_AIRPORT);
            fTip.setVisibility(View.GONE);
            fTitle.setVisibility(View.VISIBLE);
            fContent.setVisibility(View.VISIBLE);
            fTime.setVisibility(View.VISIBLE);
            fTitle.setText(flightBean.arrivalAirport.airportName);
            fContent.setText(flightBean.flightNo + " " + flightBean.depAirportName + "-" + flightBean.arrAirportName);
            fTime.setText("当地时间" + flightBean.arrDate + " " + flightBean.arrivalTime + " 降落");
            poiBean = null;
            wTip.setVisibility(View.VISIBLE);
            wTitle.setVisibility(View.GONE);
            wContent.setVisibility(View.GONE);
        } else if (FgPoiSearch.class.getSimpleName().equals(from)) {
            poiBean = (PoiBean) bundle.getSerializable("arrival");
            wTip.setVisibility(View.GONE);
            wTitle.setVisibility(View.VISIBLE);
            wContent.setVisibility(View.VISIBLE);
            wTitle.setText(poiBean.placeName);
            wContent.setText(poiBean.placeDetail);
            collapseSoftInputMethod();
        }
    }

}
