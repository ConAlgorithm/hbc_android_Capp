package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CircularProgress;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by qingcha on 16/6/3.
 */
public class FgInsureInfoAdapter extends BaseAdapter<List<InsureListBean>> {

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
        final List<InsureListBean> itemList = getItem(position);
        InsureListBean bean = itemList.get(0);
        holder.nameTV.setText(bean.insuranceUserName);
        holder.passportTV.setText(mContext.getString(R.string.insure_info_passport, bean.passportNo));
        setItemData(holder.stateContainerLayout, itemList);
        holder.resetTV.setVisibility(isShowResetView(itemList) ? View.VISIBLE : View.GONE);
        holder.resetTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int size = itemList.size();
                for(int i = 0; i < size; i++) {
                    InsureListBean bean = itemList.get(i);
                    if (bean.insuranceStatus == 4) {
                        bean.isResubmit = true;
                    }
                }
                notifyDataSetChanged();
                v.setVisibility(View.GONE);
                requestInsuranceSearch(itemList);
            }
        });
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.insuer_info_name_tv)
        TextView nameTV;
        @ViewInject(R.id.insuer_info_passport_tv)
        TextView passportTV;
        @ViewInject(R.id.insuer_info_reset_tv)
        TextView resetTV;
        @ViewInject(R.id.insuer_info_state_container_layout)
        LinearLayout stateContainerLayout;
    }

    public void requestInsuranceSearch(final List<InsureListBean> insureList) {
        final int size = insureList.size();
        String insuranceNos = "";
        final String split = ",";
        for(int i = 0; i < size; i++) {
            InsureListBean bean = insureList.get(i);
            if (bean.insuranceStatus == 4) {
                insuranceNos += bean.insuranceNo + split;
            }
        }
        if (!TextUtils.isEmpty(insuranceNos)) {
            insuranceNos = insuranceNos.substring(0, insuranceNos.lastIndexOf(split));
        }
        RequestInsuranceResubmit request = new RequestInsuranceResubmit(mContext, insuranceNos);
        HttpRequestUtils.request(mContext, request, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                if (mContext instanceof InsureInfoActivity) {
                    ((InsureInfoActivity)mContext).isUpdateOrderDetail = true;
                }
                for(int i = 0; i < size; i++) {
                    InsureListBean bean = insureList.get(i);
                    if (bean.insuranceStatus == 4) {
                        bean.insuranceStatus = 1;
                    }
                }
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

    public void setItemData(LinearLayout containerLayout, List<InsureListBean> itemList) {
        if (itemList == null || itemList.size() == 0) {
            containerLayout.setVisibility(View.GONE);
            return;
        }
        containerLayout.setVisibility(View.VISIBLE);
        final int labelsSize = itemList.size();
        for (int i = 0; i < labelsSize; i++) {
            InsureListBean insureListBean = itemList.get(i);
            if (insureListBean == null) {
                continue;
            }
            RelativeLayout itemLayout = null;
            if (i < containerLayout.getChildCount()) {
                itemLayout = (RelativeLayout) containerLayout.getChildAt(i);
                itemLayout.setVisibility(View.VISIBLE);
            } else {
                itemLayout = (RelativeLayout) mInflater.inflate(R.layout.view_insuer_state_item, null);
                if (i > 0) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = UIUtils.dip2px(12);
                    containerLayout.addView(itemLayout, params);
                } else {
                    containerLayout.addView(itemLayout);
                }
            }
            updateItem(itemLayout, insureListBean);
        }
        for (int j = labelsSize; j < containerLayout.getChildCount(); j++) {
            containerLayout.getChildAt(j).setVisibility(View.GONE);
        }
    }

    public void updateItem(RelativeLayout itemLayout, InsureListBean bean) {
        TextView policynumTV = (TextView)itemLayout.findViewById(R.id.insuer_info_item_policynum_tv);
        TextView stateTV = (TextView)itemLayout.findViewById(R.id.insuer_info_item_state_tv);
        CircularProgress progressView = (CircularProgress)itemLayout.findViewById(R.id.insuer_info_item_progress);

        String policyNum = "- - - - - - - - - - - -";
        if (bean.insuranceStatus == 4) {
            if (bean.isResubmit) {
                progressView.setVisibility(View.VISIBLE);
                stateTV.setVisibility(View.GONE);
            } else {
                progressView.setVisibility(View.GONE);
                stateTV.setVisibility(View.VISIBLE);
            }
        } else {
            progressView.setVisibility(View.GONE);
            stateTV.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(bean.insuranceNo)) {
                policyNum = bean.insuranceNo;
            }
        }
        stateTV.setText(bean.getUserStatusString());
        policynumTV.setText(policyNum);
    }

    public boolean isShowResetView(List<InsureListBean> itemList) {
        int size = itemList.size();
        for(int i = 0; i < size; i++) {
            InsureListBean bean = itemList.get(i);
            if (bean.insuranceStatus == 4 && bean.isResubmit != true) {
                return true;
            } else {
              continue;
            }
        }
        return false;
    }
}
