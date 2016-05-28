package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created  on 16/5/13.
 */
@ContentView(R.layout.fg_picksend)
public class FgPickSend extends BaseFragment implements View.OnTouchListener{
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

    @Override
    protected void initHeader() {
        fgTitle.setText(R.string.title_transfer);
        fgRightBtn.setText(R.string.noraml_question);
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((fgPick.isVisible() && !TextUtils.isEmpty(fgPick.airTitle.getText())) || (fgSend.isVisible() && !TextUtils.isEmpty(fgSend.addressTips.getText())) ){
                    AlertDialogUtils.showAlertDialog(getContext(), getString(R.string.back_alert_msg), "离开", "取消", new DialogInterface.OnClickListener() {
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
                }else{
                    finish();
                }
            }
        });
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_PROBLEM);
                startFragment(new FgWebInfo(), bundle);
                if(pickOrSend == 1){
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("source", "填写行程页面");
                    MobclickAgent.onEvent(getActivity(), "callcenter_pickup", map);
                    v.setTag("填写行程页面,calldomestic_pickup,calldomestic_pickup");
                }else if(pickOrSend == 2){
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("source", "填写行程页面");
                    MobclickAgent.onEvent(getActivity(), "callcenter_dropoff", map);
                    v.setTag("填写行程页面,calldomestic_dropoff,calloverseas_dropoff");
                }
            }
        });
    }

    @Override
    protected void initView() {


        fgPick = new FgPickNew();
        fgSend = new FgSendNew();

        Bundle bundle = new Bundle();
        if(getArguments()!=null){
            bundle.putAll(getArguments());
        }
        fgPick.setArguments(bundle);
        fgSend.setArguments(bundle);


        fm = getFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.daily_content, fgPick);
//        transaction.addToBackStack(null);
        transaction.commit();
        pickOrSend = 1;
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    private FgPickNew fgPick;
    private FgSendNew fgSend;
    private FragmentManager fm;

    @Override
    protected void inflateContent() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void selectTap(int index) {
        if (index == 1) {
            dailyTapLine1.setVisibility(View.GONE);
            dailyTapLine2.setVisibility(View.VISIBLE);
            dailyTap1.setTextColor(Color.parseColor("#878787"));
            dailyTap2.setTextColor(getResources().getColor(R.color.basic_white));
        } else {
            dailyTapLine1.setVisibility(View.VISIBLE);
            dailyTapLine2.setVisibility(View.GONE);
            dailyTap1.setTextColor(getResources().getColor(R.color.basic_white));
            dailyTap2.setTextColor(Color.parseColor("#878787"));
        }
    }

    private int pickOrSend = 1; //1接机 2送机
    FragmentTransaction transaction;

    @OnClick({R.id.header_left_btn,R.id.daily_layout_1, R.id.daily_layout_2, R.id.header_right_txt})
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
