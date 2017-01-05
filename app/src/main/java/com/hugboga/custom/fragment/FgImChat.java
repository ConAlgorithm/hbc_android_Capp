package com.hugboga.custom.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.page.Page;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.NetWork;
import com.huangbaoche.hbcframe.widget.recycler.ZListPageView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.NIMChatActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.adapter.ChatAdapter;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.data.request.RequestChatOrderDetail;
import com.hugboga.custom.data.request.RequestNIMChatList;
import com.hugboga.custom.data.request.RequestNIMRemoveChat;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.NimRecentListSyncUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.UnicornUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.netease.nim.uikit.uinfo.UserInfoHelper;
import com.netease.nim.uikit.uinfo.UserInfoObservable;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnreadCountChangeListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/8/24.
 */
@ContentView(R.layout.fg_chat)
public class FgImChat extends BaseFragment implements ZBaseAdapter.OnItemClickListener, ZListPageView.NoticeViewTask {

    @ViewInject(R.id.header_left_btn)
    private ImageView leftBtn;

    @ViewInject(R.id.chat_content)
    RelativeLayout chatLayout; //主题内容显示
    @ViewInject(R.id.listview)
    ZListPageView recyclerView;
    @ViewInject(R.id.swipe)
    ZSwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.chat_logout)
    RelativeLayout emptyLayout;

    @ViewInject(R.id.chat_list_empty_tv)
    TextView emptyTV;

    @ViewInject(R.id.login_btn)
    TextView loginBtn;

    private ChatAdapter adapter;

    private int reRequestTimes = 0;

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            computeTotalUnreadCount(adapter.getDatas());
            adapter.notifyDataSetChanged();
        }
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }

    }

    @Override
    protected void initHeader() {
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        fgTitle.setLayoutParams(titleParams);
        fgTitle.setText("私聊");
        leftBtn.setVisibility(View.GONE);
        setSensorsDefaultEvent("私聊", SensorsConstant.CHAT);

        RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(38), UIUtils.dip2px(38));
        headerRightImageParams.rightMargin = UIUtils.dip2px(18);
        headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        fgRightBtn.setLayoutParams(headerRightImageParams);
        fgRightBtn.setPadding(0,0,0,0);
        fgRightBtn.setImageResource(R.mipmap.icon_service);
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.getInstance((Activity)getContext()).showCallDialogTitle(getContext(), null);
            }
        });
    }

    @Override
    protected void initView() {
        MLog.e(this + " initView");
        initListView();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        registerObservers(true);
        Unicorn.addUnreadCountChangeListener(listener, true);
        registerUserInfoObserver();

        if (UserEntity.getUser().isLogin(getActivity()) && recyclerView != null) {
            loadData();
        }
    }


    private void initListView() {
        MLog.e(this + " initListView");
        adapter = new ChatAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setzSwipeRefreshLayout(swipeRefreshLayout);
        recyclerView.setEmptyLayout(emptyLayout);
        RequestNIMChatList parserChatList = new RequestNIMChatList(getActivity());
        recyclerView.setRequestData(parserChatList);
        recyclerView.setOnItemClickListener(this);
        recyclerView.setNoticeViewTask(this);
        recyclerView.setOnItemLongClickListener(new ZBaseAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                if (position != 0) {
                    final ChatBean chatBean = adapter.getDatas().get(position);
                    AlertDialogUtils.showAlertDialog(getActivity(), getString(R.string.del_chat), "确定", "取消", new DialogInterface.OnClickListener() {
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
        });
        if (!UserEntity.getUser().isLogin(getActivity())) {
            emptyTV.setVisibility(View.GONE);
        }
    }

    /**
     * 加载数据
     */
    public void loadData() {
        if (recyclerView != null) {
            emptyTV.setVisibility(View.GONE);
            recyclerView.showPageFirst();
        }
    }

    @Override
    protected Callback.Cancelable requestData() {
        MLog.e("isLogin=" + UserEntity.getUser().isLogin(getActivity()));
        if (!UserEntity.getUser().isLogin(getActivity())) {
            needHttpRequest = true;
            emptyLayout.setVisibility(View.VISIBLE);
            chatLayout.setVisibility(View.GONE);
            return null;
        } else {
            emptyLayout.setVisibility(View.GONE);
            chatLayout.setVisibility(View.VISIBLE);
            loadData();
            return null;
        }
    }

    @Override
    protected void inflateContent() {
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
    }

    @Override
    public String getEventId() {
        return StatisticConstant.REGIST_LAUNCH;
    }

    @Override
    public String getEventSource() {
        return "私聊页";
    }

    @Override
    public Map getEventMap() {
        return super.getEventMap();
    }

    @Event({R.id.login_btn, R.id.chat_list_empty_tv})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                intent.putExtra("source", getEventSource());
                startActivity(intent);
                break;
            case R.id.chat_list_empty_tv:
                loadData();
                break;
        }
    }


    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.e("onFragmentResult " + bundle);
        requestData();
    }

    @Override
    public void onDestroyView() {
        registerObservers(false);
        Unicorn.addUnreadCountChangeListener(null, false);
        unregisterUserInfoObserver();
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
                    adapter.getDatas().clear();
                adapter.notifyDataSetChanged();
                emptyLayout.setVisibility(View.VISIBLE);
                if (loginBtn != null)
                    loginBtn.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).setIMCount(0, 0);
                break;
            case NIM_LOGIN_SUCCESS:
                RequestNIMChatList parserChatList = new RequestNIMChatList(getActivity());
                recyclerView.setRequestData(parserChatList);
                requestData();
                //registerObservers(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ChatBean chatBean = adapter.getDatas().get(position);
        if (chatBean.targetType==3) {
            UnicornUtils.openServiceActivity(getContext(), UnicornServiceActivity.SourceType.TYPE_CHAT_LIST);
        } else if (chatBean.targetType == 1) {
            if (!IMUtil.getInstance().isLogined()) {
                return;
            }
            String titleJson = getChatInfo(chatBean);
            MyApplication.requestRemoteNimUserInfo(chatBean.nTargetId);
            NIMChatActivity.start(getContext(), chatBean.nTargetId, null, titleJson, chatBean.isCancel);
        } else {
            MLog.e("目标用户不是客服，也不是司导");
        }
    }

    private String getChatInfo(ChatBean chatBean) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.isChat = true;
        chatInfo.userId = chatBean.targetId;
        chatInfo.userAvatar = chatBean.targetAvatar;
        chatInfo.title = chatBean.targetName;
        chatInfo.targetType = "" + chatBean.targetType;
        chatInfo.inBlack = chatBean.inBlack;
        chatInfo.imUserId = chatBean.nTargetId;
        chatInfo.flag = chatBean.flag;
        chatInfo.timediff = chatBean.timediff;
        chatInfo.timezone = chatBean.timezone;
        chatInfo.cityName = chatBean.cityName;
        chatInfo.countryName = chatBean.countryName;
        return new ParserChatInfo().toJsonString(chatInfo);
    }

    @Override
    public void notice(Object object) {
        List<ChatBean> chatBeans = adapter.getDatas();
        computeTotalUnreadCount(chatBeans);
        emptyTV.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        if (UserEntity.getUser().isLogin(MyApplication.getAppContext())) {
            if (loginBtn != null)
                loginBtn.setVisibility(View.GONE);
        } else {
            if (loginBtn != null)
                loginBtn.setVisibility(View.VISIBLE);
        }

        syncChatData();
        reRequestTimes = 0;
    }


    @Override
    public void error(ExceptionInfo errorInfo, BaseRequest request) {
        if (!NetWork.isNetworkAvailable(MyApplication.getAppContext())) {
            return;
        }
        reRequestTimes++;
        if (reRequestTimes < 3) {
            loadData();
            return;
        }
        if (UserEntity.getUser().isLogin(getActivity())) {
            List<ChatBean> list = getLocalLetters();
            if (list == null || list.size() == 0) {
                emptyTV.setVisibility(View.VISIBLE);
                return;
            }
            if (adapter != null) {
                adapter.removeAll();
                adapter.addDatas(list);
                adapter.notifyDataSetChanged();
                if (recyclerView != null && recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }
    }


    private void computeTotalUnreadCount(List<ChatBean> chatBeans) {
        if (chatBeans != null && chatBeans.size() > 0) {
            int totalCount = 0;
            for (ChatBean bean : chatBeans) {
                totalCount += bean.imCount;
            }
            if (getActivity() != null) {
                ((MainActivity) getActivity()).setIMCount(totalCount, SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()),
                        SharedPre.QY_SERVICE_UNREADCOUNT, 0));
                MLog.e("totalCount = " + totalCount);
            }
        }
    }


    private void syncChatData() {
        removeRepeatChatBean();
        queryLocalRecentList();
        saveLettersToLocal();
    }

    Handler handler = new Handler();

    private void queryLocalRecentList() {
        // 查询最近联系人列表数据
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, final List<RecentContact> recents, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            return;
                        }
                        if (adapter != null) {
                            adapter.syncUpdate(recents);
                            adapter.notifyDataSetChanged();
                            if (recyclerView != null) {
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                            computeTotalUnreadCount(adapter.getDatas());
                        }
                    }
                });
            }
        },0);
    }


    private void deleteNimRecent(final ChatBean chatBean, final int position) {
        if (adapter != null) {
            adapter.removeDatas(position);
            if (recyclerView != null && recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyItemRemoved(position);
                computeTotalUnreadCount(adapter.getDatas());
            }
        }
        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
            @Override
            public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                if (code != ResponseCode.RES_SUCCESS || recents == null) {
                    return;
                }
                for (RecentContact recentContact : recents) {
                    if (recentContact.getContactId().toLowerCase().equals(chatBean.nTargetId.toLowerCase())) {
                        NIMClient.getService(MsgService.class).deleteRecentContact(recentContact);
                    }
                }
            }
        });
    }


    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeRecentContact(messageObserver, register);
    }


    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(final List<RecentContact> messages) {
            if (messages != null && adapter != null) {
                List<String> newContacts = adapter.syncNewMsgUpdate(messages);
                if (recyclerView != null && recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                    computeTotalUnreadCount(adapter.getDatas());
                }
                if (newContacts!=null && newContacts.size()>0) {
                    for(int i=0;i<newContacts.size();i++){
                        RequestChatOrderDetail requestChatOrderDetail = new RequestChatOrderDetail(MyApplication.getAppContext(),newContacts.get(i));
                        HttpRequestUtils.request(getContext(), requestChatOrderDetail, new HttpRequestListener() {
                            @Override
                            public void onDataRequestSucceed(BaseRequest request) {
                                ChatBean chatBean = (ChatBean) request.getData();
                                if(messages!=null && messages.size()>0){
                                    for(RecentContact recentContact:messages){
                                        if(recentContact.getContactId().toLowerCase().equals(chatBean.nTargetId.toLowerCase())){
                                            chatBean.message = recentContact.getContent();
                                            chatBean.imCount = recentContact.getUnreadCount();
                                            chatBean.timeStamp = recentContact.getTime();
                                            break;
                                        }
                                    }
                                }
                                if(adapter!=null && adapter.getDatas()!=null){
                                    adapter.getDatas().add(chatBean);
                                    NimRecentListSyncUtils.sortRecentContacts(adapter.getDatas());
                                    computeTotalUnreadCount(adapter.getDatas());
                                    adapter.notifyDataSetChanged();
                                    if(recyclerView!=null && recyclerView.getAdapter()!=null){
                                        recyclerView.getAdapter().notifyDataSetChanged();
                                    }
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
    };

    private void saveLettersToLocal() {
        if (adapter != null && adapter.getDatas() != null && adapter.getDatas().size() > 10) {
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


    // 添加未读数变化监听，add 为 true 是添加，为 false 是撤销监听。退出界面时，必须撤销，以免造成资源泄露
    private UnreadCountChangeListener listener = new UnreadCountChangeListener() { // 声明一个成员变量
        @Override
        public void onUnreadCountChange(int count) {
            if (count > 0) {
                SharedPre.setInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT, count);
                if (adapter != null) {
                    if (recyclerView != null && recyclerView.getAdapter() != null) {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        computeTotalUnreadCount(adapter.getDatas());
                    }
                }
            }
        }
    };

    private UserInfoObservable.UserInfoObserver userInfoObserver;
    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                }
            };
        }
        UserInfoHelper.registerObserver(userInfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            UserInfoHelper.unregisterObserver(userInfoObserver);
        }
    }

    private void removeRepeatChatBean() {
        if (adapter != null) {
            adapter.syncRemoveRepeatData(Page.DEFAULT_PAGESIZE);
        }
    }

}
