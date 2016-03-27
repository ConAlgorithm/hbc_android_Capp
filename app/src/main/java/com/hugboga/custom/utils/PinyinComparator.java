package com.hugboga.custom.utils;

import com.hugboga.custom.data.bean.AreaCodeBean;

import java.util.Comparator;

/**
 * @author xiaanming
 */
public class PinyinComparator implements Comparator<AreaCodeBean> {

    public int compare(AreaCodeBean o1, AreaCodeBean o2) {
        if (o1 == null || o2 == null || o1.getSortLetters() == null || o2.getSortLetters() == null) {
            return 1;
        }
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
