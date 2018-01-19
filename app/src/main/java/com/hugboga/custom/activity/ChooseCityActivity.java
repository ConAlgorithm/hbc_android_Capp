package com.hugboga.custom.activity;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.ChooseCityAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.ChooseCityHeaderView;
import com.hugboga.custom.widget.ChooseCityTabLayout;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.SideBar;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static android.os.Build.*;
import static android.view.View.GONE;
import static android.view.View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS;

/**
 * Created by on 16/8/3.
 */
public class ChooseCityActivity extends BaseActivity implements SideBar.OnTouchingLetterChangedListener, ChooseCityTabLayout.OnChangeListener, TextWatcher, TextView.OnEditorActionListener, AdapterView.OnItemClickListener {
    public static final String KEY_CITY_ID = "key_city_id";
    public static final String KEY_CITY = "key_city";
    public static final String KEY_CITY_LIST_CHOSEN = "key_city_list_chosen";
    public static final String KEY_CITY_LIST = "key_city_list";
    public static final String KEY_CITY_EXCEPT_ID_LIST = "key_city_except";//排除 城市id ，根据id
    public static final String KEY_SHOW_TYPE = "key_show_type";
    public static String KEY_FROM = "key_from";
    public static String KEY_FROM_TAG = "from_tag";
    public static final String KEY_COUNTRY_ID = "key_country_id";
    public static final String KEY_GROUP_ID = "key_group_id";

    public static final String PARAM_TYPE_START = "startAddress";
    public static final String KEY_BUSINESS_TYPE = "key_business_Type";

    public static final String GROUP_START = "group_start";     // 组合单开始城市
    public static final String GROUP_OUTTOWN = "group_outtown";     // 组合单开始城市
    public static final String CITY_LIST = "city_list";

    @BindView(R.id.choose_city_empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.choose_city_empty_tv)
    TextView emptyTV;
    @BindView(R.id.choose_city_empty_iv)
    ImageView emptyIV;
    @BindView(R.id.choose_city_empty_service_tv)
    TextView emptyServiceTV;

    @BindView(R.id.choose_city_listview)
    StickyListHeadersListView mListview;
    @BindView(R.id.choose_city_sidebar_firstletter)
    TextView firstletterView;
    @BindView(R.id.choose_city_sidebar)
    SideBar sideBar;
    @BindView(R.id.choose_city_tab_layout)
    ChooseCityTabLayout tabLayout;
    @BindView(R.id.choose_city_tab_below)
    View chooseCityTabBelow;
    @BindView(R.id.head_search)
    EditText editSearch;
    @BindView(R.id.choose_city_tab_inland_line)
    View inlandLineView;
    @BindView(R.id.choose_city_tab_foreign_line)
    View foreignLineView;
    @BindView(R.id.choose_city_tab_inland_tv)
    TextView inlandTV;
    @BindView(R.id.choose_city_tab_foreign_tv)
    TextView foreignTV;
//    @BindView(R.id.head_text_right)
//    TextView searchTV;

    @BindView(R.id.choose_city_head_layout)
    View chooseCityHeadLayout;
    @BindView(R.id.city_choose_head_text)
    TextView cityHeadText;
    @BindView(R.id.city_choose_btn)
    View chooseBtn;
    @BindView(R.id.header_left_btn_new)
    ImageView headerLeftBtnNew;
    @BindView(R.id.header_title_new)
    TextView headerTitleNew;
    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.head_search_clean)
    ImageView headSearchClean;
    @BindView(R.id.activity_head_layout)
    RelativeLayout activityHeadLayout;
    @BindView(R.id.daily_layout)
    RelativeLayout dailyLayout;

    private ChooseCityHeaderView headerView;
    private FrameLayout headerRootView;


    private ChooseCityAdapter mAdapter;
    private Handler mAsyncHandler;
    private List<CityBean> cityList = new ArrayList<>();
    private ArrayList<String> cityHistory = new ArrayList<>();//历史数据
    private ArrayList<CityBean> chooseCityList = new ArrayList<CityBean>();
    private ArrayList<Integer> exceptCityId = new ArrayList<>();
    private SharedPre sharedPer;
    private DbManager mDbManager;
    public String from = "startAddress";
    public int showType = ShowType.PICK_UP;
    private DialogUtil mDialogUtil;
    private int mBusinessType;
    public String fromTag;

    private List<CityBean> hotCityList;
    private List<CityBean> historyList;
    private List<CityBean> locationAndHistory = new ArrayList<CityBean>();

    public volatile int cityId = -1;
    public volatile int countryId = -1;
    public volatile int groupId = -1;
    public volatile String startCityName;
    CsDialog csDialog;

    private static final class MessageType {
        /**
         * 全部城市
         */
        public static final int ALL_CITY = 0x0001;

        /**
         * 热门城市
         */
        public static final int HOT_CITY = 0x0002;

        /**
         * 搜索历史
         */
        public static final int SEARCH_HISTORY = 0x0003;

        /**
         * 当前定位
         */
        public static final int LOCATION = 0x0004;

    }

    public static final class ShowType {
        /**
         * 选城市
         */
        public static final int SELECT_CITY = 0x0001;

        public static final int MULTIPLY = 0x0002;

        /**
         * 接送机选城市
         */
        public static final int PICK_UP = 0x0003;

        public static final int GROUP_START = 0x0004;

        public static final int GROUP_OUTTOWN = 0x0005;

        public static final int CITY_LIST = 0x0007;

    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_choose_city;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        hideInputMethod(editSearch);
        editSearch.clearFocus();
    }

    boolean fromDaily = false;//包车选城市
    private void initView() {

        /* java.lang.IllegalStateException
         * Handling non empty state of parent class is not implemented
         * https://github.com/emilsjolander/StickyListHeaders/issues/477
         */
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            mListview.setImportantForAutofill(IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

//        setProgressState(0);
        fromDaily = this.getIntent().getBooleanExtra("fromDaily",false);
        if (fromDaily) {
            dailyLayout.setVisibility(View.VISIBLE);
            headerLeftBtn.setVisibility(View.INVISIBLE);
            headerTitleNew.setText(R.string.choose_city_title1);
            headerLeftBtnNew.setImageResource(R.mipmap.top_close);
            headerLeftBtnNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSoftInput();
                    finish();
                    overridePendingTransition(R.anim.push_buttom_out,0);
                }
            });
        }else{
            dailyLayout.setVisibility(GONE);
            headerLeftBtn.setVisibility(View.VISIBLE);
            if (this.getIntent().getBooleanExtra("fromInterCity",false)){
                headerLeftBtn.setImageResource(R.mipmap.top_close);
            }
            headerLeftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSoftInput();
                    finish();
                }
            });
        }


        //searchTV.setText(getString(R.string.dialog_btn_cancel));
        //searchTV.setVisibility(GONE);
        headerLeftBtn.setVisibility(View.VISIBLE);

        mDialogUtil = DialogUtil.getInstance(this);
        mDialogUtil.showLoadingDialog();
        emptyTV.setVisibility(GONE);
        emptyIV.setVisibility(GONE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            from = bundle.getString(KEY_FROM);
            showType = bundle.getInt(KEY_SHOW_TYPE, ShowType.MULTIPLY);
            exceptCityId = (ArrayList<Integer>) bundle.getSerializable(KEY_CITY_EXCEPT_ID_LIST);
            source = bundle.getString("source");
            mBusinessType = bundle.getInt(KEY_BUSINESS_TYPE, -1);
            fromTag = bundle.getString(KEY_FROM_TAG);
            cityId = bundle.getInt(KEY_CITY_ID, -1);
            countryId = bundle.getInt(KEY_COUNTRY_ID, -1);
            groupId = bundle.getInt(KEY_GROUP_ID, -1);
        }

        sideBar.setTextView((TextView) findViewById(R.id.choose_city_sidebar_firstletter));
        sideBar.setOnTouchingLetterChangedListener(this);
        sideBar.setVisibility(View.VISIBLE);

        if (showType == ShowType.SELECT_CITY) {
            chooseCityHeadLayout.setVisibility(View.VISIBLE);
            chooseBtn.setVisibility(View.VISIBLE);
        } else {
            boolean isPickUp = showType == ShowType.PICK_UP? true:false;
            headerView = new ChooseCityHeaderView(ChooseCityActivity.this,isPickUp);
            headerView.setBackgroundColor(getResources().getColor(R.color.allbg_white));
            headerRootView = new FrameLayout(this);
            headerRootView.addView(headerView);
            mListview.addHeaderView(headerRootView);
            if (showType == ShowType.PICK_UP) {
                tabLayout.setVisibility(View.VISIBLE);
                tabLayout.setOnChangeListener(this);
                chooseCityTabBelow.setVisibility(View.VISIBLE);
            }
        }

        mAdapter = new ChooseCityAdapter(ChooseCityActivity.this);
        mListview.setAdapter(mAdapter);
        mListview.setEmptyView(emptyLayout);
        mListview.setOnItemClickListener(this);

        editSearch.setOnEditorActionListener(this);
        editSearch.addTextChangedListener(this);
        editSearch.setHint(R.string.choose_city_title2);
        if ("startAddress".equals(from)) {
            editSearch.setHint(R.string.choose_city_title2);
            if(ShowType.PICK_UP == showType){
                tabLayout.setInland(true);
            }
        } else if ("end".equals(from)) {
            editSearch.setHint(R.string.choose_city_title2);
            if(ShowType.PICK_UP == showType){
                tabLayout.setInland(false);
            }
            //tabLayout.findViewById(R.id.choose_city_tab_foreign_layout);
        } else if (showType == ShowType.SELECT_CITY) {
            editSearch.setHint(R.string.choose_city_title3);
        } else if (mBusinessType == Constants.BUSINESS_TYPE_RENT) {
            editSearch.setHint(R.string.choose_city_title2);
        } else if ("lastCity".equals(from) || "nearby".equals(from)) {
            editSearch.setHint(R.string.choose_city_title2);
        }

        sharedPer = new SharedPre(this);
        mDbManager = new DBHelper(this).getDbManager();
        if(ShowType.PICK_UP == showType){
            resetTab();
        }
        doInBackground();
        requestData();
        setSensorsBuyCityEvent();
    }

    private void resetTab(){
        if(tabLayout.isInland()){
            inlandLineView.setVisibility(View.VISIBLE);
            foreignLineView.setVisibility(View.GONE);
            inlandTV.setTextColor(getResources().getColor(R.color.common_font_color_black));
            foreignTV.setTextColor(getResources().getColor(R.color.common_font_air));
        }else{
            inlandLineView.setVisibility(View.GONE);
            foreignLineView.setVisibility(View.VISIBLE);
            inlandTV.setTextColor(getResources().getColor(R.color.common_font_air));
            foreignTV.setTextColor(getResources().getColor(R.color.common_font_color_black));
        }
    }
    private void requestData() {
        processSelectedData();
        onPreExecuteHandler.sendEmptyMessage(MessageType.ALL_CITY);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            hideSoftInput();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void OnChangeListener(boolean inland) {
        requestData();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String keyword = editSearch.getText().toString().trim();
            if (TextUtils.isEmpty(keyword)) {
                CommonUtils.showToast(R.string.choose_city_check_input);
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
        if (cityList == null) {
            return;
        }
        emptyTV.setVisibility(GONE);
        if(editSearch!=null && editSearch.getText().length()>0){
            headSearchClean.setVisibility(View.VISIBLE);
        }else if(editSearch==null || editSearch.getText().length() == 0){
            headSearchClean.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(s.toString().trim())) {
            mListview.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

            //searchTV.setVisibility(GONE);
            //headerLeftBtn.setVisibility(View.VISIBLE);
            if (showType != ShowType.SELECT_CITY) {
                headerRootView.removeAllViews();
                headerRootView.addView(headerView, UIUtils.getScreenWidth(), FrameLayout.LayoutParams.WRAP_CONTENT);
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

            //searchTV.setVisibility(View.VISIBLE);
            //headerLeftBtn.setVisibility(View.GONE);
            if (showType != ShowType.SELECT_CITY) {
                headerRootView.removeAllViews();
                if (showType == ShowType.PICK_UP) {
                    tabLayout.setVisibility(GONE);
                }
            }
            cityList.clear();
            sideBar.setVisibility(GONE);
            mListview.setAreHeadersSticky(false);

            mAdapter.setSearchPrompt(true);

            List<CityBean> dataList = null;
            if (showType == ShowType.CITY_LIST) {
                String sql = "";
                if (countryId != -1) {
                    sql = DatabaseManager.getCitysByPlaceIdSql(countryId, editSearch.getText().toString().trim(), false, false);
                } else if (groupId != -1) {
                    sql = DatabaseManager.getCitysByGroupIdSql(groupId, editSearch.getText().toString().trim(), false, false);
                }
                dataList = DatabaseManager.getCityBeanList(sql);
            } else {
                dataList = requestDataByKeyword(mBusinessType, groupId, editSearch.getText().toString().trim(), false);
            }
            if (dataList != null) {
                cityList.addAll(dataList);
            }

            if (cityList.size() < 10) {
                List<CityBean> moreDataList = null;
                if (showType == ShowType.CITY_LIST) {
                    String sql = "";
                    if (countryId != -1) {
                        sql = DatabaseManager.getCitysByPlaceIdSql(countryId, editSearch.getText().toString().trim(), true, false);
                    } else if (groupId != -1) {
                        sql = DatabaseManager.getCitysByGroupIdSql(groupId, editSearch.getText().toString().trim(), true, false);
                    }
                    moreDataList = DatabaseManager.getCityBeanList(sql);
                } else {
                    moreDataList = requestDataByKeyword(mBusinessType, groupId, editSearch.getText().toString().trim(), true);
                }
                if (moreDataList!= null && moreDataList.size() > 0) {
                    if (cityList.size() <= 0) {
                        cityList.addAll(moreDataList);
                    } else {
                        for (CityBean cityBean : moreDataList) {
                            for (int i = 0; i < cityList.size(); i++) {
                                if (cityBean.cityId == cityList.get(i).cityId) {
                                    break;
                                }
                                if (i == cityList.size() - 1) {
                                    cityList.add(cityBean);
                                }
                            }
                        }
                    }
                } else if (cityList.size() <= 0) {
                    emptyTV.setVisibility(View.VISIBLE);
                    emptyIV.setVisibility(View.VISIBLE);
                    if (mBusinessType == Constants.BUSINESS_TYPE_DAILY) {
                        if ("lastCity".equals(from) && s.toString().trim().equals(startCityName)) {
                            emptyTV.setText(getString(R.string.can_not_choose_start_city_text));
                        } else if (GROUP_OUTTOWN.equalsIgnoreCase(from) ||GROUP_START.equalsIgnoreCase(from) ) {
                            emptyTV.setText(getString(R.string.out_of_range_city_text2));
                        } else {
                            emptyTV.setText(getString(R.string.out_of_range_city_text));
                        }
                    } else if (mBusinessType == Constants.BUSINESS_TYPE_PICK) {
                        emptyTV.setText(R.string.choose_city_empty_hint);
                        emptyServiceTV.setVisibility(View.VISIBLE);
                        emptyServiceTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //DialogUtil.showServiceDialog(ChooseCityActivity.this, null, UnicornServiceActivity.SourceType.TYPE_DEFAULT, null, null, getEventSource());
                                csDialog = CommonUtils.csDialog(ChooseCityActivity.this, null, null, null, UnicornServiceActivity.SourceType.TYPE_DEFAULT, getEventSource(), new CsDialog.OnCsListener() {
                                    @Override
                                    public void onCs() {
                                        if (csDialog != null && csDialog.isShowing()) {
                                            csDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }
            mAdapter.setData(cityList);
            mListview.setSelection(0);
        }
    }

    @OnClick({R.id.city_choose_btn, R.id.head_search_clean, /*R.id.head_text_right,*/ R.id.header_left_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city_choose_btn:
//                Bundle bundle = new Bundle(getArguments());
//                bundle.putSerializable(KEY_CITY_LIST, chooseCityList);
//                finishForResult(bundle);

                break;
            case R.id.head_search_clean:
                if (TextUtils.isEmpty(editSearch.getText().toString().trim())) {
                    break;
                }
                editSearch.setText("");
                break;
            case R.id.header_left_btn:
                hideInputMethod(editSearch);
                finish();
                break;
            /*case R.id.head_text_right:
                String keyword = editSearch.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    CommonUtils.showToast("请输入搜索内容");
                    return;
                }
                editSearch.setText("");
                searchTV.setVisibility(GONE);
                headerLeftBtn.setVisibility(View.VISIBLE);
                collapseSoftInputMethod(editSearch);
                break;*/
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
        cityBean.fromTag = fromTag;
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
        } else if (showType == ShowType.CITY_LIST) {
            CityActivity.Params params = new CityActivity.Params();
            params.id = cityBean.cityId;
            params.titleName = cityBean.name;
            params.cityHomeType = CityActivity.CityHomeType.CITY;
            Intent intent = new Intent(this, CityActivity.class);
            intent.putExtra(Constants.PARAMS_DATA, params);
            intent.putExtra(Constants.PARAMS_SOURCE, "热门城市页");
            startActivity(intent);
        } else {//包车城市搜索
            saveHistoryDate(cityBean);
            bundle.putSerializable(KEY_CITY, cityBean);
            hideSoftInput();
            if (TextUtils.equals(fromTag, GuidanceOrderActivity.TAG)) {
                GuidanceOrderActivity.Params guidanceParams = (GuidanceOrderActivity.Params) getIntent().getSerializableExtra(GuidanceOrderActivity.PARAMS_GUIDANCE);
                if (guidanceParams == null) {
                    return;
                }
                Intent intent = null;
                switch (guidanceParams.orderType) {
                    case 3:
                    case 888:
                        intent = new Intent(this, CharterFirstStepActivity.class);
                        intent.putExtra(Constants.PARAMS_SOURCE, guidanceParams.source);
                        if (guidanceParams.seckillsBean != null) {
                            intent.putExtra(Constants.PARAMS_SECKILLS, guidanceParams.seckillsBean);
                        }
                        intent.putExtra(Constants.PARAMS_TAG, fromTag);
                        intent.putExtra(Constants.PARAMS_START_CITY_BEAN, cityBean);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(this, SingleActivity.class);
                        SingleActivity.Params singleParams = new SingleActivity.Params();
                        singleParams.cityId = "" + cityBean.cityId;
                        intent.putExtra(Constants.PARAMS_DATA, singleParams);
                        intent.putExtra(Constants.PARAMS_SOURCE, guidanceParams.source);
                        startActivity(intent);
                        break;
                }

            } else {
                finish();
                if ("lastCity".equalsIgnoreCase(from) || GROUP_OUTTOWN.equalsIgnoreCase(from)) {
                    EventBus.getDefault().post(new EventAction(EventType.CHOOSE_END_CITY_BACK, cityBean));
                } else if (null != from && from.equalsIgnoreCase("end")) {
                    EventBus.getDefault().post(new EventAction(EventType.CHOOSE_END_CITY_BACK, cityBean));
                } else if (null != from && from.equalsIgnoreCase("purpose")){
                    EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,cityBean));
                }else {
                    EventBus.getDefault().post(new EventAction(EventType.CHOOSE_START_CITY_BACK, cityBean));
                }
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
        if (GROUP_START.equals(from) || GROUP_OUTTOWN.equals(from) || CITY_LIST.equals(from)) {
            return;
        }
        if (cityHistory == null) cityHistory = new ArrayList<String>();
        cityHistory.remove(String.valueOf(cityBean.cityId));//排重
        cityHistory.add(0, String.valueOf(cityBean.cityId));
        if(showType == ShowType.PICK_UP){
            for (int i = cityHistory.size() - 1; i > 2; i--) {
                cityHistory.remove(i);
            }
        }else{
            for (int i = cityHistory.size() - 1; i > 3; i--) {
                cityHistory.remove(i);
            }
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
            if (historyList != null && historyList.size() > 0) {
                sectionIndices.add("历史");
            }
            if (hotCityList != null && hotCityList.size() > 0) {
                sectionIndices.add("热门");
            }
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
                } else if (msg.obj instanceof ArrayList) {
                    message.obj = msg.obj;
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
                    mAdapter.setSearchPrompt(false);
                    if (showType == ShowType.PICK_UP) {
                        if (tabLayout.isInland()) {//国内城市
                            message.obj = DatabaseManager.getInlandCitySql();
                        } else {//国际港澳台
                            message.obj = DatabaseManager.getAbroadCitySql();
                        }
                        mAdapter.setShowType(ChooseCityAdapter.ShowType.SHOW_COUNTRY);
                    } else if (showType == ShowType.CITY_LIST) {
                        if (countryId != -1) {
                            message.obj = DatabaseManager.getCitysByPlaceIdSql(countryId);
                            mAdapter.setShowType(ChooseCityAdapter.ShowType.DEFAULT);
                        } else if (groupId != -1) {
                            message.obj = DatabaseManager.getCitysByGroupIdSql(groupId);
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
                        message.obj = DatabaseManager.getAllCitySql(mBusinessType, groupId, cityId, from, countryId);
                        if (showType == ShowType.SELECT_CITY) {
                            mAdapter.setShowType(ChooseCityAdapter.ShowType.SELECT_CITY);
                        } else if (showType == ShowType.MULTIPLY && cityId <= 0) {
                            mAdapter.setShowType(ChooseCityAdapter.ShowType.SHOW_COUNTRY);
                        } else {
                            mAdapter.setShowType(ChooseCityAdapter.ShowType.DEFAULT);
                        }
                    }
                    if (showType != ShowType.SELECT_CITY) {
                        onPreExecuteHandler.sendEmptyMessage(MessageType.HOT_CITY);
                        if (showType != ShowType.GROUP_START && showType != ShowType.GROUP_OUTTOWN && showType != ShowType.CITY_LIST) {
                            if(showType != ShowType.PICK_UP){
                                onPreExecuteHandler.sendEmptyMessage(MessageType.SEARCH_HISTORY);
                            }
                            onPreExecuteHandler.sendEmptyMessage(MessageType.LOCATION);
                        }
                    }
                    mAsyncHandler.sendMessage(message);
                    break;
                case MessageType.HOT_CITY:
                    if (showType == ShowType.PICK_UP) {
                        if (tabLayout.isInland()) {//国内城市
//                            message.obj = DatabaseManager.getInlandHotCitySql();
                            message.obj = null;
                        } else {//国际港澳台
                            message.obj = DatabaseManager.getAbroadHotCitySql();
                        }
                    } else if (showType == ShowType.CITY_LIST) {
                        if (countryId != -1) {
                            message.obj = DatabaseManager.getCitysByPlaceIdSql(countryId, null, false, true);
                        } else if (groupId != -1) {
                            message.obj = DatabaseManager.getCitysByGroupIdSql(groupId, null, false, true);
                        }
                    } else {
                        if("lastCity".equals(from) || GROUP_OUTTOWN.equals(from)){
                            message.obj =  CityUtils.requestHotDate(activity, groupId, cityId, from);
                            onPostExecuteHandler.sendMessage(message);
                            break;
                        }else{
                            message.obj = DatabaseManager.getHotDateSql(mBusinessType, groupId, cityId, from, countryId);
                        }
                    }
                    mAsyncHandler.sendMessage(message);
                    break;
                case MessageType.SEARCH_HISTORY:
                    if (showType == ShowType.GROUP_START || showType == ShowType.GROUP_OUTTOWN) {
                        break;
                    }
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
                    Selector selector = DatabaseManager.getHistoryDateSql(mBusinessType, groupId, cityId, from, cityHistory);
                    try {
                        List<CityBean> list = selector.findAll();
                        List<CityBean> historyList = new ArrayList<CityBean>();
                        if (list == null || list.size() < cityHistory.size()) {
                            message.obj = selector;
                        } else {
                            for (int i = 0; i < cityHistory.size(); i++) {
                                c:for (int y = 0; y < list.size(); y++) {
                                    CityBean cityBean = list.get(y);
                                    if (cityHistory.get(i).equals(cityBean.cityId + "")) {
                                        historyList.add(cityBean);
                                        break c;
                                    }
                                }
                            }
                        }
                        message.obj = historyList;
                    } catch (Exception e) {
                        message.obj = selector;
                    }
                    mAsyncHandler.sendMessage(message);
                    break;
                case MessageType.LOCATION:
                    String cityId = sharedPer.getStringValue("cityId");
                    String cityName = sharedPer.getStringValue("cityName");
                    if (showType == ShowType.PICK_UP) {
                        if (!TextUtils.isEmpty(cityId) && !TextUtils.isEmpty(cityName)) {
                            CityBean locationCityBean = DBHelper.findCityById(cityId);
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
                    if (mDialogUtil != null) {
                        mDialogUtil.dismissLoadingDialog();
                    }
                    if (msg.obj == null) {
                        break;
                    }
                    cityList = (List<CityBean>) msg.obj;
                    if (cityList.size() <= 0) {
                        emptyLayout.setVisibility(View.VISIBLE);
                        emptyTV.setVisibility(View.VISIBLE);
                        emptyIV.setVisibility(View.VISIBLE);
                        emptyTV.setText(R.string.choose_city_overstep_range);
                    } else {
                        mAdapter.setData(cityList);
                        mListview.setSelection(0);
                        setSectionIndices();
                        emptyLayout.setVisibility(View.GONE);
                    }
                    break;
                case MessageType.HOT_CITY:
                    if (msg.obj == null) {
                        hotCityList = null;
                        headerView.setHotCitysData(hotCityList);
                        setSectionIndices();
                        break;
                    }
                    hotCityList = (List<CityBean>) msg.obj;
                    headerView.setHotCitysData(hotCityList);
                    setSectionIndices();
                    break;
                case MessageType.SEARCH_HISTORY:
                    if (msg.obj == null) {
                        break;
                    }
                    if(showType == ShowType.PICK_UP){
                        historyList = (List<CityBean>) msg.obj;
                        if(locationAndHistory.size() >0){
                            //locationAndHistory.clear();
                            locationAndHistory.addAll(historyList);
                            headerView.setHistoryData(locationAndHistory);
                        }
                        setSectionIndices();
                    }else{
                        historyList = (List<CityBean>) msg.obj;
                        headerView.setHistoryData(historyList);
                        setSectionIndices();
                    }

                    break;
                case MessageType.LOCATION:
                    CityBean cityBean = null;
                    if (msg.obj instanceof CityBean) {
                        cityBean = (CityBean) msg.obj;
                    }

                    if(showType == ShowType.PICK_UP) {
                        headerView.setLociationData(cityBean, showType == ShowType.PICK_UP);
                        //locationAndHistory = new ArrayList<CityBean>();
                        if (locationAndHistory != null) {
                            locationAndHistory.clear();
                        }
                        locationAndHistory.add(0, cityBean);
                        headerView.setHistoryData(locationAndHistory);

                        if (showType != ShowType.GROUP_START && showType != ShowType.GROUP_OUTTOWN && showType != ShowType.CITY_LIST) {
                            onPreExecuteHandler.sendEmptyMessage(MessageType.SEARCH_HISTORY);
                        }
                        setSectionIndices();
                    }else{
                        headerView.setLociationData(cityBean, showType == ShowType.PICK_UP);
                    }

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
            if (GROUP_START.equals(from)) {
                selector.and("is_daily", "=", 1);
                selector.and("group_id", "=", groupId);
                if (cityId != -1) {
                    selector.and("city_id", "<>", cityId);
                }
            } else {
                if (groupId == -1) {
                    selector.and("is_daily", "=", 1);
                } else {
                    selector.and("group_id", "=", groupId);
                }
                if ("lastCity".equals(from) && cityId != -1) {
                    selector.and("city_id", "<>", cityId);
                } else if (GROUP_OUTTOWN.equals(from) && cityId != -1) {
                    selector.and("city_id", "<>", cityId);
                }
            }
        } else if (orderType == Constants.BUSINESS_TYPE_RENT) {
            selector.and("is_single", "=", 1);
        } else if (orderType == Constants.BUSINESS_TYPE_PICK || orderType == Constants.BUSINESS_TYPE_SEND) {
            selector.and("is_city_code", "=", 1);
        }
        if (orderType != Constants.BUSINESS_TYPE_PICK && orderType != Constants.BUSINESS_TYPE_SEND) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("place_name", "<>", "中国");
            whereBuilder.and("place_name", "<>", "中国大陆");
            selector.and(whereBuilder);
        }
        try {
            dataList = selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (dataList != null && dataList.size() > 0) {
            for (CityBean cb : dataList) {
                cb.keyWord = keyword;
            }
        }
        return dataList;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
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

    //来到选城市页
    private void setSensorsBuyCityEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_refer", getIntentSource());
            SensorsDataAPI.sharedInstance(this).track("buy_city", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
