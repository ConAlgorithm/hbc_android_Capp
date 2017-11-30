package com.hugboga.custom.models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.widget.TagGroup;
import com.hugboga.custom.widget.city.CityWhatDoTag;
import com.hugboga.tools.NetImg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 目的地下单入口配置
 * Created by HONGBO on 2017/11/30 10:40.
 */

public class CityConfigModel extends EpoxyModelWithHolder<CityConfigModel.CityConfigVH> {

    Context mContext;
    ServiceConfigVo vo;

    public CityConfigModel(Context mContext, ServiceConfigVo vo) {
        this.mContext = mContext;
        this.vo = vo;
    }

    @Override
    protected CityConfigModel.CityConfigVH createNewHolder() {
        return new CityConfigVH();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_what_view_layout;
    }

    @Override
    public void bind(CityConfigVH holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        init(holder, vo);
    }

    public void init(CityConfigVH holder, ServiceConfigVo vo) {
        NetImg.showImage(mContext, holder.city_what_view_img, vo.imageUrl);
        holder.city_what_view_title.setText(vo.title);
        holder.city_what_view_subtitle.setText(vo.desc);
        addTags(holder, vo.serviceLabelList);
    }

    private void addTags(CityConfigVH holder, List<String> tags) {
        holder.city_what_view_group.removeAllViews();
        if (tags != null && tags.size() > 0) {
            for (String name : tags) {
                CityWhatDoTag tagView = new CityWhatDoTag(mContext);
                tagView.init(name);
                holder.city_what_view_group.addTag(tagView);
            }
        }
    }

    public class CityConfigVH extends EpoxyHolder {

        @BindView(R.id.city_what_view_img)
        ImageView city_what_view_img; //背景图片
        @BindView(R.id.city_what_view_title)
        TextView city_what_view_title; //标题
        @BindView(R.id.city_what_view_subtitle)
        TextView city_what_view_subtitle; //副标题
        @BindView(R.id.city_what_view_group)
        TagGroup city_what_view_group; //标签库

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
