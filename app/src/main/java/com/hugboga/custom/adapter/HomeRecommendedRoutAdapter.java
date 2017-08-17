package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeCityContentVo2;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

/**
 * Created by zhangqiang on 17/8/3.
 */

public class HomeRecommendedRoutAdapter extends PagerAdapter {

    HomeCityContentVo2 homeCityContentVo2;
    Context context;
    TextView des1;
    TextView cityName;
    TextView guidesNum;
    TextView des2;
    TextView tiyan;
    TextView perPrice;
    PolygonImageView polygonImageView;
    LinearLayout contentLayout;

    public HomeRecommendedRoutAdapter(Context context, HomeCityContentVo2 homeCityContentVo2) {
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

        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_child, null);
        ImageView imageView = (ImageView) linearLayout.findViewById(R.id.pager_img);
        imageView.getLayoutParams().width = UIUtils.getScreenWidth() - 2 * UIUtils.dip2px(16);
        imageView.getLayoutParams().height = imageView.getLayoutParams().width * 189 / 330;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Tools.showImage(imageView, homeCityContentVo2.cityGoodsList.get(position).goodsPic,R.mipmap.morentu_02);

        des1 = (TextView) linearLayout.findViewById(R.id.des1);
        cityName = (TextView) linearLayout.findViewById(R.id.cityName);
        guidesNum = (TextView) linearLayout.findViewById(R.id.guidesNum);
        des2 = (TextView) linearLayout.findViewById(R.id.des2);
        tiyan = (TextView) linearLayout.findViewById(R.id.tiyan);
        perPrice = (TextView) linearLayout.findViewById(R.id.perPrice);
        polygonImageView = (PolygonImageView) linearLayout.findViewById(R.id.avr);
        contentLayout = (LinearLayout) linearLayout.findViewById(R.id.content_layout);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interClick(position);
            }
        });

        des1.setText(homeCityContentVo2.cityGoodsList.get(position).goodsName);
        cityName.setText(homeCityContentVo2.cityName + "司导推荐");
        guidesNum.setText(homeCityContentVo2.cityGoodsList.get(position).guidesNum + "位中文司导可服务");
        des2.setText(homeCityContentVo2.cityGoodsList.get(position).recommendedReason);
        tiyan.setText("已体验" + homeCityContentVo2.cityGoodsList.get(position).purchases);
        perPrice.setText("¥" + homeCityContentVo2.cityGoodsList.get(position).perPrice + "起/人");
        if (!TextUtils.isEmpty(homeCityContentVo2.cityGoodsList.get(position).goodsPic)) {
            Tools.showRoundImage(polygonImageView, homeCityContentVo2.cityGoodsList.get(position).guidePic, UIUtils.dip2px(5), R.mipmap.home_head_portrait);
        }

        perPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interClick(position);
            }
        });

        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interClick(position);
            }
        });

        container.addView(linearLayout);
        linearLayout.getLayoutParams().height = imageView.getLayoutParams().height + UIUtils.dip2px(256);
//        linearLayout.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, container.getContext().getResources().getDisplayMetrics());
//        linearLayout.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, container.getContext().getResources().getDisplayMetrics());
        return linearLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LinearLayout view = (LinearLayout) object;
        container.removeView(view);
    }

    public void setData(HomeCityContentVo2 homeCityContentVo2) {
        this.homeCityContentVo2 = homeCityContentVo2;
        notifyDataSetChanged();
    }

    public String getEventSource() {
        return "首页";
    }

    private void interClick(int position) {
        Intent intent = new Intent(context, SkuDetailActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, homeCityContentVo2.cityGoodsList.get(position).goodsDetailUrl);
        intent.putExtra(Constants.PARAMS_ID, homeCityContentVo2.cityGoodsList.get(position).goodsNo);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        context.startActivity(intent);
        SensorsUtils.onAppClick(getEventSource(), "推荐线路", "首页-推荐线路");
    }
}
