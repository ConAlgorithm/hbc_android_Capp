package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.GoodsFilterBean;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/4/22.
 */
public class SkuThemeTagGroup extends TagGroup{

    private ArrayList<String> selectedTagIdList;
    private List<GoodsFilterBean.FilterTheme> themeList;

    public SkuThemeTagGroup(Context context) {
        this(context, null);
    }

    public SkuThemeTagGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        selectedTagIdList = new ArrayList<String>();
    }

    public List<GoodsFilterBean.FilterTheme> getThemeList() {
        selectedTagIdList.clear();
        if (themeList != null) {
            int labelsSize = themeList.size();
            for (int i = 0; i < labelsSize; i++) {
                View tagView = getChildAt(i);
                GoodsFilterBean.FilterTheme filterTheme = themeList.get(i);
                filterTheme.isSelected = tagView.isSelected();
            }
        }
        return themeList;
    }

    public void setThemeData(List<GoodsFilterBean.FilterTheme> themeList) {
        int childCount = getChildCount();

        if (themeList == null) {
            for (int j = 0; j < childCount; j++) {
                getChildAt(j).setVisibility(View.GONE);
            }
            return;
        }
        this.themeList = themeList;
        int labelsSize = themeList.size();
        for (int i = 0; i < labelsSize; i++) {
            TextView tagTV = null;
            if (i < childCount) {
                tagTV = (TextView) getChildAt(i);
                tagTV.setVisibility(View.VISIBLE);
            } else {
                tagTV = getTagNewView();
                addTag(tagTV);
            }
            tagTV.setTag(themeList.get(i).themeId);
            tagTV.setText(themeList.get(i).themeName);
            setViewSelected(tagTV, themeList.get(i).isSelected);
        }
        for (int j = labelsSize; j < childCount; j++) {
            getChildAt(j).setVisibility(View.GONE);
        }
    }

    public void setViewSelected(TextView view, boolean isSelected) {
        view.setSelected(isSelected);
        view.setTextColor(isSelected ? 0xFFFFC620 : 0xFF8A8A8A);
    }

    public TextView getTagNewView() {
        TextView tagTV = new TextView(getContext());
        tagTV.setGravity(Gravity.CENTER);
        tagTV.setTextSize(12);
        tagTV.setBackgroundResource(R.drawable.selector_guide_filter_tag);
        tagTV.setPadding(UIUtils.dip2px(16), UIUtils.dip2px(7), UIUtils.dip2px(16), UIUtils.dip2px(7));
        return tagTV;
    }
}
