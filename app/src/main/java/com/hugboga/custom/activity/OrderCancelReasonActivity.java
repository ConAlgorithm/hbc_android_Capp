package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.adapter.HbcRecyclerTypeBaseAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CancelReasonBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCancelReason;
import com.hugboga.custom.data.request.RequestOrderCancel;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.CancelReasonItemView;
import com.hugboga.custom.widget.DialogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/11/18.
 */
public class OrderCancelReasonActivity extends BaseActivity implements HbcRecyclerTypeBaseAdpater.OnItemClickListener {

    @Bind(R.id.cancel_reason_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.cancel_reason_confirm_tv)
    TextView confirmTV;

    private HbcRecyclerSingleTypeAdpater<CancelReasonBean.CancelReasonItem> mAdapter;
    private CancelReasonBean data;
    private CancelReasonBean.CancelReasonItem selectedReasonItem = null;
    private View otherReasonView;
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
        setContentView(R.layout.activity_order_cancel_reason);
        ButterKnife.bind(this);

        initDefaultTitleBar();
        fgTitle.setText("请选择取消原因");

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, CancelReasonItemView.class);
        mAdapter.setOnItemClickListener(this);

        otherReasonView = LayoutInflater.from(this).inflate(R.layout.view_cancel_reason_other, null);
        otherReasonView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mRecyclerView.setAdapter(mAdapter);

        confirmTV.setBackgroundColor(0xFFD2D2D2);
        confirmTV.setTextColor(getResources().getColor(R.color.white));

        requestData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (orderBean != null) {
            outState.putSerializable(Constants.PARAMS_DATA, orderBean);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftInput();
    }

    private void requestData() {
        requestData(new RequestCancelReason(this));
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCancelReason) {
            RequestCancelReason request = (RequestCancelReason) _request;
            data = request.getData();
            if (data == null) {
                return;
            }
            mAdapter.addData(data.cancelReasonList);
        } else if (_request instanceof RequestOrderCancel) {
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE, orderBean.orderNo));
            EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
            DialogUtil dialogUtil = DialogUtil.getInstance(this);
            dialogUtil.showCustomDialog("取消订单成功", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }

    @OnClick({R.id.cancel_reason_confirm_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_reason_confirm_tv:
                if (selectedReasonItem == null) {
                    CommonUtils.showToast("请选择取消原因");
                    return;
                }
                CancelReasonBean.CancelReasonItem cancelReasonItem = null;
                if(selectedReasonItem.isOtherReason()) {
                    cancelReasonItem =  new CancelReasonBean.CancelReasonItem();
                    cancelReasonItem.type = selectedReasonItem.type;
                    EditText editText = (EditText) otherReasonView.findViewById(R.id.cancel_reason_other_et);
                    if (editText.getText() == null || TextUtils.isEmpty(editText.getText().toString()) || TextUtils.isEmpty(editText.getText().toString().trim())) {
                        CommonUtils.showToast("请输入文字内容哦");
                        return;
                    }
                    String content = editText.getText().toString().trim();
                    final int size = content.length();
                    for(int i = 0; i < size; i++) {
                        if (!Tools.isEmojiCharacter(content.charAt(i))) {
                            CommonUtils.showToast("取消原因不能包含表情符号");
                            return;
                        }
                    }
                    cancelReasonItem.content = selectedReasonItem.content + String.format("（%1$s）", editText.getText().toString().trim());
                } else {
                    cancelReasonItem = selectedReasonItem;
                }
                String cancelReason = JsonUtils.toJson(cancelReasonItem);
                if (orderBean.orderStatus == OrderStatus.INITSTATE) {
                    cancelOrder(orderBean.orderNo, 0, cancelReason);
                } else {
                    orderBean.cancelReason = cancelReason;
                    Intent intent = new Intent(OrderCancelReasonActivity.this, OrderCancelActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, orderBean);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position, Object itemData) {
        if (data != null && data.cancelReasonList != null) {
            ArrayList<CancelReasonBean.CancelReasonItem> cancelReasonList = data.cancelReasonList;
            final int size = cancelReasonList.size();
            if (position > size) {
                return;
            }
            for (int i = 0; i < size; i++) {
                if (position == i) {
                    selectedReasonItem = cancelReasonList.get(i);
                    selectedReasonItem.isSelected = true;
                    confirmTV.setBackgroundResource(R.drawable.shape_yellow_btn);
                    confirmTV.setTextColor(getResources().getColor(R.color.default_black));
                } else {
                    cancelReasonList.get(i).isSelected = false;
                }
            }
            if (position == size -1 && selectedReasonItem.isOtherReason()) {//其它原因
                mAdapter.addFooterView(otherReasonView);
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            } else {
                mAdapter.cleanAllFooterView(false);
                hideSoftInput();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 取消订单
     */
    private void cancelOrder(String orderID, double cancelPrice, String reason) {
        if (cancelPrice < 0) cancelPrice = 0;
        RequestOrderCancel request = new RequestOrderCancel(this, orderID, cancelPrice, reason);
        requestData(request);
    }
}
