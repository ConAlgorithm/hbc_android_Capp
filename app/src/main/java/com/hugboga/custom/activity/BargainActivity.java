package com.hugboga.custom.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.widget.CountDownLayout;
import com.netease.nim.uikit.common.util.log.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/9/6.
 * 砍价
 */

public class BargainActivity extends BaseActivity {

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.countdown)
    CountDownLayout countdown;
    @Bind(R.id.cut_money)
    ImageView cutMoney;
    @Bind(R.id.list_layout)
    LinearLayout listLayout;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_bargain);
        ButterKnife.bind(this);
        initView();
    }

    int second = 5;
    CountDownTimer countDownTimer;
    private void initView() {
        initDefaultTitleBar();
        fgTitle.setText("邀请好友来砍价");
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        countDownTimer = new CountDownTimer(second *1000 + 100,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.changeTime((int)millisUntilFinished/1000);
                LogUtil.e("===",""+(int)millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                LogUtil.e("===","done");
                countdown.changeTime(0);
                cutMoney.setImageResource(R.mipmap.cut_end);
            }
        };
        countDownTimer.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        countDownTimer.cancel();
    }

    @Override
    public String getIntentSource() {
        return super.getIntentSource();
    }

    @Override
    protected void initDefaultTitleBar() {
        super.initDefaultTitleBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public String getEventId() {
        return super.getEventId();
    }

    @Override
    public String getEventSource() {
        return super.getEventSource();
    }

    @OnClick(R.id.cut_money)
    public void onClick() {
    }
}
