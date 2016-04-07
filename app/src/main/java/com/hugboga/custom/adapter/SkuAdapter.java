package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.adapter.ZHeadFootAdapter;
import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.SkuVH;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.fragment.FgSkuList;
import com.hugboga.custom.utils.ImageUtils;

import org.xutils.x;

/**
 * Created by admin on 2016/3/3.
 */
public class SkuAdapter extends ZHeadFootAdapter<SkuItemBean, SkuVH> {


    private Context context;
    private FgSkuList fragment;
    private View headerView;

    public SkuAdapter(FgSkuList fragment,View headerView) {
        super(fragment.getActivity());
        this.context = fragment.getActivity();
        this.fragment = fragment;
        this.headerView = headerView;
    }

    @Override
    protected View getHeaderView() {
        return headerView;
    }

    @Override
    protected View getFooterView() {
        return new View(context);
    }

    @Override
    protected int initResource() {
        return R.layout.item_city_sku;
    }

    @Override
    protected ZBaseViewHolder getVH(View view) {
        return new SkuVH(view);
    }

    @Override
    protected void getView(int position, SkuVH vh) {
        SkuItemBean bean = datas.get(position);

//        if(position == 0){
//            viewHolder.top_line.setVisibility(View.GONE);
//        }else{
//            viewHolder.top_line.setVisibility(View.VISIBLE);
//        }
        if (bean != null) {
            vh.tvTitle.setText(bean.goodsName);
            vh.tvLabel.setText(bean.keyWords);
            vh.tvGuide.setText(context.getString(R.string.sku_item_guide_number, bean.guideAmount));
            vh.tvAmount.setText(bean.goodsMinPrice);
            vh.tvSale.setText(context.getString(R.string.sku_sale_number, bean.saleAmount));
            vh.tvGuide.setVisibility(bean.guideAmount == 0 ? View.INVISIBLE : View.VISIBLE);
            vh.tvSale.setVisibility(bean.saleAmount == 0 ? View.INVISIBLE : View.VISIBLE);

            vh.imgBg.setLayoutParams(new RelativeLayout.LayoutParams(ImageUtils.getScreenWidth(context), ImageUtils.getResizeHeight(context, 750, 300)));
            x.image().bind(vh.imgBg, bean.goodsPicture);
        }
    }

}
