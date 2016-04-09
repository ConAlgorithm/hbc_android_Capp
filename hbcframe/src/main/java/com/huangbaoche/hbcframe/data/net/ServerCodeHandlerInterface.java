package com.huangbaoche.hbcframe.data.net;

import android.app.Activity;

import com.huangbaoche.hbcframe.data.request.BaseRequest;

/**
 * Created by admin on 2016/3/26.
 */
public  interface ServerCodeHandlerInterface  {

    boolean handleServerCode( Activity mContext,String content,  int state,  BaseRequest request, HttpRequestListener listener) ;

}
