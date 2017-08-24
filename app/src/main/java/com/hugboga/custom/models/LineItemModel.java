package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.LineSearchListItem;

/**
 * Created by zhangqiang on 17/8/24.
 */

public class LineItemModel extends EpoxyModel<LineSearchListItem> {

        @Override
        protected int getDefaultLayout() {
            return R.layout.line_search_item_layout;
        }

        @Override
        public void bind(LineSearchListItem view) {
            super.bind(view);
            view.update(null);
        }


    }

