package com.hugboga.custom.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.DestinationHotItemBean;
import com.hugboga.custom.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 热门城市显示
 * Created by HONGBO on 2017/12/15 16:30.
 */

public class SearchHotCityVH extends RecyclerView.ViewHolder {

    @BindView(R.id.search_hot_city_img)
    ImageView search_hot_city_img;
    @BindView(R.id.search_hot_city_title)
    TextView search_hot_city_title;

    DestinationHotItemBean bean;

    public SearchHotCityVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(onClickListener);
    }

    public void init(DestinationHotItemBean bean) {
        this.bean = bean;
        Tools.showImage(search_hot_city_img, bean.destinationImageUrl, R.color.default_pic_gray);
        search_hot_city_title.setText(bean.destinationName);
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(DestinationHotItemBean bean);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(bean);
            }
        }
    };
}
