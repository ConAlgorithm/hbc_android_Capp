package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.FilterItemBase;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

public class FilterTagGroupBase<T extends FilterItemBase> extends TagGroup {

    private ArrayList<String> selectedTagIdList;
    private ArrayList<T> list;

    public FilterTagGroupBase(Context context) {
        this(context, null);
    }

    public FilterTagGroupBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        selectedTagIdList = new ArrayList<String>();
    }

    public ArrayList<T> getList() {
        selectedTagIdList.clear();
        if (list != null) {
            int labelsSize = list.size();
            for (int i = 0; i < labelsSize; i++) {
                View tagView = getChildAt(i);
                list.get(i).isSelected = tagView.isSelected();
            }
        }
        return list;
    }

    public void setData(ArrayList<T> _list) {
        int childCount = getChildCount();

        if (_list == null) {
            for (int j = 0; j < childCount; j++) {
                getChildAt(j).setVisibility(View.GONE);
            }
            return;
        }
        this.list = _list;
        int labelsSize = list.size();
        for (int i = 0; i < labelsSize; i++) {
            TextView tagTV = null;
            if (i < childCount) {
                tagTV = (TextView) getChildAt(i);
                tagTV.setVisibility(View.VISIBLE);
            } else {
                tagTV = getTagNewView();
                addTag(tagTV);
            }
            FilterItemBase filterItemBase = list.get(i);
            tagTV.setTag(filterItemBase.getTagId());
            tagTV.setText(filterItemBase.getName());
            setViewSelected(tagTV, filterItemBase.isSelected);
        }
        for (int j = labelsSize; j < childCount; j++) {
            getChildAt(j).setVisibility(View.GONE);
        }
    }

    public void setViewSelected(TextView view, boolean isSelected) {
        view.setSelected(isSelected);
        view.setTextColor(isSelected ? getContext().getResources().getColor(R.color.default_yellow) : 0xFF8A8A8A);
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