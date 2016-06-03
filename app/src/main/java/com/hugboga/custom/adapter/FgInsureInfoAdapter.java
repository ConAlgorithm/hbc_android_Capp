package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.InsureResultBean;
import com.hugboga.custom.data.bean.TravelFundData;

import net.grobas.view.PolygonImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by qingcha on 16/6/3.
 */
public class FgInsureInfoAdapter extends BaseAdapter<InsureResultBean> {

    public FgInsureInfoAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_insure_info, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.insuer_info_name_tv)
        PolygonImageView avatarIV;
        @ViewInject(R.id.insuer_info_passport_tv)
        TextView amountTV;
        @ViewInject(R.id.insuer_info_policy_num_tv)
        TextView unitTV;
        @ViewInject(R.id.insuer_info_state_tv)
        TextView signTV;
    }
}
