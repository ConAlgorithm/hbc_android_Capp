package com.hugboga.custom.widget;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.request.RequestUploadLocation;
import com.hugboga.custom.fragment.FgChooseCity;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.LocationUtils;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 16/7/23.
 */
public class ChooseCityHeaderView extends LinearLayout{

    private FgChooseCity fgChooseCity;

    private LinearLayout lociationTitleView;
    private LinearLayout historyTitleView;
    private LinearLayout hotCityTitleView;

    private TextView lociationTV;
    private TagGroup historyLayout;
    private TagGroup hotCityLayout;

    private int tagWidth;
    private int tagHight;

    private CityBean locationCityBean;
    private List<CityBean> historyList;
    private List<CityBean> hotCityList;


    public ChooseCityHeaderView(FgChooseCity _fragment) {
        this(_fragment.getContext(), null);
        this.fgChooseCity = _fragment;
    }

    public ChooseCityHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFEBEBEB);

        final int paddingLeft = UIUtils.dip2px(15);
        final int paddingRight = UIUtils.dip2px(30);
        setPadding(paddingLeft, 0, paddingRight, 0);
        tagWidth = (UIUtils.getScreenWidth() - paddingLeft - paddingRight - UIUtils.dip2px(10) * 2) / 3;
        tagHight = UIUtils.dip2px(40);

        lociationTitleView = getSubTitleLayout(R.mipmap.icon_search_location, getContext().getString(R.string.choose_city_subtitle_location));
        lociationTV = getTagView("定位失败");
        this.addView(lociationTV);
        lociationTitleView.setVisibility(View.GONE);
        lociationTV.setVisibility(View.GONE);
        lociationTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationCityBean == null) {
                    initLocation();
                } else {
                    fgChooseCity.onItemClick(locationCityBean);
                }
            }
        });

        historyTitleView = getSubTitleLayout(R.mipmap.icon_search_history, getContext().getString(R.string.choose_city_subtitle_history));
        historyLayout = getTagGroup();
        historyTitleView.setVisibility(View.GONE);
        historyLayout.setVisibility(View.GONE);
        historyLayout.setOnTagItemClickListener(new TagGroup.OnTagItemClickListener() {
            @Override
            public void onTagClick(View view, int position) {
                if (historyList != null && position < historyList.size()) {
                    fgChooseCity.onItemClick(historyList.get(position));
                }
            }
        });

        hotCityTitleView = getSubTitleLayout(R.mipmap.icon_search_hot, getContext().getString(R.string.choose_city_subtitle_hot));
        hotCityLayout = getTagGroup();
        hotCityTitleView.setVisibility(View.GONE);
        hotCityLayout.setVisibility(View.GONE);
        hotCityLayout.setOnTagItemClickListener(new TagGroup.OnTagItemClickListener() {
            @Override
            public void onTagClick(View view, int position) {
                if (hotCityList != null && position < hotCityList.size()) {
                    fgChooseCity.onItemClick(hotCityList.get(position));
                }
            }
        });
    }

    public void setLociationData(CityBean cityBean, boolean isPickUp) {
        if (cityBean == null && !isPickUp) {
            lociationTitleView.setVisibility(View.GONE);
            lociationTV.setVisibility(View.GONE);
        } else {
            locationCityBean = cityBean;
            lociationTitleView.setVisibility(View.VISIBLE);
            lociationTV.setVisibility(View.VISIBLE);
            if (isPickUp && locationCityBean == null) {
                lociationTV.setText("定位失败");
                lociationTV.setTextColor(0xFFf7c216);
            } else {
                lociationTV.setTextColor(0xFF333333);
                lociationTV.setText(locationCityBean.name);
            }
        }
    }

    public void setHistoryData(List<CityBean> cityList) {
        if (cityList == null || cityList.size() <= 0) {
            historyTitleView.setVisibility(View.GONE);
            historyLayout.setVisibility(View.GONE);
        } else {
            historyTitleView.setVisibility(View.VISIBLE);
            historyLayout.setVisibility(View.VISIBLE);

            this.historyList = cityList;
            final int tagSize = cityList.size();
            ArrayList<View> viewList = new ArrayList<View>(tagSize);
            for (int i = 0; i < tagSize; i++) {
                CityBean cityBean = cityList.get(i);
                if (i < historyLayout.getChildCount()) {
                    TextView tagView = (TextView) historyLayout.getChildAt(i);
                    tagView.setText(cityBean.name);
                    tagView.setVisibility(View.VISIBLE);
                } else {
                    viewList.add(getTagView(cityBean.name));
                }
            }
            for (int j = tagSize; j < historyLayout.getChildCount(); j++) {
                historyLayout.getChildAt(j).setVisibility(View.GONE);
            }
            historyLayout.setTags(viewList, historyLayout.getChildCount() <= 0);
        }
    }

    public void setHotCitysData(List<CityBean> cityList) {
        if (cityList == null || cityList.size() <= 0) {
            hotCityTitleView.setVisibility(View.GONE);
            hotCityLayout.setVisibility(View.GONE);
        } else {
            hotCityTitleView.setVisibility(View.VISIBLE);
            hotCityLayout.setVisibility(View.VISIBLE);

            this.hotCityList = cityList;
            final int tagSize = cityList.size() > 18 ? 18 : cityList.size() ;
            ArrayList<View> viewList = new ArrayList<View>(tagSize);
            for (int i = 0; i < tagSize; i++) {
                CityBean cityBean = cityList.get(i);
                if (i < hotCityLayout.getChildCount()) {
                    TextView tagView = (TextView) hotCityLayout.getChildAt(i);
                    tagView.setText(cityBean.name);
                    tagView.setVisibility(View.VISIBLE);
                } else {
                    viewList.add(getTagView(cityBean.name));
                }
            }
            for (int j = tagSize; j < hotCityLayout.getChildCount(); j++) {
                hotCityLayout.getChildAt(j).setVisibility(View.GONE);
            }
            hotCityLayout.setTags(viewList, hotCityLayout.getChildCount() <= 0);
        }
    }

    private LinearLayout getSubTitleLayout(int iconRes, String title) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);

        ImageView iconIV = new ImageView(getContext());
        iconIV.setBackgroundResource(iconRes);
        linearLayout.addView(iconIV, UIUtils.dip2px(14), UIUtils.dip2px(14));

        TextView titleTV = new TextView(getContext());
        titleTV.setTextColor(0xFF898989);
        titleTV.setTextSize(12);
        titleTV.setText(title);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleParams.leftMargin = UIUtils.dip2px(10);
        linearLayout.addView(titleTV, titleParams);

        addView(linearLayout, LayoutParams.MATCH_PARENT, UIUtils.dip2px(40));
        return linearLayout;
    }

    private TextView getTagView() {
        TextView tagTV = new TextView(getContext());
        tagTV.setMaxLines(2);
        tagTV.setEllipsize(TextUtils.TruncateAt.END);
        tagTV.setPadding(UIUtils.dip2px(3), 0, UIUtils.dip2px(3), 0);
        tagTV.setGravity(Gravity.CENTER);
        tagTV.setBackgroundResource(R.drawable.shape_rounded_white_btn);
        tagTV.setTextColor(0xFF333333);
        tagTV.setTextSize(15);
        tagTV.setLayoutParams(new LinearLayout.LayoutParams(tagWidth, tagHight));
        return tagTV;
    }

    private TextView getTagView(String title) {
        TextView tagTV = getTagView();
        tagTV.setText(title);
        return tagTV;
    }

    private TagGroup getTagGroup() {
        TagGroup tagGroup = new TagGroup(getContext());
        tagGroup.setVerticalSpacing(UIUtils.dip2px(10));
        tagGroup.setHorizontalSpacing(UIUtils.dip2px(10));
        addView(tagGroup);
        return tagGroup;
    }

    public void requestLocation() {
        if (locationCityBean != null) {
            return;
        }
        if (lociationTV != null) {
            lociationTV.setText("重新获取中");
            lociationTV.setTextColor(0xFFf7c216);
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationListener);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    LocationManager locationManager;
    LocationListener locationListener;
    public void initLocation() {
        if(!LocationUtils.gpsIsOpen(getContext())){
            AlertDialog dialog = AlertDialogUtils.showAlertDialog(getContext(), "没有开启GPS定位,请到设置里开启", "设置", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LocationUtils.openGPSSeting(getContext());
                    dialog.dismiss();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return;
        }
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LocationUtils.saveLocationInfo(getContext(), location.getLatitude()+"",location.getLongitude()+"");
                if (!TextUtils.isEmpty(location.getLatitude()+"") && locationCityBean == null) {
                    if (lociationTV != null) {
                        lociationTV.setText("重新获取中");
                        lociationTV.setTextColor(0xFFf7c216);
                    }
                    RequestUploadLocation requestUploadLocation = new RequestUploadLocation(getContext());
                    HttpRequestUtils.request(getContext(), requestUploadLocation, new HttpRequestListener() {
                        @Override
                        public void onDataRequestSucceed(BaseRequest request) {
                            LocationUtils.cleanLocationInfo(getContext());
                            String cityId = ((RequestUploadLocation) request).getData().cityId;
                            String cityName = ((RequestUploadLocation) request).getData().cityName;
                            String countryId = ((RequestUploadLocation) request).getData().countryId;
                            String countryName = ((RequestUploadLocation) request).getData().countryName;
                            LocationUtils.saveLocationCity(getContext(), cityId, cityName, countryId, countryName);

                            locationCityBean = new CityBean();
                            locationCityBean.cityId = CommonUtils.getCountInteger(cityId);
                            locationCityBean.name = cityName;
                            if (lociationTV != null) {
                                lociationTV.setText(cityName);
                                lociationTV.setTextColor(0xFF333333);
                            }
                        }

                        @Override
                        public void onDataRequestCancel(BaseRequest request) {

                        }

                        @Override
                        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                            if (lociationTV != null) {
                                lociationTV.setText("定位失败");
                                lociationTV.setTextColor(0xFFf7c216);
                            }
                        }
                    }, false);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                LocationUtils.cleanLocationInfo(getContext());
            }
        };
        requestLocation();
    }

}
