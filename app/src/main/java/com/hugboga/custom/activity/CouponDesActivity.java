package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.BindView;

/**
 * Created by zhangqiang on 17/6/24.
 */

public class CouponDesActivity extends BaseActivity {
    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.header_title)
    TextView headerTitle;

    @Override
    public int getContentViewId() {
        return R.layout.coupon_des;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle();
    }
    private void initTitle(){
        headerTitle.setText(R.string.coupon_des_title);
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
