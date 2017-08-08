package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/5.
 */

public class HomeBottomBannerModel extends EpoxyModelWithHolder {
    Context context;
    @Override
    protected EpoxyHolder createNewHolder() {
        return new HomeBottomBannerHolder();
    }
    public HomeBottomBannerModel(Context context){
        this.context = context;
    }
    @Override
    protected int getDefaultLayout() {
        return R.layout.home_bottom_banner;
    }

    static class HomeBottomBannerHolder extends EpoxyHolder{
        View itemView;
        @Bind(R.id.home_bottom_banner_img)
        ImageView img;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        init(holder);
    }

    private void init(EpoxyHolder holder){

        ((HomeBottomBannerHolder)holder).img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TravelFundActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                context.startActivity(intent);
            }
        });
    }

    public String getEventSource() {
        return "首页";
    }
}
