package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.models.CityListModel;

import java.util.List;

/**
 * Ai adapter
 * Created by HONGBO on 2017/12/5 16:25.
 */

public class AiResultAdapter extends EpoxyAdapter {

    private Context mContext;

    public AiResultAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addGoods(List<DestinationGoodsVo> data) {
        if (data == null) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            DestinationGoodsVo vo = data.get(i);
            CityListModel model = new CityListModel(mContext, vo);
            addModel(model);
        }
    }
}
