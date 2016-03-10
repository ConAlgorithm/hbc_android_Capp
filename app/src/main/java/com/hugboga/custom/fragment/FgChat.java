package com.hugboga.custom.fragment;

import android.view.View;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.parser.ParserChatList;
import com.hugboga.custom.data.request.RequestChatList;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 聊天页面
 * Created by admin on 2016/3/8.
 */

@ContentView(R.layout.fg_chat)
public class FgChat extends BaseFragment {

    @ViewInject(R.id.chat_logout)
    private View EmptyLayout;

    @ViewInject(R.id.chat_list)
    private View chatList;



    @Override
    protected void initHeader() {
        fgTitle.setText("私聊");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        if(UserEntity.getUser().getUserToken(getActivity())==null){
            needHttpRequest = true;
            return null;
        }
        RequestChatList parserChatList = new RequestChatList(getActivity(),0,20);
        return  requestData(parserChatList);
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if(request instanceof RequestChatList){
            RequestChatList requestChatList = (RequestChatList)request;
            requestChatList.getData();
        }
    }
    @Event({R.id.login_btn})
    private void onClickView(View view){
        switch (R.id.login_btn){
            case R.id.login_btn:
                startFragment(new FgLogin());
                break;
        }
    }

}
