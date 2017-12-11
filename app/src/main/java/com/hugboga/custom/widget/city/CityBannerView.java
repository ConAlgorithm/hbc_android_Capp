package com.hugboga.custom.widget.city;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.city.BeginnerDirectionVo;
import com.hugboga.custom.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 头部Banner部分显示
 * Created by HONGBO on 2017/11/28 14:58.
 */

public class CityBannerView extends FrameLayout {

    @BindView(R.id.city_banner_img)
    ImageView city_banner_img; //背景图
    @BindView(R.id.city_banner_title)
    TextView city_banner_title; //标题
    @BindView(R.id.city_banner_subtitle)
    TextView city_banner_subtitle; //副标题

    BeginnerDirectionVo beginnerDirectionVo;

    public CityBannerView(@NonNull Context context) {
        this(context, null);
    }

    public CityBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_banner_layout, this);
        ButterKnife.bind(this, view);
    }

    public void init(Activity activity, BeginnerDirectionVo beginnerDirectionVo) {
        this.beginnerDirectionVo = beginnerDirectionVo;
        if (beginnerDirectionVo != null) {
            setVisibility(VISIBLE);
            Tools.showImageNotCenterCrop(city_banner_img, beginnerDirectionVo.backgroundImageUrl, R.mipmap.des_city_dafault);
            city_banner_title.setText(beginnerDirectionVo.title);
            city_banner_subtitle.setText(beginnerDirectionVo.subTitle);
        } else {
            setVisibility(GONE);
        }
    }

    @OnClick({R.id.city_banner_layout_bg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city_banner_layout_bg:
                // 打开新手指引H5页面
                if (beginnerDirectionVo != null && !TextUtils.isEmpty(beginnerDirectionVo.beginnerDirectionUrl)) {
                    Intent intent = new Intent(getContext(), WebInfoActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, beginnerDirectionVo.beginnerDirectionUrl);
                    intent.putExtra(WebInfoActivity.WEB_SHARE_BTN, true);
                    getContext().startActivity(intent);
                }
                break;
        }
    }
}
