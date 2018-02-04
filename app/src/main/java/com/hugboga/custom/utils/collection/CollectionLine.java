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
import com.hugboga.custom.statistic.sensors.SensorsUtils;

import static com.netease.nim.uikit.impl.NimUIKitImpl.getContext;

/**
 * 收藏线路处理
 * Created by HONGBO on 2017/12/29 18:43.
 */

public class CollectionLine extends BaseCollection {

    public CollectionLine(Context context) {
        super(context);
    }

    @Override
    public void queryFavorite() {
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
        if (!UserEntity.getUser().isLogin(getContext())) {
            return false;
        }
        return localCollections.containsKey(targetNo) ? localCollections.get(targetNo) : false;
    }

    @Override
    public void changeCollectionLine(String targetNo, boolean isCollection) {
        if (isCollection) {
            localCollections.put(targetNo, true);
            SensorsUtils.setSensorsFavorite("商品", targetNo, "");
        } else {
            localCollections.put(targetNo, false);
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
                    oldCollections = buildMap(favoriteLine.goodsNos); //记录查询收藏的数据
                    serverCollections = buildMap(favoriteLine.goodsNos); //记录为服务器端数据
                    localCollections = buildMap(favoriteLine.goodsNos); //记录为本地数据
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

    /**
     * 根据最新收藏数据识别收藏变化数
     *
     * @param targetNo
     * @return
     */
    public int showFavorsNum(String targetNo, int serviceFavorsNum) {
        if (!UserEntity.getUser().isLogin(getContext())) {
            return serviceFavorsNum;
        }
        if (oldCollections.containsKey(targetNo)) {
            if (localCollections.containsKey(targetNo)) {
                return localCollections.get(targetNo) ? serviceFavorsNum : serviceFavorsNum - 1;
            } else {
                return serviceFavorsNum - 1;
            }
        } else {
            if (localCollections.containsKey(targetNo)) {
                return localCollections.get(targetNo) ? serviceFavorsNum + 1 : serviceFavorsNum;
            } else {
                return serviceFavorsNum;
            }
        }
    }
}
