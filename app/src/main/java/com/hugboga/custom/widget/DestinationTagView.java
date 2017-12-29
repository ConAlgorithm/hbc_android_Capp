package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.DestinationListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.DestinationTabItemBean;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DestinationTagView extends LinearLayout {

    @BindView(R.id.view_destination_tag_title_tv)
    TextView titleTV;
    @BindView(R.id.view_destination_tag_group)
    TagGroup tagGroupView;

    List<DestinationTabItemBean.TagItemBean> tagList;

    public DestinationTagView(Context context) {
        this(context, null);
    }

    public DestinationTagView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_destination_tag, this);
        ButterKnife.bind(this);
    }

    public void setData(final DestinationTabItemBean.TagBean tagBean) {
        if (tagBean == null) {
            setVisibility(View.GONE);
            return;
        }
        titleTV.setText(tagBean.tagGroupName);
        tagList = tagBean.tagList;
        int size = tagList.size();
        for (int i = 0; i < size; i++) {
            tagGroupView.addTag(getNewTagView(tagList.get(i).tagName));
        }
        tagGroupView.setOnTagItemClickListener(new TagGroup.OnTagItemClickListener() {
            @Override
            public void onTagClick(View view, int position) {
                DestinationTabItemBean.TagItemBean tagItemBean = tagList.get(position);
                Intent intent = new Intent(getContext(), DestinationListActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, tagItemBean);
                intent.putExtra(Constants.PARAMS_SOURCE, "目的地");
                getContext().startActivity(intent);
                //埋点-目的地Tab页标签点击
                SensorsUtils.setSensorsClickTagTab(tagBean.tagGroupName, tagItemBean.tagName);
            }
        });
    }

    private TextView getNewTagView(String label) {
        TextView tagTV = new TextView(getContext());
        tagTV.setPadding(UIUtils.dip2px(10), UIUtils.dip2px(6), UIUtils.dip2px(10), UIUtils.dip2px(6));
        tagTV.setTextSize(12);
        tagTV.setTextColor(getContext().getResources().getColor(R.color.default_black));
        tagTV.setText(!TextUtils.isEmpty(label) ? label.trim() : "");
        tagTV.setBackgroundResource(R.drawable.shape_rounded_gray);
        return tagTV;
    }
}
