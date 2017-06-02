package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.LuggageInfoActivity;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/16.
 */

public class SkuOrderCarTypeView extends LinearLayout implements HbcViewBehavior{

    private static final int DEFAULT_SHOW_COUNT = 3;//默认展示条数

    @Bind(R.id.sku_order_car_type_container_layout)
    LinearLayout containerLayot;
    @Bind(R.id.sku_order_car_type_more_layout)
    LinearLayout moreLayout;
    @Bind(R.id.sku_order_car_type_more_arrow_iv)
    ImageView moreaArrowIV;

    @Bind(R.id.sku_order_car_type_luggage_explain_tv)
    TextView explainTV;
    @Bind(R.id.sku_order_car_type_luggage_explain_iv)
    ImageView explainIV;

    private boolean isShow = false;
    private ArrayList<CarBean> carList;
    private OnSelectedCarListener listener;
    private CarBean oldCarBean;

    private boolean isSelectedGuide = false;
    private int orderType;

    public SkuOrderCarTypeView(Context context) {
        this(context, null);
    }

    public SkuOrderCarTypeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_sku_order_car_type, this);
        ButterKnife.bind(view);
    }

    public void setIsSelectedGuide(boolean isSelectedGuide) {
        this.isSelectedGuide = isSelectedGuide;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    @Override
    public void update(Object _data) {
        if (!(_data instanceof CarListBean)){
            return;
        }
        CarListBean carListBean = (CarListBean) _data;
        carList = carListBean.carList;
        final int childCount = containerLayot.getChildCount();
        final int size = carList.size();
        for (int i = 0; i < size; i++) {
            CarBean carBean = carList.get(i);
            if (carBean == null) {
                continue;
            }
            View itemView = null;
            if (i < childCount) {
                itemView = containerLayot.getChildAt(i);
            } else {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.view_sku_order_car_type_item, null);
                containerLayot.addView(itemView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            }
            itemView.setId(i);
            itemView.setVisibility(i < DEFAULT_SHOW_COUNT ? View.VISIBLE : View.GONE);
            itemView.findViewById(R.id.sku_order_car_type_item_choose_iv).setSelected(i == 0);
            TextView priceTV = (TextView) itemView.findViewById(R.id.sku_order_car_type_price_tv);
            TextView originalPriceTV = (TextView) itemView.findViewById(R.id.sku_order_car_type_original_price_tv);
            if (carListBean.isSeckills) {
                priceTV.setText(getContext().getString(R.string.sign_rmb) + carBean.seckillingPrice);
                originalPriceTV.setText(getContext().getString(R.string.sign_rmb) + carBean.price);
                originalPriceTV.getPaint().setAntiAlias(true);
                originalPriceTV.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            } else {
                priceTV.setText(getContext().getString(R.string.sign_rmb) + carBean.price);
                originalPriceTV.setText("");
            }
            String title = carBean.carDesc;
            List<String> serviceTags = carBean.serviceTags;
            if (serviceTags != null && serviceTags.size() >= 1 && serviceTags.get(0) != null) {
                title += " + " + serviceTags.get(0);
            }
            ((TextView) itemView.findViewById(R.id.sku_order_car_type_title_tv)).setText(title);
            setSeatTV(((TextView) itemView.findViewById(R.id.sku_order_car_type_seat_tv)), carBean.capOfPerson, carBean.capOfLuggage);
            String carDes = carBean.models;
            if (isSelectedGuide && !TextUtils.isEmpty(carBean.carLicenceNoCovered)) {//carInfo2 + 车牌
                carDes += "  车牌:" + carBean.carLicenceNoCovered;
            }
            showDescriptionTV(itemView, i == 0 ? carDes : "", size, i);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemViewOnClick(v);
                }
            });
        }
        itemViewOnClick(containerLayot.getChildAt(0));
        isShow = false;
        moreaArrowIV.setBackgroundResource(R.mipmap.icon_black_arrow);
        if (size <= DEFAULT_SHOW_COUNT) {
            moreLayout.setVisibility(View.GONE);
        } else {
            moreLayout.setVisibility(View.VISIBLE);
        }
        for (int i = size; i < childCount; i++) {
            containerLayot.getChildAt(i).setVisibility(View.GONE);
        }
        if (orderType == 1 || orderType == 2 || orderType == 4) {
            moreLayout.callOnClick();
        }
    }

    @OnClick({R.id.sku_order_car_type_more_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sku_order_car_type_more_layout:
                if (carList == null && isShow) {
                    return;
                }
                final int size = carList.size();
                for (int i = 0; i < size; i++) {
                    CarBean carBean = carList.get(i);
                    if (carBean == null) {
                        continue;
                    }
                    View itemView = containerLayot.getChildAt(i);
                    if (isShow) {
                        itemView.setVisibility(i < DEFAULT_SHOW_COUNT ? View.VISIBLE : View.GONE);
                        moreaArrowIV.setBackgroundResource(R.mipmap.icon_black_arrow);
                    } else {
                        itemView.setVisibility(View.VISIBLE);
                        moreaArrowIV.setBackgroundResource(R.mipmap.icon_black_arrow_up);
                    }
                }
                isShow = !isShow;
                if (listener != null) {
                    listener.onClickHideMoreCar(isShow);
                }
                moreLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void itemViewOnClick(View view) {
        if (carList == null || view == null) {
            return;
        }
        final int size = carList.size();
        if (view.getId() >= size) {
            return;
        }
        for (int i = 0; i < size; i++) {
            View itemView = containerLayot.getChildAt(i);
            itemView.findViewById(R.id.sku_order_car_type_item_choose_iv).setSelected(i == view.getId());
            String carDes = carList.get(i).models;
            if (isSelectedGuide && !TextUtils.isEmpty(carList.get(i).carLicenceNoCovered)) {
                carDes += "  车牌:" + carList.get(i).carLicenceNoCovered;
            }
            showDescriptionTV(itemView, carDes, size, i);//i == view.getId() ? carDes : ""
        }
        CarBean carBean = carList.get(view.getId());
        if (listener != null && this.oldCarBean != carBean && carBean != null) {
            this.oldCarBean = carBean;
            listener.onSelectedCar(carBean);
        }
    }

    private void showDescriptionTV(View itemView, String description, int size, int i) {
        TextView descriptionTV = (TextView) itemView.findViewById(R.id.sku_order_car_type_description_tv);
        View lineView = itemView.findViewById(R.id.sku_order_car_type_bottom_line_view);
        View lineView2 = itemView.findViewById(R.id.sku_order_car_type_bottom_line_view2);
        boolean isEmpty = TextUtils.isEmpty(description);

        if (i == size - 1) {
            lineView.setVisibility(isEmpty ? View.GONE : View.INVISIBLE);
            lineView2.setVisibility(isEmpty ? View.INVISIBLE : View.GONE);
        } else {
            lineView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            lineView2.setVisibility(isEmpty ? View.VISIBLE  : View.GONE);
        }
        descriptionTV.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        descriptionTV.setText(description);
    }

    private void setSeatTV(TextView textView, int person, int luggage) {
        textView.setText("[ ");
        textView.append(Html.fromHtml("<img src='" + R.mipmap.icon_order_people + "'/>", imgGetter, null));
        textView.append(person + "人  ");
        textView.append(Html.fromHtml("<img src='" + R.mipmap.icon_order_bag + "'/>", imgGetter, null));
        textView.append(luggage + "行李 ]");
    }

    private Html.ImageGetter imgGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            int id = Integer.parseInt(source);
            Drawable d = getResources().getDrawable(id);
            d.setBounds(-UIUtils.dip2px(3), -UIUtils.dip2px(3), UIUtils.dip2px(12), UIUtils.dip2px(12));
            return d;
        }
    };

    public interface OnSelectedCarListener {
        public void onSelectedCar(CarBean carBean);
        public void onClickHideMoreCar(boolean isShow);
    }

    public void setOnSelectedCarListener(OnSelectedCarListener listener) {
        this.listener = listener;
    }

    //包车游需要显示
    public void showLuggageExplain() {
        explainTV.setVisibility(View.VISIBLE);
        explainIV.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.sku_order_car_type_luggage_explain_tv, R.id.sku_order_car_type_luggage_explain_iv})
    public void intentLuggageInfoActivity() {
        getContext().startActivity(new Intent(getContext(), LuggageInfoActivity.class));
    }
}
