package com.hugboga.custom.utils.collection;

import android.app.Activity;
import android.content.Context;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.event.EventAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HONGBO on 2018/1/8 17:24.
 */

public abstract class BaseCollection implements ICollection, HttpRequestListener {

    protected WeakReference<Context> weakReference;
    Map<String, Boolean> oldCollections = new HashMap<>(); //查询收藏记录数据
    Map<String, Boolean> serverCollections = new HashMap<>(); //服务器端记录收藏数据
    Map<String, Boolean> localCollections = new HashMap<>(); //本地收藏的数据

    public BaseCollection(Context context) {
        weakReference = new WeakReference<>(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
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

    protected synchronized void checkDataLine() {
        if (localCollections != null) {
            Iterator<Map.Entry<String, Boolean>> entries = localCollections.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Boolean> entry = entries.next();
                updateDataLine(entry.getKey(), serverCollections.containsKey(entry.getKey()), entry.getValue());
            }
        }
    }

    /**
     * 更新线路收藏数据
     *
     * @param targetNo
     * @param isConnected
     * @param value
     */
    abstract void updateDataLine(String targetNo, boolean isConnected, Boolean value);

    /**
     * 把list数据转换成map数据记录
     *
     * @param data
     * @return
     */
    protected Map<String, Boolean> buildMap(ArrayList<String> data) {
        Map<String, Boolean> result = new HashMap<>();
        for (String number : data) {
            result.put(number, true);
        }
        return result;
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                queryFavorite();
                break;
            case CLICK_USER_LOOUT:
                //退出后清空数据，停止同步
                oldCollections.clear();
                serverCollections.clear();
                localCollections.clear();
                break;
            case LINE_UPDATE_COLLECT:
                break;
        }
    }

}
