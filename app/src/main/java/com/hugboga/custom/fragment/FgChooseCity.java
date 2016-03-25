package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.SharedPre;
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
import java.util.List;

/**
 * 选择城市
 */
@ContentView(R.layout.fg_city)
public class FgChooseCity extends BaseFragment implements SideBar.OnTouchingLetterChangedListener, AdapterView.OnItemClickListener, View.OnKeyListener, TextWatcher {

    public static final String KEY_BUNDLE = "bundle";
    public static final String KEY_CITY_ID = "key_city_id";
    public static final String KEY_CITY = "key_city";
    public static final String KEY_CITY_LIST_CHOSEN = "key_city_list_chosen";
    public static final String KEY_CITY_LIST = "key_city_list";
    public static final String KEY_CITY_EXCEPT_ID_LIST = "key_city_except";//排除 城市id ，根据id
    public static final String KEY_CHOOSE_TYPE = "key_choose_type";
    public static final int KEY_TYPE_SINGLE = 0;
    public static final int KEY_TYPE_MULTIPLY = 1;


    private SideBar sideBar;
    private View emptyView;
    private ListView sortListView;
    @ViewInject(R.id.empty_layout_text)
    private TextView emptyViewText;
    @ViewInject(R.id.choose_city_head_layout)
    private View chooseCityHeadLayout;
    @ViewInject(R.id.city_choose_head_text)
    private TextView cityHeadText;
    @ViewInject(R.id.city_choose_btn)
    private View chooseBtn;
    private CityAdapter adapter;
    private long t = 0;
    private List<CityBean> sourceDateList;//全部城市数据
    private int chooseType = KEY_TYPE_SINGLE;
    private int cityId = -1;
    private int groupId = -1;
    private ArrayList<CityBean> chooseCityList = new ArrayList<CityBean>();
    private ArrayList<Integer> exceptCityId = new ArrayList<>();
    private String from;
    private TextView editSearch;
    //    private DbUtils mDbUtils;
    private DbManager mDbManager;
    private SharedPre sharedPer;
    private ArrayList<String> cityHistory = new ArrayList<String>();//历史数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_city, null);
        chooseType = getArguments().getInt(KEY_CHOOSE_TYPE, KEY_TYPE_SINGLE);
        from = getArguments().getString(KEY_FROM);
        cityId = getArguments().getInt(KEY_CITY_ID, -1);
        exceptCityId = (ArrayList<Integer>) getArguments().getSerializable(KEY_CITY_EXCEPT_ID_LIST);
        editSearch = (TextView) view.findViewById(R.id.head_search);
        editSearch.setOnKeyListener(this);
        editSearch.addTextChangedListener(this);
        if ("startAddress".equals(from)) {
            editSearch.setHint("搜索出发城市");
        } else if ("end".equals(from)) {
            editSearch.setHint("搜索到达城市");
        } else if (chooseType == KEY_TYPE_MULTIPLY) {
            editSearch.setHint("搜索途经城市");
        } else if (getBusinessType() == Constants.BUSINESS_TYPE_RENT) {
            editSearch.setHint("搜索用车城市");
        }

//        mDbUtils = new DBHelper(getActivity()).getDbUtils();
        mDbManager = new DBHelper(getActivity()).getDbManager();
        sharedPer = new SharedPre(getActivity());
        return view;
    }

    protected void initView() {
        //实例化汉字转拼音类
        View view = getView();
        if (chooseType == KEY_TYPE_MULTIPLY) {
            chooseCityHeadLayout.setVisibility(View.VISIBLE);
            chooseBtn.setVisibility(View.VISIBLE);
        }
        emptyView = view.findViewById(R.id.arrival_empty_layout);
        sideBar = (SideBar) view.findViewById(R.id.sidrbar);
        TextView dialog = (TextView) view.findViewById(R.id.dialog);
        initSideBar(sideBar);
        sideBar.setTextView(dialog);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(this);
        sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(this);

//		findCityList(groupId, null);
        MLog.e("adapter= " + adapter);
        //测试代码开始
        setBusinessType(3);
        //测试代码结束
        adapter = new CityAdapter(getActivity(), this, sourceDateList, String.valueOf(getBusinessType()));
        adapter.setChooseType(chooseType);
        sortListView.setAdapter(adapter);
        sortListView.setEmptyView(emptyView);
        sideBar.setVisibility(View.VISIBLE);
    }

    //    @Override
    protected void initHeader() {
        setProgressState(0);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {

    }

    private void initSideBar(SideBar sideBar) {
        String[] b = {"历史", "热门", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z", "#"};
        sideBar.setLetter(b);

    }

    protected void inflateContent() {
        MLog.e("adapter= " + adapter);
        processSelectedData();
        setFirstWord(sourceDateList);
//		initListHead();

        adapter.updateListView(sourceDateList);
//        emptyViewText.setText(getString(R.string.arrival_empty_text,editSearch.getText().toString().trim()));
    }

    private void processSelectedData() {
        ArrayList<CityBean> mChooseCityList = (ArrayList<CityBean>) getArguments().getSerializable(KEY_CITY_LIST_CHOSEN);
        chooseCityList = new ArrayList<>();
        if (mChooseCityList != null) chooseCityList.addAll(mChooseCityList);
        StringBuffer sb = new StringBuffer();
        for (CityBean bean : chooseCityList) {
            for (int i = 0; i < sourceDateList.size(); i++) {
                CityBean city = sourceDateList.get(i);
                if (bean.cityId == city.cityId) {
                    city.isSelected = true;
                    sb.append(bean.name).append(",");
                }
            }
        }
        if (exceptCityId != null)
            for (int i = sourceDateList.size() - 1; i > 0; i--) {
                CityBean city = sourceDateList.get(i);
                for (int exceptId : exceptCityId) {
                    if (exceptId == city.cityId) {
                        sourceDateList.remove(city);
                    }
                }
            }
        String str = sb.toString();
        if (str.length() > 1)
            str = str.substring(0, str.length() - 1);
        cityHeadText.setText(str);

    }


    private void setFirstWord(List<CityBean> sourceDateList) {
        if (sourceDateList == null) return;
        String key = "";
        for (int i = 0; i < sourceDateList.size(); i++) {
            CityBean model = sourceDateList.get(i);
            if (key.equals(model.firstLetter)) {
                model.isFirst = false;
            } else {
                model.isFirst = true;
                key = model.firstLetter;
            }
        }
    }

    @Override
    protected Callback.Cancelable requestData() {
        long time = System.currentTimeMillis();
        if (cityId != -1 && groupId == -1) {
            try {
                Selector<CityBean> selector = mDbManager.selector(CityBean.class);
                CityBean cityBean = selector.where("city_id", "=", cityId).findFirst();
                if (cityBean != null)
                    groupId = cityBean.groupId;
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        requestDate(getBusinessType(), groupId, null);
        if (chooseType == this.KEY_TYPE_SINGLE) {
            requestHotDate(getBusinessType(), groupId);
            requestHistoryDate(getBusinessType(), groupId);
        }
        MLog.e("time=" + (System.currentTimeMillis() - time));
        return null;
    }

//    protected void requestDate() {
//        long time = System.currentTimeMillis();
//        if(cityId!=-1&&groupId==-1){
//            try {
//                Selector<CityBean> selector = mDbManager.selector(CityBean.class);
//                CityBean cityBean = selector.where("city_id", "=", cityId).findFirst();
//                if(cityBean!=null)
//                groupId = cityBean.groupId;
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
//        }
//        requestDate(getBusinessType(), groupId, null);
//        if(chooseType==this.KEY_TYPE_SINGLE){
//            requestHotDate(getBusinessType(), groupId);
//            requestHistoryDate(getBusinessType(), groupId);
//        }
//        MLog.e("time=" + (System.currentTimeMillis() - time));
//    }

    /**
     * 查询全部城市
     *
     * @param orderType 业务类型
     * @param groupId
     * @param keyword   搜索关键字
     */

    protected void requestDate(int orderType, int groupId, String keyword) {
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("1", "=", "1");
        if (!TextUtils.isEmpty(keyword)) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("cn_name", "LIKE", "%" + keyword + "%").or("place_name", "LIKE", "%" + keyword + "%");
            selector.and(whereBuilder);
        }
        if (orderType == Constants.BUSINESS_TYPE_DAILY) {
            if (groupId == -1) {
                selector.and("group_id", "<>", null);
                selector.and("is_daily", "=", 1);
            } else {
                selector.and("group_id", "=", groupId);
            }
//
        } else if (orderType == Constants.BUSINESS_TYPE_RENT) {
            selector.and("is_single", "=", 1);
        } else if (orderType == Constants.BUSINESS_TYPE_PICK || orderType == Constants.BUSINESS_TYPE_SEND) {
            selector.and("is_city_code", "=", 1);
        }
        selector.orderBy("initial");
        try {
            sourceDateList = selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        inflateContent();
    }

    /**
     * 热门城市
     *
     * @param orderType
     * @param groupId
     */
    private void requestHotDate(int orderType, int groupId) {
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("is_hot", "=", 1);
        if (orderType == Constants.BUSINESS_TYPE_DAILY) {
            if (groupId == -1) {
                selector.and("group_id", "<>", null);
                selector.and("is_daily", "=", 1);
            } else {
                selector.and("group_id", "=", groupId);
            }
        } else if (orderType == Constants.BUSINESS_TYPE_RENT) {
            selector.and("is_single", "=", 1);
        } else if (orderType == Constants.BUSINESS_TYPE_PICK || orderType == Constants.BUSINESS_TYPE_SEND) {
            selector.and("is_city_code", "=", 1);
        }
        // 修改热门城市排序
        selector.orderBy("hot_weight", true);
//        selector.orderBy("hot_weight");
        selector.limit(30);
        try {
            List<CityBean> hotCityDate = selector.findAll();
            for (CityBean bean : hotCityDate) {
                bean.firstLetter = "热门城市";
            }
            if (hotCityDate != null && hotCityDate.size() > 0 && hotCityDate.get(0) != null) {
                hotCityDate.get(0).isFirst = true;
//                sourceDateList.addAll(0,hotCityDate);
                sourceDateList.add(0, hotCityDate.get(0));
                adapter.setHotCityList(hotCityDate);
                adapter.setIsFirstAccessHotCity(false);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索历史记录
     *
     * @param orderType
     * @param groupId
     */
    private void requestHistoryDate(int orderType, int groupId) {
        String cityHistoryStr = sharedPer.getStringValue(mBusinessType + SharedPre.RESOURCES_CITY_HISTORY);
        cityHistory = new ArrayList<String>();
        if (!TextUtils.isEmpty(cityHistoryStr)) {
            for (String city : cityHistoryStr.split(",")) {
                cityHistory.add(city);
            }
        }
        if (cityHistory.size() == 0) {
            return;
        }
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        WhereBuilder where = WhereBuilder.b();
        if (cityHistory.size() > 0) {
            where.and("city_id", "IN", cityHistory);
        }
        selector.where(where);
        if (orderType == Constants.BUSINESS_TYPE_DAILY) {
            if (groupId == -1) {
                selector.and("group_id", "<>", null);
                selector.and("is_daily", "=", 1);
            } else {
                selector.and("group_id", "=", groupId);
            }
        } else if (orderType == Constants.BUSINESS_TYPE_RENT) {
            selector.and("is_single", "=", 1);
        } else if (orderType == Constants.BUSINESS_TYPE_PICK || orderType == Constants.BUSINESS_TYPE_SEND) {
            selector.and("is_city_code", "=", 1);
        }
        try {
            List<CityBean> tmpHistoryCityDate = selector.findAll();
            ArrayList<CityBean> historyCityDate = new ArrayList<CityBean>();
            for (String historyId : cityHistory) {
                for (CityBean bean : tmpHistoryCityDate) {
                    if (historyId.equals(String.valueOf(bean.cityId))) {
                        historyCityDate.add(bean);
                    }
                    bean.firstLetter = "搜索历史";
                }
            }
            if (historyCityDate.size() > 0 && historyCityDate.get(0) != null) {
                historyCityDate.get(0).isFirst = true;
            }
            sourceDateList.addAll(0, historyCityDate);
            adapter.setSearchHistoryCount(historyCityDate.size());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_text_right:
                String keyword = editSearch.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) return;
                requestDataByKeyword(getBusinessType(), groupId, keyword, false); //进行点击搜索
                break;
            default:
                break;
        }
    }

    @Event(value = {R.id.city_choose_btn, R.id.head_search_clean})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.city_choose_btn:
                finishResult();
                break;
            case R.id.head_search_clean:
                editSearch.setText("");
                break;
        }

    }

    @Override
    public void onTouchingLetterChanged(String s) {
        //该字母首次出现的位置
        int position = adapter.getPositionForSection(s);
        if (position != -1) {
            sortListView.setSelection(position);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Bundle bundle = new Bundle(getArguments());
        CityBean cityBean = sourceDateList.get(position);
        if (cityBean.firstLetter.equals("nationality")) {
            return;
        }
        if (chooseType == KEY_TYPE_SINGLE) {
            saveHistoryDate(cityBean);
            bundle.putSerializable(KEY_CITY, cityBean);
            finishForResult(bundle);
        } else {
            cityBean.isSelected = !cityBean.isSelected;
            if (chooseCityList.size() >= 10 && cityBean.isSelected) {
                cityBean.isSelected = false;
                Toast.makeText(getActivity(), "最多选择10个城市", Toast.LENGTH_LONG).show();
                return;
            }
            if (cityBean.isSelected) {
                chooseCityList.add(cityBean);
            } else {
                chooseCityList.remove(cityBean);
            }
            StringBuffer sb = new StringBuffer();
            for (CityBean bean : chooseCityList)
                sb.append(bean.name).append(",");
            String str = sb.toString();
            if (str.length() > 1)
                str = str.substring(0, str.length() - 1);
            cityHeadText.setText(str);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 存储历史
     *
     * @param cityBean
     */
    public void saveHistoryDate(CityBean cityBean) {
        if (cityHistory == null) cityHistory = new ArrayList<String>();
        cityHistory.remove(String.valueOf(cityBean.cityId));//排重
        cityHistory.add(0, String.valueOf(cityBean.cityId));
        for (int i = cityHistory.size() - 1; i > 2; i--) {
            cityHistory.remove(i);
        }
        sharedPer.saveStringValue(mBusinessType + SharedPre.RESOURCES_CITY_HISTORY, TextUtils.join(",", cityHistory));
    }

    private void finishResult() {
        Bundle bundle = new Bundle(getArguments());
        bundle.putSerializable(KEY_CITY_LIST, chooseCityList);
        finishForResult(bundle);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER)) {
            requestDataByKeyword(getBusinessType(), groupId, editSearch.getText().toString().trim(), false);
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            sourceDateList.clear();
            requestData();
        } else {
            sourceDateList.clear();
            adapter.getHotCityList().clear();
            List<CityBean> dataList = requestDataByKeyword(getBusinessType(), groupId, editSearch.getText().toString().trim(), false);
            if (dataList.size() > 0) {
                sourceDateList.addAll(dataList);
            }
            if (sourceDateList.size() >= 10) {
                adapter.notifyDataSetChanged();
            } else {
                List<CityBean> dataList2 = requestDataByKeyword(getBusinessType(), groupId, editSearch.getText().toString().trim(), true);
                if (dataList2.size() > 0) {
                    sourceDateList.addAll(dataList2);
                }
                if (sourceDateList.size() >= 10) {
                    adapter.notifyDataSetChanged();
                } else {
                    List<CityBean> finalList = requestCountryDataByKeyword(getBusinessType(), groupId, editSearch.getText().toString().trim());
                    if (finalList.size() > 0) {
                        sourceDateList.addAll(finalList);
                    }
                    adapter.notifyDataSetChanged();
                }
            }


        }

    }

    /**
     * 按关键词搜索
     *
     * @param orderType  业务类型(BUSINESS_TYPE)
     * @param groupId    群组关系
     * @param keyword    关键词
     * @param isNeedMore 是否需要更多结果(%keyword%的),第一次调置为false
     */
    protected List<CityBean> requestDataByKeyword(int orderType, int groupId, String keyword, boolean isNeedMore) {
//        Selector selector = Selector.from(CityBean.class);
        List<CityBean> dataList = new ArrayList<CityBean>();
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("1", "=", "1");
        if (!TextUtils.isEmpty(keyword)) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            if (isNeedMore) {
                whereBuilder.and("cn_name", "LIKE", "%" + keyword + "%");
            } else {
                whereBuilder.and("cn_name", "LIKE", keyword + "%");
            }
            selector.and(whereBuilder);
        }
        if (orderType == Constants.BUSINESS_TYPE_DAILY) {
            if (groupId == -1) {
                selector.and("group_id", "<>", null);
                selector.and("is_daily", "=", 1);
            } else {
                selector.and("group_id", "=", groupId);
            }
            //条件加不加？？？？
        } else if (orderType == Constants.BUSINESS_TYPE_RENT) {
            selector.and("is_single", "=", 1);
        } else if (orderType == Constants.BUSINESS_TYPE_PICK || orderType == Constants.BUSINESS_TYPE_SEND) {
            selector.and("is_city_code", "=", 1);
        }
        try {
            dataList = selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 查询国家
     *
     * @param orderType
     * @param groupId
     * @param keyword
     */
    protected List<CityBean> requestCountryDataByKeyword(int orderType, int groupId, String keyword) {
        List<CityBean> CountryDateList = new ArrayList<CityBean>();
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("1", "=", "1");
        if (!TextUtils.isEmpty(keyword)) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("place_name", "LIKE", keyword + "%")
                    .and("place_name", "!=", "中国");
            selector.and(whereBuilder);
        }
        if (orderType == Constants.BUSINESS_TYPE_DAILY) {
            if (groupId == -1) {
                selector.and("group_id", "<>", null);
                selector.and("is_daily", "=", 1);
            } else {
                selector.and("group_id", "=", groupId);
            }
            //条件加不加？？？？
        } else if (orderType == Constants.BUSINESS_TYPE_RENT) {
            selector.and("is_single", "=", 1);
        } else if (orderType == Constants.BUSINESS_TYPE_PICK || orderType == Constants.BUSINESS_TYPE_SEND) {
            selector.and("is_city_code", "=", 1);
        }
        selector.orderBy("guide_count", true);
        selector.limit(4);
        try {
            CountryDateList = selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (CountryDateList.size() > 0) {
            for (CityBean bc : CountryDateList) {
                bc.name = "相关城市，" + bc.name;
            }
            CityBean onlyForDisplayCityBean = new CityBean();
            onlyForDisplayCityBean.name = CountryDateList.get(0).placeName + " - 该地点为国家";
            onlyForDisplayCityBean.firstLetter = "nationality";
            CountryDateList.add(0, onlyForDisplayCityBean);
        }
        return CountryDateList;
    }
}
