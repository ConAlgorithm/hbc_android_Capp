package com.huangbaoche.hbcframe.data.net;


import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.NetWork;
import com.huangbaoche.hbcframe.widget.DialogUtilInterface;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.app.RequestInterceptListener;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;


/**
 * 请求类
 */
public class HttpRequestUtils {


    public static Callback.Cancelable request(Context mContext,final BaseRequest request, final HttpRequestListener listener) {
        return request(mContext,request,listener,(HttpRequestOption)null);
    }
    public static Callback.Cancelable request(Context mContext,final BaseRequest request, final HttpRequestListener listener,boolean needShowLoading) {
        HttpRequestOption option =  new HttpRequestOption();
        option.needShowLoading = needShowLoading;
        return request(mContext,request,listener,option);
    }
    public static Callback.Cancelable request(Context mContext,final BaseRequest request, final HttpRequestListener listener,View btn) {
        HttpRequestOption option =  new HttpRequestOption();
        option.btn = btn;
        return request(mContext,request,listener,option);
    }


    /**
     * 请求
     * @param mContext
     * @param request
     * @param listener
     * @param option
     * @return
     */
    public static Callback.Cancelable request(final Context mContext,final BaseRequest request, final HttpRequestListener listener,HttpRequestOption option){
        if(option==null)option=new HttpRequestOption();
        option.setBtnEnabled(false);//设置按钮不可用
        if (!NetWork.isNetworkAvailable(mContext)) {//无网络直接报错
            ExceptionInfo result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_NET_UNAVAILABLE, null);
            if (listener != null)
                listener.onDataRequestError(result, request);
            option.setBtnEnabled(true);
            return null;
        }
        final DialogUtilInterface dialogUtil = getDialogUtil(mContext);
        if(mContext instanceof Activity) {
            final HttpRequestOption finalOption1 = option;
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialogUtil != null&& finalOption1.needShowLoading) dialogUtil.showLoadingDialog();
                }
            });
        }

        if (!checkAccessKey(mContext)){//Accesskey不能用,请求AccessKey ,回来继续请求上一个请求
            requestAccessKey(mContext,request,listener,option);
            return null;
        }
        final HttpRequestOption finalOption = option;
        Callback.Cancelable cancelable = x.http().request(request.getHttpMethod(), request, new CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {//请求成功
//                MLog.e(request.getClass().getSimpleName()+" onSuccess result=" + result);
                MLog.log(MLog.LogLevel.DEBUG, request.getClass().getSimpleName() + " onSuccess result=" + result);
                try {
                    if ("{\"status\":200}".equals(result)) {
                        return;
                    }
                    ImplParser parser = request.getParser();
                    if(parser==null) parser= new DefaultParser();//默认解析器
                    Object data = parser.parse(String.class, String.class, result);
                    request.setData(data);//设置数据
                } catch (Throwable throwable) {//内部的错误直接抛到错误回调方法内
                    listener.onDataRequestError(handleException(throwable), request);
                    return;
                }
                listener.onDataRequestSucceed(request);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MLog.e(request.getClass().getSimpleName()+" onError",ex);
                listener.onDataRequestError(handleException(ex), request);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                MLog.e(request.getClass().getSimpleName()+" onCancelled="+cex.toString());
                listener.onDataRequestCancel(request);
            }

            @Override
            public void onFinished() {
                finalOption.setBtnEnabled(true);
                if(dialogUtil !=null){
                    MLog.e("onFinished = "+request.getUrl());
                    if (dialogUtil != null&& finalOption.needShowLoading)
                        dialogUtil.dismissLoadingDialog();
                }
            }

            @Override
            public void afterRequest(UriRequest urlRequest) throws Throwable {
                super.afterRequest(urlRequest);
                if (request != null && urlRequest != null) {
                    request.responseHeaders = urlRequest.getResponseHeaders();
                }
            }

            @Override
            public void beforeRequest(UriRequest urlRequest) throws Throwable {
                super.beforeRequest(urlRequest);
            }

        });
        return cancelable;
    }

    public static class CommonCallback<String> implements Callback.CommonCallback<String>, RequestInterceptListener{

        @Override
        public void onSuccess(String result) {

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {

        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }

        @Override
        public void beforeRequest(UriRequest request) throws Throwable {

        }

        @Override
        public void afterRequest(UriRequest request) throws Throwable {

        }
    }

    /**
     * 通过反射拿到 dialog 实例
     * @param mContext
     * @return
     */
    public static DialogUtilInterface getDialogUtil(Context mContext){
        DialogUtilInterface dialogUtil = null;
        if(mContext instanceof Activity) {
            try {
                if(HbcConfig.dialogUtil!=null) {
                    Method method = HbcConfig.dialogUtil.getMethod("getInstance", Activity.class);
                    dialogUtil = (DialogUtilInterface) method.invoke(null, mContext);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dialogUtil;
    }

    /**
     * 处理错误
     * @param error
     * @return 错误信息
     */

    public static ExceptionInfo handleException(Throwable error) {
        ExceptionInfo result = null;
        if (error instanceof SocketTimeoutException) {
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_NET_TIMEOUT, null);
        } else if (error instanceof ConnectTimeoutException) {
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_NET_TIMEOUT, null);
        } else if (error instanceof MalformedURLException) {
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_URL, null);
        } else if (error instanceof SocketException) {
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_NET, null);
        } else if (error instanceof SSLHandshakeException) {
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_SSL, null);
        }else if(error instanceof UnknownHostException){
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_NET_NOTFOUND, null);
        } else if(error instanceof  ServerException){
            ServerException serverException = (ServerException)error;
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_SERVER, serverException);
        }else{
            result = new ExceptionInfo(ExceptionErrorCode.ERROR_CODE_OTHER, null);
            if (error instanceof HttpException) {
                result.setErrorCode(((HttpException)error).getErrorCode());

            }
        }
        return result;
    }

    /**
     * 检测Accesskey
     * @param mContext
     * @return
     */
    private static boolean checkAccessKey(Context mContext) {
        String accessKey = UserSession.getUser().getAccessKey(mContext);
        if (accessKey == null) {
            UserSession.getUser().setAccessKey(mContext, "");
            return false;
        }
        return true;
    }

    /**
     * 请求AccessKey
     * @param mContext
     * @param baseRequest 上一个请求
     * @param listener 上一个回调
     * @param option 上一个请求的配置
     */
    private static void requestAccessKey(final Context mContext,final BaseRequest baseRequest,final HttpRequestListener listener, final HttpRequestOption option){
        try {
            //通过反射实例化AccessKey
            Constructor constructor = HbcConfig.accessKeyRequest.getDeclaredConstructor(Context.class);
            constructor.setAccessible(true);
            final BaseRequest accessKeyRequest = (BaseRequest) constructor.newInstance(mContext);
            HttpRequestOption accessOption = option.clone();
            accessOption.needShowLoading =false;
            request(mContext, accessKeyRequest, new HttpRequestListener() {
                @Override
                public void onDataRequestSucceed(BaseRequest request) {
                    UserSession.getUser().setAccessKey(mContext, (String) request.getData());
                    String accessKey = UserSession.getUser().getAccessKey(mContext);
                    MLog.e("accessKey =" + accessKey);
                    request(mContext, baseRequest, listener, option);
                }

                @Override
                public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                    UserSession.getUser().setAccessKey(mContext, null);
                    listener.onDataRequestError(errorInfo, request);
                }

                @Override
                public void onDataRequestCancel(BaseRequest request) {
                            listener.onDataRequestCancel(request);
                        }
            },accessOption);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("AccessKeyRequest is not allow null");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 空的解析
     */
    public static class DefaultParser extends ImplParser{
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            return obj;
        }
    }
}
