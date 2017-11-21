package com.hugboga.custom.models;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.Tools;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/10.
 */
public class TravelStoryModel extends EpoxyModelWithHolder{

    private HomeBeanV2.TravelStory travelStory;

//    public void setDataPosition(int dataPosition) {
//        this.dataPosition = dataPosition;
//    }

    private int  dataPosition;


    public TravelStoryModel(HomeBeanV2.TravelStory travelStory, int dataPosition) {
        this.travelStory = travelStory;
        this.dataPosition = dataPosition;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.travel_story_item;
    }


    @Override
    protected StoryHolder createNewHolder() {
        return new StoryHolder();
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        final StoryHolder storyHolder = (StoryHolder) holder;
        if (travelStory == null) {
            return;
        }
        storyHolder.storyGuideCity.setText("@" + travelStory.cityName);
        storyHolder.storyGuideName.setText(travelStory.guideName);
        storyHolder.storyImage.getLayoutParams().height = ScreenUtil.screenWidth * 400 / 750;
        Tools.showImage(storyHolder.storyImage, travelStory.storyPicture, R.mipmap.home_default_route_item);
        Tools.showImage(storyHolder.storyGuideAvtar, travelStory.guidePic);
        storyHolder.storyDesc.setText(travelStory.storyName);

        setImageIcon(storyHolder.storyIcon,dataPosition);

        storyHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(travelStory.storyUrl)) {
                    return;
                }
                Intent intent = new Intent(storyHolder.itemView.getContext(), WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, travelStory.storyUrl);
                storyHolder.itemView.getContext().startActivity(intent);
                Map<String,String> map = new HashMap<>();
                map.put("storytype", 1 == travelStory.storyLableType ? "司导故事" : "旅客故事");
                MobClickUtils.onEvent(StatisticConstant.CLICK_GSTORY, map);
                setSensorGuideStories();
            }
        });
    }

    private void setImageIcon(ImageView imageIcon,int position){
        switch (position%8){
            case 0:
                imageIcon.setImageResource(R.mipmap.home_guide_story_0);
                break;
            case 1:
                imageIcon.setImageResource(R.mipmap.home_guide_story_1);
                break;
            case 2:
                imageIcon.setImageResource(R.mipmap.home_guide_story_2);
                break;
            case 3:
                imageIcon.setImageResource(R.mipmap.home_guide_story_3);
                break;
            case 4:
                imageIcon.setImageResource(R.mipmap.home_guide_story_4);
                break;
            case 5:
                imageIcon.setImageResource(R.mipmap.home_guide_story_5);
                break;
            case 6:
                imageIcon.setImageResource(R.mipmap.home_guide_story_6);
                break;
            case 7:
                imageIcon.setImageResource(R.mipmap.home_guide_story_7);
                break;
            default:
                imageIcon.setImageResource(R.mipmap.home_guide_story_0);
                break;
        }
    }


    static class StoryHolder extends EpoxyHolder {
        View itemView;

        @BindView(R.id.story_guide_avtar)
        ImageView storyGuideAvtar;
        @BindView(R.id.story_guide_name)
        TextView storyGuideName;
        @BindView(R.id.story_guide_city)
        TextView storyGuideCity;
        @BindView(R.id.story_icon)
        ImageView storyIcon;
        @BindView(R.id.story_image)
        ImageView storyImage;
        @BindView(R.id.story_desc)
        TextView storyDesc;

        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    //神策统计_司导故事
    public void setSensorGuideStories() {
        try {
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("G_story", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
