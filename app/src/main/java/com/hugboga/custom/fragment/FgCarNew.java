package com.hugboga.custom.fragment;

import android.graphics.Color;
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
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCollectGuidesFilter;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.ToastUtils;
import com.hugboga.custom.widget.JazzyViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.type;
import static com.hugboga.custom.R.id.child_count_cost;
import static com.hugboga.custom.R.id.driver_name;
import static com.hugboga.custom.R.id.l_sub;


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

    @Bind(R.id.l_left)
    TextView lLeft;
    @Bind(R.id.l_right)
    TextView lRight;
    @Bind(R.id.show_luggage_info)
    TextView showLuggageInfo;
    @Bind(l_sub)
    TextView lSub;
    @Bind(R.id.l_num)
    TextView lNum;
    @Bind(R.id.l_plus)
    TextView lPlus;
    @Bind(R.id.hotel_layout)
    RelativeLayout hotelLayout;

    @Override
    protected void initHeader() {

    }


    private void showChildSeatLayout(int seatNums) {
        if (null == carListBean.additionalServicePrice.childSeatPrice1
                && null == carListBean.additionalServicePrice.childSeatPrice2) {

        } else {
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

    int cityId = -1;
    String startTime = null;
    String endTime = null;

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

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_GUIDE:
                collectGuideBean = (CollectGuideBean) action.getData();
                initGuideLayout();
                break;
            case GUIDE_ERROR_TIME:
                driverTips.setVisibility(View.VISIBLE);
                break;
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

    private void genCar() {
        mAdapter = new CarViewpagerAdapter(getActivity(), mJazzy);
        if (null != collectGuideBean) {
            carList = guideCarList;
        } else {
            carList = oldCarList;
        }
        carList = CarUtils.initCarListData(carList);
        if(null != carList) {
            mAdapter.setList(carList);
            mJazzy.setState(null);
            mJazzy.setOffscreenPageLimit(3);
            mJazzy.setTransitionEffect(JazzyViewPager.TransitionEffect.Tablet);
            mJazzy.setAdapter(mAdapter);
        }
    }

    private void genData() {
        genCar();
        if(canService) {
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
            EventBus.getDefault().post(new EventAction(EventType.CAR_CHANGE_SMALL));

        }
    }

    CollectGuideBean collectGuideBean;

    int hotelNum = 1;
    @Override
    protected void initView() {
        initView(getView());

        cityId = this.getArguments().getInt("cityId");
        startTime = this.getArguments().getString("startTime");
        endTime = this.getArguments().getString("endTime");

        checkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    EventBus.getDefault().post(new EventAction(EventType.CHECK_SWITCH, true));
                } else {
                    EventBus.getDefault().post(new EventAction(EventType.CHECK_SWITCH, false));
                }
            }
        });

        waitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    EventBus.getDefault().post(new EventAction(EventType.WAIT_SWITCH, true));
                } else {
                    EventBus.getDefault().post(new EventAction(EventType.WAIT_SWITCH, false));
                }
            }
        });
        waitSwitch.setChecked(true);
        carListBean = this.getArguments().getParcelable("carListBean");
        if (null != carListBean) {
            oldCarList = carListBean.carList;

            boolean showHotel = carListBean.showHotel;
            if (showHotel) {
                lRight.setText("共"+carListBean.hotelNum+"晚");
                hotelLayout.setVisibility(View.VISIBLE);
            }
        }
        collectGuideBean = (CollectGuideBean) this.getArguments().getSerializable("collectGuideBean");
        initGuideLayout();
    }

    //是否可以服务
    boolean canService = true;
    private void initGuideLayout() {
        if (null != collectGuideBean) {

            if (null != carListBean) {
                carBean = CarUtils.isMatchLocal(CarUtils.getNewCarBean(collectGuideBean), carListBean.carList);
            } else {
                carBean = CarUtils.getNewCarBean(collectGuideBean);
            }
            if(null == carBean){
                canService = false;
                carBean = CarUtils.getNewCarBean(collectGuideBean);
                ToastUtils.showShort(R.string.no_have_car);
            }else{
                canService = true;
            }

            driver_layout.setVisibility(View.VISIBLE);
            driverName.setText(collectGuideBean.name);
            man_luggage_layout.setVisibility(View.GONE);

            final ArrayList<CarBean> newCarList = new ArrayList<>();
            newCarList.add(carBean);
            guideCarList = newCarList;
            delText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != oldCarList) {
                        driver_layout.setVisibility(View.GONE);
                        genData();
                    } else {
                        car_layout.setVisibility(View.GONE);
                    }
                    collectGuideBean = null;
                    if (null != carListBean) {
                        carListBean.carList = oldCarList;
                        carList = oldCarList;
                        mAdapter = new CarViewpagerAdapter(getActivity(), mJazzy);
                        mAdapter.setList(oldCarList);
                        mJazzy.setAdapter(mAdapter);
                        if (null != carList) {
                            carBean = carList.get(currentIndex);
                            fgCarIntro.setText("此车型包括：" + carBean.models);
                            mansNum.setText("x " + carBean.capOfPerson);
                            luggageNum.setText("x " + carBean.capOfLuggage);
                            manTips.setVisibility(View.VISIBLE);
                            manText.setText("");
                            luggageText.setText("");
                            childseatText.setText("");
                        }
                    }
                    EventBus.getDefault().post(new EventAction(EventType.GUIDE_DEL, carBean));
                }
            });

            if (null != carBean) {
                fgCarIntro.setText("此车型包括：" + carBean.models);
                mansNum.setText("x " + carBean.capOfPerson);
                luggageNum.setText("x " + carBean.capOfLuggage);
            }

            man_luggage_layout.setVisibility(View.VISIBLE);
            driverName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(startTime)) {
                        AlertDialogUtils.showAlertDialogOneBtn(getActivity(), getString(R.string.dairy_choose_guide), "好的");
                    } else {
                        FgCollectGuideList fgCollectGuideList = new FgCollectGuideList();
                        Bundle bundle = new Bundle();
                        RequestCollectGuidesFilter.CollectGuidesFilterParams params = new RequestCollectGuidesFilter.CollectGuidesFilterParams();
                        params.startCityId = cityId;
                        params.startTime = startTime;
                        params.endTime = endTime;
                        params.adultNum = collectGuideBean.numOfPerson;
                        params.childrenNum = collectGuideBean.numOfPerson;
                        params.childSeatNum = collectGuideBean.carClass;
                        params.luggageNum = collectGuideBean.numOfLuggage;
                        params.orderType = type;
                        params.totalDays = 0;
                        params.passCityId = cityId + "";
                        bundle.putSerializable(Constants.PARAMS_DATA, params);
                        fgCollectGuideList.setArguments(bundle);
                        startFragment(fgCollectGuideList);
                    }
                }
            });
            if (null == carListBean) {
                genCar();
            } else {
                genData();
            }
        } else {
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

    int hotelHourseNum = 1;
    @OnClick({l_sub,R.id.l_plus, R.id.man_tips, R.id.man_text, R.id.luggage_text, R.id.childseat_text, R.id.rl_man})
    public void onClick(View view) {
        switch (view.getId()) {
            case l_sub:
                if(hotelHourseNum >1) {
                    --hotelHourseNum;
                    EventBus.getDefault().post(new EventAction(EventType.SKU_HOTEL_NUM_CHANGE,hotelHourseNum));
                }
                lNum.setText(hotelHourseNum+"");
                if(hotelHourseNum == 1){
                    lSub.setBackgroundColor(Color.parseColor("#d5dadb"));
                }
                break;
            case R.id.l_plus:
                ++hotelHourseNum;
                lNum.setText(hotelHourseNum+"");
                EventBus.getDefault().post(new EventAction(EventType.SKU_HOTEL_NUM_CHANGE,hotelHourseNum));
                if(hotelHourseNum > 1){
                    lSub.setBackgroundColor(Color.parseColor("#fad027"));
                }
                break;
            case R.id.rl_man:
            case R.id.man_tips:
            case R.id.man_text:
            case R.id.luggage_text:
            case R.id.childseat_text:
                if (null == carListBean || null == carListBean.carList) {
                    ToastUtils.showShort(R.string.no_price_error);
                    return;
                }
                FgManLuggage fgManLuggage = new FgManLuggage();
                Bundle bundle = new Bundle();
                if (null != collectGuideBean) {
                    carListBean.carList = guideCarList;
                } else {
                    carListBean.carList = oldCarList;
                }
                bundle.putParcelable("carListBean", carListBean);
                bundle.putInt("currentIndex", currentIndex);
                bundle.putParcelable("manLuggageBean", manLuggageBean);
                fgManLuggage.setArguments(bundle);
                startFragment(fgManLuggage);
                break;
        }
    }
}
