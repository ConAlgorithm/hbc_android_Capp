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
 * 信用卡-新添加卡支付
 * Created by HONGBO on 2017/10/23 20:56.
 */

public class DomesticNewPayView extends FrameLayout {

    public DomesticNewPayView(@NonNull Context context) {
        this(context, null);
    }

    public DomesticNewPayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.domestic_new_pay_layout, this);
        ButterKnife.bind(this, view);
    }

    /**
     * 显示下一步支付弹框
     */
    public void show() {
        setVisibility(VISIBLE);
    }

    @OnClick({R.id.domestic_new_pay_root, R.id.domestic_new_con_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.domestic_new_pay_root:
                setVisibility(GONE); //关闭弹框
                break;
            case R.id.domestic_new_con_layout:
                //屏蔽点击项
                break;
        }
    }
}
