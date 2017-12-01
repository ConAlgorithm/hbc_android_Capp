package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityListBean;
import com.hugboga.custom.data.bean.CountryGroupBean;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.Tools;

import net.grobas.view.PolygonImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/4/15.
 */

public class CityListCustomView extends LinearLayout {

    @BindView(R.id.city_custom_title_tv)
    TextView titleTV;

    @BindView(R.id.city_custom_charter_layout)
    RelativeLayout charterLayout;
    @BindView(R.id.city_custom_charter_desc_tv)
    TextView charterDescTV;

    @BindView(R.id.city_custom_line_view)
    View lineView;

    @BindView(R.id.city_custom_bottom_layout)
    LinearLayout bottomLayout;

    @BindView(R.id.city_custom_picksend_layout)
    LinearLayout picksendLayout;
    @BindView(R.id.city_custom_picksend_tv)
    TextView picksendTV;

    @BindView(R.id.city_custom_bottom_vertical_line)
    View bottomVerticalLine;

    @BindView(R.id.city_custom_single_layout)
    LinearLayout singleLayout;
    @BindView(R.id.city_custom_single_tv)
    TextView singleTV;

    @BindView(R.id.city_custom_city_title_layout)
    RelativeLayout cityTitleLayout;
    @BindView(R.id.city_custom_city_title_tv)
    TextView cityTitleTV;
    @BindViews({R.id.city_custom_city_guide_avatar_iv1
            , R.id.city_custom_city_guide_avatar_iv2
            , R.id.city_custom_city_guide_avatar_iv3
            , R.id.city_custom_city_guide_avatar_iv4
            , R.id.city_custom_city_guide_avatar_iv5})
    List<PolygonImageView> avatarViewList;


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

        titleTV.setVisibility(View.GONE);
        cityTitleLayout.setVisibility(View.VISIBLE);

        int guideCount = cityListBean.cityGuides == null ? 0 : cityListBean.cityGuides.guideAmount;
        String title = CommonUtils.getString(R.string.city_guide_hint, "" + guideCount);
        SpannableString msp = new SpannableString(title);
        msp.setSpan(new RelativeSizeSpan(1.4f), 0, ("" + guideCount).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cityTitleTV.setText(msp);

        if (cityListBean.cityGuides != null && cityListBean.cityGuides.guideAvatars != null) {
            ArrayList<String> guideAvatars = cityListBean.cityGuides.guideAvatars;
            int avatarViewSize = avatarViewList.size();
            int avatarSize = guideAvatars.size();
            for (int i = 0; i < avatarSize; i++) {
                int viewIndex = avatarViewSize - i - 1;
                if (viewIndex < 0) {
                    break;
                }
                PolygonImageView avatarView = avatarViewList.get(viewIndex);
                avatarView.setVisibility(View.VISIBLE);
                Tools.showImage(avatarView, guideAvatars.get(i), R.mipmap.icon_avatar_guide);
            }
        }

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
            } else {
                bottomVerticalLine.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.city_custom_charter_layout})
    public void intentCharter() {
        Intent intent = new Intent(getContext(), CharterFirstStepActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        if (cityListBean != null && cityListBean.cityContent != null) {
            intent.putExtra(Constants.PARAMS_START_CITY_BEAN, DatabaseManager.getCityBean("" + cityListBean.cityContent.cityId));
        }
        getContext().startActivity(intent);
    }

    @OnClick({R.id.city_custom_picksend_layout})
    public void intentPickSend() {
        Intent intent = new Intent(getContext(), PickSendActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        getContext().startActivity(intent);
    }

    @OnClick({R.id.city_custom_single_layout})
    public void intentSingle() {
        Intent intent = new Intent(getContext(), SingleActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        if (cityListBean != null && cityListBean.cityContent != null) {
            SingleActivity.Params singleParams = new SingleActivity.Params();
            singleParams.cityId = "" + cityListBean.cityContent.cityId;
            intent.putExtra(Constants.PARAMS_DATA, singleParams);
        }
        getContext().startActivity(intent);
    }

    public String getEventSource() {
        if (getContext() instanceof CityListActivity) {
            return ((CityListActivity) getContext()).getEventSource();
        } else if (cityListBean != null && cityListBean.cityContent != null) {
            return "城市";
        } else {
            return "国家";
        }
    }
}
