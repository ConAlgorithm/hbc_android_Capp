package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.GuideSearchListItem;

/**
 * Created by zhangqiang on 17/8/24.
 */

public class GuideItemModel extends EpoxyModel<GuideSearchListItem> {

    @Override
    protected int getDefaultLayout() {
        return R.layout.guide_search_item_layout;
    }

    @Override
    public void bind(GuideSearchListItem view) {
        super.bind(view);
        view.update(null);
    }
}
