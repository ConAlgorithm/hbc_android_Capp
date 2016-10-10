package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.TravelFundRecordActivity;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by on 16/5/26.
 */
public class TravelFundAdapter extends BaseAdapter<TravelFundData.TravelFundBean> {

    private int type = 0;

    public TravelFundAdapter(Context context) {
        super(context);
    }

    public void setType(int _type) {
        this.type = _type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_travelfund, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TravelFundData.TravelFundBean bean = getItem(position);

        if (type == TravelFundRecordActivity.TYPE_INVITE_FRIENDS) {
            if (TextUtils.isEmpty(bean.getAvatar())) {
                holder.avatarIV.setImageResource(R.mipmap.collection_icon_pic);
            } else {
                Tools.showImage(holder.avatarIV, bean.getAvatar());
            }
            holder.nameTV.setText(bean.getUserName());
            holder.sourceTV.setText("");
            holder.dateTV.setText(bean.getUpdateTime());

            if (bean.getAmount() == -1) {//未注册/注册两种情况都是-1， 首次使用返现是大于0值
                holder.desTV.setVisibility(View.GONE);
                holder.signTV.setVisibility(View.GONE);
                holder.unitTV.setVisibility(View.GONE);
                holder.amountTV.setText(bean.getType());
                holder.amountTV.setTextColor(0xFF999999);
                holder.amountTV.setTextSize(15);
            } else {
                holder.amountTV.setTextSize(14);
                holder.desTV.setVisibility(View.VISIBLE);
                holder.desTV.setText(bean.getType());
                holder.amountTV.setText("" + bean.getAmount());
                if (bean.getAmount() >= 0) {
                    holder.signTV.setVisibility(View.VISIBLE);
                    holder.amountTV.setTextColor(mContext.getResources().getColor(R.color.travel_fund_basic));
                    holder.unitTV.setTextColor(mContext.getResources().getColor(R.color.travel_fund_basic));
                } else {
                    holder.signTV.setVisibility(View.GONE);
                    holder.amountTV.setTextColor(0xFFCECECE);
                    holder.unitTV.setTextColor(0xFFCECECE);
                }
            }
        } else {
            holder.avatarIV.setLayoutParams(new RelativeLayout.LayoutParams(0, UIUtils.dip2px(60)));
            holder.nameTV.setVisibility(View.GONE);
            holder.dateTV.setText(bean.getCreateDate());
            String sourceStr = bean.getDesc();
            if (!TextUtils.equals(UserEntity.getUser().getUserName(mContext), bean.getUsername())) {
                sourceStr = bean.getUsername() + sourceStr;
            }
            holder.sourceTV.setText(sourceStr);

            holder.desTV.setVisibility(View.GONE);
            holder.amountTV.setText("" + bean.getAmount());
            if (bean.getAmount() >= 0) {
                holder.signTV.setVisibility(View.VISIBLE);
                holder.amountTV.setTextColor(mContext.getResources().getColor(R.color.travel_fund_basic));
                holder.unitTV.setTextColor(mContext.getResources().getColor(R.color.travel_fund_basic));
            } else {
                holder.signTV.setVisibility(View.GONE);
                holder.amountTV.setTextColor(0xFFCECECE);
                holder.unitTV.setTextColor(0xFFCECECE);
            }
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
        @ViewInject(R.id.travelfund_des_tv)
        TextView desTV;
    }
}
