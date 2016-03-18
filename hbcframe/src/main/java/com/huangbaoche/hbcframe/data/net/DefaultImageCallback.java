package com.huangbaoche.hbcframe.data.net;

import org.xutils.common.Callback;

/**
 * Created by admin on 2016/3/17.
 */
public abstract class DefaultImageCallback<ResultType> implements Callback.CommonCallback<ResultType> {

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
