package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private final static int DEFAULT_TAG_COUNTS = 4;
    private final static String MORE_BTN_TAG_EVALUATE= "more_tags_evaluate";
    private final static String MORE_BTN_TAG_SHOW = "more_tags_show";

    private EvaluateTagBean tagBean = null;
    private int currentTagListSize;
    private boolean isShow = false;
    private ArrayList<String> requestTagIds;
    private boolean tagEnabled = true;
    private int tagWidth;
    private int tagHight;

    TextView lineComment;
    public EvaluateTagGroup(Context context) {
        super(context);
    }

    public EvaluateTagGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTagItemClickListener(this);
        setTagEnabled(true);
        requestTagIds = new ArrayList<String>();
        final int paddingLeft = UIUtils.dip2px(40);
        final int paddingRight = UIUtils.dip2px(40);
        tagWidth = (UIUtils.getScreenWidth() - paddingLeft - paddingRight - UIUtils.dip2px(15)) / 2;
        tagHight = UIUtils.dip2px(22);
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

    public void setLineBelow(TextView lineComment){
        this.lineComment = lineComment;
    }
    public void setPickDown(String viewTag, int size) {
        isShow = !isShow;
        for (int i = DEFAULT_TAG_COUNTS; i < size; i++) {
            getChildAt(i).setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
        //收起,就不展示more了
        if(isShow){
            findViewWithTag(viewTag).setVisibility(GONE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lineComment.getLayoutParams();
            lp.setMargins(0,UIUtils.dip2px(20),0,0);
            lineComment.setLayoutParams(lp);
        }
        //((TextView)findViewWithTag(viewTag)).setText(getContext().getString(isShow ? R.string.pick_up : R.string.more));
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
            viewList.add(getMoreTag(MORE_BTN_TAG_EVALUATE));
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
            tagTV.setSelected(false);
            if (i + 1 > DEFAULT_TAG_COUNTS) {
                tagTV.setVisibility(View.GONE);
            }
            viewList.add(tagTV);
        }

        if (labelsSize > DEFAULT_TAG_COUNTS) {
            viewList.add(getMoreTag(MORE_BTN_TAG_SHOW));
        }
        this.setTags(viewList);
    }

    public View getMoreTag(String tag){

        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(20, 20);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.setMargins(0,UIUtils.dip2px(5),0,0);
        linearLayout.setLayoutParams(layoutParams);

        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.mipmap.evaluate_down);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams imageLayout=new LinearLayout.LayoutParams(15, 10);
        imageView.setLayoutParams(imageLayout);

        linearLayout.addView(imageView);

        LinearLayout parentLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams parentLayoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parentLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        parentLayout.setLayoutParams(parentLayoutParams);
        parentLayout.addView(linearLayout);
        parentLayout.setTag(tag);
        parentLayout.setGravity(Gravity.CENTER);
        return parentLayout;
    }

    public void setLevelChanged(int level) {
        if (getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }
        ArrayList<EvaluateTagBean.EvaluateTag> tagList = tagBean.getCurrentList(level);
        int i = 0;
        int count = getChildCount();
        while (i < getChildCount()) {
            int listSize = tagList.size();
            /*if(listSize > DEFAULT_TAG_COUNTS){
                if (i == getChildCount() - 1 ) {//更多
                    getChildAt(i).setVisibility(View.VISIBLE);
                    isShow = false;
                    i++;
                    continue;
                }
            }*/
            final View tagTV = getChildAt(i);
            if (tagList == null) {
                tagTV.setVisibility(View.GONE);
            } else {
                tagTV.setSelected(false);
                this.currentTagListSize = listSize;
                if (listSize <= DEFAULT_TAG_COUNTS) {
                    if (i < listSize) {
                        tagTV.setVisibility(View.VISIBLE);
                        ((TextView)tagTV).setText(tagList.get(i).labelName);
                        tagTV.setTag("" +tagList.get(i).labelId);
                    } else {
                        tagTV.setVisibility(View.GONE);
                    }
                } else {
                    if (i < DEFAULT_TAG_COUNTS) {
                        tagTV.setVisibility(View.VISIBLE);
                        ((TextView)tagTV).setText(tagList.get(i).labelName);
                        tagTV.setTag("" +tagList.get(i).labelId);
                    } else if (i < listSize) {
                        tagTV.setVisibility(View.GONE);
                        ((TextView)tagTV).setText(tagList.get(i).labelName);
                        tagTV.setTag("" + tagList.get(i).labelId);
                    } else if (i == getChildCount() - 1 ) {//更多
                        tagTV.setVisibility(View.VISIBLE);
                        isShow = false;
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
        //tagTV.setPadding(UIUtils.dip2px(24), UIUtils.dip2px(5), UIUtils.dip2px(24), UIUtils.dip2px(6));
        tagTV.setGravity(Gravity.CENTER);
        tagTV.setTextColor(0xFF929292);
        tagTV.setTextSize(12);
        tagTV.setMaxLines(1);
        tagTV.setEllipsize(TextUtils.TruncateAt.END);
        tagTV.setLayoutParams(new LinearLayout.LayoutParams(tagWidth, tagHight));
        return tagTV;
    }
}