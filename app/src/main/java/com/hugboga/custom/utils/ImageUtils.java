/*
 * ImageUtils.java [V1.0.0]
 * classes:com.hugboga.custom.utils.ImageUtils
 * ZHZEPHI Create at 2015年3月23日 上午11:41:44
 */
package com.hugboga.custom.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * com.hugboga.custom.utils.ImageUtils
 *
 * @author zhangqiang
 */
public class ImageUtils {

    public static String bitmapToString(Bitmap bitmap) {

        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * 检查图片目录是否存在
     * 不存在，则创建
     * Created by ZHZEPHI at 2015年3月23日 下午12:10:21
     */
    public static void checkDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File fileDir = new File(Constants.IMAGE_DIR);
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
        } else {
            Log.i("ERROR:", "SD卡不存在");
        }
    }

    /**
     * 生成图片名称
     *
     * @return
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date) + ".jpg";
    }


    /**
     * 根据屏幕宽度生成图片的高
     *
     * @param width  原始width
     * @param height 原始height
     */
    public static int getResizeHeight(Context context, int width, int height) {
        if (width == 0 || height == 0) {
            return 0;
        }
        int screenWidth = getScreenWidth(context);
        return screenWidth * height / width;
    }


    /**
     * 获取手机屏幕width
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static File compressImageFile(Context context, File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        int sampleSize = options.outWidth / ScreenUtil.screenWidth;
        if (sampleSize > 0) {
            options.inSampleSize = sampleSize;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        String newFilePath = context.getExternalFilesDir(Constants.IMAGE_DIR) + File.separator + "hbc_" + System.currentTimeMillis();
        CompressFormat format = CompressFormat.JPEG;
        int quality = 70;
        OutputStream stream = null;
        File newFile = null;
        try {
            newFile = new File(newFilePath);
            if (newFile.exists()) {
                newFile.delete();
            }
            newFile.createNewFile();
            stream = new FileOutputStream(newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap != null && stream != null) {
            bitmap.compress(format, quality, stream);
            bitmap.recycle();
        }
        return newFile;
    }

    /**
     * 将bitmap处理为圆形
     *
     * @author QiZhang
     * <p>
     * 2018年2月27日
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
}
