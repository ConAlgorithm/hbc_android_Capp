package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.util.NetWork;
import com.huangbaoche.hbcframe.widget.DialogUtilInterface;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.NetWorkUtils;
import com.hugboga.custom.utils.SaveFileTask;
import com.hugboga.custom.utils.UIUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeBannerView extends RelativeLayout implements HbcViewBehavior, SaveFileTask.FileDownLoadCallBack, TextureView.SurfaceTextureListener {

    public static final String GIF_PATH_NAME = "home_gif_";
    public static final String KEY_GIF_VERSION = "gifVersion";

    /**
     * banner默认高宽比  height/width = 405/720
     */
    public static final float BANNER_RATIO_DEFAULT = 0.562f;

    @Bind(R.id.home_banner_video_textureView)
    TextureView myTextureView;
    @Bind(R.id.home_banner_default_bg_iv)
    ImageView defaultBgIV;

    private int bannerHeight;
    private DialogUtilInterface mDialogUtil;

    private MediaPlayer mediaPlayer;
    private  Surface surface;
    private int progress;

    public HomeBannerView(Context context) {
        this(context, null);
    }

    public HomeBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_home_banner, this);
        ButterKnife.bind(this, view);

        bannerHeight = (int)(UIUtils.getScreenWidth() * BANNER_RATIO_DEFAULT);
        RelativeLayout.LayoutParams bgParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, bannerHeight);
        myTextureView.setLayoutParams(bgParams);

        mediaPlayer = new MediaPlayer();
        myTextureView.setSurfaceTextureListener(this);
    }

    @Override
    public void update(Object _data) {
        HomeBean homeBean = (HomeBean) _data;
        if (homeBean == null) {
            return;
        }

        final HomeBean.HeadVideo headVideo = homeBean.headVideo;
        if (headVideo != null) {
            HomeBannerView.this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(headVideo.videoUrl)) {
                        return;
                    }
                    if (!NetWork.isNetworkAvailable(getContext())) {//判断网络
                        if (mDialogUtil == null) {
                            mDialogUtil = HttpRequestUtils.getDialogUtil(getContext());
                        }
                        mDialogUtil.showSettingDialog();
                    } else if (!NetWorkUtils.getCurrentNetwork().equals("WIFI")) {
                        DialogUtil mDialogUtil = DialogUtil.getInstance((Activity)getContext());
                        String tip = "您在使用运营商网络,观看视频会产生一定的流量费用。";
                        mDialogUtil.showCustomDialog("提示", tip, "继续观看", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                intentPlayer(headVideo.videoUrl);
                                StatisticClickEvent.click(StatisticConstant.PLAY_VIDEO,"首页视频播放");
                            }
                        }, "取消观看", null);
                    } else {
                        intentPlayer(headVideo.videoUrl);
                    }
                }
            });
        }

//        dynamicPicBean = homeBean.dynamicPic;
//        dynamicPicBean.videoVersion = 3;
//        dynamicPicBean.videoUrl = "http://fr.huangbaoche.com/video/hbc-capphome.mp4";
//        if (dynamicPicBean != null) {
//            final int gifVersion = SharedPre.getInteger(KEY_GIF_VERSION, 0);
//            if (dynamicPicBean.videoVersion != gifVersion) {
//                File gifFile = new File(CommonUtils.getDiskFilesDir(Environment.DIRECTORY_MOVIES) + File.separator + GIF_PATH_NAME + dynamicPicBean.videoVersion + ".mp4");
//                SaveFileTask saveImageTask = new SaveFileTask(getContext(), gifFile, this);
//                saveImageTask.execute(dynamicPicBean.videoUrl);
//            }
//        }
    }

    private void intentPlayer(String videoUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(videoUrl), "video/mp4");
        getContext().startActivity(intent);
    }

    @Override
    public void onDownLoadSuccess(File file) {
//        if (dynamicPicBean != null) {
//            SharedPre.setInteger(KEY_GIF_VERSION, dynamicPicBean.videoVersion);
//        }
    }

    @Override
    public void onDownLoadFailed() {
    }

    public void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        surface = new Surface(surfaceTexture);
        if (!mediaPlayer.isPlaying()) {
            play(progress);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (mediaPlayer.isPlaying()) {
            progress = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    private void play(final int currentPosition) {
        try {
            defaultBgIV.setImageResource(R.mipmap.home_default_route_item);
            mediaPlayer.reset();
            mediaPlayer.setLooping(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AssetManager assetManager = getContext().getAssets();
            AssetFileDescriptor afd = assetManager.openFd("home_header.mp4");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.setSurface(surface);
            mediaPlayer.prepare();
            mediaPlayer.start();
            if (currentPosition > 0) {
                mediaPlayer.seekTo(currentPosition);
            }
            defaultBgIV.setImageResource(0);
        } catch (Exception e) {
            defaultBgIV.setImageResource(R.mipmap.home_default_route_item);
        }
    }

}
