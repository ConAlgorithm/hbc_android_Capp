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
import com.hugboga.custom.activity.CanServiceGuideListActivity;
import com.hugboga.custom.data.bean.CanServiceGuideBean;
import com.hugboga.custom.utils.GuideItemUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/4/25.
 */
public class ChooseGuideView extends LinearLayout implements HbcViewBehavior{

    @BindView(R.id.guide_item_include_avatar_iv)
    PolygonImageView avatarIV;
    @BindView(R.id.guide_item_include_gender_iv)
    ImageView genderIV;

    @BindView(R.id.guide_item_include_name_tv)
    TextView nameTV;

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

    @BindView(R.id.guide_item_include_taggroup)
    TagGroup tagGroup;

    @BindView(R.id.view_guide_item_bottom_layout)
    RelativeLayout bottomLayout;
    @BindView(R.id.view_guide_item_car_desc_tv)
    TextView carDescTV;
    @BindView(R.id.view_guide_item_choose_tv)
    TextView chooseTV;

    public ChooseGuideView(Context context) {
        this(context, null);
    }

    public ChooseGuideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_choose_guide, this);
        ButterKnife.bind(view);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFFFFFFF);
    }

    @Override
    public void update(Object _data) {
        final CanServiceGuideBean.GuidesBean data = (CanServiceGuideBean.GuidesBean) _data;
        bottomLayout.getLayoutParams().width = UIUtils.getScreenWidth();
        Tools.showImage(avatarIV, data.getAvatarS(), R.mipmap.icon_avatar_guide);
        nameTV.setText(data.getGuideName());
        nameTV.setMaxWidth(UIUtils.dip2px(200));
        nameTV.setPadding(0, 0, UIUtils.dip2px(20), 0);
        cityIV.setVisibility(View.GONE);
        cityTV.setVisibility(View.GONE);
        genderIV.setBackgroundResource(data.getGender() == 1 ? R.mipmap.icon_man : R.mipmap.icon_woman);
        orderTV.setText(data.getOrderCounts() + "单");
        evaluateTV.setText(data.getCommentNum() + "评价");
        String level = data.getServiceStar() <= 0 ? "暂无星级" : data.getServiceStar() + "星";
        starTV.setText(level);
        GuideItemUtils.setTag(tagGroup, data.getSkillLabelNames());
        if (TextUtils.isEmpty(data.getCarName()) && TextUtils.isEmpty(data.getCarBrandName())) {
            carDescTV.setText("");
        } else {
            carDescTV.setText("服务车型: " + data.getCarBrandName() + data.getCarName());
        }
        chooseTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof CanServiceGuideListActivity) {
                    ((CanServiceGuideListActivity) getContext()).chooseGuide(data);
                }
            }
        });
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof CanServiceGuideListActivity) {
                    ((CanServiceGuideListActivity) getContext()).intentGuideDetail(data);
                }
            }
        });
    }
}
