package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.fragment.FgHome;

/**
 * Created by qingcha on 16/6/21.
 */
public class HomeSearchView extends RelativeLayout {

    public HomeSearchView(Context context) {
        this(context, null);
    }

    public HomeSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_home_search, this);
        setBackgroundColor(0xFF000000);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goChooseCity("首页");
            }
        });
    }

    private void goChooseCity(String source) {
        Intent intent = new Intent(this.getContext(), ChooseCityNewActivity.class);
        intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
        intent.putExtra("isHomeIn", true);
        intent.putExtra("source", "首页搜索框");
        this.getContext().startActivity(intent);
    }
}
