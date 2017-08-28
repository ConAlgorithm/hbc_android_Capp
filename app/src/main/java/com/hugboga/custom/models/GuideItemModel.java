package com.hugboga.custom.models;

import android.app.Activity;
import android.content.Context;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.SearchGuideBean;
import com.hugboga.custom.widget.GuideSearchListItem;

/**
 * Created by zhangqiang on 17/8/24.
 */

public class GuideItemModel extends EpoxyModel<GuideSearchListItem> {
    SearchGuideBean.GuideSearchItemBean guideSearchItemBean;
    Context context;
    String keyword;
    public GuideItemModel(Context context, SearchGuideBean.GuideSearchItemBean guideSearchItemBean, String keyword){
        this.context = context;
        this.keyword = keyword;
        this.guideSearchItemBean = guideSearchItemBean;
    }
    @Override
    protected int getDefaultLayout() {
        return R.layout.guide_search_item_layout;
    }

    @Override
    public void bind(GuideSearchListItem view) {
        super.bind(view);
        view.setkeyWord(keyword);
        view.setActivity((Activity) context);
        view.update(guideSearchItemBean);
    }
}
