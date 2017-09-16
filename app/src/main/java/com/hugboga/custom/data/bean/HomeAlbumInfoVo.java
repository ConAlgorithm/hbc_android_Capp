package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17/8/3.
 */

public class HomeAlbumInfoVo implements Serializable {
    public int albumType;//	1，商品；2，司导
    public String  albumId	= "";//	专辑ID
    public String albumName = "";//		专辑名称
    public String albumLinkUrl = "";//		专辑跳转URL
    public int albumHotLevel	= 0;//	专辑热度
    public String albumImageUrl	= "";//	图片
    public int albumPurchases = 0;// 专辑商品已售卖数量（专辑为1，商品时使用）
    public int albumOrders = 0;// 专辑司导已完成订单数量（专辑为2，司导时使用）
    public ArrayList<HomeAlbumRelItemVo> albumRelItems	= null;//	专辑商品
}
