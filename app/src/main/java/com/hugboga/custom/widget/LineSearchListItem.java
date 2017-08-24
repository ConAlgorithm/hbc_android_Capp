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

public class LineSearchListItem extends LinearLayout implements HbcViewBehavior {
    @Bind(R.id.title_line_search)
    TextView title;
    @Bind(R.id.search_line_location)
    TextView location;
    Context context;
    public LineSearchListItem(Context context) {
        this(context,null);
        this.context = context;
    }

    public LineSearchListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.line_search_item, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        SpannableString temptitle = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),"我的大家大庭","大");
        SpannableString templocation = SearchUtils.matcherSearchText(context,getResources().getColor(R.color.all_bg_yellow),"大阪=日大本","大");

        title.setText(temptitle);
        location.setText(templocation);
    }
}
