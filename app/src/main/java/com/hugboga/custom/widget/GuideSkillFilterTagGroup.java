package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.FilterGuideOptionsBean;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 17/6/22.
 */
public class GuideSkillFilterTagGroup extends FilterTagGroupBase<FilterGuideOptionsBean.GuideSkillLabel>{

    public GuideSkillFilterTagGroup(Context context) {
        this(context, null);
    }

    public GuideSkillFilterTagGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setViewSelected(TextView view, boolean isSelected) {
        super.setViewSelected(view, isSelected);
        view.setCompoundDrawablesWithIntrinsicBounds(isSelected ? R.mipmap.guide_label : R.mipmap.guide_uncheck_label, 0, 0, 0);
    }

    @Override
    public TextView getTagNewView() {
        TextView tagView = super.getTagNewView();
        tagView.setCompoundDrawablePadding(UIUtils.dip2px(3));
        return tagView;
    }
}