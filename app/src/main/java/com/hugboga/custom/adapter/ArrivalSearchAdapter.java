package com.hugboga.custom.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.PoiBean;

public class ArrivalSearchAdapter extends BaseAdapter{

	public ArrivalSearchAdapter(Activity context) {
		super(context);
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_arrival_search, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.content);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		PoiBean model = (PoiBean) getItem(position);
		viewHolder.tvTitle.setText(model.placeName);
		if(!TextUtils.isEmpty(model.placeDetail)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(model.placeDetail);
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}

		return view;

	}


	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}

}