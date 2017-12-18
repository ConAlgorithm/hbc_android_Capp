package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=8560325#id-线路圈目的地-TopLineGroupDestinationInfoVo
 */
public class DestinationTabItemBean implements Serializable {

    public List<DestinationHotItemBean> destinationList;    // 目的地列表
    public List<TagBean> topTagGroupList;                   // 顶级标签组列表
    public List<CountryItemBean> countryList;               // 过滤列表

    public static class TagBean implements Serializable{
        public String tagGroupName;        // 标签组名称
        public List<TagItemBean> tagList;  // 标签列表
    }

    public static class TagItemBean implements Serializable{
        public String tagId;            // 标签ID
        public String tagName;          // 标签名称
    }

    public static class CountryItemBean implements Serializable{
        public int countryId;           // 国家id
        public String countryName;      // 国家名称
        public String countryFlagUrl;   // 国旗URL
    }
}
