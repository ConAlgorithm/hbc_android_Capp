package com.hugboga.custom.widget.title;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/5/17.
 */
public class TitleBarPickSend extends TitleBarBase {

    @Bind(R.id.titlebar_ps_pickup_tv)
    TextView tabPickupTV;
    @Bind(R.id.titlebar_ps_pickup_line_view)
    View tabPickupLineView;

    @Bind(R.id.titlebar_ps_send_tv)
    TextView tabSendTV;
    @Bind(R.id.titlebar_ps_send_line_view)
    View tabSendLineView;

    private TitlerBarOnClickLister lister;

    public TitleBarPickSend(Context context) {
        this(context, null);
    }

    public TitleBarPickSend(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_titlebar_pick_send, this);
        ButterKnife.bind(view);
    }

    @OnClick({R.id.titlebar_ps_left_layout, R.id.titlebar_ps_right_layout, R.id.titlebar_ps_pickup_layout, R.id.titlebar_ps_send_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_ps_left_layout:
                if (lister != null) {
                    lister.onBack();
                }
                break;
            case R.id.titlebar_ps_right_layout:
                if (lister != null) {
                    lister.onCustomerService();
                }
                break;
            case R.id.titlebar_ps_pickup_layout:
                onTabSelected(0);
                break;
            case R.id.titlebar_ps_send_layout:
                onTabSelected(1);
                break;
        }
    }

    public void onTabSelected(int index) {
        tabPickupLineView.setVisibility(index == 0 ? View.VISIBLE : View.INVISIBLE);
        tabSendLineView.setVisibility(index == 0 ? View.INVISIBLE : View.VISIBLE);
        tabPickupTV.setTextColor(index == 0 ? 0xFF161616 : 0xFF7F7F7F);
        tabSendTV.setTextColor(index == 0 ? 0xFF7F7F7F : 0xFF161616);
        if (lister != null) {
            lister.onTabSelected(index);
        }
    }

    public interface TitlerBarOnClickLister {
        void onBack();
        void onCustomerService();
        void onTabSelected(int index);
    }

    public void setTitlerBarOnClickLister(TitlerBarOnClickLister lister) {
        this.lister = lister;
    }
}
