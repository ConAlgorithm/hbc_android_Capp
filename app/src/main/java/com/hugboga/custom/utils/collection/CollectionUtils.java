package com.hugboga.custom.utils.collection;

import android.app.Activity;
import android.content.Context;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.UserFavoriteLineList;
import com.hugboga.custom.data.request.FavoriteLinesaved;
import com.hugboga.custom.data.request.RequestCollectLineNo;
import com.hugboga.custom.data.request.RequestUncollectLinesNo;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.netease.nim.uikit.impl.NimUIKitImpl.getContext;

/**
 * 收藏内容处理
 * Created by HONGBO on 2017/12/29 18:43.
 */

public class CollectionUtils implements HttpRequestListener {

    private WeakReference<Context> weakReference;

    private static CollectionUtils instance;

    List<String> collectLines; //收藏的线路
    Map<String, Boolean> collectionLine = new HashMap<>(); //收藏的线路

    private CollectionUtils(Context context) {
        weakReference = new WeakReference<>(context);
    }

    public static CollectionUtils getIns(Context context) {
        if (instance == null) {
            synchronized (CollectionUtils.class) {
                if (instance == null) {
                    instance = new CollectionUtils(context);
                }
            }
        }
        return instance;
    }

    /**
     * 查询已收藏线路数据
     * TODO 重新查询司导收藏线路或者收藏司导需要在主界面进行触发
     */
    public void queryFavoriteLineList() {
        if (weakReference != null) {
            Context context = weakReference.get();
            if (UserEntity.getUser().isLogin(context)) {
                FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(context, UserEntity.getUser().getUserId(context));
                HttpRequestUtils.request(context, favoriteLinesaved, this, false);
            }
        }
    }

    private synchronized void checkDataLine() {
        if (collectionLine != null) {
            Iterator<Map.Entry<String, Boolean>> entries = collectionLine.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Boolean> entry = entries.next();
                updateDataLine(entry.getKey(), collectLines.contains(entry.getKey()), entry.getValue());
            }
        }
    }

    /**
     * 更新线路收藏数据
     *
     * @param goodsNo
     * @param isConnected
     */
    private void updateDataLine(String goodsNo, boolean isConnected, Boolean value) {
        if (isConnected && !value) {
            HttpRequestUtils.request(getContext(), new RequestUncollectLinesNo(getContext(), goodsNo), this, false);
        } else if (!isConnected && value) {
            HttpRequestUtils.request(getContext(), new RequestCollectLineNo(getContext(), goodsNo), this, false);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof FavoriteLinesaved) {
            //查询出已收藏线路信息
            if (request.getData() != null) {
                UserFavoriteLineList favoriteLine = (UserFavoriteLineList) request.getData();
                if (favoriteLine != null && favoriteLine.goodsNos != null) {
                    collectLines = favoriteLine.goodsNos;
                    resetFaviousLine(); //构建已收藏线路集合
                }
            }
        } else if (request instanceof RequestUncollectLinesNo) {
            //取消收藏提交数据成功
        } else if (request instanceof RequestCollectLineNo) {
            //收藏提交数据成功
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    private ErrorHandler errorHandler;

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (errorHandler == null && weakReference != null) {
            errorHandler = new ErrorHandler((Activity) weakReference.get(), this);
        }
        if (errorHandler != null) {
            errorHandler.onDataRequestError(errorInfo, request);
        }
    }

    /**
     * 是否已收藏该线路
     *
     * @param goodNo
     * @return
     */
    public boolean isCollectionLine(String goodNo) {
        return collectionLine.containsKey(goodNo);
    }

    /**
     * 移除收藏线路
     *
     * @param goodNo
     */
    public void changeCollectionLine(String goodNo, boolean isCollection) {
        if (isCollection) {
            collectionLine.put(goodNo, true);
        } else {
            collectionLine.put(goodNo, false);
        }
        checkDataLine(); //改变结果之后检测同步数据到服务器
    }

    /**
     * 设置是否收藏数据，数据来源为查询最新数据所得
     */
    private void resetFaviousLine() {
        collectionLine.clear();
        if (collectLines != null && collectLines.size() > 0) {
            for (String goodsNo : collectLines) {
                collectionLine.put(goodsNo, true);
            }
        }
    }
}
