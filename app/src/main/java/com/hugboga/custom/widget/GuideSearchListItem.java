package com.hugboga.custom.widget;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.SearchUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/21.
 */

public class GuideSearchListItem extends LinearLayout implements HbcViewBehavior {

    @Bind(R.id.search_guide_name)
    TextView name;
    @Bind(R.id.search_guide_location)
    TextView location;
    @Bind(R.id.label_search_guide)
    TextView label;
    Context context;
    String keyword;
    public GuideSearchListItem(Context context) {
        this(context,null);
        this.context = context;
    }

    public GuideSearchListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.guide_search_item, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {

        SpannableString tempName = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),"我的大家庭","大");
        SpannableString tempLocation = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),"大阪-大家庭","大");
        SpannableString tempLabel = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),"大货。大爷。大妈","大");

        name.setText(tempName);
        location.setText(tempLocation);
        label.setText(tempLabel);
    }
}
