package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/10/24.
 */
public class HomeTravelStoriesView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.home_travel_stories_viewpager)
    ViewPager mViewPager;

    public HomeTravelStoriesView(Context context) {
        this(context, null);
    }

    public HomeTravelStoriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_home_travel_stories, this);
        ButterKnife.bind(this, view);

        final int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        mViewPager.setPageMargin(paddingLeft / 2);
        mViewPager.setOffscreenPageLimit(3);

        int itemWidth = UIUtils.getScreenWidth() - paddingLeft * 2;
        int displayImgHeight = (int)((346 / 648.0) * itemWidth);
        int itemHeight = displayImgHeight + UIUtils.dip2px(66);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemHeight);
        params.gravity = Gravity.CENTER;
        params.bottomMargin = UIUtils.dip2px(4);
        mViewPager.setLayoutParams(params);
    }

    @Override
    public void update(Object _data) {
        ArrayList<HomeBean.TravelStory> travelStories = (ArrayList<HomeBean.TravelStory>) _data;
        if (travelStories == null || travelStories.size() <= 0) {
            this.setVisibility(View.GONE);
            return;
        }
        this.setVisibility(View.VISIBLE);
        HomeTravelStoriesAdapter adapter = new HomeTravelStoriesAdapter(getContext(), travelStories);
        mViewPager.setAdapter(adapter);
    }

    public static class HomeTravelStoriesAdapter extends PagerAdapter {


        private Context mContext;
        private ArrayList<HomeBean.TravelStory> itemList;

        public HomeTravelStoriesAdapter(Context context, ArrayList<HomeBean.TravelStory> _itemList) {
            this.mContext = context;
            this.itemList = _itemList;
        }

        @Override
        public int getCount() {
            return itemList == null ? 0 : itemList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            HomeTravelStoryItemView itemView = new HomeTravelStoryItemView(mContext);
            itemView.update(itemList.get(position));
            container.addView(itemView, 0);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
