package com.hugboga.custom.adapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.utils.CityUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchNewAdapter extends BaseExpandableListAdapter {
    Activity activity;
    String key = "";
    public void setGroupArray(List<SearchGroupBean> groupList) {
        this.groupList.clear();
        this.childList.clear();
        this.groupList.addAll(groupList);
        List<SearchGroupBean> tmpList;
        /*for (int  index = 0 ; index < groupList.size(); ++index) {
            tmpList = CityUtils.getCountLineCity(activity,groupList.get(index));
            if(null == tmpList){
                tmpList = new ArrayList<>();
            }
            childList.add(tmpList);
        }*/

        notifyDataSetChanged();
    }

    public List<List<SearchGroupBean>> getChildList(){
        return  childList;
    }

    public void setKey(String key){
        this.key = key;
    }

    public void clearList(){
        this.groupList.clear();
        notifyDataSetChanged();
    }

    private  List<SearchGroupBean> groupList = new ArrayList<>();
    private  List<List<SearchGroupBean>> childList = new  ArrayList<List<SearchGroupBean>>();

    public SearchNewAdapter(Activity activity) {
        this.activity = activity;
    }


    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        if(childList.size() <= groupPosition || null == childList.get(groupPosition)){
            return 0;
        }
        return childList.get(groupPosition).size();
    }

    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        return genChildView(childList.get(groupPosition).get(childPosition));
    }

    // group method stub
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    public int getGroupCount() {
        if(null == groupList){
            return 0;
        }
        return groupList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        return genGroupView(groupList.get(groupPosition));
    }



    public View genGroupView(SearchGroupBean searchGroupBean) {
        View view = LayoutInflater.from(activity).inflate(R.layout.search_item_layou,null);
        TextView left_name = (TextView)view.findViewById(R.id.left_name);
        TextView right_name = (TextView)view.findViewById(R.id.right_name);

//        left_name.setText(CityUtils.getSpannableString(CityUtils.getShowName(searchGroupBean),key));//所搜索的词高亮
        left_name.setText(CityUtils.getShowName(searchGroupBean));
        right_name.setText(CityUtils.getParentName(searchGroupBean));
        return view;
    }

    public View genChildView(SearchGroupBean searchGroupBean) {
        View view = LayoutInflater.from(activity).inflate(R.layout.search_child_item_layout,null);
        LinearLayout title_layout = (LinearLayout)view.findViewById(R.id.title_layout);

        TextView title = (TextView)view.findViewById(R.id.title);
        TextView name = (TextView)view.findViewById(R.id.name);

        if(searchGroupBean.group_id == -100 || searchGroupBean.group_id == -200){
            title_layout.setVisibility(View.VISIBLE);
            title.setText(searchGroupBean.group_name);
            name.setVisibility(View.GONE);
        }else{
            title_layout.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);
            name.setText(CityUtils.getShowName(searchGroupBean));
        }
        return view;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}