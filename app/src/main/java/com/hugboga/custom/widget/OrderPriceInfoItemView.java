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
import com.hugboga.custom.data.bean.OrderPriceInfoBean;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/8/25.
 */
public class OrderPriceInfoItemView extends LinearLayout implements HbcViewBehavior{

    @BindView(R.id.order_price_info_item_title_tv)
    TextView titleTV;
    @BindView(R.id.order_price_info_item_taggroup)
    TagGroup tagGroup;

    public OrderPriceInfoItemView(Context context) {
        this(context, null);
    }

    public OrderPriceInfoItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_price_info_item, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        if (_data instanceof OrderPriceInfoBean) {
            OrderPriceInfoBean itemBean = (OrderPriceInfoBean) _data;
            titleTV.setText(itemBean.title);
            setTag(tagGroup, itemBean.labelList);
        }
    }

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
                viewList.add(getNewTagView(tagGroup.getContext(), tag, i % 2 == 0));
            }
        }
        for (int j = labelsSize; j < tagGroup.getChildCount(); j++) {
            tagGroup.getChildAt(j).setVisibility(View.GONE);
        }
        tagGroup.setTags(viewList, tagGroup.getChildCount() <= 0);
    }

    private static LinearLayout getNewTagView(Context context, String label, boolean isLeft) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setMinimumWidth(UIUtils.getScreenWidth() / 2 - UIUtils.dip2px(isLeft ? 27 : 67));
        linearLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(R.mipmap.charter_popover_include_icon);
        linearLayout.addView(imageView);
        imageView.getLayoutParams().height = UIUtils.dip2px(16);
        imageView.getLayoutParams().width = UIUtils.dip2px(16);

        TextView tagTV = new TextView(context);
        tagTV.setPadding(UIUtils.dip2px(2), 0, 0, UIUtils.dip2px(2));
        tagTV.setTextSize(14);
        tagTV.setTextColor(context.getResources().getColor(R.color.default_black));
        tagTV.setEnabled(false);
        tagTV.setText(label);
        tagTV.setTag("tagTV");
        linearLayout.addView(tagTV);
        return linearLayout;
    }

}
