package com.hugboga.custom.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.hugboga.custom.MyApplication;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Created by zhangqiang on 17/8/22.
 */

public class SearchUtils {

    public static boolean isHistory;
    public static boolean isRecommend;
    public static boolean hasResult;

    public static List<String> getSaveHistorySearch() {
        try {
            Type resultType = new TypeToken<List<String>>() {
            }.getType();
            return Reservoir.get("savedHistorySearch", resultType);
        } catch (Exception e) {
            return null;
        }
    }

    public static void addCityHistorySearch(String search) {
        List<String> list = getSaveHistorySearch();
        if (null == list) {
            list = new ArrayList<>();
        } else {
            if (list.size() > 10) {
                for (int i = 0; i < (list.size() - 10); i++) {
                    list.remove(i);
                }
            }

            for (int i = list.size() - 1; i >= 0; i--) {
                if (search.equals(list.get(i))) {
                    list.remove(i);
                }
            }
        }
        list.add(search);
        try {
            Reservoir.put("savedHistorySearch", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearHistorySearch() {
        List<String> list = getSaveHistorySearch();
        for (int i = list.size() - 1; i >= 0; i--) {
            list.remove(i);
        }
        try {
            Reservoir.put("savedHistorySearch", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //凸显关键字颜色
    public static SpannableString matcherSearchText(Context context, int color, String text, String keyword) {
        if (text != null && text.length() > 0) {
            SpannableString ss = new SpannableString(text);
            List<Integer> index = findKeyOf(text, keyword);
            for (Integer ind : index) {
                ss.setSpan(new ForegroundColorSpan(color), ind, ind + keyword.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return ss;
        }
        return null;
    }

    /**
     * 获取匹配关键字位置
     *
     * @param text
     * @param keyword
     * @return
     */
    private static List<Integer> findKeyOf(String text, String keyword) {
        List<Integer> result = new ArrayList<>();
        //开始循环取值
        String target = text;
        while (target.contains(keyword)) {
            int t = target.indexOf(keyword);
            target = target.substring(t + keyword.length());
            if (result.size() > 0) {
                result.add(result.get(result.size() - 1) + keyword.length() + t);
            } else {
                result.add(t);
            }
        }
        return result;
    }

    //搜索埋点
    public static void setSensorsShareEvent(String keyWord, boolean isHistory, boolean isRecommend, boolean hasResult) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("keyWord", keyWord);
            properties.put("isHistory", isHistory);
            properties.put("isRecommend", isRecommend);
            properties.put("hasResult", hasResult);
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("searchResult", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
