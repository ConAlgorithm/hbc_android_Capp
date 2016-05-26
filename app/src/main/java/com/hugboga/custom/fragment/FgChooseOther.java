package com.hugboga.custom.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.utils.PhoneInfo;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.action;
import static android.R.attr.data;
import static android.R.attr.x;
import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;
import static com.hugboga.custom.R.id.add_other_phone_click;
import static com.hugboga.custom.R.id.user_phone_text;
import static com.hugboga.custom.data.event.EventType.CONTACT;
import de.greenrobot.event.EventBus;

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
    @Bind(R.id.user_phone_text_code_click)
    TextView userPhoneTextCodeClick;
    @Bind(user_phone_text)
    EditText userPhoneText;
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
    @Bind(add_other_phone_click)
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
        otherCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    otherLayout.setVisibility(View.VISIBLE);
                }else{
                    otherLayout.setVisibility(View.GONE);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
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
    public void onFragmentResult(Bundle bundle) {
        String fragmentName = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCountry.class.getSimpleName().equals(fragmentName)) {
            int viewId = bundle.getInt("airportCode");
            TextView codeTv = (TextView) getView().findViewById(viewId);
            if (codeTv != null) {
                String areaCode = bundle.getString(FgChooseCountry.KEY_COUNTRY_CODE);
                codeTv.setText("+" + areaCode);
            }
        }
    }

    private  int clickViewId = -1;//点击的通讯录view id
    public void onEventMainThread(EventAction action) {
        String[] contact = (String[])action.getData();
        switch (action.getType()) {
            case CONTACT:
                switch (clickViewId) {
                    case R.id.name_right:
                        nameText.setText(contact[0]);
                        userPhoneText.setText("" + contact[1]);
                        break;
                    case R.id.name1_right:
                        name1Text.setText(contact[0]);
                        user1PhoneText.setText("" + contact[1]);
                        break;
                    case R.id.name2_right:
                        name2Text.setText(contact[0]);
                        user2PhoneText.setText("" + contact[1]);
                        break;
                    case R.id.passenger_right:
                        passengerText.setText(contact[0]);
                        passengerPhoneText.setText("" + contact[1]);
                        break;
                }
                break;
            default:
                break;
        }
    }


    @OnClick({R.id.name_right,R.id.name1_right,R.id.name2_right,R.id.passenger_right,R.id.add_other_phone_click,R.id.user_phone_text_code_click, R.id.user1_phone_text_code_click, R.id.user2_phone_text_code_click, R.id.passenger_phone_text_code_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.name_right:
            case R.id.name1_right:
            case R.id.name2_right:
            case R.id.passenger_right:
                clickViewId = view.getId();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 0);
                    break;
            case R.id.add_other_phone_click:
                if(user1Layout.isShown()) {
                    user2Layout.setVisibility(View.VISIBLE);
                    addOtherPhoneClick.setTextColor(Color.parseColor("#929394"));
                }else{
                    user1Layout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.user_phone_text_code_click:
            case R.id.user1_phone_text_code_click:
            case R.id.user2_phone_text_code_click:
            case R.id.passenger_phone_text_code_click:
                FgChooseCountry chooseCountry = new FgChooseCountry();
                Bundle bundleCode = new Bundle();
                bundleCode.putInt("airportCode", view.getId());
                startFragment(chooseCountry, bundleCode);
                break;
        }
    }
}
