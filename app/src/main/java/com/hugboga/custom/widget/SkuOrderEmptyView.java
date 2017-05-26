package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CarBean;

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
            hintTV.setText("似乎与网络断开，请检查网络环境");
            refreshTV.setVisibility(View.VISIBLE);
            refreshTV.setText("刷新试试");
            type = 1;
        } else {
            setVisibility(View.GONE);
        }
    }

    public boolean setEmptyVisibility(ArrayList<CarBean> _carList, int noneCarsState, String noneCarsReason, boolean isAssignGuide) {
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
            if (TextUtils.isEmpty(noneCarsReason)) {
                if (noneCarsState == 202) {
                    emptyIV.setBackgroundResource(R.drawable.empty_time);
                    hintTV.setText("当地时间已过了您预订的服务时间，想服务但做不到啊…");
                } else if (noneCarsState == 6) {
                    emptyIV.setBackgroundResource(R.drawable.empty_time);
                    hintTV.setText("很抱歉，预留的时间太短了无法预订，建议您下次早做打算哦");
                } else if (noneCarsState == 301) {
                    emptyIV.setBackgroundResource(R.drawable.empty_trip);
                    hintTV.setText("很抱歉，您选择的地点暂时无法通过驾车的方式到达");
                }
            }
        } else if (noneCarsState == 302) {
            setVisibility(View.VISIBLE);
            emptyIV.setBackgroundResource(R.drawable.empty_trip);
            hintTV.setText("您预订的行程太远啦，包车压力好大…建议按天包车，去试试按天包车游");
            isEmpty = true;
            refreshTV.setVisibility(View.VISIBLE);
            refreshTV.setText("按天包车游");
            type = 3;
        } else if (_carList == null || _carList.size() <= 0) {
            setVisibility(View.VISIBLE);
            emptyIV.setBackgroundResource(R.drawable.empty_car);
            if (TextUtils.isEmpty(noneCarsReason)) {
                if (isAssignGuide) {
                    hintTV.setText("很抱歉，该司导暂无符合的车型\n请联系客服，我们会协助您完成预订");
                } else {
                    hintTV.setText("很抱歉，没有找到可服务的司导\n请联系客服，我们会协助您完成预订");
                }
            } else {
                hintTV.setText(noneCarsReason + "\n请联系客服，我们会协助您完成预订");
            }

            isEmpty = true;
            refreshTV.setVisibility(View.VISIBLE);
            refreshTV.setText("联系客服");
            type = 2;
        } else {
            setVisibility(View.GONE);
        }
        return isEmpty;
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

    public void setonClickCharterListener(OnClickCharterListener onClickCharterListener) {
        this.onClickCharterListener = onClickCharterListener;
    }
}
