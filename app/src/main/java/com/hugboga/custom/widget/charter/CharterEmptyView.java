package com.hugboga.custom.widget.charter;

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
 * Created by qingcha on 17/3/9.
 */
public class CharterEmptyView extends LinearLayout {

    public static final int EMPTY_TYPE = 1;
    public static final int ERROR_TYPE = 2;

    @Bind(R.id.charter_list_empty_iv)
    ImageView emptyIV;
    @Bind(R.id.charter_list_empty_hint_tv)
    TextView hintTV;
    @Bind(R.id.charter_list_empty_refresh_tv)
    TextView refreshTV;

    private CharterEmptyView.OnRefreshDataListener listener;
    private int type;

    public CharterEmptyView(Context context) {
        this(context, null);
    }

    public CharterEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view  = inflate(context, R.layout.view_charter_empty, this);
        ButterKnife.bind(view);

        refreshTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        refreshTV.getPaint().setAntiAlias(true);
    }

    public void setEmptyType(int _type) {
        this.type = _type;
        if (_type == ERROR_TYPE) {
            setVisibility(View.VISIBLE);
            emptyIV.setBackgroundResource(R.drawable.icon_sku_order_net_error);
            hintTV.setText("似乎与网络断开，请检查网络环境");
            refreshTV.setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.VISIBLE);
            emptyIV.setBackgroundResource(R.drawable.icon_sku_order_empty_car);
            hintTV.setText("很抱歉，没有找到可服务的司导，换个日期试试");
            refreshTV.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.charter_list_empty_refresh_tv})
    public void refreshData() {
        if (listener != null) {
            this.listener.onRefresh(type);
        }
    }

    public interface OnRefreshDataListener {
        public void onRefresh(int type);
    }

    public void setOnRefreshDataListener(CharterEmptyView.OnRefreshDataListener listener) {
        this.listener = listener;
    }
}
