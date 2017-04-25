package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.FilterGuideListActivity;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

import java.util.ArrayList;

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

        setTag(data.skillLabelNames);

        String serviceType = data.getServiceType();
        if (TextUtils.isEmpty(serviceType)) {
            serviceLineView.setVisibility(View.INVISIBLE);
            serviceTypeTV.setVisibility(View.GONE);
            disableIV.setVisibility(View.VISIBLE);
            setBackgroundColor(0xFFEAEAEA);
        } else {
            serviceLineView.setVisibility(View.VISIBLE);
            serviceTypeTV.setVisibility(View.VISIBLE);
            serviceTypeTV.setText(serviceType);
            disableIV.setVisibility(View.GONE);
            setBackgroundColor(0xFFFFFFFF);
        }
    }

    private void setTag(ArrayList<String> skillLabelNames) {
        if (skillLabelNames == null || skillLabelNames.size() == 0) {
            tagGroup.setVisibility(View.GONE);
            return;
        }
        tagGroup.setVisibility(View.VISIBLE);
        final int labelsSize = skillLabelNames.size() > 5 ? 5 : skillLabelNames.size();
        ArrayList<View> viewList = new ArrayList<View>(labelsSize);
        for (int i = 0; i < labelsSize; i++) {
            String tag = skillLabelNames.get(i);
            if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(tag.trim())) {
                continue;
            }
            tag = tag.trim();
            if (i < tagGroup.getChildCount()) {
                LinearLayout tagLayout = (LinearLayout)tagGroup.getChildAt(i);
                tagLayout.setVisibility(View.VISIBLE);
                TextView tagTV = (TextView)tagLayout.findViewWithTag("tagTV");
                tagTV.setText(tag);
            } else {
                viewList.add(getNewTagView(tag));
            }
        }
        for (int j = labelsSize; j < tagGroup.getChildCount(); j++) {
            tagGroup.getChildAt(j).setVisibility(View.GONE);
        }
        tagGroup.setTags(viewList, tagGroup.getChildCount() <= 0);
    }

    private LinearLayout getNewTagView(String label) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(R.mipmap.personal_label);
        linearLayout.addView(imageView);
        imageView.getLayoutParams().height = UIUtils.dip2px(12);
        imageView.getLayoutParams().width = UIUtils.dip2px(12);

        TextView tagTV = new TextView(getContext());
        tagTV.setPadding(UIUtils.dip2px(2), 0, 0, UIUtils.dip2px(3));
        tagTV.setTextSize(13);
        tagTV.setTextColor(0xFFF9B900);
        tagTV.setEnabled(false);
        tagTV.setText(label);
        tagTV.setTag("tagTV");
        linearLayout.addView(tagTV);

        return linearLayout;
    }
}
