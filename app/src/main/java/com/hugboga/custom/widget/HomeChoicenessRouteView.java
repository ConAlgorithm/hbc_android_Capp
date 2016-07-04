package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeChoicenessRouteAdapter;
import com.hugboga.custom.data.bean.HomeData;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeChoicenessRouteView extends LinearLayout {

    private ViewPager mViewPager;
    private HomeChoicenessRouteAdapter adapter;

    public HomeChoicenessRouteView(Context context) {
        this(context, null);
    }

    public HomeChoicenessRouteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_home_choiceness_route, this);

        final int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        mViewPager = (ViewPager) findViewById(R.id.home_choiceness_route_viewpager);
        mViewPager.setPageMargin(paddingLeft);
        mViewPager.setOffscreenPageLimit(5);

        int itemWidth = UIUtils.getScreenWidth() - paddingLeft * 2 - UIUtils.dip2px(18) * 2;
        int displayImgHeight = (int)((287 / 620.0) * itemWidth);
        int itemHeight = displayImgHeight + UIUtils.dip2px(76) * 3 + 3 + UIUtils.dip2px(44);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemHeight);
        params.gravity = Gravity.CENTER;
        params.bottomMargin = UIUtils.dip2px(6);
        mViewPager.setLayoutParams(params);

    }

    public void setData(FgHome _frgment, ArrayList<HomeData.CityContentItem> _itemList) {
        if (_frgment == null || _itemList == null) {
            return;
        }
        adapter = new HomeChoicenessRouteAdapter(getContext());
        adapter.setData(_frgment, _itemList);
        mViewPager.setAdapter(adapter);
    }
}
