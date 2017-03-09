package com.hugboga.custom.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.utils.Tools;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/9.
 */
public class HomeHeaderModel extends EpoxyModelWithHolder {

    HomeBeanV2.HomeHeaderInfo homeHeaderInfo;

    public HomeHeaderModel(HomeBeanV2.HomeHeaderInfo homeHeaderInfo) {
        this.homeHeaderInfo = homeHeaderInfo;
    }

    @Override
    protected HomeHeaderHolder createNewHolder() {
        return new HomeHeaderHolder();
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        HomeHeaderHolder homeHeaderHolder = (HomeHeaderHolder)holder;
        homeHeaderHolder.headerImage.getLayoutParams().height = ScreenUtil.screenWidth*(810-ScreenUtil.statusbarheight)/750;
       // homeHeaderHolder.filterView.getLayoutParams().height = ScreenUtil.screenWidth*810/750;
        Tools.showImage(homeHeaderHolder.headerImage,homeHeaderInfo.dynamicPic.videoUrl);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_header_view;
    }

    static class HomeHeaderHolder extends EpoxyHolder {

        View itemView;
        @Bind(R.id.home_header_image)
        ImageView headerImage;
//        @Bind(R.id.home_header_image_bg)
//        View filterView;
        @Bind(R.id.place_ammount_text)
        TextView placeAmmout;
        @Bind(R.id.guide_ammount_text)
        TextView gideAmmountText;
        @Bind(R.id.home_video_page)
        View homeVidePage;
        @Bind(R.id.home_chater)
        View chaterView; //包车游
        @Bind(R.id.home_picksend_layout)
        View pickSendView; //接送机
        @Bind(R.id.home_single_layout)
        View singleView; //单次接送
        @Bind(R.id.home_help)
        View homeHelp;


        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
