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
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.utils.CommonUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/23.
 */
public class SkuOrderEmptyView extends LinearLayout{

    @Bind(R.id.sku_order_empty_iv)
    ImageView emptyIV;
    @Bind(R.id.sku_order_empty_hint_tv)
    TextView hintTV;
    @Bind(R.id.sku_order_empty_refresh_tv)
    TextView refreshTV;

    private OnRefreshDataListener onRefreshDataListener;
    private OnClickServicesListener onClickServicesListener;
    private OnClickCharterListener onClickCharterListener;
    private int type;

    public SkuOrderEmptyView(Context context) {
        this(context, null);
    }

    public SkuOrderEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view  = inflate(context, R.layout.view_sku_order_empty, this);
        ButterKnife.bind(view);

    }

    public void setErrorVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            setVisibility(View.VISIBLE);
            emptyIV.setBackgroundResource(R.drawable.empty_wifi);
            hintTV.setText(R.string.order_empty_wifi);
            refreshTV.setVisibility(View.VISIBLE);
            refreshTV.setText(R.string.order_empty_refresh);
            type = 1;
        } else {
            setVisibility(View.GONE);
        }
    }

    public boolean setEmptyVisibility(ArrayList<CarBean> _carList, int noneCarsState, String noneCarsReason, boolean isAssignGuide, int orderType) {
        boolean isEmpty = false;
        // noneCarsState == 202 当地时间已过了 服务开始时间
        // noneCarsState == 6 服务开始时间 在当地时间之后，但小于提前预订期
        // noneCarsState == 301 很抱歉，您选择的地点暂时无法通过驾车的方式到达
        // noneCarsState == 302 您预订的行程太远啦，包车压力好大…建议按天包车，去试试定制包车游
        if (noneCarsState == 202 || noneCarsState == 6 || noneCarsState == 301) {
            setVisibility(View.VISIBLE);
            refreshTV.setVisibility(View.GONE);
            hintTV.setText(noneCarsReason);
            isEmpty = true;
            if (noneCarsState == 202) {
                emptyIV.setBackgroundResource(R.drawable.empty_time);
            } else if (noneCarsState == 6) {
                emptyIV.setBackgroundResource(R.drawable.empty_time);
            } else if (noneCarsState == 301) {
                emptyIV.setBackgroundResource(R.drawable.empty_trip);
            }
            if (TextUtils.isEmpty(noneCarsReason)) {
                if (noneCarsState == 202) {
                    hintTV.setText(R.string.order_empty_error1);
                } else if (noneCarsState == 6) {
                    hintTV.setText(R.string.order_empty_error2);
                } else if (noneCarsState == 301) {
                    hintTV.setText(R.string.order_empty_error3);
                }
            }
        } else if (noneCarsState == 302) {
            setVisibility(View.VISIBLE);
            emptyIV.setBackgroundResource(R.drawable.empty_trip);
            String hintText = noneCarsReason;
            if (TextUtils.isEmpty(hintText)) {
                hintText = CommonUtils.getString(R.string.order_empty_range);
            }
            if (orderType != 3 && orderType != 888) {
                hintText += CommonUtils.getString(R.string.order_empty_charter);
                refreshTV.setVisibility(View.VISIBLE);
                refreshTV.setText(R.string.custom_chartered);
                type = 3;
            } else {
                hintText += CommonUtils.getString(R.string.order_empty_service);
                refreshTV.setVisibility(View.VISIBLE);
                refreshTV.setText(R.string.contact_service);
                type = 2;
            }
            hintTV.setText(hintText);
            isEmpty = true;
        } else if (_carList == null || _carList.size() <= 0) {
            setVisibility(View.VISIBLE);
            emptyIV.setBackgroundResource(R.drawable.empty_car);
            if ((noneCarsState == 40040211 || noneCarsState == 40040211 || noneCarsState == 40040213) && !TextUtils.isEmpty(noneCarsReason)) {
                hintTV.setText(noneCarsReason + CommonUtils.getString(R.string.order_empty_service));
            } else if (isAssignGuide) {
                hintTV.setText(R.string.order_empty_car_guide);
            } else {
                hintTV.setText(R.string.order_empty_car);
            }
            isEmpty = true;
            refreshTV.setVisibility(View.VISIBLE);
            refreshTV.setText(R.string.contact_service);
            type = 2;
        } else {
            setVisibility(View.GONE);
        }
        return isEmpty;
    }

    public void setSeckillsEmpty(String noneCarsReason, OnClickListener listener) {
        setVisibility(View.VISIBLE);
        emptyIV.setBackgroundResource(R.drawable.empty_car);
        hintTV.setText(noneCarsReason);
        refreshTV.setVisibility(View.VISIBLE);
        refreshTV.setText(R.string.order_empty_continue);
        refreshTV.setOnClickListener(listener);
    }

    @OnClick(R.id.sku_order_empty_refresh_tv)
    public void onClick(View view) {
        if (type == 1) {
            if (onRefreshDataListener != null) {
                this.onRefreshDataListener.onRefresh();
            }
        } else if (type == 2) {
            if (onClickServicesListener != null) {
                this.onClickServicesListener.onClickServices();
            }
        } else if (type == 3) {
            if (onClickCharterListener != null) {
                this.onClickCharterListener.onClickCharter();
            }
        }

    }

    public interface OnRefreshDataListener {
        public void onRefresh();
    }

    public void setOnRefreshDataListener(OnRefreshDataListener onRefreshDataListener) {
        this.onRefreshDataListener = onRefreshDataListener;
    }

    public interface OnClickServicesListener {
        public void onClickServices();
    }

    public void setOnClickServicesListener(OnClickServicesListener onClickServicesListener) {
        this.onClickServicesListener = onClickServicesListener;
    }

    public interface OnClickCharterListener {
        public void onClickCharter();
    }

    public void setOnClickCharterListener(OnClickCharterListener onClickCharterListener) {
        this.onClickCharterListener = onClickCharterListener;
    }
}
