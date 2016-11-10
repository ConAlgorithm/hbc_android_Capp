package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/10/20.
 */
public class HomeHotCityPageView extends GridView implements HbcViewBehavior{

    public HomeHotCityPageView(Context context) {
        this(context, null);
    }

    public HomeHotCityPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setNumColumns(3);
        setVerticalSpacing(UIUtils.dip2px(8));
        setHorizontalSpacing(UIUtils.dip2px(8));
    }

    @Override
    public void update(Object _data) {
        ArrayList<HomeBean.HotCity> arrayList = (ArrayList<HomeBean.HotCity>)_data;
        GridViewAdapter adapter = new GridViewAdapter(getContext(), arrayList);
        this.setAdapter(adapter);
    }

    public static class GridViewAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<HomeBean.HotCity> arrayList;

        public GridViewAdapter(Context _context, ArrayList<HomeBean.HotCity> arrayList) {
            this.context = _context;
            this.arrayList = arrayList;
        }

        public void update(ArrayList<HomeBean.HotCity> arrayList) {
            this.arrayList = arrayList;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return arrayList != null ? arrayList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return arrayList != null ? arrayList.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new HomeHotCityItemView(context);
            }
            ((HbcViewBehavior)convertView).update(arrayList.get(position));
            return convertView;
        }
    }
}
