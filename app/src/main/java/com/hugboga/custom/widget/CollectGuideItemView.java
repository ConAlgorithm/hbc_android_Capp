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
 * Created by qingcha on 17/4/25.
 */
public class CollectGuideItemView extends LinearLayout implements HbcViewBehavior{
    @Bind(R.id.guide_item_include_avatar_iv)
    PolygonImageView avatarIV;
    @Bind(R.id.guide_item_include_gender_iv)
    ImageView genderIV;

    @Bind(R.id.guide_item_include_name_tv)
    TextView nameTV;

    @Bind(R.id.guide_item_include_disable_iv)
    ImageView disableIV;

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

    @Bind(R.id.view_guide_item_service_line_view)
    View serviceLineView;
    @Bind(R.id.view_guide_item_service_type_tv)
    TextView serviceTypeTV;

    public CollectGuideItemView(Context context) {
        this(context, null);
    }

    public CollectGuideItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_collect_guide_item, this);
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
        } else {
            cityIV.setVisibility(View.GONE);
            cityTV.setVisibility(View.GONE);
        }

        genderIV.setBackgroundResource("1".equals(data.gender) ? R.mipmap.icon_man : R.mipmap.icon_woman);

        orderTV.setText(data.completeOrderNum + "单");

        evaluateTV.setText(data.commentNum + "评价");

        String level = data.serviceStar <= 0 ? "暂无星级" : data.serviceStar + "星";
        starTV.setText(level);

        GuideItemUtils.setTag(tagGroup, data.skillLabelNames);

        String serviceType = data.getServiceType();
        if (TextUtils.isEmpty(serviceType)) {
            serviceLineView.setVisibility(View.INVISIBLE);
            serviceTypeTV.setVisibility(View.GONE);
            disableIV.setVisibility(View.VISIBLE);
            setBackgroundColor(0xFFeaeaea);
        } else {
            serviceLineView.setVisibility(View.VISIBLE);
            serviceTypeTV.setVisibility(View.VISIBLE);
            serviceTypeTV.setText(serviceType);
            disableIV.setVisibility(View.GONE);
            setBackgroundColor(0xFFFFFFFF);
        }
    }
}
