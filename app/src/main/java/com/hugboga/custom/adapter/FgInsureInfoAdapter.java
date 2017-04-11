package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.InsureInfoActivity;
import com.hugboga.custom.data.bean.InsureListBean;
import com.hugboga.custom.data.request.RequestInsuranceResubmit;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.widget.CircularProgress;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by qingcha on 16/6/3.
 */
public class FgInsureInfoAdapter extends BaseAdapter<InsureListBean> {

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
        final InsureListBean bean = getItem(position);
        holder.nameTV.setText(bean.insuranceUserName);
        holder.passportTV.setText(mContext.getString(R.string.insure_info_passport, bean.passportNo));
        holder.policyNumTV.setText(mContext.getString(R.string.insure_info_policy_num, TextUtils.isEmpty(bean.insuranceNo) ? "- - - - - - - -" : bean.insuranceNo));
        holder.stateTV.setText(bean.getUserStatusString());

        if (bean.insuranceStatus == 4) {//失败的情况
            holder.resetLayout.setVisibility(View.VISIBLE);
            if (bean.isResubmit) {
                holder.resetProgress.setVisibility(View.VISIBLE);
                holder.resetTV.setVisibility(View.GONE);
            } else {
                holder.resetProgress.setVisibility(View.GONE);
                holder.resetTV.setVisibility(View.VISIBLE);
                final CircularProgress resetProgress = holder.resetProgress;
                final TextView resetTV = holder.resetTV;
                holder.resetTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bean.isResubmit = true;
                        resetProgress.setVisibility(View.VISIBLE);
                        resetTV.setVisibility(View.GONE);
                        requestInsuranceSearch(bean);
                    }
                });
            }
        } else {
            holder.resetLayout.setVisibility(View.GONE);
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

        @ViewInject(R.id.insuer_info_reset_layout)
        FrameLayout resetLayout;
        @ViewInject(R.id.insuer_info_reset_tv)
        TextView resetTV;
        @ViewInject(R.id.insuer_info_reset_progress)
        CircularProgress resetProgress;
    }

    public void requestInsuranceSearch(final InsureListBean bean) {
        RequestInsuranceResubmit request = new RequestInsuranceResubmit(mContext, bean.insuranceNo);
        HttpRequestUtils.request(mContext, request, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                if (mContext instanceof InsureInfoActivity) {
                    ((InsureInfoActivity)mContext).isUpdateOrderDetail = true;
                }
                bean.insuranceStatus = 1;
                notifyDataSetChanged();
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

            }
        }, false);
    }
}
