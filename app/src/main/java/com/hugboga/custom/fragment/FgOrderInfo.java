package com.hugboga.custom.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.DayQuoteBean;
import com.hugboga.custom.data.bean.SelectCarBean;
import com.hugboga.custom.data.bean.ServiceQuoteSumBean;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dyt on 16/4/21.
 */
@ContentView(R.layout.fg_order_info)
public class FgOrderInfo extends BaseFragment {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
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
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected void initHeader() {
        fgTitle.setText(R.string.all_cost_info);
    }

    @Override
    protected void initView() {
        carBean = this.getArguments().getParcelable("carBean");
        showContent();
    }

    SelectCarBean carBean;
    public void showContent(){
        try {
            genServiceInfo(false);
            genCarsInfo(false);
            genTotal();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void genTotal(){
        allDayNum.setText(carBean.totalDays+"天 x"+carBean.numOfPerson+"人");
        allCharge.setText(carBean.price+"元");
        perCharge.setText(carBean.avgSpend+"元");
    }

    View view = null;
    TextView day_all_money_left,day_all_money_right;
    TextView day_line2_money_left,day_line2_money_right;
    TextView day_line3_money_left,day_line3_money_right;
    TextView day_line4_money_left,day_line4_money_right;
    TextView day_line2_money_middle,day_line3_money_middle,day_line4_money_middle;
    public void genCarsInfo(boolean isMando){
        try {
            carsMoneyAllInfo.removeAllViews();
            carsCharge.setText(carBean.vehiclePrice+"元");
            carsDayNum.setText("x"+carBean.totalDays+"天");
            ServiceQuoteSumBean serviceQuoteSumBean = carBean.vehicleQuoteSum;
            List<DayQuoteBean> dayQuotes = serviceQuoteSumBean.dayQuotes;
            DayQuoteBean dayQuoteBean = null;
            for (int i = 0; i < dayQuotes.size(); i++) {
                dayQuoteBean = dayQuotes.get(i);
                view = LayoutInflater.from(this.getActivity()).inflate(R.layout.car_quote_item, null);
                day_all_money_left = (TextView) view.findViewById(R.id.day_all_money_left);
                day_all_money_right = (TextView) view.findViewById(R.id.day_all_money_right);
                day_line2_money_left = (TextView) view.findViewById(R.id.day_line2_money_left);
                day_line2_money_right = (TextView) view.findViewById(R.id.day_line2_money_right);
                day_line3_money_left = (TextView) view.findViewById(R.id.day_line3_money_left);
                day_line3_money_right = (TextView) view.findViewById(R.id.day_line3_money_right);
                day_line4_money_left = (TextView) view.findViewById(R.id.day_line4_money_left);
                day_line4_money_right = (TextView) view.findViewById(R.id.day_line4_money_right);
                day_line2_money_middle = (TextView) view.findViewById(R.id.day_line2_money_middle);
                day_line3_money_middle = (TextView) view.findViewById(R.id.day_line3_money_middle);
                day_line4_money_middle = (TextView) view.findViewById(R.id.day_line4_money_middle);

                day_all_money_left.setText(String.format(getString(R.string.day_all_money), dayQuoteBean.day));
                day_all_money_right.setText(dayQuoteBean.totalPrice + "元");

                if (dayQuoteBean.vehiclePrice != 0) {
                    day_line2_money_middle.setVisibility(View.VISIBLE);
                    day_line2_money_left.setVisibility(View.VISIBLE);
                    day_line2_money_right.setVisibility(View.VISIBLE);
                    day_line2_money_left.setText(getString(R.string.service_money));
                    day_line2_money_right.setText(dayQuoteBean.vehiclePrice + "元");
                } else {
                    day_line2_money_middle.setVisibility(View.GONE);
                    day_line2_money_left.setVisibility(View.GONE);
                    day_line2_money_right.setVisibility(View.GONE);
                }


                if (dayQuoteBean.emptyDrivePrice != 0) {
                    day_line3_money_middle.setVisibility(View.VISIBLE);
                    day_line3_money_left.setVisibility(View.VISIBLE);
                    day_line3_money_right.setVisibility(View.VISIBLE);
                    day_line3_money_left.setText(getString(R.string.emptyDrivePrice));
                    day_line3_money_right.setText(dayQuoteBean.emptyDrivePrice + "元");
                } else {
                    day_line3_money_middle.setVisibility(View.GONE);
                    day_line3_money_left.setVisibility(View.GONE);
                    day_line3_money_right.setVisibility(View.GONE);
                }

                if (dayQuoteBean.longDisPrice != 0) {
                    day_line4_money_middle.setVisibility(View.VISIBLE);
                    day_line4_money_left.setVisibility(View.VISIBLE);
                    day_line4_money_right.setVisibility(View.VISIBLE);
                    day_line4_money_left.setText(getString(R.string.longDisPrice));
                    day_line4_money_right.setText(dayQuoteBean.longDisPrice + "元");
                } else {
                    day_line4_money_middle.setVisibility(View.GONE);
                    day_line4_money_left.setVisibility(View.GONE);
                    day_line4_money_right.setVisibility(View.GONE);
                }

                carsMoneyAllInfo.addView(view);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void genServiceInfo(boolean isMando){
        try {
            mansMoneyAllInfo.removeAllViews();
            mansCharge.setText(carBean.servicePrice+"元");
            mansDayNum.setText("x"+carBean.totalDays+"天");
            ServiceQuoteSumBean serviceQuoteSumBean = carBean.serviceQuoteSum;
            List<DayQuoteBean> dayQuotes = serviceQuoteSumBean.dayQuotes;
            DayQuoteBean dayQuoteBean = null;
            for (int i = 0; i < dayQuotes.size(); i++) {
                dayQuoteBean = dayQuotes.get(i);
                view = LayoutInflater.from(this.getActivity()).inflate(R.layout.car_quote_item, null);
                day_all_money_left = (TextView) view.findViewById(R.id.day_all_money_left);
                day_all_money_right = (TextView) view.findViewById(R.id.day_all_money_right);
                day_line2_money_left = (TextView) view.findViewById(R.id.day_line2_money_left);
                day_line2_money_right = (TextView) view.findViewById(R.id.day_line2_money_right);
                day_line3_money_left = (TextView) view.findViewById(R.id.day_line3_money_left);
                day_line3_money_right = (TextView) view.findViewById(R.id.day_line3_money_right);
                day_line4_money_left = (TextView) view.findViewById(R.id.day_line4_money_left);
                day_line4_money_right = (TextView) view.findViewById(R.id.day_line4_money_right);
                day_line2_money_middle = (TextView) view.findViewById(R.id.day_line2_money_middle);
                day_line3_money_middle = (TextView) view.findViewById(R.id.day_line3_money_middle);
                day_line4_money_middle = (TextView) view.findViewById(R.id.day_line4_money_middle);

                day_all_money_left.setText(String.format(getString(R.string.day_all_money), dayQuoteBean.day));
                day_all_money_right.setText(dayQuoteBean.totalPrice + "元");

                if (dayQuoteBean.guideServicePrice != 0) {
                    day_line2_money_middle.setVisibility(View.VISIBLE);
                    day_line2_money_left.setVisibility(View.VISIBLE);
                    day_line2_money_right.setVisibility(View.VISIBLE);
                    day_line2_money_left.setText(getString(R.string.service_money));
                    day_line2_money_right.setText(dayQuoteBean.guideServicePrice + "元");
                } else {
                    day_line2_money_middle.setVisibility(View.GONE);
                    day_line2_money_left.setVisibility(View.GONE);
                    day_line2_money_right.setVisibility(View.GONE);
                }

                if (dayQuoteBean.stayPrice != 0) {
                    day_line3_money_middle.setVisibility(View.VISIBLE);
                    day_line3_money_left.setVisibility(View.VISIBLE);
                    day_line3_money_right.setVisibility(View.VISIBLE);
                    day_line3_money_left.setText(getString(R.string.stayPrice));
                    day_line3_money_right.setText(dayQuoteBean.stayPrice + "元");
                } else {
                    day_line3_money_middle.setVisibility(View.GONE);
                    day_line3_money_left.setVisibility(View.GONE);
                    day_line3_money_right.setVisibility(View.GONE);
                }

                mansMoneyAllInfo.addView(view);
            }
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
}
