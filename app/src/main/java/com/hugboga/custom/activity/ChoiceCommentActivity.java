package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import java.io.Serializable;

/**
 * Created by qingcha on 17/8/3.
 */
public class ChoiceCommentActivity extends BaseActivity{

    private Params paramsData;

    public static class Params implements Serializable {
        public int id;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_choice_comment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    public void initView() {
        initTitleBar();
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText("游客说");
        fgRightTV.setVisibility(View.GONE);
    }

    @Override
    public String getEventSource() {
        return "游客说";
    }
}
