package com.hugboga.custom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.SortCountryAdapter;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.PinyinComparator;
import com.hugboga.custom.widget.SideBar;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 查询国家区号
 */
@ContentView(R.layout.activity_choose_country)
public class FgChooseCountry extends BaseFragment {

    public static final String KEY_COUNTRY_CODE = "KEY_COUNTRY_CODE";
    public static final String KEY_COUNTRY_NAME = "KEY_COUNTRY_NAME";

    private PinyinComparator pinyinComparator;

    @ViewInject(R.id.sidrbar)
    SideBar sideBar;
    @ViewInject(R.id.dialog)
    TextView dialog;
    @ViewInject(R.id.country_lvcountry)
    ListView sortListView;
    @ViewInject(R.id.country_search)
    EditText searchEditText;
//    @ViewInject(R.id.head_btn_left)
//    ImageView head_btn_leftl;


    private SortCountryAdapter adapter;
    private List<AreaCodeBean> sourceDateList;
//    private DbUtils mDbUtils;
    private DbManager mDbManager;

    @Override
    protected void initHeader() {
        mDbManager = new DBHelper(getActivity()).getDbManager();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        collapseSoftInputMethod();
        requestDate(null);
        return null;
    }

    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            initViews();
        }
    };


    /**
     * 获取区号列表
     * @param keyword 搜索的关键词
     */
    private void requestDate(String keyword){
        Selector selector = null;
        try {
            selector = mDbManager.selector(AreaCodeBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(!TextUtils.isEmpty(keyword)) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("cn_name", "LIKE", "%" + keyword + "%").or("area_code", "LIKE", "%" + keyword + "%");
            selector.where(whereBuilder);
        }
        selector.orderBy("initial");
        try {
            sourceDateList =selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        initViews();
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    protected int getBusinessType() {
        return 0;
    }

    private void initViews() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //改变值后，筛选数据
                filterData(searchEditText.getText().toString());
            }
        });
        //实例化汉字转拼音类
        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
            	Intent intent = new Intent();
            	AreaCodeBean areaCodeBean = (AreaCodeBean)adapter.getItem(position);
                Bundle bundle = new Bundle();

                bundle.putString(KEY_COUNTRY_CODE, areaCodeBean.getCode());
                bundle.putString(KEY_COUNTRY_NAME, areaCodeBean.getName());
                finishForResult(bundle);
            }
        });

        // 根据a-z进行排序源数据
//        Collections.sort(sourceDateList, pinyinComparator);
        adapter = new SortCountryAdapter(getActivity(), sourceDateList);
        sortListView.setAdapter(adapter);
        sideBar.setVisibility(View.VISIBLE);
    }

    /**
     * 数据筛选
     * Created by ZHZEPHI at 2015年2月2日 下午7:31:38
     * @param filterStr
     */
    private void filterData(String filterStr){
    	List<AreaCodeBean> filterDateList = new ArrayList<AreaCodeBean>();

        if(TextUtils.isEmpty(filterStr)){
    		filterDateList = sourceDateList;
    	}else{
    		for(AreaCodeBean sm:sourceDateList){
        		if(sm.getName().indexOf(filterStr)>=0 || sm.getCode().indexOf(filterStr)>=0){
        			filterDateList.add(sm);
        		}
            }
    	}
    	// 根据a-z进行排序源数据
        Collections.sort(filterDateList, pinyinComparator);
        adapter = new SortCountryAdapter(getActivity(), filterDateList);
        sortListView.setAdapter(adapter);
    }
    
    /**
     * 为ListView填充数据
     * @param date
     * @return
     */
    private List<AreaCodeBean> filledData(String [] date){
        List<AreaCodeBean> mSortList = new ArrayList<AreaCodeBean>();
        
        String[] codes = getResources().getStringArray(R.array.countrycode);

        for(int i=0; i<date.length; i++){
            AreaCodeBean areaCodeBean = new AreaCodeBean();
            areaCodeBean.setName(date[i]);
            areaCodeBean.setCode(codes[i]);
            areaCodeBean.setSortLetters("A");
     /*       //汉字转换成拼音
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }*/
            mSortList.add(areaCodeBean);
        }
        return mSortList;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {

    }

    @Event({R.id.head_btn_left})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.head_btn_left:
                MLog.e("header_left_btn");
                finish();
                break;
        }
    }
}
