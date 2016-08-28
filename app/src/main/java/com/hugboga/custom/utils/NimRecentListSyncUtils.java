package com.hugboga.custom.utils;

import android.text.TextUtils;

import com.hugboga.custom.data.bean.ChatBean;
import com.netease.nimlib.sdk.msg.model.RecentContact;

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
     * @param chatBeanList
     * @param recentContacts
     * @return
     */
    public static void recentListSync(List<ChatBean> chatBeanList, List<RecentContact> recentContacts){

        if(recentContacts == null || recentContacts.size()==0){
            return ;
        }

        for(Iterator<ChatBean> iter = chatBeanList.iterator();iter.hasNext();){
            //客服数据排除不同步
            ChatBean chatBean = iter.next();
            if(chatBean.targetType.equals("3")){
                continue;
            }
            boolean exist = false;
            for (RecentContact recentContact:recentContacts){
                if(chatBean.nTargetId.toLowerCase().equals(recentContact.getContactId().toLowerCase())){
                    chatBean.message = recentContact.getContent();
                    chatBean.imCount = recentContact.getUnreadCount();
                    chatBean.timeStamp = recentContact.getTime();
                    exist = true;
                }
            }
            if(!exist){
                iter.remove();
            }
        }
        sortRecentContacts(chatBeanList);
    }


    /**
     * 删除会话与云信数据同步
     * @param chatBeans
     * @param recentContact
     */
    public static int deleteSync(List<ChatBean> chatBeans,RecentContact recentContact){
        if(chatBeans!=null && recentContact!=null){
            for(int i=0;i<chatBeans.size();i++){
                ChatBean chatBean = chatBeans.get(i);
                if(TextUtils.equals(chatBean.nTargetId,recentContact.getContactId().toLowerCase())){
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
     * @param chatBeens
     * @param messages
     */
    public static void updateRecentSync(List<ChatBean> chatBeens,List<RecentContact> messages){
        if(messages==null || messages.size()==0){
            return;
        }
        for (RecentContact recentContact:messages){
            for (ChatBean chatBean:chatBeens){
                if(TextUtils.equals(recentContact.getContactId().toLowerCase(),chatBean.nTargetId)){
                    chatBean.message = recentContact.getContent();
                    chatBean.imCount = recentContact.getUnreadCount();
                    chatBean.timeStamp = recentContact.getTime();
                    break;
                }
            }
        }
        sortRecentContacts(chatBeens);
    }


    /**
     * 服务器会话列表为空，所有会话数据从云信拉取
     * @param chatBeans
     * @param recentContacts
     */
    private static void syncAllRecent(List<ChatBean> chatBeans,List<RecentContact> recentContacts){
        for(RecentContact recentContact:recentContacts){
            ChatBean chatBean = new ChatBean();
            chatBeans.add(chatBean);
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private static void sortRecentContacts(List<ChatBean> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<ChatBean> comp = new Comparator<ChatBean>() {
        @Override
        public int compare(ChatBean o1, ChatBean o2) {
            //先判断是不是客服
            long sticky = o1.userType- o2.userType;
            if (sticky != 0) {
                return sticky > 0 ? -1 : 1;
            } else {
                long time = o1.timeStamp- o2.timeStamp;
                return time == 0 ? 0 : (time > 0 ? -1 : 1);
            }
        }
    };
}
