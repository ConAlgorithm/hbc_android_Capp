package com.hugboga.custom.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UnicornUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by on 16/11/9.
 */
public class UnicornServiceActivity extends BaseActivity{

    @Bind(R.id.unicorn_service_container_layout1)
    LinearLayout containerLayout;
    @Bind(R.id.unicorn_service_container_layout2)
    LinearLayout containerLayout2;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_unicorn_service);
        ButterKnife.bind(this);
        UnicornUtils.openServiceActivity(containerLayout, R.id.unicorn_service_container_layout2);
    }
}
