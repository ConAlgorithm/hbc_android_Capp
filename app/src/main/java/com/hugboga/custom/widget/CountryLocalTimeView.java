package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/11/14.
 */
public class CountryLocalTimeView extends FrameLayout implements View.OnClickListener{
    @Bind(R.id.local_country_iv)
    ImageView countryImageIV;
    @Bind(R.id.local_time_tv)
    TextView localTimeTV;
    @Bind(R.id.country_flag_layout)
    LinearLayout localLayout;

    @Bind(R.id.local_time_detial_tv)
    TextView localTimeDetialTV;
    @Bind(R.id.local_time_detial_layout)
    LinearLayout localTimeDetialLayout;

    public CountryLocalTimeView(Context context) {
        this(context, null);
    }

    public CountryLocalTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_local_time, this);
        ButterKnife.bind(view);

        localData();
    }

    /**
     * 加载数据
     */
    public void localData(){
        showLocalAnimation();
    }

    /**
     * 国旗进行动画显示
     */
    public void showLocalAnimation(){
        localLayout.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(UIUtils.getScreenWidth() - 5, localLayout.getWidth(), localLayout.getHeight(), localLayout.getHeight());
        animation.setDuration(1000);
        localLayout.startAnimation(animation);
    }


    @OnClick({R.id.country_flag_layout, R.id.local_time_detial_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.country_flag_layout:
                localLayout.setVisibility(View.GONE);
                localTimeDetialLayout.setVisibility(View.VISIBLE);

                AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
                alphaAnimation.setDuration(300);
                localTimeDetialLayout.startAnimation(alphaAnimation);
                break;
            case R.id.local_time_detial_tv:
                localTimeDetialLayout.setVisibility(View.GONE);
                localLayout.setVisibility(View.VISIBLE);
                break;
        }
    }
}
