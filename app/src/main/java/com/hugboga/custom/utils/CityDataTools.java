package com.hugboga.custom.utils;

import com.hugboga.custom.data.bean.city.DayCountVo;
import com.hugboga.custom.data.bean.city.DestinationCityVo;
import com.hugboga.custom.data.bean.city.DestinationTagGroupVo;
import com.hugboga.custom.data.bean.city.DestinationTagVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.hongbo.label.data.LabelBean;
import tk.hongbo.label.data.LabelItemData;
import tk.hongbo.label.data.LabelParentBean;

/**
 * 目的地数据处理
 * Created by HONGBO on 2017/11/28 16:27.
 */

public class CityDataTools {

    /**
     * 获取构建游玩天数过滤器数据
     *
     * @return
     */
    public List<LabelItemData> getDayData(List<DayCountVo> dayCountList) {
        List<LabelItemData> data = new ArrayList<>();
        if (dayCountList != null && dayCountList.size() > 0) {
            for (int i = 0; i < dayCountList.size(); i += 3) {
                LabelItemData itemData = new LabelItemData();
                List<LabelParentBean> parents = new ArrayList<>();
                addDay(parents, i, dayCountList); //第1个数据
                addDay(parents, i + 1, dayCountList); //第2个数据
                addDay(parents, i + 2, dayCountList); //第3个数据
                //Item容器
                itemData.parent = parents;
                data.add(itemData);
            }
        }
        return data;
    }

    private void addDay(List<LabelParentBean> parents, int i, List<DayCountVo> dayCountList) {
        if (i < dayCountList.size()) {
            LabelParentBean lb1 = getParentDayBean(dayCountList.get(i));
            if (lb1 != null) {
                parents.add(lb1); //第1个数据
            }
        }
    }

    /**
     * 获取父标签内容
     *
     * @param vo1
     * @return
     */
    private LabelParentBean getParentDayBean(DayCountVo vo1) {
        if (vo1 == null) {
            return null;
        }
        LabelParentBean bean = new LabelParentBean();
        LabelBean beanL = new LabelBean();
        beanL.id = vo1.type;
        beanL.name = vo1.title;
        bean.parentLabel = beanL;
        return bean;
    }

    /**
     * 获取构建出发城市过滤器数据
     *
     * @return
     */
    public List<LabelItemData> getCityData(List<DestinationCityVo> depCityList) {
        List<LabelItemData> data = new ArrayList<>();
        if (depCityList != null && depCityList.size() > 0) {
            for (int i = 0; i < depCityList.size(); i += 3) {
                LabelItemData itemData = new LabelItemData();
                List<LabelParentBean> parents = new ArrayList<>();
                addCity(parents, i, depCityList); //第1个数据
                addCity(parents, i + 1, depCityList); //第2个数据
                addCity(parents, i + 2, depCityList); //第3个数据
                //Item容器
                itemData.parent = parents;
                data.add(itemData);
            }
        }
        return data;
    }

    private void addCity(List<LabelParentBean> parents, int i, List<DestinationCityVo> depCityList) {
        if (i < depCityList.size()) {
            LabelParentBean lb1 = getParentCityBean(depCityList.get(i));
            if (lb1 != null) {
                parents.add(lb1); //第1个数据
            }
        }
    }

    /**
     * 获取父标签内容
     *
     * @param vo1
     * @return
     */
    private LabelParentBean getParentCityBean(DestinationCityVo vo1) {
        if (vo1 == null) {
            return null;
        }
        LabelParentBean bean = new LabelParentBean();
        LabelBean beanL = new LabelBean();
        beanL.id = vo1.cityId;
        beanL.name = vo1.cityName;
        bean.parentLabel = beanL;
        return bean;
    }

    /**
     * 获取构建游玩线路过滤器数据
     *
     * @param destinationTagList
     * @return
     */
    public List<LabelItemData> getTagData(List<DestinationTagGroupVo> destinationTagList) {
        List<LabelItemData> data = new ArrayList<>();
        if (destinationTagList != null && destinationTagList.size() > 0) {
            for (int i = 0; i < destinationTagList.size(); i += 3) {
                LabelItemData itemData = new LabelItemData();
                List<LabelParentBean> parents = new ArrayList<>();
                addTag(parents, i, destinationTagList); //第1个数据
                addTag(parents, i + 1, destinationTagList); //第2个数据
                addTag(parents, i + 2, destinationTagList); //第3个数据
                //Item容器
                itemData.parent = parents;
                data.add(itemData);
            }
        }
        return data;
    }

    private void addTag(List<LabelParentBean> parents, int i, List<DestinationTagGroupVo> destinationTagList) {
        if (i < destinationTagList.size()) {
            LabelParentBean lb1 = getParentBean(destinationTagList.get(i));
            if (lb1 != null) {
                parents.add(lb1); //第1个数据
            }
        }
    }

    /**
     * 获取父标签内容
     *
     * @param vo1
     * @return
     */
    private LabelParentBean getParentBean(DestinationTagGroupVo vo1) {
        if (vo1 == null) {
            return null;
        }
        LabelParentBean bean = new LabelParentBean();
        LabelBean beanL = new LabelBean();
        beanL.id = vo1.tagId;
        beanL.name = vo1.tagName;
        beanL.depIdSet = vo1.goodsDepCityIdSet;
        bean.parentLabel = beanL;
        bean.childs = getChildTags(vo1.subTagList);
        return bean;
    }

    /**
     * 构建子标签数据
     *
     * @param subTagList
     * @return
     */
    private List<LabelBean> getChildTags(List<DestinationTagVo> subTagList) {
        if (subTagList == null || subTagList.size() == 0) {
            return null;
        }
        List<LabelBean> list = new ArrayList<>();
        for (DestinationTagVo vo : subTagList) {
            LabelBean beanL = new LabelBean();
            beanL.id = vo.tagId;
            beanL.name = vo.tagName;
            list.add(beanL);
        }
        return list;
    }

    /**
     * 获取选择的标签关联城市
     *
     * @param depIdSet
     * @return
     */
    public Map<String, Boolean> getDepCityIds(List<String> depIdSet) {
        Map<String, Boolean> ids = new HashMap<>();
        if (depIdSet != null) {
            for (String dep : depIdSet) {
                ids.put(dep, true);
            }
        }
        return ids;
    }

    /**
     * 获取城市关联标签
     *
     * @param destinationTagGroupList
     * @param cityId
     * @return
     */
    public Map<String, Boolean> getDepTagIds(List<DestinationTagGroupVo> destinationTagGroupList, String cityId) {
        Map<String, Boolean> ids = new HashMap<>();
        if (destinationTagGroupList != null && destinationTagGroupList.size() > 0) {
            for (DestinationTagGroupVo vo : destinationTagGroupList) {
                if (vo.goodsDepCityIdSet.contains(cityId)) {
                    ids.put(vo.tagId, true);
                }
                ids.putAll(getSubTagDep(vo.subTagList, cityId));
            }
        }
        return ids;
    }

    /**
     * 获取关联该城市的子标签
     *
     * @param subTagList
     * @param cityId
     * @return
     */
    private Map<String, Boolean> getSubTagDep(List<DestinationTagVo> subTagList, String cityId) {
        Map<String, Boolean> ids = new HashMap<>();
        if (subTagList != null && subTagList.size() > 0) {
            for (DestinationTagVo vo1 : subTagList) {
                if (vo1.goodsDepCityIdSet.contains(cityId)) {
                    ids.put(vo1.tagId, true);
                }
            }
        }
        return ids;
    }
}