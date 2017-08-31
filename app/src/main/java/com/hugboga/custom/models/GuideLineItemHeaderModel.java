package com.hugboga.custom.models;

import android.content.Context;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.GuideLineItemHeaderView;

/**
 * Created by zhangqiang on 17/8/24.
 */

public class GuideLineItemHeaderModel extends EpoxyModel<GuideLineItemHeaderView> {

    int count;
    String title;
    String keyword;
    Context context;
    public GuideLineItemHeaderModel(Context context,int count, String title, String keyword){
        this.context = context;
        this.count = count;
        this.title = title;
        this.keyword = keyword;
    }
    @Override
    protected int getDefaultLayout() {
        return R.layout.search_guide_line_head;
    }

    @Override
    public void bind(GuideLineItemHeaderView guideLineItemHeaderView) {
        super.bind(guideLineItemHeaderView);
        guideLineItemHeaderView.setData(title,count);
        guideLineItemHeaderView.setContext(context);
        guideLineItemHeaderView.update(keyword);
    }

}
