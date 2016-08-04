package com.hugboga.custom.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.ChooseCityAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.ChooseCityHeaderView;
import com.hugboga.custom.widget.ChooseCityTabLayout;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.SideBar;
import com.zhy.m.permission.MPermissions;

import org.greenrobot.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by qingcha on 16/8/3.
 */
public class ChooseCityActivity extends BaseActivity implements SideBar.OnTouchingLetterChangedListener, ChooseCityTabLayout.OnChangeListener, TextWatcher, TextView.OnEditorActionListener, AdapterView.OnItemClickListener{
    public static final String KEY_CITY_ID = "key_city_id";
    public static final String KEY_CITY = "key_city";
    public static final String KEY_CITY_LIST_CHOSEN = "key_city_list_chosen";
    public static final String KEY_CITY_LIST = "key_city_list";
    public static final String KEY_CITY_EXCEPT_ID_LIST = "key_city_except";//排除 城市id ，根据id
    public static final String KEY_SHOW_TYPE = "key_show_type";

    @Bind(R.id.choose_city_empty_layout)
    LinearLayout emptyLayout;
    @Bind(R.id.choose_city_empty_tv)
    TextView emptyTV;
    @Bind(R.id.choose_city_empty_iv)
    ImageView emptyIV;
    @Bind(R.id.choose_city_listview)
    StickyListHeadersListView mListview;
    @Bind(R.id.choose_city_sidebar_firstletter)
    TextView firstletterView;
    @Bind(R.id.choose_city_sidebar)
    SideBar sideBar;
    @Bind(R.id.choose_city_tab_layout)
    ChooseCityTabLayout tabLayout;
    @Bind(R.id.head_search)
    EditText editSearch;
    @Bind(R.id.head_text_right)
    TextView searchTV;

    @Bind(R.id.choose_city_head_layout)
    View chooseCityHeadLayout;
    @Bind(R.id.city_choose_head_text)
    TextView cityHeadText;
    @Bind(R.id.city_choose_btn)
    View chooseBtn;

    private ChooseCityHeaderView headerView;
    private FrameLayout headerRootView;


    private ChooseCityAdapter mAdapter;
    private Handler mAsyncHandler;
    private List<CityBean> cityList = new ArrayList<>();;
    private ArrayList<String> cityHistory = new ArrayList<>();//历史数据
    private ArrayList<CityBean> chooseCityList = new ArrayList<CityBean>();
    private ArrayList<Integer> exceptCityId = new ArrayList<>();
    private SharedPre sharedPer;
    private DbManager mDbManager;
    public String from = "startAddress";
    public volatile int cityId = -1;
    public int showType = ShowType.PICK_UP;
    private DialogUtil mDialogUtil;
    private int mBusinessType;

    public volatile int groupId = -1;
    public volatile String startCityName;

    private static final class MessageType {
        /**
         * 全部城市
         * */
        public static final int ALL_CITY = 0x0001;

        /**
         * 热门城市
         * */
        public static final int HOT_CITY = 0x0002;

        /**
         * 搜索历史
         * */
        public static final int SEARCH_HISTORY = 0x0003;

        /**
         * 当前定位
         * */
        public static final int LOCATION = 0x0004;

    }

    public static final class ShowType {
        /**
         * 选城市
         * */
        public static final int SELECT_CITY = 0x0001;

        public static final int MULTIPLY = 0x0002;

        /**
         * 接送机选城市
         * */
        public static final int PICK_UP = 0x0003;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_choose_city);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void initView() {
//        setProgressState(0);
        searchTV.setText(getString(R.string.dialog_btn_cancel));
        searchTV.setVisibility(View.GONE);

        mDialogUtil = DialogUtil.getInstance(this);
        mDialogUtil.showLoadingDialog();
        emptyTV.setVisibility(View.GONE);
        emptyIV.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cityId = bundle.getInt(KEY_CITY_ID, -1);
            from = bundle.getString(KEY_FROM);
            showType = bundle.getInt(KEY_SHOW_TYPE, ShowType.MULTIPLY);
            exceptCityId = (ArrayList<Integer>) bundle.getSerializable(KEY_CITY_EXCEPT_ID_LIST);
            source = bundle.getString("source");
            mBusinessType = bundle.getInt(KEY_BUSINESS_TYPE, -1);
        }

        sideBar.setTextView((TextView) findViewById(R.id.choose_city_sidebar_firstletter));
        sideBar.setOnTouchingLetterChangedListener(this);
        sideBar.setVisibility(View.VISIBLE);

        if (showType == ShowType.SELECT_CITY) {
            chooseCityHeadLayout.setVisibility(View.VISIBLE);
            chooseBtn.setVisibility(View.VISIBLE);
        } else {
            headerView = new ChooseCityHeaderView(ChooseCityActivity.this);
            headerRootView = new FrameLayout(this);
            headerRootView.addView(headerView);
            mListview.addHeaderView(headerRootView);
            if (showType == ShowType.PICK_UP) {
                tabLayout.setVisibility(View.VISIBLE);
                tabLayout.setOnChangeListener(this);
            }
        }

        mAdapter = new ChooseCityAdapter(ChooseCityActivity.this);
        mListview.setAdapter(mAdapter);
        mListview.setEmptyView(emptyLayout);
        mListview.setOnItemClickListener(this);

        editSearch.setOnEditorActionListener(this);
        editSearch.addTextChangedListener(this);
        editSearch.setHint("请输入城市名称");
        if ("startAddress".equals(from)) {
            editSearch.setHint("请输入出发城市名称");
        } else if ("end".equals(from)) {
            editSearch.setHint("请输入达到城市名称");
            tabLayout.findViewById(R.id.choose_city_tab_foreign_layout).performClick();
        } else if (showType == ShowType.SELECT_CITY) {
            editSearch.setHint("搜索途经城市");
        } else if (mBusinessType == Constants.BUSINESS_TYPE_RENT) {
            editSearch.setHint("搜索用车城市");
        } else if ("lastCity".equals(from) || "nearby".equals(from)) {
            editSearch.setHint("请输入城市名称");
        }

        sharedPer = new SharedPre(this);
        mDbManager = new DBHelper(this).getDbManager();

        doInBackground();
        requestData();
    }

    private void requestData() {
        processSelectedData();
        onPreExecuteHandler.sendEmptyMessage(MessageType.ALL_CITY);
    }

    @Override
    public void OnChangeListener(boolean inland) {
        requestData();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
            String keyword = editSearch.getText().toString().trim();
            if (TextUtils.isEmpty(keyword)) {
                CommonUtils.showToast("请输入搜索内容");
                return true;
            }
            collapseSoftInputMethod(editSearch);
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
        emptyTV.setVisibility(View.GONE);
        emptyIV.setVisibility(View.GONE);
        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(s.toString().trim())) {
            mListview.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

            searchTV.setVisibility(View.GONE);
            if (showType != ShowType.SELECT_CITY) {
                headerRootView.removeAllViews();
                headerRootView.addView(headerView);
                if (showType == ShowType.PICK_UP) {
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }
            cityList.clear();
            mListview.setAreHeadersSticky(true);
            mAdapter.setData(cityList);

            requestData();
        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = -UIUtils.dip2px(32);
            mListview.setLayoutParams(params);

            searchTV.setVisibility(View.VISIBLE);
            if (showType != ShowType.SELECT_CITY) {
                headerRootView.removeAllViews();
                if (showType == ShowType.PICK_UP) {
                    tabLayout.setVisibility(View.GONE);
                }
            }
            cityList.clear();
            sideBar.setVisibility(View.GONE);
            mListview.setAreHeadersSticky(false);

            mAdapter.setShowType(ChooseCityAdapter.ShowType.SEARCH_PROMPT);

            List<CityBean> dataList = requestDataByKeyword(mBusinessType, groupId, editSearch.getText().toString().trim(), false);
            cityList.addAll(dataList);

            if (cityList.size() < 10) {
                List<CityBean> moreDataList = requestDataByKeyword(mBusinessType, groupId, editSearch.getText().toString().trim(), true);
                if (moreDataList.size() > 0) {
                    if (cityList.size() <= 0) {
                        cityList.addAll(moreDataList);
                    } else {
                        for(CityBean cityBean : moreDataList) {
                            for (int i=0; i< cityList.size(); i++) {
                                if (cityBean.cityId == cityList.get(i).cityId) {
                                    break;
                                }
                                if ( i == cityList.size() - 1) {
                                    cityList.add(cityBean);
                                }
                            }
                        }
                    }
                } else if (cityList.size() <= 0){
                    if (mBusinessType == Constants.BUSINESS_TYPE_DAILY) {
                        if ("lastCity".equals(from) && s.toString().trim().equals(startCityName)) {
                            emptyTV.setText(getString(R.string.can_not_choose_start_city_text));
                        } else {
                            emptyTV.setText(getString(R.string.out_of_range_city_text));
                        }
                    }
                }
            }
            mAdapter.setData(cityList);
            mListview.setSelection(0);
            if (cityList == null || cityList.size() <= 0) {
                emptyTV.setVisibility(View.VISIBLE);
                emptyIV.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.city_choose_btn, R.id.head_search_clean, R.id.head_text_right, R.id.header_left_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city_choose_btn:
                //FIXME qingcha
//                Bundle bundle = new Bundle(getArguments());
//                bundle.putSerializable(KEY_CITY_LIST, chooseCityList);
//                finishForResult(bundle);

                break;
            case R.id.head_search_clean:
                if(TextUtils.isEmpty(editSearch.getText().toString().trim())){
                    break;
                }
                editSearch.setText("");
                break;
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.head_text_right:
                String keyword = editSearch.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    CommonUtils.showToast("请输入搜索内容");
                    return;
                }
                editSearch.setText("");
                searchTV.setVisibility(View.GONE);
                collapseSoftInputMethod(editSearch);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (showType != ShowType.SELECT_CITY) {
            --position;
        }
        if (position >= 0 && position < cityList.size()) {
            onItemClick(cityList.get(position));
        }
    }

    public void onItemClick(CityBean _cityBean) {
        Bundle bundle = new Bundle(getIntent().getExtras());
        CityBean cityBean = _cityBean;
        if (cityBean.isNationality) {
            return;
        }
        if (showType == ShowType.SELECT_CITY) {//途径城市
            cityBean.isSelected = !cityBean.isSelected;
            if (chooseCityList.size() >= 10 && cityBean.isSelected) {
                cityBean.isSelected = false;
                CommonUtils.showToast("最多选择10个城市");
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
            mAdapter.notifyDataSetChanged();
        } else {//包车城市搜索
            saveHistoryDate(cityBean);
            bundle.putSerializable(KEY_CITY, cityBean);

            finish();
            if(null != from  && from.equalsIgnoreCase("lastCity")){
                EventBus.getDefault().post(new EventAction(EventType.CHOOSE_END_CITY_BACK, cityBean));
            }else {
                EventBus.getDefault().post(new EventAction(EventType.CHOOSE_START_CITY_BACK, cityBean));
            }
        }
    }

    private void processSelectedData() {
        ArrayList<CityBean> mChooseCityList = null;
        if (getIntent().getExtras() != null) {
            mChooseCityList = (ArrayList<CityBean>) getIntent().getExtras().getSerializable(KEY_CITY_LIST_CHOSEN);
        }
        chooseCityList = new ArrayList<>();
        if (mChooseCityList != null) chooseCityList.addAll(mChooseCityList);
        StringBuffer sb = new StringBuffer();
        for (CityBean bean : chooseCityList) {
            for (int i = 0; i < cityList.size(); i++) {
                CityBean city = cityList.get(i);
                if (bean.cityId == city.cityId) {
                    city.isSelected = true;
                    sb.append(bean.name).append(",");
                }
            }
        }
        if (exceptCityId != null)
            for (int i = cityList.size() - 1; i > 0; i--) {
                CityBean city = cityList.get(i);
                for (int exceptId : exceptCityId) {
                    if (exceptId == city.cityId) {
                        cityList.remove(city);
                    }
                }
            }
        String str = sb.toString();
        if (str.length() > 1)
            str = str.substring(0, str.length() - 1);
        cityHeadText.setText(str);
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

    @Override
    public void onTouchingLetterChanged(String s) {
        //该字母首次出现的位置
        int position = getPositionForSection(s);
        if (s.equals("定位") || s.equals("历史") || s.equals("热门")) {
            mListview.setSelection(0);
        } else if (position != -1) {
            position += 1;
            if (position < mAdapter.getCount()) {
                mListview.setSelection(position);
            }
        }
    }

    public int getPositionForSection(String section) {
        if (cityList == null) {
            return -1;
        }
        for (int i = 0; i < cityList.size(); i++) {
            String sortStr = cityList.get(i).firstLetter;
            if (sortStr.contains(section)) {
                return i;
            }
        }
        return -1;
    }

    private void setSectionIndices() {
        sideBar.setVisibility(View.VISIBLE);
        if (cityList == null) {
            if (showType != ShowType.SELECT_CITY) {
                sideBar.setLetter(sideBar.getDefaultLetter());
            } else {
                String[] sections = {"历史", "热门", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                        "W", "X", "Y", "Z", "#"};
                sideBar.setLetter(sections);
            }
            return;
        }
        ArrayList<String> sectionIndices = new ArrayList<String>();
        if (showType != ShowType.SELECT_CITY) {
            if (showType == ShowType.PICK_UP) {
                sectionIndices.add("定位");
            }
            sectionIndices.add("历史");
            sectionIndices.add("热门");
        }
        String lastFirstLetter = null;
        for (int i = 0; i < cityList.size(); i++) {
            CityBean cityBean = cityList.get(i);
            if (cityBean == null) {
                continue;
            }
            if (!TextUtils.equals(lastFirstLetter, cityBean.firstLetter)) {
                sectionIndices.add(cityBean.firstLetter);
                lastFirstLetter = cityBean.firstLetter;
            }
        }
        String[] sections = new String[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        sideBar.setHeightWrapContent(true);
        sideBar.setLetter(sections);
    }

    private void doInBackground() {
        HandlerThread mHandlerThread = new HandlerThread("fg_choose_city");
        mHandlerThread.start();
        mAsyncHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Message message = Message.obtain();
                message.what = msg.what;
                if (msg.obj instanceof String) {
                    message.obj = DatabaseManager.getCityBeanList((String) msg.obj);
                } else if (msg.obj instanceof Selector) {
                    Selector selector = (Selector) msg.obj;
                    try {
                        if (message.what == MessageType.LOCATION) {//定位城市
                            message.obj = selector.findFirst();
                        } else {
                            message.obj = selector.findAll();
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                onPostExecuteHandler.sendMessage(message);
            }
        };
    }

    private Handler onPreExecuteHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Message message = Message.obtain();
            message.what = msg.what;
            switch (msg.what) {
                case MessageType.ALL_CITY:
                    mDialogUtil.showLoadingDialog();
                    if (showType == ShowType.PICK_UP) {
                        if (tabLayout.isInland()) {//国内城市
                            message.obj = DatabaseManager.getInlandCitySql();
                            mAdapter.setShowType(ChooseCityAdapter.ShowType.DEFAULT);
                        } else {//国际港澳台
                            message.obj = DatabaseManager.getAbroadCitySql();
                            mAdapter.setShowType(ChooseCityAdapter.ShowType.SHOW_COUNTRY);
                        }
                    } else {
                        //此处逻辑来自旧代码,之后需要移动到子线程
                        if (cityId != -1 && groupId == -1) {
                            CityBean cityBean = DatabaseManager.getCityBean("" + cityId);
                            if (cityBean != null) {
                                groupId = cityBean.groupId;
                                startCityName = cityBean.name;
                            }
                        }
                        message.obj = DatabaseManager.getAllCitySql(mBusinessType, groupId, cityId, from);
                        if (showType == ShowType.SELECT_CITY) {
                            mAdapter.setShowType(ChooseCityAdapter.ShowType.SELECT_CITY);
                        } else {
                            mAdapter.setShowType(ChooseCityAdapter.ShowType.DEFAULT);
                        }
                    }
                    if (showType != ShowType.SELECT_CITY) {
                        onPreExecuteHandler.sendEmptyMessage(MessageType.HOT_CITY);
                        onPreExecuteHandler.sendEmptyMessage(MessageType.SEARCH_HISTORY);
                        onPreExecuteHandler.sendEmptyMessage(MessageType.LOCATION);
                    }
                    mAsyncHandler.sendMessage(message);
                    break;
                case MessageType.HOT_CITY:
                    if (showType == ShowType.PICK_UP) {
                        if (tabLayout.isInland()) {//国内城市
                            message.obj = DatabaseManager.getInlandHotCitySql();
                        } else {//国际港澳台
                            message.obj = DatabaseManager.getAbroadHotCitySql();
                        }
                    } else {
                        message.obj = DatabaseManager.getHotDateSql(mBusinessType, groupId, cityId, from);
                    }
                    mAsyncHandler.sendMessage(message);
                    break;
                case MessageType.SEARCH_HISTORY:
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
                    message.obj = DatabaseManager.getHistoryDateSql(mBusinessType, groupId, cityId, from, cityHistory);
                    mAsyncHandler.sendMessage(message);
                    break;
                case MessageType.LOCATION:
                    String cityId = sharedPer.getStringValue("cityId");
                    String cityName = sharedPer.getStringValue("cityName");
                    if (showType == ShowType.PICK_UP) {
                        if (!TextUtils.isEmpty(cityId) && !TextUtils.isEmpty(cityName)) {
                            CityBean locationCityBean = new CityBean();
                            locationCityBean.cityId = CommonUtils.getCountInteger(cityId);
                            locationCityBean.name = cityName;
                            message.obj = locationCityBean;
                        }
                        onPostExecuteHandler.sendMessage(message);
                    } else {
                        message.obj = DatabaseManager.getLocationDateSql(cityId);
                        mAsyncHandler.sendMessage(message);
                    }
                    break;
            }
        }
    };

    private Handler onPostExecuteHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageType.ALL_CITY:
                    if (msg.obj == null) {
                        break;
                    }
                    cityList = (List<CityBean>) msg.obj;
                    mAdapter.setData(cityList);
                    mListview.setSelection(0);
                    setSectionIndices();
                    mDialogUtil.dismissLoadingDialog();
                    emptyLayout.setVisibility(View.GONE);
                    break;
                case MessageType.HOT_CITY:
                    if (msg.obj == null) {
                        break;
                    }
                    List<CityBean> hotCityList = (List<CityBean>) msg.obj;
                    headerView.setHotCitysData(hotCityList);
                    break;
                case MessageType.SEARCH_HISTORY:
                    if (msg.obj == null) {
                        break;
                    }
                    List<CityBean> historyList = (List<CityBean>) msg.obj;
                    headerView.setHistoryData(historyList);
                    break;
                case MessageType.LOCATION:
                    CityBean cityBean = null;
                    if (msg.obj instanceof CityBean) {
                        cityBean = (CityBean) msg.obj;
                    }
                    headerView.setLociationData(cityBean, showType == ShowType.PICK_UP);
                    break;
            }
        }
    };

    /**
     * 按关键词搜索
     *
     * @param orderType  业务类型(BUSINESS_TYPE)
     * @param groupId    群组关系
     * @param keyword    关键词
     * @param isNeedMore 是否需要更多结果(%keyword%的),第一次调置为false
     */
    protected List<CityBean> requestDataByKeyword(int orderType, int groupId, String keyword, boolean isNeedMore) {
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
                selector.and("is_daily", "=", 1);
            } else {
                selector.and("group_id", "=", groupId);
            }
            if ("lastCity".equals(from) && cityId != -1){
                selector.and("city_id", "<>", cityId);
            }
        } else if (orderType == Constants.BUSINESS_TYPE_RENT) {
            selector.and("is_single", "=", 1);
        } else if (orderType == Constants.BUSINESS_TYPE_PICK || orderType == Constants.BUSINESS_TYPE_SEND) {
            selector.and("is_city_code", "=", 1);
        } else if (orderType == Constants.BUSINESS_TYPE_HOME) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("place_name", "<>", "中国");
            selector.and(whereBuilder);
            WhereBuilder whereBuilder2 = WhereBuilder.b();
            whereBuilder2.and("has_airport", "=", 1).or("is_daily", "=", 1).or("is_single", "=", 1);
            selector.and(whereBuilder2);
        }
        try {
            dataList = selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (dataList.size() > 0) {
            for (CityBean cb : dataList) {
                cb.keyWord = keyword;
            }
        }
        return dataList;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        switch (requestCode) {
            case 11:
            case 12:
                if (headerView != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    headerView.requestLocation();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
