package com.hugboga.custom.data.bean;

import com.hugboga.custom.adapter.MenuItemAdapter;

/**
 * Created by Administrator on 2016/3/9.
 */
public class LvMenuItem {

    public String name;
    public int icon;
    public MenuItemAdapter.ItemType itemType;

    public LvMenuItem(int icon, String name, MenuItemAdapter.ItemType itemType) {
        this.icon = icon;
        this.name = name;
        this.itemType = itemType;
    }

    public LvMenuItem(int icon, String name) {
        this.icon = icon;
        this.name = name;
        this.itemType = MenuItemAdapter.ItemType.DEFAULT;
    }

    public LvMenuItem(MenuItemAdapter.ItemType itemType) {
        this.itemType = itemType;
    }
}
