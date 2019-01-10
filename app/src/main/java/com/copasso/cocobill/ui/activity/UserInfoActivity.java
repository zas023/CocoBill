package com.copasso.cocobill.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.copasso.cocobill.R;
import com.copasso.cocobill.base.BaseMVPActivity;
import com.copasso.cocobill.model.bean.remote.MyUser;
import com.copasso.cocobill.presenter.UserInfoPresenter;
import com.copasso.cocobill.presenter.contract.UserInfoContract;
import com.copasso.cocobill.utils.ImageUtils;
import com.copasso.cocobill.utils.ProgressUtils;
import com.copasso.cocobill.utils.SnackbarUtils;
import com.copasso.cocobill.utils.StringUtils;
import com.copasso.cocobill.utils.ToastUtils;
import com.copasso.cocobill.widget.CommonItemLayout;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Zhouas666 on 2019-01-10
 * Github: https://github.com/zas023
 * <p>
 * 用户中心activity
 */
public class UserInfoActivity extends BaseMVPActivity<UserInfoContract.Presenter>
        implements UserInfoContract.View, View.OnClickListener {

    private Toolbar toolbar;
    private RelativeLayout iconRL;
    private ImageView iconIv;
    private CommonItemLayout usernameCL;
    private CommonItemLayout sexCL;
    private CommonItemLayout phoneCL;
    private CommonItemLayout emailCL;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int CROP_SMALL_PICTURE = 2;
    //图片路径
    protected static Uri tempUri = null;

    private MyUser currentUser;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        currentUser= BmobUser.getCurrentUser(MyUser.class);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        toolbar = findViewById(R.id.toolbar);
        iconRL = findViewById(R.id.rlt_update_icon);
        iconIv = findViewById(R.id.img_icon);
        usernameCL = findViewById(R.id.cil_username);
        sexCL = findViewById(R.id.cil_sex);
        phoneCL = findViewById(R.id.cil_phone);
        emailCL = findViewById(R.id.cil_email);

        //初始化Toolbar
        toolbar.setTitle("账户");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v->{
            //返回消息更新上个Activity数据
            setResult(RESULT_OK, new Intent());
            finish();
        });

        //加载当前头像
        Glide.with(mContext).load(currentUser.getImage()).into(iconIv);
        //添加用户信息
        usernameCL.setRightText(currentUser.getUsername());
        sexCL.setRightText(currentUser.getGender());
        phoneCL.setRightText(currentUser.getMobilePhoneNumber());
        emailCL.setRightText(currentUser.getEmail());
    }

    @Override
    protected void initClick() {
        super.initClick();
        iconRL.setOnClickListener(this);
        usernameCL.setOnClickListener(this);
        sexCL.setOnClickListener(this);
        phoneCL.setOnClickListener(this);
        emailCL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlt_update_icon:  //头像
//                showIconDialog();
                startActivityForResult(
                        new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                        CHOOSE_PICTURE);
                break;
            case R.id.cil_username:  //用户名
                SnackbarUtils.show(mContext, "江湖人行不更名，坐不改姓！");
                break;
            case R.id.cil_sex:  //性别
                showGenderDialog();
                break;
            case R.id.cil_phone:  //电话修改
                showPhoneDialog();
                break;
            case R.id.cil_email:  //邮箱修改
                showMailDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 显示选择头像来源对话框
     */
    public void showIconDialog() {
        new MaterialDialog.Builder(mContext)
                .title("选择图片来源")
                .titleGravity(GravityEnum.CENTER)
                .items(new String[]{"相册", "相机"})
                .positiveText("确定")
                .itemsCallbackSingleChoice(0, (dialog, itemView, which, text) -> {
                    switch (which) {
                        case CHOOSE_PICTURE: // 选择本地照片
                            Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            openAlbumIntent.setType("image/*");
                            startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                            break;
                        case TAKE_PICTURE: // 拍照
                            takePicture();
                            break;
                    }
                    dialog.dismiss();
                    return false;
                }).show();
    }

    /**
     * 显示选择性别对话框
     */
    public void showGenderDialog() {

        new MaterialDialog.Builder(mContext)
                .title("选择性别")
                .titleGravity(GravityEnum.CENTER)
                .items(new String[]{"男", "女"})
                .positiveText("确定")
                .negativeText("取消")
                .itemsCallbackSingleChoice(0, (dialog, itemView, which, text) -> {
                    currentUser.setGender(text.toString());
                    doUpdate();
                    sexCL.setRightText(currentUser.getGender());
                    dialog.dismiss();
                    return false;
                }).show();
//        switch (which) {
//            case GENDER_MAN: // 男性
//                if(currentUser.getGender().equals("女")){
//                    currentUser.setGender("男");
//                    doUpdate();
//                }
//                break;
//            case GENDER_FEMALE: // 女性
//                if(currentUser.getGender().equals("男")){
//                    currentUser.setGender("女");
//                    doUpdate();
//                }
//                break;
//        }
    }

    /**
     * 显示更换电话对话框
     */
    public void showPhoneDialog() {
        String phone = currentUser.getMobilePhoneNumber();
        new MaterialDialog.Builder(mContext)
                .title("电话")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.textRed)
                .input("请输入电话号码", phone, (dialog, input) -> {
                    String inputStr=input.toString();
                    if (inputStr.equals("")) {
                        ToastUtils.show(mContext,"内容不能为空！" + input);
                    } else {
                        if (StringUtils.checkPhoneNumber(inputStr)) {
                            currentUser.setMobilePhoneNumber(inputStr);
                            phoneCL.setRightText(inputStr);
                            doUpdate();
                        } else {
                            Toast.makeText(UserInfoActivity.this,
                                    "请输入正确的电话号码", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .positiveText("确定")
                .show();
    }

    /**
     * 显示更换邮箱对话框
     */
    public void showMailDialog() {
        String email = currentUser.getMobilePhoneNumber();
        new MaterialDialog.Builder(mContext)
                .title("邮箱")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(0, 200, R.color.textRed)
                .input("请输入邮箱地址", email, (dialog, input) -> {
                    String inputStr=input.toString();
                    if (inputStr.equals("")) {
                        ToastUtils.show(mContext,"内容不能为空！" + input);
                    } else {
                        if (StringUtils.checkEmail(inputStr)) {
                            currentUser.setEmail(inputStr);
                            emailCL.setRightText(inputStr);
                            doUpdate();
                        } else {
                            Toast.makeText(UserInfoActivity.this,
                                    "请输入正确的邮箱格式", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .positiveText("确定")
                .show();
    }

    /**
     * 更新用户数据
     */
    public void doUpdate() {
        if (currentUser == null)
            return;
        ProgressUtils.show(UserInfoActivity.this, "正在修改...");
        mPresenter.updateUser(currentUser);

    }


    /**
     * 拍照
     */
    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(UserInfoActivity.this,
                    "com.copasso.cocobill.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            iconIv.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    /**
     * 保存头像并上传服务器
     *
     * @param bitmap
     */
    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String imagename = currentUser.getObjectId() + "_" + String.valueOf(System.currentTimeMillis());
        String imagePath = ImageUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), imagename + ".png");
        if (imagePath != null) {
            final BmobFile bmobFile = new BmobFile(new File(imagePath));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e==null) {
                        MyUser newUser=new MyUser();
                        newUser.setImage(bmobFile.getFileUrl());
                        newUser.update(currentUser.getObjectId(),new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e!=null)
                                    Log.i(TAG,e.getMessage());
                            }
                        });
                    }else{
                        Log.i(TAG,e.getMessage());
                    }
                }
            });
        }
    }

    /**
     * 权限请求
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }


    /***********************************************************************/
    @Override
    protected UserInfoContract.Presenter bindPresenter() {
        return new UserInfoPresenter();
    }

    @Override
    public void onSuccess() {
        ProgressUtils.dismiss();
        currentUser=BmobUser.getCurrentUser(MyUser.class);
    }

    @Override
    public void onFailure(Throwable e) {
        ProgressUtils.dismiss();
        SnackbarUtils.show(mContext, e.getMessage());
    }
}
