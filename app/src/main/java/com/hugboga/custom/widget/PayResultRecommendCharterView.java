package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/8/30.
 */
public class PayResultRecommendCharterView extends RelativeLayout {

    @BindView(R.id.view_pay_result_recommend_charter_bg_iv)
    ImageView bgIV;
    @BindView(R.id.view_pay_result_recommend_charter_city_tv)
    TextView cityTV;

    public PayResultRecommendCharterView(Context context) {
        this(context, null);
    }

    public PayResultRecommendCharterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_pay_result_recommend_charter, this);
        ButterKnife.bind(view);
    }

    public void setData(String cityName,final String cityId, String bgUrl) {
        Tools.showImage(bgIV, bgUrl, R.mipmap.line_goods_dafault);
        cityTV.setText(CommonUtils.getString(R.string.par_result_recommend_charter, cityName));
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CharterFirstStepActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_title));
                if (CommonUtils.getCountInteger(cityId) > 0) {
                    intent.putExtra(Constants.PARAMS_START_CITY_BEAN, DatabaseManager.getCityBean("" + cityId));
                }
                getContext().startActivity(intent);
                SensorsUtils.setSensorsBuyViewEvent("按天包车游", getContext().getString(R.string.par_result_title), "");
            }
        });
    }
}
