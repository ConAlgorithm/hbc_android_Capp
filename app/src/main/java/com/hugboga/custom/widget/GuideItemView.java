package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
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

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/4/21.
 */

public class GuideItemView extends LinearLayout implements HbcViewBehavior {

    @BindView(R.id.guide_item_include_avatar_iv)
    PolygonImageView avatarIV;
    @BindView(R.id.guide_item_include_gender_iv)
    ImageView genderIV;

    @BindView(R.id.guide_item_include_name_tv)
    TextView nameTV;

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
    @BindView(R.id.view_guide_item_desc_tv)
    LinearLayout descTV;
    @BindView(R.id.view_guide_item_service_type_tv)
    LinearLayout serviceTypeTV;
    @BindView(R.id.view_guide_item_label)
    LinearLayout labelTypeTV;

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
            //    cityIV.setVisibility(View.VISIBLE);
            cityTV.setVisibility(View.VISIBLE);
            cityTV.setText(data.cityName);

            nameTV.setMaxWidth(UIUtils.dip2px(100));
            nameTV.setPadding(0, 0, 0, 0);
        } else {
            //   cityIV.setVisibility(View.GONE);
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

        // GuideItemUtils.setTag(tagGroup, data.skillLabelNames);
        if (data.skillLabelNames == null && data.skillLabelNames.size() == 0) {
            labelTypeTV.setVisibility(View.GONE);
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            for (String str : data.skillLabelNames) {
                stringBuffer.append(str + " 、");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            labelTypeTV.setVisibility(View.VISIBLE);
            ((TextView) labelTypeTV.getChildAt(1)).setText(stringBuffer.toString());
        }
        if (TextUtils.isEmpty(data.getGuideDesc())) {
            descTV.setVisibility(View.GONE);
        } else {
            descTV.setVisibility(View.VISIBLE);
            ((TextView) descTV.getChildAt(1)).setText(data.getGuideDesc());
        }

        String serviceType = data.getServiceType();
        if (TextUtils.isEmpty(serviceType)) {
            serviceTypeTV.setVisibility(View.GONE);
        } else {
            serviceTypeTV.setVisibility(View.VISIBLE);
            ((TextView) serviceTypeTV.getChildAt(1)).setText(serviceType);
        }
    }
}
