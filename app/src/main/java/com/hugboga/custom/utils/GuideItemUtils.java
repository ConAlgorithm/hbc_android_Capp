package com.hugboga.custom.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.widget.TagGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/4/26.
 */
public class GuideItemUtils {

    public static void setTag(TagGroup tagGroup, List<String> skillLabelNames) {
        if (skillLabelNames == null || skillLabelNames.size() == 0) {
            tagGroup.setVisibility(View.GONE);
            return;
        }
        tagGroup.setVisibility(View.VISIBLE);
        final int labelsSize = skillLabelNames.size();
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
                viewList.add(getNewTagView(tagGroup.getContext(), tag));
            }
        }
        for (int j = labelsSize; j < tagGroup.getChildCount(); j++) {
            tagGroup.getChildAt(j).setVisibility(View.GONE);
        }
        tagGroup.setTags(viewList, tagGroup.getChildCount() <= 0);
    }

    private static LinearLayout getNewTagView(Context context, String label) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(R.mipmap.personal_label);
        linearLayout.addView(imageView);
        imageView.getLayoutParams().height = UIUtils.dip2px(12);
        imageView.getLayoutParams().width = UIUtils.dip2px(12);

        TextView tagTV = new TextView(context);
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
