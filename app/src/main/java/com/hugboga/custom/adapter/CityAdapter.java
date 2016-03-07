package com.hugboga.custom.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.fragment.FgChooseCity;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.MLog;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.NoScrollGridView;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends BaseAdapter implements View.OnClickListener,OnItemClickListener {
	private List<CityBean> list = null;
	private Context mContext;
	private SharedPre sharedPer;
	private static final int HISTORY_ITEM = 0;
	private static final int HOT_ITEM = 1;
	private static final int CITY_LIST_ITEM = 2;
	private String mBusinessType;
	private DbManager mDbManager;
	private boolean isFirstAccessHotCity = false;
    private List<CityBean> hotCityList = null;
    private int searchHistoryCount = 0;
	private int chooseType = -1;
	private FgChooseCity fragment;


	public CityAdapter(Context mContext,FgChooseCity fragment, List<CityBean> list, String mBusinessType) {
		this.mContext = mContext;
		this.list = list;
		this.fragment = fragment;
		sharedPer = new SharedPre(mContext);
		mDbManager = new DBHelper(mContext).getDbManager();
		this.mBusinessType = mBusinessType;
	}

//    private void generateHotCityData(){
//        for (CityBean cb : list){
//            if(!TextUtils.isEmpty(cb.firstLetter) && cb.firstLetter.equals("热门城市")){
//                hotCityList.add(cb);
//            }
//        }
//    }
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<CityBean> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
//		return list==null?0:this.list.size()-12+1;
		return list == null ? 0 : this.list.size();
	}

	public Object getItem(int position) {
        if (position == searchHistoryCount && hotCityList.size() != 0){
            return hotCityList;
        }else {
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
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
//		CityBean cityBean = list.get(position);
        CityBean cityBean = null;
        if (getItem(position) instanceof CityBean){
            cityBean = (CityBean)getItem(position);
        }else if (getItem(position) instanceof List){
            cityBean = (CityBean)((List) getItem(position)).get(0);
        }

		if (cityBean.firstLetter.equals("搜索历史")){
			return HISTORY_ITEM;
		}else if(cityBean.firstLetter.equals("热门城市")){
			return HOT_ITEM;
		}else{
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
			case CITY_LIST_ITEM:
				convertView = getAllCityListView(position, convertView, type);
			default:
				break;
		}
//		MLog.e("position=" + position + " view=" + view + " viewHolder =" + viewHolder + " , list.size(): " + list.size());
		return convertView;
	}

	public View getHistorySearchView(final int position, View view, int type) {
		return null;
	}

	public View getHotCityView(final int position, View view) {

		if(isFirstAccessHotCity){
			return view;
		}
//		List<CityBean> hotCityList = new ArrayList<CityBean>();

		if(hotCityList.size() == 0){
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

			HotCityGridViewAdapter hotCityGridViewAdapter = new HotCityGridViewAdapter(mContext,hotCityList);
			viewHolder.gv_hot_city.setAdapter(hotCityGridViewAdapter);
			viewHolder.gv_hot_city.setOnItemClickListener(this);
			hotCityGridViewAdapter.notifyDataSetChanged();
		} else {

			if(view.getTag() instanceof GridViewHolder){
				viewHolder = (GridViewHolder) view.getTag();
			}else{
				return null;
			}
		}
		if (hotCityList.get(0).isFirst){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(hotCityList.get(0).firstLetter);
		}else{
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
			if(view.getTag() instanceof ViewHolder){
				viewHolder = (ViewHolder) view.getTag();
			}else{
				return null;
			}
		}

//		CityBean model = list.get(position);
        CityBean model = null;
        if(getItem(position) instanceof CityBean) {
            model = (CityBean) getItem(position);
        }
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (model.isFirst){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(model.firstLetter);
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		if(model.isSelected){
			viewHolder.imgIcon.setVisibility(View.VISIBLE);
		}else{
			viewHolder.imgIcon.setVisibility(View.INVISIBLE);
		}
		viewHolder.tvTitle.setText(model.name);
		if(type == HISTORY_ITEM){
			viewHolder.imgDel.setVisibility(View.VISIBLE);
			viewHolder.imgDel.setOnClickListener(this);
			viewHolder.imgDel.setTag(integer);
			viewHolder.imgIcon.setImageResource(R.mipmap.city_search_history);
			viewHolder.imgIcon.setVisibility(View.VISIBLE);
		}else{
			viewHolder.imgDel.setVisibility(View.INVISIBLE);
			viewHolder.imgIcon.setVisibility(View.INVISIBLE);
		}
		if(model.firstLetter.equals("nationality")){
			viewHolder.ll_item.setClickable(false);
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		Object tag = v.getTag();
		Integer integer = null;
		if(tag instanceof Integer)
			integer = (Integer)tag;
		switch (v.getId()){
			case R.id.btn_del:
				// 删除历史item，刷新列表
				CityBean cityBean = list.get(integer.intValue());
				deleteHistoryItem(cityBean);
				break;
		}

	}

	/**
	 * 删除当前选中的历史条目
	 * @param cityBean
	 */
	private void deleteHistoryItem(CityBean cityBean) {
		Selector selector = null;
		try {
			selector = mDbManager.selector(CityBean.class);
		} catch (DbException e) {
			e.printStackTrace();
		}

		String cityHistoryStr = sharedPer.getStringValue(mBusinessType+SharedPre.RESOURCES_CITY_HISTORY);

		if(!TextUtils.isEmpty(cityHistoryStr)){
			ArrayList<String> cityHistory =  new ArrayList<String>();
			for (String city : cityHistoryStr.split(",")) {
				if(cityBean.cityId != Integer.parseInt(city)){
					cityHistory.add(city);
				}
			}
			sharedPer.saveStringValue(mBusinessType+SharedPre.RESOURCES_CITY_HISTORY,TextUtils.join(",", cityHistory));
		}
		for(CityBean cb : list){
			if(cityBean.cityId == cb.cityId){
				list.remove(cityBean);
				break;
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(fragment != null){
			Bundle bundle = new Bundle(fragment.getArguments());
			CityBean cityBean = hotCityList.get(position);
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
}