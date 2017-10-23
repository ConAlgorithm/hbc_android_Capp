package com.hugboga.custom.widget.domesticcc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hugboga.custom.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 信用卡-历史卡支付
 * Created by HONGBO on 2017/10/23 19:54.
 */

public class DomesticOldPayView extends FrameLayout {

    public DomesticOldPayView(@NonNull Context context) {
        this(context, null);
    }

    public DomesticOldPayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.domestic_old_pay_layout, this);
        ButterKnife.bind(this, view);
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    @OnClick({R.id.domestic_old_pay_root, R.id.domestic_old_pay_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.domestic_old_pay_root:
                //屏蔽点击项
                break;
            case R.id.domestic_old_pay_close:
                setVisibility(GONE);
                break;
        }
    }
}
