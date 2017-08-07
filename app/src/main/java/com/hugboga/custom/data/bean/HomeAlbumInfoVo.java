package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17/8/3.
 */

public class HomeAlbumInfoVo implements Serializable {
    public String  albumId	= "";//	专辑ID
    public String albumName = "";//		专辑名称
    public String albumLinkUrl = "";//		专辑跳转URL
    public int albumHotLevel	= 0;//	专辑热度
    public String albumImageUrl	= "";//	图片
    public int albumPurchases = 0;//		专辑商品已售卖数量
    public ArrayList<HomeAlbumRelGoodsVo> albumRelGoods	= null;//	专辑商品
}
