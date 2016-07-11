package com.hugboga.custom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.fragment.FgChooseCity;
import com.hugboga.custom.fragment.FgSkuList;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.NoScrollGridView;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CityAdapter extends BaseAdapter implements View.OnClickListener, OnItemClickListener {
    private List<CityBean> list = null;
    private Context mContext;
    private SharedPre sharedPer;
    private static final int HISTORY_ITEM = 0;
    private static final int HOT_ITEM = 1;
    private static final int CITY_LIST_ITEM = 2;
    private static final int LOCATION_ITEM = 3;
    private String mBusinessType;
    private DbManager mDbManager;
    private boolean isFirstAccessHotCity = false;
    private List<CityBean> hotCityList = new ArrayList<CityBean>();;
    private int searchHistoryCount = 0;
    private int locationCount = 0;
    private int chooseType = -1;
    private FgChooseCity fragment;
    private HotCityGridViewAdapter hotCityGridViewAdapter;


    public CityAdapter(Context mContext, FgChooseCity fragment, List<CityBean> list, String mBusinessType) {
        this.mContext = mContext;
        this.list = list;
        this.fragment = fragment;
        sharedPer = new SharedPre(mContext);
        mDbManager = new DBHelper(mContext).getDbManager();
        this.mBusinessType = mBusinessType;
    }

//    private void generateHotCityData(){
//        for (CityBean cb : list){
//            if(!TextUtils.isEmpty(cb.firstLetter) && cb.firstLetter.equalsIgnoreCase("热门城市")){
//                hotCityList.add(cb);
//            }
//        }
//    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<CityBean> list) {
        this.list = list;
        notifyDataSetChanged();
//        if(hotCityList!=null && hotCityList.size()>0) {
//            hotCityGridViewAdapter.notifyDataSetChanged();
//        }
    }

    public int getCount() {
//		return list==null?0:this.list.size()-12+1;
        return list == null ? 0 : this.list.size();
    }

    public Object getItem(int position) {
        if (locationCount > 0 ? position == searchHistoryCount + locationCount : position == searchHistoryCount
                && hotCityList != null && hotCityList.size() != 0) {
            return hotCityList;
        } else {
            return list.get(position);
        }
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        /*if(TextUtils.isEmpty(sharedPer.getStringValue(mBusinessType + SharedPre.RESOURCES_CITY_HISTORY))){
			MLog.e("getViewTypeCount : 2");
			return 2;
		}else{
			MLog.e("getViewTypeCount : 3");
			return 3;
		}*/
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
//		CityBean cityBean = list.get(position);
        CityBean cityBean = null;
        MLog.e("why me position:" + position);
        if (getItem(position) instanceof CityBean) {
            cityBean = (CityBean) getItem(position);
        } else if (getItem(position) instanceof List) {
            cityBean = (CityBean) ((List) getItem(position)).get(0);
        }

        if (cityBean.firstLetter != null && cityBean.firstLetter.equalsIgnoreCase("搜索历史")) {
            return HISTORY_ITEM;
        } else if (cityBean.firstLetter != null && cityBean.firstLetter.equalsIgnoreCase("热门城市")) {
            return HOT_ITEM;
        } else if (cityBean.firstLetter != null && cityBean.firstLetter.equalsIgnoreCase("定位城市")) {
            return LOCATION_ITEM;
        }else {
            return CITY_LIST_ITEM;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case HISTORY_ITEM:
                convertView = getAllCityListView(position, convertView, type);
                break;
            case HOT_ITEM:
                convertView = getHotCityView(position, convertView);
                break;
            case LOCATION_ITEM:
                convertView = getAllCityListView(position, convertView, type);
                break;
            case CITY_LIST_ITEM:
                convertView = getAllCityListView(position, convertView, type);
            default:
                break;
        }
//		MLog.e("position=" + position + " view=" + view + " viewHolder =" + viewHolder + " , list.size(): " + list.size());
        return convertView;
    }

    public View getLocationCityView(final int position, View view){
        LocationViewHolder locationViewHolder = null;
        if(null == view){
            locationViewHolder = new LocationViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.location_city_layout,null);
            locationViewHolder.location_text = (TextView)view.findViewById(R.id.location_city_name);
            view.setTag(locationViewHolder);
        }else{
            if(view.getTag() instanceof  LocationViewHolder){
                locationViewHolder = (LocationViewHolder)view.getTag();
            }
        }
        locationViewHolder.location_text.setText(new SharedPre(mContext).getStringValue("cityName"));
        return view;
    }

    public View getHotCityView(final int position, View view) {

        if (isFirstAccessHotCity) {
            return view;
        }
//		List<CityBean> hotCityList = new ArrayList<CityBean>();

        if (hotCityList.size() == 0) {
            return null;
        }

        GridViewHolder viewHolder = null;
//		Integer integer = Integer.valueOf(position);
        if (view == null) {
            viewHolder = new GridViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.hot_city_gridview, null);
            viewHolder.gv_hot_city = (NoScrollGridView) view.findViewById(R.id.gv_hot_city);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);

            hotCityGridViewAdapter = new HotCityGridViewAdapter(mContext, hotCityList);
            viewHolder.gv_hot_city.setAdapter(hotCityGridViewAdapter);
            viewHolder.gv_hot_city.setOnItemClickListener(this);
        } else {

            if (view.getTag() instanceof GridViewHolder) {
                viewHolder = (GridViewHolder) view.getTag();
//                hotCityGridViewAdapter.notifyDataSetChanged();
                hotCityGridViewAdapter = new HotCityGridViewAdapter(mContext, hotCityList);
                viewHolder.gv_hot_city.setAdapter(hotCityGridViewAdapter);
                viewHolder.gv_hot_city.setOnItemClickListener(this);
            } else {
                return null;
            }
        }
        if (hotCityList.get(0).isFirst) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(hotCityList.get(0).firstLetter);
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        isFirstAccessHotCity = true;
        return view;
    }

    public View getAllCityListView(final int position, View view, int type) {
        ViewHolder viewHolder = null;
        Integer integer = Integer.valueOf(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_sortlist_city, null);
            viewHolder.ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.imgIcon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.imgDel = (ImageView) view.findViewById(R.id.btn_del);
            view.setTag(viewHolder);
        } else {
            if (view.getTag() instanceof ViewHolder) {
                viewHolder = (ViewHolder) view.getTag();
            } else {
                return null;
            }
        }

//		CityBean model = list.get(position);
        CityBean model = null;
        if (getItem(position) instanceof CityBean) {
            model = (CityBean) getItem(position);
        }
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (model.isFirst) {
            if(!TextUtils.isEmpty(model.firstLetter) && model.firstLetter.equalsIgnoreCase(mContext.getString(R.string.guess_you_want))){
                Drawable drawable= mContext.getResources().getDrawable(R.mipmap.icon_guess_you_want);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                viewHolder.tvLetter.setCompoundDrawables(drawable, null, null, null);
                viewHolder.tvLetter.setCompoundDrawablePadding(5);
                viewHolder.tvLetter.setTextColor(mContext.getResources().getColor(R.color.progress_bg));
            }else {
                viewHolder.tvLetter.setCompoundDrawables(null, null, null, null);
                viewHolder.tvLetter.setTextColor(mContext.getResources().getColor(R.color.normal_subtitle_color));
            }
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(model.firstLetter);
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(model.keyWord)){
            if(model.keyWord.equalsIgnoreCase("相关城市")){
                viewHolder.tvTitle.setText("相关城市，" + model.name);
            }else if(model.isNationality){
                viewHolder.tvTitle.setText(getSpannableString(model.name, model.keyWord));
            }else{
                viewHolder.tvTitle.setText(getSpannableString(model.name + "，" + model.placeName, model.keyWord));
            }
        }else{
            viewHolder.tvTitle.setText(model.name);
        }
        if (type == HISTORY_ITEM) {
            viewHolder.imgDel.setVisibility(View.VISIBLE);
            viewHolder.imgDel.setOnClickListener(this);
            viewHolder.imgDel.setTag(integer);
            viewHolder.imgIcon.setImageResource(R.mipmap.city_search_history);
            viewHolder.imgIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgDel.setVisibility(View.INVISIBLE);
            viewHolder.imgIcon.setVisibility(View.INVISIBLE);
            if (model.isSelected) {
                viewHolder.imgIcon.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imgIcon.setVisibility(View.INVISIBLE);
            }
            if (model.isNationality) {
                viewHolder.ll_item.setClickable(false);
            }
        }

        return view;
    }

    private SpannableStringBuilder getSpannableString(String name, String keyWord) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(name);
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(Color.parseColor("#FDCE02"));
        int start = ssb.toString().indexOf(keyWord);
        int end = start + keyWord.length();
        ssb.setSpan(yellowSpan, start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        Integer integer = null;
        if (tag instanceof Integer)
            integer = (Integer) tag;
        switch (v.getId()) {
            case R.id.btn_del:
                // 删除历史item，刷新列表
                CityBean cityBean = list.get(integer.intValue());
                deleteHistoryItem(cityBean, integer);
                break;
        }

    }

    /**
     * 删除当前选中的历史条目
     *
     * @param cityBean
     */
    private void deleteHistoryItem(CityBean cityBean, int position) {
        String cityHistoryStr = sharedPer.getStringValue(mBusinessType + SharedPre.RESOURCES_CITY_HISTORY);

        if (!TextUtils.isEmpty(cityHistoryStr)) {
            ArrayList<String> cityHistory = new ArrayList<String>();
            for (String city : cityHistoryStr.split(",")) {
                if (cityBean.cityId != Integer.parseInt(city)) {
                    cityHistory.add(city);
                }
            }
            sharedPer.saveStringValue(mBusinessType + SharedPre.RESOURCES_CITY_HISTORY, TextUtils.join(",", cityHistory));
        }

        Iterator<CityBean> iterator = list.iterator();
        while(iterator.hasNext()){
            CityBean cb = iterator.next();
            if(cb.cityId == cityBean.cityId){
                iterator.remove();
                if(searchHistoryCount > 0){
                    searchHistoryCount--;
                }

                if(locationCount > 0){
                    if(position == 1 && searchHistoryCount > 0){
                        list.get(1).firstLetter = "搜索历史";
                        list.get(1).isFirst = true;
                    }
                }else{
                    if(position == 0 && searchHistoryCount > 0){
                        list.get(0).firstLetter = "搜索历史";
                        list.get(0).isFirst = true;
                    }
                }
                break;
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (fragment != null) {
            Bundle bundle = new Bundle(fragment.getArguments());
            CityBean cityBean = hotCityList.get(position);
            if(!TextUtils.isEmpty(mBusinessType) && Integer.parseInt(mBusinessType) == Constants.BUSINESS_TYPE_HOME){
                fragment.saveHistoryDate(cityBean);
                FgSkuList fg = new FgSkuList();
                bundle.putString(FgSkuList.KEY_CITY_ID, String.valueOf(cityBean.cityId));
                fragment.finish();
                fragment.startFragment(fg, bundle);
                return;
            }
            if (chooseType == fragment.KEY_TYPE_SINGLE) {
                fragment.saveHistoryDate(cityBean);
                bundle.putSerializable(fragment.KEY_CITY, cityBean);
                fragment.finishForResult(bundle);
            }
        }
    }

    final static class ViewHolder {
        LinearLayout ll_item;
        TextView tvLetter;
        TextView tvTitle;
        ImageView imgIcon;
        ImageView imgDel;
    }

    final static class GridViewHolder {
        NoScrollGridView gv_hot_city;
        TextView tvLetter;
    }

    final static class LocationViewHolder {
        TextView location_text;
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(String section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).firstLetter;
            if (sortStr.contains(section)) {
                return i;
            }
        }
        return -1;
    }

    public List<CityBean> getHotCityList() {
        return hotCityList;
    }

    public void setHotCityList(List<CityBean> hotCityList) {
        this.hotCityList = hotCityList;
    }

    public int getSearchHistoryCount() {
        return searchHistoryCount;
    }

    public void setSearchHistoryCount(int searchHistoryCount) {
        this.searchHistoryCount = searchHistoryCount;
    }

    public void setIsFirstAccessHotCity(boolean isFirstAccessHotCity) {
        this.isFirstAccessHotCity = isFirstAccessHotCity;
    }

    public void setChooseType(int chooseType) {
        this.chooseType = chooseType;
    }

    public int getLocationCount() {
        return locationCount;
    }

    public void setLocationCount(int locationCount) {
        this.locationCount = locationCount;
    }
}