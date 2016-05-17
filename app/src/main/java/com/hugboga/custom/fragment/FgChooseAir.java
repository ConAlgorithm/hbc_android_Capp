package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created  on 16/5/13.
 */
@ContentView(R.layout.fg_choose_air)
public class FgChooseAir extends BaseFragment {

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.choose_content)
    FrameLayout choose_content;


    TextView fgTitle;
    ImageView header_left_btn;
    @Override
    protected void initHeader() {
        fgTitle = (TextView)rootView.findViewById(R.id.header_title);
        header_left_btn = (ImageView)rootView.findViewById(R.id.header_left_btn);


        header_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fgTitle.setText("选择航班");
    }


    FgChooseAirAddress fgChooseAirAddress;
    FgChooseAirNumber fgChooseAirNumber;
    private FragmentManager fm;

    RadioButton address;
    RadioButton number;

    View rootView;
    FragmentTransaction transaction;
    @Override
    protected void initView() {

        fgChooseAirAddress = new FgChooseAirAddress();
        fgChooseAirNumber = new FgChooseAirNumber();

        final Bundle bundle = new Bundle();
        if (getArguments() != null) {
            bundle.putAll(getArguments());
        }

        fm = getFragmentManager();

        transaction = fm.beginTransaction();
        transaction.add(R.id.choose_content, fgChooseAirAddress);
        transaction.commit();
        address = (RadioButton)rootView.findViewById(R.id.address_radio);
        number = (RadioButton)rootView.findViewById(R.id.number_radio);
        address.setChecked(true);
        address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && buttonView.getId() == R.id.address_radio){
                    if (!fgChooseAirAddress.isAdded()) {
                        transaction = fm.beginTransaction();
                        transaction.add(R.id.choose_content, fgChooseAirAddress);
                        transaction.commit();
                    } else{
                        transaction = fm.beginTransaction();
                        transaction.hide(fgChooseAirNumber);
                        transaction.show(fgChooseAirAddress);
                        transaction.commit();
                    }
                }
            }
        });

        number.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked  && buttonView.getId() == R.id.number_radio){
                    if (!fgChooseAirNumber.isAdded()) {
                        transaction = fm.beginTransaction();
                        transaction.add(R.id.choose_content, fgChooseAirNumber);
                        transaction.commit();
                    } else {
                        transaction = fm.beginTransaction();
                        transaction.hide(fgChooseAirAddress);
                        transaction.show(fgChooseAirNumber);
                        transaction.commit();
                    }
                }
            }
        });

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
        String from = bundle.getString(KEY_FROM);
        if ("FlightList".equals(from)) {
            finishForResult(bundle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fg_choose_air,null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.header_left_btn, R.id.header_title, R.id.header_right_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                break;
            case R.id.header_title:
                break;
            case R.id.header_right_txt:
                break;
        }
    }
}
