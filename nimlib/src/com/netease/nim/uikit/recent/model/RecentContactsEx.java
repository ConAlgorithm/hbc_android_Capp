package com.netease.nim.uikit.recent.model;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/24.
 */
public class RecentContactsEx implements RecentContact{


    @Override
    public MsgAttachment getAttachment() {
        return null;
    }

    @Override
    public String getContactId() {
        return null;
    }

    @Override
    public String getFromAccount() {
        return null;
    }

    @Override
    public String getFromNick() {
        return null;
    }

    @Override
    public SessionTypeEnum getSessionType() {
        return null;
    }

    @Override
    public String getRecentMessageId() {
        return null;
    }

    @Override
    public MsgTypeEnum getMsgType() {
        return null;
    }

    @Override
    public MsgStatusEnum getMsgStatus() {
        return null;
    }

    @Override
    public void setMsgStatus(MsgStatusEnum msgStatusEnum) {

    }

    @Override
    public int getUnreadCount() {
        return 0;
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public long getTime() {
        return 0;
    }

    @Override
    public void setTag(long l) {

    }

    @Override
    public long getTag() {
        return 0;
    }

    @Override
    public Map<String, Object> getExtension() {
        return null;
    }

    @Override
    public void setExtension(Map<String, Object> map) {

    }


}
