package com.hugboga.custom.models;

import android.app.Activity;
import android.content.Context;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.SearchLineBean;
import com.hugboga.custom.widget.LineSearchListItem;

/**
 * Created by zhangqiang on 17/8/24.
 */

public class LineItemModel extends EpoxyModel<LineSearchListItem> {
    SearchLineBean.GoodsPublishStatusVo goodsPublishStatusVo;
    Context context;
    String keyword;

    public LineItemModel(Context context, SearchLineBean.GoodsPublishStatusVo goodsPublishStatusVo, String keyword) {
        this.context = context;
        this.goodsPublishStatusVo = goodsPublishStatusVo;
        this.keyword = keyword;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.line_search_item_layout;
    }

    @Override
    public void bind(LineSearchListItem view) {
        super.bind(view);
        if(keyword != null){
            view.setkeyWord(keyword);
        }
        view.setActivity((Activity) context);
        view.update(goodsPublishStatusVo);
    }


}

