package com.hugboga.custom.utils;

import android.text.TextUtils;
import android.util.Log;

import com.hugboga.custom.data.bean.ChatBean;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class NimRecentListSyncUtils {

    /**
     * 服务器后台会话列表同云信本地数据列表同步，以云信数据为主
     *
     * @param chatBeanList
     * @param recentContacts
     * @return
     */
    public static void recentListSync(List<ChatBean> chatBeanList, List<RecentContact> recentContacts) {
        if (recentContacts == null || recentContacts.size() == 0) {
            return;
        }
        for (Iterator<ChatBean> iter = chatBeanList.iterator(); iter.hasNext(); ) {
            //客服数据排除不同步
            ChatBean chatBean = iter.next();
            if (chatBean.targetType == 3) {
                continue;
            }
            for (RecentContact recentContact : recentContacts) {
                if (chatBean.nTargetId.toLowerCase().equals(recentContact.getContactId().toLowerCase())) {
                    chatBean.message = recentContact.getContent();
                    if (recentContact.getMsgType() == MsgTypeEnum.tip) {
                        chatBean.imCount = 0;
                    } else {
                        chatBean.imCount = recentContact.getUnreadCount();
                    }
                    chatBean.timeStamp = recentContact.getTime();
                }
            }
        }
        sortRecentContacts(chatBeanList);
    }


    /**
     * 删除会话与云信数据同步
     *
     * @param chatBeans
     * @param recentContact
     */
    public static int deleteSync(List<ChatBean> chatBeans, RecentContact recentContact) {
        if (chatBeans != null && recentContact != null) {
            for (int i = 0; i < chatBeans.size(); i++) {
                ChatBean chatBean = chatBeans.get(i);
                if (TextUtils.equals(chatBean.nTargetId, recentContact.getContactId().toLowerCase())) {
                    chatBeans.remove(chatBean);
                    return i;
                }
            }
        }
        sortRecentContacts(chatBeans);
        return -1;
    }

    /**
     * 云信新消息同步页面更新
     *
     * @param chatBeens
     * @param messages
     */
    public static List<String> updateRecentSync(List<ChatBean> chatBeens, List<RecentContact> messages) {
        if (messages == null || messages.size() == 0) {
            return null;
        }
        //boolean hasNewContact = false;
        List<String> targetIds = null;
        for (RecentContact recentContact : messages) {
            boolean flag = false;
            for (ChatBean chatBean : chatBeens) {
                if (TextUtils.equals(recentContact.getContactId().toLowerCase(), chatBean.nTargetId)) {
                    chatBean.message = recentContact.getContent();
                    if (recentContact.getMsgType() == MsgTypeEnum.tip) {
                        chatBean.imCount = 0;
                    } else {
                        chatBean.imCount = recentContact.getUnreadCount();
                    }
                    chatBean.timeStamp = recentContact.getTime();
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if(targetIds==null){
                    targetIds = new ArrayList<>();
                }
                targetIds.add(recentContact.getContactId().toLowerCase());
            }
        }
        sortRecentContacts(chatBeens);
        return targetIds;
        //sortRecentContacts(chatBeens);
    }


    /**
     * 服务器会话列表为空，所有会话数据从云信拉取
     *
     * @param chatBeans
     * @param recentContacts
     */
    private static void syncAllRecent(List<ChatBean> chatBeans, List<RecentContact> recentContacts) {
        for (RecentContact recentContact : recentContacts) {
            ChatBean chatBean = new ChatBean();
            chatBeans.add(chatBean);
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    public static void sortRecentContacts(List<ChatBean> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<ChatBean> comp = new Comparator<ChatBean>() {
        @Override
        public int compare(ChatBean o1, ChatBean o2) {
            //先判断是不是客服
            long sticky = o1.targetType - o2.targetType;
            if (sticky != 0) {
                return sticky > 0 ? -1 : 1;
            } else {
                long time = o1.timeStamp - o2.timeStamp;
                return time == 0 ? 0 : (time > 0 ? -1 : 1);
            }
        }
    };


    public static void removeRepeatData(List<ChatBean> chatBeanList,int pageSize){
        if(chatBeanList==null || chatBeanList.size()<pageSize){
            return;
        }
        List<ChatBean> lastPageChatList = new ArrayList<>(chatBeanList.subList(chatBeanList.size()-pageSize,chatBeanList.size()));
        boolean hasRepeat = false;
        for(int i=0;i<chatBeanList.size()-pageSize;i++){
            ChatBean chatBean = chatBeanList.get(i);
            for(Iterator<ChatBean> lastIter = lastPageChatList.iterator();lastIter.hasNext();){
                ChatBean temp = lastIter.next();
                if(chatBean.nTargetId.equals(temp.nTargetId)){
                    lastIter.remove();
                    hasRepeat = true;
                }
            }
        }

        if(hasRepeat){
            for(int i=0;i<pageSize;i++){
                chatBeanList.remove(chatBeanList.size()-1);
            }
            chatBeanList.addAll(lastPageChatList);
        }

//        for(int i=0;i<chatBeanList.size();i++){
//            Log.e("test","after repeat id:" + chatBeanList.get(i).nTargetId);
//        }

    }
}
