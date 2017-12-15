package com.hugboga.custom.adapter.viewholder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索城市列表页面
 * Created by HONGBO on 2017/12/15 15:40.
 */

public class SearchCityVH extends RecyclerView.ViewHolder {

    @BindView(R.id.city_name)
    TextView name;
    @BindView(R.id.right_img)
    ImageView image;
    @BindView(R.id.city_img)
    ImageView cityImg;
    @BindView(R.id.middle_line)
    TextView middle_line;
    @BindView(R.id.has_sub_img)
    ImageView has_sub_img;
    @BindView(R.id.city_selected_img)
    ImageView city_selected_img;

    SearchGroupBean bean;
    boolean middleLineShow;
    boolean isFilter;
    CityActivity.Params cityParams;

    public SearchCityVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void init(int flag, SearchGroupBean bean,int position,boolean middleLineShow,boolean isFilter,CityActivity.Params cityParams) {
        this.bean = bean;
        this.middleLineShow = middleLineShow;
        this.isFilter = isFilter;
        this.cityParams = cityParams;
        if (flag == 1) {
            name.setText(getName(position, flag));
            if (bean.isSelected) {
                itemView.setBackgroundColor(Color.parseColor("#fcd633"));
                image.setVisibility(View.VISIBLE);
                image.setImageResource(R.mipmap.search_triangle);
                name.setTextColor(Color.parseColor("#ffffff"));
            } else {
                itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                image.setVisibility(View.GONE);
                name.setTextColor(Color.parseColor("#666666"));
            }
        } else if (flag == 2) {

            if (bean.flag == 4) {
                middle_line.setVisibility(View.GONE);
                if (bean.spot_id == -1) {
                    name.setText("");
                    cityImg.setVisibility(View.VISIBLE);
                    cityImg.setImageResource(R.mipmap.search_transfer);
                } else if (bean.spot_id == -2) {
                    name.setText("");
                    cityImg.setVisibility(View.VISIBLE);
                    cityImg.setImageResource(R.mipmap.search_single);
                } else if (bean.spot_id == -3) {
                    name.setText("");
                    cityImg.setVisibility(View.VISIBLE);
                    cityImg.setImageResource(R.mipmap.search_car);
                } else {
                    middle_line.setVisibility(middleLineShow ? View.VISIBLE : View.GONE);
                    cityImg.setVisibility(View.GONE);
                    name.setText(getName(position, bean.flag));
                }
                itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                if (bean.isSelected) {
                    name.setTextColor(Color.parseColor("#fcd633"));
                } else {
                    name.setTextColor(Color.parseColor("#111111"));
                }

            } else {
                middle_line.setVisibility(middleLineShow ? View.VISIBLE : View.GONE);
                name.setText(getName(position, flag));
                itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                if (bean.isSelected) {
                    name.setTextColor(Color.parseColor("#fcd633"));
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.mipmap.search_triangle2);
                } else {
                    name.setTextColor(Color.parseColor("#111111"));
                    image.setVisibility(View.GONE);
                }
            }
        } else if (flag == 3) {
            middle_line.setVisibility(View.VISIBLE);
            if (position == 0) {
                name.setText("全境");
            } else {
                name.setText(getName(position, flag));
            }
            itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            if (bean.isSelected) {
                name.setTextColor(Color.parseColor("#fcd633"));
            } else {
                name.setTextColor(Color.parseColor("#111111"));
            }
        }

        if (bean.has_sub == 1 && flag == 2 /*&& position != 0*/ && !bean.isSelected) {
            has_sub_img.setVisibility(View.VISIBLE);
        } else {
            has_sub_img.setVisibility(View.GONE);
        }

        boolean isShowSelectedImg = (flag == 2 || flag == 3) && (bean.has_sub != 1 || flag == 3 && position == 0);

        boolean isSelectedItem = isSelectedItem(position, flag);
        if ((isShowSelectedImg && isFilter) || (isSelectedItem && isShowSelectedImg)) {
            if (isSelectedItem && isShowSelectedImg) {
                bean.isSelected = true;
                name.setTextColor(Color.parseColor("#fcd633"));
            } else {
                bean.isSelected = false;
                name.setTextColor(Color.parseColor("#111111"));
            }
            if (bean.isSelected) {
                name.setPadding(UIUtils.dip2px(20), 0, UIUtils.dip2px(18), 0);
                city_selected_img.setVisibility(View.VISIBLE);
            } else {
                name.setPadding(UIUtils.dip2px(20), 0, UIUtils.dip2px(5), 0);
                city_selected_img.setVisibility(View.GONE);
            }
        }
    }

    public String getName(int position, int flag) {
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

    public boolean isSelectedItem(int position, int _flag) {
        if (cityParams == null) {
            return false;
        }
        int flag = bean.flag == 4 ? 4 : _flag;
        if (flag == 1 && cityParams.cityHomeType == CityActivity.CityHomeType.ROUTE) {
            return bean.group_id == cityParams.id;
        } else if (flag == 2) {
            if (bean.type == 1 && cityParams.cityHomeType == CityActivity.CityHomeType.ROUTE) {
                return bean.group_id == cityParams.id;
            } else if (bean.type == 2 && cityParams.cityHomeType == CityActivity.CityHomeType.COUNTRY) {
                return bean.sub_place_id == cityParams.id;
            } else if (bean.type == 3 && cityParams.cityHomeType == CityActivity.CityHomeType.CITY) {
                return bean.sub_city_id == cityParams.id;
            }
        } else if (flag == 3) {
            if (bean.type == 1 && cityParams.cityHomeType == CityActivity.CityHomeType.ROUTE) {
                return bean.group_id == cityParams.id;
            } else if (bean.type == 2 && cityParams.cityHomeType == CityActivity.CityHomeType.COUNTRY) {
                return bean.sub_place_id == cityParams.id;
            } else if (bean.type == 3 && cityParams.cityHomeType == CityActivity.CityHomeType.CITY){
                return bean.sub_city_id == cityParams.id;
            }
        } else if (flag == 4) {
            if (bean.type == 1 && cityParams.cityHomeType == CityActivity.CityHomeType.CITY) {
                return bean.spot_id == cityParams.id;
            } else if (bean.type == 2 && cityParams.cityHomeType == CityActivity.CityHomeType.COUNTRY) {
                return bean.spot_id == cityParams.id;
            } else if (cityParams.cityHomeType == CityActivity.CityHomeType.ALL) {
                return bean.spot_id ==cityParams.id;
            }
        }
        return false;
    }
}
