package com.hugboga.custom.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.hugboga.custom.adapter.OrderSelectCityAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarInfoBean;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.SelectCarBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCollectGuidesFilter;
import com.hugboga.custom.data.request.RequestGetCarInfo;
import com.hugboga.custom.data.request.RequestGuideConflict;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBCityUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.ScreenUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;

import static android.view.View.GONE;
import static com.huangbaoche.hbcframe.fragment.BaseFragment.KEY_FRAGMENT_NAME;
import static com.hugboga.custom.R.id.baggage_text_click;
import static com.hugboga.custom.R.id.people_text_click;
import static com.hugboga.custom.R.id.start_city_click;


public class OrderSelectCityActivity extends BaseActivity {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(start_city_click)
    TextView startCityClick;
    @Bind(R.id.full_day)
    RadioButton fullDay;
    @Bind(R.id.half_day)
    RadioButton halfDay;
    @Bind(people_text_click)
    TextView peopleTextClick;
    @Bind(R.id.child_text)
    TextView childText;
    @Bind(R.id.show_child_seat_layout)
    LinearLayout showChildSeatLayout;
    @Bind(R.id.child_no_confirm_click)
    ImageView childNoConfirmClick;
    @Bind(baggage_text_click)
    TextView baggageTextClick;
    @Bind(R.id.baggage_no_confirm_click)
    ImageView baggageNoConfirmClick;
    @Bind(R.id.start_date)
    TextView startDate;

    @Bind(R.id.end_date)
    TextView endDate;
    @Bind(R.id.end_layout_click)
    RelativeLayout endLayoutClick;
    @Bind(R.id.go_city_text_click)
    TextView goCityTextClick;
    @Bind(R.id.next_btn_click)
    Button nextBtnClick;
    @Bind(R.id.half_day_show)
    LinearLayout half_day_show;

    @Bind(R.id.full_day_show)
    LinearLayout full_day_show;

    @Bind(R.id.minus)
    TextView minus;
    @Bind(R.id.add)
    TextView add;

    @Bind(R.id.full_day_date_layout)
    LinearLayout full_day_date_layout;

    @Bind(R.id.driver_layout)
    RelativeLayout driver_layout;

    @Bind(R.id.del_text)
    TextView del_text;

    @Bind(R.id.driver_name)
    TextView driver_name;

    @Bind(R.id.choose_driver)
    TextView choose_driver;

    @Bind(R.id.driver_tips)
    TextView driver_tips;

    @Bind(R.id.left_line)
    TextView left_line;

    @Bind(R.id.right_line)
    TextView right_line;


    @Bind(R.id.start_date_right)
    TextView start_date_right;

    @Bind(R.id.end_date_right)
    TextView end_date_right;

    @Bind(R.id.go_city_text_click_right)
    TextView go_city_text_click_right;

    @Bind(R.id.time_layout)
    LinearLayout time_layout;

    @Bind(R.id.time_text_click)
    TextView time_text_click;

    boolean isFromGuideList = false;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;

    public void initView() {
        initSelectPeoplePop(false);
        enableNextBtn();

        startBean = (CityBean) this.getIntent().getSerializableExtra("cityBean");

        if (null != startBean) {
            endBean = startBean;
            startCity = startBean.name;
            endCityId = startBean.cityId + "";
            startCityClick.setText(startCity);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(OrderSelectCityActivity.this, ChooseCityActivity.class);
                    intent.putExtra(BaseFragment.KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_DAILY);
                    intent.putExtra("fromDaily", true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_bottom_in, 0);
                }
            }, 500);

        }


        collectGuideBean = (CollectGuideBean) this.getIntent().getSerializableExtra("collectGuideBean");
        if (null != collectGuideBean) {
            driver_layout.setVisibility(View.VISIBLE);
            driver_name.setText(collectGuideBean.name);
            isFromGuideList = true;
        }


        fullDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (null != chooseDateBean) {
                        chooseDateBean.type = TYPE_RANGE;
                    }
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
                    if (null != chooseDateBean) {
                        chooseDateBean.type = TYPE_SINGLE;
                    }
                    fullDay.setTextColor(Color.parseColor("#888888"));
                    halfDay.setTextColor(Color.parseColor("#000000"));
                    left_line.setBackgroundColor(Color.parseColor("#edeeef"));
                    right_line.setBackgroundColor(Color.parseColor("#fbd003"));

                    showHalf();
                }
            }
        });
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
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
                if (UserEntity.getUser().isLogin(activity)) {
                    goCollectGuid(2);
                } else {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.putExtra("source", getIntentSource());
                    startActivity(intent);
                }
            }
        });
        headerTitle.setVisibility(View.VISIBLE);
        headerTitle.setText(R.string.select_city_title);
        headerRightTxt.setVisibility(View.VISIBLE);
        headerRightTxt.setText("常见问题");
        headerRightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_PROBLEM);
                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                activity.startActivity(intent);
            }
        });

        try {
            EventUtil eventUtil = EventUtil.getInstance();
            eventUtil.source = getIntentSource();
//            if (null == source || !DailyWebInfoActivity.EVENT_SOURCE.equals(eventUtil.source)) {
//                eventUtil.sourceDetail = "";
//            }

            Map map = new HashMap();
            map.put(Constants.PARAMS_SOURCE, getIntentSource());
            map.put(Constants.PARAMS_SOURCE_DETAIL, eventUtil.sourceDetail);
            MobClickUtils.onEvent(getEventId(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSaveDialog() {
        AlertDialog dialog = AlertDialogUtils.showAlertDialog(activity, "离开当前页面所选行程将会丢失，确定要离开吗？", "离开", "取消", new DialogInterface.OnClickListener() {
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
    public void onBackPressed() {
        if (isAddinfo()) {
            showSaveDialog();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_R;
    }

    @Override
    public String getEventSource() {
        return "包车下单选择司导";
    }

    private void showFull() {
        half_day_show.setVisibility(GONE);
        full_day_show.setVisibility(View.VISIBLE);
        full_day_date_layout.setVisibility(View.VISIBLE);
        isHalfTravel = false;
    }

    private void showHalf() {
        half_day_show.setVisibility(View.VISIBLE);
        full_day_show.setVisibility(GONE);
        full_day_date_layout.setVisibility(GONE);
        isHalfTravel = true;
    }


    private void disableNextBtn() {
        nextBtnClick.setEnabled(false);
        nextBtnClick.setBackgroundColor(Color.parseColor("#d5dadb"));
    }

    private void enableNextBtn() {
        nextBtnClick.setEnabled(true);
        nextBtnClick.setBackgroundColor(ContextCompat.getColor(this.activity, R.color.all_bg_yellow));
    }


    boolean isIncludeToday = false;//是否包括今天

    public boolean checkNextBtnStatus() {
        if (null == startBean) {
            CommonUtils.showToast("请选择城市");
            return false;
        }

        if (TextUtils.isEmpty(peopleTextClick.getText())) {
            CommonUtils.showToast("请选择出发人数");
            return false;
        }

        if (serverTime.equalsIgnoreCase("00:00") && isIncludeToday) {
            CommonUtils.showToast("请选择上车时间");
            return false;
        }

        if (isHalfTravel) {
            if (TextUtils.isEmpty(halfDate)) {
                CommonUtils.showToast("请选择游玩日期");
                return false;
            }
        } else {
            if (TextUtils.isEmpty(start_date_str) || TextUtils.isEmpty(end_date_str)) {
                CommonUtils.showToast("请选择开始日期和结束日期");
                return false;
            }

            if (passBeanList.size() != nums) {
                CommonUtils.showToast("请填写每日行程");
                return false;
            }
        }
        return true;
    }


    //初始化人数,座位选择
    private void init() {
        manList.setMaxValue(11);
        manList.setMinValue(1);
        manList.setValue(manNum == 0 ? 1 : manNum);
        manList.setClickable(false);
        manList.setFocusable(false);
        manList.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        childList.setMaxValue(11);
        childList.setMinValue(0);
        childList.setValue(childNum);
        childList.setClickable(false);
        childList.setFocusable(false);
        childList.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

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
    TextView out_tips, in_tips, other_tips;

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
        View view = full_day_show.getChildAt(tag);
        if (null != view && null == view.getTag()) {
            view.setTag(tag + 1);
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        } else if (null != view && null != view.getTag()) {
        } else {
            endCityId = cityId;
        }
    }

    //途径城市
    String passCities = "";

    private void initSelectPeoplePop(boolean isEndDay) {
        view = LayoutInflater.from(this.activity).inflate(R.layout.pop_select_people, null);
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
                Intent intent = new Intent(OrderSelectCityActivity.this, ChooseCityActivity.class);
                intent.putExtra("source", "首页");
                intent.putExtra(BaseFragment.KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_DAILY);
                intent.putExtra(ChooseCityActivity.KEY_CITY_ID, preCityBean.cityId);
                intent.putExtra(KEY_FROM, "lastCity");
                intent.putExtras(bundle);
                startActivity(intent);
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
                    if (childNum < childSeatNums) {
                        childSeatNums = childNum;
                        childText.setText(getString(R.string.select_city_child) + childSeatNums);
                    }
                    peopleTextClick.setTextColor(Color.parseColor("#000000"));
                }
//                checkNextBtnStatus();
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
    CityBean preCityBean;
    boolean showOtherLayout = true;

    public void initScopeLayoutValue(boolean isEndDay) {
        preCityBean = (passBeanList.size() == 0 || currentIndex == 0) ? startBean : passBeanList.get(currentIndex - 1);
        if (isEndDay) {
            scope_in_str = "在" + preCityBean.name + "市内结束行程,市内游玩";
            scope_around_str = "在" + preCityBean.name + "市内结束行程,周边游玩";
            scope_other_str = "在其它城市结束行程";
        } else {
            scope_in_str = String.format(getString(R.string.scope_in), "住在" + preCityBean.name + "市内");
            scope_around_str = String.format(getString(R.string.scope_around), "住在" + preCityBean.name + "市内");
            scope_other_str = "住在其它城市";
        }

        if (showOtherLayout) {
            scope_layout_other.setVisibility(View.VISIBLE);
        } else {
            scope_layout_other.setVisibility(View.GONE);
        }

        in_title.setText(scope_in_str);
        out_title.setText(scope_around_str);
        other_title.setText(scope_other_str);
        out_tips.setText(preCityBean.neighbourTip);
        in_tips.setText(preCityBean.dailyTip);

        if (null != hotCitys) {
            if (hotCitys.size() > 0) {
                other_tips.setVisibility(View.VISIBLE);
                other_tips.setText(CityUtils.getHotCityStr(hotCitys));
            } else {
                other_tips.setVisibility(View.INVISIBLE);
            }
        } else {
            other_tips.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isAddinfo() {
        return null != startBean || !TextUtils.isEmpty(peopleTextClick.getText())
                || !TextUtils.isEmpty(baggageTextClick.getText())
                || !TextUtils.isEmpty(halfDate)
                || !TextUtils.isEmpty(start_date_str)
                || !TextUtils.isEmpty(end_date_str);
    }


    private boolean checkParams() {
        if (null == startBean
                || TextUtils.isEmpty(peopleTextClick.getText())
//                || TextUtils.isEmpty(baggageTextClick.getText())
                || isHalfTravel ? TextUtils.isEmpty(halfDate) : TextUtils.isEmpty(start_date_str)
                || isHalfTravel ? TextUtils.isEmpty(halfDate) : TextUtils.isEmpty(end_date_str)) {
//                || isHalfTravel?false:passBeanList.size() != nums){
            AlertDialogUtils.showAlertDialogOneBtn(this.activity, getString(R.string.dairy_choose_guide), "好的");
            return false;
        } else {
            return true;
        }
    }


    //type 1 司导列表   2, 可以预约的司导列表
    private void goCollectGuid(int type) {
        if (type == 1) {
            Intent intent = new Intent(this, CollectGuideListActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE,"包车下单选择司导");
            startActivity(intent);
        } else {
            if (checkParams()) {
                if (UserEntity.getUser().isLogin(activity)) {
                    Bundle bundle = new Bundle();
                    RequestCollectGuidesFilter.CollectGuidesFilterParams params = new RequestCollectGuidesFilter.CollectGuidesFilterParams();
                    params.startCityId = startBean.cityId;
                    params.startTime = isHalfTravel ? halfDate + " " + serverTime + ":00" : start_date_str + " " + serverTime + ":00";

                    String end_time = (isHalfTravel ? halfDate : end_date_str) + " " + serverTime + ":00";
                    if ("00:00".equalsIgnoreCase(serverTime)) {
                        end_time = (isHalfTravel ? halfDate : end_date_str) + " " + "23:59:59";
                    }
                    params.endTime = end_time;
                    params.adultNum = manNum;
                    params.childrenNum = childNum;
                    params.childSeatNum = childSeatNums;
                    params.luggageNum = baggageNum;
                    params.orderType = 3;
                    params.totalDays = isHalfTravel ? 1 : nums;
                    params.passCityId = startBean.cityId + "";//isHalfTravel ? startBean.cityId + "" : getPassCitiesId();
                    bundle.putSerializable(Constants.PARAMS_DATA, params);
                    Intent intent = new Intent(this, CollectGuideListActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE,"包车下单选择司导");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.putExtra("source", getIntentSource());
                    startActivity(intent);
                }
            }
        }
    }

    CarInfoBean carBean;

    private void getCarInfo() {
        final RequestGetCarInfo requestGetCarInfo = new RequestGetCarInfo(this.activity,
                startBean.cityId + "", isHalfTravel ? (startBean.cityId + "") : passBeanList.get(passBeanList.size() - 1).cityId + "",
                isHalfTravel ? halfDate + " " + serverTime + ":00" : start_date_str + " " + serverTime + ":00",
                isHalfTravel ? halfDate + " " + serverTime + ":00" : end_date_str + " " + serverTime + ":00",
                isHalfTravel ? "1" : "0", manNum + "",
                childNum + "", childSeatNums + "", baggageNum + "", isHalfTravel ? "" : getPassCities(), "18", collectGuideBean.carType + "-" + collectGuideBean.carClass);
        HttpRequestUtils.request(this.activity, requestGetCarInfo, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                RequestGetCarInfo requestGetCarInfo = (RequestGetCarInfo) request;
                carBean = requestGetCarInfo.getData();

                if (null != carBean && null != carBean.cars && carBean.cars.size() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("guideCollectId", null != collectGuideBean ? collectGuideBean.guideId : "");
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
                    if (null != collectGuideBean) {
                        int maxLuuages = (collectGuideBean.numOfLuggage + collectGuideBean.numOfPerson)
                                - Integer.valueOf(manNum) - Math.round(Integer.valueOf(childSeatNums) * 1.5f)
                                - (Integer.valueOf(childNum) - Integer.valueOf(childSeatNums));
                        baggageNum = maxLuuages;
                    }
                    bundle.putString("luggageNum", baggageNum + "");
                    bundle.putString("passCities", isHalfTravel ? "" : getPassCities());
                    bundle.putString("carTypeName", null != getMatchCarBean() ? getMatchCarBean().carDesc : "");
                    bundle.putString("startCityName", startBean.name);
                    bundle.putString("dayNums", nums + "");
                    bundle.putSerializable("startBean", startBean);
                    bundle.putSerializable("endBean", endBean);
                    bundle.putInt("outnum", getOutNum());
                    bundle.putInt("innum", getInNum());
                    bundle.putString("source", source);
                    bundle.putBoolean("isHalfTravel", isHalfTravel);
                    bundle.putSerializable("passCityList", passBeanList);
                    bundle.putString("orderType", "3");
                    bundle.putSerializable("carBean", getMatchCarBean());
                    bundle.putBoolean("isHalfTravel", isHalfTravel);
                    bundle.putInt("type", 3);
                    bundle.putString("orderType", "3");
                    StatisticClickEvent.dailyClick(StatisticConstant.CONFIRM_R, "自定义包车确认行程", getIntentSource(), collectGuideBean, collectGuideBean.numOfPerson + "");

                    Intent intent = new Intent(activity, OrderNewActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE,getIntentSource());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    CommonUtils.showToast(R.string.no_price_error);
                }
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {
                System.out.print("1");
            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                CommonUtils.showToast(errorInfo.exception.getMessage());
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
            String end_time = (isHalfTravel ? halfDate : end_date_str) + " " + serverTime + ":00";
            if ("00:00".equalsIgnoreCase(serverTime)) {
                end_time = (isHalfTravel ? halfDate : end_date_str) + " " + "23:59:59";
            }
            OrderUtils.checkGuideCoflict(activity, 3, startBean.cityId,
                    collectGuideBean.guideId, (isHalfTravel ? halfDate : start_date_str) + " " + serverTime + ":00",
                    end_time, getPassCitiesId(),
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

    String serverTime = "00:00";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_select_city);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    boolean isHalfTravel = false;
    private final int TYPE_SINGLE = 1;
    private final int TYPE_RANGE = 2;

    @OnClick({R.id.header_right_txt, R.id.time_text_click, R.id.go_city_text_layout, R.id.choose_driver, R.id.minus, R.id.add, R.id.header_left_btn, start_city_click, people_text_click, R.id.show_child_seat_layout, R.id.child_no_confirm_click, baggage_text_click, R.id.baggage_no_confirm_click, R.id.end_layout_click, R.id.go_city_text_click, R.id.next_btn_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_right_txt:
                break;
            case R.id.time_text_click:
                showYearMonthDayTimePicker();
                break;
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
                if (childSeatNums < childNum) {
                    childSeatNums++;
                    childText.setText(getString(R.string.select_city_child) + childSeatNums);
                } else {
                    CommonUtils.showToast("儿童座椅数不能大于儿童数");
                }
                break;
            case start_city_click:
                if (null != collectGuideBean && !TextUtils.isEmpty(startCityClick.getText())) {
                    CommonUtils.showToast(R.string.alert_del_after_edit);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_FROM, "startAddress");
                    bundle.putInt(KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_DAILY);
                    Intent intent = new Intent(activity, ChooseCityActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case people_text_click:
                if (null != collectGuideBean && !TextUtils.isEmpty(peopleTextClick.getText())) {
                    CommonUtils.showToast(R.string.alert_del_after_edit);
                } else {
                    showSelectPeoplePop(1);
                }
                break;
            case R.id.show_child_seat_layout:
                break;
            case R.id.child_no_confirm_click:
                AlertDialogUtils.showAlertDialog(this.activity, getString(R.string.man_no_confirm_tips));
                break;
            case baggage_text_click:
                if (null != collectGuideBean && !TextUtils.isEmpty(baggageTextClick.getText())) {
                    CommonUtils.showToast(R.string.alert_del_after_edit);
                } else {
                    showSelectPeoplePop(2);
                }
                break;
            case R.id.baggage_no_confirm_click:
                AlertDialogUtils.showAlertDialog(this.activity, getString(R.string.baggage_no_confirm_tips));
                break;
            case R.id.end_layout_click:
                if (null != collectGuideBean && !TextUtils.isEmpty(endDate.getText())) {
                    CommonUtils.showToast(R.string.alert_del_after_edit);
                } else {
                    Intent intent = new Intent(activity, DatePickerActivity.class);
                    intent.putExtra("type", TYPE_RANGE);
                    intent.putExtra("chooseDateBean", chooseDateBean);
                    startActivity(intent);
                }
                break;
            case R.id.go_city_text_layout:
            case R.id.go_city_text_click:
                if (null != collectGuideBean && !TextUtils.isEmpty(goCityTextClick.getText())) {
                    CommonUtils.showToast(R.string.alert_del_after_edit);
                } else {
                    Intent intent = new Intent(activity, DatePickerActivity.class);
                    intent.putExtra("type", TYPE_SINGLE);
                    intent.putExtra("chooseDateBean", chooseDateBean);
                    startActivity(intent);

                }
                break;
            case R.id.next_btn_click:

                if (checkNextBtnStatus()) {

                    if (null != collectGuideBean) {
                        if ((collectGuideBean.carType == 1 && collectGuideBean.numOfPerson == 4 && (manNum + childNum) == 4)
                                || (collectGuideBean.carType == 1 && collectGuideBean.numOfPerson == 6 && (manNum + childNum) == 6)) {
                            AlertDialogUtils.showAlertDialog(activity, getString(R.string.alert_car_full),
                                    "继续下单", "更换车型", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            checkGuideCoflict();
                                            dialog.dismiss();
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            checkGuideCoflict();
                        }
                    } else {
                        Bundle bundleCar = new Bundle();
                        bundleCar.putString("startCityId", startBean.cityId + "");
                        bundleCar.putString("endCityId", isHalfTravel ? (startBean.cityId + "") : passBeanList.get(passBeanList.size() - 1).cityId + "");//endCityId);
                        bundleCar.putString("startDate", isHalfTravel ? (halfDate) : (start_date_str));
                        bundleCar.putString("endDate", isHalfTravel ? (halfDate) : (end_date_str));
                        bundleCar.putString("serverTime", serverTime);
                        bundleCar.putString("halfDay", isHalfTravel ? "1" : "0");
                        bundleCar.putString("adultNum", manNum + "");
                        bundleCar.putString("childrenNum", childNum + "");
                        bundleCar.putString("childseatNum", childSeatNums + "");
                        bundleCar.putString("luggageNum", baggageNum + "");
                        bundleCar.putString("passCities", isHalfTravel ? "" : getPassCities());

                        bundleCar.putString("startCityName", startBean.name);
                        bundleCar.putString("dayNums", nums + "");
                        bundleCar.putSerializable("startBean", startBean);
                        bundleCar.putSerializable("endBean", endBean);
                        bundleCar.putInt("outnum", getOutNum());
                        bundleCar.putInt("innum", getInNum());
                        bundleCar.putString("source", source);
                        bundleCar.putBoolean("isHalfTravel", isHalfTravel);
                        bundleCar.putSerializable("passCityList", passBeanList);
                        bundleCar.putString("orderType", "3");

                        StatisticClickEvent.dailyClick(StatisticConstant.CONFIRM_R, getIntentSource(), EventUtil.getInstance().sourceDetail, collectGuideBean, (childNum + manNum) + "");
                        Intent intent = new Intent(activity, SelectCarActivity.class);
                        intent.putExtra(Constants.PARAMS_SOURCE,getIntentSource());
                        intent.putExtras(bundleCar);
                        startActivity(intent);
                    }

                }
                break;
        }
    }

    TimePicker picker;

    public void showYearMonthDayTimePicker() {
        Calendar calendar = Calendar.getInstance();
        picker = new TimePicker(activity, TimePicker.HOUR_OF_DAY);
        picker.setTitle("请选择上车时间");
        picker.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                serverTime = hour + ":" + minute;
                time_text_click.setText(serverTime);
                picker.dismiss();
            }
        });

        picker.show();
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

    View dayView;
    TextView day_text, day_go_city_text_click;

    View currentClickView = null;
    int currentIndex = 0;

    //生成经过城市列表
    private void genDayViews(int index) {
        dayView = LayoutInflater.from(this.activity).inflate(R.layout.add_day_item, null);
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
        params.setMargins(0, 0, 0, (int) ScreenUtils.d2p(this.activity, 15));

        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != v.getTag()) {
                    currentClickView = v;
                    TextView text = (TextView) v.findViewById(R.id.day_go_city_text_click);
                    currentIndex = Integer.valueOf(currentClickView.getTag().toString()) - 1;
                    if (Integer.valueOf(v.getTag().toString()) == full_day_show.getChildCount()) {
                        initScopeLayoutValue(true);
                    } else {
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


    ChooseDateBean chooseDateBean;

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_START_CITY_BACK:
                startBean = (CityBean) action.getData();
                preCityBean = startBean;
                passBeanList.clear();
                endBean = startBean;
                if (!startCity.equalsIgnoreCase(startBean.name)) {
                    startCity = startBean.name;
                    endCityId = startBean.cityId + "";
                    startCityClick.setText(startCity);
                    startCityClick.setTextColor(Color.parseColor("#000000"));
                    initScopeLayoutValue(true);
                    addDayView(true);
                }

                List<CityBean> list = CityUtils.requestDataByKeyword(activity,
                        preCityBean.groupId, preCityBean.cityId, "", true);

                showOtherLayout = !(null == list || list.size() == 0);


                hotCitys = CityUtils.requestHotDate(activity, startBean.groupId, startBean.cityId, "lastCity");
                break;
            case CHOOSE_END_CITY_BACK:
                endBean = (CityBean) action.getData();
                setDayText(3, endBean);
                break;
            case CHOOSE_GUIDE:
                collectGuideBean = (CollectGuideBean) action.getData();
                if (null != collectGuideBean) {
                    driver_layout.setVisibility(View.VISIBLE);
                    driver_name.setText(collectGuideBean.name);
                    choose_driver.setVisibility(GONE);
                    guideCollectId = collectGuideBean.guideId;
                }
                break;
            case CHOOSE_DATE:
                chooseDateBean = (ChooseDateBean) action.getData();
                if (chooseDateBean.type == 1) {
                    halfDate = chooseDateBean.halfDateStr;
                    goCityTextClick.setText(chooseDateBean.showHalfDateStr);
                    if (chooseDateBean.isToday) {
                        go_city_text_click_right.setText("今天");
                        time_layout.setVisibility(View.VISIBLE);
                        isIncludeToday = true;
                    } else {
                        go_city_text_click_right.setText("");
                        time_layout.setVisibility(View.GONE);
                        isIncludeToday = false;
                    }

//                    checkNextBtnStatus();
                } else {
                    start_date_str = chooseDateBean.start_date;
                    end_date_str = chooseDateBean.end_date;
                    startDate.setText(chooseDateBean.showStartDateStr);
                    endDate.setText(chooseDateBean.showEndDateStr);

                    if (chooseDateBean.isToday) {
                        start_date_right.setText("今天");
                        time_layout.setVisibility(View.VISIBLE);
                        isIncludeToday = true;
                    } else {
                        start_date_right.setText("");
                        time_layout.setVisibility(View.GONE);
                        isIncludeToday = false;
                    }

                    end_date_right.setText("共包车" + chooseDateBean.dayNums + "天");
                    addDayView(false);
//                    checkNextBtnStatus();
                }
                break;
            default:
                break;
        }
    }

    CollectGuideBean collectGuideBean;

    String start_date_str = "";
    String end_date_str = "";
    String halfDate = "";


}