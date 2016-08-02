package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SkuCityBean;
import com.hugboga.custom.fragment.FgSkuList;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/27.
 */
public class SkuCityHeaderView extends LinearLayout implements HbcViewBehavior, View.OnClickListener{

    private FgSkuList fragment;

    private ImageView bgIV;
    private TextView guideCountTV, routeCountTV, citynameTV, citynameEnTV;

    private int displayLayoutHeight;
    private SkuCityBean skuCityBean;

    public SkuCityHeaderView(Context context) {
        this(context, null);
    }

    public SkuCityHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        inflate(context, R.layout.view_sku_list_city_header, this);
        bgIV = (ImageView) findViewById(R.id.skulist_header_display_iv);
        guideCountTV = (TextView) findViewById(R.id.skulist_header_guide_count_tv);
        routeCountTV = (TextView) findViewById(R.id.skulist_header_route_count_tv);
        citynameTV = (TextView) findViewById(R.id.skulist_header_cityname_tv);
        citynameEnTV = (TextView) findViewById(R.id.skulist_header_cityname_en_tv);

        findViewById(R.id.skulist_header_cityname_layout).setOnClickListener(this);

        displayLayoutHeight = (int)((360 / 750.0) * UIUtils.getScreenWidth());
        findViewById(R.id.skulist_header_display_layout).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, displayLayoutHeight));
    }

    public void setFragment(FgSkuList _fragment) {
        this.fragment = _fragment;
    }

    @Override
    public void update(Object _data) {
        if (_data == null || !(_data instanceof SkuCityBean) || skuCityBean != null) {
           return;
        }
        skuCityBean = (SkuCityBean) _data;
        Tools.showImage(bgIV, skuCityBean.cityPicture);
        citynameTV.setText(skuCityBean.cityName);
        citynameEnTV.setText(skuCityBean.cityNameEn);

        boolean isResetParams = false;//控制线的隐藏和view的位置( XXXX | XXXX )
        RelativeLayout.LayoutParams params = null;
        if (skuCityBean.cityGuideAmount <= 0 || skuCityBean.goodsCount <= 0) {
            isResetParams = true;
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
        }

        if (skuCityBean.cityGuideAmount <= 0) {//导游数
            guideCountTV.setVisibility(View.GONE);
            findViewById(R.id.skulist_header_count_line_tv).setVisibility(View.GONE);
        } else {
            guideCountTV.setVisibility(View.VISIBLE);
            guideCountTV.setText(getContext().getString(R.string.sku_list_local_guides, "" + skuCityBean.cityGuideAmount));

            if (isResetParams) {
                guideCountTV.setLayoutParams(params);
            }
        }

        if (skuCityBean.goodsCount <= 0) {//路线数
            routeCountTV.setVisibility(View.GONE);
            findViewById(R.id.skulist_header_count_line_tv).setVisibility(View.GONE);
        } else {
            routeCountTV.setVisibility(View.VISIBLE);
            routeCountTV.setText(getContext().getString(R.string.sku_list_routes, "" + skuCityBean.goodsCount));

            if (isResetParams) {
                routeCountTV.setLayoutParams(params);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (fragment == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.skulist_header_cityname_layout://搜索
//                Bundle bundle = new Bundle();
//                bundle.putInt("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
//                fragment.bringToFront(FgChooseCityNew.class, bundle);

                Intent intent = new Intent(this.getContext(), ChooseCityNewActivity.class);
                intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
                intent.putExtra("source","小搜索按钮");
                this.getContext().startActivity(intent);
                break;
        }
    }

    public int getDisplayLayoutHeight() {
        return displayLayoutHeight;
    }
}
