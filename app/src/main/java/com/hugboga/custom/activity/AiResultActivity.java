package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.AiResultAdapter;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;

import butterknife.BindView;
import butterknife.OnClick;

public class AiResultActivity extends BaseActivity {

    public static final String KEY_AI_RESULT = "key_ai_result";

    @BindView(R.id.header_title_center)
    TextView header_title_center;
    @BindView(R.id.listview)
    RecyclerView recyclerView;

    AiResultAdapter adapter;
    public DestinationHomeVo destinationHomeVo; //推荐结果显示

    @Override
    public int getContentViewId() {
        return R.layout.activity_ai_result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        header_title_center.setText(getTitle());
        if (savedInstanceState != null) {
            destinationHomeVo = (DestinationHomeVo) savedInstanceState.getSerializable(KEY_AI_RESULT);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                destinationHomeVo = (DestinationHomeVo) bundle.getSerializable(KEY_AI_RESULT);
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AiResultAdapter(this);
        recyclerView.setAdapter(adapter);
        loadResult();
    }

    private void loadResult() {
        if (adapter != null) {
            adapter.addGoods(destinationHomeVo.destinationGoodsList);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (destinationHomeVo != null) {
            outState.putSerializable(KEY_AI_RESULT, destinationHomeVo);
        }
    }

    @OnClick({R.id.header_left_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
        }
    }
}
