package com.hugboga.custom.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 17/6/2.
 */
public class PickupCouponDialog extends Dialog {

    public PickupCouponDialog(Context context) {
        super(context, R.style.MyDialog);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_pickup_coupon);

        ImageView imageView = (ImageView)findViewById(R.id.dialog_pickup_coupon_iv);
        int imgWidth = UIUtils.getScreenWidth();
        int imgHeight = (int)(750 / 720.0f * imgWidth);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imgWidth, imgHeight);
        params.gravity = Gravity.CENTER;
        imageView.setLayoutParams(params);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
