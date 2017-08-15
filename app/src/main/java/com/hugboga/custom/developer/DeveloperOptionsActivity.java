package com.hugboga.custom.developer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/8/15.
 */
public class DeveloperOptionsActivity extends BaseActivity{


    @Bind(R.id.developer_options_environment_rg)
    RadioGroup environmentRG;
    @Bind(R.id.developer_options_dev_rb)
    RadioButton devRB;
    @Bind(R.id.developer_options_test_rb)
    RadioButton testRB;
    @Bind(R.id.developer_options_release_rb)
    RadioButton releaseRB;
    @Bind(R.id.developer_options_hint_tv)
    TextView hintTV;

    @Override
    public int getContentViewId() {
        return R.layout.activity_developer_options;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initDefaultTitleBar();
        fgTitle.setText("Developer Options");

    }
}
