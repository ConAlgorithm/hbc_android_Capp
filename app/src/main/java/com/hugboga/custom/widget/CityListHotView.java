package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.activity.FilterSkuListActivity;
import com.hugboga.custom.adapter.CityListHotAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.UIUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityListHotView extends LinearLayout {

    public static final int TYPE_HOT = 1;
    public static final int TYPE_DEEP = 2;
    public static final int TYPE_SHORT = 3;

    @BindView(R.id.city_hot_title_tv)
    TextView titleTV;
    @BindView(R.id.city_hot_more_tv)
    TextView moreTV;
    @BindView(R.id.city_hot_recyclerview)
    RecyclerView cityRecyclerView;

    public List<SkuItemBean> hotLines;
    public int type;
    public CityListActivity.Params paramsData;

    public int displayImgWidth, displayImgHeight;
    public CityListHotAdapter adapter;

    public CityListHotView(Context context) {
        this(context, null);
    }

    public CityListHotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_city_hot_item, this);
        ButterKnife.bind(view);

        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.city_view_padding_left);
        displayImgWidth = UIUtils.getScreenWidth() - paddingLeft * 2 - UIUtils.dip2px(8);
        displayImgHeight = (int)((400 / 650.0) * displayImgWidth);
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
    }

    public void setDate(CityListActivity.Params _paramsData, List<SkuItemBean> hotLines, int _type) {
        this.paramsData = _paramsData;
        this.hotLines = hotLines;
        this.type = _type;


        titleTV.setText(getTitle());
        if (adapter == null) {
            adapter = new CityListHotAdapter(getContext(), paramsData, hotLines, type, displayImgWidth, displayImgHeight);
            cityRecyclerView.setAdapter(adapter);
        } else {
            adapter.setData(hotLines);
        }

        moreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterSkuListActivity.Params params = new FilterSkuListActivity.Params();
                if (paramsData != null) {
                    params.id = paramsData.id;
                    params.cityHomeType = paramsData.cityHomeType;
                    params.titleName = paramsData.titleName;
                }
                switch (type) {
                    case TYPE_DEEP://2天以上行程
                        params.days = "-1";
                        break;
                    case TYPE_SHORT://1或2天行程
                        params.days = "1,2";
                        break;
                }
                Intent intent = new Intent(v.getContext(), FilterSkuListActivity.class);
                String source = "";
                if (v.getContext() instanceof CityListActivity) {
                    source = ((CityListActivity) v.getContext()).getEventSource();
                } else if (paramsData != null) {
                    switch (paramsData.cityHomeType) {
                        case CITY:
                            source = "城市";
                            break;
                        case ROUTE:
                            source = "线路圈";
                            break;
                        case COUNTRY:
                            source = "国家";
                            break;
                    }
                }
                intent.putExtra(Constants.PARAMS_SOURCE, source);
                intent.putExtra(Constants.PARAMS_DATA, params);
                v.getContext().startActivity(intent);
            }
        });
    }

    public String getTitle() {
        String result = "";
        switch (type) {
            case TYPE_HOT:
                result = CommonUtils.getString(R.string.city_sku_item_hot);
                break;
            case TYPE_DEEP:
                result = CommonUtils.getString(R.string.city_sku_item_deep);
                break;
            case TYPE_SHORT:
                result = CommonUtils.getString(R.string.city_sku_item_short);
                break;
        }
        return result;
    }
}
