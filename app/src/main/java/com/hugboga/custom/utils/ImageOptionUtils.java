package com.hugboga.custom.utils;

import android.widget.ImageView;

import com.hugboga.custom.R;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ImageOptionUtils {
    public static ImageOptions userPortraitImageOptions = new ImageOptions.Builder()
//            .setSize(DensityUtil.dip2px(50), DensityUtil.dip2px(50))
                    // 如果ImageView的大小不是定义为wrap_content, 不要crop.
//            .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
//            .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            .setCircular(true)
            .setLoadingDrawableId(R.mipmap.chat_head)
            .setFailureDrawableId(R.mipmap.chat_head)
            .build();
}
