package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.utils.GuideItemUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/4/14.
 */
public class ChoicenessGuideView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.choiceness_guide_bg_iv)
    ImageView bgIV;
    @Bind(R.id.choiceness_guide_description_tv)
    TextView descTV;
    @Bind(R.id.choiceness_guide_level_tv)
    TextView levelTV;
    @Bind(R.id.choiceness_guide_name_tv)
    TextView nameTV;
    @Bind(R.id.choiceness_guide_taggroup)
    TagGroup tagGroup;
    @Bind(R.id.choiceness_guide_service_type_tv)
    TextView serviceTypeTV;

    @Bind(R.id.choiceness_guide_city_iv)
    ImageView cityIV;
    @Bind(R.id.choiceness_guide_city_tv)
    TextView cityTV;

    public ChoicenessGuideView(Context context) {
        this(context, null);
    }

    public ChoicenessGuideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_choiceness_guide, this);
        ButterKnife.bind(view);

        int padding = getContext().getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFFFFFFF);
        setPadding(0, padding, 0, 0);

        int imageWidth = UIUtils.getScreenWidth() - padding * 2;
        int imageHeight = (int)((400/690.0f) * imageWidth);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageWidth, imageHeight);
        bgIV.setLayoutParams(params);
    }

    @Override
    public void update(Object _data) {
        final FilterGuideBean data = (FilterGuideBean) _data;
        Tools.showImage(bgIV, data.guideCover, R.drawable.home_guide_dafault);

        if (TextUtils.isEmpty(data.homeDesc) || TextUtils.isEmpty(data.homeDesc.trim())) {
            descTV.setVisibility(View.GONE);
        } else {
            descTV.setVisibility(View.VISIBLE);
            descTV.setText(data.homeDesc.trim());
        }
        if (data.serviceStar <= 0) {
            levelTV.setText("暂无星级");
            levelTV.setTextSize(11);
            levelTV.setTextColor(0xFFD1D1D1);
        } else {
            levelTV.setText("" + data.serviceStar);
            levelTV.setTextSize(16);
            levelTV.setTextColor(0xFFF9B900);
        }
        nameTV.setText(data.guideName);
        GuideItemUtils.setTag(tagGroup, data.skillLabelNames);

        boolean isShowCity = false;
        if (!TextUtils.isEmpty(data.cityName)) {
            if (getContext() instanceof MainActivity) {
                isShowCity = true;
            } else if (getContext() instanceof CityListActivity && ((CityListActivity)getContext()).isShowCity()) {
                isShowCity = true;
            }
        }

        if (isShowCity) {
            cityIV.setVisibility(View.VISIBLE);
            cityTV.setVisibility(View.VISIBLE);
            cityTV.setText(data.cityName);
        } else {
            cityIV.setVisibility(View.GONE);
            cityTV.setVisibility(View.GONE);
        }

        String serviceType = data.getServiceType();
        if (TextUtils.isEmpty(serviceType)) {
            serviceTypeTV.setVisibility(View.GONE);
        } else {
            serviceTypeTV.setVisibility(View.VISIBLE);
            serviceTypeTV.setText(serviceType);
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
                params.guideId = data.guideId;
                Intent intent = new Intent(getContext(), GuideWebDetailActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                getContext().startActivity(intent);
            }
        });
    }
}
