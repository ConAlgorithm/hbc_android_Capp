package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.adapter.viewholder.SearchHotCityVH;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.DestinationHotItemBean;

import java.util.List;

/**
 * Created by HONGBO on 2017/12/15 16:29.
 */

public class SearchHotCityAdapter extends RecyclerView.Adapter<SearchHotCityVH> {

    Context mContext;
    List<DestinationHotItemBean> data;

    public SearchHotCityAdapter(Context mContext, List<DestinationHotItemBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public SearchHotCityVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchHotCityVH(LayoutInflater.from(mContext).inflate(R.layout.hot_search_textview, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchHotCityVH holder, int position) {
        holder.init(data.get(position));
        holder.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    SearchHotCityVH.OnItemClickListener onItemClickListener = new SearchHotCityVH.OnItemClickListener() {
        @Override
        public void onItemClick(DestinationHotItemBean bean) {
            Intent intent = new Intent(mContext, CityActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            CityActivity.Params params = new CityActivity.Params();
            params.id = bean.destinationId;
            params.cityHomeType = CityActivity.CityHomeType.getNew(bean.destinationType);
            params.titleName = bean.destinationName;
            intent.putExtra(Constants.PARAMS_DATA, params);
            intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
            mContext.startActivity(intent);
        }
    };

    public String getEventSource() {
        return mContext.getResources().getString(R.string.destiation_search);
    }
}
