package com.hugboga.custom.models.home;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.FakeAIActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.widget.home.HomeAIView;

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
        homeAIView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FakeAIActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, "首页");
                view.getContext().startActivity(intent);
            }
        });
    }
}
