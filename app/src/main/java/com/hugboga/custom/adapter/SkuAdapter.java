package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.View;

import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.fragment.FgSkuList;
import com.hugboga.custom.widget.SkuListItem;

/**
 * Created by admin on 2016/3/3.
 */
public class SkuAdapter extends HbcRecyclerBaseAdapter<SkuItemBean> {

    private FgSkuList fragment;

    public SkuAdapter(Context context, FgSkuList _fragment) {
        super(context);
        this.fragment = _fragment;
    }

    @Override
    protected View getItemView(int position) {
        return new SkuListItem(getContext());
    }
}
