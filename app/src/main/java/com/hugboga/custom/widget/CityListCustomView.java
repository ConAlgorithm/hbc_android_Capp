package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleNewActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityListBean;
import com.hugboga.custom.data.bean.CountryGroupBean;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by qingcha on 17/4/15.
 */

public class CityListCustomView extends LinearLayout {

    @Bind(R.id.city_custom_title_tv)
    TextView titleTV;

    @Bind(R.id.city_custom_charter_layout)
    RelativeLayout charterLayout;
    @Bind(R.id.city_custom_charter_desc_tv)
    TextView charterDescTV;

    @Bind(R.id.city_custom_line_view)
    View lineView;

    @Bind(R.id.city_custom_bottom_layout)
    LinearLayout bottomLayout;

    @Bind(R.id.city_custom_picksend_layout)
    LinearLayout picksendLayout;
    @Bind(R.id.city_custom_picksend_tv)
    TextView picksendTV;

    @Bind(R.id.city_custom_bottom_vertical_line)
    View bottomVerticalLine;

    @Bind(R.id.city_custom_single_layout)
    LinearLayout singleLayout;
    @Bind(R.id.city_custom_single_tv)
    TextView singleTV;

    private CityListBean cityListBean;

    public CityListCustomView(Context context) {
        this(context, null);
    }

    public CityListCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_city_custom, this);
        ButterKnife.bind(view);
    }

    public void setData(CityListBean cityListBean) {
        if (cityListBean == null) {
            return;
        }
        this.cityListBean = cityListBean;

        int guideCount = cityListBean.cityGuides == null ? 0 : cityListBean.cityGuides.guideAmount;
        titleTV.setText(String.format("%1$s位当地中文司导可服务", "" + guideCount));

        boolean dailyIsCanService = cityListBean.dailyServiceVo != null && cityListBean.dailyServiceVo.isCanService();
        if (dailyIsCanService) {
            charterLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(cityListBean.dailyServiceVo.bookNote)) {
                charterDescTV.setText(cityListBean.dailyServiceVo.bookNote);
            }
        } else {
            charterLayout.setVisibility(View.GONE);
        }

        boolean pickOrSendIsCanService = cityListBean.airportServiceVo != null && cityListBean.airportServiceVo.isCanService();
        if (pickOrSendIsCanService) {
            picksendLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(cityListBean.airportServiceVo.bookNote)) {
                picksendTV.setText(cityListBean.airportServiceVo.bookNote);
            }
        } else {
            picksendLayout.setVisibility(View.GONE);
        }

        boolean singleCanService = cityListBean.singleServiceVo != null && cityListBean.singleServiceVo.isCanService();
        if (singleCanService) {
            singleLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(cityListBean.singleServiceVo.bookNote)) {
                singleTV.setText(cityListBean.singleServiceVo.bookNote);
            }
        } else {
            singleLayout.setVisibility(View.GONE);
        }

        if (!pickOrSendIsCanService && !singleCanService) {
            bottomLayout.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
        } else {
            if (!dailyIsCanService) {
                lineView.setVisibility(View.GONE);
            }
            if (pickOrSendIsCanService && singleCanService) {
                bottomVerticalLine.setVisibility(View.VISIBLE);
            } else {
                bottomVerticalLine.setVisibility(View.GONE);
            }
        }
    }

    public void setData(CountryGroupBean countryGroupBean) {
        if (countryGroupBean == null) {
            return;
        }
        boolean dailyIsCanService = countryGroupBean.hasDailyService;
        if (dailyIsCanService) {
            charterLayout.setVisibility(View.VISIBLE);
        } else {
            charterLayout.setVisibility(View.GONE);
        }

        boolean pickOrSendIsCanService = countryGroupBean.hasAirportService;
        if (pickOrSendIsCanService) {
            picksendLayout.setVisibility(View.VISIBLE);
        } else {
            picksendLayout.setVisibility(View.GONE);
        }

        boolean singleCanService = countryGroupBean.hasSingleService;
        if (singleCanService) {
            singleLayout.setVisibility(View.VISIBLE);
        } else {
            singleLayout.setVisibility(View.GONE);
        }

        if (!pickOrSendIsCanService && !singleCanService) {
            bottomLayout.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
        } else {
            if (!dailyIsCanService) {
                lineView.setVisibility(View.GONE);
            }
            if (pickOrSendIsCanService && singleCanService) {
                bottomVerticalLine.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams lp = (LayoutParams) picksendTV.getLayoutParams();
                lp.setMargins(0,UIUtils.dip2px(6),UIUtils.dip2px(20),0);
                picksendTV.setLayoutParams(lp);

            } else {
                bottomVerticalLine.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.city_custom_charter_layout})
    public void intentCharter() {
        Intent intent = new Intent(getContext(), CharterFirstStepActivity.class);
        if (cityListBean != null && cityListBean.cityContent != null) {
            intent.putExtra(Constants.PARAMS_SOURCE, "城市页");
            intent.putExtra(Constants.PARAMS_START_CITY_BEAN, DatabaseManager.getCityBean("" + cityListBean.cityContent.cityId));
        } else {
            intent.putExtra(Constants.PARAMS_SOURCE, "国家页");
        }
        getContext().startActivity(intent);
    }

    @OnClick({R.id.city_custom_picksend_layout})
    public void intentPickSend() {
        Intent intent = new Intent(getContext(), PickSendActivity.class);
        intent.putExtra("source", "城市页");
        getContext().startActivity(intent);
    }

    @OnClick({R.id.city_custom_single_layout})
    public void intentSingle() {
        Intent intent = new Intent(getContext(), SingleNewActivity.class);
        if (cityListBean != null && cityListBean.cityContent != null) {
            intent.putExtra(Constants.PARAMS_SOURCE, "城市页");
            intent.putExtra(Constants.PARAMS_CITY_ID, "" + cityListBean.cityContent.cityId);
        } else {
            intent.putExtra(Constants.PARAMS_SOURCE, "国家页");
        }
        getContext().startActivity(intent);
    }
}
