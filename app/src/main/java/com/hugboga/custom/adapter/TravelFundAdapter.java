package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.utils.Tools;

import net.grobas.view.PolygonImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by qingcha on 16/5/26.
 */
public class TravelFundAdapter extends BaseAdapter<TravelFundData.TravelFundBean> {

    private boolean isFgTravelFund = false;

    public TravelFundAdapter(Context context) {
        super(context);
    }

    /**
     *  判断是否为旅游基金页面 FgTravelFund
     *  */
    public void setFgTravelFund(boolean fgTravelFund) {
        isFgTravelFund = fgTravelFund;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_travelfund, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
            if (isFgTravelFund) {
                int basicColor = mContext.getResources().getColor(R.color.travel_fund_basic);
                holder.sourceTV.setTextColor(basicColor);
                holder.amountTV.setTextColor(basicColor);
                holder.unitTV.setTextColor(basicColor);
                holder.signTV.setTextColor(basicColor);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TravelFundData.TravelFundBean bean = getItem(position);
        if (TextUtils.isEmpty(bean.getAvatar())) {
            holder.avatarIV.setImageResource(R.mipmap.collection_icon_pic);
        } else {
            Tools.showImage(mContext, holder.avatarIV, bean.getAvatar());
        }
        holder.nameTV.setText(bean.getUsername());
        holder.sourceTV.setText(bean.getSource());
        holder.dateTV.setText(bean.getCreateDate());
        holder.amountTV.setText("" + bean.getAmount());
        if (bean.getAmount() >= 0) {
            holder.signTV.setVisibility(View.VISIBLE);
        } else {
            holder.signTV.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.travelfund_avatar_iv)
        PolygonImageView avatarIV;
        @ViewInject(R.id.travelfund_amount_tv)
        TextView amountTV;
        @ViewInject(R.id.travelfund_amount_unit_tv)
        TextView unitTV;
        @ViewInject(R.id.travelfund_amount_sign_tv)
        TextView signTV;
        @ViewInject(R.id.travelfund_name_tv)
        TextView nameTV;
        @ViewInject(R.id.travelfund_source_tv)
        TextView sourceTV;
        @ViewInject(R.id.travelfund_date_tv)
        TextView dateTV;
    }
}
