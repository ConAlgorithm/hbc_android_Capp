package com.hugboga.custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.data.bean.OrderPriceInfoBean;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.OrderPriceInfoItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/8/25.
 */
public class OrderPriceInfoActivity extends Activity {

    @Bind(R.id.activity_order_price_info_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.activity_order_price_info_limit_layout)
    LinearLayout limitLayout;

    private HbcRecyclerSingleTypeAdpater<OrderPriceInfoBean.OrderPriceInfoItemBean> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_price_info);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        limitLayout.getLayoutParams().height = UIUtils.getScreenHeight() / 6 * 4;

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, OrderPriceInfoItemView.class);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.addData(getTestData().priceInfoList);
    }

    @OnClick(R.id.activity_order_price_info_root_layout)
    public void onClose() {
        finish();
    }

    public OrderPriceInfoBean getTestData() {
        OrderPriceInfoBean.OrderPriceInfoItemBean infoItemBean = new OrderPriceInfoBean.OrderPriceInfoItemBean();
        infoItemBean.title = "用车费用";
        List<String> skillLabelNames = new ArrayList<>();
        skillLabelNames.add("司导服务费");
        skillLabelNames.add("小费");
        skillLabelNames.add("司导餐补");
        skillLabelNames.add("长途补助");
        skillLabelNames.add("跨城市异地住宿补助*3晚哈哈");
        skillLabelNames.add("异地司导吊牌补助");
        infoItemBean.labelList = skillLabelNames;

        OrderPriceInfoBean.OrderPriceInfoItemBean infoItemBean2 = new OrderPriceInfoBean.OrderPriceInfoItemBean();
        infoItemBean2.title = "服务费用";
        List<String> skillLabelNames2 = new ArrayList<>();
        skillLabelNames2.add("2邮费");
        skillLabelNames2.add("2过路费很对的的鹅鹅鹅");
        skillLabelNames2.add("2宫本武藏");
        skillLabelNames2.add("2策克信条");
        skillLabelNames2.add("2奥巴马");
        skillLabelNames2.add("2马克思");
        skillLabelNames2.add("2呵呵呵呵额额额");
        skillLabelNames2.add("2牛逼吧");
        infoItemBean2.labelList = skillLabelNames2;

        OrderPriceInfoBean.OrderPriceInfoItemBean infoItemBean3 = new OrderPriceInfoBean.OrderPriceInfoItemBean();
        infoItemBean3.title = "附加费用";
        List<String> skillLabelNames3 = new ArrayList<>();
        skillLabelNames3.add("3周杰伦拉萨市");
        skillLabelNames3.add("3我了歌曲");
        skillLabelNames3.add("3李思晨");
        skillLabelNames3.add("3大尿急");
        skillLabelNames3.add("3小坑逼");
        skillLabelNames3.add("3小Q弹");
        skillLabelNames3.add("3小松鼠");
        skillLabelNames3.add("3小肥羊");
        infoItemBean3.labelList = skillLabelNames3;

        List<OrderPriceInfoBean.OrderPriceInfoItemBean> asd = new ArrayList<OrderPriceInfoBean.OrderPriceInfoItemBean>();
        asd.add(infoItemBean);
        asd.add(infoItemBean2);
        asd.add(infoItemBean3);
        OrderPriceInfoBean orderPriceInfoBean = new OrderPriceInfoBean();
        orderPriceInfoBean.priceInfoList = asd;
        return orderPriceInfoBean;
    }
}
