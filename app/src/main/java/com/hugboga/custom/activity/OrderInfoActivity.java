package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.DayQuoteBean;
import com.hugboga.custom.data.bean.ServiceQuoteSumBean;
import com.hugboga.custom.data.net.UrlLibs;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 16/8/4.
 */

public class OrderInfoActivity extends BaseActivity {
    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.header_right_btn)
    ImageView headerRightBtn;
    @BindView(R.id.header_right_txt)
    TextView headerRightTxt;
    @BindView(R.id.mans_left)
    TextView mansLeft;
    @BindView(R.id.mans_day_num)
    TextView mansDayNum;
    @BindView(R.id.mans_charge)
    TextView mansCharge;
    @BindView(R.id.mans_charge_tips)
    TextView mansChargeTips;
    @BindView(R.id.mans_money_all_info)
    LinearLayout mansMoneyAllInfo;
    @BindView(R.id.mans_charge_layout)
    RelativeLayout mansChargeLayout;
    @BindView(R.id.cars_left)
    TextView carsLeft;
    @BindView(R.id.cars_day_num)
    TextView carsDayNum;
    @BindView(R.id.cars_charge)
    TextView carsCharge;
    @BindView(R.id.cars_charge_tips)
    TextView carsChargeTips;
    @BindView(R.id.cars_money_all_info)
    LinearLayout carsMoneyAllInfo;
    @BindView(R.id.cars_charge_layout)
    RelativeLayout carsChargeLayout;
    @BindView(R.id.all_left)
    TextView allLeft;
    @BindView(R.id.all_day_num)
    TextView allDayNum;
    @BindView(R.id.all_charge)
    TextView allCharge;
    @BindView(R.id.per_left)
    TextView perLeft;
    @BindView(R.id.per_charge)
    TextView perCharge;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    protected void initHeader() {
        headerTitle.setText(R.string.all_cost_info);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_order_info;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initView();
        initHeader();
    }

    String halfDay = "0";
    protected void initView() {
        carBean = (CarBean) this.getIntent().getSerializableExtra("carBean");
        halfDay = this.getIntent().getStringExtra("halfDay");
        showContent();
    }

    CarBean carBean;
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
        String days = "";
        if(halfDay.equalsIgnoreCase("1")){
            days = "0.5";
        }else{
            days = carBean.totalDays+"";
        }
        allDayNum.setText(days+"天 X "+carBean.numOfPerson+"人");
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
            carsDayNum.setText(halfDay.equalsIgnoreCase("1")?"x 0.5天":"x"+carBean.totalDays+"天");
            ServiceQuoteSumBean serviceQuoteSumBean = carBean.vehicleQuoteSum;
            List<DayQuoteBean> dayQuotes = serviceQuoteSumBean.dayQuotes;
            DayQuoteBean dayQuoteBean = null;
            for (int i = 0; i < dayQuotes.size(); i++) {
                dayQuoteBean = dayQuotes.get(i);
                view = LayoutInflater.from(activity).inflate(R.layout.car_quote_item, null);
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
                    if(dayQuoteBean.busySeason == 1){
                        day_line2_money_left.setText(getString(R.string.vehiclePrice)+",旺季");
                    }else{
                        day_line2_money_left.setText(getString(R.string.vehiclePrice));
                    }

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
                    if(dayQuoteBean.busySeason == 1){
                        day_line3_money_left.setText(getString(R.string.emptyDrivePrice)+",旺季");
                    }else{
                        day_line3_money_left.setText(getString(R.string.emptyDrivePrice));
                    }
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
            mansDayNum.setText(halfDay.equalsIgnoreCase("1")?"x 0.5天":"x"+carBean.totalDays+"天");
            ServiceQuoteSumBean serviceQuoteSumBean = carBean.serviceQuoteSum;
            List<DayQuoteBean> dayQuotes = serviceQuoteSumBean.dayQuotes;
            DayQuoteBean dayQuoteBean = null;
            for (int i = 0; i < dayQuotes.size(); i++) {
                dayQuoteBean = dayQuotes.get(i);
                view = LayoutInflater.from(activity).inflate(R.layout.car_quote_item, null);
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
                    if(dayQuoteBean.busySeason == 1){
                        day_line2_money_left.setText(getString(R.string.service_money)+",旺季");
                    }else{
                        day_line2_money_left.setText(getString(R.string.service_money));
                    }
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
                    if(dayQuoteBean.busySeason == 1){
                        day_line3_money_left.setText(getString(R.string.stayPrice)+",旺季");
                    }else{
                        day_line3_money_left.setText(getString(R.string.stayPrice));
                    }
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



    @OnClick({R.id.befer48_tips,R.id.header_left_btn})
    public void onClick(View view) {
        HashMap<String,String> map = new HashMap<String,String>();
        switch (view.getId()) {
            case R.id.befer48_tips:
//                Bundle bundle = new Bundle();
//                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_CANCEL);
//                startFragment(new FgActivity(), bundle);

                Intent intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_CANCEL);
                startActivity(intent);
                break;
            case R.id.header_left_btn:
                finish();
                break;
        }
    }

}

