package com.hugboga.custom.widget.charter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.PoiSearchActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.DirectionBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/2/24.
 */
public class CharterPickupView extends LinearLayout {

    private Context context;

    @Bind(R.id.charter_pickup_item_root_layout)
    RelativeLayout rootLayout;
    @Bind(R.id.charter_pickup_item_selected_iv)
    ImageView selectedIV;
    @Bind(R.id.charter_pickup_item_bottom_space_view)
    View bottomSpaceView;

    @Bind(R.id.charter_pickup_item_title_tv)
    TextView titleTV;

    @Bind(R.id.charter_pickup_item_add_address_layout)
    LinearLayout addAddressLayout;
    @Bind(R.id.charter_pickup_item_add_address_tv)
    TextView addAddressTV;

    @Bind(R.id.charter_pickup_item_address_layout)
    RelativeLayout addressLayout;
    @Bind(R.id.charter_pickup_item_address_tv)
    TextView addressTV;
    @Bind(R.id.charter_pickup_item_address_des_tv)
    TextView addressDesTV;
    @Bind(R.id.charter_pickup_item_distance_tv)
    TextView distanceTV;

    private CharterDataUtils charterDataUtils;

    public CharterPickupView(Context context) {
        this(context, null);
    }

    public CharterPickupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setPadding(UIUtils.dip2px(8), UIUtils.dip2px(5), UIUtils.dip2px(8), UIUtils.dip2px(5));
        View view = inflate(context, R.layout.view_charter_pickup_item, this);
        ButterKnife.bind(view);
        charterDataUtils = CharterDataUtils.getInstance();

        titleTV.setText("只接机，不包车游玩");
        addAddressTV.setText("添加送达地点");

        LinearLayout.LayoutParams addAddressLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addAddressLayoutParams.topMargin = UIUtils.dip2px(-4);
        addAddressLayout.setLayoutParams(addAddressLayoutParams);

        LinearLayout.LayoutParams addressLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.dip2px(25));
        addressLayoutParams.topMargin = UIUtils.dip2px(-12);
        addressLayout.setLayoutParams(addressLayoutParams);
    }

    public void update() {
        if (isSelected()) {
            rootLayout.setBackgroundResource(R.drawable.shape_rounded_charter_selected);
            selectedIV.setVisibility(View.VISIBLE);
            bottomSpaceView.setVisibility(View.VISIBLE);

            PoiBean pickUpPoiBean = charterDataUtils.pickUpPoiBean;
            if (pickUpPoiBean != null) {
                addAddressLayout.setVisibility(View.GONE);
                addressLayout.setVisibility(View.VISIBLE);
                addressDesTV.setVisibility(View.VISIBLE);

                addressTV.setText(pickUpPoiBean.placeName);
                addressDesTV.setText(pickUpPoiBean.placeDetail);

                DirectionBean pickUpDirectionBean = charterDataUtils.pickUpDirectionBean;
                if (charterDataUtils.pickUpDirectionBean == null || pickUpDirectionBean.distanceDesc == null || pickUpDirectionBean.durationDesc == null) {
                    distanceTV.setVisibility(View.GONE);
                }else {
                    distanceTV.setVisibility(View.VISIBLE);
                    distanceTV.setText(String.format("距离：%1$s  时长：约%2$s", pickUpDirectionBean.distanceDesc, pickUpDirectionBean.durationDesc));
                }
            } else {
                addAddressLayout.setVisibility(View.VISIBLE);
                addressLayout.setVisibility(View.GONE);
                addressDesTV.setVisibility(View.GONE);
                distanceTV.setVisibility(View.GONE);
            }
        } else {
            rootLayout.setBackgroundResource(R.drawable.shape_rounded_charter_unselected);
            selectedIV.setVisibility(View.GONE);
            bottomSpaceView.setVisibility(View.GONE);

            addAddressLayout.setVisibility(View.GONE);
            addressLayout.setVisibility(View.GONE);
            addressDesTV.setVisibility(View.GONE);
            distanceTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        update();
    }

    @OnClick({R.id.charter_pickup_item_add_address_layout,
            R.id.charter_pickup_item_address_layout,
            R.id.charter_pickup_item_address_tv,
            R.id.charter_pickup_item_address_des_tv})
    public void intentPoiSearch() {
        if (charterDataUtils == null || charterDataUtils.flightBean == null) {
            return;
        }
        Bundle bundle = new Bundle();
        if (context instanceof BaseActivity) {
            bundle.putString(Constants.PARAMS_SOURCE, ((BaseActivity) context).getEventSource());
        }
        bundle.putInt(PoiSearchActivity.KEY_CITY_ID, charterDataUtils.flightBean.arrCityId);
        bundle.putString(PoiSearchActivity.KEY_LOCATION, charterDataUtils.flightBean.arrLocation);
        Intent intent = new Intent(getContext(), PoiSearchActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(PoiSearchActivity.PARAM_BUSINESS_TYPE, Constants.BUSINESS_TYPE_PICK);
        getContext().startActivity(intent);
    }
}