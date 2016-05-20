package com.hugboga.custom.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created  on 16/5/18.
 */
@ContentView(R.layout.fg_man_luggage)
public class FgManLuggage extends BaseFragment {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.m_left)
    TextView mLeft;
    @Bind(R.id.m_sub)
    TextView mSub;
    @Bind(R.id.m_num)
    TextView mNum;
    @Bind(R.id.m_plus)
    TextView mPlus;
    @Bind(R.id.c_left)
    TextView cLeft;
    @Bind(R.id.c_right)
    TextView cRight;
    @Bind(R.id.c_sub)
    TextView cSub;
    @Bind(R.id.c_num)
    TextView cNum;
    @Bind(R.id.c_plus)
    TextView cPlus;
    @Bind(R.id.l_left)
    TextView lLeft;
    @Bind(R.id.l_right)
    TextView lRight;
    @Bind(R.id.l_sub)
    TextView lSub;
    @Bind(R.id.l_num)
    TextView lNum;
    @Bind(R.id.l_plus)
    TextView lPlus;
    @Bind(R.id.c_seat_left)
    TextView cSeatLeft;
    @Bind(R.id.c_seat_sub)
    TextView cSeatSub;
    @Bind(R.id.c_seat_num)
    TextView cSeatNum;
    @Bind(R.id.c_seat_plus)
    TextView cSeatPlus;
    @Bind(R.id.free_c_seat_left)
    TextView freeCSeatLeft;
    @Bind(R.id.free_c_seat_right)
    TextView freeCSeatRight;
    @Bind(R.id.free_c_seat_num)
    TextView freeCSeatNum;
    @Bind(R.id.charge_seat_left)
    TextView chargeSeatLeft;
    @Bind(R.id.charge_seat_right)
    TextView chargeSeatRight;
    @Bind(R.id.charge_seat_num)
    TextView chargeSeatNum;
    @Bind(R.id.show_child_seat)
    LinearLayout showChildSeat;
    @Bind(R.id.top_tips)
    TextView topTips;

    ManLuggageBean bean = new ManLuggageBean();

    int mNums = 0;
    int cNums = 0;
    int lNums = 0;
    int seatNums = 0;

    int maxMans = 0;//最大人数
    int maxLuuages = 0;//最大行李数

    @Override
    protected void initHeader() {
        fgTitle.setText(R.string.man_luggage_title);
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fgRightBtn.setText(R.string.dialog_btn_sure);
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.mans = mNums;
                bean.childs = cNums;
                bean.luggages = lNums;
                bean.childSeats = seatNums;
                EventBus.getDefault().post(new EventAction(EventType.MAN_CHILD_LUUAGE,bean));
                finish();
            }
        });
    }

    CarListBean carListBean;
    int currentIndex = 0;
    CarBean carBean;
    @Override
    protected void initView() {
        carListBean =  this.getArguments().getParcelable("carListBean");
        currentIndex = this.getArguments().getInt("currentIndex");
        carBean = carListBean.carList.get(currentIndex);
        if(!carListBean.supportChildseat){
            topTips.setVisibility(View.VISIBLE);
        }
        mNums = carBean.capOfPerson;
        lNums = carBean.capOfLuggage;

        maxMans = mNums;
        maxLuuages = lNums;

        mNum.setText(mNums+"");
        lNum.setText(lNums+"");

//        showChildSeat.setVisibility(View.VISIBLE);
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
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.m_sub, R.id.m_num, R.id.m_plus, R.id.c_sub, R.id.c_num, R.id.c_plus, R.id.l_sub, R.id.l_num, R.id.l_plus, R.id.c_seat_sub, R.id.c_seat_num, R.id.c_seat_plus, R.id.free_c_seat_num, R.id.charge_seat_num})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_sub:
                if(mNums > 1){
                    mNums --;
                    mPlus.setBackgroundColor(Color.parseColor("#fad027"));
                    cPlus.setBackgroundColor(Color.parseColor("#fad027"));
                    lPlus.setBackgroundColor(Color.parseColor("#fad027"));
                    mNum.setText(mNums+"");
                    if(cNums == 1) {
                        mSub.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                }
                break;
            case R.id.m_plus:
                if(mNums < (maxMans - cNums)){
                    mNums ++;
                    mSub.setBackgroundColor(Color.parseColor("#fad027"));
                    if(mNums == (maxMans - cNums)){
                        mPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
                        cPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
                        if(lNums == maxLuuages){
                            lPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
                        }
                    }
                    mNum.setText(mNums+"");
                }
                break;
            case R.id.c_sub:
                if(cNums > 0){
                    if(seatNums > 0){
                        seatNums --;
                        cSeatPlus.setBackgroundColor(Color.parseColor("#fad027"));
                        cSeatNum.setText(seatNums+"");
                    }
                    if(seatNums == 0){
                        cSeatSub.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                    cNums --;
                    cPlus.setBackgroundColor(Color.parseColor("#fad027"));
                    mPlus.setBackgroundColor(Color.parseColor("#fad027"));
                    lPlus.setBackgroundColor(Color.parseColor("#fad027"));
                    cNum.setText(cNums+"");
                    if(cNums == 0) {
                        showChildSeat.setVisibility(View.GONE);
                        cSub.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                }
                break;
            case R.id.c_plus:
                if((mNums + cNums) < maxMans){
                    cNums ++;
                    cSub.setBackgroundColor(Color.parseColor("#fad027"));
                    cNum.setText(cNums+"");
                    if(carListBean.supportChildseat) {
                        showChildSeat.setVisibility(View.VISIBLE);
                    }

                    if(cNums == (maxMans - mNums)){
                        cPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
                        mPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
                        if(lNums == maxLuuages){
                            lPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
                        }
                    }
                }
                break;
            case R.id.l_sub:
                if(lNums > 0){
                    lNums --;
                    lPlus.setBackgroundColor(Color.parseColor("#fad027"));
                    lNum.setText(lNums+"");
                    if(lNums == 0) {
                        lSub.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                    if((mNums + cNums) < maxMans){
                        mPlus.setBackgroundColor(Color.parseColor("#fad027"));
                        cPlus.setBackgroundColor(Color.parseColor("#fad027"));
                    }
                }
                break;
            case R.id.l_plus:
                if(lNums < (maxLuuages + (maxMans - cNums - mNums))){
                    lNums ++;
                    lSub.setBackgroundColor(Color.parseColor("#fad027"));
                    if(lNums == (maxLuuages + (maxMans - cNums - mNums))){
                        lPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
                        mPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
                        cPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                    lNum.setText(lNums+"");
                }
                break;
            case R.id.c_seat_sub:
                if(seatNums > 0){
                    seatNums --;
                    cSeatPlus.setBackgroundColor(Color.parseColor("#fad027"));
                    cSeatNum.setText(seatNums+"");
                    if(seatNums == 0){
                        cSeatSub.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                }
                break;
            case R.id.c_seat_plus:
                if(seatNums < cNums){
                    seatNums ++;
                    if(seatNums == cNums){
                        cSeatPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                    cSeatSub.setBackgroundColor(Color.parseColor("#fad027"));
                    cSeatNum.setText(seatNums+"");
                }
                break;
        }
    }

}
