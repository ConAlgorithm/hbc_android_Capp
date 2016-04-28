package com.hugboga.custom.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestChangeUserInfo;
import com.hugboga.custom.data.request.RequestUpLoadFile;
import com.hugboga.custom.data.request.RequestUserInfo;
import com.hugboga.custom.utils.FileUtil;
import com.hugboga.custom.utils.ImageOptionUtils;
import com.hugboga.custom.utils.ImageUtils;
import com.hugboga.custom.utils.PermissionRes;
import com.hugboga.custom.widget.CircleImageView;
import com.hugboga.custom.widget.CircularImage;
import com.umeng.analytics.MobclickAgent;
import com.yalantis.ucrop.UCrop;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import net.grobas.view.PolygonImageView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * personInfo 个人信息
 */
@ContentView(R.layout.fg_my_info)
public class FgPersonInfo extends BaseFragment {

    @ViewInject(R.id.my_info_menu_head1)
    PolygonImageView headImageView;
    @ViewInject(R.id.my_info_nickname)
    TextView nickNameTextView;
    @ViewInject(R.id.my_info_sex)
    TextView sexTextView;
    @ViewInject(R.id.my_info_age)
    TextView ageTextView;
    @ViewInject(R.id.my_info_mobile)
    TextView mobileTextView;

    UserBean userBean;
    Bitmap head;//头像Bitmap

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestUserInfo) {
            RequestUserInfo requestUserInfo = (RequestUserInfo) request;
            userBean = requestUserInfo.getData();
            UserEntity.getUser().setNickname(getActivity(), userBean.nickname);
            UserEntity.getUser().setAvatar(getActivity(), userBean.avatar);
            inflateContent();
        } else if (request instanceof RequestChangeUserInfo) {
            RequestChangeUserInfo requestChangeUserInfo = (RequestChangeUserInfo) request;
            userBean = requestChangeUserInfo.getData();
            UserEntity.getUser().setNickname(getActivity(), userBean.nickname);
            UserEntity.getUser().setAvatar(getActivity(), userBean.avatar);
            inflateContent();
            EventBus.getDefault().post(
                    new EventAction(EventType.CLICK_USER_LOGIN));
        } else if (request instanceof RequestUpLoadFile) {
            RequestUpLoadFile requestUpLoadFile = (RequestUpLoadFile) request;
            Object obj = requestUpLoadFile.getData();
            if (obj instanceof String) {
                submitChangeUserInfo(1, obj.toString());
            } else if (obj instanceof List) {
                if (!((List) obj).isEmpty())
                    submitChangeUserInfo(1, ((List) obj).get(0).toString());
            }
        }
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if(FgBindMobile.class.getSimpleName().equals(from)){
            Object object = bundle.getSerializable("userBean");
            if(object != null && object instanceof UserBean){
                userBean = (UserBean) object;
                inflateContent();
            }
        }
    }

    @Override
    protected void inflateContent() {
//        BitmapUtils imageUtil = x.image().HttpImageUtils.getInstance(getActivity());
//        imageUtil.configDefaultLoadingImage(R.mipmap.chat_head);
//        imageUtil.configDefaultLoadFailedImage(R.mipmap.chat_head);
        if (!TextUtils.isEmpty(userBean.avatar)) {
            String avatar = userBean.avatar;
//            imageUtil.display(headImageView, avatar);
            x.image().bind(headImageView, avatar);
        }
        if (!TextUtils.isEmpty(userBean.nickname)) {
            nickNameTextView.setText(userBean.nickname);
        }
        if (!TextUtils.isEmpty(userBean.gender)) {
            sexTextView.setText(userBean.getGenderStr());
        }
        if (userBean.ageType != -1) {
            ageTextView.setText(userBean.getAgeStr());
        }
        if (!TextUtils.isEmpty(userBean.mobile)) {
            mobileTextView.setText(userBean.mobile);
        }
    }

    @Event({R.id.my_info_menu_layout1, R.id.my_info_menu_layout2, R.id.my_info_menu_layout3, R.id.my_info_menu_layout4, R.id.my_info_menu_layout5})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.my_info_menu_layout1:
                //头像
                grantSdcard();
                break;
            case R.id.my_info_menu_layout2:
                //昵称
                RelativeLayout rl = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fg_person_info_nick, null);
                final EditText inputServer = (EditText) rl.findViewById(R.id.person_info_nick_text);
                inputServer.setText(nickNameTextView.getText().toString());
                inputServer.setSelection(inputServer.getText().length());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(rl).setTitle("填写昵称").setNegativeButton("取消", null).setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String nickStr = inputServer.getText().toString();
                        if (TextUtils.isEmpty(nickStr)) {
                            showTip("没输入昵称，请重新填写");
                            return;
                        }
                        nickNameTextView.setText(inputServer.getText().toString());
                        submitChangeUserInfo(2, inputServer.getText().toString());
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.my_info_menu_layout3:
                //性别
                final CharSequence[] items3 = getResources().getStringArray(R.array.my_info_sex);
                final AlertDialog.Builder sexDialog = new AlertDialog.Builder(getActivity());
                sexDialog.setTitle("选择性别");
                sexDialog.setSingleChoiceItems(items3, getSexInt(items3), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sexStr = items3[which].toString();
                        sexTextView.setText(sexStr);
                        dialog.dismiss();
                        submitChangeUserInfo(3, String.valueOf(which + 1));
                    }
                });
                dialog = sexDialog.create();
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.my_info_menu_layout4:
                //年龄
                final CharSequence[] ages = getResources().getStringArray(R.array.my_info_age);
                AlertDialog.Builder ageDialogBuild = new AlertDialog.Builder(getActivity());
                ageDialogBuild.setTitle("选择年龄");
                ageDialogBuild.setSingleChoiceItems(ages, getAgeInt(ages), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String agesStr = ages[which].toString();
                        ageTextView.setText(agesStr);
                        dialog.dismiss();
                        submitChangeUserInfo(4, String.valueOf(getAgeLevel(which)));
                    }
                });
                dialog = ageDialogBuild.create();
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.my_info_menu_layout5:
                if(userBean != null && !TextUtils.isEmpty(userBean.mobile)){
                    //修改手机号
                    startFragment(new FgChangeMobile());
                }else {
                    //绑定手机号
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isAfterProcess",true);
                    bundle.putString("source","个人信息");
                    startFragment(new FgBindMobile(), bundle);
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("source", "个人信息");
                    MobclickAgent.onEvent(getActivity(), "bind_trigger", map);
                }
                break;

//            case R.id.my_info_menu_layout5:
//                //签名
//                final EditText inputServer1 = new EditText(getActivity());
//                inputServer1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
//                inputServer1.setMinLines(3);
//                inputServer1.setGravity(Gravity.TOP);
//                inputServer1.setText(mobileTextView.getText().toString());
//                inputServer1.setSelection(inputServer1.getText().length());
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity()).setTitle("填写个性签名").setView(inputServer1)
//                        .setNegativeButton("取消", null).setPositiveButton("提交", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                String nickStr = inputServer1.getText().toString();
//                                if (TextUtils.isEmpty(nickStr)) {
//                                    showTip("没输入个性签名，请重新填写");
//                                    return;
//                                }
//                                mobileTextView.setText(inputServer1.getText().toString());
//                                submitChangeUserInfo(5, inputServer1.getText().toString());
//                            }
//                        });
//                dialog = builder1.create();
//                dialog.setCancelable(true);
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.show();
//                break;
            default:
                break;
        }
    }

    /**
     * 授权获取照相机权限
     */
    private void grantSdcard() {
        MPermissions.requestPermissions(this, PermissionRes.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @PermissionGrant(PermissionRes.WRITE_EXTERNAL_STORAGE)
    public void requestSdcardSuccess() {
        cropPic = ImageUtils.getPhotoFileName();
        newPic = "new"+cropPic;cropPic = ImageUtils.getPhotoFileName();
        //修改头像
        final CharSequence[] items = getResources().getStringArray(R.array.my_info_phone_type);
        AlertDialog.Builder builder1= new AlertDialog.Builder(getActivity()).setTitle("上传头像").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    grantCamera();
                } else if (which == 1) {
                    Intent phoneIntent = new Intent(Intent.ACTION_PICK, null);
                    phoneIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(phoneIntent, 2);
                }
            }
        });
        AlertDialog dialog = builder1.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @PermissionDenied(PermissionRes.WRITE_EXTERNAL_STORAGE)
    public void requestSdcardFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(R.string.grant_fail_title);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            builder.setMessage(R.string.grant_fail_phone1);
        } else {
            builder.setMessage(R.string.grant_fail_sdcard);
            builder.setPositiveButton(R.string.grant_fail_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    grantSdcard();
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 授权获取照相机权限
     */
    private void grantCamera() {
        MPermissions.requestPermissions(this, PermissionRes.CAMERA, android.Manifest.permission.CAMERA);
    }

    @PermissionGrant(PermissionRes.CAMERA)
    public void requestPhoneSuccess() {
        //拍照
        ImageUtils.checkDir(); //检查并创建图片目录
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Constants.IMAGE_DIR, cropPic);
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(takeIntent, 1);
    }

    @PermissionDenied(PermissionRes.CAMERA)
    public void requestPhoneFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(R.string.grant_fail_title);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA)) {
            builder.setMessage(R.string.grant_fail_phone1);
        } else {
            builder.setMessage(R.string.grant_fail_camera);
            builder.setPositiveButton(R.string.grant_fail_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    grantCamera();
                }
            });
        }
        builder.show();
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 获取年龄选择项
     *
     * @param ages
     * @return
     */
    private int getAgeInt(CharSequence[] ages) {
        String str = ageTextView.getText().toString();
        if (str == null || str.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < ages.length; i++) {
            if (str.equals(ages[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 选择性别选择项
     *
     * @param items3
     * @return
     */
    private int getSexInt(CharSequence[] items3) {
        String str = sexTextView.getText().toString();
        if (str == null || str.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < items3.length; i++) {
            if (str.equals(items3[i])) {
                return i;
            }
        }
        return -1;
    }

    private Integer getAgeLevel(Integer item) {
        switch (item) {
            case 0:
                return 6;
            case 1:
                return 5;
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 2;
            default:
                return -1;
        }
    }

    private String cropPic = null;
    private String newPic = null;
    /**
     * 调用系统剪切图片
     */
    public void cropPhoto() {
        try {
           File oldFile = new File(Constants.IMAGE_DIR, cropPic);
            File newFile = new File(Constants.IMAGE_DIR, newPic);
            UCrop.Options options = new UCrop.Options();
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
            UCrop.of(Uri.fromFile(oldFile), Uri.fromFile(newFile)).withAspectRatio(1f, 1f).withOptions(options).withMaxResultSize(200, 200).start(getActivity(), this);


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String setPicToView(Bitmap mBitmap) {
        FileOutputStream b = null;
        ImageUtils.checkDir(); //检查并创建图片目录
        String fileName = Constants.IMAGE_DIR + File.separator + cropPic;//Constants.HEAD_IMAGE;//图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            b.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                if (b != null) {
                    b.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return fileName;
    }

    /**
     * 提交修改后的个人资料
     *
     * @param type
     * @param str
     */
    private void submitChangeUserInfo(Integer type, String str) {
        RequestChangeUserInfo request = null;
        switch (type) {
            case 1:
                //提交头像
                request = new RequestChangeUserInfo(getActivity(), str, null, null, null, null);
                break;
            case 2:
                //提交昵称
                request = new RequestChangeUserInfo(getActivity(), null, str, null, null, null);
                break;
            case 3:
                //提交性别
                request = new RequestChangeUserInfo(getActivity(), null, null, str, null, null);
                break;
            case 4:
                //提交年龄
                request = new RequestChangeUserInfo(getActivity(), null, null, null, str, null);
                break;
            case 5:
                //提交签名
                request = new RequestChangeUserInfo(getActivity(), null, null, null, null, str);
                break;
            default:
                break;
        }
        if (request != null) {
            requestData(request);
        }
    }


    @Override
    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    @Override
    protected void initHeader() {
        //设置标题颜色，返回按钮图片
        fgTitle.setText("我的资料");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        RequestUserInfo requestUserInfo = new RequestUserInfo(getActivity());
        requestData(requestUserInfo);
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    cropPhoto();//裁剪图片
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(data.getData()));
                        FileUtil.saveBitmapToFile(bitmap, Constants.IMAGE_DIR, cropPic);
                        cropPhoto();//裁剪图片
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    final Uri resultUri = UCrop.getOutput(data);
                    if (resultUri != null) {
                        Bitmap bitmap = getBitmapFromUri(resultUri);
                        if (bitmap != null) {
                            headImageView.setImageURI(resultUri);
                            String fileName = setPicToView(bitmap);//保存在SD卡中
                            MLog.e("fileName=" + fileName);
                            uploadPic(fileName);
//                            headImageView.setImageBitmap(bitmap);//用ImageView显示出来
                        }
                    }
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                    cropError.printStackTrace();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private void uploadPic(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            uploadPic(file);
        }
    }

    /**
     * 上传图片
     * type资源类型：图片 1  视频 2
     * refId 用户id
     * refType
     * // 实体对象类型： 导游车辆 1;
     * // 实体对象类型： 推送消息  2;
     * // 实体对象类型： 导游头像 3;
     * // 用户头像 4;
     *
     * @param file
     */
    private void uploadPic(File file) {
        MLog.e("uploadPic url=" + UrlLibs.SERVER_IP_PIC_UPLOAD);
        String url = UrlLibs.SERVER_IP_PIC_UPLOAD;
        url = url.replace(UrlLibs.SERVER_IP_HOST_PUBLIC_DEFAULT, UrlLibs.SERVER_IP_HOST_PUBLIC);
        MLog.e("uploadPic url=" + url);
        HashMap<String, Object> fileMap = new HashMap<>();
        fileMap.put("pic", file);
        fileMap.put("type", "1");
        fileMap.put("refId", UserEntity.getUser().getUserId(getActivity()));
        fileMap.put("refType", "4");
        RequestUpLoadFile parser = new RequestUpLoadFile(getActivity(), fileMap);
        requestData(parser);
    }

}
