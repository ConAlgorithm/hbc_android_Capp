package com.hugboga.custom.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FgInsureInfoAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.InsureListBean;
import com.hugboga.custom.data.bean.OrderBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/8/4.
 */
public class InsureInfoActivity extends BaseActivity {

    @Bind(R.id.insure_info_listview)
    ListView listView;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;

    private FgInsureInfoAdapter adapter;
    private OrderBean orderBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            orderBean = (OrderBean) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                orderBean = (OrderBean) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        setContentView(R.layout.fg_insure_info);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (orderBean != null) {
            outState.putSerializable(Constants.PARAMS_DATA, orderBean);
        }
    }

    private void initView() {
        initDefaultTitleBar();
        headerTitle.setText(getString(R.string.insure_info_title));
        List<InsureListBean> list = orderBean.insuranceList;
        if (list != null && list.size() > 0) {
            if (adapter == null) {
                adapter = new FgInsureInfoAdapter(this);
                listView.setAdapter(adapter);
            }
            adapter.setList(list);
        }
    }
}
