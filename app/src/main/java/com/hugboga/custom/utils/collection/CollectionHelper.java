package com.hugboga.custom.utils.collection;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * 获取收藏对象
 * Created by HONGBO on 2018/1/8 17:22.
 */

public class CollectionHelper {

    private WeakReference<Context> weakReference;

    private static volatile CollectionHelper instance;

    private CollectionLine collectionLine; //收藏线路
    private CollectionGuide collectionGuide; //收藏司导

    private CollectionHelper(Context context) {
        this.weakReference = new WeakReference<>(context);
    }

    public static CollectionHelper getIns(Context context) {
        if (instance == null) {
            synchronized (CollectionHelper.class) {
                if (instance == null) {
                    instance = new CollectionHelper(context);
                }
            }
        }
        return instance;
    }

    /**
     * 获取收藏对象
     *
     * @param collectionType
     * @return
     */
    public ICollection create(CollectionType collectionType) {
        if (weakReference == null || weakReference.get() == null) {
            return null;
        }
        switch (collectionType) {
            case LINE:
                if (collectionLine == null) {
                    collectionLine = new CollectionLine(weakReference.get());
                }
                return collectionLine;
            case GUIDE:
                if (collectionGuide == null) {
                    collectionGuide = new CollectionGuide(weakReference.get());
                }
                return collectionGuide;
            default:
                return null;
        }
    }

    /**
     * 初始化查询收藏信息
     */
    public void initCollection() {
        collectionLine = (CollectionLine) create(CollectionType.LINE);
        if (collectionLine != null) {
            collectionLine.queryFavorite();
        }
        collectionGuide = (CollectionGuide) create(CollectionType.GUIDE);
        if (collectionGuide != null) {
            collectionGuide.queryFavorite();
        }
    }

    public enum CollectionType {
        LINE, GUIDE
    }

    public CollectionLine getCollectionLine() {
        if (collectionLine == null) {
            initCollection();
        }
        return collectionLine;
    }

    public CollectionGuide getCollectionGuide() {
        if (collectionGuide == null) {
            initCollection();
        }
        return collectionGuide;
    }
}
