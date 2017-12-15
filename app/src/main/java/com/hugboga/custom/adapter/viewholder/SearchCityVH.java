package com.hugboga.custom.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.data.bean.SearchGroupBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索城市列表页面
 * Created by HONGBO on 2017/12/15 15:40.
 */

public class SearchCityVH extends RecyclerView.ViewHolder {

    @BindView(R.id.search_city_item_parent)
    RelativeLayout parentLayout;
    @BindView(R.id.city_name)
    TextView tvName;
    @BindView(R.id.search_city_item_tag)
    ImageView search_city_item_tag;
    @BindView(R.id.has_sub_img)
    ImageView has_sub_img;

    SearchGroupBean bean;
    boolean middleLineShow;
    CityActivity.Params cityParams;

    public SearchCityVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(bean);
                }
            }
        });
    }

    public void init(int flag, SearchGroupBean bean, int position, boolean middleLineShow, CityActivity.Params cityParams) {
        this.bean = bean;
        this.middleLineShow = middleLineShow;
        this.cityParams = cityParams;
        String nameStr = getName(flag);
        tvName.setText(nameStr);

        if (flag == 1) {
            search_city_item_tag.setVisibility(bean.isSelected ? View.VISIBLE : View.GONE);
        }
        if (flag == 1 || flag == 2) {
            parentLayout.setSelected(bean.isSelected);
        }
        tvName.setSelected(bean.isSelected);

        if (flag == 3 && position == 0) {
            tvName.setText("全境");
        }

        if (bean.has_sub == 1 && flag == 2 && !bean.isSelected) {
            has_sub_img.setVisibility(View.VISIBLE);
        } else {
            has_sub_img.setVisibility(View.GONE);
        }
    }

    public String getName(int flag) {
        if (flag == 2) {
            if (bean.type == 1) {
                return bean.group_name;
            } else if (bean.type == 2) {
                return bean.sub_place_name;
            } else if (bean.type == 3) {
                return bean.sub_city_name;
            }
        } else if (flag == 3) {
            if (bean.type == 1) {
                return bean.group_name;
            } else if (bean.type == 2) {
                return bean.sub_place_name;
            } else if (bean.type == 3) {
                return bean.sub_city_name;
            }
        } else if (flag == 1) {
            return bean.group_name;
        } else if (flag == 4) {
            return bean.spot_name;
        }
        return "";
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(SearchGroupBean bean);
    }
}
