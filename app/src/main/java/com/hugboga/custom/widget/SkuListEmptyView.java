package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.OrderSelectCityActivity;
import com.hugboga.custom.fragment.FgOrderSelectCity;
import com.hugboga.custom.fragment.FgSkuList;
import com.hugboga.custom.utils.UIUtils;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by qingcha on 16/6/28.
 */
public class SkuListEmptyView extends LinearLayout implements View.OnClickListener{

    private ImageView customIV;
    private TextView customTV, emptyTV;

    public SkuListEmptyView(Context context) {
        this(context, null);
    }

    public SkuListEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_sku_list_empty, this);
        customIV = (ImageView) findViewById(R.id.sku_list_empty_custom_iv);
        customTV = (TextView) findViewById(R.id.sku_list_empty_custom_tv);
        emptyTV = (TextView) findViewById(R.id.sku_list_empty_tv);

        customTV.setOnClickListener(this);

        float customIVWidth = (UIUtils.getScreenWidth() / 5.0f) * 4;
        float customIVHeight = (408 / 640.0f) * customIVWidth;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)customIVWidth, (int)customIVHeight);
        params.topMargin = UIUtils.dip2px(70);
        params.bottomMargin = UIUtils.dip2px(20);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        customIV.setLayoutParams(params);
    }

    public void showEmptyView(boolean isCity) {
        this.setVisibility(View.VISIBLE);
        emptyTV.setVisibility(View.GONE);
        customIV.setVisibility(View.VISIBLE);
        if (isCity) {
            customTV.setVisibility(View.GONE);
            customIV.setBackgroundResource(R.drawable.city_loding_empty);
        } else {
            customTV.setVisibility(View.VISIBLE);
            customIV.setBackgroundResource(R.drawable.city_loding_no);
        }
    }

    public void requestFailure() {
        customIV.setVisibility(View.GONE);
        customTV.setVisibility(View.GONE);

        this.setVisibility(View.VISIBLE);
        emptyTV.setVisibility(View.VISIBLE);
        emptyTV.setText(R.string.data_load_error_retry);
        emptyTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sku_list_empty_custom_tv:
                Intent intent =  new Intent(context,OrderSelectCityActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.sku_list_empty_tv:
                emptyTV.setOnClickListener(null);
                emptyTV.setText("");
//                fragment.sendRequest(0, true);//TODO qingcha
                break;
        }
    }
}
