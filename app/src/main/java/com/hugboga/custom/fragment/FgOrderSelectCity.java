package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.OrderSelectCityAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.SavedCityBean;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.DBCityUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.ScreenUtils;
import com.hugboga.custom.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.w3c.dom.Text;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created  on 16/4/14.
 */
@ContentView(R.layout.activity_order_select_city)
public class FgOrderSelectCity extends BaseFragment implements NumberPicker.OnValueChangeListener, NumberPicker.Formatter {


    @ViewInject(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @ViewInject(R.id.header_title)
    TextView headerTitle;
    @ViewInject(R.id.header_right_btn)
    ImageView headerRightBtn;
    @ViewInject(R.id.header_right_txt)
    TextView headerRightTxt;
    @ViewInject(R.id.start_city_click)
    TextView startCityClick;
    @ViewInject(R.id.full_day)
    RadioButton fullDay;
    @ViewInject(R.id.half_day)
    RadioButton halfDay;
    @ViewInject(R.id.people_text_click)
    TextView peopleTextClick;
    @ViewInject(R.id.child_text)
    TextView childText;
    @ViewInject(R.id.show_child_seat_layout)
    LinearLayout showChildSeatLayout;
    @ViewInject(R.id.child_no_confirm_click)
    ImageView childNoConfirmClick;
    @ViewInject(R.id.baggage_text_click)
    TextView baggageTextClick;
    @ViewInject(R.id.baggage_no_confirm_click)
    ImageView baggageNoConfirmClick;
    @ViewInject(R.id.start_date)
    TextView startDate;
    @ViewInject(R.id.start_layout_click)
    LinearLayout startLayoutClick;
    @ViewInject(R.id.end_date)
    TextView endDate;
    @ViewInject(R.id.end_layout_click)
    LinearLayout endLayoutClick;
    @ViewInject(R.id.go_city_text_click)
    TextView goCityTextClick;
    @ViewInject(R.id.next_btn_click)
    Button nextBtnClick;
    @ViewInject(R.id.half_day_show)
    LinearLayout half_day_show;

    @ViewInject(R.id.full_day_show)
    LinearLayout full_day_show;

    @ViewInject(R.id.minus)
    TextView minus;
    @ViewInject(R.id.add)
    TextView add;

    @ViewInject(R.id.full_day_date_layout)
    LinearLayout full_day_date_layout;

    @Override
    protected void inflateContent() {
    }

    @Override
    protected void initView() {
        initHeader();
        initSelectPeoplePop();
        fullDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showFull();
                }
            }
        });
        halfDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showHalf();
                }
            }
        });
//        showSaveInfo();
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAddinfo()){
                    showSaveDialog();
                }else{
                    finish();
                }
            }
        });
    }

    private void showSaveDialog(){
        android.support.v7.app.AlertDialog dialog =  AlertDialogUtils.showAlertDialog(getContext(), "离开当前页面，所选行程将会丢失，是否继续", "是", "否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                saveInfo();
                dialog.dismiss();
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                try {
//                    Reservoir.clear();
//                    finish();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if(isAddinfo()){
            showSaveDialog();
            return true;
        }else {
            return super.onBackPressed();
        }
    }

    private void showFull(){
        half_day_show.setVisibility(View.GONE);
        full_day_show.setVisibility(View.VISIBLE);
        full_day_date_layout.setVisibility(View.VISIBLE);
        isHalfTravel = false;
        checkNextBtnStatus();
    }

    private void showHalf(){
        half_day_show.setVisibility(View.VISIBLE);
        full_day_show.setVisibility(View.GONE);
        full_day_date_layout.setVisibility(View.GONE);
        isHalfTravel = true;
        checkNextBtnStatus();
    }


    private void disableNextBtn(){
        nextBtnClick.setEnabled(false);
        nextBtnClick.setBackgroundColor(Color.parseColor("#d5dadb"));
    }

    private void enableNextBtn(){
        nextBtnClick.setEnabled(true);
        nextBtnClick.setBackgroundColor(ContextCompat.getColor(this.getActivity(),R.color.all_bg_yellow));
    }

    public void checkNextBtnStatus() {
        if(null == startBean){
            disableNextBtn();
            return;
        }

        if(TextUtils.isEmpty(peopleTextClick.getText())){
            disableNextBtn();
            return;
        }

        if(TextUtils.isEmpty(baggageTextClick.getText())){
            disableNextBtn();
            return;
        }

        if(isHalfTravel){
            if(TextUtils.isEmpty(halfDate)){
                disableNextBtn();
                return;
            }
        }else{
            if(TextUtils.isEmpty(start_date_str)){
                disableNextBtn();
                return;
            }
            if(TextUtils.isEmpty(end_date_str)){
                disableNextBtn();
                return;
            }
            if(passBeanList.size() != nums){
                disableNextBtn();
                return;
            }
        }
        enableNextBtn();
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    protected void initHeader() {
        fgTitle.setText(R.string.select_city_title);
        source = getArguments().getString("source");
    }

    public String format(int value) {
        return value + "";
    }

    //初始化人数,座位选择
    private void init() {
        manList.setFormatter(this);
        manList.setOnValueChangedListener(this);
        manList.setMaxValue(11);
        manList.setMinValue(1);
        manList.setValue(manNum == 0?1:manNum);
        manList.setClickable(false);
        manList.setFocusable(false);
        manList.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        childList.setFormatter(this);
        childList.setOnValueChangedListener(this);
        childList.setMaxValue(11);
        childList.setMinValue(0);
        childList.setValue(childNum);
        childList.setClickable(false);
        childList.setFocusable(false);
        childList.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        baggageList.setFormatter(this);
        baggageList.setOnValueChangedListener(this);
        baggageList.setMaxValue(11);
        baggageList.setMinValue(0);
        baggageList.setValue(baggageNum);
        baggageList.setClickable(false);
        baggageList.setFocusable(false);
        baggageList.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    }

    //市外天数
    private int getOutNum(){
        int outNums = 0;
        for(int i = 0;i<passBeanList.size();i++){
            if(passBeanList.get(i).cityType == 3) {
                outNums++;
            }
        }
        return outNums;
    }
    //市内天数
    private int getInNum(){
        return passBeanList.size() - getOutNum();
    }

    int manNum = 0;
    int childNum = 0;
    int childSeatNums = 0;
    int baggageNum = 0;

    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.man_list:
                manNum = newVal;
                break;
            case R.id.child_list:
                childNum = newVal;
                break;
            case R.id.baggage_list:
                baggageNum = newVal;
                break;
        }

    }

    private PopupWindow peoplePop;
    private View view;
    OrderSelectCityAdapter adapter;
    TextView cancle;
    TextView ok;
    TextView title;
    NumberPicker manList;
    NumberPicker childList;
    NumberPicker baggageList;

    LinearLayout mans_layout;

    LinearLayout scope_layout, scope_layout_in, scope_layout_out, scope_layout_other;
    TextView out_title, in_title, other_title;
    TextView out_tips, in_tips;

//    List<String> passCitiesList = new ArrayList<>();
    List<CityBean> passBeanList = new ArrayList<>();

    //添加经过城市
    public void addPassCityBean(int type,CityBean cityBean,String tag){
        int index = Integer.valueOf(tag);
        CityBean newCityBean = (CityBean)cityBean.clone();
        newCityBean.cityType = type;
        if(index > passBeanList.size()) {
            passBeanList.add(newCityBean);
        }else{
            passBeanList.set(index-1,newCityBean);
        }
    }

    //1,市内 2,周边 3,其它城市
    private void setDayText(int type,CityBean cityBean){
        int tag = Integer.valueOf(currentClickView.getTag().toString());
        if(tag < full_day_show.getChildCount()) {
            for(int i = tag+1;i<full_day_show.getChildCount();i++) {
               View view =  full_day_show.getChildAt(i);
                view.setTag(null);

                TextView endText = (TextView)view.findViewById(R.id.day_go_city_text_click);
                TextView end_add_tips = (TextView)view.findViewById(R.id.add_tips);

                endText.setText("选择包车游玩范围");
                end_add_tips.setVisibility(View.GONE);
                view.setBackgroundColor(Color.parseColor("#d3d4d5"));
                if(tag < passBeanList.size()) {
                    passBeanList.remove(tag -1);
                }
            }
        }

        TextView text = (TextView)currentClickView.findViewById(R.id.day_go_city_text_click);
        TextView add_tips = (TextView)currentClickView.findViewById(R.id.add_tips);
        String cityId = startBean.cityId+"";
        if(type == 1) {
            text.setText(startBean.name + "市内");
            add_tips.setVisibility(View.GONE);
            addPassCityBean(1,cityBean,currentClickView.getTag().toString());
        }else if(type == 2){
            text.setText(startBean.name + "周边");
            add_tips.setVisibility(View.VISIBLE);
            add_tips.setText(R.string.select_around_city);
            addPassCityBean(2,cityBean,currentClickView.getTag().toString());
        }else if(type == 3){
            cityId = cityBean.cityId+"";
            text.setText(cityBean.name);
            add_tips.setVisibility(View.VISIBLE);
            add_tips.setText(R.string.select_other_city);
            addPassCityBean(3,cityBean,currentClickView.getTag().toString());
        }
//        if(passCitiesList.size() > tag-1){
//            passCitiesList.set((tag - 1), cityId + "-1" + "-" + type);
//        }else{
//            passCitiesList.add((tag - 1), cityId + "-1" + "-" + type);
//        }
        View view = full_day_show.getChildAt(tag);
        if(null != view && null == view.getTag()) {
            view.setTag(tag + 1);
            TextView endText = (TextView)view.findViewById(R.id.day_go_city_text_click);
            TextView end_add_tips = (TextView)view.findViewById(R.id.add_tips);
            if(type == 3 && (tag+1) == nums){
                endText.setText(R.string.select_end_city);
            }else if(type == 3 && (tag+1) != nums){
                endText.setText(R.string.select_stay_city);
            }else{
                endText.setText(R.string.select_scope);
                end_add_tips.setVisibility(View.GONE);
            }
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        }else if(null != view && null != view.getTag()){
            TextView endText = (TextView)view.findViewById(R.id.day_go_city_text_click);
            TextView end_add_tips = (TextView)view.findViewById(R.id.add_tips);
            if(type == 3 && (tag+1) == nums){
                endText.setText(R.string.select_end_city);
            }else if(type == 3 && (tag+1) != nums){
                endText.setText(R.string.select_stay_city);
            }else{
                endText.setText(R.string.select_scope);
                end_add_tips.setVisibility(View.GONE);
            }
        }else{
            endCityId = cityId;
        }
        checkNextBtnStatus();

    }

    //途径城市
    String passCities = "";
    private void initSelectPeoplePop() {
        view = LayoutInflater.from(this.getActivity()).inflate(R.layout.pop_select_people, null);
        scope_layout = (LinearLayout) view.findViewById(R.id.scope_layout);
        scope_layout_in = (LinearLayout) view.findViewById(R.id.scope_layout_in);
        scope_layout_out = (LinearLayout) view.findViewById(R.id.scope_layout_out);
        scope_layout_other = (LinearLayout) view.findViewById(R.id.scope_layout_other);
        out_title = (TextView) view.findViewById(R.id.out_title);
        in_title = (TextView) view.findViewById(R.id.in_title);
        other_title = (TextView) view.findViewById(R.id.other_title);
        out_tips = (TextView) view.findViewById(R.id.out_tips);
        in_tips = (TextView) view.findViewById(R.id.in_tips);
        scope_layout_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayText(1,startBean);
                hideSelectPeoplePop();
            }
        });
        scope_layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayText(2,startBean);
                hideSelectPeoplePop();
            }
        });
        scope_layout_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM, "end");
                bundle.putString("source", "首页");
                bundle.putInt(FgChooseCity.KEY_CITY_ID, startBean.cityId);
                startFragment(new FgChooseCity(), bundle);
                hideSelectPeoplePop();
            }
        });

        title = (TextView) view.findViewById(R.id.title);
        childList = (NumberPicker) view.findViewById(R.id.child_list);
        manList = (NumberPicker) view.findViewById(R.id.man_list);
        baggageList = (NumberPicker) view.findViewById(R.id.baggage_list);
        mans_layout = (LinearLayout) view.findViewById(R.id.mans_layout);
        init();
        cancle = (TextView) view.findViewById(R.id.cancle);
        ok = (TextView) view.findViewById(R.id.ok);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSelectPeoplePop();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (baggageList.isShown()) {
                    baggageTextClick.setText(String.format(getString(R.string.select_city_baggage_num), baggageNum));
                    baggageTextClick.setTextColor(Color.parseColor("#000000"));
                } else {
                    if (childNum > 0) {
                        showChildSeatLayout.setVisibility(View.VISIBLE);
                    } else {
                        showChildSeatLayout.setVisibility(View.GONE);
                    }
                    if(manNum == 0) manNum = 1;
                    peopleTextClick.setText(String.format(getString(R.string.select_city_man_child_num), manNum, childNum));
                    peopleTextClick.setTextColor(Color.parseColor("#000000"));
                }
                checkNextBtnStatus();
                hideSelectPeoplePop();
            }
        });
        peoplePop = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        peoplePop.setBackgroundDrawable(new BitmapDrawable());
        peoplePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        peoplePop.setOutsideTouchable(true);
        peoplePop.setFocusable(true);
    }

    //type 1,选人 2,行李 3,选城市范围
    public void showSelectPeoplePop(int type) {
        if (null != peoplePop) {
            if (type == 2) {
                baggageList.setVisibility(View.VISIBLE);
                mans_layout.setVisibility(View.GONE);
                scope_layout.setVisibility(View.GONE);
                title.setText(R.string.select_baggage_tips);
                ok.setVisibility(View.VISIBLE);
                cancle.setVisibility(View.VISIBLE);
            } else if (type == 1) {
                baggageList.setVisibility(View.GONE);
                mans_layout.setVisibility(View.VISIBLE);
                scope_layout.setVisibility(View.GONE);
                title.setText(R.string.select_people_tips);
                ok.setVisibility(View.VISIBLE);
                cancle.setVisibility(View.VISIBLE);
            } else if (type == 3) {
                baggageList.setVisibility(View.GONE);
                mans_layout.setVisibility(View.GONE);
                scope_layout.setVisibility(View.VISIBLE);
                ok.setVisibility(View.GONE);
                cancle.setVisibility(View.GONE);
                title.setText(R.string.select_scope);
            }
            peoplePop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }

    public void hideSelectPeoplePop() {
        if (null != peoplePop) {
            peoplePop.dismiss();
        }
    }

    CityBean startBean;
    CityBean endBean;
    String scope_in_str = "";
    String scope_around_str = "";
    String scope_other_str = "";


    String startCity = "";
    String endCityId = "";


    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCity.class.getSimpleName().equals(from)) {
            String fromKey = bundle.getString(KEY_FROM);
            if ("startAddress".equals(fromKey)) {
                startBean = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
                endBean = startBean;
                if (!startCity.equalsIgnoreCase(startBean.name)) {
                    startCity = startBean.name;
                    endCityId = startBean.cityId+"";
                    startCityClick.setText(startCity);
                    startCityClick.setTextColor(Color.parseColor("#000000"));
                    DBCityUtils dbCityUtils = new DBCityUtils();
//                    cityBeanList = dbCityUtils.requestDataByKeyword(startBean.name, false);
//                    initScopeLayoutValue(cityBeanList);
                    initScopeLayoutValue();
                    addDayView(true);

                }
            } else if ("end".equalsIgnoreCase(fromKey)) {
                endBean = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
                setDayText(3,endBean);
            }
            checkNextBtnStatus();
        }
    }


    public void initScopeLayoutValue() {
            scope_in_str = String.format(getString(R.string.scope_around), "住在" + startBean.name );
            scope_around_str = String.format(getString(R.string.scope_in), "住在" + startBean.name );
            scope_other_str = "住在其它城市";

            out_title.setText(scope_in_str);
            in_title.setText(scope_around_str);
            other_title.setText(scope_other_str);
            out_tips.setText(startBean.neighbourTip);
            in_tips.setText(startBean.dailyTip);
    }

    SavedCityBean savedCityBean = null;
    private  void showSaveInfo(){
        try{
            savedCityBean = Reservoir.get("savedCityBean", SavedCityBean.class);
            if(null != savedCityBean){
                startBean = savedCityBean.startCity;
                initScopeLayoutValue();
                if(null != startBean) {
                    startCity = startBean.cityId+"";
                    startCityClick.setText(startBean.name);
                }
                manNum = savedCityBean.mansNum;
                childNum = savedCityBean.childNum;
                if(manNum != 0) {
                    peopleTextClick.setText("成人" + manNum + "/儿童" + childNum);
                }
                if(childNum != 0){
                    showChildSeatLayout.setVisibility(View.VISIBLE);
                }else{
                    showChildSeatLayout.setVisibility(View.GONE);
                }

                baggageNum = savedCityBean.baggages;
                if(0!= baggageNum) {
                    baggageTextClick.setText("托运行李" + baggageNum);
                }

                childSeatNums = savedCityBean.childSeat;
                if(0 != childSeatNums) {
                    childText.setText("儿童" + childSeatNums);
                }
                start_date_str = savedCityBean.startDate;

                if(!TextUtils.isEmpty(start_date_str)) {
                    startDate.setText(start_date_str);
                }
                end_date_str = savedCityBean.endDate;

                if(!TextUtils.isEmpty(end_date_str)) {
                    endDate.setText(end_date_str);
                }

                isHalfTravel = savedCityBean.isHalf == 1?true:false;
                if(isHalfTravel){
                    halfDay.setChecked(true);
                    showHalf();
                    if(null != startBean){
                        endCityId = startBean.cityId+"";
                        endBean = startBean;
                    }
                }else{
                    fullDay.setChecked(true);
                    showFull();
                    passBeanList = savedCityBean.passCityList;
                    if(null != passBeanList && passBeanList.size() >0){
                        endBean = passBeanList.get(passBeanList.size()-1);
                        endCityId = endBean.cityId+"";
                    }
                }
                halfDate = savedCityBean.halfStartDate;
                goCityTextClick.setText(halfDate);

                addDayView(false);
                init();
                checkNextBtnStatus();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveInfo(){
        SavedCityBean savedCityBean = new SavedCityBean();
        savedCityBean.startCity = startBean;
        savedCityBean.baggages = baggageNum;
        savedCityBean.childSeat = childSeatNums;
        savedCityBean.childNum = childNum;
        savedCityBean.startDate = isHalfTravel?"":start_date_str;
        savedCityBean.endDate = isHalfTravel?"":end_date_str;
        savedCityBean.halfStartDate = halfDate;
        savedCityBean.mansNum = manNum;
        savedCityBean.isHalf = isHalfTravel?1:0;
        savedCityBean.passCityList = isHalfTravel?null:passBeanList;

        try {
            Reservoir.put("savedCityBean", savedCityBean);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isAddinfo(){
        if(null != startBean || !TextUtils.isEmpty(peopleTextClick.getText())
                || !TextUtils.isEmpty(baggageTextClick.getText())
                || !TextUtils.isEmpty(halfDate)
                || !TextUtils.isEmpty(start_date_str)
                || !TextUtils.isEmpty(end_date_str)){
            return true;
        }
        return false;
    }

    boolean isHalfTravel = false;

    @Event({R.id.minus, R.id.add, R.id.header_left_btn, R.id.start_city_click, R.id.people_text_click, R.id.show_child_seat_layout, R.id.child_no_confirm_click, R.id.baggage_text_click, R.id.baggage_no_confirm_click, R.id.start_layout_click, R.id.end_layout_click, R.id.go_city_text_click, R.id.next_btn_click})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.minus:
                if (childSeatNums >= 1) {
                    childSeatNums--;
                    childText.setText(getString(R.string.select_city_child) + childSeatNums);
                }
                break;
            case R.id.add:
                if (childSeatNums <= 10) {
                    childSeatNums++;
                    childText.setText(getString(R.string.select_city_child) + childSeatNums);
                }
                break;
            case R.id.start_city_click:
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM, "startAddress");
                bundle.putString("source", "首页");
                startFragment(new FgChooseCity(), bundle);
                break;
            case R.id.people_text_click:
                showSelectPeoplePop(1);
                break;
            case R.id.show_child_seat_layout:
                break;
            case R.id.child_no_confirm_click:
                AlertDialogUtils.showAlertDialog(this.getActivity(), getString(R.string.man_no_confirm_tips));
                break;
            case R.id.baggage_text_click:
                showSelectPeoplePop(2);
                break;
            case R.id.baggage_no_confirm_click:
                AlertDialogUtils.showAlertDialog(this.getActivity(), getString(R.string.baggage_no_confirm_tips));
                break;
            case R.id.start_layout_click:
                showDaySelect(startDate);
                savedCityBean = null;
                break;
            case R.id.end_layout_click:
                showDaySelect(endDate);
                savedCityBean = null;
                break;
            case R.id.go_city_text_click:
                showDaySelect(goCityTextClick);
                break;
            case R.id.next_btn_click:
                Bundle bundleCar = new Bundle();
                bundleCar.putString("startCityId",startBean.cityId+"");
                bundleCar.putString("endCityId",isHalfTravel?(startBean.cityId+""):endCityId);
                bundleCar.putString("startDate",isHalfTravel?(halfDate):(start_date_str));
                bundleCar.putString("endDate",isHalfTravel?(halfDate):(end_date_str));
                bundleCar.putString("halfDay",isHalfTravel?"1":"0");
                bundleCar.putString("adultNum",manNum+"");
                bundleCar.putString("childrenNum",childNum+"");
                bundleCar.putString("childseatNum",childSeatNums+"");
                bundleCar.putString("luggageNum",baggageNum+"");
                bundleCar.putString("passCities",isHalfTravel?"":getPassCities());

                bundleCar.putString("startCityName",startBean.name);
                bundleCar.putString("dayNums",nums+"");
                bundleCar.putParcelable("startBean",startBean);
                bundleCar.putParcelable("endBean",endBean);
                bundleCar.putInt("outnum",getOutNum());
                bundleCar.putInt("innum",getInNum());


                FGSelectCar fgSelectCar = new FGSelectCar();
                fgSelectCar.setArguments(bundleCar);
                startFragment(fgSelectCar);
                try{
                    Reservoir.clear();
                }catch (Exception e){
                    e.printStackTrace();
                }
                //统计,这代码应该加到点击事件方法的最后边
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("source", source);
//                map.put("begincity", begincity);
                MobclickAgent.onEventValue(getActivity(), "chosecar_oneday", map, 1);
                break;
        }
    }

    private String getPassCities(){
        passCities= "";
        for(int i = 0;i<passBeanList.size();i++){
            if(i != passBeanList.size() -1) {
                passCities += passBeanList.get(i).cityId+"-1-"+passBeanList.get(i).cityType+",";
            }else{
                passCities += passBeanList.get(i).cityId+"-1-"+passBeanList.get(i).cityType;
            }
        }
        return passCities;
    }

    //已选择的多少天数
    int oldNum = 0;
    int nums = 0;
    public void addDayView(boolean resetCity) {
        if (isHalfTravel
                || TextUtils.isEmpty(startCity)
                || TextUtils.isEmpty(start_date_str)
                || TextUtils.isEmpty(end_date_str)) {
            return;
        }
        if(resetCity){
            full_day_show.removeAllViews();
//            passCitiesList.clear();
            passBeanList.clear();;
            oldNum = 0;
        }


        full_day_show.setVisibility(View.VISIBLE);
        long days = DateUtils.getDistanceDays(start_date_str, end_date_str);
        nums = (int) days + 1;
        if (nums > oldNum) {
            if (oldNum == 0) {
                oldNum = nums;
                for (int i = 1; i <= nums; i++) {
                    genDayViews(i);
                }
            } else {
                for (int i = oldNum + 1; i <= nums; i++) {
                    genDayViews(i);
                }
                oldNum = nums;
            }
        } else if (nums < oldNum) {
            for (int i = oldNum; i > nums; i--) {
                removeDayLayout(i - 1);
            }
            oldNum = nums;
        } else {

        }

    }

    View dayView;
    TextView day_text, day_go_city_text_click;

    View currentClickView = null;
    //生成经过城市列表
    private void genDayViews(int index) {
        dayView = LayoutInflater.from(this.getActivity()).inflate(R.layout.add_day_item, null);
        day_text = (TextView) dayView.findViewById(R.id.day_text);
        day_go_city_text_click = (TextView) dayView.findViewById(R.id.day_go_city_text_click);
        day_text.setText("第" + index + "天");
        if(passBeanList.size() >=  index){
            if(passBeanList.get(index - 1).cityType == 1){
                day_go_city_text_click.setText(String.format(getString(R.string.scope_in), passBeanList.get(index - 1).name));
            }else if(passBeanList.get(index - 1).cityType == 2){
                day_go_city_text_click.setText(String.format(getString(R.string.scope_around), passBeanList.get(index - 1).name));
            }else if(passBeanList.get(index - 1).cityType == 3){
                day_go_city_text_click.setText(passBeanList.get(index - 1).name+"");
            }

            dayView.setTag(index);
            dayView.setBackgroundColor(Color.parseColor("#ffffff"));
        }else {
            if((passBeanList.size()+1) == index){
                dayView.setTag(index);
                dayView.setBackgroundColor(Color.parseColor("#ffffff"));
            }else {
                day_go_city_text_click.setText("选择包车游玩范围");
                dayView.setBackgroundColor(Color.parseColor("#d3d4d5"));
            }
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, (int) ScreenUtils.d2p(this.getActivity(), 15));

        if(null == savedCityBean) {
            if ((passBeanList.size() + 1) == index) {
                dayView.setTag(index);
                dayView.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                dayView.setBackgroundColor(Color.parseColor("#d3d4d5"));
            }
        }

        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != v.getTag()) {
                    currentClickView = v;
                    TextView text = (TextView)v.findViewById(R.id.day_go_city_text_click);
                    if(text.getText().toString().equalsIgnoreCase(getString(R.string.select_stay_city))
                            || text.getText().toString().equalsIgnoreCase(getString(R.string.select_end_city))){
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_FROM, "end");
                        bundle.putString("source", "首页");
                        startFragment(new FgChooseCity(), bundle);
                    }else {
                        showSelectPeoplePop(3);
                    }
                }
            }
        });
        full_day_show.addView(dayView, params);
    }

    private void removeDayLayout(int index) {
        full_day_show.removeViewAt(index);
        if(index < passBeanList.size()) {
            passBeanList.remove(index);
        }
    }

    public void showDaySelect(TextView sDateTime) {
        Calendar cal = Calendar.getInstance();
        MyDatePickerListener myDatePickerDialog = new MyDatePickerListener(sDateTime);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                myDatePickerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal = Calendar.getInstance();
        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        dpd.setMaxDate(cal);
        dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框

    }

    String start_date_str = "";
    String end_date_str = "";
    String halfDate = "";

    class MyDatePickerListener implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
        TextView mTextView;

        MyDatePickerListener(TextView textView) {
            this.mTextView = textView;
        }

        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear + 1;
            String monthStr = String.format("%02d", month);
            String dayOfMonthStr = String.format("%02d", dayOfMonth);
            String serverDate = year + "-" + monthStr + "-" + dayOfMonthStr;
            if (mTextView.getId() == R.id.start_date) {

                if (TextUtils.isEmpty(start_date_str)) {
                    if (TextUtils.isEmpty(end_date_str)) {
                        start_date_str = serverDate;
                        mTextView.setText(serverDate);
                    } else {
                        if (DateUtils.compareDate(serverDate, end_date_str) <= 0) {
                            start_date_str = serverDate;
                            mTextView.setText(serverDate);
                        } else {
                            ToastUtils.showLong(R.string.start_end_error);
                        }
                    }
                } else {

                    if (DateUtils.compareDate(serverDate, end_date_str) <= 0) {
                        start_date_str = serverDate;
                        mTextView.setText(serverDate);
                        addDayView(false);
                    } else {
                        ToastUtils.showLong(R.string.start_end_error);
                    }
                }

            } else if (mTextView.getId() == R.id.end_date) {
                if (TextUtils.isEmpty(start_date_str)) {
                    end_date_str = serverDate;
                    mTextView.setText(serverDate);
                } else {
                    if (DateUtils.compareDate(start_date_str, serverDate) <= 0) {
                        end_date_str = serverDate;
                        mTextView.setText(serverDate);
                        addDayView(false);
                    } else {
                        ToastUtils.showLong(R.string.start_end_error);
                    }

                }
            } else {
                halfDate = serverDate;
                goCityTextClick.setText(serverDate);
                goCityTextClick.setTextColor(Color.parseColor("#000000"));
            }
            checkNextBtnStatus();
        }
    }

    @Override
    protected int getBusinessType() {
        mBusinessType = Constants.BUSINESS_TYPE_DAILY;
        setGoodsType(Constants.BUSINESS_TYPE_DAILY);
        return mBusinessType;
    }

}
