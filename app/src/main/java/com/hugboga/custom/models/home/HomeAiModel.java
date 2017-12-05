package com.hugboga.custom.models.home;

import android.widget.LinearLayout;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.home.HomeAIView;
import com.hugboga.custom.widget.home.HomeBannerView;

/**
 * Created by qingcha on 17/11/23.
 */

public class HomeAiModel extends EpoxyModel<LinearLayout> {

    public LinearLayout itemView;
    public HomeAIView homeAIView;

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_home_ai;
    }

    @Override
    public void bind(LinearLayout view) {
        super.bind(view);
        this.itemView = view;
        homeAIView = (HomeAIView) view.findViewById(R.id.home_ai_view);
    }
}
