package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.activity.FilterSkuListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.widget.CityListHotView;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.HotLinesItemView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

public class CityListHotAdapter extends RecyclerView.Adapter<CityListHotAdapter.MyViewHolder>{

    private static final int TYPE_ITEM = 0x01;
    private static final int TYPE_MORE = 0x02;

    private Context mContext;
    private List<SkuItemBean> itemList;
    public int type;
    public CityActivity.Params paramsData;
    private int displayImgWidth;
    private int displayImgHeight;

    public CityListHotAdapter(Context context, CityActivity.Params paramsData, List<SkuItemBean> _itemList, int type, int displayImgWidth, int displayImgHeight) {
        this.mContext = context;
        this.itemList = _itemList;
        this.paramsData = paramsData;
        this.type = type;
        this.displayImgWidth = displayImgWidth;
        this.displayImgHeight = displayImgHeight;
    }

    public void setData(List<SkuItemBean> _itemList) {
        this.itemList = _itemList;
        notifyDataSetChanged();
    }

    @Override
    public CityListHotAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch (viewType) {
            case TYPE_ITEM:
                HotLinesItemView hotLinesItemView = new HotLinesItemView(mContext);
                hotLinesItemView.setImageBound(displayImgWidth, displayImgHeight);
                itemView = hotLinesItemView;
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(displayImgWidth, displayImgHeight + ScreenUtil.dip2px(84));
                itemView.setLayoutParams(params);
                break;
            case TYPE_MORE:
                FrameLayout frameLayout = new FrameLayout(mContext);
                ImageView moreIV = new ImageView(mContext);
                moreIV.setBackgroundResource(R.mipmap.home_more);
                FrameLayout.LayoutParams moreIVParams= new FrameLayout.LayoutParams(displayImgWidth, displayImgHeight);
                frameLayout.addView(moreIV, moreIVParams);
                itemView = frameLayout;
                break;
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CityListHotAdapter.MyViewHolder holder, int position) {
        if (position == getItemCount() -1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FilterSkuListActivity.Params params = new FilterSkuListActivity.Params();
                    if (paramsData != null) {
                        params.id = paramsData.id;
                        params.cityHomeType = paramsData.cityHomeType;
                        params.titleName = paramsData.titleName;
                    }
                    switch (type) {
                        case CityListHotView.TYPE_DEEP://2天以上行程
                            params.days = "-1";
                            break;
                        case CityListHotView.TYPE_SHORT://1或2天行程
                            params.days = "1,2";
                            break;
                    }
                    Intent intent = new Intent(v.getContext(), FilterSkuListActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    v.getContext().startActivity(intent);
                }
            });
        } else {
            ((HbcViewBehavior) holder.itemView).update(itemList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() -1) {
            return TYPE_MORE;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }
}