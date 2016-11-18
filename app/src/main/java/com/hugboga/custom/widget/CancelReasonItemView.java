package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CancelReasonBean;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/11/18.
 */
public class CancelReasonItemView extends RelativeLayout implements HbcViewBehavior{

    @Bind(R.id.cancel_reason_title_tv)
    TextView titleTV;
    @Bind(R.id.cancel_reason_selected_iv)
    ImageView selectedIV;
    @Bind(R.id.cancel_reason_line_view)
    View lineView;

    public CancelReasonItemView(Context context) {
        this(context, null);
    }

    public CancelReasonItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(UIUtils.dip2px(15), 0, UIUtils.dip2px(15), 0);
        View view = inflate(context, R.layout.view_cancel_reason_item, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        CancelReasonBean.CancelReasonItem data = (CancelReasonBean.CancelReasonItem) _data;
        if (data == null) {
            return;
        }
        titleTV.setText(data.content);
        selectedIV.setVisibility(data.isSelected ? View.VISIBLE : View.GONE);
        lineView.setVisibility(data.isOtherReason() ? View.GONE : View.VISIBLE);
    }
}
