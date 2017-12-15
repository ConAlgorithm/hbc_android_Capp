package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.Tools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/9/11.
 */

public class SearchHotCity extends LinearLayout {

    @BindView(R.id.hot_city_layout)
    LinearLayout hotCityLayout;

    List<SearchGroupBean> cityList;
    private int column = 2;
    private LayoutInflater inflater;

    public SearchHotCity(Context context) {
        this(context, null);
    }

    public SearchHotCity(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.search_hot_city, this);
        ButterKnife.bind(view);
        //获取LayoutInflater对象
        inflater = LayoutInflater.from(getContext());
    }

    public void setHotCitys(List<SearchGroupBean> cityList) {
        this.cityList = cityList;
        displayColumns(cityList.size());
    }

    /**
     * 生成多行多列的线性布局
     *
     * @param displayNumber 需要生成的个数，这里是需要生成的TextView的总个数
     */
    private void displayColumns(int displayNumber) {
        if (hotCityLayout != null) {
            if (hotCityLayout.getChildCount() > 0) {
                hotCityLayout.removeAllViews();
            }
        }
        if (cityList == null || cityList.size() == 0) {
            return;
        }
        //判断
        if (displayNumber <= 0) {
            return;
        }
        //判断是否被每行显示的列数整除
        boolean boo = false;
        //获取列数
        int rows = displayNumber / column;
        //判断，得到是否被每行显示的列数整除
        if (displayNumber % column == 0) {
            boo = true;
        } else {
            boo = false;
        }

        if (boo) {
            //如果能够被整除
            for (int i = 0; i < rows; i++) {
                addLinear(i * column);
            }
            return;
        } else {
            //如果不能够被整除
            for (int i = 0; i < rows; i++) {
                //createLinear(column);
                addLinear(i * column);
            }
            //用%，得到最后剩下的，不足一行的
            rows = displayNumber % column;
            //创建布局一行的布局
            addLastSingleLinear(cityList.size() - 1);
        }

    }

    private void addLinear(final int row) {
        if (cityList != null && cityList.size() > 0 && row < cityList.size()) {
            LinearLayout view = (LinearLayout) inflater.inflate(R.layout.hot_search_textview, null, false);
            TextView text1 = (TextView) view.findViewById(R.id.text1);
            ImageView img1 = view.findViewById(R.id.text1Image);
            Tools.showImageNotCenterCrop(img1, "", R.mipmap.ic_launcher);
            text1.setText(getName(row, cityList.get(row).flag));
            text1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    goCityList(cityList.get(row));
                }
            });
            TextView text2 = (TextView) view.findViewById(R.id.text2);
            ImageView img2 = view.findViewById(R.id.text2Image);
            Tools.showImageNotCenterCrop(img2, "", R.mipmap.ic_launcher);
            text2.setText(getName(row + 1, cityList.get(row + 1).flag));
            text2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    goCityList(cityList.get(row + 1));
                }
            });
            hotCityLayout.addView(view);
        }
    }

    private void addLastSingleLinear(final int row) {
        if (cityList != null && cityList.size() > 0 && row < cityList.size()) {
            LinearLayout view = (LinearLayout) inflater.inflate(R.layout.hot_search_textview, null, false);
            view.setGravity(Gravity.LEFT);
            TextView text1 = (TextView) view.findViewById(R.id.text1);
            ImageView img1 = view.findViewById(R.id.text1Image);
            Tools.showImageNotCenterCrop(img1, "", R.mipmap.ic_launcher);
            text1.setText(getName(row, cityList.get(row).flag));
            text1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    goCityList(cityList.get(row));
                }
            });
            TextView text2 = (TextView) view.findViewById(R.id.text2);
            ImageView img2 = view.findViewById(R.id.text2Image);
            img2.setVisibility(View.GONE);
            text2.setBackgroundColor(getContext().getResources().getColor(R.color.basic_white));
            text2.setEnabled(false);
            text2.setText("");
            hotCityLayout.addView(view);
        }
    }


    public String getName(int position, int flag) {
        if (flag == 2) {
            if (cityList.get(position).type == 1) {
                return cityList.get(position).group_name;
            } else if (cityList.get(position).type == 2) {
                return cityList.get(position).sub_place_name;
            } else if (cityList.get(position).type == 3) {
                return cityList.get(position).sub_city_name;
            }
        } else if (flag == 3) {
            if (cityList.get(position).type == 1) {
                return cityList.get(position).group_name;
            } else if (cityList.get(position).type == 2) {
                return cityList.get(position).sub_place_name;
            } else if (cityList.get(position).type == 3) {
                return cityList.get(position).sub_city_name;
            }
        } else if (flag == 1) {
            return cityList.get(position).group_name;
        } else if (flag == 4) {
            return cityList.get(position).spot_name;
        }
        return "";
    }

    private void goCityList(SearchGroupBean searchGroupBean) {
        CityUtils.addCityHistoryData(searchGroupBean);
        CityActivity.Params params = new CityActivity.Params();

        if (searchGroupBean.flag == 1) {
            params.id = searchGroupBean.group_id;
            params.cityHomeType = CityActivity.CityHomeType.ROUTE;
            params.titleName = searchGroupBean.group_name;
        } else if (searchGroupBean.flag == 2) {
            if (searchGroupBean.type == 1) {
                params.id = searchGroupBean.group_id;
                params.cityHomeType = CityActivity.CityHomeType.ROUTE;
                params.titleName = searchGroupBean.group_name;
            } else if (searchGroupBean.type == 2) {
                params.id = searchGroupBean.sub_place_id;
                params.titleName = searchGroupBean.sub_place_name;
                params.cityHomeType = CityActivity.CityHomeType.COUNTRY;
            } else {
                params.id = searchGroupBean.sub_city_id;
                params.cityHomeType = CityActivity.CityHomeType.CITY;
                params.titleName = searchGroupBean.sub_city_name;
            }
        } else if (searchGroupBean.flag == 3) {
            if (searchGroupBean.sub_city_name.equalsIgnoreCase(getContext().getResources().getString(R.string.destiation_all))) {
                params.id = searchGroupBean.sub_city_id;
                params.cityHomeType = CityActivity.CityHomeType.COUNTRY;
                params.titleName = searchGroupBean.sub_place_name;
            } else if (searchGroupBean.type == 1) {
                params.id = searchGroupBean.group_id;
                params.cityHomeType = CityActivity.CityHomeType.ROUTE;
                params.titleName = searchGroupBean.group_name;
            } else if (searchGroupBean.type == 2) {
                params.id = searchGroupBean.sub_place_id;
                params.titleName = searchGroupBean.sub_place_name;
                params.cityHomeType = CityActivity.CityHomeType.COUNTRY;
            } else {
                params.id = searchGroupBean.sub_city_id;
                params.cityHomeType = CityActivity.CityHomeType.CITY;
                params.titleName = searchGroupBean.sub_city_name;
            }
        } else if (searchGroupBean.flag == 4) {
            params.id = searchGroupBean.spot_id;
            if (searchGroupBean.type == 1) {
                params.cityHomeType = CityActivity.CityHomeType.CITY;
                params.titleName = searchGroupBean.spot_name;
            } else if (searchGroupBean.type == 2) {
                params.cityHomeType = CityActivity.CityHomeType.COUNTRY;
                params.titleName = searchGroupBean.spot_name;
            }
        }
        Intent intent = new Intent(getContext(), CityActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        getContext().startActivity(intent);
    }

    public String getEventSource() {
        return getContext().getResources().getString(R.string.destiation_search);
    }
}
