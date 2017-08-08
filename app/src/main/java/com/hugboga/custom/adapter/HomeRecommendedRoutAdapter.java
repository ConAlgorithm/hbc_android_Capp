package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.FilterSkuListActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeCityContentVo2;
import com.hugboga.custom.data.bean.HomeCityGoodsVo;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by zhangqiang on 17/8/3.
 */

public class HomeRecommendedRoutAdapter extends PagerAdapter {

    HomeCityContentVo2 homeCityContentVo2;
    Context context;
    public OnChangedLister onChangedLister;
    public HomeRecommendedRoutAdapter(Context context,HomeCityContentVo2 homeCityContentVo2) {
        this.homeCityContentVo2 = homeCityContentVo2;
        this.context = context;

    }

    @Override
    public int getCount() {
        return homeCityContentVo2.cityGoodsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        //EventBus.getDefault().post(new EventAction(EventType.REFRESH_POSITION,position));
        if(onChangedLister != null){
            onChangedLister.lister(position,homeCityContentVo2.cityName,homeCityContentVo2.cityGoodsList);
        }
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_child, null);
        //new LinearLayout(container.getContext());
        ImageView imageView = (ImageView) linearLayout.findViewById(R.id.pager_img);
        imageView.getLayoutParams().width = UIUtils.getScreenWidth()-2*UIUtils.dip2px(16);
        imageView.getLayoutParams().height = imageView.getLayoutParams().width * 189 /330;
        //textView.setText(position + "");
        //linearLayout.setId(R.id.item_id);
        Tools.showImage(imageView,homeCityContentVo2.cityGoodsList.get(position).goodsPic);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SkuDetailActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, homeCityContentVo2.cityGoodsList.get(position).goodsDetailUrl);
                intent.putExtra(Constants.PARAMS_ID, homeCityContentVo2.cityGoodsList.get(position).goodsNo);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                context.startActivity(intent);
            }
        });
        container.addView(linearLayout);
        linearLayout.getLayoutParams().height = imageView.getLayoutParams().height +UIUtils.dip2px(15);
//        linearLayout.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, container.getContext().getResources().getDisplayMetrics());
//        linearLayout.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, container.getContext().getResources().getDisplayMetrics());
        return linearLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LinearLayout view = (LinearLayout) object;
        container.removeView(view);
    }

    public void setData(HomeCityContentVo2 homeCityContentVo2){
        this.homeCityContentVo2 = homeCityContentVo2;
        notifyDataSetChanged();
    }

    public void setOnChangedLister(OnChangedLister onChangedLister ){
        this.onChangedLister = onChangedLister;
    }

    public interface OnChangedLister {
        public void lister(int position,String cityName, ArrayList<HomeCityGoodsVo> homeCityGoodsVos);
    }

    public String getEventSource() {
        return "首页";
    }
}
