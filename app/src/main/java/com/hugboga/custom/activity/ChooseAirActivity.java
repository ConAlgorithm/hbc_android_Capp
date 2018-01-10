package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.widget.CsDialog;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created on 16/8/3.
 */
public class ChooseAirActivity extends BaseActivity {

    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.header_right_btn)
    ImageView headerRightBtn;
    @BindView(R.id.header_right_txt)
    TextView headerRightTxt;

    private FlightBean flightBean;
    CsDialog csDialog;
    private GuidanceOrderActivity.Params guidanceParams;
    private String sourceTag;

    @Override
    public int getContentViewId() {
        return R.layout.activity_choose_air;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
        initHeader();
        getLastFlightBean();
        if (getIntent() != null) {
            guidanceParams = (GuidanceOrderActivity.Params) getIntent().getSerializableExtra(GuidanceOrderActivity.PARAMS_GUIDANCE);
            sourceTag = getIntent().getStringExtra(Constants.PARAMS_TYPE);
        }
        setSensorsBuyFlightEvent();
    }

    public void initHeader() {
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                finish();
            }
        });
        headerTitle.setText(R.string.choose_air_title);
        headerRightBtn.setVisibility(View.VISIBLE);
        headerRightBtn.setImageResource(R.mipmap.topbar_cs);
        headerRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SensorsUtils.onAppClick(getEventSource(), "客服", getIntentSource());
                //DialogUtil.getInstance(ChooseAirActivity.this).showServiceDialog(ChooseAirActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
                csDialog = CommonUtils.csDialog(ChooseAirActivity.this, null, null, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, getEventSource(), new CsDialog.OnCsListener() {
                    @Override
                    public void onCs() {
                        if (csDialog != null && csDialog.isShowing()) {
                            csDialog.dismiss();
                        }
                    }
                });

            }
        });
    }

    public String getSourceTag() {
        return sourceTag;
    }

    private FlightBean getLastFlightBean(){
        Bundle bundle = getIntent().getBundleExtra("flightBean");
        if(bundle!= null){
            flightBean = (FlightBean) bundle.getSerializable("flightBean");
            return flightBean;
        }
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(final EventAction action) {

        switch (action.getType()) {
            case AIR_NO:
                FlightBean flightBean = (FlightBean)action.getData();
                if (guidanceParams != null) {
                    if (!TextUtils.equals(flightBean.sourceTag, GuidanceOrderActivity.TAG)) {
                        break;
                    }
                    Intent intent = new Intent(this, PickSendActivity.class);
                    PickSendActivity.Params params = new PickSendActivity.Params();
                    if (guidanceParams.seckillsBean != null) {
                        params.timeLimitedSaleNo = guidanceParams.seckillsBean.timeLimitedSaleNo;
                        params.timeLimitedSaleScheduleNo = guidanceParams.seckillsBean.timeLimitedSaleScheduleNo;
                        params.isSeckills = guidanceParams.seckillsBean.isSeckills;
                    }
                    params.flightBean = flightBean;
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra(Constants.PARAMS_SOURCE, guidanceParams.source);
                    startActivity(intent);
                } else {
                    finish();
                }
                break;
        }
    }
    public FlightBean getFlightBean(){
        if(flightBean!= null){
            return flightBean;
        }
        return null;
    }

    @Override
    public String getEventSource() {
        return "选择航班";
    }

    //来到选航班页
    private void setSensorsBuyFlightEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("refer", getIntentSource());
            SensorsDataAPI.sharedInstance(this).track("buy_flight");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
