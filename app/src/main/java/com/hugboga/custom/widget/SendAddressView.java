package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/5/23.
 */
public class SendAddressView extends RelativeLayout {

    @Bind(R.id.send_start_address_tv)
    TextView startTV;
    @Bind(R.id.send_start_address_desc_tv)
    TextView startDescTV;
    @Bind(R.id.send_start_address_dash_view)
    DashView startDashView;

    @Bind(R.id.send_end_address_tv)
    TextView endTV;
    @Bind(R.id.send_end_address_desc_tv)
    TextView endDescTV;

    private OnAddressClickListener onAddressClickListener;

    public SendAddressView(Context context) {
        this(context, null);
    }

    public SendAddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_send_address, this);
        ButterKnife.bind(view);
        resetUI();
    }

    @OnClick({R.id.send_start_address_layout, R.id.send_end_address_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_start_address_layout:
                if (onAddressClickListener != null) {
                    onAddressClickListener.onStartAddressClick();
                }
                break;
            case R.id.send_end_address_layout:
                if (onAddressClickListener != null) {
                    onAddressClickListener.onEndAddressClick();
                }
                break;
        }
    }

    public void setStartAddress(String placeName, String placeDetail) {
        startTV.setText(placeName);
        if (TextUtils.isEmpty(placeDetail)) {
            startTV.setPadding(0, 0, 0, UIUtils.dip2px(12));
            startDescTV.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIUtils.dip2px(2), UIUtils.dip2px(12));
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.topMargin = UIUtils.dip2px(20);
            params.leftMargin = UIUtils.dip2px(7.5f);
            startDashView.setLayoutParams(params);

        } else {
            startTV.setPadding(0, 0, 0, 0);
            startDescTV.setVisibility(View.VISIBLE);
            startDescTV.setText(placeDetail);

            int showWidth = UIUtils.getScreenWidth() - (getContext().getResources().getDimensionPixelOffset(R.dimen.order_padding_left) * 2 + UIUtils.dip2px(20) + UIUtils.dip2px(17));
            int stringWidth = UIUtils.getStringWidth(startDescTV, placeDetail);
            int lines = stringWidth / showWidth;
            if (stringWidth%showWidth > 0) {
                lines++;
            }
            int startDesViewWidth = UIUtils.dip2px(26) + lines * UIUtils.dip2px(10);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIUtils.dip2px(2), startDesViewWidth);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.topMargin = UIUtils.dip2px(20);
            params.leftMargin = UIUtils.dip2px(7.5f);
            startDashView.setLayoutParams(params);
        }
    }

    public void setEndAddress(String placeName, String placeDetail) {
        endTV.setText(placeName);
        if (TextUtils.isEmpty(placeDetail)) {
            endDescTV.setVisibility(View.GONE);
        } else {
            endDescTV.setVisibility(View.VISIBLE);
            endDescTV.setText(placeDetail);
        }
    }

    public void resetUI() {
        setStartAddress(null, null);
        setEndAddress(null, null);
    }

    public interface OnAddressClickListener {
        public void onStartAddressClick();
        public void onEndAddressClick();
    }

    public void setOnAddressClickListener(OnAddressClickListener onAddressClickListener) {
        this.onAddressClickListener = onAddressClickListener;
    }
}
