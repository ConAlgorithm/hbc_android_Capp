package com.hugboga.custom.utils.collection;

import android.content.Context;
import android.os.Handler;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.UserFavoriteLineList;
import com.hugboga.custom.data.request.FavoriteLinesaved;
import com.hugboga.custom.data.request.RequestCollectLineNo;
import com.hugboga.custom.data.request.RequestUncollectLinesNo;

import org.greenrobot.eventbus.EventBus;

import static com.netease.nim.uikit.impl.NimUIKitImpl.getContext;

/**
 * 收藏内容处理
 * Created by HONGBO on 2017/12/29 18:43.
 */

public class CollectionLine extends BaseCollection {

    public CollectionLine(Context context) {
        super(context);
    }

    @Override
    public void queryFavorite() {
        if (!EventBus.getDefault().isRegistered(CollectionLine.this)) {
            EventBus.getDefault().register(CollectionLine.this);
        }
        if (weakReference != null) {
            Context context = weakReference.get();
            if (UserEntity.getUser().isLogin(context)) {
                FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(context, UserEntity.getUser().getUserId(context));
                HttpRequestUtils.request(context, favoriteLinesaved, this, false);
            }
        }
    }

    @Override
    public boolean isCollection(String targetNo) {
        return collectionLine.containsKey(targetNo) ? collectionLine.get(targetNo) : false;
    }

    @Override
    public void changeCollectionLine(String targetNo, boolean isCollection) {
        if (isCollection) {
            collectionLine.put(targetNo, true);
        } else {
            collectionLine.put(targetNo, false);
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                checkDataLine(); //改变结果之后检测同步数据到服务器
            }
        });
    }

    @Override
    void updateDataLine(String targetNo, boolean isConnected, Boolean value) {
        if (weakReference == null) {
            return;
        }
        Context context = weakReference.get();
        if (context == null) {
            return;
        }
        if (!UserEntity.getUser().isLogin(context)) {
            return;
        }
        if (isConnected) {
            if (serverCollections.get(targetNo).equals(value)) {
                //如果服务端数据和本地数据相同，则不做同步处理
            } else {
                if (value) {
                    HttpRequestUtils.request(getContext(), new RequestCollectLineNo(getContext(), targetNo), this, false);
                } else {
                    HttpRequestUtils.request(getContext(), new RequestUncollectLinesNo(getContext(), targetNo), this, false);
                }
            }
        } else {
            if (value) {
                HttpRequestUtils.request(getContext(), new RequestCollectLineNo(getContext(), targetNo), this, false);
            }
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof FavoriteLinesaved) {
            //查询出已收藏线路信息
            if (request.getData() != null) {
                UserFavoriteLineList favoriteLine = (UserFavoriteLineList) request.getData();
                if (favoriteLine != null && favoriteLine.goodsNos != null) {
                    serverCollections = buildMap(favoriteLine.goodsNos); //记录为服务器端数据
                    collectionLine = buildMap(favoriteLine.goodsNos); //记录为本地数据
                }
            }
        } else if (request instanceof RequestUncollectLinesNo) {
            //取消收藏提交数据成功
            serverCollections.remove(((RequestUncollectLinesNo) request).getGoodsNo());
        } else if (request instanceof RequestCollectLineNo) {
            //收藏提交数据成功
            serverCollections.put(((RequestCollectLineNo) request).getGoodsNo(), true);
        }
    }
}
