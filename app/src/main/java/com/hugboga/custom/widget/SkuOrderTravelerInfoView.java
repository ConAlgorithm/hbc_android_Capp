package com.hugboga.custom.widget;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCountryActivity;
import com.hugboga.custom.activity.SkuOrderActivity;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by qingcha on 16/12/17.
 */
public class SkuOrderTravelerInfoView extends LinearLayout{

    @Bind(R.id.sku_order_traveler_info_contacts_et)
    EditText contactsET;

    @Bind(R.id.sku_order_traveler_info_code_tv)
    TextView codeTV;
    @Bind(R.id.sku_order_traveler_info_phone_et)
    EditText phoneET;

    @Bind(R.id.sku_order_traveler_info_start_address_tv)
    TextView addressTV;
    @Bind(R.id.sku_order_traveler_info_start_address_description_tv)
    TextView addressDescriptionTV;

    @Bind(R.id.sku_order_traveler_info_mark_et)
    EditText markET;

    private TravelerInfoBean travelerInfoBean;
    private Activity activity;

    public SkuOrderTravelerInfoView(Context context) {
        this(context, null);
    }

    public SkuOrderTravelerInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_sku_order_traveler_info, this);
        ButterKnife.bind(view);

        travelerInfoBean = new TravelerInfoBean();

        contactsET.setText(travelerInfoBean.travelerName);
        codeTV.setText(CommonUtils.addPhoneCodeSign(travelerInfoBean.areaCode));
        phoneET.setText(travelerInfoBean.travelerPhone);
    }

    @OnClick({R.id.sku_order_traveler_info_address_book_tv, R.id.sku_order_traveler_info_code_arrow_iv, R.id.sku_order_traveler_info_code_tv, R.id.sku_order_traveler_info_start_address_layout})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.sku_order_traveler_info_address_book_tv://通讯录
                requestPermisson();
                intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(ContactsContract.Contacts.CONTENT_URI);
                ((Activity)getContext()).startActivityForResult(intent, SkuOrderActivity.REQUEST_CODE_PICK_CONTACTS);
                break;
            case R.id.sku_order_traveler_info_code_tv://区号
            case R.id.sku_order_traveler_info_code_arrow_iv:
                intent = new Intent(getContext(), ChooseCountryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.sku_order_traveler_info_start_address_layout://上车地点
                EventBus.getDefault().post(new EventAction(EventType.CHOOSE_POI));
                break;
        }
    }

    public void setActivity(Activity a){
        this.activity = a;
    }

    @TargetApi(23)
    public void requestPermisson(){
        if (Build.VERSION.SDK_INT >= 23) {
            final List<String> permissionsList = new ArrayList<String>();
            addPermission(getContext(), permissionsList, Manifest.permission.READ_CONTACTS);
            addPermission(getContext(), permissionsList, Manifest.permission.WRITE_CONTACTS);
            if (permissionsList.size() > 0 && null != activity) {
                requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]), SkuOrderActivity.REQUEST_CODE_PICK_CONTACTS);
                return;
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(Context Ctx, List<String> permissionsList,
                                  String permission) {
        if (checkSelfPermission(Ctx,permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
        return true;
    }

    public void setAreaCode(String code) {
        travelerInfoBean.areaCode = code;
        codeTV.setText(CommonUtils.addPhoneCodeSign(code));
    }

    public void setTravelerName(String travelerName) {
        travelerInfoBean.travelerName = travelerName;
        contactsET.setText(travelerName);
    }

    public void setTravelerPhone(String travelerPhone) {
        travelerInfoBean.travelerPhone = travelerPhone;
        phoneET.setText(travelerPhone);
    }

    public void setPlace(PoiBean poiBean) {
        if (poiBean == null) {
            return;
        }
        travelerInfoBean.poiBean = poiBean;
        addressTV.setText(poiBean.placeName);
        if (TextUtils.isEmpty(poiBean.placeDetail)) {
            addressDescriptionTV.setVisibility(View.GONE);
        } else {
            addressDescriptionTV.setVisibility(View.VISIBLE);
            addressDescriptionTV.setText(poiBean.placeDetail);
        }
    }

    public TravelerInfoBean getTravelerInfoBean() {
        travelerInfoBean.travelerName = getText(contactsET, false);
        travelerInfoBean.travelerPhone = getText(phoneET, true);
        travelerInfoBean.mark = getText(markET, false);
        return travelerInfoBean;
    }

    private String getText(EditText editText, boolean isRemoveAllBlank) {
        String result = editText.getText() != null ? editText.getText().toString() : "";
        if (!TextUtils.isEmpty(result)) {
            if (isRemoveAllBlank) {
                result.replaceAll(" ", "");
            } else {
                result.trim();
            }
        }
        return result;
    }

    public boolean checkTravelerInfo() {
        TravelerInfoBean infoBean = getTravelerInfoBean();
        if (TextUtils.isEmpty(infoBean.travelerName)) {
            CommonUtils.showToast("请填写联系人姓名!");
            return false;
        } else if (TextUtils.isEmpty(infoBean.travelerPhone)) {
            CommonUtils.showToast("请填写联系人手机号!");
            return false;
        }
        return true;
    }

    public static class TravelerInfoBean implements Serializable {

        public TravelerInfoBean() {
            final Context context = MyApplication.getAppContext();
            travelerName = UserEntity.getUser().getUserName(context);
            travelerPhone = UserEntity.getUser().getPhone(context);
            areaCode = UserEntity.getUser().getAreaCode(context);
        }

        public String travelerName;
        public String travelerPhone;
        public String areaCode;
        public PoiBean poiBean;
        public String mark;

        public String getAreaCode() {
            return CommonUtils.removePhoneCodeSign(areaCode);
        }
    }
}
