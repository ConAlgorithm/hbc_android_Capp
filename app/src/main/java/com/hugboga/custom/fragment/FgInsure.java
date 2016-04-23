package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.InSureListAdapter;
import com.hugboga.custom.data.bean.InsureBean;
import com.hugboga.custom.data.bean.InsureResultBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestDelInsure;
import com.hugboga.custom.data.request.RequestInsureList;
import com.hugboga.custom.data.request.RequestSubmitInsure;

import org.w3c.dom.Text;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by dyt on 16/4/22.
 */
@ContentView(R.layout.fg_insure_list)
public class FgInsure extends BaseFragment implements HttpRequestListener {
    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.emptyView)
    TextView emptyView;

    InSureListAdapter adapter;
    List<InsureResultBean> beanList = new ArrayList<InsureResultBean>();
    @Bind(R.id.commit)
    TextView commit;
    @Bind(R.id.people_num)
    TextView peopleNum;
    @Bind(R.id.bottom)
    RelativeLayout bottom;


    String orderNo = "";
    @Override
    protected void initHeader() {
        fgTitle.setText("常用投保人");
        rightBtnDefault();
        adapter = new InSureListAdapter(beanList, this.getContext());
        list.setAdapter(adapter);
        orderNo = this.getArguments().getString("orderNo");
        if(!TextUtils.isEmpty(orderNo)){
            bottom.setVisibility(View.VISIBLE);
        }
    }

    private void rightBtnDefault() {
        fgRightBtn.setText("新增");
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(new FgAddInsure());
            }
        });
    }

    private void rightBtnDel() {
        for (int i = 0; i < beanList.size(); i++) {
            beanList.get(i).isDel = 1;
            beanList.get(i).isCheck = 0;
        }
        adapter.notifyDataSetChanged();

        fgRightBtn.setText("删除");
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckBox();
                adapter.notifyDataSetChanged();
                rightBtnDefault();
                if (!TextUtils.isEmpty(getInsuranceUserId())) {
                    delInsure();
                }
            }
        });
    }


    private void showCheckBox(){
        for (int i = 0; i < beanList.size(); i++) {
            beanList.get(i).isDel = 0;
        }
    }

    String insuranceUserIds = "";

    private String getInsuranceUserId() {
        insuranceUserIds = "";
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
        RequestDelInsure requestDelInsure = new RequestDelInsure(this.getActivity(), getInsuranceUserId());
        HttpRequestUtils.request(this.getActivity(), requestDelInsure, this);
    }

    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case EDIT_INSURE:
                FgAddInsure fgAddInsure = new FgAddInsure();
                Bundle bundle = new Bundle();
                bundle.putParcelable("insureResultBean", (InsureResultBean) (action.data));
                fgAddInsure.setArguments(bundle);
                startFragment(fgAddInsure);
                break;
            case ADD_INSURE:
                beanList.add(0, (InsureResultBean) (action.data));
                adapter.notifyDataSetChanged();
                break;
            case EDIT_BACK_INSURE:
                final InsureResultBean bean = (InsureResultBean) (action.data);
                for (int i = 0; i < beanList.size(); i++) {
                    if (beanList.get(i).insuranceUserId.equalsIgnoreCase(bean.insuranceUserId)) {
//                        beanList.remove(i);
                        beanList.set(i, bean);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }
    }


    int offset = 0;
    int limit = 20;

    @Override
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
        RequestInsureList requestInsureList = new RequestInsureList(this.getActivity(), UserEntity.getUser().getUserId(this.getActivity()), "", offset + "", limit + "");
        HttpRequestUtils.request(this.getActivity(), requestInsureList, this);
    }


    private void commitInsure(){
        RequestSubmitInsure requestSubmitInsure = new RequestSubmitInsure(this.getContext(),
                UserEntity.getUser().getUserId(this.getContext()),getInsuranceUserId(),orderNo);
        HttpRequestUtils.request(this.getActivity(),requestSubmitInsure,this);
    }



    InsureBean bean;

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestInsureList) {
            bean = (InsureBean) request.getData();
            beanList.addAll(bean.resultBean);
            if(!TextUtils.isEmpty(orderNo)){
                showCheckBox();
                list.setOnItemLongClickListener(null);
            }
            adapter.notifyDataSetChanged();

        } else {
            for (int i = beanList.size() - 1; i >= 0; i--) {
                if (beanList.get(i).isCheck == 1) {
                    beanList.remove(i);
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.commit)
    public void onClick() {
        if(!TextUtils.isEmpty(getInsuranceUserId())){
            commitInsure();
        }
    }
}
