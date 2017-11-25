package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.widget.city.CityFilterView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelBean;
import tk.hongbo.label.data.LabelItemData;
import tk.hongbo.label.data.LabelParentBean;

public class CityListActivity extends BaseActivity {

    Toolbar toolbar;

    @BindView(R.id.content_city_filte_view1)
    FilterView content_city_filte_view1; //筛选条件内容，游玩线路
    @BindView(R.id.content_city_filte_view2)
    FilterView content_city_filte_view2; //筛选条件内容，出发城市
    @BindView(R.id.content_city_filte_view3)
    FilterView content_city_filte_view3; //筛选条件内容，游玩天数
    @BindView(R.id.city_filter_view)
    CityFilterView cityFilterView; //选择筛选项

    boolean isFromHome;
    boolean isFromDestination;

    @Override
    public int getContentViewId() {
        return R.layout.activity_city;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.topbar_back);

        if (savedInstanceState != null) {
            paramsData = (CityListActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (CityListActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        isFromHome = getIntent().getBooleanExtra("isFromHome",false);
        isFromDestination = getIntent().getBooleanExtra("isFromDestination",false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //监听筛选项变化
        cityFilterView.setFilterSeeListener(filterSeeListener);

        //初始化数据
        content_city_filte_view1.setData(testData(), new FilterView.OnSelectListener() {
            @Override
            public void onSelect(LabelBean labelBean) {
                content_city_filte_view1.hide();
                cityFilterView.clear();
                //TODO 具体选中的标签
                Snackbar.make(toolbar, "选中内容ID：" + labelBean.id + "，Title：" + labelBean.name, Snackbar.LENGTH_SHORT).show();
            }
        });
        content_city_filte_view2.setData(testData2(), new FilterView.OnSelectListener() {
            @Override
            public void onSelect(LabelBean labelBean) {
                content_city_filte_view2.hide();
                cityFilterView.clear();
                //TODO 具体选中的出发城市
                Snackbar.make(toolbar, "选中内容ID：" + labelBean.id + "，Title：" + labelBean.name, Snackbar.LENGTH_SHORT).show();
            }
        });
        content_city_filte_view3.setData(testData3(), new FilterView.OnSelectListener() {
            @Override
            public void onSelect(LabelBean labelBean) {
                cityFilterView.clear();
                content_city_filte_view3.hide();
                //TODO 具体选中的游玩天数
                Snackbar.make(toolbar, "选中内容ID：" + labelBean.id + "，Title：" + labelBean.name, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    private List<LabelItemData> testData3() {
        List<LabelItemData> data = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            LabelItemData itemData = new LabelItemData();
            List<LabelParentBean> parents = new ArrayList<>();
            //1
            LabelParentBean bean = new LabelParentBean();
            LabelBean beanL = new LabelBean();
            beanL.id = i;
            beanL.name = "1天" + i;
            bean.parentLabel = beanL;
            parents.add(bean);
            //2
            LabelParentBean bean2 = new LabelParentBean();
            LabelBean beanC = new LabelBean();
            beanC.id = i;
            beanC.name = "2~3天" + i;
            bean2.parentLabel = beanC;
            parents.add(bean2);
            //3
            LabelParentBean bean3 = new LabelParentBean();
            LabelBean beanR = new LabelBean();
            beanR.id = i;
            beanR.name = "7天及以上" + i;
            bean3.parentLabel = beanR;
            parents.add(bean3);

            itemData.parent = parents;
            data.add(itemData);
        }
        return data;
    }

    private List<LabelItemData> testData2() {
        List<LabelItemData> data = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            LabelItemData itemData = new LabelItemData();
            List<LabelParentBean> parents = new ArrayList<>();
            //1
            LabelParentBean bean = new LabelParentBean();
            LabelBean beanL = new LabelBean();
            beanL.id = i;
            beanL.name = "东京" + i;
            bean.parentLabel = beanL;
            parents.add(bean);
            //2
            LabelParentBean bean2 = new LabelParentBean();
            LabelBean beanC = new LabelBean();
            beanC.id = i;
            beanC.name = "箱根" + i;
            bean2.parentLabel = beanC;
            parents.add(bean2);
            //3
            LabelParentBean bean3 = new LabelParentBean();
            LabelBean beanR = new LabelBean();
            beanR.id = i;
            beanR.name = "镰仓" + i;
            bean3.parentLabel = beanR;
            parents.add(bean3);

            itemData.parent = parents;
            data.add(itemData);
        }
        return data;
    }

    private List<LabelItemData> testData() {
        List<LabelItemData> data = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            LabelItemData itemData = new LabelItemData();
            List<LabelParentBean> parents = new ArrayList<>();
            //1
            LabelParentBean bean = new LabelParentBean();
            LabelBean beanL = new LabelBean();
            beanL.id = i;
            beanL.name = "关西地区" + i;
            bean.parentLabel = beanL;
            bean.childs = getChild(i);
            parents.add(bean);
            //2
            LabelParentBean bean2 = new LabelParentBean();
            LabelBean beanC = new LabelBean();
            beanC.id = i;
            beanC.name = "北海道地区" + i;
            bean2.parentLabel = beanC;
//            bean2.childs = getChild(i);
            parents.add(bean2);
            //3
            LabelParentBean bean3 = new LabelParentBean();
            LabelBean beanR = new LabelBean();
            beanR.id = i;
            beanR.name = "冲绳地区" + i;
            bean3.parentLabel = beanR;
            bean3.childs = getChild(i);
            parents.add(bean3);

            itemData.parent = parents;
            data.add(itemData);
        }
        return data;
    }

    private List<LabelBean> getChild(int i) {
        //Child
        List<LabelBean> child = new ArrayList<>();
        for (int j = 1; j < 10; j++) {
            LabelBean label = new LabelBean();
            label.id = j + 100;
            label.name = i + "子" + label.id;
            child.add(label);
        }
        return child;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Snackbar.make(toolbar, "点击了联系方式按钮！！！", Snackbar.LENGTH_SHORT).show();
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    CityFilterView.FilterSeeListener filterSeeListener = new CityFilterView.FilterSeeListener() {
        @Override
        public void onShowFilter(int position, boolean isSelect) {
            clearContentCityFilteViews();
            switch (position) {
                case 0:
                    if (content_city_filte_view1 != null) {
                        content_city_filte_view1.setVisibility(isSelect ? View.VISIBLE : View.GONE);
                    }
                    break;
                case 1:
                    if (content_city_filte_view2 != null) {
                        content_city_filte_view2.setVisibility(isSelect ? View.VISIBLE : View.GONE);
                    }
                    break;
                case 2:
                    if (content_city_filte_view3 != null) {
                        content_city_filte_view3.setVisibility(isSelect ? View.VISIBLE : View.GONE);
                    }
                    break;
            }
        }
    };

    private void clearContentCityFilteViews() {
        if (content_city_filte_view1 != null) {
            content_city_filte_view1.setVisibility(View.GONE);
        }
        if (content_city_filte_view2 != null) {
            content_city_filte_view2.setVisibility(View.GONE);
        }
        if (content_city_filte_view3 != null) {
            content_city_filte_view3.setVisibility(View.GONE);
        }
    }


    /**************** Old codes *****************************/
    public static final int GUIDE_LIST_COUNT = 8;//精选司导显示的条数

    public enum CityHomeType {
        CITY, ROUTE, COUNTRY, ALL
    }

    public static class Params implements Serializable {
        public int id;
        public CityListActivity.CityHomeType cityHomeType;
        public String titleName;
    }

    public CityListActivity.Params paramsData;

    public boolean isShowCity() {
        if (paramsData.cityHomeType == CityHomeType.ROUTE || paramsData.cityHomeType == CityHomeType.COUNTRY) {
            return true;
        } else {
            return false;
        }
    }
}
