package com.hugboga.custom.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeChoicenessRouteAdapter;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeChoicenessRouteView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.home_choiceness_route_title_tv)
    TextView titleTV;
    @Bind(R.id.home_choiceness_route_recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.home_choiceness_route_top_iv)
    ImageView topIV;
    @Bind(R.id.home_choiceness_route_bottom_iv)
    ImageView bottomIV;

    private HomeChoicenessRouteAdapter adapter;

    private int type; // 1、包车线路游(超自由); 2、包车畅游(超省心)

    public HomeChoicenessRouteView(Context context) {
        this(context, null);
    }

    public HomeChoicenessRouteView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HomeChoicenessRouteView);
        type = a.getInt(R.styleable.HomeChoicenessRouteView_type, 1);
        a.recycle();

        View view = inflate(context, R.layout.view_home_choiceness_route, this);
        ButterKnife.bind(this, view);

        final int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        int topImgWidth = UIUtils.getScreenWidth() - paddingLeft * 2;
        int topImgHeight = (int)((194 / 648.0) * topImgWidth);
        LinearLayout.LayoutParams topImgParams = new LinearLayout.LayoutParams(topImgWidth, topImgHeight);
        topImgParams.leftMargin = paddingLeft;
        topIV.setLayoutParams(topImgParams);

        if (type == 1) {
            titleTV.setText("包车线路游");
            topIV.setBackgroundResource(R.mipmap.choiceness_route_top_1);
            bottomIV.setBackgroundResource(R.mipmap.choiceness_route_bottom_1);
        } else {
            titleTV.setText("包车畅游");
            topIV.setBackgroundResource(R.mipmap.choiceness_route_top_2);
            bottomIV.setBackgroundResource(R.mipmap.choiceness_route_bottom_2);
        }

        recyclerView.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHorizontalScrollBarEnabled(false);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration();
        itemDecoration.setItemOffsets(UIUtils.dip2px(8), 0, 0, 0);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new HomeChoicenessRouteAdapter(context, type);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void update(Object _data) {
        ArrayList<SkuItemBean> _itemList = (ArrayList<SkuItemBean>) _data;
        if (_itemList == null || _itemList.size() <= 0) {
            this.setVisibility(View.GONE);
            return;
        }
        this.setVisibility(View.VISIBLE);
        adapter.setData(_itemList);
    }
}
