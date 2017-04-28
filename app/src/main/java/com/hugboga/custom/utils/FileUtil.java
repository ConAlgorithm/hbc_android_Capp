package com.hugboga.custom.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import com.hugboga.custom.MyApplication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class FileUtil {

    // 手机SD卡的路径
    private String SD_PATH = null;

    public String getSD_PATH() {
        return SD_PATH;
    }

    public FileUtil() {
        // 得到当前外部存储设备的目录
        SD_PATH = Environment.getExternalStorageDirectory() + File.separator;
    }

    /**
     * 检查并创建制定目录
     *
     * @param path
     * @return
     */
    public static boolean checkAndCreateDir(String path) {
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdir();
            return false;
        }
        return true;
    }

    /**
     * 根据指定的文件名创建文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private File createSdFile(String fileName) {
        try {
            File file = new File(SD_PATH + fileName);
            file.createNewFile();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建目录
     *
     * @param folderName
     */
    private File createDir(String folderName) {
        File dir = new File(SD_PATH + folderName);
        dir.mkdirs();
        return dir;
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean fileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public File writeToSdCardFromInputStream(String path, String fileName,
                                             InputStream input, ProgressDialog myDialog) {
        File file = null;
        OutputStream out = null;
        try {
            createDir(path);
            file = createSdFile(path + fileName);
            byte[] temp = new byte[1024 * 4];
            out = new FileOutputStream(file);
            int len;
            int total = 0;
            while ((len = input.read(temp)) != -1) {
                out.write(temp, 0, len);
                total += len;
                myDialog.setProgress(total);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 将远程文件流，转成Bitmap对象
     *
     * @return
     */
    public Bitmap getURLBitmap(String url) {
        URL imageUrl = null;
        Bitmap bitmap = null;
        try {
            imageUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (imageUrl == null) {
            return bitmap;
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取指定路径，的数据。
     **/
    public static byte[] getImage(String urlpath) throws Exception {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(30 * 1000);
        // 别超过10秒。
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            return readStream(inputStream);
        } else {
        }
        return null;
    }

    /**
     * 读取数据 输入流
     */
    public static byte[] readStream(InputStream inStream) {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[5 * 1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outstream.write(buffer, 0, len);
            }

            return outstream.toByteArray();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                outstream.close();
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 将指定文件，转成Bitmap对象
     *
     * @return
     */
    public Bitmap getPathBitmap(String path) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    /**
     * 根据地址获得输入流
     *
     * @return
     */
    public InputStream getInputStream(String path) {
        InputStream input = null;
        URL url;
        try {
            url = new URL(path);
            input = url.openConnection().getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * 把注入流换成字符串
     *
     * @param tInputStream
     * @return
     */
    public static String getStreamString(InputStream tInputStream) {

        if (tInputStream != null) {
            try {
                BufferedReader tBufferedReader = new BufferedReader(
                        new InputStreamReader(tInputStream));

                StringBuffer tStringBuffer = new StringBuffer();
                String sTempOneLine;
                while ((sTempOneLine = tBufferedReader.readLine()) != null) {
                    tStringBuffer.append(sTempOneLine);
                }
                return tStringBuffer.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;

    }

    /**
     * 将一个字符串转化为输入流
     */
    private InputStream getStringStream(String sInputString) {
        if (sInputString != null && !sInputString.trim().equals("")) {
            try {
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(
                        sInputString.getBytes("UTF-8"));
                return tInputStringStream;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 向文本文件中写入内容或追加新内容,如果append为true则直接追加新内容,<br>
     * 如果append为false则覆盖原来的内容<br>
     *
     * @param path
     * @param content
     * @param append
     */
    public void writeFile(String path, String fileName, String content,
                          boolean append) {

        // 创建目录
        createDir(path);
        // 加上系统的根目录
        path = this.SD_PATH + path + File.separator + fileName;

        File writefile;
        try {
            // 通过这个对象来判断是否向文本文件中追加内容
            // boolean addStr = append;

            writefile = new File(path);

            // 如果文本文件不存在则创建它
            if (!writefile.exists()) {
                writefile.createNewFile();
                writefile = new File(path); // 重新实例化
            }

            // 将字符串转化为输入流
            InputStream is = getStringStream(content);

            // 创建输出流 append 判断是否追加内容
            FileOutputStream fw = new FileOutputStream(writefile, append);

            byte[] temp = new byte[1024 * 4];
            int len;
            while ((len = is.read(temp)) != -1) {
                fw.write(temp, 0, len);
            }
            fw.flush();
            fw.close();
        } catch (Exception ex) {
        }

    }

    /**
     * 根据网络地址返回对应的Bitmap对象
     *
     * @param url
     * @return
     */
    public synchronized static Bitmap returnBitMap(String url) {
        Bitmap bitmap = null;
        try {
            URL myFileUrl = null;
            try {
                myFileUrl = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn;
            InputStream is = null;
            try {
                conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {

        }
        return bitmap;
    }

    public static Bitmap getPic(InputStream input) {
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        return bitmap;
    }

    /**
     * 缩放图片
     *
     * @param bitmap
     * @param screenWidth
     * @return
     */
    public static Drawable resizeImage(Bitmap bitmap, int screenWidth) {

        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth(); // 获取图片的宽度

        int height = bitmap.getHeight();// 获取图片的高度

        int newWidth = screenWidth; // 传入的屏幕的宽度

        int newHeight = screenWidth * height / width; // 根据屏幕的宽度，计算按比较缩放后的高度

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        // 释放资源
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return new BitmapDrawable(resizedBitmap);

    }

    /**
     * 根据宽度缩放
     * Created by ZHZEPHI at 2015年5月28日 下午6:52:49
     *
     * @param bitmap
     * @param screenWidth
     * @return
     */
    public static Bitmap resizeImageW(Bitmap bitmap, int screenWidth) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth(); // 获取图片的宽度
        int height = bitmap.getHeight();// 获取图片的高度
        int newWidth = screenWidth; // 传入的屏幕的宽度
        int newHeight = screenWidth * height / width; // 根据屏幕的宽度，计算按比较缩放后的高度
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        // 释放资源
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return resizedBitmap;
    }

    /**
     * 根据高度缩放
     * Created by ZHZEPHI at 2015年5月28日 下午6:52:49
     *
     * @param bitmap
     * @param screenHeight
     * @return
     */
    public static Bitmap resizeImageH(Bitmap bitmap, int screenHeight) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth(); // 获取图片的宽度
        int height = bitmap.getHeight();// 获取图片的高度
        int newWidth = width * screenHeight / height;
        int newHeight = screenHeight;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        // 释放资源
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return resizedBitmap;
    }

	/* 图片缩放 */

    /**
     * 原图bitmap对象 缩放的比例
     */
    public static Bitmap toBig(Bitmap bmp, float scale) {

        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

		/* 计算这次要放大/缩小的比例 */
        float scaleWidth = bmpWidth * scale;
        float scaleHeight = bmpHeight * scale;

		/* 产生reSize后的Bitmap对象 */
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,
                matrix, true);

        return resizeBmp;
    }

    /**
     * 图片压缩
     *
     * @param fromFile 文件全路径
     * @param toFile   要写入的文件的路径
     * @param width    图片的宽度
     * @param height   图片的高度
     * @param quality
     */
    public void transImage(String fromFile, String toFile, int width, int height, int quality) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            // 缩放图片的尺寸
            float scaleWidth = (float) width / bitmapWidth;
            float scaleHeight = (float) height / bitmapHeight;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 产生缩放后的Bitmap对象
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmapWidth, bitmapHeight, matrix, false);
            // save file
            File myCaptureFile = new File(toFile);
            FileOutputStream out = new FileOutputStream(myCaptureFile);
            if (resizeBitmap.compress(CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();// 记得释放资源，否则会内存溢出
            }
            if (!resizeBitmap.isRecycled()) {
                resizeBitmap.recycle();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 压缩图片为固定高度
     *
     * @param filePath 图片地址
     * @param be       指定高度
     * @return
     */
    public static File zoomToFix(String filePath, int be) {
        File file = new File(filePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            options.inJustDecodeBounds = true; // 先设置预读图片，既只读取大小信息
            options.inJustDecodeBounds = false; // 读出大小信息后改为非预读
            options.inSampleSize = be; // 设置缩放，可惜只能是整数
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options); // 根据缩放比实际读取图片
            FileOutputStream fos = new FileOutputStream(file); // 保存图片文件
            bitmap.compress(CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
        }
        return file;
    }

    /**
     * 压缩图片为固定高度
     *
     * @param filePath 图片地址
     * @param be       缩放
     * @return
     */
    public static Bitmap zoomToBitmap(String filePath, int be) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = null;
        try {
            options.inJustDecodeBounds = true; // 先设置预读图片，既只读取大小信息
            bitmap = BitmapFactory.decodeFile(filePath, options);
            options.inJustDecodeBounds = false; // 读出大小信息后改为非预读

            options.inSampleSize = be; // 设置缩放，可惜只能是整数
            bitmap = BitmapFactory.decodeFile(filePath, options); // 根据缩放比实际读取图片

        } catch (Exception e) {
        }
        return bitmap;
    }

    /**
     * Bitmap 保存为文件
     *
     * @param bmp
     * @param filename
     * @return
     */
    public static boolean saveBitmapToFile(Bitmap bmp, String filePath, String filename) {
        CompressFormat format = CompressFormat.JPEG;
        File tempFile = new File(filePath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        int quality = 100;
        OutputStream stream = null;
        try {
            String filenewName = filePath + File.separator + filename;
            File file = new File(filenewName);
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stream = new FileOutputStream(filenewName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        // 从Uri中读取图片资源
        try {
            byte[] mContent = FileUtil.readStream(context.getContentResolver().openInputStream(Uri.parse(uri.toString())));
            bitmap = FileUtil.getBitmap(mContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getBitmap(byte[] all) {
        Bitmap bmp;
        try {
            // path文件的byte
            if (all.length > 1024 * 1024) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 5;// 图片缩放比例
                bmp = BitmapFactory
                        .decodeByteArray(all, 0, all.length, options);
            } else {
                bmp = BitmapFactory.decodeByteArray(all, 0, all.length);
            }
            return bmp;
        } catch (OutOfMemoryError o) {
        } catch (Exception oom) {
        }
        return null;
    }

    /**
     * 在SD卡下面创建唯一名称的文件
     *
     * @param path - 文件路径
     * @return
     * @author Zongfi
     * @datetime 2014-4-9 下午12:23:45
     * @email zzf_soft@163.com
     */
    public static String getFileNameOnly(String path, String fileType) {
        String fiveStr = String.valueOf(new Random().nextInt(90000) + 10000); //5位随机号码
        return Environment.getExternalStorageDirectory() + File.separator + path + File.separator + System.currentTimeMillis() + fiveStr + fileType;
    }

    public static String getNativeFile(String fileName) {
        StringBuffer json = new StringBuffer();
        InputStream in = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            in = MyApplication.getAppContext().getAssets().open(fileName);
            isr = new InputStreamReader(in, "utf8");
            reader = new BufferedReader(isr);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                json.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json.toString();
    }

    public static long getFileOrDirSize(File file) {
        if (!file.exists()) return 0;
        if (!file.isDirectory()) return file.length();
        long length = 0;
        File[] list = file.listFiles();
        if (list != null) { // 文件夹被删除时, 子文件正在被写入, 文件属性异常返回null.
            for (File item : list) {
                length += getFileOrDirSize(item);
            }
        }

        return length;
    }
}
