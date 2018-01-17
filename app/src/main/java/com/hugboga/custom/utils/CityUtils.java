package com.hugboga.custom.utils;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.hugboga.custom.activity.ChooseCityActivity;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.LineGroupBean;
import com.hugboga.custom.data.bean.LineGroupItem;
import com.hugboga.custom.data.bean.LineHotSpotBean;
import com.hugboga.custom.data.bean.SearchGroupBean;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CityUtils {


    public static SpannableString addImg(Activity activity, String content, int imgIds) {
        Drawable drawable = activity.getResources().getDrawable(imgIds);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        SpannableString spannable = new SpannableString("[icon]  " + content);
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(span, 0, "[icon]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 热门城市
     *
     * @param groupId
     */
    public static List<CityBean> requestHotDate(Activity activity, int groupId, int cityId, String from) {
        List<CityBean> sourceDateList = new ArrayList<CityBean>();//全部城市数据
        DbManager mDbManager = new DBHelper(activity).getDbManager();
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("is_passcity_hot", "=", 1);

        if (groupId == -1) {
            selector.and("is_daily", "=", 1);
        } else {
            selector.and("group_id", "=", groupId);
        }
        if (("lastCity".equals(from) || ChooseCityActivity.GROUP_OUTTOWN.equals(from)) && cityId != -1) {
            selector.and("city_id", "<>", cityId);
        }

        // 修改热门城市排序
        selector.orderBy("passcity_hot_weight", true);
//        selector.orderBy("hot_weight");
        selector.limit(30);
        try {
            List<CityBean> hotCityDate = selector.findAll();
            if (hotCityDate != null && hotCityDate.size() > 0) {
                for (CityBean bean : hotCityDate) {
                    bean.firstLetter = "热门城市";
                }
            }
            if (hotCityDate != null && hotCityDate.size() > 0 && hotCityDate.get(0) != null) {
                hotCityDate.get(0).isFirst = true;
                sourceDateList.add(0, hotCityDate.get(0));
                return hotCityDate;
            }
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static String getHotCityStr(List<CityBean> hotCitys) {
        if (null == hotCitys || hotCitys.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("热门城市:");
        CityBean cityBean;
        int size = hotCitys.size();
        for (int i = 0; i < size; i++) {
            cityBean = hotCitys.get(i);
            if (i == (size - 1) || i == 19) {
                stringBuffer.append(cityBean.name);
                if (i == 19) {
                    stringBuffer.append("等");
                    return stringBuffer.toString();
                }
            } else {
                stringBuffer.append(cityBean.name).append(",");
            }
        }
        return stringBuffer.toString();
    }

    public static List<CityBean> requestDataByKeyword(Activity activity, int groupId, int cityId,
                                                      String keyword, boolean isNeedMore) {
        List<CityBean> dataList = new ArrayList<CityBean>();
        DbManager mDbManager = new DBHelper(activity).getDbManager();
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("1", "=", "1");
        if (!TextUtils.isEmpty(keyword)) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            if (isNeedMore) {
                whereBuilder.and("cn_name", "LIKE", "%" + keyword + "%");
            } else {
                whereBuilder.and("cn_name", "LIKE", keyword + "%");
            }
            selector.and(whereBuilder);
        }

        if (groupId == -1) {
            selector.and("is_daily", "=", 1);
        } else {
            selector.and("group_id", "=", groupId);
        }
        selector.and("city_id", "<>", cityId);

        try {
            dataList = selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (dataList.size() > 0) {
            for (CityBean cb : dataList) {
                cb.keyWord = keyword;
            }
        }
        return dataList;
    }

    //获取国家下面的热门线路
    public static List<SearchGroupBean> getCountryLine(Activity activity, String parent_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();

            String sql = "select * from line_group where level=1;";

            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);

            List<LineGroupBean> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            LineGroupBean searchGroupBean = new LineGroupBean();
                            searchGroupBean.isSelected = false;

                            searchGroupBean.group_id = model.getInt("group_id");
                            searchGroupBean.group_name = model.getString("group_name");

                            searchGroupBean.type = model.getInt("type");

                            searchGroupBean.has_sub = model.getInt("has_sub");

                            searchGroupBean.hot_weight = model.getInt("hot_weight");
                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return lineGroupBeanAdapter(list, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //获取国家(搜索用)国家下面的线路
    public static List<SearchGroupBean> getCountrySearch(Activity activity, int sub_place_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();

            String sql = "select * from line_group where parent_type=2 and parent_id=" + sub_place_id + " order by hot_weight desc";

            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);

            List<LineGroupBean> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    int listsize = modelList.size();
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            LineGroupBean searchGroupBean = new LineGroupBean();
                            searchGroupBean.isSelected = false;

                            searchGroupBean.group_id = model.getInt("group_id");
                            searchGroupBean.group_name = model.getString("group_name");

                            searchGroupBean.type = 1;// model.getInt("type");

                            searchGroupBean.has_sub = model.getInt("has_sub");

                            searchGroupBean.hot_weight = model.getInt("hot_weight");
                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return lineGroupBeanAdapter(list, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //获取国家下面的城市
    public static List<SearchGroupBean> getCountryCity(Activity activity, String sub_place_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupItem.class);
            selector.where("type", "=", 3);
            selector.and("sub_place_id", "=", sub_place_id);
            selector.orderBy("hot_weight", true);
            selector.limit(5);
            List<LineGroupItem> list = selector.findAll();
            return lineGroupItemAdapter(list, 3);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //搜索一级线路下面的line
    public static List<SearchGroupBean> getLevel1Line(Activity activity, String parent_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();

            String sql = "select * from line_group where parent_id=" + parent_id;

            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);

            List<LineGroupBean> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            LineGroupBean searchGroupBean = new LineGroupBean();
                            searchGroupBean.isSelected = false;

                            searchGroupBean.group_id = model.getInt("group_id");
                            searchGroupBean.group_name = model.getString("group_name");

                            searchGroupBean.type = 1;//model.getInt("type");

                            searchGroupBean.has_sub = model.getInt("has_sub");

                            searchGroupBean.hot_weight = model.getInt("hot_weight");
                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return lineGroupBeanAdapter(list, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //搜索用
    public static List<SearchGroupBean> getCountLineCity(Activity activity, SearchGroupBean bean) {
        List<SearchGroupBean> list = new ArrayList<>();
        int max_line_num = 3;
        int max_city_num = 5;
        if (bean.type == 1 && bean.level == 1) {
            List<SearchGroupBean> lineList = getLevel1Line(activity, bean.group_id + "");

            if (null != lineList && lineList.size() > 0) {
                if (lineList.size() >= max_line_num) {
                    for (int i = 0; i < max_line_num; i++) {
                        list.add(lineList.get(i));
                    }
                } else {
                    list.addAll(lineList);
                }
            }

            List<SearchGroupBean> level2List = getType1City(activity, bean.group_id);
            if (list.size() < max_line_num) {
                if (null != level2List && level2List.size() > 0) {
                    int hasSize = list.size();
                    if ((max_line_num - hasSize) > level2List.size()) {
                        list.addAll(level2List);
                    } else {
                        for (int i = 0; i < max_line_num - hasSize; i++) {
                            list.add(level2List.get(i));
                        }
                    }

                }
            }

            if (null != lineList && lineList.size() > 0 || null != level2List && level2List.size() > 0) {
                SearchGroupBean searchGroupBean = new SearchGroupBean();
                searchGroupBean.group_id = -100;
                searchGroupBean.group_name = getShowName(bean) + "多地畅游";
                list.add(0, searchGroupBean);
            }


            List<SearchGroupBean> cityList = new ArrayList<>();
            List<SearchGroupBean> cityListTmp = new ArrayList<>();
            List<SearchGroupBean> type2List = getType2City(activity, bean.group_id);
            List<SearchGroupBean> type3List = getType3City(activity, bean.group_id);

            if (null != type2List && type2List.size() > 0) {
                cityListTmp.addAll(type2List);
            }

            if (null != type3List && type3List.size() > 0) {
                cityListTmp.addAll(type3List);
            }

            if (cityListTmp.size() > max_city_num) {
                for (int i = 0; i < max_city_num; i++) {
                    cityList.add(cityListTmp.get(i));
                }
            } else {
                cityList.addAll(cityListTmp);
            }

            if (null != type2List && type2List.size() > 0 || null != type3List && type3List.size() > 0) {
                SearchGroupBean searchGroupBean = new SearchGroupBean();
                searchGroupBean.group_id = -200;
                searchGroupBean.group_name = getShowName(bean) + "热门目的地";
                cityList.add(0, searchGroupBean);
                list.addAll(cityList);
            }
        }

        if (bean.type == 2) {

            List<SearchGroupBean> lineList = getCountrySearch(activity, bean.sub_place_id);
            List<SearchGroupBean> lineListTmp = new ArrayList<>();

            if (lineList.size() < max_line_num) {
                if (null != lineList && lineList.size() > 0) {
                    int hasSize = list.size();
                    if ((max_line_num - hasSize) > lineList.size()) {
                        lineListTmp.addAll(lineList);
                    } else {
                        for (int i = 0; i < max_line_num - hasSize; i++) {
                            lineListTmp.add(lineList.get(i));
                        }
                    }

                }
            }

            if (null != lineList && lineList.size() > 0) {
                SearchGroupBean searchGroupBean = new SearchGroupBean();
                searchGroupBean.group_id = -100;
                searchGroupBean.group_name = getShowName(bean) + "多地畅游";
                lineListTmp.add(0, searchGroupBean);
                list.addAll(lineListTmp);
            }


            List<SearchGroupBean> cityList = getType4City(activity, bean.sub_place_id);
            List<SearchGroupBean> cityListTmp = new ArrayList<>();

            if (null != cityList && cityList.size() > 0) {
                if (cityList.size() > max_city_num) {
                    for (int i = 0; i < max_city_num; i++) {
                        cityListTmp.add(cityList.get(i));
                    }
                } else {
                    cityListTmp.addAll(cityList);
                }

                SearchGroupBean searchGroupBean = new SearchGroupBean();
                searchGroupBean.group_id = -200;
                searchGroupBean.group_name = getShowName(bean) + "热门目的地";
                cityListTmp.add(0, searchGroupBean);
                list.addAll(cityListTmp);
            }
        }

        return list;

    }


    public static List<SearchGroupBean> lineGroupBeanAdapter(List<LineGroupBean> list, int flag) {
        List<SearchGroupBean> searchList = new ArrayList<>();
        SearchGroupBean searchGroupBean = null;
        if (null == list) {
            return searchList;
        }
        for (LineGroupBean bean : list) {
            searchGroupBean = new SearchGroupBean();
            searchGroupBean.group_id = bean.group_id;
            searchGroupBean.group_name = bean.group_name;

            searchGroupBean.parent_name = bean.parent_name;
            searchGroupBean.parent_id = bean.parent_id;
            searchGroupBean.has_sub = bean.has_sub;

            searchGroupBean.level = bean.level;
            searchGroupBean.type = 1;
            searchGroupBean.flag = flag;
            searchList.add(searchGroupBean);
        }
        return searchList;
    }

    public static List<SearchGroupBean> lineGroupItemAdapter(List<LineGroupItem> list, int flag) {
        List<SearchGroupBean> searchList = new ArrayList<>();
        if (null == list) {
            return searchList;
        }
        SearchGroupBean searchGroupBean = null;
        for (LineGroupItem bean : list) {
            searchGroupBean = new SearchGroupBean();
            searchGroupBean.group_id = bean.group_id;
            searchGroupBean.group_name = bean.group_name;

            searchGroupBean.sub_place_id = bean.sub_place_id;
            searchGroupBean.sub_place_name = bean.sub_place_name;

            searchGroupBean.sub_city_id = bean.sub_city_id;
            searchGroupBean.sub_city_name = bean.sub_city_name;

            searchGroupBean.has_sub = bean.has_sub;

            searchGroupBean.type = bean.type;
            searchGroupBean.flag = flag;
            searchList.add(searchGroupBean);
        }
        return searchList;
    }

    public static List<SearchGroupBean> lineHotCityAdapter(List<LineHotSpotBean> list) {
        List<SearchGroupBean> searchList = new ArrayList<>();
        SearchGroupBean searchGroupBean = null;
        if (null == list) {
            return searchList;
        }
        for (LineHotSpotBean bean : list) {
            searchGroupBean = new SearchGroupBean();
            searchGroupBean.spot_id = bean.spot_id;
            searchGroupBean.spot_name = bean.spot_name;
            searchGroupBean.type = bean.type;
            searchGroupBean.flag = 4;
            searchList.add(searchGroupBean);
        }
        return searchList;
    }


    public static List<SearchGroupBean> getLevel1City(Activity activity) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();

            String sql = "select * from line_group where level=1";

            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);

            List<LineGroupBean> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            LineGroupBean searchGroupBean = new LineGroupBean();
                            searchGroupBean.isSelected = false;

                            searchGroupBean.group_name = model.getString("group_name");
                            searchGroupBean.group_id = model.getInt("group_id");

                            searchGroupBean.parent_name = model.getString("parent_name");
                            searchGroupBean.parent_id = model.getInt("parent_id");

                            searchGroupBean.has_sub = model.getInt("has_sub");

                            searchGroupBean.hot_weight = model.getInt("hot_weight");

                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return lineGroupBeanAdapter(list, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    -- 组 parentType 1.组 2.国家
//    select * from line_group where parent_type=1 and parent_id=#{选择的组ID}

    public static List<SearchGroupBean> getType1City(Activity activity, int group_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();

            String sql = "select * from line_group where parent_type=1 and parent_id=" + group_id + " order by hot_weight";
            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);

            List<LineGroupBean> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            LineGroupBean searchGroupBean = new LineGroupBean();
                            searchGroupBean.isSelected = false;

                            searchGroupBean.group_name = model.getString("group_name");
                            searchGroupBean.group_id = model.getInt("group_id");

                            searchGroupBean.parent_name = model.getString("parent_name");
                            searchGroupBean.parent_id = model.getInt("parent_id");

                            searchGroupBean.has_sub = model.getInt("has_sub");

                            searchGroupBean.hot_weight = model.getInt("hot_weight");

                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return lineGroupBeanAdapter(list, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


//    -- 国家
//    select * from line_group_item where type=2 and group_id=#{选择的组ID};

    public static List<SearchGroupBean> getType2City(Activity activity, int group_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();

            String sql = "select * from line_group_item where type=2 and group_id=" + group_id + " and sub_place_name != '中国' and sub_place_name != '中国大陆' order by hot_weight desc";
            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);

            List<LineGroupItem> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            LineGroupItem searchGroupBean = new LineGroupItem();
                            searchGroupBean.isSelected = false;

                            searchGroupBean.group_name = model.getString("group_name");
                            searchGroupBean.group_id = model.getInt("group_id");

                            searchGroupBean.sub_city_name = model.getString("sub_city_name");
                            searchGroupBean.sub_city_id = model.getInt("sub_city_id");

                            searchGroupBean.sub_place_name = model.getString("sub_place_name");
                            searchGroupBean.sub_place_id = model.getInt("sub_place_id");

                            searchGroupBean.type = model.getInt("type");

                            searchGroupBean.has_sub = model.getInt("has_sub");

                            searchGroupBean.hot_weight = model.getInt("hot_weight");

                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return lineGroupItemAdapter(list, 2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<SearchGroupBean> getType31City(Activity activity, int group_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();

            String sql = "select * from line_group_item where type=3 and group_id=" + group_id;
            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);

            List<LineGroupItem> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            LineGroupItem searchGroupBean = new LineGroupItem();
                            searchGroupBean.isSelected = false;

                            searchGroupBean.group_name = model.getString("group_name");
                            searchGroupBean.group_id = model.getInt("group_id");

                            searchGroupBean.sub_city_name = model.getString("sub_city_name");
                            searchGroupBean.sub_city_id = model.getInt("sub_city_id");

                            searchGroupBean.sub_place_name = model.getString("sub_place_name");
                            searchGroupBean.sub_place_id = model.getInt("sub_place_id");

                            searchGroupBean.type = model.getInt("type");

                            searchGroupBean.has_sub = model.getInt("has_sub");

                            searchGroupBean.hot_weight = model.getInt("hot_weight");

                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return lineGroupItemAdapter(list, 3);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //    -- 城市
//    select * from line_group_item where type=3 and group_id=#{选择的组ID};
    public static List<SearchGroupBean> getType3City(Activity activity, int group_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();

            String sql = "select gi.* from line_group_item as gi join city on(gi.sub_city_id=city.city_id) where gi.type=3 and (city.has_airport=1 or city.is_daily=1 or city.is_single=1 or city.has_goods=1) and gi.group_id=" + group_id + " and gi.sub_place_name!='中国' AND gi.sub_place_name!='中国大陆' order by gi.hot_weight desc";
            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);

            List<LineGroupItem> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            LineGroupItem searchGroupBean = new LineGroupItem();
                            searchGroupBean.isSelected = false;

                            searchGroupBean.group_name = model.getString("group_name");
                            searchGroupBean.group_id = model.getInt("group_id");

                            searchGroupBean.sub_city_name = model.getString("sub_city_name");
                            searchGroupBean.sub_city_id = model.getInt("sub_city_id");

                            searchGroupBean.sub_place_name = model.getString("sub_place_name");
                            searchGroupBean.sub_place_id = model.getInt("sub_place_id");

                            searchGroupBean.type = model.getInt("type");

                            searchGroupBean.has_sub = model.getInt("has_sub");

                            searchGroupBean.hot_weight = model.getInt("hot_weight");

                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return lineGroupItemAdapter(list, 3);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<SearchGroupBean> getLevel2City(Activity activity, int group_id) {
        try {
            List<SearchGroupBean> list = new ArrayList<>();
            list.addAll(getType1City(activity, group_id));
            list.addAll(getType2City(activity, group_id));
            list.addAll(getType3City(activity, group_id));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<SearchGroupBean> getLevel3City(Activity activity, int place_id) {
        try {
            List<SearchGroupBean> list = new ArrayList<>();
            List<SearchGroupBean> list3 = getType4City(activity, place_id);
            if (null != list3 && list3.size() > 0) {
                list.addAll(list3);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<SearchGroupBean> getType4City(Activity activity, int place_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();

            String sql = "select -1 as group_id, '' as group_name, 4 as type, city_id as sub_city_id, cn_name as sub_city_name, place_name as sub_place_name, -1 as sub_group_id, '' as sub_group_name, city.hot_weight " +
                    "from city where city.place_id=" + place_id + " and (city.has_airport=1 or city.is_daily=1 or city.is_single=1 or city.has_goods=1) order by hot_weight desc;";

            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);

            List<CityBean> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    int specialofferlistindex = listsize;
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            CityBean searchGroupBean = new CityBean();
                            searchGroupBean.isSelected = false;
                            searchGroupBean.name = model.getString("sub_city_name");
                            searchGroupBean.cityId = model.getInt("sub_city_id");

                            searchGroupBean.placeName = model.getString("sub_place_name");
                            searchGroupBean.hotWeight = model.getInt("hot_weight");

                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return cityAdapter(list);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<SearchGroupBean> cityAdapter(List<CityBean> list) {
        List<SearchGroupBean> searchList = new ArrayList<>();
        if (null == list) {
            return searchList;
        }
        SearchGroupBean searchGroupBean = null;
        for (CityBean bean : list) {
            searchGroupBean = new SearchGroupBean();
            searchGroupBean.group_id = -1;
            searchGroupBean.group_name = "";

            searchGroupBean.sub_place_id = -1;
            searchGroupBean.sub_place_name = bean.placeName;

            searchGroupBean.sub_city_id = bean.cityId;
            searchGroupBean.sub_city_name = bean.name;

            searchGroupBean.hot_weight = bean.hotWeight;

            searchGroupBean.type = 3;
            searchGroupBean.flag = 3;
            searchList.add(searchGroupBean);
        }
        return searchList;
    }

    //type 1 组  2,国家  3, 城市
    //热门   type 1 = 城市   2, 国家

    //获取热门城市
    public static List<SearchGroupBean> getHotCity(Activity activity) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineHotSpotBean.class);
            selector.orderBy("hot_weight", true);
            List<LineHotSpotBean> list = selector.findAll();
            return lineHotCityAdapter(list);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SearchGroupBean> getHotCityWithHead(Activity activity) {
        List<SearchGroupBean> hotCityList = getHotCity(activity);
        SearchGroupBean searchGroupBean = new SearchGroupBean();
        searchGroupBean.spot_id = -1;
        searchGroupBean.spot_name = "接送机";
        searchGroupBean.flag = 4;
        hotCityList.add(0, searchGroupBean);

        searchGroupBean = new SearchGroupBean();
        searchGroupBean.spot_id = -2;
        searchGroupBean.flag = 4;
        searchGroupBean.spot_name = "单次接送";
        hotCityList.add(1, searchGroupBean);

        searchGroupBean = new SearchGroupBean();
        searchGroupBean.spot_id = -3;
        searchGroupBean.flag = 4;
        searchGroupBean.spot_name = "包车游";
        hotCityList.add(2, searchGroupBean);
        return hotCityList;
    }

    public static List<SearchGroupBean> getHotCityWithAllCityHead(Activity activity) {
        List<SearchGroupBean> hotCityList = getHotCity(activity);
        SearchGroupBean searchGroupBean = new SearchGroupBean();
        searchGroupBean.spot_id = -4;
        searchGroupBean.spot_name = "全部目的地";
        searchGroupBean.flag = 4;
        hotCityList.add(0, searchGroupBean);
        return hotCityList;
    }


    public static List<SearchGroupBean> getSaveCity() {
        try {
            Type resultType = new TypeToken<List<SearchGroupBean>>() {
            }.getType();
            return Reservoir.get("savedHistoryCity", resultType);
        } catch (Exception e) {
            return null;
        }
    }

    public static void addCityHistoryData(SearchGroupBean searchGroupBean) {

        List<SearchGroupBean> list = getSaveCity();
        if (null == list) {
            list = new ArrayList<>();
        } else {

            if (list.size() > 10) {
                for (int i = 0; i < (list.size() - 10); i++) {
                    list.remove(i);
                }
            }

            for (int i = list.size() - 1; i >= 0; i--) {
                if (searchGroupBean.flag == 2) {//2,国家
                    if (searchGroupBean.sub_place_id == list.get(i).sub_place_id) {
                        list.remove(i);
                    } else if (searchGroupBean.sub_place_name != null && TextUtils.equals(searchGroupBean.sub_place_name, list.get(i).spot_name)) {
                        list.remove(i);
                    }
                } else if (searchGroupBean.flag == 3) {//3,城市
                    if (searchGroupBean.sub_city_id == list.get(i).sub_city_id) {
                        list.remove(i);
                    } else if (searchGroupBean.sub_city_name != null && TextUtils.equals(searchGroupBean.sub_city_name, list.get(i).spot_name)) {
                        list.remove(i);
                    }
                } else if (searchGroupBean.flag == 4) {//4,热门
                    if (searchGroupBean.spot_id == list.get(i).spot_id) {
                        list.remove(i);
                    } else if (searchGroupBean.sub_place_name != null && TextUtils.equals(searchGroupBean.sub_place_name, list.get(i).spot_name)) {
                        list.remove(i);
                    } else if (searchGroupBean.sub_city_name != null && TextUtils.equals(searchGroupBean.sub_city_name, list.get(i).spot_name)) {
                        list.remove(i);
                    } else if (searchGroupBean.group_name != null && TextUtils.equals(searchGroupBean.group_name, list.get(i).spot_name)) {
                        list.remove(i);
                    } else if (searchGroupBean.spot_name != null) {
                        if (TextUtils.equals(searchGroupBean.spot_name, list.get(i).spot_name)
                                || TextUtils.equals(searchGroupBean.spot_name, list.get(i).sub_place_name)
                                || TextUtils.equals(searchGroupBean.spot_name, list.get(i).sub_city_name)
                                || TextUtils.equals(searchGroupBean.spot_name, list.get(i).group_name)) {
                            list.remove(i);
                        }
                    }
                } else if (searchGroupBean.flag == 1) {//1,线路
                    if (searchGroupBean.group_id == list.get(i).group_id && TextUtils.isEmpty(list.get(i).sub_city_name) && TextUtils.isEmpty(list.get(i).sub_place_name)) {
                        list.remove(i);
                    }
                }
            }
        }
        list.add(searchGroupBean);
        try {
            Reservoir.put("savedHistoryCity", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //type 1 组  2,国家  3, 城市
    //热门   type 1 = 城市   2, 国家
    public static boolean canGoCityList(SearchGroupBean searchGroupBean) {
        if (searchGroupBean.flag == 2) {
            if (searchGroupBean.type == 1) {
                return true;
            } else if (searchGroupBean.type == 2) {
                return false;
            } else if (searchGroupBean.type == 3) {
                return true;
            }
        } else if (searchGroupBean.flag == 4) {
            return true;
        }
        return true;
    }


    public static String getShowName(SearchGroupBean searchGroupBean) {
        if (searchGroupBean.flag == 2) {
            if (searchGroupBean.type == 1) {
                return searchGroupBean.group_name;
            } else if (searchGroupBean.type == 2) {
                return searchGroupBean.sub_place_name;
            } else if (searchGroupBean.type == 3) {
                return searchGroupBean.sub_city_name;
            }
        } else if (searchGroupBean.flag == 3) {
            if (searchGroupBean.type == 1) {
                return searchGroupBean.group_name;
            } else if (searchGroupBean.type == 2) {
                return searchGroupBean.sub_place_name;
            } else if (searchGroupBean.type == 3) {
                return searchGroupBean.sub_city_name;
            }
        } else if (searchGroupBean.flag == 1) {
            return searchGroupBean.group_name;
        } else if (searchGroupBean.flag == 4) {
            return searchGroupBean.spot_name;
        }
        return "";

    }

    //获取上级名称
    public static String getParentName(SearchGroupBean searchGroupBean) {
        if (searchGroupBean.type == 1) {
            return searchGroupBean.parent_name;
        } else if (searchGroupBean.type == 2) {
            return searchGroupBean.group_name;
        } else if (searchGroupBean.type == 3) {
            return searchGroupBean.sub_place_name;
        }

        return "";

    }


    //搜索获取城市
    public static List<SearchGroupBean> searchCity(Activity activity, String key) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            String sql = "select res.* from (" +
                    "SELECT -1 as group_id, '' as group_name, 3 AS type, city_id AS sub_city_id, cn_name AS sub_city_name, place_name AS sub_place_name, - 1 AS sub_group_id, '' AS sub_group_name, city.hot_weight, 1 as rank " +
                    "FROM city " +
                    "where cn_name like '" + key + "%' and (city.has_airport=1 or city.is_daily=1 or city.is_single=1 or city.has_goods=1) " +
                    "union SELECT -1 as group_id, '' as group_name, 3 AS type, city_id AS sub_city_id, cn_name AS sub_city_name, place_name AS sub_place_name, - 1 AS sub_group_id, '' AS sub_group_name, city.hot_weight, 2 as rank " +
                    "from city where cn_name like '%" + key + "%' and cn_name not like '" + key + "%' and (city.has_airport=1 or city.is_daily=1 or city.is_single=1 or city.has_goods=1)) as res " +
                    "where res.sub_place_name!='中国' and res.sub_place_name!='中国大陆' order by rank";
            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);

            List<CityBean> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            CityBean searchGroupBean = new CityBean();
                            searchGroupBean.isSelected = false;
                            searchGroupBean.name = model.getString("sub_city_name");
                            searchGroupBean.cityId = model.getInt("sub_city_id");

                            searchGroupBean.placeName = model.getString("sub_place_name");
                            searchGroupBean.hotWeight = model.getInt("hot_weight");

                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return cityAdapter(list, 3);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<SearchGroupBean> cityAdapter(List<CityBean> list, int type) {
        List<SearchGroupBean> searchList = new ArrayList<>();
        SearchGroupBean searchGroupBean = null;
        if (null == list) {
            return searchList;
        }
        for (CityBean bean : list) {
            searchGroupBean = new SearchGroupBean();
            searchGroupBean.group_id = -1;
            searchGroupBean.group_name = "";

            searchGroupBean.sub_city_id = bean.cityId;
            searchGroupBean.sub_city_name = bean.name;

            searchGroupBean.sub_place_name = bean.placeName;
            searchGroupBean.hot_weight = bean.hotWeight;

            searchGroupBean.type = type;
            searchGroupBean.flag = type;
            searchList.add(searchGroupBean);
        }
        return searchList;
    }

    //查询线路圈
    public static List<SearchGroupBean> searchLine(Activity activity, String key) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();

            String sql = "select * from (select gp.*, 1 as rank from line_group as gp where group_name like '" + key + "%' " +
                    "union select gp.*, 2 as rank from line_group as gp where group_name like '%" + key + "%' and group_name not like '" + key + "%') order by level, rank";

            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);
            List<LineGroupBean> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            LineGroupBean searchGroupBean = new LineGroupBean();
                            searchGroupBean.isSelected = false;

                            searchGroupBean.group_id = model.getInt("group_id");
                            searchGroupBean.group_name = model.getString("group_name");

                            searchGroupBean.parent_id = model.getInt("parent_id");
                            searchGroupBean.parent_name = model.getString("parent_name");

                            searchGroupBean.has_sub = model.getInt("has_sub");

                            searchGroupBean.type = 1;

                            searchGroupBean.level = model.getInt("level");

                            searchGroupBean.hot_weight = model.getInt("hot_weight");
                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return lineGroupBeanAdapter(list, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //查询国家
    public static List<SearchGroupBean> searchCountry(Activity activity, String key) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            String sql = "select res.*, min(level) from (" +
                    "select gi.*, 1 as rank from line_group_item as gi where type=2 and sub_place_name like '" + key + "%' union select gi.*, 2 as rank from " +
                    "line_group_item as gi where type=2 and sub_place_name like '%" + key + "%' and sub_place_name not like '" + key + "%') as res " +
                    "left join line_group as gp on(res.group_id=gp.group_id) " +
                    "where sub_place_name != '中国' AND sub_place_name != '中国大陆' GROUP BY res.sub_place_id order by res.rank";
            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);
            List<LineGroupItem> list = new ArrayList<>();
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    for (int modelindex = 0; modelindex < listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            LineGroupItem searchGroupBean = new LineGroupItem();
                            searchGroupBean.isSelected = false;
                            searchGroupBean.sub_city_name = model.getString("sub_city_name");
                            searchGroupBean.sub_city_id = model.getInt("sub_city_id");

                            searchGroupBean.sub_group_id = model.getInt("sub_group_id");
                            searchGroupBean.sub_group_name = model.getString("sub_group_name");

                            searchGroupBean.group_id = model.getInt("group_id");
                            searchGroupBean.group_name = model.getString("group_name");

                            searchGroupBean.sub_place_id = model.getInt("sub_place_id");
                            searchGroupBean.sub_place_name = model.getString("sub_place_name");

                            searchGroupBean.type = model.getInt("type");

                            searchGroupBean.has_sub = model.getInt("has_sub");

                            searchGroupBean.hot_weight = model.getInt("hot_weight");
                            list.add(searchGroupBean);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return lineGroupItemAdapter(list, 2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SearchGroupBean> search(Activity activity, String key) {
        List<SearchGroupBean> list = new ArrayList<>();

        List<SearchGroupBean> lineList = searchLine(activity, key);
        List<SearchGroupBean> countryList = searchCountry(activity, key);
        List<SearchGroupBean> cityList = searchCity(activity, key);

        List<SearchGroupBean> firstLineList = new ArrayList<>();
        List<SearchGroupBean> firstCountryList = new ArrayList<>();
        List<SearchGroupBean> firstCityList = new ArrayList<>();

        List<SearchGroupBean> afterLineList = new ArrayList<>();
        List<SearchGroupBean> afterCountryList = new ArrayList<>();
        List<SearchGroupBean> afterCityList = new ArrayList<>();

        //找到首个匹配内容
        for (int j = 0; j < lineList.size(); j++) {
            String leftName = CityUtils.getShowName(lineList.get(j));
            if (leftName.startsWith(key)) {
                firstLineList.add(lineList.get(j));
            } else {
                afterLineList.add(lineList.get(j));
            }
        }
        for (int k = 0; k < countryList.size(); k++) {
            String leftName = CityUtils.getShowName(countryList.get(k));
            if (leftName.startsWith(key)) {
                firstCountryList.add(countryList.get(k));
            } else {
                afterCountryList.add(countryList.get(k));
            }
        }
        for (int i = 0; i < cityList.size(); i++) {
            String leftName = CityUtils.getShowName(cityList.get(i));
            if (leftName.startsWith(key)) {
                firstCityList.add(cityList.get(i));
            } else {
                afterCityList.add(cityList.get(i));
            }
        }
        list.addAll(firstLineList); //添加首字母匹配线路圈
        list.addAll(afterLineList); //添加非首字母匹配线路圈
        list.addAll(firstCountryList); //添加首字母匹配国家
        list.addAll(afterCountryList); //添加非首字母匹配国家
        list.addAll(firstCityList); //添加首字母匹配城市
        list.addAll(afterCityList); //添加非首字母匹配城市
        return list;
    }


    public static SpannableStringBuilder getSpannableString(String name, String keyWord) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(name);
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(Color.parseColor("#FDCE02"));
        int start = ssb.toString().indexOf(keyWord);
        if (start != -1) {
            int end = start + keyWord.length();
            ssb.setSpan(yellowSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }


    public static LineGroupBean getLineGroupBean(Activity activity, String place_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();

            String sql = "select * from line_group_item where sub_place_id=" + place_id;

            SqlInfo sqlinfo = new SqlInfo();
            sqlinfo.setSql(sql);

            LineGroupBean lineGroupBean = null;
            try {
                DbModel model = mDbManager.findDbModelFirst(sqlinfo);
                if (model != null) {
                    lineGroupBean = new LineGroupBean();
                    lineGroupBean.isSelected = false;

                    lineGroupBean.group_id = model.getInt("group_id");
                    lineGroupBean.group_name = model.getString("group_name");

                    lineGroupBean.type = 1;//model.getInt("type");

                    lineGroupBean.has_sub = model.getInt("has_sub");

                    lineGroupBean.hot_weight = model.getInt("hot_weight");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return lineGroupBean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
