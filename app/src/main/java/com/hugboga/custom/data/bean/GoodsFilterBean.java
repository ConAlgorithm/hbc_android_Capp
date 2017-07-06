package com.hugboga.custom.data.bean;

import com.hugboga.custom.fragment.SkuScopeFilterFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=6619493#id-首页－商品-goods-list-and-search-v1.0
 * */
public class GoodsFilterBean implements Serializable{

    public int listCount;               // 总个数
    public List<SkuItemBean> listData;  // 线路列表
    public ArrayList<FilterTheme> themes;    // 主题列表

    public static class FilterTheme extends FilterItemBase{
        public int themeId;
        public String themeName;

        @Override
        public Object clone() {
            FilterTheme filterTheme = null;
            try {
                filterTheme = (FilterTheme) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return filterTheme;
        }

        @Override
        public String getTagId() {
            return themeId + "";
        }

        @Override
        public String getName() {
            return themeName;
        }
    }
}
