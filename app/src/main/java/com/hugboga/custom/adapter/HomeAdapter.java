package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.adapter.ZHeadFootAdapter;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.HomeVH;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.utils.ImageUtils;

import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * 首页信息adapter
 * Created by admin on 2016/3/2.
 */
public class HomeAdapter extends ZHeadFootAdapter<HomeBean, HomeVH> {

    private  View header;
    FgHome fgHome;
//    private final ImageOptions options;

    public HomeAdapter(Context context, FgHome fgHome,View header) {
        super(context);
        this.fgHome = fgHome;
        this.header = header;
    }

    @Override
    protected View getHeaderView() {
        return header;
    }

    @Override
    protected View getFooterView() {
        return new View(context);
    }

    @Override
    protected int initResource() {
        return R.layout.item_home;
    }

    @Override
    protected ZBaseViewHolder getVH(View view) {
        return new HomeVH(view);
    }

    @Override
    protected void getView(int position, HomeVH vh) {
        HomeBean homeBean = datas.get(position);
        if (homeBean != null) {
            if (!TextUtils.isEmpty(homeBean.mainTitle)) {
                vh.tvTitle.setText(homeBean.mainTitle);
            } else {
                vh.tvTitle.setText("");
            }
            if (!TextUtils.isEmpty(homeBean.subTitle)) {
                vh.tvSubTitle.setText(homeBean.subTitle);
            } else {
                vh.tvSubTitle.setText("");
            }
            if (!TextUtils.isEmpty(homeBean.mainTitle) && !TextUtils.isEmpty(homeBean.subTitle)) {
                vh.splitLine.setVisibility(View.VISIBLE);
            } else {
                vh.splitLine.setVisibility(View.GONE);
            }
            vh.imgBg.setBackgroundResource(R.mipmap.img_undertext);
            vh.imgBg.setLayoutParams(new RelativeLayout.LayoutParams(ImageUtils.getScreenWidth(context),ImageUtils.getResizeHeight(context,720,334)));

            x.image().bind(vh.imgBg, homeBean.picture);
        }
    }

}
