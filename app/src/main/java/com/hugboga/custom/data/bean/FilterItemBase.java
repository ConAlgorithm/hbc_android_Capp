package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by qingcha on 17/6/22.
 */
public abstract class FilterItemBase implements Serializable, Cloneable{

    public boolean isSelected = false;//本地字段

    public abstract String getTagId();

    public abstract String getName();
}
