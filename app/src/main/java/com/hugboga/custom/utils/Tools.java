package com.hugboga.custom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hugboga.custom.widget.GlideCircleTransform;

import java.io.File;

/**
 * Created by dyt on 16/4/25.
 */
public class Tools {

    public static void showImage(Context context, ImageView imageView, String url){
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void getCache(Context context,ImageView imageView,String url){
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void showRoundImage(final Context context,final ImageView imageView,String url){
        Glide.with(context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

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
}
