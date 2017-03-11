package com.hugboga.custom.widget.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SPW on 2017/3/10.
 */
public class HomeSearchTabView extends FrameLayout {

    @Bind(R.id.home_header_hot_tab_text)
    TextView hotTabText;
    @Bind(R.id.home_header_hot_tab_icon)
    View hotTabIcon;

    @Bind(R.id.home_header_dest_tab_text)
    TextView destTabText;
    @Bind(R.id.home_header_dest_tab_icon)
    View destTabIcon;

    @Bind(R.id.home_header_story_tab_text)
    TextView storyTabText;
    @Bind(R.id.home_header_story_tab_icon)
    View storyTabIcon;

    @Bind(R.id.home_activies_view)
    View activiesTabIcon;


    private HomeTabClickListener homeTabClickListener;

    public void setHomeTabClickListener(HomeTabClickListener homeTabClickListener) {
        this.homeTabClickListener = homeTabClickListener;
    }

    public HomeSearchTabView(Context context) {
        this(context, null);
    }

    public HomeSearchTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.home_header_tab, this);
        ButterKnife.bind(view);
    }

    @OnClick({R.id.home_header_hot_tab, R.id.home_header_dest_tab, R.id.home_header_story_tab,
            R.id.home_activies_view})
    public void onClick(View view) {
        if(homeTabClickListener!=null){
            homeTabClickListener.onTabClick(view.getId());
        }
        switch (view.getId()) {
            case R.id.home_header_hot_tab:
                break;
            case R.id.home_header_dest_tab:
                break;
            case R.id.home_header_story_tab:
                break;

        }
    }

    public interface HomeTabClickListener{
         void onTabClick(int resId);
    }
}
