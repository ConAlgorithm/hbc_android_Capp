package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CarInfoBean;
import com.hugboga.custom.data.bean.DayQuoteBean;
import com.hugboga.custom.data.bean.SelectCarBean;
import com.hugboga.custom.data.bean.ServiceQuoteSumBean;
import com.hugboga.custom.data.request.RequestGetCarInfo;
import com.hugboga.custom.widget.JazzyViewPager;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyt on 16/4/16.
 */
@ContentView(R.layout.activity_select_car)
public class FGSelectCar extends BaseFragment {

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.jazzy_pager)
    JazzyViewPager jazzyPager;
    @Bind(R.id.left)
    ImageView left;
    @Bind(R.id.right)
    ImageView right;
    @Bind(R.id.car_type)
    TextView carType;
    @Bind(R.id.mans)
    TextView mans;
    @Bind(R.id.baggages)
    TextView baggages;
    @Bind(R.id.car_content)
    TextView carContent;
    @Bind(R.id.mans_left)
    TextView mansLeft;
    @Bind(R.id.mans_day_num)
    TextView mansDayNum;
    @Bind(R.id.mans_charge)
    TextView mansCharge;
    @Bind(R.id.mans_charge_tips)
    TextView mansChargeTips;
    @Bind(R.id.mans_money_all_info)
    LinearLayout mansMoneyAllInfo;
    @Bind(R.id.mans_money_show_info)
    TextView mansMoneyShowInfo;
    @Bind(R.id.mans_charge_layout)
    RelativeLayout mansChargeLayout;
    @Bind(R.id.cars_left)
    TextView carsLeft;
    @Bind(R.id.cars_day_num)
    TextView carsDayNum;
    @Bind(R.id.cars_charge)
    TextView carsCharge;
    @Bind(R.id.cars_charge_tips)
    TextView carsChargeTips;
    @Bind(R.id.cars_money_all_info)
    LinearLayout carsMoneyAllInfo;
    @Bind(R.id.cars_money_show_info)
    TextView carsMoneyShowInfo;
    @Bind(R.id.cars_charge_layout)
    RelativeLayout carsChargeLayout;
    @Bind(R.id.all_left)
    TextView allLeft;
    @Bind(R.id.all_day_num)
    TextView allDayNum;
    @Bind(R.id.all_charge)
    TextView allCharge;
    @Bind(R.id.per_left)
    TextView perLeft;
    @Bind(R.id.per_charge)
    TextView perCharge;
    @Bind(R.id.befer48_tips)
    TextView befer48Tips;
    @Bind(R.id.next_btn_click)
    Button nextBtnClick;

    @Override
    protected void initHeader() {
        fgTitle.setText(R.string.select_city_title);
    }

    @Override
    protected void inflateContent() {

    }


    /**
     * http://api.dev.hbc.tech/price/v1.2/e/dailyPrice?
     * // endCityId=204&startCityId=204&channelId=1925283890
     * // &startDate=2016-05-12%2019:30:22&endDate=2016-05-13%2000:00:00
     * // &halfDay=0&adultNum=4&childrenNum=1&childseatNum=0
     * // &luggageNum=0&passCities=1_1_204,1_1_2042
     **/
    public String startCityId = "204";
    public String endCityId = "204";
    public String startDate = "2016-05-12 00:00:00";
    public String endDate = "2016-05-13 00:00:00";
    public String halfDay = "0";
    public String adultNum = "4";
    public String childrenNum = "1";
    public String childseatNum = "0";
    public String luggageNum = "0";
    public String passCities = "1_1_204,1_1_2042";
    public String channelId = "1925283890";

    @Override
    protected void initView() {
        RequestGetCarInfo requestGetCarInfo = new RequestGetCarInfo(this.getActivity(),
                startCityId, endCityId, startDate, endDate, halfDay, adultNum, childrenNum, childseatNum, luggageNum, passCities,channelId);
        HttpRequestUtils.request(this.getActivity(), requestGetCarInfo, this);
    }

    int selctIndex = 0;
    List<SelectCarBean> cars;
    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        setProgressState(0);
        if(request instanceof  RequestGetCarInfo) {
            CarInfoBean bean = (CarInfoBean) request.getData();
            if(null != bean) {
                cars = bean.cars;
                showContent();
            }
        }

    }

    public void showContent(){
        SelectCarBean carBean = cars.get(selctIndex);
        carType.setText(carBean.carDesc);
        carContent.setText("此车型包括:"+carBean.models);
        mans.setText(String.format(getString(R.string.have_mas),carBean.numOfPerson));
        baggages.setText(String.format(getString(R.string.have_baggages),carBean.match));
        genServiceInfo(false);
        genCarsInfo(false);
    }

    View view = null;
    TextView day_all_money_left,day_all_money_right;
    TextView day_line2_money_left,day_line2_money_right;
    TextView day_line3_money_left,day_line3_money_right;
    TextView day_line4_money_left,day_line4_money_right;
    TextView day_line2_money_middle,day_line3_money_middle,day_line4_money_middle;
    public void genCarsInfo(boolean isMando){
        carsMoneyAllInfo.removeAllViews();
        if(isMando) {
            if (carsMoneyAllInfo.isShown()) {
                carsMoneyAllInfo.setVisibility(View.GONE);
            } else {
                carsMoneyAllInfo.setVisibility(View.VISIBLE);
            }
        }
        SelectCarBean carBean = cars.get(selctIndex);
        ServiceQuoteSumBean serviceQuoteSumBean = carBean.vehicleQuoteSum;;
        List<DayQuoteBean> dayQuotes = serviceQuoteSumBean.dayQuotes;
        DayQuoteBean dayQuoteBean = null;
        for(int i = 0;i< dayQuotes.size();i++) {
            dayQuoteBean = dayQuotes.get(i);
            view = LayoutInflater.from(this.getActivity()).inflate(R.layout.car_quote_item, null);
            day_all_money_left = (TextView)view.findViewById(R.id.day_all_money_left);
            day_all_money_right = (TextView)view.findViewById(R.id.day_all_money_right);
            day_line2_money_left = (TextView)view.findViewById(R.id.day_line2_money_left);
            day_line2_money_right = (TextView)view.findViewById(R.id.day_line2_money_right);
            day_line3_money_left = (TextView)view.findViewById(R.id.day_line3_money_left);
            day_line3_money_right = (TextView)view.findViewById(R.id.day_line3_money_right);
            day_line4_money_left = (TextView)view.findViewById(R.id.day_line4_money_left);
            day_line4_money_right = (TextView)view.findViewById(R.id.day_line4_money_right);
            day_line2_money_middle = (TextView)view.findViewById(R.id.day_line2_money_middle);
            day_line3_money_middle = (TextView)view.findViewById(R.id.day_line3_money_middle);
            day_line4_money_middle = (TextView)view.findViewById(R.id.day_line4_money_middle);

            day_all_money_left.setText(String.format(getString(R.string.day_all_money),dayQuoteBean.day));
            day_all_money_right.setText(dayQuoteBean.vehiclePrice+"元");
            if(dayQuoteBean.longDisPrice != 0) {
                day_line2_money_middle.setVisibility(View.VISIBLE);
                day_line2_money_left.setVisibility(View.VISIBLE);
                day_line2_money_right.setVisibility(View.VISIBLE);
                day_line2_money_left.setText(getString(R.string.service_money));
                day_line2_money_right.setText(dayQuoteBean.longDisPrice + "元");
            }else{
                day_line2_money_middle.setVisibility(View.GONE);
                day_line2_money_left.setVisibility(View.GONE);
                day_line2_money_right.setVisibility(View.GONE);
            }

            carsMoneyAllInfo.addView(view);
        }
    }


    public void genServiceInfo(boolean isMando){
        mansMoneyAllInfo.removeAllViews();
        if(isMando) {
            if (mansMoneyAllInfo.isShown()) {
                mansMoneyAllInfo.setVisibility(View.GONE);
            } else {
                mansMoneyAllInfo.setVisibility(View.VISIBLE);
            }
        }
        SelectCarBean carBean = cars.get(selctIndex);
        ServiceQuoteSumBean serviceQuoteSumBean = carBean.serviceQuoteSum;;
        List<DayQuoteBean> dayQuotes = serviceQuoteSumBean.dayQuotes;
        DayQuoteBean dayQuoteBean = null;
        for(int i = 0;i< dayQuotes.size();i++) {
            dayQuoteBean = dayQuotes.get(i);
            view = LayoutInflater.from(this.getActivity()).inflate(R.layout.car_quote_item, null);
            day_all_money_left = (TextView)view.findViewById(R.id.day_all_money_left);
            day_all_money_right = (TextView)view.findViewById(R.id.day_all_money_right);
            day_line2_money_left = (TextView)view.findViewById(R.id.day_line2_money_left);
            day_line2_money_right = (TextView)view.findViewById(R.id.day_line2_money_right);
            day_line3_money_left = (TextView)view.findViewById(R.id.day_line3_money_left);
            day_line3_money_right = (TextView)view.findViewById(R.id.day_line3_money_right);
            day_line4_money_left = (TextView)view.findViewById(R.id.day_line4_money_left);
            day_line4_money_right = (TextView)view.findViewById(R.id.day_line4_money_right);
            day_line2_money_middle = (TextView)view.findViewById(R.id.day_line2_money_middle);
            day_line3_money_middle = (TextView)view.findViewById(R.id.day_line3_money_middle);
            day_line4_money_middle = (TextView)view.findViewById(R.id.day_line4_money_middle);

            day_all_money_left.setText(String.format(getString(R.string.day_all_money),dayQuoteBean.day));
            day_all_money_right.setText(dayQuoteBean.guideServicePrice+"元");
            if(dayQuoteBean.stayPrice != 0) {
                day_line2_money_middle.setVisibility(View.VISIBLE);
                day_line2_money_left.setVisibility(View.VISIBLE);
                day_line2_money_right.setVisibility(View.VISIBLE);
                day_line2_money_left.setText(getString(R.string.service_money));
                day_line2_money_right.setText(dayQuoteBean.stayPrice + "元");
            }else{
                day_line2_money_middle.setVisibility(View.GONE);
                day_line2_money_left.setVisibility(View.GONE);
                day_line2_money_right.setVisibility(View.GONE);
            }

            mansMoneyAllInfo.addView(view);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
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

    @OnClick({R.id.left, R.id.right, R.id.mans_money_show_info, R.id.cars_money_show_info, R.id.next_btn_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                if(selctIndex >=1) {
                    --selctIndex;
                    showContent();
                }
                break;
            case R.id.right:
                if(selctIndex < cars.size() -1) {
                    ++selctIndex;
                    showContent();
                }
                break;
            case R.id.mans_money_show_info:
                genServiceInfo(true);
                break;
            case R.id.cars_money_show_info:
                genCarsInfo(true);
                break;
            case R.id.next_btn_click:
                break;
        }
    }
}
