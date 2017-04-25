package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityListHotAdapter;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.UIUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CityListHotView extends LinearLayout {

    public static final int TYPE_HOT = 1;
    public static final int TYPE_DEEP = 2;
    public static final int TYPE_SHORT = 3;

    @Bind(R.id.city_hot_title_tv)
    TextView titleTV;
    @Bind(R.id.city_hot_more_tv)
    TextView moreTV;
    @Bind(R.id.city_hot_recyclerview)
    RecyclerView cityRecyclerView;

    public List<SkuItemBean> hotLines;
    public int type;

    public CityListHotView(Context context) {
        this(context, null);
    }

    public CityListHotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_city_hot_item, this);
        ButterKnife.bind(view);
    }

    public void setDate(List<SkuItemBean> hotLines, int type) {
        this.hotLines = hotLines;
        this.type = type;


        titleTV.setText(getTitle());

        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        int displayImgWidth = UIUtils.getScreenWidth() - paddingLeft * 2 - UIUtils.dip2px(8);
        int displayImgHeight = (int)((400 / 650.0) * displayImgWidth);
        int viewHeight = displayImgHeight + ScreenUtil.dip2px(84);
        cityRecyclerView.getLayoutParams().height = viewHeight;

        cityRecyclerView.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cityRecyclerView.setLayoutManager(layoutManager);
        cityRecyclerView.setHorizontalScrollBarEnabled(false);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration();
        itemDecoration.setItemOffsets(paddingLeft, 0, 0, 0, LinearLayout.HORIZONTAL);
        cityRecyclerView.addItemDecoration(itemDecoration);
//        CityListHotAdapter adapter = new CityListHotAdapter(getContext(), hotLines, displayImgWidth, displayImgHeight);
//        cityRecyclerView.setAdapter(adapter);
    }


    public String getTitle() {
        String result = "";
        switch (type) {
            case TYPE_HOT:
                result = "热门线路";
                break;
            case TYPE_DEEP:
                result = "深度长线";
                break;
            case TYPE_SHORT:
                result = "短途线路";
                break;
        }
        return result;
    }
}
