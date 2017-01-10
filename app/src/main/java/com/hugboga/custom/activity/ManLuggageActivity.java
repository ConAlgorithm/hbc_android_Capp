package com.hugboga.custom.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.Tools;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/8/3.
 */

public class ManLuggageActivity extends BaseActivity {
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

    @Bind(R.id.show_luggage_info)
    ImageView show_luggage_info;

    ManLuggageBean bean = new ManLuggageBean();

    int mNums = 0;
    int cNums = 0;
    int lNums = 0;
    int seatNums = 0;

    int maxMans = 0;//最大人数
    int maxLuuages = 0;//最大行李数
    @Bind(R.id.free_layout)
    RelativeLayout freeLayout;
    @Bind(R.id.charge_layout)
    RelativeLayout chargeLayout;
    @Bind(R.id.fg_car_class)
    TextView fgCarClass;
    @Bind(R.id.fg_car_name)
    TextView fgCarName;
    @Bind(R.id.mans_num)
    TextView mansNum;
    @Bind(R.id.luggage_num)
    TextView luggageNum;

    protected void initHeader() {
        headerTitle.setText(R.string.man_luggage_title);
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerRightTxt.setText(R.string.dialog_btn_sure);
        headerRightTxt.setVisibility(View.VISIBLE);
        headerRightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.mans = mNums;
                bean.childs = cNums;
                bean.luggages = lNums;
                bean.childSeats = seatNums;
                EventBus.getDefault().post(new EventAction(EventType.MAN_CHILD_LUUAGE, bean));
                finish();
            }
        });
    }

    CarListBean carListBean;
    int currentIndex = 0;
    CarBean carBean;

    boolean supportChildseat = true;
    ManLuggageBean manLuggageBean;
    String seat1 = "";
    String seat2 = "";

    protected void initView() {
        carListBean = (CarListBean)this.getIntent().getSerializableExtra("carListBean");
        currentIndex = this.getIntent().getIntExtra("currentIndex",0);
        manLuggageBean = (ManLuggageBean)this.getIntent().getSerializableExtra("manLuggageBean");

        carBean = carListBean.carList.get(currentIndex);
        if (null == carListBean.additionalServicePrice || (null == carListBean.additionalServicePrice.childSeatPrice1
                && null == carListBean.additionalServicePrice.childSeatPrice2)) {
            supportChildseat = false;
//            topTips.setVisibility(View.VISIBLE);
        } else {
            seat1 = carListBean.additionalServicePrice.childSeatPrice1;
            seat2 = carListBean.additionalServicePrice.childSeatPrice2;
        }
        mNums = carBean.capOfPerson - 2;
        lNums = carBean.capOfLuggage;

        maxMans = carBean.capOfPerson;
        maxLuuages = lNums;

        mNum.setText(mNums + "");
        lNum.setText(lNums + "");

        if (mNums == 1) {
            mSub.setBackgroundColor(Color.parseColor("#d5dadb"));
        }

        fgCarClass.setText(carBean.desc);
        mansNum.setText(carBean.capOfPerson+"");
        luggageNum.setText(carBean.capOfLuggage+"");

        initData(manLuggageBean);

//        showChildSeat.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_man_luggage);
        ButterKnife.bind(this);
        initView();
        initHeader();
    }

    private void initData(ManLuggageBean manLuggageBean) {
        if (null != manLuggageBean) {
            mNums = manLuggageBean.mans;
            lNums = manLuggageBean.luggages;
            cNums = manLuggageBean.childs;

            mNum.setText(manLuggageBean.mans + "");
            lNum.setText(manLuggageBean.luggages + "");
            cNum.setText(manLuggageBean.childs + "");
            if (manLuggageBean.childSeats > 0) {
                seatNums = manLuggageBean.childSeats;
                cSeatNum.setText(seatNums + "");
                showChildSeat.setVisibility(View.VISIBLE);
                manLuggageShowChildSeatLayout();
            }

            subChangeBg();
        }
    }


//    1.首次进入默认显示该车型最多乘客，和最多行李（乘客数全部累加在“大人”中）
//    2. 每个模块达到最大情况，对应加号是禁用状态
//    3. 减少1个乘客，可增加一个行李
//    4. 大人数+儿童数，不能超过最大乘车人数
//    5. 大人数+用座椅儿童数*座椅系数+不用座椅儿童数+行李箱数，不能超最大乘车人数+最大行李数（24寸）
//            6. 大人数最小值为1，等于1时，减号禁用；其他最小值为0
//    7. 座椅系数 1.5

    //检测+1后 是否满足条件5
    private boolean checkAddNums(int type) {//1 成人  2, 儿童 3,行李  4, 儿童座椅  0,检测当前值
        switch (type) {
            case 1:
            case 2:
                return ((mNums + 1 + Math.round(seatNums * 1.5) + (cNums - seatNums)) <= maxMans)
                        && ((mNums + 1 + Math.round((seatNums) * 1.5) + (cNums - seatNums)) + lNums) <= (maxMans + maxLuuages);
            case 3:
                return (lNums + 1) <= (maxLuuages + (maxMans - (mNums + Math.round(seatNums * 1.5) + (cNums - seatNums))))
                        && ((mNums + Math.round((seatNums) * 1.5) + (cNums - seatNums)) + lNums + 1) <= (maxMans + maxLuuages);
            case 4:
                return ((mNums + Math.round((seatNums + 1) * 1.5) + (cNums - seatNums - 1)) <= maxMans)
                        && (seatNums + 1) <= cNums
                        && ((mNums + Math.round((seatNums + 1) * 1.5) + (cNums - seatNums - 1)) + lNums) <= (maxMans + maxLuuages);
            default:
                return ((mNums + Math.round((seatNums) * 1.5) + (cNums - seatNums)) <= maxMans)
                        && seatNums <= cNums;
        }
    }


    public void addChangeBg() {
        if (checkAddNums(2)) {
            cPlus.setBackgroundColor(Color.parseColor("#fad027"));
        } else {
            cPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
        }

        if (checkAddNums(1)) {
            mPlus.setBackgroundColor(Color.parseColor("#fad027"));
        } else {
            mPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
        }

        if (checkAddNums(3)) {
            lPlus.setBackgroundColor(Color.parseColor("#fad027"));
        } else {
            lPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
        }

        if (checkAddNums(4)) {
            if (seatNums + 1 <= cNums) {
                cSeatPlus.setBackgroundColor(Color.parseColor("#fad027"));
            } else {
                cSeatPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
            }
        } else {
            cSeatPlus.setBackgroundColor(Color.parseColor("#d5dadb"));
        }
    }


    public void subChangeBg() {
        addChangeBg();
        if (mNums == 1) {
            mSub.setBackgroundColor(Color.parseColor("#d5dadb"));
        } else {
            mSub.setBackgroundColor(Color.parseColor("#fad027"));
        }

        if (cNums == 0) {
            cSub.setBackgroundColor(Color.parseColor("#d5dadb"));
        } else {
            cSub.setBackgroundColor(Color.parseColor("#fad027"));
        }

        if (lNums == 0) {
            lSub.setBackgroundColor(Color.parseColor("#d5dadb"));
        } else {
            lSub.setBackgroundColor(Color.parseColor("#fad027"));
        }

        if (lNums == 0) {
            lSub.setBackgroundColor(Color.parseColor("#d5dadb"));
        } else {
            lSub.setBackgroundColor(Color.parseColor("#fad027"));
        }

        if (seatNums == 0) {
            cSeatSub.setBackgroundColor(Color.parseColor("#d5dadb"));
        } else {
            cSeatSub.setBackgroundColor(Color.parseColor("#fad027"));
        }
    }


    private void manLuggageShowChildSeatLayout() {
        try {
            if (seatNums == 1 && seat1.equalsIgnoreCase("-1")) {
                freeLayout.setVisibility(View.VISIBLE);
            } else if (seatNums == 1) {
                freeCSeatLeft.setText("收费儿童座椅");
                freeLayout.setVisibility(View.VISIBLE);
                freeCSeatRight.setText(Tools.getRMB(activity)+ seat1 + "/次");
            }
            if (seatNums > 1) {
                chargeLayout.setVisibility(View.VISIBLE);
                chargeSeatRight.setText(Tools.getRMB(activity) + seat2 + "/次");
                chargeSeatNum.setText("x" + (seatNums - 1) + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideChildSeatLayout() {
        try {
            if (seatNums > 1) {
                chargeLayout.setVisibility(View.VISIBLE);
                chargeSeatNum.setText("x" + (seatNums - 1) + "");
            }

            if (seatNums == 1) {
                chargeLayout.setVisibility(View.GONE);
            }

            if (seatNums == 0) {
                freeLayout.setVisibility(View.GONE);
                chargeLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.show_luggage_info, R.id.m_sub, R.id.m_num, R.id.m_plus, R.id.c_sub, R.id.c_num, R.id.c_plus, R.id.l_sub, R.id.l_num, R.id.l_plus, R.id.c_seat_sub, R.id.c_seat_num, R.id.c_seat_plus, R.id.free_c_seat_num, R.id.charge_seat_num})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_luggage_info:
                showInfo();
                break;
            case R.id.m_sub:
                if (mNums > 1) {
                    mNums--;
                    subChangeBg();
                    mNum.setText(mNums + "");
                    if (mNums == 1) {
                        mSub.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                }
                break;
            case R.id.m_plus:
                if (checkAddNums(1)) {
                    mNums++;
                    mNum.setText(mNums + "");
                    mSub.setBackgroundColor(Color.parseColor("#fad027"));
                    mPlus.setBackgroundColor(Color.parseColor("#fad027"));
                    addChangeBg();
                }
                break;
            case R.id.c_sub:
                if (cNums > 0) {
                    if (seatNums > 0 && seatNums >= cNums) {
                        seatNums--;
                        cSeatNum.setText(seatNums + "");
                        hideChildSeatLayout();
                    }
                    if (seatNums == 0) {
                        cSeatSub.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                    cNums--;
                    subChangeBg();
                    cNum.setText(cNums + "");
                    if (cNums == 0) {
                        showChildSeat.setVisibility(View.GONE);
                        cSub.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                }
                break;
            case R.id.c_plus:
                if (checkAddNums(2)) {
                    cNums++;
                    cSub.setBackgroundColor(Color.parseColor("#fad027"));
                    cNum.setText(cNums + "");
                    if (supportChildseat) {
                        showChildSeat.setVisibility(View.VISIBLE);
                    } else {
                        topTips.setVisibility(View.VISIBLE);
                    }
                    addChangeBg();
                }
                break;
            case R.id.l_sub:
                if (lNums > 0) {
                    lNums--;
                    lPlus.setBackgroundColor(Color.parseColor("#fad027"));
                    lNum.setText(lNums + "");
                    if (lNums == 0) {
                        lSub.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                    subChangeBg();
                }
                break;
            case R.id.l_plus:
                if (checkAddNums(3)) {
                    lNums++;
                    lSub.setBackgroundColor(Color.parseColor("#fad027"));
                    addChangeBg();
                    lNum.setText(lNums + "");
                }
                break;
            case R.id.c_seat_sub:
                if (seatNums > 0) {
                    seatNums--;
                    subChangeBg();
                    cSeatNum.setText(seatNums + "");
                    if (seatNums == 0) {
                        cSeatSub.setBackgroundColor(Color.parseColor("#d5dadb"));
                    }
                    hideChildSeatLayout();
                }
                break;
            case R.id.c_seat_plus:
                if (checkAddNums(4)) {
                    seatNums++;
                    cSeatSub.setBackgroundColor(Color.parseColor("#fad027"));
                    addChangeBg();
                    cSeatNum.setText(seatNums + "");
                    manLuggageShowChildSeatLayout();
                }
                break;
        }
    }


    private void showInfo() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View popView = inflater.inflate(R.layout.show_child_seat_info, null);
        final PopupWindow pw = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //设置后进行展示
        pw.setBackgroundDrawable(new ColorDrawable(0));
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.showAtLocation(popView, Gravity.CENTER, 0, 0);
        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
    }


}
