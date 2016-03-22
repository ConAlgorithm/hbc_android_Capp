package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.ArrivalSearchAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ArrivalBean;
import com.hugboga.custom.data.request.RequestArrivalSearch;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 目的地列表 ，要去哪
 */
@ContentView(R.layout.fg_arrival_search)
public class FgArrivalSearch extends BaseFragment implements AdapterView.OnItemClickListener, View.OnKeyListener , ZListView.OnRefreshListener, ZListView.OnLoadListener{

    public static final String KEY_ARRIVAL = "arrival";


    @ViewInject(R.id.country_lvcountry)
    private ZListView sortListView;

    @ViewInject(R.id.head_search)
    private EditText editSearch;

    @ViewInject(R.id.arrival_empty_layout)
    private View emptyView;
    @ViewInject(R.id.arrival_empty_layout_text)
    private TextView emptyViewText;
    @ViewInject(R.id.arrival_tip)
    private TextView arrivalTip;

    public static final String KEY_CITY_ID ="key_city_id";
    public static final String KEY_LOCATION ="location";

    public ArrivalSearchAdapter adapter;
    private long t = 0;
    private List<ArrivalBean> sourceDateList;
    private int cityId;
    private String location;
    private int PAGESIZE = 20;
    private String searchWord="";
    private SharedPre sharedPre;
    private ArrayList<String> placeHistoryArray;


    @Override
    protected void initHeader() {
        cityId = getArguments().getInt(KEY_CITY_ID, -1);
        location = getArguments().getString(KEY_LOCATION);
        sharedPre = new SharedPre(getActivity());
//        fgTitle.setText("搜索目的地");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        initSearchTip(getBusinessType());
        editSearch.setOnKeyListener(this);
        editSearch.requestFocus();
        showSoftInputMethod(editSearch);
        adapter = new ArrivalSearchAdapter(getActivity());
        sortListView.setAdapter(adapter);
        sortListView.setOnItemClickListener(this);
        sortListView.setonLoadListener(this);
        sortListView.setonRefreshListener(this);
        sortListView.getHeadView().setVisibility(View.GONE);
        sortListView.onLoadCompleteNone();
        sortListView.setVisibility(View.VISIBLE);
        initHistoryDate();
        return null;
    }

    /**
     * 初始化搜索框提示内容
     *
     * @param type
     */
    public void initSearchTip(Integer type) {
        switch (type) {
            case Constants.BUSINESS_TYPE_PICK:
                //接机
                editSearch.setHint(getResources().getString(R.string.search_hint_pick));
                break;
            case Constants.BUSINESS_TYPE_SEND:
                //送机
                editSearch.setHint(getResources().getString(R.string.search_hint_send));
                break;
            case Constants.BUSINESS_TYPE_DAILY:
                //日租
                break;
            case Constants.BUSINESS_TYPE_RENT:
                //次租
                String from = getArguments().getString(KEY_FROM);
                if (from.equals("from")) {
                    editSearch.setHint(getResources().getString(R.string.search_hint_send));
                } else if (from.equals("to")) {
                    editSearch.setHint(getResources().getString(R.string.search_hint_pick));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化搜索历史记录
     */
    private void initHistoryDate(){
//        @color/item_title_bg
      String placeHistoryStr =  sharedPre.getStringValue(mBusinessType+ SharedPre.RESOURCES_PLACE_HISTORY);
        placeHistoryArray = new ArrayList<String>();
        ArrayList<ArrivalBean> historyList = new ArrayList<ArrivalBean>();
        ArrivalBean bean;
        if(placeHistoryStr!=null){
            for(String place:placeHistoryStr.split(",")){
                bean = new ArrivalBean();
                bean.placeName = place;
                bean.isHistory = true;
                historyList.add(bean);
                placeHistoryArray.add(place);
            }
            adapter.setList(historyList);
            if(historyList.size()>0)
            arrivalTip.setTextColor(0xff242424);
            arrivalTip.setText("搜索历史");
            return;
        }
        arrivalTip.setTextColor(getActivity().getResources().getColor(R.color.basic_rent_toolbar_color));
        arrivalTip.setText(R.string.arrival_tip_hotel);
    }

    private void requestKeyword(int offset) {
        RequestArrivalSearch requestArrivalSearch = new RequestArrivalSearch(getActivity(),cityId,location, searchWord,offset,PAGESIZE);
        requestData(requestArrivalSearch);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestArrivalSearch) {
            RequestArrivalSearch requestArrivalSearch = (RequestArrivalSearch) request;
            ArrayList<ArrivalBean> dateList = requestArrivalSearch.getData();//listDate;
            sortListView.setEmptyView(emptyView);
            if (TextUtils.isEmpty(editSearch.getText())) {
                dateList = null;
            }
            inflateContent();
            sortListView.getMoreView().setVisibility(View.VISIBLE);
            if(dateList!=null&&dateList.size()<PAGESIZE){
                sortListView.onLoadCompleteNone();
            }else {
                sortListView.onLoadComplete();
            }
            if(sortListView.state== ZListView.LOADING_MORE){
                if(dateList!=null)
                    adapter.addList(dateList);
            }else{
                adapter.setList(dateList);
                sortListView.onRefreshComplete();
            }
//            emptyViewText.setText(getString(R.string.arrival_empty_text,searchWord));
        }
    }

    @Override
    protected void inflateContent() {

    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.head_text_right:
//                search(); //进行点击搜索
//                break;
//            default:
//                super.onClick(view);
//                break;
//        }
//    }

    @Event({R.id.head_search_clean, R.id.head_text_right})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.head_search_clean:
                editSearch.setText("");
                break;
            case R.id.head_text_right:
                search(); //进行点击搜索
                break;
            default:
//                super.onClick(view);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrivalBean bean = (ArrivalBean)adapter.getItem(position-1);
        if(bean!=null){
            if(bean.isHistory){
                editSearch.setText(bean.placeName);
                search();
            }else{
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_ARRIVAL, bean);
                finishForResult(bundle);
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER)) {
            search();
            return true;
        }
        return false;
    }

    /**
     * 根据输入字符进行搜索
     */
    private void search() {
        arrivalTip.setTextColor(getActivity().getResources().getColor(R.color.basic_rent_toolbar_color));
        searchWord = editSearch.getText().toString();
        if (!TextUtils.isEmpty(searchWord)&&!TextUtils.isEmpty(searchWord.trim())) {
            searchWord = searchWord.trim();
            saveHistoryDate(searchWord);
            onRefresh();
            arrivalTip.setText(R.string.arrival_tip_same);
        } else {
            arrivalTip.setText(R.string.arrival_tip_hotel);
            sourceDateList = null;
            editSearch.setText("");
            adapter.setList(sourceDateList);
            initHistoryDate();
        }

    }
    /**
     * 存储历史
     * @param keyword
     */
    private void saveHistoryDate(String keyword) {
        placeHistoryArray.remove(keyword);//排重
        placeHistoryArray.add(0,keyword);
        for(int i=placeHistoryArray.size()-1;i>2;i--){
            placeHistoryArray.remove(i);
        }
        sharedPre.saveStringValue(mBusinessType+ SharedPre.RESOURCES_PLACE_HISTORY, TextUtils.join(",", placeHistoryArray));
    }
    @Override
    public void onLoad() {
        requestKeyword(adapter.getCount());
    }

    @Override
    public void onRefresh() {
        sortListView.state = ZListView.RELEASE_To_REFRESH;
        requestKeyword(0);
    }
}
