package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
/**
 * Created on 16/5/26.
 */

@ContentView(R.layout.add_user_layout)
public class FgChooseOther extends BaseFragment {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.name_left)
    TextView nameLeft;
    @Bind(R.id.name_right)
    TextView nameRight;
    @Bind(R.id.name_line)
    TextView nameLine;
    @Bind(R.id.name_text)
    EditText nameText;
    @Bind(R.id.hotel_phone_text_code_click)
    TextView hotelPhoneTextCodeClick;
    @Bind(R.id.hotel_phone_text)
    EditText hotelPhoneText;
    @Bind(R.id.name1_left)
    TextView name1Left;
    @Bind(R.id.name1_del)
    TextView name1Del;
    @Bind(R.id.name1_right)
    TextView name1Right;
    @Bind(R.id.name1_line)
    TextView name1Line;
    @Bind(R.id.name1_text)
    EditText name1Text;
    @Bind(R.id.user1_phone_text_code_click)
    TextView user1PhoneTextCodeClick;
    @Bind(R.id.user1_phone_text)
    EditText user1PhoneText;
    @Bind(R.id.user1_layout)
    LinearLayout user1Layout;
    @Bind(R.id.name2_left)
    TextView name2Left;
    @Bind(R.id.name2_del)
    TextView name2Del;
    @Bind(R.id.name2_right)
    TextView name2Right;
    @Bind(R.id.name2_line)
    TextView name2Line;
    @Bind(R.id.name2_text)
    EditText name2Text;
    @Bind(R.id.user2_phone_text_code_click)
    TextView user2PhoneTextCodeClick;
    @Bind(R.id.user2_phone_text)
    EditText user2PhoneText;
    @Bind(R.id.user2_layout)
    LinearLayout user2Layout;
    @Bind(R.id.add_other_phone_click)
    TextView addOtherPhoneClick;
    @Bind(R.id.other_left)
    TextView otherLeft;
    @Bind(R.id.other_check)
    CheckBox otherCheck;
    @Bind(R.id.passenger_left)
    TextView passengerLeft;
    @Bind(R.id.passenger_right)
    TextView passengerRight;
    @Bind(R.id.passenger_line)
    TextView passengerLine;
    @Bind(R.id.passenger_text)
    EditText passengerText;
    @Bind(R.id.passenger_phone_text_code_click)
    TextView passengerPhoneTextCodeClick;
    @Bind(R.id.passenger_phone_text)
    EditText passengerPhoneText;
    @Bind(R.id.message_left)
    TextView messageLeft;
    @Bind(R.id.message_check)
    CheckBox messageCheck;
    @Bind(R.id.message_right)
    TextView messageRight;
    @Bind(R.id.other_layout)
    LinearLayout otherLayout;

    @Override
    protected void initHeader() {
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fgTitle.setText("乘车联系人");
        fgRightBtn.setText("确定");
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void initHeader(Bundle savedInstanceState) {
        super.initHeader(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.name_right, R.id.name1_del, R.id.name1_right, R.id.name2_del, R.id.name2_right, R.id.passenger_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.name_right:
                break;
            case R.id.name1_del:
                break;
            case R.id.name1_right:
                break;
            case R.id.name2_del:
                break;
            case R.id.name2_right:
                break;
            case R.id.passenger_right:
                break;
        }
    }
}
