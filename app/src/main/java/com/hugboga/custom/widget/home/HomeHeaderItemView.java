package com.hugboga.custom.widget.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeTopBean;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/11/27.
 */

public class HomeHeaderItemView extends LinearLayout implements HbcViewBehavior {

    public static final float DESPLAY_RATIO = 400 / 670f;

    @BindView(R.id.home_header_item_desplay_iv)
    ImageView desplayIV;
    @BindView(R.id.home_header_item_title_tv)
    TextView titleTV;
    @BindView(R.id.home_header_item_subtitle_tv)
    TextView subtitleTV;
    @BindView(R.id.home_header_item_desc_tv)
    TextView descTV;

    public HomeHeaderItemView(Context context) {
        this(context, null);
    }

    public HomeHeaderItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_home_header_item, this);
        ButterKnife.bind(this);

        final int marginLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_margin_left);
        int pagerWidth = UIUtils.getScreenWidth() - marginLeft;
        int desplayHeight = (int) (HomeHeaderItemView.DESPLAY_RATIO * pagerWidth);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, desplayHeight);
        desplayIV.setLayoutParams(params);
    }

    @Override
    public void update(Object _data) {
        if (!(_data instanceof HomeTopBean) || _data == null) {
            return;
        }
        HomeTopBean homeTopBean = (HomeTopBean) _data;
        Tools.showImage(desplayIV, homeTopBean.picture);
        titleTV.setText(homeTopBean.pictureTitle);
        subtitleTV.setText(homeTopBean.pictureSubTitle);
        descTV.setText(homeTopBean.desc);
    }
}
