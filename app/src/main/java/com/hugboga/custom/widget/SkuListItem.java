package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/24.
 */
public class SkuListItem extends RelativeLayout implements HbcViewBehavior{

    private ImageView displayIV, reserveIV;
    private TextView guidesCountTV, priceTV, priceHintTV, reserveTV, describeTV, headLableTV;
    private LinearLayout guidesCountLayout;
    private View priceLine;
    private TagGroup tagGroup;

    public SkuListItem(Context context) {
        this(context, null);
    }

    public SkuListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(UIUtils.dip2px(15), 0, UIUtils.dip2px(15), UIUtils.dip2px(15));

        inflate(context, R.layout.view_sku_list_item, this);
        displayIV = (ImageView) findViewById(R.id.sku_item_display_iv);
        guidesCountTV = (TextView) findViewById(R.id.sku_item_city_guides_tv);
        guidesCountLayout = (LinearLayout) findViewById(R.id.sku_item_city_guides_layout);
        priceTV = (TextView) findViewById(R.id.sku_item_price_tv);
        priceHintTV = (TextView) findViewById(R.id.sku_item_price_hint_tv);
        priceLine = findViewById(R.id.sku_item_vertical_price_line);
        reserveIV = (ImageView) findViewById(R.id.sku_item_reserve_iv);
        reserveTV = (TextView) findViewById(R.id.sku_item_reserve_tv);
        headLableTV = (TextView) findViewById(R.id.sku_item_headLable_tv);
        describeTV = (TextView) findViewById(R.id.sku_item_describe_tv);
        tagGroup = (TagGroup) findViewById(R.id.sku_item_taggroup);

        int displayImgHeight = (int)((339 / 690.0) * (UIUtils.getScreenWidth() - UIUtils.dip2px(30)));
        displayIV.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, displayImgHeight));
    }

    @Override
    public void update(Object _data) {
        if (_data == null || !(_data instanceof SkuItemBean)) {
            return;
        }
        SkuItemBean skuItemBean = (SkuItemBean)_data;

        if (TextUtils.isEmpty(skuItemBean.goodsPicture)) {
            displayIV.setImageResource(0);
        } else {
            Tools.showImage(displayIV, skuItemBean.goodsPicture);
        }

        //描述
        describeTV.setText(skuItemBean.goodsName);

        if (skuItemBean.guideAmount <= 0) {
            guidesCountLayout.setVisibility(View.GONE);
        } else {
            guidesCountTV.setText("" + skuItemBean.guideAmount);
            guidesCountLayout.setVisibility(View.VISIBLE);
        }

        //是否可定
        if (TextUtils.isEmpty(skuItemBean.bookLable)) {
            priceLine.setVisibility(View.GONE);
            reserveIV.setVisibility(View.GONE);
            reserveTV.setVisibility(View.GONE);
        } else {
            priceLine.setVisibility(View.VISIBLE);
            reserveIV.setVisibility(View.VISIBLE);
            reserveTV.setVisibility(View.VISIBLE);
            reserveTV.setText(skuItemBean.bookLable);
        }

        if (skuItemBean.goodsClass == -1) {//-1 按天包车
            priceTV.setText(skuItemBean.goodsDesc);
            priceHintTV.setVisibility(View.GONE);
            headLableTV.setText(skuItemBean.headLable);//背景色 固定蓝色
            headLableTV.setBackgroundResource(R.drawable.shape_sku_list_lable_blue);
        } else {
            priceTV.setText(skuItemBean.perPrice + getContext().getResources().getString(R.string.sign_rmb));
            priceHintTV.setVisibility(View.VISIBLE);
            headLableTV.setText(skuItemBean.headLable);
            if (skuItemBean.goodsClass == 1) {//1固定线路 超省心 绿色
                headLableTV.setBackgroundResource(R.drawable.shape_sku_list_lable_green);
            } else if (skuItemBean.goodsClass == 2) {//2推荐线路 超自由 蓝色
                headLableTV.setBackgroundResource(R.drawable.shape_sku_list_lable_blue);
            }
        }
        if (skuItemBean.characteristicLables != null && skuItemBean.characteristicLables.size() > 0) {
            tagGroup.setVisibility(View.VISIBLE);
            final int labelsSize = skuItemBean.characteristicLables.size();
            ArrayList<View> viewList = new ArrayList<View>(labelsSize);
            for (int i = 0; i < labelsSize; i++) {
                SkuItemBean.CharacteristicLables lablesBean = skuItemBean.characteristicLables.get(i);
                if (lablesBean == null) {
                    continue;
                }
                if (i < tagGroup.getChildCount()) {
                    LinearLayout tagLayout = (LinearLayout)tagGroup.getChildAt(i);
                    tagLayout.setVisibility(View.VISIBLE);
                    ImageView iconIV = (ImageView)tagLayout.getChildAt(0);
                    iconIV.setBackgroundResource(getLableIconRes(skuItemBean.goodsClass, lablesBean.lableType));
                    TextView tagTV = (TextView)tagLayout.getChildAt(1);
                    tagTV.setText(lablesBean.lableName);
                    tagTV.setTextColor(getTagColor(skuItemBean.goodsClass));
                } else {
                    viewList.add(getNewTagView(lablesBean.lableName, lablesBean.lableType, skuItemBean.goodsClass));
                }
            }
            for (int j = labelsSize; j < tagGroup.getChildCount(); j++) {
                tagGroup.getChildAt(j).setVisibility(View.GONE);
            }
            tagGroup.setTags(viewList, tagGroup.getChildCount() <= 0);
        } else {
            tagGroup.setVisibility(View.GONE);
        }
    }

    public LinearLayout getNewTagView(String lableName, int lableType, int goodsClass) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        ImageView iconIV = new ImageView(getContext());
        iconIV.setBackgroundResource(getLableIconRes(goodsClass, lableType));
        layout.addView(iconIV, UIUtils.dip2px(14), UIUtils.dip2px(14));

        TextView tagTV = new TextView(getContext());
        tagTV.setPadding(UIUtils.dip2px(5), 0, 0, 0);
        tagTV.setTextSize(12);
        tagTV.setTextColor(getTagColor(goodsClass));
        tagTV.setText(lableName);
        layout.addView(tagTV);

        return layout;
    }

    public int getTagColor(int goodsClass) {
        int result = 0;
        if (goodsClass == 1) {
            result = 0xFF6EE12F;
        } else {
            result = 0xFF31B7FD;
        }
        return result;
    }

    /**
     * goodsClass==1
     * 固定线路
     * 0:“线路含酒店”
     * 3:“按固定线路游玩”
     *
     * goodsClass==2
     * 推荐线路
     * 3:“可与司导协商协调行程”
     * 4:“每日10小时300公里畅游”
     *
     * 按天包车
     * 3:“行程随心定”
     * 4:“每日10小时300公里畅游”
     * */
    private int getLableIconRes(int goodsClass, int lableType) {
        int result = 0;
        if (goodsClass == 1) {
            switch (lableType) {//1固定线路 超省心 绿色
                case 0:
                    result = R.mipmap.line_hotel;
                    break;
                case 3:
                    result = R.mipmap.line_fixedplay;
                    break;
            }
        } else if (goodsClass == 2) {//2推荐线路 超自由 蓝色
            switch (lableType) {
                case 3:
                    result = R.mipmap.line_consult;
                    break;
                case 4:
                    result = R.mipmap.line_swim;
                    break;
            }
        } else if (goodsClass == -1) {//按天包车 蓝色
            switch (lableType) {
                case 3:
                    result = R.mipmap.line_trip;
                    break;
                case 4:
                    result = R.mipmap.line_swim;
                    break;
            }
        }
        return result;
    }
}
