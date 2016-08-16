package com.hugboga.custom.developer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/8/15.
 */
public class DeveloperOptionsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_developer_options);
        ButterKnife.bind(this);
        initDefaultTitleBar();
        fgTitle.setText("Developer Options");
    }

    @OnClick({R.id.developer_options_action_layout})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.developer_options_action_layout:
                intent = new Intent(this, DeveloperActionTestActivity.class);
                startActivity(intent);
                break;
        }
    }

}
