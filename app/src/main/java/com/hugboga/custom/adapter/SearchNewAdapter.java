package com.hugboga.custom.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hugboga.custom.data.bean.SearchGroupBean;

import java.util.ArrayList;
import java.util.List;

public class SearchNewAdapter extends BaseExpandableListAdapter {
    Activity activity;

    public void setGroupArray(List<SearchGroupBean> groupList) {
        this.groupList.clear();
        this.groupList.addAll(groupList);
        notifyDataSetChanged();
    }

    public void clearList(){
        this.groupList.clear();
        notifyDataSetChanged();
    }

    private  List<SearchGroupBean> groupList = new ArrayList<>();
    private  List<List<SearchGroupBean>> childList = new  ArrayList<List<SearchGroupBean>>();

    public SearchNewAdapter(Activity activity) {
        this.activity = activity;

//        groupArray.add("第一行" );
//        groupArray.add("第二行" );

//        List<String> tempArray = new  ArrayList<String>();
//        tempArray.add("第一条" );
//        tempArray.add("第二条" );
//        tempArray.add("第三条" );
//
//        for (int  index = 0 ; index <groupArray.size(); ++index)
//        {
//            childArray.add(tempArray);
//        }

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
        String string = childList.get(groupPosition).get(childPosition).group_name;
        return getGenericView(string);
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
        String string = groupList.get(groupPosition).group_name;
        return getGenericView(string);
    }

    // View stub to create Group/Children 's View
    public TextView getGenericView(String string) {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, 200);
        TextView text = new TextView(activity);
        text.setLayoutParams(layoutParams);
        // Center the text vertically
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        text.setPadding(36, 0, 0, 0);
        text.setText(string);
        return text;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}