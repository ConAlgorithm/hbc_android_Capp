package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.BindView;

public class LuggageInfoActivity extends BaseActivity {
    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.header_title)
    TextView headerTitle;

    @Override
    public int getContentViewId() {
        return R.layout.activity_luggageinfo;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerTitle.setText(R.string.luggage_info_title);
    }

}
