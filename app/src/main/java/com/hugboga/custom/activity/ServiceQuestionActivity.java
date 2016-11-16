package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.ServiceQuestionAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ServiceQuestionBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequsetServiceQuestionList;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.SpaceItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/11/14.
 */
public class ServiceQuestionActivity extends BaseActivity{

    @Bind(R.id.service_question_rv)
    RecyclerView questionRV;
    private ServiceQuestionAdapter adapter;

    private UnicornServiceActivity.Params params;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (UnicornServiceActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (UnicornServiceActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        setContentView(R.layout.activity_service_question);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initDefaultTitleBar();
        fgTitle.setText("请选择内容");

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        questionRV.setLayoutManager(layoutManager);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration();
        itemDecoration.setItemOffsets(0, 0, 0, UIUtils.dip2px(22), LinearLayout.VERTICAL);
        questionRV.addItemDecoration(itemDecoration);
        adapter = new ServiceQuestionAdapter(this);
        questionRV.setAdapter(adapter);

        requestData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case QUESTION_ITEM:
                ServiceQuestionBean.QuestionItem questionItem = (ServiceQuestionBean.QuestionItem) action.getData();
                if (questionItem.type == 3) { //进客服
                    if (questionItem.customRole <= 0) {
                        return;
                    }
                    if (!CommonUtils.isLogin(this)) {
                        return;
                    }
                    params.questionItem = questionItem;
                    Intent intent = new Intent(this, UnicornServiceActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    startActivity(intent);
                } else {
                    ServiceQuestionBean userServiceQuestionBean = new ServiceQuestionBean();
                    userServiceQuestionBean.viewType = ServiceQuestionBean.TYPE_QUESTION_USER_ITEM;
                    ArrayList<ServiceQuestionBean.QuestionItem> userQuestionList = new ArrayList<ServiceQuestionBean.QuestionItem>(1);
                    userQuestionList.add(questionItem);
                    userServiceQuestionBean.questionList = userQuestionList;

                    ServiceQuestionBean serviceQuestionBean = new ServiceQuestionBean();
                    serviceQuestionBean.questionList = questionItem.questionItemList;

                    ArrayList<ServiceQuestionBean> questionList = new ArrayList<ServiceQuestionBean>(2);
                    questionList.add(userServiceQuestionBean);
                    questionList.add(serviceQuestionBean);

                    adapter.addData(questionList, true);
                    questionRV.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
                break;
        }
    }

    private void requestData() {
        if (params != null) {
            requestData(new RequsetServiceQuestionList(this, UnicornServiceActivity.SourceType.getRequsetType(params.sourceType)));
//            requestData(new RequsetServiceQuestionList(this, 1));
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequsetServiceQuestionList) {
            RequsetServiceQuestionList request = (RequsetServiceQuestionList) _request;
            ServiceQuestionBean data = request.getData();
//            ServiceQuestionBean data = JsonUtils.getNativeObject("emoji.json", ServiceQuestionBean.class);
            if (data == null) {
                intentDefaultServiceActivity();
            }
            ArrayList<ServiceQuestionBean> questionList = new ArrayList<ServiceQuestionBean>(1);
            questionList.add(data);
            adapter.addData(questionList);
        }
    }

    private void intentDefaultServiceActivity() {
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        Intent intent = new Intent(this, UnicornServiceActivity.class);
        params.sourceType = UnicornServiceActivity.SourceType.TYPE_DEFAULT;
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }
}
