package com.hugboga.custom.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.widget.city.CityWhatView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 目的地玩法想怎么玩
 * Created by HONGBO on 2017/11/29 16:49.
 */

public class CityWhatVH extends RecyclerView.ViewHolder {

    @BindView(R.id.city_item_what_do_layout)
    LinearLayout city_item_what_do_layout; //下单入口

    Context mContext;

    public CityWhatVH(Context mContext, View itemView) {
        super(itemView);
        this.mContext = mContext;
        ButterKnife.bind(this, itemView);
    }

    public void init(List<ServiceConfigVo> dailyServiceConfig) {
        resetWhatDoLayout(dailyServiceConfig);
        //TODO 想怎么玩配置
    }

    /**
     * 下单入口配置
     *
     * @param dailyServiceConfig
     */
    private void resetWhatDoLayout(List<ServiceConfigVo> dailyServiceConfig) {
        city_item_what_do_layout.removeAllViews();
        for (ServiceConfigVo vo : dailyServiceConfig) {
            CityWhatView view = new CityWhatView(mContext);
            view.init(vo);
            city_item_what_do_layout.addView(view);
        }
    }
}
