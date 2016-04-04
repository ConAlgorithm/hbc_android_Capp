package com.hugboga.custom.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.STGVImageView;

import org.xutils.view.annotation.ViewInject;

/**
 * 首页列表信息
 * Created by ZHZEPHI on 2016/4/1.
 */
public class HomeVH extends ZBaseViewHolder {

    @ViewInject(R.id.item_home_title)
    public TextView tvTitle;
    @ViewInject(R.id.item_home_sub_title)
    public TextView tvSubTitle;
    @ViewInject(R.id.item_home_img)
    public ImageView imgBg;
    @ViewInject(R.id.split_line)
    public View splitLine;

    public HomeVH(View itemView) {
        super(itemView);
    }
}
