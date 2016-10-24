package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

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
        HomeBean.TravelStory data = (HomeBean.TravelStory) _data;
        if (data == null) {
            return;
        }
        Tools.showImage(displayIV, data.storyPicture);
        guideCountTV.setText(data.storyName);
    }
}
