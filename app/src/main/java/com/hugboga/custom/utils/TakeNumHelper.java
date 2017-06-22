package com.hugboga.custom.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestOption;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OssTokenBean;
import com.hugboga.custom.data.bean.OssTokenKeyBean;
import com.hugboga.custom.data.request.OssTokenRequest;
import com.hugboga.tools.HLog;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 上传文件取号器
 * Created by ZHZEPHI on 2016-12-21.
 */

public class TakeNumHelper {

    private OssTokenBean ossTokenBean; //当前上传环境参数
    private List<OssTokenKeyBean> keysList = Collections.synchronizedList(new LinkedList<OssTokenKeyBean>());
    private long getTime = 0; //记录最后一次获取key的时间
    private int current = 0; //重试获取key次数

    private static TakeNumHelper instance;

    private WeakReference<Context> weak;

    private TakeNumHelper() {
    }

    public static TakeNumHelper getInstance() {
        if (instance == null) {
            synchronized (TakeNumHelper.class) {
                if (instance == null) {
                    instance = new TakeNumHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 上次取号是否过期
     *
     * @return
     */
    private boolean isValidate() {
        long haveTime = System.currentTimeMillis() - getTime; //现在剩余的时间
        if (ossTokenBean != null) {
            double sumTime = ossTokenBean.getValidMinutes() * 60 * 1000 * 0.7;
            HLog.d("=======>已过Time:" + haveTime + "，剩余Time:" + sumTime);
            if (haveTime < sumTime) {
                return true;
            }
        }
        return false;
    }

    /*
    1.检查key是否过期（剩余有效期的70%）
    2.检查剩余是否有key
    3.有可用的key则直接使用
     */
    public void getKey(Context context, @NonNull KeyBackListener keyBackListener) {
        current = 0;
        this.weak = new WeakReference<>(context);
        if (keyBackListener != null) {
            if (!isValidate() || isHaveKey()) {
                getUploadToken(keyBackListener);
            } else {
                current = 0;
                keyBackListener.onKeyBack(ossTokenBean, keysList.get(0));
                keysList.remove(0);
            }
        }
    }

    private boolean isHaveKey() {
        return keysList.size() <= 0;
    }

    /**
     * 获取上传文件所需要环境
     */
    private void getUploadToken(final KeyBackListener keyBackListener) {
        if (current < 2 && weak != null && weak.get() != null) {
            //只允许重新获取一次
            final OssTokenRequest request = new OssTokenRequest(weak.get());
            HttpRequestOption option = new HttpRequestOption();
            option.needShowLoading = false;
            HttpRequestUtils.request(weak.get(), request, new HttpRequestListener(){

                @Override
                public void onDataRequestSucceed(BaseRequest request) {
                    if (request.getData() instanceof OssTokenBean) {
                        getTime = System.currentTimeMillis();
                        ossTokenBean = (OssTokenBean) request.getData();
                        keysList.clear();
                        keysList.addAll(ossTokenBean.getKeys());
                        getKey(weak.get(), keyBackListener);

                    }
                }

                @Override
                public void onDataRequestCancel(BaseRequest request) {
                    Log.d("aa","aa");
                }

                @Override
                public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                    keyBackListener.onFail(errorInfo.exception.getMessage());
                }
            },option);
        }
    }

    public interface KeyBackListener {
        void onKeyBack(OssTokenBean ossTokenBean, OssTokenKeyBean ossTokenKeyBean);

        void onFail(String message);
    }
}
