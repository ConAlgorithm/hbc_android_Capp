package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.adapter.ZHeadFootAdapter;
import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.SkuVH;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.fragment.FgSkuList;
import com.hugboga.custom.utils.ImageUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.SkuListItem;

import org.xutils.x;

/**
 * Created by admin on 2016/3/3.
 */
public class SkuAdapter extends HbcRecyclerBaseAdapter<SkuItemBean> {

    private FgSkuList fragment;

    public SkuAdapter(Context context, FgSkuList _fragment) {
        super(context);
        this.fragment = _fragment;
        LinearLayout header = new LinearLayout(context);
        header.setBackgroundColor(0x77FF0000);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(200));
        header.setLayoutParams(params);
        addHeaderView(header);

        LinearLayout header2 = new LinearLayout(context);
        header2.setBackgroundColor(0x770000FF);
        header2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(200)));
        addFooterView(header2);
    }

    @Override
    protected View getItemView(int position) {
        return new SkuListItem(getContext());
    }


//    private Context context;
//    private FgSkuList fragment;
//    private View headerView;
//
//    public SkuAdapter(FgSkuList fragment,View headerView) {
//        super(fragment.getActivity());
//        this.context = fragment.getActivity();
//        this.fragment = fragment;
//        this.headerView = headerView;
//    }
//
//    @Override
//    protected View getHeaderView() {
//        return headerView;
//    }
//
//    @Override
//    protected View getFooterView() {
//        return new View(context);
//    }
//
//    @Override
//    protected int initResource() {
//        return R.layout.item_city_sku;
//    }
//
//    @Override
//    protected ZBaseViewHolder getVH(View view) {
//        return new SkuVH(view);
//    }
//
//    @Override
//    protected void getView(int position, SkuVH vh) {
//        SkuItemBean bean = datas.get(position);
//
////        if(position == 0){
////            viewHolder.top_line.setVisibility(View.GONE);
////        }else{
////            viewHolder.top_line.setVisibility(View.VISIBLE);
////        }
//        if (bean != null) {
//            vh.tvTitle.setText(bean.goodsName);
//            vh.tvLabel.setText(bean.keyWords);
//            vh.tvGuide.setText(context.getString(R.string.sku_item_guide_number, bean.guideAmount));
//            vh.tvAmount.setText(bean.goodsMinPrice);
//            vh.tvSale.setText(context.getString(R.string.sku_sale_number, bean.saleAmount));
//            vh.tvGuide.setVisibility(bean.guideAmount == 0 ? View.INVISIBLE : View.VISIBLE);
//            vh.tvSale.setVisibility(bean.saleAmount == 0 ? View.INVISIBLE : View.VISIBLE);
//
//            vh.imgBg.setLayoutParams(new RelativeLayout.LayoutParams(ImageUtils.getScreenWidth(context),ImageUtils.getResizeHeight(context,750,300)));
//            vh.imgBg.setBackgroundResource(R.mipmap.img_notext);
//            vh.imgBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//            x.image().bind(vh.imgBg, bean.goodsPicture);
//        }
//    }

}
