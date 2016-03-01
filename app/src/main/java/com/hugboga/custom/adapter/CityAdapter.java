package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityBean;

import java.util.List;

public class CityAdapter extends BaseAdapter {
	private List<CityBean> list = null;
	private Context mContext;
	
	public CityAdapter(Context mContext, List<CityBean> list) {
		this.mContext = mContext;
		this.list = list;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<CityBean> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return list==null?0:this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_sortlist_city, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.imgIcon = (ImageView) view.findViewById(R.id.icon);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		CityBean model = list.get(position);
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(model.isFirst){
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
		
		return view;

	}
	


	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		ImageView imgIcon;
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
}