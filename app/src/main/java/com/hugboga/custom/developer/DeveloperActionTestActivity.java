package com.hugboga.custom.developer;

import android.os.Bundle;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/8/15.
 */
public class DeveloperActionTestActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_developer_action_test);
        ButterKnife.bind(this);
        initDefaultTitleBar();
        fgTitle.setText("Action test");
    }
}
