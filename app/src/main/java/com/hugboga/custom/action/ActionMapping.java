package com.hugboga.custom.action;

import com.hugboga.custom.action.constants.ActionPageType;
import com.hugboga.custom.action.vcid.ActionPageActivityList;
import com.hugboga.custom.action.vcid.ActionPageCoupon;
import com.hugboga.custom.action.vcid.ActionPageHome;
import com.hugboga.custom.action.vcid.ActionPageSearch;
import com.hugboga.custom.action.vcid.ActionPageTravelFund;
import com.hugboga.custom.action.vcid.ActionPageUsedBill;
import com.hugboga.custom.action.vcid.ActionPageWeb;
import com.hugboga.custom.utils.CommonUtils;

/**
 * Created by qingcha on 16/8/13.
 */
public class ActionMapping {

    public static ActionPageBase getActionPage(String type) {
        ActionPageBase result = null;
        switch (CommonUtils.getCountInteger(type)) {
            case ActionPageType.WEBVIEW:// 1：通用webView TODO
                result = new ActionPageWeb();
                break;
            case ActionPageType.HOME:// 2：首页
                result = new ActionPageHome();
                break;
            case ActionPageType.SEARCH:// 3：搜索页
                result = new ActionPageSearch();
                break;
            case ActionPageType.SKU_LIST:// 4：商品列表页（type：组合、国家、城市，areaId, areaName）
                break;
            case ActionPageType.SKU_DETAIL:// 5：商品详情页（cityId, goodsNo）
                break;
            case ActionPageType.CHARTERED_CAR:// 6：自定义包车游
                break;
            case ActionPageType.PICK_UP:// 7：中文接机
                break;
            case ActionPageType.SEND:// 8：中文送机
                break;
            case ActionPageType.SINGLE:// 9：单次接送
                break;
            case ActionPageType.IM_CHAT:// 10：聊天页面（登录、id、name、avatar、type）
                break;
            case ActionPageType.ORDER_LIST:// 11：订单列表（type:进行中、已完成、已取消）
                break;
            case ActionPageType.ORDER_DETAIL:// 12：订单详情（登录、orderNo，orderType）
                break;
            case ActionPageType.USER_INFO:// 14：个人资料（登录）
                break;
            case ActionPageType.COUPON:// 15：优惠券列表（登录）TODO
                result = new ActionPageCoupon();
                break;
            case ActionPageType.TRAVEL_FUND:// 16：旅游基金（登录）TODO
                result = new ActionPageTravelFund();
                break;
            case ActionPageType.INSURE:// 18：常用投保人（登录）
                break;
            case ActionPageType.COLLECT_GUIDE:// 19：收藏的司导（登录）
                break;
            case ActionPageType.GUIDE_DETAIL:// 20：司导详情页（登录、司导ID）
                break;
            case ActionPageType.ACTIVITY_LIST:// 21：活动列表 TODO
                result = new ActionPageActivityList();
                break;
            case ActionPageType.SERVICER_CENTER:// 22：服务规则
                break;
            case ActionPageType.CHAt_LIST:// 30：私聊列表
                break;
            case ActionPageType.TRAVEL_FUND_USED_BILL:// 31：旅游基金使用明细 TODO
                result = new ActionPageUsedBill();
                break;
            case ActionPageType.INVITE_BILL:// 32：邀请记录（登录）
                break;

        }
        return result;
    }
}
