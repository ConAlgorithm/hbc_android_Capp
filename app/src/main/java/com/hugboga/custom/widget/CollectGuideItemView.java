package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.FilterGuideListActivity;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.utils.GuideItemUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/4/25.
 */
public class CollectGuideItemView extends RelativeLayout implements HbcViewBehavior{
    @BindView(R.id.guide_item_include_avatar_iv)
    PolygonImageView avatarIV;
    @BindView(R.id.guide_item_include_gender_iv)
    ImageView genderIV;

    @BindView(R.id.guide_item_include_name_tv)
    TextView nameTV;

    @BindView(R.id.view_guide_item_disable_tv)
    TextView disableTV;
    @BindView(R.id.view_guide_item_disable_shade_view)
    View disableShadeView;

    @BindView(R.id.guide_item_include_city_iv)
    ImageView cityIV;
    @BindView(R.id.guide_item_include_city_tv)
    TextView cityTV;

    @BindView(R.id.guide_item_include_order_tv)
    TextView orderTV;
    @BindView(R.id.guide_item_include_evaluate_tv)
    TextView evaluateTV;
    @BindView(R.id.guide_item_include_star_tv)
    TextView starTV;

    @BindView(R.id.guide_item_include_info_layout)
    LinearLayout infoLayout;
    @BindView(R.id.guide_item_include_taggroup)
    TagGroup tagGroup;

    @BindView(R.id.view_guide_item_service_line_view)
    View serviceLineView;
    @BindView(R.id.view_guide_item_service_type_tv)
    TextView serviceTypeTV;

    public CollectGuideItemView(Context context) {
        this(context, null);
    }

    public CollectGuideItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_collect_guide_item, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        FilterGuideBean data = (FilterGuideBean) _data;
        serviceTypeTV.getLayoutParams().width = UIUtils.getScreenWidth();
        Tools.showImage(avatarIV, data.guideAvatarUrl, R.mipmap.icon_avatar_guide);

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
        } else {
            cityIV.setVisibility(View.GONE);
            cityTV.setVisibility(View.GONE);
        }

        genderIV.setBackgroundResource("1".equals(data.gender) ? R.mipmap.icon_man : R.mipmap.icon_woman);

        orderTV.setText(data.completeOrderNum + "单");

        evaluateTV.setText(data.commentNum + "评价");

        double serviceStar = data.getServiceStar();
        String level = serviceStar <= 0 ? "暂无星级" : serviceStar + "星";
        starTV.setText(level);

        GuideItemUtils.setTag(tagGroup, data.skillLabelNames);

        String serviceType = "";

        if (!data.isCanService()) {
            disableTV.setVisibility(View.VISIBLE);
            disableShadeView.setVisibility(View.VISIBLE);
            serviceType = "司导暂不可为您提供服务";
        } else {
            disableTV.setVisibility(View.GONE);
            disableShadeView.setVisibility(View.GONE);
            serviceType = data.getServiceType();
        }

        if (TextUtils.isEmpty(serviceType)) {
            serviceLineView.setVisibility(View.INVISIBLE);
            serviceTypeTV.setVisibility(View.GONE);
        } else {
            serviceLineView.setVisibility(View.VISIBLE);
            serviceTypeTV.setVisibility(View.VISIBLE);
            serviceTypeTV.setText(serviceType);
        }
    }
}
