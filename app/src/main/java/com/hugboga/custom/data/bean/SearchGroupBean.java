package com.hugboga.custom.data.bean;


public class SearchGroupBean implements Cloneable{
    public int group_id;
    public String group_name;
    public boolean isSelected;

    public int sub_place_id;//国家id
    public String sub_place_name;//国家名称

    public int sub_city_id;//城市id
    public String sub_city_name;//城市名称

    public int parent_id;
    public int parent_type;
    public String parent_name;

    public int spot_id;
    public String spot_name;

    public int hot_weight;
    public int type;

    public int level;//1 顶级组

    public int flag;//1,线路 2,国家,3,城市 4,热门

    @Override
    public Object clone() {
        try {
            return super.clone();
        }catch (Exception e){
            return  this;
        }
    }
}
