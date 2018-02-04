package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.InSureListAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.InsureBean;
import com.hugboga.custom.data.bean.InsureResultBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestDelInsure;
import com.hugboga.custom.data.request.RequestInsureList;
import com.hugboga.custom.data.request.RequestOrderDetail;
import com.hugboga.custom.data.request.RequestSubmitInsure;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 16/8/6.
 */

public class InsureActivity extends BaseActivity implements HttpRequestListener {
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;

    InSureListAdapter adapter;
    List<InsureResultBean> beanList = new ArrayList<InsureResultBean>();
    @BindView(R.id.commit)
    TextView commit;
    @BindView(R.id.people_num)
    TextView peopleNum;
    @BindView(R.id.bottom)
    RelativeLayout bottom;


    OrderBean orderBean;
    String from = "";
    String orderNo;

    int insureListSize = 0;
    @BindView(R.id.people_num_all)
    TextView peopleNumAll;
    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.header_right_btn)
    ImageView headerRightBtn;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.header_right_txt)
    TextView headerRightTxt;
    @BindView(R.id.left)
    TextView left;

    protected void initHeader() {
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });

        headerTitle.setText(R.string.insure_title);
        rightBtnDefault();
        adapter = new InSureListAdapter(beanList, activity);
        list.setAdapter(adapter);
        if (null != this.getIntent()) {
            orderBean = (OrderBean) this.getIntent().getSerializableExtra("orderBean");
            orderNo = this.getIntent().getStringExtra(Constants.PARAMS_ID);
            if (orderBean == null && !TextUtils.isEmpty(orderNo)) {
                requestData(new RequestOrderDetail(this, orderNo));
            }
            from = this.getIntent().getStringExtra("from");
            update();
        }
    }

    public void update() {
        if (null != orderBean && !TextUtils.isEmpty(orderBean.orderNo)) {
            headerTitle.setText(R.string.insure_add);
            insureListSize = orderBean.insuranceMap == null ? 0 : orderBean.insuranceMap.size();
            bottom.setVisibility(View.VISIBLE);
            peopleNum.setText(insureListSize + "");
            peopleNumAll.setText("/" + (orderBean.adult + orderBean.child));
        }
    }

    private void rightBtnDefault() {
        headerRightTxt.setText(R.string.insure_add2);
        headerRightTxt.setVisibility(View.VISIBLE);
        headerRightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startFragment(new FgAddInsure());
                Intent intent = new Intent(activity,AddInsureActivity.class);
                startActivity(intent);
            }
        });
    }

    private void rightBtnDel() {
        for (int i = 0; i < beanList.size(); i++) {
            beanList.get(i).isDel = 1;
            beanList.get(i).isCheck = 0;
        }
        adapter.notifyDataSetChanged();

        headerRightTxt.setText(R.string.insure_del);
        headerRightTxt.setVisibility(View.VISIBLE);
        headerRightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCheckBox();
                rightBtnDefault();
                if (!TextUtils.isEmpty(getInsuranceUserId())) {
                    delInsure();
                }
            }
        });
    }

    private void hideCheckBox() {
        for (int i = 0; i < beanList.size(); i++) {
            beanList.get(i).isDel = 0;
        }
        adapter.notifyDataSetChanged();
    }


    private void showCheckBox() {
        for (int i = 0; i < beanList.size(); i++) {
            beanList.get(i).isDel = 1;
        }
        adapter.notifyDataSetChanged();
    }

    private int getCheckNums() {
        int checkNums = insureListSize;
        for (int i = 0; i < beanList.size(); i++) {
            if (1 == beanList.get(i).isCheck) {
                checkNums += 1;
            }
        }
        return checkNums;
    }

    private void resetCheck(InsureResultBean bean) {
        for (int i = 0; i < beanList.size(); i++) {
            if (bean.insuranceUserId.equalsIgnoreCase(beanList.get(i).insuranceUserId)) {
                beanList.get(i).isCheck = 0;
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_insure_list;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
        initView();
        initHeader();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //    String insuranceUserIds = "";

    private String getInsuranceUserId() {
        String insuranceUserIds = "";
        for (int i = 0; i < beanList.size(); i++) {
            if (1 == beanList.get(i).isCheck) {
                insuranceUserIds += beanList.get(i).insuranceUserId + ",";
            }
        }
        if (insuranceUserIds.length() > 1) {
            insuranceUserIds = insuranceUserIds.substring(0, insuranceUserIds.length() - 1);
        }
        return insuranceUserIds;

    }

    private void delInsure() {
        RequestDelInsure requestDelInsure = new RequestDelInsure(activity, getInsuranceUserId());
        HttpRequestUtils.request(activity, requestDelInsure, this);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case EDIT_INSURE:
                Bundle bundle = new Bundle();
                bundle.putSerializable("insureResultBean", (InsureResultBean) (action.data));
                Intent intent = new Intent(activity,AddInsureActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case ADD_INSURE:
                beanList.add(0, (InsureResultBean) (action.data));
                if (null != orderBean) {
                    showCheckBox();
                    if(beanList.size() > 0){
                        bottom.setVisibility(View.VISIBLE);
                    }
                } else {
                    adapter.notifyDataSetChanged();
                }
                break;
            case EDIT_BACK_INSURE:
                final InsureResultBean bean = (InsureResultBean) (action.data);
                for (int i = 0; i < beanList.size(); i++) {
                    if (beanList.get(i).insuranceUserId.equalsIgnoreCase(bean.insuranceUserId)) {
                        beanList.remove(i);
                        beanList.add(0, bean);
                    }
                }
                if (null != orderBean) {
                    showCheckBox();
                } else {
                    adapter.notifyDataSetChanged();
                }
                break;
            case CHECK_INSURE:
                final InsureResultBean beanCheck = (InsureResultBean) (action.data);
                if(null != orderBean) {
                    if (getCheckNums() <= (orderBean.adult + orderBean.child)) {
                        int checkNums = getCheckNums();
                        peopleNum.setText("" + checkNums);
                    } else {
                        resetCheck(beanCheck);
                        CommonUtils.showToast(R.string.insure_check_people_num);
                    }
                }
                break;
            default:
                break;
        }
    }


    int offset = 0;
    int limit = 20;

    protected void initView() {
        list.setEmptyView(emptyView);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                rightBtnDel();
                return false;
            }
        });
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1) && view.getCount() < bean.totalSize) {
                        offset = (offset + 1) * limit;
                        getData();
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        getData();
    }

    private void getData() {
        RequestInsureList requestInsureList = new RequestInsureList(activity, UserEntity.getUser().getUserId(activity), "", offset + "", limit + "");
        HttpRequestUtils.request(activity, requestInsureList, this);
    }


    private void commitInsure() {
        if (orderBean == null) {
            finish();
        }
        RequestSubmitInsure requestSubmitInsure = new RequestSubmitInsure(activity,
                UserEntity.getUser().getUserId(activity), getInsuranceUserId(), orderBean.orderNo);
        HttpRequestUtils.request(activity, requestSubmitInsure, this);
    }


    InsureBean bean;

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestInsureList) {
            bean = (InsureBean) request.getData();
            beanList.addAll(bean.resultBean);
            if (null != orderBean || orderNo != null) {
                showCheckBox();
                list.setOnItemLongClickListener(null);
                if(beanList.size() > 0){
                    bottom.setVisibility(View.VISIBLE);
                }else{
                    bottom.setVisibility(View.GONE);
                }
            }
            adapter.notifyDataSetChanged();

        } else if (request instanceof RequestOrderDetail) {
            RequestOrderDetail mParser = (RequestOrderDetail) request;
            orderBean = mParser.getData();
            update();
        } else if (request instanceof RequestDelInsure) {
            for (int i = beanList.size() - 1; i >= 0; i--) {
                if (beanList.get(i).isCheck == 1) {
                    beanList.remove(i);
                }
            }
            adapter.notifyDataSetChanged();
        } else if (request instanceof RequestSubmitInsure) {
            CommonUtils.showToast(R.string.insure_add_succesd);
            onBack();
//            if(TextUtils.isEmpty(from)) {
            EventBus.getDefault().post(new EventAction(EventType.ADD_INSURE_SUCCESS, orderBean.orderNo));
//            }
            EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
//            Bundle bundle = new Bundle();
//            bundle.putInt(FgOrder.KEY_BUSINESS_TYPE, orderBean.orderType);
//            bundle.putInt(FgOrder.KEY_GOODS_TYPE, orderBean.orderGoodsType);
//            bundle.putString(FgOrder.KEY_ORDER_ID, orderBean.orderNo);
//            startFragment(new FgOrder(), bundle);
//            FgOrderDetail.Params params = new FgOrderDetail.Params();
//            params.orderType = orderBean.orderType;
//            params.orderId = orderBean.orderNo;
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(Constants.PARAMS_DATA, params);
//            bringToFront(FgOrderDetail.class, bundle);
        }

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }


    @OnClick(R.id.commit)
    public void onClick() {
        if (!TextUtils.isEmpty(getInsuranceUserId())) {
            AlertDialogUtils.showAlertDialog(this, false, getString(R.string.insure_dialog_title), getString(R.string.insure_dialog_content)
                    , getString(R.string.insure_dialog_submit), getString(R.string.insure_dialog_check), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    commitInsure();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else{
            CommonUtils.showToast(R.string.insure_check_add);
        }
    }

    @Override
    public String getEventSource() {
        return "添加投保人";
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            onBack();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void onBack() {
        if (orderNo != null) {
            startActivity(new Intent(InsureActivity.this, MainActivity.class));
            EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
            EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));

            OrderDetailActivity.Params params = new OrderDetailActivity.Params();
            params.orderId = orderNo;
            params.source = getEventSource();
            Intent intent = new Intent(InsureActivity.this, OrderDetailActivity.class);
            intent.putExtra(Constants.PARAMS_DATA, params);
            InsureActivity.this.startActivity(intent);
        } else {
            finish();
        }
    }
}
