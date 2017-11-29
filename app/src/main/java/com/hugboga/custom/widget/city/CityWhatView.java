package com.hugboga.custom.widget.city;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.widget.TagGroup;
import com.hugboga.tools.NetImg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 玩法列表底部下单入口
 * Created by HONGBO on 2017/11/29 20:13.
 */

public class CityWhatView extends FrameLayout {

    @BindView(R.id.city_what_view_img)
    ImageView city_what_view_img; //背景图片
    @BindView(R.id.city_what_view_title)
    TextView city_what_view_title; //标题
    @BindView(R.id.city_what_view_subtitle)
    TextView city_what_view_subtitle; //副标题
    @BindView(R.id.city_what_view_group)
    TagGroup city_what_view_group; //标签库

    public CityWhatView(@NonNull Context context) {
        this(context, null);
    }

    public CityWhatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_what_view_layout, this);
        ButterKnife.bind(this, view);
    }

    public void init(ServiceConfigVo vo) {
        NetImg.showImage(getContext(), city_what_view_img, vo.imageUrl);
        city_what_view_title.setText(vo.title);
        city_what_view_subtitle.setText(vo.desc);
        addTags(vo.serviceLabelList);
    }

    private void addTags(List<String> tags) {
        city_what_view_group.removeAllViews();
        if (tags != null && tags.size() > 0) {
            for (String name : tags) {
                CityWhatDoTag tagView = new CityWhatDoTag(getContext());
                tagView.init(name);
                city_what_view_group.addTag(tagView);
            }
        }
    }
}
