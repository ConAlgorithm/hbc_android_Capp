package com.hugboga.custom.data.bean;


public class SearchGroupBean {
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

    public int hot_weight;
    public int type;//1,组 2,国家,3,城市

    public int flag;//1,左 2,中,3,右
}
