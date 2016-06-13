package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCollectGuidesFilter;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.widget.JazzyViewPager;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.hugboga.custom.R.id.child_count_cost;
import static com.hugboga.custom.R.id.choose_driver;
import static com.hugboga.custom.R.id.del_text;
import static com.hugboga.custom.R.id.driver_name;
import static com.hugboga.custom.R.id.nums;
import static com.hugboga.custom.R.id.show_cars_layout_pick;


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
    @Bind(driver_name)
    TextView driverName;
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
    @Bind(child_count_cost)
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

    @Bind(R.id.driver_layout)
    RelativeLayout driver_layout;


    @Bind(R.id.man_luggage_layout)
    LinearLayout man_luggage_layout;

    @Bind(R.id.car_layout)
    LinearLayout car_layout;

    @Bind(R.id.have_data_layout)
    LinearLayout have_data_layout;

    @Override
    protected void initHeader() {

    }


    private void showChildSeatLayout(int seatNums) {
        if(null == carListBean.additionalServicePrice.childSeatPrice1
                && null == carListBean.additionalServicePrice.childSeatPrice2){

        }else {
            String seat1 = carListBean.additionalServicePrice.childSeatPrice1;
            String seat2 = carListBean.additionalServicePrice.childSeatPrice2;
            if (seatNums == 1 && null == seat1) {
                freeSeatLayout.setVisibility(View.VISIBLE);
            } else if (seatNums == 1) {
                freeCSeatLeft.setText("收费儿童座椅");
                freeSeatLayout.setVisibility(View.VISIBLE);
                freeCSeatRight.setText("￥" + seat1 + "/次");
            }
            if (seatNums > 1) {
                if (null != seat1) {
                    freeCSeatLeft.setText("收费儿童座椅");
                    freeSeatLayout.setVisibility(View.VISIBLE);
                    freeCSeatRight.setText("￥" + seat1 + "/次");
                } else {
                    freeSeatLayout.setVisibility(View.VISIBLE);
                }

                chargeSeatLayout.setVisibility(View.VISIBLE);
                childCountCost.setText("￥" + seat2 + "/次");
                childCountText.setText("x" + (seatNums - 1) + "");
            }
        }
    }

    private void hideChildSeatLayout(int seatNums) {
        if (seatNums > 1) {
            chargeSeatLayout.setVisibility(View.VISIBLE);
            childCountText.setText("x" + (seatNums - 1) + "");
        }

        if (seatNums == 1) {
            chargeSeatLayout.setVisibility(View.GONE);
        }

        if (seatNums == 0) {
            freeSeatLayout.setVisibility(View.GONE);
            chargeSeatLayout.setVisibility(View.GONE);
        }
    }

    ManLuggageBean manLuggageBean;

    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case MAN_CHILD_LUUAGE:
                manLuggageBean = (ManLuggageBean) action.getData();
                manTips.setVisibility(View.GONE);
                manText.setVisibility(View.VISIBLE);
                if (manLuggageBean.luggages != 0) {
                    luggageText.setVisibility(View.VISIBLE);
                } else {
                    luggageText.setVisibility(View.GONE);
                }

                if (manLuggageBean.childSeats != 0) {
                    childseatText.setVisibility(View.VISIBLE);
                    showChildSeatLayout(manLuggageBean.childSeats);
                } else {
                    hideChildSeatLayout(manLuggageBean.childSeats);
                    childseatText.setVisibility(View.GONE);
                }

                manText.setText("乘客 x " + (manLuggageBean.mans + manLuggageBean.childs));
                luggageText.setText("行李箱 x " + manLuggageBean.luggages);
                childseatText.setText("儿童座椅 x " + (manLuggageBean.childSeats));
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


    private void genCar(){
        mAdapter = new CarViewpagerAdapter(getActivity(), mJazzy);
        if(null != collectGuideBean) {
            carList = guideCarList;
            if(null == carListBean){
                carListBean = new CarListBean();
            }
            carListBean.carList = guideCarList;
        }else{
            carList = oldCarList;
        }
        carList = CarUtils.initCarListData(carList);
        mAdapter.setList(carList);
        mJazzy.setState(null);
        mJazzy.setTransitionEffect(JazzyViewPager.TransitionEffect.Tablet);
        mJazzy.setAdapter(mAdapter);
    }

    private void genData() {
        genCar();
        this.distance = carListBean.distance;
        this.interval = carListBean.interval;
        if (carList == null || carList.size() == 0) {
            carEmptyLayout.setVisibility(View.VISIBLE);
            have_data_layout.setVisibility(View.GONE);
        } else {
            carEmptyLayout.setVisibility(View.GONE);
            have_data_layout.setVisibility(View.VISIBLE);
            changeText();
            if (null != carListBean.additionalServicePrice && null != carListBean.additionalServicePrice.checkInPrice) {
                checkinLayout.setVisibility(View.VISIBLE);
                checkinMoney.setText("(服务费 ￥" + carListBean.additionalServicePrice.checkInPrice + ")");
            }
            if (null != carListBean.additionalServicePrice && null != carListBean.additionalServicePrice.pickupSignPrice) {
                waitLayout.setVisibility(View.VISIBLE);
                waitMoney.setText("(服务费 ￥" + carListBean.additionalServicePrice.pickupSignPrice + ")");
            }
        }
    }


    private void changeText() {
            carBean = carList.get(currentIndex);
            fgCarIntro.setText("此车型包括：" + carBean.models);
            mansNum.setText("x " + carBean.capOfPerson);
            luggageNum.setText("x " + carBean.capOfLuggage);


        int selectMansNUm = 0;
        if (null != manLuggageBean) {
            selectMansNUm = manLuggageBean.mans
                    + (int) Math.round((manLuggageBean.childSeats) * 1.5)
                    + (manLuggageBean.childs - manLuggageBean.childSeats);
        }

        if (null != manLuggageBean
                && selectMansNUm >= carBean.capOfPerson
                && (selectMansNUm + manLuggageBean.luggages) >= (carBean.capOfPerson + carBean.capOfLuggage)) {
            manTips.setVisibility(View.VISIBLE);
            manText.setVisibility(View.GONE);
            luggageText.setVisibility(View.GONE);
            childseatText.setVisibility(View.GONE);
            hideChildSeatLayout(0);
            manLuggageBean = null;
        }
    }
    CollectGuideBean collectGuideBean;


    @Override
    protected void initView() {
        initView(getView());

        checkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    EventBus.getDefault().post(new EventAction(EventType.CHECK_SWITCH,true));
                }else{
                    EventBus.getDefault().post(new EventAction(EventType.CHECK_SWITCH,false));
                }
            }
        });

        waitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    EventBus.getDefault().post(new EventAction(EventType.WAIT_SWITCH,true));
                }else{
                    EventBus.getDefault().post(new EventAction(EventType.WAIT_SWITCH,false));
                }
            }
        });
        carListBean = this.getArguments().getParcelable("carListBean");
        if(null != carListBean) {
            oldCarList = carListBean.carList;
        }
        collectGuideBean = (CollectGuideBean) this.getArguments().getSerializable("collectGuideBean");
        if (null != collectGuideBean) {
            driver_layout.setVisibility(View.VISIBLE);
            driverName.setText(collectGuideBean.name);
            man_luggage_layout.setVisibility(View.GONE);
            if(null != carListBean) {
                carBean = CarUtils.isMatchLocal(CarUtils.getNewCarBean(collectGuideBean), carListBean.carList);
            }else{
                carBean = CarUtils.getNewCarBean(collectGuideBean);
            }
            final ArrayList<CarBean> newCarList = new ArrayList<>();
            newCarList.add(carBean);
            guideCarList = newCarList;
            delText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != oldCarList) {
                        driver_layout.setVisibility(View.GONE);
                        genData();
                    }else{
                        car_layout.setVisibility(View.GONE);
                    }
                    collectGuideBean = null;
                    carListBean.carList = oldCarList;
                    carList = oldCarList;
                    mAdapter = new CarViewpagerAdapter(getActivity(), mJazzy);
                    mAdapter.setList(oldCarList);
                    mJazzy.setAdapter(mAdapter);
                    carBean = carList.get(currentIndex);
                    fgCarIntro.setText("此车型包括：" + carBean.models);
                    mansNum.setText("x " + carBean.capOfPerson);
                    luggageNum.setText("x " + carBean.capOfLuggage);

                    manTips.setVisibility(View.VISIBLE);
                    manText.setText("");
                    luggageText.setText("");
                    childseatText.setText("");


                    EventBus.getDefault().post(new EventAction(EventType.GUIDE_DEL,carBean));
                }
            });

            fgCarIntro.setText("此车型包括：" + carBean.models);
            mansNum.setText("x " + carBean.capOfPerson);
            luggageNum.setText("x " + carBean.capOfLuggage);

            man_luggage_layout.setVisibility(View.VISIBLE);
            driverName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    FgCollectGuideList fgCollectGuideList = new FgCollectGuideList();
//                    Bundle bundle = new Bundle();
//                    RequestCollectGuidesFilter.CollectGuidesFilterParams params = new RequestCollectGuidesFilter.CollectGuidesFilterParams();
//                    params.startCityId = startBean.cityId;
//                    params.startTime = isHalfTravel ? halfDate + " 00:00:00" : start_date_str + " 00:00:00";
//                    params.endTime = isHalfTravel ? halfDate + " 00:00:00" : end_date_str + " 00:00:00";
//                    params.adultNum = collectGuideBean.numOfPerson;
//                    params.childrenNum = collectGuideBean.numOfPerson;
//                    params.childSeatNum = collectGuideBean.carClass;
//                    params.luggageNum = collectGuideBean.numOfLuggage;
//                    params.orderType = 5;
//                    params.totalDays = 0;
//                    params.passCityId = startBean.cityId+"";
//                    bundle.putSerializable(Constants.PARAMS_DATA, params);
//                    fgCollectGuideList.setArguments(bundle);
//                    startFragment(fgCollectGuideList);
                }
            });
            genCar();
        }else{
            man_luggage_layout.setVisibility(View.VISIBLE);
            genData();
        }

    }

    private JazzyViewPager mJazzy;
    private double distance;//预估路程（单位：公里）
    private int interval;//预估时间（单位：分钟）
    private ArrayList<CarBean> carList = new ArrayList<CarBean>();
    private ArrayList<CarBean> guideCarList = new ArrayList<CarBean>();
    private ArrayList<CarBean> oldCarList = null;
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
//        initListData();
        mAdapter.setList(carList);
        mJazzy.setAdapter(mAdapter);
        mJazzy.setOffscreenPageLimit(1);
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


    @OnClick({del_text, driver_name, R.id.man_tips, R.id.man_text, R.id.luggage_text, R.id.childseat_text, R.id.rl_man})
    public void onClick(View view) {
        switch (view.getId()) {
            case del_text:
                break;
            case driver_name:
                break;
            case R.id.rl_man:
            case R.id.man_tips:
            case R.id.man_text:
            case R.id.luggage_text:
            case R.id.childseat_text:
                FgManLuggage fgManLuggage = new FgManLuggage();
                Bundle bundle = new Bundle();
                bundle.putParcelable("carListBean", carListBean);
                bundle.putInt("currentIndex", currentIndex);
                bundle.putParcelable("manLuggageBean",manLuggageBean);
                fgManLuggage.setArguments(bundle);
                startFragment(fgManLuggage);
                break;
        }
    }
}
