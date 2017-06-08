package com.hugboga.custom.widget;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityActivity;
import com.hugboga.custom.activity.SkuOrderActivity;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.request.RequestUploadLocation;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.LocationUtils;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
/**
 * Created by on 16/7/23.
 */
public class ChooseCityHeaderView extends LinearLayout{

    private ChooseCityActivity chooseCityActivity;

    //private LinearLayout lociationTitleView;
    public LinearLayout historyTitleView;
    private LinearLayout hotCityTitleView;
    private boolean isPickUp = false;
    //private TextView lociationTV;
    private TagGroup historyLayout;
    private TagGroup hotCityLayout;

    private int tagWidth;
    private int tagHight;
    private CityBean locationCityBean;
    private List<CityBean> historyList;
    private List<CityBean> hotCityList;

    public ChooseCityHeaderView(Context context) {
        super(context);
    }

    public ChooseCityHeaderView(Context context , final boolean isPickUp) {
        super(context, null);
        this.chooseCityActivity = (ChooseCityActivity) context;

        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFEBEBEB);

        final int paddingLeft = UIUtils.dip2px(12);
        final int paddingRight = UIUtils.dip2px(25);
        setPadding(paddingLeft, 0, paddingRight, UIUtils.dip2px(13));
        tagWidth = (UIUtils.getScreenWidth() - paddingLeft - paddingRight - UIUtils.dip2px(10) * 2) / 4;
        tagHight = UIUtils.dip2px(30);

        //lociationTitleView = getSubTitleLayout(R.mipmap.icon_search_location, getContext().getString(R.string.choose_city_subtitle_location));
        //lociationTV = getTagView("定位失败");
        //this.addView(lociationTV);
        //lociationTitleView.setVisibility(View.GONE);
        //lociationTV.setVisibility(View.GONE);
        /*lociationTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationCityBean == null) {
                    initLocation();
                } else {
                    chooseCityActivity.onItemClick(locationCityBean);
                }
            }
        });*/
        this.isPickUp = isPickUp;
        if(isPickUp){
            historyTitleView = getSubTitleLayout(R.mipmap.icon_search_history, "定位/历史");
        }else{
            historyTitleView = getSubTitleLayout(R.mipmap.icon_search_history, "历史足迹");
        }
        historyLayout = getTagGroup();
        historyTitleView.setVisibility(View.GONE);
        historyLayout.setVisibility(View.GONE);
        historyLayout.setOnTagItemClickListener(new TagGroup.OnTagItemClickListener() {
            @Override
            public void onTagClick(View view, int position) {
                //第一个是当前定位
                if(isPickUp && position == 0){
                    if (locationCityBean == null) {
                        initLocation(position);
                    } else {
                        chooseCityActivity.onItemClick(locationCityBean);
                    }
                    return;
                }
                if (historyList != null && position < historyList.size()) {
                    chooseCityActivity.onItemClick(historyList.get(position));
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
                    chooseCityActivity.onItemClick(hotCityList.get(position));
                }
            }
        });
    }

    public void setLociationData(CityBean cityBean, boolean isPickUp) {
        if(isPickUp){
            this.isPickUp = true;
            locationCityBean = cityBean;
        }
        /*if (cityBean == null && !isPickUp) {
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
        }*/
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
                if(isPickUp ){
                    if(i==0 && cityBean == null){
                        String cityName = "定位失败";

                        if (i < historyLayout.getChildCount()) {
                            TextView tagView = (TextView) historyLayout.getChildAt(i);
                            tagView.setText(cityName);
                            tagView.setVisibility(View.VISIBLE);
                        } else {
                            viewList.add(getTagViewForNotLocation(cityName));
                        }
                    }else {
                        if (i < historyLayout.getChildCount()) {
                            TextView tagView = (TextView) historyLayout.getChildAt(i);
                            //tagView.setText(cityBean.name);
                            tagView.setVisibility(View.VISIBLE);
                        } else {
                            if(i == 0){
                                TextView locationView = getTagViewWithLocation(cityBean.name);
                                viewList.add(locationView);
                            }else {
                                viewList.add(getTagView(cityBean.name));
                            }
                        }
                    }

                }else{
                    if (i < historyLayout.getChildCount()) {
                        TextView tagView = (TextView) historyLayout.getChildAt(i);
                        tagView.setText(cityBean.name);
                        tagView.setVisibility(View.VISIBLE);
                    } else {
                        viewList.add(getTagView(cityBean.name));
                    }
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
        linearLayout.setGravity(Gravity.TOP);

//        ImageView iconIV = new ImageView(getContext());
//        iconIV.setBackgroundResource(iconRes);
//        linearLayout.addView(iconIV, UIUtils.dip2px(14), UIUtils.dip2px(14));

        TextView titleTV = new TextView(getContext());
        titleTV.setTextColor(0xFF898989);
        titleTV.setTextSize(12);
        titleTV.setText(title.substring(0,2));
        Drawable drawable = getResources().getDrawable(R.drawable.yellow_under_line);
        drawable.setBounds(0,0,UIUtils.dip2px(25),UIUtils.dip2px(2));
        titleTV.setCompoundDrawables(null,null,null,drawable);
        TextView titleTV2 = new TextView(getContext());
        titleTV2.setTextColor(0xFF898989);
        titleTV2.setTextSize(12);
        titleTV2.setText(title.substring(2,title.length()));
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleParams.leftMargin = UIUtils.dip2px(2);
        linearLayout.addView(titleTV, titleParams);
        linearLayout.addView(titleTV2);

        LinearLayout layParent = new LinearLayout(getContext());
        layParent.setOrientation(LinearLayout.HORIZONTAL);
        layParent.setGravity(Gravity.CENTER_VERTICAL);
        layParent.addView(linearLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(layParent, LayoutParams.MATCH_PARENT, UIUtils.dip2px(40));
        return layParent;
    }

    private TextView getTagView() {
        TextView tagTV = new TextView(getContext());
        tagTV.setMaxLines(2);
        tagTV.setEllipsize(TextUtils.TruncateAt.END);
        tagTV.setPadding(UIUtils.dip2px(2), 0, UIUtils.dip2px(2), 0);
        tagTV.setGravity(Gravity.CENTER);
        tagTV.setBackgroundResource(R.drawable.shape_rounded_white_btn);
        tagTV.setTextColor(0xFF7F7F7F);
        tagTV.setTextSize(12);
        tagTV.setLayoutParams(new LinearLayout.LayoutParams(tagWidth, tagHight));
        return tagTV;
    }
    private TextView getTagViewWithLocation(String text) {
        TextView tagTV = new TextView(getContext());
        tagTV.setMaxLines(2);
        tagTV.setEllipsize(TextUtils.TruncateAt.END);
        tagTV.setPadding(UIUtils.dip2px(2), 0, UIUtils.dip2px(2), 0);
        tagTV.setBackgroundResource(R.drawable.shape_rounded_white_btn);
        //Drawable image = ContextCompat.getDrawable(MyApplication.getAppContext(),R.mipmap.trip_icon_place);
        Drawable image = getResources().getDrawable(R.mipmap.trip_icon_place2);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        //tagTV.setCompoundDrawables(image,null,null,null);
        SpannableString spannable = new SpannableString("[smile]");
        ImageSpan span = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
        spannable.setSpan(span, 0,"[smile]".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tagTV.setTextColor(0xFF7F7F7F);
        tagTV.setTextSize(12);
        tagTV.setText(spannable);
        tagTV.append(text);
        tagTV.setGravity(Gravity.CENTER);
        tagTV.setLayoutParams(new LinearLayout.LayoutParams(tagWidth, tagHight));
        return tagTV;
    }
    private TextView getTagViewForNotLocation() {
        TextView tagTV = new TextView(getContext());
        tagTV.setMaxLines(2);
        tagTV.setEllipsize(TextUtils.TruncateAt.END);
        tagTV.setPadding(UIUtils.dip2px(2), 0, UIUtils.dip2px(2), 0);
        tagTV.setGravity(Gravity.CENTER);
        tagTV.setBackgroundResource(R.drawable.shape_rounded_red_btn);
        tagTV.setTextColor(0xFFFF2525);
        tagTV.setTextSize(12);
        tagTV.setLayoutParams(new LinearLayout.LayoutParams(tagWidth, tagHight));
        return tagTV;
    }
    private TextView getTagViewForNotLocation(String title){
        TextView tagTV = getTagViewForNotLocation();
        tagTV.setText(title);
        return tagTV;
    }
    private TextView getTagView(String title) {
        TextView tagTV = getTagView();
        tagTV.setText(title);
        return tagTV;
    }

    private TagGroup getTagGroup() {
        TagGroup tagGroup = new TagGroup(getContext());
        tagGroup.setVerticalSpacing(UIUtils.dip2px(5));
        tagGroup.setHorizontalSpacing(UIUtils.dip2px(5));
        tagGroup.setPadding(0,0,0,UIUtils.dip2px(5));
        addView(tagGroup);
        return tagGroup;
    }

    public void requestLocation() {
        if (locationCityBean != null) {
            return;
        }
        if(isPickUp && historyLayout.getChildCount() > 0){
            ((TextView) historyLayout.getChildAt(0)).setText("重新获取中");
        }
        /*if (lociationTV != null) {
            lociationTV.setText("重新获取中");
            lociationTV.setTextColor(0xFFf7c216);
        }*/
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
    public void initLocation(int position) {
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
        requestPermisson();
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LocationUtils.saveLocationInfo(getContext(), location.getLatitude()+"",location.getLongitude()+"");
                if (!TextUtils.isEmpty(location.getLatitude()+"") && locationCityBean == null) {
                    if(isPickUp && historyLayout.getChildCount() > 0){
                        ((TextView) historyLayout.getChildAt(0)).setText("重新获取中");
                    }
                    /*if (lociationTV != null) {
                        lociationTV.setText("重新获取中");
                        lociationTV.setTextColor(0xFFf7c216);
                    }*/
                    RequestUploadLocation requestUploadLocation = new RequestUploadLocation(getContext());
                    HttpRequestUtils.request(getContext(), requestUploadLocation, new HttpRequestListener() {
                        @Override
                        public void onDataRequestSucceed(BaseRequest request) {
                            ApiReportHelper.getInstance().addReport(request);
                            LocationUtils.cleanLocationInfo(getContext());
                            String cityId = ((RequestUploadLocation) request).getData().cityId;
                            String cityName = ((RequestUploadLocation) request).getData().cityName;
                            String countryId = ((RequestUploadLocation) request).getData().countryId;
                            String countryName = ((RequestUploadLocation) request).getData().countryName;
                            LocationUtils.saveLocationCity(getContext(), cityId, cityName, countryId, countryName);

                            locationCityBean = DBHelper.findCityById(cityId);

                            if(isPickUp && historyLayout.getChildCount() > 0){
                                ((TextView) historyLayout.getChildAt(0)).setText(cityName);
                            }
                            /*if (lociationTV != null) {
                                lociationTV.setText(cityName);
                                lociationTV.setTextColor(0xFF333333);
                            }*/
                        }

                        @Override
                        public void onDataRequestCancel(BaseRequest request) {

                        }

                        @Override
                        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                            /*if (lociationTV != null) {
                                lociationTV.setText("定位失败");
                                lociationTV.setTextColor(0xFFf7c216);
                                checkGpsOpen();
                            }*/
                            if(isPickUp && historyLayout.getChildCount() > 0){
                                ((TextView) historyLayout.getChildAt(0)).setText("定位失败");
                                checkGpsOpen();
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

    private void checkGpsOpen() {
        if (!gpsIsOpen(getContext())) {
            new android.app.AlertDialog.Builder(getContext()).setMessage("没有开启GPS定位,请到设置里开启")
                    .setNegativeButton("取消", null).setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openGPSSeting(getContext());
                }
            }).show();
        }
    }

    public static boolean gpsIsOpen(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void openGPSSeting(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                context.startActivity(intent);
            } catch (Exception e1) {
            }
        }
    }

    @TargetApi(23)
    public void requestPermisson(){
        if (Build.VERSION.SDK_INT >= 23) {
            final List<String> permissionsList = new ArrayList<String>();
            addPermission(getContext(), permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION);
            addPermission(getContext(), permissionsList, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionsList.size() > 0 && null != chooseCityActivity) {
                requestPermissions(chooseCityActivity, permissionsList.toArray(new String[permissionsList.size()]), 101);
            }
        }
    }

    @TargetApi(23)
    public boolean addPermission(Context Ctx, List<String> permissionsList,
                                  String permission) {
        if (checkSelfPermission(Ctx,permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
        return true;
    }
}
