package com.hugboga.custom.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/3/16.
 */
public class DetailPassCityListActivity extends Activity {

    @BindView(R.id.detail_pass_city_title_tv)
    TextView titleTV;
    @BindView(R.id.detail_pass_city_subtitle_tv)
    TextView subtitleTV;
    @BindView(R.id.detail_pass_city_listview)
    ListView listView;

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
        setContentView(R.layout.activity_detail_pass_city);
        ButterKnife.bind(this);

        if (orderBean == null || orderBean.passByCity == null) {
            finish();
            return;
        }

        titleTV.setText(CommonUtils.getString(R.string.order_detail_item_travel) + orderBean.orderIndex);

        subtitleTV.setText(String.format("(%1$s-%2$s)",
                DateUtils.orderChooseDateTransform(orderBean.serviceTime),
                DateUtils.orderChooseDateTransform(orderBean.serviceEndTime)));

        PassCityListAdapter adapter = new PassCityListAdapter(this);
        listView.setAdapter(adapter);
        adapter.setList(orderBean.passByCity);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.PARAMS_DATA, orderBean);
    }

    @OnClick({R.id.detail_pass_city_close_iv})
    public void closeActivity() {
        finish();
    }

    public class PassCityListAdapter extends BaseAdapter<CityBean> {

        public PassCityListAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.view_detail_pass_city_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CityBean bean = getItem(position);
            holder.dateTV.setText(DateUtils.orderChooseDateTransform(DateUtils.getDay(orderBean.serviceTime, position)));
            holder.titleTV.setText(bean.description);
            return convertView;
        }
    }

    class ViewHolder {
        @BindView(R.id.detail_pass_city_item_date_tv)
        TextView dateTV;
        @BindView(R.id.detail_pass_city_item_title_tv)
        TextView titleTV;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
