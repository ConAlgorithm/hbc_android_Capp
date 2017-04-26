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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/4/25.
 */
public class ChooseGuideView extends LinearLayout implements HbcViewBehavior{

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

    @Bind(R.id.guide_item_include_taggroup)
    TagGroup tagGroup;

    @Bind(R.id.view_guide_item_bottom_layout)
    RelativeLayout bottomLayout;
    @Bind(R.id.view_guide_item_car_desc_tv)
    TextView carDescTV;
    @Bind(R.id.view_guide_item_choose_tv)
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
        nameTV.setText(data.getGenderName());
        cityIV.setVisibility(View.GONE);
        cityTV.setVisibility(View.GONE);
        genderIV.setBackgroundResource(data.getGender() == 1 ? R.mipmap.icon_man : R.mipmap.icon_woman);
        orderTV.setText(data.getOrderCounts() + "单");
        evaluateTV.setText(data.getCommentNum() + "评价");
        String level = data.getServiceStar() <= 0 ? "暂无星级" : data.getServiceStar() + "星";
        starTV.setText(level);
        GuideItemUtils.setTag(tagGroup, data.getSkillLabelNames());
        if (TextUtils.isEmpty(data.getCarName())) {
            carDescTV.setText("");
        } else {
            carDescTV.setText("服务车型: " + data.getCarName());
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
