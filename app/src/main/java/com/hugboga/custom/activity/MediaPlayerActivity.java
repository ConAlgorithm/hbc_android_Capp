package com.hugboga.custom.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

//import com.hugboga.custom.R;
//import com.hugboga.custom.utils.CommonUtils;
//import com.hugboga.mediaplayer.MediaPlayerView;
//import com.hugboga.mediaplayer.PlayListener;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;

public class MediaPlayerActivity extends BaseActivity {

//    @Bind(R.id.media_player)
//    MediaPlayerView mediaPlayerView;

    public static final String KEY_URL = "key_url";

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_media_player);
//        ButterKnife.bind(this);
//        screenFull();
//
//        //视频设置
//        mediaPlayerView.changeAngle(90); //改成横屏显示
//        mediaPlayerView.setPlayListener(new PlayListener() {
//            @Override
//            public void onCompleted() {
//                finish();
//            }
//        });
//        play(); //进行播放
//    }
//
//    private void screenFull() {
//        //全屏显示
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//    }
//
//    /**
//     * 播放视频
//     */
//    private void play() {
//        //接收网络视频地址
//        if (getIntent() != null) {
//            String videoPath = getIntent().getStringExtra(KEY_URL);
//            //开始播放
//            if (!TextUtils.isEmpty(videoPath)) {
//                mediaPlayerView.start(videoPath);
//            } else {
//                CommonUtils.showToast("视频地址不存在");
//            }
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mediaPlayerView.pause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        screenFull();
//        mediaPlayerView.resume();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mediaPlayerView.destroy();
//    }
}
