package com.hugboga.custom.models;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.activity.FilterSkuListActivity;
import com.hugboga.custom.adapter.CityListHotAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.SpaceItemDecoration;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class CityListHotModel extends EpoxyModelWithHolder {

    public static final int TYPE_HOT = 1;
    public static final int TYPE_DEEP = 2;
    public static final int TYPE_SHORT = 3;

    public List<SkuItemBean> hotLines;
    public int type;
    public CityListActivity.Params paramsData;

    public void setDate(CityListActivity.Params paramsData, List<SkuItemBean> hotLines, int type) {
        this.paramsData = paramsData;
        this.hotLines = hotLines;
        this.type = type;
    }

    @Override
    protected HotExplorationHolder createNewHolder() {
        return new HotExplorationHolder();
    }

    @Override
    public void bind(EpoxyHolder _holder) {
        super.bind(_holder);
        HotExplorationHolder holder = (HotExplorationHolder) _holder;
        holder.titleTV.setText(getTitle());

        final int paddingLeft = holder.titleTV.getContext().getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        int displayImgWidth = UIUtils.getScreenWidth() - paddingLeft * 2 - UIUtils.dip2px(8);
        int displayImgHeight = (int)((400 / 650.0) * displayImgWidth);
        int viewHeight = displayImgHeight + ScreenUtil.dip2px(84);
        holder.cityRecyclerView.getLayoutParams().height = viewHeight;

        holder.cityRecyclerView.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.cityRecyclerView.setLayoutManager(layoutManager);
        holder.cityRecyclerView.setHorizontalScrollBarEnabled(false);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration();
        itemDecoration.setItemOffsets(paddingLeft, 0, 0, 0, LinearLayout.HORIZONTAL);
        holder.cityRecyclerView.addItemDecoration(itemDecoration);
        CityListHotAdapter adapter = new CityListHotAdapter(holder.itemView.getContext(), paramsData, hotLines, type, displayImgWidth, displayImgHeight);
        holder.cityRecyclerView.setAdapter(adapter);

        holder.moreTV.setOnClickListener(new View.OnClickListener() {
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
                intent.putExtra(Constants.PARAMS_DATA, params);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_city_hot_item;
    }

    static class HotExplorationHolder extends EpoxyHolder {
        @Bind(R.id.city_hot_title_tv)
        TextView titleTV;
        @Bind(R.id.city_hot_more_tv)
        TextView moreTV;
        @Bind(R.id.city_hot_recyclerview)
        RecyclerView cityRecyclerView;

        View itemView;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
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