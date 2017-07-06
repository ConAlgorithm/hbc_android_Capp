package com.hugboga.custom.utils;

import android.text.TextUtils;

import com.hugboga.custom.data.bean.FilterItemBase;
import com.hugboga.custom.data.request.RequestFilterGuide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/6/23.
 */
public final class FilterTagUtils {


    public static void resetLocalLangsList(List<? extends FilterItemBase> list) {
        if (list != null) {
            int labelsSize = list.size();
            for (int i = 0; i < labelsSize; i++) {
                if (TextUtils.equals(RequestFilterGuide.MANDARIN_ID + "",list.get(i).getTagId())) {
                    list.get(i).isSelected = true;
                } else {
                    list.get(i).isSelected = false;
                }
            }
        }
    }

    public static void reset(List<? extends FilterItemBase> list) {
        if (list != null) {
            int labelsSize = list.size();
            for (int i = 0; i < labelsSize; i++) {
                list.get(i).isSelected = false;
            }
        }
    }

    public static int getOperateCount(List<? extends FilterItemBase> list) {
        int operateCount = 0;
        if (list != null) {
            int labelsSize = list.size();
            for (int i = 0; i < labelsSize; i++) {
                if (list.get(i).isSelected) {
                    operateCount++;
                }
            }
        }
        return operateCount;
    }

    public static String getIds(ArrayList<? extends FilterItemBase> list) {
        if (list == null) {
            return null;
        }
        String result = "";
        final int size = list.size();
        for (int i = 0; i < size; i++) {
            if (!list.get(i).isSelected) {
                continue;
            }
            if (!TextUtils.isEmpty(result)) {
                result += ",";
            }
            result += list.get(i).getTagId();
        }
        return result;
    }

    public static String getLocalLangsIds(ArrayList<? extends FilterItemBase> list) {
        if (list == null) {
            return null;
        }
        String result = "";
        final int size = list.size();
        for (int i = 0; i < size; i++) {
            if (!list.get(i).isSelected || ("" + RequestFilterGuide.MANDARIN_ID).equals(list.get(i).getTagId())) {
                continue;
            }
            if (!TextUtils.isEmpty(result)) {
                result += ",";
            }
            result += list.get(i).getTagId();
        }
        return result;
    }
}
