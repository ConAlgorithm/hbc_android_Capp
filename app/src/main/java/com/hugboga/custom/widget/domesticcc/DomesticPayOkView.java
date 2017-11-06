package com.hugboga.custom.widget.domesticcc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.DomesticCreditCardActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 信用卡-历史卡支付确认
 * Created by HONGBO on 2017/10/23 19:54.
 */

public class DomesticPayOkView extends FrameLayout {

    @Bind(R.id.domestic_pay_ok_img)
    ImageView domestic_pay_ok_img; //银行图标
    @Bind(R.id.domestic_pay_ok_name)
    TextView domestic_pay_ok_name; //银行名称
    @Bind(R.id.domestic_pay_ok_card)
    TextView domestic_pay_ok_card; //卡号
    @Bind(R.id.domestic_pay_ok_btn)
    Button domestic_pay_ok_btn;

    String bindId;

    public DomesticPayOkView(@NonNull Context context) {
        this(context, null);
    }

    public DomesticPayOkView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.domestic_pay_ok_layout, this);
        ButterKnife.bind(this, view);
    }

    public void show(String bindId, int iconResId, String bankName, String cardNum, String price) {
        this.bindId = bindId;
        domestic_pay_ok_img.setImageResource(iconResId);
        domestic_pay_ok_name.setText(bankName);
        domestic_pay_ok_card.setText(cardNum);
        domestic_pay_ok_btn.setText(String.format(getContext().getString(R.string.domestic_card_pay_btn_txt), price));
        setVisibility(VISIBLE);
    }

    public void close() {
        setVisibility(GONE);
    }

    @OnClick({R.id.domestic_old_pay_root, R.id.domestic_old_pay_close, R.id.domestic_pay_ok_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.domestic_old_pay_root:
                //屏蔽点击项
                break;
            case R.id.domestic_old_pay_close:
                setVisibility(GONE);
                break;
            case R.id.domestic_pay_ok_btn:
                //确认支付
                if (getContext() != null && (getContext() instanceof DomesticCreditCardActivity)) {
                    ((DomesticCreditCardActivity) getContext()).payOk(bindId);
                }
                break;
        }
    }
}
