package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/16.
 * 注意和CombinationOrderCountView 使用同一个R.layout.view_sku_order_count
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
    @Bind(R.id.sku_order_count_luggage_explain_tv)
    TextView luggageExplainTV;

    @Bind(R.id.sku_order_count_hint_layout)
    LinearLayout hintLayout;
    @Bind(R.id.sku_order_count_hint_tv)
    TextView hintTV;

    @Bind(R.id.sku_order_count_room_layout)
    LinearLayout roomLayout;
    @Bind(R.id.sku_order_count_room_price_tv)
    TextView roomPriceTV;
    @Bind(R.id.sku_order_count_room_choose_count_view)
    ChooseCountView roomCountView;
    @Bind(R.id.sku_order_count_room_description_tv)
    TextView roomDescriptionTV;

    @Bind(R.id.sku_order_count_extrasprice_layout)
    LinearLayout extrasPriceLayout;
    @Bind(R.id.sku_order_count_extrasprice_title_tv)
    TextView extrasPriceTitleTV;
    @Bind(R.id.sku_order_count_extrasprice_price_tv)
    TextView extrasPricePriceTV;
    @Bind(R.id.sku_order_count_extrasprice_description_tv)
    TextView extrasPriceDescriptionTV;

    private ManLuggageBean manLuggageBean;
    private boolean supportChildseat = true;
    private String childSeatPrice1 = "";
    private String childSeatPrice2 = "";
    private CarBean carBean;
    private CarListBean carListBean;
    private String serverDate;
    private boolean isHasExtrasPrice = false;

    private int adultCount = 0;      // 成人数
    private int childCount = 0;      // 儿童数
    private int childSeatCount = 0;  // 儿童座椅数
    private int roomCount = 0;       // 房间数
    private int maxLuuages = 0;      // 可携带最大行李数

    private double additionalPrice = 0; // 总价格（儿童座椅 + 酒店）
    private double seatTotalPrice = 0;  // 儿童座椅总价
    private double hotelTotalPrice = 0; // 酒店总价

    private OnCountChangeListener listener;

    public SkuOrderCountView(Context context) {
        this(context, null);
    }

    public SkuOrderCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_sku_order_count, this);
        ButterKnife.bind(view);

        luggageExplainTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        luggageExplainTV.getPaint().setAntiAlias(true);

        adultCountView.setOnCountChangeListener(this);
        childCountView.setOnCountChangeListener(this);
        childSeatCountView.setOnCountChangeListener(this);
        roomCountView.setOnCountChangeListener(this);

        roomCount = 1;
        roomCountView.setMinCount(1).setCount(roomCount, false);
    }

    //线路
    public void update(CarBean _carBean, CarListBean _carListBean, String _serverDate, SkuItemBean skuItemBean) {
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
        setMaxLuggage();

        //当前只有线路有酒店和其它费用，skuItemBean != null为线路
        boolean isSkuOrder = skuItemBean != null;

        if (isSkuOrder) {
            //酒店
            if (skuItemBean.hotelStatus == 1) {
                roomLayout.setVisibility(View.VISIBLE);
                setPriceText(roomPriceTV, getHotelTotalPrice(), roomCount);
                roomDescriptionTV.setText(String.format("单价¥%1$s/间 | 含%2$s晚", CommonUtils.doubleTrans(carListBean.hotelPrice), "" + skuItemBean.hotelCostAmount));
            } else {
                roomLayout.setVisibility(View.GONE);
            }

            //其他费用
            if (carListBean.goodsOtherPrice != null && carListBean.goodsOtherPrice >= 0 && !TextUtils.isEmpty(carListBean.goodsOtherPriceComment)) {
                extrasPriceLayout.setVisibility(View.VISIBLE);
                updateExtrasPriceText();
                extrasPriceDescriptionTV.setText(String.format("单价¥%1$s/人 | %2$s", CommonUtils.doubleTrans(carListBean.goodsOtherPrice), carListBean.goodsOtherPriceComment));
                isHasExtrasPrice = true;
            } else {
                isHasExtrasPrice = false;
                extrasPriceLayout.setVisibility(View.GONE);
            }
        }
    }

    //接送次
    public void update(CarBean _carBean, CarListBean _carListBean, String _serverDate) {
        update(_carBean, _carListBean, _serverDate, null);
    }

    @Override
    public void onCountChange(View view, int count) {
        switch (view.getId()) {
            case R.id.sku_order_count_adult_choose_count_view://成人数
                this.adultCount = count;
                checkCountView();
                setMaxLuggage();
                if (isHasExtrasPrice) {
                    updateExtrasPriceText();
                    checkPrice();
                }
                break;
            case R.id.sku_order_count_child_choose_count_view://儿童数
                this.childCount = count;
                checkCountView();

                if (childSeatCount > childCount) {
                    childSeatCount--;
                    childSeatCountView.setCount(childSeatCount);
                }

                if (childCount > 0) {
                    hintLayout.setVisibility(View.VISIBLE);
                    if (supportChildseat) {
                        childSeatLayout.setVisibility(View.VISIBLE);
                        hintTV.setText("请在出行前告诉司导需要何种年龄段的儿童座椅");
                    } else {
                        childSeatLayout.setVisibility(View.GONE);
                        hintTV.setText("很抱歉，该城市暂不提供儿童座椅");
                    }
                } else {
                    childSeatLayout.setVisibility(View.GONE);
                    hintLayout.setVisibility(View.GONE);
                }
                setMaxLuggage();
                if (isHasExtrasPrice) {
                    updateExtrasPriceText();
                    checkPrice();
                }
                break;
            case R.id.sku_order_count_child_seat_choose_count_view://儿童座椅
                this.childSeatCount = count;
                checkCountView();
                getSeatTotalPrice();
                checkPrice();
                setMaxLuggage();
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

    private void setPriceText(TextView textView, double totalPrice, int count) {
        String priceStr = "";
        if (count > 0) {
            priceStr = String.format("（%1$s）", totalPrice <= 0 ? "免费" : getContext().getString(R.string.sign_rmb) + CommonUtils.doubleTrans(totalPrice));
        }
        textView.setText(priceStr);
    }

    private void updateExtrasPriceText() {
        double totalPrice = getTotalExtrasPrice();
        int peopleCount = getTotalPeople();
        setPriceText(extrasPricePriceTV, totalPrice, peopleCount);
        extrasPriceTitleTV.setText(String.format("出行人数×%1$s人", peopleCount));
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
        double allPrice = seatTotalPrice + hotelTotalPrice + getTotalExtrasPrice();
        if (additionalPrice == allPrice) {
            return;
        }
        additionalPrice = allPrice;
        if (listener != null) {
            listener.onAdditionalPriceChange(additionalPrice);
        }
    }

    public double getSeatTotalPrice() {
        seatTotalPrice = 0;
        if (childSeatCount >= 1 && CommonUtils.getCountInteger(childSeatPrice1) > 0) {
            seatTotalPrice = CommonUtils.getCountInteger(childSeatPrice1);
        }
        if (childSeatCount > 1 && CommonUtils.getCountInteger(childSeatPrice2) > 0) {
            seatTotalPrice += (CommonUtils.getCountInteger(childSeatPrice2) * (childSeatCount - 1));
        }
        return seatTotalPrice;
    }

    public double getHotelTotalPrice() {
        return hotelTotalPrice = carListBean == null || carListBean.hotelPrice <= 0 ? 0 : carListBean.hotelPrice * roomCount;
    }

    public double getTotalExtrasPrice() {
        return carListBean == null || carListBean.goodsOtherPrice <= 0 ? 0 : carListBean.goodsOtherPrice * getTotalPeople();
    }

    public double getExtrasPrice() {
        return carListBean == null || carListBean.goodsOtherPrice <= 0 ? 0 : carListBean.goodsOtherPrice;
    }

    public double getAdditionalPrice() {
        return additionalPrice = getSeatTotalPrice() + getHotelTotalPrice() + getTotalExtrasPrice();
    }

    public int getTotalPeople() {
        return adultCount + childCount;
    }

    public interface OnCountChangeListener {
        public void onCountChange(ManLuggageBean bean);
        public void onAdditionalPriceChange(double price);
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
        manLuggageBean.roomCount = roomCount;
        return manLuggageBean;
    }
}
