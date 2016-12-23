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
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/16.
 */
public class SkuOrderCountView extends LinearLayout implements ChooseCountView.OnCountChangeListener {

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
    private String childSeatPrice1 = "";
    private String childSeatPrice2 = "";
    private CarBean carBean;
    private CarListBean carListBean;
    private String serverDate;

    private int adultCount = 0;      // 成人数
    private int childCount = 0;      // 儿童数
    private int childSeatCount = 0;  // 儿童座椅数
    private int roomCount = 0;       // 房间数

    private int additionalPrice = 0; // 总价格（儿童座椅 + 酒店）
    private int seatTotalPrice = 0;  // 儿童座椅总价
    private int hotelTotalPrice = 0; // 酒店总价

    private OnCountChangeListener listener;

    public SkuOrderCountView(Context context) {
        this(context, null);
    }

    public SkuOrderCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_sku_order_count, this);
        ButterKnife.bind(view);

        roomCount = 1;
        roomCountView.setMinCount(1).setCount(roomCount, false);
        adultCountView.setOnCountChangeListener(this);
        childCountView.setOnCountChangeListener(this);
        childSeatCountView.setOnCountChangeListener(this);
        roomCountView.setOnCountChangeListener(this);
    }

    public void update(CarBean _carBean, CarListBean _carListBean, String _serverDate) {
        if (_carBean == null || _carListBean == null) {
            return;
        }

        this.carBean = _carBean;
        this.carListBean = _carListBean;
        CarAdditionalServicePrice additionalServicePrice = carListBean.additionalServicePrice;

        if (additionalServicePrice == null || (additionalServicePrice.childSeatPrice1 == null && additionalServicePrice.childSeatPrice2 == null)) {
            supportChildseat = false;
        } else {
            childSeatPrice1 = additionalServicePrice.childSeatPrice1;
            childSeatPrice2 = additionalServicePrice.childSeatPrice2;
        }

        if (!TextUtils.equals(serverDate, _serverDate) || isResetCountView()) {
            this.serverDate = _serverDate;

            adultCount = carBean.capOfPerson >= 2 ? 2 : 1;
            childCount = 0;
            childSeatCount = 0;

            adultCountView.setMinCount(1).setCount(adultCount, false);
            childCountView.setCount(0, false);
            childSeatCountView.setCount(0, false);
            childSeatLayout.setVisibility(View.GONE);
            hintLayout.setVisibility(View.GONE);
            if (listener != null) {
                listener.onCountChange(getManLuggageBean());
            }
        }
        checkCountView();

        //可携带行李数
        luggageCountTV.setText("" + carBean.capOfLuggage);

        //房间
        if (carListBean.showHotel) {
            roomLayout.setVisibility(View.VISIBLE);
            roomTitleTV.setText(String.format("%1$s晚", "" + carListBean.hotelNum));
            setPriceText(roomPriceTV, carListBean.hotelPrice, roomCount);
        } else {
            roomLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCountChange(View view, int count) {
        switch (view.getId()) {
            case R.id.sku_order_count_adult_choose_count_view://成人数
                this.adultCount = count;
                checkCountView();
                break;
            case R.id.sku_order_count_child_choose_count_view://儿童数
                this.childCount = count;
                checkCountView();

                if (childCount > 0) {
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

                break;
            case R.id.sku_order_count_child_seat_choose_count_view://儿童座椅
                this.childSeatCount = count;
                checkCountView();
                getSeatTotalPrice();
                checkPrice();
                setPriceText(childSeatPriceTV, seatTotalPrice, childSeatCount);
                break;
            case R.id.sku_order_count_room_choose_count_view://房间数
                this.roomCount = count;
                getHotelTotalPrice();
                checkPrice();
                setPriceText(roomPriceTV, hotelTotalPrice, roomCount);
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

    private boolean checkCount(int type) {//type 1:成人、2:儿童、3:儿童座椅(1.5)
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

    private void checkCountView() {
        adultCountView.setIsCanAdd(checkCount(1));
        childCountView.setIsCanAdd(checkCount(2));
        childSeatCountView.setIsCanAdd(checkCount(3));
    }

    private void checkPrice() {
        if (additionalPrice == seatTotalPrice + hotelTotalPrice) {
            return;
        }
        additionalPrice = seatTotalPrice + hotelTotalPrice;
        if (listener != null) {
            listener.onAdditionalPriceChange(additionalPrice);
        }
    }

    public int getSeatTotalPrice() {
        seatTotalPrice = 0;
        if (childSeatCount >= 1 && CommonUtils.getCountInteger(childSeatPrice1) > 0) {
            seatTotalPrice = CommonUtils.getCountInteger(childSeatPrice1);
        }
        if (childSeatCount > 1 && CommonUtils.getCountInteger(childSeatPrice2) > 0) {
            seatTotalPrice += (CommonUtils.getCountInteger(childSeatPrice2) * (childSeatCount - 1));
        }
        return seatTotalPrice;
    }

    public int getHotelTotalPrice() {
        return hotelTotalPrice = carListBean.hotelPrice * roomCount;
    }

    public int getAdditionalPrice() {
        return additionalPrice = getSeatTotalPrice() + getHotelTotalPrice();
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
        manLuggageBean.luggages = carBean.capOfLuggage;
        manLuggageBean.childSeats = childSeatCount;
        manLuggageBean.roomCount = roomCount;
        return manLuggageBean;
    }
}