package com.hugboga.custom.utils.collection;

import android.content.Context;

import com.huangbaoche.hbcframe.data.request.BaseRequest;

/**
 * Created by HONGBO on 2018/1/8 17:47.
 */

public class CollectionGuide extends BaseCollection {

    public CollectionGuide(Context context) {
        super(context);
    }

    @Override
    public void queryFavorite() {

    }

    @Override
    public boolean isCollection(String targetNo) {
        return false;
    }

    @Override
    public void changeCollectionLine(String targetNo, boolean isCollection) {

    }

    @Override
    void updateDataLine(String targetNo, boolean isConnected, Boolean value) {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {

    }
}
