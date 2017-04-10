package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

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
    private int type;

    public SkuOrderEmptyView(Context context) {
        this(context, null);
    }

    public SkuOrderEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view  = inflate(context, R.layout.view_sku_order_empty, this);
        ButterKnife.bind(view);

        refreshTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        refreshTV.getPaint().setAntiAlias(true);
    }

    public void setEmptyVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            setVisibility(View.VISIBLE);
            emptyIV.setBackgroundResource(R.drawable.empty_car);
            hintTV.setText("很抱歉，没有找到可服务的司导，换个日期试试");
            refreshTV.setVisibility(View.GONE);
            type = 0;
        } else {
            setVisibility(View.GONE);
        }
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

    public void setNoCarVisibility(int visibility, boolean isAssignGuide) {
        if (visibility == View.VISIBLE) {
            setVisibility(View.VISIBLE);
            emptyIV.setBackgroundResource(R.drawable.empty_car);
            if (isAssignGuide) {
                hintTV.setText("很抱歉，该司导暂无可服务的车辆\n请联系客服，我们会协助您完成预订");
            } else {
                hintTV.setText("很抱歉，没有找到可服务的司导\n请联系客服，我们会协助您完成预订");
            }
            refreshTV.setVisibility(View.VISIBLE);
            refreshTV.setText("联系客服");
            type = 2;
        } else {
            setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.sku_order_empty_refresh_tv})
    public void refreshData() {
        if (type == 1) {
            if (onRefreshDataListener != null) {
                this.onRefreshDataListener.onRefresh();
            }
        } else if (type == 2) {
            if (onClickServicesListener != null) {
                this.onClickServicesListener.onClickServices();
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

}
