package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.View;
import java.lang.reflect.Constructor;

/**
 * Created by qingcha on 16/11/18.
 */
public class HbcRecyclerSingleTypeAdpater<T> extends HbcRecyclerTypeBaseAdpater<T>{

    private static final int TYPE_ITEM = 0x03;
    private Class itemViewClass;
    private Context context;

    public HbcRecyclerSingleTypeAdpater(Context context, Class itemViewClass) {
        super(context);
        this.context = context;
        this.itemViewClass = itemViewClass;
    }

    @Override
    protected View getItemView(int position) {
        View resultView = null;
        try {
            Class<View> cls = (Class<View>) Class.forName(itemViewClass.getName());
            Constructor constructor = cls.getDeclaredConstructor(new Class[] {Context.class});
            constructor.setAccessible(true);
            resultView = (View) constructor.newInstance(new Object[] {context});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultView;
    }

    @Override
    protected int getChildItemViewType(int position) {
        return TYPE_ITEM;
    }
}
