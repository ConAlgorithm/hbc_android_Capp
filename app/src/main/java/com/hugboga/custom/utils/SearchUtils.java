package com.hugboga.custom.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Created by zhangqiang on 17/8/22.
 */

public class SearchUtils {

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
            Pattern pattern = Pattern.compile(keyword);
            Matcher matcher = pattern.matcher(ss);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                ss.setSpan(new ForegroundColorSpan(color), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return ss;
        }
        return null;
    }
}
