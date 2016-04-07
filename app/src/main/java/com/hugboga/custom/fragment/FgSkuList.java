package com.hugboga.custom.fragment;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.net.DefaultImageCallback;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.recycler.ZListPageView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.SkuAdapter;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.SkuCityBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.request.RequestHome;
import com.hugboga.custom.data.request.RequestSkuList;
import com.hugboga.custom.utils.DBHelper;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 城市SKU列表
 * Created by admin on 2016/3/3.
 */
@ContentView(R.layout.fg_sku_list)
public class FgSkuList extends  BaseFragment implements  View.OnClickListener, ZBaseAdapter.OnItemClickListener, ZListPageView.NoticeViewTask {

    public static final String KEY_CITY_ID = "KEY_CITY_ID";

    @ViewInject(R.id.listview)
    ZListPageView recyclerView;
    @ViewInject(R.id.swipe)
    ZSwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.fg_sku_list_layout)
    LinearLayout listViewLayout;
    @ViewInject(R.id.sku_list_empty)
    View listViewEmpty;

    @ViewInject(R.id.city_sku_empty_guide_number)
    TextView guideNumber;
    @ViewInject(R.id.sku_list_empty_title_content)
    TextView listViewEmptyContent;


    View skuSubtitle;


    protected String mCityId;
    private CityBean mCityBean;

    private SkuAdapter adapter;
    private View headerBg;
    private SkuCityBean skuCityBean;
    private View listHeader;


    @Override
    protected void initHeader() {
        mCityId = getArguments().getString(KEY_CITY_ID);
        listHeader = LayoutInflater.from(getActivity()).inflate(R.layout.fg_sku_header, null);
        headerBg = listHeader.findViewById(R.id.home_menu_layout);
        skuSubtitle = listHeader.findViewById(R.id.sku_subtitle);
        mCityBean = findCityById(mCityId);
        initListHeader();
    }

    private void initListHeader() {
        View menu1 = listHeader.findViewById(R.id.fg_home_menu1);
        View menu2 = listHeader.findViewById(R.id.fg_home_menu2);
        View menu3 = listHeader.findViewById(R.id.fg_home_menu3);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        if(mCityBean!=null){
            menu1.setVisibility(mCityBean.hasAirport?View.VISIBLE:View.GONE);
            menu2.setVisibility(mCityBean.isDaily?View.VISIBLE:View.GONE);
            menu3.setVisibility(mCityBean.isSingle?View.VISIBLE:View.GONE);
        }
        MLog.e(mCityBean.toString());
    }

    @Override
    protected void initView() {
        adapter = new SkuAdapter(this, listHeader);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnItemClickListener(this);
        recyclerView.setzSwipeRefreshLayout(swipeRefreshLayout);
        RequestSkuList request = new RequestSkuList(getActivity(),mCityId);
        recyclerView.setRequestData(request);
        recyclerView.setNoticeViewTask(this);
    }

    @Override
    protected Callback.Cancelable requestData() {
        if (recyclerView != null) {
            recyclerView.showPageFirst();
        }
        return null;
    }

    @Override
    public void notice(Object object) {
        Object[] obj = (Object[]) object;
        skuCityBean = (SkuCityBean) obj[2];
        inflateContent();
    }

    @Override
    protected void inflateContent() {
        ImageOptions options = new ImageOptions.Builder().setFailureDrawableId(R.mipmap.img_undertext).build();
        fgTitle.setText(skuCityBean.cityName);
        if(skuCityBean.goodsList.size()==0){
            MLog.e("skuCityBean.goodsList.size"+skuCityBean.goodsList.size());
            x.image().loadDrawable(skuCityBean.cityPicture, options, new DefaultImageCallback<Drawable>() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onSuccess(Drawable result) {
                    recyclerView.setBackground(null);
                    skuSubtitle.setVisibility(View.GONE);
                    headerBg.setBackgroundResource(R.drawable.city_sku_bg);
                    listViewLayout.setBackground(result);
                    listViewEmpty.setVisibility(View.VISIBLE);
                    guideNumber.setText(getString(R.string.sku_item_guide_number, skuCityBean.cityGuideAmount));
                    listViewEmptyContent.setText("       "+skuCityBean.cityDesc);
                    MLog.e(" cityPicture result" + result);
                }
            });
        }else{
            MLog.e("skuCityBean.goodsList.size"+skuCityBean.goodsList.size());
            x.image().loadDrawable(skuCityBean.cityHeadPicture, options, new DefaultImageCallback<Drawable>() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onSuccess(Drawable result) {
                    headerBg.setBackground(result);
                    MLog.e("cityHeadPicture result" + result);
                }
            });
        }

    }


    private CityBean findCityById(String cityId){
        CityBean cityBean = null;
        DbManager mDbManager = new DBHelper(getActivity()).getDbManager();
        try {
            cityBean = mDbManager.findById(CityBean.class,cityId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(cityBean!=null)
        MLog.e("cityBean"+cityBean.name+ cityBean.location+" hasAirport="+cityBean.hasAirport);
        else
        MLog.e("citybean is null");
        return cityBean;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FgDaily.KEY_CITY_BEAN,mCityBean);
        switch (v.getId()){
            case R.id.fg_home_menu1://中文接送机
                startFragment(new FgTransfer());
                break;
            case R.id.fg_home_menu2://按天包车
                startFragment(new FgDaily(),bundle);
                break;
            case R.id.fg_home_menu3://单次接送
                startFragment(new FgSingle(),bundle);
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        MLog.e("position = " + position);
        if(mCityBean==null)return;
        SkuItemBean bean = adapter.getDatas().get(position);
        Bundle bundle = new Bundle();
        bundle.putString(FgWebInfo.WEB_URL, bean.skuDetailUrl);
        bundle.putSerializable(FgSkuDetail.WEB_SKU, bean);
        bundle.putSerializable(FgSkuDetail.WEB_CITY, mCityBean);
        startFragment(new FgSkuDetail(),bundle);
    }


}
