package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.FilterGuideListActivity;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.utils.GuideItemUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/4/21.
 */

public class GuideItemView extends LinearLayout implements HbcViewBehavior {

    @Bind(R.id.guide_item_include_avatar_iv)
    PolygonImageView avatarIV;
    @Bind(R.id.guide_item_include_gender_iv)
    ImageView genderIV;

    @Bind(R.id.guide_item_include_name_tv)
    TextView nameTV;

    @Bind(R.id.guide_item_include_city_iv)
    ImageView cityIV;
    @Bind(R.id.guide_item_include_city_tv)
    TextView cityTV;

    @Bind(R.id.guide_item_include_order_tv)
    TextView orderTV;
    @Bind(R.id.guide_item_include_evaluate_tv)
    TextView evaluateTV;
    @Bind(R.id.guide_item_include_star_tv)
    TextView starTV;

    @Bind(R.id.guide_item_include_info_layout)
    LinearLayout infoLayout;
    @Bind(R.id.guide_item_include_taggroup)
    TagGroup tagGroup;
    @Bind(R.id.view_guide_item_desc_tv)
    TextView descTV;
    @Bind(R.id.view_guide_item_service_type_tv)
    TextView serviceTypeTV;

    public GuideItemView(Context context) {
        this(context, null);
    }

    public GuideItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_guide_item, this);
        ButterKnife.bind(view);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFFFFFFF);
    }

    @Override
    public void update(Object _data) {
        FilterGuideBean data = (FilterGuideBean) _data;
        serviceTypeTV.getLayoutParams().width = UIUtils.getScreenWidth();
        Tools.showImage(avatarIV, data.avatar, R.mipmap.icon_avatar_guide);

        nameTV.setText(data.guideName);

        boolean isShowCity = !TextUtils.isEmpty(data.cityName);
        if (isShowCity && getContext() instanceof FilterGuideListActivity) {
            FilterGuideListActivity filterGuideListActivity = (FilterGuideListActivity) getContext();
            isShowCity = filterGuideListActivity.isShowCity();
        }

        if (isShowCity) {
            cityIV.setVisibility(View.VISIBLE);
            cityTV.setVisibility(View.VISIBLE);
            cityTV.setText(data.cityName);

            nameTV.setMaxWidth(UIUtils.dip2px(100));
            nameTV.setPadding(0, 0, 0, 0);
        } else {
            cityIV.setVisibility(View.GONE);
            cityTV.setVisibility(View.GONE);

            nameTV.setMaxWidth(UIUtils.dip2px(200));
            nameTV.setPadding(0, 0, UIUtils.dip2px(20), 0);
        }

        genderIV.setBackgroundResource("1".equals(data.gender) ? R.mipmap.icon_man : R.mipmap.icon_woman);

        orderTV.setText(data.completeOrderNum + "单");

        evaluateTV.setText(data.commentNum + "评价");

        double serviceStar = data.getServiceStar();
        String level = serviceStar <= 0 ? "暂无星级" : serviceStar + "星";
        starTV.setText(level);

        GuideItemUtils.setTag(tagGroup, data.skillLabelNames);

        if (TextUtils.isEmpty(data.homeDesc) || TextUtils.isEmpty(data.homeDesc.trim())) {
            descTV.setVisibility(View.GONE);
        } else {
            descTV.setVisibility(View.VISIBLE);
            descTV.setText(data.homeDesc.trim());
        }

        String serviceType = data.getServiceType();
        if (TextUtils.isEmpty(serviceType)) {
            serviceTypeTV.setVisibility(View.GONE);
        } else {
            serviceTypeTV.setVisibility(View.VISIBLE);
            serviceTypeTV.setText(serviceType);
        }
    }
}
