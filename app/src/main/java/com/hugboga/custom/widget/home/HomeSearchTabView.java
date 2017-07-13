package com.hugboga.custom.widget.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.fragment.FgHomePage;

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
    @Bind(R.id.home_header_hot_tab_line)
    View hotTabLine;

    @Bind(R.id.home_header_dest_tab_text)
    TextView destTabText;
    @Bind(R.id.home_header_dest_tab_icon)
    View destTabIcon;
    @Bind(R.id.home_header_dest_tab_line)
    View destTabLine;

    @Bind(R.id.home_header_story_tab_text)
    TextView storyTabText;
    @Bind(R.id.home_header_story_tab_icon)
    View storyTabIcon;
    @Bind(R.id.home_header_story_tab_line)
    View storyTabLine;

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
        setBackgroundColor(0xFFFFFFFF);
    }

    @OnClick({R.id.home_header_hot_tab, R.id.home_header_dest_tab, R.id.home_header_story_tab,
            R.id.home_activies_view})
    public void onClick(View view) {
        if (homeTabClickListener != null) {
            homeTabClickListener.onTabClick(view.getId());
        }
        switch (view.getId()) {
            case R.id.home_header_hot_tab:
                hotTabIcon.setVisibility(View.VISIBLE);
                destTabIcon.setVisibility(View.GONE);
                storyTabIcon.setVisibility(View.GONE);

                hotTabLine.setVisibility(View.VISIBLE);
                destTabLine.setVisibility(View.GONE);
                storyTabLine.setVisibility(View.GONE);
                break;
            /*case R.id.home_header_dest_tab:
                hotTabIcon.setVisibility(View.GONE);
                destTabIcon.setVisibility(View.VISIBLE);
                storyTabIcon.setVisibility(View.GONE);

                hotTabLine.setVisibility(View.GONE);
                destTabLine.setVisibility(View.VISIBLE);
                storyTabLine.setVisibility(View.GONE);
                break;*/
            case R.id.home_header_story_tab:
                hotTabIcon.setVisibility(View.GONE);
                destTabIcon.setVisibility(View.GONE);
                storyTabIcon.setVisibility(View.VISIBLE);

                hotTabLine.setVisibility(View.GONE);
                destTabLine.setVisibility(View.GONE);
                storyTabLine.setVisibility(View.VISIBLE);
                break;
        }
    }


    public void tabIndex(int index){
        switch (index){
            case FgHomePage.TAB_HOTEXPLORE:
                hotTabIcon.setVisibility(View.VISIBLE);
                destTabIcon.setVisibility(View.GONE);
                storyTabIcon.setVisibility(View.GONE);

                hotTabLine.setVisibility(View.VISIBLE);
                destTabLine.setVisibility(View.GONE);
                storyTabLine.setVisibility(View.GONE);
                break;
            case FgHomePage.TAB_DESTION:
                hotTabIcon.setVisibility(View.GONE);
                destTabIcon.setVisibility(View.VISIBLE);
                storyTabIcon.setVisibility(View.GONE);

                hotTabLine.setVisibility(View.GONE);
                destTabLine.setVisibility(View.VISIBLE);
                storyTabLine.setVisibility(View.GONE);
                break;
            case FgHomePage.TAB_GUIDE:
                hotTabIcon.setVisibility(View.GONE);
                destTabIcon.setVisibility(View.GONE);
                storyTabIcon.setVisibility(View.VISIBLE);

                hotTabLine.setVisibility(View.GONE);
                destTabLine.setVisibility(View.GONE);
                storyTabLine.setVisibility(View.VISIBLE);
                break;
        }
    }

    public interface HomeTabClickListener {
        void onTabClick(int resId);
    }

   /* public static int getFilterTabTop(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }*/
}
