package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.fragment.FgPickNew;
import com.hugboga.custom.fragment.FgSendNew;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.AlertDialogUtils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/8/3.
 */

public class PickSendActivity extends BaseActivity {

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.daily_tap_1)
    TextView dailyTap1;
    @Bind(R.id.daily_tap_line1)
    View dailyTapLine1;
    @Bind(R.id.daily_layout_1)
    RelativeLayout dailyLayout1;
    @Bind(R.id.daily_tap_2)
    TextView dailyTap2;
    @Bind(R.id.daily_tap_line2)
    View dailyTapLine2;
    @Bind(R.id.daily_layout_2)
    RelativeLayout dailyLayout2;
    @Bind(R.id.header_center)
    LinearLayout headerCenter;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.daily_content)
    FrameLayout dailyContent;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;

    private void backPress() {
        if ((fgPick.isVisible() && !TextUtils.isEmpty(fgPick.airTitle.getText())) || (fgSend.isVisible() && !TextUtils.isEmpty(fgSend.addressTips.getText()))) {
            AlertDialogUtils.showAlertDialog(activity, getString(R.string.back_alert_msg), "离开", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_picksend;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle bundle = getIntent().getExtras();

        initView();
        initHeader();
        EventUtil eventUtil = EventUtil.getInstance();
        eventUtil.source = getIntentSource();

        if (bundle != null && bundle.getInt(Constants.PARAMS_TYPE) == 1) {
            findViewById(R.id.daily_layout_2).performClick();
        }
    }

    public void initHeader() {
        headerTitle.setText(R.string.title_transfer);
        headerRightTxt.setText(R.string.noraml_question);
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPress();
            }
        });
        headerRightTxt.setVisibility(View.VISIBLE);
        headerRightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_PROBLEM);
                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                startActivity(intent);
            }
        });
    }


    CollectGuideBean collectGuideBean;

    public void initView() {

        collectGuideBean = (CollectGuideBean) this.getIntent().getSerializableExtra("collectGuideBean");

        fgPick = new FgPickNew();
        fgSend = new FgSendNew();


        Bundle bundle = new Bundle();
        bundle.putSerializable("collectGuideBean", collectGuideBean);
        bundle.putString("source",getIntentSource());
        fgPick.setArguments(bundle);
        fgSend.setArguments(bundle);


        fm = getSupportFragmentManager();//getFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.daily_content, fgPick);
//        transaction.addToBackStack(null);
        transaction.commit();
        pickOrSend = 1;
    }

    private FgPickNew fgPick;
    private FgSendNew fgSend;
    private FragmentManager fm;

    private void selectTap(int index) {
        if (index == 1) {
            dailyTapLine1.setVisibility(View.GONE);
            dailyTapLine2.setVisibility(View.VISIBLE);
            dailyTap1.setTextColor(getResources().getColor(R.color.basic_black));
            dailyTap2.setTextColor(getResources().getColor(R.color.basic_daily_toolbar_color));
        } else {
            dailyTapLine1.setVisibility(View.VISIBLE);
            dailyTapLine2.setVisibility(View.GONE);
            dailyTap1.setTextColor(getResources().getColor(R.color.basic_daily_toolbar_color));
            dailyTap2.setTextColor(getResources().getColor(R.color.basic_black));
        }
    }

    private int pickOrSend = 1; //1接机 2送机
    FragmentTransaction transaction;

    @OnClick({R.id.header_left_btn, R.id.daily_layout_1, R.id.daily_layout_2, R.id.header_right_txt})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.daily_layout_1:
                selectTap(0);
                pickOrSend = 1;
                if (!fgPick.isAdded()) {
                    transaction = fm.beginTransaction();
                    transaction.add(R.id.daily_content, fgPick);
//                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (!fgPick.isVisible()) {
                    transaction = fm.beginTransaction();
                    transaction.hide(fgSend);
                    transaction.show(fgPick);
                    transaction.commit();
                }
                break;
            case R.id.daily_layout_2:
                selectTap(1);
                pickOrSend = 2;
                if (!fgSend.isAdded()) {
                    transaction = fm.beginTransaction();
                    transaction.add(R.id.daily_content, fgSend);
                    transaction.hide(fgPick);
//                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (!fgSend.isVisible()) {
                    transaction = fm.beginTransaction();
                    transaction.hide(fgPick);
                    transaction.show(fgSend);
                    transaction.commit();
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case PICK_SEND_ONBACKPRESS:
                backPress();
                break;
            default:
                break;
        }
    }

    public void onBackPressed() {
        backPress();
    }

}