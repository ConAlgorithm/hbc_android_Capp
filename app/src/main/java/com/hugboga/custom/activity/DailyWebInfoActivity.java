package com.hugboga.custom.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

@ContentView(R.layout.fg_sku_detail)
public class DailyWebInfoActivity extends WebInfoActivity {
    @ViewInject(R.id.goto_order)
    TextView gotoOrder;

    public static final String WEB_SKU = "web_sku";
    public static final String WEB_CITY = "web_city";

    private CityBean cityBean;
    private String goodsNo;

    public boolean isGoodsOut = false;//商品是否已下架
    private boolean isPerformClick = false;

    @Override
    public void initView() {
        super.initView();
        isGoodsOut = false;
        findViewById(R.id.header_right_btn).setVisibility(WXShareUtils.getInstance(activity).isInstall(false)? View.VISIBLE:View.VISIBLE);
        if(this.getIntent()!=null){
            cityBean =  (CityBean)getIntent().getSerializableExtra("cityBean");
        }
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private CityBean findCityById(String cityId) {
        DbManager mDbManager = new DBHelper(activity).getDbManager();
        try {
            cityBean = mDbManager.findById(CityBean.class, cityId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return cityBean;
    }

    @Event({R.id.header_right_btn,R.id.goto_order})
    private void onClickView(View view){
        HashMap<String,String> map = new HashMap<String,String>();
        switch (view.getId()){
            case R.id.header_right_btn:
                skuShare(UrlLibs.H5_DAIRY);
                break;
            case R.id.goto_order:
//                Bundle bundle =new Bundle();
//                bundle.putSerializable("cityBean", cityBean);
//                startFragment(new FgOrderSelectCity(),bundle);

                Intent intent =  new Intent(context,OrderSelectCityActivity.class);
                intent.putExtra("cityBean", cityBean);
                startActivity(intent);

                break;
        }
    }

    private void skuShare(final String shareUrl) {
        CommonUtils.shareDialog(activity,R.drawable.wxshare_img, getString(R.string.share_title), getString(R.string.share_content), shareUrl);
    }

}
