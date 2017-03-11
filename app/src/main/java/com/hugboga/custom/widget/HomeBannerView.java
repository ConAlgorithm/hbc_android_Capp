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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.util.NetWork;
import com.huangbaoche.hbcframe.widget.DialogUtilInterface;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.MediaPlayerActivity;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.NetWorkUtils;
import com.hugboga.custom.utils.UIUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeBannerView extends RelativeLayout implements HbcViewBehavior, TextureView.SurfaceTextureListener {

    public static final String VIDEO_PATH_NAME = "home_video_";
    public static final String KEY_VIDEO_VERSION = "videoVersion";

    /**
     * banner默认高宽比  height/width = 405/720
     */
    public static final float BANNER_RATIO_DEFAULT = 0.562f;

    @Bind(R.id.home_banner_parent_layout)
    RelativeLayout parentLayout;
    @Bind(R.id.home_banner_video_textureView)
    TextureView myTextureView;
    @Bind(R.id.home_banner_default_bg_iv)
    ImageView defaultBgIV;

    private int bannerHeight;
    private DialogUtilInterface mDialogUtil;

    private MediaPlayer mediaPlayer;
    private Surface surface;
    private int progress;

    private HomeBean.HeadVideo headVideo;

    public HomeBannerView(Context context) {
        this(context, null);
    }

    public HomeBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_home_banner, this);
        ButterKnife.bind(this, view);

        bannerHeight = (int) (UIUtils.getScreenWidth() * BANNER_RATIO_DEFAULT);
        RelativeLayout.LayoutParams bgParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, bannerHeight);
        parentLayout.setLayoutParams(bgParams);
        myTextureView.setLayoutParams(bgParams);

        mediaPlayer = new MediaPlayer();
        myTextureView.setSurfaceTextureListener(this);

        HomeBannerView.this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                final int videoVersion = SharedPre.getInteger(KEY_VIDEO_VERSION, 0);
//                File videoFile = new File(CommonUtils.getDiskFilesDir(Environment.DIRECTORY_MOVIES) + File.separator + VIDEO_PATH_NAME + videoVersion + ".mp4");
//                if (videoVersion > 0 && !videoFile.isDirectory() && videoFile.exists()) {
//                    intentPlayer(Uri.fromFile(videoFile));
//                    setSensorPlayVideo(); //神策统计播放视频
//                    return;
//                }
                /*
                注意：以上逻辑为，如果本地已存在视频，直接播放本地视频
                由于本次采用网络直接播放的形式，故此去除本地播放逻辑
                 */
                if (headVideo == null || TextUtils.isEmpty(headVideo.videoUrl)) {
                    return;
                }
                if (!NetWork.isNetworkAvailable(getContext())) {//判断网络
                    if (mDialogUtil == null) {
                        mDialogUtil = HttpRequestUtils.getDialogUtil(getContext());
                    }
                    mDialogUtil.showSettingDialog();
                } else if (!NetWorkUtils.isWifi()) {
                    DialogUtil mDialogUtil = DialogUtil.getInstance((Activity) getContext());
                    String tip = "您在使用运营商网络,观看视频会产生一定的流量费用。";
                    mDialogUtil.showCustomDialog("提示", tip, "继续观看", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            intentPlayer(headVideo.videoUrl);
                            StatisticClickEvent.click(StatisticConstant.PLAY_VIDEO, "首页视频播放");
                            setSensorPlayVideo(); //神策统计视频播放
                        }
                    }, "取消观看", null);
                } else {
                    intentPlayer(headVideo.videoUrl);
                    setSensorPlayVideo(); //神策统计视频播放
                }
            }
        });
    }

    @Override
    public void update(Object _data) {
        HomeBean homeBean = (HomeBean) _data;
        if (homeBean == null) {
            return;
        }

        headVideo = homeBean.headVideo;

        /*
        去除wifi状态下的下载视频逻辑
         */
//        if (headVideo != null && NetWorkUtils.isWifi()) {
//            final int videoVersion = SharedPre.getInteger(KEY_VIDEO_VERSION, 0);
//            if (headVideo.videoVersion != videoVersion) {
//                File videoFile = new File(CommonUtils.getDiskFilesDir(Environment.DIRECTORY_MOVIES) + File.separator + VIDEO_PATH_NAME + headVideo.videoVersion + ".mp4");
//                SaveFileTask saveImageTask = new SaveFileTask(getContext(), videoFile, this);
//                saveImageTask.execute(headVideo.videoUrl);
//            }
//        }
    }

    /**
     * 进入播放网络视频界面
     *
     * @param url
     */
    private void intentPlayer(String url) {
        Intent intent = new Intent(getContext(), MediaPlayerActivity.class);
        intent.putExtra(MediaPlayerActivity.KEY_URL, url);
        getContext().startActivity(intent);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(url, "video/mp4");
//        getContext().startActivity(intent);
    }

//    @Override
//    public void onDownLoadSuccess(File file) {
//        if (headVideo != null) {
//            SharedPre.setInteger(KEY_VIDEO_VERSION, headVideo.videoVersion);
//        }
//    }

//    @Override
//    public void onDownLoadFailed() {
//    }

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

    //神策统计_播放视频
    public void setSensorPlayVideo() {
        try {
            SensorsDataAPI.sharedInstance(getContext()).track("play_video", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
