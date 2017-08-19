package com.huangbaoche.hbcframe.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.huangbaoche.hbcframe.HbcConfig;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public String source;//评价来源
    public int type;

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

    public static String getPhotoFileName(String picUrl) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String end = ".jpg";
        if(picUrl.endsWith(".jpg") || picUrl.endsWith("jpeg")){
            end = ".jpg";
        }else if(picUrl.endsWith(".png")){
            end = ".png";
        }

        return dateFormat.format(date) + end;
    }
    /**
     * 微信分享
     *@param type (1分享到微信好友,2分享到微信朋友圈)
     * @param picUrl
     * @param title
     * @param content
     * @param goUrl
     */
    public void share(final int type, final String picUrl, final String title, final String content, final String goUrl) {
        MLog.e("cache type="+type+" bitmap="+picUrl+" title="+title+" content="+content+" goUrl="+goUrl);
        if (isInstall(true)) {

            if(TextUtils.isEmpty(picUrl)){
                return;
            }
            String picName = picUrl.substring(picUrl.lastIndexOf("/")+1,picUrl.length());
            String preUrl = picUrl.substring(0,picUrl.lastIndexOf("/"));
            String smallPic = picUrl;//preUrl + "/s_"+ picName;
            MLog.e(preUrl+"cache picName==="+picName);
            MLog.e("cache smallPic==="+smallPic);
            Uri downloadUri = Uri.parse(smallPic);
            final Uri destinationUri = Uri.parse(mContext.getExternalCacheDir().toString() + getPhotoFileName(picUrl));
            DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                    .addCustomHeader("Auth-Token", "YourTokenApiKey")
                    .setRetryPolicy(new DefaultRetryPolicy())
                    .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                    .setDownloadListener(new DownloadStatusListener() {
                        @Override
                        public void onDownloadComplete(int id) {
                            MLog.e("onSuccess=====1111==c");
                            try {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 2;
                                Bitmap bitmap = createBitmapThumbnail(BitmapFactory.decodeFile(destinationUri.getPath(), options));
                                share(type, bitmap, title, content, goUrl);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                            Bitmap bitmap = null;
                            share(type, bitmap, title, content, goUrl);
                        }

                        @Override
                        public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress){


                        }
                    });
            ThinDownloadManager downloadManager;
            downloadManager = new ThinDownloadManager();
            downloadManager.add(downloadRequest);
//            x.image().loadFile(picUrl, null, new Callback.CacheCallback<File>() {
//                @Override
//                public boolean onCache(File result) {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 2;
//                    Bitmap bitmap= BitmapFactory.decodeFile(result.getAbsolutePath(), options);
//
//                    share(type,bitmap,title,content,goUrl);
//                    return true;
//                }
//
//                @Override
//                public void onSuccess(File result) {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 2;
//                    Bitmap bitmap= BitmapFactory.decodeFile(result.getAbsolutePath(), options);
//                    share(type,bitmap,title,content,goUrl);
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//                    if(mContext!=null)
//                    Toast.makeText(mContext,"图片加载失败",Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//
//                }
//
//                @Override
//                public void onFinished() {
//
//                }
//
//                });

        }else{
            ToastUtils.showToast(mContext, "未安装微信");
        }
    }

    public Bitmap createBitmapThumbnail(Bitmap bitMap) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = 99;
        int newHeight = 99;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
        return newBitMap;
    }


    public void share(final int type, final Bitmap bitmap, final String title, final String content, final String goUrl) {
        if (isInstall(true)) {
            this.type = type;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = goUrl;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title;
            msg.description = content;
            if (bitmap != null) {
                msg.setThumbImage(bitmap);
            }
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = type == TYPE_SESSION ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
            iwxapi.sendReq(req);
        }
    }

    public void share(final int type, final int resID, final String title, final String content, final String goUrl) {
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), resID);
        share(type, thumb, title, content, goUrl);
    }

}
