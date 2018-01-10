package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.DestinationHotItemBean;
import com.hugboga.custom.data.bean.DestinationTabItemBean;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DestinationServiceview;
import com.hugboga.custom.widget.DestinationTagView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/10.
 */
public class DestinationAggModel extends EpoxyModelWithHolder {

    private List<DestinationHotItemBean> hotCitys;
    private DestinationTabItemBean lineGroup;
    int position = 0;

    public DestinationAggModel(List<DestinationHotItemBean> hotCity, int position) {
        this.hotCitys = hotCity;
        this.position = position;
    }

    public DestinationAggModel(DestinationTabItemBean lineGroup, int position) {
        this.lineGroup = lineGroup;
        this.position = position;
    }

    @Override
    protected DestinationViewHolder createNewHolder() {
        return new DestinationViewHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_dest_item;
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        DestinationViewHolder destinationViewHolder = (DestinationViewHolder) holder;
        if (this.position == 0) {
            destinationViewHolder.destinationServiceview.setVisibility(View.VISIBLE);
        } else {
            destinationViewHolder.destinationServiceview.setVisibility(View.GONE);
        }
        if (hotCitys != null) {
            renderHotCity(destinationViewHolder);
        }
        if (lineGroup != null) {
            renderLineGrounp(destinationViewHolder);
        }
    }

    @Override
    public void unbind(EpoxyHolder holder) {
        super.unbind(holder);
    }

    private void renderHotCity(DestinationViewHolder destinationViewHolder) {
        destinationViewHolder.titleLayout.setVisibility(View.GONE);
        destinationViewHolder.cityGridView.setVisibility(View.VISIBLE);
        setCityGridParams(destinationViewHolder.cityGridView, hotCitys);
        destinationViewHolder.countryGridView.setVisibility(View.GONE);
        destinationViewHolder.countryTextLabel.setVisibility(View.GONE);
        destinationViewHolder.countryTextLabelLayout.setVisibility(View.GONE);
        destinationViewHolder.viewBelowId.setVisibility(View.VISIBLE);
        destinationViewHolder.cityMoreLayout.setVisibility(View.GONE);
        destinationViewHolder.tagMoreLayout.setVisibility(View.GONE);
        destinationViewHolder.containeLayout.setVisibility(View.GONE);
    }

    private void renderLineGrounp(final DestinationViewHolder destinationViewHolder) {
        destinationViewHolder.viewBelowId.setVisibility(View.GONE);
        if (lineGroup.destinationList != null && lineGroup.destinationList.size() > 0) {
            destinationViewHolder.titleLayout.setVisibility(View.VISIBLE);
            destinationViewHolder.cityGridView.setVisibility(View.VISIBLE);
            setDestinationData(destinationViewHolder.cityGridView, lineGroup.destinationList, false);

            if (lineGroup.destinationList.size() > 6) {
                destinationViewHolder.cityMoreTV.setVisibility(View.VISIBLE);
                destinationViewHolder.cityMoreIV.setVisibility(View.VISIBLE);
                destinationViewHolder.cityMoreLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isUnfoldMoreDestination = !isUnfoldMoreDestination;
                        if (isUnfoldMoreDestination) {
                            destinationViewHolder.cityMoreTV.setText("点击收起");
                            destinationViewHolder.cityMoreIV.setBackgroundResource(R.mipmap.share_withdraw);
                            //点击展开更多埋点
                            SensorsUtils.onAppClick("目的地", "目的地", "展开更多", "");
                        } else {
                            destinationViewHolder.cityMoreTV.setText("展开更多");
                            destinationViewHolder.cityMoreIV.setBackgroundResource(R.mipmap.share_unfold);
                        }
                        setDestinationData(destinationViewHolder.cityGridView, lineGroup.destinationList, isUnfoldMoreDestination);
                    }
                });
            } else {
                destinationViewHolder.cityMoreTV.setVisibility(View.GONE);
                destinationViewHolder.cityMoreIV.setVisibility(View.GONE);
            }
        } else {
            destinationViewHolder.titleLayout.setVisibility(View.GONE);
            destinationViewHolder.cityGridView.setVisibility(View.GONE);
            destinationViewHolder.cityMoreLayout.setVisibility(View.GONE);
        }

        if (lineGroup.topTagGroupList != null && lineGroup.topTagGroupList.size() > 0) {
            destinationViewHolder.containeLayout.removeAllViews();
            destinationViewHolder.containeLayout.setVisibility(View.VISIBLE);
            destinationViewHolder.tagMoreLayout.setVisibility(View.VISIBLE);
            if (lineGroup.topTagGroupList.size() > 1) {
                destinationViewHolder.tagMoreTV.setVisibility(View.VISIBLE);
                destinationViewHolder.tagMoreIV.setVisibility(View.VISIBLE);
                destinationViewHolder.tagMoreLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isUnfoldMoreTag = !isUnfoldMoreTag;
                        if (isUnfoldMoreTag) {
                            destinationViewHolder.tagMoreTV.setText("点击收起");
                            destinationViewHolder.tagMoreIV.setBackgroundResource(R.mipmap.share_withdraw);
                        } else {
                            destinationViewHolder.tagMoreTV.setText("展开更多");
                            destinationViewHolder.tagMoreIV.setBackgroundResource(R.mipmap.share_unfold);
                        }
                        setTagData(destinationViewHolder.containeLayout, lineGroup.topTagGroupList, isUnfoldMoreTag);
                    }
                });
            } else {
                destinationViewHolder.tagMoreTV.setVisibility(View.GONE);
                destinationViewHolder.tagMoreIV.setVisibility(View.GONE);
            }
            setTagData(destinationViewHolder.containeLayout, lineGroup.topTagGroupList, false);
        } else {
            destinationViewHolder.containeLayout.removeAllViews();
            destinationViewHolder.containeLayout.setVisibility(View.GONE);
            destinationViewHolder.tagMoreTV.setVisibility(View.GONE);
            destinationViewHolder.tagMoreIV.setVisibility(View.GONE);
            destinationViewHolder.tagMoreLayout.setVisibility(View.GONE);
        }

        if (lineGroup.countryList != null && lineGroup.countryList.size() > 0) {
            int lineGroupCountrySize = lineGroup.countryList.size();
            destinationViewHolder.countryGridView.setVisibility(View.VISIBLE);
            destinationViewHolder.countryTextLabelLayout.setVisibility(View.VISIBLE);
            destinationViewHolder.countryTextLabel.setVisibility(View.VISIBLE);
            destinationViewHolder.countryTextLabel.setText(lineGroupCountrySize + "个国家/地区");
            handlerCountryGridView(destinationViewHolder.countryGridView);
        } else {
            destinationViewHolder.countryGridView.setVisibility(View.GONE);
            destinationViewHolder.countryTextLabel.setVisibility(View.GONE);
            destinationViewHolder.countryTextLabelLayout.setVisibility(View.GONE);
        }
    }

    private void handlerCountryGridView(GridView countryGridView) {
        setCountryGridParams(countryGridView, lineGroup.countryList);
    }

    boolean isUnfoldMoreTag;

    private void setTagData(LinearLayout containeLayout, List<DestinationTabItemBean.TagBean> topTagGroupList, boolean isUnfoldMoreTag) {
        this.isUnfoldMoreTag = isUnfoldMoreTag;
        if (containeLayout.getChildCount() != topTagGroupList.size()) {
            int size = topTagGroupList.size();
            for (int i = 0; i < size; i++) {
                DestinationTagView destinationTagView = new DestinationTagView(containeLayout.getContext());
                destinationTagView.setData(topTagGroupList.get(i));
                containeLayout.addView(destinationTagView);
            }
        }
        int childCount = containeLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (isUnfoldMoreTag) {
                containeLayout.getChildAt(i).setVisibility(View.VISIBLE);
            } else {
                containeLayout.getChildAt(i).setVisibility(i == 0 ? View.VISIBLE : View.GONE);
            }
        }
    }

    CityAdapter destinationAdapter;
    boolean isUnfoldMoreDestination;

    private void setDestinationData(GridView gridView, List<DestinationHotItemBean> cities, boolean isUnfoldMoreDestination) {
        this.isUnfoldMoreDestination = isUnfoldMoreDestination;
        if (destinationAdapter == null) {
            destinationAdapter = new CityAdapter(gridView.getContext());
            int gridWidth = (UIUtils.screenWidth - UIUtils.dip2px(6 * 2 + 15 * 2)) / 3;
            gridView.setColumnWidth(gridWidth);
            gridView.setNumColumns(3);
            gridView.setAdapter(destinationAdapter);
        }
        destinationAdapter.setData(cities, isUnfoldMoreDestination);
    }

    private void setCityGridParams(GridView gridView, List<DestinationHotItemBean> cities) {
        CityAdapter cityAdapter = new CityAdapter(cities, gridView.getContext());
        int gridWidth = (UIUtils.screenWidth - UIUtils.dip2px(6 * 2 + 15 * 2)) / 3;
        gridView.setColumnWidth(gridWidth);
        gridView.setNumColumns(3);
        gridView.setAdapter(cityAdapter);
    }

    private void setCountryGridParams(GridView gridView, List<DestinationTabItemBean.CountryItemBean> countries) {
        CountryAdapter countryAdapter = new CountryAdapter(countries, gridView.getContext());
        int gridWidth = (UIUtils.screenWidth - UIUtils.dip2px(6 * 2 + 15 * 2)) / 3;
        gridView.setColumnWidth(gridWidth);
        gridView.setNumColumns(3);
        gridView.setAdapter(countryAdapter);
    }

    static class DestinationViewHolder extends EpoxyHolder {

        View itemView;
        @BindView(R.id.home_dest_title_layout)
        LinearLayout titleLayout;
        @BindView(R.id.home_dest_city_gridview)
        GridView cityGridView;
        @BindView(R.id.home_dest_country_text_label)
        TextView countryTextLabel;
        @BindView(R.id.home_dest_country_text_layout)
        LinearLayout countryTextLabelLayout;
        @BindView(R.id.home_dest_country_gridview)
        GridView countryGridView;
        @BindView(R.id.des_service)
        DestinationServiceview destinationServiceview;
        @BindView(R.id.view_below_id)
        View viewBelowId;

        @BindView(R.id.home_dest_city_more_layout)
        RelativeLayout cityMoreLayout;
        @BindView(R.id.home_dest_city_more_tv)
        TextView cityMoreTV;
        @BindView(R.id.home_dest_city_more_iv)
        ImageView cityMoreIV;

        @BindView(R.id.home_dest_city_containe_layout)
        LinearLayout containeLayout;

        @BindView(R.id.view_destination_tag_more_tv)
        TextView tagMoreTV;
        @BindView(R.id.view_destination_tag_more_iv)
        ImageView tagMoreIV;
        @BindView(R.id.view_destination_tag_more_layout)
        RelativeLayout tagMoreLayout;

        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    class CityAdapter extends BaseAdapter {

        private List<DestinationHotItemBean> hotCityList;
        private Context mContext;

        public CityAdapter(List<DestinationHotItemBean> hotCityList, Context context) {
            this.hotCityList = hotCityList;
            this.mContext = context;
        }

        public CityAdapter(Context context) {
            this.mContext = context;
            this.hotCityList = new ArrayList<DestinationHotItemBean>();
        }

        public void setData(List<DestinationHotItemBean> _hotCityList, boolean isUnfold) {
            hotCityList.clear();
            if (isUnfold) {
                hotCityList.addAll(_hotCityList);
            } else {
                int itemSize = Math.min(6, _hotCityList.size());
                for (int i = 0; i < itemSize; i++) {
                    hotCityList.add(_hotCityList.get(i));
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return hotCityList == null ? 0 : hotCityList.size();
        }

        @Override
        public Object getItem(int position) {
            return hotCityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CityViewHolder cityViewHolder;
            if (convertView == null) {
                cityViewHolder = new CityViewHolder();
                convertView = LayoutInflater.from(mContext).
                        inflate(R.layout.home_dest_gridcity_item, null);
                ButterKnife.bind(cityViewHolder, convertView);
                convertView.setTag(cityViewHolder);
            } else {
                cityViewHolder = (CityViewHolder) convertView.getTag();
            }
            final DestinationHotItemBean hotCity = hotCityList.get(position);
            cityViewHolder.cityGuideCount.setVisibility(CommonUtils.getCountInteger(hotCity.guideCount) == 0 ? View.INVISIBLE : View.VISIBLE);
            cityViewHolder.cityGuideCount.setText(hotCity.guideCount + "位司导");
            cityViewHolder.cityName.setText(hotCity.destinationName);
            int gridWidth = (UIUtils.screenWidth - UIUtils.dip2px(6 * 2 + 15 * 2)) / 3;
            cityViewHolder.cityPicture.getLayoutParams().width = gridWidth;
            cityViewHolder.cityPicture.getLayoutParams().height = gridWidth;
            //cityViewHolder.filterPictureView.getLayoutParams().width = gridWidth;
            //cityViewHolder.filterPictureView.getLayoutParams().height =  gridWidth * 80 / 110;
            Tools.showImage(cityViewHolder.cityPicture, hotCity.destinationImageUrl, R.mipmap.home_default_route_item);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    CityActivity.Params params = new CityActivity.Params();
                    Intent intent = new Intent(v.getContext(), CityActivity.class);
                    bundle.putSerializable(Constants.PARAMS_SOURCE, "目的地");
                    params.cityHomeType = CityActivity.CityHomeType.getNew(hotCity.destinationType);
                    params.titleName = hotCity.destinationName;
                    params.id = hotCity.destinationId;
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra(Constants.PARAMS_SOURCE, "目的地");
                    mContext.startActivity(intent);
                    StatisticClickEvent.click(StatisticConstant.LAUNCH_CITY, "目的地");
                }
            });
            return convertView;
        }
    }

    class CountryAdapter extends BaseAdapter {

        private List<DestinationTabItemBean.CountryItemBean> hotCountrys;
        private Context mContext;

        public CountryAdapter(List<DestinationTabItemBean.CountryItemBean> hotCountrys, Context context) {
            this.hotCountrys = hotCountrys;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return hotCountrys == null ? 0 : hotCountrys.size();
        }

        @Override
        public Object getItem(int position) {
            return hotCountrys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CountryViewHolder countryViewHolder;
            if (convertView == null) {
                countryViewHolder = new CountryViewHolder();
                convertView = LayoutInflater.from(mContext).
                        inflate(R.layout.home_dest_gridcountry_item, null);
                ButterKnife.bind(countryViewHolder, convertView);
                convertView.setTag(countryViewHolder);
            } else {
                countryViewHolder = (CountryViewHolder) convertView.getTag();
            }
            final DestinationTabItemBean.CountryItemBean hotCountry = hotCountrys.get(position);
            countryViewHolder.countryName.setText(hotCountry.countryName);

            int countryFlagWidth = UIUtils.dip2px(26);
            int countryFlagHeight = countryFlagWidth * 16 / 24;

            if (countryViewHolder.countryFlag.getLayoutParams() == null) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(countryFlagWidth, countryFlagHeight);
                countryViewHolder.countryFlag.setLayoutParams(params);
            } else {
                countryViewHolder.countryFlag.getLayoutParams().width = countryFlagWidth;
                countryViewHolder.countryFlag.getLayoutParams().height = countryFlagHeight;
            }
            Tools.showImage(countryViewHolder.countryFlag, hotCountry.countryFlagUrl, R.mipmap.home_country_dafault);

            if (countryViewHolder.border.getLayoutParams() == null) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(countryFlagWidth, countryFlagHeight);
                countryViewHolder.border.setLayoutParams(params);
            } else {
                countryViewHolder.border.getLayoutParams().width = countryFlagWidth;
                countryViewHolder.border.getLayoutParams().height = countryFlagHeight;
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CityActivity.Params params = new CityActivity.Params();
                    params.id = hotCountry.countryId;
                    params.cityHomeType = CityActivity.CityHomeType.COUNTRY;
                    params.titleName = hotCountry.countryName;
                    Intent intent = new Intent(v.getContext(), CityActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra("source", "目的地");
                    v.getContext().startActivity(intent);
                }
            });
            return convertView;
        }
    }

    static class CityViewHolder {
        @BindView(R.id.home_dest_gridcity_img)
        ImageView cityPicture;
        @BindView(R.id.home_dest_gridcity_guide_count)
        TextView cityGuideCount;
        @BindView(R.id.home_dest_gridcity_name)
        TextView cityName;
    }

    static class CountryViewHolder {
        @BindView(R.id.home_dest_gridcounty_flag)
        ImageView countryFlag;
        @BindView(R.id.home_dest_gridcounty_border)
        ImageView border;
        @BindView(R.id.home_dest_gridcountry)
        TextView countryName;
    }
}
