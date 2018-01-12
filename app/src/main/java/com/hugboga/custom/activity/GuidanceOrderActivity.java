package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SeckillsBean;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.GuidanceBottomView;
import com.hugboga.custom.widget.ScrollViewWrapper;
import com.hugboga.custom.widget.title.TitleBar;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONObject;

import java.io.Serializable;

import butterknife.BindView;

/**
 * Created by qingcha on 17/12/6.
 */

public class GuidanceOrderActivity extends BaseActivity implements ScrollViewWrapper.OnScrollChangedListener {

    public static final String TAG = GuidanceOrderActivity.class.getSimpleName();
    public static final String PARAMS_GUIDANCE = "params_guidance";

    @BindView(R.id.guidance_titlebar)
    TitleBar titlebar;
    @BindView(R.id.guidance_bottom_view)
    GuidanceBottomView bottomView;
    @BindView(R.id.guidance_scroll_view)
    ScrollViewWrapper scrollView;

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
        public String source;

        public SeckillsBean seckillsBean;
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
        setSensorsDefaultEvent(); //BasicActivity中事件由于执行时params为空，所以需要获取params后再次执行
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
        scrollView.setOnScrollChangedListener(this);

        bottomView.setOrderType(params.orderType);
        bottomView.setOnInfoViewClickListener(new GuidanceBottomView.OnInfoViewClickListener() {
            @Override
            public void onInfoViewClicked(int orderType) {
                Intent intent = null;

                switch (orderType) {
                    case 1:
                        intent = new Intent(GuidanceOrderActivity.this, ChooseAirActivity.class);
                        intent.putExtra(Constants.PARAMS_TYPE, TAG);
                        intent.putExtra(PARAMS_GUIDANCE, params);
                        intent.putExtra(Constants.PARAMS_SOURCE, params.source);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(GuidanceOrderActivity.this, ChooseAirPortActivity.class);
                        intent.putExtra(PARAMS_GUIDANCE, params);
                        intent.putExtra(Constants.PARAMS_SOURCE, params.source);
                        startActivity(intent);
                        break;
                    case 3:
                    case 888:
                        intent = new Intent(GuidanceOrderActivity.this, ChooseCityActivity.class);
                        intent.putExtra(ChooseCityActivity.KEY_FROM, ChooseCityActivity.PARAM_TYPE_START);
                        intent.putExtra(KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_DAILY);
                        intent.putExtra(ChooseCityActivity.KEY_FROM_TAG, TAG);
                        intent.putExtra(Constants.PARAMS_SOURCE, params.source);
                        intent.putExtra(PARAMS_GUIDANCE, params);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(GuidanceOrderActivity.this, ChooseCityActivity.class);
                        intent.putExtra(KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_RENT);
                        intent.putExtra(ChooseCityActivity.KEY_FROM_TAG, TAG);
                        intent.putExtra(Constants.PARAMS_SOURCE, params.source);
                        intent.putExtra(PARAMS_GUIDANCE, params);
                        startActivity(intent);
                        SensorsUtils.onAppClick("单次接送","用车城市",getIntentSource());
                        break;
                }
            }
        });

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

        setSensorsViewIntroEvent();
    }

    @Override
    public void onScrollChanged(int _scrollX, int _scrollY, int _oldScrollX, int _oldScrollY) {
        if (_scrollY < 0) {
            _scrollY = 0;
        }
        if (_oldScrollY < 0) {
            _oldScrollY = 0;
        }
        int scrollViewHeight = scrollView.getMeasuredHeight();
        if (scrollViewHeight > 0) {
            if (_scrollY > scrollViewHeight) {
                _scrollY = scrollViewHeight;
            }
            if (_oldScrollY > scrollViewHeight) {
                _oldScrollY = scrollViewHeight;
            }
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titlebar.getLayoutParams();
        int titlebarScrollY = -params.topMargin;
        int border = UIUtils.dip2px(60);
        int moveDistance = Math.max(titlebarScrollY + (_scrollY - _oldScrollY), 0);
        if (_scrollY == scrollViewHeight) {
            moveDistance = border;
        }
        if (Math.abs(moveDistance) <= border) {
            params.topMargin = -moveDistance;
            titlebar.requestLayout();
        }
    }

    @Override
    protected boolean isDefaultEvent() {
        return false;
    }

    @Override
    public String getEventSource() {
        if (params != null) {
            switch (params.orderType) {
                case 1:
                case 2:
                    return "接送机引导页";
                case 3:
                case 888:
                    return "按天包车引导页";
                case 4:
                    return "单次接送引导页";
            }
        }
        return super.getEventSource();
    }

    private void setSensorsViewIntroEvent() {
        try {
            String hbcSkuType = null;
            switch (params.orderType) {
                case 1:
                    hbcSkuType = "接机";
                    break;
                case 2:
                    hbcSkuType = "送机";
                    break;
                case 3:
                case 888:
                    hbcSkuType = "按天包车游";
                    break;
                case 4:
                    hbcSkuType = "单次";
                    break;
            }
            JSONObject properties = new JSONObject();
            properties.put("hbc_refer", getIntentSource());
            properties.put("hbc_sku_type", hbcSkuType);
            SensorsDataAPI.sharedInstance(this).track("viewIntro", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
