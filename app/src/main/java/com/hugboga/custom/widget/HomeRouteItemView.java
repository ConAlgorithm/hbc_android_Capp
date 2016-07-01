package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeData;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.fragment.FgSkuDetail;
import com.hugboga.custom.fragment.FgSkuList;
import com.hugboga.custom.fragment.FgWebInfo;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeRouteItemView extends RelativeLayout implements HbcViewBehavior, View.OnClickListener{

    private FgHome fgHome;

    private ImageView displayIV;
    private TextView titleTV, subtitleTV, guideAmountTV;

    private RelativeLayout item1Layout, item2Layout, item3Layout;
    private TextView price1TV, price2TV, price3TV;
    private TextView cotent1TV, cotent2TV, cotent3TV;
    private View line1View, line2View, line3View;

    private RelativeLayout moreLayout;

    private HomeData.CityContentItem data;

    public HomeRouteItemView(Context context) {
        this(context, null);
    }

    public HomeRouteItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0xFFFFFFFF);
        inflate(getContext(), R.layout.view_home_route_item, this);
        displayIV = (ImageView) findViewById(R.id.home_route_item_display_iv);
        titleTV = (TextView) findViewById(R.id.home_route_item_title_tv);
        subtitleTV = (TextView) findViewById(R.id.home_route_item_subtitle_tv);
        guideAmountTV = (TextView) findViewById(R.id.home_route_item_guideamount_tv);

        item1Layout = (RelativeLayout) findViewById(R.id.home_route_item_1_layout);
        cotent1TV = (TextView) findViewById(R.id.home_route_item_cotent_1_tv);
        price1TV = (TextView) findViewById(R.id.home_route_item_price_1_tv);
        line1View = findViewById(R.id.home_route_item_1_line);

        item2Layout = (RelativeLayout) findViewById(R.id.home_route_item_2_layout);
        cotent2TV = (TextView) findViewById(R.id.home_route_item_cotent_2_tv);
        price2TV = (TextView) findViewById(R.id.home_route_item_price_2_tv);
        line2View = findViewById(R.id.home_route_item_2_line);

        item3Layout = (RelativeLayout) findViewById(R.id.home_route_item_3_layout);
        cotent3TV = (TextView) findViewById(R.id.home_route_item_cotent_3_tv);
        price3TV = (TextView) findViewById(R.id.home_route_item_price_3_tv);
        line3View = findViewById(R.id.home_route_item_3_line);

        moreLayout = (RelativeLayout) findViewById(R.id.home_route_item_more_layout);

        int displayImgHeight = (int)((287 / 620.0) * (UIUtils.getScreenWidth() - context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left) * 2 - UIUtils.dip2px(20)));
        displayIV.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, displayImgHeight));

        item1Layout.setOnClickListener(this);
        item2Layout.setOnClickListener(this);
        item3Layout.setOnClickListener(this);
        moreLayout.setOnClickListener(this);
        displayIV.setOnClickListener(this);
    }

    public void setFgHomeContext(FgHome context) {
        this.fgHome = context;
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        data = (HomeData.CityContentItem) _data;
        Tools.showImage(displayIV, data.getPicture());
        if (TextUtils.isEmpty(data.getMainTitle())) {
            titleTV.setVisibility(View.GONE);
        } else {
            titleTV.setVisibility(View.VISIBLE);
            titleTV.setText(data.getMainTitle());
        }

        if (TextUtils.isEmpty(data.getSubTitle())) {
            subtitleTV.setVisibility(View.GONE);
        } else {
            subtitleTV.setVisibility(View.VISIBLE);
            subtitleTV.setText(data.getSubTitle());
        }
        if (TextUtils.isEmpty(data.getTip())) {
            guideAmountTV.setVisibility(View.GONE);
        } else {
            guideAmountTV.setVisibility(View.VISIBLE);
            guideAmountTV.setText(data.getTip());
        }

        if (data.getTraveLineList() != null) {
            if (data.getTraveLineList().size() < 1 || data.getTraveLineList().get(0) == null) {
                item1Layout.setVisibility(View.INVISIBLE);
                line1View.setVisibility(View.GONE);
            } else {
                item1Layout.setVisibility(View.VISIBLE);
                line1View.setVisibility(View.VISIBLE);
                HomeData.TraveLineItem item = data.getTraveLineList().get(0);
                cotent1TV.setText(item.getDescribe());
                price1TV.setText("" + item.getPrice());
            }
            if (data.getTraveLineList().size() < 2 || data.getTraveLineList().get(1) == null) {
                item2Layout.setVisibility(View.INVISIBLE);
                line2View.setVisibility(View.GONE);
            } else {
                item2Layout.setVisibility(View.VISIBLE);
                line2View.setVisibility(View.VISIBLE);
                HomeData.TraveLineItem item = data.getTraveLineList().get(1);
                cotent2TV.setText(item.getDescribe());
                price2TV.setText("" + item.getPrice());
            }
            if (data.getTraveLineList().size() < 3 || data.getTraveLineList().get(2) == null) {
                item3Layout.setVisibility(View.INVISIBLE);
                line3View.setVisibility(View.GONE);
            } else {
                item3Layout.setVisibility(View.VISIBLE);
                line3View.setVisibility(View.VISIBLE);
                HomeData.TraveLineItem item = data.getTraveLineList().get(2);
                cotent3TV.setText(item.getDescribe());
                price3TV.setText("" + item.getPrice());
            }
        } else {
            item1Layout.setVisibility(View.INVISIBLE);
            line1View.setVisibility(View.GONE);
            item2Layout.setVisibility(View.INVISIBLE);
            line2View.setVisibility(View.GONE);
            item3Layout.setVisibility(View.INVISIBLE);
            line3View.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (fgHome == null || data == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.home_route_item_1_layout:
                intentSkuDetail(data.getTraveLineList().get(0).getSkuItemBean());
                break;
            case R.id.home_route_item_2_layout:
                intentSkuDetail(data.getTraveLineList().get(1).getSkuItemBean());
                break;
            case R.id.home_route_item_3_layout:
                intentSkuDetail(data.getTraveLineList().get(2).getSkuItemBean());
                break;
            case R.id.home_route_item_display_iv:
            case R.id.home_route_item_more_layout:
                FgSkuList.Params params = new FgSkuList.Params();
                params.id = data.getCityId();
                params.skuType = FgSkuList.SkuType.CITY;
                fgHome.startFragment(FgSkuList.newInstance(params));
                break;
        }
    }

    private void intentSkuDetail(SkuItemBean skuItemBean) {
        if (skuItemBean == null) {
            return;
        }
        skuItemBean.cityId = "" + data.getCityId();
        Bundle bundle = new Bundle();
        bundle.putString(FgWebInfo.WEB_URL, skuItemBean.skuDetailUrl);
        bundle.putSerializable(FgSkuDetail.WEB_SKU, skuItemBean);
        bundle.putString("source" , "首页");
        fgHome.startFragment(new FgSkuDetail(),bundle);
    }
}
