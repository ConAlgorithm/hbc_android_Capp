package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.utils.NetWorkUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeBannerView extends RelativeLayout implements HbcViewBehavior{

    /**
     * banner默认高宽比  height/width = 460/720
     */
    public static final float BANNER_RATIO_DEFAULT = 0.639f;

    @Bind(R.id.home_banner_bg_iv)
    ImageView bannerBgIV;

    private int bannerHeight;

    public HomeBannerView(Context context) {
        this(context, null);
    }

    public HomeBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_home_banner, this);
        ButterKnife.bind(this, view);

        bannerHeight = (int)(UIUtils.getScreenWidth() * BANNER_RATIO_DEFAULT);
        RelativeLayout.LayoutParams bgParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, bannerHeight);
        bannerBgIV.setLayoutParams(bgParams);
        Tools.showGif(bannerBgIV, R.drawable.rock);

    }

    @Override
    public void update(Object _data) {
        final HomeBean.HeadVideo headVideo = (HomeBean.HeadVideo) _data;
        HomeBannerView.this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(headVideo.videoUrl)) {
                    return;
                }
                if (!NetWorkUtils.getCurrentNetwork().equals("WIFI")) {
                    DialogUtil mDialogUtil = DialogUtil.getInstance((Activity)getContext());
                    String tip = "您在使用运营商网络,观看视频会产生一定的流量费用。";
                    mDialogUtil.showCustomDialog("提示", tip, "继续观看", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            intentPlayer(headVideo.videoUrl);
                        }
                    }, "取消观看", null);
                } else {
                    intentPlayer(headVideo.videoUrl);
                }
            }
        });
    }

    private void intentPlayer(String videoUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(videoUrl), "video/mp4");
        getContext().startActivity(intent);
    }

    public int getBannerHeight() {
        return bannerHeight;
    }

}
