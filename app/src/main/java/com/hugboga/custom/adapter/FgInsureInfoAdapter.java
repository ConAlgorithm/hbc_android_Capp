package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.InsureResultBean;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.utils.Tools;

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
        final InsureResultBean bean = getItem(position);
        holder.nameTV.setText(bean.name);
        holder.passportTV.setText(mContext.getString(R.string.insure_info_passport, bean.passportNo));
        holder.policyNumTV.setText(mContext.getString(R.string.insure_info_policy_num, TextUtils.isEmpty(bean.insuranceUserId) ? "----" : bean.insuranceUserId));
        holder.stateTV.setText(bean.getUserStatusString());
        if (bean.userStatus == 4 || bean.userStatus == 7 || bean.userStatus == 8) {//失败的情况
            holder.stateTV.setTextColor(0xFFFE6635);
        } else {
            holder.stateTV.setTextColor(0xFF979797);
        }
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.insuer_info_name_tv)
        TextView nameTV;
        @ViewInject(R.id.insuer_info_passport_tv)
        TextView passportTV;
        @ViewInject(R.id.insuer_info_policy_num_tv)
        TextView policyNumTV;
        @ViewInject(R.id.insuer_info_state_tv)
        TextView stateTV;
    }
}
