package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeData;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeRouteItemView extends RelativeLayout implements HbcViewBehavior{

    private FgHome fgHome;

    private ImageView displayIV;
    private TextView titleTV, subtitleTV, guideAmountTV;

    private RelativeLayout item1Layout, item2Layout, item3Layout;
    private TextView price1TV, price2TV, price3TV;
    private TextView cotent1TV, cotent2TV, cotent3TV;
    private View line1View, line2View, line3View;

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

        int displayImgHeight = (int)((287 / 620.0) * (UIUtils.getScreenWidth() - context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left) * 2 - UIUtils.dip2px(20)));
        displayIV.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, displayImgHeight));
    }

    public void setFgHomeContext(FgHome context) {
        this.fgHome = context;
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        HomeData.CityContentItem data = (HomeData.CityContentItem) _data;
        Tools.showImageCenterCrop(displayIV, data.getPicture());
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
            subtitleTV.setText(data.getMainTitle());
        }
        if (TextUtils.isEmpty(data.getTip())) {
            guideAmountTV.setVisibility(View.GONE);
        } else {
            guideAmountTV.setVisibility(View.VISIBLE);
            guideAmountTV.setText(data.getTip());
        }

        if (data.getTraveLineList() != null) {
            if (data.getTraveLineList().get(0) == null) {
                item1Layout.setVisibility(View.INVISIBLE);
                line1View.setVisibility(View.GONE);
            } else {
                item1Layout.setVisibility(View.VISIBLE);
                line1View.setVisibility(View.VISIBLE);
                HomeData.TraveLineItem item = data.getTraveLineList().get(0);
                cotent1TV.setText(item.getDescribe());
                price1TV.setText("" + item.getPrice());
            }
            if (data.getTraveLineList().get(1) == null) {
                item2Layout.setVisibility(View.INVISIBLE);
                line2View.setVisibility(View.GONE);
            } else {
                item2Layout.setVisibility(View.VISIBLE);
                line2View.setVisibility(View.VISIBLE);
                HomeData.TraveLineItem item = data.getTraveLineList().get(1);
                cotent2TV.setText(item.getDescribe());
                price2TV.setText("" + item.getPrice());
            }
            if (data.getTraveLineList().get(2) == null) {
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
}
