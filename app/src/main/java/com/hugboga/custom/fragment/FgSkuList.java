package com.hugboga.custom.fragment;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huangbaoche.hbcframe.data.net.DefaultImageCallback;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.SkuAdapter;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.SkuCityBean;
import com.hugboga.custom.data.bean.SkuItemBean;
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
public class FgSkuList extends  BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String KEY_CITY_ID = "KEY_CITY_ID";

    @ViewInject(android.R.id.list)
    ListView listView;

    @ViewInject(R.id.fg_sku_list_layout)
    LinearLayout listViewLayout;
    @ViewInject(R.id.sku_list_empty)
    View listViewEmpty;


    View skuSubtitle;


    protected String mCityId;
    private CityBean mCityBean;

    private SkuAdapter adapter;
    private View headerBg;
    private SkuCityBean skuCityBean;
    private View ListHeader;


    @Override
    protected void initHeader() {
        fgTitle.setText("测试");
        mCityId = getArguments().getString(KEY_CITY_ID);
        ListHeader = LayoutInflater.from(getActivity()).inflate(R.layout.fg_sku_header, null);
        headerBg = ListHeader.findViewById(R.id.home_menu_layout);

        skuSubtitle = ListHeader.findViewById(R.id.sku_subtitle);
        listView.addHeaderView(ListHeader);
        listView.setOnItemClickListener(this);
        listView.setEmptyView(listViewEmpty);
        mCityBean = findCityById(mCityId);
        initListHeader();
    }

    private void initListHeader() {
        View menu1 = ListHeader.findViewById(R.id.fg_home_menu1);
        View menu2 = ListHeader.findViewById(R.id.fg_home_menu2);
        View menu3 = ListHeader.findViewById(R.id.fg_home_menu3);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        if(mCityBean!=null){
            menu1.setVisibility(mCityBean.isCityCode?View.VISIBLE:View.GONE);
            menu2.setVisibility(mCityBean.isDaily?View.VISIBLE:View.GONE);
            menu3.setVisibility(mCityBean.isSingle?View.VISIBLE:View.GONE);
        }

    }

    @Override
    protected void initView() {
        adapter = new SkuAdapter(getActivity());
        listView.setAdapter(adapter);
    }

    @Override
    protected Callback.Cancelable requestData() {
        RequestSkuList requestSkuList = new RequestSkuList(getActivity(),mCityId);
        return requestData(requestSkuList);
    }


    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if(request instanceof RequestSkuList){
            RequestSkuList requestSkuList = (RequestSkuList)request;
            skuCityBean = requestSkuList.getData();
            inflateContent();
        }
    }

    @Override
    protected void inflateContent() {
        adapter.setList(skuCityBean.goodsList);
        ImageOptions options = new ImageOptions.Builder().setFailureDrawableId(R.mipmap.img_undertext).build();
        if(skuCityBean.goodsList.size()==0){
            MLog.e("skuCityBean.goodsList.size"+skuCityBean.goodsList.size());
            x.image().loadDrawable(skuCityBean.cityPicture, options, new DefaultImageCallback<Drawable>() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onSuccess(Drawable result) {
                    listView.setBackground(null);
                    skuSubtitle.setVisibility(View.GONE);
                    headerBg.setBackground(null);
                    listViewLayout.setBackground(result);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MLog.e("position = " + position);
        if(position==0||mCityBean==null)return;
        SkuItemBean bean = adapter.getItem(position-1);
        Bundle bundle = new Bundle();
//        String url = "http://res.test.hbc.tech/h5/csku/skuDetail.html?source=c&goodsNo="+bean.goodsNo;
//        url = "http://res.dev.hbc.tech/h5/test/api.html?";
        bundle.putString(FgWebInfo.WEB_URL, bean.skuDetailUrl);
        bundle.putSerializable(FgSkuDetail.WEB_SKU, bean);
        bundle.putSerializable(FgSkuDetail.WEB_CITY, mCityBean);
        startFragment(new FgSkuDetail(),bundle);
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
        MLog.e("cityBean"+cityBean.name+ cityBean.location);
        else
        MLog.e("citybean is null");
        return cityBean;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fg_home_menu1://中文接送机
                startFragment(new FgTransfer());
                break;
            case R.id.fg_home_menu2://按天包车
                startFragment(new FgDaily());
                break;
            case R.id.fg_home_menu3://单次接送
                startFragment(new FgSingle());
                break;
        }
    }
}
