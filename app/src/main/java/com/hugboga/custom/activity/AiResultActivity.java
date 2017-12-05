package com.hugboga.custom.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.AiResultAdapter;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AiResultActivity extends BaseActivity {

    public static final String KEY_GOODS = "key_goods";

    @BindView(R.id.header_title_center)
    TextView header_title_center;
    @BindView(R.id.listview)
    RecyclerView recyclerView;

    AiResultAdapter adapter;
    public List<DestinationGoodsVo> goodsList;

    @Override
    public int getContentViewId() {
        return R.layout.activity_ai_result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        header_title_center.setText(getTitle());
        if (savedInstanceState != null) {
            goodsList = savedInstanceState.getParcelableArrayList(KEY_GOODS);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                goodsList = bundle.getParcelableArrayList(KEY_GOODS);
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AiResultAdapter(this);
        recyclerView.setAdapter(adapter);
        loadResult();
    }

    private void loadResult(){
        if(adapter!=null){
            adapter.addGoods(goodsList);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (goodsList != null) {
            outState.putParcelableArrayList(KEY_GOODS, (ArrayList<? extends Parcelable>) goodsList);
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
