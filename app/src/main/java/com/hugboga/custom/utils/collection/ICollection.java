package com.hugboga.custom.utils.collection;

/**
 * 收藏
 * Created by HONGBO on 2018/1/8 17:14.
 */

public interface ICollection {

    /**
     * 重新查询收藏信息
     */
    void queryFavorite();

    /**
     * 判断是否收藏
     * @param targetNo
     * @return
     */
    boolean isCollection(String targetNo);

    /**
     * 改变收藏数据
     * @param targetNo
     * @param isCollection
     */
    void changeCollectionLine(String targetNo, boolean isCollection);
}
