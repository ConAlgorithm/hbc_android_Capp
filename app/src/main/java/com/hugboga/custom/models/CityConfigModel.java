package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.TagGroup;
import com.hugboga.custom.widget.city.CityWhatDoTag;

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
        holder.itemView.setOnClickListener(onClickListener); //点击事件
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //进入下单入口
            switch (vo.serviceType) {
                case 1:
                    //进入接送机
                    intentActivity(mContext, PickSendActivity.class);
                    break;
                case 3:
                    //进入包车
                    intentActivity(mContext, CharterFirstStepActivity.class);
                    break;
                case 4:
                    intentActivity(mContext, SingleActivity.class);
                    //进入次租
                    break;
            }
        }
    };

    private void intentActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.PARAMS_SOURCE, "目的地首页");
        context.startActivity(intent);
    }

    public void init(CityConfigVH holder, ServiceConfigVo vo) {
        Tools.showImageNotCenterCrop(holder.city_what_view_img, vo.imageUrl, R.mipmap.home_default_route_item);
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

        View itemView;

        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
