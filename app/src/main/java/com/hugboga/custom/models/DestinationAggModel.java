package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityHomeListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
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
        destinationViewHolder.cityGridView.setVisibility(View.VISIBLE);
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
//        gridView.removeAllViews();
//        Context context = gridView.getContext();
//        for(int i=0;i<cities.size();i++){
//            LinearLayout linearLayout;
//            if(i%3==0){
//                 linearLayout = new LinearLayout(context);
//            }else{
//                linearLayout = (LinearLayout) gridView.getChildAt(gridView.getChildCount()-1);
//            }
//            View view = getView(context,cities.get(i));
//            if((i+1)%3!=0){
//                LinearLayout.LayoutParams clayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
//                ,ViewGroup.LayoutParams.WRAP_CONTENT);
//                clayoutParams.rightMargin = UIUtils.dip2px(10);
//                linearLayout.addView(view,clayoutParams);
//            }else{
//                linearLayout.addView(view);
//            }
//            if(i%3==0){
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT);
//                lp.topMargin = UIUtils.dip2px(10);
//                gridView.addView(linearLayout,lp);
//            }
//        }
    }


    private void setCountryGridParams(GridView gridView, List<HomeBeanV2.HotCountry> countries) {
        CountryAdapter countryAdapter = new CountryAdapter(countries,gridView.getContext());
        int gridWidth = (UIUtils.screenWidth - UIUtils.dip2px(50)) / 3;
        gridView.setColumnWidth(gridWidth);
        gridView.setNumColumns(3);
        if(countries!=null){
            if(countries.size()%3==0){
               // cityPicture.getLayoutParams().height = gridWidth * 80 / 110;
                int height = countries.size()/3* UIUtils.dip2px(30)*16/24 + (countries.size()/3-1)*UIUtils.dip2px(15);
                gridView.getLayoutParams().height = height;
                //gridViewContainer.getLayoutParams().height = height;
            }else{
                int height = (countries.size()/3 +1)* UIUtils.dip2px(30)*16/24 + countries.size()/3*UIUtils.dip2px(15);
                gridView.getLayoutParams().height = height;
                //gridViewContainer.getLayoutParams().height = height;
            }
        }
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

    private View getView(Context context,final HomeBeanV2.HotCity hotCity){
       View view  = LayoutInflater.from(context).
                inflate(R.layout.home_dest_gridcity_item, null);
        ImageView cityPicture = (ImageView)view.findViewById(R.id.home_dest_gridcity_img);
        TextView cityGuideCount= (TextView) view.findViewById(R.id.home_dest_gridcity_guide_count);;
        TextView cityName = (TextView) view.findViewById(R.id.home_dest_gridcity_name);
        cityGuideCount.setText(hotCity.cityGuideAmount + "位司导");
        cityName.setText(hotCity.cityName);
        int gridWidth = (UIUtils.screenWidth - UIUtils.dip2px(50)) / 3;
        cityPicture.getLayoutParams().width = gridWidth;
        cityPicture.getLayoutParams().height = gridWidth * 80 / 110;
        view.findViewById(R.id.home_dest_gridcity_img_filter).getLayoutParams().width = gridWidth;
        view.findViewById(R.id.home_dest_gridcity_img_filter).getLayoutParams().height = gridWidth * 80 / 110;

        Tools.showImage(cityPicture, hotCity.cityPicture, R.mipmap.home_default_route_item);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"city",Toast.LENGTH_LONG).show();
                Bundle bundle=new Bundle();
                CityHomeListActivity.Params params = new CityHomeListActivity.Params();
                Intent intent = new Intent(v.getContext(), CityHomeListActivity.class);
                bundle.putSerializable(Constants.PARAMS_SOURCE,"首页热门目的地");
                params.cityHomeType=CityHomeListActivity.CityHomeType.CITY;
                params.titleName=hotCity.cityName;
                params.id=hotCity.cityId;
                intent.putExtra(Constants.PARAMS_DATA,params);
                v.getContext().startActivity(intent);
                StatisticClickEvent.click(StatisticConstant.LAUNCH_CITY, "首页热门目的地");
            }
        });
        return view;
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
            cityViewHolder.filterPictureView.getLayoutParams().width = gridWidth;
            cityViewHolder.filterPictureView.getLayoutParams().height =  gridWidth * 80 / 110;
            Tools.showImage(cityViewHolder.cityPicture, hotCity.cityPicture, R.mipmap.home_default_route_item);
            return convertView;
        }
    }


    class CountryAdapter extends BaseAdapter {

        private List<HomeBeanV2.HotCountry> hotCountrys;
        private Context mContext;

        public CountryAdapter(List<HomeBeanV2.HotCountry> hotCountrys,Context context) {
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
            final HomeBeanV2.HotCountry hotCountry = hotCountrys.get(position);
            countryViewHolder.countryName.setText(hotCountry.countryName);

            int countryFlagWidth = UIUtils.dip2px(30);
            int countryFlagHeight = countryFlagWidth * 16 / 24;

            countryViewHolder.countryFlag.getLayoutParams().width = countryFlagWidth;
            countryViewHolder.countryFlag.getLayoutParams().height = countryFlagHeight;
            Tools.showImage(countryViewHolder.countryFlag, hotCountry.countryPicture, R.mipmap.home_country_dafault);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CityHomeListActivity.Params params = new CityHomeListActivity.Params();
                    params.id = hotCountry.countryId;
                    params.cityHomeType = CityHomeListActivity.CityHomeType.COUNTRY;
                    params.titleName = hotCountry.countryName;
                    Intent intent = new Intent(v.getContext(), CityHomeListActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra("source","搜索");
                    v.getContext().startActivity(intent);
                }
            });
            return convertView;
        }
    }

    static class CityViewHolder {
        @Bind(R.id.home_dest_gridcity_img)
        ImageView cityPicture;
        @Bind(R.id.home_dest_gridcity_img_filter)
        View filterPictureView;
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
