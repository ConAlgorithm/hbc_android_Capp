package com.hugboga.custom.developer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.HomeBannerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/8/15.
 */
public class DeveloperOptionsActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    public static String CURRENT_ENVIRONMENT = "current_environment";
    public static String DEFULT_ENVIRONMENT = "defult_environment";

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
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_developer_options);
        ButterKnife.bind(this);
        initDefaultTitleBar();
        fgTitle.setText("Developer Options");
        environmentRG.setOnCheckedChangeListener(this);

        switch (SharedPre.getInteger(DeveloperOptionsActivity.CURRENT_ENVIRONMENT, -1)) {
            case 1:
                devRB.setChecked(true);
                break;
            case 2:
                testRB.setChecked(true);
                break;
            case 3:
                releaseRB.setChecked(true);
                break;
        }
        String defultEnvironment = null;
        switch (SharedPre.getInteger(DeveloperOptionsActivity.DEFULT_ENVIRONMENT, -1)) {
            case 1:
                defultEnvironment = "开发(dev)";
                break;
            case 2:
                defultEnvironment = "测试(test)";
                break;
            case 3:
                defultEnvironment = "线上(release)";
                break;
        }
        hintTV.setText(hintTV.getText() + defultEnvironment);

    }

    @OnClick({R.id.developer_options_action_layout, R.id.developer_options_close_tv})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.developer_options_action_layout:
                intent = new Intent(this, DeveloperActionTestActivity.class);
                startActivity(intent);
                break;
            case R.id.developer_options_close_tv:
                android.os.Process.killProcess(android.os.Process.myPid());
                CommonUtils.showToast("请从后台杀死应用后,重新进入!");
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int environmentType = 0;
        switch (checkedId) {
            case R.id.developer_options_dev_rb:
                environmentType = 1;
                CommonUtils.showToast("当前环境: 开发(dev) ");
                break;
            case R.id.developer_options_test_rb:
                environmentType = 2;
                CommonUtils.showToast("当前环境: 测试(test) ");
                break;
            case R.id.developer_options_release_rb:
                environmentType = 3;
                CommonUtils.showToast("当前环境: 线上(release) ");
                break;
        }
        SharedPre.setInteger(DeveloperOptionsActivity.CURRENT_ENVIRONMENT, environmentType);
    }
}
