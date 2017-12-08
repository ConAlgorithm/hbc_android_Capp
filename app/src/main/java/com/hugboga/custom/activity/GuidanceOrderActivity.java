package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.GuidanceBottomView;

import java.io.Serializable;

import butterknife.BindView;

/**
 * Created by qingcha on 17/12/6.
 */

public class GuidanceOrderActivity extends BaseActivity {

    @BindView(R.id.guidance_bottom_view)
    GuidanceBottomView bottomView;

    @BindView(R.id.guidance_img_iv1)
    ImageView imgIV1;
    @BindView(R.id.guidance_img_iv2)
    ImageView imgIV2;
    @BindView(R.id.guidance_img_iv3)
    ImageView imgIV3;
    @BindView(R.id.guidance_img_iv4)
    ImageView imgIV4;

    private Params params;

    public static class Params implements Serializable {
        public int orderType;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_guidance_order;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (GuidanceOrderActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (GuidanceOrderActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        if (params == null || params.orderType == 0) {
            finish();
        }
        init();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    private void init() {
        bottomView.setOrderType(params.orderType);

        int imgIV3Width = 0;
        int imgIV3Height = 0;

        int imgIV4Width = 0;
        int imgIV4Height = 0;

        switch (params.orderType) {
            case 1:
            case 2:

                imgIV1.setBackgroundResource(R.drawable.aeroplaneorder_picture_a);
                imgIV2.setBackgroundResource(R.drawable.aeroplaneorder_picture_b);
                imgIV3.setBackgroundResource(R.drawable.aeroplaneorder_picture_c);
                imgIV4.setBackgroundResource(R.drawable.aeroplaneorder_picture_d);

                imgIV3Width = (int) (UIUtils.getScreenWidth() * (388 / 750f));
                imgIV3Height = (int) ((222 / 388f) * imgIV3Width);

                imgIV4Width = (int) (UIUtils.getScreenWidth() * (472 / 750f));
                imgIV4Height = (int) ((444 / 472f) * imgIV4Width);
              break;
            case 3:
            case 888:
                imgIV1.setBackgroundResource(R.drawable.charter_order_picture_a);
                imgIV2.setBackgroundResource(R.drawable.charter_order_picture_b);
                imgIV3.setBackgroundResource(R.drawable.charter_order_picture_c);
                imgIV4.setBackgroundResource(R.drawable.charter_order_picture_d);

                imgIV3Width = (int) (UIUtils.getScreenWidth() * (382f / 750f));
                imgIV3Height = (int) ((276 / 382f) * imgIV3Width);

                imgIV4Width = (int) (UIUtils.getScreenWidth() * (538 / 750f));
                imgIV4Height = (int) ((612 / 538f) * imgIV4Width);
                break;
            case 4:
                imgIV1.setBackgroundResource(R.drawable.pick_order_picture_a);
                imgIV2.setBackgroundResource(R.drawable.pick_order_picture_b);
                imgIV3.setBackgroundResource(R.drawable.pick_order_picture_c);
                imgIV4.setBackgroundResource(R.drawable.pick_order_picture_d);

                imgIV3Width = (int) (UIUtils.getScreenWidth() * (382f / 750f));
                imgIV3Height = (int) ((222 / 382f) * imgIV3Width);

                imgIV4Width = (int) (UIUtils.getScreenWidth() * (472 / 750f));
                imgIV4Height = (int) ((444 / 472f) * imgIV4Width);
                break;
        }

        int imgIV1Width = (int) (UIUtils.getScreenWidth() * (562 / 750f));
        int imgIV1Height = (int) ((376 / 562f) * imgIV1Width);
        LinearLayout.LayoutParams imgIV1Params = new LinearLayout.LayoutParams(imgIV1Width, imgIV1Height);
        imgIV1Params.gravity = Gravity.CENTER_HORIZONTAL;
        imgIV1Params.topMargin = UIUtils.dip2px(36);
        imgIV1.setLayoutParams(imgIV1Params);

        int imgIV2Width = (int) (UIUtils.getScreenWidth() * (668 / 750f));
        int imgIV2Height = (int) ((272 / 668f) * imgIV2Width);
        LinearLayout.LayoutParams imgIV2Params = new LinearLayout.LayoutParams(imgIV2Width, imgIV2Height);
        imgIV2Params.gravity = Gravity.CENTER_HORIZONTAL;
        imgIV2Params.topMargin = UIUtils.dip2px(50);
        imgIV2.setLayoutParams(imgIV2Params);

        LinearLayout.LayoutParams imgIV3Params = new LinearLayout.LayoutParams(imgIV3Width, imgIV3Height);
        imgIV3Params.topMargin = UIUtils.dip2px(60);
        imgIV3Params.leftMargin = UIUtils.dip2px(22);
        imgIV3.setLayoutParams(imgIV3Params);

        LinearLayout.LayoutParams imgIV4Params = new LinearLayout.LayoutParams(imgIV4Width, imgIV4Height);
        imgIV4Params.topMargin = UIUtils.dip2px(45);
        imgIV4Params.bottomMargin = UIUtils.dip2px(50);
        imgIV4Params.leftMargin = UIUtils.dip2px(22);
        imgIV4.setLayoutParams(imgIV4Params);
    }

}
