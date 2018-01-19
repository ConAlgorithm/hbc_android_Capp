package com.hugboga.custom.models;

import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.FakeAIActivity;
import com.hugboga.custom.data.bean.ai.ServiceType;
import com.hugboga.custom.widget.ai.AiCardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqi on 2018/1/16.
 */

public class FakeAIItemCardModel extends EpoxyModel<RelativeLayout> {
    @BindView(R.id.scrollview)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
    private List<ServiceType> serviceTypes;
    public ArrayList<AiCardView> aiClickAgreementlist;

    @Override
    protected int getDefaultLayout() {
        return R.layout.fake_item_card;
    }

    @Override
    public void bind(RelativeLayout view) {
        super.bind(view);
        ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        aiClickAgreementlist = null;
        aiClickAgreementlist = new ArrayList<>();
        linearLayout.removeAllViews();
        if (serviceTypes != null && serviceTypes.size() != 0) {
            for (int i = 0; i < serviceTypes.size(); i++) {
                AiCardView aiCardView = new AiCardView(linearLayout.getContext());
                aiCardView.bindData(serviceTypes.get(i));
                linearLayout.addView(aiCardView);
                aiClickAgreementlist.add(aiCardView);
            }
        }
    }

    public void cardClickState(boolean b) {
        if (aiClickAgreementlist != null && aiClickAgreementlist.size() != 0)
            for (int i = 0; i < aiClickAgreementlist.size(); i++) {
                aiClickAgreementlist.get(i).clickState(b);
            }
    }

    public void bindData(List<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }
}
