package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.ServiceQuestionAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ServiceQuestionBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequsetServiceQuestionList;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.SpaceItemDecoration;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by qingcha on 16/11/14.
 */
public class ServiceQuestionActivity extends BaseActivity{

    @BindView(R.id.service_question_rv)
    RecyclerView mRecyclerView;
    private ServiceQuestionAdapter adapter;

    private UnicornServiceActivity.Params params;
    private int lastCustomRole;
    private ServiceQuestionBean serviceQuestionBean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_service_question;
    }

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

        EventBus.getDefault().register(this);

        initDefaultTitleBar();
        fgTitle.setText(R.string.service_question_title);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration();
        itemDecoration.setItemOffsets(0, 0, 0, UIUtils.dip2px(22), LinearLayout.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        adapter = new ServiceQuestionAdapter(this);
        mRecyclerView.setAdapter(adapter);

        requestData();

        unreadMsg();
        setSensorsLaunchIM();
    }

    private void unreadMsg() {
        if (params == null || params.sourceType != UnicornServiceActivity.SourceType.TYPE_CHAT_LIST) {
            return;
        }
        int unreadCount = SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT, 0);
        if (unreadCount > 0) {
            AlertDialogUtils.showAlertDialog(activity, CommonUtils.getString(R.string.service_question_dialog_title),
                    CommonUtils.getString(R.string.service_question_dialog_go), CommonUtils.getString(R.string.service_question_dialog_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ServiceQuestionBean.QuestionItem questionItem = new ServiceQuestionBean.QuestionItem();
                            questionItem.customRole = SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_GROUP_ID, 0);
                            params.questionItem = questionItem;
                            Intent intent = new Intent(ServiceQuestionActivity.this, UnicornServiceActivity.class);
                            intent.putExtra(Constants.PARAMS_DATA, params);
                            startActivity(intent);
                            ServiceQuestionActivity.this.finish();
                            dialog.dismiss();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
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
                final ServiceQuestionBean.QuestionItem questionItem = (ServiceQuestionBean.QuestionItem) action.getData();
                if (questionItem.isRoot) {
                    ArrayList<ServiceQuestionBean> questionList = new ArrayList<ServiceQuestionBean>(1);
                    questionList.add(serviceQuestionBean);
                    adapter.addData(questionList, true);
                    mRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    handler.sendEmptyMessageDelayed(0, 100);
                    return;
                }
                if (questionItem.type == 3) { //进客服
                    if (!CommonUtils.isLogin(this,getEventSource())) {
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

                    ServiceQuestionBean questionBean = new ServiceQuestionBean();
                    if (questionItem.type == 2) {
                        ArrayList<ServiceQuestionBean.QuestionItem> questionList = new ArrayList<ServiceQuestionBean.QuestionItem>(3);
                        ServiceQuestionBean.QuestionItem questionItem2 = (ServiceQuestionBean.QuestionItem) questionItem.clone();
                        if (questionItem2 == null) {
                            questionItem2 = questionItem;
                        }
                        questionItem2.isAnswer = true;
                        questionList.add(questionItem2);
                        if (lastCustomRole > 0) {
                            ServiceQuestionBean.QuestionItem defaultServiceQuestionItem = new ServiceQuestionBean.QuestionItem();
                            defaultServiceQuestionItem.type = 3;
                            defaultServiceQuestionItem.customRole = lastCustomRole;
                            defaultServiceQuestionItem.parentName = questionItem2.adviceName;
                            defaultServiceQuestionItem.adviceName = "还没解决您的问题？转接人工服务";
                            questionList.add(defaultServiceQuestionItem);
                        }

                        ServiceQuestionBean.QuestionItem serviceQuestionItem = new ServiceQuestionBean.QuestionItem();
                        serviceQuestionItem.type = 1;
                        serviceQuestionItem.adviceName = "查看其它问题";
                        serviceQuestionItem.isRoot = true;
                        serviceQuestionItem.questionItemList = serviceQuestionBean.questionList;
                        questionList.add(serviceQuestionItem);

                        questionBean.questionList = questionList;
                    } else {
                        questionBean.questionList = questionItem.questionItemList;
                    }
                    ArrayList<ServiceQuestionBean> questionList = new ArrayList<ServiceQuestionBean>(2);
                    questionList.add(userServiceQuestionBean);
                    questionList.add(questionBean);
                    adapter.addData(questionList, true);
                    mRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    handler.sendEmptyMessageDelayed(0, 100);
                }
                setSensorsIM(questionItem);
                break;
        }
    }

    public void setLastCustomRole(int lastCustomRole) {
        this.lastCustomRole = lastCustomRole;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    };

    private void requestData() {
        if (params != null) {
            requestData(new RequsetServiceQuestionList(this, UnicornServiceActivity.SourceType.getRequsetType(params.sourceType)));
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequsetServiceQuestionList) {
            RequsetServiceQuestionList request = (RequsetServiceQuestionList) _request;
            ServiceQuestionBean data = request.getData();
            if (data == null) {
                intentDefaultServiceActivity();
            }
            serviceQuestionBean = data;
            ArrayList<ServiceQuestionBean> questionList = new ArrayList<ServiceQuestionBean>(1);
            questionList.add(data);
            adapter.addData(questionList);
        }
    }

    private void intentDefaultServiceActivity() {
        if (!CommonUtils.isLogin(this,getEventSource())) {
            return;
        }
        Intent intent = new Intent(this, UnicornServiceActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }

    //神策统计_IM客服（定位im的入口和用户进行的操作）
    public void setSensorsIM(ServiceQuestionBean.QuestionItem questionItem) {
        try {
            JSONObject properties = new JSONObject();
            switch (params.sourceType) {
                case UnicornServiceActivity.SourceType.TYPE_DEFAULT:
                    properties.put("hbc_im_type", "统一入口");
                    break;
                case UnicornServiceActivity.SourceType.TYPE_CHARTERED:
                    properties.put("hbc_im_type", "包车详情");//在线客服进入路径：订单页
                    break;
                case UnicornServiceActivity.SourceType.TYPE_ORDER:
                    properties.put("hbc_im_type", "订单页");//在线客服进入路径：订单页
                    break;
                case UnicornServiceActivity.SourceType.TYPE_LINE:
                    properties.put("hbc_im_type", "商品详情");//在线客服进入路径：商品详情
                    break;
                case UnicornServiceActivity.SourceType.TYPE_CHAT_LIST:
                    properties.put("hbc_im_type", "私聊");//在线客服进入路径：私聊
                    break;
            }
            properties.put("hbc_im_title", questionItem.adviceName);//IM标题（进入IM后点击热门问题，人工客服等）
            if (TextUtils.isEmpty(questionItem.parentName)) {
                properties.put("hbc_im_father", "");//该标题的父级，无父级记为空
            } else {
                properties.put("hbc_im_father", questionItem.parentName);//该标题的父级，无父级记为空
            }
            SensorsDataAPI.sharedInstance(this).track("contact_im", properties);//事件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //神策统计_聊天入口展开
    public void setSensorsLaunchIM() {
        JSONObject properties = new JSONObject();
        try {
            switch (params.sourceType) {
                case UnicornServiceActivity.SourceType.TYPE_DEFAULT:
                    properties.put("launch_type", "统一入口");
                    break;
                case UnicornServiceActivity.SourceType.TYPE_CHARTERED:
                    properties.put("launch_type", "包车详情");//在线客服进入路径：订单页
                    break;
                case UnicornServiceActivity.SourceType.TYPE_ORDER:
                    properties.put("launch_type", "订单页");//在线客服进入路径：订单页
                    break;
                case UnicornServiceActivity.SourceType.TYPE_LINE:
                    properties.put("launch_type", "商品详情");//在线客服进入路径：商品详情
                    break;
                case UnicornServiceActivity.SourceType.TYPE_CHAT_LIST:
                    properties.put("launch_type", "私聊");//在线客服进入路径：私聊
                    break;
            }
            SensorsDataAPI.sharedInstance(this).track("launch_im", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
