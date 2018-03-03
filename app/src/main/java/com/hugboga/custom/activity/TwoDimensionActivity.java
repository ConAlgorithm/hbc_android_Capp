package com.hugboga.custom.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.ImageUtils;
import com.hugboga.custom.utils.TwoDimensionUtlis;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangqi on 2018/2/27.
 */

public class TwoDimensionActivity extends BaseActivity {
    @BindView(R.id.two_dimension_imageView)
    ImageView twoDimensionImageView;
    @BindView(R.id.explain)
    TextView explain;
    @BindView(R.id.card_View)
    RelativeLayout cardView;
    @BindView(R.id.this_title)
    TextView this_title;
    public static final String TWODIMENSION = "TwoDimension";
    private String url = "";
    private Handler handler = new Handler();
    private Bitmap avatarBitmap;

    @Override
    public int getContentViewId() {
        return R.layout.activity_two_dimension;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDefaultTitleBar();
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra(TWODIMENSION);
        }
        createTwoDimension();
    }

    /**
     * 生成二维码
     **/
    private void createTwoDimension() {
        TextView headerTitle = findViewById(R.id.header_title_center);
        SpannableStringBuilder span = new SpannableStringBuilder("我有600元皇包车旅行红包送给你");
        span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.my_two_dimension_text_color)), 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        this_title.setText(span);
        headerTitle.setText("我的邀请二维码");

        if ("".equals(url)) {
            return;
        }
        String avatar = UserEntity.getUser().getAvatar(this);
        if (!TextUtils.isEmpty(avatar)) {
            returnBitmap(avatar);
        } else {
            avatarBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_avatar_user);
            handler.post(runnable);
        }

    }

    @OnClick(R.id.bottom_text)
    public void OnClick(View view) {
        if ("".equals(url)) {
            return;
        }
        cardView.setDrawingCacheEnabled(true);
        cardView.buildDrawingCache();
        Bitmap bitmap = cardView.getDrawingCache();

        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "we", "ssssdfsdf");
        String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
        MediaScannerConnection.scanFile(this, paths, null, null);
        CommonUtils.showToast("已保存到系统相册");
        SensorsUtils.onAppClick(getEventSource(), "保存", UserEntity.getUser().isProxyUser(this) ? "邀请新用户" : "邀请好友赢免单");
    }

    private void returnBitmap(final String url) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URL fileUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    avatarBitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    handler.post(runnable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (avatarBitmap == null) {
                return;
            }
            Bitmap bitmap = TwoDimensionUtlis.createQRCodeBitmap(url, 700, ImageUtils.toRoundBitmap(avatarBitmap), 0.2F);
            twoDimensionImageView.setImageBitmap(bitmap);
            avatarBitmap = null;
            bitmap = null;
        }
    };

    @Override
    public String getEventSource() {
        return "我的邀请二维码";
    }
}
