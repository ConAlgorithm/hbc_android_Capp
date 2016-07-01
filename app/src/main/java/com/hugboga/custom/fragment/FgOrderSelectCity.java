package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.DatePickerActivity;
import com.hugboga.custom.adapter.OrderSelectCityAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarInfoBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.SelectCarBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCollectGuidesFilter;
import com.hugboga.custom.data.request.RequestGetCarInfo;
import com.hugboga.custom.data.request.RequestGuideConflict;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.DBCityUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.ScreenUtils;
import com.hugboga.custom.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

import static android.view.View.GONE;
import static com.hugboga.custom.R.id.baggage_text_click;
import static com.hugboga.custom.R.id.left_line;
import static com.hugboga.custom.R.id.people_text_click;
import static com.hugboga.custom.R.id.start_city_click;

/**
 * Created  on 16/4/14.
 */
@ContentView(R.layout.activity_order_select_city)
public class FgOrderSelectCity extends BaseFragment implements NumberPicker.Formatter {


    @ViewInject(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @ViewInject(R.id.header_title)
    TextView headerTitle;
    @ViewInject(R.id.header_right_btn)
    ImageView headerRightBtn;
    @ViewInject(start_city_click)
    TextView startCityClick;
    @ViewInject(R.id.full_day)
    RadioButton fullDay;
    @ViewInject(R.id.half_day)
    RadioButton halfDay;
    @ViewInject(people_text_click)
    TextView peopleTextClick;
    @ViewInject(R.id.child_text)
    TextView childText;
    @ViewInject(R.id.show_child_seat_layout)
    LinearLayout showChildSeatLayout;
    @ViewInject(R.id.child_no_confirm_click)
    ImageView childNoConfirmClick;
    @ViewInject(baggage_text_click)
    TextView baggageTextClick;
    @ViewInject(R.id.baggage_no_confirm_click)
    ImageView baggageNoConfirmClick;
    @ViewInject(R.id.start_date)
    TextView startDate;

    @ViewInject(R.id.end_date)
    TextView endDate;
    @ViewInject(R.id.end_layout_click)
    RelativeLayout endLayoutClick;
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

    @ViewInject(R.id.driver_layout)
    RelativeLayout driver_layout;

    @ViewInject(R.id.del_text)
    TextView del_text;

    @ViewInject(R.id.driver_name)
    TextView driver_name;

    @ViewInject(R.id.choose_driver)
    TextView choose_driver;

    @ViewInject(R.id.driver_tips)
    TextView driver_tips;

    @ViewInject(R.id.left_line)
    TextView left_line;

    @ViewInject(R.id.right_line)
    TextView right_line;

    @Override
    protected void inflateContent() {
    }

    boolean isFromGuideList = false;

    @Override
    protected void initView() {
        initHeader();
        initSelectPeoplePop(false);

        startBean = this.getArguments().getParcelable("cityBean");

        if(null !=startBean){
            endBean = startBean;
            startCity = startBean.name;
            endCityId = startBean.cityId + "";
            startCityClick.setText(startCity);
        }


        collectGuideBean = (CollectGuideBean) this.getArguments().getSerializable("collectGuideBean");
        if (null != collectGuideBean) {
            driver_layout.setVisibility(View.VISIBLE);
            driver_name.setText(collectGuideBean.name);
            isFromGuideList = true;
        }


        fullDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fullDay.setTextColor(Color.parseColor("#000000"));
                    halfDay.setTextColor(Color.parseColor("#888888"));
                    left_line.setBackgroundColor(Color.parseColor("#fbd003"));
                    right_line.setBackgroundColor(Color.parseColor("#edeeef"));
                    showFull();
                }
            }
        });
        halfDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fullDay.setTextColor(Color.parseColor("#888888"));
                    halfDay.setTextColor(Color.parseColor("#000000"));
                    left_line.setBackgroundColor(Color.parseColor("#edeeef"));
                    right_line.setBackgroundColor(Color.parseColor("#fbd003"));

                    showHalf();
                }
            }
        });
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddinfo()) {
                    showSaveDialog();
                } else {
                    finish();
                }
            }
        });

        del_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driver_layout.setVisibility(GONE);
                driver_tips.setVisibility(GONE);
                choose_driver.setVisibility(View.VISIBLE);
                collectGuideBean = null;
                isFromGuideList = false;
            }
        });

        driver_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserEntity.getUser().isLogin(getActivity())) {
                    goCollectGuid(2);
                } else {
                    Bundle bundle = new Bundle();//用于统计
                    bundle.putString("source", "包车下单");
                    startFragment(new FgLogin(), bundle);
                }
            }
        });

    }

    private void showSaveDialog() {
        android.support.v7.app.AlertDialog dialog = AlertDialogUtils.showAlertDialog(getContext(), "离开当前页面所选行程将会丢失，确定要离开吗？", "离开", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if (isAddinfo()) {
            showSaveDialog();
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    private void showFull() {
        half_day_show.setVisibility(GONE);
        full_day_show.setVisibility(View.VISIBLE);
        full_day_date_layout.setVisibility(View.VISIBLE);
        isHalfTravel = false;
        checkNextBtnStatus();
    }

    private void showHalf() {
        half_day_show.setVisibility(View.VISIBLE);
        full_day_show.setVisibility(GONE);
        full_day_date_layout.setVisibility(GONE);
        isHalfTravel = true;
        checkNextBtnStatus();
    }


    private void disableNextBtn() {
        nextBtnClick.setEnabled(false);
        nextBtnClick.setBackgroundColor(Color.parseColor("#d5dadb"));
    }

    private void enableNextBtn() {
        nextBtnClick.setEnabled(true);
        nextBtnClick.setBackgroundColor(ContextCompat.getColor(this.getActivity(), R.color.all_bg_yellow));
    }

    public void checkNextBtnStatus() {
        if (null == startBean) {
            disableNextBtn();
            return;
        }

        if (TextUtils.isEmpty(peopleTextClick.getText())) {
            disableNextBtn();
            return;
        }

        if (TextUtils.isEmpty(baggageTextClick.getText())) {
            disableNextBtn();
            return;
        }

        if (isHalfTravel) {
            if (TextUtils.isEmpty(halfDate)) {
                disableNextBtn();
                return;
            }
        } else {
            if (TextUtils.isEmpty(start_date_str)) {
                disableNextBtn();
                return;
            }
            if (TextUtils.isEmpty(end_date_str)) {
                disableNextBtn();
                return;
            }
            if (passBeanList.size() != nums) {
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
        fgRightBtn.setVisibility(View.VISIBLE);
        fgTitle.setText(R.string.select_city_title);
        source = getArguments().getString("source");
    }

    public String format(int value) {
        return value + "";
    }

    //初始化人数,座位选择
    private void init() {
        manList.setFormatter(this);
        manList.setMaxValue(11);
        manList.setMinValue(1);
        manList.setValue(manNum == 0 ? 1 : manNum);
        manList.setClickable(false);
        manList.setFocusable(false);
        manList.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        childList.setFormatter(this);
        childList.setMaxValue(11);
        childList.setMinValue(0);
        childList.setValue(childNum);
        childList.setClickable(false);
        childList.setFocusable(false);
        childList.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        baggageList.setFormatter(this);
        baggageList.setMaxValue(11);
        baggageList.setMinValue(0);
        baggageList.setValue(baggageNum);
        baggageList.setClickable(false);
        baggageList.setFocusable(false);
        baggageList.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    }

    //市外天数
    private int getOutNum() {
        int outNums = 0;
        for (int i = 0; i < passBeanList.size(); i++) {
            if (passBeanList.get(i).cityType == 3 || passBeanList.get(i).cityType == 2) {
                outNums++;
            }
        }
        return outNums;
    }

    //市内天数
    private int getInNum() {
        return passBeanList.size() - getOutNum();
    }

    int manNum = 0;
    int childNum = 0;
    int childSeatNums = 0;
    int baggageNum = 0;


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
    TextView out_tips, in_tips,other_tips;

    //    List<String> passCitiesList = new ArrayList<>();
    ArrayList<CityBean> passBeanList = new ArrayList<>();

    //添加经过城市
    public void addPassCityBean(int type, CityBean cityBean, String tag) {
        int index = Integer.valueOf(tag);
        CityBean newCityBean = (CityBean) cityBean.clone();
        newCityBean.cityType = type;
        if (index > passBeanList.size()) {
            passBeanList.add(newCityBean);
        } else {
            passBeanList.set(index - 1, newCityBean);
        }
    }

    //1,市内 2,周边 3,其它城市
    private void setDayText(int type, CityBean cityBean) {

        int tag = Integer.valueOf(currentClickView.getTag().toString());
        if (tag < full_day_show.getChildCount()) {
            for (int i = tag + 1; i < full_day_show.getChildCount(); i++) {
                View view = full_day_show.getChildAt(i);
                view.setTag(null);

                TextView endText = (TextView) view.findViewById(R.id.day_go_city_text_click);
                TextView end_add_tips = (TextView) view.findViewById(R.id.add_tips);

                endText.setText("选择包车游玩范围");
                end_add_tips.setVisibility(GONE);
                view.setBackgroundColor(Color.parseColor("#d3d4d5"));
                if (tag < passBeanList.size()) {
                    passBeanList.remove(tag - 1);
                }
            }
        }

        TextView text = (TextView) currentClickView.findViewById(R.id.day_go_city_text_click);
        TextView add_tips = (TextView) currentClickView.findViewById(R.id.add_tips);
        String cityId = cityBean.cityId + "";
        if (type == 1) {
            text.setText(cityBean.name + "市内");
            add_tips.setVisibility(GONE);
            addPassCityBean(1, cityBean, currentClickView.getTag().toString());
        } else if (type == 2) {
            text.setText(cityBean.name + "周边");
            add_tips.setVisibility(GONE);
            add_tips.setText(R.string.select_around_city);
            addPassCityBean(2, cityBean, currentClickView.getTag().toString());
        } else if (type == 3) {
            cityId = cityBean.cityId + "";
            text.setText(cityBean.name);
            add_tips.setVisibility(GONE);
            if (cityBean.cityId == startBean.cityId) {
                add_tips.setText(R.string.select_around_city);
            } else {
                add_tips.setText(R.string.select_other_city);
            }
            addPassCityBean(3, cityBean, currentClickView.getTag().toString());
        }
//        if(passCitiesList.size() > tag-1){
//            passCitiesList.set((tag - 1), cityId + "-1" + "-" + type);
//        }else{
//            passCitiesList.add((tag - 1), cityId + "-1" + "-" + type);
//        }
        View view = full_day_show.getChildAt(tag);
        if (null != view && null == view.getTag()) {
            view.setTag(tag + 1);
//            TextView endText = (TextView) view.findViewById(R.id.day_go_city_text_click);
//            TextView end_add_tips = (TextView) view.findViewById(R.id.add_tips);
//            if (type == 3 && (tag + 1) == nums) {
//                endText.setText(R.string.select_end_city);
//            } else if (type == 3 && (tag + 1) != nums) {
//                endText.setText(R.string.select_stay_city);
//            } else {
//                endText.setText(R.string.select_scope);
//                end_add_tips.setVisibility(View.GONE);
//            }
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        } else if (null != view && null != view.getTag()) {
//            TextView endText = (TextView) view.findViewById(R.id.day_go_city_text_click);
//            TextView end_add_tips = (TextView) view.findViewById(R.id.add_tips);
//            if (type == 3 && (tag + 1) == nums) {
//                endText.setText(R.string.select_end_city);
//            } else if (type == 3 && (tag + 1) != nums) {
//                endText.setText(R.string.select_stay_city);
//            } else {
//                endText.setText(R.string.select_scope);
//                end_add_tips.setVisibility(View.GONE);
//            }
        } else {
            endCityId = cityId;
        }
        checkNextBtnStatus();

    }

    //途径城市
    String passCities = "";
    private void initSelectPeoplePop(boolean isEndDay) {
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
        other_tips = (TextView) view.findViewById(R.id.other_tips);

//        if(currentIndex > 0) {
//            preCityBean = passBeanList.get(currentIndex);
//        }else{
//            preCityBean = startBean;
//        }

        if (isEndDay) {
            in_title.setText("在" + preCityBean.name + "市内结束行程,市内游玩");
            out_title.setText("在" + preCityBean.name + "市内结束行程,周边游玩");
            other_title.setText("在其它城市结束行程");
        }


        scope_layout_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayText(1, preCityBean);
                hideSelectPeoplePop();
            }
        });
        scope_layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayText(2, preCityBean);
                hideSelectPeoplePop();
            }
        });
        scope_layout_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM, "lastCity");
                bundle.putString("source", "首页");
                bundle.putInt(FgChooseCity.KEY_CITY_ID, preCityBean.cityId);
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
                    baggageNum = baggageList.getValue();
                    baggageTextClick.setText(String.format(getString(R.string.select_city_baggage_num), baggageNum));
                    baggageTextClick.setTextColor(Color.parseColor("#000000"));
                } else {
                    childNum = childList.getValue();
                    manNum = manList.getValue();
                    baggageList.getValue();
                    if (childNum > 0) {
                        showChildSeatLayout.setVisibility(View.VISIBLE);
                    } else {
                        showChildSeatLayout.setVisibility(GONE);
                    }
                    if (manNum == 0) manNum = 1;
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
        init();
        if (null != peoplePop) {
            if (type == 2) {
                baggageList.setVisibility(View.VISIBLE);
                mans_layout.setVisibility(GONE);
                scope_layout.setVisibility(GONE);
                title.setText(R.string.select_baggage_tips);
                ok.setVisibility(View.VISIBLE);
                cancle.setVisibility(View.VISIBLE);
            } else if (type == 1) {
                baggageList.setVisibility(GONE);
                mans_layout.setVisibility(View.VISIBLE);
                scope_layout.setVisibility(GONE);
                title.setText(R.string.select_people_tips);
                ok.setVisibility(View.VISIBLE);
                cancle.setVisibility(View.VISIBLE);
            } else if (type == 3) {
                baggageList.setVisibility(GONE);
                mans_layout.setVisibility(GONE);
                scope_layout.setVisibility(View.VISIBLE);
                ok.setVisibility(GONE);
                cancle.setVisibility(GONE);
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

    List<CityBean> hotCitys;//热门城市

    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCity.class.getSimpleName().equals(from)) {
            String fromKey = bundle.getString(KEY_FROM);
            if ("startAddress".equals(fromKey)) {
                startBean = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
                preCityBean = startBean;
                passBeanList.clear();
//                passBeanList.add(startBean);
                endBean = startBean;
                if (!startCity.equalsIgnoreCase(startBean.name)) {
                    startCity = startBean.name;
                    endCityId = startBean.cityId + "";
                    startCityClick.setText(startCity);
                    startCityClick.setTextColor(Color.parseColor("#000000"));
                    DBCityUtils dbCityUtils = new DBCityUtils();
//                    cityBeanList = dbCityUtils.requestDataByKeyword(startBean.name, false);
//                    initScopeLayoutValue(cityBeanList);
                    initScopeLayoutValue(true);
                    addDayView(true);
                }

                List<CityBean> list = CityUtils.requestDataByKeyword(getActivity(),
                        preCityBean.groupId, preCityBean.cityId, "", true);

                if (null == list || list.size() == 0) {
                    showOtherLayout = false;
                } else {
                    showOtherLayout = true;
                }


                hotCitys = CityUtils.requestHotDate(getActivity(),startBean.groupId);
            } else if ("lastCity".equalsIgnoreCase(fromKey) || "nearby".equalsIgnoreCase(fromKey)) {
                endBean = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);

//                passBeanList.add(endBean);
                setDayText(3, endBean);
//                resetLastText();
//                if(Integer.valueOf(currentClickView.getTag().toString()) != full_day_show.getChildCount()) {
//                    if (endBean.cityId == startBean.cityId) {
//                        resetLastText(false);
//                    } else {
//                        resetLastText(true);
//                    }
//                }
            }
            checkNextBtnStatus();
        }
    }

    CityBean preCityBean;
    boolean showOtherLayout = true;
    public void initScopeLayoutValue(boolean isEndDay) {
        preCityBean = (passBeanList.size() == 0 || currentIndex ==0 )?startBean:passBeanList.get(currentIndex -1);
        if (isEndDay) {
            scope_in_str = "在" + preCityBean.name + "市内结束行程,市内游玩";
            scope_around_str = "在" + preCityBean.name + "市内结束行程,周边游玩";
            scope_other_str = "在其它城市结束行程";
        } else {
            scope_in_str = String.format(getString(R.string.scope_in), "住在" + preCityBean.name+"市内");
            scope_around_str = String.format(getString(R.string.scope_around), "住在" + preCityBean.name+"市内");
            scope_other_str = "住在其它城市";
        }

        if(showOtherLayout) {
            scope_layout_other.setVisibility(View.VISIBLE);
        }else{
            scope_layout_other.setVisibility(View.GONE);
        }

        in_title.setText(scope_in_str);
        out_title.setText(scope_around_str);
        other_title.setText(scope_other_str);
        out_tips.setText(preCityBean.neighbourTip);
        in_tips.setText(preCityBean.dailyTip);

        if(null != hotCitys) {
            if (hotCitys.size() > 0) {
                other_tips.setVisibility(View.VISIBLE);
                other_tips.setText(CityUtils.getHotCityStr(hotCitys));
            } else {
                other_tips.setVisibility(View.INVISIBLE);
            }
        }else{
            other_tips.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isAddinfo() {
        if (null != startBean || !TextUtils.isEmpty(peopleTextClick.getText())
                || !TextUtils.isEmpty(baggageTextClick.getText())
                || !TextUtils.isEmpty(halfDate)
                || !TextUtils.isEmpty(start_date_str)
                || !TextUtils.isEmpty(end_date_str)) {
            return true;
        }
        return false;
    }


    private boolean checkParams() {
        if (null == startBean
                || TextUtils.isEmpty(peopleTextClick.getText())
                || TextUtils.isEmpty(baggageTextClick.getText())
                || isHalfTravel ? TextUtils.isEmpty(halfDate) : TextUtils.isEmpty(start_date_str)
                || isHalfTravel ? TextUtils.isEmpty(halfDate) : TextUtils.isEmpty(end_date_str)) {
//                || isHalfTravel?false:passBeanList.size() != nums){
            AlertDialogUtils.showAlertDialogOneBtn(this.getActivity(), getString(R.string.dairy_choose_guide), "好的");
            return false;
        } else {
            return true;
        }
    }


    //type 1 司导列表   2, 预约司导列表
    private void goCollectGuid(int type) {
        if (type == 1) {
            FgCollectGuideList fgCollectGuideList = new FgCollectGuideList();
            startFragment(fgCollectGuideList);
        } else {
            if (checkParams()) {
                if (UserEntity.getUser().isLogin(getActivity())) {
                    FgCollectGuideList fgCollectGuideList = new FgCollectGuideList();
                    Bundle bundle = new Bundle();
                    RequestCollectGuidesFilter.CollectGuidesFilterParams params = new RequestCollectGuidesFilter.CollectGuidesFilterParams();
                    params.startCityId = startBean.cityId;
                    params.startTime = isHalfTravel ? halfDate + " 00:00:00" : start_date_str + " 00:00:00";
                    params.endTime = isHalfTravel ? halfDate + " 00:00:00" : end_date_str + " 00:00:00";
                    params.adultNum = manNum;
                    params.childrenNum = childNum;
                    params.childSeatNum = childSeatNums;
                    params.luggageNum = baggageNum;
                    params.orderType = 3;
                    params.totalDays = isHalfTravel ? 1 : nums;
                    params.passCityId = startBean.cityId + "";//isHalfTravel ? startBean.cityId + "" : getPassCitiesId();
                    bundle.putSerializable(Constants.PARAMS_DATA, params);
                    fgCollectGuideList.setArguments(bundle);
                    startFragment(fgCollectGuideList);
                } else {
                    Bundle bundle = new Bundle();//用于统计
                    bundle.putString("source", "包车下单");
                    startFragment(new FgLogin(), bundle);
                }
            }
        }
    }

    CarInfoBean carBean;

    private void getCarInfo() {
        final RequestGetCarInfo requestGetCarInfo = new RequestGetCarInfo(this.getActivity(),
                startBean.cityId + "", isHalfTravel ? (startBean.cityId + "") : passBeanList.get(passBeanList.size() - 1).cityId + "",
                isHalfTravel ? halfDate + " 00:00:00" : start_date_str + " 00:00:00",
                isHalfTravel ? halfDate + " 00:00:00" : end_date_str + " 00:00:00",
                isHalfTravel ? "1" : "0", manNum + "",
                childNum + "", childSeatNums + "", baggageNum + "", isHalfTravel ? "" : getPassCities(), "18");
        HttpRequestUtils.request(this.getActivity(), requestGetCarInfo, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                RequestGetCarInfo requestGetCarInfo1 = (RequestGetCarInfo) request;
                carBean = requestGetCarInfo.getData();

                FGOrderNew fgOrderNew = new FGOrderNew();
                Bundle bundle = new Bundle();
                bundle.putString("guideCollectId", guideCollectId);
                bundle.putSerializable("collectGuideBean", collectGuideBean);
                bundle.putString("source", source);


                bundle.putString("startCityId", startBean.cityId + "");
                bundle.putString("endCityId", isHalfTravel ? (startBean.cityId + "") : passBeanList.get(passBeanList.size() - 1).cityId + "");//endCityId);
                bundle.putString("startDate", isHalfTravel ? (halfDate) : (start_date_str));
                bundle.putString("endDate", isHalfTravel ? (halfDate) : (end_date_str));
                bundle.putString("halfDay", isHalfTravel ? "1" : "0");
                bundle.putString("adultNum", manNum + "");
                bundle.putString("childrenNum", childNum + "");
                bundle.putString("childseatNum", childSeatNums + "");
                bundle.putString("luggageNum", baggageNum + "");
                bundle.putString("passCities", isHalfTravel ? "" : getPassCities());
                bundle.putString("carTypeName", null != getMatchCarBean() ? getMatchCarBean().carDesc : "");
                bundle.putString("startCityName", startBean.name);
                bundle.putString("dayNums", nums + "");
                bundle.putParcelable("startBean", startBean);
                bundle.putParcelable("endBean", endBean);
                bundle.putInt("outnum", getOutNum());
                bundle.putInt("innum", getInNum());
                bundle.putString("source", source);
                bundle.putBoolean("isHalfTravel", isHalfTravel);
                bundle.putSerializable("passCityList", passBeanList);
                bundle.putString("orderType", "3");
                bundle.putParcelable("carBean", getMatchCarBean());
                bundle.putBoolean("isHalfTravel", isHalfTravel);
                bundle.putInt("type", 3);
                bundle.putString("orderType", "3");
                fgOrderNew.setArguments(bundle);
                startFragment(fgOrderNew);
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {
                System.out.print("1");
            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                System.out.print("2");
            }
        });
    }

    //获取满足条件的car
    private SelectCarBean getMatchCarBean() {
        SelectCarBean selectCarBean = null;
        if (null != carBean && null != carBean.cars) {
            for (int i = 0; i < carBean.cars.size(); i++) {
                selectCarBean = carBean.cars.get(i);
                if (selectCarBean.carType == collectGuideBean.carType
                        && selectCarBean.seatCategory == collectGuideBean.carClass) {
                    return selectCarBean;
                }
            }
        }
        return selectCarBean;
    }

    String guideCollectId = "";

    private void checkGuideCoflict() {
        if (((manNum + Math.round(childSeatNums * 1.5) + (childNum - childSeatNums)) <= collectGuideBean.numOfPerson)
                && ((manNum + Math.round((childSeatNums) * 1.5) + (childNum - childSeatNums)) + baggageNum)
                <= (collectGuideBean.numOfPerson + collectGuideBean.numOfLuggage)) {
            OrderUtils.checkGuideCoflict(getContext(), 3, startBean.cityId,
                    collectGuideBean.guideId, (isHalfTravel ? halfDate : start_date_str) + " 00:00:00",
                    (isHalfTravel ? halfDate : end_date_str) + " 00:00:00", getPassCitiesId(),
                    nums, collectGuideBean.carType, collectGuideBean.carClass,
                    new HttpRequestListener() {
                        @Override
                        public void onDataRequestSucceed(BaseRequest request) {
                            RequestGuideConflict mRequest = (RequestGuideConflict) request;
                            List<String> guideList = mRequest.getData();
                            if (guideList.size() == 0) {
                                driver_tips.setVisibility(View.VISIBLE);
                            } else {
                                getCarInfo();
                            }
                        }

                        @Override
                        public void onDataRequestCancel(BaseRequest request) {
                            System.out.print(request);
                        }

                        @Override
                        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                            System.out.print(request);
                        }
                    });
        } else {
            driver_tips.setVisibility(View.VISIBLE);
        }
    }

    boolean isHalfTravel = false;

    @Event({R.id.go_city_text_layout,R.id.choose_driver, R.id.minus, R.id.add, R.id.header_left_btn, start_city_click, people_text_click, R.id.show_child_seat_layout, R.id.child_no_confirm_click, baggage_text_click, R.id.baggage_no_confirm_click, R.id.end_layout_click, R.id.go_city_text_click, R.id.next_btn_click})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.choose_driver:
                goCollectGuid(2);
                break;
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
            case start_city_click:
                if (null != collectGuideBean && !TextUtils.isEmpty(startCityClick.getText())) {
                    ToastUtils.showShort(R.string.alert_del_after_edit);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_FROM, "startAddress");
                    bundle.putString("source", "首页");
                    startFragment(new FgChooseCity(), bundle);
                }
                break;
            case people_text_click:
                if (null != collectGuideBean && !TextUtils.isEmpty(peopleTextClick.getText())) {
                    ToastUtils.showShort(R.string.alert_del_after_edit);
                } else {
                    showSelectPeoplePop(1);
                }
                break;
            case R.id.show_child_seat_layout:
                break;
            case R.id.child_no_confirm_click:
                AlertDialogUtils.showAlertDialog(this.getActivity(), getString(R.string.man_no_confirm_tips));
                break;
            case baggage_text_click:
                if (null != collectGuideBean && !TextUtils.isEmpty(baggageTextClick.getText())) {
                    ToastUtils.showShort(R.string.alert_del_after_edit);
                } else {
                    showSelectPeoplePop(2);
                }
                break;
            case R.id.baggage_no_confirm_click:
                AlertDialogUtils.showAlertDialog(this.getActivity(), getString(R.string.baggage_no_confirm_tips));
                break;
            case R.id.end_layout_click:
                if (null != collectGuideBean && !TextUtils.isEmpty(endDate.getText())) {
                    ToastUtils.showShort(R.string.alert_del_after_edit);
                } else {
                    showDaySelect(endDate);
                }
                break;
            case R.id.go_city_text_layout:
            case R.id.go_city_text_click:
                if (null != collectGuideBean && !TextUtils.isEmpty(goCityTextClick.getText())) {
                    ToastUtils.showShort(R.string.alert_del_after_edit);
                } else {
//                    showDaySelect(goCityTextClick);
                    startActivity(new Intent(getActivity(),DatePickerActivity.class));

                }
                break;
            case R.id.next_btn_click:

                if (null != collectGuideBean) {
                    if(collectGuideBean.numOfPerson == 4 && (manNum + childNum) == 4
                            || collectGuideBean.numOfPerson == 6 && (manNum + childNum) == 6){
                        AlertDialogUtils.showAlertDialog(getActivity(),getString(R.string.alert_car_full),
                                "继续下单","更换车型",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkGuideCoflict();
                                        dialog.dismiss();
                                    }
                                },new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                    }else{
                        checkGuideCoflict();
                    }
                } else {
                    Bundle bundleCar = new Bundle();
                    bundleCar.putString("startCityId", startBean.cityId + "");
                    bundleCar.putString("endCityId", isHalfTravel ? (startBean.cityId + "") : passBeanList.get(passBeanList.size() - 1).cityId + "");//endCityId);
                    bundleCar.putString("startDate", isHalfTravel ? (halfDate) : (start_date_str));
                    bundleCar.putString("endDate", isHalfTravel ? (halfDate) : (end_date_str));
                    bundleCar.putString("halfDay", isHalfTravel ? "1" : "0");
                    bundleCar.putString("adultNum", manNum + "");
                    bundleCar.putString("childrenNum", childNum + "");
                    bundleCar.putString("childseatNum", childSeatNums + "");
                    bundleCar.putString("luggageNum", baggageNum + "");
                    bundleCar.putString("passCities", isHalfTravel ? "" : getPassCities());

                    bundleCar.putString("startCityName", startBean.name);
                    bundleCar.putString("dayNums", nums + "");
                    bundleCar.putParcelable("startBean", startBean);
                    bundleCar.putParcelable("endBean", endBean);
                    bundleCar.putInt("outnum", getOutNum());
                    bundleCar.putInt("innum", getInNum());
                    bundleCar.putString("source", source);
                    bundleCar.putBoolean("isHalfTravel", isHalfTravel);
                    bundleCar.putSerializable("passCityList", passBeanList);
                    bundleCar.putString("orderType", "3");

                    FGSelectCar fgSelectCar = new FGSelectCar();
                    fgSelectCar.setArguments(bundleCar);
                    startFragment(fgSelectCar);
                }
//                try {
//                    Reservoir.clear();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                //统计,这代码应该加到点击事件方法的最后边
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("source", source);
                map.put("begincity", startBean.name);
//                map.put("guestcount", manNum + childNum + "");
//                map.put("luggagecount", baggageNum + "");
//                map.put("drivedays", getOutNum() + getInNum() + "");
                MobclickAgent.onEventValue(getActivity(), "chosecar_oneday", map, isHalfTravel ? 1 : getOutNum() * 2 + getInNum() * 2);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_right_txt:
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("source", "填写行程页面");
                MobclickAgent.onEvent(getActivity(), "callcenter_oneday", map);
                v.setTag("填写行程页面,calldomestic_oneday,calloverseas_oneday");
                break;
        }
        super.onClick(v);
    }

    private String getPassCities() {
        passCities = "";
        for (int i = 0; i < passBeanList.size(); i++) {
            if (i != passBeanList.size() - 1) {
                passCities += passBeanList.get(i).cityId + "-1-" + passBeanList.get(i).cityType + ",";
            } else {
                passCities += passBeanList.get(i).cityId + "-1-" + passBeanList.get(i).cityType;
            }
        }
        return passCities;
    }

    private String getPassCitiesId() {
        passCities = "";
        for (int i = 0; i < passBeanList.size(); i++) {
            if (i != passBeanList.size() - 1) {
                passCities += passBeanList.get(i).cityId + ",";
            } else {
                passCities += passBeanList.get(i).cityId;
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
        if (resetCity) {
            full_day_show.removeAllViews();
//            passCitiesList.clear();
            passBeanList.clear();
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
//                resetLastText();
                oldNum = nums;
            }
        } else if (nums < oldNum) {
            for (int i = oldNum; i > nums; i--) {
                removeDayLayout(i - 1);
            }
//            resetLastText();
            oldNum = nums;
        } else {

        }

    }

    //根据第一天的选择改变最后一天的文字显示
    private void resetLastText() {
        try {
            int count = full_day_show.getChildCount();
            TextView text = null;
            if (passBeanList.get(currentIndex - 1).cityId == startBean.cityId) {
                for (int i = currentIndex; i < count; i++) {
                    text = (TextView) (full_day_show.getChildAt(i).findViewById(R.id.day_go_city_text_click));
                    text.setText("选择包车游玩范围");
                }
            } else {
                for (int i = currentIndex; i < count; i++) {
                    text = (TextView) (full_day_show.getChildAt(i).findViewById(R.id.day_go_city_text_click));
                    if (i == count - 1) {
                        text.setText("选择结束城市");
                    } else {
                        text.setText("选择住宿城市");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    View dayView;
    TextView day_text, day_go_city_text_click;

    View currentClickView = null;
    int currentIndex = 0;
    //生成经过城市列表
    private void genDayViews(int index) {
        dayView = LayoutInflater.from(this.getActivity()).inflate(R.layout.add_day_item, null);
        day_text = (TextView) dayView.findViewById(R.id.day_text);
        day_go_city_text_click = (TextView) dayView.findViewById(R.id.day_go_city_text_click);
        day_text.setText("第" + index + "天");
        if (passBeanList.size() >= index) {
            if (passBeanList.get(index - 1).cityType == 1) {
                day_go_city_text_click.setText(String.format(getString(R.string.scope_in), passBeanList.get(index - 1).name));
            } else if (passBeanList.get(index - 1).cityType == 2) {
                day_go_city_text_click.setText(String.format(getString(R.string.scope_around), passBeanList.get(index - 1).name));
            } else if (passBeanList.get(index - 1).cityType == 3) {
                day_go_city_text_click.setText(passBeanList.get(index - 1).name + "");
            }

            dayView.setTag(index);
            dayView.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            if ((passBeanList.size() + 1) == index) {
                dayView.setTag(index);
                dayView.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                day_go_city_text_click.setText("选择包车游玩范围");
                dayView.setBackgroundColor(Color.parseColor("#d3d4d5"));
            }
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, (int) ScreenUtils.d2p(this.getActivity(), 15));

        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != v.getTag()) {
                    currentClickView = v;
                    TextView text = (TextView) v.findViewById(R.id.day_go_city_text_click);
                     currentIndex = Integer.valueOf(currentClickView.getTag().toString()) - 1;
//                    if (currentIndex != 0 && passBeanList.get(currentIndex - 1).cityType == 3 && startBean.cityId != passBeanList.get(currentIndex - 1).cityId) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString(KEY_FROM, "nearby");
//                        bundle.putString("source", "首页");
//                        bundle.putInt(FgChooseCity.KEY_CITY_ID, startBean.cityId);
//                        startFragment(new FgChooseCity(), bundle);
//                    } else {
                        if (Integer.valueOf(v.getTag().toString()) == full_day_show.getChildCount()) {
                            initScopeLayoutValue(true);
                        } else{
                            initScopeLayoutValue(false);
                        }
                        showSelectPeoplePop(3);

//                    }
                }
            }
        });
        full_day_show.addView(dayView, params);
    }

    private void removeDayLayout(int index) {
        full_day_show.removeViewAt(index);
        if (index < passBeanList.size()) {
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

    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_GUIDE:
                collectGuideBean = (CollectGuideBean) action.getData();
                if (null != collectGuideBean) {
                    driver_layout.setVisibility(View.VISIBLE);
                    driver_name.setText(collectGuideBean.name);
                    choose_driver.setVisibility(GONE);
                }
                break;
            default:
                break;
        }
    }

    CollectGuideBean collectGuideBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getBusinessType() {
        mBusinessType = Constants.BUSINESS_TYPE_DAILY;
        setGoodsType(mBusinessType);
        return mBusinessType;
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


}
