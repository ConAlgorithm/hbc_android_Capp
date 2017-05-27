package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.DeductionBean;
import com.hugboga.custom.data.bean.MostFitBean;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/17.
 */
public class SkuOrderDiscountView extends LinearLayout{

    public static final int TYPE_INVALID = -1;
    public static final int TYPE_COUPON = 1;
    public static final int TYPE_TRAVEL_FUND = 2;

    @Bind(R.id.sku_order_discount_coupon_choose_iv)
    ImageView couponChooseIV;
    @Bind(R.id.sku_order_discount_coupon_count_tv)
    TextView couponCountTV;

    @Bind(R.id.sku_order_discount_travel_fund_choose_iv)
    ImageView travelFundChooseIV;
    @Bind(R.id.sku_order_discount_travel_fund_count_tv)
    TextView travelFundCountTV;

    @Bind(R.id.sku_order_discount_insurance_choose_iv)
    ImageView insuranceChooseIV;
    @Bind(R.id.sku_order_discount_insurance_count_tv)
    TextView insuranceCountTV;

    private DiscountOnClickListener listener;
    private int currentType = TYPE_COUPON;
    private int oldType;

    private boolean isInvalidCoupon = false;
    private boolean isInvalidTravelFund = false;

    public SkuOrderDiscountView(Context context) {
        this(context, null);
    }

    public SkuOrderDiscountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_sku_order_discount, this);
        ButterKnife.bind(view);
        insuranceChooseIV.setSelected(true);
    }

    @OnClick({R.id.sku_order_discount_coupon_count_tv, R.id.sku_order_discount_travel_fund_count_tv, R.id.sku_order_discount_coupon_layout, R.id.sku_order_discount_travel_fund_layout, R.id.sku_order_discount_insurance_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sku_order_discount_coupon_count_tv:
                if (listener != null && !isInvalidCoupon) {
                    listener.intentCouponList();
                }
                break;
            case R.id.sku_order_discount_travel_fund_count_tv:
                if (listener != null && !isInvalidTravelFund) {
                    listener.intentTravelFund();
                }
                break;
            case R.id.sku_order_discount_coupon_layout:
                if (isInvalidCoupon || currentType == TYPE_COUPON) {
                    break;
                }
                currentType = TYPE_COUPON;
                resetCheckedView();
                break;
            case R.id.sku_order_discount_travel_fund_layout:
                if (isInvalidTravelFund || currentType == TYPE_TRAVEL_FUND) {
                    break;
                }
                currentType = TYPE_TRAVEL_FUND;
                resetCheckedView();
                break;
            case R.id.sku_order_discount_insurance_layout:
                insuranceChooseIV.setSelected(!insuranceChooseIV.isSelected());
                break;
        }
    }

    public interface DiscountOnClickListener {
        public void chooseDiscount(int type);
        public void intentCouponList();
        public void intentTravelFund();
    }

    public void setDiscountOnClickListener(DiscountOnClickListener listener) {
        this.listener = listener;
    }

    public void setCouponBean(CouponBean couponBean) {
        if (couponBean == null) {
            couponCountTV.setText("当前没有用劵，请选择");
        } else {
            couponCountTV.setText(couponBean.price);
        }
        currentType = TYPE_COUPON;
        resetCheckedView();
    }

    public void setMostFitBean(MostFitBean mostFitBean) {
        if (TextUtils.isEmpty(mostFitBean.priceInfo)) {
            couponCountTV.setText("无优惠券可用");
            isInvalidCoupon = true;
        } else {
            couponCountTV.setText(mostFitBean.priceInfo);
            isInvalidCoupon = false;
        }
        resetCheckedView();
    }

    public void setDeductionBean(DeductionBean deductionBean) {
        int price = CommonUtils.getCountInteger(deductionBean.deduction);

        if (price <= 0) {
            travelFundCountTV.setText(getContext().getString(R.string.sign_rmb) + price);
            isInvalidTravelFund = true;
        } else {
            travelFundCountTV.setText("- " + getContext().getString(R.string.sign_rmb) + price);
            isInvalidTravelFund = false;
        }
        resetCheckedView();
    }

    public synchronized void resetCheckedView() {
        if (isInvalidCoupon && isInvalidTravelFund) {
            currentType = TYPE_INVALID;
        } else if (currentType != TYPE_TRAVEL_FUND && isInvalidCoupon && !isInvalidTravelFund) {
            currentType = TYPE_TRAVEL_FUND;
        } else if (currentType != TYPE_COUPON && isInvalidTravelFund && !isInvalidCoupon) {
            currentType = TYPE_COUPON;
        } else if (!isInvalidCoupon && !isInvalidTravelFund && oldType == TYPE_TRAVEL_FUND) {
            currentType = TYPE_COUPON;
        }
        boolean isTravelFund = currentType == TYPE_TRAVEL_FUND && !isInvalidTravelFund;
        boolean isCoupon = currentType == TYPE_COUPON && !isInvalidCoupon;
        int priceColor = getContext().getResources().getColor(R.color.default_price_red);
        travelFundChooseIV.setSelected(isTravelFund);
        travelFundCountTV.setTextColor(isTravelFund ? priceColor : 0xFFA8A8A8);
        couponChooseIV.setSelected(isCoupon);
        couponCountTV.setTextColor(isCoupon ? priceColor : 0xFFA8A8A8);

        if (currentType == TYPE_INVALID || isTravelFund || isCoupon) {
            oldType = currentType;
            if (listener != null) {
                listener.chooseDiscount(currentType);
            }
        }
    }

    public void setInsuranceCount(int count) {
        insuranceCountTV.setText(String.format(" × %1$s份", "" + count));
    }

    public boolean isCheckedTravelFund() {
        return currentType == TYPE_TRAVEL_FUND;
    }

    public boolean isCheckedCoupon() {
        return currentType == TYPE_COUPON;
    }
}
