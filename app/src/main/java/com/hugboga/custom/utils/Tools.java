package com.hugboga.custom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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

import org.xutils.common.util.*;

import java.io.File;

import jp.wasabeef.blurry.Blurry;

/**
 * Created  on 16/4/25.
 */
public class Tools {

    public static void showImage(ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(MyApplication.getAppContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

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
    public static void showImage(final ImageView imageView, String url, final int resId) {
        if (TextUtils.isEmpty(url)) {
//            imageView.setBackgroundResource(resId);
            imageView.setImageResource(resId);
            return;
        }
//        imageView.setBackgroundResource(resId);
        Glide.with(MyApplication.getAppContext())
                .load(url)
                .centerCrop()
                .error(resId)
                .placeholder(resId)
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

    //placeholder 显示有问题
    public static void showImageFitCenter(final ImageView imageView, String url, final int resId) {
        if (TextUtils.isEmpty(url)) {
//            imageView.setBackgroundResource(resId);
            imageView.setImageResource(resId);
            return;
        }
//        imageView.setBackgroundResource(resId);
        Glide.with(MyApplication.getAppContext())
                .load(url)
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

//    public static void showRoundImage(final Context context,final ImageView imageView,String url){
//        Glide.with(context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                imageView.setImageDrawable(circularBitmapDrawable);
//            }
//        });
//    }


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
}
