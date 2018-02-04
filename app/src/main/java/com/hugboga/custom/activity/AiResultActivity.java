package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.AiResultAdapter;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.data.event.EventAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class AiResultActivity extends BaseActivity {

    public static final String KEY_AI_RESULT = "key_ai_result";
    public static final String KEY_AI_RESULT_TO_SERVICE = "key_ai_result_two";

    @BindView(R.id.header_title_center)
    TextView header_title_center;
    @BindView(R.id.listview)
    RecyclerView recyclerView;

    AiResultAdapter adapter;
    public DestinationHomeVo destinationHomeVo; //推荐结果显示
    public UnicornServiceActivity.Params params;

    @Override
    public int getContentViewId() {
        return R.layout.activity_ai_result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        header_title_center.setText(getTitle());
        if (savedInstanceState != null) {
            destinationHomeVo = savedInstanceState.getParcelable(KEY_AI_RESULT);

        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                destinationHomeVo = bundle.getParcelable(KEY_AI_RESULT);
                params = (UnicornServiceActivity.Params) bundle.getSerializable(KEY_AI_RESULT_TO_SERVICE);
            }
        }
        EventBus.getDefault().register(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AiResultAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.showAiResult(destinationHomeVo, params);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (destinationHomeVo != null) {
            outState.putParcelable(KEY_AI_RESULT, destinationHomeVo);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.header_left_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
            case CLICK_USER_LOOUT:
            case LINE_UPDATE_COLLECT:
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public String getEventSource() {
        return "AI推荐结果";
    }
}
