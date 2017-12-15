package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=8560325#id-线路圈目的地-DestinationVo
 */
public class DestinationHotItemBean implements Serializable {

    public int destinationId;               // 目的地ID
    public String destinationName;          // 目的地名称
    public int destinationType;             // 目的地类型,101：线路圈,201：国家,202：城市
    public String destinationImageUrl;      // 目的地图片URL
    public String guideCount;               // 目的地司导数量

}
