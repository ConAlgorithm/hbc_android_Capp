package com.hugboga.custom.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.DestinationHotItemBean;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

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

    public void init(DestinationHotItemBean bean, int listWidth) {
        this.bean = bean;
        resetWidthImg(listWidth); //重置图片宽和高
        Tools.showImage(search_hot_city_img, bean.destinationImageUrl, R.color.default_pic_gray);
        search_hot_city_title.setText(bean.destinationName);
    }

    /**
     * 重置图片宽度和高度
     * @param listWidth
     */
    private void resetWidthImg(int listWidth) {
        ViewGroup.LayoutParams layoutParams = search_hot_city_img.getLayoutParams();
        int width = (listWidth - UIUtils.dip2px(10)) / 2;
        layoutParams.width = width;
        layoutParams.height = width;
        search_hot_city_img.setLayoutParams(layoutParams);
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
