package com.hugboga.custom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.widget.GlideRoundTransform;

import java.io.File;

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

    public static void showImage(ImageView imageView, String url,int resId) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(MyApplication.getAppContext())
                .load(url)
                .placeholder(resId)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    public static void showRoundImage(ImageView imageView, String url, float radius) {
        Glide.with(MyApplication.getAppContext())
                .load(url)
                .transform(new GlideRoundTransform(MyApplication.getAppContext(), radius))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
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


//    public static void showCircleImage(Context context,ImageView imageView,String url){
//        Glide.with(context).load(url).transform(new GlideCircleTransform(context)).into(imageView);
//    }

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
    public static String getRMB(){
        char symbol = 165;
        return String.valueOf(symbol);
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
}
