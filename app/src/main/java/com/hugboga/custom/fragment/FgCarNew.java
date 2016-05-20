package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CarViewpagerAdapter;
import com.hugboga.custom.constants.CarTypeEnum;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.widget.JazzyViewPager;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created  on 16/5/19.
 */
@ContentView(R.layout.fg_car_new)
public class FgCarNew extends BaseFragment implements ViewPager.OnPageChangeListener {
    @Bind(R.id.jazzy_pager)
    JazzyViewPager jazzyPager;
    @Bind(R.id.viewpager_layout)
    LinearLayout viewpagerLayout;
    @Bind(R.id.fg_car_name)
    TextView fgCarName;
    @Bind(R.id.mans_num)
    TextView mansNum;
    @Bind(R.id.luggage_num)
    TextView luggageNum;
    @Bind(R.id.fg_car_intro)
    TextView fgCarIntro;
    @Bind(R.id.car_info)
    LinearLayout carInfo;
    @Bind(R.id.car_price_info)
    TextView carPriceInfo;
    @Bind(R.id.driver_left)
    TextView driverLeft;
    @Bind(R.id.del_text)
    TextView delText;
    @Bind(R.id.driver_line)
    TextView driverLine;
    @Bind(R.id.driver_arrow)
    ImageView driverArrow;
    @Bind(R.id.driver_name)
    TextView driverName;
    @Bind(R.id.driver_rl)
    RelativeLayout driverRl;
    @Bind(R.id.driver_tips)
    TextView driverTips;
    @Bind(R.id.man_left)
    TextView manLeft;
    @Bind(R.id.man_right)
    ImageView manRight;
    @Bind(R.id.man_tips)
    TextView manTips;
    @Bind(R.id.man_text)
    TextView manText;
    @Bind(R.id.luggage_text)
    TextView luggageText;
    @Bind(R.id.childseat_text)
    TextView childseatText;
    @Bind(R.id.rl_man)
    RelativeLayout rlMan;
    @Bind(R.id.free_c_seat_left)
    TextView freeCSeatLeft;
    @Bind(R.id.free_c_seat_right)
    TextView freeCSeatRight;
    @Bind(R.id.free_c_seat_num)
    TextView freeCSeatNum;
    @Bind(R.id.child_left)
    TextView childLeft;
    @Bind(R.id.child_count_cost)
    TextView childCountCost;
    @Bind(R.id.child_count_text)
    TextView childCountText;
    @Bind(R.id.checkin_left)
    TextView checkinLeft;
    @Bind(R.id.checkin_money)
    TextView checkinMoney;
    @Bind(R.id.check_switch)
    CheckBox checkSwitch;
    @Bind(R.id.wait_left)
    TextView waitLeft;
    @Bind(R.id.wait_money)
    TextView waitMoney;
    @Bind(R.id.wait_switch)
    CheckBox waitSwitch;
    @Bind(R.id.car_empty_layout)
    LinearLayout carEmptyLayout;
    @Bind(R.id.free_seat_layout)
    RelativeLayout freeSeatLayout;
    @Bind(R.id.charge_seat_layout)
    RelativeLayout chargeSeatLayout;
    @Bind(R.id.checkin_layout)
    RelativeLayout checkinLayout;
    @Bind(R.id.wait_layout)
    RelativeLayout waitLayout;

    @Override
    protected void initHeader() {

    }

    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case MAN_CHILD_LUUAGE:
                ManLuggageBean manLuggageBean = (ManLuggageBean)action.getData();
                manTips.setVisibility(View.GONE);
                manText.setVisibility(View.VISIBLE);
                luggageText.setVisibility(View.VISIBLE);
                childseatText.setVisibility(View.VISIBLE);

                manText.setText("乘客 x "+(manLuggageBean.mans+manLuggageBean.childs));
                luggageText.setText("行李箱 x "+manLuggageBean.luggages);
                childseatText.setText("儿童座椅 x "+(manLuggageBean.childSeats));

                break;
            default:
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }


    int currentIndex = 0;

    @Override
    public void onPageSelected(int position) {
        if (carList.size() == 0) return;
        currentIndex = position;
        carBean = carList.get(position);
        changeText();
        EventBus.getDefault().post(new EventAction(EventType.CHANGE_CAR, carBean));
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }


    CarListBean carListBean;
    CarBean carBean;

    private void genData() {
        this.distance = carListBean.distance;
        this.interval = carListBean.interval;
//            processCarList(mParser.carList);
        carList = carListBean.carList;
        sortListDataImage();
        mAdapter = new CarViewpagerAdapter(getActivity(), mJazzy);
        mAdapter.setList(carList);
        mJazzy.setState(null);
        mJazzy.setAdapter(mAdapter);
        inflateContent();
        if (carList == null || carList.size() == 0) {
            carEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            carEmptyLayout.setVisibility(View.GONE);
        }

        if(null != carListBean.additionalServicePrice && !TextUtils.isEmpty(carListBean.additionalServicePrice.checkInPrice)){
            checkinLayout.setVisibility(View.VISIBLE);
            checkinMoney.setText("(服务费 ￥"+carListBean.additionalServicePrice.checkInPrice+")");
        }
        if(null != carListBean.additionalServicePrice && !TextUtils.isEmpty(carListBean.additionalServicePrice.pickupSignPrice)){
            waitLayout.setVisibility(View.VISIBLE);
            waitMoney.setText("(服务费 ￥"+carListBean.additionalServicePrice.checkInPrice+")");
        }
        changeText();
    }


    private void changeText() {
        carBean = carList.get(currentIndex);
        Integer[] carInfo = Constants.CarSeatInfoMap.get(carBean.carSeat);
        fgCarIntro.setText("此车型包括：" + carBean.models);
        mansNum.setText("x " + carBean.capOfPerson);
        luggageNum.setText("x " + carBean.capOfLuggage);
    }



    private void initListData() {
        int id = 1;
        CarBean bean;
        carList = new ArrayList<CarBean>(16);
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                bean = new CarBean();
                bean.id = id;
                bean.carType = i;
                bean.carSeat = Constants.CarSeatMap.get(j);
                bean.originalPrice = 0;
                bean.models = Constants.CarDescInfoMap.get(i).get(j);
                CarTypeEnum carTypeEnum = CarTypeEnum.getCarType(bean.carType, bean.carSeat);
                if (carTypeEnum != null) {
                    bean.imgRes = carTypeEnum.imgRes;
                }
                carList.add(bean);
                id++;
            }
        }
    }

    private void sortListDataImage() {
        for (CarBean bean : carList) {
            CarTypeEnum carTypeEnum = CarTypeEnum.getCarType(bean.carType, bean.carSeat);
            if (carTypeEnum != null) {
                bean.imgRes = carTypeEnum.imgRes;
            }
        }
    }

    @Override
    protected void initView() {
        initView(getView());
        carListBean = this.getArguments().getParcelable("carListBean");
        genData();
    }

    private JazzyViewPager mJazzy;
    private double distance;//预估路程（单位：公里）
    private int interval;//预估时间（单位：分钟）
    private List<CarBean> carList = new ArrayList<CarBean>();
    private CarViewpagerAdapter mAdapter;

    private void initView(View view) {
        View flGalleryContainer = view.findViewById(R.id.viewpager_layout);
        flGalleryContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mJazzy.dispatchTouchEvent(event);
            }
        });
        mJazzy = (JazzyViewPager) view.findViewById(R.id.jazzy_pager);
        mJazzy.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);
        mAdapter = new CarViewpagerAdapter(getActivity(), mJazzy);
        initListData();
        mAdapter.setList(carList);
        mJazzy.setAdapter(mAdapter);
        mJazzy.setOffscreenPageLimit(5);
        mJazzy.addOnPageChangeListener(this);

        carPriceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FgWebInfo fgWebInfo = new FgWebInfo();
                Bundle bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_PRICE);
                startFragment(fgWebInfo, bundle);
            }
        });
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


    @OnClick({R.id.del_text, R.id.driver_name, R.id.man_tips, R.id.man_text, R.id.luggage_text, R.id.childseat_text,R.id.rl_man})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.del_text:
                break;
            case R.id.driver_name:
                break;
            case R.id.rl_man:
            case R.id.man_tips:
            case R.id.man_text:
            case R.id.luggage_text:
            case R.id.childseat_text:
                FgManLuggage fgManLuggage = new FgManLuggage();
                Bundle bundle = new Bundle();
                bundle.putParcelable("carListBean",carListBean);
                bundle.putInt("currentIndex",currentIndex);
                fgManLuggage.setArguments(bundle);
                startFragment(fgManLuggage);
                break;
        }
    }
}
