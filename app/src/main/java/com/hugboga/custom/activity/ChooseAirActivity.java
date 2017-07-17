package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.widget.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import butterknife.Bind;

/**
 * Created on 16/8/3.
 */
public class ChooseAirActivity extends BaseActivity {

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;

    private FlightBean flightBean;
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

    }

    public void initHeader() {
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                finish();
            }
        });
        headerTitle.setText("选择航班");
        headerRightBtn.setVisibility(View.VISIBLE);
        headerRightBtn.setImageResource(R.mipmap.topbar_cs);
        headerRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.getInstance(ChooseAirActivity.this).showServiceDialog(ChooseAirActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
            }
        });
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
                finish();
                break;
        }
    }
    public FlightBean getFlightBean(){
        if(flightBean!= null){
            return flightBean;
        }
        return null;
    }
}
