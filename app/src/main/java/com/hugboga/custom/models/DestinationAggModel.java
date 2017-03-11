package com.hugboga.custom.models;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/10.
 */
public class DestinationAggModel extends EpoxyModelWithHolder {

    private List<HomeBeanV2.HotCity> hotCitys;
    private HomeBeanV2.LineGroupAgg lineGroup;

    public DestinationAggModel(List<HomeBeanV2.HotCity> hotCity) {
        this.hotCitys = hotCity;
        initSrc();
    }

    public DestinationAggModel(HomeBeanV2.LineGroupAgg lineGroup) {
        this.lineGroup = lineGroup;
        initSrc();
    }

    Drawable openArrawIconDrawable,closeArrawIconDrawable;

    private void initSrc(){
         openArrawIconDrawable = ContextCompat.getDrawable(MyApplication.getAppContext(),R.mipmap.more_arrow_down);
         closeArrawIconDrawable = ContextCompat.getDrawable(MyApplication.getAppContext(),R.mipmap.more_arrow_up);
         closeArrawIconDrawable.setBounds(0,0,closeArrawIconDrawable.getIntrinsicWidth(),closeArrawIconDrawable.getIntrinsicHeight());
         openArrawIconDrawable.setBounds(0,0,openArrawIconDrawable.getIntrinsicWidth(),openArrawIconDrawable.getIntrinsicHeight());
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
        destinationViewHolder.titleName.setText("热门城市");
        setCityGridParams(destinationViewHolder.cityGridView, hotCitys);
        destinationViewHolder.speLine.setVisibility(View.GONE);
        destinationViewHolder.countryGridView.setVisibility(View.GONE);
        destinationViewHolder.countryTextLabel.setVisibility(View.GONE);
        destinationViewHolder.countrySwitcher.setVisibility(View.GONE);
    }

    private void renderLineGrounp(final DestinationViewHolder destinationViewHolder) {
        destinationViewHolder.titleName.setText(lineGroup.lineGroupName);
        if (lineGroup.lineGroupCities != null && lineGroup.lineGroupCities.size() > 0) {
            destinationViewHolder.cityGridView.setVisibility(View.VISIBLE);
            setCityGridParams(destinationViewHolder.cityGridView, lineGroup.lineGroupCities);
        } else {
            destinationViewHolder.cityGridView.setVisibility(View.GONE);
        }
        if (lineGroup.lineGroupCountries != null && lineGroup.lineGroupCountries.size() > 0) {
            int lineGroupCountrySize = lineGroup.lineGroupCountries.size();
            destinationViewHolder.speLine.setVisibility(View.VISIBLE);
            destinationViewHolder.countryGridView.setVisibility(View.VISIBLE);
            destinationViewHolder.countryTextLabel.setVisibility(View.VISIBLE);

            destinationViewHolder.countryTextLabel.setText(lineGroupCountrySize + "个国家/地区");
            if (lineGroupCountrySize > 3) {
                destinationViewHolder.countrySwitcher.setVisibility(View.VISIBLE);
                destinationViewHolder.countrySwitcher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lineGroup.hasOpenCountryExtentdView = !lineGroup.hasOpenCountryExtentdView;
                        handlerCountryGridView(destinationViewHolder.countryGridView, destinationViewHolder.countrySwitcher);
                    }
                });
            } else {
                destinationViewHolder.countrySwitcher.setVisibility(View.GONE);
            }
            handlerCountryGridView(destinationViewHolder.countryGridView, destinationViewHolder.countrySwitcher);
        } else {
            destinationViewHolder.speLine.setVisibility(View.GONE);
            destinationViewHolder.countryGridView.setVisibility(View.GONE);
            destinationViewHolder.countryTextLabel.setVisibility(View.GONE);
            destinationViewHolder.countrySwitcher.setVisibility(View.GONE);
        }
    }

    private void handlerCountryGridView(GridView countryGridView, TextView openSwticher) {
        if (lineGroup.hasOpenCountryExtentdView) {
            setCountryGridParams(countryGridView, lineGroup.lineGroupCountries);
            openSwticher.setText("收起全部");
            openSwticher.setCompoundDrawables(closeArrawIconDrawable,null,null,null);
        } else {
            if (lineGroup.lineGroupCountries.size() > 3) {
                setCountryGridParams(countryGridView, lineGroup.lineGroupCountries.subList(0, 3));
            } else {
                setCountryGridParams(countryGridView, lineGroup.lineGroupCountries);
            }
            openSwticher.setText("展开全部");
            openSwticher.setCompoundDrawables(openArrawIconDrawable,null,null,null);
        }
    }


    private void setCityGridParams(GridView gridView, List<HomeBeanV2.HotCity> cities) {
        CityAdapter cityAdapter = new CityAdapter(cities);
        int gridWidth = (UIUtils.screenWidth - UIUtils.dip2px(50)) / 3;
        gridView.setColumnWidth(gridWidth);
        gridView.setNumColumns(3);
        gridView.setAdapter(cityAdapter);
    }


    private void setCountryGridParams(GridView gridView, List<HomeBeanV2.HotCountry> countries) {
        CountryAdapter countryAdapter = new CountryAdapter(countries);
        int gridWidth = (UIUtils.screenWidth - UIUtils.dip2px(50)) / 3;
        gridView.setColumnWidth(gridWidth);
        gridView.setNumColumns(3);
        gridView.setAdapter(countryAdapter);
    }

    static class DestinationViewHolder extends EpoxyHolder {

        View itemView;
        @Bind(R.id.home_dest_title_name)
        TextView titleName;
        @Bind(R.id.home_dest_city_gridview)
        GridView cityGridView;
        @Bind(R.id.home_dest_spe_line)
        View speLine;
        @Bind(R.id.home_dest_country_text_label)
        TextView countryTextLabel;
        @Bind(R.id.home_dest_country_gridview)
        GridView countryGridView;
        @Bind(R.id.home_dest_country_open_switcher)
        TextView countrySwitcher;

        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    class CityAdapter extends BaseAdapter {

        private List<HomeBeanV2.HotCity> hotCityList;

        public CityAdapter(List<HomeBeanV2.HotCity> hotCityList) {
            this.hotCityList = hotCityList;
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
                convertView = LayoutInflater.from(MyApplication.getAppContext()).
                        inflate(R.layout.home_dest_gridcity_item, null);
                ButterKnife.bind(cityViewHolder, convertView);
                convertView.setTag(cityViewHolder);
            } else {
                cityViewHolder = (CityViewHolder) convertView.getTag();
            }
            HomeBeanV2.HotCity hotCity = hotCityList.get(position);
            cityViewHolder.cityGuideCount.setText(hotCity.cityGuideAmount + "位司导");
            cityViewHolder.cityName.setText(hotCity.cityName);
            int gridWidth = (UIUtils.screenWidth - UIUtils.dip2px(50)) / 3;
            cityViewHolder.cityPicture.getLayoutParams().width = gridWidth;
            cityViewHolder.cityPicture.getLayoutParams().height = gridWidth * 80 / 110;
            Tools.showImageHasPlaceHolder(cityViewHolder.cityPicture, hotCity.cityPicture, R.mipmap.home_default_route_item);
            return convertView;
        }
    }


    class CountryAdapter extends BaseAdapter {

        private List<HomeBeanV2.HotCountry> hotCountrys;

        public CountryAdapter(List<HomeBeanV2.HotCountry> hotCountrys) {
            this.hotCountrys = hotCountrys;
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
                convertView = LayoutInflater.from(MyApplication.getAppContext()).
                        inflate(R.layout.home_dest_gridcountry_item, null);
                ButterKnife.bind(countryViewHolder, convertView);
                convertView.setTag(countryViewHolder);
            } else {
                countryViewHolder = (CountryViewHolder) convertView.getTag();
            }
            HomeBeanV2.HotCountry hotCountry = hotCountrys.get(position);
            countryViewHolder.countryName.setText(hotCountry.countryName);

            int countryFlagWidth = UIUtils.dip2px(30);
            int countryFlagHeight = countryFlagWidth * 16 / 24;

            countryViewHolder.countryFlag.getLayoutParams().width = countryFlagWidth;
            countryViewHolder.countryFlag.getLayoutParams().height = countryFlagHeight;
            Tools.showImageHasPlaceHolder(countryViewHolder.countryFlag, hotCountry.countryPicture, R.mipmap.home_country_dafault);
            return convertView;
        }
    }

    static class CityViewHolder {
        @Bind(R.id.home_dest_gridcity_img)
        ImageView cityPicture;
        @Bind(R.id.home_dest_gridcity_guide_count)
        TextView cityGuideCount;
        @Bind(R.id.home_dest_gridcity_name)
        TextView cityName;
    }

    static class CountryViewHolder {
        @Bind(R.id.home_dest_gridcounty_flag)
        ImageView countryFlag;
        @Bind(R.id.home_dest_gridcountry)
        TextView countryName;
    }
}
