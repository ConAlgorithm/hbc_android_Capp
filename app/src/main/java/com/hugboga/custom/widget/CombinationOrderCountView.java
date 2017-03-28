package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.LuggageInfoActivity;
import com.hugboga.custom.data.bean.CarAdditionalServicePrice;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.GroupQuotesBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.CommonUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/3/9.
 * 注意和SkuOrderCountView 使用同一个R.layout.view_sku_order_count
 */
public class CombinationOrderCountView extends LinearLayout implements ChooseCountView.OnCountChangeListener {

    @Bind(R.id.sku_order_count_adult_choose_count_view)
    ChooseCountView adultCountView;
    @Bind(R.id.sku_order_count_child_choose_count_view)
    ChooseCountView childCountView;

    @Bind(R.id.sku_order_count_child_seat_layout)
    RelativeLayout childSeatLayout;
    @Bind(R.id.sku_order_count_child_seat_price_tv)
    TextView childSeatPriceTV;
    @Bind(R.id.sku_order_count_child_seat_choose_count_view)
    ChooseCountView childSeatCountView;

    @Bind(R.id.sku_order_count_luggage_count_tv)
    TextView luggageCountTV;

    @Bind(R.id.sku_order_count_hint_layout)
    LinearLayout hintLayout;
    @Bind(R.id.sku_order_count_hint_tv)
    TextView hintTV;

    @Bind(R.id.sku_order_count_room_layout)
    LinearLayout roomLayout;
    @Bind(R.id.sku_order_count_room_title_tv)
    TextView roomTitleTV;
    @Bind(R.id.sku_order_count_room_price_tv)
    TextView roomPriceTV;
    @Bind(R.id.sku_order_count_room_choose_count_view)
    ChooseCountView roomCountView;

    private ManLuggageBean manLuggageBean;
    private boolean supportChildseat = true;
    private int childSeatPrice1 = 0;
    private int childSeatPrice2 = 0;
    private CarBean carBean;
    private String serverDate;

    private int adultCount = 0;      // 成人数
    private int childCount = 0;      // 儿童数
    private int childSeatCount = 0;  // 儿童座椅数
    private int maxLuuages = 0;      // 可携带最大行李数

    private int additionalPrice = 0; // 总价格
    private int seatTotalPrice = 0;  // 儿童座椅总价

    private boolean isGroupOrder;
    private OnCountChangeListener listener;

    public CombinationOrderCountView(Context context) {
        this(context, null);
    }

    public CombinationOrderCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_sku_order_count, this);
        ButterKnife.bind(view);

        adultCountView.setOnCountChangeListener(this);
        childCountView.setOnCountChangeListener(this);
        childSeatCountView.setOnCountChangeListener(this);
        roomCountView.setOnCountChangeListener(this);

        //酒店
        roomLayout.setVisibility(View.GONE);
    }

    public void update(CarBean _carBean, CharterDataUtils charterDataUtils, String _serverDate, SkuItemBean skuItemBean) {
        if (_carBean == null) {
            return;
        }

        this.carBean = _carBean;
        this.isGroupOrder = charterDataUtils.isGroupOrder;

        int allChildSeatPrice1 = -1;
        int allChildSeatPrice2 = -1;
        ArrayList<GroupQuotesBean> quotes = carBean.quotes;
        int size = quotes.size();
        for (int i = 0; i < size; i++) {
            GroupQuotesBean groupQuotesBean = quotes.get(i);
            if (groupQuotesBean.additionalServicePrice != null) {
                CarAdditionalServicePrice additionalServicePrice = groupQuotesBean.additionalServicePrice;
                if (additionalServicePrice == null ||
                        TextUtils.isEmpty(additionalServicePrice.childSeatPrice1)
                        || TextUtils.isEmpty(additionalServicePrice.childSeatPrice2)) {
                    continue;
                }
                int childSeatPrice1 = CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice1);
                int childSeatPrice2 = CommonUtils.getCountInteger(additionalServicePrice.childSeatPrice2);
                if (childSeatPrice1 == -1 || childSeatPrice2 == -1) {
                    continue;
                }
                if (allChildSeatPrice1 == -1 && childSeatPrice1 >= 0) {
                    allChildSeatPrice1 = 0;
                }
                if (allChildSeatPrice2 == -1 && childSeatPrice2 >= 0) {
                    allChildSeatPrice2 = 0;
                }
                allChildSeatPrice1 += childSeatPrice1;
                allChildSeatPrice2 += childSeatPrice2;
            }
        }

        if (allChildSeatPrice1 == -1 || allChildSeatPrice2 == -1 || charterDataUtils.isGroupOrder) {
            supportChildseat = false;
        } else {
            this.childSeatPrice1 = allChildSeatPrice1;
            this.childSeatPrice2 = allChildSeatPrice2;
        }

        if (!TextUtils.equals(serverDate, _serverDate) || isResetCountView()) {
            boolean isInit = false;
            if (serverDate == null) {
                isInit = true;
            }
            this.serverDate = _serverDate;

            if (isInit) {
                adultCount = charterDataUtils.adultCount;
                childCount = charterDataUtils.childCount;
                if (charterDataUtils.isGroupOrder) {
                    childSeatLayout.setVisibility(View.GONE);
                } else {
                    childSeatLayout.setVisibility(supportChildseat && childCount > 0 ? View.VISIBLE : View.GONE);
                }
            } else {
                adultCount = carBean.capOfPerson >= 2 ? 2 : 1;
                childCount = 0;
                childSeatLayout.setVisibility(View.GONE);
            }
            adultCountView.setMinCount(1).setCount(adultCount, false);
            childCountView.setCount(childCount, false);
            childSeatCount = 0;
            childSeatCountView.setCount(0, false);
            hintLayout.setVisibility(View.GONE);
            if (listener != null) {
                listener.onCountChange(getManLuggageBean());
            }
        }
        checkCountView();

        //可携带行李数
        setMaxLuggage();
    }

    @Override
    public void onCountChange(View view, int count) {
        switch (view.getId()) {
            case R.id.sku_order_count_adult_choose_count_view://成人数
                this.adultCount = count;
                checkCountView();
                setMaxLuggage();
                break;
            case R.id.sku_order_count_child_choose_count_view://儿童数
                this.childCount = count;
                checkCountView();

                if (childSeatCount > childCount) {
                    childSeatCount--;
                    childSeatCountView.setCount(childSeatCount);
                }

                if (childCount > 0 && !isGroupOrder) {
                    hintLayout.setVisibility(View.VISIBLE);
                    if (supportChildseat) {
                        childSeatLayout.setVisibility(View.VISIBLE);
                        hintTV.setText("出行前请与司导联系，需要何种年龄段的儿童安全座椅");
                    } else {
                        childSeatLayout.setVisibility(View.GONE);
                        hintTV.setText("很抱歉，该城市暂不提供儿童座椅");
                    }
                } else {
                    childSeatLayout.setVisibility(View.GONE);
                    hintLayout.setVisibility(View.GONE);
                }
                setMaxLuggage();
                break;
            case R.id.sku_order_count_child_seat_choose_count_view://儿童座椅
                this.childSeatCount = count;
                checkCountView();
                getSeatTotalPrice();
                checkPrice();
                setMaxLuggage();
                setPriceText(childSeatPriceTV, seatTotalPrice, childSeatCount);
                break;
        }
        if (listener != null) {
            listener.onCountChange(getManLuggageBean());
        }
    }

    private void setPriceText(TextView textView, int price, int count) {
        String priceStr = "";
        if (count > 0) {
            priceStr = String.format("（%1$s）", price <= 0 ? "免费" : getContext().getString(R.string.sign_rmb) + price);
        }
        textView.setText(priceStr);
    }

    @OnClick({R.id.sku_order_count_luggage_explain_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sku_order_count_luggage_explain_tv://行李标准说明
                getContext().startActivity(new Intent(getContext(), LuggageInfoActivity.class));
                break;
        }
    }

    /**
     * type 1:成人、2:儿童、3:儿童座椅(1.5)
     * 乘车人数 =（成人数 + 儿童座椅数 * 1.5 + 不用座椅儿童数）
     * */
    private boolean checkCount(int type) {
        switch (type) {
            case 1:
            case 2:
                return (adultCount + Math.round(childSeatCount * 1.5) + (childCount - childSeatCount) < carBean.capOfPerson);
            case 3:
                double count = adultCount + childSeatCount * 1.5 + (childCount - childSeatCount);
                return carBean.capOfPerson - count >= 0.5 && childSeatCount < childCount;
            default:
                return false;
        }
    }

    private boolean isResetCountView() {
        return  adultCount + childSeatCount * 1.5 + (childCount - childSeatCount) > carBean.capOfPerson;
    }

    /**
     *  可携带行李数
     *  行李数 =（最大乘车人数 + 最大行李数）- （成人数 + 儿童座椅数 * 1.5 + 不用座椅儿童数）
     */
    private void setMaxLuggage() {
        maxLuuages = (carBean.capOfLuggage + carBean.capOfPerson) - (adultCount + (int)Math.round(childSeatCount * 1.5) + (childCount - childSeatCount));
        luggageCountTV.setText("" + maxLuuages);
    }

    private void checkCountView() {
        adultCountView.setIsCanAdd(checkCount(1));
        childCountView.setIsCanAdd(checkCount(2));
        childSeatCountView.setIsCanAdd(checkCount(3));
    }

    private void checkPrice() {
        if (additionalPrice == seatTotalPrice) {
            return;
        }
        additionalPrice = seatTotalPrice;
        if (listener != null) {
            listener.onAdditionalPriceChange(additionalPrice);
        }
    }

    public int getSeatTotalPrice() {
        seatTotalPrice = 0;
        if (childSeatCount >= 1 && childSeatPrice1 > 0) {
            seatTotalPrice = childSeatPrice1;
        }
        if (childSeatCount > 1 && childSeatPrice2 > 0) {
            seatTotalPrice += childSeatPrice2 * (childSeatCount - 1);
        }
        return seatTotalPrice;
    }

    public int getAdditionalPrice() {
        return additionalPrice = getSeatTotalPrice();
    }

    public int getTotalPeople() {
        return adultCount + childCount;
    }

    public interface OnCountChangeListener {
        public void onCountChange(ManLuggageBean bean);
        public void onAdditionalPriceChange(int price);
    }

    public void setOnCountChangeListener(OnCountChangeListener listener) {
        this.listener = listener;
    }

    public ManLuggageBean getManLuggageBean() {
        if (manLuggageBean == null) {
            manLuggageBean = new ManLuggageBean();
        }
        manLuggageBean.mans = adultCount;
        manLuggageBean.childs = childCount;
        manLuggageBean.luggages = maxLuuages;
        manLuggageBean.childSeats = childSeatCount;
        return manLuggageBean;
    }
}