package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class DesTabModel extends EpoxyModelWithHolder {
    @Override
    protected EpoxyHolder createNewHolder() {
        return null;
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
    }

    @Override
    protected int getDefaultLayout() {
        return 0;
    }
}
