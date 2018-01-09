package com.hugboga.custom.utils.collection;

import android.content.Context;
import android.os.Handler;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.UserFavoriteGuideListVo3;
import com.hugboga.custom.data.request.FavoriteGuideSaved;
import com.hugboga.custom.data.request.RequestCollectGuidesId;
import com.hugboga.custom.data.request.RequestUncollectGuidesId;

import static com.netease.nim.uikit.impl.NimUIKitImpl.getContext;

/**
 * 收藏司导处理
 * Created by HONGBO on 2018/1/8 17:47.
 */

public class CollectionGuide extends BaseCollection {

    public CollectionGuide(Context context) {
        super(context);
    }

    @Override
    public void queryFavorite() {
        if (weakReference != null) {
            Context context = weakReference.get();
            if (UserEntity.getUser().isLogin(context)) {
                FavoriteGuideSaved favoriteLinesaved = new FavoriteGuideSaved(context, UserEntity.getUser().getUserId(context), null);
                HttpRequestUtils.request(context, favoriteLinesaved, this, false);
            }
        }
    }

    @Override
    public boolean isCollection(String targetNo) {
        return localCollections.containsKey(targetNo) ? localCollections.get(targetNo) : false;
    }

    @Override
    public void changeCollectionLine(String targetNo, boolean isCollection) {
        if (isCollection) {
            localCollections.put(targetNo, true);
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
                    HttpRequestUtils.request(getContext(), new RequestCollectGuidesId(getContext(), targetNo), this, false);
                } else {
                    HttpRequestUtils.request(getContext(), new RequestUncollectGuidesId(getContext(), targetNo), this, false);
                }
            }
        } else {
            if (value) {
                HttpRequestUtils.request(getContext(), new RequestCollectGuidesId(getContext(), targetNo), this, false);
            }
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof FavoriteGuideSaved) {
            //查询出已收藏线路信息
            if (request.getData() != null) {
                UserFavoriteGuideListVo3 favoriteLine = (UserFavoriteGuideListVo3) request.getData();
                if (favoriteLine != null && favoriteLine.guides != null) {
                    serverCollections = buildMap(favoriteLine.guides); //记录为服务器端数据
                    localCollections = buildMap(favoriteLine.guides); //记录为本地数据
                }
            }
        } else if (request instanceof RequestUncollectGuidesId) {
            //取消收藏提交数据成功
            serverCollections.remove(((RequestUncollectGuidesId) request).getGuideId());
        } else if (request instanceof RequestCollectGuidesId) {
            //收藏提交数据成功
            serverCollections.put(((RequestCollectGuidesId) request).getGuideId(), true);
        }
    }
}
