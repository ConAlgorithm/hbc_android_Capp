package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/10/24.
 */
public class HomeTravelStoryItemView extends RelativeLayout implements HbcViewBehavior{

    @Bind(R.id.home_travel_story_item_bg_iv)
    ImageView displayIV;
    @Bind(R.id.home_travel_story_item_title_tv)
    TextView guideCountTV;

    public HomeTravelStoryItemView(Context context) {
        this(context, null);
    }

    public HomeTravelStoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_home_travel_story_item, this);
        ButterKnife.bind(this, view);

        int displayImgHeight = (int)((346 / 648.0) * (UIUtils.getScreenWidth() - context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left) * 2));
        displayIV.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, displayImgHeight));
    }

    @Override
    public void update(Object _data) {
        final HomeBean.TravelStory data = (HomeBean.TravelStory) _data;
        if (data == null) {
            return;
        }
        if (TextUtils.isEmpty(data.storyPicture)) {
            displayIV.setImageResource(R.mipmap.home_default_route_item);
        } else {
            Tools.showImage(displayIV, data.storyPicture, R.mipmap.home_default_route_item);
        }
        guideCountTV.setText(data.storyName);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(data.storyUrl)) {
                    return;
                }
                Intent intent = new Intent(getContext(), WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, data.storyUrl);
                getContext().startActivity(intent);
                StatisticClickEvent.click(StatisticConstant.CLICK_STORY, "首页");
                setSensorGuideStories();
            }
        });
    }

    //神策统计_司导故事
    public void setSensorGuideStories(){
        try {
            SensorsDataAPI.sharedInstance(getContext()).track("G_story",null);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }
    }
}
