package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CarViewpagerAdapter;
import com.hugboga.custom.constants.CarTypeEnum;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.widget.JazzyViewPager;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Sku 选车
 * Created by admin on 2016/3/20.
 */
@ContentView(R.layout.fg_car_sku)
public class FgCarSuk extends BaseFragment implements  ViewPager.OnPageChangeListener {

    public static final String KEY_CAR_LIST = "KEY_CAR_LIST";
    public static final String KYE_POSITION = "KYE_POSITION";

    @ViewInject(R.id.activity_head_layout)
    View headerLayout;
    @ViewInject(R.id.head_text_title)
    TextView headerTitle;
    @ViewInject(R.id.head_text_right)
    TextView headerRight;
    @ViewInject(R.id.head_btn_left)
    View headerLeft;
    @ViewInject(R.id.jazzy_pager)
    private JazzyViewPager mJazzy;

    @ViewInject(R.id.fg_car_info)
    private TextView carInfoText;//乘坐%d人 | 行李%d件
    @ViewInject(R.id.fg_car_intro)
    private TextView carInfoIntro;//此车型包括
    @ViewInject(R.id.fg_car_tip)
    private TextView carTip;// 提示

    private CarViewpagerAdapter mAdapter;
    private CarListBean carListBean;
    private int mPosition;

    @Override
    protected void initHeader() {
        headerLayout.setBackgroundColor(getResources().getColor(R.color.all_bg_yellow));
        headerTitle.setText(R.string.select_car_type);
        headerLeft.setVisibility(View.GONE);
        headerRight.setText(R.string.dialog_btn_ok);
        headerRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView() {
        View flGalleryContainer = getView().findViewById(R.id.viewpager_layout);
        flGalleryContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mJazzy.dispatchTouchEvent(event);
            }
        });
        mJazzy = (JazzyViewPager) getView().findViewById(R.id.jazzy_pager);
        mJazzy.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);
        mAdapter = new CarViewpagerAdapter(getActivity(), mJazzy);
        mJazzy.setAdapter(mAdapter);
        mJazzy.setOffscreenPageLimit(5);
        mJazzy.addOnPageChangeListener(this);
        carListBean = (CarListBean) getArguments().getSerializable(KEY_CAR_LIST);
        if(carListBean!=null) {
            initListData();
            mAdapter.setList(carListBean.carList);

        }
    }

    private void initListData() {
        for(CarBean bean :carListBean.carList){
            CarTypeEnum carTypeEnum = CarTypeEnum.getCarType(bean.carType,bean.carSeat);
            if(carTypeEnum!=null){
                bean.imgRes = carTypeEnum.imgRes;
            }
        }
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }
 @Event({R.id.head_text_right,R.id.car_sku_layout})
    private void onClickView(View view){
        switch (view.getId()){
            case R.id.head_text_right:
                Bundle bundle = new Bundle();
                bundle.putInt(KYE_POSITION,mPosition);
                finishForResult(bundle);
                break;
            case R.id.car_sku_layout:
//                finish();
                break;
        }

    }

    
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ArrayList<CarBean> carList = carListBean.carList;
        if(carList==null||carList.size()==0)return;
        this.mPosition = position;
        CarBean carBean = carList.get(position);
        Integer[] carInfo = Constants.CarSeatInfoMap.get(carBean.carSeat);
        carInfoText.setText(String.format("乘坐%d人 | 行李%d件", carInfo[0], carInfo[1]));
        carInfoIntro.setText("此车型包括："+carBean.models);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
