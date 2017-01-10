package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.AppraisementBean;
import com.hugboga.custom.data.bean.EvaluateTagBean;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/7.
 */
public class EvaluateTagGroup extends TagGroup implements TagGroup.OnTagItemClickListener {

    private final static int DEFAULT_TAG_COUNTS = 6;
    private final static String MORE_BTN_TAG_EVALUATE= "more_tags_evaluate";
    private final static String MORE_BTN_TAG_SHOW = "more_tags_show";

    private EvaluateTagBean tagBean = null;
    private int currentTagListSize;
    private boolean isShow = false;
    private ArrayList<String> requestTagIds;
    private boolean tagEnabled = true;

    public EvaluateTagGroup(Context context) {
        super(context);
    }

    public EvaluateTagGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTagItemClickListener(this);
        setTagEnabled(true);
        requestTagIds = new ArrayList<String>();
    }

    public void setTagEnabled(boolean isEnabled) {
        this.tagEnabled = isEnabled;
        for (int i = 0; i < getChildCount(); i++) {
            if (!MORE_BTN_TAG_SHOW.equals(getChildAt(i).getTag()) && !MORE_BTN_TAG_EVALUATE.equals(getChildAt(i).getTag())) {
                getChildAt(i).setEnabled(false);
            }
        }
    }

    public String getRequestTagIds() {
        if (requestTagIds == null) {
            return null;
        }
        String result = "";
        final int size = requestTagIds.size();
        for (int i = 0; i < size; i++) {
            result += requestTagIds.get(i);
            if (i <= size - 1) {
                result += ",";
            }
        }
        return result;
    }

    @Override
    public void onTagClick(View view, int position) {
        if (MORE_BTN_TAG_SHOW.equals(view.getTag()) && getChildCount() > DEFAULT_TAG_COUNTS) {
            int size = getChildCount();
            if (getChildCount() > DEFAULT_TAG_COUNTS + 1) {
                --size;
            }
            setPickDown(MORE_BTN_TAG_SHOW, size);
        } else if (MORE_BTN_TAG_EVALUATE.equals(view.getTag())) {
            setPickDown(MORE_BTN_TAG_EVALUATE, currentTagListSize);
        } else if (tagEnabled) {
            if (view.isSelected()) {//取消
                if (requestTagIds.contains(String.valueOf(view.getTag()))) {
                    requestTagIds.remove(String.valueOf(view.getTag()));
                }
            } else {//添加
                if (!requestTagIds.contains(String.valueOf(view.getTag()))) {
                    requestTagIds.add(String.valueOf(view.getTag()));
                }
            }
            view.setSelected(!view.isSelected());
        }
    }

    public void setPickDown(String viewTag, int size) {
        isShow = !isShow;
        for (int i = DEFAULT_TAG_COUNTS; i < size; i++) {
            getChildAt(i).setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
        ((TextView)findViewWithTag(viewTag)).setText(getContext().getString(isShow ? R.string.pick_up : R.string.more));
    }

    public void initTagView(EvaluateTagBean _tagBean) {
        removeAllViews();
        setVisibility(View.GONE);
        if (_tagBean == null) {
            return;
        }
        this.tagBean = _tagBean;
        int[] tagSize = new int[5];
        tagSize[0] = tagBean.oneStarTags != null ? tagBean.oneStarTags.size() : 0;
        tagSize[1] = tagBean.twoStarTags != null ? tagBean.twoStarTags.size() : 0;
        tagSize[2] = tagBean.threeStarTags != null ? tagBean.threeStarTags.size() : 0;
        tagSize[3] = tagBean.fourStarTags != null ? tagBean.fourStarTags.size() : 0;
        tagSize[4] = tagBean.fiveStarTags != null ? tagBean.fiveStarTags.size() : 0;

        for (int i = 0; i < tagSize.length - 1; i++) {
            if (tagSize[i] > tagSize[i+1]) {
                int temp = tagSize[i];
                tagSize[i] = tagSize[i+1];
                tagSize[i+1] = temp;
            }
        }
        ArrayList<View> viewList = new ArrayList<View>();
        int getMaxTagSize = tagSize[tagSize.length - 1];
        for (int i = 0; i < getMaxTagSize; i++) {
            TextView tagTV = getTagNewView();
            tagTV.setBackgroundResource(R.drawable.shape_evaluate_tag);
            if (i + 1 > DEFAULT_TAG_COUNTS) {
                tagTV.setVisibility(View.GONE);
            }
            viewList.add(tagTV);
        }
        if (getMaxTagSize > DEFAULT_TAG_COUNTS) {
            TextView moreTV = getTagNewView();
            moreTV.setText(getContext().getString(R.string.more));
            moreTV.setBackgroundResource(R.drawable.shape_evaluate_more);
            moreTV.setTag(MORE_BTN_TAG_EVALUATE);
            moreTV.setVisibility(View.GONE);
            viewList.add(moreTV);
        }
        this.setTags(viewList);
    }

    public void setEvaluatedData(ArrayList<AppraisementBean.GuideLabels> guideLabels) {
        if (guideLabels == null) {
            return;
        }
        ArrayList<View> viewList = new ArrayList<View>();
        final int labelsSize = guideLabels.size();
        for (int i = 0; i < labelsSize; i++) {
            TextView tagTV = getTagNewView();
            tagTV.setBackgroundResource(R.drawable.shape_evaluate_tag);
            tagTV.setText(guideLabels.get(i).name);
            tagTV.setSelected(guideLabels.get(i).checked);
            if (i + 1 > DEFAULT_TAG_COUNTS) {
                tagTV.setVisibility(View.GONE);
            }
            viewList.add(tagTV);
        }
        if (labelsSize > DEFAULT_TAG_COUNTS) {
            TextView moreTV = getTagNewView();
            moreTV.setText(getContext().getString(R.string.more));
            moreTV.setBackgroundResource(R.drawable.shape_evaluate_more);
            moreTV.setTag(MORE_BTN_TAG_SHOW);
            viewList.add(moreTV);
        }
        this.setTags(viewList);
    }

    public void setLevelChanged(int level) {
        if (getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }
        ArrayList<EvaluateTagBean.EvaluateTag> tagList = tagBean.getCurrentList(level);
        int i = 0;
        while (i < getChildCount()) {
            final TextView tagTV = (TextView)getChildAt(i);
            if (tagList == null) {
                tagTV.setVisibility(View.GONE);
            } else {
                tagTV.setSelected(false);
                int listSize = tagList.size();
                this.currentTagListSize = listSize;
                if (listSize <= DEFAULT_TAG_COUNTS) {
                    if (i < listSize) {
                        tagTV.setVisibility(View.VISIBLE);
                        tagTV.setText(tagList.get(i).labelName);
                        tagTV.setTag("" +tagList.get(i).labelId);
                    } else {
                        tagTV.setVisibility(View.GONE);
                    }
                } else {
                    if (i < DEFAULT_TAG_COUNTS) {
                        tagTV.setVisibility(View.VISIBLE);
                        tagTV.setText(tagList.get(i).labelName);
                        tagTV.setTag("" +tagList.get(i).labelId);
                    } else if (i < listSize) {
                        tagTV.setVisibility(View.GONE);
                        tagTV.setText(tagList.get(i).labelName);
                        tagTV.setTag("" + tagList.get(i).labelId);
                    } else if (i == getChildCount() - 1 ) {//更多
                        tagTV.setVisibility(View.VISIBLE);
                        isShow = false;
                        tagTV.setText(getContext().getString(R.string.more));
                    } else {
                        tagTV.setVisibility(View.GONE);
                    }
                }
            }
            i++;
        }
    }

    public TextView getTagNewView() {
        TextView tagTV = new TextView(getContext());
        tagTV.setPadding(UIUtils.dip2px(24), UIUtils.dip2px(5), UIUtils.dip2px(24), UIUtils.dip2px(6));
        return tagTV;
    }
}