package com.huangbaoche.hbcframe.data.net;


import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLHandshakeException;

public class HttpRequestUtils {

    public static Callback.Cancelable request(final BaseRequest request, final HttpRequestListener listener){

        Callback.Cancelable cancelable = x.http().request(request.getHttpMethod(), request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("onSuccess result=" + result);
                try {
                    ImplParser parser = request.getParser();
                    if(parser==null) parser= new DefaultParser();
                    Object data = parser.parse(String.class, String.class, result);
                    request.setData(data);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    listener.onDataRequestError(handleException(throwable), request);
                    return;
                }
                listener.onDataRequestSucceed(request);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError="+ex.toString());
                listener.onDataRequestError(handleException(ex),request);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled="+cex.toString());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished=");
            }
        });
        return cancelable;
    }

    public static ExceptionInfo handleException(Throwable error) {
        ExceptionInfo result = null;
        if (error.getCause() instanceof SocketTimeoutException) {
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_NET_TIMEOUT, null);
        } else if (error.getCause() instanceof ConnectTimeoutException) {
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_NET_TIMEOUT, null);
        } else if (error.getCause() instanceof MalformedURLException) {
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_URL, null);
        } else if (error.getCause() instanceof SocketException) {
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_NET, null);
        } else if (error.getCause() instanceof SSLHandshakeException) {
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_SSL, null);
        } else if(error instanceof  ServerException){
            ServerException serverException = (ServerException)error;
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_SERVER, serverException);
//        } else if (error.getCode() == 404) {
//            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_NET_NOTFOUND, null);
        }
        return result;
    }

    public static class DefaultParser extends ImplParser{
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            return obj;
        }
    }
}
