package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.IntentUtils;
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
    DestinationHomeVo data; //城市ID

    public CityConfigModel(Context mContext, ServiceConfigVo vo, DestinationHomeVo data) {
        this.mContext = mContext;
        this.vo = vo;
        this.data = data;
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
            CityBean cityBean = null; //查询城市信息
            if (CityActivity.CityHomeType.getNew(data.destinationType) == CityActivity.CityHomeType.CITY) {
                cityBean = DatabaseManager.getCityBean(String.valueOf(data.destinationId));
            }
            switch (vo.serviceType) {
                case 1:
                    //进入接送机
                    PickSendActivity.Params params = new PickSendActivity.Params();
                    if (cityBean != null) {
                        params.cityId = String.valueOf(cityBean.cityId);
                        params.cityName = cityBean.name;
                    }
                    IntentUtils.intentPickupActivity(mContext, params, "目的地首页");
                    break;
                case 3:
                    //进入包车
                    IntentUtils.intentCharterActivity(mContext, null, null, cityBean, "目的地首页");
                    break;
                case 4:
                    //进入次租
                    SingleActivity.Params params1 = new SingleActivity.Params();
                    if (cityBean != null) {
                        params1.cityId = String.valueOf(cityBean.cityId);
                    }
                    IntentUtils.intentSingleActivity(mContext, params1, "目的地首页");
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
