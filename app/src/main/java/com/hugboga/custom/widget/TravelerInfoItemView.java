package com.hugboga.custom.widget;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCountryActivity;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by qingcha on 17/7/11.
 */
public class TravelerInfoItemView extends LinearLayout {

    @Bind(R.id.traveler_info_contacts_star_tv)
    TextView contactsStarTV;
    @Bind(R.id.traveler_info_contacts_hint_tv)
    TextView contactsHintTV;
    @Bind(R.id.traveler_info_address_book_tv)
    TextView addressBookTV;
    @Bind(R.id.traveler_info_address_book_iv)
    ImageView addressBookIV;
    @Bind(R.id.traveler_info_contacts_et)
    EditText contactsET;
    @Bind(R.id.traveler_info_phone_star_tv)
    TextView phoneStarTV;
    @Bind(R.id.traveler_info_phone_hint_tv)
    TextView phoneHintTV;
    @Bind(R.id.traveler_info_code_tv)
    TextView codeTV;
    @Bind(R.id.traveler_info_code_arrow_iv)
    ImageView codeArrowIV;
    @Bind(R.id.traveler_info_phone_et)
    EditText phoneET;
    @Bind(R.id.traveler_info_bottom_line)
    View bottomLineView;

    private int requestCode;

    public TravelerInfoItemView(Context context) {
        this(context, null);
    }

    public TravelerInfoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_traveler_info_item, this);
        ButterKnife.bind(view);
    }

    public void setContactsHintText(String text) {
        contactsHintTV.setText(text);
    }

    public void init(String travelerName, String code, String travelerPhone) {
        setName(travelerName);
        setAreaCode(code);
        setPhone(travelerPhone);
    }

    public void setAreaCode(String code) {
        codeTV.setText(CommonUtils.addPhoneCodeSign(code));
    }

    public void setName(String travelerName) {
        contactsET.setText(travelerName);
    }

    public void setPhone(String travelerPhone) {
        phoneET.setText(travelerPhone);
    }

    public CharSequence getAreaCode() {
        return codeTV.getText();
    }

    public CharSequence getName() {
        return contactsET.getText();
    }

    public CharSequence getPhone() {
        return phoneET.getText();
    }

    public TextView getAreaCodeView() {
        return codeTV;
    }

    public EditText getNameView() {
        return contactsET;
    }

    public EditText getPhoneView() {
        return phoneET;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setBottomLineVisibility(int visibility) {
        bottomLineView.setVisibility(visibility);
    }

    public void setHint(String contacts, String phone) {
        contactsHintTV.setHint(contacts);
        phoneHintTV.setHint(phone);
    }

    public void setReadOnly() {
        setHideTV(contactsET);
        setHideTV(codeTV);
        setHideTV(phoneET);
        codeArrowIV.setEnabled(false);
        codeArrowIV.setVisibility(View.INVISIBLE);
        addressBookTV.setVisibility(View.INVISIBLE);
        addressBookIV.setVisibility(View.INVISIBLE);
//        contactsStarTV.setVisibility(View.INVISIBLE);
//        phoneStarTV.setVisibility(View.INVISIBLE);
    }

    private void setHideTV(TextView textView) {
        if (textView.getText() == null || TextUtils.isEmpty(textView.getText().toString())) {
            textView.setHint("");
        }
        textView.setEnabled(false);
    }

    @OnClick({R.id.traveler_info_address_book_tv
            , R.id.traveler_info_code_arrow_iv
            , R.id.traveler_info_code_tv})
    public void setContact(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.traveler_info_address_book_tv://通讯录
                intentPickContactsCheckPermisson(requestCode);
                break;
            case R.id.traveler_info_code_tv://区号
            case R.id.traveler_info_code_arrow_iv:
                intent = new Intent(getContext(), ChooseCountryActivity.class);
                intent.putExtra("viewId", requestCode);
                getContext().startActivity(intent);
                break;
        }
    }

    public void intentPickContacts(int requestCode) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        ((Activity) getContext()).startActivityForResult(intent, requestCode);
    }

    public void intentPickContactsCheckPermisson(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23 && requestPermisson(requestCode)) {
            intentPickContacts(requestCode);
        } else {
            intentPickContacts(requestCode);
        }
    }

    public void onRequestPermissionsResult(int _requestCode) {
        if (_requestCode == requestCode) {
            intentPickContacts(requestCode);
        }
    }

    @TargetApi(23)
    public boolean requestPermisson(int requestCode) {
        if (checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions((Activity)getContext(), new String[]{Manifest.permission.READ_CONTACTS}, requestCode);
            return false;
        } else {
            return true;
        }
    }
}
