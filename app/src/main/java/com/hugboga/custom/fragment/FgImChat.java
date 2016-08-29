package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.recycler.ZListPageView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.NIMChatActivity;
import com.hugboga.custom.adapter.ChatAdapter;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.data.request.RequestChatList;
import com.hugboga.custom.data.request.RequestNIMChatList;
import com.hugboga.custom.data.request.RequestNIMRemoveChat;
import com.hugboga.custom.data.request.RequestRemoveChat;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.UnicornUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/8/24.
 */
@ContentView(R.layout.fg_chat)
public class FgImChat extends BaseFragment implements View.OnClickListener, ZBaseAdapter.OnItemClickListener, ZListPageView.NoticeViewTask {

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

    @ViewInject(R.id.im_statusview)
    TextView imStatusView;

    @ViewInject(R.id.login_btn)
    TextView loginBtn;


    private ChatAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        if (UserEntity.getUser().isLogin(getActivity()) && recyclerView != null && !recyclerView.isLoading() && adapter != null && adapter.getItemCount() <= 0) {
            loadData();
        }
    }

    @Override
    protected void initHeader() {
        fgTitle.setText("私聊");
        leftBtn.setImageResource(R.mipmap.header_menu);
        leftBtn.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        MLog.e(this + " initView");
        initListView();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        registerObservers(true);
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
        recyclerView.setOnItemLongClickListener(new ZBaseAdapter.OnItemLongClickListener(){
            @Override
            public void onItemLongClick(View view, final int position) {
                if(position != 0){
                    final ChatBean chatBean = adapter.getDatas().get(position);
                    AlertDialogUtils.showAlertDialog(getActivity(), getString(R.string.del_chat), "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestNIMRemoveChat requestRemoveChat = new RequestNIMRemoveChat(getActivity(),chatBean.targetId);
                            HttpRequestUtils.request(getContext(), requestRemoveChat, new HttpRequestListener() {
                                @Override
                                public void onDataRequestSucceed(BaseRequest request) {
                                    deleteNimRecent(chatBean,position);
                                    //adapter.removeDatas(position);
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
                    },new DialogInterface.OnClickListener() {
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
            adapter.notifyDataSetChanged();
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

    @Event({R.id.login_btn, R.id.header_left_btn, R.id.chat_list_empty_tv})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                intent.putExtra("source",getEventSource());
                startActivity(intent);
                break;
            case R.id.header_left_btn:
                MLog.e("left  " + view);
                ((MainActivity) getActivity()).openDrawer();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                ((MainActivity) getActivity()).openDrawer();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        registerObservers(false);
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
            case CLICK_USER_LOGIN:
            case REFRESH_CHAT_LIST:
                RequestNIMChatList parserChatList = new RequestNIMChatList(getActivity());
                recyclerView.setRequestData(parserChatList);
                requestData();
                break;
            case CLICK_USER_LOOUT:
                chatLayout.setVisibility(View.GONE);
                //清理列表数据
                if(adapter.getDatas()!=null)
                    adapter.getDatas().clear();
                adapter.notifyDataSetChanged();
                emptyLayout.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).setIMCount(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ChatBean chatBean = adapter.getDatas().get(position);
        if (chatBean.targetType==3) {
            //String titleJson = getChatInfo(chatBean.targetId, chatBean.targetAvatar, chatBean.targetName, chatBean.targetType,chatBean.inBlack);
            //RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.APP_PUBLIC_SERVICE, chatBean.targetId, titleJson);
            //Toast.makeText(getActivity(),"启动7鱼客服",Toast.LENGTH_SHORT).show();
            UnicornUtils.openServiceActivity();

        } else if (chatBean.targetType==1) {
            String titleJson = getChatInfo(chatBean.targetId, chatBean.targetAvatar, chatBean.targetName, chatBean.targetType+"",chatBean.inBlack,chatBean.nTargetId);
            //RongIM.getInstance().startPrivateChat(getActivity(), chatBean.targetId, titleJson);
            NIMChatActivity.start(getContext(),chatBean.nTargetId,null,titleJson);
        } else {
            MLog.e("目标用户不是客服，也不是司导");
        }
    }

    private String getChatInfo(String userId, String userAvatar, String title, String targetType,int inBlack,String imUserid) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.isChat = true;
        chatInfo.userId = userId;
        chatInfo.userAvatar = userAvatar;
        chatInfo.title = title;
        chatInfo.targetType = targetType;
        chatInfo.inBlack = inBlack;
        chatInfo.imUserId = imUserid;
        return new ParserChatInfo().toJsonString(chatInfo);
    }

    @Override
    public void notice(Object object) {
        List<ChatBean> chatBeans = adapter.getDatas();
        computeTotalUnreadCount(chatBeans);
        emptyTV.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        if(UserEntity.getUser().isLogin(MyApplication.getAppContext())){
            if(loginBtn!=null)
            loginBtn.setVisibility(View.GONE);
        }else{
            if(loginBtn!=null)
                loginBtn.setVisibility(View.VISIBLE);
        }
        queryLocalRecentList();
    }


    private void computeTotalUnreadCount( List<ChatBean> chatBeans){
        if (chatBeans != null && chatBeans.size() > 0) {
            int totalCount = 0;
            for (ChatBean bean : chatBeans) {
                totalCount += bean.imCount;
            }
            if(getActivity()!=null){
                ((MainActivity) getActivity()).setIMCount(totalCount);
                MLog.e("totalCount = " + totalCount);
            }
        }
    }


    @Override
    public void error(ExceptionInfo errorInfo, BaseRequest request) {
        if (UserEntity.getUser().isLogin(getActivity())) {
            emptyTV.setVisibility(View.VISIBLE);
        }
    }


    Handler handler = new Handler();
    private void queryLocalRecentList(){
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
                        if(adapter!=null){
                            adapter.syncUpdate(recents);
                            if(recyclerView!=null){
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                            computeTotalUnreadCount(adapter.getDatas());
                        }
                    }
                });
            }
        },200);
    }


    private void deleteNimRecent(final ChatBean chatBean,final int position){
        if(adapter!=null){
            adapter.removeDatas(position);
            if(recyclerView!=null&& recyclerView.getAdapter()!=null){
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
                for (RecentContact recentContact:recents){
                    if(recentContact.getContactId().toLowerCase().equals(chatBean.nTargetId.toLowerCase())){
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

        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeRecentContact(messageObserver, register);
//        service.observeMsgStatus(statusObserver, register);
//        service.observeRecentContactDeleted(deleteObserver, register);
//        if (register) {
//            registerUserInfoObserver();
//        } else {
//            unregisterUserInfoObserver();
//        }
    }


    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> messages) {
            if(messages!=null && adapter!= null){
                adapter.syncNewMsgUpdate(messages);
                if(recyclerView!=null && recyclerView.getAdapter()!=null){
                    recyclerView.getAdapter().notifyDataSetChanged();
                    computeTotalUnreadCount(adapter.getDatas());
                }
            }
        }
    };

//    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
//        @Override
//        public void onEvent(IMMessage message) {
//            Log.i("test","onEvent message status:" + message.getStatus().toString());
//        }
//    };

//    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
//        @Override
//        public void onEvent(RecentContact recentContact) {
//            if(adapter!=null){
//                adapter.syncDeleteItemUpdate(recentContact);
//                if(recyclerView!=null){
//                    recentContact.
//                }
//            }
//        }
//    };


//    private UserInfoObservable.UserInfoObserver userInfoObserver;
//    private void registerUserInfoObserver() {
//        if (userInfoObserver == null) {
//            userInfoObserver = new UserInfoObservable.UserInfoObserver() {
//                @Override
//                public void onUserInfoChanged(List<String> accounts) {
//                    Log.i("test","accounts size:" + accounts);
//                }
//            };
//        }
//        UserInfoHelper.registerObserver(userInfoObserver);
//    }

//    private void unregisterUserInfoObserver() {
//        if (userInfoObserver != null) {
//            UserInfoHelper.unregisterObserver(userInfoObserver);
//        }
//    }


    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode code) {
            if(!UserEntity.getUser().isLogin(MyApplication.getAppContext())){
                return;
            }
            if (code.wontAutoLogin()) {
                return;
            } else {
                if(imStatusView==null){
                    return;
                }
                if (code == StatusCode.NET_BROKEN) {
                    imStatusView.setVisibility(View.VISIBLE);
                    imStatusView.setText(R.string.net_broken);
                } else if (code == StatusCode.UNLOGIN) {
                    imStatusView.setVisibility(View.VISIBLE);
                    imStatusView.setText(R.string.nim_status_unlogin);
                } else if (code == StatusCode.CONNECTING) {
                    imStatusView.setVisibility(View.VISIBLE);
                    imStatusView.setText(R.string.nim_status_connecting);
                } else if (code == StatusCode.LOGINING) {
                    imStatusView.setVisibility(View.VISIBLE);
                    imStatusView.setText(R.string.nim_status_logining);
                } else if(code == StatusCode.LOGINED) {
                    imStatusView.setVisibility(View.GONE);
                    if(adapter!=null && adapter.getDatas()!=null){
                        queryLocalRecentList();
                    }
                }
            }
        }
    };


//    private void test(final RecentContact recentContact){
//        fgTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String orderJson = "{\"isChat\":true,\"userId\":\"221450316914\",\"userAv" +
//                        "atar\":\"http:\\/\\/fr.test.hbc.tech\\/guide\\/20160721\\/f_201607" +
//                        "211450254772.jpg\",\"title\":\"宋朋旺\",\"targetType\":\"1\",\"inB" +
//                        "lack\":0,\"isHideMoreBtn\":0}";
//                NIMChatActivity.start(getContext(),recentContact.getContactId(),null,orderJson);
//            }
//        });
//    }

}
