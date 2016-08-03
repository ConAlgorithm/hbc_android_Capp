package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.SkuListActivity;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.SkuCityBean;
import com.hugboga.custom.fragment.FgPickSend;
import com.hugboga.custom.fragment.FgSingleNew;
import com.hugboga.custom.fragment.FgSkuList;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import static com.hugboga.custom.fragment.FgSkuList.KEY_CITY_BEAN;

/**
 * Created by qingcha on 16/6/27.
 */
public class SkuCityFooterView extends LinearLayout implements HbcViewBehavior, View.OnClickListener{

    private TextView pickupTV, singleTV;
    private ImageView emptyIV;
    private LinearLayout pickupLayout, guidesLayout, avatarsLayout;

    private SkuCityBean skuCityBean;

    public SkuCityFooterView(Context context) {
        this(context, null);
    }

    public SkuCityFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        inflate(context, R.layout.view_sku_city_footer, this);

        emptyIV = (ImageView) findViewById(R.id.sku_city_footer_empty_iv);
        pickupLayout = (LinearLayout) findViewById(R.id.sku_city_footer_pickup_layout);
        pickupTV = (TextView) findViewById(R.id.sku_city_footer_pickup_tv);
        singleTV = (TextView) findViewById(R.id.sku_city_footer_single_tv);
        guidesLayout = (LinearLayout) findViewById(R.id.sku_city_footer_guides_layout);
        avatarsLayout = (LinearLayout) findViewById(R.id.sku_city_footer_guides_avatar_layout);

        pickupTV.setOnClickListener(this);
        singleTV.setOnClickListener(this);

        int guidesLayoutHeight = (int)((367 / 750.0) * UIUtils.getScreenWidth());
        LinearLayout.LayoutParams guidesParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, guidesLayoutHeight);
        guidesLayout.setLayoutParams(guidesParams);
    }

    @Override
    public void update(Object _data) {
        if (_data == null || !(_data instanceof SkuCityBean) || skuCityBean != null) {
            return;
        }
        skuCityBean = (SkuCityBean) _data;

        //城市列表emptyView
        if (skuCityBean.goodsList == null || skuCityBean.goodsList.size() <= 0) {
            emptyIV.setVisibility(View.VISIBLE);
            float emptyViewWidth = UIUtils.getScreenWidth() / 3.0f * 2;
            float emptyViewHeight = (779 / 735.0f) * emptyViewWidth;
            LinearLayout.LayoutParams emptyParams = new LinearLayout.LayoutParams((int)emptyViewWidth, (int)emptyViewHeight);
            emptyParams.gravity = Gravity.CENTER_HORIZONTAL;
            emptyParams.topMargin = UIUtils.dip2px(30);
            emptyParams.bottomMargin = UIUtils.dip2px(50);
            emptyIV.setLayoutParams(emptyParams);
        } else {
            emptyIV.setVisibility(View.GONE);
        }

        //接送机和单次接送
        if (!skuCityBean.hasAirporService() && !skuCityBean.hasSingleService()) {
            pickupLayout.setVisibility(View.GONE);
        } else {
            pickupLayout.setVisibility(View.VISIBLE);
            float pickupTVWidth = (UIUtils.getScreenWidth() - UIUtils.dip2px(15) * 2 - UIUtils.dip2px(10)) / 2.0f;
            float pickupTVHight = (int)((86 / 241.0) * pickupTVWidth);

            if (skuCityBean.hasAirporService()) {
                pickupTV.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams pickupParams= new LinearLayout.LayoutParams((int)pickupTVWidth, (int)pickupTVHight);
                pickupParams.rightMargin = UIUtils.dip2px(10);
                pickupTV.setLayoutParams(pickupParams);
            } else {
                pickupTV.setVisibility(View.GONE);
            }

            if (skuCityBean.hasSingleService()) {
                singleTV.setVisibility(View.VISIBLE);
                singleTV.setLayoutParams(new LinearLayout.LayoutParams((int)pickupTVWidth, (int)pickupTVHight));
            } else {
                singleTV.setVisibility(View.GONE);
            }
        }

        //司导头像
        if (skuCityBean.guideAvatars != null && skuCityBean.guideAvatars.size() > 0) {
            avatarsLayout.removeAllViews();
            int size = skuCityBean.guideAvatars.size();
            int viewWidth = UIUtils.dip2px(30);
            j:for (int i = 0; i < size; i++) {
                viewWidth +=  UIUtils.dip2px(15) + UIUtils.dip2px(45);
                if (viewWidth > UIUtils.getScreenWidth()) {
                    break j;
                }
                CircleImageView circleImageView = new CircleImageView(getContext());
                circleImageView.setBackgroundResource(R.mipmap.journey_head_portrait);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2px(45), UIUtils.dip2px(45));
                params.rightMargin = UIUtils.dip2px(15);
                Tools.showImage(circleImageView, skuCityBean.guideAvatars.get(i));
                avatarsLayout.addView(circleImageView, params);
            }
        }
    }

    @Override
    public void onClick(View v) {
        SkuListActivity activity = null;
        if (getContext() instanceof SkuListActivity) {
            activity = (SkuListActivity) getContext();
        }
        CityBean cityBean = activity.getCityBean();
        switch (v.getId()) {
            case R.id.sku_city_footer_pickup_tv:
//                if (cityBean != null) {
//                    FgPickSend fgPickSend = new FgPickSend();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(KEY_CITY_BEAN, cityBean);
//                    bundle.putSerializable("cityBean", cityBean);
//                    bundle.putString("source", cityBean.name);
//                    fgPickSend.setArguments(bundle);
//                    fragment.startFragment(fgPickSend, bundle);
//                } else {
//                    fragment.startFragment(new FgPickSend());
//                }
                break;
            case R.id.sku_city_footer_single_tv:
//                if (cityBean != null) {
//                    FgSingleNew fgSingleNew = new FgSingleNew();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(KEY_CITY_BEAN, cityBean);
//                    bundle.putSerializable("cityBean", cityBean);
//                    bundle.putString("source", cityBean.name);
//                    fgSingleNew.setArguments(bundle);
//                    fragment.startFragment(fgSingleNew);
//                } else {
//                    fragment.startFragment(new FgSingleNew());
//                }
                break;
        }
    }
}
