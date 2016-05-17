package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.utils.ToastUtils;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created  on 16/5/13.
 */

@ContentView(R.layout.fg_picknew)
public class FgPickNew extends BaseFragment {
    @Bind(R.id.info_left)
    TextView infoLeft;
    @Bind(R.id.info_tips)
    TextView infoTips;
    @Bind(R.id.air_title)
    TextView airTitle;
    @Bind(R.id.air_detail)
    TextView airDetail;
    @Bind(R.id.rl_info)
    LinearLayout rlInfo;
    @Bind(R.id.address_left)
    TextView addressLeft;
    @Bind(R.id.address_tips)
    TextView addressTips;
    @Bind(R.id.address_title)
    TextView addressTitle;
    @Bind(R.id.address_detail)
    TextView addressDetail;
    @Bind(R.id.rl_address)
    LinearLayout rlAddress;

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

    FlightBean flightBean;

    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case AIR_NO:
                FlightBean bean = (FlightBean) action.getData();
                if (mBusinessType == Constants.BUSINESS_TYPE_SEND && bean != null) {
                }else{
                    flightBean = bean;
                    String flightInfoStr = bean.flightNo + " ";
                    flightInfoStr += bean.depAirport.cityName + "-" + bean.arrivalAirport.cityName;
                    flightInfoStr += "\n当地时间" + bean.arrDate + " " + bean.depTime + "起飞";
                    infoTips.setVisibility(View.GONE);
                    airTitle.setVisibility(View.VISIBLE);
                    airDetail.setVisibility(View.VISIBLE);
                    airTitle.setText(bean.arrAirportName);
                    airDetail.setText(flightInfoStr);
                }
                break;
            default:
                break;
        }
    }

    PoiBean poiBean;
    @Override
    public void onFragmentResult(Bundle bundle) {
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgPoiSearch.class.getSimpleName().equals(from)) {
            poiBean = (PoiBean) bundle.getSerializable("arrival");
            addressTips.setVisibility(View.GONE);
            addressTitle.setVisibility(View.VISIBLE);
            addressDetail.setVisibility(View.VISIBLE);
            addressTitle.setText(poiBean.placeName);
            addressDetail.setText(poiBean.placeDetail);
            collapseSoftInputMethod();
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
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

    @OnClick({R.id.info_tips, R.id.air_title, R.id.air_detail, R.id.rl_info, R.id.address_tips, R.id.address_title, R.id.address_detail, R.id.rl_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_tips:
            case R.id.air_title:
            case R.id.air_detail:
            case R.id.rl_info:
                FgChooseAir fgChooseAir = new FgChooseAir();
                startFragment(fgChooseAir);
                break;
            case R.id.address_tips:
            case R.id.address_title:
            case R.id.address_detail:
            case R.id.rl_address:
                if(airDetail.isShown()) {
                    FgPoiSearch fg = new FgPoiSearch();
                    Bundle bundle = new Bundle();
                    bundle.putString("source", "下单过程中");
                    bundle.putInt(FgPoiSearch.KEY_CITY_ID, flightBean.arrivalAirport.cityId);
                    bundle.putString(FgPoiSearch.KEY_LOCATION, flightBean.arrivalAirport.location);
                    startFragment(fg, bundle);
                }else{
                    ToastUtils.showShort("请先选择航班");
                }
                break;
        }
    }
}
