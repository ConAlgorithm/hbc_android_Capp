package com.hugboga.custom.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.NetWork;
import com.huangbaoche.hbcframe.widget.recycler.ZDefaultDivider;
import com.huangbaoche.hbcframe.widget.recycler.ZListRecyclerView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.NIMChatActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.ImListBean;
import com.hugboga.custom.data.bean.ServiceQuestionBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestChatOrderDetail;
import com.hugboga.custom.data.request.RequestNIMChatList;
import com.hugboga.custom.data.request.RequestNIMRemoveChat;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.UnicornUtils;
import com.hugboga.custom.widget.ChatLogoutView;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.ImItemView;
import com.hugboga.im.ImDataSyncUtils;
import com.hugboga.im.ImHelper;
import com.hugboga.im.ImObserverHelper;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnreadCountChangeListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by SPW on 2017/1/5.
 */
public class FgNimChat extends BaseFragment implements HbcRecyclerSingleTypeAdpater.OnItemClickListener, HbcRecyclerSingleTypeAdpater.OnItemLongClickListener, ImObserverHelper.OnUserStatusListener {

    @BindView(R.id.header_left_btn)
    ImageView leftBtn;

    @BindView(R.id.chat_content)
    RelativeLayout chatLayout; //主题内容显示
    @BindView(R.id.listview)
    ZListRecyclerView recyclerView;
    @BindView(R.id.swipe)
    ZSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.chat_logout)
    RelativeLayout emptyLayout;
    @BindView(R.id.im_login_hint_tv)
    TextView loginHintTV;

    @BindView(R.id.im_chat_logout_view)
    ChatLogoutView chatLogoutView;

    @BindView(R.id.chat_list_empty_tv)
    TextView emptyTV;

    private int reRequestTimes = 0;

    private int limitSize = Constants.DEFAULT_PAGESIZE;

    ImListBean imListBean;
    ArrayList<ImListBean.ServiceBean> serviceBean;

    HbcRecyclerSingleTypeAdpater<ChatBean> adapter;

    boolean isLoading = false;

    ImObserverHelper imObserverHelper;
    CsDialog csDialog;

    @Override
    public int getContentViewId() {
        return R.layout.fg_chat;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    protected void initHeader() {
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        fgTitle.setLayoutParams(titleParams);
        fgTitle.setText(R.string.chat);
        leftBtn.setVisibility(View.GONE);
        setSensorsDefaultEvent("私聊", SensorsConstant.CHAT);

        RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(47), UIUtils.dip2px(16));
        headerRightImageParams.rightMargin = UIUtils.dip2px(18);
        headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        fgRightBtn.setLayoutParams(headerRightImageParams);
        fgRightBtn.setPadding(0, 0, 0, 0);
        fgRightBtn.setImageResource(R.mipmap.topbar_cs2);
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsUtils.onAppClick(getEventSource(), "客服", null);
                //DialogUtil.showDefaultServiceDialog(getContext(), getEventSource());
                csDialog = CommonUtils.csDialog(getContext(), null, null, null, UnicornServiceActivity.SourceType.TYPE_DEFAULT, getEventSource(), new CsDialog.OnCsListener() {
                    @Override
                    public void onCs() {
                        if (csDialog != null && csDialog.isShowing()) {
                            csDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void initView() {
        MLog.e(this + " initView");
        initListView();

        String hintStr = CommonUtils.getString(R.string.chat_login_hint);
        SpannableString msp = new SpannableString(hintStr);
        msp.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                gotoLogin();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.basic_black));
                ds.setUnderlineText(false);
                ds.clearShadowLayer();
            }
        }, 8, hintStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginHintTV.setMovementMethod(LinkMovementMethod.getInstance()); //不设置没有点击事件
        loginHintTV.setText(msp);

        initRegisterMsgOberserve();

        try {
            if (Unicorn.isServiceAvailable()) {
                Unicorn.addUnreadCountChangeListener(listener, true);
            }
        } catch (Exception e) {
            uploadQYErrorMsg(e);
        }

        loadImList();
    }

    private void initRegisterMsgOberserve() {
        imObserverHelper = new ImObserverHelper();
        imObserverHelper.setOnNewMsgListener(new ImObserverHelper.OnNewMsgListener() {
            @Override
            public void onPostNewMsgs(List<RecentContact> list) {
                newHandlerNewMsg(list);
            }
        });
        imObserverHelper.registerMessageObservers(true);
        imObserverHelper.setOnUserStatusListener(this);
        imObserverHelper.registerUserStatusObservers(true);
    }


    private void initListView() {
        MLog.e(this + " initListView");
        adapter = new HbcRecyclerSingleTypeAdpater<>(getActivity(), ImItemView.class);
        recyclerView.setAdapter(adapter);
        ZDefaultDivider divider = recyclerView.getItemDecoration();
        divider.setItemOffsets(0, 0, 0, 0);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        if (!UserEntity.getUser().isLogin(getActivity())) {
            emptyTV.setVisibility(View.GONE);
        }
        setReFreshEvent();
        addOnScrollEvent();
    }


    private void setReFreshEvent() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest(0, false);
            }
        });
    }


    private void addOnScrollEvent() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (imListBean == null || imListBean.totalSize == 0) {
                        return;
                    }
                    int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount() - 1;
                    if (lastVisibleItem >= totalItemCount - adapter.getHeadersCount() - adapter.getFootersCount() && adapter.getListCount() < imListBean.totalSize) {
                        sendRequest(adapter.getListCount(), false);//加载下一页
                    }
                }
            }
        });
    }

    private void sendRequest(int pageIndex, boolean needShowLoading) {
//        emptyTV.setVisibility(View.GONE);
        if (isLoading) {
            return;
        }
        isLoading = true;
        RequestNIMChatList request = new RequestNIMChatList(getActivity(), pageIndex, limitSize);
        HttpRequestUtils.request(getActivity(), request, this, needShowLoading);
    }


    protected void loadImList() {
        MLog.e("isLogin=" + UserEntity.getUser().isLogin(getActivity()));
        if (!UserEntity.getUser().isLogin(getActivity())) {
            needHttpRequest = true;
            if (emptyLayout != null) {
                emptyLayout.setVisibility(View.VISIBLE);
            }
            chatLogoutView.setLooper(true);
            chatLayout.setVisibility(View.GONE);
        } else {
            if (emptyLayout != null) {
                emptyLayout.setVisibility(View.GONE);
            }
            chatLogoutView.setLooper(false);
            chatLayout.setVisibility(View.VISIBLE);
            sendRequest(0, true);
        }
    }

    /**
     * 刷新消息列表
     */
    public void flushList() {
        loadImList();
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestNIMChatList) {
            imListBean = ((RequestNIMChatList) request).getData();
            reRequestTimes = 0;
            if (imListBean != null) {
                if (imListBean.serviceBean != null) {
                    serviceBean = imListBean.serviceBean;
                }
                imListBean.filterService();
            }
            syncChatData();
        }
        swipeRefreshLayout.setRefreshing(false);
    }


    private void updateUI() {
        emptyLayout.setVisibility(View.GONE);
        chatLogoutView.setLooper(false);
    }


    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        super.onDataRequestError(errorInfo, _request);
        requestError();
    }

    @Override
    public void onDataRequestCancel(BaseRequest _request) {
        super.onDataRequestCancel(_request);
        requestError();
    }

    @Override
    public String getEventId() {
        return StatisticConstant.REGIST_LAUNCH;
    }

    @Override
    public String getEventSource() {
        return "私聊";
    }

    @Override
    public Map getEventMap() {
        return super.getEventMap();
    }

    @OnClick({R.id.login_btn, R.id.chat_list_empty_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                gotoLogin();
                break;
            case R.id.chat_list_empty_tv:
                sendRequest(0, true);
                break;
        }
    }

    private void gotoLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra("source", getEventSource());
        startActivity(intent);
        SensorsUtils.onAppClick(getEventSource(), "登录", "");
    }

    @Override
    public void onDestroyView() {
        if (imObserverHelper != null) {
            imObserverHelper.registerMessageObservers(false);
            imObserverHelper.registerUserStatusObservers(false);
        }
        try {
            if (Unicorn.isServiceAvailable()) {
                Unicorn.addUnreadCountChangeListener(null, false);
            }
        } catch (Exception e) {
            uploadQYErrorMsg(e);
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        MLog.e(this + " onEventMainThread " + action.getType());
        switch (action.getType()) {
            case CLICK_USER_LOOUT:
                chatLayout.setVisibility(View.GONE);
                //清理列表数据
                if (adapter.getDatas() != null)
                    adapter.clearData();
                adapter.notifyDataSetChanged();
                emptyLayout.setVisibility(View.VISIBLE);
                chatLogoutView.setLooper(true);
                emptyTV.setVisibility(View.GONE);
                ((MainActivity) getActivity()).setIMCount(0, 0);
                break;
            case CLICK_USER_LOGIN:
                updateUI();
                break;
            case NIM_LOGIN_SUCCESS:
            case ORDER_DETAIL_GUIDE_SUCCEED: //选择司导成功，刷新IM列表
                loadImList();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position, Object itemData) {
        ChatBean chatBean = (ChatBean) itemData;
        if (chatBean.getTargetType() == 3) {
            UnicornServiceActivity.Params params = new UnicornServiceActivity.Params();
            ServiceQuestionBean.QuestionItem questionItem = new ServiceQuestionBean.QuestionItem();
            int unreadCount = SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT, 0);
            if (unreadCount > 0) {
                questionItem.customRole = SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_GROUP_ID, 0);
            }
            params.questionItem = questionItem;
            params.sourceType = UnicornServiceActivity.SourceType.TYPE_CHARTERED;
            Intent intent = new Intent(getContext(), UnicornServiceActivity.class);
            intent.putExtra(Constants.PARAMS_DATA, params);
            intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
            startActivity(intent);
            SensorsUtils.onAppClick(getEventSource(), "旅行小管家", "");
        } else if (chatBean.getTargetType() == 1) {
            SensorsUtils.onAppClick(getEventSource(), "联系司导", "");
            NIMChatActivity.start(getContext(), chatBean.targetId, false, chatBean.getNeTargetId(), getEventSource(), null);
        } else {
            MLog.e("目标用户不是客服，也不是司导");
        }
    }

    @Override
    public void onItemLongClick(View view, final int position, Object itemData) {
        final ChatBean chatBean = (ChatBean) itemData;
        if (chatBean.getTargetType() != 3) {
            AlertDialogUtils.showAlertDialog(getActivity(), getString(R.string.del_chat), CommonUtils.getString(R.string.hbc_confirm), CommonUtils.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RequestNIMRemoveChat requestRemoveChat = new RequestNIMRemoveChat(getActivity(), chatBean.targetId);
                    HttpRequestUtils.request(getContext(), requestRemoveChat, new HttpRequestListener() {
                        @Override
                        public void onDataRequestSucceed(BaseRequest request) {
                            deleteNimRecent(chatBean, position);
                        }

                        @Override
                        public void onDataRequestCancel(BaseRequest request) {
                        }

                        @Override
                        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                        }
                    });
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


    private void requestError() {
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
        if (!NetWork.isNetworkAvailable(MyApplication.getAppContext())) {
            setLocalData();
            return;
        }
        reRequestTimes++;
        if (reRequestTimes < 3) {
            sendRequest(0, false);
            return;
        }

        setLocalData();
    }

    private void setLocalData() {
        if (UserEntity.getUser().isLogin(getActivity())) {
            List<ChatBean> list = getLocalLetters();
            if (list == null || list.size() == 0) {
                emptyTV.setEnabled(true);
                emptyTV.setText(R.string.data_load_error_retry);
                emptyTV.setVisibility(View.VISIBLE);
                emptyTV.setTextSize(14);
                return;
            }
            if (adapter != null) {
                adapter.clearData();
                adapter.addData(list);
            }
        }

    }

    private void computeTotalUnreadCount(List<ChatBean> chatBeans) {
        int totalCount = 0;
        if (chatBeans != null && chatBeans.size() > 0) {
            for (ChatBean bean : chatBeans) {
                totalCount += bean.getImCount();
            }
            EventBus.getDefault().post(new EventAction(EventType.SKU_PUTH_MESSAGE, totalCount));
        }
        if (getActivity() != null) {
            SharedPre.setInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.IM_CHAT_COUNT, totalCount);
            ((MainActivity) getActivity()).setIMCount(totalCount, SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT, 0));
            MLog.e("totalCount = " + totalCount);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void syncChatData() {
        queryLocalRecentList();
    }

    Handler handler = new Handler();

    private void queryLocalRecentList() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImHelper.getRecentContacts(new ImHelper.RecentContactsCallback() {
                    @Override
                    public void onResult(List<RecentContact> list) {
                        isLoading = false;
                        if (list == null || list.size() == 0) {
                            updateUI();
                            if (imListBean != null && imListBean.resultBean != null && adapter != null) {
                                if (imListBean.offset == 0) {
                                    adapter.clearData();
                                }
                                if (imListBean.serviceBean == null && serviceBean != null) {
                                    imListBean.serviceBean = serviceBean;
                                    imListBean.filterService();
                                }
                                for (ChatBean chatBean : imListBean.resultBean) {
                                    chatBean.setImCount(0);
                                    chatBean.setLastMsg("");
                                }
                                adapter.addData(imListBean.resultBean);
                                computeTotalUnreadCount(adapter.getDatas());
                            }
                        } else {
                            if (adapter != null) {
                                updateUI();
                                if (imListBean != null && imListBean.resultBean != null) {
                                    if (imListBean.offset == 0) {
                                        adapter.clearData();
                                    }
                                    ArrayList<ChatBean> chatBeen = new ArrayList<>();
                                    chatBeen.addAll(adapter.getDatas());
                                    chatBeen.addAll(imListBean.resultBean);

                                    if (chatBeen.size() > 0 && chatBeen.get(0).getTargetType() != 3
                                            && serviceBean != null && serviceBean.size() > 0) {
                                        ImListBean.ServiceBean serviceItemBean = serviceBean.get(0);
                                        ChatBean itemChatBean = new ChatBean();
                                        itemChatBean.targetAvatar = serviceItemBean.targetAvatar;
                                        itemChatBean.targetName = serviceItemBean.targetName;
                                        itemChatBean.setTargetType(serviceItemBean.targetType);
                                        chatBeen.add(0, itemChatBean);
                                    }
                                    ImDataSyncUtils.removeRepeatData(chatBeen, limitSize);
                                    ImDataSyncUtils.recentListSync(chatBeen, list);
                                    adapter.clearData();
                                    adapter.addData(chatBeen);
                                    computeTotalUnreadCount(adapter.getDatas());
                                }
                                saveLettersToLocal();
                            }
                        }
                    }
                });
            }
        }, 20);
    }


    private void deleteNimRecent(final ChatBean chatBean, final int position) {
        if (adapter != null) {
            adapter.getDatas().remove(position);
            adapter.notifyDataSetChanged();
            computeTotalUnreadCount(adapter.getDatas());
        }
        ImHelper.delRecentContactById(chatBean.getNeTargetId());
    }


    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void newHandlerNewMsg(final List<RecentContact> messages) {
        if (imListBean == null) {
            return;
        }
        if (messages != null && adapter != null) {
            List<String> newContacts = ImDataSyncUtils.updateRecentSync(adapter.getDatas(), messages);
            if (recyclerView != null && adapter != null) {
                adapter.notifyDataSetChanged();
                computeTotalUnreadCount(adapter.getDatas());
            }
            if (newContacts != null && newContacts.size() > 0) {
                for (int i = 0; i < newContacts.size(); i++) {
                    RequestChatOrderDetail requestChatOrderDetail = new RequestChatOrderDetail(MyApplication.getAppContext(), newContacts.get(i));
                    HttpRequestUtils.request(getContext(), requestChatOrderDetail, new HttpRequestListener() {
                        @Override
                        public void onDataRequestSucceed(BaseRequest request) {
                            ChatBean chatBean = (ChatBean) request.getData();
                            if (messages != null && messages.size() > 0) {
                                for (RecentContact recentContact : messages) {
                                    if (!TextUtils.isEmpty(recentContact.getContactId()) && !TextUtils.isEmpty(chatBean.getNeTargetId()) && recentContact.getContactId().toLowerCase().equals(chatBean.getNeTargetId().toLowerCase())) {
                                        chatBean.setLastMsg(recentContact.getContent());
                                        chatBean.setImCount(recentContact.getUnreadCount());
                                        chatBean.setTimeStamp(recentContact.getTime());
                                        break;
                                    }
                                }
                            }
                            if (adapter != null && adapter.getDatas() != null) {
                                boolean hasExist = false;
                                for (ChatBean tmp : adapter.getDatas()) {
                                    if (tmp != null && chatBean != null && tmp.getNeTargetId() != null && chatBean.getNeTargetId() != null
                                            && tmp.getNeTargetId().toLowerCase().equals(chatBean.getNeTargetId().toLowerCase())) {
                                        hasExist = true;
                                        break;
                                    }
                                }
                                if (!hasExist) {
                                    adapter.getDatas().add(chatBean);
                                }
                                ImDataSyncUtils.sortRecentContacts(adapter.getDatas());
                                computeTotalUnreadCount(adapter.getDatas());
                            }
                        }

                        @Override
                        public void onDataRequestCancel(BaseRequest request) {
                        }

                        @Override
                        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                        }
                    });
                }
            }
        }
    }


    // 添加未读数变化监听，add 为 true 是添加，为 false 是撤销监听。退出界面时，必须撤销，以免造成资源泄露
    private UnreadCountChangeListener listener = new UnreadCountChangeListener() { // 声明一个成员变量
        @Override
        public void onUnreadCountChange(int count) {
            SharedPre.setInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT, count);
            computeTotalUnreadCount(adapter.getDatas());
        }
    };


    private void saveLettersToLocal() {
        if (adapter != null && adapter.getDatas() != null && adapter.getDatas().size() > limitSize) {
            return;
        }
        List<ChatBean> list = adapter.getDatas();
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = MyApplication.getAppContext().openFileOutput(
                    UserEntity.getUser().getUserId(MyApplication.getAppContext()), Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private List<ChatBean> getLocalLetters() {
        List<ChatBean> list = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = MyApplication.getAppContext().openFileInput(UserEntity.getUser().getUserId(MyApplication.getAppContext()));
            ois = new ObjectInputStream(fis);
            list = (List<ChatBean>) ois.readObject();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    private void uploadQYErrorMsg(Exception e) {
        String errorMsg = "七鱼初始化失败";
        if (e != null && !TextUtils.isEmpty(e.getMessage())) {
            errorMsg = e.getMessage();
        }
        UnicornUtils.uploadQiyuInitError(errorMsg);
    }

    @Override
    public void onPostUserStatus(StatusCode code) {
//        if(code!=StatusCode.LOGINED && code!=StatusCode.CONNECTING){
//            ApiFeedbackUtils.requestIMFeedback(3,String .valueOf(code.getValue()));
//        }
        if (code.wontAutoLogin()) {
            //IMUtil.getInstance().connect();
            UserEntity.getUser().clean(getActivity());
            loadImList();
        }
    }
}
