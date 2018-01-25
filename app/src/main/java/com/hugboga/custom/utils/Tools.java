package com.hugboga.custom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.GlideCircleTransform;
import com.hugboga.custom.widget.GlideRoundTransform;

import java.io.File;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

/**
 * Created  on 16/4/25.
 */
public class Tools {

    public static void showImage(final ImageView imageView, final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Glide.with(MyApplication.getAppContext())
                        .load(transformImgUrl(imageView, url))
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }
    public static void showAdImage(ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(MyApplication.getAppContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }

    public static void showImageForHomePage(final ImageView imageView, String url, final int resId) {

        imageView.setImageResource(resId);
        Glide.with(MyApplication.getAppContext())
                .load(url)
                .centerCrop()
                .error(resId)
                .placeholder(resId)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        imageView.setImageResource(resId);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imageView.setBackgroundResource(0);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void showImage(final ImageView imageView,final String url, final int resId) {
        if (TextUtils.isEmpty(url)) {
//            imageView.setBackgroundResource(resId);
            imageView.setImageResource(resId);
            return;
        }
//        imageView.setBackgroundResource(resId);

        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Glide.with(MyApplication.getAppContext())
                        .load(transformImgUrl(imageView, url))
                        .centerCrop()
                        .error(resId)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        if (imageView != null && resId != 0) {
//                            imageView.setBackgroundResource(0);
                                imageView.setImageResource(resId);
//                        }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                imageView.setBackgroundResource(0);
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    //placeholder 显示有问题
    public static void showImageFitCenter(final ImageView imageView, final String url, final int resId) {
        if (TextUtils.isEmpty(url)) {
//            imageView.setBackgroundResource(resId);
            imageView.setImageResource(resId);
            return;
        }
//        imageView.setBackgroundResource(resId);
        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Glide.with(MyApplication.getAppContext())
                        .load(transformImgUrl(imageView, url))
                        .fitCenter()
                        .error(resId)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        if (imageView != null && resId != 0) {
//                            imageView.setBackgroundResource(0);
                                imageView.setImageResource(resId);
//                        }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                imageView.setBackgroundResource(0);
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }


    public static void showImageNotCenterCrop(final ImageView imageView, String url, final int resId) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
//        imageView.setBackgroundResource(resId);
        Glide.with(MyApplication.getAppContext())
                .load(url)
                .error(resId)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        if (imageView != null && resId != 0) {
//                            imageView.setBackgroundResource(0);
                        imageView.setImageResource(resId);
//                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imageView.setBackgroundResource(0);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)

                .into(imageView);
    }
    public static void showAdImageWithAnim(final Context context, final ImageView imageView, String url, final Animation animation) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource == null) {
                    return;
                }
                imageView.setImageBitmap(resource);
                imageView.startAnimation(animation);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {

            }
        });
    }
    public static void showImageHasPlaceHolder(final ImageView imageView, String url, final int placeHolderResId) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(MyApplication.getAppContext())
                .load(url)
                .centerCrop()
                .dontAnimate()
                .placeholder(placeHolderResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    public static void showGif(ImageView imageView, int resId) {
        Glide.with(MyApplication.getAppContext())
                .load(resId)
                .asGif()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public static void showGif(ImageView imageView, File file) {
        Glide.with(MyApplication.getAppContext())
                .load(file)
                .asGif()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public static void showRoundImage(ImageView imageView, String url, float radius) {
        Glide.with(MyApplication.getAppContext())
                .load(url)
                .transform(new GlideRoundTransform(MyApplication.getAppContext(), radius))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void showRoundImage(final ImageView imageView, String url, float radius, final int resId) {
        Glide.with(MyApplication.getAppContext())
                .load(url)
                .transform(new GlideRoundTransform(MyApplication.getAppContext(), radius))
                .error(resId)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        if (imageView != null && resId != 0) {
//                            imageView.setBackgroundResource(0);
                        imageView.setImageResource(resId);
//                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imageView.setBackgroundResource(0);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void showBlurryImage(final ImageView imageView, String url, final int resId, final int radius, final int sampling) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(MyApplication.getAppContext())
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource == null) {
                            return;
                        }
                        imageView.setImageBitmap(resource);
                        try {
                            Blurry.with(imageView.getContext())
                                    .radius(radius)
                                    .sampling(sampling)
                                    .async()
                                    .capture(imageView)
                                    .into(imageView);
                        }catch (Exception e) {
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        if (resId != 0) {
                            imageView.setImageResource(resId);
                        }
                    }
                });
    }

    @Deprecated
    public static void showImage(Context context, ImageView imageView, String url){
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void showCircleImage(Context context,ImageView imageView,String url){
        Glide.with(context).load(url).transform(new GlideCircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().into(imageView);
    }

    public static void showCircleImage(Context context,ImageView imageView,String url,int placeHolder){
        Glide.with(context).load(url).transform(new GlideCircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder)
                .centerCrop().into(imageView);
    }

    public static Bitmap getBitmap(Context context,String url){
        try {
            Bitmap myBitmap = Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .centerCrop()
                    .into(500, 500)
                    .get();
            return myBitmap;
        }catch (Exception e){
            return null;
        }
    }

    //人民币符号
    public static String getRMB(Context context){
//        char symbol = 165;
//        return String.valueOf(symbol);
        return context.getString(R.string.sign_rmb);
    }

    public static File downLoadImage(Context context,String url){
        FutureTarget<File> future = Glide.with(context)
                .load(url)
                .downloadOnly(500, 500);
        try {
            File cacheFile = future.get();
            return cacheFile;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isEmojiCharacter(char codePoint) {
                return ((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                        || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF))
                        || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                        || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
     }

    public static void deleteCache(){
        try{
            String cachePath = Glide.getPhotoCacheDir(MyApplication.getAppContext()).getPath();
            File cacheDir1 = new File(cachePath);
            if(cacheDir1.exists()){
                delete(cacheDir1);
            }
        }catch (Exception e){

        }
    }

    public static void delete(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i=0;i<files.length;i++){
                File tmpFile = files[i];
                if(tmpFile.isDirectory()){
                    delete(tmpFile);
                }else {
                    tmpFile.delete();
                }
            }
        }
    }

    public static String transformImgUrl(ImageView imageView, final String url) {
        return transformImgUrl(imageView.getWidth(), imageView.getHeight(), url);
    }

    // 情况1
    // https://hbcdn.huangbaoche.com/fr-hd[-t-d]/Dtjf9B6AQA0 替换前
    // https://fr-hd.huangbaoche.com/Dtjf9B6AQA0   替换后

    // 情况2
    // https://hbcdn.huangbaoche.com/C61Yn_6AKA0!m  替换前，并去掉叹号及叹号之后内容!s !l
    // https://fr-static.huangbaoche.com/C61Yn_6AKA0 替换后

    // 阿里云 https://help.aliyun.com/document_detail/44688.html?spm=5176.doc44701.6.939.fhSUWP
    // JS逻辑代码 http://gitlab.hbc.tech/widgets/comImg/blob/master/src/index.js#L56

    public static String transformImgUrl(int width, int height, final String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        String resultUrl = url;
        Uri uri = Uri.parse(url);

        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments == null || pathSegments.size() <= 0) {
            return resultUrl;
        }

        String host = uri.getHost();
        if (TextUtils.isEmpty(host)) {
            return resultUrl;
        }
        String local = host.substring(0, host.indexOf("."));
        if (!TextUtils.equals(local,"hbcdn")) {
            return resultUrl;
        }

        String firstSchemt = pathSegments.get(0);
        if (firstSchemt.indexOf("fr-") > -1) {//规则1
            resultUrl = resultUrl.replace("/" + firstSchemt, "");
            resultUrl = resultUrl.replace("hbcdn", firstSchemt);
        } else {//规则2
            resultUrl = resultUrl.replace("hbcdn", "fr-static");
        }

        // 去掉 !m !l !s
        String lastSchemt = pathSegments.get(pathSegments.size() - 1);
        if (!TextUtils.isEmpty(lastSchemt) && lastSchemt.contains("!")) {
            resultUrl = resultUrl.substring(0, resultUrl.lastIndexOf("!"));
        }

        //指定宽高缩放
        if (width > 0 && height > 0) {
            String params = String.format("x-oss-process=image/resize,m_fill,h_%1$s,w_%2$s,limit_0", "" + width, "" + height);
            resultUrl = CommonUtils.getBaseUrl(resultUrl) + params;
        }
        Log.i("imgUrl", "url = " + url +" --- resultUrl = " + resultUrl);
        return resultUrl;
    }

}
