package com.hugboga.custom.action;


import android.support.v4.util.ArrayMap;

import com.hugboga.custom.action.constants.ActionPageType;
import com.hugboga.custom.action.vcid.ActionPageActivityList;
import com.hugboga.custom.action.vcid.ActionPageCharteredCar;
import com.hugboga.custom.action.vcid.ActionPageChatList;
import com.hugboga.custom.action.vcid.ActionPageCollectGuide;
import com.hugboga.custom.action.vcid.ActionPageCoupon;
import com.hugboga.custom.action.vcid.ActionPageGuideDetail;
import com.hugboga.custom.action.vcid.ActionPageHome;
import com.hugboga.custom.action.vcid.ActionPageImChat;
import com.hugboga.custom.action.vcid.ActionPageInsure;
import com.hugboga.custom.action.vcid.ActionPageInviteBill;
import com.hugboga.custom.action.vcid.ActionPageOrderDetail;
import com.hugboga.custom.action.vcid.ActionPageSearch;
import com.hugboga.custom.action.vcid.ActionPageSend;
import com.hugboga.custom.action.vcid.ActionPageServicerCenter;
import com.hugboga.custom.action.vcid.ActionPageSingle;
import com.hugboga.custom.action.vcid.ActionPageSkuDetail;
import com.hugboga.custom.action.vcid.ActionPageSkuList;
import com.hugboga.custom.action.vcid.ActionPageTravelFund;
import com.hugboga.custom.action.vcid.ActionPageTravelList;
import com.hugboga.custom.action.vcid.ActionPageUsedBill;
import com.hugboga.custom.action.vcid.ActionPageUserInfo;
import com.hugboga.custom.action.vcid.ActionPageWeb;
import com.hugboga.custom.action.vcid.ActionPagePick;

/**
 * Created by qingcha on 16/8/13.
 */
public class ActionMapping {

    public static ArrayMap getPageMap() {
        ArrayMap<Integer, Class> arrayMap = new ArrayMap();
        arrayMap.put(ActionPageType.WEBVIEW, ActionPageWeb.class);                            // 1：通用webView
        arrayMap.put(ActionPageType.HOME, ActionPageHome.class);                              // 2：首页
        arrayMap.put(ActionPageType.SEARCH, ActionPageSearch.class);                          // 3：搜索页
        arrayMap.put(ActionPageType.SKU_LIST, ActionPageSkuList.class);                       // 4：商品列表页（type：组合、国家、城市，areaId, areaName）
        arrayMap.put(ActionPageType.SKU_DETAIL, ActionPageSkuDetail.class);                   // 5：商品详情页（cityId, goodsNo）
        arrayMap.put(ActionPageType.CHARTERED_CAR, ActionPageCharteredCar.class);             // 6：自定义包车游
        arrayMap.put(ActionPageType.PICK_UP, ActionPagePick.class);                           // 7：中文接机
        arrayMap.put(ActionPageType.SEND, ActionPageSend.class);                              // 8：中文送机
        arrayMap.put(ActionPageType.SINGLE, ActionPageSingle.class);                          // 9：单次接送
        arrayMap.put(ActionPageType.IM_CHAT, ActionPageImChat.class);                         // 10：聊天页面（登录、id、name、avatar、type）
        arrayMap.put(ActionPageType.ORDER_LIST, ActionPageTravelList.class);                  // 11：订单列表（type:进行中、已完成、已取消）
        arrayMap.put(ActionPageType.ORDER_DETAIL, ActionPageOrderDetail.class);               // 12：订单详情（登录、orderNo，orderType）
        arrayMap.put(ActionPageType.USER_INFO, ActionPageUserInfo.class);                     // 14：个人资料（登录）
        arrayMap.put(ActionPageType.COUPON, ActionPageCoupon.class);                          // 15：优惠券列表（登录）
        arrayMap.put(ActionPageType.TRAVEL_FUND, ActionPageTravelFund.class);                 // 16：旅游基金（登录）
        arrayMap.put(ActionPageType.INSURE, ActionPageInsure.class);                          // 18：常用投保人（登录）
        arrayMap.put(ActionPageType.COLLECT_GUIDE, ActionPageCollectGuide.class);             // 19：收藏的司导（登录）
        arrayMap.put(ActionPageType.GUIDE_DETAIL, ActionPageGuideDetail.class);               // 20：司导详情页（登录、司导ID）
        arrayMap.put(ActionPageType.ACTIVITY_LIST, ActionPageActivityList.class);             // 21：活动列表
        arrayMap.put(ActionPageType.SERVICER_CENTER, ActionPageServicerCenter.class);         // 22：服务规则
        arrayMap.put(ActionPageType.CHAT_LIST, ActionPageChatList.class);                     // 30：私聊列表
        arrayMap.put(ActionPageType.TRAVEL_FUND_USED_BILL, ActionPageUsedBill.class);         // 31：旅游基金使用明细
        arrayMap.put(ActionPageType.INVITE_BILL, ActionPageInviteBill.class);                 // 32：邀请记录（登录）
        return arrayMap;
    }
}
