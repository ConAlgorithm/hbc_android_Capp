package com.huangbaoche.hbcframe.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.huangbaoche.hbcframe.HbcConfig;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.net.URLEncoder;

/**
 * 微信分享相关操作
 * Created by ZHZEPHI on 2016/1/26.
 */
public class WXShareUtils {
    public static final int TYPE_SESSION = 1;//好友
    public static final int TYPE_TIMELINE = 2;//朋友圈


    Context mContext;
    private static WXShareUtils wxShareUtils;
    private static IWXAPI iwxapi; //微信分享

    private WXShareUtils(Context mContext) {
        this.mContext = mContext;
        /*
         * 微信分享初始化
		 */
        iwxapi = WXAPIFactory.createWXAPI(mContext, HbcConfig.WX_APP_ID);
        iwxapi.registerApp(HbcConfig.WX_APP_ID);
    }

    public static WXShareUtils getInstance(Context context) {
        if (wxShareUtils == null) {
            wxShareUtils = new WXShareUtils(context);
        }
        return wxShareUtils;
    }

    /**
     * 是否安装微信
     *
     * @return
     */
    public boolean isInstall(boolean isShow) {
        if (!iwxapi.isWXAppInstalled()) {
            MLog.e("手机未安装微信");
            if(isShow)
            new AlertDialog.Builder(mContext).setTitle("手机未安装微信").setPositiveButton("知道了", null).show();
            return false;
        }
        if (!iwxapi.isWXAppSupportAPI()) {
            MLog.e("微信版本太低");
            if(isShow)
            new AlertDialog.Builder(mContext).setTitle("微信版本太低").setPositiveButton("知道了", null).show();
            return false;
        }
        return true;
    }

    /**
     * 微信分享
     *@param type (0分享到微信好友,1分享到微信朋友圈)
     * @param picUrl
     * @param title
     * @param content
     * @param goUrl
     */
    public void share(final int type, final String picUrl, final String title, final String content, final String goUrl) {
        MLog.e("cache type="+type+" bitmap="+picUrl+" title="+title+" content="+content+" goUrl="+goUrl);
        if (isInstall(true)) {
            x.image().loadFile(picUrl, null, new Callback.CacheCallback<File>() {
                @Override
                public boolean onCache(File result) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bitmap= BitmapFactory.decodeFile(result.getAbsolutePath(), options);

                    share(type,bitmap,title,content,goUrl);
                    return true;
                }

                @Override
                public void onSuccess(File result) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bitmap= BitmapFactory.decodeFile(result.getAbsolutePath(), options);
                    share(type,bitmap,title,content,goUrl);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if(mContext!=null)
                    Toast.makeText(mContext,"图片加载失败",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }

                });

        }else{
            Toast.makeText(mContext,"未安装微信",Toast.LENGTH_LONG).show();
        }
    }


    public void share(final int type, final Bitmap bitmap, final String title, final String content, final String goUrl) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = goUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;
        msg.setThumbImage(bitmap);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = type == TYPE_SESSION ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }

}
